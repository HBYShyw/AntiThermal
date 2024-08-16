package com.oplus.internal.telephony;

import android.os.Handler;
import android.telephony.Rlog;
import com.android.internal.telephony.IOplusGsmCdmaPhone;
import com.android.internal.telephony.OplusTelephonyFactory;
import com.android.internal.telephony.Phone;

/* loaded from: classes.dex */
public class GsmCdmaPhoneExt {
    private static final String TAG = "GsmCdmaPhoneExt";
    private IOplusGsmCdmaPhone mImpl;
    private Phone mPhone;

    public GsmCdmaPhoneExt(Phone phone) {
        this.mPhone = phone;
        if (phone != null) {
            this.mImpl = OplusTelephonyFactory.getFeatureFromCache(phone.getPhoneId(), IOplusGsmCdmaPhone.DEFAULT);
        } else {
            loge("fatal error phone is null");
        }
    }

    public void registerForCrssSuppServiceNotification(Handler h, int what, Object obj) {
        IOplusGsmCdmaPhone iOplusGsmCdmaPhone = this.mImpl;
        if (iOplusGsmCdmaPhone != null) {
            iOplusGsmCdmaPhone.registerForCrssSuppServiceNotification(h, what, obj);
        } else {
            loge("fatal error phone is null");
        }
    }

    public void unregisterForCrssSuppServiceNotification(Handler h) {
        IOplusGsmCdmaPhone iOplusGsmCdmaPhone = this.mImpl;
        if (iOplusGsmCdmaPhone != null) {
            iOplusGsmCdmaPhone.unregisterForCrssSuppServiceNotification(h);
        } else {
            loge("fatal error phone is null");
        }
    }

    private static void logd(String msg) {
        Rlog.d(TAG, msg);
    }

    private static void loge(String msg) {
        Rlog.e(TAG, msg);
    }
}
