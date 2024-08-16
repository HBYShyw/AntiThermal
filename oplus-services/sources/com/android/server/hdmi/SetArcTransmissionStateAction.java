package com.android.server.hdmi;

import android.util.Slog;
import com.android.server.hdmi.HdmiControlService;
import com.android.server.hdmi.RequestSadAction;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SetArcTransmissionStateAction extends HdmiCecFeatureAction {
    private static final int STATE_WAITING_TIMEOUT = 1;
    private static final String TAG = "SetArcTransmissionStateAction";
    private final int mAvrAddress;
    private final boolean mEnabled;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SetArcTransmissionStateAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, boolean z) {
        super(hdmiCecLocalDevice);
        HdmiUtils.verifyAddressType(getSourceAddress(), 0);
        HdmiUtils.verifyAddressType(i, 5);
        this.mAvrAddress = i;
        this.mEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        if (this.mEnabled) {
            addAndStartAction(new RequestSadAction(localDevice(), 5, new RequestSadAction.RequestSadCallback() { // from class: com.android.server.hdmi.SetArcTransmissionStateAction.1
                @Override // com.android.server.hdmi.RequestSadAction.RequestSadCallback
                public void onRequestSadDone(List<byte[]> list) {
                    Slog.i(SetArcTransmissionStateAction.TAG, "Enabling ARC");
                    SetArcTransmissionStateAction.this.tv().enableArc(list);
                    SetArcTransmissionStateAction setArcTransmissionStateAction = SetArcTransmissionStateAction.this;
                    setArcTransmissionStateAction.mState = 1;
                    setArcTransmissionStateAction.addTimer(1, 2000);
                    SetArcTransmissionStateAction.this.sendReportArcInitiated();
                }
            }));
            return true;
        }
        disableArc();
        finish();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendReportArcInitiated() {
        sendCommand(HdmiCecMessageBuilder.buildReportArcInitiated(getSourceAddress(), this.mAvrAddress), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SetArcTransmissionStateAction.2
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int i) {
                if (i != 1) {
                    return;
                }
                SetArcTransmissionStateAction.this.disableArc();
                HdmiLogger.debug("Failed to send <Report Arc Initiated>.", new Object[0]);
                SetArcTransmissionStateAction.this.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableArc() {
        Slog.i(TAG, "Disabling ARC");
        tv().disableArc();
        sendCommand(HdmiCecMessageBuilder.buildReportArcTerminated(getSourceAddress(), this.mAvrAddress));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState != 1 || hdmiCecMessage.getOpcode() != 0 || (hdmiCecMessage.getParams()[0] & 255) != 193) {
            return false;
        }
        HdmiLogger.debug("Feature aborted for <Report Arc Initiated>", new Object[0]);
        disableArc();
        finish();
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 == i && i2 == 1) {
            finish();
        }
    }
}
