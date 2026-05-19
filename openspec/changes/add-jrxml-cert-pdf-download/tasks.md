# 实施任务清单：JRXML 证书 PDF 下载

## 1. 后端基础设施（依赖与配置）

- [x] 1.1 在 `pom.xml` 新增 `net.sf.jasperreports:jasperreports:6.21.0` 依赖
- [x] 1.2 从 my-gyms 复制 5 个中文 ttf 字体文件到 `src/main/resources/fonts/`（simsun.ttf、simhei.ttf、simkai.ttf、simfang.ttf、msyh.ttf、msyhbd.ttf）
- [x] 1.3 从 my-gyms 复制 `fonts.xml` 到 `src/main/resources/fonts/fonts.xml`
- [x] 1.4 从 my-gyms 复制 `jasperreports_extension.properties` 到 `src/main/resources/`
- [x] 1.5 从 my-gyms 复制并调整包名得到 `com.ruoyi.framework.config.JasperReportsConfig`（`@Configuration` + `@PostConstruct` 注册默认字体）

## 2. 数据库与实体

- [x] 2.1 编写 DDL：已落盘到 `sql/update.sql`，与 my-gyms 一致采用 `varchar(500) COLLATE utf8mb4_bin`，`AFTER qun_code`
- [x] 2.2 在 `JwMatch.java` 新增 `private String certTemplatePath` 字段及 getter/setter
- [x] 2.3 在 `mybatis/jiewu/JwMatchMapper.xml` 的 `resultMap`、`selectXxx` SQL、`insert`、`update` 语句中新增 `cert_template_path` 字段映射

## 3. PDF 渲染核心工具（两层封装）

- [x] 3.1 从 my-gyms 复制并调整包名得到 `com.ruoyi.project.jiewu.utils.JasperTemplateCache`（模板编译缓存）
- [x] 3.2 从 my-gyms 复制并调整包名得到 `com.ruoyi.project.jiewu.utils.JasperPdfGenerator`（PDF 合并导出）
- [x] 3.3 新建 `com.ruoyi.project.jiewu.utils.CertificateUtils`：
  - [x] 3.3.1 底层通用方法 `public static byte[] renderCertificatePdf(String jrxmlAbsolutePath, List<Map<String,Object>> paramsList)`：
    - 通过 `JasperTemplateCache.getTemplate(jrxmlAbsolutePath)` 获取已编译模板
    - 遍历 paramsList，对每个 Map 调用 `JasperFillManager.fillReport(report, params, new JREmptyDataSource())` 生成 `JasperPrint`
    - 将所有 `JasperPrint` 通过 `JasperPdfGenerator.mergeAndExport(prints)` 合并为单个 PDF byte[]
    - paramsList 为空时返回 `new byte[0]`
  - [x] 3.3.2 默认转换器 `public static List<Map<String,Object>> buildParamsFromSignRecords(List<JwSignRecord> records)`：
    - 遍历 records，对每条 record 的 jwSignRecordSportList 拆分为 N 张
    - 每张证书的 Map 包含 4 键：teamName / playerName / itemName / grade
    - 字段映射：teamName ← record.jwTeam.teamName；playerName ← jwSignRecordSport.playerName；itemName ← record.jwGameItem.name；grade ← record.rankOrderDes（为 null 时取空字符串）

## 4. 后端下载接口

