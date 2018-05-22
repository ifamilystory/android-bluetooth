package com.zhangling.bluetooth.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RxAddCookiesInterceptor implements Interceptor {
    private Context context;

    public RxAddCookiesInterceptor(Context context) {
        super();
        this.context = context;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可
        Observable.just(sharedPreferences.getString("cookie", ""))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        builder.addHeader("Cookie", s);
                    }
                });
        return chain.proceed(builder.build());
    }


    public  class SaveCookiesInterceptor implements Interceptor {
        private static final String COOKIE_PREF = "cookies_prefs";
        private Context mContext;

        public SaveCookiesInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            //set-cookie可能为多个
            if (!response.headers("set-cookie").isEmpty()) {
                List<String> cookies = response.headers("set-cookie");
                String cookie = encodeCookie(cookies);
                saveCookie(request.url().toString(),request.url().host(),cookie);
            }

            return response;
        }

        //整合cookie为唯一字符串
        private String encodeCookie(List<String> cookies) {
            StringBuilder sb = new StringBuilder();
            Set<String> set=new HashSet<>();
            for (String cookie : cookies) {
                String[] arr = cookie.split(";");
                for (String s : arr) {
                    if(set.contains(s))continue;
                    set.add(s);

                }
            }

            Iterator<String> ite = set.iterator();
            while (ite.hasNext()) {
                String cookie = ite.next();
                sb.append(cookie).append(";");
            }

            int last = sb.lastIndexOf(";");
            if (sb.length() - 1 == last) {
                sb.deleteCharAt(last);
            }

            return sb.toString();
        }

        //保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
        //这样能使得该cookie的应用范围更广
        private void saveCookie(String url,String domain,String cookies) {
            SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if (TextUtils.isEmpty(url)) {
                throw new NullPointerException("url is null.");
            }else{
                editor.putString(url, cookies);
            }

            if (!TextUtils.isEmpty(domain)) {
                editor.putString(domain, cookies);
            }

            editor.apply();

        }
    }


    public class AddCookiesInterceptor implements Interceptor {
        private static final String COOKIE_PREF = "cookies_prefs";
        private Context mContext;

        public AddCookiesInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            String cookie = getCookie(request.url().toString(), request.url().host());
            if (!TextUtils.isEmpty(cookie)) {
                builder.addHeader("Cookie", cookie);
            }

            return chain.proceed(builder.build());
        }

        private String getCookie(String url, String domain) {
            SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
            if (!TextUtils.isEmpty(url)&&sp.contains(url)&&!TextUtils.isEmpty(sp.getString(url,""))) {
                return sp.getString(url, "");
            }
            if (!TextUtils.isEmpty(domain)&&sp.contains(domain) && !TextUtils.isEmpty(sp.getString(domain, ""))) {
                return sp.getString(domain, "");
            }

            return null;
        }
    }
}