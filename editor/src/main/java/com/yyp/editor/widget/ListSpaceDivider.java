package com.yyp.editor.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 自定义RecyclerView的线性布局分割线
 *
 * create at 2019/3/18 上午9:03
 */
public class ListSpaceDivider extends AbsSpaceDivider {
    protected int mDividerHeight = 0;
    protected int mLeftMargin = 0;
    protected int mRightMargin = 0;
    // 画笔
    protected Paint mPaint;
    protected boolean mIsHorizontal = true;
    protected boolean mIncludeLastItem = true;

    /**
     * 默认0.5dp 的间距, 没有颜色
     */
    public ListSpaceDivider() {
        this(0.5, Color.TRANSPARENT, false);
    }

    /**
     * @param heightDip      分割线高度
     * @param colorOrColorId 分割线color或colorId
     * @param isColorId      true:表示是colorId；false:表示是Color
     */
    public ListSpaceDivider(double heightDip, int colorOrColorId, boolean isColorId) {
        this(heightDip, colorOrColorId, true, isColorId);
    }

    /**
     * @param heightDip      分割线高度
     * @param colorOrColorId 分割线color或colorId
     * @param isHorizontal   是否为水平方向
     * @param isColorId      true:表示是colorId；false:表示是Color
     */
    public ListSpaceDivider(double heightDip, int colorOrColorId, boolean isHorizontal,
                            boolean isColorId) {
        this(heightDip, colorOrColorId, 0, isHorizontal, isColorId);
    }

    /**
     * @param heightDip     分割线高度
     * @param margin        左右两边的边距
     * @param colorOrAttrId 分割线color或attrId
     * @param isHorizontal  是否为水平方向
     * @param isColorId     true:表示是colorId；false:表示是Color
     */
    public ListSpaceDivider(double heightDip, int colorOrAttrId, float margin, boolean isHorizontal,
                            boolean isColorId) {
        this(heightDip, colorOrAttrId, margin, margin, isHorizontal, true, isColorId);
    }

    /**
     * @param heightDip       分割线高度
     * @param colorOrColorId  分割线color或colorId
     * @param leftMargin      左边距
     * @param rightMargin     右边距
     * @param isHorizontal    是否为水平方向
     * @param includeLastItem 是否包括最后一条
     * @param isColorId       true:表示是colorId；false:表示是Color
     */
    public ListSpaceDivider(double heightDip, int colorOrColorId, float leftMargin,
                            float rightMargin, boolean isHorizontal,
                            boolean includeLastItem, boolean isColorId) {
        super(colorOrColorId, isColorId);
        mDividerHeight = dp2px((float) heightDip);
        if (isColorId || colorOrColorId != Color.TRANSPARENT) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true); // 设置画笔抗锯齿
        }
        mLeftMargin = dp2px(leftMargin);
        mRightMargin = dp2px(rightMargin);
        mIsHorizontal = isHorizontal;
        mIncludeLastItem = includeLastItem;
    }

    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        if (parent.getAdapter() == null) return;
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int footerCount = 0;
        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position == RecyclerView.NO_POSITION) {
                continue;
            }
            if (!mIncludeLastItem && position == itemCount - 1 - footerCount) {
                continue;
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDividerHeight;
            c.drawRect(left + mLeftMargin, top, right - mRightMargin, bottom, mPaint);
        }
    }

    protected void drawVertical(Canvas c, RecyclerView parent) {
        if (parent.getAdapter() == null) return;
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        int footerCount = 0;
        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (!mIncludeLastItem && position == itemCount - 1 - footerCount) {
                continue;
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDividerHeight;
            c.drawRect(left, top + mLeftMargin, right, bottom - mRightMargin, mPaint);
        }
    }

    /**
     * 绘制
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mPaint == null) return;
        mPaint.setColor(getUiModeColor(parent.getContext())); // 设置颜色
        if (mIsHorizontal) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    /**
     * 在绘制ItemDivider之前,需要调用此方法。以便RecyclerView给该条目预留空间,供我们绘制Divider
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State
            state) {
        int position = parent.getChildAdapterPosition(view);
        if (!mIncludeLastItem) {
            if (position == parent.getAdapter().getItemCount() - 1) {
                return;
            }
        }

        if (mIsHorizontal) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            outRect.set(0, 0, mDividerHeight, 0);
        }
    }
}