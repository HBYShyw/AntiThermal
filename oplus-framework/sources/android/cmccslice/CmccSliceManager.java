package android.cmccslice;

import android.cmccslice.ICmccSliceManager;
import android.cmccslice.ICmccSliceManagerCallback;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CmccSliceManager {
    public static final int APP_ACTIVED = 21;
    public static final int APP_BACKGROUND = 22;
    public static final String CC = "connectionCapabilities";
    public static final String CMCCSLICE_CONNECTIVITY_ACTION = "android.net.conn.CMCCSLICE_CONNECTIVITY_CHANGE";
    public static final String CMCCSLICE_STATE = "state";
    private static final boolean DEBUG = true;
    public static final String DESTPORT = "destPort";
    public static final String DESTPORT_END = "destPortEndRange";
    public static final String DESTPORT_START = "destPortStartRange";
    public static final String DNN = "dnn";
    public static final String DOMAIN_DESCRIPTORS = "domainDescriptors";
    public static final String FQDN2IP = "fqdn2Ip";
    public static final String INDEX_OF_RULE = "indexOfRule";
    public static final String IPV4 = "ipv4";
    public static final String IPV6 = "ipv6";
    public static final int IP_CLOSE = 2;
    public static final int IP_OPEN = 1;
    public static final String JSON_URSP = "jsonUrsp";
    public static final String MASKV4 = "maskV4";
    public static final String MATCHALL = "matchAll";
    public static final String NO_OF_RULES = "noOfRules";
    public static final String OSAPPID = "osAppId";
    public static final String PRECEDENCE = "precedence";
    public static final String PREFIX_LEN = "prefixLength";
    public static final String PROTOCOL = "protocol";
    public static final int PROXY = 0;
    public static final int PROXY_ABORT = 1;
    public static final int SLICE_STATE_CONNECTED = 1;
    public static final int SLICE_STATE_UNCONNECTED = 0;
    public static final String SLOTID = "slotid";
    private static final String TAG = "CmccSliceManager";
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_WIFI = 1;
    private Context mContext;
    public static final Uri TD_URI = Uri.parse("content://com.cmccslice.proxyapp.provider/td");
    public static final Uri QCOM_URI = Uri.parse("content://com.cmccslice.proxyapp.provider/qcom");
    private ArrayList<Callback> mCallbackList = new ArrayList<>();
    private ICmccSliceManagerCallback mCmccSliceManagerCallback = new ICmccSliceManagerCallback.Stub() { // from class: android.cmccslice.CmccSliceManager.1
        @Override // android.cmccslice.ICmccSliceManagerCallback
        public void onPostUrsp(int slotid, List<UrspRule> urspList) throws RemoteException {
            if (CmccSliceManager.this.mCallbackList != null) {
                for (int i = 0; i < CmccSliceManager.this.mCallbackList.size(); i++) {
                    Log.d(CmccSliceManager.TAG, "onPostTd: slotid=" + slotid + ";urspList" + urspList);
                    ((Callback) CmccSliceManager.this.mCallbackList.get(i)).onPostUrsp(slotid, urspList);
                }
            }
        }

        @Override // android.cmccslice.ICmccSliceManagerCallback
        public void onPostUrspForQcom(int slotIndex, int noOfRules, int indexOfRule, String jsonUrsp) throws RemoteException {
            if (CmccSliceManager.this.mCallbackList != null) {
                for (int i = 0; i < CmccSliceManager.this.mCallbackList.size(); i++) {
                    ((Callback) CmccSliceManager.this.mCallbackList.get(i)).onPostUrspForQcom(slotIndex, noOfRules, indexOfRule, jsonUrsp);
                }
            }
        }
    };
    private ICmccSliceManager mService = ICmccSliceManager.Stub.asInterface(ServiceManager.getService("cmcc5gslice"));

    /* loaded from: classes.dex */
    public interface Callback {
        void onPostUrsp(int i, List<UrspRule> list);

        void onPostUrspForQcom(int i, int i2, int i3, String str);
    }

    public CmccSliceManager(Context context, Handler handler) {
        this.mContext = context;
    }

    public void registerCallback(Callback mCallback) {
        Log.d(TAG, "registerCallback ");
        if (!this.mCallbackList.contains(mCallback)) {
            this.mCallbackList.add(mCallback);
            try {
                this.mService.registerCallback(this.mCmccSliceManagerCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterCallback(Callback mCallback) {
        Log.d(TAG, "unregisterCallback");
        if (this.mCallbackList.contains(mCallback)) {
            this.mCallbackList.remove(mCallback);
            try {
                this.mService.unregisterCallback(this.mCmccSliceManagerCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void requestSliceNetwork(TrafficDescriptor trafficDescriptor, int precedence) {
        Log.d(TAG, "requestSliceNetwork: " + trafficDescriptor.toString());
        try {
            this.mService.requestSliceNetwork(trafficDescriptor, precedence);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void appProxyAbort(int key) {
        Log.d(TAG, "appProxyAbort: " + key);
        try {
            this.mService.appProxyAbort(this.mContext.getPackageName(), key);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean isSliceEstablished() {
        Log.d(TAG, "isSliceEstablished: ");
        try {
            return this.mService.isSliceEstablished();
        } catch (RemoteException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isSliceEnabled() {
        Log.d(TAG, "isSliceEnabled: ");
        try {
            return this.mService.isSliceEnabled();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCertForTD(int networkCapabilities) {
        Log.d(TAG, "getCertForTD: " + networkCapabilities);
        try {
            return this.mService.getCertForTD(networkCapabilities);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCertOfApp() {
        Log.d(TAG, "getCertOfApp: ");
        try {
            return this.mService.getCertOfApp();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setSliceEnabled(boolean enabled) {
        Log.d(TAG, "setSliceEnabled: " + enabled);
        try {
            this.mService.setSliceEnabled(enabled);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setNetworkPriorType(int type) {
        Log.d(TAG, "setNetworkPriorType: " + type);
        try {
            this.mService.setNetworkPriorType(type);
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }

    public void postUrsp(int slotIndex, int type, String originalUrsp, List<UrspRule> urspList) {
        Log.d(TAG, "postUrsp: slotIndex=" + slotIndex + ";type=" + type + ";originalUrsp=" + originalUrsp + ";urspList=" + urspList);
        try {
            this.mService.postUrsp(slotIndex, type, originalUrsp, urspList);
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }
}
