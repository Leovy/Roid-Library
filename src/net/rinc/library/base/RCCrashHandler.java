package net.rinc.library.base;

import java.lang.Thread.UncaughtExceptionHandler;

import net.rinc.library.R;
import net.rinc.library.util.UiUtil;
import android.content.Context;
import android.os.Looper;

public class RCCrashHandler implements UncaughtExceptionHandler {
	private Context mContext;
	private static RCCrashHandler instance;
	
	/**
	 * 
	 * @return
	 */
	public static RCCrashHandler getInstance() {
		if(instance==null){
			instance=new RCCrashHandler();
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
				UiUtil.toast(mContext,R.string.exception_occured);
		        Looper.loop();
			}
		}.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}