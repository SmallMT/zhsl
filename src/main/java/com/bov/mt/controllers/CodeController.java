package com.bov.mt.controllers;

import com.bov.mt.utils.MessageCode;
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

    @GetMapping("getcode")
    public void getCode(@RequestParam("phone") String phone){
        messageCode.smsCode(phone);
    }

    @GetMapping("verifycode")
    public boolean verifyCode(@RequestParam("phone") String phone,
                           @RequestParam("code") String code){
        boolean flag = messageCode.verifyCode(phone,code);
        System.out.println(flag);
        return flag;
    }
}
