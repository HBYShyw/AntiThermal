package com.oplus.telephony;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.oplus.telephony.ISubExt;

/* loaded from: classes.dex */
public class SubscriptionManagerExt extends BaseManagerExt {
    public static final String ISUB_EXT = "isub_ext";
    private static final String TAG = "SubscriptionManagerExt";
    private ISubExt mISubExt;

    public SubscriptionManagerExt(Context context) {
        super(context);
    }

    public static SubscriptionManagerExt from(Context context) {
        return (SubscriptionManagerExt) context.getSystemService(ISUB_EXT);
    }

    private ISubExt getISubExt() {
        ISubExt temp;
        ISubExt temp2;
        synchronized (this.mLock) {
            if (!isServiceConnected() && (temp2 = ISubExt.Stub.asInterface(ServiceManager.getService(ISUB_EXT))) != null) {
                try {
                    this.mISubExt = temp2;
                    setIBinder(temp2.asBinder());
                } catch (Exception e) {
                    this.mISubExt = null;
                }
            }
            temp = this.mISubExt;
        }
        return temp;
    }

    public String getIsimImpiForSubscriber(int subId) {
        try {
            ISubExt isubExt = getISubExt();
            if (isubExt == null) {
                return null;
            }
            return isubExt.getIsimImpiForSubscriber(subId);
        } catch (RemoteException e) {
            return null;
        }
    }
}
