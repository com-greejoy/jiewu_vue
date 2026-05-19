package com.ruoyi.project.jiewu.utils;

import com.ruoyi.project.jiewu.domain.JwSignRecord;
import com.ruoyi.project.jiewu.domain.JwSignRecordSport;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 证书 PDF 渲染工具。
 *
 * 两层封装：
 * 1. renderCertificatePdf(jrxmlPath, paramsList)：底层通用，与业务实体解耦
 * 2. buildParamsFromSignRecords(records)：默认转换器，从 JwSignRecord 列表构造参数
 *
 * 后续如需「按队伍」「按运动员」等粒度，新增对应转换器即可，核心 render 方法不变。
 */
public class CertificateUtils {

    private static final Logger log = LoggerFactory.getLogger(CertificateUtils.class);

    public static final String PARAM_TEAM_NAME = "teamName";
    public static final String PARAM_PLAYER_NAME = "playerName";
    public static final String PARAM_ITEM_NAME = "itemName";
    public static final String PARAM_GRADE = "grade";

    /**
     * 底层通用 PDF 渲染：传入 JRXML 模板绝对路径与参数 Map 列表，返回合并后的 PDF 字节流。
     * 每个 Map 对应一张证书；空列表返回 new byte[0]。
     */
    public static byte[] renderCertificatePdf(String jrxmlAbsolutePath, List<Map<String, Object>> paramsList) {
        if (paramsList == null || paramsList.isEmpty()) {
            return new byte[0];
        }

        JasperReport report = JasperTemplateCache.getTemplate(jrxmlAbsolutePath);
        List<JasperPrint> prints = new ArrayList<>(paramsList.size());
        try {
            for (Map<String, Object> params : paramsList) {
                JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(params), new JREmptyDataSource());
                prints.add(print);
            }
        } catch (JRException e) {
            log.error("JasperReports 填充失败, template={}", jrxmlAbsolutePath, e);
            throw new RuntimeException("证书模板填充失败", e);
        }

        return JasperPdfGenerator.mergeAndExport(prints);
    }

    /**
     * 默认转换器：将 JwSignRecord 列表转换为参数 Map 列表。
     * 一条 record 中的每个 jwSignRecordSport 拆分为一张证书；
     * 每个 Map 包含 4 个键：teamName / playerName / itemName / grade。
     * 不做过滤，过滤逻辑放在 controller 层。
     */
    public static List<Map<String, Object>> buildParamsFromSignRecords(List<JwSignRecord> records) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (records == null || records.isEmpty()) {
            return result;
        }

        for (JwSignRecord record : records) {
            String teamName = resolveTeamName(record);
            String itemName = resolveItemName(record);
            String grade = record.getRankOrderDes() == null ? "" : record.getRankOrderDes();

            List<JwSignRecordSport> sports = record.getJwSignRecordSportList();
            if (sports == null || sports.isEmpty()) {
                Map<String, Object> params = new HashMap<>();
                params.put(PARAM_TEAM_NAME, teamName);
                params.put(PARAM_PLAYER_NAME, "");
                params.put(PARAM_ITEM_NAME, itemName);
                params.put(PARAM_GRADE, grade);
                result.add(params);
                continue;
            }

            for (JwSignRecordSport sport : sports) {
                Map<String, Object> params = new HashMap<>();
                params.put(PARAM_TEAM_NAME, teamName);
                params.put(PARAM_PLAYER_NAME, sport.getPlayerName() == null ? "" : sport.getPlayerName());
                params.put(PARAM_ITEM_NAME, itemName);
                params.put(PARAM_GRADE, grade);
                result.add(params);
            }
        }

        return result;
    }

    private static String resolveTeamName(JwSignRecord record) {
        if (record.getJwTeam() != null && record.getJwTeam().getTeamName() != null) {
            return record.getJwTeam().getTeamName();
        }
        return record.getTeamName() == null ? "" : record.getTeamName();
    }

    private static String resolveItemName(JwSignRecord record) {
        if (record.getJwGameItem() != null && record.getJwGameItem().getName() != null) {
            return record.getJwGameItem().getName();
        }
        return record.getItemName() == null ? "" : record.getItemName();
    }
}