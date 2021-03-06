package com.zmt.boxin.Application;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by Dangelo on 2016/7/5.
 */
public class App extends Application {

    public static User user = new User();

    public void onCreate(){
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(getApplicationContext());
        ImageLoader.getInstance().init(configuration);
    }

    public User getUser(){
        return user;
    }

}
