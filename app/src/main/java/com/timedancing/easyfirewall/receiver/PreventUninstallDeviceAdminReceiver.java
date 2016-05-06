package com.timedancing.easyfirewall.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.timedancing.easyfirewall.R;

/**
 * Created by zengzheying on 16/1/18.
 */
public class PreventUninstallDeviceAdminReceiver extends DeviceAdminReceiver {

	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		return context.getString(R.string.warning_when_disable_admin_manager);
	}
}
