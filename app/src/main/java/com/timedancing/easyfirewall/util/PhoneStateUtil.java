package com.timedancing.easyfirewall.util;

import android.content.Context;
import android.content.pm.PackageManager;

import com.timedancing.easyfirewall.constant.AppDebug;

/**
 * Created by zengzheying on 16/1/20.
 */
public class PhoneStateUtil {

	public static String getVersionName(Context context) {
		String result = "";

		try {
			result = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0)
					.versionName;
		} catch (PackageManager.NameNotFoundException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}

		return result;
	}

	public static int getVersionCode(Context context) {
		int result = 0;

		try {
			result = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0)
					.versionCode;
		} catch (PackageManager.NameNotFoundException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}

		return result;
	}

}
