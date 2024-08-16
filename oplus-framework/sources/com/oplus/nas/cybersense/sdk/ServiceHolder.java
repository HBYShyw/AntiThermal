package com.oplus.nas.cybersense.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.oplus.nas.cybersense.sdk.ICyberSense;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ServiceHolder {
    private static final int MAX_COUNT = 3;
    private static final int MSG_START_TO_QUERY_SERVICE = 100;
    private static final long QUERY_INTERNAL = 60000;
    private static final String TAG = ServiceHolder.class.getSimpleName();
    private Context mContext;
    private IBinder mRemote;
    private ArrayList mServiceDownList = new ArrayList();
    private ArrayList mServiceUpList = new ArrayList();
    private boolean mDeathRecipientRegister = false;
    private boolean mUpRecipientRegister = false;
    private boolean mSeviceReady = false;
    private int mListenCount = 0;
    private ICyberSense mDefault = new ICyberSense.Default();
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.nas.cybersense.sdk.ServiceHolder.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d(ServiceHolder.TAG, "service died");
            if (ServiceHolder.this.mRemote != null) {
                ServiceHolder.this.mRemote.unlinkToDeath(ServiceHolder.this.mDeathRecipient, 0);
                ServiceHolder.this.mRemote = null;
                synchronized (ServiceHolder.this.mServiceDownList) {
                    ServiceHolder.this.mDeathRecipientRegister = false;
                    for (int i = 0; i < ServiceHolder.this.mServiceDownList.size(); i++) {
                        Message msg = Message.obtain((Message) ServiceHolder.this.mServiceDownList.get(i));
                        if (msg != null) {
                            try {
                                msg.sendToTarget();
                            } catch (Exception e) {
                                Log.e(ServiceHolder.TAG, "death decipient err:" + e.getMessage());
                            }
                        }
                    }
                }
                synchronized (ServiceHolder.this.mServiceUpList) {
                    ServiceHolder.this.mSeviceReady = false;
                    ServiceHolder.this.listenServiceUp();
                }
            }
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.oplus.nas.cybersense.sdk.ServiceHolder.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d(ServiceHolder.TAG, "handle msg:" + message.what);
            switch (message.what) {
                case 100:
                    ServiceHolder.this.listenServiceUp();
                    return;
                default:
                    return;
            }
        }
    };

    public ServiceHolder(Context context) {
        this.mContext = context;
    }

    public ICyberSense getCyberSenseService() {
        if (this.mRemote == null) {
            this.mRemote = ServiceManager.getService(CyberSenseManager.SERVICE_NAME);
        }
        IBinder iBinder = this.mRemote;
        if (iBinder != null) {
            return ICyberSense.Stub.asInterface(iBinder);
        }
        Log.e(TAG, "getCyberSenseService null");
        return this.mDefault;
    }

    private void registerCyberSenseServiceDown() {
        synchronized (this.mServiceDownList) {
            if (!this.mDeathRecipientRegister && registerDeathRecipient()) {
                this.mDeathRecipientRegister = true;
            }
        }
    }

    public void registerCyberSenseServiceDownCallback(Handler handler, int what) {
        synchronized (this.mServiceDownList) {
            Iterator<Message> iterator = this.mServiceDownList.iterator();
            while (iterator.hasNext()) {
                Message msg = iterator.next();
                if (msg.getTarget() == handler) {
                    Log.d(TAG, "registerCyberSenseServiceDownCallback duplicate");
                    return;
                }
            }
            Message msg2 = handler.obtainMessage(what);
            this.mServiceDownList.add(msg2);
            registerCyberSenseServiceDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void listenServiceUp() {
        String str = TAG;
        Log.d(str, "listen service up " + this.mListenCount);
        this.mHandler.removeMessages(100);
        if (this.mSeviceReady) {
            return;
        }
        IBinder remote = ServiceManager.getService(CyberSenseManager.SERVICE_NAME);
        Log.d(str, "listen service up rlt=" + remote);
        if (remote == null) {
            int i = this.mListenCount + 1;
            this.mListenCount = i;
            if (i > 3) {
                this.mHandler.removeMessages(100);
                this.mListenCount = 0;
                return;
            } else {
                Message msg = new Message();
                msg.what = 100;
                this.mHandler.sendMessageDelayed(msg, QUERY_INTERNAL);
                return;
            }
        }
        synchronized (this.mServiceUpList) {
            this.mSeviceReady = true;
            this.mListenCount = 0;
            Log.d(str, "registerCyberSenseServiceUpCallback notify clients " + this.mServiceUpList);
            for (int i2 = 0; i2 < this.mServiceUpList.size(); i2++) {
                Message msg2 = Message.obtain((Message) this.mServiceUpList.get(i2));
                if (msg2 != null) {
                    try {
                        msg2.sendToTarget();
                    } catch (Exception e) {
                        Log.e(TAG, "service up err:" + e.getMessage());
                    }
                }
            }
        }
        registerCyberSenseServiceDown();
    }

    public void unregisterCyberSenseServiceDownCallback(Handler handler) {
        synchronized (this.mServiceDownList) {
            Iterator<Message> iterator = this.mServiceDownList.iterator();
            while (iterator.hasNext()) {
                Message msg = iterator.next();
                if (msg.getTarget() == handler) {
                    iterator.remove();
                }
            }
            if (this.mServiceDownList.size() == 0 && this.mDeathRecipientRegister) {
                unregisterDeathRecipient();
                this.mDeathRecipientRegister = false;
            }
        }
    }

    public void registerCyberSenseServiceUpCallback(Handler handler, int what) {
        synchronized (this.mServiceUpList) {
            if (handler == null) {
                return;
            }
            Iterator<Message> iterator = this.mServiceUpList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getTarget() == handler) {
                    Log.d(TAG, "registerCyberSenseServiceUpCallback duplicate");
                    return;
                }
            }
            Message msg = handler.obtainMessage(what);
            Log.d(TAG, "registerCyberSenseServiceUpCallback add client " + msg);
            this.mServiceUpList.add(msg);
            if (this.mSeviceReady) {
                try {
                    msg.sendToTarget();
                } catch (Exception e) {
                    Log.d(TAG, "registerCyberSenseServiceUpCallback err:" + e.getMessage());
                }
            } else if (!this.mUpRecipientRegister) {
                listenServiceUp();
                this.mUpRecipientRegister = true;
            }
        }
    }

    public void unregisterCyberSenseServiceUpCallback(Handler handler) {
        synchronized (this.mServiceUpList) {
            if (handler == null) {
                Log.d(TAG, "unregisterCyberSenseServiceUpCallback check params err");
                return;
            }
            Iterator<Message> iterator = this.mServiceUpList.iterator();
            while (iterator.hasNext()) {
                Message msg = iterator.next();
                if (msg.getTarget() == handler) {
                    iterator.remove();
                }
            }
            if (this.mServiceUpList.size() == 0 && this.mUpRecipientRegister) {
                this.mUpRecipientRegister = false;
            }
        }
    }

    private boolean registerDeathRecipient() {
        IBinder service = ServiceManager.getService(CyberSenseManager.SERVICE_NAME);
        this.mRemote = service;
        if (service != null) {
            try {
                service.linkToDeath(this.mDeathRecipient, 0);
                return true;
            } catch (RemoteException e) {
                Log.d(TAG, "registerDeathRecipient err:" + e.getMessage());
            }
        }
        return false;
    }

    private void unregisterDeathRecipient() {
        IBinder service = ServiceManager.getService(CyberSenseManager.SERVICE_NAME);
        this.mRemote = service;
        if (service != null) {
            try {
                service.unlinkToDeath(this.mDeathRecipient, 0);
            } catch (Exception e) {
                Log.d(TAG, "unregisterDeathRecipient err:" + e.getMessage());
            }
        }
    }
}
