package com.android.server.backup.restore;

import android.app.IBackupAgent;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.android.server.backup.FileMetadata;
import com.android.server.backup.UserBackupManagerService;
import java.io.IOException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class RestoreFileRunnable implements Runnable {
    private final IBackupAgent mAgent;
    private final UserBackupManagerService mBackupManagerService;
    private final FileMetadata mInfo;
    private final ParcelFileDescriptor mSocket;
    private final int mToken;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RestoreFileRunnable(UserBackupManagerService userBackupManagerService, IBackupAgent iBackupAgent, FileMetadata fileMetadata, ParcelFileDescriptor parcelFileDescriptor, int i) throws IOException {
        this.mAgent = iBackupAgent;
        this.mInfo = fileMetadata;
        this.mToken = i;
        this.mSocket = ParcelFileDescriptor.dup(parcelFileDescriptor.getFileDescriptor());
        this.mBackupManagerService = userBackupManagerService;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            IBackupAgent iBackupAgent = this.mAgent;
            ParcelFileDescriptor parcelFileDescriptor = this.mSocket;
            FileMetadata fileMetadata = this.mInfo;
            iBackupAgent.doRestoreFile(parcelFileDescriptor, fileMetadata.size, fileMetadata.type, fileMetadata.domain, fileMetadata.path, fileMetadata.mode, fileMetadata.mtime, this.mToken, this.mBackupManagerService.getBackupManagerBinder());
        } catch (RemoteException unused) {
        }
    }
}
