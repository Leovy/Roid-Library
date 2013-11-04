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
package com.rincliu.library.util;

import com.rincliu.library.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RLUiUtil {
	/**
	 * 
	 * @param context
	 * @param msg
	 * @param duration
	 */
	public static void toast(Context context, String msg, int duration){
		Toast toast;
		toast=new Toast(context);
		View view=LayoutInflater.from(context).inflate(R.layout.toast, null);
		TextView tv=(TextView) view.findViewById(R.id.tv_msg);
		tv.setText(msg);
		toast.setView(view);
		toast.setDuration(duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	/**
	 * 
	 * @param context
	 * @param msg
	 */
	public static void toast(Context context, String msg){
		toast(context, msg, Toast.LENGTH_SHORT);
	}
	/**
	 * 
	 * @param context
	 * @param strResId
	 * @param duration
	 */
	public static void toast(Context context, int strResId, int duration){
		toast(context,context.getString(strResId),duration);
	}
	/**
	 * 
	 * @param context
	 * @param strResId
	 */
	public static void toast(Context context, int strResId){
		toast(context,context.getString(strResId));
	}
}