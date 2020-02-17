package com.yyp.editor.interfaces;

import com.yyp.editor.bean.FontSizeBean;

/**
 * 文字大小选择回调接口
 *
 * Created by yyp on 2019.04.11
 */
public interface OnFontSizeSelectListener {

    void onFontSizeSelect(FontSizeBean bean, int pos);
}
