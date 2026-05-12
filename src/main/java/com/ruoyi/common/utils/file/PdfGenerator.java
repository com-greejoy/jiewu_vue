package com.ruoyi.common.utils.file;

import com.ruoyi.common.utils.BigDecimalUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.project.jiewu.domain.*;

import com.ruoyi.project.jiewu.service.JwGameItemService;
import com.ruoyi.project.jiewu.service.JwSignRecordService;
import com.ruoyi.project.jiewu.service.JwTeamService;
import com.ruoyi.project.monitor.service.ISysJobLogService;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PdfGenerator {

    private static int extractPlaceOrder(String key) {
        String parts = key.split(" ")[0];
        try {
            return Integer.parseInt(parts.replaceAll("\\D", ""));
        } catch (NumberFormatException e) {

        }
        throw new IllegalArgumentException("无法从键中提取PlaceOrder: " + key);
    }


    private static int getSessionNumber(String name) {
        // 提取"第几场"数字部分
        Pattern pattern = Pattern.compile("第(\\d+)场");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1; // 默认值，如果未找到匹配项
    }


    private static String getSessionNumberTop(String name) {
        // 提取"第几场"数字部分
        Pattern pattern = Pattern.compile("第(\\d+)阶段");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return String.valueOf(matcher.group(1));
        }
        return ""; // 默认值，如果未找到匹配项
    }

