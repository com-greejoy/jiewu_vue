# 新增 JRXML 证书 PDF 下载能力

## Why

当前 `JwHaiScore/index.vue` 的「证书打印」/「全部证书」dialog 依赖浏览器打印（CSS 渲染），存在三个痛点：

1. 打印输出依赖客户端浏览器与打印机驱动，跨环境样式漂移严重，无法保证版式一致
2. 业务方无法以「证书 PDF 文件」的形式留档、分发或线上展示
3. 模板写死在前端，赛事方想换证书设计必须改前端代码并重新发版

姊妹项目 my-gyms 已经落地了一套基于 JRXML + JasperReports 的服务端 PDF 渲染方案，证明可行。捷众赛事项目需要将该能力复用过来，做到「上传 .jrxml → 服务端渲染 → 一键下载 PDF」。

## What Changes

- 在 `JwMatch` 实体新增 `certTemplatePath` 字段，存储该比赛绑定的 .jrxml 模板路径
- 在比赛编辑页（`jwMatch/index.vue`）新增 .jrxml 文件上传控件，复用现有 `<FileUpload>` 组件
- 在 `JwHaiScore/index.vue` 的两个证书 dialog 顶部，新增「下载 PDF」按钮（与现有「打印」按钮并列；新旧能力共存）
- 新增 `JwHaiScoreController#downloadCertificatePdf(matchId, gameItemId)` 接口：支持「单组别」和「全部组别」两种粒度
- 新增 `com.ruoyi.project.jiewu.utils.CertificateUtils` 工具类，**核心方法分两层封装**：
  - 底层通用：`renderCertificatePdf(String jrxmlPath, List<Map<String,Object>> paramsList): byte[]`
  - 默认转换器：`buildParamsFromSignRecords(List<JwSignRecord>): List<Map<String,Object>>`
  - 后续扩展（按队伍/特定运动员等）只需新增转换器，核心方法不变
- 在 `pom.xml` 引入 `net.sf.jasperreports:jasperreports:6.21.0`
- 从 my-gyms 复制：`JasperPdfGenerator`、`JasperTemplateCache`、`JasperReportsConfig`、`fonts.xml`、五个中文 .ttf 字体、`jasperreports_extension.properties`
- 未配置 `certTemplatePath` 时，调用接口直接返回错误「该比赛未配置证书模板」

## Capabilities

### New Capabilities

- `cert-pdf-rendering`: 基于 JRXML 模板的证书 PDF 服务端渲染与下载能力。涵盖模板上传/绑定、参数填充、单人/批量 PDF 生成与合并、流式下载

### Modified Capabilities

无（项目目前 `specs/` 为空，本次为首个 capability）

## Impact

**新增代码**

- 实体：`JwMatch.certTemplatePath` 字段
- 数据库：`jw_match` 表新增 `cert_template_path` 列
- 工具类：`com.ruoyi.project.jiewu.utils.CertificateUtils`
- 配置类：`com.ruoyi.framework.config.JasperReportsConfig`
- 资源：`src/main/resources/fonts/` 目录下 5 个 ttf 字体、`fonts.xml`、`jasperreports_extension.properties`

**修改代码**

- `JwHaiScoreController` 新增 `downloadCertificatePdf` 接口
- `JwMatch.java` / `JwMatchMapper.xml` 新增字段映射
- `ui/src/views/jiewu/jwMatch/index.vue` 新增上传控件
- `ui/src/views/jiewu/JwHaiScore/index.vue` 新增下载按钮 + API 调用
- `ui/src/api/jiewu/JwHaiScore.js` 新增下载方法
- `pom.xml` 新增 JasperReports 依赖

**API 变更**

- 新增 `POST /jiewu/JwHaiScore/downloadCertificatePdf`（返回 `application/pdf` 流）

**依赖**

- 引入 `net.sf.jasperreports:jasperreports:6.21.0`（约 5MB，引入字体扩展机制）

**不影响**

- 现有「证书打印」/「全部证书」浏览器打印逻辑完整保留
- 不修改 `JwHaiScore` / `JwSignRecord` 等核心实体
