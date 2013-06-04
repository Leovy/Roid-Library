package net.rinc.library.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

@SuppressWarnings("deprecation")
public class ViewCompat {
	
	private static final int VERSION=android.os.Build.VERSION.SDK_INT;
	
	/**
	 * 
	 * @param view
	 * @param runnable
	 */
	public static void postOnAnimation(View view, Runnable runnable) {
		if (VERSION >= 16) {
			SDK16.postOnAnimation(view, runnable);
		} else {
			view.postDelayed(runnable, 16);
		}
	}
	
	/**
	 * 
	 * @param view
	 * @param drawable
	 */
	public static void setBackground(View view, Drawable drawable) {
		if (VERSION >= 16) {
			SDK16.setBackground(view, drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}
	
	/**
	 * 
	 * @param view
	 * @param layerType
	 */
	public static void setLayerType(View view, int layerType) {
		if (VERSION >= 11) {
			SDK11.setLayerType(view, layerType);
		}
	}
	
	/**
	 * 
	 * @param viewTreeObserver
	 * @param onGlobalLayoutListener
	 */
	public static void removeGlobalLayoutListener(ViewTreeObserver viewTreeObserver, OnGlobalLayoutListener onGlobalLayoutListener){
		if(VERSION>=16){
			SDK16.removeGlobalLayoutListener(viewTreeObserver, onGlobalLayoutListener);
		}else{
			viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener);
		}
	}
	
	@TargetApi(9)
	private static class SDK9 {
		
	}
	
	@TargetApi(10)
	private static class SDK10 {
		
	}

	@TargetApi(11)
	private static class SDK11 {
		public static void setLayerType(View view, int layerType) {
			view.setLayerType(layerType, null);
		}
	}
	
	@TargetApi(12)
	private static class SDK12 {
		
	}
	
	@TargetApi(13)
	private static class SDK13 {
		
	}
	
	@TargetApi(14)
	private static class SDK14 {
		
	}
	
	@TargetApi(15)
	private static class SDK15 {
		
	}

	@TargetApi(16)
	private static class SDK16 {
		public static void postOnAnimation(View view, Runnable runnable) {
			view.postOnAnimation(runnable);
		}
		public static void setBackground(View view, Drawable background) {
			view.setBackground(background);
		}
		public static void removeGlobalLayoutListener(ViewTreeObserver observer, OnGlobalLayoutListener listener){
			observer.removeOnGlobalLayoutListener(listener);
		}
	}
}