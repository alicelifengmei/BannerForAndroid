# BannerForAndroid
可以自动无限轮播的自定义banner

1，这是一款可自动循环播放的banner：
    banner.setAutoPlay(boolean)设置是否可自动播放（默认自动播放）
    banner.setIsRecycle(boolean)设置是否可以循环播放（默认是）；
    banner.setImageReses(imageUrls);//设置图片资源，可以是本地图片，也可以是网络图片资源
    banner.setDelayTime(3000);//设置切换间隔
    banner.setPageTransformer();//设置切换动画
    banner.setBannerStyle(BannerConfig.NUM_INDICATOR);//设置banner样式，这是底部有数字角标的样式，不设置默认是有指示圆点的样式
    banner.start();//初始化

  具体用法Demo中有详细用法和说明。

2，两种样式：带指示小点；和带右下角数字角标。图片说明可选。

3，轮播实现原理：多加两个item，并在特定时机调用viewPager.setCurrentItem(count, false)无滑动效果的切换，来制造循环假象。
   具体就是前面多加一个item放最后一张图，最后面多加一个item放第一张图片，监听onPageScrollStateChanged()方法，
   当向左滑到最后一个item的时候（里面放的是第一张图），无滑动切换到真正第一张图的item去；
   当向右滑到第一个item的时候（里面放的是最后一张图），无滑动切换到真正的最后一张图的item中去。
