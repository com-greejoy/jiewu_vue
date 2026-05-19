package com.ruoyi.project.jiewu.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JasperPdfGenerator {

    private static final Logger log = LoggerFactory.getLogger(JasperPdfGenerator.class);

    public static byte[] mergeAndExport(List<JasperPrint> prints) {
        if (prints == null || prints.isEmpty()) {
            return new byte[0];
        }

        JasperPrint merged = prints.get(0);
        for (int i = 1; i < prints.size(); i++) {
            for (JRPrintPage page : prints.get(i).getPages()) {
                merged.addPage(page);
            }
        }

        try {
            log.info("导出 PDF, 总页数={}", merged.getPages().size());
            return JasperExportManager.exportReportToPdf(merged);
        } catch (JRException e) {
            log.error("PDF 导出失败", e);
            throw new RuntimeException("PDF 导出失败", e);
        }
    }
}