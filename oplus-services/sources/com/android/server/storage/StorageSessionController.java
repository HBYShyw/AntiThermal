package com.android.server.storage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.os.IVold;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.VolumeInfo;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class StorageSessionController {
    private static final String TAG = "StorageSessionController";
    private final Context mContext;
    private volatile int mExternalStorageServiceAppId;
    private volatile ComponentName mExternalStorageServiceComponent;
    private volatile String mExternalStorageServicePackageName;
    private volatile boolean mIsResetting;
    private final UserManager mUserManager;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final SparseArray<StorageUserConnection> mConnections = new SparseArray<>();

    public StorageSessionController(Context context) {
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public int getConnectionUserIdForVolume(VolumeInfo volumeInfo) {
        boolean isMediaSharedWithParent = ((UserManager) this.mContext.createContextAsUser(UserHandle.of(volumeInfo.mountUserId), 0).getSystemService(UserManager.class)).isMediaSharedWithParent();
        UserInfo userInfo = this.mUserManager.getUserInfo(volumeInfo.mountUserId);
        if (userInfo != null && isMediaSharedWithParent) {
            return userInfo.profileGroupId;
        }
        return volumeInfo.mountUserId;
    }

    public void onVolumeMount(ParcelFileDescriptor parcelFileDescriptor, VolumeInfo volumeInfo) throws ExternalStorageServiceException {
        if (shouldHandle(volumeInfo)) {
            Slog.i(TAG, "On volume mount " + volumeInfo);
            String id = volumeInfo.getId();
            int connectionUserIdForVolume = getConnectionUserIdForVolume(volumeInfo);
            synchronized (this.mLock) {
                StorageUserConnection storageUserConnection = this.mConnections.get(connectionUserIdForVolume);
                if (storageUserConnection == null) {
                    Slog.i(TAG, "Creating connection for user: " + connectionUserIdForVolume);
                    storageUserConnection = new StorageUserConnection(this.mContext, connectionUserIdForVolume, this);
                    this.mConnections.put(connectionUserIdForVolume, storageUserConnection);
                }
                Slog.i(TAG, "Creating and starting session with id: " + id);
                storageUserConnection.startSession(id, parcelFileDescriptor, volumeInfo.getPath().getPath(), volumeInfo.getInternalPath().getPath());
                Slog.i(TAG, "onVolumeMount session:" + id + " started!");
            }
        }
    }

    public void notifyVolumeStateChanged(VolumeInfo volumeInfo) throws ExternalStorageServiceException {
        if (shouldHandle(volumeInfo)) {
            String id = volumeInfo.getId();
            int connectionUserIdForVolume = getConnectionUserIdForVolume(volumeInfo);
            synchronized (this.mLock) {
                StorageUserConnection storageUserConnection = this.mConnections.get(connectionUserIdForVolume);
                if (storageUserConnection != null) {
                    Slog.i(TAG, "Notifying volume state changed for session with id: " + id);
                    storageUserConnection.notifyVolumeStateChanged(id, volumeInfo.buildStorageVolume(this.mContext, volumeInfo.getMountUserId(), false));
                } else {
                    Slog.w(TAG, "No available storage user connection for userId : " + connectionUserIdForVolume);
                }
            }
        }
    }

    public void freeCache(String str, long j) throws ExternalStorageServiceException {
        synchronized (this.mLock) {
            int size = this.mConnections.size();
            for (int i = 0; i < size; i++) {
                StorageUserConnection storageUserConnection = this.mConnections.get(this.mConnections.keyAt(i));
                if (storageUserConnection != null) {
                    storageUserConnection.freeCache(str, j);
                }
            }
        }
    }

    public void notifyAnrDelayStarted(String str, int i, int i2, int i3) throws ExternalStorageServiceException {
        StorageUserConnection storageUserConnection;
        int userId = UserHandle.getUserId(i);
        synchronized (this.mLock) {
            storageUserConnection = this.mConnections.get(userId);
        }
        if (storageUserConnection != null) {
            storageUserConnection.notifyAnrDelayStarted(str, i, i2, i3);
        }
    }

    public StorageUserConnection onVolumeRemove(VolumeInfo volumeInfo) {
        if (!shouldHandle(volumeInfo)) {
            return null;
        }
        Slog.i(TAG, "On volume remove " + volumeInfo);
        String id = volumeInfo.getId();
        int connectionUserIdForVolume = getConnectionUserIdForVolume(volumeInfo);
        synchronized (this.mLock) {
            StorageUserConnection storageUserConnection = this.mConnections.get(connectionUserIdForVolume);
            if (storageUserConnection != null) {
                Slog.i(TAG, "Removed session for vol with id: " + id);
                if (volumeInfo.type == 0) {
                    Slog.i(TAG, "removeSessionAndWait public volume");
                    try {
                        storageUserConnection.removeSessionAndWait(id);
                    } catch (ExternalStorageServiceException e) {
                        Slog.e(TAG, "Failed to end session for vol with id: " + id, e);
                    }
                } else {
                    storageUserConnection.removeSession(id);
                }
                return storageUserConnection;
            }
            Slog.w(TAG, "Session already removed for vol with id: " + id);
            return null;
        }
    }

    public void onVolumeUnmount(VolumeInfo volumeInfo) {
        StorageUserConnection onVolumeRemove = onVolumeRemove(volumeInfo);
        Slog.i(TAG, "On volume unmount " + volumeInfo);
        if (onVolumeRemove != null) {
            String id = volumeInfo.getId();
            try {
                onVolumeRemove.removeSessionAndWait(id);
            } catch (ExternalStorageServiceException e) {
                Slog.e(TAG, "Failed to end session for vol with id: " + id, e);
            }
        }
    }

    public void onUnlockUser(int i) throws ExternalStorageServiceException {
        Slog.i(TAG, "On user unlock " + i);
        if (i == 0 || i == 888) {
            initExternalStorageServiceComponent(i);
        }
    }

    public void onUserStopping(int i) {
        StorageUserConnection storageUserConnection;
        if (shouldHandle(null)) {
            synchronized (this.mLock) {
                storageUserConnection = this.mConnections.get(i);
            }
            if (storageUserConnection != null) {
                Slog.i(TAG, "Removing all sessions for user: " + i);
                storageUserConnection.removeAllSessions();
                return;
            }
            Slog.w(TAG, "No connection found for user: " + i);
        }
    }

    public void onReset(IVold iVold, Runnable runnable) {
        if (shouldHandle(null)) {
            SparseArray sparseArray = new SparseArray();
            synchronized (this.mLock) {
                this.mIsResetting = true;
                Slog.i(TAG, "Started resetting external storage service...");
                for (int i = 0; i < this.mConnections.size(); i++) {
                    sparseArray.put(this.mConnections.keyAt(i), this.mConnections.valueAt(i));
                }
            }
            for (int i2 = 0; i2 < sparseArray.size(); i2++) {
                StorageUserConnection storageUserConnection = (StorageUserConnection) sparseArray.valueAt(i2);
                for (String str : storageUserConnection.getAllSessionIds()) {
                    try {
                        Slog.i(TAG, "Unmounting " + str);
                        iVold.unmount(str);
                        Slog.i(TAG, "Unmounted " + str);
                    } catch (ServiceSpecificException | RemoteException | NullPointerException e) {
                        Slog.e(TAG, "Failed to unmount volume: " + str, e);
                    }
                    try {
                        Slog.i(TAG, "Exiting " + str);
                        storageUserConnection.removeSessionAndWait(str);
                        Slog.i(TAG, "Exited " + str);
                    } catch (ExternalStorageServiceException | IllegalStateException e2) {
                        Slog.e(TAG, "Failed to exit session: " + str + ". Killing MediaProvider...", e2);
                        killExternalStorageService(sparseArray.keyAt(i2));
                    }
                }
                storageUserConnection.close();
            }
            runnable.run();
            synchronized (this.mLock) {
                this.mConnections.clear();
                this.mIsResetting = false;
                Slog.i(TAG, "Finished resetting external storage service");
            }
        }
    }

    private void initExternalStorageServiceComponent(int i) throws ExternalStorageServiceException {
        ProviderInfo resolveContentProvider;
        Slog.i(TAG, "Initialialising...");
        if (i == 888) {
            this.mContext.createContextAsUser(UserHandle.of(i), 0);
            resolveContentProvider = this.mContext.getPackageManager().resolveContentProviderAsUser("media", 1835008, 888);
        } else {
            resolveContentProvider = this.mContext.getPackageManager().resolveContentProvider("media", 1835008);
        }
        if (resolveContentProvider == null) {
            throw new ExternalStorageServiceException("No valid MediaStore provider found");
        }
        this.mExternalStorageServicePackageName = resolveContentProvider.applicationInfo.packageName;
        this.mExternalStorageServiceAppId = UserHandle.getAppId(resolveContentProvider.applicationInfo.uid);
        ServiceInfo resolveExternalStorageServiceAsUser = resolveExternalStorageServiceAsUser(i);
        if (resolveExternalStorageServiceAsUser == null) {
            throw new ExternalStorageServiceException("No valid ExternalStorageService component found");
        }
        ComponentName componentName = new ComponentName(resolveExternalStorageServiceAsUser.packageName, resolveExternalStorageServiceAsUser.name);
        if (!"android.permission.BIND_EXTERNAL_STORAGE_SERVICE".equals(resolveExternalStorageServiceAsUser.permission)) {
            throw new ExternalStorageServiceException(componentName.flattenToShortString() + " does not require permission android.permission.BIND_EXTERNAL_STORAGE_SERVICE");
        }
        this.mExternalStorageServiceComponent = componentName;
    }

    public ComponentName getExternalStorageServiceComponentName() {
        return this.mExternalStorageServiceComponent;
    }

    public void notifyAppIoBlocked(String str, int i, int i2, int i3) {
        StorageUserConnection storageUserConnection;
        int userId = UserHandle.getUserId(i);
        synchronized (this.mLock) {
            storageUserConnection = this.mConnections.get(userId);
        }
        if (storageUserConnection != null) {
            storageUserConnection.notifyAppIoBlocked(str, i, i2, i3);
        }
    }

    public void notifyAppIoResumed(String str, int i, int i2, int i3) {
        StorageUserConnection storageUserConnection;
        int userId = UserHandle.getUserId(i);
        synchronized (this.mLock) {
            storageUserConnection = this.mConnections.get(userId);
        }
        if (storageUserConnection != null) {
            storageUserConnection.notifyAppIoResumed(str, i, i2, i3);
        }
    }

    public boolean isAppIoBlocked(int i) {
        StorageUserConnection storageUserConnection;
        int userId = UserHandle.getUserId(i);
        synchronized (this.mLock) {
            storageUserConnection = this.mConnections.get(userId);
        }
        if (storageUserConnection != null) {
            return storageUserConnection.isAppIoBlocked(i);
        }
        return false;
    }

    private void killExternalStorageService(int i) {
        try {
            ActivityManager.getService().killApplication(this.mExternalStorageServicePackageName, this.mExternalStorageServiceAppId, i, "storage_session_controller reset", 13);
        } catch (RemoteException unused) {
            Slog.i(TAG, "Failed to kill the ExtenalStorageService for user " + i);
        }
    }

    public static boolean isEmulatedOrPublic(VolumeInfo volumeInfo) {
        int i = volumeInfo.type;
        return i == 2 || (i == 0 && volumeInfo.isVisible());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ExternalStorageServiceException extends Exception {
        public ExternalStorageServiceException(Throwable th) {
            super(th);
        }

        public ExternalStorageServiceException(String str) {
            super(str);
        }

        public ExternalStorageServiceException(String str, Throwable th) {
            super(str, th);
        }
    }

    private static boolean isSupportedVolume(VolumeInfo volumeInfo) {
        return isEmulatedOrPublic(volumeInfo) || volumeInfo.type == 5;
    }

    private boolean shouldHandle(VolumeInfo volumeInfo) {
        return !this.mIsResetting && (volumeInfo == null || isSupportedVolume(volumeInfo));
    }

    public boolean supportsExternalStorage(int i) {
        return resolveExternalStorageServiceAsUser(i) != null;
    }

    private ServiceInfo resolveExternalStorageServiceAsUser(int i) {
        Intent intent = new Intent("android.service.storage.ExternalStorageService");
        intent.setPackage(this.mExternalStorageServicePackageName);
        ResolveInfo resolveServiceAsUser = this.mContext.getPackageManager().resolveServiceAsUser(intent, 132, i);
        if (resolveServiceAsUser == null) {
            return null;
        }
        return resolveServiceAsUser.serviceInfo;
    }
}
