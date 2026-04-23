package com.ruoyi.common.utils.file;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomSessionGroupSorter {

    public static void sort(String[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        Arrays.sort(array, new RoomInfoComparator());
    }

    /**
     * 辅助打印方法
     */
    private static void printArray(String[] arr) {
        for (String s : arr) {
            System.out.println(s);
        }
    }

    static class RoomInfoComparator implements Comparator<String> {

        // 正则1：匹配开头的日期，如 "5月1日"
        private static final Pattern DATE_PATTERN = Pattern.compile("^(\\d{1,2})月(\\d{1,2})日");

        // 正则2：提取场地中的中文数字，如 "第三场地" -> "三"
        private static final Pattern ROOM_PATTERN = Pattern.compile("第(.*?)场地");

        // 正则3：提取场次和组别，如 "第1场 - 第3组"
        private static final Pattern DETAIL_PATTERN = Pattern.compile("第(\\d+)场.*?第(\\d+)组");

        @Override
        public int compare(String s1, String s2) {
            Info info1 = parse(s1);
            Info info2 = parse(s2);

            if (info1 == null || info2 == null) {
                return s1.compareTo(s2);
            }

            // 1. 按日期排序 (month * 100 + day)
            int dateCompare = Integer.compare(info1.dateKey, info2.dateKey);
            if (dateCompare != 0) {
                return dateCompare;
            }

            // 2. 按场地序号排序
            int roomCompare = Integer.compare(info1.roomIndex, info2.roomIndex);
            if (roomCompare != 0) {
                return roomCompare;
            }

            // 3. 按场次排序
            int sessionCompare = Integer.compare(info1.sessionIndex, info2.sessionIndex);
            if (sessionCompare != 0) {
                return sessionCompare;
            }

            // 4. 按组别排序
            return Integer.compare(info1.groupIndex, info2.groupIndex);
        }

        private Info parse(String str) {
            // 1. 解析日期
            int dateKey = 0;
            Matcher mDate = DATE_PATTERN.matcher(str);
            if (mDate.find()) {
                try {
                    int month = Integer.parseInt(mDate.group(1));
                    int day = Integer.parseInt(mDate.group(2));
                    dateKey = month * 100 + day; // 如 5月1日 -> 501
                } catch (NumberFormatException e) {
                    // ignore, keep dateKey = 0
                }
            }

            // 2. 解析场地（中文数字）
            int roomIdx = 0;
            Matcher mRoom = ROOM_PATTERN.matcher(str);
            if (mRoom.find()) {
                String chineseNum = mRoom.group(1);
                roomIdx = convertChineseToArabic(chineseNum);
            }

            // 3. 解析场次和组别
            int sessionIdx = 0;
            int groupIdx = 0;
            Matcher mDetail = DETAIL_PATTERN.matcher(str);
            if (mDetail.find()) {
                try {
                    sessionIdx = Integer.parseInt(mDetail.group(1));
                    groupIdx = Integer.parseInt(mDetail.group(2));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }

            return new Info(dateKey, roomIdx, sessionIdx, groupIdx);
        }

        /**
         * 中文数字转阿拉伯数字（支持一~十，以及直接阿拉伯数字）
         */
        private int convertChineseToArabic(String cn) {
            if (cn == null || cn.isEmpty()) return 0;

            // 先尝试直接解析为数字（兼容“1”这种写法）
            try {
                return Integer.parseInt(cn);
            } catch (NumberFormatException ignored) {
            }

            switch (cn) {
                case "一": return 1;
                case "二": return 2;
                case "三": return 3;
                case "四": return 4;
                case "五": return 5;
                case "六": return 6;
                case "七": return 7;
                case "八": return 8;
                case "九": return 9;
                case "十": return 10;
                case "十一": return 11;
                case "十二": return 12;
                case "十三": return 13;
                case "十四": return 14;
                case "十五": return 15;
                case "十六": return 16;
                case "十七": return 17;
                case "十八": return 18;
                case "十九": return 19;
                case "二十": return 20;
                case "二十一": return 21;
                case "二十二": return 22;
                case "二十三": return 23;
                case "二十四": return 24;
                case "二十五": return 25;
                case "二十六": return 26;
                case "二十七": return 27;
                // 如需支持十一、十二等，可扩展
                default:
                    return 0; // 未知时排前面（或可改为999排后面）
            }
        }
    }

    /**
     * 内部数据载体类
     */
    private static class Info {
        int dateKey;      // 月*100 + 日，如 501
        int roomIndex;    // 场地序号
        int sessionIndex; // 场次
        int groupIndex;   // 组别

        public Info(int dateKey, int roomIndex, int sessionIndex, int groupIndex) {
            this.dateKey = dateKey;
            this.roomIndex = roomIndex;
            this.sessionIndex = sessionIndex;
            this.groupIndex = groupIndex;
        }
    }
}