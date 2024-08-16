package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.hdmi.HdmiCecLocalDevice;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class ActiveSourceHandler {
    private static final String TAG = "ActiveSourceHandler";
    private final IHdmiControlCallback mCallback;
    private final HdmiControlService mService;
    private final HdmiCecLocalDeviceTv mSource;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActiveSourceHandler create(HdmiCecLocalDeviceTv hdmiCecLocalDeviceTv, IHdmiControlCallback iHdmiControlCallback) {
        if (hdmiCecLocalDeviceTv == null) {
            Slog.e(TAG, "Wrong arguments");
            return null;
        }
        return new ActiveSourceHandler(hdmiCecLocalDeviceTv, iHdmiControlCallback);
    }

    private ActiveSourceHandler(HdmiCecLocalDeviceTv hdmiCecLocalDeviceTv, IHdmiControlCallback iHdmiControlCallback) {
        this.mSource = hdmiCecLocalDeviceTv;
        this.mService = hdmiCecLocalDeviceTv.getService();
        this.mCallback = iHdmiControlCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void process(HdmiCecLocalDevice.ActiveSource activeSource, int i) {
        HdmiCecLocalDeviceTv hdmiCecLocalDeviceTv = this.mSource;
        if (this.mService.getDeviceInfo(activeSource.logicalAddress) == null) {
            hdmiCecLocalDeviceTv.startNewDeviceAction(activeSource, i);
        }
        if (!hdmiCecLocalDeviceTv.isProhibitMode()) {
            HdmiCecLocalDevice.ActiveSource of = HdmiCecLocalDevice.ActiveSource.of(hdmiCecLocalDeviceTv.getActiveSource());
            hdmiCecLocalDeviceTv.updateActiveSource(activeSource, TAG);
            boolean z = this.mCallback == null;
            if (!of.equals(activeSource)) {
                hdmiCecLocalDeviceTv.setPrevPortId(hdmiCecLocalDeviceTv.getActivePortId());
            }
            hdmiCecLocalDeviceTv.updateActiveInput(activeSource.physicalAddress, z);
            invokeCallback(0);
            return;
        }
        HdmiCecLocalDevice.ActiveSource activeSource2 = hdmiCecLocalDeviceTv.getActiveSource();
        if (activeSource2.logicalAddress == getSourceAddress()) {
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildActiveSource(activeSource2.logicalAddress, activeSource2.physicalAddress));
            hdmiCecLocalDeviceTv.updateActiveSource(activeSource2, TAG);
            invokeCallback(0);
            return;
        }
        hdmiCecLocalDeviceTv.startRoutingControl(activeSource.physicalAddress, activeSource2.physicalAddress, this.mCallback);
    }

    private final int getSourceAddress() {
        return this.mSource.getDeviceInfo().getLogicalAddress();
    }

    private void invokeCallback(int i) {
        IHdmiControlCallback iHdmiControlCallback = this.mCallback;
        if (iHdmiControlCallback == null) {
            return;
        }
        try {
            iHdmiControlCallback.onComplete(i);
        } catch (RemoteException e) {
            Slog.e(TAG, "Callback failed:" + e);
        }
    }
}
