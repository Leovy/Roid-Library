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
package com.rincliu.library.app;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;

import com.rincliu.library.R;
import com.rincliu.library.util.RLUiUtil;

public class RLCrashHandler implements UncaughtExceptionHandler
{
    private Context mContext;

    private static RLCrashHandler instance;

    /**
     * @return
     */
    public static RLCrashHandler getInstance()
    {
        if (instance == null)
        {
            instance = new RLCrashHandler();
        }
        return instance;
    }

    /**
     * @param context
     */
    public void init(Context context)
    {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex)
    {
        ex.printStackTrace();
        new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                RLUiUtil.toast(mContext, R.string.exception_occured);
                Looper.loop();
            }
        }.start();
        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        // Intent intent=new Intent(Intent.ACTION_MAIN);
        // intent.addCategory(Intent.CATEGORY_HOME);
        // mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        // ((ActivityManager)
        // mContext.getSystemService(Context.ACTIVITY_SERVICE))
        // .killBackgroundProcesses(mContext.getPackageName());
    }
}
