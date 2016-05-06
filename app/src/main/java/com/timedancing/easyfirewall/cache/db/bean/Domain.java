package com.timedancing.easyfirewall.cache.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zengzheying on 16/1/14.
 * 一个域名对应一个ip
 */
@DatabaseTable(tableName = "tb_domain")
public class Domain {
	@DatabaseField(generatedId = true, columnName = "id")
	private int id;

	@DatabaseField(columnName = "domain")
	private String mDomain;

	@DatabaseField(columnName = "ip")
	private int mIp;

	public Domain() {
	}

	public Domain(String domain, int ip) {
		mDomain = domain;
		mIp = ip;
	}

	public String getDomain() {
		return mDomain;
	}

	public int getIp() {
		return mIp;
	}

	public void setIp(int ip) {
		mIp = ip;
	}
}
