package com.android.server.hdmi;

import android.R;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.IHdmiControlCallback;
import android.os.Binder;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.sysprop.HdmiProperties;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.LocalePicker;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.hdmi.DeviceDiscoveryAction;
import com.android.server.hdmi.HdmiAnnotations;
import com.android.server.hdmi.HdmiCecLocalDevice;
import com.android.server.hdmi.HdmiControlService;
import com.android.server.location.gnss.hal.GnssNative;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HdmiCecLocalDevicePlayback extends HdmiCecLocalDeviceSource {

    @VisibleForTesting
    static final long STANDBY_AFTER_HOTPLUG_OUT_DELAY_MS = 30000;
    private static final String TAG = "HdmiCecLocalDevicePlayback";
    private Handler mDelayedStandbyHandler;

    @VisibleForTesting
    protected HdmiProperties.playback_device_action_on_routing_control_values mPlaybackDeviceActionOnRoutingControl;
    private ActiveWakeLock mWakeLock;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ActiveWakeLock {
        void acquire();

        boolean isHeld();

        void release();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int findKeyReceiverAddress() {
        return 0;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    public /* bridge */ /* synthetic */ ArrayBlockingQueue getActiveSourceHistory() {
        return super.getActiveSourceHistory();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiCecLocalDevicePlayback(HdmiControlService hdmiControlService) {
        super(hdmiControlService, 4);
        this.mPlaybackDeviceActionOnRoutingControl = (HdmiProperties.playback_device_action_on_routing_control_values) HdmiProperties.playback_device_action_on_routing_control().orElse(HdmiProperties.playback_device_action_on_routing_control_values.NONE);
        this.mDelayedStandbyHandler = new Handler(hdmiControlService.getServiceLooper());
        this.mStandbyHandler = new HdmiCecStandbyModeHandler(hdmiControlService, this);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected void onAddressAllocated(int i, int i2) {
        assertRunOnServiceThread();
        HdmiControlService hdmiControlService = this.mService;
        if (i2 == 0) {
            hdmiControlService.setAndBroadcastActiveSource(hdmiControlService.getPhysicalAddress(), getDeviceInfo().getDeviceType(), 15, "HdmiCecLocalDevicePlayback#onAddressAllocated()");
        }
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildReportPhysicalAddressCommand(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress(), this.mDeviceType));
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildDeviceVendorIdCommand(getDeviceInfo().getLogicalAddress(), this.mService.getVendorId()));
        buildAndSendSetOsdName(0);
        if (this.mService.audioSystem() == null) {
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildGiveSystemAudioModeStatus(getDeviceInfo().getLogicalAddress(), 5), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.1
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public void onSendCompleted(int i3) {
                    if (i3 != 0) {
                        HdmiLogger.debug("AVR did not respond to <Give System Audio Mode Status>", new Object[0]);
                        HdmiCecLocalDevicePlayback.this.mService.setSystemAudioActivated(false);
                    }
                }
            });
        }
        launchDeviceDiscovery();
        startQueuedActions();
    }

    @HdmiAnnotations.ServiceThreadOnly
    private void launchDeviceDiscovery() {
        assertRunOnServiceThread();
        clearDeviceInfoList();
        addAndStartAction(new DeviceDiscoveryAction(this, new DeviceDiscoveryAction.DeviceDiscoveryCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.2
            @Override // com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback
            public void onDeviceDiscoveryDone(List<HdmiDeviceInfo> list) {
                Iterator<HdmiDeviceInfo> it = list.iterator();
                while (it.hasNext()) {
                    HdmiCecLocalDevicePlayback.this.mService.getHdmiCecNetwork().addCecDevice(it.next());
                }
                Iterator<HdmiCecLocalDevice> it2 = HdmiCecLocalDevicePlayback.this.mService.getAllCecLocalDevices().iterator();
                while (it2.hasNext()) {
                    HdmiCecLocalDevicePlayback.this.mService.getHdmiCecNetwork().addCecDevice(it2.next().getDeviceInfo());
                }
                if (HdmiCecLocalDevicePlayback.this.getActions(HotplugDetectionAction.class).isEmpty()) {
                    HdmiCecLocalDevicePlayback hdmiCecLocalDevicePlayback = HdmiCecLocalDevicePlayback.this;
                    hdmiCecLocalDevicePlayback.addAndStartAction(new HotplugDetectionAction(hdmiCecLocalDevicePlayback));
                }
            }
        }));
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected int getPreferredAddress() {
        assertRunOnServiceThread();
        return SystemProperties.getInt("persist.sys.hdmi.addr.playback", 15);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected void setPreferredAddress(int i) {
        assertRunOnServiceThread();
        this.mService.writeStringSystemProperty("persist.sys.hdmi.addr.playback", String.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void deviceSelect(int i, IHdmiControlCallback iHdmiControlCallback) {
        assertRunOnServiceThread();
        if (i == getDeviceInfo().getId()) {
            this.mService.oneTouchPlay(iHdmiControlCallback);
            return;
        }
        HdmiDeviceInfo deviceInfo = this.mService.getHdmiCecNetwork().getDeviceInfo(i);
        if (deviceInfo == null) {
            invokeCallback(iHdmiControlCallback, 3);
            return;
        }
        if (isAlreadyActiveSource(deviceInfo, deviceInfo.getLogicalAddress(), iHdmiControlCallback)) {
            return;
        }
        if (!this.mService.isCecControlEnabled()) {
            setActiveSource(deviceInfo, "HdmiCecLocalDevicePlayback#deviceSelect()");
            invokeCallback(iHdmiControlCallback, 6);
        } else {
            removeAction(DeviceSelectActionFromPlayback.class);
            addAndStartAction(new DeviceSelectActionFromPlayback(this, deviceInfo, iHdmiControlCallback));
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    void onHotplug(int i, boolean z) {
        assertRunOnServiceThread();
        this.mCecMessageCache.flushAll();
        if (z) {
            this.mDelayedStandbyHandler.removeCallbacksAndMessages(null);
            return;
        }
        getWakeLock().release();
        this.mService.getHdmiCecNetwork().removeDevicesConnectedToPort(i);
        this.mDelayedStandbyHandler.removeCallbacksAndMessages(null);
        this.mDelayedStandbyHandler.postDelayed(new DelayedStandbyRunnable(), STANDBY_AFTER_HOTPLUG_OUT_DELAY_MS);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class DelayedStandbyRunnable implements Runnable {
        private DelayedStandbyRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (HdmiCecLocalDevicePlayback.this.mService.getPowerManagerInternal().wasDeviceIdleFor(HdmiCecLocalDevicePlayback.STANDBY_AFTER_HOTPLUG_OUT_DELAY_MS)) {
                HdmiCecLocalDevicePlayback.this.mService.standby();
            } else {
                HdmiCecLocalDevicePlayback.this.mDelayedStandbyHandler.postDelayed(new DelayedStandbyRunnable(), HdmiCecLocalDevicePlayback.STANDBY_AFTER_HOTPLUG_OUT_DELAY_MS);
            }
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected void onStandby(boolean z, int i) {
        assertRunOnServiceThread();
        if (this.mService.isCecControlEnabled()) {
            boolean isActiveSource = isActiveSource();
            char c = 65535;
            this.mService.setActiveSource(-1, GnssNative.GNSS_AIDING_TYPE_ALL, "HdmiCecLocalDevicePlayback#onStandby()");
            if (isActiveSource) {
                if (z) {
                    this.mService.sendCecCommand(HdmiCecMessageBuilder.buildInactiveSource(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress()));
                    return;
                }
                if (i != 0) {
                    if (i != 1) {
                        return;
                    }
                    this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 15));
                    return;
                }
                String stringValue = this.mService.getHdmiCecConfig().getStringValue("power_control_mode");
                stringValue.hashCode();
                switch (stringValue.hashCode()) {
                    case -1744153479:
                        if (stringValue.equals("to_tv_and_audio_system")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1618876223:
                        if (stringValue.equals("broadcast")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 3387192:
                        if (stringValue.equals("none")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 110530246:
                        if (stringValue.equals("to_tv")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 0));
                        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 5));
                        return;
                    case 1:
                        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 15));
                        return;
                    case 2:
                        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildInactiveSource(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress()));
                        return;
                    case 3:
                        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 0));
                        return;
                    default:
                        return;
                }
            }
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected void onInitializeCecComplete(int i) {
        if (i == 2 && !this.mService.getHdmiCecConfig().getStringValue("power_control_mode").equals("none")) {
            oneTouchPlay(new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.3
                public void onComplete(int i2) {
                    if (i2 != 0) {
                        Slog.w(HdmiCecLocalDevicePlayback.TAG, "Failed to complete One Touch Play. result=" + i2);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    public void setActiveSource(int i, int i2, String str) {
        assertRunOnServiceThread();
        super.setActiveSource(i, i2, str);
        if (isActiveSource()) {
            getWakeLock().acquire();
        } else {
            getWakeLock().release();
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    private ActiveWakeLock getWakeLock() {
        assertRunOnServiceThread();
        if (this.mWakeLock == null) {
            if (SystemProperties.getBoolean("persist.sys.hdmi.keep_awake", true)) {
                this.mWakeLock = new SystemWakeLock();
            } else {
                this.mWakeLock = new ActiveWakeLock() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.4
                    @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
                    public void acquire() {
                    }

                    @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
                    public boolean isHeld() {
                        return false;
                    }

                    @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
                    public void release() {
                    }
                };
                HdmiLogger.debug("No wakelock is used to keep the display on.", new Object[0]);
            }
        }
        return this.mWakeLock;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected boolean canGoToStandby() {
        return !getWakeLock().isHeld();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource
    @HdmiAnnotations.ServiceThreadOnly
    protected void onActiveSourceLost() {
        assertRunOnServiceThread();
        this.mService.pauseActiveMediaSessions();
        String stringValue = this.mService.getHdmiCecConfig().getStringValue("power_state_change_on_active_source_lost");
        stringValue.hashCode();
        if (stringValue.equals("standby_now")) {
            this.mService.standby();
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected int handleUserControlPressed(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        wakeUpIfActiveSource();
        return super.handleUserControlPressed(hdmiCecMessage);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected int handleSetMenuLanguage(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        if (this.mService.getHdmiCecConfig().getIntValue("set_menu_language") == 0) {
            return 0;
        }
        try {
            String str = new String(hdmiCecMessage.getParams(), 0, 3, "US-ASCII");
            String localeToMenuLanguage = HdmiControlService.localeToMenuLanguage(this.mService.getContext().getResources().getConfiguration().locale);
            HdmiLogger.debug("handleSetMenuLanguage " + str + " cur:" + localeToMenuLanguage, new Object[0]);
            if (localeToMenuLanguage.equals(str)) {
                return -1;
            }
            for (LocalePicker.LocaleInfo localeInfo : LocalePicker.getAllAssetLocales(this.mService.getContext(), false)) {
                if (HdmiControlService.localeToMenuLanguage(localeInfo.getLocale()).equals(str)) {
                    startSetMenuLanguageActivity(localeInfo.getLocale());
                    return -1;
                }
            }
            Slog.w(TAG, "Can't handle <Set Menu Language> of " + str);
            return 3;
        } catch (UnsupportedEncodingException e) {
            Slog.w(TAG, "Can't handle <Set Menu Language>", e);
            return 3;
        }
    }

    private void startSetMenuLanguageActivity(Locale locale) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                Context context = this.mService.getContext();
                Intent intent = new Intent();
                intent.putExtra("android.hardware.hdmi.extra.LOCALE", locale.toLanguageTag());
                intent.setComponent(ComponentName.unflattenFromString(context.getResources().getString(R.string.contentServiceTooManyDeletesNotificationDesc)));
                intent.addFlags(268435456);
                context.startActivityAsUser(intent, context.getUser());
            } catch (ActivityNotFoundException unused) {
                Slog.e(TAG, "unable to start HdmiCecSetMenuLanguageActivity");
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleSetSystemAudioMode(HdmiCecMessage hdmiCecMessage) {
        boolean parseCommandParamSystemAudioStatus;
        if (hdmiCecMessage.getDestination() == 15 && hdmiCecMessage.getSource() == 5 && this.mService.audioSystem() == null && this.mService.isSystemAudioActivated() != (parseCommandParamSystemAudioStatus = HdmiUtils.parseCommandParamSystemAudioStatus(hdmiCecMessage))) {
            this.mService.setSystemAudioActivated(parseCommandParamSystemAudioStatus);
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleSystemAudioModeStatus(HdmiCecMessage hdmiCecMessage) {
        boolean parseCommandParamSystemAudioStatus;
        if (hdmiCecMessage.getDestination() != getDeviceInfo().getLogicalAddress() || hdmiCecMessage.getSource() != 5 || this.mService.isSystemAudioActivated() == (parseCommandParamSystemAudioStatus = HdmiUtils.parseCommandParamSystemAudioStatus(hdmiCecMessage))) {
            return -1;
        }
        this.mService.setSystemAudioActivated(parseCommandParamSystemAudioStatus);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingChange(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        handleRoutingChangeAndInformation(HdmiUtils.twoBytesToInt(hdmiCecMessage.getParams(), 2), hdmiCecMessage);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingInformation(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        handleRoutingChangeAndInformation(HdmiUtils.twoBytesToInt(hdmiCecMessage.getParams()), hdmiCecMessage);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource
    @HdmiAnnotations.ServiceThreadOnly
    protected void handleRoutingChangeAndInformation(int i, HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        if (i != this.mService.getPhysicalAddress()) {
            setActiveSource(i, "HdmiCecLocalDevicePlayback#handleRoutingChangeAndInformation()");
            return;
        }
        if (!isActiveSource()) {
            setActiveSource(i, "HdmiCecLocalDevicePlayback#handleRoutingChangeAndInformation()");
        }
        int i2 = AnonymousClass5.$SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values[this.mPlaybackDeviceActionOnRoutingControl.ordinal()];
        if (i2 == 1) {
            setAndBroadcastActiveSource(hdmiCecMessage, i, "HdmiCecLocalDevicePlayback#handleRoutingChangeAndInformation()");
        } else {
            if (i2 != 2) {
                return;
            }
            this.mService.wakeUp();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.hdmi.HdmiCecLocalDevicePlayback$5, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values;

        static {
            int[] iArr = new int[HdmiProperties.playback_device_action_on_routing_control_values.values().length];
            $SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values = iArr;
            try {
                iArr[HdmiProperties.playback_device_action_on_routing_control_values.WAKE_UP_AND_SEND_ACTIVE_SOURCE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values[HdmiProperties.playback_device_action_on_routing_control_values.WAKE_UP_ONLY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values[HdmiProperties.playback_device_action_on_routing_control_values.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected void preprocessBufferedMessages(List<HdmiCecMessage> list) {
        for (HdmiCecMessage hdmiCecMessage : list) {
            if (hdmiCecMessage.getOpcode() == 128 || hdmiCecMessage.getOpcode() == 134 || hdmiCecMessage.getOpcode() == 130) {
                removeAction(ActiveSourceAction.class);
                removeAction(OneTouchPlayAction.class);
                return;
            }
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int findAudioReceiverAddress() {
        return this.mService.isSystemAudioActivated() ? 5 : 0;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @HdmiAnnotations.ServiceThreadOnly
    protected void disableDevice(boolean z, HdmiCecLocalDevice.PendingActionClearedCallback pendingActionClearedCallback) {
        assertRunOnServiceThread();
        removeAction(DeviceDiscoveryAction.class);
        removeAction(HotplugDetectionAction.class);
        removeAction(NewDeviceAction.class);
        super.disableDevice(z, pendingActionClearedCallback);
        clearDeviceInfoList();
        checkIfPendingActionsCleared();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected void dump(IndentingPrintWriter indentingPrintWriter) {
        super.dump(indentingPrintWriter);
        indentingPrintWriter.println("isActiveSource(): " + isActiveSource());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class SystemWakeLock implements ActiveWakeLock {
        private final PowerManager.WakeLock mWakeLock;

        public SystemWakeLock() {
            PowerManager.WakeLock newWakeLock = HdmiCecLocalDevicePlayback.this.mService.getPowerManager().newWakeLock(1, HdmiCecLocalDevicePlayback.TAG);
            this.mWakeLock = newWakeLock;
            newWakeLock.setReferenceCounted(false);
        }

        @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
        public void acquire() {
            this.mWakeLock.acquire();
            HdmiLogger.debug("active source: %b. Wake lock acquired", Boolean.valueOf(HdmiCecLocalDevicePlayback.this.isActiveSource()));
        }

        @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
        public void release() {
            this.mWakeLock.release();
            HdmiLogger.debug("Wake lock released", new Object[0]);
        }

        @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
        public boolean isHeld() {
            return this.mWakeLock.isHeld();
        }
    }
}
