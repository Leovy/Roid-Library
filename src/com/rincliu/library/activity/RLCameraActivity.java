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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import com.rincliu.library.util.RLSysUtil;
import com.rincliu.library.widget.RLOnClickListener;
import com.rincliu.library.R;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

public class RLCameraActivity extends RLActivity{
	private Camera camera;
	private byte[] mData;
	private boolean isFlashEnabled=false;
	private int rotation=0;
	private String flashMode=Parameters.FLASH_MODE_OFF;
	private ImageView iv_flash, iv_yes, iv_no, iv_camera;
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		if(!getIntent().hasExtra("savePath")||!RLSysUtil.isExternalStorageAvailable()){
			return;
		}
		rotation=getWindowManager().getDefaultDisplay().getRotation();
		iv_flash=(ImageView)findViewById(R.id.iv_flash);
		iv_yes=(ImageView)findViewById(R.id.iv_yes);
		iv_no=(ImageView)findViewById(R.id.iv_no);
		iv_camera=(ImageView)findViewById(R.id.iv_camera);
		SurfaceView surfaceView=(SurfaceView)this.findViewById(R.id.sv_camera);
		
		if(getIntent().getBooleanExtra("isAutoFlash", false)){
			iv_flash.setVisibility(View.GONE);
			flashMode=Parameters.FLASH_MODE_AUTO;
		}
		iv_flash.setOnClickListener(new RLOnClickListener(){
			@Override
			public void onClickX(View view) {
				isFlashEnabled=!isFlashEnabled;
				flashMode=isFlashEnabled?Parameters.FLASH_MODE_ON:Parameters.FLASH_MODE_OFF;
				Parameters params=camera.getParameters(); 
				params.setFlashMode(flashMode);
				camera.setParameters(params);  
				iv_flash.setImageDrawable(getResources().getDrawable(
						isFlashEnabled?R.drawable.btn_camera_flash_on:R.drawable.btn_camera_flash_off));
			}
		});
		iv_yes.setOnClickListener(new RLOnClickListener(){
			@Override
			public void onClickX(View arg0) {
				try{
					Bitmap bMap=BitmapFactory.decodeByteArray(mData, 0, mData.length);
					Bitmap bMapRotate;
					float degrees=0f;
					switch(rotation){
					case Surface.ROTATION_0:
						degrees=90f;
						break;
					case Surface.ROTATION_90:
						degrees=0f;
						break;
					case Surface.ROTATION_180:
						degrees=270f;
						break;
					case Surface.ROTATION_270:
						degrees=180f;
						break;
					}
					Matrix matrix=new Matrix();
					matrix.reset();
					matrix.postRotate(degrees);
					bMapRotate=Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), 
							bMap.getHeight(), matrix, true);
					bMap=bMapRotate;
					String savePath=getIntent().getStringExtra("savePath");
					BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(savePath));
					bMap.compress(Bitmap.CompressFormat.JPEG,100,bos);
					bos.flush();
					bos.close();
					setResult(RESULT_OK, getIntent());
				}catch(Exception e){
					e.printStackTrace();
					setResult(RESULT_CANCELED, getIntent());
				}finally{
					finish();
					overridePendingTransition(R.anim.reload, R.anim.reload);
				}
			}
		});
		
		iv_no.setOnClickListener(new RLOnClickListener(){
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
				camera.takePicture(null, null, new PictureCallback(){
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						iv_yes.setVisibility(View.VISIBLE);
						iv_no.setVisibility(View.VISIBLE);
						iv_camera.setVisibility(View.GONE);
						mData=data;
					}
				});
			}
		});
		
		surfaceView.setFocusable(true); 
		surfaceView.setFocusableInTouchMode(true);
		SurfaceHolder holder=surfaceView.getHolder();
		if(android.os.Build.VERSION.SDK_INT<11){
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				Camera.Parameters params=camera.getParameters(); 
				params.setFlashMode(flashMode);
				params.setPictureFormat(ImageFormat.JPEG);
				switch(rotation){
				case Surface.ROTATION_0:
		        	params.setPreviewSize(height, width);
		        	camera.setDisplayOrientation(90);
		        	break;
		        case Surface.ROTATION_90:
		        	params.setPreviewSize(width, height);
		        	camera.setDisplayOrientation(0);
		        	break;
		        case Surface.ROTATION_180:
		        	params.setPreviewSize(height, width);
		        	camera.setDisplayOrientation(270);
		        	break;
		        case Surface.ROTATION_270:
		        	params.setPreviewSize(width, height);
		        	camera.setDisplayOrientation(180);
		        	break;
				}
				params.setPreviewSize(640,480);
				camera.setParameters(params);
				camera.startPreview(); 
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				camera=Camera.open();
				try {
					camera.setPreviewDisplay(holder);
					camera.startPreview();
					camera.autoFocus(new AutoFocusCallback(){
						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							if(success){
								Camera.Parameters params=camera.getParameters();
								params.setFlashMode(flashMode);
								params.setPictureFormat(ImageFormat.JPEG);
								params.setPreviewSize(640,480);
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
				mData=null;
				camera=null;
			}
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		rotation=getWindowManager().getDefaultDisplay().getRotation();
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition(R.anim.reload, R.anim.reload);
		mData=null;
	}
}