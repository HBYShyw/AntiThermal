package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.hdmi.HdmiControlService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class HdmiCecFeatureAction {
    protected static final int MSG_TIMEOUT = 100;
    protected static final int STATE_NONE = 0;
    private static final String TAG = "HdmiCecFeatureAction";
    protected ActionTimer mActionTimer;
    final List<IHdmiControlCallback> mCallbacks;
    private ArrayList<Pair<HdmiCecFeatureAction, Runnable>> mOnFinishedCallbacks;
    private final HdmiControlService mService;
    private final HdmiCecLocalDevice mSource;
    protected int mState;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ActionTimer {
        void clearTimerMessage();

        void sendTimerMessage(int i, long j);
    }

    abstract void handleTimerEvent(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean processCommand(HdmiCecMessage hdmiCecMessage);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean start();

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiCecFeatureAction(HdmiCecLocalDevice hdmiCecLocalDevice) {
        this(hdmiCecLocalDevice, new ArrayList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiCecFeatureAction(HdmiCecLocalDevice hdmiCecLocalDevice, IHdmiControlCallback iHdmiControlCallback) {
        this(hdmiCecLocalDevice, (List<IHdmiControlCallback>) Arrays.asList(iHdmiControlCallback));
    }

    HdmiCecFeatureAction(HdmiCecLocalDevice hdmiCecLocalDevice, List<IHdmiControlCallback> list) {
        this.mState = 0;
        this.mCallbacks = new ArrayList();
        Iterator<IHdmiControlCallback> it = list.iterator();
        while (it.hasNext()) {
            addCallback(it.next());
        }
        this.mSource = hdmiCecLocalDevice;
        HdmiControlService service = hdmiCecLocalDevice.getService();
        this.mService = service;
        this.mActionTimer = createActionTimer(service.getServiceLooper());
    }

    @VisibleForTesting
    void setActionTimer(ActionTimer actionTimer) {
        this.mActionTimer = actionTimer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ActionTimerHandler extends Handler implements ActionTimer {
        public ActionTimerHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.server.hdmi.HdmiCecFeatureAction.ActionTimer
        public void sendTimerMessage(int i, long j) {
            sendMessageDelayed(obtainMessage(100, i, 0), j);
        }

        @Override // com.android.server.hdmi.HdmiCecFeatureAction.ActionTimer
        public void clearTimerMessage() {
            removeMessages(100);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 100) {
                HdmiCecFeatureAction.this.handleTimerEvent(message.arg1);
                return;
            }
            Slog.w(HdmiCecFeatureAction.TAG, "Unsupported message:" + message.what);
        }
    }

    private ActionTimer createActionTimer(Looper looper) {
        return new ActionTimerHandler(looper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addTimer(int i, int i2) {
        this.mActionTimer.sendTimerMessage(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean started() {
        return this.mState != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void sendCommand(HdmiCecMessage hdmiCecMessage) {
        this.mService.sendCecCommand(hdmiCecMessage);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void sendCommand(HdmiCecMessage hdmiCecMessage, HdmiControlService.SendMessageCallback sendMessageCallback) {
        this.mService.sendCecCommand(hdmiCecMessage, sendMessageCallback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void addAndStartAction(HdmiCecFeatureAction hdmiCecFeatureAction) {
        this.mSource.addAndStartAction(hdmiCecFeatureAction);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final <T extends HdmiCecFeatureAction> List<T> getActions(Class<T> cls) {
        return this.mSource.getActions(cls);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final HdmiCecMessageCache getCecMessageCache() {
        return this.mSource.getCecMessageCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void removeAction(HdmiCecFeatureAction hdmiCecFeatureAction) {
        this.mSource.removeAction(hdmiCecFeatureAction);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final <T extends HdmiCecFeatureAction> void removeAction(Class<T> cls) {
        this.mSource.removeActionExcept(cls, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final <T extends HdmiCecFeatureAction> void removeActionExcept(Class<T> cls, HdmiCecFeatureAction hdmiCecFeatureAction) {
        this.mSource.removeActionExcept(cls, hdmiCecFeatureAction);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void pollDevices(HdmiControlService.DevicePollingCallback devicePollingCallback, int i, int i2) {
        this.mService.pollDevices(devicePollingCallback, getSourceAddress(), i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        this.mState = 0;
        this.mActionTimer.clearTimerMessage();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void finish() {
        finish(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finish(boolean z) {
        clear();
        if (z) {
            removeAction(this);
        }
        ArrayList<Pair<HdmiCecFeatureAction, Runnable>> arrayList = this.mOnFinishedCallbacks;
        if (arrayList != null) {
            Iterator<Pair<HdmiCecFeatureAction, Runnable>> it = arrayList.iterator();
            while (it.hasNext()) {
                Pair<HdmiCecFeatureAction, Runnable> next = it.next();
                if (((HdmiCecFeatureAction) next.first).mState != 0) {
                    ((Runnable) next.second).run();
                }
            }
            this.mOnFinishedCallbacks = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final HdmiCecLocalDevice localDevice() {
        return this.mSource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final HdmiCecLocalDevicePlayback playback() {
        return (HdmiCecLocalDevicePlayback) this.mSource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final HdmiCecLocalDeviceSource source() {
        return (HdmiCecLocalDeviceSource) this.mSource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final HdmiCecLocalDeviceTv tv() {
        return (HdmiCecLocalDeviceTv) this.mSource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final HdmiCecLocalDeviceAudioSystem audioSystem() {
        return (HdmiCecLocalDeviceAudioSystem) this.mSource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int getSourceAddress() {
        return this.mSource.getDeviceInfo().getLogicalAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int getSourcePath() {
        return this.mSource.getDeviceInfo().getPhysicalAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void sendUserControlPressedAndReleased(int i, int i2) {
        this.mSource.sendUserControlPressedAndReleased(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void addOnFinishedCallback(HdmiCecFeatureAction hdmiCecFeatureAction, Runnable runnable) {
        if (this.mOnFinishedCallbacks == null) {
            this.mOnFinishedCallbacks = new ArrayList<>();
        }
        this.mOnFinishedCallbacks.add(Pair.create(hdmiCecFeatureAction, runnable));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void finishWithCallback(int i) {
        invokeCallback(i);
        finish();
    }

    public void addCallback(IHdmiControlCallback iHdmiControlCallback) {
        this.mCallbacks.add(iHdmiControlCallback);
    }

    private void invokeCallback(int i) {
        try {
            for (IHdmiControlCallback iHdmiControlCallback : this.mCallbacks) {
                if (iHdmiControlCallback != null) {
                    iHdmiControlCallback.onComplete(i);
                }
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Callback failed:" + e);
        }
    }
}
