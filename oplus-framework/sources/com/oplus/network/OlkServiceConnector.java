package com.oplus.network;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.network.IOlk;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OlkServiceConnector {
    private static final long EXIT_WAIT = 10000;
    private static final long INIT_WAIT = 3000;
    private static final String TAG = "OlkServiceConnector";
    private static volatile OlkServiceConnector sInstance = null;
    private Context mContext;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private ArrayList<ConnectorListener> mListeners = new ArrayList<>();
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.network.OlkServiceConnector$$ExternalSyntheticLambda0
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            OlkServiceConnector.this.lambda$new$0();
        }
    };
    private Runnable mExitRunnable = new Runnable() { // from class: com.oplus.network.OlkServiceConnector.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (OlkServiceConnector.class) {
                OlkServiceConnector.this.mHandler.removeCallbacksAndMessages(null);
                OlkServiceConnector.this.mHandlerThread.quitSafely();
                OlkServiceConnector.this.mHandlerThread = null;
                OlkServiceConnector.this.mHandler = null;
            }
        }
    };
    private Runnable mConnectorRunnable = new Runnable() { // from class: com.oplus.network.OlkServiceConnector.2
        @Override // java.lang.Runnable
        public void run() {
            if (OlkServiceConnector.this.mHandler == null) {
                Log.e(OlkServiceConnector.TAG, "mHandler exited unexpectedly!");
                return;
            }
            OlkServiceConnector.this.mHandler.removeCallbacks(OlkServiceConnector.this.mConnectorRunnable);
            IBinder binder = ServiceManager.waitForService(OlkManager.SRV_NAME);
            if (binder == null) {
                Log.e(OlkServiceConnector.TAG, "get OlkService is null, need retry");
                OlkServiceConnector.this.mHandler.postDelayed(OlkServiceConnector.this.mConnectorRunnable, OlkServiceConnector.INIT_WAIT);
                return;
            }
            try {
                binder.linkToDeath(OlkServiceConnector.this.mDeathRecipient, 0);
            } catch (RemoteException e) {
                Log.e(OlkServiceConnector.TAG, "linkToDeath exception:" + e.getMessage());
            }
            synchronized (OlkServiceConnector.class) {
                Log.i(OlkServiceConnector.TAG, "OlkService connected!");
                IOlk service = IOlk.Stub.asInterface(binder);
                OlkServiceConnector.this.notifyConnected(service);
                OlkServiceConnector.this.mHandler.postDelayed(OlkServiceConnector.this.mExitRunnable, OlkServiceConnector.EXIT_WAIT);
            }
        }
    };

    /* loaded from: classes.dex */
    public interface ConnectorListener {
        void onServiceConnected(IOlk iOlk);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        Log.e(TAG, "binderDied, retry");
        tryConnectInternal();
    }

    private OlkServiceConnector(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static OlkServiceConnector create(Context context) {
        OlkServiceConnector olkServiceConnector;
        synchronized (OlkServiceConnector.class) {
            if (sInstance == null) {
                sInstance = new OlkServiceConnector(context);
            }
            olkServiceConnector = sInstance;
        }
        return olkServiceConnector;
    }

    public void connect(ConnectorListener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
        IBinder binder = ServiceManager.getService(OlkManager.SRV_NAME);
        if (binder == null) {
            tryConnectInternal();
        } else {
            IOlk service = IOlk.Stub.asInterface(binder);
            notifyConnected(service);
        }
    }

    private void tryConnectInternal() {
        Log.i(TAG, "tryConnectInternal...");
        synchronized (OlkServiceConnector.class) {
            if (this.mHandlerThread == null) {
                HandlerThread handlerThread = new HandlerThread("olk-connector");
                this.mHandlerThread = handlerThread;
                handlerThread.start();
                this.mHandler = new Handler(this.mHandlerThread.getLooper());
            }
            this.mHandler.removeCallbacks(this.mExitRunnable);
            if (!this.mHandler.hasCallbacks(this.mConnectorRunnable)) {
                this.mHandler.postDelayed(this.mConnectorRunnable, INIT_WAIT);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnected(IOlk service) {
        if (!this.mListeners.isEmpty()) {
            Iterator<ConnectorListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                ConnectorListener listener = it.next();
                listener.onServiceConnected(service);
            }
        }
    }
}
