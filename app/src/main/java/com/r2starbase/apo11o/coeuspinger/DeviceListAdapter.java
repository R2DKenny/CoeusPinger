package com.r2starbase.apo11o.coeuspinger;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Originally created by apo11o on 2/1/16.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "DeviceListAdapter";
    private final int LABEL = 0, DEVICE = 1;
    private List<Object> deviceList;

    public DeviceListAdapter(List<Object> dList) {
        deviceList = dList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        LayoutInflater li = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case LABEL:
                vh = new ListSeparatorHolder(li.inflate(R.layout.list_separator, parent, false));
                break;
            case DEVICE:
                vh = new DeviceInfoHolder(li.inflate(R.layout.device_card, parent, false));
                break;
            default:
                vh = new ListSeparatorHolder(li.inflate(R.layout.list_separator, parent, false));
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case LABEL:
                ListSeparatorHolder ls = (ListSeparatorHolder) holder;
                setLabelData(ls, position);
                break;
            case DEVICE:
                DeviceInfoHolder di = (DeviceInfoHolder) holder;
                setDeviceData(di, position);
                break;
            default:
                setLabelData((ListSeparatorHolder) holder, -1);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (deviceList.get(position) instanceof String) {
            return LABEL;
        } else if (deviceList.get(position) instanceof DeviceInfo) {
            return DEVICE;
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public void setLabelData(ListSeparatorHolder ls, int position) {
        if (position < 0) {
            ls.setText("Dummy");
        } else {
            ls.setText(this.deviceList.get(position).toString());
        }
    }

    public void setDeviceData(DeviceInfoHolder diHolder, int position) {
        DeviceInfo di = (DeviceInfo) this.deviceList.get(position);
        diHolder.setNameView(di.getDeviceName());
        diHolder.setAddressText(di.getDeviceAddress());
        diHolder.setStatusText(di.getDeviceStatusMsg());
    }

    public void clear() {
        this.deviceList.clear();
        notifyDataSetChanged();
    }

    public void addAll(Object... args) {
        for (Object arg : args) {
            if (arg instanceof List) {
                this.deviceList.addAll((List) arg);
            } else {
                this.deviceList.add(arg);
            }
        }
        notifyDataSetChanged();
    }
}
