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
package com.rincliu.library.widget.dialog;

import com.rincliu.library.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class RLLoadingDialog extends RLDialog{	
	private Context context;
	private TextView tv_msg;
	
	/**
	 * 
	 * @param context
	 */
	public RLLoadingDialog(Context context) {
		super(context);
		this.context=context;
		super.createView();
		super.setCanceledOnTouchOutside(false);
	}
	
	@Override
	protected View getView() {
		View view=LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		tv_msg=(TextView) view.findViewById(R.id.tv_msg);
		return view;
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void setMessage(String msg){
		tv_msg.setVisibility(View.VISIBLE);
		tv_msg.setText(msg);
	}
	
	/**
	 * 
	 * @param msgStrResId
	 */
	public void setMessage(int msgStrResId){
		setMessage(context.getString(msgStrResId));
	}
}