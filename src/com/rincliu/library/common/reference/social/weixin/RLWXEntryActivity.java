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
package com.rincliu.library.common.reference.social.weixin;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.content.Intent;
import android.os.Bundle;

import com.rincliu.library.R;
import com.rincliu.library.activity.RLActivity;
import com.rincliu.library.util.RLUiUtil;

public class RLWXEntryActivity extends RLActivity implements IWXAPIEventHandler{
	private RLWeixinHelper helper;
	
	@Override
	protected void onCreate(Bundle b){
		super.onCreate(b);
		helper=RLWeixinHelper.getInstance(this);
		helper.handleIntent(getIntent(), this);
	}
	
	@Override
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
		helper.handleIntent(intent, this);
	}
	
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			//TODO;
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			//TODO:
			break;
		default:
			break;
		}
	}
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.weixin_send_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.weixin_send_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.weixin_send_deny;
			break;
		default:
			result = R.string.weixin_send_unknown_error;
			break;
		}
		RLUiUtil.toast(this, result);
		finish();
	}
}