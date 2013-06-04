package net.rinc.library.base;

import net.rinc.library.BuildConfig;
import net.rinc.library.common.reference.push.PushHelper;
import net.rinc.library.entity.DisplayInfo;
import android.app.Application;
import android.content.res.Configuration;

public class RCApplication extends Application{
	
	@Override
	public void onCreate(){
		super.onCreate();
		PushHelper.getInstance(this).init(BuildConfig.DEBUG);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onLowMemory(){
		super.onLowMemory();
	}
	
	@Override
	public void onTerminate(){
		super.onTerminate();
	}
	
	private DisplayInfo displayInfo;

	public DisplayInfo getDisplayInfo() {
		return displayInfo;
	}

	public void setDisplayInfo(DisplayInfo displayInfo) {
		this.displayInfo = displayInfo;
	}
}