package com.zhangling.bluetooth.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhangling.bluetooth.R;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;

/**
 * Created by ysec on 2018/3/19.
 */

public class DeviceListAdapter extends BaseAdapter {

    // Adapter for holding devices found through scanning.

    private ArrayList<SearchResult> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;

    public DeviceListAdapter(Activity c) {
        super();
        mContext = c;
        mLeDevices = new ArrayList<SearchResult>();
        mInflator = mContext.getLayoutInflater();
    }

    public void addDevice(SearchResult device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    public SearchResult getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.


        if (view == null) {
            // 按当前所需的样式，确定new的布局
            view = mInflator.inflate(R.layout.device_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view
                    .findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view
                    .findViewById(R.id.device_name);
            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }
        SearchResult device = mLeDevices.get(i);
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
