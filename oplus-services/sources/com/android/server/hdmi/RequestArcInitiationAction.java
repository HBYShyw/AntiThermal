package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import com.android.server.hdmi.HdmiControlService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class RequestArcInitiationAction extends RequestArcAction {
    private static final String TAG = "RequestArcInitiationAction";

    RequestArcInitiationAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i) {
        super(hdmiCecLocalDevice, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestArcInitiationAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, IHdmiControlCallback iHdmiControlCallback) {
        super(hdmiCecLocalDevice, i, iHdmiControlCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        this.mState = 1;
        addTimer(1, 2000);
        sendCommand(HdmiCecMessageBuilder.buildRequestArcInitiation(getSourceAddress(), this.mAvrAddress), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.RequestArcInitiationAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int i) {
                if (i != 0) {
                    RequestArcInitiationAction.this.tv().disableArc();
                    RequestArcInitiationAction.this.finishWithCallback(3);
                }
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState == 1 && HdmiUtils.checkCommandSource(hdmiCecMessage, this.mAvrAddress, TAG)) {
            int opcode = hdmiCecMessage.getOpcode();
            if (opcode != 0) {
                if (opcode != 192) {
                    return false;
                }
                finishWithCallback(0);
                return false;
            }
            if ((hdmiCecMessage.getParams()[0] & 255) == 195) {
                tv().disableArc();
                finishWithCallback(3);
                return true;
            }
        }
        return false;
    }
}
