package com.yyp.editor.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 图片处理工具
 *
 * Created by yyp on 2019/02/25
 */
public class ImageUtils {

    /**
     * bitmap 转 base64
     *
     * @param bitmap bitmap对象
     * @return base64字符串
     */
    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * Drawable 转 Bitmap
     *
     * @param drawable Drawable对象
     * @return Bitmap对象
     */
    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 根据图片资源id获取Bitmap
     *
     * @param context 上下文
     * @param resId 图片资源id
     * @return Bitmap对象
     */
    public static Bitmap decodeResource(Context context,@IdRes int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    /**
     * int色值转16进制色值
     *
     * @param color int色值
     * @return 16进制色值
     */
    public static String convertHexColorString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    /**
     * 将Drawable改成对应状态、颜色的Drawable集合
     *
     * @param d 原Drawable
     * @param color 对应状态的颜色集合
     */
    public static void setTintList(Drawable d, ColorStateList color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTintList(wrappedDrawable, color);
    }

    /**
     * dip转换px
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
