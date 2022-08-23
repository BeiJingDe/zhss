package com.ylxx.cloud.upms.login.model;


import com.ylxx.cloud.upms.menu.model.MenuVOTmp;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserIscInfo implements Serializable {

    List<MenuVOTmp> menus;

    List<String> buttons;

    public List<MenuVOTmp> getMenus() {
        if (menus == null) {
            menus = new ArrayList<>();
        }
        return menus;
    }
}