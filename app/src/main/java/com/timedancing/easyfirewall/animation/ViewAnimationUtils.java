package com.timedancing.easyfirewall.animation;

import android.os.Build;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.lang.ref.WeakReference;

/**
 * Created by zengzheying on 15/7/10.
 */
public class ViewAnimationUtils {

//    private final static boolean LOLLIPOP_PLUS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

	public static final int SCALE_UP_DURATION = 500;

	//@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static SupportAnimator createCircularReveal(View view, int centerX, int centerY, float startRadius, float
			endRadius) {
		if (!(view.getParent() instanceof RevealAnimator)) {
			throw new IllegalArgumentException("View must be inside RevealFrameLayout or RevealLinearLayout.");
		}

		RevealAnimator revealLayout = (RevealAnimator) view.getParent();
		revealLayout.attachRevealInfo(new RevealAnimator.RevealInfo(centerX, centerY, startRadius, endRadius, new
				WeakReference<>(view)));

//        if (LOLLIPOP_PLUS){
//            android.animation.Animator revealAnimator = android.view.ViewAnimationUtils.createCircularReveal(view,
// centerX, centerY, startRadius, endRadius);
//            return new SupportAnimatorLollipop(revealAnimator, revealLayout);
//        }

		ObjectAnimator reveal = ObjectAnimator.ofFloat(revealLayout, RevealAnimator.CLIP_RADIUS, startRadius,
				endRadius);
		reveal.addListener(getRevealFinishListener(revealLayout));
		return new SupportAnimatorPreL(reveal, revealLayout);
	}

	private static Animator.AnimatorListener getRevealFinishListener(RevealAnimator target) {
		if (Build.VERSION.SDK_INT >= 18) {
			return new RevealAnimator.RevealFinishedJellyBeanMr2(target);
		} else if (Build.VERSION.SDK_INT >= 14) {
			return new RevealAnimator.RevealFinishedIceCreamSandwich(target);
		} else {
			return new RevealAnimator.RevealFinishedGingerBread(target);
		}
	}

	static class SimpleAnimationListener implements Animator.AnimatorListener {
		@Override
		public void onAnimationStart(Animator animation) {

		}

		@Override
		public void onAnimationEnd(Animator animation) {

		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {

		}
	}

}
