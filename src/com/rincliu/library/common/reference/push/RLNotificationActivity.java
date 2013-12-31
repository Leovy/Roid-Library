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
        RLNotificationEntity ne = (RLNotificationEntity) getIntent().getSerializableExtra("data");
        setTitle(ne.getTitle());
        setContentView(R.layout.push);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setText(ne.getContent());
        String x = ne.getExtra();
        if (x != null && !x.equals("")) {
            JSONObject object = null;
            try {
                object = new JSONObject(x);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (object != null && object.has("data")) {
                String data = null;
                try {
                    data = object.get("data").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (data != null) {
                    System.out.println(x);
                }
            }
        }
    }
}
