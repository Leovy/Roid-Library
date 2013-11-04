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
package com.rincliu.library.common.reference.push;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import android.app.Notification;
import android.content.Context;

public class RLPushHelper {
	private static RLPushHelper helper;
	private static Context mContext;
	
	private static final int LASTEST_NOTIFICATION_NUMBER=3;
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static RLPushHelper getInstance(Context context){
		if(helper==null){
			helper=new RLPushHelper(context);
		}
		return helper;
	}
	
	private RLPushHelper(Context context){
		if(mContext==null){
			mContext=context.getApplicationContext();
		}
	}
	
	/**
	 * 
	 * @param isDebug
	 */
	public void init(boolean isDebug){
		JPushInterface.setDebugMode(isDebug);
		JPushInterface.init(mContext);
		JPushInterface.setLatestNotifactionNumber(mContext, LASTEST_NOTIFICATION_NUMBER);
		BasicPushNotificationBuilder builder=new BasicPushNotificationBuilder(mContext);
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;
		builder.notificationDefaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE; 
		builder.developerArg0="data";
		JPushInterface.setPushNotificationBuilder(1,builder);
	}
	
	/**
	 * 
	 */
	public void stop(){
		JPushInterface.stopPush(mContext);
	}
	
	/**
	 * 
	 */
	public void resume(){
		JPushInterface.resumePush(mContext);
	}
	
	/**
	 * 
	 * @param weekDays 0-6分别表示周日-周六
	 * @param startHour 范围0-23
	 * @param endHour 范围0-23
	 */
	public void setTime(HashSet<Integer> weekDays, int startHour, int endHour){
		JPushInterface.setPushTime(mContext, weekDays, startHour, endHour);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public boolean isValidAliasAndTags(String str){
		if(str==null||str.equals("")||str.length()>40){
			return false;
		}
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(str);
        return m.matches();
	}
	
	/**
	 * 
	 * @param alias 最大长度40
	 * @param tags 最大长度40，最多100个
	 */
	public void setAliasAndTags(String alias, LinkedHashSet<String> tags){
		JPushInterface.setAliasAndTags(mContext, alias, tags);
	}
}