package com.yyp.editor.bean;

import com.yyp.editor.config.MaterialsMenuType;

/**
 * 素材菜单数据
 *
 * Created by yyp on 2019.04.11
 */
public class MaterialsMenuBean {

    private MaterialsMenuType id; //菜单id
    private int imgResId; //图片资源id
    private String title; //标题

    public MaterialsMenuBean(MaterialsMenuType id, int imgResId, String title) {
        this.id = id;
        this.imgResId = imgResId;
        this.title = title;
    }

    public MaterialsMenuType getId() {
        return id;
    }

    public void setId(MaterialsMenuType id) {
        this.id = id;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
