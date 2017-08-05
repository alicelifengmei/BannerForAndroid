package com.example.administrator.mybannerdemo.mybannerview;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * Created by lifengmei on 2017/8/4.
 * 图片加载接口，便于用户自己实现图片加载方式
 */

public interface MyImageLoaderInterface<T extends View> extends Serializable {

    void displayImage(Context context, Object path, T imageView);

    T createImageView(Context context);
}
