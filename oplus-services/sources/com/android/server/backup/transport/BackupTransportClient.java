package com.android.server.backup.transport;

import android.app.backup.IBackupManagerMonitor;
import android.app.backup.RestoreDescription;
import android.app.backup.RestoreSet;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.backup.IBackupTransport;
import com.android.internal.infra.AndroidFuture;
import com.android.server.backup.BackupAndRestoreFeatureFlags;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BackupTransportClient {
    private static final String TAG = "BackupTransportClient";
    private final IBackupTransport mTransportBinder;
    private final TransportStatusCallbackPool mCallbackPool = new TransportStatusCallbackPool();
    private final TransportFutures mTransportFutures = new TransportFutures();

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackupTransportClient(IBackupTransport iBackupTransport) {
        this.mTransportBinder = iBackupTransport;
    }

    public String name() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.name(newFuture);
        return (String) getFutureResult(newFuture);
    }

    public Intent configurationIntent() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.configurationIntent(newFuture);
        return (Intent) getFutureResult(newFuture);
    }

    public String currentDestinationString() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.currentDestinationString(newFuture);
        return (String) getFutureResult(newFuture);
    }

    public Intent dataManagementIntent() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.dataManagementIntent(newFuture);
        return (Intent) getFutureResult(newFuture);
    }

    public CharSequence dataManagementIntentLabel() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.dataManagementIntentLabel(newFuture);
        return (CharSequence) getFutureResult(newFuture);
    }

    public String transportDirName() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.transportDirName(newFuture);
        return (String) getFutureResult(newFuture);
    }

    public int initializeDevice() throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.initializeDevice(acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public int clearBackupData(PackageInfo packageInfo) throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.clearBackupData(packageInfo, acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public int finishBackup() throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.finishBackup(acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public long requestBackupTime() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.requestBackupTime(newFuture);
        Long l = (Long) getFutureResult(newFuture);
        if (l == null) {
            return -1000L;
        }
        return l.longValue();
    }

    public int performBackup(PackageInfo packageInfo, ParcelFileDescriptor parcelFileDescriptor, int i) throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.performBackup(packageInfo, parcelFileDescriptor, i, acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public List<RestoreSet> getAvailableRestoreSets() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getAvailableRestoreSets(newFuture);
        return (List) getFutureResult(newFuture);
    }

    public long getCurrentRestoreSet() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getCurrentRestoreSet(newFuture);
        Long l = (Long) getFutureResult(newFuture);
        if (l == null) {
            return -1000L;
        }
        return l.longValue();
    }

    public int startRestore(long j, PackageInfo[] packageInfoArr) throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.startRestore(j, packageInfoArr, acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public RestoreDescription nextRestorePackage() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.nextRestorePackage(newFuture);
        return (RestoreDescription) getFutureResult(newFuture);
    }

    public int getRestoreData(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.getRestoreData(parcelFileDescriptor, acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public void finishRestore() throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.finishRestore(acquire);
            acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public long requestFullBackupTime() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.requestFullBackupTime(newFuture);
        Long l = (Long) getFutureResult(newFuture);
        if (l == null) {
            return -1000L;
        }
        return l.longValue();
    }

    public int performFullBackup(PackageInfo packageInfo, ParcelFileDescriptor parcelFileDescriptor, int i) throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.performFullBackup(packageInfo, parcelFileDescriptor, i, acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public int checkFullBackupSize(long j) throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.checkFullBackupSize(j, acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public int sendBackupData(int i) throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        this.mTransportBinder.sendBackupData(i, acquire);
        try {
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public void cancelFullBackup() throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.cancelFullBackup(acquire);
            acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public boolean isAppEligibleForBackup(PackageInfo packageInfo, boolean z) throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.isAppEligibleForBackup(packageInfo, z, newFuture);
        Boolean bool = (Boolean) getFutureResult(newFuture);
        return bool != null && bool.booleanValue();
    }

    public long getBackupQuota(String str, boolean z) throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getBackupQuota(str, z, newFuture);
        Long l = (Long) getFutureResult(newFuture);
        if (l == null) {
            return -1000L;
        }
        return l.longValue();
    }

    public int getNextFullRestoreDataChunk(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.getNextFullRestoreDataChunk(parcelFileDescriptor, acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public int abortFullRestore() throws RemoteException {
        TransportStatusCallback acquire = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.abortFullRestore(acquire);
            return acquire.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(acquire);
        }
    }

    public int getTransportFlags() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getTransportFlags(newFuture);
        Integer num = (Integer) getFutureResult(newFuture);
        if (num == null) {
            return -1000;
        }
        return num.intValue();
    }

    public IBackupManagerMonitor getBackupManagerMonitor() throws RemoteException {
        AndroidFuture newFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getBackupManagerMonitor(newFuture);
        return IBackupManagerMonitor.Stub.asInterface((IBinder) getFutureResult(newFuture));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBecomingUnusable() {
        this.mCallbackPool.cancelActiveCallbacks();
        this.mTransportFutures.cancelActiveFutures();
    }

    private <T> T getFutureResult(AndroidFuture<T> androidFuture) {
        try {
            try {
                return (T) androidFuture.get(BackupAndRestoreFeatureFlags.getBackupTransportFutureTimeoutMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException | CancellationException | ExecutionException | TimeoutException e) {
                Slog.w(TAG, "Failed to get result from transport:", e);
                this.mTransportFutures.remove(androidFuture);
                return null;
            }
        } finally {
            this.mTransportFutures.remove(androidFuture);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class TransportFutures {
        private final Set<AndroidFuture<?>> mActiveFutures;
        private final Object mActiveFuturesLock;

        private TransportFutures() {
            this.mActiveFuturesLock = new Object();
            this.mActiveFutures = new HashSet();
        }

        <T> AndroidFuture<T> newFuture() {
            AndroidFuture<T> androidFuture = new AndroidFuture<>();
            synchronized (this.mActiveFuturesLock) {
                this.mActiveFutures.add(androidFuture);
            }
            return androidFuture;
        }

        <T> void remove(AndroidFuture<T> androidFuture) {
            synchronized (this.mActiveFuturesLock) {
                this.mActiveFutures.remove(androidFuture);
            }
        }

        void cancelActiveFutures() {
            synchronized (this.mActiveFuturesLock) {
                Iterator<AndroidFuture<?>> it = this.mActiveFutures.iterator();
                while (it.hasNext()) {
                    try {
                        it.next().cancel(true);
                    } catch (CancellationException unused) {
                    }
                }
                this.mActiveFutures.clear();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class TransportStatusCallbackPool {
        private static final int MAX_POOL_SIZE = 100;
        private final Set<TransportStatusCallback> mActiveCallbacks;
        private final Queue<TransportStatusCallback> mCallbackPool;
        private final Object mPoolLock;

        private TransportStatusCallbackPool() {
            this.mPoolLock = new Object();
            this.mCallbackPool = new ArrayDeque();
            this.mActiveCallbacks = new HashSet();
        }

        TransportStatusCallback acquire() {
            TransportStatusCallback poll;
            synchronized (this.mPoolLock) {
                poll = this.mCallbackPool.poll();
                if (poll == null) {
                    poll = new TransportStatusCallback();
                }
                poll.reset();
                this.mActiveCallbacks.add(poll);
            }
            return poll;
        }

        void recycle(TransportStatusCallback transportStatusCallback) {
            synchronized (this.mPoolLock) {
                this.mActiveCallbacks.remove(transportStatusCallback);
                if (this.mCallbackPool.size() > 100) {
                    Slog.d(BackupTransportClient.TAG, "TransportStatusCallback pool size exceeded");
                } else {
                    this.mCallbackPool.add(transportStatusCallback);
                }
            }
        }

        void cancelActiveCallbacks() {
            synchronized (this.mPoolLock) {
                for (TransportStatusCallback transportStatusCallback : this.mActiveCallbacks) {
                    try {
                        transportStatusCallback.onOperationCompleteWithStatus(-1000);
                        transportStatusCallback.getOperationStatus();
                    } catch (RemoteException unused) {
                    }
                    if (this.mCallbackPool.size() < 100) {
                        this.mCallbackPool.add(transportStatusCallback);
                    }
                }
                this.mActiveCallbacks.clear();
            }
        }
    }
}
