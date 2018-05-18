package com.zhangling.bluetooth.adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhangling.bluetooth.R;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;

/**
 * Created by ysec on 2018/5/16.
 */

public class ClassDeviceListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mDevices;
    private LayoutInflater mInflator;
    private Activity mContext;

    public ClassDeviceListAdapter(Activity c) {
        super();
        mContext = c;
        mDevices = new ArrayList<BluetoothDevice>();
        mInflator = mContext.getLayoutInflater();
    }

    public void addDevice(BluetoothDevice device) {
        if (!mDevices.contains(device)) {
            mDevices.add(device);
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mDevices.get(position);
    }

    public void clear() {
        mDevices.clear();
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ClassDeviceListAdapter.ViewHolder viewHolder;
        // General ListView optimization code.


        if (view == null) {
            // 按当前所需的样式，确定new的布局
            view = mInflator.inflate(R.layout.device_list_item, null);
            viewHolder = new ClassDeviceListAdapter.ViewHolder();
            viewHolder.deviceAddress = (TextView) view
                    .findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view
                    .findViewById(R.id.device_name);
            view.setTag(viewHolder);

        } else {

            viewHolder = (ClassDeviceListAdapter.ViewHolder) view.getTag();
        }
        BluetoothDevice device = mDevices.get(i);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0){
            viewHolder.deviceName.setText(deviceName);
            viewHolder.deviceAddress.setText(device.getAddress());
        }else {
            viewHolder.deviceName.setText("未知");
            viewHolder.deviceAddress.setText(device.getAddress());
        }




        return view;
    }

    class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}
