package com.ylxx.cloud.upms.login.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xldong
 * @version 2022年3月4日
 */

public enum BoardModuleEnum {

    SEARCH("search", "搜索入口"),
    KEY_INDEX("key_index", "关键业绩指标"),
    NEW_REPORT("new_report", "最新数说"),
    HOT_REPORT("hot_report", "热门数说"),
    RANK_CITY("rank_city", "地市公司负责人关键业绩"),
    RANK_BM_VISIT("rank_bm_visit", "访问部门排行");


    private String code;
    private String name;

    BoardModuleEnum(final String value, final String description) {
        this.setCode(value);
        this.setName(description);
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

    public static String getName(String code) {
        for (BoardModuleEnum enums : BoardModuleEnum.values()) {
            if (enums.getCode().equalsIgnoreCase(code)) {
                return enums.getName();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> codeList = new ArrayList<>();
        BoardModuleEnum[] codes = BoardModuleEnum.values();
        for(BoardModuleEnum enums:codes){
            System.out.println(enums.getCode());
            codeList.add(enums.getCode());
        }
    }

}
