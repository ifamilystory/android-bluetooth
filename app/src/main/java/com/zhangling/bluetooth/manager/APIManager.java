package com.zhangling.bluetooth.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhangling.bluetooth.Application;
import com.zhangling.bluetooth.ZLGlobal;
import com.zhangling.bluetooth.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zhangling.bluetooth.interceptor.RxAddCookiesInterceptor;
import com.zhangling.bluetooth.interceptor.RxSaveCookiesInterceptor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class APIManager {
    private static volatile APIManager instance=null;
    private APIManager (){

    }
    public static APIManager getInstance(){
        if(instance==null){
            synchronized(APIManager.class){
                instance=new APIManager ();
            }
        }
        return instance;
    }

    private Retrofit mRetrofit;

    ClearableCookieJar cookieJar =
            new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Application.getApplication()));

    public Retrofit getRetrofit() {
        if (mRetrofit != null){
            return mRetrofit;
        }
        OkHttpClient okHttpClient = new  OkHttpClient.Builder().cookieJar(cookieJar).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZLGlobal.remoteHost)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient)
                .build();
        mRetrofit = retrofit;
        return retrofit;
    }





}
