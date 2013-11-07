### 简介
Android集成开发框架， 包含HttpClient、ImageLoader、SQLite、RootManager、ZXing、LBS、Voice、Video、Ads、Analytics、Push、SNS等常用模块以及一些常用组件和工具类。

### 常用功能模块
* Android持久化框架[Afinal](https://github.com/RincLiu/afinal)中的FinalActivity（支持组件事件注解）和FinalDb（SQLite操作）；
* HTTP框架[AsyncHttpClient](https://github.com/loopj/android-async-http)</a>；
* 图片处理框架[UniversalImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader)</a>;
* ROOT相关操作模块[RootManager](https://github.com/Chrisplus/RootManager)</a>；

### 第三方SDK模块
* 基于[多盟](http://www.duomeng.net/developers/developers.htm)的广告模块；
* 基于[友盟](http://www.umeng.com)的数据分析统计模块、用户反馈、版本更新等模块；
* 基于[极光推送](http://www.jpush.cn/)的消息推送模块；
* 基于[百度地图API](http://developer.baidu.com/map/)的定位模块；
* 基于[百度媒体云](http://developer.baidu.com/wiki/index.php?title=docs/cplat/media)的视频播放模块，支持本地视频文件和网络视频资源播放；
* 基于[科大讯飞](http://open.voicecloud.cn/developer.php)的语音引擎模块，支持语音识别和语音朗读；
* 社会化组件：新浪微博SSO登录、微博分享、微信分享；

### 组件及工具类
* 国际化：支持English、简体中文、繁體中文；
* 经过简化和定制的二维码扫描模块zxing；
* 全局异常处理模块CrashHandler；
* API兼容类RLAPICompat；
* RLScrollView，新增了滚动相关方法；
* 列表分页组件ListPagerView；
* 定制的下拉刷新组件，包括PullToRefreshGridView、PullToRefreshListView、PullToRefreshScrollView；
* 改进的轮播组件ViewFlow，增加了BitmapIndicator(可设置图片指针)、可设置播放模式（手动、自动向左、自动向右）；
* 定制的侧滑菜单组件MenuDrawer；
* 定制的手势缩放图片视图组件GestureImageView；
* 其他自定义组件：RLDialog、RLAlertDialog、RLListDialog、RLLoadingDialog、RLFileExplorer、RLSpinner、RLWebView、RLOnClickListener；
* 工具类：RLAppUtil(应用管理)、RLFileUtil(IO操作)、RLImgUtil(图像处理)、RLIntentUtil(系统服务调用)、RLJsonUtil(JSON解析)、RLNetUtil(网络服务)、RLStrUtil(字符串处理)、RLSysUtil(系统参数配置)、RLUiUtil(UI相关，自定义toast等)；

### 使用注意
* Analytics、CrashHandler、Feedback、Push这些需要在Application或Activity创建时初始化的组件可以在AndroidManifest.xml中配置是否启用;
