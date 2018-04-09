package com.bov.mt.utils;

import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class LangChaoService {

    private static String LC_SAVEDATA = "http://localhost:9090/saveData";

    public String saveData(String formId,JSONObject data){
        JSONObject params = new JSONObject();
        params.put("formId",formId);
        params.put("formData",data.toString());
        return JSONObject.fromObject(post(LC_SAVEDATA,params)).getString("dataId");
    }

    private String get(String url){

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet();
        return "";
    }

    private String post(String url, JSONObject args){
        String result = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(args.toString(),"utf-8");
        post.setEntity(entity);
        try{
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
