package com.ruoyi.common.utils;

import com.ruoyi.framework.web.domain.server.Sys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;

public class IDCardUtils {

    private static final String[] CHECK_INDEX = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static final int SECOND_ID_CARD_CHECK_MOD = 11;

    private static final String SECOND_ID_CARD_REGULAR_EXP = "(^\\d{18}$)|(^\\d{17}(\\d|X|x)$|(^\\d{15}$))";
    private static final int SECOND_ID_CARD_LENGTH = 18;

    public static boolean isIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        StringBuilder idCardBuilder = new StringBuilder(idCard);
        // 正则表达式
        boolean matches = idCard.matches(SECOND_ID_CARD_REGULAR_EXP);
        // 第二代身份证的验证
        if (matches && idCardBuilder.length() == SECOND_ID_CARD_LENGTH) {
            int index = getIdCardCheckIndex(idCardBuilder);
            // 获取传入身份证的检验码
            String check = String.valueOf(idCardBuilder.charAt(idCardBuilder.length() - 1));
            // 检验码校验
            return StringUtils.equalsIgnoreCase(CHECK_INDEX[index], check);
        }
        return matches;
    }

//    public static void main(String[] args){
//        System.out.println(isIdCard("500231201809040012"));
//
//    }

    private static int getIdCardCheckIndex(StringBuilder idCardBuilder) {
        // 判断传入的是17位还是18位身份证号
        int length = idCardBuilder.length() == SECOND_ID_CARD_LENGTH ? idCardBuilder.length() - 1 : idCardBuilder.length();
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

        return getAgeInYears(idCard);
//        try {
//            if (StringUtils.isNotEmpty(idCard)) idCard = idCard.replaceAll(" ", "");
//            String birth = idCard.substring(6, 14);
//            int year = Integer.parseInt(birth.substring(0, 4));
//            int month = Integer.parseInt(birth.substring(4, 6));
//            int day = Integer.parseInt(birth.substring(6, 8));
//
//            Calendar cal = Calendar.getInstance();
//            int curYear = cal.get(Calendar.YEAR);
//            int curMonth = cal.get(Calendar.MONTH) + 1;
//            int curDay = cal.get(Calendar.DAY_OF_MONTH);
//
//            // 暂时改成一月一号
////            curYear = 2025;
//
//            int age = curYear - year;
//            // 暂时改成一月一号
////            if (curMonth < month || (curMonth == month && curDay < day)) {
////                age--;
////            }
//            return Long.valueOf(age);
//        } catch (Exception e) {
//            return 0l;
//        }
    }

    public static Long getAgeInYears(String idCard) {
        try {
            if (idCard == null) {
                return 0l;
//            throw new IllegalArgumentException("身份证号码不能为 null");
            }

            idCard = idCard.trim();
            LocalDate birthDate;

            if (idCard.length() == 18) {
                // 18位：yyyyMMdd（第7-14位）
                String birthStr = idCard.substring(6, 14);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                birthDate = LocalDate.parse(birthStr, formatter);
            } else if (idCard.length() == 15) {
                // 15位：yyMMdd（第7-12位），默认为19XX年
                String yy = idCard.substring(6, 8);
                String MM = idCard.substring(8, 10);
                String dd = idCard.substring(10, 12);

                // 拼接为 yyyyMMdd，前缀 "19"
                String fullYear = "19" + yy;
                String birthStr = fullYear + MM + dd;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                birthDate = LocalDate.parse(birthStr, formatter);
            } else {
                return 0l;
//            throw new IllegalArgumentException("身份证号码必须是15位或18位");
            }

            // 对齐到各自月份的第一天
            LocalDate birthFirstDay = birthDate.withDayOfMonth(1);
            LocalDate currentFirstDay = LocalDate.now().withDayOfMonth(1);

            // 计算完整月份数
            long monthsBetween = java.time.temporal.ChronoUnit.MONTHS.between(birthFirstDay, currentFirstDay);
            return Long.valueOf((int) (monthsBetween / 12));

        } catch (Exception e) {
            return 0l;
        }
    }


    public static String getGender(String idCard) {
        try {
            if (StringUtils.isNotEmpty(idCard)) idCard = idCard.replaceAll(" ", "");
            int genderNum = Integer.parseInt(idCard.substring(idCard.length() - 2, idCard.length() - 1));
            return genderNum % 2 == 0 ? "f" : "m";
        } catch (Exception e) {
            return "m";
        }
    }


    public static String RandomIdCard() {
        Random random = new Random();
        int[] coefficient = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        String[] idCardNumberArray = new String[18];
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            int number = random.nextInt(10);
            sum += number * coefficient[i];
            idCardNumberArray[i] = String.valueOf(number);
        }
        int remainder = sum % 11;
        String lastNumber = String.valueOf(remainder);
        idCardNumberArray[17] = remainder == 10 ? "X" : lastNumber;
        idCardNumberArray[17] = "A";
        String idCardNumber = String.join("", idCardNumberArray);
        System.out.println("随机生成的身份证号码为：" + idCardNumber);

        return idCardNumber;
    }

}
