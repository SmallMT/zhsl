//package com.bov.mt.utils.uaa;
//
//import com.bov.mt.Interceptor.BasicAuthInterceptor;
//import com.bov.mt.constant.AdminInfo;
//import com.bov.mt.constant.OAuth2Client;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import okhttp3.*;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
//@Component
//public class UAAUtils {
//
//    private static final String UAA_TOKEN_URL = "http://localhost:8088/oauth/token";
//    private static final String UAA_ONEUSER_URL = "http://localhost:8089/api/users/";
//    private static final String UAA_ONEUSERVERIFY = "http://localhost:8089/api/account/realName/";
//    private static final String UAA_MODIFYPHONE = "http://localhost:8089/api/account/resetPhone";
//    private static final String UAA_CHANGEPWD = "http://localhost:8089/api/account/change-password";
//    private static final String UAA_REGISTERUSER = "http://localhost:8089/api/outerRegister/finish";
//    private static final String UAA_CERTIFICATION = "http://localhost:8089/api/account/realName";
//    private static final String UAA_GETCODE = "http://localhost:8089/api/outerRegister/init";
//    private static final String UAA_BINDCOMPANY = "http://localhost:8089/api/account/bindEnterprise";
//    private static final String UAA_GETBINDCOMPANYINFO = "http://localhost:8089/api/account/bindEnterprise?login.equals=";
//    private static final String UAA_COMPANYLICENSEPHOTO = "http://localhost:8089/api/account/bindEnterprise/";
//    private static final String UAA_GETBINDCOMPANYINFOBYCODE = "http://localhost:8089/api/account/bindEnterprise?";
//
//
//    //不根据用户名密码获取token值
//    public JSONObject getToken(){
//        JSONObject result = null;
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost post = new HttpPost(UAA_TOKEN_URL);
//        post.setHeader("Accept","application/json");
//        post.setHeader("Authorization","Basic dGVzdDoxMjM=");
//        post.setHeader("Content-Type","application/json; charset=utf-8");
//        JSONObject data = new JSONObject();
//        data.put("grant_type","client_credentials");
//        StringEntity entity = new StringEntity(data.toString(),"UTF-8");
//        post.setEntity(entity);
//        try {
//            HttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
//                result = JSONObject.fromObject(content);
//            }
//            if (response.getStatusLine().getStatusCode() == 400) {
//                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
//                result = JSONObject.fromObject(content);
//            }
//            client.close();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public JSONObject getUserInfo(String username,String token){
//        String url = UAA_ONEUSER_URL + username;
//        return httpGet(url,token);
//    }
//    //用户修改手机号
//    public boolean modifyPhone(String username,String phone,String token){
//        String url = UAA_MODIFYPHONE;
//        JSONObject data = new JSONObject();
//        data.put("login",username);
//        data.put("phone",phone);
//        return put(url,token,data);
//    }
//
//    //用户修改密码
//    public boolean changePassword(String username,String password,String token){
//        JSONObject data = new JSONObject();
//        data.put("login",username);
//        data.put("password",password);
//        return httpPOST(UAA_CHANGEPWD,token,data);
//    }
//    //获取用户认证信息
//    public JSONObject getUserVerifiyInfo(String username,String token) {
//        String url = UAA_ONEUSERVERIFY + username;
//        return httpGet(url,token);
//    }
//
//    //获取短信验证码
//    public void getOCode(String phone){
//        JSONObject data = new JSONObject();
//        data.put("phone",phone);
//        postCode(UAA_GETCODE,data);
//    }
//
//    //注册新用户
//   public boolean registerNewUser(JSONObject user) {
//        JSONArray authorities = new JSONArray();
//        authorities.add("ROLE_USER");
//        user.put("authorities",authorities);
//        return postWithoutToken(UAA_REGISTERUSER,user);
//   }
//    //用户实名认证
////    public boolean certificationUser(String access_token,JSONObject info,Map<String,byte[]> files){
////        return postWithBytes(UAA_CERTIFICATION,access_token,info,files);
////    }
//
//    //用户实名认证
////    public boolean certificationUser(String access_token,JSONObject info,Map<String,File> files){
////        return postWithFile(UAA_CERTIFICATION,access_token,info,files);
////    }
//    public JSONObject certificationUser(String access_token,JSONObject info,Map<String,File> files){
//        return postRealName(UAA_CERTIFICATION,access_token,info,files);
//    }
//    //获取实名认证身份证图片
//    public byte[] getCertificationIDCardImage(String url,String token){
//        return getImage(url,token);
//    }
//
//    //绑定公司信息
//    public boolean bindCompany(JSONObject companyInfo,String token,File companyLicensePhoto){
//        Map<String,File> map = new HashMap<>();
//        map.put("businessLicenseFile",companyLicensePhoto);
//        return postWithFile(UAA_BINDCOMPANY,token,companyInfo,map);
//    }
//
//    //绑定公司信息
//    public boolean bindCompany(JSONObject companyInfo,String token,byte[] companyLicensePhoto){
//        Map<String,byte[]> map = new HashMap<>();
//        map.put("businessLicenseFile",companyLicensePhoto);
//        return postWithBytes(UAA_BINDCOMPANY,token,companyInfo,map);
//    }
//
//
//    //获取用户全部的企业绑定信息
//    public JSONObject getBindCompanyInfo(String username,String token){
//        String url = UAA_GETBINDCOMPANYINFO + username;
//        return httpGet(url,token);
//    }
//
//    //根据用户名，企业代码获取企业信息
//    public JSONObject getBindCompanyInfoByCode(String username,String code,String token){
//        String url = UAA_GETBINDCOMPANYINFOBYCODE + "login.equals=" + username + "&creditCode.equals="+code;
//        return httpGet(url,token);
//    }
//
//    //判断指定用户名是否存在
//    //返回null表示不存在
//    public JSONObject checkUsernameIsExist(String username){
//        JSONObject tokenJSON = getTokenJSON(AdminInfo.ADMINNAME,AdminInfo.ADMINPWD);
//        if (!"200".equalsIgnoreCase(tokenJSON.getString("code"))){
//            return null;
//        }
//        String token = tokenJSON.getJSONObject("tokens").getString("token");
//        return getUserInfo(username,token);
//    }
//
//    //重置用户密码
//    public void findPassword(String username,String password){
//        JSONObject tokenJSON = getTokenJSON(AdminInfo.ADMINNAME,AdminInfo.ADMINPWD);
//        String token = tokenJSON.getJSONObject("tokens").getString("token");
//        changePassword(username,password,token);
//    }
//
//    //获取用户企业绑定的营业执照
//    public byte[] getCompanyLicensePhoto(String url,String token){
//        return getCompanyPhoto(url,token);
//    }
//
//
//    private JSONObject httpGet(String url,String access_token){
//        //get请求返回结果
//        JSONObject jsonResult = null;
//        //设置token请求值
//        String token="Bearer "+access_token;
//        try {
//            DefaultHttpClient client = new DefaultHttpClient();
//            //发送get请求
//            HttpGet request = new HttpGet(url);
//
//            request.addHeader("Content-type","application/json; charset=utf-8");
//            request.setHeader("Accept", "application/json");
//            request.setHeader("Authorization", token);
//            HttpResponse response = client.execute(request);
//            /**请求发送成功，并得到响应**/
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                /**读取服务器返回过来的json字符串数据**/
//                String strResult = EntityUtils.toString(response.getEntity());
//                /**把json字符串转换成json对象**/
//                if("".equals(strResult)){
//                    jsonResult = null;
//                }else {
//                    jsonResult = JSONObject.fromObject(strResult);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return jsonResult;
//    }
//
//    private boolean put(String url,String access_token,JSONObject data){
//
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPut put = new HttpPut(url);
//        put.setHeader("Content-Type","application/json");
//        String token="Bearer "+access_token;
//        put.setHeader("Authorization",token);
//        List<NameValuePair> list = new ArrayList<>();
//        Set<String> keys = data.keySet();
//        for (String key : keys) {
//            list.add(new BasicNameValuePair(key,data.getString(key)));
//        }
//        try {
//            StringEntity entity = new StringEntity(data.toString(),"UTF-8");
////            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
//            put.setEntity(entity);
//            HttpResponse response = client.execute(put);
//            if (response.getStatusLine().getStatusCode() == 204) {
//                return true;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    private boolean httpPOST(String url,String access_token,JSONObject data){
//
//        boolean flag = false;
//
//        //设置token请求值
//        String token = "Bearer "+access_token;
//
//        HttpClient httpclient = HttpClientBuilder.create().build();
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.addHeader("Content-Type","application/json;charset=utf-8");
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Authorization",token);
//        StringEntity entity = new StringEntity(data.toString(),"utf-8");//解决中文乱码问题
//        try {
//            httpPost.setEntity(entity);
//            HttpResponse res =  httpclient.execute(httpPost);
//            if (res.getStatusLine().getStatusCode() < 300) {//200
//                /**读取服务器返回过来的json字符串数据**/
//                String strResult = EntityUtils.toString(res.getEntity(), "UTF-8");
//                flag = true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return flag;
//    }
//
//    private boolean postWithFile(String url, String access_token, JSONObject info, Map<String,File> files){
//        boolean flag = false;
//        String token = "Bearer "+access_token;
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Accept","application/json");
//        post.setHeader("Authorization",token);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        Set<String> stringKeys = info.keySet();
//        try {
//            for (String key : stringKeys) {
//            builder.addTextBody(key,info.getString(key),ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        Set<String> fileKeys = files.keySet();
//        for (String key : fileKeys) {
//            builder.addBinaryBody(key, files.get(key));
//        }
//
//        HttpEntity entity = builder.build();
//        post.setEntity(entity);
//        try {
//            HttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() == 201) {
//                flag = true;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                client.close();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return flag;
//    }
//
//    private JSONObject postRealName(String url, String access_token, JSONObject info, Map<String,File> files){
//        JSONObject result = null;
//        String token = "Bearer "+access_token;
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Accept","application/json");
//        post.setHeader("Authorization",token);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        Set<String> stringKeys = info.keySet();
//        try {
//            for (String key : stringKeys) {
//                builder.addTextBody(key,info.getString(key),ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        Set<String> fileKeys = files.keySet();
//        for (String key : fileKeys) {
//            builder.addBinaryBody(key, files.get(key));
//        }
//        HttpEntity entity = builder.build();
//        post.setEntity(entity);
//        try {
//            HttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() != 201) {
//                //发送失败
//                String re = EntityUtils.toString(response.getEntity(),"UTF-8");
//                result = JSONObject.fromObject(re);
//            }
//        }catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                client.close();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//
//    private boolean postWithBytes(String url ,String access_token, JSONObject info ,Map<String,byte[]> files) {
//        boolean flag = false;
//        String token = "Bearer "+access_token;
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Accept","application/json");
//        post.setHeader("Authorization",token);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        Set<String> stringKeys = info.keySet();
//        try {
//            for (String key : stringKeys) {
//                builder.addTextBody(key,info.getString(key), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        Set<String> fileKeys = files.keySet();
//        for (String key : fileKeys) {
//            builder.addBinaryBody(key, files.get(key));
//        }
//        HttpEntity entity = builder.build();
//        post.setEntity(entity);
//        try {
//            HttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() == 201) {
//                flag = true;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                client.close();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return flag;
//    }
//    private boolean postCode(String url,JSONObject data){
//        boolean flag = false;
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Content-Type","application/json");
//        StringEntity entity = new StringEntity(data.toString(),"utf-8");
//        post.setEntity(entity);
//        try {
//            CloseableHttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                flag = true;
//            }else {
//                flag = false;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                client.close();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return flag;
//    }
//
//    private boolean postWithoutToken(String url,JSONObject user){
//        boolean flag = false;
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Content-Type","application/json");
//        StringEntity entity = new StringEntity(user.toString(),"utf-8");
//        post.setEntity(entity);
//        try {
//            HttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                flag = true;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                client.close();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return flag;
//    }
//
//    private byte[] getImage(String url,String access_token){
//
//        String token = "Bearer "+ access_token;
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpGet get = new HttpGet(url);
//        get.setHeader("Authorization",token);
//        try {
//            HttpResponse response = client.execute(get);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                byte[] image = EntityUtils.toByteArray(response.getEntity());
//                return image;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                client.close();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    private byte[] getCompanyPhoto(String url,String access_token){
//        byte[] photo = null;
//        CloseableHttpClient client = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//        HttpGet get = new HttpGet(url);
//        String token = "Bearer " + access_token;
//        get.setHeader("Authorization",token);
//        try {
//            response = client.execute(get);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                photo = EntityUtils.toByteArray(response.getEntity());
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            if (client != null) {
//                try {
//                    client.close();
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (response != null) {
//                try {
//                    response.close();
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return photo;
//    }
//
//    public static void main(String[] args) {
////        UAAUtils uaaUtils = new UAAUtils();
////        JSONObject o = uaaUtils.getTokenJSON("mt","mtmt123");
////        String token = o.getJSONObject("tokens").getString("token");
//////        String t = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTY3MTY0MzAsInVzZXJfbmFtZSI6Im10IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjhlMDZlZDQxLThkNzgtNDRhYS05OTYxLWM5MTE3NzM0ZTljMyIsImNsaWVudF9pZCI6Inpoc2wiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.Z8MAH2y8NAW7HlHb6_BDC8iCRRONwdR_xtRA3tQdI3vGYUZcRldardmnRBLpT5lAtDlxPCmM9UKASpYne6jYpDTqOTPc-RBcNKu_1uKdMBbUyddlG5elHami0iBrBwITwP918JgkMRiEeEtYqJq_e6dbJInR1BEg0yNziQ1YYjXSjH-Z57fpmE3lxaH_9q2m5cRpUlloklgu2MEHdF3OS-MlhjrvBilzRHyZLZqNnTE2_EGUtfBiGFlCbBt5WUPf0cIlVJUMyaFRC6dXwTdzn1gL9pb7YMdOo68Hl8JFDHxwyVnjn7bC40XfMD-Y1FzXgDYIbHsYRvweG00uAGkQHw";
////        uaaUtils.getUserInfo("mt",token);
//        String s1 = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTY4ODYxMDUsInVzZXJfbmFtZSI6Im10IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjM3Y2ExMmMyLWUzY2UtNGQ2ZS1hZGZhLWIwYWRjMzAyMzdjYyIsImNsaWVudF9pZCI6Inpoc2wiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.TO95H0oK_4Sx31K5lOy4zLi44x6PkDCmR8lqYQs_91BFYqvupa1hoFedkNAZcVLZjxwWbA1AeE2Kmx8D8n2otfnoPagB_svcR2TxAAEXjbT1brAc4uuRcs_1rknrKx2ASTwjt0wztyeGhb5oM928G_1BodiD6UJoKac12VYW_zzgODb3d661e1m1BOljwlZVVrkmtrA6TrP2Gj1OAvi8fScUM7ro7GyHYPEX-UV4UQPw6VJmhjCIN35ZI5v1pdCorsm9x6hzgEnbHu50F_Ke6JD6kRm-d2LOn5YF9FSo9LZBPRHQps5ItO9M8awi9nzxTdD0A9o3t8VZNpL93hsk-Q";
//        String s2 = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTY4ODYxMDUsInVzZXJfbmFtZSI6Im10IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjM3Y2ExMmMyLWUzY2UtNGQ2ZS1hZGZhLWIwYWRjMzAyMzdjYyIsImNsaWVudF9pZCI6Inpoc2wiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.TO95H0oK_4Sx31K5lOy4zLi44x6PkDCmR8lqYQs_91BFYqvupa1hoFedkNAZcVLZjxwWbA1AeE2Kmx8D8n2otfnoPagB_svcR2TxAAEXjbT1brAc4uuRcs_1rknrKx2ASTwjt0wztyeGhb5oM928G_1BodiD6UJoKac12VYW_zzgODb3d661e1m1BOljwlZVVrkmtrA6TrP2Gj1OAvi8fScUM7ro7GyHYPEX-UV4UQPw6VJmhjCIN35ZI5v1pdCorsm9x6hzgEnbHu50F_Ke6JD6kRm-d2LOn5YF9FSo9LZBPRHQps5ItO9M8awi9nzxTdD0A9o3t8VZNpL93hsk-Q";
//        System.out.println(s1.equals(s2));
//    }
//}
