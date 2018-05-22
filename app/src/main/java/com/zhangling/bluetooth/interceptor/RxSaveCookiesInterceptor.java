package com.zhangling.bluetooth.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RxSaveCookiesInterceptor implements Interceptor {
    private Context context;
    SharedPreferences sharedPreferences;

    public RxSaveCookiesInterceptor(Context context) {
        super();
        this.context = context;
        sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("set-cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据
            Observable.just(originalResponse.headers("set-cookie")).map(new Function<List<String>, String>() {
                @Override
                public String apply(List<String> strings) throws Exception {
                    String[] cookieArray = strings.get(0).split(";");
                    return cookieArray[0];
                }
            }).subscribe(new Consumer<String>() {
                @Override
                public void accept(String o) throws Exception {
                    cookieBuffer.append(o).append(";");
                }
            });
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie", cookieBuffer.toString());
            editor.commit();
        }

        return originalResponse;
    }



}





