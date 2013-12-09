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
package com.rincliu.library.common.reference.lbs;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.rincliu.library.R;
import com.rincliu.library.entity.RLLocationInfo;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RLLocationHelper
{

    private static RLLocationHelper helper;

    private static RLLocationInfo location;

    private static LocationClient client;

    private static LocationClientOption option;

    private boolean isRun = false;

    private static Context mContext;

    /**
     * @param context
     * @return
     */
    public static RLLocationHelper getInstance(Context context)
    {
        mContext = context;
        if (helper == null)
        {
            helper = new RLLocationHelper(context);
        }
        return helper;
    }

    private RLLocationHelper(Context context)
    {
        if (client == null)
        {
            client = new LocationClient(context.getApplicationContext());
            if (option == null)
            {
                option = new LocationClientOption();
                option.setOpenGps(true);
                option.setCoorType("bd09ll");// TODO
                option.setServiceName("com.baidu.location.service_v2.9");
                option.setPoiExtraInfo(false);
                option.setTimeOut(10 * 1000);
                option.setAddrType("all");
                option.setScanSpan(LocationClientOption.MIN_SCAN_SPAN);
                option.setPriority(LocationClientOption.NetWorkFirst);
                option.disableCache(true);
            }
            client.setLocOption(option);
        }
    }

    /**
     * @param isOpenGps
     * @param isNetworkFirst
     * @param isCache
     * @param timeOut
     * @return
     */
    public RLLocationHelper setOptions(boolean isOpenGps, boolean isNetworkFirst, boolean isCache, int timeOut)
    {
        option.setOpenGps(isOpenGps);
        option.setPriority((isNetworkFirst || !isOpenGps) ? LocationClientOption.NetWorkFirst
                : LocationClientOption.GpsFirst);
        option.disableCache(!isCache);
        option.setTimeOut(timeOut);
        client.setLocOption(option);
        return helper;
    }

    /**
	 * 
	 */
    public interface Listener
    {
        public void onReceive(RLLocationInfo locInfo);

        public void onFailed(String error);
    }

    /**
     * @param listener
     */
    public void locate(final Listener listener)
    {
        if (!isRun)
        {
            new AsyncTask<Object, Object, RLLocationInfo>()
            {
                @Override
                protected void onPreExecute()
                {
                    location = null;
                    client.registerLocationListener(bdlocListener);
                    if (!client.isStarted())
                    {
                        client.start();
                    }
                    isSuccess = false;
                    isRun = true;
                }

                @Override
                protected RLLocationInfo doInBackground(Object... arg0)
                {
                    do
                    {
                        client.requestLocation();
                    }
                    while (location == null);
                    return location;
                }

                @Override
                protected void onPostExecute(RLLocationInfo result)
                {
                    client.unRegisterLocationListener(bdlocListener);
                    client.stop();
                    isRun = false;
                    if (isSuccess)
                    {
                        listener.onReceive(result);
                    }
                    else
                    {
                        listener.onFailed(error);
                    }
                }

                private boolean isSuccess;

                private String error;

                private BDLocationListener bdlocListener = new BDLocationListener()
                {
                    @Override
                    public void onReceiveLocation(BDLocation bdloc)
                    {
                        location = new RLLocationInfo();
                        BDLocation loc = null;
                        if (bdloc == null || bdloc.getAddrStr() == null)
                        {
                            loc = client.getLastKnownLocation();
                            if (loc == null)
                            {
                                error = mContext.getString(R.string.bdloc_error_first);
                                return;
                            }
                        }
                        else
                        {
                            loc = bdloc;
                        }
                        int code = loc.getLocType();
                        switch (code)
                        {
                            case 61:
                                isSuccess = true;
                                Log.d(">>>>>>>>>>", "Located with GPS...");
                                break;
                            case 62:
                                error = mContext.getString(R.string.bdloc_error_62);
                                return;
                            case 63:
                                error = mContext.getString(R.string.bdloc_error_63);
                                return;
                            case 65:
                                isSuccess = true;
                                Log.d(">>>>>>>>>>", "Read location from cache...");
                                break;
                            case 66:
                                isSuccess = true;
                                Log.d(">>>>>>>>>>", "Located with offline...");
                                break;
                            case 67:
                                error = mContext.getString(R.string.bdloc_error_67);
                                return;
                            case 68:
                                isSuccess = true;
                                Log.d(">>>>>>>>>>", "Located with offline as network error...");
                                break;
                            case 161:
                                isSuccess = true;
                                Log.d(">>>>>>>>>>", "Located with network...");
                                break;
                            case 162:
                            case 163:
                            case 164:
                            case 165:
                            case 166:
                            case 167:
                                error = mContext.getString(R.string.bdloc_error_162);
                                return;
                        }
                        location.setAltitude(loc.getAltitude());
                        location.setLatitude(loc.getLatitude());
                        location.setLongitude(loc.getLongitude());
                        location.setRadius(loc.getRadius());
                        location.setSpeed(loc.getSpeed());
                        location.setProvince(loc.getProvince());
                        location.setDistrict(loc.getDistrict());
                        location.setCity(loc.getCity());
                        location.setStreet(loc.getStreet());
                        location.setAddress(loc.getAddrStr());
                    }

                    @Override
                    public void onReceivePoi(BDLocation bdLoc)
                    {
                        // TODO Auto-generated method stub
                    }
                };
            }.execute();
        }
    }
}
