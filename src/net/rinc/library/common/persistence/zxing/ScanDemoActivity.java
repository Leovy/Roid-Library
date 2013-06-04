package net.rinc.library.common.persistence.zxing;

import net.rinc.library.R;
import net.rinc.library.base.RCActivity;
import net.rinc.library.util.UiUtil;
import net.rinc.library.widget.dialog.PromptDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;

public class ScanDemoActivity extends RCActivity {
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
            	UiUtil.toast(this, R.string.scan_retry);
                return;
            }
            PromptDialog dialog=new PromptDialog(this,getString(R.string.scan_result),
            		result,Linkify.ALL,
            		getString(android.R.string.yes),
            		getString(android.R.string.cancel),new PromptDialog.Listener(){
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