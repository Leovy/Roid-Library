package net.rinc.library.common.reference.push;

import org.json.JSONException;
import org.json.JSONObject;

import net.rinc.library.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationEntity ne=(NotificationEntity) getIntent().getSerializableExtra("data");
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