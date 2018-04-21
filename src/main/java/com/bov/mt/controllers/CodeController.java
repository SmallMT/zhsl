package com.bov.mt.controllers;

import com.bov.mt.utils.MessageCode;
import com.bov.mt.utils.uaa.UaaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code/")
public class CodeController {

    @Autowired
    private MessageCode messageCode;

    @Autowired
    private UaaUtil uaaUtil;

    @GetMapping("getcode")
    public boolean getCode(@RequestParam("phone") String phone){
        //检测手机号是否已经绑定了相关用户
        boolean hasUsed = uaaUtil.checkPhoneHasUsed(phone);
        if (!hasUsed) {
            //手机号没有绑定用户,不发送短信
            return false;
        }
        messageCode.smsCode(phone);
        return true;
    }

    @GetMapping("verifycode")
    public boolean verifyCode(@RequestParam("phone") String phone,
                           @RequestParam("code") String code){
        boolean flag = messageCode.verifyCode(phone,code);
        return flag;
    }
}
