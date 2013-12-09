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
package com.rincliu.library.common.reference.update;

import com.rincliu.library.R;
import com.rincliu.library.util.RLUiUtil;
import com.rincliu.library.widget.dialog.RLLoadingDialog;
import android.content.Context;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class RLUpdateHelper
{
    /**
     * @param context
     * @param isQuietly
     */
    public static void checkUpdate(final Context context, final boolean isQuietly)
    {
        final RLLoadingDialog pd = new RLLoadingDialog(context);
        if (!isQuietly)
        {
            pd.setMessage(R.string.is_checking_update);
            pd.show();
        }
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setOnDownloadListener(null);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener()
        {
            @Override
            public void onUpdateReturned(int status, UpdateResponse resp)
            {
                pd.dismiss();
                if (status == 0)
                {
                    UmengUpdateAgent.showUpdateDialog(context, resp);
                }
                else
                {
                    if (isQuietly)
                    {
                        return;
                    }
                    if (status == 1)
                    {
                        RLUiUtil.toast(context, R.string.UMNoUpdate);
                    }
                    else if (status == 2)
                    {
                        RLUiUtil.toast(context, R.string.UMNoWifi);
                    }
                    else if (status == 3)
                    {
                        RLUiUtil.toast(context, R.string.UMTimeout);
                    }
                }
            }
        });
        UmengUpdateAgent.update(context);
    }
}
