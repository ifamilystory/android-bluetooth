package com.zhangling.bluetooth.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;

import com.zhangling.bluetooth.base.BaseActivity;
import com.zhangling.bluetooth.util.ZLUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.search.SearchResult;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * Created by ysec on 2018/5/16.
 */

public class ClassBlueToothManager {

    public interface DeviceChangedCallback {
        public void changed(List<BluetoothDevice> mDevices);
        public void connected();
        public void connecting();
        public void disconnect();
    }



    private static volatile ClassBlueToothManager instance=null;
    public BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
    public DeviceChangedCallback callback;
    public BaseActivity mContext;
    public static String serverUUID = "FFE0";
    public static String characteristicUUID = "FFE1";
    public static int formatDataLength = 15;
    public static int dataBufferSize = 1024;

    private ClassBlueToothManager (){

    }
    public static  ClassBlueToothManager getInstance(){
        if(instance==null){
            synchronized(ClassBlueToothManager.class){
                if(instance==null){
                    instance=new ClassBlueToothManager ();
                }
            }
        }
        return instance;
    }

    public ClassBlueToothManager(BaseActivity context){
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableBtIntent, 100);
        }
    }

    public synchronized void createClassBluetoothClient(BaseActivity context) {
        if (ClassBlueToothManager.getInstance().mBluetoothAdapter != null) {
            if (!ClassBlueToothManager.getInstance().mBluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivityForResult(enableBtIntent, 100);
            }
            return;
        }
        mContext = context;
        ClassBlueToothManager.getInstance().mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!ClassBlueToothManager.getInstance().mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableBtIntent, 100);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                RxBusManager.getInstance().send(RxBusManager.SearchClassDevice,device);
            }
        }
    };
    private BroadcastReceiver mStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.i(String.format("连接状态: %s",action));
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                RxBusManager.getInstance().send(RxBusManager.DeviceConnectionStatue, action);
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                RxBusManager.getInstance().send(RxBusManager.DeviceConnectionStatue, action);
            }

        }
    };

    public void search(){
        // Register the BroadcastReceiver
        if (mReceiver.getResultCode() != 0){
            mContext.unregisterReceiver(mReceiver);
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);
        mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();

    }

    public void connect(BluetoothDevice device){
        if (mStateReceiver.getResultCode() != 0){
            mContext.unregisterReceiver(mStateReceiver);
        }

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        mContext.registerReceiver(mStateReceiver, filter1);
        mContext.registerReceiver(mStateReceiver, filter3);
        mContext.registerReceiver(mStateReceiver, filter2);
        ConnectThread connect = new ConnectThread(device);
        connect.start();
    }



    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code

                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(device.getUuids()[0].getUuid().toString()));
            } catch (IOException e) {

            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            new ConnectedThread(mmSocket).start();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[ClassBlueToothManager.dataBufferSize];  // buffer store for the stream
            byte[] logBuffer = new byte[ClassBlueToothManager.formatDataLength];
            int bytes = 0; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    byte[] real = new byte[bytes];
                    if (totalCounter == Integer.MAX_VALUE) {
                        totalCounter = 0;
                    }
//                    if (totalCounter%ClassBlueToothManager.formatDataLength == 0){
//                        System.arraycopy (buffer,0,logBuffer,0,bytes);
//                    }else {
//                        int header = totalCounter%ClassBlueToothManager.formatDataLength;
//                        if (header + bytes > ClassBlueToothManager.formatDataLength){
//                            System.arraycopy (buffer,0,logBuffer,header-1,ClassBlueToothManager.formatDataLength-header);
//                            LogManager.getInstance().writeLog(ZLUtil.bytesToHexString(logBuffer),"obdtotal");
//                            System.arraycopy (buffer,ClassBlueToothManager.formatDataLength-header-1,logBuffer,0,header + bytes-ClassBlueToothManager.formatDataLength);
//                        }else if (header + bytes == ClassBlueToothManager.formatDataLength){
//                            LogManager.getInstance().writeLog(ZLUtil.bytesToHexString(logBuffer),"obdtotal");
//                            System.arraycopy (buffer,0,logBuffer,0,bytes);
//                        }else{
//                            System.arraycopy (buffer,0,logBuffer,header-1,bytes);
//                        }
//
//                    }
                    totalCounter += bytes;
                    System.arraycopy (buffer,0,real,0,bytes);
                    decodeData(real);
                    LogManager.getInstance().writeLog(String.valueOf(totalCounter/ClassBlueToothManager.formatDataLength),"obdtotalcount");
                } catch (IOException e) {
                    break;
                }
            }
        }

        private Integer totalCounter = 0;
        private Integer validCounter = 0;
        /// 蓝牙传输的一帧数据
        private String dataString = ""; //一帧数据 15字节
        /// 需要上传的数据
        private String  needDataString = ""; //需要上传的数据  8字节
        /// 当前的字节下标
        private int  currentByteIndex = -1;
        /// 数据的Id
        private int messageId  = 0;
        /// 检验值
        private long check = 0;
        private long checkErrorCounter = 0;
        private void decodeData(byte[] encodeData){
            for (byte byteData:encodeData){
                if (currentByteIndex == -1){
                    if (ZLUtil.byte2ToUnsignedShort(byteData)==0xFF){
                        createDataString(byteData);
                        check += ZLUtil.byte2ToUnsignedShort(byteData);
                    }else {
                        checkErrorCounter += 1;
                    }
                }else {
                    if (currentByteIndex == 3){
                        if (ZLUtil.byte2ToUnsignedShort(byteData) != 0){
                            currentByteIndex = -1;
                            resetupData();
                            checkErrorCounter += 1;
                            continue;
                        }
                    }
                    if (currentByteIndex == 4){
                        if (ZLUtil.byte2ToUnsignedShort(byteData) != 8){
                            currentByteIndex = -1;
                            resetupData();
                            checkErrorCounter += 1;
                            continue;
                        }
                    }
                    createDataString(byteData);
                    if (currentByteIndex != 14) {
                        check += ZLUtil.byte2ToUnsignedShort(byteData);
                        if (currentByteIndex == 2 || currentByteIndex == 3) {
                            messageId += ZLUtil.byte2ToUnsignedShort(byteData);
                            if (currentByteIndex == 2 && ZLUtil.byte2ToUnsignedShort(byteData) != 0){
                                messageId += ZLUtil.byte2ToUnsignedShort(byteData)*255;
                            }
                        }else if (currentByteIndex >= 6) {
                            needData(byteData);
                        }
                    }
                    if (currentByteIndex == 14) {
                        if  ((check & 0x000000FF) == ZLUtil.byte2ToUnsignedShort(byteData)){
                            validCounter += 1;
                            LogManager.getInstance().writeLog(String.valueOf(validCounter),"obdvalidcount");
                            RxBusManager.getInstance().send(RxBusManager.DeviceData, needDataString);
                        }else {

                        }
                        currentByteIndex = -1;
                        resetupData();
                    }

                }

            }
        }

        private void createDataString(byte src){
            String valueString =ZLUtil.byteToHexString(src);
            dataString += valueString;
            dataString += " ";
            currentByteIndex += 1;
        }

        private void resetupData() {
            messageId = 0;
            check = 0;
            dataString = "";
            needDataString = "";
        }

        private void needData(byte src) {
            String valueString =ZLUtil.byteToHexString(src);
            needDataString += valueString;
            needDataString += " ";
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }





}
