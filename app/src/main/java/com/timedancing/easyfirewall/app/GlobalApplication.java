package com.timedancing.easyfirewall.app;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.tencent.stat.StatService;
import com.timedancing.easyfirewall.constant.ApiConstant;
import com.timedancing.easyfirewall.core.ProxyConfig;

import java.util.Properties;

/**
 * Created by zengzheying on 16/1/7.
 */
public class GlobalApplication extends Application {

	private static GlobalApplication sInstance;

	public static GlobalApplication getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
//		if (BuildConfig.DEBUG) {
//			StrictMode.VmPolicy vmPolicy = null;
//			if (Build.VERSION.SDK_INT >= 16) {
//				vmPolicy = new StrictMode.VmPolicy.Builder()
//						.detectActivityLeaks()
//						.detectLeakedSqlLiteObjects()
//						.detectLeakedClosableObjects()
//						.detectLeakedRegistrationObjects()
//						.penaltyLog()
//						.build();
//			} else {
//				vmPolicy = new StrictMode.VmPolicy.Builder()
//						.detectActivityLeaks()
//						.detectLeakedSqlLiteObjects()
//						.detectLeakedClosableObjects()
//						.penaltyLog()
//						.build();
//			}
//			StrictMode.setVmPolicy(vmPolicy);
//		}
		super.onCreate();
//		LeakCanary.install(this);
		sInstance = this;

		AVOSCloud.initialize(this, ApiConstant.LEANCLOUND_APP_ID, ApiConstant.LEANCLOUD_APP_KEY);

		ProxyConfig.Instance.setVpnStatusListener(new StatusListener());
	}


	static class StatusListener implements ProxyConfig.VpnStatusListener {

		Properties mProperties = new Properties();

		@Override
		public void onVpnStart(Context context) {
			StatService.trackCustomBeginKVEvent(context, "VPN_OPEN", mProperties);
		}

		@Override
		public void onVpnEnd(Context context) {
			StatService.trackCustomEndKVEvent(context, "VPN_OPEN", mProperties);
		}
	}
}
