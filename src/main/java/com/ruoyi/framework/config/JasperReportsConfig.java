package com.ruoyi.framework.config;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class JasperReportsConfig {

    private static final Logger log = LoggerFactory.getLogger(JasperReportsConfig.class);

    @PostConstruct
    public void init() {
        DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
        context.setProperty("net.sf.jasperreports.default.font.name", "宋体");
        context.setProperty("net.sf.jasperreports.default.pdf.font.name", "宋体");
        context.setProperty("net.sf.jasperreports.default.pdf.encoding", "Identity-H");
        context.setProperty("net.sf.jasperreports.default.pdf.embedded", "true");
        log.info("JasperReports 字体扩展已加载，默认字体: 宋体，支持: 宋体/黑体/楷体/仿宋/微软雅黑");
    }
}