package com.timedancing.easyfirewall.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.timedancing.easyfirewall.R;
import com.timedancing.easyfirewall.animation.SupportAnimator;
import com.timedancing.easyfirewall.animation.ViewAnimationUtils;
import com.timedancing.easyfirewall.cache.AppCache;
import com.timedancing.easyfirewall.cache.AppConfig;
import com.timedancing.easyfirewall.core.util.VpnServiceHelper;
import com.timedancing.easyfirewall.event.HostUpdateEvent;
import com.timedancing.easyfirewall.event.VPNEvent;
import com.timedancing.easyfirewall.network.HostHelper;
import com.timedancing.easyfirewall.util.DebugLog;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

	private View mImgStart;
	private View mImgEnd;
	private TextView mTvRun;
	private View mRippleView;
	private View mTipsView;
	private View mMaskView;
	private View mSettingView;

	private ProgressDialog mProgressDialog = null;
	private ProgressDialog mUpdateProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			// clear FLAG_TRANSLUCENT_STATUS flag:
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			window.setStatusBarColor(Color.parseColor("#00000000"));

			window.getDecorView()
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}

		setContentView(R.layout.activity_main);

		mImgEnd = findViewById(R.id.img_end);
		mImgStart = findViewById(R.id.img_start);
		mRippleView = findViewById(R.id.rippleView);
		mTvRun = (TextView) findViewById(R.id.btn_run);
		mMaskView = findViewById(R.id.blackMask);
		mTipsView = findViewById(R.id.tipsLayout);
		mSettingView = findViewById(R.id.img_setting);
		mTvRun.setSelected(VpnServiceHelper.vpnRunningStatus());
		mRippleView.setSelected(mTvRun.isSelected());
		mTvRun.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConfig.isNeedShowTips(MainActivity.this)) {
					AppConfig.setIsNeedShowTips(MainActivity.this, false);
					mMaskView.setVisibility(View.GONE);
					mTipsView.setVisibility(View.GONE);
				}


				boolean nextStatus = !mTvRun.isSelected();
				if (!nextStatus) { //如果是关闭操作，立即更新界面
					changeButtonStatus(false);
				}
				startAnimation(nextStatus);
				if (VpnServiceHelper.vpnRunningStatus() != nextStatus) {
					VpnServiceHelper.changeVpnRunningStatus(MainActivity.this, nextStatus);
				}

			}
		});

		mMaskView.setVisibility(AppConfig.isNeedShowTips(this) ? View.VISIBLE : View.GONE);
		mTipsView.setVisibility(AppConfig.isNeedShowTips(this) ? View.VISIBLE : View.GONE);

		mSettingView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});

		mProgressDialog = new ProgressDialog(MainActivity.this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage(getString(R.string.preparing));

		mUpdateProgressDialog = new ProgressDialog(MainActivity.this);
		mUpdateProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mUpdateProgressDialog.setMessage(getString(R.string.updating_config));

		AppCache.syncBlockCountWithLeanCloud(this);

		if (!VpnServiceHelper.vpnRunningStatus()) {
			HostHelper.updateHost(this);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onDestroy() {
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mProgressDialog = null;
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VpnServiceHelper.START_VPN_SERVICE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				VpnServiceHelper.startVpnService(this);
			} else {
				changeButtonStatus(false);
				DebugLog.e("canceled");
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (VpnServiceHelper.vpnRunningStatus()) {
			mImgStart.setVisibility(View.GONE);
			mTvRun.setText(R.string.shutdown);
		} else {
			mImgStart.setVisibility(View.VISIBLE);
			mTvRun.setText(R.string.start);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().registerSticky(this);
	}

	private void startAnimation(boolean isRunning) {
		SupportAnimator animator = null;
		int width = mImgStart.getWidth() / 2;
		int height = mImgStart.getHeight() / 2;
		double longRadius = Math.sqrt((width * width) + (height * height));
		if (isRunning) {
			animator = ViewAnimationUtils.createCircularReveal(mImgStart, width, height, (float) longRadius, 0);
			animator.addListener(new SupportAnimator.SimpleAnimationListener() {
				@Override
				public void onAnimationStart() {
					mImgStart.setVisibility(View.VISIBLE);
				}

				@Override
				public void onAnimationEnd() {
					mImgStart.setVisibility(View.GONE);
				}
			});
		} else {
			animator = ViewAnimationUtils.createCircularReveal(mImgStart, width, height, 0, (float) longRadius);
			animator.addListener(new SupportAnimator.SimpleAnimationListener() {
				@Override
				public void onAnimationEnd() {
					mImgStart.setVisibility(View.VISIBLE);
				}

				@Override
				public void onAnimationStart() {
					mImgStart.setVisibility(View.VISIBLE);
				}
			});
		}

		animator.setDuration(350);
		animator.start();
	}

	@SuppressWarnings("unused")
	public void onEventMainThread(VPNEvent event) {
		boolean selected = event.isEstablished();

		switch (event.getStatus()) {
			case STARTING:
				mProgressDialog.show();
				break;
			default:
				changeButtonStatus(selected);
				mTvRun.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (mProgressDialog.isShowing()) {
							mProgressDialog.dismiss();
						}
					}
				}, 500);
				break;
		}

	}

	@SuppressWarnings("unused")
	public void onEventMainThread(HostUpdateEvent event) {
		switch (event.getStatus()) {
			case Updating:
				mUpdateProgressDialog.show();
				break;
			case UpdateFinished:
				mUpdateProgressDialog.dismiss();
				break;
		}
	}

	private void changeButtonStatus(boolean isRunning) {
		mTvRun.setSelected(isRunning);
		mTvRun.setText(mTvRun.isSelected() ? R.string.shutdown : R.string.start);
		mRippleView.setSelected(mTvRun.isSelected());
	}
}
