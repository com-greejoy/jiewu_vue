# 设计文档：JRXML 证书 PDF 下载

## Context

捷众赛事项目 jiewu_vue 当前的「证书打印」功能依赖 Vue 端 CSS 渲染 + 浏览器 `window.print()`。同公司另一个项目 my-gyms（艺术体操）已落地基于 JRXML + JasperReports 的服务端 PDF 渲染方案，包含完整的中文字体扩展、模板缓存、PDF 合并能力。本设计目标是将该方案复制并适配到 jiewu_vue，同时确保核心方法可被复用，便于未来扩展到「下载整个队伍的证书」「下载某个组别的证书」等场景。

技术栈约束：

- 后端 Spring Boot 2.5.15 + Java 1.8，jiewu_vue 使用 RuoYi-Vue 标准目录结构（`com.ruoyi.project.<module>.<layer>`），与 my-gyms 的 `com.ruoyi.gyms.*` 包结构不同但理念一致
- 前端 Vue 2 + Element UI，已有 `<FileUpload>` 组件 + `/common/upload` 通用上传接口 + `this.download()` blob 流式下载工具
- 数据库 MySQL，`jw_match` 表

## Goals / Non-Goals

**Goals：**

- 实现核心方法的两层封装：底层 `renderCertificatePdf(jrxmlPath, paramsList)` 完全通用，上层提供「JwSignRecord → params」默认转换器
- 复用 my-gyms 已验证的字体扩展、模板缓存、PDF 合并实现
- 新旧渲染逻辑共存，不影响现有「证书打印」浏览器渲染
- 模板上传走 RuoYi 标准 `/common/upload`，路径以 `/profile/...` 形式入库，与项目其他上传字段一致
- 接口设计为通用入口：单组别 / 全部组别共用同一个接口（gameItemId 可选）

**Non-Goals：**

- 不替换或废除现有浏览器打印逻辑
- 不实现「按队伍下载」「按运动员下载」等扩展粒度（但核心方法必须能支撑）
- 不修改 my-gyms 的代码或共享公共库
- 不引入模板热更新机制（手动 invalidate cache 即可，模板修改频率低）
- 不做证书版本管理（同一个 matchId 只能配一个模板）
- 不做异步生成 / 任务队列（同步生成，单次接口耗时控制在 30s 内即可）

## Decisions

### 决策 1：模板绑定层级 — JwMatch 级

**选择**：`JwMatch.certTemplatePath`

**理由**：

- 与 my-gyms 实现完全对齐，最低成本复用
- 一场比赛通常用一套统一的证书设计，赛事方上传一次即可
- 路径存在 JwMatch 上时，未来想扩展到 JwGameItem 级，新增 fallback 逻辑即可，向后兼容

**备选**：JwGameItem 级模板。理由：可支持「A 组用奖牌版、B 组用证书版」。但当前业务无此诉求，先按 my-gyms 一致实现。

### 决策 2：核心方法分两层封装

**选择**：

```java
// 底层：完全通用，不依赖任何业务实体
public static byte[] renderCertificatePdf(String jrxmlPath, List<Map<String, Object>> paramsList);

// 上层默认转换器：JwSignRecord → 4 个标准参数（teamName/playerName/itemName/grade）
public static List<Map<String, Object>> buildParamsFromSignRecords(List<JwSignRecord> records);
```

**理由**：

- 用户明确强调「核心方法封装一下，然后再通过 controller 传递不同的实体类的 list」
- 两层封装兼顾通用性与便利性：底层不入侵 entity，上层贴当前场景
- 未来扩展示例：要支持按队伍下载 → 新增 `buildParamsFromTeams(List<JwTeam>)` 转换器；要支持按运动员下载 → 新增 `buildParamsFromSports(List<JwSport>)`；核心 `renderCertificatePdf` 不变

**备选**：

- 单层 `renderForSignRecords(List<JwSignRecord>)`：扩展时必须改核心方法签名，违背开闭原则
- 泛型 `<T> renderCertificatePdf(List<T>, Function<T, Map>)`：理论更优雅，但 controller 调用现场每次都要传 lambda，可读性下降，且 Java 8 lambda 调试体验一般

### 决策 3：多运动员场景的拆分粒度 — 一人一张

**选择**：JwSignRecord 中如果有 N 个 jwSignRecordSport，就生成 N 张证书；每张证书的 `playerName` 是当前选手名

**理由**：

- 与现有 dialog 渲染粒度完全一致（dialog 模板里就是 `v-for="itemm in item.jwSignRecordSportList"`）
- 模板参数最少（不需要「主名+陪名」拼接逻辑）
- 集体项目要打集体名时，自行修改 `buildParamsFromSignRecords` 即可

