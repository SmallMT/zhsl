package com.bov.mt.utils;

import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageCode {

    private Logger logger = LoggerFactory.getLogger(MessageCode.class);
    private static String appId = "iej5bQfs0fnQ1jUiIoCRVNLS-gzGzoHsz";
    private static String appKey = "YjEaXzNDJpha3MRlNleVxuDF";

    //获取短信验证码
    public void smsCode(String phone){
        String url = "https://leancloud.cn/1.1/requestSmsCode";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("mobilePhoneNumber",phone);
        jsonParam.put("template","注册");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-LC-Id",appId);
        httpPost.setHeader("X-LC-Key", appKey);
        httpPost.setHeader("Content-Type","application/json");
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
        httpPost.setEntity(entity);
        try{
            response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.debug("========获取验证码成功=========");
            }
        }catch (IOException e) {
            logger.debug("========获取验证码失败=========");
            logger.debug(e.getMessage());
        }finally {
            if (null != response) {
                try{
                    response.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != client) {
                try{
                    client.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //验证短信验证码
    public boolean verifyCode(String phone,String code){
        String url = "https://leancloud.cn/1.1/verifySmsCode/"+code;
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("mobilePhoneNumber",phone);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-LC-Id",appId);
        httpPost.setHeader("X-LC-Key", appKey);
        httpPost.setHeader("Content-Type","application/json");
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
        httpPost.setEntity(entity);
        boolean flag = false;
        try {
            response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                flag = true;
            }
        }catch (IOException e) {
            logger.debug("============请求失败===============");
        }finally {
            if (null != response) {
                try{
                    response.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != client) {
                try{
                    client.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }
}
