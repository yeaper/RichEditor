package com.yyp.editor.bean;

/**
 * 文字大小数据
 *
 * Created by yyp on 2019.04.12
 */
public class FontSizeBean {

    private int size; //实际大小 1-7
    private String textSize; //文字的显示大小 px为单位
    private boolean isSelect; //选中状态

    public FontSizeBean(){

    }

    public FontSizeBean(int size, String textSize, boolean isSelect) {
        this.size = size;
        this.textSize = textSize;
        this.isSelect = isSelect;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
