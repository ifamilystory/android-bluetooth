package com.ccydsz.cloud.activity;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ccydsz.cloud.R;
import com.ccydsz.cloud.adapter.ClassDeviceListAdapter;
import com.ccydsz.cloud.adapter.DeviceListAdapter;
import com.ccydsz.cloud.base.BaseActivity;
import com.ccydsz.cloud.manager.BlueToothManager;
import com.ccydsz.cloud.manager.ClassBlueToothManager;
import com.ccydsz.cloud.manager.LogManager;
import com.ccydsz.cloud.manager.RxBusManager;
import com.ccydsz.cloud.util.ZLUtil;
import com.ccydsz.cloud.view.DeviceListView;
import com.inuker.bluetooth.library.search.SearchResult;
import com.orhanobut.logger.Logger;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

import static com.ccydsz.cloud.manager.RxBusManager.DeviceData;
import static com.ccydsz.cloud.manager.RxBusManager.SearchClassDevice;
import static com.ccydsz.cloud.manager.RxBusManager.SearchDevice;

/**
 * Created by ysec on 2018/3/19.
 */

public class HomeActivity extends BaseActivity {
    DeviceListView mBlueToothListView;
    SearchResult mSelectedDevice;
    BluetoothDevice mSelectedClassDevice;
    @BindView(R.id.bluetooth_view)
    public RelativeLayout blueToothSuperView;
    @BindView(R.id.bluetooth)
    public ImageButton bluetoothButton;

    static BlueToothManager.BlueToothType type = BlueToothManager.BlueToothType.CLASS;

    @BindView(R.id.view)
    public ConstraintLayout view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBusManager.getInstance().tObservable(SearchDevice,SearchResult.class).subscribe(new Consumer<SearchResult>() {
            @Override
            public void accept(SearchResult searchResult) throws Exception {
                mBlueToothListView.getListAdapter().addDevice(searchResult);
                mBlueToothListView.getListAdapter().notifyDataSetChanged();
            }
        });
        RxBusManager.getInstance().tObservable(SearchClassDevice,BluetoothDevice.class).subscribe(new Consumer<BluetoothDevice>() {
            @Override
            public void accept(BluetoothDevice searchResult) throws Exception {
                mBlueToothListView.getClassListAdapter().addDevice(searchResult);
                mBlueToothListView.getClassListAdapter().notifyDataSetChanged();
            }
        });
        RxBusManager.getInstance().tObservable(DeviceData,String.class).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
//                Logger.i(String.format("蓝牙onNotify的数据: %s",s));
//                LogManager.getInstance().writeLog(s,"obd");
            }
        });


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    BlueToothManager.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//判断是否需要 向用户解释，为什么要申请该权限
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
//                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }
        }else {

        }



    }

    @Override
    public void rightNavigationViewAction(View view) {
        super.rightNavigationViewAction(view);
    }

    @Override
    public void setupUI() {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mNavigationViewModel.setTitle("首页");
        mNavigationViewModel.setRightIconNameId(R.mipmap.add);
        updateNavigationView();
        ZLUtil.tinkButtonColor(bluetoothButton,Color.GRAY);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueToothListView!=null){
                    view.removeView(mBlueToothListView);
                }
                switch (type){
                    case BLE:
                        BlueToothManager.getInstance().search(3000,3);
                        mBlueToothListView.setListAdapter(new DeviceListAdapter(HomeActivity.this));
                        mBlueToothListView.setClassListAdapter(new ClassDeviceListAdapter(HomeActivity.this));
                        ConstraintLayout.LayoutParams container = new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT
                        );
                        mBlueToothListView.onListItemClick = new DeviceListView.OnListItemClick() {
                            @Override
                                public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
                                SearchResult device = mBlueToothListView.getListAdapter().getDevice(position);
                                mSelectedDevice = device;
                                BlueToothManager.getInstance().connect(device,3,30000,3,20000);
                            }
                        };
                        container.startToStart = blueToothSuperView.getId();
                        container.endToEnd = blueToothSuperView.getId();
                        container.topToBottom = blueToothSuperView.getId();
                        container.height = 300;
                        mBlueToothListView.setLayoutParams(container);
                    case CLASS:
                        ClassBlueToothManager.getInstance().createClassBluetoothClient(HomeActivity.this);
                        ClassBlueToothManager.getInstance().search();
                        mBlueToothListView = new DeviceListView(HomeActivity.this);
                        mBlueToothListView.setClassListAdapter(new ClassDeviceListAdapter(HomeActivity.this));
                        ConstraintLayout.LayoutParams container2 = new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT
                        );
                        mBlueToothListView.onListItemClick = new DeviceListView.OnListItemClick() {
                            @Override
                            public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
                                BluetoothDevice device = mBlueToothListView.getClassListAdapter().getDevice(position);
                                mSelectedClassDevice = device;
                                ClassBlueToothManager.getInstance().connect(device);
                            }
                        };
                        container2.startToStart = blueToothSuperView.getId();
                        container2.endToEnd = blueToothSuperView.getId();
                        container2.topToBottom = blueToothSuperView.getId();
                        container2.height = 300;
                        mBlueToothListView.setLayoutParams(container2);
                }


                view.addView(mBlueToothListView);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BlueToothManager.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            BlueToothManager.getInstance().search(3000,3);
        }
    }
}
