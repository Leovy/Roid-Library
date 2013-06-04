package net.rinc.library.base;

import net.rinc.library.BuildConfig;
import net.rinc.library.R;
import net.rinc.library.common.persistence.afinal.FinalActivity;
import net.rinc.library.common.reference.analytics.AnalyticsHelper;
import net.rinc.library.common.reference.feedback.FeedbackHelper;
import net.rinc.library.util.SysUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

public class RCActivity extends FinalActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RCCrashHandler.getInstance().init(this);
		AnalyticsHelper.init(this, BuildConfig.DEBUG);
		FeedbackHelper.init(this, BuildConfig.DEBUG);
		
		((RCApplication)getApplication()).setDisplayInfo(SysUtil.getDisplayInfo(this));
	}
	
	@Override
	protected void onNewIntent(Intent intent){
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
		AnalyticsHelper.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		AnalyticsHelper.onPause(this);
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
		((RCApplication)getApplication()).setDisplayInfo(SysUtil.getDisplayInfo(this));
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