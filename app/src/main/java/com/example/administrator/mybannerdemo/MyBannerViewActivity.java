package com.example.administrator.mybannerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.mybannerdemo.mybannerview.BannerConfig;
import com.example.administrator.mybannerdemo.mybannerview.RecyclerBanner;
import com.example.administrator.mybannerdemo.mybannerview.MyGlideImageLoader;
import com.example.administrator.mybannerdemo.mybannerview.OnBannerItemClickListener;
import com.example.administrator.mybannerdemo.transformer.AccordionTransformer;
import com.example.administrator.mybannerdemo.transformer.BackgroundToForegroundTransformer;
import com.example.administrator.mybannerdemo.transformer.CubeInTransformer;
import com.example.administrator.mybannerdemo.transformer.CubeOutTransformer;
import com.example.administrator.mybannerdemo.transformer.DefaultTransformer;
import com.example.administrator.mybannerdemo.transformer.DepthPageTransformer;
import com.example.administrator.mybannerdemo.transformer.FlipHorizontalTransformer;
import com.example.administrator.mybannerdemo.transformer.FlipVerticalTransformer;
import com.example.administrator.mybannerdemo.transformer.ForegroundToBackgroundTransformer;
import com.example.administrator.mybannerdemo.transformer.RotateDownTransformer;
import com.example.administrator.mybannerdemo.transformer.RotateUpTransformer;
import com.example.administrator.mybannerdemo.transformer.ScaleInOutTransformer;
import com.example.administrator.mybannerdemo.transformer.StackTransformer;
import com.example.administrator.mybannerdemo.transformer.TabletTransformer;
import com.example.administrator.mybannerdemo.transformer.ZoomInTransformer;
import com.example.administrator.mybannerdemo.transformer.ZoomOutSlideTransformer;
import com.example.administrator.mybannerdemo.transformer.ZoomOutTranformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示可循环banner的例子
 */
public class MyBannerViewActivity extends AppCompatActivity {

    //存放图片资源
//    private int[] imageIds = new int[]{R.drawable.banner1,R.drawable.banner2,R.drawable.banner_c,R.drawable.banner_cp,R.drawable.banner_hk};
    private List<Integer> imageIds = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    //存放图片的标题
    private String[] titles = new String[]{"图片说明1", "图片说明2", "图片说明3", "图片说明4", "图片说明5"};
    private RecyclerBanner banner1;
    private RecyclerBanner banner2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_banner_view);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        //演示自动轮播的banner，有指示点
        banner1 = (RecyclerBanner) findViewById(R.id.my_banner);
        //非自动，且不能轮播的banner，底部有数字角标
        banner2 = (RecyclerBanner) findViewById(R.id.banner_num_no_recycle);
    }

    private void initData() {
        initBanner1();//初始化banner1
        initBanner2();//初始化banner2
    }

    private void initListener() {
        banner1.setOnItemClickLIstener(new OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MyBannerViewActivity.this, "点击对应的内容是：" + titles[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBanner1() {
        imageIds.add(R.drawable.banner1);
        imageIds.add(R.drawable.banner2);
        imageIds.add(R.drawable.banner_c);
        imageIds.add(R.drawable.banner_hk);
        imageIds.add(R.drawable.banner_cp);
        banner1.setImageReses(imageIds);
        banner1.setTitles(titles);//设置每个图片的说明
        banner1.setDelayTime(3000);//切换间隔
        banner1.setPageTransformer(new DefaultTransformer());//切换动画,动画有十七种
        banner1.setImageLoader(new MyGlideImageLoader());//设置图片加载器
        banner1.start();//banner初始化
    }

    private void initBanner2() {
        imageUrls.add("http://i3.download.fd.pchome.net/t_960x600/g1/M00/07/09/oYYBAFMv8q2IQHunACi90oB0OHIAABbUQAAXO4AKL3q706.jpg");
        imageUrls.add("http://images.weiphone.net/attachments/photo/Day_120308/118871_91f6133116504086ed1b82e0eb951.jpg");
        imageUrls.add("http://benyouhuifile.it168.com/forum/macos/attachments/month_1104/1104241737046031b3a754f783.jpg");
        banner2.setImageReses(imageUrls);//设置网络图片
        banner2.setDelayTime(3000);
        banner2.setPageTransformer(new DefaultTransformer());
        banner2.setImageLoader(new MyGlideImageLoader());
        banner2.setIsRecycle(false);//设置不可循环
        banner2.setAutoPlay(false);//设置不可自动播放
        banner2.setBannerStyle(BannerConfig.NUM_INDICATOR);//设置banner样式，这是底部有数字角标的样式，不设置默认是有指示圆点的样式
        banner2.start();//banner初始化
    }

    /***
     * 点击按钮选择不同的动画效果,注意9-12的动画效果需要出去之后再进来演示才能看到完整效果
     * @param view
     */
    public void onTapClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                banner1.setPageTransformer(new AccordionTransformer());
                break;
            case R.id.btn2:
                banner1.setPageTransformer(new BackgroundToForegroundTransformer());
                break;
            case R.id.btn3:
                banner1.setPageTransformer(new CubeInTransformer());
                break;
            case R.id.btn4:
                banner1.setPageTransformer(new CubeOutTransformer());
                break;
            case R.id.btn5:
                banner1.setPageTransformer(new DefaultTransformer());
                break;
            case R.id.btn6:
                banner1.setPageTransformer(new DepthPageTransformer());
                break;
            case R.id.btn7:
                banner1.setPageTransformer(new FlipHorizontalTransformer());
                break;
            case R.id.btn8:
                banner1.setPageTransformer(new FlipVerticalTransformer());
                break;
            case R.id.btn9:
                banner1.setPageTransformer(new ForegroundToBackgroundTransformer());
                break;
            case R.id.btn10:
                banner1.setPageTransformer(new RotateDownTransformer());
                break;
            case R.id.btn11:
                banner1.setPageTransformer(new RotateUpTransformer());
                break;
            case R.id.btn12:
                banner1.setPageTransformer(new ScaleInOutTransformer());
                break;
            case R.id.btn13:
                banner1.setPageTransformer(new StackTransformer());
                break;
            case R.id.btn14:
                banner1.setPageTransformer(new TabletTransformer());
                break;
            case R.id.btn15:
                banner1.setPageTransformer(new ZoomInTransformer());
                break;
            case R.id.btn16:
                banner1.setPageTransformer(new ZoomOutSlideTransformer());
                break;
            case R.id.btn17:
                banner1.setPageTransformer(new ZoomOutTranformer());
                break;
        }
    }

}
