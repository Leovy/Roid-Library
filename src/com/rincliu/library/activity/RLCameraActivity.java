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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.rincliu.library.R;
import com.rincliu.library.util.RLSysUtil;
import com.rincliu.library.widget.RLOnClickListener;
import com.rincliu.library.widget.dialog.RLLoadingDialog;

public class RLCameraActivity extends RLActivity {
    private Camera camera;

    private byte[] mData;

    private boolean isFlashEnabled = false;

    private String flashMode = Parameters.FLASH_MODE_OFF;

    private ImageView iv_flash, iv_yes, iv_no, iv_camera;

    private RLLoadingDialog pd;

    private int maxWidth, maxHeight;

    private long maxSize;

    private String outputFile;

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (!RLSysUtil.isExternalStorageAvailable()) {
            exit(false);
        }
        
        setContentView(R.layout.activity_camera);

        String savePath = getIntent().getStringExtra("savePath");
        DisplayMetrics dm = getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        maxWidth = getIntent().getIntExtra("maxWidth", dm.widthPixels);
        maxHeight = getIntent().getIntExtra("maxHeight", dm.heightPixels);
        maxSize = getIntent().getLongExtra("maxSize", 4 * dm.widthPixels * dm.heightPixels);
        
        if (TextUtils.isEmpty(savePath)) {
            savePath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM;
        } else {
            if (!new File(savePath).exists()) {
                new File(savePath).mkdirs();
            }
        }
        if (!savePath.endsWith(File.separator)) {
            savePath += File.separator;
        }
        outputFile = savePath + System.currentTimeMillis() + ".jpg";

        pd = new RLLoadingDialog(this);
        iv_flash = (ImageView) findViewById(R.id.iv_flash);
        iv_yes = (ImageView) findViewById(R.id.iv_yes);
        iv_no = (ImageView) findViewById(R.id.iv_no);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        SurfaceView surfaceView = (SurfaceView) this.findViewById(R.id.sv_camera);

