package com.yyp.editor.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yyp.editor.R;
import com.yyp.editor.adapter.FontColorAdapter;
import com.yyp.editor.bean.FontColorBean;
import com.yyp.editor.interfaces.OnColorSelectListener;

import java.util.List;

/**
 * 文字颜色选择弹窗
 *
 * Created by yyp on 2019.04.11
 */
public class ColorSelectDialog extends AlertDialog implements View.OnClickListener {

    private View mView;
    private TextView mTitle;
    private TextView mCancel;
    private RecyclerView mRvColor;
    private FontColorAdapter mFontColorAdapter;

    private OnColorSelectListener mOnColorSelectListener;

    public ColorSelectDialog(Context context) {
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
        mRvColor = mView.findViewById(R.id.rv_bottom_dialog_list);

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
     * @return ColorSelectDialog
     */
    public ColorSelectDialog setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
        return this;
    }

    /**
     * 设置颜色
     *
     * @param fontColorBeans 颜色组
     * @return ColorSelectDialog
     */
    public ColorSelectDialog setColors(List<FontColorBean> fontColorBeans) {
        initColorsView(fontColorBeans);
        return this;
    }

    /**
     * 初始化颜色列表
     *
     * @param fontColorBeans 颜色数据集
     */
    private void initColorsView(List<FontColorBean> fontColorBeans){
        mRvColor.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mFontColorAdapter = new FontColorAdapter(fontColorBeans);
        mRvColor.setAdapter(mFontColorAdapter);
        mRvColor.addItemDecoration(new FontColorItemDecoration(getContext()));
        mFontColorAdapter.setOnColorSelectListener(new OnColorSelectListener() {
            @Override
            public void onColorSelect(FontColorBean bean, int pos) {
                if(mOnColorSelectListener != null){
                    mOnColorSelectListener.onColorSelect(bean, pos);
                }
                dismiss(); //选完关闭弹窗
            }
        });
    }

    /**
     * 设置颜色选择监听
     *
     * @param onColorSelectListener 颜色选择监听
     * @return .
     */
    public ColorSelectDialog setOnColorSelectListener(OnColorSelectListener onColorSelectListener){
        this.mOnColorSelectListener = onColorSelectListener;
        return this;
    }
}