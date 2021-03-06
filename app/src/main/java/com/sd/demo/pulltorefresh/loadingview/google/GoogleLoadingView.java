package com.sd.demo.pulltorefresh.loadingview.google;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.sd.lib.pulltorefresh.PullToRefreshView;
import com.sd.lib.pulltorefresh.loadingview.BaseLoadingView;

/**
 * Created by Administrator on 2017/7/26.
 */

public class GoogleLoadingView extends BaseLoadingView
{
    public GoogleLoadingView(@NonNull Context context)
    {
        super(context);
        init();
    }

    public GoogleLoadingView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public GoogleLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static final int[] DEFAULT_SCHEME_COLORS = {Color.parseColor("#3F51B5"),
            Color.parseColor("#FF4081")};

    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    private static final float MAX_PROGRESS_ANGLE = .8f;

    private MaterialProgressDrawable mProgress;
    private CircleImageView mCircleView;

    private void init()
    {
        setBackgroundColor(Color.BLACK);
        createProgressDrawable();
        createCircleImageView();
    }

    private void createProgressDrawable()
    {
        mProgress = new MaterialProgressDrawable(getContext(), this);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        mProgress.setAlpha(255);
        mProgress.setColorSchemeColors(DEFAULT_SCHEME_COLORS);
    }

    private void createCircleImageView()
    {
        mCircleView = new CircleImageView(getContext(), CIRCLE_BG_LIGHT);
        mCircleView.setImageDrawable(mProgress);

        LayoutParams params = generateDefaultLayoutParams();
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.topMargin = (int) (getResources().getDisplayMetrics().density * 10);
        params.bottomMargin = params.topMargin;
        addView(mCircleView, params);
    }

    public void setColorSchemeColors(int... colors)
    {
        mProgress.setColorSchemeColors(DEFAULT_SCHEME_COLORS);
    }

    @Override
    public void onViewPositionChanged(PullToRefreshView view)
    {
        super.onViewPositionChanged(view);

        float scrollDistance = Math.abs(view.getScrollDistance());
        float dragPercent = Math.min(1f, scrollDistance / (float) getRefreshingHeight());
        float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;
        float strokeStart = adjustedPercent * .8f;
        float startAngle = 0f;
        float endAngle = Math.min(MAX_PROGRESS_ANGLE, strokeStart);
        mProgress.setStartEndTrim(startAngle, endAngle);

        float arrowScale = Math.min(1f, adjustedPercent);
        mProgress.setArrowScale(arrowScale);

        float rotation = (-0.25f + .4f * adjustedPercent) * .5f;
        mProgress.setProgressRotation(rotation);
    }

    @Override
    public void onStateChanged(PullToRefreshView.State newState, PullToRefreshView.State oldState, PullToRefreshView view)
    {
        switch (newState)
        {
            case RESET:
                mProgress.stop();
                break;
            case PULL_TO_REFRESH:
                mProgress.showArrow(true);
                break;
            case RELEASE_TO_REFRESH:
                break;
            case REFRESHING:
                mProgress.start();
                break;
            case REFRESHING_SUCCESS:
                break;
            case REFRESHING_FAILURE:
                break;
            case FINISH:
                mProgress.stop();
                break;
        }
    }
}
