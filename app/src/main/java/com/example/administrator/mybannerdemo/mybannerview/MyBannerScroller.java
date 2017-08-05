package com.example.administrator.mybannerdemo.mybannerview;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by lifengmei on 2017/8/4.
 * 设置切换动画的时间间隔
 */

public class MyBannerScroller extends Scroller {
    private int mDuration = 800;

    public MyBannerScroller(Context context) {
        super(context);
    }

    public MyBannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public MyBannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDuration(int time) {
        mDuration = time;
    }

}

