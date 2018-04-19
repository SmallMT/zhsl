package com.bov.mt.controllers;

import com.bov.mt.constant.Mark;
import com.bov.mt.constant.RegexString;
import com.bov.mt.entity.Token;
import com.bov.mt.entity.User;
import com.bov.mt.entity.result.CertificateUserResult;
import com.bov.mt.entity.result.RegisterResult;
import com.bov.mt.entity.result.SendPhoneResult;
import com.bov.mt.entity.vm.*;
import com.bov.mt.utils.StringUtil;
import com.bov.mt.utils.uaa.UaaUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UaaUtil uaaUtils;

    //用户登录页面
    @GetMapping("login")
    public String login(){
        return "user/login";
    }

    //登录操作
    @PostMapping("dologin")
    public String doLogin(@RequestParam("username") String username,@RequestParam("password") String password,
                          HttpServletRequest request,RedirectAttributes redirectAttributes){
        Token tokens = uaaUtils.getTokenByUsernameAndPwd(username,password);
        if (!"200".equalsIgnoreCase(tokens.getCode())) {
            //登录失败
            redirectAttributes.addFlashAttribute("msg","用户名或密码不正确");
            return "redirect:/user/login";
        }
        String token = tokens.getToken();
        Optional<User> optional = uaaUtils.getUserInfo(token,username);
        List<BindingCompanyInfoVM> companyInfoVMS = uaaUtils.bindingCompanyInfo(token, username);
        //初始化用户信息
        request.getSession().setAttribute("token",token);
        request.getSession().setAttribute("user",optional.get());
        request.getSession().setAttribute("username",optional.get().getLogin());
        request.getSession().setAttribute("companyInfos",companyInfoVMS);
        return "redirect:/user/index";
    }

    //用户主页面
    @GetMapping("index")
    public String index(){
        //初始化用户信息

        return "index";
    }

    //注册新用户页面
    @GetMapping("register")
    public String register(Model model){
        if (!model.containsAttribute("registerUserVM")) {
            model.addAttribute("registerUserVM",new RegisterUserVM());
        }
        return "user/addUserInfo";
    }

    //执行新用户注册
    @PostMapping("doregister")
    public String doRegister(@Valid RegisterUserVM registerUserVM , BindingResult bindingResult , RedirectAttributes redirectAttributes){
        //校验页面数据发生错误
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerUserVM",bindingResult);
            redirectAttributes.addFlashAttribute("registerUserVM",registerUserVM);
            return "redirect:/user/register";
        }
        String msg = "";
        //检测用户是否已经注册
        if (uaaUtils.usernameIsExist(registerUserVM.getUsername())) {
            msg = "该用户已经注册";
        }else {
            //注册用户
            RegisterResult result = uaaUtils.registerUser(registerUserVM);
            if (result.getStatus() == 200) {//注册成功
                //进入注册用户与用户实名认证之间
                return "redirect:/user/betweenregisterauth?username="+registerUserVM.getUsername()+"&password="+registerUserVM.getPassword();
            }else {
                msg = result.getTitle();
            }
        }
        redirectAttributes.addFlashAttribute("registerUserVM",registerUserVM);
        redirectAttributes.addFlashAttribute("msg",msg);
        return "redirect:/user/register";
    }

    //新用户注册完成后进入
    @GetMapping("betweenregisterauth")
    public String betweenRegisterAuth(@RequestParam String username, @RequestParam String password, HttpServletRequest request){
        String url = "";
        //刚注册的用户默认后台进行登录
        Token tokens = uaaUtils.getTokenByUsernameAndPwd(username,password);
        String token = tokens.getToken();
        //获取用户信息
        Optional<User> user = uaaUtils.getUserInfo(token,username);
        if (user.isPresent()) {
            //将用户信息放入session中
            request.getSession().setAttribute("user",user.get());
            request.getSession().setAttribute("username",user.get().getLogin());
            request.getSession().setAttribute("token",token);
            //进入用户实名认证界面
            url = "redirect:/user/userauth?type=ING";
        }else {
            //获取用户信息失败，进入用户登录界面
            url = "redirect:/user/login";
        }
        return url;
    }

    //进入用户实名认证页面
    @GetMapping("userauth")
    public String userAuth(@RequestParam("type") String type , Model model){
        if (!model.containsAttribute("authUserVM")) {
            model.addAttribute("authUserVM",new AuthUserVM());
        }
        Mark mark = Mark.valueOf(type);
        String url = "";
        switch (mark) {
            case ING:
                url = "user/newUserAuth";
                break;
            case AFTER:
                url = "user/afterloginuserauth";
                break;
            default:
                break;
        }
        return url;
    }

    //执行用户实名认证操作
    @PostMapping("douserauth")
    public String doUserAuth(@Valid AuthUserVM authUserVM, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             @RequestParam("type") String type,HttpServletRequest request){
        String failUrl = "redirect:/user/userauth?type="+type;
        String successUrl = "";
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserVM",bindingResult);
            redirectAttributes.addFlashAttribute("authUserVM",authUserVM);
            return failUrl;
        }
        //用户是否已经实名认证
        User user = (User) request.getSession().getAttribute("user");
        String token = (String) request.getSession().getAttribute("token");
        CertificateUserResult result = uaaUtils.realName(user.getLogin(),token,authUserVM);
        if (result.getStatus() == 200) {
            //实名认证成功根据标记进行跳转
            // type为ing跳转到实名认证与企业绑定之间
            // type为after跳转到个人信息页面
            switch (Mark.valueOf(type)) {
                case ING:
                    successUrl = "redirect:/user/betweenauthbindcompany";
                    break;
                case AFTER:
                    successUrl = "redirect:/user/afterafteruserauth";
                    break;
                default:
                    break;
            }
            return successUrl;
        }else {
            //实名认证失败,提示错误信息
            redirectAttributes.addFlashAttribute("authUserVM",authUserVM);
            redirectAttributes.addFlashAttribute("msg",result.getTitle());
        }
        return failUrl;
    }

    //用户完成实名认证后跳转
    @GetMapping("betweenauthbindcompany")
    public String betweenAuthBindCompany(HttpServletRequest request){
        String url = "";
        //获取最新的用户信息
        String token = (String) request.getSession().getAttribute("token");
        User user = (User) request.getSession().getAttribute("user");
        Optional<User> optional = uaaUtils.getUserInfo(token,user.getLogin());
        if (optional.isPresent()) {
            //获取用户信息成功,刷新session中用户信息,跳转用户企业绑定页面
            request.getSession().setAttribute("user",optional.get());
            url = "redirect:/user/bindcompany?type=ING";
        }else {
            //获取用户信息失败,返回登录界面
            url =  "redirect:/user/login";
        }
        return url;
    }

    //用户绑定企业页面
    @GetMapping("bindcompany")
    public String bindCompany(Model model,@RequestParam("type") String type){
        if (!model.containsAttribute("bindCompanyVM")) {
            model.addAttribute("bindCompanyVM",new BindCompanyVM());
        }
        String url = "";
        switch (Mark.valueOf(type)) {
            case ING:
                url = "user/companyAuth";
                break;
            case AFTER:
                url = "user/afterlogincompanyauth";
                break;
            default:
                break;
        }
        return url;
    }

    //绑定企业信息
    @PostMapping("dobindcompany")
    public String doBindCompany(@Valid BindCompanyVM bindCompanyVM,BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,HttpServletRequest request,
                                @RequestParam("type") String type){
        String failUrl = "redirect:/user/bindcompany?type="+type;
        //数据校验
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bindCompanyVM",bindingResult);
            redirectAttributes.addFlashAttribute("bindCompanyVM",bindCompanyVM);
            return failUrl;
        }
        //企业绑定
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getLogin();
        String token = (String) request.getSession().getAttribute("token");
        CertificateUserResult result = uaaUtils.bindCompany(username, token, bindCompanyVM);
        if (result.getStatus() == 200) {
            //绑定企业成功,进入主页面
            return "redirect:/user/index";
        }else {
            //失败重新绑定企业
            redirectAttributes.addFlashAttribute("bindCompanyVM",bindCompanyVM);
            redirectAttributes.addFlashAttribute("msg",result.getTitle());
        }
        return failUrl;
    }


    @GetMapping("afterafteruserauth")
    public String afterAfterUserAuth(HttpServletRequest request){
        //刷新用户信息
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getLogin();
        String token = (String) request.getSession().getAttribute("token");
        Optional<User> optional = uaaUtils.getUserInfo(token,username);
        request.getSession().setAttribute("user",optional.get());
        return "redirect:/user/index";
    }

    //个人信息
    @GetMapping("editInfo")
    public String editUserInfo(HttpServletRequest request,Model model){
        User user = (User) request.getSession().getAttribute("user");
        //个人账户信息
        ShowUserInfoVM info = new ShowUserInfoVM();
        info.setUsername(StringUtil.transerString(user.getLogin(),"*"));
        info.setCreateTime(user.getCreatedDate());
        info.setTel(StringUtil.transerString(user.getTel(),"*"));
        info.setVerified(user.isVerified());
        model.addAttribute("info",info);
        return "user/editInfo";
    }

    //检测用户名是否可用
    @GetMapping("checkusername")
    @ResponseBody
    public String checkUserExist(@RequestParam String username){
        //身份证校验
        if (username == null || "".equalsIgnoreCase(username) || !username.matches(RegexString.IDCARD_REGEX)) {
            logger.debug("=========身份证校验失败=============");
            return "身份证校验失败";
        }
        return uaaUtils.usernameIsExist(username)?"true":"false";
    }

    //用户认证信息主页面
    @GetMapping("certifacationindex")
    public String showCertificationInfo(HttpServletRequest request,Model model){
        //获取绑定企业信息
        User user = (User) request.getSession().getAttribute("user");
        String token = (String) request.getSession().getAttribute("token");
        String username = user.getLogin();
        List<BindingCompanyInfoVM> companyInfoVMS = uaaUtils.bindingCompanyInfo(token,username);
        //获取个人实名认证信息
        UserCertificationInfoVM userInfoVM = uaaUtils.realNameInfo(username, token);
        model.addAttribute("companySize",companyInfoVMS.size());
        model.addAttribute("verify",user.isVerified());
        if (companyInfoVMS.size() != 0) {
            model.addAttribute("companyInfo",companyInfoVMS.get(0));
            request.getSession().setAttribute("companyInfos",companyInfoVMS);
        }
        if (user.isVerified()) {
            model.addAttribute("userInfo",userInfoVM);
        }
        model.addAttribute("index",1);
        request.getSession().setAttribute("size",companyInfoVMS.size());
        return "certification/certificationindex";
    }

    //用户注册获取手机验证码
    @GetMapping("getregistercode")
    @ResponseBody
    public String registerPhoneCode(@RequestParam String phone){
        if (phone == null || "".equalsIgnoreCase(phone) || !phone.matches(RegexString.PHONE_REGEX)) {
            return "请填写正确的手机号";
        }
        SendPhoneResult result = uaaUtils.sendPhoneCode(phone);
        return result.getStatus() == 200 ? "true" : result.getDetail();
    }

    //获取认证图片
    @GetMapping("certificationphoto")
    public void certificationPhoto(@RequestParam("url") String url, HttpServletResponse response ,HttpServletRequest request){
        String token = (String) request.getSession().getAttribute("token");
        byte[] image = uaaUtils.certificationPhoto(token,url);
        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(image);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e) {
            logger.debug("==========获取实名认证或企业认证的图片失败===================");
        }
    }

    //主页面中的认证选项
    @GetMapping("certification")
    public String certification(HttpServletRequest request,Model model){
        //获取最新的用户信息
        String token = (String)request.getSession().getAttribute("token");
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getLogin();
        User newUser = uaaUtils.getUserInfo(token,username).get();
        //更新用户信息
        request.getSession().setAttribute("user",newUser);
        model.addAttribute("isCertification",newUser.isVerified());
        return "certification/certification";
    }

    //分页企业信息
    @GetMapping("companypage")
    public String companyInfoPage(@RequestParam("index") int index,@RequestParam("type") String type, HttpServletRequest request,Model model){
        //获取绑定的企业信息
        List<BindingCompanyInfoVM> companyInfos = (List<BindingCompanyInfoVM>) request.getSession().getAttribute("companyInfos");
        switch (type) {
            case "first" :
                index = 1;
                break;
            case "last" :
                index = companyInfos.size();
                break;
            case "before" :
                index--;
                break;
            case "next" :
                index ++;
                break;
        }
        BindingCompanyInfoVM companyInfo = companyInfos.get(index-1);
        model.addAttribute("companyInfo",companyInfo);
        model.addAttribute("index",index);
        return "certification/showCompanyInfo";
    }

    @GetMapping("checkphonehasused")
    @ResponseBody
    public boolean checkPhoneUsed(@RequestParam("phone") String phone){
        return uaaUtils.checkPhoneHasUsed(phone);
    }
}
