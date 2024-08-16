package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import com.android.server.hdmi.HdmiControlService;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class SystemAudioAction extends HdmiCecFeatureAction {
    private static final int MAX_SEND_RETRY_COUNT = 2;
    private static final int OFF_TIMEOUT_MS = 2000;
    private static final int ON_TIMEOUT_MS = 5000;
    private static final int STATE_CHECK_ROUTING_IN_PRGRESS = 1;
    private static final int STATE_WAIT_FOR_SET_SYSTEM_AUDIO_MODE = 2;
    private static final String TAG = "SystemAudioAction";
    protected final int mAvrLogicalAddress;
    private int mSendRetryCount;
    protected boolean mTargetAudioStatus;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemAudioAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, boolean z, IHdmiControlCallback iHdmiControlCallback) {
        super(hdmiCecLocalDevice, iHdmiControlCallback);
        this.mSendRetryCount = 0;
        HdmiUtils.verifyAddressType(i, 5);
        this.mAvrLogicalAddress = i;
        this.mTargetAudioStatus = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendSystemAudioModeRequest() {
        List actions = getActions(RoutingControlAction.class);
        if (!actions.isEmpty()) {
            this.mState = 1;
            ((RoutingControlAction) actions.get(0)).addOnFinishedCallback(this, new Runnable() { // from class: com.android.server.hdmi.SystemAudioAction.1
                @Override // java.lang.Runnable
                public void run() {
                    SystemAudioAction.this.sendSystemAudioModeRequestInternal();
                }
            });
        } else {
            sendSystemAudioModeRequestInternal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSystemAudioModeRequestInternal() {
        sendCommand(HdmiCecMessageBuilder.buildSystemAudioModeRequest(getSourceAddress(), this.mAvrLogicalAddress, getSystemAudioModeRequestParam(), this.mTargetAudioStatus), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SystemAudioAction.2
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int i) {
                if (i != 0) {
                    HdmiLogger.debug("Failed to send <System Audio Mode Request>:" + i, new Object[0]);
                    SystemAudioAction.this.setSystemAudioMode(false);
                    SystemAudioAction.this.finishWithCallback(7);
                }
            }
        });
        this.mState = 2;
        addTimer(2, this.mTargetAudioStatus ? 5000 : 2000);
    }

    private int getSystemAudioModeRequestParam() {
        if (tv().getActiveSource().isValid()) {
            return tv().getActiveSource().physicalAddress;
        }
        int activePath = tv().getActivePath();
        if (activePath != 65535) {
            return activePath;
        }
        return 0;
    }

    private void handleSendSystemAudioModeRequestTimeout() {
        if (this.mTargetAudioStatus) {
            int i = this.mSendRetryCount;
            this.mSendRetryCount = i + 1;
            if (i < 2) {
                sendSystemAudioModeRequest();
                return;
            }
        }
        HdmiLogger.debug("[T]:wait for <Set System Audio Mode>.", new Object[0]);
        setSystemAudioMode(false);
        finishWithCallback(1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSystemAudioMode(boolean z) {
        tv().setSystemAudioMode(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public final boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (hdmiCecMessage.getSource() != this.mAvrLogicalAddress || this.mState != 2) {
            return false;
        }
        if (hdmiCecMessage.getOpcode() == 0 && (hdmiCecMessage.getParams()[0] & 255) == 112) {
            HdmiLogger.debug("Failed to start system audio mode request.", new Object[0]);
            setSystemAudioMode(false);
            finishWithCallback(5);
            return true;
        }
        if (hdmiCecMessage.getOpcode() == 114 && HdmiUtils.checkCommandSource(hdmiCecMessage, this.mAvrLogicalAddress, TAG)) {
            boolean parseCommandParamSystemAudioStatus = HdmiUtils.parseCommandParamSystemAudioStatus(hdmiCecMessage);
            if (parseCommandParamSystemAudioStatus == this.mTargetAudioStatus) {
                setSystemAudioMode(parseCommandParamSystemAudioStatus);
                finish();
                return true;
            }
            HdmiLogger.debug("Unexpected system audio mode request:" + parseCommandParamSystemAudioStatus, new Object[0]);
            finishWithCallback(5);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeSystemAudioActionInProgress() {
        removeActionExcept(SystemAudioActionFromTv.class, this);
        removeActionExcept(SystemAudioActionFromAvr.class, this);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    final void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 == i && i2 == 2) {
            handleSendSystemAudioModeRequestTimeout();
        }
    }
}
