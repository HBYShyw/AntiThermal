package com.oplus.internal.telephony;

import android.os.Bundle;
import android.os.IBinder;
import com.android.internal.telephony.IOplusTelephonyExtCallback;

/* loaded from: classes.dex */
public class OplusTelephonyExtCallbackBase {
    private OplusTelephonyExtCallbackStub mBinder = new OplusTelephonyExtCallbackStub();

    public IBinder getBinder() {
        return this.mBinder;
    }

    /* loaded from: classes.dex */
    private class OplusTelephonyExtCallbackStub extends IOplusTelephonyExtCallback.Stub {
        private OplusTelephonyExtCallbackStub() {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyExtCallback
        public void onTelephonyEventReport(int slotId, int eventId, Bundle data) {
            OplusTelephonyExtCallbackBase.this.onTelephonyEventReport(slotId, eventId, data);
        }
    }

    public void onTelephonyEventReport(int slotId, int eventId, Bundle data) {
    }
}
