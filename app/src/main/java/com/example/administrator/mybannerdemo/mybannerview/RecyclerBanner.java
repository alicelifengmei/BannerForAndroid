package com.example.administrator.mybannerdemo.mybannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.mybannerdemo.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lifengmei on 2017/8/3.
 * 自定義的自動輪播圖：可设置展示图片，选中指示图，可选展示说明文字，可点击展示图片，可自动播放或停止轮播，可按住展示图片自动轮播停止。
 * 用法：
 * 在xml文件中可以设置
 * 1,切换动画播放执行时间：scroll_time
 * 2,展示图片的伸缩类型：image_scale_model
 * 3,说明文字的：是否隐藏，大小，颜色，距离左上右下距离，gravity
 * 4,indicator指示图片的：宽，高，左、上、右、下、边距，资源图，gravity
 * 5,整体indicator和文字的背景色：title_indicator_bg_color
 * 在代码中可以设置：
 * 1，设置要播放的图片资源，可以是本地的也可以是网络图片：setImageReses(List<?> imageReses)
 * 2，设置图片对应的说明文字：setTitles(String[] titles)
 * 3，设置每个item的点击事件：setOnItemClickLIstener(OnBannerItemClickListener onMyItemClickListener)
 * 4，图片加载方式：setImageLoader(ImageLoaderInterface imageLoader)
 * 5，设置切换间隔时间：setDelayTime(long delayTime)
 * 6，设置播放动画样式：setPageTransformer(ViewPager.PageTransformer pageTransformer)
 * 7，设置
 */

public class RecyclerBanner extends FrameLayout implements ViewPager.OnPageChangeListener{
    private String tag = "MyBanner";

    private Context mContext;
    private ControlledViewPager viewPager;
    private LinearLayout llContain;
    private LinearLayout llIndicators;
    private TextView tvTitle;

    private List<View> imageViews;//存放轮播的ImageView
    private List<ImageView> indicatorViews;//存放指示图View
    private String[] titles;//存放标题内容集合
    public List<?> imageReses;//存放图片资源的集合：可以是网络图片的url，也可以是本地图片资源
    private ViewPagerAdapter adapter;//viewpager的adapter
    //----以下为xml设置的属性
    private int scaleType = 1;//图片在控件中的伸缩方式，默认1是centerCrop
    private int llBgColor;//说明文字和指示图片整块区域的背景图
    private boolean titleVisible = false;//说明文字是否展示，默认false
    private int titleSize;
    private int titleColor;
    private int titleGravity;
    private int titleMargin;
    private int indicatorWidth;
    private int indicatorHeight;
    private int indicatorMarginLeft;
    private int indicatorMarginTop;
    private int indicatorMarginRight;
    private int indicatorMarginBottom;
    private int indicatorSelectedSrc;
    private int indicatorUnSelectedSrc;
    private int indicatorGravity;


    private int lastPosition = 1;//记录上一次点的位置
    private int currentItem;//当前item
    private int count;//实际总共的图片张数
    private long delayTime = 2000;//图片播放的时间间隔，可以设置
    private int scrollTime = 800;//轮播动画执行的时间
    private boolean isAutoPlay = true;
    private boolean isScroll = true;//是否可以滚动，默认可以滚动，只有一张图片时不可以滚动
    private OnBannerItemClickListener onMyItemClickListener;//点击item的点击事件
    private WeakHandler mHandler = new WeakHandler();//发送轮播任务的弱引用handler，避免了内存泄露
    private ImageLoaderInterface<ImageView> imageLoader;//图片的加载方式，自己集成接口实现

    private int bannerStyle = BannerConfig.CIRCLE_INDICATOR;//指示器类型，默认是小圆点，也可以设置右下角数字
    private TextView tvNumIndicator;
    private boolean isRecycle = true;//是否可以循环，默认是


    public RecyclerBanner(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        imageViews = new ArrayList<>();
        indicatorViews = new ArrayList<>();
        imageReses = new ArrayList<>();
        //初始化布局和自定义属性
        initView(attrs,defStyleAttr);
    }

