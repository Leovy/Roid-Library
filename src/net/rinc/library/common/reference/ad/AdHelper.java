package net.rinc.library.common.reference.ad;

import net.rinc.library.R;
import net.rinc.library.util.SysUtil;

import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdManager.ErrorCode;
import cn.domob.android.ads.DomobAdView;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

public class AdHelper {
	
	/**
	 * 
	 */
	public interface AdListener{
		public void onClick();
		public void onReceive();
		public void onFailedReceive();
	}
	
	/**
	 * 
	 * @param activity
	 * @param listener
	 * @return
	 */
	public static View onCreateView(Activity activity, final AdListener listener){
		DomobAdView adView = null;
		int width=SysUtil.getDisplayInfo(activity).getDisplayWidth();
		String key=activity.getString(R.string.domob_key);
		if(width<320){
			return null;
		}else{
			String size = null;
			if(320<=width&&width<600){
				size=DomobAdView.INLINE_SIZE_320X50;
			}else if(600<=width&&width<728){
				size=DomobAdView.INLINE_SIZE_600X94;
			}else if(728<=width){
				size=DomobAdView.INLINE_SIZE_728X90;
			}
			adView=new DomobAdView(activity, key, size, true);
		}
		
		adView.setGravity(Gravity.CENTER);
		adView.setAdEventListener(new DomobAdEventListener() {
			@Override
			public void onDomobAdClicked(DomobAdView adView) {
				listener.onClick();
			}
			@Override
			public void onDomobAdFailed(DomobAdView adView, ErrorCode errorCode) {
				listener.onFailedReceive();
			}
			@Override
			public void onDomobAdOverlayDismissed(DomobAdView adView) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onDomobAdOverlayPresented(DomobAdView adView) {
				// TODO Auto-generated method stub
			}
			@Override
			public Context onDomobAdRequiresCurrentContext() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public void onDomobAdReturned(DomobAdView adView) {
				listener.onReceive();
			}
			@Override
			public void onDomobLeaveApplication(DomobAdView adView) {
				// TODO Auto-generated method stub
			}
		});
		return adView;
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void onDestroy(View view){
		if(view==null){
			return;
		}
		DomobAdView adView=(DomobAdView) view;
		adView.clean();
	}
}