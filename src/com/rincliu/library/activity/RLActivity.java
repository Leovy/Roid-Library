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
package com.rincliu.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.rincliu.library.BuildConfig;
import com.rincliu.library.R;
import com.rincliu.library.app.RLApplication;
import com.rincliu.library.app.RLCrashHandler;
import com.rincliu.library.common.persistence.afinal.RLFinalActivity;
import com.rincliu.library.common.reference.analytics.RLAnalyticsHelper;
import com.rincliu.library.common.reference.feedback.RLFeedbackHelper;
import com.rincliu.library.util.RLSysUtil;

public class RLActivity extends RLFinalActivity {
    private boolean isEnableCrashHandler = true;

    private boolean isEnableAnalytics = false;

    private boolean isEnableFeedback = false;

    private int displayRotation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayRotation = getWindowManager().getDefaultDisplay().getRotation();

        String enableCrashHandler = RLSysUtil.getApplicationMetaData(this, "ENABLE_CRASH_HANDLER");
        if (enableCrashHandler != null && enableCrashHandler.equals("false")) {
            isEnableCrashHandler = false;
        }
        String enableAnalytics = RLSysUtil.getApplicationMetaData(this, "ENABLE_ANALYTICS");
        if (enableAnalytics != null && enableAnalytics.equals("true")) {
            isEnableAnalytics = true;
        }
        String enableFeedback = RLSysUtil.getApplicationMetaData(this, "ENABLE_FEEDBACK");
        if (enableFeedback != null && enableFeedback.equals("true")) {
            isEnableFeedback = true;
        }

        if (isEnableCrashHandler) {
            RLCrashHandler.getInstance().init(this);
        }
        if (isEnableAnalytics) {
            RLAnalyticsHelper.init(this, BuildConfig.DEBUG);
        }
        if (isEnableFeedback) {
            RLFeedbackHelper.init(this, BuildConfig.DEBUG);
        }

        if (getApplication() instanceof RLApplication) {
            ((RLApplication) getApplication()).setDisplayInfo(RLSysUtil.getDisplayInfo(this));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        displayRotation = getWindowManager().getDefaultDisplay().getRotation();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isEnableAnalytics) {
            RLAnalyticsHelper.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isEnableAnalytics) {
            RLAnalyticsHelper.onPause(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    @Override
    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
        super.startActivityFromChild(child, intent, requestCode);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void finishFromChild(Activity child) {
        super.finishFromChild(child);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void finishActivityFromChild(Activity child, int requestCode) {
        super.finishActivityFromChild(child, requestCode);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getApplication() instanceof RLApplication) {
            ((RLApplication) getApplication()).setDisplayInfo(RLSysUtil.getDisplayInfo(this));
        }
        displayRotation = getWindowManager().getDefaultDisplay().getRotation();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * @return
     */
    public int getDisplayRotation() {
        return displayRotation;
    }
}
