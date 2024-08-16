package com.android.server.rollback;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.VersionedPackage;
import android.content.rollback.PackageRollbackInfo;
import android.content.rollback.RollbackInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.ext.SdkExtensions;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseIntArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.server.LocalServices;
import com.android.server.RescueParty;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Rollback {
    static final int ROLLBACK_STATE_AVAILABLE = 1;
    static final int ROLLBACK_STATE_COMMITTED = 3;
    static final int ROLLBACK_STATE_DELETED = 4;
    static final int ROLLBACK_STATE_ENABLING = 0;
    private static final String TAG = "RollbackManager";
    public final RollbackInfo info;
    private final File mBackupDir;
    private final SparseIntArray mExtensionVersions;
    private final Handler mHandler;
    private final String mInstallerPackageName;
    private final int mOriginalSessionId;
    private final int[] mPackageSessionIds;
    private boolean mRestoreUserDataInProgress;
    private int mState;
    private String mStateDescription;
    private Instant mTimestamp;
    private final int mUserId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface RollbackState {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rollback(int i, File file, int i2, boolean z, int i3, String str, int[] iArr, SparseIntArray sparseIntArray) {
        this.mStateDescription = "";
        this.mRestoreUserDataInProgress = false;
        this.info = new RollbackInfo(i, new ArrayList(), z, new ArrayList(), -1);
        this.mUserId = i3;
        this.mInstallerPackageName = str;
        this.mBackupDir = file;
        this.mOriginalSessionId = i2;
        this.mState = 0;
        this.mTimestamp = Instant.now();
        this.mPackageSessionIds = iArr != null ? iArr : new int[0];
        Objects.requireNonNull(sparseIntArray);
        this.mExtensionVersions = sparseIntArray;
        this.mHandler = Looper.myLooper() != null ? new Handler(Looper.myLooper()) : null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rollback(RollbackInfo rollbackInfo, File file, Instant instant, int i, int i2, String str, boolean z, int i3, String str2, SparseIntArray sparseIntArray) {
        this.info = rollbackInfo;
        this.mUserId = i3;
        this.mInstallerPackageName = str2;
        this.mBackupDir = file;
        this.mTimestamp = instant;
        this.mOriginalSessionId = i;
        this.mState = i2;
        this.mStateDescription = str;
        this.mRestoreUserDataInProgress = z;
        Objects.requireNonNull(sparseIntArray);
        this.mExtensionVersions = sparseIntArray;
        this.mPackageSessionIds = new int[0];
        this.mHandler = Looper.myLooper() != null ? new Handler(Looper.myLooper()) : null;
    }

    private void assertInWorkerThread() {
        Handler handler = this.mHandler;
        Preconditions.checkState(handler == null || handler.getLooper().isCurrentThread());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isStaged() {
        return this.info.isStaged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public File getBackupDir() {
        return this.mBackupDir;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Instant getTimestamp() {
        assertInWorkerThread();
        return this.mTimestamp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTimestamp(Instant instant) {
        assertInWorkerThread();
        this.mTimestamp = instant;
        RollbackStore.saveRollback(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getOriginalSessionId() {
        return this.mOriginalSessionId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getUserId() {
        return this.mUserId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getInstallerPackageName() {
        return this.mInstallerPackageName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseIntArray getExtensionVersions() {
        return this.mExtensionVersions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEnabling() {
        assertInWorkerThread();
        return this.mState == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAvailable() {
        assertInWorkerThread();
        return this.mState == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCommitted() {
        assertInWorkerThread();
        return this.mState == 3;
    }

    boolean isDeleted() {
        assertInWorkerThread();
        return this.mState == 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveRollback() {
        assertInWorkerThread();
        RollbackStore.saveRollback(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean enableForPackage(String str, long j, long j2, boolean z, String str2, String[] strArr, int i) {
        assertInWorkerThread();
        try {
            RollbackStore.backupPackageCodePath(this, str, str2);
            if (!ArrayUtils.isEmpty(strArr)) {
                for (String str3 : strArr) {
                    RollbackStore.backupPackageCodePath(this, str, str3);
                }
            }
            this.info.getPackages().add(new PackageRollbackInfo(new VersionedPackage(str, j), new VersionedPackage(str, j2), new ArrayList(), new ArrayList(), z, false, new ArrayList(), i));
            return true;
        } catch (IOException e) {
            Slog.e(TAG, "Unable to copy package for rollback for " + str, e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean enableForPackageInApex(String str, long j, int i) {
        assertInWorkerThread();
        this.info.getPackages().add(new PackageRollbackInfo(new VersionedPackage(str, 0), new VersionedPackage(str, j), new ArrayList(), new ArrayList(), false, true, new ArrayList(), i));
        return true;
    }

    private static void addAll(List<Integer> list, int[] iArr) {
        for (int i : iArr) {
            list.add(Integer.valueOf(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void snapshotUserData(String str, int[] iArr, AppDataRollbackHelper appDataRollbackHelper) {
        assertInWorkerThread();
        if (isEnabling()) {
            for (PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
                if (packageRollbackInfo.getPackageName().equals(str)) {
                    if (packageRollbackInfo.getRollbackDataPolicy() == 0) {
                        appDataRollbackHelper.snapshotAppData(this.info.getRollbackId(), packageRollbackInfo, iArr);
                        addAll(packageRollbackInfo.getSnapshottedUsers(), iArr);
                        RollbackStore.saveRollback(this);
                        return;
                    }
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void commitPendingBackupAndRestoreForUser(int i, AppDataRollbackHelper appDataRollbackHelper) {
        assertInWorkerThread();
        if (appDataRollbackHelper.commitPendingBackupAndRestoreForUser(i, this)) {
            RollbackStore.saveRollback(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeAvailable() {
        assertInWorkerThread();
        if (isDeleted()) {
            Slog.w(TAG, "Cannot make deleted rollback available.");
            return;
        }
        setState(1, "");
        this.mTimestamp = Instant.now();
        RollbackStore.saveRollback(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void commit(final Context context, final List<VersionedPackage> list, String str, final IntentSender intentSender) {
        boolean z;
        File[] fileArr;
        PackageInstaller.Session session;
        assertInWorkerThread();
        if (!isAvailable()) {
            RollbackManagerServiceImpl.sendFailure(context, intentSender, 2, "Rollback unavailable");
            return;
        }
        boolean z2 = true;
        if (containsApex() && wasCreatedAtLowerExtensionVersion()) {
            if (extensionVersionReductionWouldViolateConstraint(this.mExtensionVersions, (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class))) {
                RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "Rollback may violate a minExtensionVersion constraint");
                return;
            }
        }
        try {
            int i = 0;
            PackageManager packageManager = context.createPackageContextAsUser(str, 0, UserHandle.of(this.mUserId)).getPackageManager();
            try {
                PackageInstaller packageInstaller = packageManager.getPackageInstaller();
                PackageInstaller.SessionParams sessionParams = new PackageInstaller.SessionParams(1);
                sessionParams.setRequestDowngrade(true);
                sessionParams.setMultiPackage();
                if (isStaged()) {
                    sessionParams.setStaged();
                }
                int i2 = 5;
                sessionParams.setInstallReason(5);
                int createSession = packageInstaller.createSession(sessionParams);
                PackageInstaller.Session openSession = packageInstaller.openSession(createSession);
                ArrayList arrayList = new ArrayList(this.info.getPackages().size());
                for (PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
                    arrayList.add(packageRollbackInfo.getPackageName());
                    if (!packageRollbackInfo.isApkInApex()) {
                        PackageInstaller.SessionParams sessionParams2 = new PackageInstaller.SessionParams(z2 ? 1 : 0);
                        String str2 = this.mInstallerPackageName;
                        if (TextUtils.isEmpty(str2)) {
                            str2 = packageManager.getInstallerPackageName(packageRollbackInfo.getPackageName());
                        }
                        if (str2 != null) {
                            sessionParams2.setInstallerPackageName(str2);
                        }
                        sessionParams2.setRequestDowngrade(z2);
                        sessionParams2.setRequiredInstalledVersionCode(packageRollbackInfo.getVersionRolledBackFrom().getLongVersionCode());
                        sessionParams2.setInstallReason(i2);
                        if (isStaged()) {
                            sessionParams2.setStaged();
                        }
                        if (packageRollbackInfo.isApex()) {
                            sessionParams2.setInstallAsApex();
                        }
                        int createSession2 = packageInstaller.createSession(sessionParams2);
                        PackageInstaller.Session openSession2 = packageInstaller.openSession(createSession2);
                        File[] packageCodePaths = RollbackStore.getPackageCodePaths(this, packageRollbackInfo.getPackageName());
                        if (packageCodePaths == null) {
                            RollbackManagerServiceImpl.sendFailure(context, intentSender, z2 ? 1 : 0, "Backup copy of package: " + packageRollbackInfo.getPackageName() + " is inaccessible");
                            return;
                        }
                        int length = packageCodePaths.length;
                        while (i < length) {
                            File file = packageCodePaths[i];
                            ParcelFileDescriptor open = ParcelFileDescriptor.open(file, 268435456);
                            try {
                                long clearCallingIdentity = Binder.clearCallingIdentity();
                                PackageManager packageManager2 = packageManager;
                                try {
                                    try {
                                        openSession2.stageViaHardLink(file.getAbsolutePath());
                                        z = false;
                                    } catch (Throwable th) {
                                        Binder.restoreCallingIdentity(clearCallingIdentity);
                                        throw th;
                                    }
                                } catch (Exception unused) {
                                    z = true;
                                }
                                if (z) {
                                    String name = file.getName();
                                    long length2 = file.length();
                                    fileArr = packageCodePaths;
                                    session = openSession2;
                                    openSession2.write(name, 0L, length2, open);
                                } else {
                                    fileArr = packageCodePaths;
                                    session = openSession2;
                                }
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                                if (open != null) {
                                    open.close();
                                }
                                i++;
                                packageCodePaths = fileArr;
                                packageManager = packageManager2;
                                openSession2 = session;
                            } finally {
                            }
                        }
                        openSession.addChildSessionId(createSession2);
                        packageManager = packageManager;
                        z2 = true;
                        i = 0;
                        i2 = 5;
                    }
                }
                RescueParty.resetDeviceConfigForPackages(arrayList);
                LocalIntentReceiver localIntentReceiver = new LocalIntentReceiver(new Consumer() { // from class: com.android.server.rollback.Rollback$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        Rollback.this.lambda$commit$1(context, intentSender, list, (Intent) obj);
                    }
                });
                setState(3, "");
                this.info.setCommittedSessionId(createSession);
                this.mRestoreUserDataInProgress = true;
                openSession.commit(localIntentReceiver.getIntentSender());
            } catch (IOException e) {
                Slog.e(TAG, "Rollback failed", e);
                RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "IOException: " + e.toString());
            }
        } catch (PackageManager.NameNotFoundException unused2) {
            RollbackManagerServiceImpl.sendFailure(context, intentSender, 1, "Invalid callerPackageName");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commit$1(final Context context, final IntentSender intentSender, final List list, final Intent intent) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.rollback.Rollback$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Rollback.this.lambda$commit$0(intent, context, intentSender, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commit$0(Intent intent, Context context, IntentSender intentSender, List list) {
        assertInWorkerThread();
        if (intent.getIntExtra("android.content.pm.extra.STATUS", 1) != 0) {
            setState(1, "Commit failed");
            this.mRestoreUserDataInProgress = false;
            this.info.setCommittedSessionId(-1);
            RollbackManagerServiceImpl.sendFailure(context, intentSender, 3, "Rollback downgrade install failed: " + intent.getStringExtra("android.content.pm.extra.STATUS_MESSAGE"));
            return;
        }
        if (!isStaged()) {
            this.mRestoreUserDataInProgress = false;
        }
        this.info.getCausePackages().addAll(list);
        RollbackStore.deletePackageCodePaths(this);
        RollbackStore.saveRollback(this);
        try {
            Intent intent2 = new Intent();
            intent2.putExtra("android.content.rollback.extra.STATUS", 0);
            intentSender.sendIntent(context, 0, intent2, null, null);
        } catch (IntentSender.SendIntentException unused) {
        }
        Intent intent3 = new Intent("android.intent.action.ROLLBACK_COMMITTED");
        Iterator it = ((UserManager) context.getSystemService(UserManager.class)).getUserHandles(true).iterator();
        while (it.hasNext()) {
            context.sendBroadcastAsUser(intent3, (UserHandle) it.next(), "android.permission.MANAGE_ROLLBACKS");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean restoreUserDataForPackageIfInProgress(String str, int[] iArr, int i, String str2, AppDataRollbackHelper appDataRollbackHelper) {
        assertInWorkerThread();
        if (!isRestoreUserDataInProgress()) {
            return false;
        }
        for (PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
            if (packageRollbackInfo.getPackageName().equals(str)) {
                boolean z = false;
                for (int i2 : iArr) {
                    z |= appDataRollbackHelper.restoreAppData(this.info.getRollbackId(), packageRollbackInfo, i2, i, str2);
                }
                if (z) {
                    RollbackStore.saveRollback(this);
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void delete(AppDataRollbackHelper appDataRollbackHelper, String str) {
        assertInWorkerThread();
        ArraySet arraySet = new ArraySet();
        boolean z = false;
        for (PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
            List snapshottedUsers = packageRollbackInfo.getSnapshottedUsers();
            if (packageRollbackInfo.isApex()) {
                arraySet.addAll(snapshottedUsers);
                z = true;
            } else {
                for (int i = 0; i < snapshottedUsers.size(); i++) {
                    appDataRollbackHelper.destroyAppDataSnapshot(this.info.getRollbackId(), packageRollbackInfo, ((Integer) snapshottedUsers.get(i)).intValue());
                }
            }
        }
        if (z) {
            appDataRollbackHelper.destroyApexDeSnapshots(this.info.getRollbackId());
            Iterator it = arraySet.iterator();
            while (it.hasNext()) {
                appDataRollbackHelper.destroyApexCeSnapshots(((Integer) it.next()).intValue(), this.info.getRollbackId());
            }
        }
        RollbackStore.deleteRollback(this);
        setState(4, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRestoreUserDataInProgress() {
        assertInWorkerThread();
        return this.mRestoreUserDataInProgress;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRestoreUserDataInProgress(boolean z) {
        assertInWorkerThread();
        this.mRestoreUserDataInProgress = z;
        RollbackStore.saveRollback(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean includesPackage(String str) {
        assertInWorkerThread();
        Iterator it = this.info.getPackages().iterator();
        while (it.hasNext()) {
            if (((PackageRollbackInfo) it.next()).getPackageName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean includesPackageWithDifferentVersion(String str, long j) {
        assertInWorkerThread();
        for (PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
            if (packageRollbackInfo.getPackageName().equals(str) && packageRollbackInfo.getVersionRolledBackFrom().getLongVersionCode() != j) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getPackageNames() {
        assertInWorkerThread();
        ArrayList arrayList = new ArrayList();
        Iterator it = this.info.getPackages().iterator();
        while (it.hasNext()) {
            arrayList.add(((PackageRollbackInfo) it.next()).getPackageName());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getApexPackageNames() {
        assertInWorkerThread();
        ArrayList arrayList = new ArrayList();
        for (PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
            if (packageRollbackInfo.isApex()) {
                arrayList.add(packageRollbackInfo.getPackageName());
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsSessionId(int i) {
        for (int i2 : this.mPackageSessionIds) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allPackagesEnabled() {
        assertInWorkerThread();
        Iterator it = this.info.getPackages().iterator();
        int i = 0;
        while (it.hasNext()) {
            if (!((PackageRollbackInfo) it.next()).isApkInApex()) {
                i++;
            }
        }
        return i == this.mPackageSessionIds.length;
    }

    static String rollbackStateToString(int i) {
        if (i == 0) {
            return "enabling";
        }
        if (i == 1) {
            return "available";
        }
        if (i == 3) {
            return "committed";
        }
        if (i == 4) {
            return "deleted";
        }
        throw new AssertionError("Invalid rollback state: " + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int rollbackStateFromString(String str) throws ParseException {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1491142788:
                if (str.equals("committed")) {
                    c = 0;
                    break;
                }
                break;
            case -733902135:
                if (str.equals("available")) {
                    c = 1;
                    break;
                }
                break;
            case 1550463001:
                if (str.equals("deleted")) {
                    c = 2;
                    break;
                }
                break;
            case 1642196352:
                if (str.equals("enabling")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 3;
            case 1:
                return 1;
            case 2:
                return 4;
            case 3:
                return 0;
            default:
                throw new ParseException("Invalid rollback state: " + str, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getStateAsString() {
        assertInWorkerThread();
        return rollbackStateToString(this.mState);
    }

    @VisibleForTesting
    static boolean extensionVersionReductionWouldViolateConstraint(SparseIntArray sparseIntArray, PackageManagerInternal packageManagerInternal) {
        if (sparseIntArray.size() == 0) {
            return false;
        }
        List<String> packageNames = packageManagerInternal.getPackageList().getPackageNames();
        for (int i = 0; i < packageNames.size(); i++) {
            SparseIntArray minExtensionVersions = packageManagerInternal.getPackage(packageNames.get(i)).getMinExtensionVersions();
            if (minExtensionVersions != null) {
                for (int i2 = 0; i2 < sparseIntArray.size(); i2++) {
                    if (sparseIntArray.valueAt(i2) < minExtensionVersions.get(sparseIntArray.keyAt(i2), -1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean wasCreatedAtLowerExtensionVersion() {
        for (int i = 0; i < this.mExtensionVersions.size(); i++) {
            if (SdkExtensions.getExtensionVersion(this.mExtensionVersions.keyAt(i)) > this.mExtensionVersions.valueAt(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsApex() {
        Iterator it = this.info.getPackages().iterator();
        while (it.hasNext()) {
            if (((PackageRollbackInfo) it.next()).isApex()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        assertInWorkerThread();
        indentingPrintWriter.println(this.info.getRollbackId() + ":");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("-state: " + getStateAsString());
        indentingPrintWriter.println("-stateDescription: " + this.mStateDescription);
        indentingPrintWriter.println("-timestamp: " + getTimestamp());
        indentingPrintWriter.println("-isStaged: " + isStaged());
        indentingPrintWriter.println("-originalSessionId: " + getOriginalSessionId());
        indentingPrintWriter.println("-packages:");
        indentingPrintWriter.increaseIndent();
        for (PackageRollbackInfo packageRollbackInfo : this.info.getPackages()) {
            indentingPrintWriter.println(packageRollbackInfo.getPackageName() + " " + packageRollbackInfo.getVersionRolledBackFrom().getLongVersionCode() + " -> " + packageRollbackInfo.getVersionRolledBackTo().getLongVersionCode());
        }
        indentingPrintWriter.decreaseIndent();
        if (isCommitted()) {
            indentingPrintWriter.println("-causePackages:");
            indentingPrintWriter.increaseIndent();
            for (VersionedPackage versionedPackage : this.info.getCausePackages()) {
                indentingPrintWriter.println(versionedPackage.getPackageName() + " " + versionedPackage.getLongVersionCode());
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("-committedSessionId: " + this.info.getCommittedSessionId());
        }
        if (this.mExtensionVersions.size() > 0) {
            indentingPrintWriter.println("-extensionVersions:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println(this.mExtensionVersions.toString());
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getStateDescription() {
        assertInWorkerThread();
        return this.mStateDescription;
    }

    @VisibleForTesting
    void setState(int i, String str) {
        assertInWorkerThread();
        this.mState = i;
        this.mStateDescription = str;
    }
}
