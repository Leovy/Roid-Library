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
package com.rincliu.library.common.reference.analytics;

import java.util.HashMap;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class RLAnalyticsHelper {

    /**
     * @param context
     * @param isDebug
     */
    public static void init(Context context, boolean isDebug) {
        com.umeng.common.Log.LOG = isDebug;
        MobclickAgent.setDebugMode(isDebug);
        MobclickAgent.setAutoLocation(true);
        MobclickAgent.setSessionContinueMillis(1000);
        // MobclickAgent.setUpdateOnlyWifi(false);
        // MobclickAgent.setDefaultReportPolicy(context,
        // ReportPolicy.BATCH_BY_INTERVAL, 5*1000);
        MobclickAgent.updateOnlineConfig(context);
        MobclickAgent.onError(context);
    }

    /**
     * @param context
     */
    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    /**
     * @param context
     */
    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    /**
     * @param context
     * @param event
     */
    public static void onEvent(Context context, String event) {
        MobclickAgent.onEvent(context, event);
    }

    /**
     * @param context
     * @param event
     * @param map
     */
    public static void onEvent(Context context, String event, HashMap<String, String> map) {
        MobclickAgent.onEvent(context, event, map);
    }
}
