package com.timedancing.easyfirewall.cache.db.dao;

import android.app.Application;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.timedancing.easyfirewall.cache.db.helper.DatabaseHelper;
import com.timedancing.easyfirewall.cache.db.bean.Domain;
import com.timedancing.easyfirewall.constant.AppDebug;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zengzheying on 16/1/14.
 */
public class DomainDao {

	private static DomainDao sIntance;
	private Context mContext;


	public DomainDao(Context context) {
		if (context instanceof Application) {
			mContext = context;
		} else {
			mContext = context.getApplicationContext();
		}
	}

	public static DomainDao get(Context context) {
		if (sIntance == null) {
			sIntance = new DomainDao(context);
		}

		return sIntance;
	}

	public void add(Domain domain) {
		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(mContext);
			List<Domain> domains = helper.getDomains().queryBuilder().where().eq("domain", domain.getDomain()).query();
			if (domains == null || domains.size() == 0) {
				helper.getDomains().create(domain);
			}
		} catch (SQLException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public void update(Domain domain) {
		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(mContext);
			Dao<Domain, Integer> dao = helper.getDomains();
			dao.update(domain);
		} catch (SQLException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public Domain getDomainByString(String str) {
		Domain result = null;

		try {
			DatabaseHelper helper = DatabaseHelper.getHelper(mContext);
			Dao<Domain, Integer> domains = helper.getDomains();
			result = domains.queryBuilder().where().eq("domain", str).queryForFirst();
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
			result = helper.getDomains().queryBuilder().queryForFirst() != null;
		} catch (SQLException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}

		return result;
	}

}
