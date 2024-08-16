package com.android.server.pm;

import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Environment;
import android.os.FileUtils;
import android.os.RecoverySystem;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UserDataPreparer {
    private static final String TAG = "UserDataPreparer";
    private static final String XATTR_SERIAL = "user.serial";
    private final Context mContext;
    private final Object mInstallLock;
    private final Installer mInstaller;
    IUserDataPreparerExt userDataExt = (IUserDataPreparerExt) ExtLoader.type(IUserDataPreparerExt.class).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserDataPreparer(Installer installer, Object obj, Context context) {
        this.mInstallLock = obj;
        this.mContext = context;
        this.mInstaller = installer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareUserData(int i, int i2, int i3) {
        IUserDataPreparerExt iUserDataPreparerExt = this.userDataExt;
        if (iUserDataPreparerExt != null) {
            iUserDataPreparerExt.prepareUserData(i, i2, i3);
        }
        synchronized (this.mInstallLock) {
            StorageManager storageManager = (StorageManager) this.mContext.getSystemService(StorageManager.class);
            prepareUserDataLI(null, i, i2, i3, true);
            Iterator it = storageManager.getWritablePrivateVolumes().iterator();
            while (it.hasNext()) {
                String fsUuid = ((VolumeInfo) it.next()).getFsUuid();
                if (fsUuid != null) {
                    prepareUserDataLI(fsUuid, i, i2, i3, true);
                }
            }
        }
    }

    private void prepareUserDataLI(String str, int i, int i2, int i3, boolean z) {
        try {
            ((StorageManager) this.mContext.getSystemService(StorageManager.class)).prepareUserStorage(str, i, i2, i3);
            if ((i3 & 1) != 0) {
                enforceSerialNumber(getDataUserDeDirectory(str, i), i2);
                if (Objects.equals(str, StorageManager.UUID_PRIVATE_INTERNAL)) {
                    enforceSerialNumber(getDataSystemDeDirectory(i), i2);
                }
            }
            int i4 = i3 & 2;
            if (i4 != 0) {
                enforceSerialNumber(getDataUserCeDirectory(str, i), i2);
                if (Objects.equals(str, StorageManager.UUID_PRIVATE_INTERNAL)) {
                    enforceSerialNumber(getDataSystemCeDirectory(i), i2);
                }
            }
            this.mInstaller.createUserData(str, i, i2, i3);
            if (i4 == 0 || i != 0) {
                return;
            }
            String str2 = "sys.user." + i + ".ce_available";
            Slog.d(TAG, "Setting property: " + str2 + "=true");
            SystemProperties.set(str2, "true");
        } catch (Exception e) {
            PackageManagerServiceUtils.logCriticalInfo(5, "Destroying user " + i + " on volume " + str + " because we failed to prepare: " + e);
            destroyUserDataLI(str, i, i3);
            if (z) {
                prepareUserDataLI(str, i, i2, i3 | 1, false);
                return;
            }
            try {
                Log.wtf(TAG, "prepareUserData failed for user " + i, e);
                if (i == 0) {
                    RecoverySystem.rebootPromptAndWipeUserData(this.mContext, "prepareUserData failed for system user");
                }
            } catch (IOException e2) {
                throw new RuntimeException("error rebooting into recovery", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroyUserData(int i, int i2) {
        IUserDataPreparerExt iUserDataPreparerExt = this.userDataExt;
        if (iUserDataPreparerExt != null) {
            iUserDataPreparerExt.destroyUserData(i, i2);
        }
        synchronized (this.mInstallLock) {
            Iterator it = ((StorageManager) this.mContext.getSystemService(StorageManager.class)).getWritablePrivateVolumes().iterator();
            while (it.hasNext()) {
                String fsUuid = ((VolumeInfo) it.next()).getFsUuid();
                if (fsUuid != null) {
                    destroyUserDataLI(fsUuid, i, i2);
                }
            }
            destroyUserDataLI(null, i, i2);
        }
    }

    void destroyUserDataLI(String str, int i, int i2) {
        StorageManager storageManager = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        try {
            this.mInstaller.destroyUserData(str, i, i2);
            if (Objects.equals(str, StorageManager.UUID_PRIVATE_INTERNAL)) {
                if ((i2 & 1) != 0) {
                    FileUtils.deleteContentsAndDir(getUserSystemDirectory(i));
                    FileUtils.deleteContents(getDataSystemDeDirectory(i));
                }
                if ((i2 & 2) != 0) {
                    FileUtils.deleteContents(getDataSystemCeDirectory(i));
                }
            }
            storageManager.destroyUserStorage(str, i, i2);
        } catch (Exception e) {
            PackageManagerServiceUtils.logCriticalInfo(5, "Failed to destroy user " + i + " on volume " + str + ": " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reconcileUsers(String str, List<UserInfo> list) {
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, FileUtils.listFilesOrEmpty(Environment.getDataUserDeDirectory(str)));
        Collections.addAll(arrayList, FileUtils.listFilesOrEmpty(Environment.getDataUserCeDirectory(str)));
        Collections.addAll(arrayList, FileUtils.listFilesOrEmpty(Environment.getDataSystemDeDirectory()));
        Collections.addAll(arrayList, FileUtils.listFilesOrEmpty(Environment.getDataSystemCeDirectory()));
        Collections.addAll(arrayList, FileUtils.listFilesOrEmpty(Environment.getDataMiscCeDirectory()));
        reconcileUsers(str, list, arrayList);
    }

    @VisibleForTesting
    void reconcileUsers(String str, List<UserInfo> list, List<File> list2) {
        int size = list.size();
        SparseArray sparseArray = new SparseArray(size);
        for (int i = 0; i < size; i++) {
            UserInfo userInfo = list.get(i);
            sparseArray.put(userInfo.id, userInfo);
        }
        for (File file : list2) {
            if (file.isDirectory()) {
                try {
                    int parseInt = Integer.parseInt(file.getName());
                    UserInfo userInfo2 = (UserInfo) sparseArray.get(parseInt);
                    boolean z = true;
                    if (userInfo2 == null) {
                        PackageManagerServiceUtils.logCriticalInfo(5, "Destroying user directory " + file + " because no matching user was found");
                    } else {
                        try {
                            enforceSerialNumber(file, userInfo2.serialNumber);
                            z = false;
                        } catch (IOException e) {
                            PackageManagerServiceUtils.logCriticalInfo(5, "Destroying user directory " + file + " because we failed to enforce serial number: " + e);
                        }
                    }
                    if (z) {
                        IUserDataPreparerExt iUserDataPreparerExt = this.userDataExt;
                        if (iUserDataPreparerExt != null) {
                            iUserDataPreparerExt.reconcileUsers(str, list, list2);
                        }
                        synchronized (this.mInstallLock) {
                            destroyUserDataLI(str, parseInt, 3);
                        }
                    } else {
                        continue;
                    }
                } catch (NumberFormatException unused) {
                    Slog.w(TAG, "Invalid user directory " + file);
                }
            }
        }
    }

    @VisibleForTesting
    protected File getDataMiscCeDirectory(int i) {
        return Environment.getDataMiscCeDirectory(i);
    }

    @VisibleForTesting
    protected File getDataSystemCeDirectory(int i) {
        return Environment.getDataSystemCeDirectory(i);
    }

    @VisibleForTesting
    protected File getDataMiscDeDirectory(int i) {
        return Environment.getDataMiscDeDirectory(i);
    }

    @VisibleForTesting
    protected File getUserSystemDirectory(int i) {
        return Environment.getUserSystemDirectory(i);
    }

    @VisibleForTesting
    protected File getDataUserCeDirectory(String str, int i) {
        return Environment.getDataUserCeDirectory(str, i);
    }

    @VisibleForTesting
    protected File getDataSystemDeDirectory(int i) {
        return Environment.getDataSystemDeDirectory(i);
    }

    @VisibleForTesting
    protected File getDataUserDeDirectory(String str, int i) {
        return Environment.getDataUserDeDirectory(str, i);
    }

    void enforceSerialNumber(File file, int i) throws IOException {
        int serialNumber = getSerialNumber(file);
        Slog.v(TAG, "Found " + file + " with serial number " + serialNumber);
        if (serialNumber != -1) {
            if (serialNumber == i) {
                return;
            }
            throw new IOException("Found serial number " + serialNumber + " doesn't match expected " + i);
        }
        Slog.d(TAG, "Serial number missing on " + file + "; assuming current is valid");
        try {
            setSerialNumber(file, i);
        } catch (IOException e) {
            Slog.w(TAG, "Failed to set serial number on " + file, e);
        }
    }

    private static void setSerialNumber(File file, int i) throws IOException {
        try {
            Os.setxattr(file.getAbsolutePath(), XATTR_SERIAL, Integer.toString(i).getBytes(StandardCharsets.UTF_8), OsConstants.XATTR_CREATE);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    @VisibleForTesting
    static int getSerialNumber(File file) throws IOException {
        try {
            String str = new String(Os.getxattr(file.getAbsolutePath(), XATTR_SERIAL));
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException unused) {
                throw new IOException("Bad serial number: " + str);
            }
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.ENODATA) {
                return -1;
            }
            throw e.rethrowAsIOException();
        }
    }
}
