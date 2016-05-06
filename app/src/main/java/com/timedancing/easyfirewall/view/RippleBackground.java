package com.timedancing.easyfirewall.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.timedancing.easyfirewall.R;

import java.util.ArrayList;

/**
 * Created by fyu on 11/3/14.
 */

public class RippleBackground extends RelativeLayout {

	private static final int DEFAULT_RIPPLE_COUNT = 6;
	private static final int DEFAULT_DURATION_TIME = 3000;
	private static final float DEFAULT_SCALE = 6.0f;
	private static final int DEFAULT_FILL_TYPE = 0;

	private int rippleColor;
	private int rippleColorSelected;
	private float rippleStrokeWidth;
	private float rippleRadius;
	private int rippleDurationTime;
	private int rippleAmount;
	private int rippleDelay;
	private float rippleScale;
	private int rippleType;

	private boolean animationRunning = false;
	private AnimatorSet animatorSet;
	private ArrayList<Animator> animatorList;
	private LayoutParams rippleParams;
	private ArrayList<RippleView> rippleViewList = new ArrayList<RippleView>();

	public RippleBackground(Context context) {
		super(context);
	}

	public RippleBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public RippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(final Context context, final AttributeSet attrs) {
		if (isInEditMode())
			return;

		if (null == attrs) {
			throw new IllegalArgumentException("Attributes should be provided to this view,");
		}

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground);
		rippleColor = typedArray.getColor(R.styleable.RippleBackground_rb_color,
				getResources().getColor(R.color.rippelColor));
		rippleColorSelected = typedArray.getColor(R.styleable.RippleBackground_rb_color_selected,
				getResources().getColor(R.color.rippelColor));
		rippleStrokeWidth = typedArray.getDimension(R.styleable.RippleBackground_rb_strokeWidth, getResources()
				.getDimension(R.dimen.rippleStrokeWidth));
		rippleRadius = typedArray.getDimension(R.styleable.RippleBackground_rb_radius,
				getResources().getDimension(R.dimen.rippleRadius));
		rippleDurationTime = typedArray.getInt(R.styleable.RippleBackground_rb_duration, DEFAULT_DURATION_TIME);
		rippleAmount = typedArray.getInt(R.styleable.RippleBackground_rb_rippleAmount, DEFAULT_RIPPLE_COUNT);
		rippleScale = typedArray.getFloat(R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE);
		rippleType = typedArray.getInt(R.styleable.RippleBackground_rb_type, DEFAULT_FILL_TYPE);
		typedArray.recycle();

		rippleDelay = rippleDurationTime / rippleAmount;

		rippleParams = new LayoutParams((int) (2 * (rippleRadius + rippleStrokeWidth)),
				(int) (2 * (rippleRadius + rippleStrokeWidth)));
		rippleParams.addRule(CENTER_IN_PARENT, TRUE);

		animatorSet = new AnimatorSet();
		animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
		animatorList = new ArrayList<Animator>();
		for (int i = 0; i < rippleAmount; i++) {
			RippleView rippleView = new RippleView(getContext());
			rippleView.setTag(i);
			addView(rippleView, rippleParams);
			rippleViewList.add(rippleView);
			final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, rippleScale);
			scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
			scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
			scaleXAnimator.setStartDelay(i * rippleDelay);
			scaleXAnimator.setDuration(rippleDurationTime);
			animatorList.add(scaleXAnimator);
			final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, rippleScale);
			scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
			scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
			scaleYAnimator.setStartDelay(i * rippleDelay);
			scaleYAnimator.setDuration(rippleDurationTime);
			animatorList.add(scaleYAnimator);
			final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleView, "Alpha", 1.0f, 0f);
			alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
			alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
			alphaAnimator.setStartDelay(i * rippleDelay);
			alphaAnimator.setDuration(rippleDurationTime);

//			alphaAnimator.addListener(new AnimatorListenerAdapter() {
//				@Override
//				public void onAnimationRepeat(Animator animation) {
//					changeRippleColor(animation);
//				}
//
//				@Override
//				public void onAnimationStart(Animator animation) {
//					changeRippleColor(animation);
//				}
//
//				private void changeRippleColor(Animator animation) {
//					if (animation instanceof ObjectAnimator) {
//						ObjectAnimator objectAnimator = (ObjectAnimator) animation;
//						if (objectAnimator.getTarget() instanceof RippleView) {
//							RippleView view = (RippleView) objectAnimator.getTarget();
//							view.setRippleColor(isSelected() ? rippleColorSelected : rippleColor);
//						}
//					}
//				}
//			});

			animatorList.add(alphaAnimator);
		}

		animatorSet.playTogether(animatorList);

		startRippleAnimation();
	}

	public void startRippleAnimation() {
		if (!isRippleAnimationRunning()) {
			for (RippleView rippleView : rippleViewList) {
				rippleView.setVisibility(VISIBLE);
			}
			animatorSet.start();
			animationRunning = true;
		}
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);

		for (int i = 0; i < getChildCount(); i++) {
			changeRippleColor(getChildAt(i));
		}
	}

	public void stopRippleAnimation() {
		if (isRippleAnimationRunning()) {
			animatorSet.end();
			animationRunning = false;
		}
	}

	public boolean isRippleAnimationRunning() {
		return animationRunning;
	}

	private void changeRippleColor(View view) {
		if (view != null && view instanceof RippleView) {
			((RippleView) view).setRippleColor(isSelected() ? rippleColorSelected : rippleColor);
		}
	}

	private class RippleView extends View {

		private Paint paint;

		private int mRippleColor;

		public RippleView(Context context) {
			super(context);
			this.setVisibility(View.INVISIBLE);

			paint = new Paint();
			paint.setAntiAlias(true);
			if (rippleType == DEFAULT_FILL_TYPE) {
				rippleStrokeWidth = 0;
				paint.setStyle(Paint.Style.FILL);
			} else
				paint.setStyle(Paint.Style.STROKE);
			paint.setColor(rippleColor);
			mRippleColor = rippleColor;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			int radius = (Math.min(getWidth(), getHeight())) / 2;
			canvas.drawCircle(radius, radius, radius - rippleStrokeWidth, paint);
		}

		private void setRippleColor(int rippleColor) {
			if (rippleColor == mRippleColor) {
				return;
			}
			paint.setColor(rippleColor);
			mRippleColor = rippleColor;
			invalidate();
		}
	}


}
