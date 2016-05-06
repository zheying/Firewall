package com.timedancing.easyfirewall.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.timedancing.easyfirewall.util.PhoneStateUtil;

/**
 * Created by zengzheying on 16/1/14.
 */
public class AppConfig {

	private static final String IS_NEED_SHOW_TIPS = "isNeedShowTips";
	private static final String SHOULD_AUTO_RUN_WHEN_BOOT_COMPLETED = "shouldAutoRunWhenBootCompleted";
	private static final String LOCK_PASSWORD = "lockPassword";
	private static final String SHOW_GUIDE_PAGE = "shouldShowGuidePage";

	public static void setIsNeedShowTips(Context context, boolean isNeedShowTips) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(IS_NEED_SHOW_TIPS, isNeedShowTips);
		editor.apply();
	}


	public static boolean isNeedShowTips(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_NEED_SHOW_TIPS, true);
	}

	public static void setShouldAutoRunWhenBootCompleted(Context context, boolean autoRun) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(SHOULD_AUTO_RUN_WHEN_BOOT_COMPLETED, autoRun);
		editor.apply();
	}

	public static boolean isShouldAutoRunWhenBootCompleted(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(SHOULD_AUTO_RUN_WHEN_BOOT_COMPLETED, true);
	}

	public static String getLockPassword(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(LOCK_PASSWORD, "");
	}

	public static void setLockPassword(Context context, String lockPassword) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(LOCK_PASSWORD, lockPassword);
		editor.apply();
	}

	public static boolean isShouldShowGuidePage(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String key = String.format("%s_%s", SHOW_GUIDE_PAGE, PhoneStateUtil.getVersionName(context));
		return sp.getBoolean(key, true);
	}

	public static void setShouldShowGuidePage(Context context, boolean isShouldShowGuidePage) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		String key = String.format("%s_%s", SHOW_GUIDE_PAGE, PhoneStateUtil.getVersionName(context));
		editor.putBoolean(key, isShouldShowGuidePage);
		editor.apply();
	}
}
