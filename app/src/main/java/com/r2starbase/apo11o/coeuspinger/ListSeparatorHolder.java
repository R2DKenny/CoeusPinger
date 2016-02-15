package com.r2starbase.apo11o.coeuspinger;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by apo11o.
 */
public class ListSeparatorHolder extends RecyclerView.ViewHolder {
    private TextView tv;

    public ListSeparatorHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.list_separator_box);
    }

    public void setText(String label) {
        tv.setText(label);
    }
}
