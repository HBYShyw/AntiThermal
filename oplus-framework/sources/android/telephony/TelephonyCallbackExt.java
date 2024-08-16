package android.telephony;

import android.os.Binder;
import android.os.PersistableBundle;
import android.telephony.TelephonyCallbackExt;
import com.android.internal.telephony.IPhoneStateListenerExt;
import com.android.internal.util.FunctionalUtils;
import com.oplus.virtualcomm.VirtualCommServiceState;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class TelephonyCallbackExt {
    public static final int DEFAULT_PER_PID_REGISTRATION_LIMIT = 50;
    public static final int EVENT_IMS_REG_REMAIN_TIME_IND = 4;
    public static final int EVENT_NR_STATUS_STATE_CHANGED = 5;
    public static final int EVENT_PLMN_CARRIER_CONFIG_CHANGED = 1;
    public static final int EVENT_POLL_CS_PS_IN_SERVICE = 6;
    public static final int EVENT_REALTIME_OOS = 8;
    public static final int EVENT_SIM_RECOVERY = 7;
    public static final int EVENT_VIRTUALCOMM_ENABLED_CHANGED = 2;
    public static final int EVENT_VIRTUALCOMM_SERVICE_STATE_CHANGED = 3;
    public static final String FLAG_PER_PID_REGISTRATION_LIMIT = "phone_state_listener_per_pid_registration_limit";
    public static final boolean IS_REGISTRATION_LIMIT_ENABLED = true;
    public static final long PHONE_STATE_LISTENER_LIMIT_CHANGE_ID = 150880553;
    public IPhoneStateListenerExt callback;

    /* loaded from: classes.dex */
    public interface ImsRemainTimeListener {
        void onImsRemainTimeReported(String str);
    }

    /* loaded from: classes.dex */
    public interface NRStatusChangedListener {
        void onNRIconTypeChanged(int i, int i2);
    }

    /* loaded from: classes.dex */
    public interface PlmnCarrierConfigListener {
        void onPlmnCarrierConfigChanged(int i, PersistableBundle persistableBundle);
    }

    /* loaded from: classes.dex */
    public interface PollCsPsRspListener {
        void onPollCsPsInService(int i, int i2);
    }

    /* loaded from: classes.dex */
    public interface RealtimeOosListener {
        void onRealtimeOos(int i, boolean z);
    }

    /* loaded from: classes.dex */
    public interface SimRecoveryListener {
        void onSimRecovery(int i, int i2);
    }

    /* loaded from: classes.dex */
    public interface VirtualCommEnabledListener {
        void onVirtualcommEnabledChanged(boolean[] zArr);
    }

    /* loaded from: classes.dex */
    public interface VirtualCommServiceStateListener {
        void onVirtualcommServiceStateChanged(VirtualCommServiceState virtualCommServiceState);
    }

    public void init(Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("TelephonyCallbackExt Executor must be non-null");
        }
        this.callback = new IPhoneStateListenerStub(this, executor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class IPhoneStateListenerStub extends IPhoneStateListenerExt.Stub {
        private Executor mExecutor;
        private WeakReference<TelephonyCallbackExt> mTelephonyCallbackWeakRef;

        IPhoneStateListenerStub(TelephonyCallbackExt telephonyCallbackExt, Executor executor) {
            this.mTelephonyCallbackWeakRef = new WeakReference<>(telephonyCallbackExt);
            this.mExecutor = executor;
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onPlmnCarrierConfigChanged(final int slotId, final PersistableBundle result) {
            final PlmnCarrierConfigListener listener = (PlmnCarrierConfigListener) this.mTelephonyCallbackWeakRef.get();
            if (listener == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda5
                public final void runOrThrow() {
                    TelephonyCallbackExt.IPhoneStateListenerStub.this.lambda$onPlmnCarrierConfigChanged$1(listener, slotId, result);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlmnCarrierConfigChanged$1(final PlmnCarrierConfigListener listener, final int slotId, final PersistableBundle result) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyCallbackExt.PlmnCarrierConfigListener.this.onPlmnCarrierConfigChanged(slotId, result);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onNRIconTypeChanged(final int slotId, final int type) {
            final NRStatusChangedListener listener = (NRStatusChangedListener) this.mTelephonyCallbackWeakRef.get();
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda12
                public final void runOrThrow() {
                    TelephonyCallbackExt.IPhoneStateListenerStub.this.lambda$onNRIconTypeChanged$3(listener, slotId, type);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNRIconTypeChanged$3(final NRStatusChangedListener listener, final int slotId, final int type) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyCallbackExt.NRStatusChangedListener.this.onNRIconTypeChanged(slotId, type);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onVMEnabledChanged(final boolean[] enabled) {
            final VirtualCommEnabledListener listener = (VirtualCommEnabledListener) this.mTelephonyCallbackWeakRef.get();
            if (listener == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda11
                public final void runOrThrow() {
                    TelephonyCallbackExt.IPhoneStateListenerStub.this.lambda$onVMEnabledChanged$5(listener, enabled);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onVMEnabledChanged$5(final VirtualCommEnabledListener listener, final boolean[] enabled) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyCallbackExt.VirtualCommEnabledListener.this.onVirtualcommEnabledChanged(enabled);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onVMServiceStateChanged(final VirtualCommServiceState serviceState) {
            final VirtualCommServiceStateListener listener = (VirtualCommServiceStateListener) this.mTelephonyCallbackWeakRef.get();
            if (listener == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda7
                public final void runOrThrow() {
                    TelephonyCallbackExt.IPhoneStateListenerStub.this.lambda$onVMServiceStateChanged$7(listener, serviceState);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onVMServiceStateChanged$7(final VirtualCommServiceStateListener listener, final VirtualCommServiceState serviceState) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyCallbackExt.VirtualCommServiceStateListener.this.onVirtualcommServiceStateChanged(serviceState);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onImsRemainTimeReported(final String remainTimeData) {
            final ImsRemainTimeListener listener = (ImsRemainTimeListener) this.mTelephonyCallbackWeakRef.get();
            if (listener == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda6
                public final void runOrThrow() {
                    TelephonyCallbackExt.IPhoneStateListenerStub.this.lambda$onImsRemainTimeReported$9(listener, remainTimeData);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onImsRemainTimeReported$9(final ImsRemainTimeListener listener, final String remainTimeData) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyCallbackExt.ImsRemainTimeListener.this.onImsRemainTimeReported(remainTimeData);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onPollCsPsInService(final int slotId, final int domain) {
            final PollCsPsRspListener listener = (PollCsPsRspListener) this.mTelephonyCallbackWeakRef.get();
            if (listener == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda9
                public final void runOrThrow() {
                    TelephonyCallbackExt.IPhoneStateListenerStub.this.lambda$onPollCsPsInService$11(listener, slotId, domain);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPollCsPsInService$11(final PollCsPsRspListener listener, final int slotId, final int domain) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyCallbackExt.PollCsPsRspListener.this.onPollCsPsInService(slotId, domain);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onSimRecovery(final int slotId, final int stat) {
            final SimRecoveryListener listener = (SimRecoveryListener) this.mTelephonyCallbackWeakRef.get();
            if (listener == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda8
                public final void runOrThrow() {
                    TelephonyCallbackExt.IPhoneStateListenerStub.this.lambda$onSimRecovery$13(listener, slotId, stat);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSimRecovery$13(final SimRecoveryListener listener, final int slotId, final int stat) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyCallbackExt.SimRecoveryListener.this.onSimRecovery(slotId, stat);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListenerExt
        public void onRealtimeOos(final int slotId, final boolean oos) {
            final RealtimeOosListener listener = (RealtimeOosListener) this.mTelephonyCallbackWeakRef.get();
            if (listener == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda13
                public final void runOrThrow() {
                    TelephonyCallbackExt.IPhoneStateListenerStub.this.lambda$onRealtimeOos$15(listener, slotId, oos);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRealtimeOos$15(final RealtimeOosListener listener, final int slotId, final boolean oos) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.TelephonyCallbackExt$IPhoneStateListenerStub$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyCallbackExt.RealtimeOosListener.this.onRealtimeOos(slotId, oos);
                }
            });
        }
    }
}
