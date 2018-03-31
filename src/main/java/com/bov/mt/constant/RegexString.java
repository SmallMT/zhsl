package com.bov.mt.constant;

public class RegexString {

    public static final String IDCARD_REGEX = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    public static final String PHONE_REGEX = "^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\d{8})$";

    public static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,19}$";
}
