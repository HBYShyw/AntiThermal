package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import com.android.server.hdmi.HdmiControlService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ArcTerminationActionFromAvr extends HdmiCecFeatureAction {
    private static final int STATE_ARC_TERMINATED = 2;
    private static final int STATE_WAITING_FOR_INITIATE_ARC_RESPONSE = 1;
    public static final int TIMEOUT_MS = 1000;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArcTerminationActionFromAvr(HdmiCecLocalDevice hdmiCecLocalDevice) {
        super(hdmiCecLocalDevice);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArcTerminationActionFromAvr(HdmiCecLocalDevice hdmiCecLocalDevice, IHdmiControlCallback iHdmiControlCallback) {
        super(hdmiCecLocalDevice, iHdmiControlCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        this.mState = 1;
        addTimer(1, 1000);
        sendTerminateArc();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState != 1) {
            return false;
        }
        int opcode = hdmiCecMessage.getOpcode();
        if (opcode != 0) {
            if (opcode != 194) {
                return false;
            }
            this.mState = 2;
            audioSystem().processArcTermination();
            finishWithCallback(0);
            return true;
        }
        if ((hdmiCecMessage.getParams()[0] & 255) != 197) {
            return false;
        }
        this.mState = 2;
        audioSystem().processArcTermination();
        finishWithCallback(3);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 == i && i2 == 1) {
            handleTerminateArcTimeout();
        }
    }

    protected void sendTerminateArc() {
        sendCommand(HdmiCecMessageBuilder.buildTerminateArc(getSourceAddress(), 0), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.ArcTerminationActionFromAvr$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                ArcTerminationActionFromAvr.this.lambda$sendTerminateArc$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendTerminateArc$0(int i) {
        if (i != 0) {
            if (i == 1) {
                audioSystem().setArcStatus(false);
            }
            HdmiLogger.debug("Terminate ARC was not successfully sent.", new Object[0]);
            finishWithCallback(3);
        }
    }

    private void handleTerminateArcTimeout() {
        audioSystem().setArcStatus(false);
        HdmiLogger.debug("handleTerminateArcTimeout", new Object[0]);
        finishWithCallback(1);
    }
}
