# SouthWindMusic

MVVM架构的仿网易云安卓客户端

## 项目简介

仿网易云音乐安卓客户端，后端接口来自[NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)

实现功能：

* 网易云账号登录与注册
* 用户创建歌单与收藏歌单获取
* 本地歌曲扫描
* 歌曲搜索
* 歌曲收藏
* 创建歌单
* 高仿网易云播放页与通知栏样式
* 每日推荐歌单
* 私人雷达
* 推荐歌单
* 推荐视频获取与播放

扫码下载apk体验，如果二维码图片不加载，点击这个[下载链接](http://io.inaction.fun/static/SouthWindMusic.apk)

<img src="http://img.inaction.fun/static/92709.png" alt="二维码" style="zoom:50%;" />

### 图片展示

* **账号登录注册（使用网易云音乐的账号）**

  <img src="http://img.inaction.fun/static/66243.jpg" alt="启动页" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/61500.jpg" alt="登录注册" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/58612.jpg" alt="登录注册" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/51148.jpg" alt="登录注册" style="zoom: 33%;" width="205px" />

* **“我的”页与歌单页**

  <img src="http://img.inaction.fun/static/58558.jpg" alt="我的" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/78410.jpg" alt="歌单" style="zoom: 33%;" width="205px" />

* **播放页面**

  <img src="http://img.inaction.fun/static/75416.jpg" alt="歌单" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/32416.jpg" alt="播放页" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/71962.jpg" alt="播放" style="zoom: 33%;" width="205px" />

* **"发现"页**

  <img src="http://img.inaction.fun/static/80659.jpg" alt="发现页" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/12654.jpg" alt="每日推荐" style="zoom: 33%;" width="205px" />

* **“视频”页**

  <img src="http://img.inaction.fun/static/86884.jpg" alt="视频页" style="zoom: 33%;" width="205px" />

  <img src="http://img.inaction.fun/static/69984.jpg" alt="视频页" style="zoom: 33%;"/>

* **”搜索“页**

  <img src="http://img.inaction.fun/static/91506.jpg" alt="搜索页" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/55831.jpg" alt="搜索页" style="zoom: 33%;" width="205px" />

## 项目难点

### 播放器设计

使用了安卓原生的`MediaPlayer`，在其基础上根据实际需求封装。

<img src="http://img.inaction.fun/static/47801.jpg" alt="播放器生命周期图" style="zoom: 33%;" />

### 高仿网易云播放页实现

<img src="http://img.inaction.fun/static/75416.jpg" alt="歌单" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/32416.jpg" alt="播放页" style="zoom: 33%;" width="205px" />

播放时旋转唱片，可无限滑动的ViewPager，由自定义View实现

滚动的歌词，在RecyclerView的基础上实现

### 高仿网易云歌单页UI

<img src="http://img.inaction.fun/static/78410.jpg" alt="歌单" style="zoom: 33%;" width="205px" /><img src="http://img.inaction.fun/static/24452.jpg" alt="歌单" style="zoom: 33%;" width="205px" />

顶部有一张高斯模糊的背景图，在先上划动列表时，Toolbar的背景将会渐变，完美的与原有位置背景图对应。同时，”播放全部“这个View将在列表继续向上滑动时吸顶。

这个页面并没有使用RecyclerView的多布局来实现，因为代码太繁琐。而是借助于[ConsecutiveScroller](https://github.com/donkingliang/ConsecutiveScroller)。



## 不足

时间不够，有很多功能还没有实现，如下载功能，还有无资源歌曲的处理等等

## 感谢

[NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)