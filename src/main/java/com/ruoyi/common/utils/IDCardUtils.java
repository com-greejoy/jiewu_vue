package com.ruoyi.common.utils;

import java.util.Calendar;

public class IDCardUtils {

    private static final String[] CHECK_INDEX = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static final int SECOND_ID_CARD_CHECK_MOD = 11;

    private static final String SECOND_ID_CARD_REGULAR_EXP = "(^\\d{18}$)|(^\\d{17}(\\d|X|x)$|(^\\d{15}$))";
    private static final int SECOND_ID_CARD_LENGTH = 18;

    public static boolean isIdCard(String idCard){
        if (StringUtils.isBlank(idCard)){
            return false;
        }
        StringBuilder idCardBuilder = new StringBuilder(idCard);
        // 正则表达式
        boolean matches = idCard.matches(SECOND_ID_CARD_REGULAR_EXP);
        // 第二代身份证的验证
        if (matches && idCardBuilder.length() == SECOND_ID_CARD_LENGTH){
            int index = getIdCardCheckIndex(idCardBuilder);
            // 获取传入身份证的检验码
            String check = String.valueOf(idCardBuilder.charAt(idCardBuilder.length()-1));
            // 检验码校验
            return StringUtils.equalsIgnoreCase(CHECK_INDEX[index], check);
        }
        return matches;
    }

    private static int getIdCardCheckIndex(StringBuilder idCardBuilder){
        // 判断传入的是17位还是18位身份证号
        int length = idCardBuilder.length() == SECOND_ID_CARD_LENGTH ? idCardBuilder.length()-1 : idCardBuilder.length();
        // 计算出校验码
        int sum = 0;
        // length=17, i=0、2、3...16
        for (int i = 0; i < length; i++) {
            // 前17位数字
            int numVal = Integer.parseInt(String.valueOf(idCardBuilder.charAt(i)));
            int numMultiple = (int) (Math.pow(2, length - i) % SECOND_ID_CARD_CHECK_MOD);
            sum += (numVal * numMultiple);
        }
        // 总和取模11
        return sum % SECOND_ID_CARD_CHECK_MOD;
    }


    public static Long getAge(String idCard) {
        String birth = idCard.substring(6, 14);
        int year = Integer.parseInt(birth.substring(0, 4));
        int month = Integer.parseInt(birth.substring(4, 6));
        int day = Integer.parseInt(birth.substring(6, 8));

        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curDay = cal.get(Calendar.DAY_OF_MONTH);

        int age = curYear - year;
        if (curMonth < month || (curMonth == month && curDay < day)) {
            age--;
        }

        return Long.valueOf(age);
    }

    public static String getGender(String idCard) {
        int genderNum = Integer.parseInt(idCard.substring(idCard.length() - 2, idCard.length() - 1));
        return genderNum % 2 == 0 ? "f" : "m";
    }
}
