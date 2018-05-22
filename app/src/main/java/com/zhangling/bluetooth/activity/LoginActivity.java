package com.zhangling.bluetooth.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.orhanobut.logger.Logger;
import com.zhangling.bluetooth.R;
import com.zhangling.bluetooth.base.BaseActivity;
import com.zhangling.bluetooth.manager.APIManager;
import com.zhangling.bluetooth.manager.ClassBlueToothManager;
import com.zhangling.bluetooth.manager.UploadFileManager;
import com.zhangling.bluetooth.model.ResponseModel;
import com.zhangling.bluetooth.model.UI.UserModel;
import com.zhangling.bluetooth.model.rt.UserLoginModel;
import com.zhangling.bluetooth.service.UserService;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_button)
    public Button loginButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupUI() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mNavigationViewModel.setTitle("登录");
        updateNavigationView();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService service = APIManager.getInstance().getRetrofit().create(UserService.class);
                UserLoginModel userLoginModel = new UserLoginModel();
                userLoginModel.setAccount("jlm");
                userLoginModel.setPassword("jlm");
                RequestBody account = RequestBody.create(MediaType.parse("text/plain"), userLoginModel.getAccount());
                RequestBody password = RequestBody.create(MediaType.parse("text/plain"), userLoginModel.getPassword());
                Map<String,RequestBody> datas = new HashMap<>();
                datas.put("account",account);
                datas.put("password",password);
                service.login(datas).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseModel<UserModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseModel<UserModel> userModelResponseModel) {
                        Logger.i(userModelResponseModel.getData().getAccount());
                        UploadFileManager.getInstance().config();
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("%s",e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
    }
}
