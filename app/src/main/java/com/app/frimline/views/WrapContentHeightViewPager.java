package com.app.frimline.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class WrapContentHeightViewPager extends ViewPager {
    private int mCurrentPagePosition = 0;

    public WrapContentHeightViewPager(Context context) {
        super(context);
        initPageChangeListener();
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPageChangeListener();
    }

    private void initPageChangeListener() {
        addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        int height = 0;
//        for (int i = 0; i < getChildCount(); ++i) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            if (h > height) {
//                height = h;
//            }
//        }
//        heightMeasureSpec = (MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int height = 0;

        View child = getChildAt(getCurrentItem());
        if (child != null) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            //if (h > height) {
            height = h;
            //}

            heightMeasureSpec = (MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }


}