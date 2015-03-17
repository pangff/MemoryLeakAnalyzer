package com.pangff.memoryleakanalyzer;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;

/**
 * 全局异常处理
 * @author pangff
 */
public class MyCrashHandler implements UncaughtExceptionHandler {

	private Thread.UncaughtExceptionHandler mSystemDefaultHandler;
	private static MyCrashHandler myCrashHandler;

    private MyCrashHandler(){  
          
    }  
      
    public static synchronized MyCrashHandler getInstance(){  
        if(myCrashHandler!=null){  
            return myCrashHandler;  
        }else {  
            myCrashHandler  = new MyCrashHandler();  
            return myCrashHandler;  
        }  
    }  

	/**
	 * 设置CrashHandler为默认的错误处理
	 */
	public void setDefaultUncaughtExceptionHandler() {
		if (Thread.getDefaultUncaughtExceptionHandler() == this) {
			return;
		}
		//这是系统默认的
		mSystemDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		
		//设置当前为默认
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	Throwable lastThrowable;

	@Override
	public void uncaughtException(final Thread thread, final Throwable ex) {
		if (ex == null) {
			return;
		}

		if (lastThrowable == ex || ex.getCause() != null
				&& lastThrowable == ex.getCause()) {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
			return;
		}

		StringBuilder sbActivities = new StringBuilder();
		sbActivities.append("activities: ");
		StringBuilder sbLeakActivities = new StringBuilder();
		sbLeakActivities.append("leak activities: ");
		ArrayList<WeakReference<Activity>> list = BaseApplication.instance.listLeak;
		for (WeakReference<Activity> wr : list) {
			if (wr == null) {
				continue;
			}
			Activity activity = wr.get();
			if (activity == null) {
				continue;
			}
			sbActivities.append(activity.getClass().getSimpleName()).append(", ");
			if (!BaseApplication.instance.listCurrent.contains(activity)) {
				sbLeakActivities.append(activity.getClass().getSimpleName()).append(", ");
			}
		}
		sbActivities.append("\n").append(sbLeakActivities);

		Throwable myThrowable = ex;

		if (ex instanceof Exception) {
			myThrowable = new Exception(sbActivities.toString(), ex);
		} else if (ex instanceof Error) {
			myThrowable = new Error(sbActivities.toString(), ex);
		}

		lastThrowable = myThrowable;
		
		//交还给系统处理，我们只是在wrapperThrowable附加信息
		mSystemDefaultHandler.uncaughtException(thread, myThrowable);
		return;

	}

}
