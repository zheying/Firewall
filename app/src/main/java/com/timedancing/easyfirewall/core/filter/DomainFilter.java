package com.timedancing.easyfirewall.core.filter;

/**
 * Created by zengzheying on 16/1/6.
 * 针对域名的过滤
 */
public interface DomainFilter {

	void prepare();

	boolean needFilter(String domain, int ip);

}
