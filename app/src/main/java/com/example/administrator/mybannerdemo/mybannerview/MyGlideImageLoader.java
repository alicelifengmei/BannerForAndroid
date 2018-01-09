package com.example.administrator.mybannerdemo.mybannerview;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.mybannerdemo.R;

/**
 * Created by lifengmei on 2017/8/4.
 */

public class MyGlideImageLoader implements ImageLoaderInterface<ImageView> {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).placeholder(R.drawable.default_icon).error(R.drawable.default_icon).dontAnimate().into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }
}
