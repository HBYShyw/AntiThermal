package com.android.server.hdmi;

import android.hardware.tv.hdmi.earc.IEArc;
import android.hardware.tv.hdmi.earc.IEArcCallback;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.hdmi.Constants;
import com.android.server.hdmi.HdmiAnnotations;
import com.android.server.hdmi.HdmiEarcController;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HdmiEarcController {
    private static final String TAG = "HdmiEarcController";
    private Handler mControlHandler;
    private EarcNativeWrapper mEarcNativeWrapperImpl;
    private final HdmiControlService mService;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface EarcNativeWrapper {
        byte[] nativeGetLastReportedAudioCapabilities(int i);

        byte nativeGetState(int i);

        boolean nativeInit();

        boolean nativeIsEarcEnabled();

        void nativeSetCallback(EarcAidlCallback earcAidlCallback);

        void nativeSetEarcEnabled(boolean z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class EarcNativeWrapperImpl implements EarcNativeWrapper, IBinder.DeathRecipient {
        private IEArc mEarc;
        private EarcAidlCallback mEarcCallback;

        private EarcNativeWrapperImpl() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mEarc.asBinder().unlinkToDeath(this, 0);
            connectToHal();
            EarcAidlCallback earcAidlCallback = this.mEarcCallback;
            if (earcAidlCallback != null) {
                nativeSetCallback(earcAidlCallback);
            }
        }

        boolean connectToHal() {
            IEArc asInterface = IEArc.Stub.asInterface(ServiceManager.getService(IEArc.DESCRIPTOR + "/default"));
            this.mEarc = asInterface;
            if (asInterface == null) {
                return false;
            }
            try {
                asInterface.asBinder().linkToDeath(this, 0);
                return true;
            } catch (RemoteException e) {
                HdmiLogger.error("Couldn't link callback object: ", e, new Object[0]);
                return true;
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public boolean nativeInit() {
            return connectToHal();
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public void nativeSetEarcEnabled(boolean z) {
            try {
                this.mEarc.setEArcEnabled(z);
            } catch (ServiceSpecificException e) {
                HdmiLogger.error("Could not set eARC enabled to " + z + ". Error: ", Integer.valueOf(e.errorCode));
            } catch (RemoteException e2) {
                HdmiLogger.error("Could not set eARC enabled to " + z + ":. Exception: ", e2, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public boolean nativeIsEarcEnabled() {
            try {
                return this.mEarc.isEArcEnabled();
            } catch (RemoteException e) {
                HdmiLogger.error("Could not read if eARC is enabled. Exception: ", e, new Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public void nativeSetCallback(EarcAidlCallback earcAidlCallback) {
            this.mEarcCallback = earcAidlCallback;
            try {
                this.mEarc.setCallback(earcAidlCallback);
            } catch (RemoteException e) {
                HdmiLogger.error("Could not set callback. Exception: ", e, new Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public byte nativeGetState(int i) {
            try {
                return this.mEarc.getState(i);
            } catch (RemoteException e) {
                HdmiLogger.error("Could not get eARC state. Exception: ", e, new Object[0]);
                return (byte) -1;
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public byte[] nativeGetLastReportedAudioCapabilities(int i) {
            try {
                return this.mEarc.getLastReportedAudioCapabilities(i);
            } catch (RemoteException e) {
                HdmiLogger.error("Could not read last reported audio capabilities. Exception: ", e, new Object[0]);
                return null;
            }
        }
    }

    private HdmiEarcController(HdmiControlService hdmiControlService, EarcNativeWrapper earcNativeWrapper) {
        this.mService = hdmiControlService;
        this.mEarcNativeWrapperImpl = earcNativeWrapper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiEarcController create(HdmiControlService hdmiControlService) {
        return createWithNativeWrapper(hdmiControlService, new EarcNativeWrapperImpl());
    }

    static HdmiEarcController createWithNativeWrapper(HdmiControlService hdmiControlService, EarcNativeWrapper earcNativeWrapper) {
        HdmiEarcController hdmiEarcController = new HdmiEarcController(hdmiControlService, earcNativeWrapper);
        if (hdmiEarcController.init(earcNativeWrapper)) {
            return hdmiEarcController;
        }
        HdmiLogger.warning("Could not connect to eARC AIDL HAL.", new Object[0]);
        return null;
    }

    private boolean init(EarcNativeWrapper earcNativeWrapper) {
        if (!earcNativeWrapper.nativeInit()) {
            return false;
        }
        this.mControlHandler = new Handler(this.mService.getServiceLooper());
        this.mEarcNativeWrapperImpl.nativeSetCallback(new EarcAidlCallback());
        return true;
    }

    private void assertRunOnServiceThread() {
        if (Looper.myLooper() != this.mControlHandler.getLooper()) {
            throw new IllegalStateException("Should run on service thread.");
        }
    }

    @VisibleForTesting
    void runOnServiceThread(Runnable runnable) {
        this.mControlHandler.post(new WorkSourceUidPreservingRunnable(runnable));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void setEarcEnabled(boolean z) {
        assertRunOnServiceThread();
        this.mEarcNativeWrapperImpl.nativeSetEarcEnabled(z);
    }

    @Constants.EarcStatus
    @HdmiAnnotations.ServiceThreadOnly
    int getState(int i) {
        return this.mEarcNativeWrapperImpl.nativeGetState(i);
    }

    @HdmiAnnotations.ServiceThreadOnly
    byte[] getLastReportedCaps(int i) {
        return this.mEarcNativeWrapperImpl.nativeGetLastReportedAudioCapabilities(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class EarcAidlCallback extends IEArcCallback.Stub {
        public int getInterfaceVersion() throws RemoteException {
            return 1;
        }

        EarcAidlCallback() {
        }

        public void onStateChange(@Constants.EarcStatus final byte b, final int i) {
            HdmiEarcController.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiEarcController$EarcAidlCallback$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    HdmiEarcController.EarcAidlCallback.this.lambda$onStateChange$0(b, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStateChange$0(byte b, int i) {
            HdmiEarcController.this.mService.handleEarcStateChange(b, i);
        }

        public void onCapabilitiesReported(final byte[] bArr, final int i) {
            HdmiEarcController.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiEarcController$EarcAidlCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HdmiEarcController.EarcAidlCallback.this.lambda$onCapabilitiesReported$1(bArr, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCapabilitiesReported$1(byte[] bArr, int i) {
            HdmiEarcController.this.mService.handleEarcCapabilitiesReported(bArr, i);
        }

        public synchronized String getInterfaceHash() throws RemoteException {
            return "101230f18c7b8438921e517e80eea4ccc7c1e463";
        }
    }
}
