package com.bov.mt.entity.vm;

import com.bov.mt.constant.RegexString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AuthUserVM {
    @NotBlank(message = "真实姓名不能为空")
    private String trueName;
    @Pattern(regexp = RegexString.IDCARD_REGEX,message = "身份证格式错误")
    private String trueID;
    @NotNull(message = "请上传身份证正面照")
    private MultipartFile idCardFront;
    @NotNull(message = "请上传身份证反面照")
    private MultipartFile idCardBack;

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getTrueID() {
        return trueID;
    }

    public void setTrueID(String trueID) {
        this.trueID = trueID;
    }

    public MultipartFile getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(MultipartFile idCardFront) {
        this.idCardFront = idCardFront;
    }

    public MultipartFile getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(MultipartFile idCardBack) {
        this.idCardBack = idCardBack;
    }
}
