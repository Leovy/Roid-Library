package com.rincliu.library.common.persistence.zxing;

import com.rincliu.library.R;
import com.rincliu.library.app.RLActivity;
import com.rincliu.library.util.RLUiUtil;
import com.rincliu.library.widget.dialog.RLAlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;

public class ScanDemoActivity extends RLActivity {
	private static final int MSG_SCAN=888;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivityForResult(new Intent(ScanDemoActivity.this,ScanActivity.class),MSG_SCAN);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 888) {
            String result = data.getExtras().getString("result");
            if (TextUtils.isEmpty(result)) {
            	RLUiUtil.toast(this, R.string.scan_retry);
                return;
            }
            RLAlertDialog dialog=new RLAlertDialog(this,getString(R.string.scan_result),
            		result,Linkify.ALL,
            		getString(android.R.string.yes),
            		getString(android.R.string.cancel),new RLAlertDialog.Listener(){
				@Override
				public void onLeftClick() {
				}
				@Override
				public void onRightClick() {
				}
            });
            dialog.show();
        }
    }
}