package com.ylxx.cloud.upms.user.model;

import lombok.Data;

@Data
public class BaseOrg {
    private String id;
    private String orgName;
    private String orgType;
    private String parentId;
    private String orgCode;
    private String dataId;
    //private Integer sortNo;
    private String remarks;
    private String location;
    private String fax;
    private String phone;
}
