package com.timedancing.easyfirewall.network;

import android.content.Context;
import android.text.TextUtils;

import com.timedancing.easyfirewall.cache.AppCache;
import com.timedancing.easyfirewall.constant.AppDebug;
import com.timedancing.easyfirewall.event.HostUpdateEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import de.greenrobot.event.EventBus;

/**
 * Created by zengzheying on 16/1/29.
 */
public class HostHelper {

	private static final String HOST_URL = "http://dn-mwsl-hosts.qbox.me/hosts.txt";

	public static void updateHost(final Context context) {


		new Thread(new Runnable() {
			@Override
			public void run() {
				InputStream inputStream = null;
				BufferedReader reader = null;


				try {
					EventBus.getDefault().post(new HostUpdateEvent(HostUpdateEvent.Status.Updating));
					URL url = new URL(HOST_URL);
					URLConnection connection = url.openConnection();
					HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

					httpURLConnection.setDoInput(true);
					httpURLConnection.setRequestProperty(AppCache.KEY_IF_SINCE_MODIFIED_SINCE, AppCache
							.getIfSinceModifiedSince(context));
					httpURLConnection.setConnectTimeout(10 * 1000); //10s超时
					httpURLConnection.connect();

					int state = httpURLConnection.getResponseCode();
					if (state == 200) {
						String lastModified = httpURLConnection.getHeaderField("Last-Modified");
						if (!TextUtils.isEmpty(lastModified)) {
							AppCache.setIfSinceModifiedSince(context, lastModified);
						}
						inputStream = httpURLConnection.getInputStream();
						StringBuilder sb = new StringBuilder("");

						reader = new BufferedReader(new InputStreamReader(inputStream));
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line);
							sb.append("\r\n");
						}

						if (!TextUtils.isEmpty(sb.toString())) {
							writeHostFile(context, sb.toString());
						}

					}

				} catch (Exception ex) {

					if (AppDebug.IS_DEBUG) {
						ex.printStackTrace(System.err);
					}

				} finally {
					EventBus.getDefault().post(new HostUpdateEvent(HostUpdateEvent.Status.UpdateFinished));
					try {
						if (reader != null) {
							reader.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();


	}

	private static void writeHostFile(Context context, String content) {
		File file = null;
		OutputStream outputStream = null;
		BufferedWriter writer = null;
		try {
			file = new File(context.getExternalCacheDir(), "host.txt");
			if (file.exists()) {
				file.delete();
			}

			outputStream = new FileOutputStream(file);

			writer = new BufferedWriter(new OutputStreamWriter(outputStream));

			writer.write(content, 0, content.length());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}

			} catch (IOException e) {
				e.printStackTrace(System.err);
			}

		}


	}

}
