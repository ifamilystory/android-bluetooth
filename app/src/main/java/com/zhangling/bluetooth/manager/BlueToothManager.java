package com.zhangling.bluetooth.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zhangling.bluetooth.util.ZLUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.orhanobut.logger.Logger;

import java.util.UUID;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Created by ysec on 2018/3/19.
 */

public class BlueToothManager {

    public enum BlueToothType {
        BLE,CLASS
    }

    public static String serverUUID = "FFE0";
    public static String characteristicUUID = "FFE1";
    public static Integer MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 9;
    private static volatile BlueToothManager instance=null;
    private BluetoothClient bluetoothClient = null;
    private SearchResult mDevice;
    private BleGattService mService = null;
    private BleGattCharacter mCharacter = null;
    private Thread mBluetoothConnectThread = null;
    private Handler mBluetoothConnectThreadHander = null;
    private Thread mbluetoothDataThread = null;
    private Handler mbluetoothDataThreadHander = null;

    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {//蓝牙状态
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {

        }

    };

    private final BluetoothBondListener mBluetoothBondListener = new BluetoothBondListener() {//设备配对状态
        @Override
        public void onBondStateChanged(String mac, int bondState) {
            // bondState = Constants.BOND_NONE, BOND_BONDING, BOND_BONDED
        }
    };
    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {//设备连接状态

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                Logger.i(String.format("蓝牙: ---------------------------%s",String.valueOf(status)));
            } else if (status == STATUS_DISCONNECTED) {
                Logger.i(String.format("蓝牙: ---------------------------%s",String.valueOf(status)));
            }else {
                Logger.d(status);
            }
        }
    };
    private BlueToothManager (){

    }
    public static  BlueToothManager getInstance(){
        if(instance==null){
            synchronized(BlueToothManager.class){
                instance=new BlueToothManager ();
            }
        }
        return instance;
    }

    public synchronized void createBluetoothClient(Context context) {
        if (BlueToothManager.getInstance().bluetoothClient != null) {
            if (!BlueToothManager.getInstance().bluetoothClient.isBluetoothOpened()) {
                BlueToothManager.getInstance().bluetoothClient.openBluetooth();
            }
            return;
        }
        BlueToothManager.getInstance().bluetoothClient = new BluetoothClient(context);
        if (!BlueToothManager.getInstance().bluetoothClient.isBluetoothOpened()){
            BlueToothManager.getInstance().bluetoothClient.openBluetooth();
        }
    }

    public void search(final int duration, final int times){
        if (mBluetoothConnectThread == null){
            mBluetoothConnectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    mBluetoothConnectThreadHander = new Handler();
                    SearchRequest request = new SearchRequest.Builder().searchBluetoothLeDevice(duration,times).searchBluetoothClassicDevice(duration,times).build();
                    BlueToothManager.getInstance().bluetoothClient.search(request, new SearchResponse() {
                        @Override
                        public void onSearchStarted() {

                        }

                        @Override
                        public void onDeviceFounded(SearchResult device) {
                            RxBusManager.getInstance().send(RxBusManager.SearchDevice,device);
                        }

                        @Override
                        public void onSearchStopped() {

                        }

                        @Override
                        public void onSearchCanceled() {

                        }
                    });
                    Looper.loop();//开启loop
                }
            });

            mBluetoothConnectThread.start();
        }

    }

    public void connect(final SearchResult device, final int connectRetryCount, final int connectTimeout, final int serviceDiscoverRetryCount, final int serviceDiscoverTimeout){
        Logger.i(Thread.currentThread().getName());
        mBluetoothConnectThreadHander.post(new Runnable() {
            @Override
            public void run() {
                Logger.i(Thread.currentThread().getName());
                if (device != null) {
                    if (BlueToothManager.getInstance().bluetoothClient.getConnectStatus(device.getAddress()) == STATUS_CONNECTED){
                        return;
                    }
                    BlueToothManager.getInstance().bluetoothClient.unregisterConnectStatusListener(device.getAddress(), mBleConnectStatusListener);
//            if (mService != null){
//                BlueToothManager.getInstance().bluetoothClient.unnotify(device.getAddress(), mService.getUUID(), mCharacter.getUuid(), new BleUnnotifyResponse() {
//                    @Override
//                    public void onResponse(int code) {
//
//                    }
//                });
//            }

                }else {
                    return;
                }
                mDevice = device;
                BleConnectOptions options = new BleConnectOptions.Builder()
                        .setConnectRetry(connectRetryCount)   // 连接如果失败重试3次
                        .setConnectTimeout(connectTimeout)   // 连接超时30s
                        .setServiceDiscoverRetry(serviceDiscoverRetryCount)  // 发现服务如果失败重试3次
                        .setServiceDiscoverTimeout(serviceDiscoverTimeout)  // 发现服务超时20s
                        .build();

                BlueToothManager.getInstance().bluetoothClient.connect(device.getAddress(), options, new BleConnectResponse() {
                    @Override
                    public void onResponse(int code, BleGattProfile data) {Logger.i(String.format("蓝牙连接: ---------------------------%s",String.valueOf(code)));
                        if (code == REQUEST_SUCCESS) {
                            for (BleGattService service : data.getServices()) {
                                if (service.getUUID().toString().toUpperCase().contains(serverUUID)) {
                                    mService = service;
                                    break;
                                }
                            }
                            for (BleGattCharacter c : mService.getCharacters()) {
                                if (c.getUuid().toString().toUpperCase().contains(characteristicUUID)){
                                    mCharacter = c;
                                    break;
                                }
                            }
                            if (mbluetoothDataThread == null){
                                mbluetoothDataThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        mbluetoothDataThreadHander = new Handler();
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        BlueToothManager.getInstance().notify(mDevice, mService.getUUID(),mCharacter.getUuid());
                                        Looper.loop();
                                    }
                                });
                                mbluetoothDataThread.start();
                            }else {
                                mBluetoothConnectThreadHander.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        BlueToothManager.getInstance().notify(mDevice, mService.getUUID(),mCharacter.getUuid());
                                    }
                                });
                            }


                        }else {

                        }
                    }
                });
                BlueToothManager.getInstance().bluetoothClient.registerConnectStatusListener(device.getAddress(), mBleConnectStatusListener);
            }
        });

    }

    public void notify(SearchResult device,UUID serviceUUID,UUID characterUUID){

        BlueToothManager.getInstance().bluetoothClient.notify(device.getAddress(), serviceUUID, characterUUID, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, final byte[] value) {
                mBluetoothConnectThreadHander.post(new Runnable() {
                    @Override
                    public void run() {
                        RxBusManager.getInstance().send(RxBusManager.DeviceData, ZLUtil.bytesToHexString(value));
                    }
                });

            }

            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {

                }
            }
        });
    }

}
