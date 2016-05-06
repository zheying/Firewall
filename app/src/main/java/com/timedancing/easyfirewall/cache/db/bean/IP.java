package com.timedancing.easyfirewall.cache.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zengzheying on 16/1/14.
 */
@DatabaseTable(tableName = "tb_ip")
public class IP {
	@DatabaseField(id = true, columnName = "ip")
	private int ip;

	public IP() {
	}

	public IP(int ip) {
		this.ip = ip;
	}

	public int getIp() {
		return ip;
	}
}
