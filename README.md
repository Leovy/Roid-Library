Common Modules
==========
* Async http request: [AsyncHttpClient](https://github.com/loopj/android-async-http)；
* Image loading/display/cache: [UniversalImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader);
* Other: FinalActivity(Supports view annotation) and FinalDB(A SQLite helper);

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
* QR bar scanning: zxing；
* Global CrashHandler；
* API compatibility Util: RLAPICompat；
* RLCameraActivity: A custom camera.
* View switcher: ViewFlow；
* Sliding menu: MenuDrawer；
* GestureImageView: Support to scale ImageView with gesture；
* Custom dialogs：RLDialog、RLAlertDialog、RLListDialog、RLLoadingDialog;
* Other custom widgets: RLFileExplorer、RLSpinner、RLWebView、RLOnClickListener；
* Utils：RLAppUtil、RLFileUtil、RLImgUtil、RLIntentUtil、RLJsonUtil、RLNetUtil、RLStrUtil、RLSysUtil、RLUiUtil；

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
