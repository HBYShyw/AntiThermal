package android.net.wifi.oplus.p2p;

import android.content.Context;
import android.net.wifi.IOplusWifiManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusWifiP2pManager implements IOplusWifiP2pManager {
    public static final int CONTENT_STATUS_END = 4;
    public static final int CONTENT_STATUS_FAILURE = 3;
    public static final int CONTENT_STATUS_LINK_SUCCESS = 2;
    public static final int CONTENT_STATUS_START = 1;
    public static final int CONTENT_TYPE_INTERNET = 3;
    public static final int CONTENT_TYPE_SCREENCAST = 1;
    public static final int CONTENT_TYPE_TELEPHONE = 4;
    public static final int CONTENT_TYPE_TRANSFERFILE = 2;
    public static final int RELO_P2P_GC = 4;
    public static final int RELO_P2P_GO = 3;
    public static final int RELO_P2P_UNKNOWN = 5;
    public static final int RELO_SAP = 2;
    public static final int RELO_STA = 1;
    public static final int RELO_UNKNOWN = 0;
    public static final int ROLE_CONNECT_BLE_PROVIDER = 12;
    public static final int ROLE_CONNECT_BLE_SEEKER = 11;
    public static final int ROLE_CONTENT_RESERVE = 3;
    public static final int ROLE_CONTENT_SINK = 2;
    public static final int ROLE_CONTENT_SOURCE = 1;
    public static final int ROLE_CONTENT_UNKNOWN = 0;
    public static final int STATUS_BLE_CONNECT_DISCONNECT = 36;
    public static final int STATUS_BLE_CONNECT_FAILED = 35;
    public static final int STATUS_BLE_CONNECT_START = 33;
    public static final int STATUS_BLE_CONNECT_SUCCESS = 34;
    public static final int STATUS_BLE_DISCOVER_START = 31;
    public static final int STATUS_BLE_DISCOVER_STOP = 32;
    public static final int STATUS_BLE_UNKNOWN = 30;
    public static final int STATUS_BT_CONNECT_COMPLETE = 14;
    public static final int STATUS_BT_CONNECT_DISCONNECT = 16;
    public static final int STATUS_BT_CONNECT_FAILED = 15;
    public static final int STATUS_BT_CONNECT_START = 13;
    public static final int STATUS_BT_DISCOVER_START = 11;
    public static final int STATUS_BT_DISCOVER_STOP = 12;
    public static final int STATUS_BT_UNKNOWN = 10;
    public static final int STATUS_CAST_CONNECTED = 98;
    public static final int STATUS_CAST_DISCONNECTED = 99;
    public static final int STATUS_CAST_START = 97;
    public static final int STATUS_CONTENT_END = 2;
    public static final int STATUS_CONTENT_RESERVE = 3;
    public static final int STATUS_CONTENT_START = 1;
    public static final int STATUS_CONTENT_UNKNOWN = 0;
    public static final int STATUS_DCS_CONNECT_FAILED = 101;
    public static final int STATUS_DCS_LINK_POOR = 102;
    public static final int STATUS_P2P_CONNECT_COMPLETE = 4;
    public static final int STATUS_P2P_CONNECT_DISCONNECT = 6;
    public static final int STATUS_P2P_CONNECT_FAILED = 5;
    public static final int STATUS_P2P_CONNECT_START = 3;
    public static final int STATUS_P2P_DISCOVER_START = 1;
    public static final int STATUS_P2P_DISCOVER_STOP = 2;
    public static final int STATUS_P2P_UNKNOWN = 0;
    public static final int STATUS_RTSP_CONNECT_COMPLETE = 24;
    public static final int STATUS_RTSP_CONNECT_DISCONNECT = 26;
    public static final int STATUS_RTSP_CONNECT_FAILED = 25;
    public static final int STATUS_RTSP_CONNECT_START = 23;
    public static final int STATUS_RTSP_DISCOVER_START = 21;
    public static final int STATUS_RTSP_DISCOVER_STOP = 22;
    public static final int STATUS_RTSP_UNKNOWN = 20;
    private static final String TAG = "OplusWifiP2pManager";
    public static final int TYPE_CAST_OCAR = 4;
    public static final int TYPE_CAST_OPPOTV = 2;
    public static final int TYPE_CAST_PAD = 5;
    public static final int TYPE_CAST_PCCONNECT = 3;
    public static final int TYPE_CAST_PHONE = 6;
    public static final int TYPE_CAST_UNKNOWN = 0;
    public static final int TYPE_CAST_WFD = 1;
    public static final int TYPE_CONTENT_FILE = 2;
    public static final int TYPE_CONTENT_IDLE = 3;
    public static final int TYPE_CONTENT_RESERVE = 5;
    public static final int TYPE_CONTENT_SCREEN = 1;
    public static final int TYPE_CONTENT_SCREEN_FILE = 4;
    public static final int TYPE_CONTENT_UNKNOWN = 0;
    public static final int TYPE_OSHARE = 8;
    public static final int TYPE_RESERVE = 9;
    private Context mContext;
    IOplusWifiManager mService = IOplusWifiManager.Stub.asInterface(ServiceManager.getService("opluswifikitservice"));

    public OplusWifiP2pManager(Context context) {
        this.mContext = context;
    }

    @Override // android.net.wifi.oplus.p2p.IOplusWifiP2pManager
    public boolean setNfcTriggered(boolean enable) throws RemoteException {
        try {
            boolean ret = this.mService.setNfcTriggered(enable);
            return ret;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    @Override // android.net.wifi.oplus.p2p.IOplusWifiP2pManager
    public void saveExternalPeerAddress(String address) throws RemoteException {
        if (address == null) {
            Log.d(TAG, "saveExternalPeerAddress the address is null");
            return;
        }
        try {
            this.mService.saveExternalPeerAddress(address);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    @Override // android.net.wifi.oplus.p2p.IOplusWifiP2pManager
    public boolean setPcAutonomousGo(boolean enable) throws RemoteException {
        boolean ret = this.mService.setPcAutonomousGo(enable, 0, null);
        return ret;
    }

    public boolean setPcAutonomousGo(boolean enable, int freq, String reverse) throws RemoteException {
        boolean ret = this.mService.setPcAutonomousGo(enable, freq, reverse);
        return ret;
    }

    public void setOshareEnabled(boolean enable, int freq, boolean isGo, boolean isStaticIp) throws RemoteException {
        try {
            this.mService.setOshareEnabled(enable, freq, isGo, isStaticIp);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    @Override // android.net.wifi.oplus.p2p.IOplusWifiP2pManager
    public void setCastStatus(int type, int state, int role, String reverse) throws RemoteException {
        try {
            this.mService.setCastStatus(type, state, role, reverse);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    @Override // android.net.wifi.oplus.p2p.IOplusWifiP2pManager
    public void setCastContent(int type, int state, int role, String reverse) throws RemoteException {
        try {
            this.mService.setCastContent(type, state, role, reverse);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }
}
