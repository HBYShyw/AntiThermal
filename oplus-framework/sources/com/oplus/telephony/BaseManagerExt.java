package com.oplus.telephony;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.Rlog;
import java.util.NoSuchElementException;
import java.util.Objects;

/* loaded from: classes.dex */
public abstract class BaseManagerExt {
    private IBinder mBinder;
    protected Context mContext;
    protected final Object mLock = new Object();
    private final DeathRecipient mServiceDeath = new DeathRecipient();

    public BaseManagerExt(Context context) {
        Context appContext = context.getApplicationContext();
        if (appContext != null && Objects.equals(context.getAttributionTag(), appContext.getAttributionTag())) {
            this.mContext = appContext;
        } else {
            this.mContext = context;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setIBinder(IBinder iBinder) {
        synchronized (this.mLock) {
            this.mBinder = iBinder;
            if (iBinder != null) {
                try {
                    iBinder.linkToDeath(this.mServiceDeath, 0);
                } catch (RemoteException e) {
                    Rlog.e(getClass().getSimpleName(), "linkToDeath NoSuchElementException.");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isServiceConnected() {
        boolean isConnected;
        synchronized (this.mLock) {
            isConnected = this.mBinder != null;
            if (!isConnected) {
                Rlog.e(getClass().getSimpleName(), "binder not Connected.");
            }
        }
        return isConnected;
    }

    /* loaded from: classes.dex */
    private class DeathRecipient implements IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (BaseManagerExt.this.mLock) {
                if (BaseManagerExt.this.mBinder != null) {
                    try {
                        BaseManagerExt.this.mBinder.unlinkToDeath(BaseManagerExt.this.mServiceDeath, 0);
                    } catch (NoSuchElementException e) {
                        Rlog.e(getClass().getSimpleName(), "unlinkToDeath NoSuchElementException.");
                    }
                    BaseManagerExt.this.mBinder = null;
                }
            }
        }
    }
}
