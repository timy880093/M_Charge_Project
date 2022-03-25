package com.gate.web.beans;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/8/8
 * Time: AM 11:22
 * To change this template use File | Settings | File Templates.
 */
public class MenuBean {
    private String id;
    private String name;
    private String url;
    private String enable;
    private String pop;
    private List<MenuBean> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public List<MenuBean> getChildren() {
        return children;
    }

    public void setChildren(List<MenuBean> children) {
        this.children = children;
    }


}
