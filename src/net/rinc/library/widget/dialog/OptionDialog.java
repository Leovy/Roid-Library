package net.rinc.library.widget.dialog;

import net.rinc.library.R;
import net.rinc.library.widget.OnClickListenerX;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OptionDialog extends RCDialog{
	private Context context;
	private String title;
	private int[] icons;
	private String[] items;
	private Listener listener;
	
	/**
	 * 
	 * @param context
	 * @param title
	 * @param items
	 * @param listener
	 */
	public OptionDialog(Context context,String title, String[] items, Listener listener) {
		super(context);
		this.title=title;
		this.items=items;
		this.listener=listener;
		this.context=context;
		super.createView();
		super.setCanceledOnTouchOutside(false);
		super.setWindowAnimations(R.style.ANIMATIONS_SLIDE_FROM_BOTTOM);
		super.setWindowGravity(Gravity.BOTTOM);
	}
	
	/**
	 * 
	 * @param context
	 * @param theme
	 * @param title
	 * @param icons
	 * @param items
	 * @param listener
	 */
	public OptionDialog(Context context, String title, int[] icons, String[] items, Listener listener) {
		super(context);
		this.title=title;
		this.icons=icons;
		this.items=items;
		this.listener=listener;
		this.context=context;
		super.createView();
		super.setCanceledOnTouchOutside(false);
		super.setWindowAnimations(R.style.ANIMATIONS_SLIDE_FROM_BOTTOM);
		super.setWindowGravity(Gravity.BOTTOM);
	}
	
	@Override
	protected View getView() {
		View view=LayoutInflater.from(context).inflate(R.layout.dialog_option, null);
		Button bt_cancel=(Button) view.findViewById(R.id.bt_cancel);
		TextView tv_title=(TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		ListView lv=(ListView) view.findViewById(R.id.lv_menu);
		lv.setAdapter(new LVAdapter());
		lv.getLayoutParams().width=LayoutParams.MATCH_PARENT;
		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				OptionDialog.this.dismiss();
				listener.onItemClick(arg2);
			}			
		});
		bt_cancel.setOnClickListener(new OnClickListenerX(){
			public void onClickX(View view) {
				OptionDialog.this.dismiss();
				listener.onCancel();
			}
    	});
		return view;
	}
	
	/**
	 * 
	 */
	public interface Listener{
		public void onItemClick(int position);
		public void onCancel();
	}
	
	private class LVAdapter extends BaseAdapter{
		@Override
		public int getCount() {		
			return items.length;
		}
		@Override
		public Object getItem(int position) {		
			return null;
		}
		@Override
		public long getItemId(int position) {		
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(icons!=null){
				convertView=LayoutInflater.from(context).inflate(R.layout.dialog_option_item1,null);
			}else{
				convertView=LayoutInflater.from(context).inflate(R.layout.dialog_option_item2,null);
			}
			if(icons!=null){
				ImageView iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
				iv_icon.setImageResource(icons[position]);
			}
			TextView tv_item=(TextView)convertView.findViewById(R.id.tv_item);
			tv_item.setText(items[position]);
			return convertView;
		}		
	}
}