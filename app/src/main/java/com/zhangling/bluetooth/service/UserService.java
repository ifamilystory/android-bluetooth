package com.zhangling.bluetooth.service;

import com.zhangling.bluetooth.model.ResponseModel;
import com.zhangling.bluetooth.model.UI.UserModel;
import com.zhangling.bluetooth.model.rt.UserLoginModel;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface UserService {
    @Multipart
    @POST("/api/user/login")
    Observable<ResponseModel<UserModel>> login(@PartMap Map<String, RequestBody> params);
}
