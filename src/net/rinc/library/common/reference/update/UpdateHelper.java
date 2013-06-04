package net.rinc.library.common.reference.update;

import net.rinc.library.R;
import net.rinc.library.util.UiUtil;
import net.rinc.library.widget.dialog.TransitionDialog;
import android.content.Context;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class UpdateHelper {
	/**
	 * 
	 * @param context
	 * @param isQuietly
	 */
	public static void checkUpdate(final Context context, final boolean isQuietly){
		final TransitionDialog pd=new TransitionDialog(context);
		if(!isQuietly){
			pd.setMessage(R.string.is_checking_update);
			pd.show();
		}
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setOnDownloadListener(null);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int status,UpdateResponse resp) {
				pd.dismiss();
				if(status==0){
					UmengUpdateAgent.showUpdateDialog(context, resp);
				}else{
					if(isQuietly){
						return;
					}
					if (status == 1) {
						UiUtil.toast(context, R.string.UMNoUpdate);
					} else if (status == 2) {
						UiUtil.toast(context, R.string.UMNoWifi);
					} else if (status == 3) {
						UiUtil.toast(context, R.string.UMTimeout);
					}
				}
			}
		});
		UmengUpdateAgent.update(context);
	}
}