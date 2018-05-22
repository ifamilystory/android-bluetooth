package com.zhangling.bluetooth.activity;

//import android.app.Fragment;
import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.inuker.bluetooth.library.search.SearchResult;
import com.zhangling.bluetooth.MainActivity;
import com.zhangling.bluetooth.R;
import com.zhangling.bluetooth.adapter.ClassDeviceListAdapter;
import com.zhangling.bluetooth.adapter.DeviceListAdapter;
import com.zhangling.bluetooth.manager.BlueToothManager;
import com.zhangling.bluetooth.manager.ClassBlueToothManager;
import com.zhangling.bluetooth.manager.RxBusManager;
import com.zhangling.bluetooth.util.ZLUtil;
import com.zhangling.bluetooth.view.DeviceListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

import static com.zhangling.bluetooth.manager.RxBusManager.DeviceData;
import static com.zhangling.bluetooth.manager.RxBusManager.SearchClassDevice;
import static com.zhangling.bluetooth.manager.RxBusManager.SearchDevice;

public class HomeFragment extends Fragment {
    public View mView;
    @BindView(R.id.view)
    public ConstraintLayout contentView;
    DeviceListView mBlueToothListView;
    SearchResult mSelectedDevice;
    BluetoothDevice mSelectedClassDevice;
    @BindView(R.id.bluetooth_view)
    public RelativeLayout blueToothSuperView;
    @BindView(R.id.bluetooth)
    public ImageButton bluetoothButton;
    static BlueToothManager.BlueToothType type = BlueToothManager.BlueToothType.CLASS;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        ZLUtil.tinkButtonColor(bluetoothButton, Color.GRAY);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueToothListView!=null){
                    contentView.removeView(mBlueToothListView);
                }
                switch (type){
                    case BLE:
                        BlueToothManager.getInstance().search(3000,3);
                        mBlueToothListView.setListAdapter(new DeviceListAdapter(MainActivity.getMainActivity()));
                        mBlueToothListView.setClassListAdapter(new ClassDeviceListAdapter(MainActivity.getMainActivity()));
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
                        ClassBlueToothManager.getInstance().createClassBluetoothClient(MainActivity.getMainActivity());
                        ClassBlueToothManager.getInstance().search();
                        mBlueToothListView = new DeviceListView(MainActivity.getMainActivity());
                        mBlueToothListView.setClassListAdapter(new ClassDeviceListAdapter(MainActivity.getMainActivity()));
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


                contentView.addView(mBlueToothListView);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
    }

}
