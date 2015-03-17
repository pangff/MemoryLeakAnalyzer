package com.pangff.memoryleakanalyzer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;

public class BaseApplication extends Application {

	public ArrayList<Activity> listCurrent;//当前activity列表
	public ArrayList<WeakReference<Activity>> listLeak;//泄漏列表

	public static BaseApplication instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		listCurrent = new ArrayList<Activity>();
		listLeak = new ArrayList<WeakReference<Activity>>();
	}
	
	/**
	 * 添加activity，在activity的onCreate中
	 * @param activity
	 */
	public void addActivity(final BaseActivity activity) {
		listCurrent.add(activity);
		synchronized (listLeak) {
			for (int j = listLeak.size() - 1; j >= 0; j--) {
				WeakReference<Activity> wr = listLeak.get(j);
				if (wr == null || wr.get() == null) {
					listLeak.remove(j);
				}
			}
			listLeak.add(new WeakReference<Activity>(activity));
		}
	}

	/**
	 * activity的destory中删除
	 * @param activity
	 */
	public void removeActivity(final BaseActivity activity) {
		listCurrent.remove(activity);
	}
}
