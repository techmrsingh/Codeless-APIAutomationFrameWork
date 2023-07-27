package org.example;

import java.util.regex.Pattern;

public class regExCsvUtil {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern STRING_PATTERN = Pattern.compile("[a-zA-Z]+");

    public static boolean isNumber(String strNum) {
        if (strNum == null) {
            return false;
        }
        return NUMBER_PATTERN.matcher(strNum).matches();
    }

    public static boolean isString(String str) {
        if (str == null) {
            return false;
        }
        return STRING_PATTERN.matcher(str).matches();
    }

}
