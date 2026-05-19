package com.ruoyi.project.jiewu.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class JasperTemplateCache {

    private static final Logger log = LoggerFactory.getLogger(JasperTemplateCache.class);

    private static final ConcurrentHashMap<String, JasperReport> CACHE = new ConcurrentHashMap<>();

    public static JasperReport getTemplate(String filePath) {
        return CACHE.computeIfAbsent(filePath, path -> {
            try (InputStream is = new FileInputStream(path)) {
                log.info("编译 JasperReports 模板: {}", path);
                return JasperCompileManager.compileReport(is);
            } catch (JRException e) {
                log.error("JasperReports 模板编译失败: {}", path, e);
                throw new RuntimeException("证书模板编译失败", e);
            } catch (Exception e) {
                log.error("JasperReports 模板文件读取失败: {}", path, e);
                throw new RuntimeException("证书模板文件不存在", e);
            }
        });
    }

    public static void invalidate(String filePath) {
        CACHE.remove(filePath);
        log.info("已清除 JasperReports 模板缓存: {}", filePath);
    }
}