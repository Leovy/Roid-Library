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
package com.rincliu.library.widget.view.listpager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import com.rincliu.library.R;
import com.rincliu.library.app.RLAsyncTask;
import com.rincliu.library.util.RLFileUtil;
import com.rincliu.library.util.RLUiUtil;
import com.rincliu.library.widget.view.pulltorefresh.ILoadingLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListPagerAdapter implements ListPagerView.DataObserver{
	private ListPagerView pager;
	private final Object obj=new Object();
	private ArrayList<Object> list=new ArrayList<Object>();
	private ArrayList<?> tmpList;
	private boolean hasFetchedData=false;
	private boolean isReset=false;
	private boolean hasMore=false;
	private String cacheFile=null;
	private String data=null;
	private long cacheKeepTime=0;
	private int start=0;
	private int tmpStart=0;
	private int pageLimit=-1;
	private int currentPage=0;
	private UpdateDataTask task;
	
	/**
	 * 
	 * @return
	 */
	protected int getNextStart(){
		return start;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasMore(){
		return hasMore;
	}
	
	@Override
	public BaseAdapter getBaseAdapter() {
		return adapter;
	}
	
	@Override
	public ArrayList<Object> getDataSet(){
		return list;
	}
	
	@Override
	public void setDataSet(ArrayList<Object> list){
		this.list=list;
	}
	
	@Override
	public void notifyAdapter(){
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void refresh(){
		if(task==null||task.getStatus()==RLAsyncTask.Status.FINISHED){
			pager.setRefreshing(true);
			pager.setEmptyView(null);
			isReset=true;
			start=0;
			tmpStart=0;
			currentPage=0;
			task=new UpdateDataTask();
			executeTask();
		}else{
			pager.notifyFinish(true);
		}
	}
	
	@Override
	public void loadMore(){
		if(hasMore){
			if(task==null||task.getStatus()==RLAsyncTask.Status.FINISHED){
				pager.setRefreshing(true);
				pager.setEmptyView(null);
				isReset=false;
				task=new UpdateDataTask();
				executeTask();
			}else{
				pager.notifyFinish(false);
			}
		}else{
			pager.notifyFinish(false);
			RLUiUtil.toast(pager.context, "No more data.");
		}
	}
	
	/**
	 * 
	 * @param pager
	 * @param cacheFile
	 * @param cacheKeepTime
	 */
	public ListPagerAdapter(ListPagerView pager,int pageLimit,String cacheFile, long cacheKeepTime){
		this.pager=pager;
		this.cacheFile=cacheFile;
		this.cacheKeepTime=cacheKeepTime;
		this.pageLimit=pageLimit;
	}
	
	/**
	 * 
	 * @param data
	 */
	public void notifyFetchDataSuccess(String data){
		this.data=data;
		if(data!=null){
			if(cacheFile!=null&&start==0){
				RLFileUtil.getInstance().write2Sd(cacheFile, data);
			}
			currentPage++;
			tmpList=onReadDataSet(data);
		}
		hasFetchedData=true;
		synchronized (obj) {
			obj.notify();
		}
	}
	
	/**
	 * 
	 */
	public void notifyFetchDataFailed(){
		hasFetchedData=true;
		synchronized (obj) {
			obj.notify();
		}
	}
	
	private void fetchData() {
		if(tmpList!=null){
			if(!tmpList.isEmpty()){
				tmpList.clear();
			}
			tmpList=null;
		}
		boolean isLoadFirstPage=start==0;
		boolean isNeedCache=cacheFile!=null&&new File(cacheFile).exists();
		ConnectivityManager connManager=(ConnectivityManager)pager.context.
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo=connManager.getActiveNetworkInfo();
		boolean isNetworkNotAvailable=netInfo==null||!netInfo.isAvailable()||!netInfo.isConnected();
		boolean isCacheNeverOverTime=cacheKeepTime==-1;
		boolean isCacheNotOverTime=false;
		if(isNeedCache){
			isCacheNotOverTime=(cacheKeepTime!=-1&&
				System.currentTimeMillis()-new File(cacheFile).lastModified()<cacheKeepTime);
		}
		if(isLoadFirstPage&&isNeedCache&&(
					isNetworkNotAvailable||isCacheNeverOverTime||isCacheNotOverTime
				)&&readCache()
			){
		}else{
			onFetchDataHttp(currentPage,start);
		}
		if(!hasFetchedData){
			synchronized (obj) {
				try {
					obj.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		hasFetchedData=false;
	}
	
	private boolean readCache(){
		boolean readSuccess=false;
		data=RLFileUtil.getInstance().readFromSd(cacheFile);
		if(onCheckValid(data)){
			tmpList=onReadDataSet(data);
			readSuccess=true;
			hasFetchedData=true;
		}
		return readSuccess;
	}
	
	private void dealWithData(){
		if(onCheckValid(data)&&tmpList!=null){ 
   			if(isReset){
   				tmpStart=0;
   				if(list!=null){
   					list.clear();
   				}else{
   					list=new ArrayList<Object>();
   				}
   				pager.removeFooter();
   			}
   			start=onReadNextStart(data);
   			if(pager.isReverse){
   				Collections.reverse(tmpList);
   				if(list==null){
   					list=new ArrayList<Object>();
   				}
   				list.addAll(0, tmpList);
   			}else{
   				if(list==null){
   					list=new ArrayList<Object>();
   				}
   				list.addAll(tmpList);
   			}
   			adapter.notifyDataSetChanged();
   			if(pageLimit==-1||!(hasMore=onCheckHasMore(data))){
   				pager.addFooter();
   			}
   		}else{
   			if(isReset){
   				start=tmpStart;
				tmpStart=0;
   			}
   			if(onCheckValid(data)){
   				String errorStr=onReadErrorMessage(data);
   				if(errorStr!=null){
   					RLUiUtil.toast(pager.context, errorStr);
   				}
   			}
   		}
		adapter.notifyDataSetChanged();
		ILoadingLayout layout=null;
		if(isReset){
			if(pager.getIsShowStartTime()){
				if(pager.getIsShowEndTime()){
					layout=pager.getLoadingLayoutProxy(true, true);
				}else{
					layout=pager.getLoadingLayoutProxy(true, false);
				}
			}
		}else{
			if(pager.getIsShowEndTime()){
				layout=pager.getLoadingLayoutProxy(false, true);
			}
		}
		if(layout!=null){
			layout.setLastUpdatedLabel(pager.context.getString(R.string.ptr_updated_at)
					+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA)
					.format(new Date(System.currentTimeMillis())));
		}
		pager.notifyFinish(isReset);
		isReset=false;
	}
	
	private void executeTask(){
		task.executeOnExecutor(RLAsyncTask.DUAL_THREAD_EXECUTOR);
	}
	
	private class UpdateDataTask extends RLAsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
	   		fetchData();
			return null;
		}
		@Override
		protected void onCancelled(){
			pager.notifyFinish(isReset);
		}
		@Override
		protected void onCancelled(Void result){
			pager.notifyFinish(isReset);
		}
		@Override
		protected void onProgressUpdate(Void ...values){
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(Void result) {
			dealWithData();
		}
	}
	
	private BaseAdapter adapter=new BaseAdapter(){
		@Override
		public int getCount() {		
			return list==null?0:list.size();
		}
		@Override
		public Object getItem(int position) {		
			return list==null?null:list.get(position);
		}
		@Override
		public long getItemId(int position) {		
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return pager.itemHandler.onGetItemView(position, convertView, parent);
		}
	};

	/**
	 * 
	 * @param currentPage
	 * @param currentStart
	 */
	public abstract void onFetchDataHttp(int currentPage,int currentStart);
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public abstract boolean onCheckValid(String data);
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public abstract String onReadErrorMessage(String data);
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public abstract boolean onCheckHasMore(String data);
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public abstract int onReadNextStart(String data);
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public abstract ArrayList<?> onReadDataSet(String data);
}