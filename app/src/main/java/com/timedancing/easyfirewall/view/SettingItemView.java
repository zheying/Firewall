package com.timedancing.easyfirewall.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timedancing.easyfirewall.R;

/**
 * Created by zengzheying on 16/1/18.
 */
public class SettingItemView extends RelativeLayout {

	private static final int CHECK_INVISIBLE = 0;
	private static final int CHECK_VISIBLE = 1;

	private View mCheckView;
	private TextView mTvTitle;

	public SettingItemView(Context context) {
		this(context, null);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.layout_setting_item, this, true);
		mCheckView = findViewById(R.id.v_check);
		mTvTitle = (TextView) findViewById(R.id.tv_title);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
		String title = typedArray.getString(R.styleable.SettingItemView_siv_title);
		boolean checked = typedArray.getBoolean(R.styleable.SettingItemView_siv_checked, false);
		int checkVisible = typedArray.getInt(R.styleable.SettingItemView_siv_check_visibility, CHECK_VISIBLE);
		typedArray.recycle();

		mTvTitle.setText(title);
		setChecked(checked);
		if (checkVisible == CHECK_VISIBLE) {
			mCheckView.setVisibility(VISIBLE);
		} else {
			mCheckView.setVisibility(GONE);
		}
	}

	public boolean isChecked() {
		return mCheckView.isSelected();
	}

	public void setChecked(boolean isChecked) {
		mCheckView.setSelected(isChecked);
	}
}
