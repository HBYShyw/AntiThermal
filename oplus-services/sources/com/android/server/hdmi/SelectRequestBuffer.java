package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import android.os.RemoteException;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SelectRequestBuffer {
    public static final SelectRequestBuffer EMPTY_BUFFER = new SelectRequestBuffer() { // from class: com.android.server.hdmi.SelectRequestBuffer.1
        @Override // com.android.server.hdmi.SelectRequestBuffer
        public void process() {
        }
    };
    private static final String TAG = "SelectRequestBuffer";
    private SelectRequest mRequest;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class SelectRequest {
        protected final IHdmiControlCallback mCallback;
        protected final int mId;
        protected final HdmiControlService mService;

        public abstract void process();

        public SelectRequest(HdmiControlService hdmiControlService, int i, IHdmiControlCallback iHdmiControlCallback) {
            this.mService = hdmiControlService;
            this.mId = i;
            this.mCallback = iHdmiControlCallback;
        }

        protected HdmiCecLocalDeviceTv tv() {
            return this.mService.tv();
        }

        protected HdmiCecLocalDeviceAudioSystem audioSystem() {
            return this.mService.audioSystem();
        }

        protected boolean isLocalDeviceReady() {
            if (tv() != null) {
                return true;
            }
            Slog.e(SelectRequestBuffer.TAG, "Local tv device not available");
            invokeCallback(2);
            return false;
        }

        private void invokeCallback(int i) {
            try {
                IHdmiControlCallback iHdmiControlCallback = this.mCallback;
                if (iHdmiControlCallback != null) {
                    iHdmiControlCallback.onComplete(i);
                }
            } catch (RemoteException e) {
                Slog.e(SelectRequestBuffer.TAG, "Invoking callback failed:" + e);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class DeviceSelectRequest extends SelectRequest {
        private DeviceSelectRequest(HdmiControlService hdmiControlService, int i, IHdmiControlCallback iHdmiControlCallback) {
            super(hdmiControlService, i, iHdmiControlCallback);
        }

        @Override // com.android.server.hdmi.SelectRequestBuffer.SelectRequest
        public void process() {
            if (isLocalDeviceReady()) {
                Slog.v(SelectRequestBuffer.TAG, "calling delayed deviceSelect id:" + this.mId);
                tv().deviceSelect(this.mId, this.mCallback);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PortSelectRequest extends SelectRequest {
        private PortSelectRequest(HdmiControlService hdmiControlService, int i, IHdmiControlCallback iHdmiControlCallback) {
            super(hdmiControlService, i, iHdmiControlCallback);
        }

        @Override // com.android.server.hdmi.SelectRequestBuffer.SelectRequest
        public void process() {
            if (isLocalDeviceReady()) {
                Slog.v(SelectRequestBuffer.TAG, "calling delayed portSelect id:" + this.mId);
                HdmiCecLocalDeviceTv tv = tv();
                if (tv != null) {
                    tv.doManualPortSwitching(this.mId, this.mCallback);
                    return;
                }
                HdmiCecLocalDeviceAudioSystem audioSystem = audioSystem();
                if (audioSystem != null) {
                    audioSystem.doManualPortSwitching(this.mId, this.mCallback);
                }
            }
        }
    }

    public static DeviceSelectRequest newDeviceSelect(HdmiControlService hdmiControlService, int i, IHdmiControlCallback iHdmiControlCallback) {
        return new DeviceSelectRequest(hdmiControlService, i, iHdmiControlCallback);
    }

    public static PortSelectRequest newPortSelect(HdmiControlService hdmiControlService, int i, IHdmiControlCallback iHdmiControlCallback) {
        return new PortSelectRequest(hdmiControlService, i, iHdmiControlCallback);
    }

    public void set(SelectRequest selectRequest) {
        this.mRequest = selectRequest;
    }

    public void process() {
        SelectRequest selectRequest = this.mRequest;
        if (selectRequest != null) {
            selectRequest.process();
            clear();
        }
    }

    public void clear() {
        this.mRequest = null;
    }
}
