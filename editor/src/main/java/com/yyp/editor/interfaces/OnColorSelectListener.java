package com.yyp.editor.interfaces;

import com.yyp.editor.bean.FontColorBean;

/**
 * 颜色选择回调接口
 *
 * Created by yyp on 2019.04.11
 */
public interface OnColorSelectListener {

    void onColorSelect(FontColorBean bean, int pos);
}
