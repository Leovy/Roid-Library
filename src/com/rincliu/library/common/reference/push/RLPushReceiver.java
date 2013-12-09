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
package com.rincliu.library.common.reference.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

public class RLPushReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        RLNotificationEntity ne = new RLNotificationEntity();
        ne.setId(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
        ne.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
        ne.setContent(bundle.getString(JPushInterface.EXTRA_ALERT));
        ne.setExtra(bundle.getString(JPushInterface.EXTRA_EXTRA));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
        {
            // TODO
        }
        else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction()))
        {
            // TODO
        }
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
        {
            // String msg=bundle.getString(JPushInterface.EXTRA_MESSAGE);
            // TODO
        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
        {
            // TODO
        }
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
        {
            Intent i = new Intent(context, RLNotificationActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("data", ne);
            context.startActivity(i);
        }
    }
}
