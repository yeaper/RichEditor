package com.yyp.editor.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yyp.editor.utils.ImageUtils;

/**
 * 颜色圆形控件
 *
 * Created by yyp on 2019.04.12
 */
public class ColorCircleView extends View {

    private int mColor; //颜色
    private boolean mSelect; //是否选中

    private Paint mColorPaint; //圆形颜色画笔
    private Paint mRightPaint; //对号画笔
    private Path mRightPath;
    private int mCircleRadius; //圆半径

    public ColorCircleView(Context context) {
        this(context, null);
    }

    public ColorCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        mRightPath = new Path();
        mColorPaint = new Paint();
        mColorPaint.setStyle(Paint.Style.FILL); //实心
        mColorPaint.setAntiAlias(true); //抗锯齿
        mRightPaint = new Paint();
        mRightPaint.setStyle(Paint.Style.STROKE); //空心
        mRightPaint.setAntiAlias(true);
        mRightPaint.setStrokeWidth(ImageUtils.dip2px(getContext(), 2));
        mRightPaint.setColor(Color.WHITE);
    }

    /**
     * 设置圆的颜色
     *
     * @param color 色值
     */
    public void setColor(int color){
        this.mColor = color;
        invalidate();
    }

    /**
     * 设置是否选中
     *
     * @param select 选中状态
     */
    public void setSelect(boolean select){
        this.mSelect = select;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCircleRadius = Math.min(getWidth(), getHeight()) / 2; //拿到视图圆的半径
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        mColorPaint.setColor(mColor);
        canvas.drawCircle(mCircleRadius, mCircleRadius, mCircleRadius, mColorPaint);
        //画对号
        if(mSelect){
            mRightPath.moveTo(mCircleRadius - 10, mCircleRadius);
            mRightPath.lineTo(mCircleRadius, mCircleRadius + 10);
            mRightPath.lineTo(mCircleRadius + 18, mCircleRadius - 10);
            canvas.drawPath(mRightPath, mRightPaint);
        }
    }
}
