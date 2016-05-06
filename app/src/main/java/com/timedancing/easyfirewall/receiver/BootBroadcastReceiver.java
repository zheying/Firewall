package com.timedancing.easyfirewall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.timedancing.easyfirewall.cache.AppConfig;
import com.timedancing.easyfirewall.core.util.VpnServiceHelper;
import com.timedancing.easyfirewall.util.DebugLog;

/**
 * Created by zengzheying on 16/1/18.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (AppConfig.isShouldAutoRunWhenBootCompleted(context)) {
			DebugLog.d("Auto run when boot completed!");
			VpnServiceHelper.changeVpnRunningStatus(context, true);
		}
	}
}