//    private static int getSessionNumber(String name) {
//        // 提取"第几场"数字部分
//        Pattern pattern = Pattern.compile("第(\\d+)场");
//        Matcher matcher = pattern.matcher(name);
//        if (matcher.find()) {
//            return Integer.parseInt(matcher.group(1));
//        }
//        return -1; // 默认值，如果未找到匹配项
//    }

    private static char getVenue(String name) {
        // 提取最后的场地字符（假设格式正确）
        Pattern pattern = Pattern.compile("【([A-Za-z])】$");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return matcher.group(1).charAt(0);
        }
        return ' ';
    }


    public static byte[] generateGradeWord(List<JwSignRecord> records, String matchName) throws IOException {
        XWPFDocument document = new XWPFDocument();

        // 创建标题
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun1 = titleParagraph.createRun();
        titleRun1.setText(matchName);
        titleRun1.setFontSize(18);
        titleRun1.setBold(true);
        titleRun1.addBreak();

        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("成绩单");
        titleRun.setFontSize(16);
        titleRun.setBold(true);

        XWPFRun aaa22 = titleParagraph.createRun();
        aaa22.setText("");
        aaa22.setFontSize(16);
        aaa22.setBold(true);
        aaa22.addBreak();

        Map<String, List<JwSignRecord>> groupedRecords = records.stream().collect(Collectors.groupingBy(jws -> jws.getJwGameItem().getCode() + ":" + jws.getJwGameItem().getName()));

        String[] keyS = groupedRecords.keySet().toArray(new String[0]);

        Arrays.sort(keyS, (s1, s2) -> {
            Long m1 = Long.parseLong(s1.split(":")[0]);
            Long m2 = Long.parseLong(s2.split(":")[0]);
            return Long.compare(m1, m2);
        });

        // 添加每个分组的数据为表格
        for (String key : keyS) {
            JwGameItem jwGameItem = SpringUtils.getBean(JwGameItemService.class).selectJwGameItemByCode(key.split(":")[0], records.get(0).getMatchId());
            List<JwSignRecord> jwSignRecordList = groupedRecords.get(key);
            if (jwSignRecordList != null && jwSignRecordList.size() > 0) {

                // 按照上场序号排序
                List<JwSignRecord> sortedList = jwSignRecordList.stream().sorted(Comparator.comparingLong(JwSignRecord::getRankOrder)).collect(Collectors.toList());

                // 创建分组标题
                XWPFParagraph groupTitleParagraph = document.createParagraph();
                groupTitleParagraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun groupTitleRun = groupTitleParagraph.createRun();
                groupTitleRun.setText(key);
                groupTitleRun.setFontSize(14);
                groupTitleRun.setBold(true);

                // 设置页面宽度（假设为100%）
                CTTblWidth tableWidth = CTTblWidth.Factory.newInstance();
                tableWidth.setType(STTblWidth.PCT);
                tableWidth.setW(BigInteger.valueOf(5000));

                // 创建表格
                int cols = 5;
                if (jwGameItem.getMatchType().equals("2")) {
                    cols = 6;
                }
                XWPFTable table = document.createTable(sortedList.size() + 1, cols); // 表头+数据行数
                table.getCTTbl().addNewTblPr().setTblW(tableWidth);

                int columnCount = 5;
                for (int col = 0; col < columnCount; col++) {
                    CTTblWidth cellWidth = CTTblWidth.Factory.newInstance();
                    cellWidth.setType(STTblWidth.DXA);
                    int w = 2000;
                    if (jwGameItem.getMatchType().equals("2")) {
                        switch (col) {
                            case 0:
                                w = 500;
                                break;
                            case 1:
                                w = 1000;
                                break;
                            case 2:
                                w = 1000;
                                break;
                            case 3:
                                w = 800;
                                break;
                            case 4:
                                w = 2000;
                                break;
                            case 5:
                                w = 2000;
                                break;
                        }
                    } else {
                        switch (col) {
                            case 0:
                                w = 500;
                                break;
                            case 1:
                                w = 800;
                                break;
                            case 2:
                                w = 800;
                                break;
                            case 3:
                                w = 2000;
                                break;
                            case 4:
                                w = 2000;
                                break;
                        }
                    }

                    cellWidth.setW(BigInteger.valueOf(w)); // 2000 对应于大约25%

                    for (XWPFTableRow row : table.getRows()) {
                        XWPFTableCell cell = row.getCell(col);
                        if (cell.getCTTc().getTcPr() == null) {
                            cell.getCTTc().addNewTcPr();
                        }
                        cell.getCTTc().getTcPr().setTcW(cellWidth);
                    }
                }

                // 设置表头
                XWPFTableRow headerRow = table.getRow(0);
                String[] headers = {"名次", "成绩", "选手编号", "选手", "代表队"};
                if (jwGameItem.getMatchType().equals("2")) {
                    headers = new String[]{"名次", "决赛成绩", "海选成绩", "背号", "选手", "代表队"};
                }
                for (int i = 0; i < headers.length; i++) {

                    if (headerRow.getCell(i) == null) {
                        headerRow.createCell();
                    }

                    XWPFTableCell cell = headerRow.getCell(i);
                    XWPFParagraph p = cell.getParagraphs().get(0);
                    if (p == null) {
                        p = cell.addParagraph();
                    }
                    p.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = p.createRun();
                    run.setText(headers[i]);
                    run.setBold(true);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                }

                // 添加记录到表格中，并设置单元格内容居中
                int rowIndex = 1;
                for (JwSignRecord record : sortedList) {
                    XWPFTableRow dataRow = table.getRow(rowIndex++);
                    String[] rowContents = {
                            String.valueOf(record.getRankOrder()),
                            String.valueOf(record.getRankOrderDes()),
                            String.valueOf(StringUtils.isNotNull(record.getBackNumber()) ? record.getBackNumber() : "-"),
                            record.getJwSignRecordSportList().stream().sorted(Comparator.comparing(JwSignRecordSport::getPlayerName)).map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")),
                            record.getTeamName()
                    };
                    if (jwGameItem.getMatchType().equals("2")) {
                        rowContents = new String[]{
                                String.valueOf(record.getRankOrder()),
                                String.valueOf(StringUtils.isNotEmpty(record.getDescription()) ? record.getDescription() : "-"),
                                String.valueOf(record.getAvgScore()),
                                String.valueOf(StringUtils.isNotNull(record.getBackNumber()) ? record.getBackNumber() : "-"),
                                record.getJwSignRecordSportList().stream().sorted(Comparator.comparing(JwSignRecordSport::getPlayerName)).map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")),
                                record.getTeamName()
                        };
                    }

                    for (int i = 0; i < rowContents.length; i++) {
                        XWPFTableCell cell = dataRow.getCell(i);

                        XWPFParagraph p = cell.getParagraphs().get(0);
                        if (p == null) {
                            p = cell.addParagraph();
                        }

                        p.setAlignment(ParagraphAlignment.CENTER);

                        XWPFRun run = p.createRun();
                        run.setText(rowContents[i]);
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    }
                }
                // 创建分组标题  中间空行
                XWPFParagraph groupTitleParagraph2 = document.createParagraph();
                groupTitleParagraph2.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun2 = groupTitleParagraph2.createRun();
                groupTitleRun2.setText("");
                groupTitleRun2.setFontSize(34);
                groupTitleRun2.setBold(true);
                XWPFParagraph groupTitleParagraph3 = document.createParagraph();
                groupTitleParagraph3.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun3 = groupTitleParagraph3.createRun();
                groupTitleRun3.setText("");
                groupTitleRun3.setFontSize(34);
                groupTitleRun3.setBold(true);
            }
        }

        // 调用方法来设置整个文档的页边距
        setPageMargins(document, 720, 720, 720, 720);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        document.write(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] generateTeamGradeWord(List<JwMatchTeamGrade> jwMatchTeamGradeList, String matchName) throws IOException {
        XWPFDocument document = new XWPFDocument();

        // 创建标题
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun1 = titleParagraph.createRun();
        titleRun1.setText(matchName);
        titleRun1.setFontSize(18);
        titleRun1.setBold(true);
        titleRun1.addBreak();


        for (JwMatchTeamGrade jwMatchTeamGrade : jwMatchTeamGradeList) {

            XWPFParagraph titleParagraph2 = document.createParagraph();
            titleParagraph2.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun titleRunt = titleParagraph2.createRun();
            titleRunt.setText(SpringUtils.getBean(JwTeamService.class).selectJwTeamById(jwMatchTeamGrade.getTeamId()).getTeamName());
            titleRunt.setFontSize(16);
            titleRunt.setBold(true);
            titleRunt.addBreak();

            XWPFRun titleRun = titleParagraph2.createRun();
            titleRun.setText("成绩单");
            titleRun.setFontSize(16);
            titleRun.setBold(true);

            XWPFRun aaa22 = titleParagraph2.createRun();
            aaa22.setText("");
            aaa22.setFontSize(16);
            aaa22.setBold(true);
            aaa22.addBreak();


            List<JwSignRecord> jwSignRecordList = jwMatchTeamGrade.getJwSignRecordList();

            if (jwSignRecordList != null && jwSignRecordList.size() > 0) {

                List<JwSignRecord> sortedList = jwSignRecordList.stream().sorted(Comparator.comparingLong(JwSignRecord::getGameItemCode)).collect(Collectors.toList());

                // 创建分组标题
                XWPFParagraph groupTitleParagraph = document.createParagraph();
                groupTitleParagraph.setAlignment(ParagraphAlignment.CENTER);

                // 设置页面宽度（假设为100%）
                CTTblWidth tableWidth = CTTblWidth.Factory.newInstance();
                tableWidth.setType(STTblWidth.PCT);
                tableWidth.setW(BigInteger.valueOf(5000));

                // 创建表格
                int cols = 4;

                XWPFTable table = document.createTable(sortedList.size() + 1, cols); // 表头+数据行数
                table.getCTTbl().addNewTblPr().setTblW(tableWidth);

                int columnCount = 4;
                for (int col = 0; col < columnCount; col++) {
                    CTTblWidth cellWidth = CTTblWidth.Factory.newInstance();
                    cellWidth.setType(STTblWidth.DXA);
                    int w = 2000;
                    switch (col) {
                        case 0:
                            w = 1000;
                            break;
                        case 1:
                            w = 1500;
                            break;
                        case 2:
                            w = 1000;
                            break;
                        case 3:
                            w = 2500;
                            break;
                    }

                    cellWidth.setW(BigInteger.valueOf(w)); // 2000 对应于大约25%

                    for (XWPFTableRow row : table.getRows()) {
                        XWPFTableCell cell = row.getCell(col);
                        if (cell.getCTTc().getTcPr() == null) {
                            cell.getCTTc().addNewTcPr();
                        }
                        cell.getCTTc().getTcPr().setTcW(cellWidth);
                    }
                }

                // 设置表头
                XWPFTableRow headerRow = table.getRow(0);
                String[] headers = {"选手编号", "选手", "成绩", "项目"};

                for (int i = 0; i < headers.length; i++) {

                    if (headerRow.getCell(i) == null) {
                        headerRow.createCell();
                    }

                    XWPFTableCell cell = headerRow.getCell(i);
                    XWPFParagraph p = cell.getParagraphs().get(0);
                    if (p == null) {
                        p = cell.addParagraph();
                    }
                    p.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = p.createRun();
                    run.setText(headers[i]);
                    run.setBold(true);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                }

                // 添加记录到表格中，并设置单元格内容居中
                int rowIndex = 1;
                for (JwSignRecord record : sortedList) {
                    XWPFTableRow dataRow = table.getRow(rowIndex++);
                    String[] rowContents = {
                            String.valueOf(record.getBackNumber()),
                            record.getJwSignRecordSportList().stream().sorted(Comparator.comparing(JwSignRecordSport::getPlayerName)).map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")),
                            String.valueOf(record.getRankOrderDes()),
                            record.getItemName()
                    };

                    for (int i = 0; i < rowContents.length; i++) {
                        XWPFTableCell cell = dataRow.getCell(i);

                        XWPFParagraph p = cell.getParagraphs().get(0);
                        if (p == null) {
                            p = cell.addParagraph();
                        }

                        p.setAlignment(ParagraphAlignment.CENTER);

                        XWPFRun run = p.createRun();
                        run.setText(rowContents[i]);
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    }
                }
                // 创建分组标题  中间空行
                XWPFParagraph groupTitleParagraph2 = document.createParagraph();
                groupTitleParagraph2.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun2 = groupTitleParagraph2.createRun();
                groupTitleRun2.setText("");
                groupTitleRun2.setFontSize(34);
                groupTitleRun2.setBold(true);
                XWPFParagraph groupTitleParagraph3 = document.createParagraph();
                groupTitleParagraph3.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun3 = groupTitleParagraph3.createRun();
                groupTitleRun3.setText("");
                groupTitleRun3.setFontSize(34);
                groupTitleRun3.setBold(true);
            }

            List<JwAwardsItem> jwAwardsItemList = jwMatchTeamGrade.getAwardsItemList();

            if (jwAwardsItemList != null && jwAwardsItemList.size() > 0) {

                XWPFParagraph titleParagraph3 = document.createParagraph();
                titleParagraph3.setAlignment(ParagraphAlignment.CENTER);


                XWPFRun titleRunc = titleParagraph3.createRun();
                titleRunc.setText("成绩统计");
                titleRunc.setFontSize(16);
                titleRunc.setBold(true);

                XWPFRun aaa22cc = titleParagraph3.createRun();
                aaa22cc.setText("");
                aaa22cc.setFontSize(16);
                aaa22cc.setBold(true);
                aaa22cc.addBreak();


                // 创建分组标题
                XWPFParagraph groupTitleParagraph = document.createParagraph();
                groupTitleParagraph.setAlignment(ParagraphAlignment.CENTER);

                // 设置页面宽度（假设为100%）
                CTTblWidth tableWidth = CTTblWidth.Factory.newInstance();
                tableWidth.setType(STTblWidth.PCT);
                tableWidth.setW(BigInteger.valueOf(5000));

                // 创建表格
                int cols = 4;

                XWPFTable table = document.createTable(jwAwardsItemList.size() + 1, cols); // 表头+数据行数
                table.getCTTbl().addNewTblPr().setTblW(tableWidth);

                int columnCount = 4;
                for (int col = 0; col < columnCount; col++) {
                    CTTblWidth cellWidth = CTTblWidth.Factory.newInstance();
                    cellWidth.setType(STTblWidth.DXA);
                    int w = 2000;
                    switch (col) {
                        case 0:
                            w = 2000;
                            break;
                        case 1:
                            w = 2000;
                            break;
                        case 2:
                            w = 2000;
                            break;
                        case 3:
                            w = 2000;
                            break;
                    }

                    cellWidth.setW(BigInteger.valueOf(w)); // 2000 对应于大约25%

                    for (XWPFTableRow row : table.getRows()) {
                        XWPFTableCell cell = row.getCell(col);
                        if (cell.getCTTc().getTcPr() == null) {
                            cell.getCTTc().addNewTcPr();
                        }
                        cell.getCTTc().getTcPr().setTcW(cellWidth);
                    }
                }

                // 设置表头
                XWPFTableRow headerRow = table.getRow(0);
                String[] headers = {"奖项", "数量", "证书", "奖牌"};

                for (int i = 0; i < headers.length; i++) {

                    if (headerRow.getCell(i) == null) {
                        headerRow.createCell();
                    }

                    XWPFTableCell cell = headerRow.getCell(i);
                    XWPFParagraph p = cell.getParagraphs().get(0);
                    if (p == null) {
                        p = cell.addParagraph();
                    }
                    p.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = p.createRun();
                    run.setText(headers[i]);
                    run.setBold(true);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                }

                // 添加记录到表格中，并设置单元格内容居中
                int rowIndex = 1;
                for (JwAwardsItem awardsItem : jwAwardsItemList) {
                    XWPFTableRow dataRow = table.getRow(rowIndex++);
                    String[] rowContents = {
                            String.valueOf(awardsItem.getRankText()),
                            String.valueOf(awardsItem.getCountNum()),
                            String.valueOf(awardsItem.getZhengShu()),
                            String.valueOf(awardsItem.getJiangPai())
                    };

                    for (int i = 0; i < rowContents.length; i++) {
                        XWPFTableCell cell = dataRow.getCell(i);

                        XWPFParagraph p = cell.getParagraphs().get(0);
                        if (p == null) {
                            p = cell.addParagraph();
                        }

                        p.setAlignment(ParagraphAlignment.CENTER);

                        XWPFRun run = p.createRun();
                        run.setText(rowContents[i]);
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    }
                }
                // 创建分组标题  中间空行
                XWPFParagraph groupTitleParagraph2 = document.createParagraph();
                groupTitleParagraph2.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun2 = groupTitleParagraph2.createRun();
                groupTitleRun2.setText("");
                groupTitleRun2.setFontSize(34);
                groupTitleRun2.setBold(true);
                XWPFParagraph groupTitleParagraph3 = document.createParagraph();
                groupTitleParagraph3.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun3 = groupTitleParagraph3.createRun();
                groupTitleRun3.setText("");
                groupTitleRun3.setFontSize(34);
                groupTitleRun3.setBold(true);


            }

        }
        // 调用方法来设置整个文档的页边距
        setPageMargins(document, 720, 720, 720, 720);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        document.write(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }


    public static byte[] generateWord(List<JwSignRecord> records, String matchName, JwTeam jwTeam, String type2) throws IOException {
        XWPFDocument document = new XWPFDocument();

        // 创建标题
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun1 = titleParagraph.createRun();
        titleRun1.setText(matchName);
        titleRun1.setFontSize(18);
        titleRun1.setBold(true);
        titleRun1.addBreak();

        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("赛程表明细");
        titleRun.setFontSize(16);
        titleRun.setBold(true);


        if (jwTeam != null) {
            titleRun.addBreak();
            XWPFRun aaa = titleParagraph.createRun();
            aaa.setText(jwTeam.getTeamName());
            aaa.setFontSize(16);
            aaa.setBold(true);
            aaa.addBreak();
        }

        XWPFRun aaa22 = titleParagraph.createRun();
        aaa22.setText("");
        aaa22.setFontSize(16);
        aaa22.setBold(true);
        aaa22.addBreak();


        Map<String, List<JwSignRecord>> groupedRecords = records.stream().collect(
                Collectors.groupingBy(jws -> jws.getScheduleName() + "【第" + jws.getPlaceOrder() + "场 - 第" + jws.getJwScheduleItem().getScheduleIndex() + "组 "
                        + DateUtils.parseDateToStr("MM-dd HH:mm", jws.getJwScheduleItem().getShceduleTime())
                        + "】 ooo" + jws.getItemName()
                        + "【" + DictUtils.getDictLabel("jw_area", jws.getJwScheduleItem().getArea()) + "】"
                ));

        String[] keyS = groupedRecords.keySet().toArray(new String[0]);

        RoomSessionGroupSorter.sort(keyS);

        for (String key : keyS) {
            List<JwSignRecord> jwSignRecordList = groupedRecords.get(key);
            if (jwSignRecordList != null && jwSignRecordList.size() > 0) {

                // 按照上场序号排序
                List<JwSignRecord> sortedList = jwSignRecordList.stream().sorted(Comparator.comparingLong(JwSignRecord::getIndexOrder)).collect(Collectors.toList());

                // 创建分组标题
                XWPFParagraph groupTitleParagraph = document.createParagraph();
                groupTitleParagraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun = groupTitleParagraph.createRun();

//                groupTitleRun.setText(key);
                String[] lines = key.split("ooo");
                for (int i = 0; i < lines.length; i++) {
                    groupTitleRun.setText(lines[i], i);
                    if (i < lines.length - 1) {
                        CTBr br = groupTitleRun.getCTR().addNewBr();
                        br.setType(STBrType.TEXT_WRAPPING);
                    }
                }

                groupTitleRun.setFontSize(14);
                groupTitleRun.setBold(true);

                // 设置页面宽度（假设为100%）
                CTTblWidth tableWidth = CTTblWidth.Factory.newInstance();
                tableWidth.setType(STTblWidth.PCT);
                tableWidth.setW(BigInteger.valueOf(5000));

                // 创建表格
                XWPFTable table = document.createTable(sortedList.size() + 1, type2.equals("1") ? 6 : 5); // 表头+数据行数
                table.getCTTbl().addNewTblPr().setTblW(tableWidth);

                int columnCount = type2.equals("1") ? 6 : 5;
                for (int col = 0; col < columnCount; col++) {
                    CTTblWidth cellWidth = CTTblWidth.Factory.newInstance();
                    cellWidth.setType(STTblWidth.DXA);
                    int w = 2000;
                    if (type2.equals("1")) {
                        switch (col) {
                            case 0:
                                w = 800;
                                break;
                            case 1:
                                w = 1500;
                                break;
                            case 2:
                                w = 800;
                                break;
                            case 3:
                                w = 1300;
                                break;
                            case 4:
                                w = 3000;
                                break;
                            case 5:
                                w = 800;
                                break;
                        }
                    } else {
                        switch (col) {
                            case 0:
                                w = 800;
                                break;
                            case 1:
                                w = 800;
                                break;
                            case 2:
                                w = 1300;
                                break;
                            case 3:
                                w = 4500;
                                break;
                            case 4:
                                w = 800;
                                break;
                        }
                    }

                    cellWidth.setW(BigInteger.valueOf(w)); // 2000 对应于大约25%

                    for (XWPFTableRow row : table.getRows()) {

                        XWPFTableCell cell = row.getCell(col);
                        if (cell.getCTTc().getTcPr() == null) {
                            cell.getCTTc().addNewTcPr();
                        }
                        cell.getCTTc().getTcPr().setTcW(cellWidth);
//                        row.setHeight(200);
                    }
                }

                // 设置表头
                XWPFTableRow headerRow = table.getRow(0);
                headerRow.setHeight(500);
                String[] headers = null;
                if (type2.equals("1")) {
                    headers = new String[]{"上场序号", "预计时间", "选手编号", "选手", "代表队", "备注"};
                } else {
                    headers = new String[]{"上场序号", "选手编号", "选手", "备注", "得分"};
                }

                for (int i = 0; i < headers.length; i++) {

                    if (headerRow.getCell(i) == null) {
                        headerRow.createCell();
                    }

                    XWPFTableCell cell = headerRow.getCell(i);
                    XWPFParagraph p = cell.getParagraphs().get(0);
                    if (p == null) {
                        p = cell.addParagraph();
                    }
                    p.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = p.createRun();
                    run.setText(headers[i]);
                    run.setBold(true);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                }

                // 添加记录到表格中，并设置单元格内容居中
                int rowIndex = 1;
                for (JwSignRecord record : sortedList) {
                    XWPFTableRow dataRow = table.getRow(rowIndex++);
                    dataRow.setHeight(500);
                    String h = StringUtils.isNotEmpty(record.getWorksName()) ? record.getWorksName() + "\\n" : "";
                    String[] rowContents = {
                            String.valueOf(record.getIndexOrder()),
                            DateUtils.parseDateToStr("MM月dd日 HH:mm", record.getIndexTime()),
                            String.valueOf(StringUtils.isNotNull(record.getBackNumber()) ? record.getBackNumber() : "-"),
                            record.getJwSignRecordSportList().stream().sorted(Comparator.comparing(JwSignRecordSport::getPlayerName)).map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")),
                            type2.equals("1") ? record.getTeamName() : "",
                            ""};
                    if (type2.equals("1")) {
                        rowContents = new String[]{
                                String.valueOf(record.getIndexOrder()),
                                DateUtils.parseDateToStr("MM月dd日 HH:mm", record.getIndexTime()),
                                String.valueOf(StringUtils.isNotNull(record.getBackNumber()) ? record.getBackNumber() : "-"),
                                record.getJwSignRecordSportList().stream().sorted(Comparator.comparing(JwSignRecordSport::getPlayerName)).map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")),
                                record.getTeamName(),
                                ""};
                    } else {
                        rowContents = new String[]{
                                String.valueOf(record.getIndexOrder()),
                                String.valueOf(StringUtils.isNotNull(record.getBackNumber()) ? record.getBackNumber() : "-"),
                                record.getJwSignRecordSportList().stream().sorted(Comparator.comparing(JwSignRecordSport::getPlayerName)).map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")),
                                "",
                                ""};
                    }

                    for (int i = 0; i < rowContents.length; i++) {
                        XWPFTableCell cell = dataRow.getCell(i);

                        XWPFParagraph p = cell.getParagraphs().get(0);
                        if (p == null) {
                            p = cell.addParagraph();
                        }

                        p.setAlignment(ParagraphAlignment.CENTER);

                        // 插入作品的名字
//                        if (i == 3 && StringUtils.isNotEmpty(record.getWorksName())) {
//
//                            XWPFRun run1 = p.createRun();
//                            run1.setBold(true);
//                            run1.setText(record.getWorksName());
//                            // 添加断行符以在同一段落中换行
//                            run1.addBreak(); // 插入断行符
//
//                        }

                        XWPFRun run = p.createRun();
                        run.setText(rowContents[i]);
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    }
                }
                // 创建分组标题  中间空行
                XWPFParagraph groupTitleParagraph2 = document.createParagraph();
                groupTitleParagraph2.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun2 = groupTitleParagraph2.createRun();
                groupTitleRun2.setText("");
                groupTitleRun2.setFontSize(34);
                groupTitleRun2.setBold(true);
                XWPFParagraph groupTitleParagraph3 = document.createParagraph();
                groupTitleParagraph3.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun3 = groupTitleParagraph3.createRun();
                groupTitleRun3.setText("");
                groupTitleRun3.setFontSize(34);
                groupTitleRun3.setBold(true);
            }
        }

        // 调用方法来设置整个文档的页边距
        setPageMargins(document, 720, 720, 360, 360);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        document.write(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] generateFeeWord(List<JwSignRecord> jwSignRecordList, JwMatch jwMatch, JwTeam jwTeam) throws IOException {

        // 人数
        Long sportNum = SpringUtils.getBean(JwSignRecordService.class).selectJwSignRecordSportNum(jwTeam.getId(), jwMatch.getId());

        // 人次
        Long sportTime = SpringUtils.getBean(JwSignRecordService.class).selectJwSignRecordSportTime(jwTeam.getId(), jwMatch.getId());


        XWPFDocument document = new XWPFDocument();

        // 创建标题
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun1 = titleParagraph.createRun();
        titleRun1.setText(jwMatch.getMatchName());
        titleRun1.setFontSize(18);
        titleRun1.setBold(true);
        titleRun1.addBreak();

        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("收费通知单");
        titleRun.setFontSize(16);
        titleRun.setBold(true);
        titleRun.addBreak();

        if (jwTeam != null) {
            XWPFRun aaa22 = titleParagraph.createRun();
            aaa22.setText(jwTeam.getTeamName());
            aaa22.setFontSize(16);
            aaa22.setBold(true);
            aaa22.addBreak();
        }

        if (jwSignRecordList != null && jwSignRecordList.size() > 0) {

            // 按照上场序号排序
            List<JwSignRecord> sortedList = jwSignRecordList.stream().sorted(Comparator.comparing(JwSignRecord::getSportLimit)).collect(Collectors.toList());

            // 设置页面宽度（假设为100%）
            CTTblWidth tableWidth = CTTblWidth.Factory.newInstance();
            tableWidth.setType(STTblWidth.PCT);
            tableWidth.setW(BigInteger.valueOf(5000));

            // 创建表格
            XWPFTable table = document.createTable(sortedList.size() + 1, 5); // 表头+数据行数
            table.getCTTbl().addNewTblPr().setTblW(tableWidth);

            int columnCount = 5;
            for (int col = 0; col < columnCount; col++) {
                CTTblWidth cellWidth = CTTblWidth.Factory.newInstance();
                cellWidth.setType(STTblWidth.DXA);
                int w = 2000;
                switch (col) {
                    case 0:
                        w = 750;
                        break;
                    case 1:
                        w = 750;
                        break;
                    case 2:
                        w = 2000;
                        break;
                    case 3:
                        w = 4000;
                        break;
                    case 4:
                        w = 1000;
                        break;
                }
                cellWidth.setW(BigInteger.valueOf(w)); // 2000 对应于大约2%

                for (XWPFTableRow row : table.getRows()) {
                    XWPFTableCell cell = row.getCell(col);
                    if (cell.getCTTc().getTcPr() == null) {
                        cell.getCTTc().addNewTcPr();
                    }
                    cell.getCTTc().getTcPr().setTcW(cellWidth);
                }
            }

            // 设置表头
            XWPFTableRow headerRow = table.getRow(0);
            String[] headers = {"序号", "背号", "选手", "组别", "报名费"};
            for (int i = 0; i < headers.length; i++) {

                if (headerRow.getCell(i) == null) {
                    headerRow.createCell();
                }

                XWPFTableCell cell = headerRow.getCell(i);
                XWPFParagraph p = cell.getParagraphs().get(0);
                if (p == null) {
                    p = cell.addParagraph();
                }
                p.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun run = p.createRun();
                run.setText(headers[i]);
                run.setBold(true);
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            }

            // 添加记录到表格中，并设置单元格内容居中
            int rowIndex = 1;
            BigDecimal allFee = new BigDecimal("0");
            for (JwSignRecord record : sortedList) {
                XWPFTableRow dataRow = table.getRow(rowIndex++);
                String[] rowContents = {
                        String.valueOf(rowIndex - 1),
                        (StringUtils.isNotNull(record.getBackNumber()) ? record.getBackNumber() : "-"),
                        record.getJwSignRecordSportList().stream().sorted(Comparator.comparing(JwSignRecordSport::getPlayerName)).map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")),
                        record.getJwGameItem().getCode() + ":" + record.getJwGameItem().getName(),
                        record.getFee().toPlainString() + (record.getSportLimit().equals("1") ? "" : "(" + record.getAvgFee().toPlainString() + "/人)")
                };
                for (int i = 0; i < rowContents.length; i++) {
                    XWPFTableCell cell = dataRow.getCell(i);
                    XWPFParagraph p = cell.getParagraphs().get(0);
                    if (p == null) {
                        p = cell.addParagraph();
                    }
                    p.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = p.createRun();
                    run.setText(rowContents[i]);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                }
                if (BigDecimalUtil.isNotNull(record.getFee())) {
                    allFee = allFee.add(record.getFee());
                }
            }

            // 添加最后一行，并合并第一到第三列
            XWPFTableRow newRow = table.createRow();
            // 填充第一格内容
            XWPFTableCell cell = newRow.getCell(0);
            cell.getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);

            // 创建 Run 并设置样式
            XWPFRun run = cell.getParagraphs().get(0).createRun();
            run.setBold(true);
            run.setFontSize(13);
            run.setText("人数：" + sportNum + "      人次：" + sportTime + "      费用总计：" + allFee.toPlainString());
//            run.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            // 合并第1行的第1到第3个单元格（从索引0开始）
            mergeCellsHorizontal(newRow, 0, 4); // 合并从第0个单元格到第2个
            newRow.removeCell(1);
            newRow.removeCell(1);
            newRow.removeCell(1);
            newRow.removeCell(1);
        }

        // 调用方法来设置整个文档的页边距
        setPageMargins(document, 720, 720, 720, 720);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        document.write(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static void setPageMargins(XWPFDocument document, int top, int bottom, int left, int right) {

        // 获取文档主体中的默认节属性
        CTSectPr sectPr = document.getDocument().getBody().isSetSectPr() ?
                document.getDocument().getBody().getSectPr() :
                document.getDocument().getBody().addNewSectPr();

        // 设置页边距 (单位是twips - 千分之一英寸)
        CTPageMar pageMar = sectPr.isSetPgMar() ? sectPr.getPgMar() : sectPr.addNewPgMar();
        pageMar.setTop(top);   // 例如1440 twips = 1 inch
        pageMar.setBottom(bottom);
        pageMar.setLeft(left);
        pageMar.setRight(right);
    }

    public static void mergeCellsHorizontal(XWPFTableRow row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            if (cellIndex == fromCell) {
                // 设置第一个单元格为合并起点
                CTTc ctTc = row.getCell(cellIndex).getCTTc();
                CTTcPr ctTcPr = ctTc.isSetTcPr() ? ctTc.getTcPr() : ctTc.addNewTcPr();
                CTDecimalNumber gridSpan = ctTcPr.isSetGridSpan() ? ctTcPr.getGridSpan() : ctTcPr.addNewGridSpan();
                gridSpan.setVal(BigInteger.valueOf(toCell - fromCell + 1));
            } else {
                // 其他单元格设置为空，并隐藏边框
//                row.removeCell(cellIndex);
//                row.getCell(cellIndex).setText("");
//                CTTc ctTc = row.getCell(cellIndex).getCTTc();
//                CTTcPr ctTcPr = ctTc.isSetTcPr() ? ctTc.getTcPr() : ctTc.addNewTcPr();
//                ctTcPr.addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
}