package com.timedancing.easyfirewall.animation;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.util.FloatProperty;

import java.lang.ref.WeakReference;

/**
 * Created by zengzheying on 15/7/10.
 */
public interface RevealAnimator {

	RevealRadius CLIP_RADIUS = new RevealRadius();

	void onRevealAnimationStart();

	void onRevealAnimationEnd();

	void onRevealAnimationCancel();

	float getRevealRadius();

	void setRevealRadius(float value);

	void invalidate(Rect bounds);

	void attachRevealInfo(RevealInfo info);

	SupportAnimator startReverseAnimation();

	class RevealInfo {
		public final int centerX;
		public final int centerY;
		public final float startRadius;
		public final float endRadius;
		public final WeakReference<View> target;

		public RevealInfo(int centerX, int centerY, float startRadius, float endRadius, WeakReference<View> target) {
			this.centerX = centerX;
			this.centerY = centerY;
			this.startRadius = startRadius;
			this.endRadius = endRadius;
			this.target = target;
		}

		public View getTarget() {
			return target.get();
		}

		public boolean hasTarget() {
			return getTarget() != null;
		}
	}

	class RevealFinishedGingerBread extends ViewAnimationUtils.SimpleAnimationListener {
		WeakReference<RevealAnimator> mReference;

		RevealFinishedGingerBread(RevealAnimator target) {
			mReference = new WeakReference<>(target);
		}

		@Override
		public void onAnimationStart(Animator animator) {
			RevealAnimator target = mReference.get();
			if (target != null) {
				target.onRevealAnimationStart();
			}
		}

		@Override
		public void onAnimationEnd(Animator animator) {
			RevealAnimator target = mReference.get();
			if (target != null) {
				target.onRevealAnimationEnd();
			}
		}

		@Override
		public void onAnimationCancel(Animator animator) {
			RevealAnimator target = mReference.get();
			if (target != null) {
				target.onRevealAnimationCancel();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	class RevealFinishedIceCreamSandwich extends RevealFinishedGingerBread {
		int mFeaturedLayerType;
		int mLayerType;

		public RevealFinishedIceCreamSandwich(RevealAnimator target) {
			super(target);

			mLayerType = ((View) target).getLayerType();
			mFeaturedLayerType = View.LAYER_TYPE_SOFTWARE;
		}

		@Override
		public void onAnimationStart(Animator animator) {
			View target = (View) mReference.get();
			if (target != null) {
				target.setLayerType(mFeaturedLayerType, null);
			}
			super.onAnimationStart(animator);
		}

		@Override
		public void onAnimationEnd(Animator animator) {
			View target = (View) mReference.get();
			if (target != null) {
				target.setLayerType(mLayerType, null);
			}
			super.onAnimationEnd(animator);
		}

		@Override
		public void onAnimationCancel(Animator animator) {
			View target = (View) mReference.get();
			if (target != null) {
				target.setLayerType(mLayerType, null);
			}
			super.onAnimationCancel(animator);
		}
	}

	class RevealFinishedJellyBeanMr2 extends RevealFinishedIceCreamSandwich {

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public RevealFinishedJellyBeanMr2(RevealAnimator target) {
			super(target);

			mFeaturedLayerType = View.LAYER_TYPE_HARDWARE;
		}
	}

	class RevealRadius extends FloatProperty<RevealAnimator> {

		public RevealRadius() {
			super("revealRadius");
		}

		@Override
		public void setValue(RevealAnimator object, float value) {
			object.setRevealRadius(value);
		}

		@Override
		public Float get(RevealAnimator object) {
			return object.getRevealRadius();
		}
	}
}
