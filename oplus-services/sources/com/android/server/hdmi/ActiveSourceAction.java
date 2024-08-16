package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ActiveSourceAction extends HdmiCecFeatureAction {
    private static final int STATE_FINISHED = 2;
    private static final int STATE_STARTED = 1;
    private final int mDestination;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActiveSourceAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i) {
        super(hdmiCecLocalDevice);
        this.mDestination = i;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        int sourceAddress = getSourceAddress();
        int sourcePath = getSourcePath();
        sendCommand(HdmiCecMessageBuilder.buildActiveSource(sourceAddress, sourcePath));
        if (source().getType() == 4) {
            sendCommand(HdmiCecMessageBuilder.buildReportMenuStatus(sourceAddress, this.mDestination, 0));
        }
        source().setActiveSource(sourceAddress, sourcePath, "ActiveSourceAction");
        this.mState = 2;
        finish();
        return true;
    }
}
