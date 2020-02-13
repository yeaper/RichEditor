package com.yyp.editor.bean;

/**
 * 文字颜色数据
 *
 * Created by yyp on 2019.04.12
 */
public class FontColorBean {

    private int color; //色值
    private boolean isSelect; //选中状态

    public FontColorBean(){

    }

    public FontColorBean(int color, boolean isSelect) {
        this.color = color;
        this.isSelect = isSelect;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
