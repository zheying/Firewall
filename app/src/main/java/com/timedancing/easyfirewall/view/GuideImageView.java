package com.timedancing.easyfirewall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zengzheying on 16/1/22.
 */
public class GuideImageView extends ImageView {

	public GuideImageView(Context context) {
		super(context);
	}

	public GuideImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GuideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int newHeight = (int) (width * (826f / 518f));
		if (newHeight < height) {
			height = newHeight;
		} else {
			width = (int) (height * (518f / 826f));
		}
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
