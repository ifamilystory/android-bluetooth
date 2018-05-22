package com.zhangling.bluetooth;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.zhangling.bluetooth.manager.UploadFileManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ysec on 2018/3/20.
 */

public class Application extends android.app.Application{
    private static Application context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        Realm.init(this);
//        RealmConfiguration config = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(config);
        configLoger();
//        UploadFileManager.getInstance().config();
    }

    private void configLoger(){
        /*Logger.d("debug");
        Logger.e("error");
        Logger.w("warning");
        Logger.v("verbose");
        Logger.i("information");
        Logger.wtf("wtf!!!!");*/
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(3)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(null) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("Steven.Zhang's Log")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
        Logger.addLogAdapter(new DiskLogAdapter());
    }

    /**
     * 获取全局上下文*/
    public static Application getApplication() {
        return context;
    }
}
