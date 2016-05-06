package com.timedancing.easyfirewall.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.timedancing.easyfirewall.constant.ApiConstant;
import com.timedancing.easyfirewall.constant.AppDebug;

/**
 * Created by zengzheying on 16/1/20.
 */
public class AppCache {

	public static final String KEY_IF_SINCE_MODIFIED_SINCE = "If-Modified-Since";
	private static final String BLOCK_COUNT = "blockCount";
	private static final long MIN_INCREMENT_GAP = 10 * 1000L * 1000L * 1000L;  // 10秒之内不要重复计数
	private static long lastIncrementNanoTime = 0;

	public static void setBlockCount(Context context, int blockCount) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(BLOCK_COUNT, blockCount);
		editor.apply();
	}

	public static int getBlockCount(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(BLOCK_COUNT, 1);
	}


	public static void syncBlockCountWithLeanCloud(final Context context) {
		try {
			AVQuery<AVObject> query = new AVQuery<>("BlockInfo");
			query.getInBackground(ApiConstant.BLOCK_COUNT_ID, new GetCallback<AVObject>() {
				@Override
				public void done(AVObject avObject, AVException e) {
					if (avObject == null) {
						if (AppDebug.IS_DEBUG) {
							e.printStackTrace(System.err);
						}
						return;
					}
					int count = avObject.getLong("count") > Integer.MAX_VALUE ? Integer.MAX_VALUE - 1 : (int) avObject
							.getLong("count");
					setBlockCount(context, count + 1);
				}
			});
		} catch (Exception ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public static void syncAndIncreaseBlockWithLeanCloud(final Context context) {
		try {
			AVQuery<AVObject> query = new AVQuery<>("BlockInfo");
			long nanoTime = System.nanoTime();
			// 10秒之内不要重复计数
			if ((nanoTime - lastIncrementNanoTime) > MIN_INCREMENT_GAP) {
				query.getInBackground(ApiConstant.BLOCK_COUNT_ID, new GetCallback<AVObject>() {
					@Override
					public void done(AVObject avObject, AVException e) {
						if (avObject == null) {
							if (AppDebug.IS_DEBUG) {
								e.printStackTrace(System.err);
							}
							return;
						}
						int count = avObject.getLong("count") > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) avObject
								.getLong("count");
						avObject.increment("count");
						avObject.saveInBackground();
						setBlockCount(context, count + 1);
					}
				});
				lastIncrementNanoTime = nanoTime;
			}
		} catch (Exception ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public static void setIfSinceModifiedSince(Context context, String ifSinceModifiedSince) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(KEY_IF_SINCE_MODIFIED_SINCE, ifSinceModifiedSince);
		editor.apply();
	}

	public static String getIfSinceModifiedSince(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(KEY_IF_SINCE_MODIFIED_SINCE, "Sun, 24 Jan 2016 14:17:36 GMT"); //raw中host文件版本
	}

}
