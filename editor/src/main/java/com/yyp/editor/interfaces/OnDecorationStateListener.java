package com.yyp.editor.interfaces;

import com.yyp.editor.config.EditorOpType;

import java.util.List;

/**
 * 状态改变的回调接口
 *
 * Created by yyp on 2019/02/25.
 */
public interface OnDecorationStateListener {

    /**
     * 操作状态改变回调
     *
     * @param text 所有状态的字符串
     * @param types 所有状态的集合
     */
    void onStateChange(String text, List<EditorOpType> types);
}