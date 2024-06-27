package com.ruoyi.common.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {

    public static Boolean isNotNull(BigDecimal v) {

        return v != null && v.compareTo(new BigDecimal(0)) > 0;
    }

    public static Boolean isEqual(BigDecimal v1, BigDecimal v2) {
        BigDecimal decimal1 = new BigDecimal("0");
        BigDecimal decimal2 = new BigDecimal("0");

        if(isNotNull(v1)) decimal1 = v1;

        if(isNotNull(v2)) decimal2 = v2;

        return decimal1.compareTo(decimal2) == 0;
    }
}
