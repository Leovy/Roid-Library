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
package com.rincliu.library.widget.view;

import com.rincliu.library.R;
import com.rincliu.library.util.RLUiUtil;
import com.rincliu.library.widget.RLOnClickListener;
import com.rincliu.library.widget.dialog.RLListDialog;
import com.rincliu.library.widget.dialog.RLAlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RLWebBrowser extends ViewGroup{
	private Context context;
	private WebView webView;
	private boolean isAllowClearCookie=true;
	private boolean isLoadFinished=false;
	private ImageView iv_refresh, iv_goback, iv_goforward, iv_stop, iv_more;
	private LinearLayout layout_loading;
	
	/**
	 * 
	 * @param context
	 */
	public RLWebBrowser(Context context) {
		super(context);
		init(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public RLWebBrowser(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RLWebBrowser(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) { 
        for (int i = 0; i < getChildCount(); i++) {  
            View child = getChildAt(i);  
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());  
        }
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childCount = getChildCount() ;  
		for(int i=0 ;i<childCount ;i++){  
		  View child = getChildAt(i) ;  
		  child.measure(widthMeasureSpec, heightMeasureSpec);
		 }
	}
	
	/**
	 * 
	 * @param url
	 * @param listener
	 */
	public void load(String url, Listener listener){
		this.listener=listener;
		listener.onPrepare();
		if(url!=null&&URLUtil.isValidUrl(url)){
			webView.loadUrl(url);
		}else{
			RLUiUtil.toast(context, R.string.invalid_url);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public WebView getWebView(){
		return webView;
	}
	
	/**
	 * 
	 */
	public void denyClearCookie(){
		isAllowClearCookie=false;
	}
	
	private Listener listener;
	
	/**
	 * 
	 */
	public interface Listener{
		public void onPrepare();
		public void onReceivedTitle(String title);
		public void onReceivedIcon(Bitmap icon);
		public void onReceivedUrl(String url);
	}
	
	private void init(Context context){
		this.context=context;
		View rootView = LayoutInflater.from(context).inflate(R.layout.web_browser, null);
		this.addView(rootView);
		layout_loading=(LinearLayout) rootView.findViewById(R.id.layout_loading);
		layout_loading.setVisibility(View.GONE);
		iv_refresh=(ImageView) rootView.findViewById(R.id.iv_refresh);
		iv_refresh.setOnClickListener(myClickListener);
		iv_stop=(ImageView) rootView.findViewById(R.id.iv_stop);
		iv_stop.setOnClickListener(myClickListener);
		iv_goback=(ImageView) rootView.findViewById(R.id.iv_goback);
		iv_goback.setOnClickListener(myClickListener);
		iv_more=(ImageView) rootView.findViewById(R.id.iv_more);
		iv_more.setOnClickListener(myClickListener);
		iv_goback.setEnabled(false);
		iv_goforward=(ImageView) rootView.findViewById(R.id.iv_goforward);
		iv_goforward.setOnClickListener(myClickListener);
		iv_goforward.setEnabled(false);
		webView=(WebView)rootView.findViewById(R.id.webView);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setUseWideViewPort(true); 
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBlockNetworkLoads(false);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebChromeClient(myWebChromeClient);
        webView.setWebViewClient(myWebViewClient);
        webView.setOnTouchListener(myTouchListener);
        webView.setDownloadListener(myDownLoadListener);
	}
	
	private DownloadListener myDownLoadListener=new DownloadListener(){
		@Override  
	    public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {  
			RLAlertDialog dialog=new RLAlertDialog(context,
					context.getString(R.string.download_file),url,
					context.getString(android.R.string.ok),
					context.getString(android.R.string.cancel),
					new RLAlertDialog.Listener(){
				@Override
				public void onLeftClick() {
					openWithBrowser(url);
				}
				@Override
				public void onRightClick() {
				}
			});
			dialog.show();
	    }  
	};
	
	private void openWithBrowser(String url){
		Uri uri = Uri.parse(url);  
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
        context.startActivity(intent);
	}
	
	private WebViewClient myWebViewClient=new WebViewClient(){
		@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
			isLoadFinished=false;
        	webView.getSettings().setBlockNetworkImage(true);
	        super.onPageStarted(view, url, favicon);
	        iv_refresh.setVisibility(View.GONE);
	        iv_stop.setVisibility(View.VISIBLE);
	        layout_loading.setVisibility(View.VISIBLE);
	        updateNaviButtonState();
        }
        @Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	view.loadUrl(url);
			return true;
		}
        @Override
        public void onReceivedError (WebView view, int errorCode, String description, String failingUrl){
        	super.onReceivedError(view, errorCode, description, failingUrl);
        	iv_refresh.setVisibility(View.VISIBLE);
	        iv_stop.setVisibility(View.GONE);
        	layout_loading.setVisibility(View.GONE);
        	updateNaviButtonState();
        }
        @Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
        	handler.proceed();
        }
		@Override
        public void onPageFinished(WebView view, String url) {
        	webView.getSettings().setBlockNetworkImage(false);
	        super.onPageFinished(view, url);
	        iv_refresh.setVisibility(View.VISIBLE);
	        iv_stop.setVisibility(View.GONE);
	        layout_loading.setVisibility(View.GONE);
	        updateNaviButtonState();
	        isLoadFinished=true;
	        listener.onReceivedUrl(url);
    	}
        @Override
        public void doUpdateVisitedHistory (WebView view, String url, boolean isReload) {
        	super.doUpdateVisitedHistory(view, url, isReload);
        	updateNaviButtonState();
        }
	};
	
    private WebChromeClient myWebChromeClient=new WebChromeClient(){
    	@Override
    	public void onProgressChanged(WebView view, int newProgress) {	
			super.onProgressChanged(view, newProgress);	
//			if(newProgress<100){
//				tv_loading.setText(newProgress+"%");
//			}
		}
    	@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			listener.onReceivedTitle(title);
		}
    	@Override
    	public void onReceivedIcon(WebView view, Bitmap icon){
    		super.onReceivedIcon(view, icon);
    		listener.onReceivedIcon(icon);
    	}
    };
    
    private void updateNaviButtonState(){
    	if(webView.canGoBack()){
			iv_goback.setEnabled(true);
		}else{
			iv_goback.setEnabled(false);
		}
		if(webView.canGoForward()){
			iv_goforward.setEnabled(true);
		}else{
			iv_goforward.setEnabled(false);
		}
    }
    
    private RLOnClickListener myClickListener=new RLOnClickListener(){
		@Override
		public void onClickX(View view) {
			int id = view.getId();
			if (id == R.id.iv_refresh) {
				webView.getSettings().setBlockNetworkLoads(false);
				webView.reload();
			} else if (id == R.id.iv_stop) {
				webView.getSettings().setBlockNetworkLoads(true);
				webView.getSettings().setBlockNetworkImage(true);
				webView.stopLoading();
			} else if (id == R.id.iv_goback) {
				if(webView.canGoBack()){
					webView.goBack();
				}
			}else if (id == R.id.iv_goforward) {
				if(webView.canGoForward()){
					webView.goForward();
				}
			} else if (id == R.id.iv_more) {
				showMenu();
			}
		}
    };
    
    private void showMenu(){
    	RLListDialog dialog=new RLListDialog(context,
    			context.getString(R.string.select_item),
    			getResources().getStringArray(R.array.webview_items),new RLListDialog.Listener(){
					@Override
					public void onItemClick(int position) {
						switch(position){
						case 0:
							webView.clearCache(true);
							RLUiUtil.toast(context, R.string.cache_cleared);
							break;
						case 1:
							if(isAllowClearCookie){
								if(CookieManager.getInstance().hasCookies()){
									CookieManager.getInstance().removeAllCookie();
						    		RLUiUtil.toast(context, R.string.cookie_cleared);
						    	}else{
						    		RLUiUtil.toast(context, R.string.has_no_cookie);
						    	}
							}else{
								RLUiUtil.toast(context, R.string.deny_clear_cookie);
							}
							break;
						case 2:
							if(webView.canGoBack()||webView.canGoForward()){
								webView.clearHistory();
								updateNaviButtonState();
								RLUiUtil.toast(context, R.string.history_cleared);
							}else{
								RLUiUtil.toast(context, R.string.has_no_history);
							}
							break;
						}
					}
					@Override
					public void onCancel() {
					}
    	});
    	dialog.show();
    }
    
    private OnTouchListener myTouchListener=new OnTouchListener(){
    	private boolean isZoom=false;
        private double oldDist=1d;
        private double newDist=1d;
        @Override
        public boolean onTouch(View v, MotionEvent event){
        	if(!isLoadFinished){
        		return false;
        	}
            switch(event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_POINTER_UP:
            	isZoom=false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            	oldDist=computeDist(event);
                if(oldDist>10f){
                    isZoom=true;
                }else{
                	isZoom=false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            	if(isZoom){
            		newDist=computeDist(event);
            		if(newDist>10f){
            			double scale=newDist/oldDist;
            			if(scale<1f){
            				webView.zoomOut();
            			}
            			if(scale>1f){
            				webView.zoomIn();
            			}
            		}
                }
            	break;
            }
            return false;
        }
        
        private double computeDist(MotionEvent event){
        	float x=event.getX(0)-event.getX(1);
            float y=event.getY(0)-event.getY(1);
            return Math.sqrt(x*x+y*y);
        }
    };
}