package com.redtop.engaze.logger;

import android.util.Log;

/**
 * Class for sending Logger output.
 */
public class Tracer
{
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;

	private static volatile Logger sLogger = null;

    
    public static boolean isLoggable (String tag, int level)
    {
    	final Logger logger = getLogger();

    	return (null != logger && logger.isLoggable(tag, level));
    }

    public static void v (String tag, String msg)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.v(tag, msg);
        }
    }

    public static void v (String tag, String msg, Throwable tr)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.v(tag, msg, tr);
        }
    }

    public static void d (String tag, String msg)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.d(tag, msg);
        }
    }

    public static void d (String tag, String msg, Throwable tr)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.d(tag, msg, tr);
        }
    }

    public static void i (String tag, String msg)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.i(tag, msg);
        }
    }

    public static void i (String tag, String msg, Throwable tr)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.i(tag, msg, tr);
        }
    }

    public static void w (String tag, String msg)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.w(tag, msg);
        }
    }

    public static void w (String tag, String msg, Throwable tr)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.w(tag, msg, tr);
        }
    }

    public static void e (String tag, String msg)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.e(tag, msg);
        }
    }

    public static void e (String tag, String msg, Throwable tr)
    {
    	final Logger logger = getLogger();
    	if (null != logger)
        {
    		logger.e(tag, msg, tr);
        }
    }

    public static synchronized void setLogger (Logger logger)
    {
    	sLogger = logger;
    }

    public static synchronized Logger getLogger ()
	{
		return sLogger;
	}
}
