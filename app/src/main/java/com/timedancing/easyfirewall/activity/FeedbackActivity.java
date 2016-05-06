package com.timedancing.easyfirewall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.stat.StatFBDispatchCallback;
import com.tencent.stat.StatService;
import com.timedancing.easyfirewall.R;
import com.timedancing.easyfirewall.util.DebugLog;

/**
 * Created by zengzheying on 16/1/19.
 */
public class FeedbackActivity extends AppCompatActivity {

	private EditText mETAdvise;
	private EditText mETConnect;
	private Toolbar mToolbar;
	private TextView mTvSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feedback);

		mETAdvise = (EditText) findViewById(R.id.edit_advise);
		mETConnect = (EditText) findViewById(R.id.edit_connect);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mTvSubmit = (TextView) findViewById(R.id.tv_submit);

		mTvSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submit();
			}
		});

		setUpViews();
	}

	private void setUpViews() {
		mToolbar.setTitle(R.string.setting_feedback);
		mToolbar.setNavigationIcon(R.drawable.back);
		setSupportActionBar(mToolbar);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void submit() {
		String contect = mETConnect.getText().toString().trim();
		if (TextUtils.isEmpty(contect)) {
			contect = "匿名用户";
		}
		String content = mETAdvise.getText().toString().trim();
		if (!TextUtils.isEmpty(content)) {

			StatService.postFeedBackFiles(this, String.format("%s: %s", contect, content), null, new
					StatFBDispatchCallback() {


				@Override
				public void onFBDispatch(String s) {
					DebugLog.d(s);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							finish();
						}
					});
				}
			});

		}
	}
}
