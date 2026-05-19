-- jrxml-cert-pdf: 比赛表新增证书模板路径字段
ALTER TABLE `jw_match` ADD COLUMN `cert_template_path` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '证书模板路径(JRXML)' AFTER `qun_code`;