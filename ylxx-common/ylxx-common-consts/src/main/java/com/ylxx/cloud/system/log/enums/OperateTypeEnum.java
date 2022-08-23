package com.ylxx.cloud.system.log.enums;

/**
 * 
 * @ClassName: OperateTypeEnum 
 * @Description: rest接口请求操作类型
 * @author: caixiaopeng
 * @date: 2020年4月7日 下午10:03:08
 */
public enum OperateTypeEnum {
	
	INSERT		("01", "新增"),
	DELETE		("02", "删除"),
	UPDATE		("03", "修改"),
	QUERY		("04", "查询"),
	IMPORT		("05", "导入"),
	EXPORT		("06", "导出"),
	LOGIN		("07", "登录"),
	LOGOUT		("08", "注销"),
	UPLOAD		("09", "上传"),
	DOWNLOAD	("10", "下载"),
	REFRESH		("11", "刷新"),
	START		("12", "开始"),
	STOP		("13", "停止"),
	PUBLISH		("14", "发布"),
	UNPUBLISH	("15", "取消发布"),
	BACKUP		("16", "备份"),
	RECOVERY	("17", "恢复"),
	ASYNC		("18", "同步"),
	PASS		("19", "通过"),
	REFUSE		("20", "拒绝"),
	APPLY		("21", "申请"),
	DEPLOY		("22", "部署"),
	EXECUTE		("23", "执行"),
	SUSPEND		("24", "挂起"),
	ACTIVATE	("25", "激活"),
	SYNC        ("26", "同步"),

	UNKNOW		("99", "未知");


    private String value;
	
	private String description;
	
	OperateTypeEnum(final String value, final String description) {
		this.setValue(value);
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
	
}
