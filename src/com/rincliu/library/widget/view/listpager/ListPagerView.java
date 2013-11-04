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

import java.util.ArrayList;

import com.rincliu.library.R;
import com.rincliu.library.widget.view.pulltorefresh.PullToRefreshBase;
import com.rincliu.library.widget.view.pulltorefresh.PullToRefreshListView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ListPagerView extends PullToRefreshListView{
	private DataObserver dataObserver;
	private View custom_header_view;
	private View custom_footer_view;
	private View custom_empty_view;
	private ListView list_view;
	private Drawable dividerDrawable=null;
	private boolean isShowStartTime=true;
	private boolean isShowEndTime=true;
	private boolean isShowFastScrollBar=false;
	private int dividerHeight=0;
	
	Context context;
	ItemHandler itemHandler;
	boolean isReverse=false;
	
	/**
	 *
	 * @param context
	 */
	protected ListPagerView(Context context) {
		super(context);
		this.context=context;
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	protected ListPagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListPagerView);
		if (typedArray.hasValue(R.styleable.ListPagerView_isReverse)) {
			this.isReverse=typedArray.getBoolean(R.styleable.ListPagerView_isReverse, false);
		}
		if (typedArray.hasValue(R.styleable.ListPagerView_isShowStartTime)) {
			this.isShowStartTime=typedArray.getBoolean(R.styleable.ListPagerView_isShowStartTime, true);
		}
		if (typedArray.hasValue(R.styleable.ListPagerView_isShowEndTime)) {
			this.isShowEndTime=typedArray.getBoolean(R.styleable.ListPagerView_isShowEndTime, true);
		}
		if (typedArray.hasValue(R.styleable.ListPagerView_isShowFastScrollBar)) {
			this.isShowFastScrollBar=typedArray.getBoolean(R.styleable.ListPagerView_isShowFastScrollBar, true);
		}
		if (typedArray.hasValue(R.styleable.ListPagerView_dividerHeight)) {
			this.dividerHeight=typedArray.getInteger(R.styleable.ListPagerView_dividerHeight, 0);
		}
		if (typedArray.hasValue(R.styleable.ListPagerView_dividerDrawable)) {
			this.dividerDrawable=typedArray.getDrawable(R.styleable.ListPagerView_dividerDrawable);
		}
		typedArray.recycle();
	}
	
	/**
	 * 
	 * @param is
	 */
	public void setIsReverse(boolean is){
		isReverse=is;
	}
	
	/**
	 * 
	 * @param customHeaderView
	 * @param customFooterView
	 * @param customEmptyView
	 */
	public void setCustomViews(View customHeaderView, View customFooterView, View customEmptyView){
		this.custom_header_view=customHeaderView;
		this.custom_footer_view=customFooterView;
		this.custom_empty_view=customEmptyView;
	}
	
	/**
	 * 
	 * @param position
	 */
	public void setSelection(int position){
		list_view.setSelection(position);
	}
	
	/**
	 * 
	 * @param itemHandler
	 */
	public void handleItem(ItemHandler itemHandler){
		this.itemHandler=itemHandler;
	}
	
	/**
	 * 
	 * @param dataObserver
	 */
	public void setDataAdapter(DataObserver dataObserver){
		this.dataObserver=dataObserver;
	}
	
	/**
	 * 
	 * @param isShowStartTime
	 */
	public void setIsShowStartTime(boolean isShowStartTime){
		this.isShowStartTime=isShowStartTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getIsShowStartTime(){
		return isShowStartTime;
	}
	
	/**
	 * 
	 * @param isShowEndTime
	 */
	public void setIsShowEndTime(boolean isShowEndTime){
		this.isShowEndTime=isShowEndTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getIsShowEndTime(){
		return isShowEndTime;
	}
	
	/**
	 * 
	 * @param isShowFastScrollBar
	 */
	public void setIsShowFastScrollBar(boolean isShowFastScrollBar){
		this.isShowFastScrollBar=isShowFastScrollBar;
	}
	
	/**
	 * 
	 * @param dividerHeight
	 */
	public void setDividerHeight(int dividerHeight){
		this.dividerHeight=dividerHeight;
	}
	
	/**
	 * 
	 * @param dividerDrawable
	 */
	public void setDividerDrawable(Drawable dividerDrawable){
		this.dividerDrawable=dividerDrawable;
	}
	
	/**
	 * Get the data set.
	 * @return
	 */
	public ArrayList<Object> getDataSet(){
		if(dataObserver==null){
			return null;
		}
		return dataObserver.getDataSet();
	}
	
	/**
	 * Modify the data set without notifying the adapter.
	 * @param list
	 */
	public void setDataSet(ArrayList<Object> list){
		if(dataObserver==null){
			return;
		}
		dataObserver.setDataSet(list);
	}
	
	/**
	 * 
	 */
	public void notifyAdapter(){
		if(dataObserver==null){
			return;
		}
		dataObserver.notifyAdapter();
	}
	
	private boolean hasInit=false;
	
	/**
	 * 
	 */
	public void show(){
		if(!hasInit){
			list_view=super.getRefreshableView();
			if(dividerDrawable!=null){
				list_view.setDivider(dividerDrawable);
			}	
			list_view.setDividerHeight(dividerHeight);
			if(isShowFastScrollBar){
				list_view.setFastScrollEnabled(true);
			}
			list_view.setOnItemClickListener(onItemClickListener);
			list_view.setOnItemLongClickListener(onItemLongClickListener);
			if(custom_header_view!=null){
				list_view.addHeaderView(custom_header_view);
				custom_header_view.setVisibility(View.GONE);
			}
			super.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
					if(dataObserver!=null){
						if(isReverse){
							onStartLoadMore();
							dataObserver.loadMore();
						}else{
							onStartRefresh();
							dataObserver.refresh();
						}
					}
				}
				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
					if(dataObserver!=null){
						if(isReverse){
							onStartRefresh();
							dataObserver.refresh();
						}else{
							onStartLoadMore();
							dataObserver.loadMore();
						}
					}
				}
			});
			super.setOnLastItemVisibleListener(new OnLastItemVisibleListener(){
				@Override
				public void onLastItemVisible() {
					if(itemHandler!=null){
						if(dataObserver!=null){
							if(dataObserver.hasMore()){
								itemHandler.onPageLastItemVisible();
							}else{
								itemHandler.onGlobalLastItemVisible();
							}
						}else{
							itemHandler.onPageLastItemVisible();
						}
					}
				}
			});
			hasInit=true;
		}
		if(dataObserver!=null){
			super.setAdapter(dataObserver.getBaseAdapter());
			onStartRefresh();
			super.setShowViewWhileRefreshing(true);
			super.setRefreshing(true);
			dataObserver.refresh();
		}
	}
	
	/**
	 * 
	 */
	public void clear(){
		if(dataObserver!=null&&getDataSet()!=null){
			getDataSet().clear();
			dataObserver.getBaseAdapter().notifyDataSetChanged();
		}
	}
	
	/**
	 * 
	 */
	public void onStartRefresh(){
		//TODO
	}
	
	/**
	 * 
	 */
	public void onStopRefresh(){
		if(isReverse&&getDataSet()!=null&&getDataSet().size()>0){
			getRefreshableView().setSelection(getDataSet().size()-1);
		}
		//TODO
	}
	
	/**
	 * 
	 */
	public void onStartLoadMore(){
		//TODO
	}
	
	/**
	 * 
	 */
	public void onStopLoadMore(){
		//TODO
	}
	
	void notifyFinish(boolean isRefresh){
		super.onRefreshComplete();
		if(isRefresh){
			onStopRefresh();
		}else{
			onStopLoadMore();
		}
		if(custom_header_view!=null){
			custom_header_view.setVisibility(View.VISIBLE);
		}
		if(custom_empty_view!=null){
			setEmptyView(custom_empty_view);
		}else{
			setEmptyView(LayoutInflater.from(context).inflate(R.layout.listpager_empty, null));
		}
	}
	
	void addFooter(){
		if(custom_footer_view!=null){
			list_view.addFooterView(custom_footer_view);
		}
	}
	
	void removeFooter(){
		if(custom_footer_view!=null){
			list_view.removeFooterView(custom_footer_view);
		}
	}
	
	private OnItemClickListener onItemClickListener=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			if(dataObserver==null||itemHandler==null){
				return;
			}
			int headers=list_view.getHeaderViewsCount();
			if(position>=headers&&position<dataObserver.getDataSet().size()+headers){
				itemHandler.onItemClick(parent, view, position-headers, id);
			}
		}
	};
	
	private OnItemLongClickListener onItemLongClickListener=new OnItemLongClickListener(){
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if(dataObserver==null||itemHandler==null){
				return false;
			}
			int headers=list_view.getHeaderViewsCount();
			if(position>=headers&&position<dataObserver.getDataSet().size()+headers){
				return itemHandler.onItemLongClick(parent, view, position-headers, id);
			}else{
				return false;
			}
		}
	};
	
	/**
	 * 
	 */
	public interface ItemHandler {
		public View onGetItemView(int position, View convertView, ViewGroup parent);
		public void onItemClick(AdapterView<?> parent, View view, int position, long id);
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id);
		public void onPageLastItemVisible();
		public void onGlobalLastItemVisible();
	}
	
	/**
	 * 
	 */
	public interface DataObserver{
		public void notifyAdapter();
		public BaseAdapter getBaseAdapter();
		public ArrayList<Object> getDataSet();
		public void setDataSet(ArrayList<Object> list);
		public void refresh();
		public void loadMore();
		public boolean hasMore();
	}
}