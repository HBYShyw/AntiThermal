package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import com.android.server.hdmi.HdmiControlService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ArcInitiationActionFromAvr extends HdmiCecFeatureAction {
    private static final int STATE_ARC_INITIATED = 2;
    private static final int STATE_WAITING_FOR_INITIATE_ARC_RESPONSE = 1;
    private static final int TIMEOUT_MS = 1000;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArcInitiationActionFromAvr(HdmiCecLocalDevice hdmiCecLocalDevice) {
        super(hdmiCecLocalDevice);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        audioSystem().setArcStatus(true);
        this.mState = 1;
        addTimer(1, 1000);
        sendInitiateArc();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState != 1) {
            return false;
        }
        int opcode = hdmiCecMessage.getOpcode();
        if (opcode == 0) {
            if ((hdmiCecMessage.getParams()[0] & 255) != 192) {
                return false;
            }
            audioSystem().setArcStatus(false);
            finish();
            return true;
        }
        if (opcode == 193) {
            this.mState = 2;
            finish();
            return true;
        }
        if (opcode != 194) {
            return false;
        }
        audioSystem().setArcStatus(false);
        finish();
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 == i && i2 == 1) {
            handleInitiateArcTimeout();
        }
    }

    protected void sendInitiateArc() {
        sendCommand(HdmiCecMessageBuilder.buildInitiateArc(getSourceAddress(), 0), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.ArcInitiationActionFromAvr$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                ArcInitiationActionFromAvr.this.lambda$sendInitiateArc$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendInitiateArc$0(int i) {
        if (i != 0) {
            audioSystem().setArcStatus(false);
            finish();
        }
    }

    private void handleInitiateArcTimeout() {
        HdmiLogger.debug("handleInitiateArcTimeout", new Object[0]);
        finish();
    }
}
