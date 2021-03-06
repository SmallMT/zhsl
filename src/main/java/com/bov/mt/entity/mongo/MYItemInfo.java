package com.bov.mt.entity.mongo;

import net.sf.json.JSONObject;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Map;

public class MYItemInfo {
    private ObjectId _id;
    private String _class;
    private String username;
    private String companyCode;
    private String code;
    private String name;
    private String gsj;
    private Date saveTime;
    private String time;
    private JSONObject data;
    private String dataId;
    private String receiveNum;
    private boolean hasApply;

    public boolean isHasApply() {
        return hasApply;
    }

    public void setHasApply(boolean hasApply) {
        this.hasApply = hasApply;
    }

    public String getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(String receiveNum) {
        this.receiveNum = receiveNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGsj() {
        return gsj;
    }

    public void setGsj(String gsj) {
        this.gsj = gsj;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
