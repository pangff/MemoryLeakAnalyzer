package com.pangff.memoryleakanalyzer;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseApplication.instance.addActivity(this);
		MyCrashHandler.getInstance().setDefaultUncaughtExceptionHandler();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseApplication.instance.removeActivity(this);
	}
}
