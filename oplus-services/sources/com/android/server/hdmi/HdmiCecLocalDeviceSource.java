package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import android.sysprop.HdmiProperties;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.hdmi.Constants;
import com.android.server.hdmi.HdmiAnnotations;
import com.android.server.hdmi.HdmiCecLocalDevice;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class HdmiCecLocalDeviceSource extends HdmiCecLocalDevice {
    private static final String TAG = "HdmiCecLocalDeviceSource";
    protected boolean mIsSwitchDevice;

    @GuardedBy({"mLock"})
    @Constants.LocalActivePort
    protected int mLocalActivePort;

    @GuardedBy({"mLock"})
    protected boolean mRoutingControlFeatureEnabled;

    @GuardedBy({"mLock"})
    @Constants.LocalActivePort
    private int mRoutingPort;

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @Constants.RcProfile
    protected int getRcProfile() {
        return 1;
    }

    protected void handleRoutingChangeAndInformation(int i, HdmiCecMessage hdmiCecMessage) {
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected void onActiveSourceLost() {
    }

    protected void switchInputOnReceivingNewActivePath(int i) {
    }

    protected void updateDevicePowerStatus(int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HdmiCecLocalDeviceSource(HdmiControlService hdmiControlService, int i) {
        super(hdmiControlService, i);
        this.mIsSwitchDevice = ((Boolean) HdmiProperties.is_switch().orElse(Boolean.FALSE)).booleanValue();
        this.mRoutingPort = 0;
        this.mLocalActivePort = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void queryDisplayStatus(IHdmiControlCallback iHdmiControlCallback) {
        assertRunOnServiceThread();
        List actions = getActions(DevicePowerStatusAction.class);
        if (!actions.isEmpty()) {
            Slog.i(TAG, "queryDisplayStatus already in progress");
            ((DevicePowerStatusAction) actions.get(0)).addCallback(iHdmiControlCallback);
            return;
        }
        DevicePowerStatusAction create = DevicePowerStatusAction.create(this, 0, iHdmiControlCallback);
        if (create == null) {
            Slog.w(TAG, "Cannot initiate queryDisplayStatus");
            invokeCallback(iHdmiControlCallback, -1);
        } else {
            addAndStartAction(create);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    public void onHotplug(int i, boolean z) {
        assertRunOnServiceThread();
        if (this.mService.getPortInfo(i).getType() == 1) {
            this.mCecMessageCache.flushAll();
        }
        if (z) {
            this.mService.wakeUp();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    public void sendStandby(int i) {
        assertRunOnServiceThread();
        String stringValue = this.mService.getHdmiCecConfig().getStringValue("power_control_mode");
        if (stringValue.equals("broadcast")) {
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 15));
            return;
        }
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 0));
        if (stringValue.equals("to_tv_and_audio_system")) {
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 5));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void oneTouchPlay(IHdmiControlCallback iHdmiControlCallback) {
        assertRunOnServiceThread();
        List actions = getActions(OneTouchPlayAction.class);
        if (!actions.isEmpty()) {
            Slog.i(TAG, "oneTouchPlay already in progress");
            ((OneTouchPlayAction) actions.get(0)).addCallback(iHdmiControlCallback);
            return;
        }
        OneTouchPlayAction create = OneTouchPlayAction.create(this, 0, iHdmiControlCallback);
        if (create == null) {
            Slog.w(TAG, "Cannot initiate oneTouchPlay");
            invokeCallback(iHdmiControlCallback, 5);
        } else {
            addAndStartAction(create);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void toggleAndFollowTvPower() {
        assertRunOnServiceThread();
        if (this.mService.getPowerManager().isInteractive()) {
            this.mService.pauseActiveMediaSessions();
        } else {
            this.mService.wakeUp();
        }
        this.mService.queryDisplayStatus(new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceSource.1
            public void onComplete(int i) {
                if (i == -1) {
                    Slog.i(HdmiCecLocalDeviceSource.TAG, "TV power toggle: TV power status unknown");
                    HdmiCecLocalDeviceSource.this.sendUserControlPressedAndReleased(0, HdmiCecKeycode.CEC_KEYCODE_POWER_TOGGLE_FUNCTION);
                    return;
                }
                if (i == 0 || i == 2) {
                    Slog.i(HdmiCecLocalDeviceSource.TAG, "TV power toggle: turning off TV");
                    HdmiCecLocalDeviceSource.this.sendStandby(0);
                    HdmiCecLocalDeviceSource.this.mService.standby();
                } else if (i == 1 || i == 3) {
                    Slog.i(HdmiCecLocalDeviceSource.TAG, "TV power toggle: turning on TV");
                    HdmiCecLocalDeviceSource.this.oneTouchPlay(new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceSource.1.1
                        public void onComplete(int i2) {
                            if (i2 != 0) {
                                Slog.w(HdmiCecLocalDeviceSource.TAG, "Failed to complete One Touch Play. result=" + i2);
                                HdmiCecLocalDeviceSource.this.sendUserControlPressedAndReleased(0, HdmiCecKeycode.CEC_KEYCODE_POWER_TOGGLE_FUNCTION);
                            }
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    public void setActiveSource(int i, int i2, String str) {
        boolean isActiveSource = isActiveSource();
        super.setActiveSource(i, i2, str);
        if (!isActiveSource || isActiveSource()) {
            return;
        }
        onActiveSourceLost();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    public void setActiveSource(int i, String str) {
        assertRunOnServiceThread();
        setActiveSource(HdmiCecLocalDevice.ActiveSource.of(-1, i), str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    public int handleActiveSource(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        int source = hdmiCecMessage.getSource();
        int twoBytesToInt = HdmiUtils.twoBytesToInt(hdmiCecMessage.getParams());
        HdmiCecLocalDevice.ActiveSource of = HdmiCecLocalDevice.ActiveSource.of(source, twoBytesToInt);
        if (!getActiveSource().equals(of)) {
            setActiveSource(of, "HdmiCecLocalDeviceSource#handleActiveSource()");
        }
        updateDevicePowerStatus(source, 0);
        if (!isRoutingControlFeatureEnabled()) {
            return -1;
        }
        switchInputOnReceivingNewActivePath(twoBytesToInt);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected int handleRequestActiveSource(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        maySendActiveSource(hdmiCecMessage.getSource());
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    public int handleSetStreamPath(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        int twoBytesToInt = HdmiUtils.twoBytesToInt(hdmiCecMessage.getParams());
        if (twoBytesToInt == this.mService.getPhysicalAddress() && this.mService.isPlaybackDevice()) {
            setAndBroadcastActiveSource(hdmiCecMessage, twoBytesToInt, "HdmiCecLocalDeviceSource#handleSetStreamPath()");
        } else if (twoBytesToInt != this.mService.getPhysicalAddress() || !isActiveSource()) {
            setActiveSource(twoBytesToInt, "HdmiCecLocalDeviceSource#handleSetStreamPath()");
        }
        switchInputOnReceivingNewActivePath(twoBytesToInt);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingChange(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        int twoBytesToInt = HdmiUtils.twoBytesToInt(hdmiCecMessage.getParams(), 2);
        if (twoBytesToInt != this.mService.getPhysicalAddress() || !isActiveSource()) {
            setActiveSource(twoBytesToInt, "HdmiCecLocalDeviceSource#handleRoutingChange()");
        }
        if (!isRoutingControlFeatureEnabled()) {
            return 4;
        }
        handleRoutingChangeAndInformation(twoBytesToInt, hdmiCecMessage);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingInformation(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        int twoBytesToInt = HdmiUtils.twoBytesToInt(hdmiCecMessage.getParams());
        if (twoBytesToInt != this.mService.getPhysicalAddress() || !isActiveSource()) {
            setActiveSource(twoBytesToInt, "HdmiCecLocalDeviceSource#handleRoutingInformation()");
        }
        if (!isRoutingControlFeatureEnabled()) {
            return 4;
        }
        handleRoutingChangeAndInformation(twoBytesToInt, hdmiCecMessage);
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    public void disableDevice(boolean z, HdmiCecLocalDevice.PendingActionClearedCallback pendingActionClearedCallback) {
        removeAction(OneTouchPlayAction.class);
        removeAction(DevicePowerStatusAction.class);
        removeAction(AbsoluteVolumeAudioStatusAction.class);
        super.disableDevice(z, pendingActionClearedCallback);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected List<Integer> getRcFeatures() {
        ArrayList arrayList = new ArrayList();
        HdmiCecConfig hdmiCecConfig = this.mService.getHdmiCecConfig();
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_root_menu") == 1) {
            arrayList.add(4);
        }
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_setup_menu") == 1) {
            arrayList.add(3);
        }
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_contents_menu") == 1) {
            arrayList.add(2);
        }
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_top_menu") == 1) {
            arrayList.add(1);
        }
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_media_context_sensitive_menu") == 1) {
            arrayList.add(0);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAndBroadcastActiveSource(HdmiCecMessage hdmiCecMessage, int i, String str) {
        this.mService.setAndBroadcastActiveSource(i, getDeviceInfo().getDeviceType(), hdmiCecMessage.getSource(), str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    public boolean isActiveSource() {
        if (getDeviceInfo() == null) {
            return false;
        }
        return getActiveSource().equals(getDeviceInfo().getLogicalAddress(), getDeviceInfo().getPhysicalAddress());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void wakeUpIfActiveSource() {
        if (isActiveSource()) {
            this.mService.wakeUp();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void maySendActiveSource(int i) {
        if (isActiveSource()) {
            addAndStartAction(new ActiveSourceAction(this, i));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public void setRoutingPort(@Constants.LocalActivePort int i) {
        synchronized (this.mLock) {
            this.mRoutingPort = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Constants.LocalActivePort
    public int getRoutingPort() {
        int i;
        synchronized (this.mLock) {
            i = this.mRoutingPort;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Constants.LocalActivePort
    public int getLocalActivePort() {
        int i;
        synchronized (this.mLock) {
            i = this.mLocalActivePort;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setLocalActivePort(@Constants.LocalActivePort int i) {
        synchronized (this.mLock) {
            this.mLocalActivePort = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRoutingControlFeatureEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mRoutingControlFeatureEnabled;
        }
        return z;
    }

    protected boolean isSwitchingToTheSameInput(@Constants.LocalActivePort int i) {
        return i == getLocalActivePort();
    }
}