    /**
     * 设置指示器类型：底部中间小圆点，或者底部右侧数字
     * @param bannerStyle
     * @return
     */
    public RecyclerBanner setBannerStyle(int bannerStyle) {
        this.bannerStyle = bannerStyle;
        return this;
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_my_banner,this,true);//TODO 调查一下true和false的区别
        viewPager = (ControlledViewPager) view.findViewById(R.id.vp_pics);
        llContain = (LinearLayout) view.findViewById(R.id.ll_contain);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        llIndicators = (LinearLayout) view.findViewById(R.id.ll_indicators);
        tvNumIndicator = (TextView) view.findViewById(R.id.numIndicator);
        initTypedArray(attrs, defStyleAttr);
        initViewPagerScroll();

    }

    private void initTypedArray(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RecyclerBanner,defStyleAttr,0);//注意四个参数的方法
        if(typedArray != null){
            scrollTime = typedArray.getInt(R.styleable.RecyclerBanner_scroll_time, 800);
            scaleType = typedArray.getInt(R.styleable.RecyclerBanner_image_scale_model,1);
            llBgColor = typedArray.getResourceId(R.styleable.RecyclerBanner_title_indicator_bg_color,Color.TRANSPARENT);
            titleVisible = typedArray.getBoolean(R.styleable.RecyclerBanner_title_visible,false);
            titleSize = typedArray.getDimensionPixelSize(R.styleable.RecyclerBanner_title_size,32);
            titleColor = typedArray.getColor(R.styleable.RecyclerBanner_title_color,Color.BLACK);
            titleGravity = typedArray.getInt(R.styleable.RecyclerBanner_title_gravity,0);
            titleMargin = typedArray.getDimensionPixelSize(R.styleable.RecyclerBanner_title_margin,10);
            indicatorWidth = typedArray.getDimensionPixelSize(R.styleable.RecyclerBanner_indicator_width,6);
            indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.RecyclerBanner_indicator_height,6);
            indicatorMarginLeft = typedArray.getDimensionPixelSize(R.styleable.RecyclerBanner_indicator_margin_left,3);
            indicatorMarginTop = typedArray.getDimensionPixelSize(R.styleable.RecyclerBanner_indicator_margin_top,3);
            indicatorMarginRight = typedArray.getDimensionPixelSize(R.styleable.RecyclerBanner_indicator_margin_right,3);
            indicatorMarginBottom = typedArray.getDimensionPixelSize(R.styleable.RecyclerBanner_indicator_margin_bottom,3);
            indicatorSelectedSrc = typedArray.getResourceId(R.styleable.RecyclerBanner_indicator_selected_src,R.drawable.ic_page_indicator_focused);
            indicatorUnSelectedSrc = typedArray.getResourceId(R.styleable.RecyclerBanner_indicator_unselected_src,R.drawable.ic_page_indicator);
            indicatorGravity = typedArray.getInt(R.styleable.RecyclerBanner_indicator_gravity,0);
            typedArray.recycle();
        }
    }

    /***
     * 初始化播放动画的动画执行时间
     */
    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            BannerScroller mScroller = new BannerScroller(viewPager.getContext());
            mScroller.setDuration(scrollTime);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
    }

    public void start() {//要播放时再将所有的数据拿过来拼接
        setBannerStyleUI();
        setImageViewList(imageReses);
        setData();
    }

    /**
     * 设置是否可以循环，默认是循环
     * @param isRecycle
     */
    public void setIsRecycle(boolean isRecycle){
        this.isRecycle = isRecycle;
    }

    /**
     * 设置是否自动播放，默认自动播放
     * @param isAutoPlay
     */
    public void setAutoPlay(boolean isAutoPlay){
        this.isAutoPlay = isAutoPlay;
        if(isAutoPlay){//自动播放肯定是需要循环的
            this.isRecycle = true;
        }
    }

    /***
     * 播放开始start()方法之后就用该方法继续播放
     */
    public void startAutoPlay(){
        mHandler.removeCallbacks(task);
        mHandler.postDelayed(task,delayTime);
    }

    /***
     * 播放开始start()方法后就用该方法停止播放
     */
    public void stopAutoPlay(){
        mHandler.removeCallbacks(task);
    }
    /***
     * 轮播任务
     */
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {//自动播放默认顺序是从左到右的顺序,
                currentItem = currentItem % (count + 1) + 1;//切换到下一个
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);//不需要动画的切换
                    mHandler.post(task);//为什么此处不需要延迟---因为这个item和最后一个item是同一张图片，在从最后一张图切换到最后一个item的时候已经有时间间隔了，这里只需要偷梁换柱就行了，就不需要再延迟了，否则就会让用户觉得同一张图等了两倍的间隔时间才播放下一张
                } else {
                    viewPager.setCurrentItem(currentItem);
                    mHandler.postDelayed(task, delayTime);
                }
            }
        }
    };

    private void setBannerStyleUI() {
        int visibility;
        if (count > 1) visibility = View.VISIBLE;//只有一张图不展示数字提示小脚标
        else visibility = View.GONE;
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR:
                llIndicators.setVisibility(visibility);
                break;
            case BannerConfig.NUM_INDICATOR:
                tvNumIndicator.setVisibility(VISIBLE);
                break;
        }
    }
    /***
     * 初始化要展示的item，文字说明title，选中指示图片indicator
     * @param imagesUrls
     */
    public void setImageViewList(List<?> imagesUrls) {
        if (imagesUrls == null || imagesUrls.size() <= 0) {
            Log.e(tag, "Please set the images data.");
            return;
        }
        llContain.setBackgroundResource(llBgColor);
        initTitle();
        initImages();
//        createIndicator();
        if(isRecycle){//可循环

            for (int i = 0; i <= count + 1; i++) {
                ImageView imageView = null;
                if (imageLoader != null) {
                    imageView = imageLoader.createImageView(mContext);
                }
                if (imageView == null) {
                    imageView = new ImageView(mContext);
                    imageView.setImageResource(R.drawable.default_icon);
                }
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                setScaleType(imageView);
                Object url = null;//加载的时候有没有屏蔽null的机制
                if (i == 0) {//第0张放最后一张的图，
                    url = imagesUrls.get(count - 1);
                } else if (i == count + 1) {
                    url = imagesUrls.get(0);
                } else {
                    url = imagesUrls.get(i - 1);
                }
                if (imageLoader != null)
                    imageLoader.displayImage(mContext, url, imageView);
                else{
                    imageView.setBackgroundResource(R.drawable.default_icon);
                    Log.e(tag, "Please set images loader.");
                }
                imageViews.add(imageView);
            }
        }else{//不可循环
            for (int i = 0; i < count; i++) {
                ImageView imageView = null;
                if (imageLoader != null) {
                    imageView = imageLoader.createImageView(mContext);
                }
                if (imageView == null) {
                    imageView = new ImageView(mContext);
                    imageView.setImageResource(R.drawable.default_icon);
                }
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                setScaleType(imageView);
                Object url = imagesUrls.get(i);
                if (imageLoader != null)
                    imageLoader.displayImage(mContext, url, imageView);
                else{
                    imageView.setBackgroundResource(R.drawable.default_icon);
                    Log.e(tag, "Please set images loader.");
                }
                imageViews.add(imageView);
            }
        }
    }

    /***
     * 设置title说明文字的隐藏和样式问题
     */
    private void initTitle() {
        if(titles == null || titles.length<=0 || !titleVisible){//没有设置文字标题或者可见性为false
            tvTitle.setVisibility(GONE);
            return;
        }
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(titles[0]);//默认第一个展示文字
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);
        tvTitle.setTextColor(titleColor);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = titleMargin;
        params.topMargin = titleMargin;
        params.rightMargin = titleMargin;
        params.bottomMargin = titleMargin;
        switch (titleGravity){
            case 0:
                params.gravity = Gravity.CENTER;
                break;
            case 1:
                params.gravity = Gravity.LEFT;
                break;
            case 2:
                params.gravity = Gravity.RIGHT;
                break;
            default:
                params.gravity = Gravity.CENTER;
                break;
        }
        tvTitle.setLayoutParams(params);
    }

    /**
     * 设置展示图片的伸缩样式
     * @param view
     */
    private void setScaleType(View view) {
        if(view instanceof ImageView){
            ImageView imageView = (ImageView) view;
            switch (scaleType){
                case 0:
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    break;
                case 1:
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 2:
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    break;
                case 3:
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    break;
                case 4:
                    imageView.setScaleType(ImageView.ScaleType.FIT_END);
                    break;
                case 5:
                    imageView.setScaleType(ImageView.ScaleType.FIT_START);
                    break;
                case 6:
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    break;
                case 7:
                    imageView.setScaleType(ImageView.ScaleType.MATRIX);
                    break;
            }
        }
    }

    private void initImages() {
        imageViews.clear();
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ) {
            createIndicator();
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR) {
            SpannableString sStr = new SpannableString("1/" + count);
            sStr.setSpan(new AbsoluteSizeSpan(18,true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvNumIndicator.setText(sStr);
        }
    }

    /***
     * 初始化指示图片的view
     */
    private void createIndicator() {
        indicatorViews.clear();
        llIndicators.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, indicatorHeight);
            params.leftMargin = indicatorMarginLeft;
            params.topMargin = indicatorMarginTop;
            params.rightMargin = indicatorMarginRight;
            params.bottomMargin = indicatorMarginBottom;
//            if (i == 0) {
//                imageView.setBackgroundResource(indicatorSelectedSrc);
//            } else {
            imageView.setBackgroundResource(indicatorUnSelectedSrc);
//            }
            indicatorViews.add(imageView);
            llIndicators.addView(imageView,params);
            switch (indicatorGravity){//设置指示图标的摆放：中 左 右
                case 0:
                    llIndicators.setGravity(Gravity.CENTER);
                    break;
                case 1:
                    llIndicators.setGravity(Gravity.LEFT);
                    break;
                case 2:
                    llIndicators.setGravity(Gravity.RIGHT);
                    break;
            }

        }
    }

    /***
     * 将要展示的数据填充
     */
    private void setData() {
        if (adapter == null) {
            adapter = new ViewPagerAdapter();
            viewPager.addOnPageChangeListener(this);
        }
        viewPager.setAdapter(adapter);
        viewPager.setFocusable(true);
//        viewPager.setOffscreenPageLimit(1);//设置预加载个数，不能小于1
        //默认第一张图选中状态
        if(isRecycle){
            viewPager.setCurrentItem(1,false);
            lastPosition = 1;
            currentItem = 1;
        }else{
            viewPager.setCurrentItem(0,false);
            lastPosition = 0;
            currentItem = 0;
        }
        if(bannerStyle == BannerConfig.CIRCLE_INDICATOR){
            indicatorViews.get(0).setImageResource(indicatorSelectedSrc);
        }
        if (isScroll && count > 1) {//只有一张图且设置可以滚动时 才可以滚动
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        if (isAutoPlay)
            startAutoPlay();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {//item被选中时
        if(isRecycle){//需要循环
            if (position == 0) position = count;
            if (position > count) position = 1;//数字位置，第六张时是第一张
            if(titles != null && titles.length>0 && titleVisible){
                tvTitle.setText(titles[(position - 1 + count) % count]);
            }
            //思路很简单：因为有错位，所以对应关系都是position-1，由于滑到两头的时候会造成position-1成负，或者超出实际数量，所以用+count再与count求模运算一下
            switch (bannerStyle) {
                case BannerConfig.CIRCLE_INDICATOR:
                    indicatorViews.get((lastPosition - 1 + count) % count).setImageResource(indicatorUnSelectedSrc);
                    indicatorViews.get((position - 1 + count) % count).setImageResource(indicatorSelectedSrc);
                    lastPosition = position;
                    break;
                case BannerConfig.NUM_INDICATOR:
                    //设置字体大小（绝对值,单位：像素）,第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素
                    SpannableString sStr = new SpannableString(position + "/" + count);
                    //注意position的位数，如果是两位数三位数的情况
                    sStr.setSpan(new AbsoluteSizeSpan(18,true), 0, String.valueOf(position).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvNumIndicator.setText(sStr);
                    break;
            }
        }else{//不需要循环正常走
            if(titles != null && titles.length>0 && titleVisible){
                tvTitle.setText(titles[position]);
            }
            switch (bannerStyle) {
                case BannerConfig.CIRCLE_INDICATOR:
                    indicatorViews.get(lastPosition).setImageResource(indicatorUnSelectedSrc);
                    indicatorViews.get(position).setImageResource(indicatorSelectedSrc);
                    lastPosition = position;
                    break;
                case BannerConfig.NUM_INDICATOR:
                    //设置字体大小（绝对值,单位：像素）,第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素
                    SpannableString sStr = new SpannableString(position+1 + "/" + count);
                    //注意position的位数，如果是两位数三位数的情况
                    sStr.setSpan(new AbsoluteSizeSpan(18,true), 0, String.valueOf(position+1).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvNumIndicator.setText(sStr);
                    break;
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        currentItem = viewPager.getCurrentItem();
        switch (state) {
            case 0://No operation
                if(isRecycle){//可以循环的话，滑到最后一个立马切换到第一个，统一往前滑到第0个，立马切换到最后一个
                    if (currentItem == 0) {
                        viewPager.setCurrentItem(count, false);
                    } else if (currentItem == count + 1) {
                        viewPager.setCurrentItem(1, false);
                    }
                }
                break;
            case 1://start Sliding
                if(isRecycle){
                    if (currentItem == count + 1) {
                        viewPager.setCurrentItem(1, false);
                    } else if (currentItem == 0) {
                        viewPager.setCurrentItem(count, false);
                    }
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    /***
     * 手指按下时不自动滚动，手指离开时继续自动滚动
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN ) {//down的时候停止自动播放，在up，cancel，outside的时候恢复自动播放
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /***
     * viewpager的adapter
     */
    private class ViewPagerAdapter extends PagerAdapter {

        public ViewPagerAdapter(){}


        //必须重写以下四个方法
        @Override
        public int getCount() {
            return imageViews == null ? 0 : imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//销毁
            container.removeView(imageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {//预加载
            View childView = imageViews.get(position);
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMyItemClickListener != null){
                        onMyItemClickListener.onItemClick(toRealPosition(position));
                    }
                }
            });
            container.addView(childView);
            return childView;
        }
    }
    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        int realPosition = position;
        if(isRecycle){
            realPosition = ((position - 1) + count ) % count;
        }
        return realPosition;
    }

    /***
     * 设置要播放的图片资源，可以是本地的也可以是网络图片
     * @param imageReses
     */
    public void setImageReses(List<?> imageReses) {
        this.imageReses = imageReses;
        this.count = imageReses.size();
    }

    /***
     * 设置图片对应的说明文字
     * @param titles
     */
    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    /***
     * 设置每个item的点击事件
     * @param onMyItemClickListener
     */
    public void setOnItemClickLIstener(OnBannerItemClickListener onMyItemClickListener){
        this.onMyItemClickListener = onMyItemClickListener;
    }

    /***
     * 图片加载方式
     * @param imageLoader
     */
    public void setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
    }

    /***
     * 设置切换间隔时间
     * @param delayTime
     */
    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    /***
     * 设置播放动画样式
     * @param pageTransformer
     */
    public void setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        try {
            viewPager.setPageTransformer(true,pageTransformer);
        } catch (Exception e) {
            Log.e(tag, "Please set the PageTransformer class");
        }

    }
}
