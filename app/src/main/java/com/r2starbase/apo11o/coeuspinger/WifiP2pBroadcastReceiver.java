package com.r2starbase.apo11o.coeuspinger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Originally created by apo11o on 2/4/16.
 */
public class WifiP2pBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "WifiP2pBRecv";
    private WifiP2pManager pManager;
    private WifiP2pManager.Channel pChannel;
    private CoeusPinger pActivity;
    private boolean pLastGroupFormed = false;

    public WifiP2pBroadcastReceiver(WifiP2pManager pManager,
                                    WifiP2pManager.Channel pChannel,
                                    CoeusPinger pActivity) {
        this.pManager = pManager;
        this.pChannel = pChannel;
        this.pActivity = pActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            this.pActivity.updateWifiP2pStatus(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.d(WifiP2pBroadcastReceiver.TAG, "Wifi Peer Changed");
            if (pManager != null) {
                pManager.requestPeers(pChannel, (WifiP2pManager.PeerListListener) pActivity
                        .getFragmentManager().findFragmentById(R.id.cp_fragment));
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(WifiP2pBroadcastReceiver.TAG, "Wifi Connection Changed");
            if (pManager != null) {
                NetworkInfo ni = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                WifiP2pInfo wi = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                pManager.requestGroupInfo(pChannel, (WifiP2pManager.GroupInfoListener) pActivity
                        .getFragmentManager().findFragmentById(R.id.cp_fragment));
                pManager.requestConnectionInfo(pChannel, (WifiP2pManager.ConnectionInfoListener) pActivity
                        .getFragmentManager().findFragmentById(R.id.cp_fragment));
                if (ni.isConnected()) {
                    Log.d(TAG, "Connected to network");
                } else if (!pLastGroupFormed) {
                    this.pActivity.startDiscovery();
                }
                pLastGroupFormed = wi.groupFormed;
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(WifiP2pBroadcastReceiver.TAG, "Wifi Device Changed");
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            this.pActivity.updateDeviceInfo(device);
        }
    }
}
