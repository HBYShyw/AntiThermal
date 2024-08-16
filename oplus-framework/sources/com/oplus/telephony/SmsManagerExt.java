package com.oplus.telephony;

import android.content.Context;
import android.os.ServiceManager;
import com.oplus.telephony.ISmsExt;

/* loaded from: classes.dex */
public class SmsManagerExt extends BaseManagerExt {
    public static final String ISMS_EXT = "isms_ext";
    private static final String TAG = "SmsManagerExt";
    private ISmsExt mISmsExt;

    public SmsManagerExt(Context context) {
        super(context);
    }

    public static SmsManagerExt from(Context context) {
        return (SmsManagerExt) context.getSystemService(ISMS_EXT);
    }

    private ISmsExt getISmsExt() {
        ISmsExt temp;
        ISmsExt temp2;
        synchronized (this.mLock) {
            if (!isServiceConnected() && (temp2 = ISmsExt.Stub.asInterface(ServiceManager.getService(ISMS_EXT))) != null) {
                try {
                    this.mISmsExt = temp2;
                    setIBinder(temp2.asBinder());
                } catch (Exception e) {
                    this.mISmsExt = null;
                }
            }
            temp = this.mISmsExt;
        }
        return temp;
    }
}
