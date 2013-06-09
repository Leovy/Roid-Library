package com.rincliu.library.common.reference.push;

import org.json.JSONException;
import org.json.JSONObject;

import com.rincliu.library.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RLNotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RLNotificationEntity ne=(RLNotificationEntity) getIntent().getSerializableExtra("data");
        setTitle(ne.getTitle());
        setContentView(R.layout.push);
        TextView tv_content=(TextView)findViewById(R.id.tv_content);
        tv_content.setText(ne.getContent());
        String x=ne.getExtra();
        if(x!=null&&!x.equals("")){
        	JSONObject object=null;
        	try {
				object=new JSONObject(x);
			} catch (JSONException e) {
				e.printStackTrace();
			}
        	if(object!=null&&object.has("data")){
        		String data=null;
        		try {
					data=object.get("data").toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
        		if(data!=null){
        			System.out.println(x);
        		}
        	}
        }
    }
}