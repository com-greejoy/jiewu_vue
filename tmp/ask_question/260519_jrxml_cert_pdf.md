# JRXML 证书 PDF 下载 - 需求澄清记录

## 需求摘要（最终）

在 jiewu_vue 的 `JwHaiScore/index.vue` 「证书打印」/「全部证书」两个 dialog 顶部，新增「下载 PDF」按钮，下载渲染后的 PDF（渲染范围与 dialog 一致）。
渲染采用 JRXML + JasperReports，参考 my-gyms 已落地方案：

- **模板绑定**：`JwMatch.certTemplatePath` 字段，存 RuoYi 标准相对路径 `/profile/upload/yyyy/MM/dd/xxx.jrxml`
- **上传方式**：JwMatch 编辑页用现有 `<FileUpload>` 组件（限制 `.jrxml`），走 `/common/upload` 通用接口
- **模板参数**（最小 4 个）：teamName / playerName / itemName(组别名) / grade(奖项)
- **粒度**：一人一张证书（多运动员场景拆分为多张，playerName = 当前选手名）
- **过滤条件**：与现 dialog 一致 —— 证书打印 dialog 用 `rankOrder` 过滤、全部证书 dialog 用 `rankOrder && rankOrderDes` 过滤
- **下载形态**：后端 `ResponseEntity<byte[]>` 流式返回（与项目现有 `downloadAllScore` 一致），前端 `this.download()` 触发；文件名支持自定义传入，默认 `证书.pdf` / `全部证书.pdf`
- **核心方法（两层封装）**：
  - 底层通用：`com.ruoyi.project.jiewu.utils.CertificateUtils#renderCertificatePdf(String jrxmlPath, List<Map<String,Object>> paramsList): byte[]`
  - 默认转换器：`buildParamsFromSignRecords(List<JwSignRecord>): List<Map<String,Object>>`
  - 后续扩展（队伍/特定运动员/组别）只需新增转换器，核心方法不变
- **新旧并存**：保留现有 dialog 浏览器打印，新增 PDF 下载按钮在打印按钮旁边
- **未配置模板**：返回错误「该比赛未配置证书模板」
- **依赖**：pom.xml 新增 `net.sf.jasperreports:jasperreports:6.21.0`
- **从 my-gyms 直接复制**：`JasperPdfGenerator` / `JasperTemplateCache` / `JasperReportsConfig` / `fonts/*.ttf` / `fonts.xml` / `jasperreports_extension.properties`

## 项目发现

- `ui/src/views/jiewu/JwHaiScore/index.vue` - 证书打印 dialog 当前实现（CSS + 浏览器打印），现有「证书打印」「全部证书」两个按钮
- `ui/src/views/jiewu/JwHaiScore/index.vue#handlePrintZhengshu` - 当前组别证书数据获取入口（listGameItemGradeDes）
- `ui/src/views/jiewu/JwHaiScore/index.vue#handlePrintAllZhengshu` - 全部证书数据获取入口（listAllGameItemGradeDes）
- `src/main/java/com/ruoyi/project/jiewu/controller/JwHaiScoreController.java#listGameItemGradeDes` - 当前组别成绩
- `src/main/java/com/ruoyi/project/jiewu/controller/JwHaiScoreController.java#listAllGameItemGradeDes` - 全部成绩
- `src/main/java/com/ruoyi/project/jiewu/controller/JwHaiScoreController.java#downloadAllScore` - 现有 `ResponseEntity<byte[]>` 流式下载范例
- `src/main/java/com/ruoyi/project/jiewu/domain/JwSignRecord.java` - 报名记录（teamName、jwSignRecordSportList、rankOrder、rankOrderDes、jwGameItem 等）
- `src/main/java/com/ruoyi/project/jiewu/domain/JwMatch.java` - 比赛实体，**需新增 certTemplatePath**
- `src/main/resources/mybatis/jiewu/JwMatchMapper.xml` - 需新增 certTemplatePath 字段映射（待确认）
- `src/main/java/com/ruoyi/common/utils/file/FileUploadUtils.java` - 已有 RuoYi 标准上传工具
- `src/main/java/com/ruoyi/project/common/CommonController.java#uploadFile` - 通用 `/common/upload` 接口
- `ui/src/components/FileUpload/index.vue` - 通用文件上传组件，支持 fileType 限制
- `ui/src/views/jiewu/jwMatch/index.vue` - 比赛管理页面，需要新增 .jrxml 上传控件（form item）
- `ui/src/utils/request.js#download` - `this.download(url, params, filename)` 通用 blob 下载方法
- `src/main/resources/application.yml` - profile 路径 `C:/workspace/code/greejoy/jiewu_vue/upload`
- `pom.xml` - **尚未引入 jasperreports**
- `my-gyms/.../JasperPdfGenerator.java` - 核心 PDF 合并工具（直接复制）
- `my-gyms/.../JasperTemplateCache.java` - 模板编译缓存（直接复制）
- `my-gyms/.../JasperReportsConfig.java` - 默认字体配置（直接复制）
- `my-gyms/.../resources/fonts/fonts.xml` + ttf - 字体扩展（直接复制）
- `my-gyms/.../GymsScoreController.java#exportCertificatePdf` - 参考实现

