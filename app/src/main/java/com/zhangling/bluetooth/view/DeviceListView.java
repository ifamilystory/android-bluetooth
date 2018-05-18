package com.zhangling.bluetooth.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhangling.bluetooth.R;
import com.zhangling.bluetooth.adapter.ClassDeviceListAdapter;
import com.zhangling.bluetooth.adapter.DeviceListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ysec on 2018/3/19.
 */

public class DeviceListView extends ConstraintLayout implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        onListItemClick.onListItemClick(adapterView,view,i,l);
    }

    public interface OnListItemClick{
        public void onListItemClick(AdapterView<?> l, View v, int position, long id);
    }

    public DeviceListView.OnListItemClick onListItemClick;
    @BindView(R.id.list_view)
    public ListView listView;



    private DeviceListAdapter listAdapter;

    public ClassDeviceListAdapter getClassListAdapter() {
        return classListAdapter;
    }

    public void setClassListAdapter(ClassDeviceListAdapter classListAdapter) {
        this.classListAdapter = classListAdapter;
        listView.setAdapter(this.classListAdapter);
        listView.setOnItemClickListener(this);
    }

    private ClassDeviceListAdapter classListAdapter;
    public DeviceListView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.device_listview, this);
        ButterKnife.bind(this);
    }
    public DeviceListAdapter getListAdapter() {
        return listAdapter;
    }

    public void setListAdapter(DeviceListAdapter listAdapter) {
        this.listAdapter = listAdapter;
        listView.setAdapter(this.listAdapter);
        listView.setOnItemClickListener(this);
    }



}