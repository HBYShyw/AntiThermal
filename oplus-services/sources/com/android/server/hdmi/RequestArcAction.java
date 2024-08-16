package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
abstract class RequestArcAction extends HdmiCecFeatureAction {
    protected static final int STATE_WATING_FOR_REQUEST_ARC_REQUEST_RESPONSE = 1;
    private static final String TAG = "RequestArcAction";
    protected final int mAvrAddress;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestArcAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, IHdmiControlCallback iHdmiControlCallback) {
        super(hdmiCecLocalDevice, iHdmiControlCallback);
        HdmiUtils.verifyAddressType(getSourceAddress(), 0);
        HdmiUtils.verifyAddressType(i, 5);
        this.mAvrAddress = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestArcAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i) {
        this(hdmiCecLocalDevice, i, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void disableArcTransmission() {
        addAndStartAction(new SetArcTransmissionStateAction(localDevice(), this.mAvrAddress, false));
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    final void handleTimerEvent(int i) {
        if (this.mState == i && i == 1) {
            HdmiLogger.debug("[T] RequestArcAction.", new Object[0]);
            disableArcTransmission();
            finishWithCallback(1);
        }
    }
}
