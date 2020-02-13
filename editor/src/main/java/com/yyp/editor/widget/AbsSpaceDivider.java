package com.yyp.editor.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

public abstract class AbsSpaceDivider extends RecyclerView.ItemDecoration {

    private int mColorId = NO_ATTR_ID;
    protected int mCurrColor = Color.TRANSPARENT;

    protected static final int NO_ATTR_ID = -1;

    protected AbsSpaceDivider(@ColorInt int color) {
        mCurrColor = color;
    }

    protected AbsSpaceDivider(@ColorRes @ColorInt int colorOrColorId, boolean isColorId) {
        if (isColorId) {
            mColorId = colorOrColorId;
        } else {
            mCurrColor = colorOrColorId;
        }
    }

    /**
     * 获取当前日夜间模式分割线的颜色
     *
     * @return Color的int值
     */
    public int getUiModeColor(Context context) {
        if (context != null && mColorId != NO_ATTR_ID) {
            return ContextCompat.getColor(context, mColorId);
        }
        return mCurrColor;
    }

    protected int dp2px(float dp) {
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                Resources.getSystem().getDisplayMetrics()
        ) + 0.5f);
    }

}