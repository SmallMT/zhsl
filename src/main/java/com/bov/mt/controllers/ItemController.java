package com.bov.mt.controllers;

import com.bov.mt.constant.MongoTable;
import com.bov.mt.entity.User;
import com.bov.mt.entity.mongo.ItemInfo;
import com.bov.mt.entity.mongo.MYItemInfo;
import com.bov.mt.entity.vm.BindingCompanyInfoVM;
import com.bov.mt.entity.vm.UserCertificationInfoVM;
import com.bov.mt.service.mongodb.MongoService;
import com.bov.mt.utils.ItemUtil;
import com.bov.mt.utils.page.Page;
import com.bov.mt.utils.page.PageFactory;
import com.bov.mt.utils.uaa.UaaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/item/")
public class ItemController {

    @Autowired
    private MongoTemplate template;
    @Autowired
    private MongoService service;
    @Autowired
    private UaaUtil uaaUtil;
    @Autowired
    private ItemUtil itemUtil;

    @GetMapping("zhslindex")
    public String zhslIndex(HttpServletRequest request, Model model){
        //获取所有的办件信息
        List<ItemInfo> items = template.findAll(ItemInfo.class, MongoTable.ITEMINFO);
        //获取所有的绑定企业信息
        String token = (String) request.getSession().getAttribute("token");
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getLogin();
        List<BindingCompanyInfoVM> companyInfoVMS = uaaUtil.bindingCompanyInfo(token, username);
        model.addAttribute("items",items);
        model.addAttribute("companyInfoVMS",companyInfoVMS);
        return "item/zhsl";
    }

    @GetMapping("banjian")
    public String myItem(HttpServletRequest request,Model model){
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getLogin();
        String token = (String) request.getSession().getAttribute("token");
        //查询我的办件前五条
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        int count = service.count(query,MongoTable.BANJIAN);
        Page<MYItemInfo> page = PageFactory.getPage(1,5,count);
        List<MYItemInfo> items = service.findByPage(page,query,MYItemInfo.class,MongoTable.BANJIAN);
        itemUtil.dateToString(items);
        page.setData(items);
        model.addAttribute("page",page);
        //获取绑定的企业个数
        int companySize = uaaUtil.bindingCompanyInfo(token,username).size();
        model.addAttribute("companySize",companySize);
        return "item/mybanjian";
    }

    @GetMapping("banjianpage")
    public String myItemPage(@RequestParam("index") int currentPage , HttpServletRequest request,
                             Model model){
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getLogin();
        //查询我的办件前五条
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        int count = service.count(query,MongoTable.BANJIAN);
        Page<MYItemInfo> page = PageFactory.getPage(currentPage,5,count);
        List<MYItemInfo> items = service.findByPage(page,query,MYItemInfo.class,MongoTable.BANJIAN);
        itemUtil.dateToString(items);
        page.setData(items);
        model.addAttribute("page",page);
        return "item/mybanjian";
    }

    @PostMapping("searchbanjian")
    public String searchBanJian(HttpServletRequest request , Model model){
        String search = request.getParameter("search").trim();
        String username = (String) request.getSession().getAttribute("username");
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username).and("name").regex(search));
        int count = service.count(query,MongoTable.BANJIAN);
        Page<MYItemInfo> page = PageFactory.getPage(1,5,count);
        List<MYItemInfo> items = service.findByPage(page,query,MYItemInfo.class,MongoTable.BANJIAN);
        itemUtil.dateToString(items);
        page.setData(items);
        model.addAttribute("page",page);
        model.addAttribute("search",search);
        model.addAttribute("count",count);
        return "item/search";
    }

    //分页查询我的办件信息
    @GetMapping("searchbanjianpage")
    public String searchBanjianPage(HttpServletRequest request , Model model){
        String search = request.getParameter("search").trim();
        String currentPageStr = request.getParameter("index");
        int currentPage = Integer.valueOf(currentPageStr);
        String username = (String) request.getSession().getAttribute("username");
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username).and("name").regex(search));
        int count = service.count(query,MongoTable.BANJIAN);
        Page<MYItemInfo> page = PageFactory.getPage(currentPage,5,count);
        List<MYItemInfo> items = service.findByPage(page,query,MYItemInfo.class,MongoTable.BANJIAN);
        itemUtil.dateToString(items);
        page.setData(items);
        model.addAttribute("page",page);
        model.addAttribute("search",search);
        model.addAttribute("count",count);
        return "item/search";
    }

    //条件查询综合受理事项
    @PostMapping("searchzhsl")
    public String searchZHSL(HttpServletRequest request,Model model){
        String username = (String) request.getSession().getAttribute("username");
        String token = (String) request.getSession().getAttribute("token");
        String search = request.getParameter("search").trim();
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(search));
        List<ItemInfo> items = template.find(query,ItemInfo.class,MongoTable.ITEMINFO);
        //获取用户绑定企业信息
        List<BindingCompanyInfoVM> infoVMS = uaaUtil.bindingCompanyInfo(token,username);
        model.addAttribute("items",items);
        model.addAttribute("companyInfoVMS",infoVMS);
        return "item/zhsl";
    }

    @GetMapping("addbanjian")
    public String addBanJian(@RequestParam("code") String code,@RequestParam("companyCode") String companyCode,
                             HttpServletRequest request,Model model){
        //获取该code的事项名称
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        ItemInfo itemInfo = template.findOne(query,ItemInfo.class,MongoTable.ITEMINFO);
        String itemName = itemInfo.getName();
        //获取用户选取的企业信息
        String username = (String) request.getSession().getAttribute("username");
        String token = (String) request.getSession().getAttribute("token");
        BindingCompanyInfoVM vm = uaaUtil.bindingCompanyOneInfo(token,username,companyCode);
        //获取用户实名认证信息
        UserCertificationInfoVM userCertificationInfoVM = uaaUtil.realNameInfo(username,token);
        model.addAttribute("companyInfo",vm);
        model.addAttribute("name",itemName);
        model.addAttribute("userCertificationInfo",userCertificationInfoVM);
        return "item/forms/"+code;
    }
}
