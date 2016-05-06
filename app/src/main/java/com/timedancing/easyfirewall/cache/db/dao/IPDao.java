package com.timedancing.easyfirewall.cache.db.dao;

import android.app.Application;
import android.content.Context;

import com.timedancing.easyfirewall.cache.db.bean.IP;
import com.timedancing.easyfirewall.cache.db.helper.DatabaseHelper;
import com.timedancing.easyfirewall.constant.AppDebug;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zengzheying on 16/1/15.
 */
public class IPDao {

	private static IPDao sInstance;
	private Context mContext;

	private IPDao(Context context) {
		if (context instanceof Application) {
			mContext = context;
		} else {
			mContext = context.getApplicationContext();
		}
	}

	public static IPDao get(Context context) {
		if (sInstance == null) {
			sInstance = new IPDao(context);
		}
		return sInstance;
	}

	public void add(IP ip) {
		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(mContext);
			List<IP> ips = helper.getIPs().queryBuilder().where().eq("ip", ip.getIp()).query();
			if (ips == null || ips.size() == 0) {
				helper.getIPs().create(ip);
			}
		} catch (SQLException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}


	public IP getIP(int ip) {
		IP result = null;

		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(mContext);
			result = helper.getIPs().queryBuilder().where().eq("ip", ip).queryForFirst();
		} catch (SQLException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}

		return result;
	}


	public boolean hasData() {
		boolean result = false;

		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(mContext);
			result = helper.getIPs().queryBuilder().queryForFirst() != null;
		} catch (SQLException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}

		return result;
	}
}