        if (getIntent().getBooleanExtra("isAutoFlash", false)) {
            iv_flash.setVisibility(View.GONE);
            flashMode = Parameters.FLASH_MODE_AUTO;
        }
        iv_flash.setOnClickListener(new RLOnClickListener() {
            @Override
            public void onClickX(View view) {
                isFlashEnabled = !isFlashEnabled;
                flashMode = isFlashEnabled ? Parameters.FLASH_MODE_ON : Parameters.FLASH_MODE_OFF;
                Parameters params = camera.getParameters();
                params.setFlashMode(flashMode);
                camera.setParameters(params);
                iv_flash.setImageDrawable(getResources().getDrawable(
                        isFlashEnabled ? R.drawable.btn_camera_flash_on : R.drawable.btn_camera_flash_off));
            }
        });
        iv_yes.setOnClickListener(new RLOnClickListener() {
            @Override
            public void onClickX(View arg0) {
                pd.show();
                new Thread() {
                    @Override
                    public void run() {
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inJustDecodeBounds = true;
                        Bitmap srcBmp = BitmapFactory.decodeByteArray(mData, 0, mData.length, opts);
                        opts.inJustDecodeBounds = false;
                        int w = opts.outWidth;
                        int h = opts.outHeight;
                        int size = 0;
                        if (w > maxWidth || h > maxHeight) {
                            double scale = 1;
                            if (w > maxWidth) {
                                if (h > maxHeight) {
                                    scale = w > h ? w / maxWidth : h / maxHeight;
                                } else {
                                    scale = w / maxWidth;
                                }
                            } else {
                                scale = h / maxHeight;
                            }
                            double log = Math.log(scale) / Math.log(2);
                            double logCeil = Math.ceil(log);
                            size = (int) Math.pow(2, logCeil);
                        }
                        opts.inSampleSize = size;
                        srcBmp = BitmapFactory.decodeByteArray(mData, 0, mData.length, opts);
                        Bitmap dstBmp;
                        float degrees = 0f;
                        switch (getDisplayRotation()) {
                            case Surface.ROTATION_0:
                                degrees = 90f;
                                break;
                            case Surface.ROTATION_90:
                                degrees = 0f;
                                break;
                            case Surface.ROTATION_180:
                                degrees = 270f;
                                break;
                            case Surface.ROTATION_270:
                                degrees = 180f;
                                break;
                        }
                        Matrix matrix = new Matrix();
                        matrix.reset();
                        matrix.postRotate(degrees);
                        dstBmp = Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.getWidth(), srcBmp.getHeight(), matrix, true);
                        if (!srcBmp.isRecycled()) {
                            srcBmp.recycle();
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int quality = 100;
                        dstBmp.compress(CompressFormat.JPEG, quality, baos);
                        while (baos.toByteArray().length > maxSize) {
                            baos.reset();
                            dstBmp.compress(CompressFormat.JPEG, quality, baos);
                            quality -= 10;
                        }
                        try {
                            baos.writeTo(new FileOutputStream(outputFile));
                        } catch (Exception e) {
                            e.printStackTrace();
                            exit(false);
                        } finally {
                            try {
                                baos.flush();
                                baos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                exit(false);
                            }
                        }
                        if (!dstBmp.isRecycled()) {
                            dstBmp.recycle();
                        }
                        getIntent().putExtra("outputFile", outputFile);
                        
                        String path = new File(outputFile).getParent();
                        String mime = "image/jpg";
                        MediaScannerConnection.scanFile(RLCameraActivity.this, new String[] {path}, new String[] {mime},
                                new OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String arg0, Uri arg1) {
                                        exit(true);
                                    }
                                });
                    }
                }.start();
            }
        });

        iv_no.setOnClickListener(new RLOnClickListener() {
            @Override
            public void onClickX(View arg0) {
                camera.startPreview();
                iv_yes.setVisibility(View.GONE);
                iv_no.setVisibility(View.GONE);
                iv_camera.setVisibility(View.VISIBLE);
            }
        });

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        iv_yes.setVisibility(View.VISIBLE);
                        iv_no.setVisibility(View.VISIBLE);
                        iv_camera.setVisibility(View.GONE);
                        mData = data;
                    }
                });
            }
        });

        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(true);
        SurfaceHolder holder = surfaceView.getHolder();
        if (android.os.Build.VERSION.SDK_INT < 11) {
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode(flashMode);
                params.setPictureFormat(ImageFormat.JPEG);
                switch (getDisplayRotation()) {
                    case Surface.ROTATION_0:
                        camera.setDisplayOrientation(90);
                        break;
                    case Surface.ROTATION_90:
                        camera.setDisplayOrientation(0);
                        break;
                    case Surface.ROTATION_180:
                        camera.setDisplayOrientation(270);
                        break;
                    case Surface.ROTATION_270:
                        camera.setDisplayOrientation(180);
                        break;
                }
                setBestPreviewSize(params);
                camera.setParameters(params);
                camera.startPreview();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (camera == null) {
                    camera = Camera.open();
                }
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    camera.autoFocus(new AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                Camera.Parameters params = camera.getParameters();
                                params.setFlashMode(flashMode);
                                params.setPictureFormat(ImageFormat.JPEG);
                                setBestPreviewSize(params);
                                camera.setParameters(params);
                            }
                        }
                    });
                } catch (Exception e) {
                    camera.release();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.stopPreview();
                camera.release();
                mData = null;
                camera = null;
            }
        });
    }
    
    private void setBestPreviewSize(Camera.Parameters params)
    {
        int maxPreviewWidth = 0, maxPreviewHeight = 0;
        for (Size size : params.getSupportedPreviewSizes())
        {
            if (size.width >= maxPreviewWidth || size.height >= maxPreviewHeight)
            {
                maxPreviewWidth = size.width;
                maxPreviewHeight = size.height;
            }
        }
        params.setPreviewSize(maxPreviewWidth, maxPreviewHeight);
    }

    private final Handler handler = new Handler();

    private void exit(final boolean isSuccess) {
        mData = null;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                setResult(isSuccess ? RESULT_OK : RESULT_CANCELED, getIntent());
                finish();
                overridePendingTransition(R.anim.reload, R.anim.reload);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit(false);
    }
}
