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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class RLBluetoothHelper {
    public interface OnDiscoveryListener {
        public void onDiscoveryStarted();

        public void onDeviceFound(BluetoothDevice device);

        public void onDiscoveryFinished();
    }

    /**
     * [description]
     * 
     * @param context
     * @param onDiscoveryListener
     * @return
     * @see []
     */
    public BroadcastReceiver discovery(Context context, final OnDiscoveryListener onDiscoveryListener) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && btAdapter.isEnabled() && !btAdapter.isDiscovering()) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            BroadcastReceiver mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (onDiscoveryListener == null) {
                        return;
                    }
                    String action = intent.getAction();
                    if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                        onDiscoveryListener.onDiscoveryStarted();
                    } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        onDiscoveryListener.onDeviceFound(device);
                    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        onDiscoveryListener.onDiscoveryFinished();
                    }
                }
            };

            context.registerReceiver(mReceiver, filter);
            btAdapter.startDiscovery();
            return mReceiver;
        }
        return null;
    }
}
