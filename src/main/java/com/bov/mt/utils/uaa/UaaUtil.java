package com.bov.mt.utils.uaa;

import com.bov.mt.Interceptor.BasicAuthInterceptor;
import com.bov.mt.constant.AdminInfo;
import com.bov.mt.constant.OAuth2Client;
import com.bov.mt.entity.*;
import com.bov.mt.entity.result.CertificateUserResult;
import com.bov.mt.entity.result.HttpClientResult;
import com.bov.mt.entity.result.RegisterResult;
import com.bov.mt.entity.result.SendPhoneResult;
import com.bov.mt.entity.vm.*;
import com.bov.mt.exception.GetTokenFailException;
import com.bov.mt.exception.PostException;
import com.bov.mt.utils.StringUtil;
import com.bov.mt.utils.uaa.post.PostMan;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class UaaUtil {


    private static final String UAA_TOKEN_URL = "http://localhost:8088/oauth/token"; //根据账号密码获取token
    private static final String UAA_ONEUSER_URL = "http://localhost:8089/api/users/";//获取某个用户的信息
    private static final String UAA_ONEUSERVERIFY = "http://localhost:8089/api/account/realName/";
    private static final String UAA_MODIFYPHONE = "http://localhost:8089/api/account/resetPhone";
    private static final String UAA_CHANGEPWD = "http://localhost:8089/api/account/change-password";
    private static final String UAA_REGISTERUSER = "http://localhost:8089/api/outerRegister/finish";//注册用户
    private static final String UAA_CERTIFICATION = "http://localhost:8089/api/account/realName";//实名认证
    private static final String UAA_GETCODE = "http://localhost:8089/api/outerRegister/init";//获取手机验证码
    private static final String UAA_BINDCOMPANY = "http://localhost:8089/api/account/bindEnterprise";//绑定企业
    private static final String UAA_GETBINDCOMPANYINFO = "http://localhost:8089/api/account/bindEnterprise?login.equals=";
    private static final String UAA_COMPANYLICENSEPHOTO = "http://localhost:8089/api/account/bindEnterprise/";
//    http://localhost:8081/api/account/bindEnterprise?login.equals=lyt1025&creditCode.equals=444&state.equals=待验证
    private static final String UAA_GETBINDCOMPANYINFOBYCODE = "http://localhost:8089/api/account/bindEnterprise?";
    private static final String UAA_CHECK_USER_PHONE = "http://localhost:9099/api/checkphonehasused?phone=";//查看手机号是否已经注册
    private static final String UAA_FIND_USERNAME_BY_PHONE = "http://localhost:9099/api/findusername?phone=";//根据手机号获取用户名
    private Logger logger = LoggerFactory.getLogger(UaaUtil.class);

    @Autowired
    private PostMan postMan;

    @Value("${realName.filePath}")
    private String dirPath;

    //根据用户名密码获取token值
    public Token getTokenByUsernameAndPwd(String username, String password){
        Token token = new Token();
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username",username)
                .add("password",password)
                .build();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(OAuth2Client.USER.getValue(),OAuth2Client.PASSWORD.getValue())).build();
        Request request = new Request.Builder()
                .url(UAA_TOKEN_URL)//
                .post(formBody)//
                .addHeader("Accept", "application/json")//
                .addHeader("Authorization","Basic dGVzdDoxMjM=")//
                .build();
        try {
            Response response = client.newCall(request).execute();
            int code = response.code();//获取返回状态码
            String responseBody = response.body().string();
            switch (code) {
                case 200 :
                    JSONObject json = JSONObject.fromObject(responseBody);
                    token.setCode("200");
                    token.setToken(json.getString("access_token"));
                    token.setError("");
                    break;
                case 401 :
                    token.setCode("401");
                    token.setToken("");
                    token.setError("获取token值时失败，401->未授权");
                    break;
                case 400 :
                    token.setCode("400");
                    token.setToken("");
                    token.setError("获取token值时失败，400->参数错误");
                    break;
                case 500 :
                    token.setCode("500");
                    token.setToken("");
                    token.setError("获取token值时失败，500->访问远程服务失败");
                    break;
                default :
                    break;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    //根据用户名获取用户信息
    public Optional<User> getUserInfo(String token,String username) throws PostException {
        User user = null;
        String url = UAA_ONEUSER_URL + username;
        OAuthHeader header = new OAuthHeader();
        header.setAccept("application/json");
        header.setAuthorization(token);
        Optional<HttpClientResult> optional = postMan.getMethod(url,header);
        checkHttpIsOK(optional);
        //解析
        HttpClientResult result = optional.get();
        int code = result.getCode();
        if (code != 200) {
            return Optional.ofNullable(user);
        }
        //获取用户信息
        JSONObject json = JSONObject.fromObject(result.getContent());
        //封装为User对象
        user = (User) JSONObject.toBean(json,User.class);
        return Optional.ofNullable(user);
    }

    //检测用户名是否存在

    /**
     *
     * @param username 用户名
     * @return 用户名存在返回 true 不存在返回false
     * @throws GetTokenFailException 获取token失败抛此异常
     */
    public boolean usernameIsExist(String username) throws GetTokenFailException{
        //使用管理员账号获取token
        Token tokens = getTokenByUsernameAndPwd(AdminInfo.ADMINNAME,AdminInfo.ADMINPWD);
        if (!tokens.getCode().equalsIgnoreCase("200")) {
            //获取token值失败
            logger.debug("========"+tokens.getCode()+"===========");
            logger.debug("========"+tokens.getError()+"===========");
            throw new GetTokenFailException("get token fail "+tokens.getError());
        }
        String token = tokens.getToken();
        Optional<User> optional = getUserInfo(token,username);
        return optional.isPresent();
    }

    //发送手机验证码
    public SendPhoneResult sendPhoneCode(String phone){
        SendPhoneResult phoneResult = null;
        String url = UAA_GETCODE;
        OAuthHeader header = new OAuthHeader();
        header.setContentType("application/json");
        JSONObject args = new JSONObject();
        args.put("phone",phone);
        Optional<HttpClientResult> optional = postMan.postMethod(url,header,args);
        checkHttpIsOK(optional);
        HttpClientResult result = optional.get();
        int code = result.getCode();
        if (code == 200) {
            //发送验证码成功
            phoneResult = new SendPhoneResult();
            phoneResult.setStatus(200);
        }else {
            //发送验证码失败
            phoneResult = (SendPhoneResult) JSONObject.toBean(JSONObject.fromObject(result.getContent()),SendPhoneResult.class);
        }
        return phoneResult;
    }

    //注册用户
    public RegisterResult registerUser(RegisterUserVM user){
        RegisterResult registerResult = null;
        //设置响应头
        OAuthHeader header = new OAuthHeader();
        header.setContentType("application/json");
        String url = UAA_REGISTERUSER;
        JSONObject args = new JSONObject();
        args.put("login",user.getUsername());
        args.put("password",user.getPassword());
        args.put("phone",user.getPhone());
        args.put("smsCode",user.getCode());
        JSONArray authorities = new JSONArray();
        authorities.add("ROLE_USER");
        args.put("authorities",authorities);
        Optional<HttpClientResult> optional = postMan.postMethod(url,header,args);
        checkHttpIsOK(optional);
        HttpClientResult result = optional.get();
        if (result.getCode() != 200) { //注册失败
            registerResult = (RegisterResult) JSONObject.toBean(JSONObject.fromObject(result.getContent()),RegisterResult.class);
        }else {
            registerResult = new RegisterResult();
            registerResult.setStatus(200);
        }
        return registerResult;
    }

    //实名认证
    public CertificateUserResult realName(String username,String token ,AuthUserVM realName){
        CertificateUserResult result = new CertificateUserResult();
        //响应头
        OAuthHeader header = new OAuthHeader();
        header.setAuthorization(token);
        //普通字段
        Map<String,String> texts = new HashMap<>();
        texts.put("login",username);
        texts.put("identity",realName.getTrueID());
        texts.put("name",realName.getTrueName());
        texts.put("state","通过");
        //设置file
        File front = new File(dirPath + StringUtil.transferFileName(realName.getIdCardFront().getOriginalFilename()));
        File back = new File(dirPath + StringUtil.transferFileName(realName.getIdCardBack().getOriginalFilename()));
        try {
            realName.getIdCardFront().transferTo(front);
            realName.getIdCardBack().transferTo(back);
        }catch (Exception e) {
            logger.debug("==========实名认证写入临时文件失败==============");
            result.setStatus(500);
            result.setTitle("服务器异常,请重新绑定");
            return result;
        }
        Map<String,File> files = new HashMap<>();
        files.put("frontFile",front);
        files.put("backFile",back);
        Optional<HttpClientResult> optional = postMan.postWithFile(UAA_CERTIFICATION,header,texts,files);
        if (!optional.isPresent()) {
            logger.debug("==========实名认证失败=============");
            result.setStatus(500);
            result.setTitle("服务器异常,请重新绑定");
            return result;
        }
        HttpClientResult httpClientResult = optional.get();
        if (httpClientResult.getCode() != 201) { //注册失败
            result = (CertificateUserResult) JSONObject.toBean(JSONObject.fromObject(httpClientResult.getContent()),CertificateUserResult.class);
        }else {
            result.setStatus(200);
            result.setTitle("OK");
            result.setMessage("certification is successful");
        }
        return result;
    }

    //获取实名认证信息
    public UserCertificationInfoVM realNameInfo(String username,String token){
        UserCertificationInfoVM infoVM = null;
        String url = UAA_ONEUSERVERIFY + username;
        OAuthHeader headers = new OAuthHeader();
        headers.setAccept("application/json");
        headers.setAuthorization(token);
        Optional<HttpClientResult> optional = postMan.getMethod(url, headers);
        HttpClientResult httpClientResult = optional.get();
        if (httpClientResult.getCode() == 200 && null != httpClientResult.getContent() && !"".equalsIgnoreCase(httpClientResult.getContent())) {
            infoVM = (UserCertificationInfoVM)JSONObject.toBean(JSONObject.fromObject(httpClientResult.getContent()),UserCertificationInfoVM.class);
        }
        return infoVM;
    }

    //绑定企业
    public CertificateUserResult bindCompany(String username, String token, BindCompanyVM bindCompanyVM){
        CertificateUserResult result = new CertificateUserResult();
        OAuthHeader header = new OAuthHeader();
        header.setAuthorization(token);
        //普通字段
        Map<String,String> texts = new HashMap<>();
        texts.put("login",username);
        texts.put("creditCode",bindCompanyVM.getCompanyCode());
        texts.put("enterpriseName",bindCompanyVM.getCompanyName());
        texts.put("enterpriseAddress",bindCompanyVM.getCompanyAddress());
        texts.put("legalPersonID",bindCompanyVM.getLegalPersonID());
        texts.put("legalPersonName",bindCompanyVM.getLegalPersonName());
        texts.put("legalPersonPhone",bindCompanyVM.getLegalPersonPhone());
        texts.put("state","通过");
        //文件
        File businessLicenseFile = new File(dirPath + StringUtil.transferFileName(bindCompanyVM.getBusinessLicence().getOriginalFilename()));
        try {
            bindCompanyVM.getBusinessLicence().transferTo(businessLicenseFile);
        }catch (IOException e) {
            logger.debug("===========绑定企业写入临时文件失败===============");
            result.setStatus(500);
            result.setTitle("服务器异常,请重新绑定");
            return result;
        }
        Map<String,File> files = new HashMap<>();
        files.put("businessLicenseFile",businessLicenseFile);
        Optional<HttpClientResult> optional = postMan.postWithFile(UAA_BINDCOMPANY, header, texts, files);
        if (!optional.isPresent()) {
            result.setStatus(500);
            result.setTitle("服务器异常,请重新绑定");
            return result;
        }
        HttpClientResult httpClientResult = optional.get();
        if (httpClientResult.getCode() != 201) {
            result = (CertificateUserResult) JSONObject.toBean(JSONObject.fromObject(httpClientResult.getContent()),CertificateUserResult.class);
        }else {
            result.setStatus(200);
            result.setTitle("OK");
            result.setMessage("binding company successful");
        }
        return result;
    }

    //已绑定的企业信息
    public List<BindingCompanyInfoVM> bindingCompanyInfo(String token, String username){
        List<BindingCompanyInfoVM> infoVMS = new ArrayList<>();
        OAuthHeader headers = new OAuthHeader();
        headers.setAuthorization(token);
        String url = UAA_GETBINDCOMPANYINFO + username;
        Optional<HttpClientResult> optional = postMan.getMethod(url, headers);
        HttpClientResult httpClientResult = optional.get();
        JSONObject result = JSONObject.fromObject(httpClientResult.getContent());
        if (result.getInt("totalElements") == 0) {
            //没有绑定的企业
            return infoVMS;
        }
        JSONArray companyInfos = result.getJSONArray("content");
        for (int i=0;i<companyInfos.size();i++) {
            BindingCompanyInfoVM vm = new BindingCompanyInfoVM();
            JSONObject temp = companyInfos.getJSONObject(i);
            vm.setId(temp.getInt("id"));
            vm.setEnterpriseName(temp.getString("enterpriseName"));
            vm.setCreditCode(temp.getString("creditCode"));
            vm.setBusinessLicense(temp.getString("businessLicense"));
            vm.setState(temp.getString("state"));
            vm.setLegalPersonId(temp.getString("legalPersonId"));
            vm.setLegalPersonName(temp.getString("legalPersonName"));
            vm.setLegalPersonPhone(temp.getString("legalPersonPhone"));
            vm.setEnterpriserAddress(temp.getString("enterpriserAddress"));
            vm.setBusinessLicenseFile(temp.getString("businessLicenseFile"));
            infoVMS.add(vm);
        }
        return infoVMS;
    }

    public BindingCompanyInfoVM bindingCompanyOneInfo(String token,String username,String companyCode){
        OAuthHeader headers = new OAuthHeader();
        headers.setAuthorization(token);
        String url = UAA_GETBINDCOMPANYINFO + username + "&creditCode.equals="+ companyCode;
        Optional<HttpClientResult> optional = postMan.getMethod(url, headers);
        HttpClientResult httpClientResult = optional.get();
        JSONObject result = JSONObject.fromObject(httpClientResult.getContent());
        JSONArray companyInfos = result.getJSONArray("content");
        BindingCompanyInfoVM vm = new BindingCompanyInfoVM();
        JSONObject temp = companyInfos.getJSONObject(0);
        vm.setId(temp.getInt("id"));
        vm.setEnterpriseName(temp.getString("enterpriseName"));
        vm.setCreditCode(temp.getString("creditCode"));
        vm.setBusinessLicense(temp.getString("businessLicense"));
        vm.setState(temp.getString("state"));
        vm.setLegalPersonId(temp.getString("legalPersonId"));
        vm.setLegalPersonName(temp.getString("legalPersonName"));
        vm.setLegalPersonPhone(temp.getString("legalPersonPhone"));
        vm.setEnterpriserAddress(temp.getString("enterpriserAddress"));
        vm.setBusinessLicenseFile(temp.getString("businessLicenseFile"));
        return vm;
    }

    //判断手机号是否已经注册
    public boolean checkPhoneHasUsed(String phone){
        String url = UAA_CHECK_USER_PHONE + phone;
        OAuthHeader header = new OAuthHeader();
        Optional<HttpClientResult> result = postMan.getMethod(url,header);
        String flag = result.get().getContent();
        return "true".equalsIgnoreCase(flag);
    }
    //根据手机号获取用户名
    public String findUsernameByPhone(String phone){
        String url = UAA_FIND_USERNAME_BY_PHONE + phone;
        OAuthHeader header = new OAuthHeader();
        Optional<HttpClientResult> result = postMan.getMethod(url,header);
        return result.get().getContent();
    }


    //获取认证的图片
    public byte[] certificationPhoto(String token,String url){
        return postMan.getImage(token,url);
    }

    protected void checkHttpIsOK(Optional<HttpClientResult> optional){
        if (!optional.isPresent()) {
            logger.debug("=============发送请求失败==============");
            throw new PostException("sent post/get/.. fail");
        }
    }
}
