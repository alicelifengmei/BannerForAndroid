package com.example.administrator.mybannerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.mybannerdemo.mybannerview.MyBanner;
import com.example.administrator.mybannerdemo.mybannerview.MyGlideImageLoader;
import com.example.administrator.mybannerdemo.mybannerview.OnMyItemClickListener;
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

public class MyBannerViewActivity extends AppCompatActivity {

    //存放图片资源
//    private int[] imageIds = new int[]{R.drawable.banner1,R.drawable.banner2,R.drawable.banner_c,R.drawable.banner_cp,R.drawable.banner_hk};
    private List<Integer> imageIds = new ArrayList<>();

    //存放图片的标题
    private String[] titles = new String[]{"巩俐不低俗，我就不能低俗", "扑树又回来啦！再唱经典老歌引万人大合唱", "揭秘北京电影如何升级", "乐视网TV版大派送", "热血屌丝的反杀"};
    private MyBanner myBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_banner_view);
        imageIds.add(R.drawable.banner1);
        imageIds.add(R.drawable.banner2);
        imageIds.add(R.drawable.banner_c);
        imageIds.add(R.drawable.banner_hk);
        imageIds.add(R.drawable.banner_cp);
        myBanner = (MyBanner) findViewById(R.id.my_banner);
        myBanner.setImageReses(imageIds);
        myBanner.setTitles(titles);
        myBanner.setDelayTime(3000);//切换间隔
        myBanner.setPageTransformer(new DefaultTransformer());//切换动画,动画有十七种
        myBanner.setImageLoader(new MyGlideImageLoader());
        myBanner.setOnItemClickLIstener(new OnMyItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MyBannerViewActivity.this, "点击对应的内容是：" + titles[position], Toast.LENGTH_SHORT).show();
            }
        });
        myBanner.start();
    }

    /***
     * 点击按钮选择不同的动画效果,注意9-12的动画效果需要出去之后再进来演示才能看到完整效果
     * @param view
     */
    public void onTapClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                myBanner.setPageTransformer(new AccordionTransformer());
                break;
            case R.id.btn2:
                myBanner.setPageTransformer(new BackgroundToForegroundTransformer());
                break;
            case R.id.btn3:
                myBanner.setPageTransformer(new CubeInTransformer());
                break;
            case R.id.btn4:
                myBanner.setPageTransformer(new CubeOutTransformer());
                break;
            case R.id.btn5:
                myBanner.setPageTransformer(new DefaultTransformer());
                break;
            case R.id.btn6:
                myBanner.setPageTransformer(new DepthPageTransformer());
                break;
            case R.id.btn7:
                myBanner.setPageTransformer(new FlipHorizontalTransformer());
                break;
            case R.id.btn8:
                myBanner.setPageTransformer(new FlipVerticalTransformer());
                break;
            case R.id.btn9:
                myBanner.setPageTransformer(new ForegroundToBackgroundTransformer());
                break;
            case R.id.btn10:
                myBanner.setPageTransformer(new RotateDownTransformer());
                break;
            case R.id.btn11:
                myBanner.setPageTransformer(new RotateUpTransformer());
                break;
            case R.id.btn12:
                myBanner.setPageTransformer(new ScaleInOutTransformer());
                break;
            case R.id.btn13:
                myBanner.setPageTransformer(new StackTransformer());
                break;
            case R.id.btn14:
                myBanner.setPageTransformer(new TabletTransformer());
                break;
            case R.id.btn15:
                myBanner.setPageTransformer(new ZoomInTransformer());
                break;
            case R.id.btn16:
                myBanner.setPageTransformer(new ZoomOutSlideTransformer());
                break;
            case R.id.btn17:
                myBanner.setPageTransformer(new ZoomOutTranformer());
                break;
        }
    }

}
