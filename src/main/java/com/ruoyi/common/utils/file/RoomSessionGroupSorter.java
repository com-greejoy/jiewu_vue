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

        // 正则1：提取教室部分，捕获 "第(.*?)教室" 中的中文数字部分
        private static final Pattern ROOM_PATTERN = Pattern.compile("第(.*?)场地");

        // 正则2：提取场次和组别，捕获 "第(\\d+)场" 和 "第(\\d+)组" 中的阿拉伯数字
        private static final Pattern DETAIL_PATTERN = Pattern.compile(".*?第(\\d+)场.*?第(\\d+)组");

        @Override
        public int compare(String s1, String s2) {
            Info info1 = parse(s1);
            Info info2 = parse(s2);

            // 防御性处理：如果解析失败，按原字符串字典序排列，避免崩溃
            if (info1 == null || info2 == null) {
                return s1.compareTo(s2);
            }

            // 1. 第一关键字：教室序号 (数字比较：1 < 2 < 3 < 4)
            // 这里解决了 "第二" 排在 "第三" 后面的问题
            int roomCompare = Integer.compare(info1.roomIndex, info2.roomIndex);
            if (roomCompare != 0) {
                return roomCompare;
            }

            // 2. 第二关键字：场次 (数字比较：1 < 2)
            int sessionCompare = Integer.compare(info1.sessionIndex, info2.sessionIndex);
            if (sessionCompare != 0) {
                return sessionCompare;
            }

            // 3. 第三关键字：组别 (数字比较：1 < 2 < 3)
            return Integer.compare(info1.groupIndex, info2.groupIndex);
        }

        /**
         * 解析字符串，提取三个关键数字指标
         */
        private Info parse(String str) {
            // 提取教室序号
            Matcher mRoom = ROOM_PATTERN.matcher(str);
            int roomIdx = 0;
            if (mRoom.find()) {
                String chineseNum = mRoom.group(1);
                roomIdx = convertChineseToArabic(chineseNum);
            }

            // 提取场次和组别
            Matcher mDetail = DETAIL_PATTERN.matcher(str);
            int sessionIdx = 0;
            int groupIdx = 0;

            if (mDetail.find()) {
                try {
                    sessionIdx = Integer.parseInt(mDetail.group(1));
                    groupIdx = Integer.parseInt(mDetail.group(2));
                } catch (NumberFormatException e) {
                    // 如果数字格式异常，返回0或保持默认
                }
            }

            return new Info(roomIdx, sessionIdx, groupIdx);
        }

        /**
         * 中文数字转阿拉伯数字工具方法
         * 支持：一, 二, 三, 四, 五, 六, 七, 八, 九, 十
         * 如果传入的已经是阿拉伯数字字符串 (如 "1"), 也能兼容处理
         */
        private int convertChineseToArabic(String cn) {
            if (cn == null || cn.isEmpty()) return 0;

            // 尝试直接解析是否为阿拉伯数字 (兼容混用情况)
            try {
                return Integer.parseInt(cn);
            } catch (NumberFormatException e) {
                // 不是阿拉伯数字，则按中文处理
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
                // 如需支持 "十一", "十二" 等，可在此扩展逻辑
                default:
                    // 未知格式默认返回 0 (排在最前) 或 999 (排在最后)，视需求定
                    // 这里为了安全，返回一个较大的数，防止乱插队，或者返回0
                    return 0;
            }
        }
    }

    /**
     * 内部数据载体类，存储解析后的三个整数指标
     */
    private static class Info {
        int roomIndex;    // 教室序号 (1, 2, 3...)
        int sessionIndex; // 场次序号 (1, 2...)
        int groupIndex;   // 组别序号 (1, 2, 3...)

        public Info(int roomIndex, int sessionIndex, int groupIndex) {
            this.roomIndex = roomIndex;
            this.sessionIndex = sessionIndex;
            this.groupIndex = groupIndex;
        }
    }
}