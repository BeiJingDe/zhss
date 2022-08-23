package com.ylxx.cloud.upms.login.model;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class IscRule implements Serializable {


    List<String> menus;

    List<String> buttons;

    List<String> dps;


    public List<String> getMenus() {
        if (menus == null) {
            menus = new ArrayList<>();
        }
        return menus;
    }

    public List<String> getButtons() {
        if (buttons == null) {
            buttons = new ArrayList<>();
        }
        return buttons;
    }

    public List<String> getDps() {
        if (dps == null) {
            dps = new ArrayList<>();
        }
        return dps;
    }
}