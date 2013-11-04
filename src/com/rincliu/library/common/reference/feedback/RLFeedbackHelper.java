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
package com.rincliu.library.common.reference.feedback;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.fb.util.FeedBackListener;

public class RLFeedbackHelper {
	/**
	 * 
	 * @param context
	 * @param isDebug
	 */
	public static void init(Context context, boolean isDebug){
		com.umeng.common.Log.LOG=isDebug;
		UMFeedbackService.enableNewReplyNotification(context, NotificationType.AlertDialog);
		UMFeedbackService.setGoBackButtonVisible();
	}
	
	/**
	 * 
	 * @param context
	 */
	public static void gotoFeedback(Context context){
		UMFeedbackService.setFeedBackListener(new FeedBackListener() {
		    @Override
		    public void onSubmitFB(Activity activity) {
//		        EditText phoneText = (EditText) activity
//		                .findViewById(R.id.feedback_phone);
//		        EditText qqText = (EditText) activity
//		                .findViewById(R.id.feedback_qq);
//		        EditText nameText = (EditText) activity
//		                .findViewById(R.id.feedback_name);
//		        EditText emailText = (EditText) activity
//		                .findViewById(R.id.feedback_email);
//		        Map<String, String> contactMap = new HashMap<String, String>();
//		        contactMap.put("name", nameText.getText().toString());
//		        contactMap.put("phone", phoneText.getText().toString());
//		        contactMap.put("email", emailText.getText().toString());
//		        contactMap.put("qq", qqText.getText().toString());
//		        UMFeedbackService.setContactMap(contactMap);
		        Map<String, String> remarkMap = new HashMap<String, String>();
		        Display display=activity.getWindowManager().getDefaultDisplay();
		        DisplayMetrics dm = new DisplayMetrics();
				display.getMetrics(dm);
				remarkMap.put("width_pixels",""+dm.widthPixels);
				remarkMap.put("height_pixels",""+dm.heightPixels);
				remarkMap.put("density",""+dm.density);
		        UMFeedbackService.setRemarkMap(remarkMap);
		    }
		    @Override
		    public void onResetFB(Activity activity, Map<String, String> contactMap, Map<String, String> remarkMap) {
//		        EditText phoneText = (EditText) activity
//		                .findViewById(R.id.feedback_phone);
//		        EditText qqText = (EditText) activity
//		                .findViewById(R.id.feedback_qq);
//		        EditText nameText = (EditText) activity
//		                .findViewById(R.id.feedback_name);
//		        EditText emailText = (EditText) activity
//		                .findViewById(R.id.feedback_email);
//		        if (contactMap != null) {
//		        	nameText.setText(remarkMap.get("name"));
//		            phoneText.setText(contactMap.get("phone"));
//		            qqText.setText(contactMap.get("qq"));
//		            emailText.setText(contactMap.get("email"));
//		        }
		    }
		});
		UMFeedbackService.openUmengFeedbackSDK(context);
	}
}