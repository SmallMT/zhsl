package com.bov.mt.entity.mongo;

import org.bson.types.ObjectId;

import java.util.Date;

public class MetailInfo {
    private ObjectId _id;
    private String username; //用户名
    private String dataId;//dataId
    private String code;//事项编码
    private String metailId;//材料id
    private Date upTime;//上传时间
    private String docid;
    private String uuid;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMetailId() {
        return metailId;
    }

    public void setMetailId(String metailId) {
        this.metailId = metailId;
    }

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