- [x] 4.1 在 `JwHaiScoreController` 新增 `POST /downloadCertificatePdf` 接口：
  - [x] 4.1.1 参数：`Long matchId`（必填）、`Long gameItemId`（可选）、`String filename`（可选）
  - [x] 4.1.2 加权限注解 `@PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:export')")` 与日志注解
  - [x] 4.1.3 通过 `jwMatchService.selectJwMatchById(matchId)` 取 `certTemplatePath`，为空时返回 `AjaxResult.error("该比赛未配置证书模板")`
  - [x] 4.1.4 通过 `RuoYiConfig.getProfile()` + `StringUtils.substringAfter(certTemplatePath, "/profile")` 将相对路径转为绝对路径
  - [x] 4.1.5 根据 gameItemId 是否为空，调用 `listGameItemGradeDes(query)` 或 `listAllGameItemGradeDes(matchId, null)` 获取报名列表
  - [x] 4.1.6 过滤逻辑：gameItemId 非空时过滤 `rankOrder != null`（service 已过滤）；gameItemId 为空时再补 `StringUtils.isNotEmpty(rankOrderDes)`
  - [x] 4.1.7 过滤后列表为空，返回 `AjaxResult.error("没有可生成的证书数据")`
  - [x] 4.1.8 调用 `CertificateUtils.buildParamsFromSignRecords` 转换后调 `renderCertificatePdf` 生成 byte[]
  - [x] 4.1.9 默认 filename：gameItemId 非空 → `证书.pdf`；gameItemId 为空 → `全部证书.pdf`；客户端传入则覆盖
  - [x] 4.1.10 用 `ResponseEntity<byte[]>` 返回，`Content-Type: application/pdf`、`Content-Disposition: attachment; filename=<URL编码后的名字>`
  - [x] 4.1.11 try-catch 包裹整个生成流程，异常时返回 `AjaxResult.error("生成 PDF 证书失败，请检查证书模板是否正确")` 并 log.error

## 5. 前端：比赛编辑页上传模板

- [x] 5.1 在 `ui/src/views/jiewu/jwMatch/index.vue` 的 form 中新增 `<el-form-item label="证书模板" prop="certTemplatePath">` 并嵌入 `<FileUpload :limit="1" :fileType="['jrxml']" v-model="form.certTemplatePath"/>`
- [x] 5.2 在 `data().form` 与重置表单逻辑中加入 `certTemplatePath: null` 初始值

## 6. 前端：JwHaiScore dialog 下载按钮

- [x] 6.1 在 `ui/src/api/jiewu/JwHaiScore.js` 新增 `export function downloadCertificatePdf(query) { ... responseType: 'blob' }`（备用，主链路用 `this.download`）
- [x] 6.2 在 `JwHaiScore/index.vue` 的「证书打印」dialog 顶部按钮行新增 `<el-button type="primary" size="mini" @click="handleDownloadCertificatePdf">下载 PDF</el-button>`
- [x] 6.3 实际项目中只有一个 dialog（`showZhengShu`），两个入口共用 dom，按 tasks 备注通过 `zhengShuMode` 区分；无需再加第二个按钮
- [x] 6.4 实现 `methods.handleDownloadCertificatePdf`：当 `zhengShuMode === 'single'` 时调用 `this.download('jiewu/JwHaiScore/downloadCertificatePdf', { matchId, gameItemId }, '证书.pdf')`
- [x] 6.5 当 `zhengShuMode === 'all'` 时调用 `this.download('jiewu/JwHaiScore/downloadCertificatePdf', { matchId }, '全部证书.pdf')`

## 7. 验证

- [ ] 7.1 后端启动验证：日志中出现「JasperReports 字体扩展已加载」字样
- [ ] 7.2 准备一个简单的 .jrxml 测试模板（含 4 个 `$P` 变量）上传到一个比赛
- [ ] 7.3 「证书打印」dialog 点「下载 PDF」，验证下载的 PDF 内容、文件名为「证书.pdf」、中文显示正常
- [ ] 7.4 「全部证书」dialog 点「下载 PDF」，验证多组别全部上榜选手 PDF 正确合并
- [ ] 7.5 验证未配置模板的比赛点击下载时提示「该比赛未配置证书模板」
- [ ] 7.6 验证现有「证书打印」浏览器打印功能不受影响

## 8. 收尾

- [x] 8.1 更新 `src/main/java/com/ruoyi/project/jiewu/CLAUDE.md` 模块文档（在「现场评分」与新增「证书 PDF 渲染」段落中标注位置）
- [x] 8.2 更新 `ui/src/views/jiewu/JwHaiScore/CLAUDE.md` 描述新增的 PDF 下载按钮
- [x] 8.3 运行 `openspec validate add-jrxml-cert-pdf-download` 验证 spec 一致性
