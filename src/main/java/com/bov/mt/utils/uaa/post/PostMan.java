package com.bov.mt.utils.uaa.post;

import com.bov.mt.entity.OAuthHeader;
import com.bov.mt.entity.result.HttpClientResult;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

//发送请求工具类
@Component
public class PostMan {

    private Logger logger = LoggerFactory.getLogger(PostMan.class);

    public Optional<HttpClientResult> getMethod(String url, OAuthHeader headers){
        HttpClientResult result = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        //设置响应头
        if (headers.isHasSetContentType()) {
            httpGet.setHeader("Content-Type",headers.getContentType());
        }
        if (headers.isHasSetAccept()) {
            httpGet.setHeader("Accept",headers.getAccept());
        }
        if (headers.isHasSetAuthorization()) {
            httpGet.setHeader("Authorization","Bearer "+headers.getAuthorization());
        }
        try {
            response = client.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            String content  = EntityUtils.toString(response.getEntity(),"utf-8");
            result = new HttpClientResult();
            result.setCode(code);
            result.setContent(content);
        }catch (IOException e){
            logger.debug("=========发送GET请求失败========");
            logger.debug("==========="+e.getMessage()+"=======================");
        }finally {
            close(client,response);
        }
        return Optional.ofNullable(result);
    }

    public Optional<HttpClientResult> postMethod(String url, OAuthHeader headers, JSONObject args){
        HttpClientResult result = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        //设置响应头
        if (headers.isHasSetContentType()) {
            httpPost.setHeader("Content-Type",headers.getContentType());
        }
        if (headers.isHasSetAccept()) {
            httpPost.setHeader("Accept",headers.getAccept());
        }
        if (headers.isHasSetAuthorization()) {
            httpPost.setHeader("Authorization","Bearer "+headers.getAuthorization());
        }
        //设置参数
        StringEntity params = new StringEntity(args.toString(),"utf-8");
        httpPost.setEntity(params);
        try {
            response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            String content  = EntityUtils.toString(response.getEntity(),"utf-8");
            result = new HttpClientResult();
            result.setCode(code);
            result.setContent(content);
        }catch (IOException e){
            logger.debug("=========发送GET请求失败========");
            logger.debug("==========="+e.getMessage()+"=======================");
        }finally {
            close(client,response);
        }
        return Optional.ofNullable(result);
    }

    public Optional<HttpClientResult> postWithFile(String url, OAuthHeader headers, Map<String,String> texts, Map<String,File> files){
        HttpClientResult result = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        //设置响应头
        if (headers.isHasSetContentType()) {
            httpPost.setHeader("Content-Type",headers.getContentType());
        }
        if (headers.isHasSetAccept()) {
            httpPost.setHeader("Accept",headers.getAccept());
        }
        if (headers.isHasSetAuthorization()) {
            httpPost.setHeader("Authorization","Bearer "+headers.getAuthorization());
        }
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        //设置普通字段属性
        for (String key : texts.keySet()) {
            multipartEntityBuilder.addTextBody(key,texts.get(key), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));
        }
        //设置文件
        for (String key : files.keySet()) {
            multipartEntityBuilder.addBinaryBody(key,files.get(key));
        }
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);
        try {
            response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            String content  = EntityUtils.toString(response.getEntity(),"utf-8");
            result = new HttpClientResult();
            result.setCode(code);
            result.setContent(content);
        }catch (IOException e) {
            logger.debug("=========发送GET请求失败========");
            logger.debug("==========="+e.getMessage()+"=======================");
        }finally {
            close(client,response);
        }
        return Optional.ofNullable(result);
    }

    public byte[] getImage(String token,String imageURL){
        byte[] image = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(imageURL);
        httpGet.setHeader("Authorization","Bearer "+token);
        try {
            response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                image = EntityUtils.toByteArray(response.getEntity());
            }
        }catch (IOException e) {
            logger.debug("=========发送GET请求失败========");
            logger.debug("==========="+e.getMessage()+"=======================");
        }finally {
            close(client,response);
        }
        return image;
    }

    private void close(CloseableHttpClient client,CloseableHttpResponse response){
        if (response != null) {
            try {
                response.close();
            }catch (IOException e) {
                logger.debug("===========关闭response失败=======================");
            }
        }
        if (client != null) {
            try {
                client.close();
            }catch (IOException e) {
                logger.debug("===========关闭client失败=======================");
            }
        }
    }
}
