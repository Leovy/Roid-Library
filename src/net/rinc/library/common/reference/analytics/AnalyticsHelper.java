package net.rinc.library.common.reference.analytics;

import java.util.HashMap;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class AnalyticsHelper {
	
	/**
	 * 
	 * @param context
	 * @param isDebug
	 */
	public static void init(Context context, boolean isDebug){
		com.umeng.common.Log.LOG=isDebug;
		MobclickAgent.setDebugMode(isDebug);
		MobclickAgent.setAutoLocation(true);
		MobclickAgent.setSessionContinueMillis(1000);
//		MobclickAgent.setUpdateOnlyWifi(false);
//		MobclickAgent.setDefaultReportPolicy(context, ReportPolicy.BATCH_BY_INTERVAL, 5*1000);
		MobclickAgent.updateOnlineConfig(context);
		MobclickAgent.onError(context);
	}
	
	/**
	 * 
	 * @param context
	 */
	public static void onPause(Context context){
		MobclickAgent.onPause(context);
	}
	
	/**
	 * 
	 * @param context
	 */
	public static void onResume(Context context){
		MobclickAgent.onResume(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param event
	 */
	public static void onEvent(Context context, String event){
		MobclickAgent.onEvent(context, event);
	}
	
	/**
	 * 
	 * @param context
	 * @param event
	 * @param map
	 */
	public static void onEvent(Context context, String event, HashMap<String,String> map){
		MobclickAgent.onEvent(context, event, map);
	}
}