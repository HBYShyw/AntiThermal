package com.android.server.oemlock;

import android.content.Context;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.persistentdata.PersistentDataBlockManager;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class PersistentDataBlockLock extends OemLock {
    private static final String TAG = "OemLock";
    private Context mContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public String getLockName() {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PersistentDataBlockLock(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public void setOemUnlockAllowedByCarrier(boolean z, byte[] bArr) {
        if (bArr != null) {
            Slog.w(TAG, "Signature provided but is not being used");
        }
        UserManager.get(this.mContext).setUserRestriction("no_oem_unlock", !z, UserHandle.SYSTEM);
        if (z) {
            return;
        }
        disallowUnlockIfNotUnlocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public boolean isOemUnlockAllowedByCarrier() {
        return !UserManager.get(this.mContext).hasUserRestriction("no_oem_unlock", UserHandle.SYSTEM);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public void setOemUnlockAllowedByDevice(boolean z) {
        PersistentDataBlockManager persistentDataBlockManager = (PersistentDataBlockManager) this.mContext.getSystemService("persistent_data_block");
        if (persistentDataBlockManager == null) {
            Slog.w(TAG, "PersistentDataBlock is not supported on this device");
        } else {
            persistentDataBlockManager.setOemUnlockEnabled(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.oemlock.OemLock
    public boolean isOemUnlockAllowedByDevice() {
        PersistentDataBlockManager persistentDataBlockManager = (PersistentDataBlockManager) this.mContext.getSystemService("persistent_data_block");
        if (persistentDataBlockManager == null) {
            Slog.w(TAG, "PersistentDataBlock is not supported on this device");
            return false;
        }
        return persistentDataBlockManager.getOemUnlockEnabled();
    }

    private void disallowUnlockIfNotUnlocked() {
        PersistentDataBlockManager persistentDataBlockManager = (PersistentDataBlockManager) this.mContext.getSystemService("persistent_data_block");
        if (persistentDataBlockManager == null) {
            Slog.w(TAG, "PersistentDataBlock is not supported on this device");
        } else if (persistentDataBlockManager.getFlashLockState() != 0) {
            persistentDataBlockManager.setOemUnlockEnabled(false);
        }
    }
}
