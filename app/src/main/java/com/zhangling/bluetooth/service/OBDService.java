package com.zhangling.bluetooth.service;

import com.zhangling.bluetooth.ZLGlobal;
import com.zhangling.bluetooth.model.ResponseModel;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface OBDService {
    @Multipart
    @POST("/can/uploadDataFile")
    Observable<ResponseModel<ResponseModel.NullModel>> uploadFile(@PartMap Map<String, RequestBody> params,@Part List<MultipartBody.Part> parts);
}
