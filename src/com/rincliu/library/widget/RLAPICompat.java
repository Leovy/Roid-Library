package com.rincliu.library.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

@SuppressWarnings("deprecation")
public class RLAPICompat {
	
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
	
	/**
	 * 
	 * @return
	 */
	public static long getAvailableExternalStorageSize(){
		long size=0;
		if(VERSION>=18){
			size=SDK18.getAvailableExternalStorageSize();
		}else{
			if(Environment.getExternalStorageDirectory()!=null){
				StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
				size=(long)stat.getAvailableBlocks()*(long)stat.getBlockSize();
			}
		}
		return size;
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getTotalExternalStorageSize(){
		long size=0;
		if(VERSION>=18){
			size=SDK18.getTotalExternalStorageSize();
		}else{
			if(Environment.getExternalStorageDirectory()!=null){
				StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
				size=(long)stat.getFreeBlocks()*(long)stat.getBlockSize();
			}
		}
		return size;
	}

	@TargetApi(11)
	private static class SDK11 {
		public static void setLayerType(View view, int layerType) {
			view.setLayerType(layerType, null);
		}
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
	
	@TargetApi(18)
	private static class SDK18 {
		public static long getAvailableExternalStorageSize(){
			long size=0;
			if(Environment.getExternalStorageDirectory()!=null){
				StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
				size=stat.getAvailableBlocksLong()*stat.getBlockSizeLong();
			}
			return size;
		}
		public static long getTotalExternalStorageSize(){
			long size=0;
			if(Environment.getExternalStorageDirectory()!=null){
				StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
				size=stat.getFreeBlocksLong()*stat.getBlockSizeLong();
			}
			return size;
		}
	}
}