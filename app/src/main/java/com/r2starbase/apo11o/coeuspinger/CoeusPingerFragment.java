package com.r2starbase.apo11o.coeuspinger;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CoeusPingerFragment extends Fragment
        implements WifiP2pManager.PeerListListener,
        WifiP2pManager.ConnectionInfoListener,
        WifiP2pManager.GroupInfoListener {
    public static final String TAG = "CoeusPingerFragment";

    private WifiP2pGroup pConnectedGroup;
    private WifiP2pInfo pInfo;

    private SwipeRefreshLayout srLayout;
    private DeviceListAdapter dlAdapter;
    private List<Object> dList = new ArrayList<>();

    public CoeusPingerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_coeus_pinger, container, false);

        RecyclerView ladView = (RecyclerView) v.findViewById(R.id.ping_view);
        ladView.setHasFixedSize(true);
        ladView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dlAdapter = new DeviceListAdapter(dList);
        ladView.setAdapter(dlAdapter);

        // Setup the list item click callback
        ladView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if (dList.get(position) instanceof DeviceInfo) {
                            DeviceInfo di = (DeviceInfo)dList.get(position);
                            Log.d(TAG, di.toString());
                            if (di.getDevice().status == WifiP2pDevice.CONNECTED) {
                                ((CoeusPinger)getActivity()).disconnect();
                            } else if (di.getDevice().status == WifiP2pDevice.INVITED) {
                                ((CoeusPinger)getActivity()).cancelConnect();
                            } else {
                                ((CoeusPinger)getActivity()).connect(di.getDevice());
                            }
                        }
                    }
                }));

        // Setup the swipe down to refresh callback
        srLayout = (SwipeRefreshLayout) v.findViewById(R.id.ping_layout);
        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((CoeusPinger) getActivity()).startDiscovery();
            }
        });

        return v;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        reloadPeers(peers);
        srLayout.setRefreshing(false);
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        this.pInfo = info;
    }

    @Override
    public void onGroupInfoAvailable(WifiP2pGroup group) {
        pConnectedGroup = group;
    }


    // Re-populate the peer array
    public void reloadPeers(WifiP2pDeviceList newList) {
        List<Object> connectedPeerList = new ArrayList<>();
        List<Object> otherPeerList = new ArrayList<>();

        String deviceLabel = "My Device";
        if (!((CoeusPinger)getActivity()).isWifiP2pEnabled()) {
            deviceLabel += " (Wifi P2P Disabled)";
        }

        for (WifiP2pDevice dev : newList.getDeviceList()) {
            if (dev.status == WifiP2pDevice.CONNECTED) {
                connectedPeerList.add(new DeviceInfo(dev));
            } else {
                otherPeerList.add(new DeviceInfo(dev));
            }
        }

        dlAdapter.clear();
        dlAdapter.addAll(deviceLabel,
                ((CoeusPinger)getActivity()).getDeviceInfo(),
                "Connected Peer",
                connectedPeerList,
                "Other Peers",
                otherPeerList);
    }

}
