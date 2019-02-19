package com.redtop.engaze.logger;

import java.io.File;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * File logger.
 */
public class FileLogger implements Logger
{
	public static final String DEBUG_LOG_FILENAME = "Coordify"; // .log is appended when the file is used

	/**
	 * Configurable on build time.
	 */
	private static final String DEBUG_LOG_DIRECTORY = "Coordify";
	private static final String DEBUG_LOG_PARTNER_NAME = "Coordify";

	/**
	 * Control the log level of production.
	 */
	private static final int LEVEL_PROD_LOG = Log.ERROR + 1;

	/**
	 * Log name for each level.
	 */
	private static final String[] LEVEL_NAME = new String[] { "V", "D", "I", "W", "E", "A" };


	/**
	 * Indicates whether the holder application is debuggable.
	 */
	private static volatile boolean sDebuggable = false;

	/**
	 * Indicates whether the holder application is debuggable.
	 */
	private static volatile boolean sProductionDebuggable = false;


	/**
	 * The underlying logger.
	 */
	//private static volatile LoggingThread sLogger = null;

	private static Context sContext; 


	/**
	 * Turn on/off debug log at runtime.
	 * @param enabled
	 */
	public static void setDebuggable (Context context, boolean debuggable, boolean productionDebuggable)
	{
//		if(null == sContext)
//			sContext = context;
//
//		boolean setting = DebugSettings.getBoolean(context, DebugSettings.PROPERTY_DEBUGLOG_ENABLED, false);
//
//		// Can turn off the debug log only when the debug settings is off.
//		sDebuggable = debuggable | setting | productionDebuggable;
//		sProductionDebuggable = productionDebuggable;
//
//		Analyzer.enable(sDebuggable);
	}

	/**
	 * Returns whether debug log is on.
	 */
	public static boolean isDebuggable ()
	{
		return sDebuggable;
	}

	/**
	 * Returns the folder that to be used to save log files.
	 * @return
	 */
	public static String getLogDir ()
	{
		StringBuilder builder = new StringBuilder();

		if(sProductionDebuggable)
		{
			builder.append(sContext.getFilesDir());
			builder.append(File.separatorChar);
			builder.append(DEBUG_LOG_FILENAME);
		}
		else
		{
			builder.append(Environment.getExternalStorageDirectory());
			builder.append(File.separatorChar);
			builder.append(DEBUG_LOG_DIRECTORY);
			builder.append(DEBUG_LOG_PARTNER_NAME);
		}

		return builder.toString();
	}

	/**
	 * Constructor function.
	 * @param context
	 */
	public FileLogger (Context context)
	{
		setDebuggable(context, false, false);
	}

	@Override
	public boolean isLoggable (String tag, int level)
	{
		return sDebuggable || LEVEL_PROD_LOG <= level;
	}

	@Override
	public void v (String tag, String msg)
	{	
		log(Log.VERBOSE, tag, msg, null);
	}

	@Override
	public void v (String tag, String msg, Throwable tr)
	{
		log(Log.VERBOSE, tag, msg, tr);
	}

	@Override
	public void d (String tag, String msg)
	{
		log(Log.DEBUG, tag, msg, null);
	}

	@Override
	public void d (String tag, String msg, Throwable tr)
	{
		log(Log.DEBUG, tag, msg, tr);
	}

	@Override
	public void i (String tag, String msg)
	{
		log(Log.INFO, tag, msg, null);
	}

	@Override
	public void i (String tag, String msg, Throwable tr)
	{
		log(Log.INFO, tag, msg, tr);
	}

	@Override
	public void w (String tag, String msg)
	{
		log(Log.WARN, tag, msg, null);
	}

	@Override
	public void w (String tag, String msg, Throwable tr)
	{
		log(Log.WARN, tag, msg, tr);
	}

	@Override
	public void e (String tag, String msg)
	{
		log(Log.ERROR, tag, msg, null);
	}

	@Override
	public void e (String tag, String msg, Throwable tr)
	{
		log(Log.ERROR, tag, msg, tr);
	}

	private static void log (int level, String tag, String msg, Throwable tr)
	{
//		if (sDebuggable || LEVEL_PROD_LOG <= level)
//		{
//			String log = getLogMessage(msg, tr);
//
//			Log.println(level, tag, log);
//
//			if (null == sLogger)
//			{
//				synchronized (FileLogger.class)
//				{
//					if (null == sLogger)
//					{
//						sLogger = new LoggingThread(getLogDir(), DEBUG_LOG_FILENAME);
//						sLogger.setDaemon(true);
//						sLogger.start();
//					}
//				}
//			}
//			sLogger.write(getLogLevel(level), tag, log);
//		}
	}

	private static String getLogMessage (String msg, Throwable tr)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Thread: ");
		builder.append(Thread.currentThread().getId());
		builder.append("\t ");
		builder.append(msg);
		if (null != tr)
		{
			builder.append('\n');
			builder.append(Log.getStackTraceString(tr));
		}
		return builder.toString();
	}

	private static String getLogLevel (int level)
	{
		int pos = level - Log.VERBOSE;
		return pos >= 0 && pos < LEVEL_NAME.length ? LEVEL_NAME[pos]
				: String.valueOf(level);
	}
}

