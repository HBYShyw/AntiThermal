package com.android.server.hdmi;

import android.util.IndentingPrintWriter;
import com.android.internal.annotations.GuardedBy;
import com.android.server.hdmi.Constants;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class HdmiEarcLocalDevice extends HdmiLocalDevice {
    private static final String TAG = "HdmiEarcLocalDevice";

    @Constants.EarcStatus
    @GuardedBy({"mLock"})
    protected int mEarcStatus;

    /* JADX INFO: Access modifiers changed from: protected */
    public void disableDevice() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void handleEarcCapabilitiesReported(byte[] bArr);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void handleEarcStateChange(@Constants.EarcStatus int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public HdmiEarcLocalDevice(HdmiControlService hdmiControlService, int i) {
        super(hdmiControlService, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiEarcLocalDevice create(HdmiControlService hdmiControlService, int i) {
        if (i != 0) {
            return null;
        }
        return new HdmiEarcLocalDeviceTx(hdmiControlService);
    }
}
