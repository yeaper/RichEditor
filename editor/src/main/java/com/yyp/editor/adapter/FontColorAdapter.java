package com.yyp.editor.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyp.editor.R;
import com.yyp.editor.bean.FontColorBean;
import com.yyp.editor.interfaces.OnColorSelectListener;
import com.yyp.editor.widget.ColorCircleView;

import java.util.List;

/**
 * 字体颜色适配器
 *
 * Created by yyp on 2019.04.12
 */
public class FontColorAdapter extends RecyclerView.Adapter<FontColorAdapter.FontColorHolder> {

    private List<FontColorBean> mData;
    private OnColorSelectListener mOnColorSelectListener;

    public FontColorAdapter(List<FontColorBean> data) {
        this.mData = data;
    }

    /**
     * 设置颜色item点击监听
     *
     * @param onColorSelectListener .
     */
    public void setOnColorSelectListener(OnColorSelectListener onColorSelectListener) {
        this.mOnColorSelectListener = onColorSelectListener;
    }

    @NonNull
    @Override
    public FontColorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FontColorHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.module_editor_item_font_color, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FontColorHolder fontColorHolder, @SuppressLint("RecyclerView") final int pos) {
        final FontColorBean bean = mData.get(pos);
        fontColorHolder.mColorCircleView.setColor(bean.getColor());
        fontColorHolder.mColorCircleView.setSelect(bean.isSelect());
        fontColorHolder.mColorCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(pos);
                notifyDataSetChanged();
                //颜色回调
                if(mOnColorSelectListener != null){
                    mOnColorSelectListener.onColorSelect(bean, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    /**
     * 设置只有一个为选中
     *
     * @param pos 选中的位置
     */
    private void setSelect(int pos){
        for(FontColorBean bean: mData){
            bean.setSelect(false);
        }
        mData.get(pos).setSelect(true);
    }

    /**
     * 文字颜色holder
     */
    class FontColorHolder extends RecyclerView.ViewHolder{

        ColorCircleView mColorCircleView;

        FontColorHolder(@NonNull View itemView) {
            super(itemView);
            mColorCircleView = itemView.findViewById(R.id.ccv_font_color);
        }
    }
}
