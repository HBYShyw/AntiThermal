package android.telephony.ims;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.OplusPropertyList;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.telephony.ims.aidl.IImsRil;
import android.telephony.ims.aidl.IImsRilInd;
import android.util.Log;
import com.android.internal.telephony.OplusFeature;
import com.android.internal.telephony.OplusFeatureHelper;
import com.oplus.content.IOplusFeatureConfigList;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ImsRilConnector {
    private static final int IMS_EXT_SERVICE_CONNECT = 1;
    private static final int IMS_EXT_SERVICE_CONNECT_IF_NEEDED = 2;
    private static final int IMS_RETRY_TIMEOUT_MS = 500;
    private static final int MAX_IMS_RETRY_COUNT = 8;
    private static final String MTK_IMS_PACKAGE_NAME = "com.mediatek.ims";
    private static final String QCOM_IMS_PACKAGE_NAME = "org.codeaurora.ims";
    private final String TAG;
    private final boolean isTablet;
    private final boolean isVmModemSupport;
    private boolean mBound;
    private Handler mConnectionRetryHandler;
    private final Context mContext;
    private ArrayList<IImsRilIndListener> mIImsRilIndListeners;
    private IImsRil mImsRil;
    private IImsRilInd mImsRilInd;
    private ServiceConnection mImsRilServiceConnection;
    private boolean mIsBinderAvailable;
    private IListener mListener;
    private boolean mNeedCallback;
    private int mRetryCount;
    private ImsRilManager mRilMgr;
    private static final boolean isQcomPlatform = SystemProperties.get("ro.boot.hardware", "unknow").toLowerCase().startsWith("qcom");
    private static final boolean isMTKPlatform = SystemProperties.get("ro.boot.hardware", "unknow").toLowerCase().startsWith("mt");

    /* loaded from: classes.dex */
    public interface IImsRilIndListener {
        void onImsRilInd(int i, int i2, Bundle bundle);
    }

    /* loaded from: classes.dex */
    public interface IListener {
        void onConnectionAvailable(ImsRilManager imsRilManager);

        void onConnectionUnavailable();
    }

    public ImsRilConnector(Context context, IListener listener) throws Exception {
        this(context, listener, Looper.myLooper());
    }

    public ImsRilConnector(Context context, IListener listener, Looper looper) throws Exception {
        this.TAG = "ImsRilConnector";
        this.mBound = false;
        this.mConnectionRetryHandler = null;
        this.mIsBinderAvailable = false;
        this.mNeedCallback = false;
        this.mIImsRilIndListeners = new ArrayList<>();
        this.mRetryCount = 0;
        this.mImsRilServiceConnection = new ServiceConnection() { // from class: android.telephony.ims.ImsRilConnector.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder b) {
                ImsRilConnector.this.mImsRil = IImsRil.Stub.asInterface(b);
                Log.d("ImsRilConnector", "onServiceConnected");
                if (ImsRilConnector.this.mImsRil == null) {
                    Log.e("ImsRilConnector", "QtiImsExtService is not running");
                    return;
                }
                ImsRilConnector imsRilConnector = ImsRilConnector.this;
                imsRilConnector.mRilMgr = new ImsRilManager(imsRilConnector.mContext, ImsRilConnector.this.mImsRil);
                try {
                    ImsRilConnector.this.mRilMgr.setImsExt(ImsRilConnector.this.mImsRil.getIImsExtBinder());
                    if (ImsRilConnector.this.mListener != null) {
                        ImsRilConnector.this.mListener.onConnectionAvailable(ImsRilConnector.this.mRilMgr);
                    }
                } catch (RemoteException e) {
                    Log.e("ImsRilConnector", "ImsExt is not running");
                }
                if (ImsRilConnector.this.mNeedCallback) {
                    try {
                        if (ImsRilConnector.this.mImsRilInd == null) {
                            ImsRilConnector.this.mImsRilInd = new IImsRilInd.Stub() { // from class: android.telephony.ims.ImsRilConnector.1.1
                                @Override // android.telephony.ims.aidl.IImsRilInd
                                public void onImsRilInd(int phoneId, int eventId, Bundle bundle) {
                                    synchronized (ImsRilConnector.this.mIImsRilIndListeners) {
                                        Iterator it = ImsRilConnector.this.mIImsRilIndListeners.iterator();
                                        while (it.hasNext()) {
                                            IImsRilIndListener listener2 = (IImsRilIndListener) it.next();
                                            listener2.onImsRilInd(phoneId, eventId, bundle);
                                        }
                                    }
                                }
                            };
                        }
                        ImsRilConnector.this.mRilMgr.registerIndication(ImsRilConnector.this.mImsRilInd);
                    } catch (Exception e2) {
                        Log.e("ImsRilConnector", "registerIndication " + e2);
                    }
                }
                ImsRilConnector.this.mIsBinderAvailable = true;
                ImsRilConnector.this.mRetryCount = 0;
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                Log.d("ImsRilConnector", "onServiceDisconnected " + ImsRilConnector.this.mListener);
                if (ImsRilConnector.this.mListener != null) {
                    ImsRilConnector.this.mListener.onConnectionUnavailable();
                }
                if (ImsRilConnector.this.mNeedCallback) {
                    try {
                        ImsRilConnector.this.mRilMgr.unRegisterIndication(ImsRilConnector.this.mImsRilInd);
                    } catch (Exception e) {
                        Log.e("ImsRilConnector", "unRegisterIndication " + e);
                    }
                }
                ImsRilConnector.this.mIsBinderAvailable = false;
                ImsRilConnector.this.cleanUp();
            }

            @Override // android.content.ServiceConnection
            public void onNullBinding(ComponentName name) {
                Log.d("ImsRilConnector", "onNullBinding componentName " + name);
                ImsRilConnector.this.unbindImsRilService();
                ImsRilConnector.this.mConnectionRetryHandler.sendEmptyMessageDelayed(1, 500L);
            }
        };
        if (context == null || listener == null || looper == null) {
            throw new Exception("context, listener and looper should not be null ");
        }
        this.mContext = context;
        this.mListener = listener;
        this.isTablet = OplusFeatureHelper.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_TABLET);
        this.isVmModemSupport = OplusFeature.OPLUS_FEATURE_RADIO_VIRTUALMODEM;
        this.mConnectionRetryHandler = new ConnectionRetryHandler(looper);
    }

    public void connect() {
        bindImsRilService();
    }

    public void disconnect() {
        this.mListener = null;
        unbindImsRilService();
        cleanUp();
    }

    /* loaded from: classes.dex */
    private class ConnectionRetryHandler extends Handler {
        public ConnectionRetryHandler() {
        }

        public ConnectionRetryHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ImsRilConnector.this.bindImsRilService();
                    return;
                case 2:
                    ImsRilConnector.this.bindImsRilServiceIfExist();
                    return;
                default:
                    return;
            }
        }
    }

    public void bindImsRilService() {
        if (SystemProperties.getBoolean("ro.build.tablet.with_modem", false) && SystemProperties.get("ro.carrier", OplusPropertyList.UNKNOWN).equals("wifi-only")) {
            return;
        }
        bindImsRilServiceIfExist();
    }

    public boolean connect(boolean needCallback) {
        this.mNeedCallback = needCallback;
        return bindImsRilServiceIfExist();
    }

    public boolean bindImsRilServiceIfExist() {
        int i;
        Intent intent = new Intent();
        boolean z = isQcomPlatform;
        if (z) {
            intent.setClassName(QCOM_IMS_PACKAGE_NAME, "org.codeaurora.ims.ImsRilService");
        } else {
            if (!isMTKPlatform) {
                return false;
            }
            intent.setClassName(MTK_IMS_PACKAGE_NAME, "com.mediatek.ims.ImsRilService");
        }
        if (z && this.isTablet && !this.isVmModemSupport) {
            Log.d("ImsRilConnector", "bindImsRilServiceIfExist:  Tablet with VmModem Not Supported");
            return false;
        }
        this.mBound = this.mContext.bindService(intent, this.mImsRilServiceConnection, 1);
        Log.d("ImsRilConnector", "Attempt to bind ImsRilService service returned with: " + this.mBound);
        if (!this.mBound && (i = this.mRetryCount) < 8) {
            this.mConnectionRetryHandler.sendEmptyMessageDelayed(2, (1 << i) * 500);
            this.mRetryCount++;
        }
        return this.mBound;
    }

    public void registerImsRilIndExternal(IImsRilIndListener cb) {
        synchronized (this.mIImsRilIndListeners) {
            if (!this.mIImsRilIndListeners.contains(cb)) {
                this.mIImsRilIndListeners.add(cb);
            }
        }
    }

    public void unregisterImsRilIndExternal(IImsRilIndListener cb) {
        synchronized (this.mIImsRilIndListeners) {
            if (this.mIImsRilIndListeners.contains(cb)) {
                this.mIImsRilIndListeners.remove(cb);
            }
        }
    }

    public boolean isBinderAlive() {
        return this.mIsBinderAvailable;
    }

    protected void unbindImsRilService() {
        Context context = this.mContext;
        if (context != null && this.mBound) {
            context.unbindService(this.mImsRilServiceConnection);
            this.mBound = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanUp() {
        this.mConnectionRetryHandler.removeMessages(1);
        this.mImsRil = null;
        ImsRilManager imsRilManager = this.mRilMgr;
        if (imsRilManager != null) {
            imsRilManager.dispose();
        }
    }
}
