package com.android.server.hdmi;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.util.Slog;
import com.android.server.hdmi.HdmiControlService;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HotplugDetectionAction extends HdmiCecFeatureAction {
    private static final int AVR_COUNT_MAX = 3;
    private static final int NUM_OF_ADDRESS = 15;
    public static final int POLLING_INTERVAL_MS_FOR_PLAYBACK = 60000;
    public static final int POLLING_INTERVAL_MS_FOR_TV = 5000;
    private static final int STATE_WAIT_FOR_NEXT_POLLING = 1;
    private static final String TAG = "HotPlugDetectionAction";
    public static final int TIMEOUT_COUNT = 3;
    private int mAvrStatusCount;
    private final boolean mIsTvDevice;
    private int mTimeoutCount;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HotplugDetectionAction(HdmiCecLocalDevice hdmiCecLocalDevice) {
        super(hdmiCecLocalDevice);
        this.mTimeoutCount = 0;
        this.mAvrStatusCount = 0;
        this.mIsTvDevice = localDevice().mService.isTvDevice();
    }

    private int getPollingInterval() {
        if (this.mIsTvDevice) {
            return POLLING_INTERVAL_MS_FOR_TV;
        }
        return 60000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        Slog.v(TAG, "Hot-plug detection started.");
        this.mState = 1;
        this.mTimeoutCount = 0;
        addTimer(1, getPollingInterval());
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 == i && i2 == 1) {
            if (this.mIsTvDevice) {
                int i3 = (this.mTimeoutCount + 1) % 3;
                this.mTimeoutCount = i3;
                if (i3 == 0) {
                    pollAllDevices();
                } else if (tv().isSystemAudioActivated()) {
                    pollAudioSystem();
                }
                addTimer(this.mState, POLLING_INTERVAL_MS_FOR_TV);
                return;
            }
            pollAllDevices();
            addTimer(this.mState, 60000);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pollAllDevicesNow() {
        this.mActionTimer.clearTimerMessage();
        this.mTimeoutCount = 0;
        this.mState = 1;
        pollAllDevices();
        addTimer(this.mState, getPollingInterval());
    }

    private void pollAllDevices() {
        Slog.v(TAG, "Poll all devices.");
        pollDevices(new HdmiControlService.DevicePollingCallback() { // from class: com.android.server.hdmi.HotplugDetectionAction.1
            @Override // com.android.server.hdmi.HdmiControlService.DevicePollingCallback
            public void onPollingFinished(List<Integer> list) {
                HotplugDetectionAction.this.checkHotplug(list, false);
            }
        }, 65537, 1);
    }

    private void pollAudioSystem() {
        Slog.v(TAG, "Poll audio system.");
        pollDevices(new HdmiControlService.DevicePollingCallback() { // from class: com.android.server.hdmi.HotplugDetectionAction.2
            @Override // com.android.server.hdmi.HdmiControlService.DevicePollingCallback
            public void onPollingFinished(List<Integer> list) {
                HotplugDetectionAction.this.checkHotplug(list, true);
            }
        }, 65538, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkHotplug(List<Integer> list, boolean z) {
        HdmiDeviceInfo avrDeviceInfo;
        List<HdmiDeviceInfo> deviceInfoList = localDevice().mService.getHdmiCecNetwork().getDeviceInfoList(false);
        BitSet infoListToBitSet = infoListToBitSet(deviceInfoList, z, false);
        BitSet addressListToBitSet = addressListToBitSet(list);
        BitSet complement = complement(infoListToBitSet, addressListToBitSet);
        int i = -1;
        while (true) {
            i = complement.nextSetBit(i + 1);
            if (i == -1) {
                break;
            }
            if (this.mIsTvDevice && i == 5 && (avrDeviceInfo = tv().getAvrDeviceInfo()) != null && tv().isConnected(avrDeviceInfo.getPortId())) {
                this.mAvrStatusCount++;
                Slog.w(TAG, "Ack not returned from AVR. count: " + this.mAvrStatusCount);
                if (this.mAvrStatusCount < 3) {
                }
            }
            Slog.v(TAG, "Remove device by hot-plug detection:" + i);
            removeDevice(i);
        }
        if (!complement.get(5)) {
            this.mAvrStatusCount = 0;
        }
        BitSet complement2 = complement(addressListToBitSet, infoListToBitSet(deviceInfoList, z, true));
        int i2 = -1;
        while (true) {
            i2 = complement2.nextSetBit(i2 + 1);
            if (i2 == -1) {
                return;
            }
            Slog.v(TAG, "Add device by hot-plug detection:" + i2);
            addDevice(i2);
        }
    }

    private static BitSet infoListToBitSet(List<HdmiDeviceInfo> list, boolean z, boolean z2) {
        BitSet bitSet = new BitSet(15);
        for (HdmiDeviceInfo hdmiDeviceInfo : list) {
            boolean z3 = !z || hdmiDeviceInfo.getDeviceType() == 5;
            boolean z4 = (z2 && hdmiDeviceInfo.getPhysicalAddress() == 65535) ? false : true;
            if (z3 && z4) {
                bitSet.set(hdmiDeviceInfo.getLogicalAddress());
            }
        }
        return bitSet;
    }

    private static BitSet addressListToBitSet(List<Integer> list) {
        BitSet bitSet = new BitSet(15);
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            bitSet.set(it.next().intValue());
        }
        return bitSet;
    }

    private static BitSet complement(BitSet bitSet, BitSet bitSet2) {
        BitSet bitSet3 = (BitSet) bitSet.clone();
        bitSet3.andNot(bitSet2);
        return bitSet3;
    }

    private void addDevice(int i) {
        sendCommand(HdmiCecMessageBuilder.buildGivePhysicalAddress(getSourceAddress(), i));
    }

    private void removeDevice(int i) {
        if (this.mIsTvDevice) {
            mayChangeRoutingPath(i);
            mayCancelOneTouchRecord(i);
            mayDisableSystemAudioAndARC(i);
        }
        mayCancelDeviceSelect(i);
        localDevice().mService.getHdmiCecNetwork().removeCecDevice(localDevice(), i);
    }

    private void mayChangeRoutingPath(int i) {
        HdmiDeviceInfo cecDeviceInfo = localDevice().mService.getHdmiCecNetwork().getCecDeviceInfo(i);
        if (cecDeviceInfo != null) {
            tv().handleRemoveActiveRoutingPath(cecDeviceInfo.getPhysicalAddress());
        }
    }

    private void mayCancelDeviceSelect(int i) {
        Iterator it = getActions(DeviceSelectActionFromTv.class).iterator();
        while (it.hasNext()) {
            if (((DeviceSelectActionFromTv) it.next()).getTargetAddress() == i) {
                removeAction(DeviceSelectActionFromTv.class);
            }
        }
        Iterator it2 = getActions(DeviceSelectActionFromPlayback.class).iterator();
        while (it2.hasNext()) {
            if (((DeviceSelectActionFromPlayback) it2.next()).getTargetAddress() == i) {
                removeAction(DeviceSelectActionFromTv.class);
            }
        }
    }

    private void mayCancelOneTouchRecord(int i) {
        for (OneTouchRecordAction oneTouchRecordAction : getActions(OneTouchRecordAction.class)) {
            if (oneTouchRecordAction.getRecorderAddress() == i) {
                removeAction(oneTouchRecordAction);
            }
        }
    }

    private void mayDisableSystemAudioAndARC(int i) {
        if (HdmiUtils.isEligibleAddressForDevice(5, i)) {
            tv().setSystemAudioMode(false);
            if (tv().isArcEstablished()) {
                tv().enableAudioReturnChannel(false);
                addAndStartAction(new RequestArcTerminationAction(localDevice(), i));
            }
        }
    }
}
