package com.timedancing.easyfirewall.cache.db.helper;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.timedancing.easyfirewall.cache.db.bean.Domain;
import com.timedancing.easyfirewall.cache.db.bean.IP;
import com.timedancing.easyfirewall.constant.AppDebug;

import java.sql.SQLException;

/**
 * Created by zengzheying on 16/1/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATA_BASE = "sqlite_blacklist.db";

	private static DatabaseHelper sInstance;
	private Dao<Domain, Integer> mDomains;
	private Dao<IP, Integer> mIPs;

	private DatabaseHelper(Context context) {
		super(context, DATA_BASE, null, 2);
	}

	public static synchronized DatabaseHelper getHelper(Context context) {
		if (sInstance == null) {
			synchronized (DatabaseHelper.class) {
				if (sInstance == null) {
					if (context instanceof Application) {
						sInstance = new DatabaseHelper(context);
					} else {
						sInstance = new DatabaseHelper(context.getApplicationContext());
					}
				}
			}
		}
		return sInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		try {

			TableUtils.createTable(connectionSource, Domain.class);
			TableUtils.createTable(connectionSource, IP.class);

		} catch (SQLException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
		try {
			TableUtils.dropTable(connectionSource, Domain.class, true);
			TableUtils.dropTable(connectionSource, IP.class, true);
			onCreate(sqLiteDatabase, connectionSource);
		} catch (SQLException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}

	@Override
	public void close() {
		super.close();
		mDomains = null;
		mIPs = null;
	}

	public Dao<Domain, Integer> getDomains() throws SQLException {
		if (mDomains == null) {
			mDomains = getDao(Domain.class);
		}
		return mDomains;
	}

	public Dao<IP, Integer> getIPs() throws SQLException {
		if (mIPs == null) {
			mIPs = getDao(IP.class);
		}
		return mIPs;
	}
}
