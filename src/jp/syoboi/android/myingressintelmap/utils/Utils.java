package jp.syoboi.android.myingressintelmap.utils;

import android.content.res.Resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
	public static String loadAssetText(Resources res, String name) throws IOException {
		InputStream is = null;
		try {
			is = res.getAssets().open("userscript.js");
			byte [] buf = new byte [4096];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			int readSize;
			while ((readSize = is.read(buf)) != -1) {
				baos.write(buf, 0, readSize);
			}
			return baos.toString("utf-8");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
