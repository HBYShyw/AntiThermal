package android.telephony;

import android.os.Looper;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusPhoneStateListener extends PhoneStateListener {
    public static final int LISTEN_SRVCC_STATE_CHANGED = 16384;
    private static final String TAG = "ColorPhoneStateListener";

    public OplusPhoneStateListener(Looper looper) {
        super(looper);
    }

    public void onSrvccStateChanged(int srvccState) {
        super.onSrvccStateChanged(srvccState);
    }

    @Override // android.telephony.PhoneStateListener
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
    }

    public void setSubId(int subId) {
        try {
            this.mSubId = Integer.valueOf(subId);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
        }
    }

    public int getSubId() {
        try {
            return this.mSubId.intValue();
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return -1;
        }
    }
}
