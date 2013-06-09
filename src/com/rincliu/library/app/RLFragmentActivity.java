package com.rincliu.library.app;

import com.rincliu.library.BuildConfig;
import com.rincliu.library.R;
import com.rincliu.library.common.reference.analytics.RLAnalyticsHelper;
import com.rincliu.library.common.reference.feedback.RLFeedbackHelper;
import com.rincliu.library.util.RLSysUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class RLFragmentActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RLCrashHandler.getInstance().init(this);
		RLAnalyticsHelper.init(this, BuildConfig.DEBUG);
		RLFeedbackHelper.init(this, BuildConfig.DEBUG);
		
		((RLApplication)getApplication()).setDisplayInfo(RLSysUtil.getDisplayInfo(this));
	}
	
	@Override
	public void onNewIntent(Intent intent){
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		RLAnalyticsHelper.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		RLAnalyticsHelper.onPause(this);
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void startActivity(Intent intent){
		super.startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
	
	@Override
	public void startActivityForResult(Intent intent,int requestCode){
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
	
	@Override
	public void startActivityFromChild(Activity child,Intent intent,int requestCode){
		super.startActivityFromChild(child, intent, requestCode);
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}
	
	@Override
	public void finish(){
		super.finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}
	
	@Override
	public void finishActivity(int requestCode){
		super.finishActivity(requestCode);
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}
	
	@Override
	public void finishFromChild(Activity child){
		super.finishFromChild(child);
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}
	
	@Override
	public void finishActivityFromChild(Activity child, int requestCode){
		super.finishActivityFromChild(child, requestCode);
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		((RLApplication)getApplication()).setDisplayInfo(RLSysUtil.getDisplayInfo(this));
	}
	
	@Override
	public void onLowMemory(){
		super.onLowMemory();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
}