## Q&A 记录

### 第 1 轮

- **Q1**: JRXML 模板绑定层级 → **比赛 JwMatch.certTemplatePath**
- **Q2**: dialog 下载按钮形态 → **只加一个「下载 PDF」按钮，渲染范围与当前 dialog 一致**
- **Q3**: JRXML 模板参数 → **4 个最小集**：teamName、playerName、itemName、grade
- **Q4**: 深度控制 → Agent 判断遗漏点

### 第 2 轮

- **Q5**: 核心方法接口形态 → **两层封装**：底层通用 `renderCertificatePdf(jrxmlPath, List<Map>)` + 默认 `buildParamsFromSignRecords` 转换器
- **Q6**: 多运动员处理 → **一人一张**，playerName = 当前选手名
- **Q7**: 下载交互 → **遵循项目惯例**：`ResponseEntity<byte[]>` 流式 + `this.download()`
- **Q8**: 模板上传 → **JwMatch 编辑页加 .jrxml 上传控件**
- **Q9**: 未配置模板 → **报错「该比赛未配置证书模板」**
- **Q10**: 深度控制 → 继续深入

### 第 3 轮

- **Q11**: PDF 过滤条件 → **与现 dialog 一致**：证书打印 dialog 用 rankOrder 过滤、全部证书 dialog 用 rankOrder && rankOrderDes 过滤
- **Q12**: 核心方法包路径 → `com.ruoyi.project.jiewu.utils.CertificateUtils`（业务模块下）
- **Q13**: 「下载 PDF」按钮位置 → **dialog 顶部「打印」按钮旁边**（两个按钮并列）
- **Q14**: 现有逻辑处理 → **保留，新旧并存**
- **Q15**: 深度控制 → Agent 判断遗漏点

### 第 4 轮

- **Q16**: certTemplatePath 存储格式 → **RuoYi 标准相对路径**（`/profile/upload/yyyy/MM/dd/xxx.jrxml`），与项目其他上传一致
- **Q17**: PDF 文件名 → **支持传入自定义**，默认 `证书.pdf` / `全部证书.pdf`
- **Q18**: 深度控制 → **结束澄清**，进入 `/openspec-propose` 创建提案

## 备忘

- jiewu_vue 的 `download` 工具是 `POST + blob`，下载方法为 `this.download('jiewu/JwHaiScore/xxx', params, '文件名.pdf')`
- 后端 `RuoYiConfig.getProfile()` 即对应 `C:/workspace/code/greejoy/jiewu_vue/upload`，`Constants.RESOURCE_PREFIX = "/profile"`
- 当前 jiewu_vue 的 `rankOrderDes` 已是「一等奖/二等奖/...」格式，无需复制 my-gyms 的 `CertificateUtils.buildRankingGrade` 等奖计算逻辑
