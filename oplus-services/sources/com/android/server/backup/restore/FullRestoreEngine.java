package com.android.server.backup.restore;

import android.app.IBackupAgent;
import android.app.backup.IBackupManagerMonitor;
import android.app.backup.IFullBackupRestoreObserver;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.Signature;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.Settings;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.backup.BackupAgentTimeoutParameters;
import com.android.server.backup.BackupManagerService;
import com.android.server.backup.BackupRestoreTask;
import com.android.server.backup.FileMetadata;
import com.android.server.backup.KeyValueAdbRestoreEngine;
import com.android.server.backup.OperationStorage;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.fullbackup.FullBackupObbConnection;
import com.android.server.backup.utils.BackupEligibilityRules;
import com.android.server.backup.utils.BytesReadListener;
import com.android.server.backup.utils.FullBackupRestoreObserverUtils;
import com.android.server.backup.utils.RestoreUtils;
import com.android.server.backup.utils.TarBackupReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FullRestoreEngine extends RestoreEngine {
    private IBackupAgent mAgent;
    private String mAgentPackage;
    private final BackupAgentTimeoutParameters mAgentTimeoutParameters;
    final boolean mAllowApks;
    private long mAppVersion;
    private final BackupEligibilityRules mBackupEligibilityRules;
    private final UserBackupManagerService mBackupManagerService;
    final byte[] mBuffer;
    private final HashSet<String> mClearedPackages;
    private final RestoreDeleteObserver mDeleteObserver;
    final int mEphemeralOpToken;
    private final boolean mIsAdbRestore;
    private final HashMap<String, Signature[]> mManifestSignatures;
    final IBackupManagerMonitor mMonitor;
    private final BackupRestoreTask mMonitorTask;
    private FullBackupObbConnection mObbConnection;
    private IFullBackupRestoreObserver mObserver;
    final PackageInfo mOnlyPackage;
    private final OperationStorage mOperationStorage;
    private final HashMap<String, String> mPackageInstallers;
    private final HashMap<String, RestorePolicy> mPackagePolicies;
    private ParcelFileDescriptor[] mPipes;

    @GuardedBy({"mPipesLock"})
    private boolean mPipesClosed;
    private final Object mPipesLock;
    private FileMetadata mReadOnlyParent;
    private ApplicationInfo mTargetApp;
    private final int mUserId;
    private byte[] mWidgetData;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$restoreOneFile$0(long j) {
    }

    public FullRestoreEngine(UserBackupManagerService userBackupManagerService, OperationStorage operationStorage, BackupRestoreTask backupRestoreTask, IFullBackupRestoreObserver iFullBackupRestoreObserver, IBackupManagerMonitor iBackupManagerMonitor, PackageInfo packageInfo, boolean z, int i, boolean z2, BackupEligibilityRules backupEligibilityRules) {
        this.mDeleteObserver = new RestoreDeleteObserver();
        this.mObbConnection = null;
        this.mPackagePolicies = new HashMap<>();
        this.mPackageInstallers = new HashMap<>();
        this.mManifestSignatures = new HashMap<>();
        this.mClearedPackages = new HashSet<>();
        this.mPipes = null;
        this.mPipesLock = new Object();
        this.mWidgetData = null;
        this.mReadOnlyParent = null;
        this.mBackupManagerService = userBackupManagerService;
        this.mOperationStorage = operationStorage;
        this.mEphemeralOpToken = i;
        this.mMonitorTask = backupRestoreTask;
        this.mObserver = iFullBackupRestoreObserver;
        this.mMonitor = iBackupManagerMonitor;
        this.mOnlyPackage = packageInfo;
        this.mAllowApks = z;
        this.mBuffer = new byte[32768];
        BackupAgentTimeoutParameters agentTimeoutParameters = userBackupManagerService.getAgentTimeoutParameters();
        Objects.requireNonNull(agentTimeoutParameters, "Timeout parameters cannot be null");
        this.mAgentTimeoutParameters = agentTimeoutParameters;
        this.mIsAdbRestore = z2;
        this.mUserId = userBackupManagerService.getUserId();
        this.mBackupEligibilityRules = backupEligibilityRules;
    }

    @VisibleForTesting
    FullRestoreEngine() {
        this.mDeleteObserver = new RestoreDeleteObserver();
        this.mObbConnection = null;
        this.mPackagePolicies = new HashMap<>();
        this.mPackageInstallers = new HashMap<>();
        this.mManifestSignatures = new HashMap<>();
        this.mClearedPackages = new HashSet<>();
        this.mPipes = null;
        this.mPipesLock = new Object();
        this.mWidgetData = null;
        this.mReadOnlyParent = null;
        this.mIsAdbRestore = false;
        this.mAllowApks = false;
        this.mEphemeralOpToken = 0;
        this.mUserId = 0;
        this.mBackupEligibilityRules = null;
        this.mAgentTimeoutParameters = null;
        this.mBuffer = null;
        this.mBackupManagerService = null;
        this.mOperationStorage = null;
        this.mMonitor = null;
        this.mMonitorTask = null;
        this.mOnlyPackage = null;
    }

    public IBackupAgent getAgent() {
        return this.mAgent;
    }

    public byte[] getWidgetData() {
        return this.mWidgetData;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:112:0x046f A[Catch: IOException -> 0x04b6, TRY_LEAVE, TryCatch #4 {IOException -> 0x04b6, blocks: (B:89:0x0405, B:92:0x041c, B:94:0x0424, B:95:0x0427, B:106:0x043b, B:110:0x0459, B:112:0x046f, B:149:0x0426, B:160:0x03ef, B:163:0x03fc, B:100:0x0435), top: B:75:0x028a, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x04c3 A[Catch: IOException -> 0x04ea, TryCatch #19 {IOException -> 0x04ea, blocks: (B:115:0x049f, B:117:0x04aa, B:121:0x04c3, B:124:0x04d1, B:126:0x04d7, B:127:0x04da, B:132:0x04d9), top: B:114:0x049f }] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0517  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x052b  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x052e  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0528  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x04b3  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x04bc  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01b1 A[Catch: IOException -> 0x04ec, TRY_LEAVE, TryCatch #8 {IOException -> 0x04ec, blocks: (B:38:0x01ab, B:40:0x01b1, B:44:0x01bf, B:58:0x0217, B:60:0x021b, B:64:0x023c, B:66:0x0244, B:67:0x0263, B:71:0x026c, B:73:0x0276, B:208:0x027f, B:241:0x0199), top: B:240:0x0199 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01bf A[Catch: IOException -> 0x04ec, TRY_ENTER, TRY_LEAVE, TryCatch #8 {IOException -> 0x04ec, blocks: (B:38:0x01ab, B:40:0x01b1, B:44:0x01bf, B:58:0x0217, B:60:0x021b, B:64:0x023c, B:66:0x0244, B:67:0x0263, B:71:0x026c, B:73:0x0276, B:208:0x027f, B:241:0x0199), top: B:240:0x0199 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01da A[Catch: NameNotFoundException | IOException -> 0x0217, TryCatch #20 {NameNotFoundException | IOException -> 0x0217, blocks: (B:47:0x01c3, B:49:0x01da, B:52:0x01f4, B:53:0x01ea, B:54:0x01f9, B:57:0x0209), top: B:46:0x01c3 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0208  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x021b A[Catch: IOException -> 0x04ec, TryCatch #8 {IOException -> 0x04ec, blocks: (B:38:0x01ab, B:40:0x01b1, B:44:0x01bf, B:58:0x0217, B:60:0x021b, B:64:0x023c, B:66:0x0244, B:67:0x0263, B:71:0x026c, B:73:0x0276, B:208:0x027f, B:241:0x0199), top: B:240:0x0199 }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x023c A[Catch: IOException -> 0x04ec, TryCatch #8 {IOException -> 0x04ec, blocks: (B:38:0x01ab, B:40:0x01b1, B:44:0x01bf, B:58:0x0217, B:60:0x021b, B:64:0x023c, B:66:0x0244, B:67:0x0263, B:71:0x026c, B:73:0x0276, B:208:0x027f, B:241:0x0199), top: B:240:0x0199 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x026c A[Catch: IOException -> 0x04ec, TryCatch #8 {IOException -> 0x04ec, blocks: (B:38:0x01ab, B:40:0x01b1, B:44:0x01bf, B:58:0x0217, B:60:0x021b, B:64:0x023c, B:66:0x0244, B:67:0x0263, B:71:0x026c, B:73:0x0276, B:208:0x027f, B:241:0x0199), top: B:240:0x0199 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0405 A[Catch: IOException -> 0x04b6, TryCatch #4 {IOException -> 0x04b6, blocks: (B:89:0x0405, B:92:0x041c, B:94:0x0424, B:95:0x0427, B:106:0x043b, B:110:0x0459, B:112:0x046f, B:149:0x0426, B:160:0x03ef, B:163:0x03fc, B:100:0x0435), top: B:75:0x028a, inners: #0 }] */
    /* JADX WARN: Type inference failed for: r2v0, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v17, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v18, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v21 */
    /* JADX WARN: Type inference failed for: r2v22 */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v29 */
    /* JADX WARN: Type inference failed for: r2v34, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v36 */
    /* JADX WARN: Type inference failed for: r2v39 */
    /* JADX WARN: Type inference failed for: r2v53 */
    /* JADX WARN: Type inference failed for: r2v54 */
    /* JADX WARN: Type inference failed for: r2v57 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Type inference failed for: r6v25, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v26 */
    /* JADX WARN: Type inference failed for: r6v27 */
    /* JADX WARN: Type inference failed for: r6v32 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean restoreOneFile(InputStream inputStream, boolean z, byte[] bArr, PackageInfo packageInfo, boolean z2, int i, IBackupManagerMonitor iBackupManagerMonitor) {
        boolean z3;
        FileMetadata fileMetadata;
        FileMetadata fileMetadata2;
        boolean z4;
        FileMetadata readTarHeaders;
        TarBackupReader tarBackupReader;
        boolean z5;
        InputStream inputStream2;
        byte[] bArr2;
        long restoreAgentTimeoutMillis;
        TarBackupReader tarBackupReader2;
        long j;
        String str;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        String str2;
        String str3;
        boolean z10;
        String str4;
        String str5;
        String str6;
        RestorePolicy restorePolicy;
        ?? r2 = inputStream;
        if (!isRunning()) {
            Slog.w(BackupManagerService.TAG, "Restore engine used after halting");
            return false;
        }
        BytesReadListener bytesReadListener = new BytesReadListener() { // from class: com.android.server.backup.restore.FullRestoreEngine$$ExternalSyntheticLambda0
            @Override // com.android.server.backup.utils.BytesReadListener
            public final void onBytesRead(long j2) {
                FullRestoreEngine.lambda$restoreOneFile$0(j2);
            }
        };
        TarBackupReader tarBackupReader3 = new TarBackupReader(r2, bytesReadListener, iBackupManagerMonitor);
        try {
            readTarHeaders = tarBackupReader3.readTarHeaders();
        } catch (IOException e) {
            e = e;
            z3 = true;
        }
        if (readTarHeaders != null) {
            String str7 = readTarHeaders.packageName;
            if (!str7.equals(this.mAgentPackage)) {
                if (packageInfo != null && !str7.equals(packageInfo.packageName)) {
                    Slog.w(BackupManagerService.TAG, "Expected data for " + packageInfo + " but saw " + str7);
                    setResult(-3);
                    setRunning(false);
                    return false;
                }
                if (!this.mPackagePolicies.containsKey(str7)) {
                    this.mPackagePolicies.put(str7, RestorePolicy.IGNORE);
                }
                if (this.mAgent != null) {
                    Slog.d(BackupManagerService.TAG, "Saw new package; finalizing old one");
                    tearDownPipes();
                    tearDownAgent(this.mTargetApp, this.mIsAdbRestore);
                    this.mTargetApp = null;
                    this.mAgentPackage = null;
                }
            }
            if (readTarHeaders.path.equals(UserBackupManagerService.BACKUP_MANIFEST_FILENAME)) {
                Signature[] readAppManifestAndReturnSignatures = tarBackupReader3.readAppManifestAndReturnSignatures(readTarHeaders);
                this.mAppVersion = readTarHeaders.version;
                RestorePolicy chooseRestorePolicy = tarBackupReader3.chooseRestorePolicy(this.mBackupManagerService.getPackageManager(), z2, readTarHeaders, readAppManifestAndReturnSignatures, (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class), this.mUserId, this.mBackupEligibilityRules);
                this.mManifestSignatures.put(readTarHeaders.packageName, readAppManifestAndReturnSignatures);
                this.mPackagePolicies.put(str7, chooseRestorePolicy);
                this.mPackageInstallers.put(str7, readTarHeaders.installerPackageName);
                tarBackupReader3.skipTarPadding(readTarHeaders.size);
                this.mObserver = FullBackupRestoreObserverUtils.sendOnRestorePackage(this.mObserver, str7);
            } else if (readTarHeaders.path.equals(UserBackupManagerService.BACKUP_METADATA_FILENAME)) {
                tarBackupReader3.readMetadata(readTarHeaders);
                this.mWidgetData = tarBackupReader3.getWidgetData();
                tarBackupReader3.getMonitor();
                tarBackupReader3.skipTarPadding(readTarHeaders.size);
            } else {
                int i2 = AnonymousClass1.$SwitchMap$com$android$server$backup$restore$RestorePolicy[this.mPackagePolicies.get(str7).ordinal()];
                if (i2 != 1) {
                    if (i2 == 2) {
                        try {
                            if (readTarHeaders.domain.equals("a")) {
                                Slog.d(BackupManagerService.TAG, "APK file; installing");
                                z3 = true;
                                try {
                                    boolean installApk = RestoreUtils.installApk(inputStream, this.mBackupManagerService.getContext(), this.mDeleteObserver, this.mManifestSignatures, this.mPackagePolicies, readTarHeaders, this.mPackageInstallers.get(str7), bytesReadListener, this.mUserId);
                                    HashMap<String, RestorePolicy> hashMap = this.mPackagePolicies;
                                    if (installApk) {
                                        restorePolicy = RestorePolicy.ACCEPT;
                                    } else {
                                        restorePolicy = RestorePolicy.IGNORE;
                                    }
                                    hashMap.put(str7, restorePolicy);
                                    tarBackupReader3.skipTarPadding(readTarHeaders.size);
                                    return true;
                                } catch (IOException e2) {
                                    e = e2;
                                    r2 = BackupManagerService.TAG;
                                    fileMetadata = null;
                                }
                            } else {
                                fileMetadata = null;
                                tarBackupReader = tarBackupReader3;
                                z3 = true;
                                z3 = true;
                                try {
                                    this.mPackagePolicies.put(str7, RestorePolicy.IGNORE);
                                } catch (IOException e3) {
                                    e = e3;
                                    r2 = BackupManagerService.TAG;
                                    Slog.w((String) r2, "io exception on restore socket read: " + e.getMessage());
                                    setResult(-3);
                                    fileMetadata2 = fileMetadata;
                                    if (fileMetadata2 == null) {
                                    }
                                    if (fileMetadata2 != null) {
                                    }
                                }
                            }
                        } catch (IOException e4) {
                            e = e4;
                            z3 = true;
                            fileMetadata = null;
                            r2 = BackupManagerService.TAG;
                            Slog.w((String) r2, "io exception on restore socket read: " + e.getMessage());
                            setResult(-3);
                            fileMetadata2 = fileMetadata;
                            if (fileMetadata2 == null) {
                            }
                            if (fileMetadata2 != null) {
                            }
                        }
                    } else {
                        try {
                        } catch (IOException e5) {
                            e = e5;
                            fileMetadata = null;
                            r2 = BackupManagerService.TAG;
                            z3 = true;
                        }
                        if (i2 == 3) {
                            if (readTarHeaders.domain.equals("a")) {
                                Slog.d(BackupManagerService.TAG, "apk present but ACCEPT");
                            } else {
                                tarBackupReader = tarBackupReader3;
                                z5 = true;
                                z3 = true;
                                if (isRestorableFile(readTarHeaders) || !isCanonicalFilePath(readTarHeaders.path)) {
                                    z5 = false;
                                }
                                if (z5 && this.mAgent == null) {
                                    try {
                                        this.mTargetApp = this.mBackupManagerService.getPackageManager().getApplicationInfoAsUser(str7, 0, this.mUserId);
                                        if (!this.mClearedPackages.contains(str7)) {
                                            boolean shouldForceClearAppDataOnFullRestore = shouldForceClearAppDataOnFullRestore(this.mTargetApp.packageName);
                                            if (this.mTargetApp.backupAgentName == null || shouldForceClearAppDataOnFullRestore) {
                                                Slog.d(BackupManagerService.TAG, "Clearing app data preparatory to full restore");
                                                this.mBackupManagerService.clearApplicationDataBeforeRestore(str7);
                                            }
                                            this.mClearedPackages.add(str7);
                                        }
                                        setUpPipes();
                                        this.mAgent = this.mBackupManagerService.bindToAgentSynchronous(this.mTargetApp, "k".equals(readTarHeaders.domain) ? 2 : 3, this.mBackupEligibilityRules.getBackupDestination());
                                        this.mAgentPackage = str7;
                                    } catch (PackageManager.NameNotFoundException | IOException unused) {
                                    }
                                    if (this.mAgent == null) {
                                        Slog.e(BackupManagerService.TAG, "Unable to create agent for " + str7);
                                        tearDownPipes();
                                        this.mPackagePolicies.put(str7, RestorePolicy.IGNORE);
                                        z5 = false;
                                    }
                                }
                                if (z5 && !str7.equals(this.mAgentPackage)) {
                                    Slog.e(BackupManagerService.TAG, "Restoring data for " + str7 + " but agent is for " + this.mAgentPackage);
                                    z5 = false;
                                }
                                if (shouldSkipReadOnlyDir(readTarHeaders)) {
                                    z5 = false;
                                }
                                if (z5) {
                                    long j2 = readTarHeaders.size;
                                    if (str7.equals(UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE)) {
                                        restoreAgentTimeoutMillis = this.mAgentTimeoutParameters.getSharedBackupAgentTimeoutMillis();
                                    } else {
                                        try {
                                            restoreAgentTimeoutMillis = this.mAgentTimeoutParameters.getRestoreAgentTimeoutMillis(this.mTargetApp.uid);
                                        } catch (IOException e6) {
                                            e = e6;
                                            r2 = BackupManagerService.TAG;
                                            fileMetadata = null;
                                            Slog.w((String) r2, "io exception on restore socket read: " + e.getMessage());
                                            setResult(-3);
                                            fileMetadata2 = fileMetadata;
                                            if (fileMetadata2 == null) {
                                            }
                                            if (fileMetadata2 != null) {
                                            }
                                        }
                                    }
                                    try {
                                        try {
                                            this.mBackupManagerService.prepareOperationTimeout(i, restoreAgentTimeoutMillis, this.mMonitorTask, 1);
                                            ?? equals = "obb".equals(readTarHeaders.domain);
                                            try {
                                                if (equals != 0) {
                                                    try {
                                                        Slog.d(BackupManagerService.TAG, "Restoring OBB file for " + str7 + " : " + readTarHeaders.path);
                                                        FullBackupObbConnection fullBackupObbConnection = this.mObbConnection;
                                                        ParcelFileDescriptor parcelFileDescriptor = this.mPipes[0];
                                                        long j3 = readTarHeaders.size;
                                                        int i3 = readTarHeaders.type;
                                                        z10 = z5;
                                                        String str8 = readTarHeaders.path;
                                                        long j4 = readTarHeaders.mode;
                                                        String str9 = BackupManagerService.TAG;
                                                        tarBackupReader2 = tarBackupReader;
                                                        try {
                                                            try {
                                                                j = j2;
                                                                fullBackupObbConnection.restoreObbFile(str7, parcelFileDescriptor, j3, i3, str8, j4, readTarHeaders.mtime, i, this.mBackupManagerService.getBackupManagerBinder());
                                                                str = str7;
                                                                str4 = str9;
                                                                str5 = str9;
                                                            } catch (RemoteException unused2) {
                                                                j = j2;
                                                                str = str7;
                                                                str3 = str9;
                                                                r2 = str3;
                                                                Slog.e((String) r2, "Agent crashed during full restore");
                                                                z6 = r2;
                                                                z7 = false;
                                                                z8 = false;
                                                                r2 = z6;
                                                                if (z7) {
                                                                }
                                                                if (!z9) {
                                                                }
                                                                z5 = z7;
                                                                if (!z5) {
                                                                }
                                                                fileMetadata2 = readTarHeaders;
                                                                if (fileMetadata2 == null) {
                                                                }
                                                                if (fileMetadata2 != null) {
                                                                }
                                                            } catch (IOException unused3) {
                                                                j = j2;
                                                                str = str7;
                                                                str2 = str9;
                                                                r2 = str2;
                                                                Slog.d((String) r2, "Couldn't establish restore");
                                                                z6 = r2;
                                                                z7 = false;
                                                                z8 = false;
                                                                r2 = z6;
                                                                if (z7) {
                                                                }
                                                                if (!z9) {
                                                                }
                                                                z5 = z7;
                                                                if (!z5) {
                                                                }
                                                                fileMetadata2 = readTarHeaders;
                                                                if (fileMetadata2 == null) {
                                                                }
                                                                if (fileMetadata2 != null) {
                                                                }
                                                            }
                                                        } catch (RemoteException unused4) {
                                                            j = j2;
                                                        } catch (IOException unused5) {
                                                            j = j2;
                                                        }
                                                    } catch (RemoteException unused6) {
                                                        tarBackupReader2 = tarBackupReader;
                                                        j = j2;
                                                        r2 = BackupManagerService.TAG;
                                                        str = str7;
                                                        Slog.e((String) r2, "Agent crashed during full restore");
                                                        z6 = r2;
                                                        z7 = false;
                                                        z8 = false;
                                                        r2 = z6;
                                                        if (z7) {
                                                        }
                                                        if (!z9) {
                                                        }
                                                        z5 = z7;
                                                        if (!z5) {
                                                        }
                                                        fileMetadata2 = readTarHeaders;
                                                        if (fileMetadata2 == null) {
                                                        }
                                                        if (fileMetadata2 != null) {
                                                        }
                                                    } catch (IOException unused7) {
                                                        tarBackupReader2 = tarBackupReader;
                                                        j = j2;
                                                        r2 = BackupManagerService.TAG;
                                                        str = str7;
                                                        Slog.d((String) r2, "Couldn't establish restore");
                                                        z6 = r2;
                                                        z7 = false;
                                                        z8 = false;
                                                        r2 = z6;
                                                        if (z7) {
                                                        }
                                                        if (!z9) {
                                                        }
                                                        z5 = z7;
                                                        if (!z5) {
                                                        }
                                                        fileMetadata2 = readTarHeaders;
                                                        if (fileMetadata2 == null) {
                                                        }
                                                        if (fileMetadata2 != null) {
                                                        }
                                                    }
                                                } else {
                                                    z10 = z5;
                                                    String str10 = BackupManagerService.TAG;
                                                    tarBackupReader2 = tarBackupReader;
                                                    j = j2;
                                                    try {
                                                        if ("k".equals(readTarHeaders.domain)) {
                                                            try {
                                                                String str11 = str10;
                                                                Slog.d(str11, "Restoring key-value file for " + str7 + " : " + readTarHeaders.path);
                                                                readTarHeaders.version = this.mAppVersion;
                                                                UserBackupManagerService userBackupManagerService = this.mBackupManagerService;
                                                                new Thread(new KeyValueAdbRestoreEngine(userBackupManagerService, userBackupManagerService.getDataDir(), readTarHeaders, this.mPipes[0], this.mAgent, i), "restore-key-value-runner").start();
                                                                str6 = str11;
                                                            } catch (RemoteException unused8) {
                                                                r2 = str10;
                                                                str = str7;
                                                                Slog.e((String) r2, "Agent crashed during full restore");
                                                                z6 = r2;
                                                                z7 = false;
                                                                z8 = false;
                                                                r2 = z6;
                                                                if (z7) {
                                                                }
                                                                if (!z9) {
                                                                }
                                                                z5 = z7;
                                                                if (!z5) {
                                                                }
                                                                fileMetadata2 = readTarHeaders;
                                                                if (fileMetadata2 == null) {
                                                                }
                                                                if (fileMetadata2 != null) {
                                                                }
                                                            } catch (IOException unused9) {
                                                                r2 = str10;
                                                                str = str7;
                                                                Slog.d((String) r2, "Couldn't establish restore");
                                                                z6 = r2;
                                                                z7 = false;
                                                                z8 = false;
                                                                r2 = z6;
                                                                if (z7) {
                                                                }
                                                                if (!z9) {
                                                                }
                                                                z5 = z7;
                                                                if (!z5) {
                                                                }
                                                                fileMetadata2 = readTarHeaders;
                                                                if (fileMetadata2 == null) {
                                                                }
                                                                if (fileMetadata2 != null) {
                                                                }
                                                            }
                                                        } else {
                                                            r2 = str10;
                                                            if (this.mTargetApp.processName.equals("system")) {
                                                                Slog.d((String) r2, "system process agent - spinning a thread");
                                                                new Thread(new RestoreFileRunnable(this.mBackupManagerService, this.mAgent, readTarHeaders, this.mPipes[0], i), "restore-sys-runner").start();
                                                                str6 = r2;
                                                            } else {
                                                                str = str7;
                                                                try {
                                                                    this.mAgent.doRestoreFile(this.mPipes[0], readTarHeaders.size, readTarHeaders.type, readTarHeaders.domain, readTarHeaders.path, readTarHeaders.mode, readTarHeaders.mtime, i, this.mBackupManagerService.getBackupManagerBinder());
                                                                    str4 = r2;
                                                                    str5 = str10;
                                                                } catch (RemoteException unused10) {
                                                                    Slog.e((String) r2, "Agent crashed during full restore");
                                                                    z6 = r2;
                                                                    z7 = false;
                                                                    z8 = false;
                                                                    r2 = z6;
                                                                    if (z7) {
                                                                    }
                                                                    if (!z9) {
                                                                    }
                                                                    z5 = z7;
                                                                    if (!z5) {
                                                                    }
                                                                    fileMetadata2 = readTarHeaders;
                                                                    if (fileMetadata2 == null) {
                                                                    }
                                                                    if (fileMetadata2 != null) {
                                                                    }
                                                                } catch (IOException unused11) {
                                                                    Slog.d((String) r2, "Couldn't establish restore");
                                                                    z6 = r2;
                                                                    z7 = false;
                                                                    z8 = false;
                                                                    r2 = z6;
                                                                    if (z7) {
                                                                    }
                                                                    if (!z9) {
                                                                    }
                                                                    z5 = z7;
                                                                    if (!z5) {
                                                                    }
                                                                    fileMetadata2 = readTarHeaders;
                                                                    if (fileMetadata2 == null) {
                                                                    }
                                                                    if (fileMetadata2 != null) {
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        str = str7;
                                                        str4 = str6;
                                                        str5 = str10;
                                                    } catch (RemoteException unused12) {
                                                    } catch (IOException unused13) {
                                                    }
                                                }
                                                z7 = z10;
                                                equals = z3 ? 1 : 0;
                                                r2 = str4;
                                                z8 = equals;
                                            } catch (RemoteException unused14) {
                                                str = equals;
                                                str3 = str5;
                                            } catch (IOException unused15) {
                                                str = equals;
                                                str2 = str5;
                                            }
                                        } catch (RemoteException unused16) {
                                            r2 = BackupManagerService.TAG;
                                            tarBackupReader2 = tarBackupReader;
                                            j = j2;
                                        } catch (IOException unused17) {
                                            r2 = BackupManagerService.TAG;
                                            tarBackupReader2 = tarBackupReader;
                                            j = j2;
                                        }
                                        if (z7) {
                                            FileOutputStream fileOutputStream = new FileOutputStream(this.mPipes[z3 ? 1 : 0].getFileDescriptor());
                                            boolean z11 = z3 ? 1 : 0;
                                            long j5 = j;
                                            while (true) {
                                                if (j5 <= 0) {
                                                    inputStream2 = inputStream;
                                                    bArr2 = bArr;
                                                    break;
                                                }
                                                bArr2 = bArr;
                                                inputStream2 = inputStream;
                                                int read = inputStream2.read(bArr2, 0, j5 > ((long) bArr2.length) ? bArr2.length : (int) j5);
                                                if (read <= 0) {
                                                    break;
                                                }
                                                j5 -= read;
                                                if (z11) {
                                                    try {
                                                        fileOutputStream.write(bArr2, 0, read);
                                                    } catch (IOException e7) {
                                                        Slog.e((String) r2, "Failed to write to restore pipe: " + e7.getMessage());
                                                        z11 = false;
                                                    }
                                                }
                                            }
                                            tarBackupReader2.skipTarPadding(readTarHeaders.size);
                                            z9 = this.mBackupManagerService.waitUntilOperationComplete(i);
                                        } else {
                                            inputStream2 = inputStream;
                                            bArr2 = bArr;
                                            z9 = z8;
                                        }
                                        if (!z9) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("Agent failure restoring ");
                                            String str12 = str;
                                            sb.append(str12);
                                            sb.append("; ending restore");
                                            Slog.w((String) r2, sb.toString());
                                            this.mBackupManagerService.getBackupHandler().removeMessages(18);
                                            tearDownPipes();
                                            tearDownAgent(this.mTargetApp, false);
                                            fileMetadata = null;
                                            try {
                                                this.mAgent = null;
                                                this.mPackagePolicies.put(str12, RestorePolicy.IGNORE);
                                                if (packageInfo != null) {
                                                    setResult(-2);
                                                    setRunning(false);
                                                    return false;
                                                }
                                            } catch (IOException e8) {
                                                e = e8;
                                            }
                                        }
                                        z5 = z7;
                                    } catch (IOException e9) {
                                        e = e9;
                                        r2 = r2;
                                        fileMetadata = null;
                                        Slog.w((String) r2, "io exception on restore socket read: " + e.getMessage());
                                        setResult(-3);
                                        fileMetadata2 = fileMetadata;
                                        if (fileMetadata2 == null) {
                                        }
                                        if (fileMetadata2 != null) {
                                        }
                                    }
                                } else {
                                    inputStream2 = r2;
                                    bArr2 = bArr;
                                }
                                if (!z5) {
                                    long j6 = (readTarHeaders.size + 511) & (-512);
                                    for (long j7 = 0; j6 > j7; j7 = 0) {
                                        long read2 = inputStream2.read(bArr2, 0, j6 > ((long) bArr2.length) ? bArr2.length : (int) j6);
                                        if (read2 <= 0) {
                                            break;
                                        }
                                        j6 -= read2;
                                    }
                                }
                                fileMetadata2 = readTarHeaders;
                                if (fileMetadata2 == null) {
                                    tearDownPipes();
                                    z4 = false;
                                    setRunning(false);
                                    if (z) {
                                        tearDownAgent(this.mTargetApp, this.mIsAdbRestore);
                                    }
                                } else {
                                    z4 = false;
                                }
                                return fileMetadata2 != null ? z3 : z4;
                            }
                        } else {
                            Slog.e(BackupManagerService.TAG, "Invalid policy from manifest");
                            this.mPackagePolicies.put(str7, RestorePolicy.IGNORE);
                        }
                        z5 = false;
                        z3 = true;
                        tarBackupReader = tarBackupReader3;
                        if (isRestorableFile(readTarHeaders)) {
                        }
                        z5 = false;
                        if (z5) {
                            this.mTargetApp = this.mBackupManagerService.getPackageManager().getApplicationInfoAsUser(str7, 0, this.mUserId);
                            if (!this.mClearedPackages.contains(str7)) {
                            }
                            setUpPipes();
                            this.mAgent = this.mBackupManagerService.bindToAgentSynchronous(this.mTargetApp, "k".equals(readTarHeaders.domain) ? 2 : 3, this.mBackupEligibilityRules.getBackupDestination());
                            this.mAgentPackage = str7;
                            if (this.mAgent == null) {
                            }
                        }
                        if (z5) {
                            Slog.e(BackupManagerService.TAG, "Restoring data for " + str7 + " but agent is for " + this.mAgentPackage);
                            z5 = false;
                        }
                        if (shouldSkipReadOnlyDir(readTarHeaders)) {
                        }
                        if (z5) {
                        }
                        if (!z5) {
                        }
                        fileMetadata2 = readTarHeaders;
                        if (fileMetadata2 == null) {
                        }
                        if (fileMetadata2 != null) {
                        }
                    }
                    Slog.w((String) r2, "io exception on restore socket read: " + e.getMessage());
                    setResult(-3);
                    fileMetadata2 = fileMetadata;
                    if (fileMetadata2 == null) {
                    }
                    if (fileMetadata2 != null) {
                    }
                } else {
                    z3 = true;
                    tarBackupReader = tarBackupReader3;
                }
                z5 = false;
                if (isRestorableFile(readTarHeaders)) {
                }
                z5 = false;
                if (z5) {
                }
                if (z5) {
                }
                if (shouldSkipReadOnlyDir(readTarHeaders)) {
                }
                if (z5) {
                }
                if (!z5) {
                }
                fileMetadata2 = readTarHeaders;
                if (fileMetadata2 == null) {
                }
                if (fileMetadata2 != null) {
                }
            }
        }
        z3 = true;
        fileMetadata2 = readTarHeaders;
        if (fileMetadata2 == null) {
        }
        if (fileMetadata2 != null) {
        }
    }

    /* renamed from: com.android.server.backup.restore.FullRestoreEngine$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$backup$restore$RestorePolicy;

        static {
            int[] iArr = new int[RestorePolicy.values().length];
            $SwitchMap$com$android$server$backup$restore$RestorePolicy = iArr;
            try {
                iArr[RestorePolicy.IGNORE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$server$backup$restore$RestorePolicy[RestorePolicy.ACCEPT_IF_APK.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$server$backup$restore$RestorePolicy[RestorePolicy.ACCEPT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    boolean shouldSkipReadOnlyDir(FileMetadata fileMetadata) {
        if (isValidParent(this.mReadOnlyParent, fileMetadata)) {
            return true;
        }
        if (isReadOnlyDir(fileMetadata)) {
            this.mReadOnlyParent = fileMetadata;
            Slog.w(BackupManagerService.TAG, "Skipping restore of " + fileMetadata.path + " and its contents as read-only dirs are currently not supported.");
            return true;
        }
        this.mReadOnlyParent = null;
        return false;
    }

    private static boolean isValidParent(FileMetadata fileMetadata, FileMetadata fileMetadata2) {
        return fileMetadata != null && fileMetadata2.packageName.equals(fileMetadata.packageName) && fileMetadata2.domain.equals(fileMetadata.domain) && fileMetadata2.path.startsWith(getPathWithTrailingSeparator(fileMetadata.path));
    }

    private static String getPathWithTrailingSeparator(String str) {
        String str2 = File.separator;
        if (str.endsWith(str2)) {
            return str;
        }
        return str + str2;
    }

    private static boolean isReadOnlyDir(FileMetadata fileMetadata) {
        return fileMetadata.type == 2 && (fileMetadata.mode & ((long) OsConstants.S_IWUSR)) == 0;
    }

    private void setUpPipes() throws IOException {
        synchronized (this.mPipesLock) {
            this.mPipes = ParcelFileDescriptor.createPipe();
            this.mPipesClosed = false;
        }
    }

    private void tearDownPipes() {
        ParcelFileDescriptor[] parcelFileDescriptorArr;
        synchronized (this.mPipesLock) {
            if (!this.mPipesClosed && (parcelFileDescriptorArr = this.mPipes) != null) {
                try {
                    parcelFileDescriptorArr[0].close();
                    this.mPipes[1].close();
                    this.mPipesClosed = true;
                } catch (IOException e) {
                    Slog.w(BackupManagerService.TAG, "Couldn't close agent pipes", e);
                }
            }
        }
    }

    private void tearDownAgent(ApplicationInfo applicationInfo, boolean z) {
        if (this.mAgent != null) {
            if (z) {
                try {
                    int generateRandomIntegerToken = this.mBackupManagerService.generateRandomIntegerToken();
                    long fullBackupAgentTimeoutMillis = this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
                    AdbRestoreFinishedLatch adbRestoreFinishedLatch = new AdbRestoreFinishedLatch(this.mBackupManagerService, this.mOperationStorage, generateRandomIntegerToken);
                    this.mBackupManagerService.prepareOperationTimeout(generateRandomIntegerToken, fullBackupAgentTimeoutMillis, adbRestoreFinishedLatch, 1);
                    if (this.mTargetApp.processName.equals("system")) {
                        new Thread(new AdbRestoreFinishedRunnable(this.mAgent, generateRandomIntegerToken, this.mBackupManagerService), "restore-sys-finished-runner").start();
                    } else {
                        this.mAgent.doRestoreFinished(generateRandomIntegerToken, this.mBackupManagerService.getBackupManagerBinder());
                    }
                    adbRestoreFinishedLatch.await();
                } catch (RemoteException unused) {
                    Slog.d(BackupManagerService.TAG, "Lost app trying to shut down");
                }
            }
            this.mBackupManagerService.tearDownAgentAndKill(applicationInfo);
            this.mAgent = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleTimeout() {
        tearDownPipes();
        setResult(-2);
        setRunning(false);
    }

    private boolean isRestorableFile(FileMetadata fileMetadata) {
        if (this.mBackupEligibilityRules.getBackupDestination() == 1) {
            return true;
        }
        if ("c".equals(fileMetadata.domain)) {
            return false;
        }
        return ("r".equals(fileMetadata.domain) && fileMetadata.path.startsWith("no_backup/")) ? false : true;
    }

    private static boolean isCanonicalFilePath(String str) {
        return (str.contains("..") || str.contains("//")) ? false : true;
    }

    private boolean shouldForceClearAppDataOnFullRestore(String str) {
        String stringForUser = Settings.Secure.getStringForUser(this.mBackupManagerService.getContext().getContentResolver(), "packages_to_clear_data_before_full_restore", this.mUserId);
        if (TextUtils.isEmpty(stringForUser)) {
            return false;
        }
        return Arrays.asList(stringForUser.split(";")).contains(str);
    }

    void sendOnRestorePackage(String str) {
        IFullBackupRestoreObserver iFullBackupRestoreObserver = this.mObserver;
        if (iFullBackupRestoreObserver != null) {
            try {
                iFullBackupRestoreObserver.onRestorePackage(str);
            } catch (RemoteException unused) {
                Slog.w(BackupManagerService.TAG, "full restore observer went away: restorePackage");
                this.mObserver = null;
            }
        }
    }
}
