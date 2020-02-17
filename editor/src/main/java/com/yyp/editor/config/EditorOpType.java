package com.yyp.editor.config;

/**
 * 操作类型枚举类
 *
 * Created by yyp on 2019/02/25.
 */
public enum EditorOpType {
    BOLD,
    ITALIC,
    UNDERLINE,
    FORECOLOR,
    FONTSIZE,
    JUSTIFYLEFT,
    JUSTIFYRIGHT,
    JUSTIFYCENTER,
    ORDEREDLIST,
    IMAGE,
    LINK;

    private Object mValue = null;

    public Object getValue() {
        return this.mValue;
    }

    public void setValue(Object mValue) {
        switch(this) {
            case FONTSIZE:
                this.mValue = Integer.parseInt(mValue.toString()
                        .replaceAll("[\\D]", "")); //把非数字字符替换成空的
                break;
            case FORECOLOR:
                this.mValue = "#" + mValue.toString().split("#")[1]; //拿到16进制色值
        }
    }
}