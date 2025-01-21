package com.ruoyi.common.utils.file;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.domain.JwSignRecord;
import com.ruoyi.project.jiewu.domain.JwSignRecordSport;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import java.io.*;
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

    public static byte[] generateWord(List<JwSignRecord> records, String matchName) throws IOException {
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

        XWPFRun aaa = titleParagraph.createRun();
        aaa.setText("");
        aaa.setFontSize(16);
        aaa.setBold(true);
        aaa.addBreak();
        XWPFRun aaa22 = titleParagraph.createRun();
        aaa22.setText("");
        aaa22.setFontSize(16);
        aaa22.setBold(true);
        aaa22.addBreak();


        Map<String, List<JwSignRecord>> groupedRecords = records.stream().collect(Collectors.groupingBy(jws -> "【第" + jws.getPlaceOrder() + "场 "
                + DateUtils.parseDateToStr("HH:mm", jws.getPlaceTime())
                + "】    " + jws.getItemName()
                + "【" + DictUtils.getDictLabel("jw_area", jws.getJwScheduleItem().getArea()) + "】"
        ));

//        Map<String, List<JwSignRecord>> groupedRecords = records.stream().collect(Collectors.groupingBy(
//                jws -> "【第" + jws.getPlaceOrder() + "场 "
//                        + DateUtils.parseDateToStr("HH:mm", jws.getPlaceTime())
//                        + "】    " + jws.getItemName()
//                        + "【" + DictUtils.getDictLabel("jw_area", jws.getJwScheduleItem().getArea()) + "】",
//                () -> new TreeMap<>(customOrderComparator), // 提取数字部分进行比较
//                Collectors.toList()
//        ));

        String[] keyS = groupedRecords.keySet().toArray(new String[0]);

        Arrays.sort(keyS, (s1, s2) -> {
            int sessionComparison = Integer.compare(getSessionNumber(s1), getSessionNumber(s2));
            if (sessionComparison != 0) {
                return sessionComparison;
            }
            return Character.compare(getVenue(s1), getVenue(s2));
        });


        // 添加每个分组的数据为表格
        for (String key : keyS) {
            List<JwSignRecord> jwSignRecordList = groupedRecords.get(key);
            if (jwSignRecordList != null && jwSignRecordList.size() > 0) {


                // 按照上场序号排序
                List<JwSignRecord> sortedList = jwSignRecordList.stream().sorted(Comparator.comparingLong(JwSignRecord::getIndexOrder)).collect(Collectors.toList());

                // 创建分组标题
                XWPFParagraph groupTitleParagraph = document.createParagraph();
                groupTitleParagraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun groupTitleRun = groupTitleParagraph.createRun();
                groupTitleRun.setText(key);
                groupTitleRun.setFontSize(14);
                groupTitleRun.setBold(true);

                // 设置页面宽度（假设为100%）
                CTTblWidth tableWidth = CTTblWidth.Factory.newInstance();
                tableWidth.setType(STTblWidth.PCT);
                tableWidth.setW(BigInteger.valueOf(5000));

                // 创建表格
                XWPFTable table = document.createTable(sortedList.size() + 1, 4); // 表头+数据行数
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
                            w = 1000;
                            break;
                        case 2:
                            w = 4000;
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
                String[] headers = {"上场序号", "背号", "选手", "代表队"};
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
                    String h = StringUtils.isNotEmpty(record.getWorksName()) ? record.getWorksName() + "\\n" : "";
                    String[] rowContents = {
                            String.valueOf(record.getIndexOrder()),
                            String.valueOf(record.getBackNumber()),
                            record.getJwSignRecordSportList().stream().sorted(Comparator.comparing(JwSignRecordSport::getPlayerName)).map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(" ")),
                            record.getTeamName()
                    };

                    for (int i = 0; i < rowContents.length; i++) {
                        XWPFTableCell cell = dataRow.getCell(i);


                        XWPFParagraph p = cell.getParagraphs().get(0);
                        if (p == null) {
                            p = cell.addParagraph();
                        }

                        p.setAlignment(ParagraphAlignment.CENTER);

                        // 插入作品的名字
                        if (i == 2 && StringUtils.isNotEmpty(record.getWorksName())) {

                            XWPFRun run1 = p.createRun();
                            run1.setBold(true);
                            run1.setText(record.getWorksName());
                            // 添加断行符以在同一段落中换行
                            run1.addBreak(); // 插入断行符

                        }

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
}