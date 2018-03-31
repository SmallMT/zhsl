package com.bov.mt.entity.vm;

import com.bov.mt.constant.RegexString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterUserVM {
    @NotBlank
    @Pattern(regexp = RegexString.IDCARD_REGEX , message = "身份证不合法")
    private String username;

    @NotBlank
    @Pattern(regexp = RegexString.PASSWORD_REGEX, message = "密码不合法")
    private String password;

    @NotBlank
    private String repassword;

    @NotBlank
    @Pattern(regexp = RegexString.PHONE_REGEX,message = "手机号码不正确")
    private String phone;

    @NotBlank
    @Size(min = 6 , max = 6,message = "请填入正确的6位验证码")
    private String code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
