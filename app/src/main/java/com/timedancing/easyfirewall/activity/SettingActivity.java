package com.timedancing.easyfirewall.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.timedancing.easyfirewall.R;
import com.timedancing.easyfirewall.cache.AppConfig;
import com.timedancing.easyfirewall.receiver.PreventUninstallDeviceAdminReceiver;
import com.timedancing.easyfirewall.view.SettingItemView;

/**
 * Created by zengzheying on 16/1/18.
 */
public class SettingActivity extends AppCompatActivity {

	private static final int REQUEST_DEVICE_MANAGER_CODE = 118;
	private SettingItemView mSivRunWhenCompleted;
	private SettingItemView mSivPreventUninstall;
	private SettingItemView mSivFeedback;
	private SettingItemView mSivAbout;
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		mSivRunWhenCompleted = (SettingItemView) findViewById(R.id.siv_run_auto);
		mSivPreventUninstall = (SettingItemView) findViewById(R.id.siv_prevent_uninstall);
		mSivFeedback = (SettingItemView) findViewById(R.id.siv_feedback);
		mSivAbout = (SettingItemView) findViewById(R.id.siv_about);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setUpViews();
	}

	private void setUpViews() {

		mToolbar.setNavigationIcon(R.drawable.back);
		mToolbar.setTitle(R.string.setting);
		setSupportActionBar(mToolbar);

		mSivRunWhenCompleted.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSivRunWhenCompleted.setChecked(!mSivRunWhenCompleted.isChecked());
				AppConfig.setShouldAutoRunWhenBootCompleted(SettingActivity.this, mSivRunWhenCompleted.isChecked());
			}
		});

		mSivPreventUninstall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireAdmin();
			}
		});

		mSivFeedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, FeedbackActivity.class);
				startActivity(intent);
			}
		});

		mSivAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_DEVICE_MANAGER_CODE) {
			mSivPreventUninstall.setChecked(resultCode == RESULT_OK);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSivRunWhenCompleted.setChecked(AppConfig.isShouldAutoRunWhenBootCompleted(this));
		DevicePolicyManager manager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mSivPreventUninstall.setChecked(manager.isAdminActive(new ComponentName(this,
				PreventUninstallDeviceAdminReceiver.class)));
	}

//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		startActivity(intent);
//
//		finish();
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void requireAdmin() {
		ComponentName componentName = new ComponentName(SettingActivity.this,
				PreventUninstallDeviceAdminReceiver.class);
		DevicePolicyManager manager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		if (!manager.isAdminActive(componentName)) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(SettingActivity.this,
					PreventUninstallDeviceAdminReceiver.class));
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string
					.admin_request_description));

			startActivityForResult(intent, REQUEST_DEVICE_MANAGER_CODE);
		} else {
			manager.removeActiveAdmin(componentName);
			mSivPreventUninstall.setChecked(false);
		}
	}
}
