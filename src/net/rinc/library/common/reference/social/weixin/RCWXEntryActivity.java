package net.rinc.library.common.reference.social.weixin;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.content.Intent;
import android.os.Bundle;
import net.rinc.library.R;
import net.rinc.library.base.RCActivity;
import net.rinc.library.util.UiUtil;

public class RCWXEntryActivity extends RCActivity implements IWXAPIEventHandler{
	private WeixinHelper helper;
	
	@Override
	protected void onCreate(Bundle b){
		super.onCreate(b);
		helper=WeixinHelper.getInstance(this);
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
		UiUtil.toast(this, result);
		finish();
	}
}