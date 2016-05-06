package com.timedancing.easyfirewall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.timedancing.easyfirewall.R;
import com.timedancing.easyfirewall.cache.AppConfig;
import com.timedancing.easyfirewall.constant.AppDebug;
import com.timedancing.easyfirewall.view.NumberKeyboard;

/**
 * Created by zengzheying on 16/1/13.
 */
public class UnlockActivity extends AppCompatActivity {

	private NumberKeyboard mNumberKeyboard;
	private EditText mEditText;
	private TextView mTvHint;

	private boolean isNeedSetPassword;
	private String mFirstPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock);

		mEditText = (EditText) findViewById(R.id.et_pwd);
		mTvHint = (TextView) findViewById(R.id.tv_hint);
		mNumberKeyboard = (NumberKeyboard) findViewById(R.id.numberKeyboard);

		setUpMta();
		setUpViews();

		AppConfig.setShouldShowGuidePage(this, false);
	}

	private void setUpViews() {

		isNeedSetPassword = TextUtils.isEmpty(AppConfig.getLockPassword(this));

		if (isNeedSetPassword) {
			mTvHint.setText(R.string.set_password);
		} else {
			mTvHint.setText(R.string.input_password);
		}

		mNumberKeyboard.setKeyboardInputListener(new NumberKeyboard.OnKeyboardInputListener() {
			@Override
			public void onKeyboardInput(int number) {
				mEditText.append(String.valueOf(number));
			}
		});

		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				validate(s);
			}
		});

	}

	private void validate(final Editable s) {
		if (s.length() == 4) {
			if (!isNeedSetPassword && s.toString().equals(AppConfig.getLockPassword(this))) {
				navigateToMainActivity();
			} else {
				if (isNeedSetPassword) {
					if (TextUtils.isEmpty(mFirstPassword)) {
						mFirstPassword = s.toString();
						mTvHint.setText(R.string.confirm_password);
					} else {
						if (mFirstPassword.equals(s.toString())){
							AppConfig.setLockPassword(this, mFirstPassword);
							navigateToMainActivity();
							return;
						} else {
							mTvHint.setText(R.string.set_password_again);
							mFirstPassword = null;
						}
					}
				} else {
					mTvHint.setText(R.string.wrong_password);
				}
				mEditText.postDelayed(new Runnable() {
					@Override
					public void run() {
						s.clear();
					}
				}, 100);
			}
		}
	}

	private void navigateToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void setUpMta() {
		StatConfig.setDebugEnable(AppDebug.IS_DEBUG);

		StatService.registerActivityLifecycleCallbacks(getApplication());
	}
}
