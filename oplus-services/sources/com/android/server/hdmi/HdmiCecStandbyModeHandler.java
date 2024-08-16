package com.android.server.hdmi;

import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HdmiCecStandbyModeHandler {
    private final CecMessageHandler mAborterIncorrectMode;
    private final CecMessageHandler mAborterRefused;
    private final CecMessageHandler mAborterUnrecognizedOpcode;
    private final CecMessageHandler mAutoOnHandler;
    private final CecMessageHandler mBypasser;
    private final CecMessageHandler mBystander;
    private final SparseArray<CecMessageHandler> mCecMessageHandlers = new SparseArray<>();
    private final CecMessageHandler mDefaultHandler;
    private final HdmiCecLocalDevice mDevice;
    private final HdmiControlService mService;
    private final UserControlProcessedHandler mUserControlProcessedHandler;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface CecMessageHandler {
        boolean handle(HdmiCecMessage hdmiCecMessage);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class Bystander implements CecMessageHandler {
        @Override // com.android.server.hdmi.HdmiCecStandbyModeHandler.CecMessageHandler
        public boolean handle(HdmiCecMessage hdmiCecMessage) {
            return true;
        }

        private Bystander() {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class Bypasser implements CecMessageHandler {
        @Override // com.android.server.hdmi.HdmiCecStandbyModeHandler.CecMessageHandler
        public boolean handle(HdmiCecMessage hdmiCecMessage) {
            return false;
        }

        private Bypasser() {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class Aborter implements CecMessageHandler {
        private final int mReason;

        public Aborter(int i) {
            this.mReason = i;
        }

        @Override // com.android.server.hdmi.HdmiCecStandbyModeHandler.CecMessageHandler
        public boolean handle(HdmiCecMessage hdmiCecMessage) {
            HdmiCecStandbyModeHandler.this.mService.maySendFeatureAbortCommand(hdmiCecMessage, this.mReason);
            return true;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class AutoOnHandler implements CecMessageHandler {
        private AutoOnHandler() {
        }

        @Override // com.android.server.hdmi.HdmiCecStandbyModeHandler.CecMessageHandler
        public boolean handle(HdmiCecMessage hdmiCecMessage) {
            if (((HdmiCecLocalDeviceTv) HdmiCecStandbyModeHandler.this.mDevice).getAutoWakeup()) {
                return false;
            }
            HdmiCecStandbyModeHandler.this.mAborterRefused.handle(hdmiCecMessage);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class UserControlProcessedHandler implements CecMessageHandler {
        private UserControlProcessedHandler() {
        }

        @Override // com.android.server.hdmi.HdmiCecStandbyModeHandler.CecMessageHandler
        public boolean handle(HdmiCecMessage hdmiCecMessage) {
            if (HdmiCecLocalDevice.isPowerOnOrToggleCommand(hdmiCecMessage)) {
                return false;
            }
            if (HdmiCecLocalDevice.isPowerOffOrToggleCommand(hdmiCecMessage)) {
                return true;
            }
            return HdmiCecStandbyModeHandler.this.mAborterIncorrectMode.handle(hdmiCecMessage);
        }
    }

    private void addCommonHandlers() {
        addHandler(68, this.mUserControlProcessedHandler);
    }

    private void addTvHandlers() {
        addHandler(130, this.mBystander);
        addHandler(133, this.mBystander);
        addHandler(128, this.mBystander);
        addHandler(129, this.mBystander);
        addHandler(134, this.mBystander);
        addHandler(54, this.mBystander);
        addHandler(50, this.mBystander);
        addHandler(69, this.mBystander);
        addHandler(0, this.mBystander);
        addHandler(157, this.mBystander);
        addHandler(126, this.mBystander);
        addHandler(122, this.mBystander);
        addHandler(131, this.mBypasser);
        addHandler(HdmiCecKeycode.UI_BROADCAST_DIGITAL_COMMNICATIONS_SATELLITE_2, this.mBypasser);
        addHandler(132, this.mBypasser);
        addHandler(140, this.mBypasser);
        addHandler(70, this.mBypasser);
        addHandler(71, this.mBypasser);
        addHandler(135, this.mBypasser);
        addHandler(144, this.mBypasser);
        addHandler(165, this.mBypasser);
        addHandler(143, this.mBypasser);
        addHandler(255, this.mBypasser);
        addHandler(159, this.mBypasser);
        addHandler(160, this.mAborterIncorrectMode);
        addHandler(HdmiCecKeycode.CEC_KEYCODE_F2_RED, this.mAborterIncorrectMode);
        addHandler(4, this.mAutoOnHandler);
        addHandler(13, this.mAutoOnHandler);
        addHandler(10, this.mBystander);
        addHandler(15, this.mAborterIncorrectMode);
        addHandler(192, this.mAborterIncorrectMode);
        addHandler(197, this.mAborterIncorrectMode);
    }

    public HdmiCecStandbyModeHandler(HdmiControlService hdmiControlService, HdmiCecLocalDevice hdmiCecLocalDevice) {
        Aborter aborter = new Aborter(0);
        this.mAborterUnrecognizedOpcode = aborter;
        this.mAborterIncorrectMode = new Aborter(1);
        this.mAborterRefused = new Aborter(4);
        this.mAutoOnHandler = new AutoOnHandler();
        Bypasser bypasser = new Bypasser();
        this.mBypasser = bypasser;
        this.mBystander = new Bystander();
        this.mUserControlProcessedHandler = new UserControlProcessedHandler();
        this.mService = hdmiControlService;
        this.mDevice = hdmiCecLocalDevice;
        addCommonHandlers();
        if (hdmiCecLocalDevice.getType() == 0) {
            addTvHandlers();
            this.mDefaultHandler = aborter;
        } else {
            this.mDefaultHandler = bypasser;
        }
    }

    private void addHandler(int i, CecMessageHandler cecMessageHandler) {
        this.mCecMessageHandlers.put(i, cecMessageHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleCommand(HdmiCecMessage hdmiCecMessage) {
        CecMessageHandler cecMessageHandler = this.mCecMessageHandlers.get(hdmiCecMessage.getOpcode());
        if (cecMessageHandler != null) {
            return cecMessageHandler.handle(hdmiCecMessage);
        }
        return this.mDefaultHandler.handle(hdmiCecMessage);
    }
}
