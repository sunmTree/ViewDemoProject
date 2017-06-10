package com.sm.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by sm on 17-2-23.
 */

public class ScrollView extends View {

    private Paint mPaint;
    private int mRectWidth = 20;

    private float lastX, lastY;
    private Scroller mScroller;

    public ScrollView(Context context) {
        this(context,null);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(30);
        mPaint.setStrokeWidth(3f);

        mScroller = new Scroller(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        String cp = "∙∙";
        canvas.drawText(cp,width,height,mPaint);
//        Rect mr = new Rect(width - mRectWidth, height - mRectWidth,width + mRectWidth, height+mRectWidth);
//        canvas.drawRect(mr,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) (x - lastX);
                int offsetY = (int) (y - lastY);
//                layout(getLeft()+offsetX,getTop()+offsetY,
//                        getRight()+offsetX,getBottom()+offsetY);   1、移动布局  四个边界

//                offsetLeftAndRight(offsetX);
//                offsetTopAndBottom(offsetY);              ２、平移布局      四个边界

//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
//                layoutParams.leftMargin = getLeft()+offsetX;
//                layoutParams.topMargin = getTop()+offsetY;
//                setLayoutParams(layoutParams);　　　         ３、改变布局参数，两个边界

                ((View)getParent()).scrollTo(-offsetX,-offsetY);
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void smoothScroller(int destX, int destY){
        int scaleX = (int) getScaleX();
        int delat =(destX - scaleX);
        mScroller.startScroll(scaleX,0,delat,0,5000);
        invalidate();
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller != null){
            if (mScroller.computeScrollOffset()){
                ((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
                invalidate();
            }
        }
    }
}
