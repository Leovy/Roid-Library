package net.rinc.library.common.reference.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

public class RCPushReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        NotificationEntity ne=new NotificationEntity();
		ne.setId(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
    	ne.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
		ne.setContent(bundle.getString(JPushInterface.EXTRA_ALERT));
		ne.setExtra(bundle.getString(JPushInterface.EXTRA_EXTRA));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        	//TODO
        }else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())){
        	//TODO
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//        	String msg=bundle.getString(JPushInterface.EXTRA_MESSAGE);
        	//TODO
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            //TODO
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
        	Intent i = new Intent(context, NotificationActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.putExtra("data", ne);
        	context.startActivity(i);
        }
	}
}