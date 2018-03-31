package com.bov.mt.entity.vm;

import com.bov.mt.constant.RegexString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class BindCompanyVM {

    @NotBlank(message = "统一社会信用代码不能为空")
    private String companyCode;

    @NotBlank(message = "企业名称不能为空")
    private String companyName;

    @NotBlank(message = "法定代表人姓名不能为空")
    private String legalPersonName;

    @NotBlank(message = "法定代表人身份证不能为空")
    @Pattern(regexp = RegexString.IDCARD_REGEX , message = "身份证格式不正确")
    private String legalPersonID;

    @NotBlank(message = "法定代表人手机号不能为空")
    @Pattern(regexp = RegexString.PHONE_REGEX , message = "手机号码格式不正确")
    private String legalPersonPhone;

    @NotBlank(message = "公司注册地址不能为空")
    private String companyAddress;

    @NotNull(message = "企业营业执照不能为空")
    private MultipartFile businessLicence;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getLegalPersonID() {
        return legalPersonID;
    }

    public void setLegalPersonID(String legalPersonID) {
        this.legalPersonID = legalPersonID;
    }

    public String getLegalPersonPhone() {
        return legalPersonPhone;
    }

    public void setLegalPersonPhone(String legalPersonPhone) {
        this.legalPersonPhone = legalPersonPhone;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public MultipartFile getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(MultipartFile businessLicence) {
        this.businessLicence = businessLicence;
    }
}
