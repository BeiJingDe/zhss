package com.ylxx.cloud.enums;

/**
 * @author xldong
 * @version 2021年12月24日
 */

public enum ReportOperateEnum {

    REPORT_EDIT("report_edit", "re", "编辑"),
    REPORT_VIEW("report_view", "rv", "查看"),
    REPORT_PUBLISH("report_publish", "rp", "发布"),
    AUDIT("audit", "audit", "发布"),
    AUDIT_ITEM("audit_item", "ai", "发布"),
    MEETING_LEVEL("meeting_level", "ml", "会议查看");

    private String value;
    private String iscCode;
    private String description;

    ReportOperateEnum(String value, String iscCode, String description) {
        this.setValue(value);
        this.setIscCode(iscCode);
        this.setDescription(description);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIscCode() {
        return iscCode;
    }

    public void setIscCode(String iscCode) {
        this.iscCode = iscCode;
    }

    public static void main(String[] args) {
        ReportOperateEnum[] values = ReportOperateEnum.values();
        for (ReportOperateEnum reportOperateEnum : values) {
            System.out.println(reportOperateEnum.getValue());
        }
    }

}
