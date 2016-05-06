package com.timedancing.easyfirewall.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.timedancing.easyfirewall.animation.RevealAnimator;
import com.timedancing.easyfirewall.animation.SupportAnimator;
import com.timedancing.easyfirewall.animation.ViewAnimationUtils;


/**
 * Created by zengzheying on 15/7/10.
 */
public class RevealFrameLayout extends FrameLayout implements RevealAnimator {

	private final Rect mTargetBounds = new Rect();
	private Path mRevealPath;
	private RevealInfo mRevealInfo;
	private boolean mRunning;
	private float mRadius;

	public RevealFrameLayout(Context context) {
		this(context, null);
	}

	public RevealFrameLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RevealFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mRevealPath = new Path();
	}

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public RevealFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        mRevealPath = new Path();
//    }

	@Override
	public void onRevealAnimationStart() {
		mRunning = true;
	}

	@Override
	public void onRevealAnimationEnd() {
		mRunning = false;
		invalidate(mTargetBounds);
	}

	@Override
	public void onRevealAnimationCancel() {
		onRevealAnimationEnd();
	}

	@Override
	public float getRevealRadius() {
		return mRadius;
	}

	@Override
	public void setRevealRadius(float value) {
		mRadius = value;
		invalidate(mTargetBounds);
	}

	@Override
	public void attachRevealInfo(RevealInfo info) {
		info.getTarget().getHitRect(mTargetBounds);
		mRevealInfo = info;
	}

	@Override
	public SupportAnimator startReverseAnimation() {
		if (mRevealInfo != null && mRevealInfo.hasTarget() && !mRunning) {
			return ViewAnimationUtils.createCircularReveal(mRevealInfo.getTarget(),
					mRevealInfo.centerX, mRevealInfo.centerY,
					mRevealInfo.endRadius, mRevealInfo.startRadius);
		}
		return null;
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		if (mRunning && child == mRevealInfo.getTarget()) {
			final int state = canvas.save();

			mRevealPath.reset();
			mRevealPath.addCircle(mRevealInfo.centerX, mRevealInfo.centerY, mRadius, Path.Direction.CW);

			canvas.clipPath(mRevealPath);

			boolean isInvalided = super.drawChild(canvas, child, drawingTime);

			canvas.restoreToCount(state);

			return isInvalided;
		}
		return super.drawChild(canvas, child, drawingTime);
	}
}
