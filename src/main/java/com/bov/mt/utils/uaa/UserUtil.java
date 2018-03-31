//package com.bov.mt.utils.uaa;
//
//import com.bov.mt.constant.AdminInfo;
//import com.bov.mt.exception.GetTokenFailException;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class UserUtil {
//
//    private UserUtil(){}
//    Logger logger = LoggerFactory.getLogger(UserUtil.class);
//    @Autowired
//    private UAAUtils uaaUtils;
//    //检测用户是否已经注册
//    public boolean checkUserExist(String username){
//        JSONObject tokenJSON = uaaUtils.getTokenJSON(AdminInfo.ADMINNAME,AdminInfo.ADMINPWD);
//        if (!tokenJSON.getString("code").equalsIgnoreCase("200")) {
//            //获取token值失败
//            logger.debug("===========获取token值失败============");
//            logger.debug("================"+tokenJSON.getString("error")+"======================");
//            throw new GetTokenFailException("error to get token");
//        }
//        String token = tokenJSON.getJSONObject("tokens").getString("token");
//        Optional<JSONObject> userInfo = Optional.ofNullable(uaaUtils.getUserInfo(username,token));
//        return userInfo.isPresent();
//    }
//}
