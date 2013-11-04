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
package com.rincliu.library.common.reference.social.weibo;

import com.weibo.sdk.android.sso.SsoHandler;

import com.rincliu.library.app.RLActivity;
import com.rincliu.library.common.reference.social.weibo.RLWeiboHelper.ReqHandler;
import com.rincliu.library.util.RLUiUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class RLWeiboDemoActivity extends RLActivity{
	private RLWeiboHelper weibo;
	private SsoHandler ssoHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		weibo = RLWeiboHelper.getInstance(this);
		if(weibo.isTokenAvailable(this)){
        	sendWeibo();
        }else{
        	ssoHandler=weibo.auth(this, new ReqHandler(){
				@Override
				public void onSucceed() {
					sendWeibo();
				}
				@Override
				public void onFail(String error) {
					sendMsg("授权失败:"+error);
				}
        	});
        }
	}
	
	private void sendWeibo(){
    	weibo.sendText(this,"content","32.0","120.0",new ReqHandler(){
			@Override
			public void onSucceed() {
				sendMsg("成功分享到新浪微博");
			}
			@Override
			public void onFail(String error) {
				sendMsg(error);
			}
		});
    }
	
	private void sendMsg(String msg){
		Message message=new Message();
		message.obj=msg;
		handler.sendMessage(message);
	}
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			RLUiUtil.toast(RLWeiboDemoActivity.this,msg.obj.toString());
		}
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        weibo.onActivityResult(ssoHandler, requestCode, resultCode, data);
	}
}