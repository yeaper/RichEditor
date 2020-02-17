package com.yyp.editor.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yyp.editor.R;
import com.yyp.editor.adapter.FontSizeAdapter;
import com.yyp.editor.bean.FontSizeBean;
import com.yyp.editor.interfaces.OnFontSizeSelectListener;

import java.util.List;

/**
 * 文字大小选择弹窗
 *
 * Created by yyp on 2019.04.12
 */
public class FontSizeSelectDialog extends AlertDialog implements View.OnClickListener {

    private View mView;
    private TextView mTitle;
    private TextView mCancel;
    private RecyclerView mRvFontSize;
    private FontSizeAdapter mFontSizeAdapter;

    private OnFontSizeSelectListener mOnFontSizeSelectListener;

    public FontSizeSelectDialog(Context context) {
        super(context, android.R.style.Theme_Dialog);
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        configDialog();
    }

    private void configDialog() {
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        //设置对话框在底部
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mView = LayoutInflater.from(getContext()).inflate(
                R.layout.module_editor_layout_bottom_list_dialog, null);
        mTitle = mView.findViewById(R.id.tv_bottom_dialog_title);
        mCancel = mView.findViewById(R.id.tv_bottom_dialog_cancel);
        mRvFontSize = mView.findViewById(R.id.rv_bottom_dialog_list);

        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_bottom_dialog_cancel) {
            dismiss();
        }
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return FontSizeSelectDialog
     */
    public FontSizeSelectDialog setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
        return this;
    }

    /**
     * 设置文字大小选项集合
     *
     * @param fontSizeBeans 文字大小选项
     * @return FontSizeSelectDialog
     */
    public FontSizeSelectDialog setFontSizes(List<FontSizeBean> fontSizeBeans) {
        initFontSizeView(fontSizeBeans);
        return this;
    }

    /**
     * 初始化文字大小列表
     *
     * @param fontSizeBeans 文字大小数据集
     */
    private void initFontSizeView(List<FontSizeBean> fontSizeBeans){
        mRvFontSize.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvFontSize.addItemDecoration(new ListSpaceDivider(1, R.color._f0f0f0,
                10, 0, true, false, true)); //分割线
        mFontSizeAdapter = new FontSizeAdapter(fontSizeBeans);
        mRvFontSize.setAdapter(mFontSizeAdapter);
        mFontSizeAdapter.setOnFontSizeSelectListener(new OnFontSizeSelectListener() {

            @Override
            public void onFontSizeSelect(FontSizeBean bean, int pos) {
                if(mOnFontSizeSelectListener != null){
                    mOnFontSizeSelectListener.onFontSizeSelect(bean, pos);
                }
                dismiss(); //选完关闭弹窗
            }
        });
    }

    /**
     * 设置文字大小选择监听
     *
     * @param onFontSizeSelectListener 文字大小选择监听
     * @return FontSizeSelectDialog
     */
    public FontSizeSelectDialog setOnFontSizeSelectListener(OnFontSizeSelectListener onFontSizeSelectListener){
        this.mOnFontSizeSelectListener = onFontSizeSelectListener;
        return this;
    }
}