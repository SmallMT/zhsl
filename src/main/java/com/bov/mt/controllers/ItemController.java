package com.bov.mt.controllers;

import com.bov.mt.constant.MongoTable;
import com.bov.mt.entity.User;
import com.bov.mt.entity.mongo.ItemInfo;
import com.bov.mt.entity.mongo.MYItemInfo;
import com.bov.mt.entity.mongo.MetailInfo;
import com.bov.mt.entity.vm.BindingCompanyInfoVM;
import com.bov.mt.entity.vm.UserCertificationInfoVM;
import com.bov.mt.service.mongodb.MongoService;
import com.bov.mt.utils.ItemUtil;
import com.bov.mt.utils.LangChaoService;
import com.bov.mt.utils.StringUtil;
import com.bov.mt.utils.page.Page;
import com.bov.mt.utils.page.PageFactory;
import com.bov.mt.utils.uaa.UaaUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/item/")
public class ItemController {

    private Logger logger = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private MongoTemplate template;
    @Autowired
    private MongoService service;
    @Autowired
    private UaaUtil uaaUtil;
    @Autowired
    private ItemUtil itemUtil;
    @Autowired
    private LangChaoService lc;


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
        model.addAttribute("itemCode",code);
        return "item/forms/"+code;
    }


    //保存数据到本地数据库，以及保存(没有申报)数据到浪潮
    @PostMapping("insertitem")
    @ResponseBody
    public String insertItem(@RequestBody String data,HttpServletRequest request){
        String username = (String) request.getSession().getAttribute("username");
        JSONObject dataJSON = JSONObject.fromObject(data);
        String companyCode = dataJSON.getString("companyCode");
        String itemCode = dataJSON.getString("itemCode");
        //保存数据到模拟浪潮系统
        //1.获取formId
        ItemInfo itemInfo = template.findOne(new Query().addCriteria(Criteria.where("code").is(itemCode)),ItemInfo.class,MongoTable.ITEMINFO);
        String formId = itemInfo.getFormId();
        //2.处理data
        dataJSON.remove("companyCode");
        dataJSON.remove("itemCode");
        String dataId = lc.saveData(formId,dataJSON);
        Document banJian = new Document();
        banJian.put("username",username);
        banJian.put("companyCode",companyCode);
        banJian.put("code",itemCode);
        //获取办件名称
        String itemName = itemInfo.getName();
        String gsj = itemInfo.getGsj();
        banJian.put("name",itemName);
        banJian.put("gsj",gsj);
        banJian.put("data",dataJSON);
        banJian.put("saveTime",new Date());
        banJian.put("dataId",dataId);
        banJian.put("receiveNum","");
        banJian.put("hasApply",false);
        template.insert(banJian,MongoTable.BANJIAN);
        return dataId;
    }

    @GetMapping("metails")
    public String metails(@RequestParam("dataId") String dataId , HttpServletRequest request,Model model){

        //根据dataId获取code
        MYItemInfo myItemInfo = template.findOne(new Query().addCriteria(Criteria.where("dataId").is(dataId)),MYItemInfo.class,MongoTable.BANJIAN);
        String itemCode = myItemInfo.getCode();
        //获取材料信息
        String metailStr = lc.getMetails(itemCode);
        JSONArray array = JSONArray.fromObject(JSONObject.fromObject(metailStr).getString("ItemInfo"));
        JSONArray metail = new JSONArray();
        for (int i=0;i<array.size();i++) {
            JSONObject metailUnit = new JSONObject();
            JSONObject unit = array.getJSONObject(i);
            metailUnit.put("id",unit.getString("CODE"));
            metailUnit.put("name",unit.getString("NAME"));
            metail.add(metailUnit);
        }
        model.addAttribute("metail",metail);
        model.addAttribute("dataId",dataId);
        model.addAttribute("code",itemCode);
        return "item/metail";
    }

    @RequestMapping(value = "upanddisplay" ,method = RequestMethod.POST)
    @ResponseBody
    public String uploadItemMetailAndDisplay(@RequestParam("metail") MultipartFile multipartFile,@RequestParam("metailId") String metailId,
                                             @RequestParam("dataId") String dataId,HttpServletRequest request,@RequestParam("itemCode") String itemCode){
        Document metail = new Document();
        String username = (String) request.getSession().getAttribute("username");
        //先查找是否当前存在该材料id的doc
        Query query = new Query();
        query.addCriteria(Criteria.where("dataId").is(dataId).and("metailId").is(metailId));
        MetailInfo metailInfoOld = template.findOne(query, MetailInfo.class,MongoTable.ITEMMETAIL);
        String docid = JSONObject.fromObject(lc.upImage(multipartFile)).getString("docid");
        metail.put("username",username);
        metail.put("dataId",dataId);
        metail.put("code",itemCode);
        metail.put("metailId",metailId);
        metail.put("upTime",new Date());
        metail.put("docid",docid);
        metail.put("uuid",docid);
        if (metailInfoOld == null) {
            //不存在则进行添加
            template.insert(metail,MongoTable.ITEMMETAIL);
        }else {
            //存在，则进行更新
            template.remove(query,MongoTable.ITEMMETAIL);
            template.insert(metail,MongoTable.ITEMMETAIL);
        }
        JSONObject result = new JSONObject();
        result.put("docid",docid);
        result.put("metailId",metailId);
        return result.toString();
    }

    @GetMapping("metailimage")
    @ResponseBody
    public void metailImage(@RequestParam("docid") String docid, HttpServletResponse response){
        byte[] image = lc.loadImage(docid);
        try{
            OutputStream out = response.getOutputStream();
            out.write(image);
            out.flush();
            out.close();
        }catch (IOException e) {
            logger.debug("=========输出材料图片失败========");
        }

    }

    @GetMapping("deletemybanjian")
    public String deleteMyBanJian(@RequestParam("dataId") String dataId){

        //在模拟浪潮系统中删除办件信息
        lc.deleteData(dataId);
        //删除本地库中数据
        Query query = new Query();
        query.addCriteria(Criteria.where("dataId").is(dataId));
        template.remove(query,MongoTable.BANJIAN);
        //删除办件的材料
        template.remove(query,MongoTable.ITEMMETAIL);
        return "redirect:/item/banjian";
    }

    @GetMapping("updatemybanjian")
    public String updateMyBanJian(@RequestParam("dataId") String dataId,Model model){

        //获取办件表单信息
        Query query = new Query();
        query.addCriteria(Criteria.where("dataId").is(dataId));
        MYItemInfo myItemInfo = template.findOne(query,MYItemInfo.class,MongoTable.BANJIAN);
        String itemCode = myItemInfo.getCode();
        JSONObject data = myItemInfo.getData();
        //获取办件材料信息
        List<MetailInfo> metailInfos = template.find(query,MetailInfo.class,MongoTable.ITEMMETAIL);
        //获取该办件所有需要提交的材料信息
        String metails = lc.getMetails(itemCode);
        JSONArray array = JSONArray.fromObject(JSONObject.fromObject(metails).getString("ItemInfo"));
        JSONArray metailArr = new JSONArray();
        for (int i=0;i<array.size();i++) {
            JSONObject metailUnit = new JSONObject();
            JSONObject unit = array.getJSONObject(i);
            String metailId = unit.getString("CODE");
            metailUnit.put("id",metailId);
            metailUnit.put("name",unit.getString("NAME"));
            metailUnit.put("docid","");
            //此事项是否提交了材料
            for (MetailInfo metailInfo : metailInfos) {
                if (metailId.equalsIgnoreCase(metailInfo.getMetailId())) {
                    metailUnit.put("docid",metailInfo.getDocid());
                    break;
                }
            }
            metailArr.add(metailUnit);
        }
        model.addAttribute("myItemInfo",myItemInfo);
        model.addAttribute("metails",metailInfos);
        model.addAttribute("metailArr",metailArr);
        return "item/forms/update/"+itemCode;
    }

    @PostMapping("doupdateitem")
    @ResponseBody
    public String doUpdateMyBanJian(@RequestBody String data){

        JSONObject dataJSON = JSONObject.fromObject(data);
        String dataId = dataJSON.getString("dataId");
        dataJSON.remove("companyCode");
        dataJSON.remove("itemCode");
        dataJSON.remove("dataId");
        //将新的办件更新到模拟浪潮系统中
        lc.updateBanJian(dataId,data);
        //获取旧的办件信息
        Query query = new Query();
        query.addCriteria(Criteria.where("dataId").is(dataId));
        Document oldBanJian = template.findAndRemove(query,Document.class,MongoTable.BANJIAN);
        oldBanJian.put("data",dataJSON);
        oldBanJian.put("saveTime",new Date());
        template.insert(oldBanJian,MongoTable.BANJIAN);
        return "ok";
    }

    //办件申报
    @GetMapping("webapply")
    public String webapply(@RequestParam("dataId") String dataId,HttpServletRequest request){

        //获取此dataId的表单数据
        Query query = new Query();
        query.addCriteria(Criteria.where("dataId").is(dataId));
        Document myItemInfo = template.findOne(query,Document.class,MongoTable.BANJIAN);
        JSONObject dataJSON = JSONObject.fromObject(myItemInfo.get("data"));
        String itemCode = myItemInfo.getString("code");
        //申报到模拟浪潮系统,拿回receiveNum
        String receiveNum = lc.webapply(itemCode,dataJSON.toString());
        String username = (String) request.getSession().getAttribute("username");
        Document webapplyItem = new Document();
        webapplyItem.put("username",username);
        webapplyItem.put("receiveNum",receiveNum);
        webapplyItem.put("postdata",dataJSON);
        template.insert(webapplyItem,MongoTable.WEBAPPLYINFO);
        //更新我的办件信息
        template.remove(query,MongoTable.BANJIAN);
        myItemInfo.put("receiveNum",receiveNum);
        myItemInfo.put("applyTime",new Date());
        myItemInfo.put("hasApply",true);
        template.insert(myItemInfo,MongoTable.BANJIAN);
        return "redirect:/item/banjian";
    }

    @GetMapping("ratedetail")
    public String itemRateDetail(@RequestParam("dataId") String dataId,Model model){
        Query query = new Query();
        query.addCriteria(Criteria.where("dataId").is(dataId));
        Document item = template.findOne(query,Document.class,MongoTable.BANJIAN);
        model.addAttribute("itemName",item.getString("name"));
        List<JSONObject> states = new ArrayList<>();
        JSONObject save = new JSONObject();
        save.put("state","已保存");
        save.put("time",itemUtil.dateToString(item.getDate("saveTime")));
        save.put("option","已保存");
        states.add(save);
        boolean hasApply = item.getBoolean("hasApply");
        if (hasApply) {
            //已经申报
            JSONObject apply = new JSONObject();
            apply.put("state","已申报");
            apply.put("time",itemUtil.dateToString(item.getDate("applyTime")));
            apply.put("option","已申报");
            states.add(apply);
            JSONObject doing = new JSONObject();
            doing.put("state","处理中");
            doing.put("time",itemUtil.dateToString(new Date()));
            doing.put("option","处理中");
            states.add(doing);
        }
        model.addAttribute("items",states);
        return "item/displaystate";

    }
}
