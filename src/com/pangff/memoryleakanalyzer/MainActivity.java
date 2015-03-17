package com.pangff.memoryleakanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void gotoLeakActivity(View view){
		Intent intent = new Intent();
		intent.setClass(this, LeakActivity.class);
		startActivity(intent);
	}
}
