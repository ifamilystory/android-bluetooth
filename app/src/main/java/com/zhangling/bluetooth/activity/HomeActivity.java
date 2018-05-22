package com.zhangling.bluetooth.activity;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhangling.bluetooth.R;
import com.zhangling.bluetooth.adapter.ClassDeviceListAdapter;
import com.zhangling.bluetooth.adapter.DeviceListAdapter;
import com.zhangling.bluetooth.base.BaseActivity;
import com.zhangling.bluetooth.manager.BlueToothManager;
import com.zhangling.bluetooth.manager.ClassBlueToothManager;
import com.zhangling.bluetooth.manager.LogManager;
import com.zhangling.bluetooth.manager.RxBusManager;
import com.zhangling.bluetooth.util.ZLUtil;
import com.zhangling.bluetooth.view.DeviceListView;
import com.inuker.bluetooth.library.search.SearchResult;
import com.orhanobut.logger.Logger;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

import static com.zhangling.bluetooth.manager.RxBusManager.DeviceData;
import static com.zhangling.bluetooth.manager.RxBusManager.SearchClassDevice;
import static com.zhangling.bluetooth.manager.RxBusManager.SearchDevice;

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


    ResolveInfo mHomeInfo;
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
//
//        PackageManager pm = getPackageManager();
//        mHomeInfo =pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
////工作内容
//
////工作内容
//        go2Idle();

        startActivity(new Intent(this, LoginActivity.class));

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
                                mBlueToothListView.setVisibility(View.INVISIBLE);

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





    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            moveTaskToBack(true);
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);//新建一个对话框
//            dialog.setMessage("确定要退出测试吗?");//设置提示信息
//            //设置确定按钮并监听
//            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
////                    finish();//结束当前Activity
//                    moveTaskToBack(true);
//                }
//            });
//            //设置取消按钮并监听
//            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //这里什么也不用做
//                }
//            });
//            dialog.show();//最后不要忘记把对话框显示出来
        }
        return false;
    }


    private void go2Idle(){
        ActivityInfo ai = mHomeInfo.activityInfo;
        Intent startIntent= new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        startIntent.setComponent(new ComponentName(ai.packageName,
                ai.name));
        startActivitySafely(startIntent);
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch(ActivityNotFoundException e) {
            Toast.makeText(this, "work wrongly",
                    Toast.LENGTH_SHORT).show();
        } catch(SecurityException e) {
            Toast.makeText(this, "notsecurity",Toast.LENGTH_SHORT).show();
            Log.e(HomeActivity.class.getName(),"Launcher does not have the permission to launch "
                            + intent
                            + ".Make sure to create a MAIN intent-filter for the corresponding activity "
                            + "oruse the exported attribute for this activity.",
                    e);
        }
    }
}
