package com.android.server.companion.datatransfer.contextsync;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.companion.CompanionDeviceManagerServiceInternal;
import com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService;
import com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CallMetadataSyncConnectionService extends ConnectionService {
    private static final String TAG = "CallMetadataSyncConnectionService";

    @VisibleForTesting
    AudioManager mAudioManager;
    private CompanionDeviceManagerServiceInternal mCdmsi;

    @VisibleForTesting
    TelecomManager mTelecomManager;

    @VisibleForTesting
    final Map<CallMetadataSyncConnectionIdentifier, CallMetadataSyncConnection> mActiveConnections = new HashMap();

    @VisibleForTesting
    final CrossDeviceSyncControllerCallback mCrossDeviceSyncControllerCallback = new AnonymousClass1();

    /* renamed from: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class AnonymousClass1 extends CrossDeviceSyncControllerCallback {
        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback
        public void processContextSyncMessage(final int i, final CallMetadataSyncData callMetadataSyncData) {
            CallMetadataSyncConnectionIdentifier callMetadataSyncConnectionIdentifier;
            for (CallMetadataSyncData.Call call : callMetadataSyncData.getCalls()) {
                CallMetadataSyncConnection callMetadataSyncConnection = CallMetadataSyncConnectionService.this.mActiveConnections.get(new CallMetadataSyncConnectionIdentifier(i, call.getId()));
                if (callMetadataSyncConnection != null) {
                    callMetadataSyncConnection.update(call);
                } else {
                    Iterator<Map.Entry<CallMetadataSyncConnectionIdentifier, CallMetadataSyncConnection>> it = CallMetadataSyncConnectionService.this.mActiveConnections.entrySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            callMetadataSyncConnectionIdentifier = null;
                            break;
                        }
                        Map.Entry<CallMetadataSyncConnectionIdentifier, CallMetadataSyncConnection> next = it.next();
                        if (next.getValue().getAssociationId() == i && !next.getValue().isIdFinalized() && call.getId().endsWith(next.getValue().getCallId())) {
                            callMetadataSyncConnectionIdentifier = next.getKey();
                            break;
                        }
                    }
                    if (callMetadataSyncConnectionIdentifier != null) {
                        CallMetadataSyncConnection remove = CallMetadataSyncConnectionService.this.mActiveConnections.remove(callMetadataSyncConnectionIdentifier);
                        remove.update(call);
                        CallMetadataSyncConnectionService.this.mActiveConnections.put(new CallMetadataSyncConnectionIdentifier(i, call.getId()), remove);
                    }
                }
            }
            CallMetadataSyncConnectionService.this.mActiveConnections.values().removeIf(new Predicate() { // from class: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService$1$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$processContextSyncMessage$0;
                    lambda$processContextSyncMessage$0 = CallMetadataSyncConnectionService.AnonymousClass1.lambda$processContextSyncMessage$0(i, callMetadataSyncData, (CallMetadataSyncConnectionService.CallMetadataSyncConnection) obj);
                    return lambda$processContextSyncMessage$0;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$processContextSyncMessage$0(int i, CallMetadataSyncData callMetadataSyncData, CallMetadataSyncConnection callMetadataSyncConnection) {
            if (!callMetadataSyncConnection.isIdFinalized() || i != callMetadataSyncConnection.getAssociationId() || callMetadataSyncData.hasCall(callMetadataSyncConnection.getCallId())) {
                return false;
            }
            callMetadataSyncConnection.setDisconnected(new DisconnectCause(3));
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback
        public void cleanUpCallIds(final Set<String> set) {
            CallMetadataSyncConnectionService.this.mActiveConnections.values().removeIf(new Predicate() { // from class: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$cleanUpCallIds$1;
                    lambda$cleanUpCallIds$1 = CallMetadataSyncConnectionService.AnonymousClass1.lambda$cleanUpCallIds$1(set, (CallMetadataSyncConnectionService.CallMetadataSyncConnection) obj);
                    return lambda$cleanUpCallIds$1;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$cleanUpCallIds$1(Set set, CallMetadataSyncConnection callMetadataSyncConnection) {
            if (!set.contains(callMetadataSyncConnection.getCallId())) {
                return false;
            }
            callMetadataSyncConnection.setDisconnected(new DisconnectCause(3));
            return true;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mAudioManager = (AudioManager) getSystemService(AudioManager.class);
        this.mTelecomManager = (TelecomManager) getSystemService(TelecomManager.class);
        CompanionDeviceManagerServiceInternal companionDeviceManagerServiceInternal = (CompanionDeviceManagerServiceInternal) LocalServices.getService(CompanionDeviceManagerServiceInternal.class);
        this.mCdmsi = companionDeviceManagerServiceInternal;
        companionDeviceManagerServiceInternal.registerCallMetadataSyncCallback(this.mCrossDeviceSyncControllerCallback, 1);
    }

    @Override // android.telecom.ConnectionService
    public Connection onCreateIncomingConnection(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        int i = connectionRequest.getExtras().getInt("com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID");
        CallMetadataSyncData.Call fromBundle = CallMetadataSyncData.Call.fromBundle(connectionRequest.getExtras().getBundle("com.android.server.companion.datatransfer.contextsync.extra.CALL"));
        fromBundle.setDirection(1);
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.CALL");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.CALL_FACILITATOR_ID");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID");
        CallMetadataSyncConnection callMetadataSyncConnection = new CallMetadataSyncConnection(this.mTelecomManager, this.mAudioManager, i, fromBundle, new CallMetadataSyncConnectionCallback() { // from class: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.2
            @Override // com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionCallback
            void sendCallAction(int i2, String str, int i3) {
                CallMetadataSyncConnectionService.this.mCdmsi.sendCrossDeviceSyncMessage(i2, CrossDeviceSyncController.createCallControlMessage(str, i3));
            }
        });
        callMetadataSyncConnection.setConnectionProperties(16);
        callMetadataSyncConnection.setInitializing();
        return callMetadataSyncConnection;
    }

    @Override // android.telecom.ConnectionService
    public void onCreateIncomingConnectionFailed(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        Slog.e(TAG, "onCreateOutgoingConnectionFailed for: " + (phoneAccountHandle != null ? phoneAccountHandle.getId() : "unknown PhoneAccount"));
    }

    @Override // android.telecom.ConnectionService
    public Connection onCreateOutgoingConnection(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        String shortClassName;
        String packageName;
        if (phoneAccountHandle == null) {
            phoneAccountHandle = connectionRequest.getAccountHandle();
        }
        PhoneAccount phoneAccount = this.mTelecomManager.getPhoneAccount(phoneAccountHandle);
        CallMetadataSyncData.Call call = new CallMetadataSyncData.Call();
        call.setId(connectionRequest.getExtras().getString(CrossDeviceSyncController.EXTRA_CALL_ID));
        call.setStatus(0);
        if (phoneAccount != null) {
            shortClassName = phoneAccount.getLabel().toString();
        } else {
            shortClassName = phoneAccountHandle.getComponentName().getShortClassName();
        }
        if (phoneAccount != null) {
            packageName = phoneAccount.getExtras().getString("com.android.server.companion.datatransfer.contextsync.extra.CALL_FACILITATOR_ID");
        } else {
            packageName = phoneAccountHandle.getComponentName().getPackageName();
        }
        call.setFacilitator(new CallMetadataSyncData.CallFacilitator(shortClassName, packageName, phoneAccountHandle.getComponentName().flattenToString()));
        call.setDirection(2);
        call.setCallerId(connectionRequest.getAddress().getSchemeSpecificPart());
        int i = phoneAccount.getExtras().getInt("com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.CALL");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.CALL_FACILITATOR_ID");
        connectionRequest.getExtras().remove("com.android.server.companion.datatransfer.contextsync.extra.ASSOCIATION_ID");
        CallMetadataSyncConnection callMetadataSyncConnection = new CallMetadataSyncConnection(this.mTelecomManager, this.mAudioManager, i, call, new CallMetadataSyncConnectionCallback() { // from class: com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.3
            @Override // com.android.server.companion.datatransfer.contextsync.CallMetadataSyncConnectionService.CallMetadataSyncConnectionCallback
            void sendCallAction(int i2, String str, int i3) {
                CallMetadataSyncConnectionService.this.mCdmsi.sendCrossDeviceSyncMessage(i2, CrossDeviceSyncController.createCallControlMessage(str, i3));
            }
        });
        callMetadataSyncConnection.setCallerDisplayName(call.getCallerId(), 1);
        this.mCdmsi.addSelfOwnedCallId(call.getId());
        this.mCdmsi.sendCrossDeviceSyncMessage(i, CrossDeviceSyncController.createCallCreateMessage(call.getId(), connectionRequest.getAddress().toString(), call.getFacilitator().getIdentifier()));
        callMetadataSyncConnection.setInitializing();
        return callMetadataSyncConnection;
    }

    @Override // android.telecom.ConnectionService
    public void onCreateOutgoingConnectionFailed(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        Slog.e(TAG, "onCreateOutgoingConnectionFailed for: " + (phoneAccountHandle != null ? phoneAccountHandle.getId() : "unknown PhoneAccount"));
    }

    public void onCreateConnectionComplete(Connection connection) {
        if (connection instanceof CallMetadataSyncConnection) {
            CallMetadataSyncConnection callMetadataSyncConnection = (CallMetadataSyncConnection) connection;
            callMetadataSyncConnection.initialize();
            this.mActiveConnections.put(new CallMetadataSyncConnectionIdentifier(callMetadataSyncConnection.getAssociationId(), callMetadataSyncConnection.getCallId()), callMetadataSyncConnection);
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static final class CallMetadataSyncConnectionIdentifier {
        private final int mAssociationId;
        private final String mCallId;

        CallMetadataSyncConnectionIdentifier(int i, String str) {
            this.mAssociationId = i;
            this.mCallId = str;
        }

        public int getAssociationId() {
            return this.mAssociationId;
        }

        public String getCallId() {
            return this.mCallId;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mAssociationId), this.mCallId);
        }

        public boolean equals(Object obj) {
            String str;
            if (!(obj instanceof CallMetadataSyncConnectionIdentifier)) {
                return false;
            }
            CallMetadataSyncConnectionIdentifier callMetadataSyncConnectionIdentifier = (CallMetadataSyncConnectionIdentifier) obj;
            return callMetadataSyncConnectionIdentifier.getAssociationId() == this.mAssociationId && (str = this.mCallId) != null && str.equals(callMetadataSyncConnectionIdentifier.getCallId());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class CallMetadataSyncConnectionCallback {
        abstract void sendCallAction(int i, String str, int i2);

        CallMetadataSyncConnectionCallback() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CallMetadataSyncConnection extends Connection {
        private final int mAssociationId;
        private final AudioManager mAudioManager;
        private final CallMetadataSyncData.Call mCall;
        private final CallMetadataSyncConnectionCallback mCallback;
        private boolean mIsIdFinalized;
        private final TelecomManager mTelecomManager;

        CallMetadataSyncConnection(TelecomManager telecomManager, AudioManager audioManager, int i, CallMetadataSyncData.Call call, CallMetadataSyncConnectionCallback callMetadataSyncConnectionCallback) {
            this.mTelecomManager = telecomManager;
            this.mAudioManager = audioManager;
            this.mAssociationId = i;
            this.mCall = call;
            this.mCallback = callMetadataSyncConnectionCallback;
        }

        public String getCallId() {
            return this.mCall.getId();
        }

        public int getAssociationId() {
            return this.mAssociationId;
        }

        public boolean isIdFinalized() {
            return this.mIsIdFinalized;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initialize() {
            int status = this.mCall.getStatus();
            if (status == 4) {
                this.mTelecomManager.silenceRinger();
            }
            int convertStatusToState = CrossDeviceCall.convertStatusToState(status);
            if (convertStatusToState == 2) {
                setRinging();
            } else if (convertStatusToState == 4) {
                setActive();
            } else if (convertStatusToState == 3) {
                setOnHold();
            } else if (convertStatusToState == 7) {
                setDisconnected(new DisconnectCause(3));
            } else if (convertStatusToState == 1) {
                setDialing();
            } else {
                setInitialized();
            }
            String callerId = this.mCall.getCallerId();
            if (callerId != null) {
                setCallerDisplayName(callerId, 1);
                setAddress(Uri.fromParts("custom", this.mCall.getCallerId(), null), 1);
            }
            Bundle bundle = new Bundle();
            bundle.putString(CrossDeviceSyncController.EXTRA_CALL_ID, this.mCall.getId());
            putExtras(bundle);
            int connectionCapabilities = getConnectionCapabilities();
            int i = this.mCall.hasControl(7) ? connectionCapabilities | 1 : connectionCapabilities & (-2);
            int i2 = this.mCall.hasControl(4) ? i | 64 : i & (-65);
            this.mAudioManager.setMicrophoneMute(this.mCall.hasControl(5));
            if (i2 != getConnectionCapabilities()) {
                setConnectionCapabilities(i2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void update(CallMetadataSyncData.Call call) {
            boolean z = true;
            if (!this.mIsIdFinalized) {
                this.mCall.setId(call.getId());
                this.mIsIdFinalized = true;
            }
            int status = call.getStatus();
            if (status == 4 && this.mCall.getStatus() != 4) {
                this.mTelecomManager.silenceRinger();
            }
            this.mCall.setStatus(status);
            int convertStatusToState = CrossDeviceCall.convertStatusToState(status);
            if (convertStatusToState != getState()) {
                if (convertStatusToState == 2) {
                    setRinging();
                } else if (convertStatusToState == 4) {
                    setActive();
                } else if (convertStatusToState == 3) {
                    setOnHold();
                } else if (convertStatusToState == 7) {
                    setDisconnected(new DisconnectCause(3));
                } else if (convertStatusToState == 1) {
                    setDialing();
                } else {
                    Slog.e(CallMetadataSyncConnectionService.TAG, "Could not update call to unknown state");
                }
            }
            int connectionCapabilities = getConnectionCapabilities();
            this.mCall.setControls(call.getControls());
            int i = this.mCall.hasControl(7) || this.mCall.hasControl(8) ? connectionCapabilities | 1 : connectionCapabilities & (-2);
            if (!this.mCall.hasControl(4) && !this.mCall.hasControl(5)) {
                z = false;
            }
            int i2 = z ? i | 64 : i & (-65);
            this.mAudioManager.setMicrophoneMute(this.mCall.hasControl(5));
            if (i2 != getConnectionCapabilities()) {
                setConnectionCapabilities(i2);
            }
        }

        @Override // android.telecom.Connection
        public void onAnswer(int i) {
            sendCallAction(1);
        }

        @Override // android.telecom.Connection
        public void onReject() {
            sendCallAction(2);
        }

        @Override // android.telecom.Connection
        public void onReject(int i) {
            onReject();
        }

        @Override // android.telecom.Connection
        public void onReject(String str) {
            onReject();
        }

        @Override // android.telecom.Connection
        public void onSilence() {
            sendCallAction(3);
        }

        @Override // android.telecom.Connection
        public void onHold() {
            sendCallAction(7);
        }

        @Override // android.telecom.Connection
        public void onUnhold() {
            sendCallAction(8);
        }

        @Override // android.telecom.Connection
        public void onMuteStateChanged(boolean z) {
            sendCallAction(z ? 4 : 5);
        }

        @Override // android.telecom.Connection
        public void onDisconnect() {
            sendCallAction(6);
        }

        private void sendCallAction(int i) {
            this.mCallback.sendCallAction(this.mAssociationId, this.mCall.getId(), i);
        }
    }
}
