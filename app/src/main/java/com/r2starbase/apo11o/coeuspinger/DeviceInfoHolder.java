package com.r2starbase.apo11o.coeuspinger;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by apo11o.
 */
public class DeviceInfoHolder extends RecyclerView.ViewHolder{
    private TextView nameView;
    private TextView statusText;
    private TextView addressText;

    public DeviceInfoHolder(View itemView) {
        super(itemView);
        nameView = (TextView) itemView.findViewById(R.id.device_name);
        statusText = (TextView) itemView.findViewById(R.id.device_status);
        addressText = (TextView) itemView.findViewById(R.id.device_address);
    }

    public void setNameView(String name) {
        this.nameView.setText(name);
    }

    public void setStatusText(String status) {
        this.statusText.setText(status);
    }

    public void setAddressText(String address) {
        this.addressText.setText(address);
    }
}