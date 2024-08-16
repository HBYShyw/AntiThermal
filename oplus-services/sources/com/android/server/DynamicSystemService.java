package com.android.server;

import android.annotation.EnforcePermission;
import android.annotation.RequiresNoPermission;
import android.content.Context;
import android.gsi.AvbPublicKey;
import android.gsi.GsiProgress;
import android.gsi.IGsiService;
import android.gsi.IGsiServiceCallback;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.image.IDynamicSystemService;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.util.Slog;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DynamicSystemService extends IDynamicSystemService.Stub {
    private static final int GSID_ROUGH_TIMEOUT_MS = 8192;
    private static final long MINIMUM_SD_MB = 30720;
    private static final String PATH_DEFAULT = "/data/gsi/";
    private static final String TAG = "DynamicSystemService";
    private Context mContext;
    private String mDsuSlot;
    private volatile IGsiService mGsiService;
    private String mInstallPath;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DynamicSystemService(Context context) {
        this.mContext = context;
    }

    private IGsiService getGsiService() {
        if (this.mGsiService != null) {
            return this.mGsiService;
        }
        return IGsiService.Stub.asInterface(ServiceManager.waitForService("gsiservice"));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class GsiServiceCallback extends IGsiServiceCallback.Stub {
        private int mResult = -1;

        GsiServiceCallback() {
        }

        @Override // android.gsi.IGsiServiceCallback
        public synchronized void onResult(int i) {
            this.mResult = i;
            notify();
        }

        public int getResult() {
            return this.mResult;
        }
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean startInstallation(String str) throws RemoteException {
        super.startInstallation_enforcePermission();
        IGsiService gsiService = getGsiService();
        this.mGsiService = gsiService;
        String str2 = SystemProperties.get("os.aot.path");
        if (str2.isEmpty()) {
            int myUserId = UserHandle.myUserId();
            for (VolumeInfo volumeInfo : ((StorageManager) this.mContext.getSystemService(StorageManager.class)).getVolumes()) {
                if (volumeInfo.getType() == 0 && volumeInfo.isMountedWritable() && "vfat".equalsIgnoreCase(volumeInfo.fsType)) {
                    long j = volumeInfo.getDisk().size >> 20;
                    Slog.i(TAG, volumeInfo.getPath() + ": " + j + " MB");
                    if (j < MINIMUM_SD_MB) {
                        Slog.i(TAG, volumeInfo.getPath() + ": insufficient storage");
                    } else {
                        File internalPathForUser = volumeInfo.getInternalPathForUser(myUserId);
                        if (internalPathForUser != null) {
                            str2 = new File(internalPathForUser, str).getPath();
                        }
                    }
                }
            }
            if (str2.isEmpty()) {
                str2 = PATH_DEFAULT + str;
            }
            Slog.i(TAG, "startInstallation -> " + str2);
        }
        this.mInstallPath = str2;
        this.mDsuSlot = str;
        if (gsiService.openInstall(str2) == 0) {
            return true;
        }
        Slog.i(TAG, "Failed to open " + str2);
        return false;
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public int createPartition(String str, long j, boolean z) throws RemoteException {
        super.createPartition_enforcePermission();
        int createPartition = getGsiService().createPartition(str, j, z);
        if (createPartition != 0) {
            Slog.i(TAG, "Failed to create partition: " + str);
        }
        return createPartition;
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean closePartition() throws RemoteException {
        super.closePartition_enforcePermission();
        if (getGsiService().closePartition() == 0) {
            return true;
        }
        Slog.i(TAG, "Partition installation completes with error");
        return false;
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean finishInstallation() throws RemoteException {
        super.finishInstallation_enforcePermission();
        if (getGsiService().closeInstall() == 0) {
            return true;
        }
        Slog.i(TAG, "Failed to finish installation");
        return false;
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public GsiProgress getInstallationProgress() throws RemoteException {
        super.getInstallationProgress_enforcePermission();
        return getGsiService().getInstallProgress();
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean abort() throws RemoteException {
        super.abort_enforcePermission();
        return getGsiService().cancelGsiInstall();
    }

    @RequiresNoPermission
    public boolean isInUse() {
        return SystemProperties.getBoolean("ro.gsid.image_running", false);
    }

    @RequiresNoPermission
    public boolean isInstalled() {
        boolean z = SystemProperties.getBoolean("gsid.image_installed", false);
        Slog.i(TAG, "isInstalled(): " + z);
        return z;
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean isEnabled() throws RemoteException {
        super.isEnabled_enforcePermission();
        return getGsiService().isGsiEnabled();
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean remove() throws RemoteException {
        super.remove_enforcePermission();
        try {
            GsiServiceCallback gsiServiceCallback = new GsiServiceCallback();
            synchronized (gsiServiceCallback) {
                getGsiService().removeGsiAsync(gsiServiceCallback);
                gsiServiceCallback.wait(8192L);
            }
            return gsiServiceCallback.getResult() == 0;
        } catch (InterruptedException unused) {
            Slog.e(TAG, "remove() was interrupted");
            return false;
        }
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean setEnable(boolean z, boolean z2) throws RemoteException {
        super.setEnable_enforcePermission();
        IGsiService gsiService = getGsiService();
        if (z) {
            try {
                if (this.mDsuSlot == null) {
                    this.mDsuSlot = gsiService.getActiveDsuSlot();
                }
                GsiServiceCallback gsiServiceCallback = new GsiServiceCallback();
                synchronized (gsiServiceCallback) {
                    gsiService.enableGsiAsync(z2, this.mDsuSlot, gsiServiceCallback);
                    gsiServiceCallback.wait(8192L);
                }
                return gsiServiceCallback.getResult() == 0;
            } catch (InterruptedException unused) {
                Slog.e(TAG, "setEnable() was interrupted");
                return false;
            }
        }
        return gsiService.disableGsi();
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean setAshmem(ParcelFileDescriptor parcelFileDescriptor, long j) {
        super.setAshmem_enforcePermission();
        try {
            return getGsiService().setGsiAshmem(parcelFileDescriptor, j);
        } catch (RemoteException e) {
            throw new RuntimeException(e.toString());
        }
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean submitFromAshmem(long j) {
        super.submitFromAshmem_enforcePermission();
        try {
            return getGsiService().commitGsiChunkFromAshmem(j);
        } catch (RemoteException e) {
            throw new RuntimeException(e.toString());
        }
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public boolean getAvbPublicKey(AvbPublicKey avbPublicKey) {
        super.getAvbPublicKey_enforcePermission();
        try {
            return getGsiService().getAvbPublicKey(avbPublicKey) == 0;
        } catch (RemoteException e) {
            throw new RuntimeException(e.toString());
        }
    }

    @EnforcePermission("android.permission.MANAGE_DYNAMIC_SYSTEM")
    public long suggestScratchSize() throws RemoteException {
        super.suggestScratchSize_enforcePermission();
        return getGsiService().suggestScratchSize();
    }
}
