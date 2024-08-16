package com.android.server.hdmi;

import com.android.server.hdmi.HdmiAnnotations;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HdmiCecPowerStatusController {
    private final HdmiControlService mHdmiControlService;
    private int mPowerStatus = 1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiCecPowerStatusController(HdmiControlService hdmiControlService) {
        this.mHdmiControlService = hdmiControlService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPowerStatus() {
        return this.mPowerStatus;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPowerStatusOn() {
        return this.mPowerStatus == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPowerStatusStandby() {
        return this.mPowerStatus == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPowerStatusTransientToOn() {
        return this.mPowerStatus == 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPowerStatusTransientToStandby() {
        return this.mPowerStatus == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void setPowerStatus(int i) {
        setPowerStatus(i, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void setPowerStatus(int i, boolean z) {
        if (i == this.mPowerStatus) {
            return;
        }
        this.mPowerStatus = i;
        if (!z || this.mHdmiControlService.getCecVersion() < 6) {
            return;
        }
        sendReportPowerStatus(this.mPowerStatus);
    }

    private void sendReportPowerStatus(int i) {
        Iterator<HdmiCecLocalDevice> it = this.mHdmiControlService.getAllCecLocalDevices().iterator();
        while (it.hasNext()) {
            this.mHdmiControlService.sendCecCommand(HdmiCecMessageBuilder.buildReportPowerStatus(it.next().getDeviceInfo().getLogicalAddress(), 15, i));
        }
    }
}
