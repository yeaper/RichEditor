package com.yyp.editor.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyp.editor.R;
import com.yyp.editor.bean.FontSizeBean;
import com.yyp.editor.interfaces.OnFontSizeSelectListener;

import java.util.List;

/**
 * 文字大小适配器
 *
 * Created by yyp on 2019.04.12
 */
public class FontSizeAdapter extends RecyclerView.Adapter<FontSizeAdapter.FontSizeHolder> {

    private List<FontSizeBean> mData;
    private OnFontSizeSelectListener mOnFontSizeSelectListener;

    public FontSizeAdapter(List<FontSizeBean> data) {
        this.mData = data;
    }

    /**
     * 设置文字大小item点击监听
     *
     * @param onFontSizeSelectListener .
     */
    public void setOnFontSizeSelectListener(OnFontSizeSelectListener onFontSizeSelectListener) {
        this.mOnFontSizeSelectListener = onFontSizeSelectListener;
    }

    @NonNull
    @Override
    public FontSizeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FontSizeHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.module_editor_item_font_size, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FontSizeHolder fontColorHolder, @SuppressLint("RecyclerView") final int pos) {
        final FontSizeBean bean = mData.get(pos);
        fontColorHolder.mFontSize.setText(String.format("%spx", bean.getTextSize()));
        fontColorHolder.mFontSizeSelect.setVisibility(bean.isSelect() ? View.VISIBLE : View.GONE);
        fontColorHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(pos);
                notifyDataSetChanged();
                //颜色回调
                if(mOnFontSizeSelectListener != null){
                    mOnFontSizeSelectListener.onFontSizeSelect(bean, pos);
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
        for(FontSizeBean bean: mData){
            bean.setSelect(false);
        }
        mData.get(pos).setSelect(true);
    }

    /**
     * 文字大小holder
     */
    class FontSizeHolder extends RecyclerView.ViewHolder{

        TextView mFontSize;
        ImageView mFontSizeSelect;

        FontSizeHolder(@NonNull View itemView) {
            super(itemView);
            mFontSize = itemView.findViewById(R.id.tv_font_size);
            mFontSizeSelect = itemView.findViewById(R.id.iv_font_size_select);
        }
    }
}
