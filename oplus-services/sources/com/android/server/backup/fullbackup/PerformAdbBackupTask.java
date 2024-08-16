package com.android.server.backup.fullbackup;

import android.app.backup.IFullBackupRestoreObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.AppWidgetBackupBridge;
import com.android.server.backup.BackupManagerService;
import com.android.server.backup.BackupPasswordManager;
import com.android.server.backup.BackupRestoreTask;
import com.android.server.backup.KeyValueAdbBackupEngine;
import com.android.server.backup.OperationStorage;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.utils.BackupEligibilityRules;
import com.android.server.backup.utils.PasswordUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PerformAdbBackupTask extends FullBackupTask implements BackupRestoreTask {
    private final boolean mAllApps;
    private final BackupEligibilityRules mBackupEligibilityRules;
    private final boolean mCompress;
    private final int mCurrentOpToken;
    private final String mCurrentPassword;
    private PackageInfo mCurrentTarget;
    private final boolean mDoWidgets;
    private final String mEncryptPassword;
    private final boolean mIncludeApks;
    private final boolean mIncludeObbs;
    private final boolean mIncludeShared;
    private final boolean mIncludeSystem;
    private final boolean mKeyValue;
    private final AtomicBoolean mLatch;
    private final OperationStorage mOperationStorage;
    private final ParcelFileDescriptor mOutputFile;
    private final ArrayList<String> mPackages;
    private final UserBackupManagerService mUserBackupManagerService;

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long j) {
    }

    public PerformAdbBackupTask(UserBackupManagerService userBackupManagerService, OperationStorage operationStorage, ParcelFileDescriptor parcelFileDescriptor, IFullBackupRestoreObserver iFullBackupRestoreObserver, boolean z, boolean z2, boolean z3, boolean z4, String str, String str2, boolean z5, boolean z6, boolean z7, boolean z8, String[] strArr, AtomicBoolean atomicBoolean, BackupEligibilityRules backupEligibilityRules) {
        super(iFullBackupRestoreObserver);
        ArrayList<String> arrayList;
        this.mUserBackupManagerService = userBackupManagerService;
        this.mOperationStorage = operationStorage;
        this.mCurrentOpToken = userBackupManagerService.generateRandomIntegerToken();
        this.mLatch = atomicBoolean;
        this.mOutputFile = parcelFileDescriptor;
        this.mIncludeApks = z;
        this.mIncludeObbs = z2;
        this.mIncludeShared = z3;
        this.mDoWidgets = z4;
        this.mAllApps = z5;
        this.mIncludeSystem = z6;
        if (strArr == null) {
            arrayList = new ArrayList<>();
        } else {
            arrayList = new ArrayList<>(Arrays.asList(strArr));
        }
        this.mPackages = arrayList;
        this.mCurrentPassword = str;
        if (str2 == null || "".equals(str2)) {
            this.mEncryptPassword = str;
        } else {
            this.mEncryptPassword = str2;
        }
        this.mCompress = z7;
        this.mKeyValue = z8;
        this.mBackupEligibilityRules = backupEligibilityRules;
    }

    private void addPackagesToSet(TreeMap<String, PackageInfo> treeMap, List<String> list) {
        for (String str : list) {
            if (!treeMap.containsKey(str)) {
                try {
                    treeMap.put(str, this.mUserBackupManagerService.getPackageManager().getPackageInfo(str, AudioFormat.OPUS));
                } catch (PackageManager.NameNotFoundException unused) {
                    Slog.w(BackupManagerService.TAG, "Unknown package " + str + ", skipping");
                }
            }
        }
    }

    private OutputStream emitAesBackupHeader(StringBuilder sb, OutputStream outputStream) throws Exception {
        byte[] randomBytes = this.mUserBackupManagerService.randomBytes(512);
        SecretKey buildPasswordKey = PasswordUtils.buildPasswordKey(BackupPasswordManager.PBKDF_CURRENT, this.mEncryptPassword, randomBytes, 10000);
        byte[] bArr = new byte[32];
        this.mUserBackupManagerService.getRng().nextBytes(bArr);
        byte[] randomBytes2 = this.mUserBackupManagerService.randomBytes(512);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        cipher.init(1, secretKeySpec);
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
        sb.append(PasswordUtils.ENCRYPTION_ALGORITHM_NAME);
        sb.append('\n');
        sb.append(PasswordUtils.byteArrayToHex(randomBytes));
        sb.append('\n');
        sb.append(PasswordUtils.byteArrayToHex(randomBytes2));
        sb.append('\n');
        sb.append(10000);
        sb.append('\n');
        Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher2.init(1, buildPasswordKey);
        sb.append(PasswordUtils.byteArrayToHex(cipher2.getIV()));
        sb.append('\n');
        byte[] iv = cipher.getIV();
        byte[] encoded = secretKeySpec.getEncoded();
        byte[] makeKeyChecksum = PasswordUtils.makeKeyChecksum(BackupPasswordManager.PBKDF_CURRENT, secretKeySpec.getEncoded(), randomBytes2, 10000);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(iv.length + encoded.length + makeKeyChecksum.length + 3);
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeByte(iv.length);
        dataOutputStream.write(iv);
        dataOutputStream.writeByte(encoded.length);
        dataOutputStream.write(encoded);
        dataOutputStream.writeByte(makeKeyChecksum.length);
        dataOutputStream.write(makeKeyChecksum);
        dataOutputStream.flush();
        sb.append(PasswordUtils.byteArrayToHex(cipher2.doFinal(byteArrayOutputStream.toByteArray())));
        sb.append('\n');
        return cipherOutputStream;
    }

    private void finalizeBackup(OutputStream outputStream) {
        try {
            outputStream.write(new byte[1024]);
        } catch (IOException unused) {
            Slog.w(BackupManagerService.TAG, "Error attempting to finalize backup stream");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x0448 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x041e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0405 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x03db A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x049c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0472 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0130 A[Catch: all -> 0x0120, Exception -> 0x03d0, RemoteException -> 0x0414, TRY_ENTER, TRY_LEAVE, TryCatch #1 {all -> 0x0120, blocks: (B:274:0x0118, B:53:0x0130, B:77:0x019e, B:80:0x01bb), top: B:273:0x0118 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0180 A[Catch: all -> 0x03cb, Exception -> 0x03d0, RemoteException -> 0x0414, TRY_ENTER, TRY_LEAVE, TryCatch #27 {all -> 0x03cb, blocks: (B:48:0x0114, B:51:0x0126, B:71:0x0180, B:74:0x0199, B:78:0x01aa, B:249:0x01a3), top: B:47:0x0114 }] */
    /* JADX WARN: Unreachable blocks removed: 1, instructions: 1 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void run() {
        boolean z;
        Throwable th;
        String str;
        boolean z2;
        OutputStream outputStream;
        OutputStream outputStream2;
        ArrayList arrayList;
        OutputStream outputStream3;
        PackageInfo packageInfo;
        String str2;
        List<String> widgetParticipants;
        Slog.i(BackupManagerService.TAG, "--- Performing adb backup" + (this.mKeyValue ? ", including key-value backups" : "") + " ---");
        TreeMap<String, PackageInfo> treeMap = new TreeMap<>();
        FullBackupObbConnection fullBackupObbConnection = new FullBackupObbConnection(this.mUserBackupManagerService);
        fullBackupObbConnection.establish();
        sendStartBackup();
        PackageManager packageManager = this.mUserBackupManagerService.getPackageManager();
        boolean z3 = false;
        boolean z4 = true;
        if (this.mAllApps) {
            List<PackageInfo> installedPackages = packageManager.getInstalledPackages(AudioFormat.OPUS);
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo2 = installedPackages.get(i);
                if (this.mIncludeSystem || (packageInfo2.applicationInfo.flags & 1) == 0) {
                    treeMap.put(packageInfo2.packageName, packageInfo2);
                }
            }
        }
        if (this.mDoWidgets && (widgetParticipants = AppWidgetBackupBridge.getWidgetParticipants(0)) != null) {
            addPackagesToSet(treeMap, widgetParticipants);
        }
        List<String> list = this.mPackages;
        if (list != null) {
            addPackagesToSet(treeMap, list);
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator<Map.Entry<String, PackageInfo>> it = treeMap.entrySet().iterator();
        while (it.hasNext()) {
            PackageInfo value = it.next().getValue();
            if (!this.mBackupEligibilityRules.appIsEligibleForBackup(value.applicationInfo) || this.mBackupEligibilityRules.appIsStopped(value.applicationInfo)) {
                it.remove();
                Slog.i(BackupManagerService.TAG, "Package " + value.packageName + " is not eligible for backup, removing.");
            } else if (this.mBackupEligibilityRules.appIsKeyValueOnly(value)) {
                it.remove();
                Slog.i(BackupManagerService.TAG, "Package " + value.packageName + " is key-value.");
                arrayList2.add(value);
            }
        }
        ArrayList arrayList3 = new ArrayList(treeMap.values());
        FileOutputStream fileOutputStream = new FileOutputStream(this.mOutputFile.getFileDescriptor());
        OutputStream outputStream4 = null;
        try {
            try {
                try {
                    str = this.mEncryptPassword;
                } catch (RemoteException unused) {
                } catch (Exception e) {
                    e = e;
                }
                if (str != null) {
                    try {
                        if (str.length() > 0) {
                            z2 = true;
                            if (this.mUserBackupManagerService.backupPasswordMatches(this.mCurrentPassword)) {
                                Slog.w(BackupManagerService.TAG, "Backup password mismatch; aborting");
                                try {
                                    this.mOutputFile.close();
                                } catch (IOException e2) {
                                    Slog.e(BackupManagerService.TAG, "IO error closing adb backup file: " + e2.getMessage());
                                }
                                synchronized (this.mLatch) {
                                    this.mLatch.set(true);
                                    this.mLatch.notifyAll();
                                }
                            } else {
                                StringBuilder sb = new StringBuilder(1024);
                                sb.append(UserBackupManagerService.BACKUP_FILE_HEADER_MAGIC);
                                sb.append(5);
                                sb.append(this.mCompress ? "\n1\n" : "\n0\n");
                                try {
                                    if (z2) {
                                        outputStream = emitAesBackupHeader(sb, fileOutputStream);
                                    } else {
                                        sb.append("none\n");
                                        outputStream = fileOutputStream;
                                    }
                                    fileOutputStream.write(sb.toString().getBytes("UTF-8"));
                                    OutputStream deflaterOutputStream = this.mCompress ? new DeflaterOutputStream(outputStream, new Deflater(9), true) : outputStream;
                                    try {
                                        if (this.mIncludeShared) {
                                            try {
                                                try {
                                                    arrayList3.add(this.mUserBackupManagerService.getPackageManager().getPackageInfo(UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE, 0));
                                                } catch (RemoteException unused2) {
                                                    outputStream4 = deflaterOutputStream;
                                                    z = z4;
                                                    Slog.e(BackupManagerService.TAG, "App died during full backup");
                                                    if (outputStream4 != null) {
                                                    }
                                                    this.mOutputFile.close();
                                                    synchronized (this.mLatch) {
                                                    }
                                                } catch (Exception e3) {
                                                    e = e3;
                                                    outputStream4 = deflaterOutputStream;
                                                    z = z4;
                                                    Slog.e(BackupManagerService.TAG, "Internal exception during full backup", e);
                                                    if (outputStream4 != null) {
                                                    }
                                                    this.mOutputFile.close();
                                                    synchronized (this.mLatch) {
                                                    }
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    outputStream4 = deflaterOutputStream;
                                                    z3 = true;
                                                    if (outputStream4 != null) {
                                                    }
                                                    this.mOutputFile.close();
                                                    synchronized (this.mLatch) {
                                                    }
                                                }
                                            } catch (PackageManager.NameNotFoundException unused3) {
                                                Slog.e(BackupManagerService.TAG, "Unable to find shared-storage backup handler");
                                            }
                                        }
                                        int size = arrayList3.size();
                                        int i2 = 0;
                                        while (i2 < size) {
                                            try {
                                                PackageInfo packageInfo3 = (PackageInfo) arrayList3.get(i2);
                                                Slog.i(BackupManagerService.TAG, "--- Performing full backup for package " + packageInfo3.packageName + " ---");
                                                boolean equals = packageInfo3.packageName.equals(UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE);
                                                int i3 = i2;
                                                OutputStream outputStream5 = deflaterOutputStream;
                                                ArrayList arrayList4 = arrayList3;
                                                ArrayList arrayList5 = arrayList2;
                                                try {
                                                    FullBackupEngine fullBackupEngine = new FullBackupEngine(this.mUserBackupManagerService, deflaterOutputStream, null, packageInfo3, this.mIncludeApks, this, Long.MAX_VALUE, this.mCurrentOpToken, 0, this.mBackupEligibilityRules, null);
                                                    if (equals) {
                                                        str2 = "Shared storage";
                                                        packageInfo = packageInfo3;
                                                    } else {
                                                        packageInfo = packageInfo3;
                                                        str2 = packageInfo.packageName;
                                                    }
                                                    sendOnBackupPackage(str2);
                                                    this.mCurrentTarget = packageInfo;
                                                    fullBackupEngine.backupOnePackage();
                                                    if (!this.mIncludeObbs || equals) {
                                                        outputStream3 = outputStream5;
                                                    } else {
                                                        outputStream3 = outputStream5;
                                                        try {
                                                            if (!fullBackupObbConnection.backupObbs(packageInfo, outputStream3)) {
                                                                throw new RuntimeException("Failure writing OBB stack for " + packageInfo);
                                                            }
                                                        } catch (RemoteException unused4) {
                                                            outputStream4 = outputStream3;
                                                            z = true;
                                                            Slog.e(BackupManagerService.TAG, "App died during full backup");
                                                            if (outputStream4 != null) {
                                                                try {
                                                                    outputStream4.flush();
                                                                    outputStream4.close();
                                                                } catch (IOException e4) {
                                                                    Slog.e(BackupManagerService.TAG, "IO error closing adb backup file: " + e4.getMessage());
                                                                    synchronized (this.mLatch) {
                                                                        this.mLatch.set(z);
                                                                        this.mLatch.notifyAll();
                                                                    }
                                                                    sendEndBackup();
                                                                    fullBackupObbConnection.tearDown();
                                                                    Slog.d(BackupManagerService.TAG, "Full backup pass complete.");
                                                                    this.mUserBackupManagerService.getWakelock().release();
                                                                    return;
                                                                }
                                                            }
                                                            this.mOutputFile.close();
                                                            synchronized (this.mLatch) {
                                                            }
                                                        } catch (Exception e5) {
                                                            e = e5;
                                                            outputStream4 = outputStream3;
                                                            z = true;
                                                            Slog.e(BackupManagerService.TAG, "Internal exception during full backup", e);
                                                            if (outputStream4 != null) {
                                                                try {
                                                                    outputStream4.flush();
                                                                    outputStream4.close();
                                                                } catch (IOException e6) {
                                                                    Slog.e(BackupManagerService.TAG, "IO error closing adb backup file: " + e6.getMessage());
                                                                    synchronized (this.mLatch) {
                                                                        this.mLatch.set(z);
                                                                        this.mLatch.notifyAll();
                                                                    }
                                                                    sendEndBackup();
                                                                    fullBackupObbConnection.tearDown();
                                                                    Slog.d(BackupManagerService.TAG, "Full backup pass complete.");
                                                                    this.mUserBackupManagerService.getWakelock().release();
                                                                    return;
                                                                }
                                                            }
                                                            this.mOutputFile.close();
                                                            synchronized (this.mLatch) {
                                                            }
                                                        } catch (Throwable th3) {
                                                            th = th3;
                                                            outputStream4 = outputStream3;
                                                            z3 = true;
                                                            th = th;
                                                            if (outputStream4 != null) {
                                                            }
                                                            this.mOutputFile.close();
                                                            synchronized (this.mLatch) {
                                                            }
                                                        }
                                                    }
                                                    i2 = i3 + 1;
                                                    deflaterOutputStream = outputStream3;
                                                    arrayList3 = arrayList4;
                                                    arrayList2 = arrayList5;
                                                    z4 = true;
                                                } catch (RemoteException unused5) {
                                                    outputStream3 = outputStream5;
                                                } catch (Exception e7) {
                                                    e = e7;
                                                    outputStream3 = outputStream5;
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                    outputStream3 = outputStream5;
                                                }
                                            } catch (RemoteException unused6) {
                                                outputStream4 = deflaterOutputStream;
                                                z = z4;
                                                Slog.e(BackupManagerService.TAG, "App died during full backup");
                                                if (outputStream4 != null) {
                                                }
                                                this.mOutputFile.close();
                                                synchronized (this.mLatch) {
                                                }
                                            } catch (Exception e8) {
                                                e = e8;
                                                outputStream4 = deflaterOutputStream;
                                                z = z4;
                                                Slog.e(BackupManagerService.TAG, "Internal exception during full backup", e);
                                                if (outputStream4 != null) {
                                                }
                                                this.mOutputFile.close();
                                                synchronized (this.mLatch) {
                                                }
                                            } catch (Throwable th5) {
                                                th = th5;
                                                outputStream4 = deflaterOutputStream;
                                                z3 = z4;
                                                th = th;
                                                if (outputStream4 != null) {
                                                }
                                                this.mOutputFile.close();
                                                synchronized (this.mLatch) {
                                                }
                                            }
                                        }
                                        outputStream2 = deflaterOutputStream;
                                        arrayList = arrayList2;
                                    } catch (RemoteException unused7) {
                                        outputStream2 = deflaterOutputStream;
                                        z = true;
                                    } catch (Exception e9) {
                                        e = e9;
                                        outputStream2 = deflaterOutputStream;
                                        z = true;
                                    } catch (Throwable th6) {
                                        th = th6;
                                        outputStream2 = deflaterOutputStream;
                                        z3 = true;
                                    }
                                    try {
                                        if (this.mKeyValue) {
                                            Iterator it2 = arrayList.iterator();
                                            while (it2.hasNext()) {
                                                PackageInfo packageInfo4 = (PackageInfo) it2.next();
                                                Slog.i(BackupManagerService.TAG, "--- Performing key-value backup for package " + packageInfo4.packageName + " ---");
                                                UserBackupManagerService userBackupManagerService = this.mUserBackupManagerService;
                                                KeyValueAdbBackupEngine keyValueAdbBackupEngine = new KeyValueAdbBackupEngine(outputStream2, packageInfo4, userBackupManagerService, userBackupManagerService.getPackageManager(), this.mUserBackupManagerService.getBaseStateDir(), this.mUserBackupManagerService.getDataDir());
                                                sendOnBackupPackage(packageInfo4.packageName);
                                                keyValueAdbBackupEngine.backupOnePackage();
                                            }
                                        }
                                        finalizeBackup(outputStream2);
                                        if (outputStream2 != null) {
                                            try {
                                                outputStream2.flush();
                                                outputStream2.close();
                                            } catch (IOException e10) {
                                                Slog.e(BackupManagerService.TAG, "IO error closing adb backup file: " + e10.getMessage());
                                            }
                                        }
                                        this.mOutputFile.close();
                                        synchronized (this.mLatch) {
                                            this.mLatch.set(true);
                                            this.mLatch.notifyAll();
                                        }
                                    } catch (RemoteException unused8) {
                                        z = true;
                                        outputStream4 = outputStream2;
                                        Slog.e(BackupManagerService.TAG, "App died during full backup");
                                        if (outputStream4 != null) {
                                        }
                                        this.mOutputFile.close();
                                        synchronized (this.mLatch) {
                                        }
                                    } catch (Exception e11) {
                                        e = e11;
                                        z = true;
                                        outputStream4 = outputStream2;
                                        Slog.e(BackupManagerService.TAG, "Internal exception during full backup", e);
                                        if (outputStream4 != null) {
                                        }
                                        this.mOutputFile.close();
                                        synchronized (this.mLatch) {
                                        }
                                    } catch (Throwable th7) {
                                        th = th7;
                                        z3 = true;
                                        outputStream4 = outputStream2;
                                        th = th;
                                        if (outputStream4 != null) {
                                        }
                                        this.mOutputFile.close();
                                        synchronized (this.mLatch) {
                                        }
                                    }
                                    sendEndBackup();
                                    fullBackupObbConnection.tearDown();
                                    Slog.d(BackupManagerService.TAG, "Full backup pass complete.");
                                    this.mUserBackupManagerService.getWakelock().release();
                                    return;
                                } catch (Exception e12) {
                                    z = true;
                                    try {
                                        Slog.e(BackupManagerService.TAG, "Unable to emit archive header", e12);
                                        try {
                                            this.mOutputFile.close();
                                        } catch (IOException e13) {
                                            Slog.e(BackupManagerService.TAG, "IO error closing adb backup file: " + e13.getMessage());
                                        }
                                        synchronized (this.mLatch) {
                                            this.mLatch.set(true);
                                            this.mLatch.notifyAll();
                                        }
                                    } catch (RemoteException unused9) {
                                        Slog.e(BackupManagerService.TAG, "App died during full backup");
                                        if (outputStream4 != null) {
                                        }
                                        this.mOutputFile.close();
                                        synchronized (this.mLatch) {
                                        }
                                    } catch (Exception e14) {
                                        e = e14;
                                        Slog.e(BackupManagerService.TAG, "Internal exception during full backup", e);
                                        if (outputStream4 != null) {
                                        }
                                        this.mOutputFile.close();
                                        synchronized (this.mLatch) {
                                        }
                                    }
                                }
                            }
                            sendEndBackup();
                            fullBackupObbConnection.tearDown();
                            Slog.d(BackupManagerService.TAG, "Full backup pass complete.");
                            this.mUserBackupManagerService.getWakelock().release();
                        }
                    } catch (Throwable th8) {
                        th = th8;
                        z3 = true;
                        if (outputStream4 != null) {
                            try {
                                outputStream4.flush();
                                outputStream4.close();
                            } catch (IOException e15) {
                                Slog.e(BackupManagerService.TAG, "IO error closing adb backup file: " + e15.getMessage());
                                synchronized (this.mLatch) {
                                }
                            }
                        }
                        this.mOutputFile.close();
                        synchronized (this.mLatch) {
                            this.mLatch.set(z3);
                            this.mLatch.notifyAll();
                        }
                        sendEndBackup();
                        fullBackupObbConnection.tearDown();
                        Slog.d(BackupManagerService.TAG, "Full backup pass complete.");
                        this.mUserBackupManagerService.getWakelock().release();
                        throw th;
                    }
                }
                z2 = false;
                if (this.mUserBackupManagerService.backupPasswordMatches(this.mCurrentPassword)) {
                }
                sendEndBackup();
                fullBackupObbConnection.tearDown();
                Slog.d(BackupManagerService.TAG, "Full backup pass complete.");
                this.mUserBackupManagerService.getWakelock().release();
            } catch (Throwable th9) {
                th = th9;
            }
        } catch (Throwable th10) {
            th = th10;
        }
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void handleCancel(boolean z) {
        PackageInfo packageInfo = this.mCurrentTarget;
        Slog.w(BackupManagerService.TAG, "adb backup cancel of " + packageInfo);
        if (packageInfo != null) {
            this.mUserBackupManagerService.tearDownAgentAndKill(this.mCurrentTarget.applicationInfo);
        }
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }
}
