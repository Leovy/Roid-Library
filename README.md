Common Modules
==========
* Async http request: [AsyncHttpClient](https://github.com/loopj/android-async-http)；
* Image loading/display/cache: [UniversalImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader);
* Other: <code>FinalActivity</code>(Supports view annotation) and <code>FinalDB</code>(A SQLite helper);

3rd Party Modules
==========
* Ads module based on [DoMob(多盟)](http://www.duomeng.net/developers/developers.htm)；
* Analytics/Feedback/Upgrade module based on [Umeng(友盟)](http://www.umeng.com)；
* Push module based on [JPush(极光推送)](http://www.jpush.cn/)；
* Location module based on [Baidu Map(百度地图)](http://developer.baidu.com/map/)；
* Video player module based on [Baidu Media Cloud(百度媒体云)](http://developer.baidu.com/wiki/index.php?title=docs/cplat/media)；
* Voice engine based on [IFlyTek(科大讯飞)](http://open.voicecloud.cn/developer.php)；
* SNS sharing：Sina Weibo(SSO)、Tencent Weixin；

Widgets and Utils
==========
* ROOT Utils: [RootManager](https://github.com/Chrisplus/RootManager)；
* QR bar scanning: <code>zxing</code>；
* Global CrashHandler；
* API compatibility Util: <code>RLAPICompat</code>；
* <code>RLCameraActivity</code>: A custom camera.(Supports compress.)
* View switcher: <code>ViewFlow</code>；
* Sliding menu: <code>MenuDrawer</code>；
* <code>GestureImageView</code>: Support to scale ImageView with gesture；
* Custom dialogs：<code>RLDialog</code>、<code>RLAlertDialog</code>、<code>RLListDialog</code>、<code>RLLoadingDialog</code>;
* Other custom widgets: <code>RLFileExplorer</code>、<code>RLSpinner</code>、<code>RLWebView</code>、<code>RLOnClickListener</code>；
* Utils：<code>RLBluetoothHelper</code>、<code>RLAppUtil</code>、<code>RLFileUtil</code>、<code>RLImgUtil</code>、<code>RLIntentUtil</code>、<code>RLJsonUtil</code>、<code>RLNetUtil</code>、<code>RLStrUtil</code>、<code>RLSysUtil</code>、<code>RLUiUtil</code>；

Usage
==========
If you wanna prevent the unused modules from launching in background, just edit the AndroidManifest.xml file:

```xml
...
<application android:name="com.rincliu.library.app.RLApplication" 
        android:label="@string/app_name">
        <meta-data  android:name="ENABLE_CRASH_HANDLER" android:value="true"/>
        <meta-data  android:name="ENABLE_PUSH" android:value="false"/>
        <meta-data  android:name="ENABLE_FEEDBACK" android:value="false"/>
        <meta-data  android:name="ENABLE_ANALYTICS" android:value="false"/>
        ...
</application>
...
```
