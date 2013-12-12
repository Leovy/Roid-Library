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
package com.rincliu.library.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class RLNetUtil
{
    private static String LOG_TAG = "NetWorkHelper";

    /**
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
        {
            Log.w(LOG_TAG, "couldn't get connectivity manager");
        }
        else
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
            {
                for (int i = 0; i < info.length; i++)
                {
                    if (info[i].isAvailable())
                    {
                        Log.d(LOG_TAG, "network is available");
                        return true;
                    }
                }
            }
        }
        Log.d(LOG_TAG, "network is not available");
        return false;
    }

    /**
     * @param context
     * @return
     */
    public static boolean isNetworkRoaming(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
        {
            Log.w(LOG_TAG, "couldn't get connectivity manager");
        }
        else
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null && tm.isNetworkRoaming())
                {
                    Log.d(LOG_TAG, "network is roaming");
                    return true;
                }
                else
                {
                    Log.d(LOG_TAG, "network is not roaming");
                }
            }
            else
            {
                Log.d(LOG_TAG, "not using mobile network");
            }
        }
        return false;
    }

    /**
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isMobileDataEnable(Context context) throws Exception
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;
        isMobileDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return isMobileDataEnable;
    }

    /**
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isWifiDataEnable(Context context) throws Exception
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }
    
    /**
     * Returns MAC address of the given interface name.
     * 
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName)
    {
        try
        {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces)
            {
                if (interfaceName != null)
                {
                    if (!intf.getName().equalsIgnoreCase(interfaceName))
                        continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null)
                    return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0)
                    buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        }
        catch (Exception ex)
        {
        } // for now eat exceptions
        return "";
//        try
//        { // this is so Linux hack return
//            loadFileAsString("/sys/class/net/" + interfaceName + "/address").toUpperCase().trim();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            return null;
//        }
    }
    
    /**
     * Load UTF8withBOM or any ansi text file.
     * 
     * @param filename
     * @return
     * @throws java.io.IOException
     */
//    private static String loadFileAsString(String filename) throws java.io.IOException
//    {
//        final int BUFLEN = 1024;
//        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
//        try
//        {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
//            byte[] bytes = new byte[BUFLEN];
//            boolean isUTF8 = false;
//            int read, count = 0;
//            while ((read = is.read(bytes)) != -1)
//            {
//                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF)
//                {
//                    isUTF8 = true;
//                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
//                }
//                else
//                {
//                    baos.write(bytes, 0, read);
//                }
//                count += read;
//            }
//            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
//        }
//        finally
//        {
//            try
//            {
//                is.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * Get IP address from first non-localhost interface
     * 
     * @param ipv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4)
    {
        try
        {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces)
            {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs)
                {
                    if (!addr.isLoopbackAddress())
                    {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4)
                        {
                            if (isIPv4)
                                return sAddr;
                        }
                        else
                        {
                            if (!isIPv4)
                            {
                                int delim = sAddr.indexOf('%'); // drop ip6
                                                                // port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
