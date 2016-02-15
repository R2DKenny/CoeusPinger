package com.r2starbase.apo11o.coeuspinger;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CoeusPinger extends AppCompatActivity {
    public static final String TAG = "CoeusPinger";
    public static final int SERVER_PORT = 8988;

    private WifiP2pManager pManager;
    private WifiP2pManager.Channel pChannel;
    private WifiP2pBroadcastReceiver pReceiver;
    private DeviceInfo pThisDevice;

    private boolean isWifiP2pEnabled;

    private final IntentFilter pIntentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coeus_pinger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupP2pWifi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coeus_pinger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_wifi_settings) {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupIntentFilter() {
        pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        pIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }
    private void setupP2pWifi() {
        setupIntentFilter();
        pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        if (pManager != null) {
            pChannel = pManager.initialize(this, getMainLooper(), null);
            if (pChannel == null) {
                pManager = null;
                Log.e(TAG, "Failed to setup channel");
            }
        } else {
            Log.e(TAG, "Failed to get Wifi P2P service");
        }
        pReceiver = new WifiP2pBroadcastReceiver(pManager, pChannel, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(pReceiver, pIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        pManager.stopPeerDiscovery(pChannel, null);
        unregisterReceiver(pReceiver);
    }

    public void updateWifiP2pStatus(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    public void updateDeviceInfo(WifiP2pDevice device) {
        this.pThisDevice = new DeviceInfo(device);
    }

    public boolean isWifiP2pEnabled() {
        return this.isWifiP2pEnabled;
    }

    public DeviceInfo getDeviceInfo() {
        return this.pThisDevice;
    }

    public void startDiscovery() {
        if (pManager != null) {
            pManager.discoverPeers(pChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int reason) {
                    Log.d(TAG, "Discovery failed: " + reason);
                }
            });
        }
    }

    public void connect(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        if (pManager != null) {
            pManager.connect(pChannel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Successfully connected");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d(TAG, "Failed to connect: " + reason);
                }
            });
        }
    }

    public void disconnect() {
        if (pManager != null) {
            pManager.removeGroup(pChannel, new WifiP2pManager.ActionListener() {
                public void onSuccess() {
                    Log.d(TAG, "Removed from group");
                }

                public void onFailure(int reason) {
                    Log.d(TAG, "Failed to remove from group: " + reason);
                }
            });
        }
    }

    public void cancelConnect() {
        if (pManager != null) {
            pManager.cancelConnect(pChannel, new WifiP2pManager.ActionListener() {
                public void onSuccess() {
                    Log.d(TAG, "Canceled connection");
                }

                public void onFailure(int reason) {
                    Log.d(TAG, "Failed to cancel connection: " + reason);
                }
            });
        }
    }
}
