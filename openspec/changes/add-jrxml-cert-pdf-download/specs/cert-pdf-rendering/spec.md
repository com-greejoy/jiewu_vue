# Capability: cert-pdf-rendering

基于 JRXML 模板的证书 PDF 服务端渲染与下载能力。涵盖模板上传/绑定、参数填充、单人/批量 PDF 生成与合并、流式下载。

## ADDED Requirements

### Requirement: 比赛绑定证书模板

系统 SHALL 允许管理员在比赛级别绑定一份 .jrxml 证书模板，模板路径以 `JwMatch.certTemplatePath` 字段形式持久化。

#### Scenario: 上传 .jrxml 模板并保存

- **WHEN** 管理员在比赛编辑页选择一个后缀为 .jrxml 的文件并保存比赛
- **THEN** 系统通过 `/common/upload` 接口将文件存入服务器 `${ruoyi.profile}/upload/yyyy/MM/dd/` 目录
- **AND** 将相对路径（形如 `/profile/upload/2026/05/19/xxx_<seq>.jrxml`）写入 `jw_match.cert_template_path` 列

#### Scenario: 上传非 .jrxml 文件

- **WHEN** 管理员尝试上传一个后缀非 .jrxml 的文件
- **THEN** 前端文件选择控件拒绝该文件并提示「请上传 .jrxml 格式的文件」

#### Scenario: 修改已绑定的模板

- **WHEN** 管理员将一个新的 .jrxml 文件上传到已经绑定模板的比赛并保存
- **THEN** `jw_match.cert_template_path` 列更新为新路径
- **AND** 下次下载证书时使用新模板进行渲染

### Requirement: 通用 PDF 渲染核心方法

系统 SHALL 提供一个不依赖任何业务实体的通用 PDF 渲染方法，接收 JRXML 模板路径和参数列表，返回合并后的 PDF 字节流。

#### Scenario: 渲染多页 PDF

- **WHEN** 调用 `CertificateUtils.renderCertificatePdf(jrxmlPath, paramsList)` 传入有效模板路径与一个长度为 N 的参数 Map 列表
- **THEN** 系统返回一个包含 N 页的合并 PDF byte 数组
- **AND** 每一页对应一个 Map 参数填充结果

#### Scenario: 模板文件不存在

- **WHEN** 调用核心方法传入一个不存在的 jrxmlPath
- **THEN** 系统抛出明确异常，错误消息包含「证书模板文件不存在」字样

#### Scenario: 模板编译失败

- **WHEN** 调用核心方法传入一个内容损坏或语法错误的 .jrxml 文件
- **THEN** 系统抛出明确异常，错误消息包含「证书模板编译失败」字样

#### Scenario: 空参数列表

- **WHEN** 调用核心方法传入空的 paramsList
- **THEN** 系统返回空 byte 数组 `new byte[0]`

#### Scenario: 模板缓存命中

- **WHEN** 同一 jrxmlPath 被连续多次调用核心方法
- **THEN** 模板仅在首次调用时编译，后续调用直接命中 `JasperTemplateCache` 内存缓存

### Requirement: 默认 JwSignRecord 参数转换器

系统 SHALL 提供一个默认转换器，将 `List<JwSignRecord>` 转换为核心方法所需的参数 Map 列表，每条 record 中的每个 jwSignRecordSport 拆分为一张证书。

#### Scenario: 单人项目转换

- **WHEN** 转换器收到一条 jwSignRecordSportList 长度为 1 的 record
- **THEN** 返回一个 Map，包含四个键：teamName=record.jwTeam.teamName、playerName=该唯一选手姓名、itemName=record.jwGameItem.name、grade=record.rankOrderDes

#### Scenario: 集体项目按人拆分

- **WHEN** 转换器收到一条 jwSignRecordSportList 长度为 N（N>1）的 record
- **THEN** 返回 N 个 Map，每个 Map 的 playerName 字段为该位置选手的姓名，其余三个字段相同

#### Scenario: 缺少奖项的 record 也参与转换

- **WHEN** 转换器收到一条 rankOrderDes 为 null 的 record
- **THEN** 该 record 仍参与转换，grade 字段为空字符串
- **NOTE** 过滤逻辑应在 controller 层完成，转换器不做过滤

### Requirement: 当前组别证书 PDF 下载接口

系统 SHALL 提供一个接口，根据比赛 ID 和组别 ID 返回该组别所有获奖选手的证书 PDF 字节流。

#### Scenario: 成功下载单组别 PDF

- **WHEN** 客户端请求 `POST /jiewu/JwHaiScore/downloadCertificatePdf`，传入 matchId 与 gameItemId
- **AND** 该比赛已配置 certTemplatePath
- **AND** 该组别存在 rankOrder 非空的报名记录
- **THEN** 系统返回 HTTP 200，Content-Type=application/pdf
- **AND** 响应体为合并后的 PDF 字节流
- **AND** 仅包含 rankOrder 非空的记录对应的证书

