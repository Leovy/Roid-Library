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
package com.rincliu.library.common.reference.social.weixin;

import com.rincliu.library.R;
import com.rincliu.library.util.RLUiUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class RLWeixinHelper {
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    private static final int IMG_THUMB_SIZE = 150;

    private static RLWeixinHelper helper;

    private static IWXAPI api;

    private static Context mContext;

    private RLWeixinHelper(Context context) {
        if (api == null) {
            String key = context.getString(R.string.weixin_key);
            api = WXAPIFactory.createWXAPI(context, key, false);
            api.registerApp(key);
        }
    }

    /**
     * @param context
     * @return
     */
    public static RLWeixinHelper getInstance(Context context) {
        mContext = context;
        if (helper == null) {
            helper = new RLWeixinHelper(context);
        }
        return helper;
    }

    private boolean sendReq(WXMediaMessage msg, boolean isTimeline) {
        boolean res = false;
        int error = -1;
        if (api.isWXAppInstalled()) {
            if (api.isWXAppSupportAPI()) {
                if (isTimeline && api.getWXAppSupportAPI() < TIMELINE_SUPPORTED_VERSION) {
                    error = R.string.weixin_not_support_timeline;
                } else {
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                    res = api.sendReq(req);
                }
            } else {
                error = R.string.weixin_not_support_api;
            }
        } else {
            error = R.string.weixin_not_install;
        }
        if (error != -1) {
            Message message = new Message();
            Bundle data = new Bundle();
            data.putInt("error", error);
            message.setData(data);
            handler.sendMessage(message);
        }
        return res;
    }

    private static final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            RLUiUtil.toast(mContext, msg.getData().getInt("error"));
        }
    };

    private WXMediaMessage createMediaMsg(String title, String desc) {
        WXMediaMessage msg = new WXMediaMessage();
        msg.title = title;
        msg.description = desc;
        return msg;
    }

    private void setThumbBmpDataToMsg(WXMediaMessage msg, Bitmap bmp) {
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, IMG_THUMB_SIZE, IMG_THUMB_SIZE, true);
        bmp.recycle();
        if (thumbBmp != null && !thumbBmp.isRecycled()) {
            msg.setThumbImage(thumbBmp);
        }
    }

    /**
     * @param title
     * @param desc
     * @param text
     * @param isTimeline
     * @return
     */
    public boolean sendText(String title, String desc, String text, boolean isTimeline) {
        WXMediaMessage msg = createMediaMsg(title, desc);
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        msg.mediaObject = textObj;
        return sendReq(msg, isTimeline);
    }

    /**
     * @param title
     * @param desc
     * @param img
     * @param bmp
     * @param isTimeline
     * @return
     */
    public boolean sendImageBmp(String title, String desc, String img, Bitmap bmp, boolean isTimeline) {
        WXMediaMessage msg = createMediaMsg(title, desc);
        WXImageObject imgObj = new WXImageObject();
        if (img.startsWith("http://") || img.startsWith("https://")) {
            imgObj.imageUrl = img;
        } else {
            imgObj.setImagePath(img);
        }
        msg.mediaObject = imgObj;
        setThumbBmpDataToMsg(msg, bmp);
        return sendReq(msg, isTimeline);
    }

    /**
     * @param title
     * @param desc
     * @param url
     * @param bmp
     * @param isTimeline
     * @return
     */
    public boolean sendUrlBmp(String title, String desc, String url, Bitmap bmp, boolean isTimeline) {
        WXMediaMessage msg = createMediaMsg(title, desc);
        WXWebpageObject webpageObj = new WXWebpageObject();
        webpageObj.webpageUrl = url;
        msg.mediaObject = webpageObj;
        if (bmp != null && !bmp.isRecycled()) {
            setThumbBmpDataToMsg(msg, bmp);
        }
        return sendReq(msg, isTimeline);
    }

    /**
     * @param title
     * @param desc
     * @param musicUrl
     * @param bmp
     * @param isTimeline
     * @return
     */
    public boolean sendMusicUrlBmp(String title, String desc, String musicUrl, Bitmap bmp, boolean isTimeline) {
        WXMediaMessage msg = createMediaMsg(title, desc);
        WXMusicObject musicObj = new WXMusicObject();
        musicObj.musicUrl = musicUrl;
        msg.mediaObject = musicObj;
        if (bmp != null && !bmp.isRecycled()) {
            setThumbBmpDataToMsg(msg, bmp);
        }
        return sendReq(msg, isTimeline);
    }

    /**
     * @param title
     * @param desc
     * @param videoUrl
     * @param bmp
     * @param isTimeline
     * @return
     */
    public boolean sendVideoUrlBmp(String title, String desc, String videoUrl, Bitmap bmp, boolean isTimeline) {
        WXMediaMessage msg = createMediaMsg(title, desc);
        WXVideoObject videoObj = new WXVideoObject();
        videoObj.videoUrl = videoUrl;
        msg.mediaObject = videoObj;
        if (bmp != null && !bmp.isRecycled()) {
            setThumbBmpDataToMsg(msg, bmp);
        }
        return sendReq(msg, isTimeline);
    }

    boolean handleIntent(Intent intent, IWXAPIEventHandler handler) {
        return api.handleIntent(intent, handler);
    }
}
