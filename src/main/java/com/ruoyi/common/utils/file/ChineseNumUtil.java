package com.ruoyi.common.utils.file;


import java.util.HashMap;
import java.util.Map;

public class ChineseNumUtil {
    private static final Map<Character, Integer> chineseNumMap = new HashMap<>();

    static {
        chineseNumMap.put('零', 0);
        chineseNumMap.put('一', 1);
        chineseNumMap.put('二', 2);
        chineseNumMap.put('三', 3);
        chineseNumMap.put('四', 4);
        chineseNumMap.put('五', 5);
        chineseNumMap.put('六', 6);
        chineseNumMap.put('七', 7);
        chineseNumMap.put('八', 8);
        chineseNumMap.put('九', 9);
        chineseNumMap.put('十', 10);
    }

    public static int chineseToArabic(String chineseNum) {
        if (chineseNum == null || chineseNum.isEmpty()) {
            return 0;
        }

        if (chineseNum.equals("十一")) return 11;
        if (chineseNum.equals("十二")) return 12;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;
        if (chineseNum.equals("十三")) return 13;

        // 可以扩展更多，或者使用更复杂的中文数字解析逻辑

        int result = 0;
        int temp = 0;

        for (int i = 0; i < chineseNum.length(); i++) {
            char c = chineseNum.charAt(i);
            Integer value = chineseNumMap.get(c);

            if (value == null) continue;

            if (value == 10) {
                if (temp == 0) temp = 1;
                result += temp * value;
                temp = 0;
            } else {
                temp += value;
            }
        }

        result += temp;
        return result;
    }
}

