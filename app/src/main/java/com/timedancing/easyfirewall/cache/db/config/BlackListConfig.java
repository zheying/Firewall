package com.timedancing.easyfirewall.cache.db.config;

import android.content.Context;

import com.timedancing.easyfirewall.R;
import com.timedancing.easyfirewall.cache.db.bean.Domain;
import com.timedancing.easyfirewall.cache.db.bean.IP;
import com.timedancing.easyfirewall.cache.db.dao.DomainDao;
import com.timedancing.easyfirewall.cache.db.dao.IPDao;
import com.timedancing.easyfirewall.constant.AppDebug;
import com.timedancing.easyfirewall.core.tcpip.CommonMethods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zengzheying on 16/1/15.
 */
public class BlackListConfig {

	public static void configBlackList(Context context) {
		DomainDao domainDao = DomainDao.get(context);
		IPDao ipDao = IPDao.get(context);
		if (domainDao.hasData() || ipDao.hasData()) {
			InputStream in = context.getResources().openRawResource(R.raw.hosts);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.startsWith("#")) {
						continue;
					}

					String[] parts = line.split(" ");
					if (parts.length == 2) {
						String ipStr = parts[0];
						int ip = CommonMethods.ipStringToInt(ipStr);
						Domain domain = new Domain(parts[1], ip);
						IP ipModel = new IP(ip);
						domainDao.add(domain);
						ipDao.add(ipModel);
					}
				}
			} catch (IOException ex) {
				if (AppDebug.IS_DEBUG) {
					ex.printStackTrace(System.err);
				}
			} finally {
				try {
					reader.close();
					in.close();
				} catch (IOException ex) {
					if (AppDebug.IS_DEBUG) {
						ex.printStackTrace(System.err);
					}
				}
			}
		}
	}

}
