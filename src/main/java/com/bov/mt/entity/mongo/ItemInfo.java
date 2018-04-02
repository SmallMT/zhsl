package com.bov.mt.entity.mongo;

import org.bson.types.ObjectId;

public class ItemInfo {
    private ObjectId _id;
    private String code;
    private String name;
    private String formId;
    private String gsj;
    private String itemId;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getGsj() {
        return gsj;
    }

    public void setGsj(String gsj) {
        this.gsj = gsj;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
