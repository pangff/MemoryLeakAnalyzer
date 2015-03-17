package com.pangff.memoryleakanalyzer;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LeakActivity extends BaseActivity{
	
	public static  int MSG = 0;
	private Handler handler = new MyHandler();
	ArrayList<String> list = new ArrayList<String>();
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leak);
		
		handler.sendEmptyMessage(MSG);
	}
	
 	/**
 	 * 模拟内存泄漏
 	 * @author pangff
 	 */
	private class MyHandler extends Handler {
	    @Override
	    public void handleMessage(final Message msg) {
	      super.handleMessage(msg);
	      if(msg.what == MSG){
	    	  		for(int i=0;i<10000;i++){
	    	  			list.add(String.valueOf(i));//为了尽快的造成OutOfMemory
	    	  		}
				handler.sendEmptyMessageDelayed(MSG, 1000);//循环发送消息
		  }
	    }
	  }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeMessages(MSG);
	}
}