#### Scenario: 比赛未配置证书模板

- **WHEN** 客户端请求下载接口
- **AND** 该比赛的 certTemplatePath 为 null 或空字符串
- **THEN** 系统返回 HTTP 200 + AjaxResult.error("该比赛未配置证书模板")

#### Scenario: 模板文件丢失

- **WHEN** 客户端请求下载接口
- **AND** certTemplatePath 指向的文件在磁盘上不存在
- **THEN** 系统返回 HTTP 500 或 AjaxResult.error("生成 PDF 证书失败，请检查证书模板是否正确")

### Requirement: 全部组别证书 PDF 下载接口

系统 SHALL 支持下载一场比赛全部组别的所有获奖证书 PDF（在同一接口上通过省略 gameItemId 实现）。

#### Scenario: 成功下载全部组别 PDF

- **WHEN** 客户端请求 `POST /jiewu/JwHaiScore/downloadCertificatePdf`，传入 matchId 但不传 gameItemId
- **AND** 该比赛已配置 certTemplatePath
- **AND** 该比赛存在 rankOrder 与 rankOrderDes 都非空的报名记录
- **THEN** 系统返回 HTTP 200，Content-Type=application/pdf
- **AND** 响应体为合并后的 PDF 字节流，覆盖所有组别所有获奖选手
- **AND** 仅包含 rankOrder 非空且 rankOrderDes 非空的记录对应的证书

#### Scenario: 全部组别筛选时为空

- **WHEN** 客户端请求全部组别下载，但全部记录的 rankOrder 都为 null
- **THEN** 系统返回 HTTP 200 + AjaxResult.error("没有可生成的证书数据")

### Requirement: PDF 下载文件名

系统 SHALL 支持客户端指定下载文件名，未指定时返回默认文件名。

#### Scenario: 客户端指定文件名

- **WHEN** 客户端在请求中传入 `filename` 参数
- **THEN** 响应头 `Content-Disposition: attachment; filename=<指定值>.pdf`

#### Scenario: 客户端未指定 — 单组别默认名

- **WHEN** 客户端请求单组别下载且未传 filename
- **THEN** 响应头默认文件名为 `证书.pdf`

#### Scenario: 客户端未指定 — 全部组别默认名

- **WHEN** 客户端请求全部组别下载且未传 filename
- **THEN** 响应头默认文件名为 `全部证书.pdf`

### Requirement: 前端 dialog 下载按钮

JwHaiScore 页面的「证书打印」与「全部证书」两个 dialog 顶部 SHALL 各自新增一个「下载 PDF」按钮，点击后调用对应粒度的下载接口。

#### Scenario: 单组别 dialog 下载

- **WHEN** 用户在「证书打印」dialog 顶部点击「下载 PDF」按钮
- **THEN** 前端调用 `this.download('jiewu/JwHaiScore/downloadCertificatePdf', { matchId, gameItemId }, '证书.pdf')`
- **AND** 浏览器触发下载

#### Scenario: 全部证书 dialog 下载

- **WHEN** 用户在「全部证书」dialog 顶部点击「下载 PDF」按钮
- **THEN** 前端调用 `this.download('jiewu/JwHaiScore/downloadCertificatePdf', { matchId }, '全部证书.pdf')`
- **AND** 浏览器触发下载

#### Scenario: 后端返回业务错误（如未配置模板）

- **WHEN** 后端返回 AjaxResult.error
- **THEN** 前端弹出 `el-message` 或 `$modal.msgError` 提示对应错误信息
- **AND** 不触发文件下载

### Requirement: JRXML 模板必要变量

系统 SHALL 约定证书模板中必须使用以下四个参数变量，转换器与渲染层据此填充。

#### Scenario: 模板包含全部 4 个标准变量

- **WHEN** 一个 .jrxml 模板声明了 `$P{teamName}` `$P{playerName}` `$P{itemName}` `$P{grade}`
- **THEN** 渲染时这四个变量被填充为对应字段值

#### Scenario: 模板缺失某个变量

- **WHEN** 一个 .jrxml 模板未声明 `$P{grade}`，但运行时收到 grade 参数
- **THEN** JasperReports 忽略未声明的参数，不抛错，仅该字段在 PDF 中不显示

### Requirement: 中文字体支持

系统 SHALL 内嵌中文字体扩展（宋体、黑体、楷体、仿宋、微软雅黑），确保 PDF 在任何运行环境下均能正确渲染中文。

#### Scenario: Linux 服务器渲染中文

- **WHEN** PDF 渲染在没有安装中文字体的 Linux 服务器上执行
- **AND** 模板使用 fontName="宋体"
- **THEN** 输出 PDF 仍正确显示中文字符，字体内嵌于 PDF 文件中

#### Scenario: 模板使用未支持的字体名

- **WHEN** 模板使用 fontName="某未注册字体"
- **THEN** JasperReports 降级到默认字体「宋体」继续渲染，不抛错
