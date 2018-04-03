package com.bov.mt.controllers;

import com.bov.mt.constant.MongoTable;
import com.bov.mt.entity.User;
import com.bov.mt.entity.mongo.ItemInfo;
import com.bov.mt.entity.mongo.MYItemInfo;
import com.bov.mt.entity.vm.BindingCompanyInfoVM;
import com.bov.mt.service.mongodb.MongoService;
import com.bov.mt.utils.uaa.UaaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String myItem(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getLogin();
        //查询我的办件前五条
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.skip(5);
        List<MYItemInfo> items = template.find(query,MYItemInfo.class,MongoTable.BANJIAN);

        return "item/mybanjian";
    }
}
