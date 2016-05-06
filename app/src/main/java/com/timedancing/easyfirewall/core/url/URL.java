package com.timedancing.easyfirewall.core.url;

import android.net.Uri;
import android.text.TextUtils;

import com.timedancing.easyfirewall.core.tcpip.CommonMethods;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zengzheying on 16/1/5.
 */
public class URL {

	private static Pattern typeprog = Pattern.compile("^([^/:]+):.*");
	private static Pattern hostprog = Pattern.compile("^//([^/?]*)(.*)$");
	private static Pattern userprog = Pattern.compile("^(.*)@(.*)$");
	private static Pattern portprog = Pattern.compile("^(.*):([0-9]+)$");
	private String mUrl;


	public URL(String url) {
		mUrl = url;
	}

	public static String strip(String str, String stripStr) {
		if (str != null) {
			str = str.trim();
			if (str.startsWith(stripStr)) {
				str = str.substring(stripStr.length());
			}

			if (str.endsWith(stripStr)) {
				str = str.substring(0, str.length() - stripStr.length());
			}
		}
		return str;
	}

	public static String fullUnescape(String url) {
		if (url == null) {
			return url;
		}
		String unescapeUrl = Uri.decode(url);
		if (url.equals(unescapeUrl)) {
			return url;
		} else {
			return fullUnescape(unescapeUrl);
		}
	}

	public static String quote(String s) {
		if (s == null) {
			return s;
		}
		String safeChars = "!\"$&\'()*+,-./:;<=>?@[\\]^_`{|}~";
		return Uri.encode(s, safeChars);
	}

	public static String[] splitType(String url) {
		String[] strs = new String[2];
		if (url != null) {
			Matcher matcher = typeprog.matcher(url);
			String scheme = null;
			String domain = url;
			if (matcher.matches()) {
				scheme = matcher.group(1);
				domain = url.substring(scheme.length() + 1);
			}
			strs[0] = scheme;
			strs[1] = domain;
		}
		return strs;
	}

	public static String[] splitHost(String url) {
		String[] strs = new String[2];
		if (url != null) {
			Matcher matcher = hostprog.matcher(url);
			String host = null;
			String path = url;
			if (matcher.matches()) {
				host = matcher.group(1);
				path = matcher.group(2);
				if (path != null && !path.startsWith("/")) {
					path = "/" + path;
				}
			}
			strs[0] = host;
			strs[1] = path;
		}
		return strs;
	}

	public static String[] splitUser(String url) {
		String[] strs = new String[2];
		if (url != null) {
			Matcher matcher = userprog.matcher(url);
			String user = null;
			String host = url;
			if (matcher.matches()) {
				user = matcher.group(1);
				host = matcher.group(2);
			}
			strs[0] = user;
			strs[1] = host;
		}
		return strs;
	}

	public static String[] splitPort(String url) {
		String[] strs = new String[2];
		if (url != null) {
			Matcher matcher = portprog.matcher(url);
			String host = url;
			String port = null;
			if (matcher.matches()) {
				host = matcher.group(1);
				port = matcher.group(2);
			}
			strs[0] = host;
			strs[1] = port;
		}
		return strs;
	}

	public static List<String> urlHostPermutations(String host) {
		List<String> list = new ArrayList<>();
		if (host != null) {
			if (Pattern.matches("\\d+\\.\\d+\\.\\d+\\.\\d+", host)) {
				list.add(host);
				return list;
			}
			String[] parts = host.split("\\.");
			int l = Math.min(parts.length, 5);
			if (l > 4) {
				list.add(host);
			}
			for (int i = 0; i < l - 1; i++) {
				int start = parts.length + (i - l);
				int subStrIndex = 0;
				for (int j = 0; j < start; j++) {
					subStrIndex += parts[j].length();
				}
				subStrIndex += start;
				list.add(host.substring(subStrIndex));
			}
		}
		return list;
	}

	public static List<String> urlPathPermutations(String path) {
		List<String> list = new ArrayList<>();
		if (path != null) {
			if (!"/".equals(path)) {
				list.add(path);
			}
			String query = null;
			if (path.contains("?")) {
				String[] parts = path.split("\\?", 2);
				path = parts[0];
				query = parts[1];
			}
			if (query != null) {
				list.add(path);
			}
			ArrayList<String> pathParts = new ArrayList<>();
			int limit = 0;
			for (int i = 0; i < path.length(); i++) {
				if (path.charAt(i) == '/') {
					limit++;
				}
			}
			pathParts.addAll(Arrays.asList(path.split("/", limit + 1)));
			if (pathParts.size() > 0) {
				pathParts.remove(pathParts.size() - 1);
			}
			String currPath = "";
			for (int i = 0; i < Math.min(4, pathParts.size()); i++) {
				currPath = currPath + pathParts.get(i) + "/";
				list.add(currPath);
			}
		}
		return list;
	}

	public static List<String> urlPermutations(String url) {
		ArrayList<String> list = new ArrayList<>();
		String host, path;
		String[] protocolAndAddr = splitType(url);
		String[] hostAndPath = splitHost(protocolAndAddr[1]);
		host = hostAndPath[0];
		path = hostAndPath[1];
		String[] userAndHost = splitUser(host);
		host = userAndHost[1];
		String[] hostAndPort = splitPort(host);
		host = hostAndPort[0];
		host = strip(host, "/");
		for (String h : urlHostPermutations(host)) {
			for (String p : urlPathPermutations(path)) {
				list.add(String.format("%s%s", h, p));
			}
		}
		return list;
	}

	public static String digest(String str) {
		String res = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(str.getBytes("utf-8"));
			res = bytes2Hex(hash);
		} catch (Exception ex) {
			//ignore
		}
		return res;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public String getUrl() {
		return mUrl;
	}

	public List<String> hashes() {
		List<String> list = new ArrayList<>();
		for (String str : urlPermutations(canonical())) {
			list.add(digest(str));
		}
		return list;
	}

	public String canonical() {
		String url = mUrl.trim();
		url = url.replace("\n", "").replace("\r", "").replace("\t", "");
		url = url.split("#", 2)[0];
		url = fullUnescape(url);
		url = quote(url);
		Uri uri = Uri.parse(url);
		if (TextUtils.isEmpty(uri.getScheme())) {
			url = String.format("http://%s", url);
			uri = Uri.parse(url);
		}
		String protocol = uri.getScheme();
		String host = fullUnescape(uri.getHost());
		String path = fullUnescape(uri.getPath());
		String query = uri.getQuery();
		if (TextUtils.isEmpty(query) && !url.contains("?")) {
			query = null;
		}
		if (TextUtils.isEmpty(path)) {
			path = "/";
		}
		path = path.replace("//", "/");
		String user = uri.getUserInfo();
		int port = uri.getPort();
		host = strip(host, ".");
		host = host.replaceAll("\\.+", ".").toLowerCase();
		if (TextUtils.isDigitsOnly(host)) {
			host = CommonMethods.ipIntToString(Integer.valueOf(host));
		} else if (host.startsWith("0x") && !host.contains(".")) {
			host = CommonMethods.ipIntToString(Integer.valueOf(host, 16));
		}
		String quotedPath = quote(path);
		String quotedHost = quote(host);
		if (port != -1) {
			quotedHost = String.format("%s:%d", quotedHost, port);
		}
		String cononicalUrl = String.format("%s://%s%s", protocol, quotedHost, quotedPath);
		if (!TextUtils.isEmpty(query)) {
			cononicalUrl = String.format("%s?%s", cononicalUrl, quote(query));
		}

		return cononicalUrl;
	}
}
