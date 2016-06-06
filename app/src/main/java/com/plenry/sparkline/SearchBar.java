package com.plenry.sparkline;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by xiaoguangmo on 6/5/16.
 */
public class SearchBar extends RelativeLayout {
    private static final int SLIDE_UP = 0;
    public static final int TOP=0;
    public static final int BOTTOM=1;
    private ListView mListView;
    private int mTouchSlop;
    private ObjectAnimator mAnimator;
    private static final int SLIDE_DOWN = 1;
    private float mEndY;
    private float mStartY;
    private boolean mShow;
    private int mScreenHeight;
    private float mInitialY;

    public SearchBar(Context context) {
        super(context);
        init();
    }

    private void init() {
        mShow = true;
        this.setVisibility(INVISIBLE);
        //得到系统认为滑动操作的最短滑动距离
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenHeight = dm.heightPixels;
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    private void startAnimBottom(int action) {
        if (mAnimator != null && mAnimator.isRunning())
            mAnimator.cancel();
        if (action == SLIDE_DOWN) {
            //mToolbar.getTranslationY()返回顶部对应的Y坐标值
            mAnimator = ObjectAnimator.ofFloat(this,
                    "translationY", this.getTranslationY(), 0);
        } else {
            //隐藏bar
            mAnimator = ObjectAnimator.ofFloat(this,
                    "translationY", this.getTranslationY(),
                    -this.getHeight());
        }
        mAnimator.start();
    }

    private void startAnimTop(int action) {
        if (mAnimator != null && mAnimator.isRunning())
            mAnimator.cancel();
        if (action == SLIDE_DOWN) {
            mAnimator = ObjectAnimator.ofFloat(this, "translationY", this.getTranslationY(), mScreenHeight);
        } else {
            mAnimator = ObjectAnimator.ofFloat(this, "translationY", this.getTranslationY(), mInitialY);
        }
        mAnimator.start();
    }

    public void bindWithListview(ListView listView, final int style) {
        mListView = listView;
        mInitialY = getTranslationY();
        if (mListView != null) {
            mListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            mEndY = event.getY();
                            if (Math.abs(mEndY - mStartY) > mTouchSlop) {
                                show();
                                //下滑
                                if (!mShow && mEndY - mStartY > 0) {
                                    if (style == TOP) {
                                        startAnimTop(SLIDE_DOWN);
                                    }else startAnimBottom(SLIDE_UP);
                                    mShow = !mShow;
                                } //上滑
                                else if (mShow && mEndY - mStartY < 0) {
                                    if (style == TOP) {
                                        startAnimTop(SLIDE_UP);
                                    }else startAnimBottom(SLIDE_DOWN);
                                    mShow = !mShow;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            mEndY = event.getY();
                            if (Math.abs(mEndY - mStartY) > mTouchSlop) {
                                show();
                                //下滑
                                if (!mShow && mEndY - mStartY > 0) {
                                    if (style == 0) {
                                        startAnimTop(SLIDE_DOWN);
                                    }else startAnimBottom(SLIDE_UP);
                                    mShow = !mShow;
                                } //上滑
                                else if (mShow && mEndY - mStartY < 0) {
                                    if (style == 0) {
                                        startAnimTop(SLIDE_UP);
                                    }else startAnimBottom(SLIDE_DOWN);
                                    mShow = !mShow;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_DOWN:
                            mStartY = event.getY();
                            break;
                    }
                    return false;
                }
            });
        }
    }
}
