package com.bov.mt.utils;

import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class LangChaoService {

    private static String LC_SAVEDATA = "http://localhost:9090/saveData";
    private static String LC_METAIL = "http://localhost:9090/getItemInfo?itemCode=";
    private static String LC_UPFILE = "http://localhost:9090/upfile";
    private static String LC_LOADFILE = "http://localhost:9090/load?docid=";
    private static String LC_DELETEITEM = "http://localhost:9090/deleteitem?dataId=";
    private static String LC_UPDATEITEM = "http://localhost:9090/updateitem";
    @Value("${realName.filePath}")
    private String dirPath;
    private Logger logger = LoggerFactory.getLogger(LangChaoService.class);
    //保存数据到模拟浪潮表单
    public String saveData(String formId,JSONObject data){
        JSONObject params = new JSONObject();
        params.put("formId",formId);
        params.put("formData",data.toString());
        return JSONObject.fromObject(post(LC_SAVEDATA,params)).getString("dataId");
    }

    public String deleteData(String dataId){
        String url = LC_DELETEITEM + dataId;
        return get(url);
    }

    //获取事项材料信息
    public String getMetails(String itemCode){
        String url = LC_METAIL + itemCode;
        return get(url);
    }

    //更新办件到模拟浪潮系统
    public String updateBanJian(String dataId,String data){
        JSONObject params = JSONObject.fromObject(data);
        params.put("dataId",dataId);
        return post(LC_UPDATEITEM,params);
    }

    //上传材料到模拟浪潮
    public String upImage(MultipartFile multipartFile){
        String imageName = multipartFile.getOriginalFilename();
        File file = new File(dirPath+imageName);
        try{
            multipartFile.transferTo(file);
        }catch(IOException e) {
            logger.debug("========上传材料信息写入临时文件失败========");
        }
        return postFile(LC_UPFILE,file);
    }

    public byte[] loadImage(String docid){
        return getFile(LC_LOADFILE+docid);
    }



    private String get(String url){
        String result = "";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpGet get = new HttpGet(url);
        try{
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                try{
                    response.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try{
                    client.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private byte[] getFile(String url){
        byte[] result = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpGet get = new HttpGet(url);
        try{
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toByteArray(response.getEntity());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                try{
                    response.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try{
                    client.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
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
        }finally {
            if (response != null) {
                try{
                    response.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try{
                    client.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private String postFile(String url, File file){
        String result = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file",file);
        post.setEntity(builder.build());
        try{
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(),"utf-8");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                try{
                    response.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try{
                    client.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
