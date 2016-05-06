package com.timedancing.easyfirewall.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.timedancing.easyfirewall.constant.AppDebug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by zengzheying on 16/1/15.
 */
public class AssetsUtil {

	public static void copyFileOrDir(Context context, String path) {
		AssetManager assetManager = context.getAssets();
		String[] assets = new String[0];
		try {
			assets = assetManager.list(path);
			if (assets.length == 0) {
				copyFile(context, path);
			} else {
				File dir = new File(context.getExternalCacheDir(), path);
				if (!dir.exists()) {
					dir.mkdir();
				}
				for (int i = 0; i < assets.length; i++) {
					String newFileName = (TextUtils.isEmpty(path) ? path : path + "/") + assets[i];
					copyFileOrDir(context, newFileName);
				}
			}
		} catch (IOException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}

	private static void copyFile(Context context, String fileName) {
		AssetManager assetManager = context.getAssets();

		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(fileName);
			File newFile = new File(context.getExternalCacheDir(), fileName);
			out = new FileOutputStream(newFile);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			out.flush();
			out.close();
		} catch (IOException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public static String readAssetsTextFile(Context context, String fileName) {
		AssetManager assetManager = context.getAssets();

		StringBuilder sb = new StringBuilder("");
		InputStream in = null;
		try {
			in = assetManager.open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			in.close();
		} catch (IOException ex) {
			if (AppDebug.IS_DEBUG) {
				ex.printStackTrace(System.err);
			}
		}
		return sb.toString();
	}

}
