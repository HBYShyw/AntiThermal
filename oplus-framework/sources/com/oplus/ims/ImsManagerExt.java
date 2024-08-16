package com.oplus.ims;

import android.content.Context;
import android.telephony.ims.ImsRilConnector;
import android.telephony.ims.ImsRilManager;
import android.util.Log;
import com.oplus.telephony.BaseManagerExt;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ImsManagerExt extends BaseManagerExt {
    public static final String IIMS_EXT = "iims_ext";
    private static final String TAG = "ImsManagerExt";
    private static ImsManagerExt sInstance;
    private ImsRilConnector.IListener mConnectorListener;
    private IImsExt mIImsExt;
    private ImsRilConnector mImsRilConnector;
    private ImsRilManager mImsRilManager;
    private boolean mIsBinderAvailable;
    private boolean mIsPendingConnect;
    private ArrayList<IListenerExternal> mListenerExternalList;

    /* loaded from: classes.dex */
    public interface IListenerExternal {
        void onConnectionAvailable();

        void onConnectionUnavailable();
    }

    public ImsManagerExt(Context context) {
        super(context);
        this.mListenerExternalList = new ArrayList<>();
        this.mIsBinderAvailable = false;
        this.mIsPendingConnect = false;
    }

    public static ImsManagerExt from(Context context) {
        return (ImsManagerExt) context.getSystemService(IIMS_EXT);
    }

    public static ImsManagerExt getInstance(Context context) {
        ImsManagerExt imsManagerExt;
        synchronized (ImsManagerExt.class) {
            if (sInstance == null) {
                sInstance = new ImsManagerExt(context);
            }
            imsManagerExt = sInstance;
        }
        return imsManagerExt;
    }

    /* loaded from: classes.dex */
    private class ImsConnectorListener implements ImsRilConnector.IListener {
        private ImsConnectorListener() {
        }

        @Override // android.telephony.ims.ImsRilConnector.IListener
        public void onConnectionAvailable(ImsRilManager mgr) {
            ImsManagerExt.this.mImsRilManager = mgr;
            ImsManagerExt.this.mIsBinderAvailable = true;
            ImsManagerExt.this.mIsPendingConnect = false;
            ImsManagerExt.this.notifyConnectionAvailable();
        }

        @Override // android.telephony.ims.ImsRilConnector.IListener
        public void onConnectionUnavailable() {
            ImsManagerExt.this.mIsBinderAvailable = false;
            ImsManagerExt.this.mIsPendingConnect = false;
            ImsManagerExt.this.mImsRilManager = null;
            ImsManagerExt.this.notifyConnectionUnavailable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnectionAvailable() {
        synchronized (this.mLock) {
            Iterator<IListenerExternal> it = this.mListenerExternalList.iterator();
            while (it.hasNext()) {
                IListenerExternal iListenerExternal = it.next();
                iListenerExternal.onConnectionAvailable();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnectionUnavailable() {
        synchronized (this.mLock) {
            Iterator<IListenerExternal> it = this.mListenerExternalList.iterator();
            while (it.hasNext()) {
                IListenerExternal iListenerExternal = it.next();
                iListenerExternal.onConnectionUnavailable();
            }
        }
    }

    public void connect() {
        ImsRilConnector imsRilConnector;
        synchronized (this.mLock) {
            if (this.mConnectorListener == null) {
                this.mConnectorListener = new ImsConnectorListener();
            }
            if (this.mImsRilConnector == null) {
                try {
                    this.mImsRilConnector = new ImsRilConnector(this.mContext, this.mConnectorListener);
                } catch (Exception e) {
                    Log.d(TAG, "connect imsext failed: " + e.getMessage());
                }
            }
            if (!this.mIsBinderAvailable && !this.mIsPendingConnect && (imsRilConnector = this.mImsRilConnector) != null) {
                imsRilConnector.connect(false);
                this.mIsPendingConnect = true;
            }
        }
    }

    public void disconnect() {
        ImsRilConnector imsRilConnector = this.mImsRilConnector;
        if (imsRilConnector != null) {
            imsRilConnector.disconnect();
        }
    }

    public boolean isBinderAlive() {
        return this.mIsBinderAvailable;
    }

    public void registerListenerExternal(IListenerExternal listenerExternal) {
        synchronized (this.mLock) {
            if (!this.mListenerExternalList.contains(listenerExternal)) {
                this.mListenerExternalList.add(listenerExternal);
                if (isBinderAlive()) {
                    listenerExternal.onConnectionAvailable();
                }
            }
        }
    }

    public void unregisterListenerExternal(IListenerExternal listenerExternal) {
        synchronized (this.mLock) {
            if (this.mListenerExternalList.contains(listenerExternal)) {
                this.mListenerExternalList.remove(listenerExternal);
                if (!isBinderAlive()) {
                    listenerExternal.onConnectionUnavailable();
                }
            }
        }
    }

    private IImsExt getIImsExt() {
        IImsExt temp;
        IImsExt temp2;
        synchronized (this.mLock) {
            if (!isServiceConnected()) {
                this.mIImsExt = null;
                ImsRilManager imsRilManager = this.mImsRilManager;
                if (imsRilManager != null && (temp2 = imsRilManager.getImsExt()) != null) {
                    try {
                        this.mIImsExt = temp2;
                        setIBinder(temp2.asBinder());
                    } catch (Exception e) {
                        Log.e(TAG, "getIImsExt failed");
                    }
                }
            }
            temp = this.mIImsExt;
        }
        return temp;
    }
}
