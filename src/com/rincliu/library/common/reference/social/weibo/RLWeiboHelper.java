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
package com.rincliu.library.common.reference.social.weibo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.rincliu.library.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;

public class RLWeiboHelper
{
    private static RLWeiboHelper helper;

    private static Weibo weibo;

    private static final String REDIRECT_URL = "http://open.weibo.com/apps/";

    private RLWeiboHelper(Context context)
    {
        if (weibo == null)
        {
            String key = context.getString(R.string.weibo_key);
            weibo = Weibo.getInstance(key, REDIRECT_URL + key);
        }
    }

    /**
     * @param context
     * @return
     */
    public static RLWeiboHelper getInstance(Context context)
    {
        if (helper == null)
        {
            helper = new RLWeiboHelper(context);
        }
        return helper;
    }

    /**
     * @param context
     * @return
     */
    public boolean isTokenAvailable(Context context)
    {
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
        return token != null && token.isSessionValid();
    }

    /**
     * @param activity
     * @param handler
     * @return
     */
    public SsoHandler auth(final Activity activity, final ReqHandler handler)
    {
        SsoHandler ssoHandler = new SsoHandler(activity, weibo);
        ssoHandler.authorize(new WeiboAuthListener()
        {
            @Override
            public void onComplete(Bundle data)
            {
                AccessTokenKeeper.clear(activity);
                Oauth2AccessToken token = new Oauth2AccessToken(data.getString("access_token"),
                        data.getString("expires_in"));
                AccessTokenKeeper.keepAccessToken(activity, token);
                handler.onSucceed();
            }

            @Override
            public void onCancel()
            {
                handler.onFail(activity.getString(R.string.weibo_auth_cancel));
            }

            @Override
            public void onError(WeiboDialogError error)
            {
                handler.onFail(error.getMessage());
            }

            @Override
            public void onWeiboException(WeiboException exception)
            {
                handler.onFail(exception.getMessage());
            }
        });
        return ssoHandler;
    }

    /**
     * @param handler
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(SsoHandler handler, int requestCode, int resultCode, Intent data)
    {
        if (handler != null)
        {
            handler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
	 * 
	 */
    public interface ReqHandler
    {
        public void onSucceed();

        public void onFail(String error);
    }

    /**
     * @param context
     * @param content
     * @param latitude
     * @param longitude
     * @param handler
     */
    public void sendText(final Context context, String content, String latitude, String longitude,
            final ReqHandler handler)
    {
        new StatusesAPI(AccessTokenKeeper.readAccessToken(context)).update(content, latitude, longitude,
                getRequestListener(context, handler));
    }

    /**
     * @param context
     * @param content
     * @param imgFile
     * @param latitude
     * @param longitude
     * @param handler
     */
    public void sendImageText(final Context context, String content, File imgFile, String latitude, String longitude,
            final ReqHandler handler)
    {
        new StatusesAPI(AccessTokenKeeper.readAccessToken(context)).upload(content, imgFile.getPath(), latitude,
                longitude, getRequestListener(context, handler));
    }

    /**
     * @param context
     * @param content
     * @param imgUrl
     * @param latitude
     * @param longitude
     * @param handler
     */
    public void sendImageText(final Context context, String content, URL imgUrl, String latitude, String longitude,
            final ReqHandler handler)
    {
        new StatusesAPI(AccessTokenKeeper.readAccessToken(context)).uploadUrlText(content, imgUrl.toString(), latitude,
                longitude, getRequestListener(context, handler));
    }

    private RequestListener getRequestListener(final Context context, final ReqHandler handler)
    {
        return new RequestListener()
        {
            @Override
            public void onComplete(String arg0)
            {
                handler.onSucceed();
            }

            @Override
            public void onError(WeiboException exception)
            {
                String msg = exception.getMessage();
                if (msg != null && !msg.equals(""))
                {
                    try
                    {
                        JSONObject obj = new JSONObject(msg);
                        if (obj != null && obj.has("error"))
                        {
                            handler.onFail(obj.get("error").toString());
                        }
                        else
                        {
                            handler.onFail(context.getString(R.string.weibo_unknow_error));
                        }
                    }
                    catch (JSONException e)
                    {
                        handler.onFail(context.getString(R.string.weibo_unknow_error));
                    }
                }
                else
                {
                    handler.onFail(context.getString(R.string.weibo_unknow_error));
                }
            }

            @Override
            public void onIOException(IOException arg0)
            {
                handler.onFail(context.getString(R.string.weibo_io_error));
            }
        };
    }
}