**备选**：与 my-gyms 一致的「主名 + 陪名」拼接（`buildParamsFromSignRecords` 内对每条 record 生成 N 张，每张主名靠前其他人靠后）。但 jiewu_vue 当前 dialog 没有这种用法，无业务依据，先按最简化处理

### 决策 4：核心方法包路径 — `com.ruoyi.project.jiewu.utils.CertificateUtils`

**选择**：放业务模块下，与 `JwHaiScoreController` 同包根

**理由**：

- 用户明确选择此包路径（澄清 Q12）
- 工具类直接服务捷众赛事业务，未来如果 jiewu_vue 抽出公共 RuoYi 框架库再升级到 common 层
- 与 my-gyms 的 `com.ruoyi.gyms.utils.*` 风格对齐（业务模块内部工具）

### 决策 5：JasperReports 配置类放 framework 层

**选择**：`com.ruoyi.framework.config.JasperReportsConfig`

**理由**：

- 配置类带 `@Configuration` + `@PostConstruct`，是 Spring 启动期生效，按 RuoYi 惯例放 framework.config 包
- 字体扩展是全局基础设施，跨业务模块共享

### 决策 6：certTemplatePath 存储格式 — RuoYi 标准相对路径

**选择**：存 `/profile/upload/yyyy/MM/dd/xxx_<timestamp>.jrxml`

**理由**：

- 与 jiewu_vue 中现有 `posterImg` / `mainImg` / `qunCode` 等字段完全一致
- 上传逻辑可直接复用现有 `/common/upload` 接口，零改动
- 后端读取时用 `RuoYiConfig.getProfile() + path.substringAfter("/profile")` 转绝对路径

### 决策 7：下载接口 — 单接口、gameItemId 可选

**选择**：`POST /jiewu/JwHaiScore/downloadCertificatePdf`，参数 `matchId`（必填）+ `gameItemId`（可选）+ `filename`（可选）

**理由**：

- 单接口减少 controller 代码重复
- gameItemId 为空 → 走「全部组别」分支（复用 `listAllGameItemGradeDes`）；非空 → 走「单组别」分支（复用 `listGameItemGradeDes`）
- filename 可选实现澄清 Q17 的「支持自定义、有默认值」

### 决策 8：返回形态 — `ResponseEntity<byte[]>` 流式

**选择**：返回 PDF 字节流（`application/pdf`），前端 `this.download()` 触发浏览器下载

**理由**：

- 与 jiewu_vue 现有 `downloadAllScore` / `downloadGameItemScore` 完全一致
- 不在服务器留下临时文件，避免清理负担

## Risks / Trade-offs

- **风险**：JasperReports 库引入 ~5MB 增量，可能拖慢启动 → 与 my-gyms 项目实际运行情况对比，启动慢约 1-2s，可接受
- **风险**：模板缓存使用 `ConcurrentHashMap`，业务方更新模板后老模板会被缓存命中 → 暴露 `JasperTemplateCache.invalidate(path)` 静态方法，未来在 JwMatch 编辑保存时调用即可清理；本次先不实现自动清理，文档中说明「修改模板需重启或调 invalidate」
- **风险**：JRXML 模板由业务方制作，可能存在变量名拼错（如写成 `team_name`）导致渲染失败 → 接口返回明确错误「PDF 生成失败，请检查证书模板变量是否为 teamName/playerName/itemName/grade」
- **风险**：大型比赛（500+ 选手）一次性合并 PDF 可能 OOM → 当前同步实现，监控内存；未来如有需要再加分批生成与异步任务
- **权衡**：保留新旧两套渲染逻辑增加了维护面积 → 但用户明确要求并存，且新旧入口独立、互不影响，维护成本可控
- **权衡**：核心方法只做 4 个标准参数，对模板设计者形成约束 → 但极大简化了 controller 层；如果业务方需要更多参数（比如 sealUnit/awardDate），后续可在 `buildParamsFromSignRecords` 内自由补充，模板侧只需添加变量声明

## Migration Plan

无破坏性变更，新增能力，无需迁移：

1. 后端先发布（pom 引入、配置类、工具类、controller 接口、JwMatch 字段、Mapper 改动、数据库 DDL）
2. 数据库执行：`ALTER TABLE jw_match ADD COLUMN cert_template_path VARCHAR(255) DEFAULT NULL COMMENT '证书 JRXML 模板路径';`
3. 前端发布：JwMatch 编辑页 + JwHaiScore dialog 修改
4. 业务方在每个需要 PDF 证书的比赛上传 .jrxml 模板
5. 老的「证书打印」功能在整个过程中均可正常使用

回滚策略：

- 前端回滚：移除「下载 PDF」按钮即可，老逻辑独立无影响
- 后端回滚：移除 controller 接口，pom 依赖可保留（无副作用）
- 数据库列保留（向后兼容）

## Open Questions

- 暂无（澄清环节四轮问答已收敛全部关键决策）
