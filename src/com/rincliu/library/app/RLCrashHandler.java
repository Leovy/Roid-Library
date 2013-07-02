package com.rincliu.library.app;

import java.lang.Thread.UncaughtExceptionHandler;

import com.rincliu.library.R;
import com.rincliu.library.util.RLUiUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;

public class RLCrashHandler implements UncaughtExceptionHandler {
	private Context mContext;
	private static RLCrashHandler instance;
	
	/**
	 * 
	 * @return
	 */
	public static RLCrashHandler getInstance() {
		if(instance==null){
			instance=new RLCrashHandler();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		ex.printStackTrace();
		new Thread(){
			public void run(){
				Looper.prepare();
				RLUiUtil.toast(mContext,R.string.exception_occured);
		        Looper.loop();
			}
		}.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE))
    		.killBackgroundProcesses(mContext.getPackageName());
	}
}