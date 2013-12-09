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
package com.rincliu.library.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class RLIntentUtil
{
    /**
     * @param outputFile
     * @return
     */
    public static Intent getCameraIntent(String outputFile)
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputFile)));
        return intent;
    }

    /**
     * @param uri
     * @param outputX
     * @param outputY
     * @return
     */
    public static Intent getCutPicIntent(Uri uri, int outputX, int outputY)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        return intent;
    }

    /**
     * @return
     */
    public static Intent getGalleryIntent()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    /**
     * @param context
     * @param chooserTitle
     * @param shareTitle
     * @param shareText
     * @param mime
     * @param uri
     */
    public static void callSysShare(Context context, String chooserTitle, String shareTitle, String shareText,
            String mime, Uri uri)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        intent.putExtra(Intent.EXTRA_SUBJECT, shareTitle);
        intent.setType(mime);
        if (uri != null)
        {
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        context.startActivity(Intent.createChooser(intent, chooserTitle));
    }

    /**
     * @param context
     * @return
     */
    public static Intent getSysAppSearchIntent(String key)
    {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + key));
    }

    /**
     * @param context
     * @return
     */
    public static Intent getSysAppDetailIntent(Context context)
    {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
    }
}
