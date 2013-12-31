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
package com.rincliu.library.app;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import com.rincliu.library.BuildConfig;
import com.rincliu.library.common.persistence.http.AsyncHttpClient;
import com.rincliu.library.common.persistence.image.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.rincliu.library.common.persistence.image.cache.disc.naming.Md5FileNameGenerator;
import com.rincliu.library.common.persistence.image.cache.memory.impl.LruMemoryCache;
import com.rincliu.library.common.persistence.image.core.DisplayImageOptions;
import com.rincliu.library.common.persistence.image.core.ImageLoader;
import com.rincliu.library.common.persistence.image.core.ImageLoaderConfiguration;
import com.rincliu.library.common.persistence.image.core.assist.QueueProcessingType;
import com.rincliu.library.common.persistence.image.core.decode.BaseImageDecoder;
import com.rincliu.library.common.persistence.image.core.display.FadeInBitmapDisplayer;
import com.rincliu.library.common.persistence.image.core.download.BaseImageDownloader;
import com.rincliu.library.common.persistence.image.utils.StorageUtils;
import com.rincliu.library.common.reference.push.RLPushHelper;
import com.rincliu.library.entity.RLDisplayInfo;
import com.rincliu.library.util.RLSysUtil;

public class RLApplication extends Application {
    private RLDisplayInfo displayInfo;

    public ImageLoader imgLoader;

    public DisplayImageOptions defaultDisplayImageOptions;

    public AsyncHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        String enablePush = RLSysUtil.getApplicationMetaData(this, "ENABLE_PUSH");
        if (enablePush != null && enablePush.equals("true")) {
            RLPushHelper.getInstance(this).init(BuildConfig.DEBUG);
        }
        initImageCache();
        initHttpClient();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public RLDisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public void setDisplayInfo(RLDisplayInfo displayInfo) {
        this.displayInfo = displayInfo;
    }

    private void initImageCache() {// TODO
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
        builder.threadPoolSize(10).threadPriority(Thread.NORM_PRIORITY - 2).tasksProcessingOrder(
                QueueProcessingType.LIFO).imageDownloader(new BaseImageDownloader(this)).imageDecoder(
                new BaseImageDecoder(BuildConfig.DEBUG)).defaultDisplayImageOptions(DisplayImageOptions.createSimple()).denyCacheImageMultipleSizesInMemory();
        long availableMemory = Runtime.getRuntime().maxMemory();
        if (availableMemory > 0) {
            builder.memoryCache(new LruMemoryCache((int) (availableMemory / 10))).memoryCacheSize(
                    (int) (availableMemory / 10)).memoryCacheSizePercentage(10);
        }
        long availableStorage = RLSysUtil.getAvailableExternalStorageSize();
        if (availableStorage > 0) {
            builder.discCache(
                    new TotalSizeLimitedDiscCache(StorageUtils.getCacheDirectory(this), (int) (availableStorage / 10))).discCacheFileCount(
                    1024 * 8).discCacheSize((int) (availableStorage / 10)).discCacheFileNameGenerator(
                    new Md5FileNameGenerator());
        }
        if (BuildConfig.DEBUG) {
            builder.writeDebugLogs();
        }
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(builder.build());
        defaultDisplayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(
                Runtime.getRuntime().freeMemory() > 0).cacheOnDisc(RLSysUtil.getAvailableExternalStorageSize() > 0).resetViewBeforeLoading(
                true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(300)).build();
    }

    private void initHttpClient() {// TODO
        httpClient = new AsyncHttpClient();
        httpClient.addHeader("Connection", "Close");
        httpClient.addHeader("Accept", "*/*");
        httpClient.setTimeout(1000 * 60);
    }
}
