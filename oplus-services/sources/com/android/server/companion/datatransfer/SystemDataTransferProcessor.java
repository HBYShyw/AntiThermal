package com.android.server.companion.datatransfer;

import android.app.PendingIntent;
import android.companion.AssociationInfo;
import android.companion.DeviceNotAssociatedException;
import android.companion.IOnMessageReceivedListener;
import android.companion.ISystemDataTransferCallback;
import android.companion.datatransfer.PermissionSyncRequest;
import android.companion.datatransfer.SystemDataTransferRequest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.permission.PermissionControllerManager;
import android.util.Slog;
import com.android.server.companion.AssociationStore;
import com.android.server.companion.CompanionDeviceManagerService;
import com.android.server.companion.PermissionsUtils;
import com.android.server.companion.Utils;
import com.android.server.companion.transport.CompanionTransportManager;
import com.android.server.companion.transport.Transport;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SystemDataTransferProcessor {
    private static final String EXTRA_COMPANION_DEVICE_NAME = "companion_device_name";
    private static final String EXTRA_PERMISSION_SYNC_REQUEST = "permission_sync_request";
    private static final String EXTRA_SYSTEM_DATA_TRANSFER_RESULT_RECEIVER = "system_data_transfer_result_receiver";
    private static final String LOG_TAG = "CDM_SystemDataTransferProcessor";
    private static final int RESULT_CODE_SYSTEM_DATA_TRANSFER_ALLOWED = 0;
    private static final int RESULT_CODE_SYSTEM_DATA_TRANSFER_DISALLOWED = 1;
    private static final ComponentName SYSTEM_DATA_TRANSFER_REQUEST_APPROVAL_ACTIVITY = ComponentName.createRelative("com.android.companiondevicemanager", ".CompanionDeviceDataTransferActivity");
    private final AssociationStore mAssociationStore;
    private final Context mContext;
    private final ExecutorService mExecutor;
    private final ResultReceiver mOnSystemDataTransferRequestConfirmationReceiver = new ResultReceiver(Handler.getMain()) { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor.2
        @Override // android.os.ResultReceiver
        protected void onReceiveResult(int i, Bundle bundle) {
            Slog.d(SystemDataTransferProcessor.LOG_TAG, "onReceiveResult() code=" + i + ", data=" + bundle);
            if (i == 0 || i == 1) {
                SystemDataTransferRequest systemDataTransferRequest = (PermissionSyncRequest) bundle.getParcelable(SystemDataTransferProcessor.EXTRA_PERMISSION_SYNC_REQUEST, PermissionSyncRequest.class);
                if (systemDataTransferRequest != null) {
                    systemDataTransferRequest.setUserConsented(i == 0);
                    Slog.i(SystemDataTransferProcessor.LOG_TAG, "Recording request: " + systemDataTransferRequest);
                    SystemDataTransferProcessor.this.mSystemDataTransferRequestStore.writeRequest(systemDataTransferRequest.getUserId(), systemDataTransferRequest);
                    return;
                }
                return;
            }
            Slog.e(SystemDataTransferProcessor.LOG_TAG, "Unknown result code:" + i);
        }
    };
    private final PermissionControllerManager mPermissionControllerManager;
    private final SystemDataTransferRequestStore mSystemDataTransferRequestStore;
    private final CompanionTransportManager mTransportManager;

    public SystemDataTransferProcessor(CompanionDeviceManagerService companionDeviceManagerService, AssociationStore associationStore, SystemDataTransferRequestStore systemDataTransferRequestStore, CompanionTransportManager companionTransportManager) {
        Context context = companionDeviceManagerService.getContext();
        this.mContext = context;
        this.mAssociationStore = associationStore;
        this.mSystemDataTransferRequestStore = systemDataTransferRequestStore;
        this.mTransportManager = companionTransportManager;
        companionTransportManager.addListener(Transport.MESSAGE_REQUEST_PERMISSION_RESTORE, new IOnMessageReceivedListener() { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor.1
            public IBinder asBinder() {
                return null;
            }

            public void onMessageReceived(int i, byte[] bArr) throws RemoteException {
                SystemDataTransferProcessor.this.onReceivePermissionRestore(bArr);
            }
        });
        this.mPermissionControllerManager = (PermissionControllerManager) context.getSystemService(PermissionControllerManager.class);
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    private AssociationInfo resolveAssociation(String str, int i, int i2) {
        AssociationInfo sanitizeWithCallerChecks = PermissionsUtils.sanitizeWithCallerChecks(this.mContext, this.mAssociationStore.getAssociationById(i2));
        if (sanitizeWithCallerChecks != null) {
            return sanitizeWithCallerChecks;
        }
        throw new DeviceNotAssociatedException("Association " + i2 + " is not associated with the app " + str + " for user " + i);
    }

    public PendingIntent buildPermissionTransferUserConsentIntent(String str, int i, int i2) {
        AssociationInfo resolveAssociation = resolveAssociation(str, i, i2);
        Slog.i(LOG_TAG, "Creating permission sync intent for userId [" + i + "] associationId [" + i2 + "]");
        Bundle bundle = new Bundle();
        PermissionSyncRequest permissionSyncRequest = new PermissionSyncRequest(i2);
        permissionSyncRequest.setUserId(i);
        bundle.putParcelable(EXTRA_PERMISSION_SYNC_REQUEST, permissionSyncRequest);
        bundle.putCharSequence(EXTRA_COMPANION_DEVICE_NAME, resolveAssociation.getDisplayName());
        bundle.putParcelable(EXTRA_SYSTEM_DATA_TRANSFER_RESULT_RECEIVER, Utils.prepareForIpc(this.mOnSystemDataTransferRequestConfirmationReceiver));
        Intent intent = new Intent();
        intent.setComponent(SYSTEM_DATA_TRANSFER_REQUEST_APPROVAL_ACTIVITY);
        intent.putExtras(bundle);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return PendingIntent.getActivityAsUser(this.mContext, i2, intent, 1409286144, null, UserHandle.CURRENT);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void startSystemDataTransfer(String str, int i, final int i2, final ISystemDataTransferCallback iSystemDataTransferCallback) {
        boolean z;
        Slog.i(LOG_TAG, "Start system data transfer for package [" + str + "] userId [" + i + "] associationId [" + i2 + "]");
        resolveAssociation(str, i, i2);
        Iterator<SystemDataTransferRequest> it = this.mSystemDataTransferRequestStore.readRequestsByAssociationId(i, i2).iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            SystemDataTransferRequest next = it.next();
            if ((next instanceof PermissionSyncRequest) && next.isUserConsented()) {
                z = true;
                break;
            }
        }
        if (!z) {
            String str2 = "User " + i + " hasn't consented permission sync.";
            Slog.e(LOG_TAG, str2);
            try {
                iSystemDataTransferCallback.onError(str2);
                return;
            } catch (RemoteException unused) {
                return;
            }
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mPermissionControllerManager.getRuntimePermissionBackup(UserHandle.of(i), this.mExecutor, new Consumer() { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    SystemDataTransferProcessor.this.lambda$startSystemDataTransfer$0(i2, iSystemDataTransferCallback, (byte[]) obj);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSystemDataTransfer$0(int i, ISystemDataTransferCallback iSystemDataTransferCallback, byte[] bArr) {
        translateFutureToCallback(this.mTransportManager.requestPermissionRestore(i, bArr), iSystemDataTransferCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReceivePermissionRestore(byte[] bArr) {
        Slog.i(LOG_TAG, "Applying permissions.");
        UserHandle user = this.mContext.getUser();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mPermissionControllerManager.stageAndApplyRuntimePermissionsBackup(bArr, user);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private static void translateFutureToCallback(Future<?> future, ISystemDataTransferCallback iSystemDataTransferCallback) {
        try {
            try {
                future.get(15L, TimeUnit.SECONDS);
                if (iSystemDataTransferCallback != null) {
                    iSystemDataTransferCallback.onResult();
                }
            } catch (Exception e) {
                if (iSystemDataTransferCallback != null) {
                    iSystemDataTransferCallback.onError(e.getMessage());
                }
            }
        } catch (RemoteException unused) {
        }
    }
}
