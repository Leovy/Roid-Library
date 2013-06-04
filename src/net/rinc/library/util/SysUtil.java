package net.rinc.library.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import net.rinc.library.entity.DisplayInfo;

public class SysUtil {
	/**
	 * 
	 * @param activity
	 * @return
	 */
	public static DisplayInfo getDisplayInfo(final Activity activity){
		final Resources res=activity.getResources();
		final DisplayInfo di=new DisplayInfo();
		Display display=activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm=res.getDisplayMetrics();
		display.getMetrics(dm);
		di.setDisplayWidth(dm.widthPixels);
		di.setDisplayHeight(dm.heightPixels);
		di.setDisplayDensity(dm.density);
		int statusResId=res.getIdentifier("status_bar_height","dimen","android");
		if(statusResId>0){
			di.setStatusBarHeight(res.getDimensionPixelSize(statusResId));
		}
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			int portraitResId=res.getIdentifier("navigation_bar_height","dimen","android");
			if(portraitResId>0){
				di.setPortraitNavigationBarHeight(res.getDimensionPixelSize(portraitResId));
			}
		}else{
			new Thread(){
				public void run(){
					try {
						Thread.sleep(200);
					}catch(Exception e){
						e.printStackTrace();
					} 
			        Window window=activity.getWindow();  
			        int contentViewTop=window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
			        int titleBarHeight=contentViewTop - di.getStatusBarHeight();
			        Configuration config=res.getConfiguration();
			        if(config.orientation == Configuration.ORIENTATION_PORTRAIT){ 
			        	di.setPortraitNavigationBarHeight(titleBarHeight); 
			        }else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
			        	di.setLandscapeNavigationBarHeight(titleBarHeight); 
			        }
				}
	    	}.start();
		}
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
			int landscapeResId=res.getIdentifier("navigation_bar_height_landscape","dimen","android");
			if(landscapeResId>0){
				di.setLandscapeNavigationBarHeight(res.getDimensionPixelSize(landscapeResId));
			}
		}else{
			//TODO
		}
		return di;
	}
	
	/**
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dip2px(Context context, float dp){ 
		final float scale=context.getResources().getDisplayMetrics().density;
		return(int)(dp*scale+0.5f);
	}
	
	/**
	 * 
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dip(Context context, float px){
		final float scale=context.getResources().getDisplayMetrics().density;
		return(int)(px/scale+0.5f);
	}
	
	/**
	 * 
	 * @param context
	 */
	public static void notifyScanMediaFiles(Context context){  
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
        		Uri.parse("file://"+Environment.getExternalStorageDirectory())));  
    }
	
	/**
	 * 
	 * @return
	 */
	public static boolean isExternalStorageAvailable(){
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				||!isExternalStorageRemovable();
	}
	
	/**
	 * 
	 * @return
	 */
    static boolean isExternalStorageRemovable(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
    
    /**
     * 
     * @return
     */
	public static long getAvailableExternalSpace(){
		File path=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		StatFs stat=new StatFs(path.getPath());
		long blockSize=stat.getBlockSize();
		long availableBlocks=stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 
	 * @return
	 */
	public static long getAvailableInnerSpace(){
		File path=Environment.getDataDirectory();
		StatFs stat=new StatFs(path.getPath());
		long blockSize=stat.getBlockSize();
		long availableBlocks=stat.getAvailableBlocks();
		long realSize=blockSize * availableBlocks;
		return realSize;
	}
    
    /**
     * 
     * @param context
     * @return
     */
    public static String getExternalCacheDir(Context context){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
			File f1=context.getExternalCacheDir();
			if(f1!=null){
				return f1.getPath();
			}else{
				return null;
			}
		}else{
			final String cacheDir="/Android/data/"+context.getPackageName()+"/cache/";
			File f2=Environment.getExternalStorageDirectory();
			if(f2!=null){
				return f2.getPath() + cacheDir;
			}else{
				return null;
			}
		}
	}
    
    /**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean requestRootPermission(String path){
	    Process process=null;
	    DataOutputStream dos=null;
	    try {
	        process=Runtime.getRuntime().exec("su");
	        if(process==null){
	        	return false;
	        }
	        OutputStream os=process.getOutputStream();
	        if(os==null){
	        	return false;
	        }
	        dos=new DataOutputStream(os);
	        dos.writeBytes("chmod 777 -R "+path+"\n");
	        dos.writeBytes("exit\n");
	        dos.flush();
	        process.waitFor();
	    } catch(Exception e){
	    	e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if(dos!=null){
	                dos.close();
	            }
	            if(process!=null){
	            	process.destroy();
	            }
	        } catch(Exception e){
	        	e.printStackTrace();
	        }
	    }
	    return true;
	}
}