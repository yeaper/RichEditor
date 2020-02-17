package com.yyp.editor.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyp.editor.utils.ImageUtils;

/**
 * 文字颜色列表分割线
 *
 * Created by yyp on 2019.04.12
 */
public class FontColorItemDecoration extends RecyclerView.ItemDecoration {

    private int mPx20;
    private int mPx10;

    public FontColorItemDecoration(Context context) {
        mPx20 = ImageUtils.dip2px(context, 20);
        mPx10 = ImageUtils.dip2px(context, 10);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
        if(isFirstRaw(parent, spanCount, view)){ //第一行
            outRect.set(mPx10, mPx20, mPx10, mPx10);
        }else if(isLastRaw(parent, spanCount, childCount, view)){ //最后一行
            outRect.set(mPx10, mPx10, mPx10, mPx20);
        }else { //其他行
            outRect.set(mPx10, mPx10, mPx10, mPx10);
        }

    }

    /**
     * 获取列数
     *
     * @param parent RecyclerView
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    /**
     * 是否第一行
     *
     * @param parent
     * @param spanCount
     * @param view
     * @return
     */
    private boolean isFirstRaw(RecyclerView parent, int spanCount, View view) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int pos = layoutManager.getPosition(view);
        if (layoutManager instanceof GridLayoutManager) {
            return pos < spanCount;
        }
        return false;
    }

    /**
     * 是否最后一行
     *
     * @param parent
     * @param spanCount
     * @param childCount
     * @param view
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int spanCount, int childCount, View view) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int pos = layoutManager.getPosition(view);
        if (layoutManager instanceof GridLayoutManager) {
            return ((pos >= (childCount - spanCount)) && pos < childCount);
        }
        return false;
    }

}
