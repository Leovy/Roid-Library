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
package com.rincliu.library.common.reference.voice;

import java.util.ArrayList;

import com.rincliu.library.R;
import com.rincliu.library.util.RLUiUtil;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.SynthesizerDialog;
import com.iflytek.ui.SynthesizerDialogListener;

import android.content.Context;

public class RLVoiceHelper {
	
	private static RLVoiceHelper helper;
	private String mParams;
	private RATE mRate=RATE.rate16k;
	private Reader mReader=Reader.MALE;
	private int readSpeed=50;
	private int readVolume=100;
	
	/**
	 * 
	 */
	public enum Reader{
		MALE,
		FEMALE
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static RLVoiceHelper getInstance(Context context){
		if(helper==null){
			helper=new RLVoiceHelper(context);
		}
		return helper;
	}
	
	private RLVoiceHelper(Context context){
		mParams="appid="+context.getString(R.string.iflytek_key);
	}
	
	/**
	 * 
	 * @param reader
	 */
	public void setReader(Reader reader){
		mReader=reader;
	}
	
	/**
	 * 
	 * @param rate
	 */
	public void setRate(RATE rate){
		mRate=rate;
	}
	
	/**
	 * 
	 * @param speed
	 */
	public void setReadSpeed(int speed){
		if(0<=speed&&speed<=100){
			readSpeed=speed;
		}else{
			throw new IllegalStateException("Speed must be an integer between 0 and 100.");
		}
	}
	
	/**
	 * 
	 * @param volume
	 */
	public void setReadVolume(int volume){
		if(0<=volume&&volume<=100){
			readVolume=volume;
		}else{
			throw new IllegalStateException("Volume must be an integer between 0 and 100.");
		}
	}
	
	/**
	 * 
	 */
	public interface RecogniseListener{
		public void onReceived(String res);
	}
	
	/**
	 * 
	 * @param context
	 * @param listener
	 */
	public void recognise(final Context context, final RecogniseListener listener){
		RecognizerDialog dialog = new RecognizerDialog(context, mParams);
		dialog.setEngine("sms", null, null);
		dialog.setSampleRate(mRate);
		dialog.setListener(new RecognizerDialogListener(){
			@Override
			public void onEnd(SpeechError error) {
				showError(context,error);
			}
			@Override
			public void onResults(ArrayList<RecognizerResult> results,boolean isLast) {
				for (RecognizerResult recognizerResult : results) {
					builder.append(recognizerResult.text);
				}
				if(isLast){
					listener.onReceived(builder.toString());
					builder = new StringBuilder();
				}
			}
		});
		dialog.show();
	}
	
	private StringBuilder builder = new StringBuilder();
	
	/**
	 * 
	 * @param context
	 * @param source
	 * @param isShowDialog
	 */
	public void read(final Context context, String source, boolean isShowDialog){
		String voiceName=mReader==Reader.MALE?"xiaoyu":"xiaoyan";
		if(isShowDialog){
			SynthesizerDialog dialog = new SynthesizerDialog(context, mParams);
			dialog.setText(source, null);
			dialog.setSampleRate(mRate);
			dialog.setVoiceName(voiceName);
			dialog.setSpeed(readSpeed);
			dialog.setVolume(readVolume);
			dialog.setListener(new SynthesizerDialogListener(){
				@Override
				public void onEnd(SpeechError error) {
					showError(context,error);
				}
			});
			dialog.show();
		}else{
			SynthesizerPlayer player=SynthesizerPlayer.createSynthesizerPlayer(context, mParams);
			player.setSampleRate(mRate);
			player.setVoiceName(voiceName);
			player.setSpeed(readSpeed);
			player.setVolume(readVolume);
			player.playText(source, null, new SynthesizerPlayerListener(){
				@Override
				public void onBufferPercent(int percent,int beginPos,int endPos) {
				}
				@Override
				public void onEnd(SpeechError error) {
					showError(context,error);
				}
				@Override
				public void onPlayBegin() {
				}
				@Override
				public void onPlayPaused() {
				}
				@Override
				public void onPlayPercent(int percent,int beginPos,int endPos) {
				}
				@Override
				public void onPlayResumed() {
				}
			});
		}
	}
	
	private void showError(Context context, SpeechError error){
		if(error!=null){
			RLUiUtil.toast(context, error.getErrorDesc());
		}
	}
}