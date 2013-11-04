/**
 * Copyright (c) 2013-2014, Rinc Liu (http://rincliu.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rincliu.library.common.reference.media;

import com.rincliu.library.R;
import com.rincliu.library.util.RLSysUtil;
import com.rincliu.library.util.RLUiUtil;
import com.rincliu.library.widget.dialog.RLLoadingDialog;

import com.baidu.cyberplayer.sdk.BCyberPlayerFactory;
import com.baidu.cyberplayer.sdk.BEngineManager;
import com.baidu.cyberplayer.sdk.BEngineManager.OnEngineListener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class RLVideoPlayer {
	private Context context;
	private RLLoadingDialog pb;
	public static final String[] SUPPORT_TYPES={
		".avi",
		".flv",
		".mkv",
		".mp4",
		".mov",
		".rmvb",
		".wmv"
	};
	
	/**
	 * should be called before setContentView(layout)
	 * @param context
	 */
	public RLVideoPlayer(Context context){
		this.context=context;
		pb=new RLLoadingDialog(context);
		BCyberPlayerFactory.init(context);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEngineInstalled(){
		BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
		return mgr.EngineInstalled();
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isFileSupport(String file){
		for(int i=0;i<SUPPORT_TYPES.length;i++){
			if(file.endsWith(SUPPORT_TYPES[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param uriStr
	 * @param isHW	是否开启硬件解码（MP4、3GP）
	 */
	public void openPlayer(String uriStr, boolean isHW){
		if(isEngineInstalled()&&uriStr!=null&&!uriStr.equals("")){
			Intent it = new Intent(Intent.ACTION_VIEW);   
			it.putExtra("isHW", isHW);
			Uri uri = Uri.parse(uriStr);
            it.setClassName("com.baidu.cyberplayer.engine", "com.baidu.cyberplayer.engine.PlayingActivity");
            it.setData(uri); 
         	context.startActivity(it);
		}
	}
	
	/**
	 * 
	 */
	public void installEngine(){
		if(RLSysUtil.isExternalStorageAvailable()){
			RLUiUtil.toast(context, R.string.check_sdcard);
			return;
		}
		BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
		mgr.installAsync(new OnEngineListener(){
			@Override
			public boolean onPrepare() {
				handler.sendEmptyMessage(MSG_ON_PREPARE);
				return true;
			}
			@Override
			public int onDownload(int total, int current) {
				Message msg=new Message();
				msg.what=MSG_ON_DOWNLOAD;
				Bundle data=new Bundle();
				data.putInt("total", total);
				data.putInt("current", current);
				msg.setData(data);
				handler.sendMessage(msg);
				return DOWNLOAD_CONTINUE;
			}
			@Override
			public int onPreInstall() {
				handler.sendEmptyMessage(MSG_ON_PRE_INSTALL);
				return DOWNLOAD_CONTINUE;
			}
			@Override
			public void onInstalled(int result) {
				Message msg=new Message();
				msg.what=MSG_ON_INSTALLED;
				Bundle data=new Bundle();
				data.putInt("result", result);
				msg.setData(data);
				handler.sendMessage(msg);
			}		
		});
	}
	
	private static final int MSG_ON_PREPARE=111;
	private static final int MSG_ON_DOWNLOAD=333;
	private static final int MSG_ON_PRE_INSTALL=555;
	private static final int MSG_ON_INSTALLED=999;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_ON_PREPARE:
				//TODO
				break;
			case MSG_ON_DOWNLOAD:
				int total=msg.getData().getInt("total");
				int current=msg.getData().getInt("current");
				pb.setMessage(context.getString(R.string.downloading)+current+"/"+total);
				if(!pb.isShowing()){
					pb.show();
				}
				break;
			case MSG_ON_PRE_INSTALL:
				if(pb.isShowing()){
					pb.dismiss();
				}
				break;
			case MSG_ON_INSTALLED:
				int result=msg.getData().getInt("result");
				switch(result){
				case OnEngineListener.RET_FAILED_ALREADY_INSTALLED:
					RLUiUtil.toast(context, R.string.ret_failed_already_installed);
					break;
				case OnEngineListener.RET_FAILED_ALREADY_RUNNING:
					RLUiUtil.toast(context, R.string.ret_failed_already_running);
					break;
				case OnEngineListener.RET_FAILED_INVALID_APK:
					RLUiUtil.toast(context, R.string.ret_failed_invalid_apk);
					break;
				case OnEngineListener.RET_FAILED_NETWORK:
					RLUiUtil.toast(context, R.string.ret_failed_network);
					break;
				case OnEngineListener.RET_FAILED_OTHERS:
					RLUiUtil.toast(context, R.string.ret_failed_others);
					break;
				case OnEngineListener.RET_FAILED_STORAGE_IO:
					RLUiUtil.toast(context, R.string.ret_failed_storage_io);
					break;
				case OnEngineListener.RET_NEW_PACKAGE_INSTALLED:
					RLUiUtil.toast(context, R.string.ret_new_package_installed);
					break;
				case OnEngineListener.RET_NO_NEW_PACKAGE:
					RLUiUtil.toast(context, R.string.ret_no_new_package);
					break;
				case OnEngineListener.RET_STOPPED:
					RLUiUtil.toast(context, R.string.ret_stopped);
					break;
				}
				break;
			}
		}
	};
}