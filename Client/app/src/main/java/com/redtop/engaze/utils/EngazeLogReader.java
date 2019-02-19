package com.redtop.engaze.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;


public final class EngazeLogReader {

	private static final String TAG = EngazeLogReader.class.getCanonicalName();
	private static final String processId = Integer.toString(android.os.Process
			.myPid());

	public static StringBuilder getLog() {

		StringBuilder builder = new StringBuilder();

		try {
			String[] command = new String[] { "logcat", "-d", "-v", "threadtime" };

			Process process = Runtime.getRuntime().exec(command);

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains(processId)) {
					builder.append(line);
					//Code here
				}
			}
		} catch (IOException ex) {
			Log.e(TAG, "getLog failed", ex);
		}

		return builder;
	}

}