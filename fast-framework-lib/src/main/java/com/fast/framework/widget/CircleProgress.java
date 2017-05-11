/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.widget;

import com.fast.framework.util.DensityUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形进度条
 * <p>
 * ps: 圆环会自动根据宽度高度取小边的大小的比例 你也可以自己设置圆环的大小@see #scrollBy(int, int)
 */
public class CircleProgress extends View {

    private int width; // 控件的宽度
    private int height; // 控件的高度
    private int radius; // 圆形的半径

    private Paint paint = new Paint();
    private RectF rectf = new RectF();
    private Rect rec = new Rect();

    private int progressColor;
    private int progressColorComplete;
    private int progressColorCenter;
    private int progressWidth;

    private int startAngle = 270;
    private float paddingScale = 1.0f;// 控件内偏距占空间本身的比例

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        progressColor = Color.parseColor("#ffffff");// 进度条未完成的颜色
        progressColorComplete = Color.parseColor("#000000");// 进度条已完成的颜色
        progressColorCenter = Color.parseColor("#CCCCCC");// 圆中间的背景颜色
        progressWidth = DensityUtil.dp2px(context, 4); // 圆环进度条的宽度

        textSize = DensityUtil.dp2px(context, 8); // 文字大小
        textColor = Color.parseColor("#6bb849"); // 文字颜色
    }

    @Override
    protected void onDraw(Canvas canvas) {

        width = getWidth();

        int size = height = getHeight();
        if (height > width) {
            size = width;
        }

        radius = (int) (size * paddingScale / 2f);
        paint.setAntiAlias(true);

        // 绘制进度的环
        paint.setColor(progressColor);
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        // 绘制当前进度
        rectf.set((width - radius * 2) / 2f, (height - radius * 2) / 2f, ((width - radius * 2) / 2f) + (2 * radius),
                  ((height - radius * 2) / 2f) + (2 * radius));
        paint.setColor(progressColorComplete);
        //canvas.drawArc(rectf, startAngle, value * 3.6f, true, paint);
        canvas.drawArc(rectf, startAngle, currentPosition, true, paint);

        // 绘制中间部分
        paint.setColor(progressColorCenter);
        canvas.drawCircle(width / 2, height / 2, radius - progressWidth, paint);

        if (bitmap != null) {
            // 绘制中间的图片
            int width2 = (int) (rectf.width() * scale);
            int height2 = (int) (rectf.height() * scale);
            rectf.set(rectf.left + width2, rectf.top + height2, rectf.right - width2, rectf.bottom - height2);
            canvas.drawBitmap(bitmap, null, rectf, null);
        }

        if (!hiddenText) {
            // 绘制中间文字
            String text = currentPosition + "%";
            paint.setColor(textColor);
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), rec);

            // 绘制中间文字
            canvas.drawText(text, (width - rec.width()) / 2, ((height + rec.height()) / 2), paint);
        }

        super.onDraw(canvas);
    }

    /**
     * 设置圆环进度条的宽度 px
     */
    public void setProdressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置开始的位置
     *
     * @param startAngle 0~360
     *                   <p>
     *                   ps 0代表在最右边 90 最下方 按照然后顺时针旋转
     */
    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    /**
     * 设置进度条之前的颜色
     *
     * @param precolor
     */
    public void setProgressColor(int precolor) {
        this.progressColor = precolor;
    }

    /**
     * 设置进度颜色
     *
     * @param color
     */
    public void setProgressColorComplete(int color) {
        this.progressColorComplete = color;
    }

    /**
     * 设置圆心中间的背景颜色
     *
     * @param color
     *
     * @return
     */
    public void setProgressColorCenter(int color) {
        this.progressColorCenter = color;
    }

    // ------------------------------ 显示进度 ------------------------------
    private float currentPosition = 0;
    private int duration;

    /**
     * 设置进度
     *
     * @param currentPosition
     */
    public void setCurrentDuration(int currentPosition) {
        if (duration > 0) {
            if (currentPosition > duration) {
                return;
            }
            this.currentPosition = currentPosition * 360f / duration;
            invalidate();
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // ------------------------------ 中间文字 ------------------------------
    private int textSize; // 文字大小
    private int textColor; // 文字颜色
    private boolean hiddenText;

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        textSize = textSize;
    }

    /**
     * 设置文字大小
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setHiddenText(boolean hiddenText) {
        this.hiddenText = hiddenText;
    }

    // ------------------------------ 中间图片 ------------------------------
    private Bitmap bitmap; // 中间图片
    private float scale = 0.15f; // 中间背景图片相对圆环的大小的比例

    /**
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * @param scale
     */
    public void setPaddingScale(float scale) {
        this.paddingScale = scale;
    }

}
