package com.timedancing.easyfirewall.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import com.timedancing.easyfirewall.R;

/**
 * Created by zengzheying on 16/1/13.
 */
public class NumberKeyboard extends FrameLayout implements View.OnClickListener {

	private int mWidth;
	private int mTextSize;

	private OnKeyboardInputListener mKeyboardInputListener;

	public NumberKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24.0f, dm);
	}

	public NumberKeyboard(Context context) {
		this(context, null);
	}

	private void init(Context context) {
		GridLayout gridLayout = new GridLayout(context);
		addView(gridLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
				.MATCH_PARENT));
		gridLayout.setColumnCount(3);
		gridLayout.setRowCount(4);
		for (int i = 1; i <= 10; i++) {
			GridLayout.Spec row;
			GridLayout.Spec col;
			if (i == 10) {
				row = GridLayout.spec(3);
				col = GridLayout.spec(1);
			} else {
				row = GridLayout.spec((i - 1) / 3);
				col = GridLayout.spec((i - 1) % 3);
			}
			GridLayout.LayoutParams lp = new GridLayout.LayoutParams(row, col);
			lp.width = mWidth / 3;
			lp.height = mWidth / 4;
			TextView textView = new TextView(getContext());
			textView.setText(String.valueOf(i % 10));
			textView.setGravity(Gravity.CENTER);
			textView.setClickable(true);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(mTextSize);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
			textView.setBackgroundResource(R.drawable.selector_keyboard_number);
			textView.setTag(i != 10 ? i : 0);
			textView.setOnClickListener(this);
			gridLayout.addView(textView, lp);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		mWidth = Math.min(width, height);
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		removeAllViews();
		init(getContext());
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void onClick(View v) {
		int number = (Integer)v.getTag();
		if (mKeyboardInputListener != null) {
			mKeyboardInputListener.onKeyboardInput(number);
		}
	}

	public void setKeyboardInputListener(OnKeyboardInputListener keyboardInputListener) {
		mKeyboardInputListener = keyboardInputListener;
	}

	public interface OnKeyboardInputListener {
		void onKeyboardInput(int number);
	}
}
