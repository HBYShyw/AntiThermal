package com.android.server.pm;

import android.apex.ApexInfo;
import android.apex.ApexSessionInfo;
import android.apex.ApexSessionParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApexStagedEvent;
import android.content.pm.IStagedApexObserver;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManagerInternal;
import android.content.pm.StagedApexInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IntArray;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimingsTraceLog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.InstallLocationUtils;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.Preconditions;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.SystemServiceManager;
import com.android.server.pm.StagingManager;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.PackageStateUtils;
import com.android.server.rollback.RollbackManagerInternal;
import com.android.server.rollback.WatchdogRollbackLogger;
import com.android.server.usb.descriptors.UsbEndpointDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class StagingManager {
    private static final String TAG = "StagingManager";
    private final ApexManager mApexManager;
    private final CompletableFuture<Void> mBootCompleted;
    private final Context mContext;

    @GuardedBy({"mFailedPackageNames"})
    private final List<String> mFailedPackageNames;
    private String mFailureReason;
    private final File mFailureReasonFile;
    private String mNativeFailureReason;
    private final PowerManager mPowerManager;

    @GuardedBy({"mStagedApexObservers"})
    private final List<IStagedApexObserver> mStagedApexObservers;

    @GuardedBy({"mStagedSessions"})
    private final SparseArray<StagedSession> mStagedSessions;
    public final IStagingManagerExt mStagingManageExt;

    @GuardedBy({"mSuccessfulStagedSessionIds"})
    private final List<Integer> mSuccessfulStagedSessionIds;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface StagedSession {
        void abandon();

        boolean containsApexSession();

        boolean containsApkSession();

        List<StagedSession> getChildSessions();

        long getCommittedMillis();

        String getPackageName();

        int getParentSessionId();

        boolean hasParentSessionId();

        CompletableFuture<Void> installSession();

        boolean isApexSession();

        boolean isCommitted();

        boolean isDestroyed();

        boolean isInTerminalState();

        boolean isMultiPackage();

        boolean isSessionApplied();

        boolean isSessionFailed();

        boolean isSessionReady();

        boolean sessionContains(Predicate<StagedSession> predicate);

        int sessionId();

        PackageInstaller.SessionParams sessionParams();

        void setSessionApplied();

        void setSessionFailed(int i, String str);

        void setSessionReady();

        void verifySession();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StagingManager(Context context) {
        this(context, ApexManager.getInstance());
    }

    @VisibleForTesting
    StagingManager(Context context, ApexManager apexManager) {
        File file = new File("/metadata/staged-install/failure_reason.txt");
        this.mFailureReasonFile = file;
        this.mStagedSessions = new SparseArray<>();
        this.mFailedPackageNames = new ArrayList();
        this.mSuccessfulStagedSessionIds = new ArrayList();
        this.mStagedApexObservers = new ArrayList();
        this.mBootCompleted = new CompletableFuture<>();
        this.mStagingManageExt = (IStagingManagerExt) ExtLoader.type(IStagingManagerExt.class).base(this).create();
        this.mContext = context;
        this.mApexManager = apexManager;
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        if (file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                try {
                    this.mFailureReason = bufferedReader.readLine();
                    bufferedReader.close();
                } finally {
                }
            } catch (Exception unused) {
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Lifecycle extends SystemService {
        private static StagingManager sStagingManager;

        public void onStart() {
        }

        public Lifecycle(Context context) {
            super(context);
        }

        void startService(StagingManager stagingManager) {
            sStagingManager = stagingManager;
            ((SystemServiceManager) LocalServices.getService(SystemServiceManager.class)).startService(this);
        }

        public void onBootPhase(int i) {
            StagingManager stagingManager;
            if (i != 1000 || (stagingManager = sStagingManager) == null) {
                return;
            }
            stagingManager.markStagedSessionsAsSuccessful();
            sStagingManager.markBootCompleted();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markBootCompleted() {
        this.mApexManager.markBootCompleted();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerStagedApexObserver(final IStagedApexObserver iStagedApexObserver) {
        if (iStagedApexObserver == null) {
            return;
        }
        if (iStagedApexObserver.asBinder() != null) {
            try {
                iStagedApexObserver.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.pm.StagingManager.1
                    @Override // android.os.IBinder.DeathRecipient
                    public void binderDied() {
                        synchronized (StagingManager.this.mStagedApexObservers) {
                            StagingManager.this.mStagedApexObservers.remove(iStagedApexObserver);
                        }
                    }
                }, 0);
            } catch (RemoteException e) {
                Slog.w(TAG, e.getMessage());
            }
        }
        synchronized (this.mStagedApexObservers) {
            this.mStagedApexObservers.add(iStagedApexObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterStagedApexObserver(IStagedApexObserver iStagedApexObserver) {
        synchronized (this.mStagedApexObservers) {
            this.mStagedApexObservers.remove(iStagedApexObserver);
        }
    }

    private void abortCheckpoint(String str, boolean z, boolean z2) {
        Slog.e(TAG, str);
        if (z) {
            try {
                if (z2) {
                    try {
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.mFailureReasonFile));
                        try {
                            bufferedWriter.write(str);
                            bufferedWriter.close();
                        } finally {
                        }
                    } catch (Exception e) {
                        Slog.w(TAG, "Failed to save failure reason: ", e);
                    }
                    if (this.mApexManager.isApexSupported()) {
                        this.mApexManager.revertActiveSessions();
                    }
                    InstallLocationUtils.getStorageManager().abortChanges("abort-staged-install", false);
                }
            } catch (Exception e2) {
                Slog.wtf(TAG, "Failed to abort checkpoint", e2);
                if (this.mApexManager.isApexSupported()) {
                    this.mApexManager.revertActiveSessions();
                }
                this.mPowerManager.reboot(null);
            }
        }
    }

    private List<StagedSession> extractApexSessions(StagedSession stagedSession) {
        ArrayList arrayList = new ArrayList();
        if (stagedSession.isMultiPackage()) {
            for (StagedSession stagedSession2 : stagedSession.getChildSessions()) {
                if (stagedSession2.containsApexSession()) {
                    arrayList.add(stagedSession2);
                }
            }
        } else {
            arrayList.add(stagedSession);
        }
        return arrayList;
    }

    private void checkInstallationOfApkInApexSuccessful(StagedSession stagedSession) throws PackageManagerException {
        List<StagedSession> extractApexSessions = extractApexSessions(stagedSession);
        if (extractApexSessions.isEmpty()) {
            return;
        }
        Iterator<StagedSession> it = extractApexSessions.iterator();
        while (it.hasNext()) {
            String packageName = it.next().getPackageName();
            String apkInApexInstallError = this.mApexManager.getApkInApexInstallError(packageName);
            if (apkInApexInstallError != null) {
                throw new PackageManagerException(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Failed to install apk-in-apex of " + packageName + " : " + apkInApexInstallError);
            }
        }
    }

    private void snapshotAndRestoreForApexSession(StagedSession stagedSession) {
        if ((stagedSession.sessionParams().installFlags & DumpState.DUMP_DOMAIN_PREFERRED) != 0 || stagedSession.sessionParams().installReason == 5) {
            List<StagedSession> extractApexSessions = extractApexSessions(stagedSession);
            if (extractApexSessions.isEmpty()) {
                return;
            }
            int[] userIds = ((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).getUserIds();
            RollbackManagerInternal rollbackManagerInternal = (RollbackManagerInternal) LocalServices.getService(RollbackManagerInternal.class);
            int size = extractApexSessions.size();
            for (int i = 0; i < size; i++) {
                String packageName = extractApexSessions.get(i).getPackageName();
                snapshotAndRestoreApexUserData(packageName, userIds, rollbackManagerInternal);
                List<String> apksInApex = this.mApexManager.getApksInApex(packageName);
                int size2 = apksInApex.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    snapshotAndRestoreApkInApexUserData(apksInApex.get(i2), userIds, rollbackManagerInternal);
                }
            }
        }
    }

    private void snapshotAndRestoreApexUserData(String str, int[] iArr, RollbackManagerInternal rollbackManagerInternal) {
        rollbackManagerInternal.snapshotAndRestoreUserData(str, UserHandle.toUserHandles(iArr), 0, 0L, null, 0);
    }

    private void snapshotAndRestoreApkInApexUserData(String str, int[] iArr, RollbackManagerInternal rollbackManagerInternal) {
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        if (packageManagerInternal.getPackage(str) == null) {
            Slog.e(TAG, "Could not find package: " + str + "for snapshotting/restoring user data.");
            return;
        }
        PackageStateInternal packageStateInternal = packageManagerInternal.getPackageStateInternal(str);
        if (packageStateInternal != null) {
            rollbackManagerInternal.snapshotAndRestoreUserData(str, UserHandle.toUserHandles(PackageStateUtils.queryInstalledUsers(packageStateInternal, iArr, true)), packageStateInternal.getAppId(), packageStateInternal.getUserStateOrDefault(0).getCeDataInode(), packageStateInternal.getSeInfo(), 0);
        }
    }

    private void prepareForLoggingApexdRevert(StagedSession stagedSession, String str) {
        synchronized (this.mFailedPackageNames) {
            this.mNativeFailureReason = str;
            if (stagedSession.getPackageName() != null) {
                this.mFailedPackageNames.add(stagedSession.getPackageName());
            }
        }
    }

    private void resumeSession(StagedSession stagedSession, boolean z, boolean z2) throws PackageManagerException {
        boolean z3;
        Slog.d(TAG, "Resuming session " + stagedSession.sessionId());
        boolean containsApexSession = stagedSession.containsApexSession();
        PackageInstaller.SessionParams sessionParams = stagedSession.sessionParams();
        if (sessionParams == null || !this.mStagingManageExt.isSotaAppSession(stagedSession)) {
            z3 = false;
        } else {
            Slog.d(TAG, "sota app install from sau ,appPackageName is " + sessionParams.appPackageName + " ,needsCheckpoint is " + z2 + " ,force set isSotaApp true to jump revert.");
            z3 = true;
        }
        if (z && !z2 && !z3) {
            String str = "Reverting back to safe state. Marking " + stagedSession.sessionId() + " as failed.";
            String reasonForRevert = getReasonForRevert();
            if (!TextUtils.isEmpty(reasonForRevert)) {
                str = str + " Reason for revert: " + reasonForRevert;
            }
            Slog.d(TAG, str);
            stagedSession.setSessionFailed(-110, str);
            return;
        }
        if (containsApexSession) {
            checkInstallationOfApkInApexSuccessful(stagedSession);
            checkDuplicateApkInApex(stagedSession);
            snapshotAndRestoreForApexSession(stagedSession);
            Slog.i(TAG, "APEX packages in session " + stagedSession.sessionId() + " were successfully activated. Proceeding with APK packages, if any");
        }
        Slog.d(TAG, "Installing APK packages in session " + stagedSession.sessionId());
        TimingsTraceLog timingsTraceLog = new TimingsTraceLog("StagingManagerTiming", 262144L);
        timingsTraceLog.traceBegin("installApksInSession");
        installApksInSession(stagedSession);
        timingsTraceLog.traceEnd();
        if (containsApexSession) {
            if (z) {
                synchronized (this.mSuccessfulStagedSessionIds) {
                    this.mSuccessfulStagedSessionIds.add(Integer.valueOf(stagedSession.sessionId()));
                }
                return;
            }
            this.mApexManager.markStagedSessionSuccessful(stagedSession.sessionId());
        }
    }

    void onInstallationFailure(StagedSession stagedSession, PackageManagerException packageManagerException, boolean z, boolean z2) {
        stagedSession.setSessionFailed(packageManagerException.error, packageManagerException.getMessage());
        if (this.mStagingManageExt.isSotaAppSession(stagedSession)) {
            Slog.d(TAG, "Sota session do not abortCheckpoint and other rollback/reboot, return. Failed to install sessionId: " + stagedSession.sessionId() + " Error: " + packageManagerException.getMessage());
            return;
        }
        abortCheckpoint("Failed to install sessionId: " + stagedSession.sessionId() + " Error: " + packageManagerException.getMessage(), z, z2);
        if (stagedSession.containsApexSession()) {
            if (!this.mApexManager.revertActiveSessions()) {
                Slog.e(TAG, "Failed to abort APEXd session");
            } else {
                Slog.e(TAG, "Successfully aborted apexd session. Rebooting device in order to revert to the previous state of APEXd.");
                this.mPowerManager.reboot(null);
            }
        }
    }

    private String getReasonForRevert() {
        if (!TextUtils.isEmpty(this.mFailureReason)) {
            return this.mFailureReason;
        }
        if (TextUtils.isEmpty(this.mNativeFailureReason)) {
            return "";
        }
        return "Session reverted due to crashing native process: " + this.mNativeFailureReason;
    }

    private void checkDuplicateApkInApex(StagedSession stagedSession) throws PackageManagerException {
        if (stagedSession.isMultiPackage()) {
            ArraySet arraySet = new ArraySet();
            for (StagedSession stagedSession2 : stagedSession.getChildSessions()) {
                if (!stagedSession2.isApexSession()) {
                    arraySet.add(stagedSession2.getPackageName());
                }
            }
            for (StagedSession stagedSession3 : extractApexSessions(stagedSession)) {
                String packageName = stagedSession3.getPackageName();
                for (String str : this.mApexManager.getApksInApex(packageName)) {
                    if (!arraySet.add(str)) {
                        throw new PackageManagerException(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Package: " + packageName + " in session: " + stagedSession3.sessionId() + " has duplicate apk-in-apex: " + str, (Throwable) null);
                    }
                }
            }
        }
    }

    private void installApksInSession(StagedSession stagedSession) throws PackageManagerException {
        try {
            stagedSession.installSession().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e2) {
            throw ((PackageManagerException) e2.getCause());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void commitSession(StagedSession stagedSession) {
        createSession(stagedSession);
        handleCommittedSession(stagedSession);
    }

    private void handleCommittedSession(StagedSession stagedSession) {
        if (stagedSession.isSessionReady() && stagedSession.containsApexSession()) {
            notifyStagedApexObservers();
        }
    }

    @VisibleForTesting
    void createSession(StagedSession stagedSession) {
        synchronized (this.mStagedSessions) {
            this.mStagedSessions.append(stagedSession.sessionId(), stagedSession);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abortSession(StagedSession stagedSession) {
        synchronized (this.mStagedSessions) {
            this.mStagedSessions.remove(stagedSession.sessionId());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abortCommittedSession(StagedSession stagedSession) {
        int sessionId = stagedSession.sessionId();
        if (stagedSession.isInTerminalState()) {
            Slog.w(TAG, "Cannot abort session in final state: " + sessionId);
            return;
        }
        if (!stagedSession.isDestroyed()) {
            throw new IllegalStateException("Committed session must be destroyed before aborting it from StagingManager");
        }
        if (getStagedSession(sessionId) == null) {
            Slog.w(TAG, "Session " + sessionId + " has been abandoned already");
            return;
        }
        if (stagedSession.isSessionReady()) {
            if (!ensureActiveApexSessionIsAborted(stagedSession)) {
                Slog.e(TAG, "Failed to abort apex session " + stagedSession.sessionId());
            }
            if (stagedSession.containsApexSession()) {
                notifyStagedApexObservers();
            }
        }
        abortSession(stagedSession);
    }

    private boolean ensureActiveApexSessionIsAborted(StagedSession stagedSession) {
        ApexSessionInfo stagedSessionInfo;
        if (!stagedSession.containsApexSession() || (stagedSessionInfo = this.mApexManager.getStagedSessionInfo(stagedSession.sessionId())) == null || isApexSessionFinalized(stagedSessionInfo)) {
            return true;
        }
        return this.mApexManager.abortStagedSession(stagedSession.sessionId());
    }

    private boolean isApexSessionFinalized(ApexSessionInfo apexSessionInfo) {
        return apexSessionInfo.isUnknown || apexSessionInfo.isActivationFailed || apexSessionInfo.isSuccess || apexSessionInfo.isReverted;
    }

    private static boolean isApexSessionFailed(ApexSessionInfo apexSessionInfo) {
        return apexSessionInfo.isActivationFailed || apexSessionInfo.isUnknown || apexSessionInfo.isReverted || apexSessionInfo.isRevertInProgress || apexSessionInfo.isRevertFailed;
    }

    private void handleNonReadyAndDestroyedSessions(List<StagedSession> list) {
        int size = list.size();
        int i = 0;
        while (i < size) {
            final StagedSession stagedSession = list.get(i);
            if (stagedSession.isDestroyed()) {
                stagedSession.abandon();
                list.set(i, list.set(size - 1, stagedSession));
            } else if (stagedSession.isSessionReady()) {
                i++;
            } else {
                Slog.i(TAG, "Restart verification for session=" + stagedSession.sessionId());
                this.mBootCompleted.thenRun(new Runnable() { // from class: com.android.server.pm.StagingManager$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        StagingManager.StagedSession.this.verifySession();
                    }
                });
                list.set(i, list.set(size + (-1), stagedSession));
            }
            size--;
        }
        list.subList(size, list.size()).clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreSessions(List<StagedSession> list, boolean z) {
        List<StagedSession> list2;
        TimingsTraceLog timingsTraceLog = new TimingsTraceLog("StagingManagerTiming", 262144L);
        timingsTraceLog.traceBegin("restoreSessions");
        if (SystemProperties.getBoolean("sys.boot_completed", false)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            StagedSession stagedSession = list.get(i);
            Preconditions.checkArgument(!stagedSession.hasParentSessionId(), stagedSession.sessionId() + " is a child session");
            Preconditions.checkArgument(stagedSession.isCommitted(), stagedSession.sessionId() + " is not committed");
            Preconditions.checkArgument(true ^ stagedSession.isInTerminalState(), stagedSession.sessionId() + " is in terminal state");
            createSession(stagedSession);
        }
        if (!z) {
            list2 = list;
        } else if (this.mStagingManageExt.isBootFromSotaAppUpdate()) {
            list2 = new ArrayList<>();
            for (StagedSession stagedSession2 : list) {
                if (this.mStagingManageExt.isSotaAppSession(stagedSession2)) {
                    list2.add(stagedSession2);
                } else {
                    stagedSession2.setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Build fingerprint has changed");
                }
            }
            if (list2.isEmpty()) {
                return;
            }
        } else {
            for (int i2 = 0; i2 < list.size(); i2++) {
                list.get(i2).setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Build fingerprint has changed");
            }
            return;
        }
        try {
            boolean supportsCheckpoint = InstallLocationUtils.getStorageManager().supportsCheckpoint();
            boolean needsCheckpoint = InstallLocationUtils.getStorageManager().needsCheckpoint();
            if (list2.size() > 1 && !supportsCheckpoint) {
                throw new IllegalStateException("Detected multiple staged sessions on a device without fs-checkpoint support");
            }
            handleNonReadyAndDestroyedSessions(list2);
            SparseArray<ApexSessionInfo> sessions = this.mApexManager.getSessions();
            boolean z2 = false;
            boolean z3 = false;
            for (int i3 = 0; i3 < list2.size(); i3++) {
                StagedSession stagedSession3 = list2.get(i3);
                if (stagedSession3.containsApexSession()) {
                    ApexSessionInfo apexSessionInfo = sessions.get(stagedSession3.sessionId());
                    if (apexSessionInfo == null || apexSessionInfo.isUnknown) {
                        stagedSession3.setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "apexd did not know anything about a staged session supposed to be activated");
                    } else if (isApexSessionFailed(apexSessionInfo)) {
                        if (!TextUtils.isEmpty(apexSessionInfo.crashingNativeProcess)) {
                            prepareForLoggingApexdRevert(stagedSession3, apexSessionInfo.crashingNativeProcess);
                        }
                        String reasonForRevert = getReasonForRevert();
                        String str = "APEX activation failed.";
                        if (!TextUtils.isEmpty(reasonForRevert)) {
                            str = "APEX activation failed. Reason: " + reasonForRevert;
                        } else if (!TextUtils.isEmpty(apexSessionInfo.errorMessage)) {
                            str = "APEX activation failed. Error: " + apexSessionInfo.errorMessage;
                        }
                        Slog.d(TAG, str);
                        stagedSession3.setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, str);
                    } else if (apexSessionInfo.isActivated || apexSessionInfo.isSuccess) {
                        z2 = true;
                    } else if (apexSessionInfo.isStaged) {
                        stagedSession3.setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Staged session " + stagedSession3.sessionId() + " at boot didn't activate nor fail. Marking it as failed anyway.");
                    } else {
                        Slog.w(TAG, "Apex session " + stagedSession3.sessionId() + " is in impossible state");
                        stagedSession3.setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Impossible state");
                    }
                    z3 = true;
                }
            }
            if (z2 && z3) {
                abortCheckpoint("Found both applied and failed apex sessions", supportsCheckpoint, needsCheckpoint);
                return;
            }
            if (z3) {
                for (int i4 = 0; i4 < list2.size(); i4++) {
                    StagedSession stagedSession4 = list2.get(i4);
                    if (!stagedSession4.isSessionFailed()) {
                        stagedSession4.setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "Another apex session failed");
                    }
                }
                return;
            }
            for (int i5 = 0; i5 < list2.size(); i5++) {
                StagedSession stagedSession5 = list2.get(i5);
                try {
                    resumeSession(stagedSession5, supportsCheckpoint, needsCheckpoint);
                } catch (PackageManagerException e) {
                    onInstallationFailure(stagedSession5, e, supportsCheckpoint, needsCheckpoint);
                } catch (Exception e2) {
                    Slog.e(TAG, "Staged install failed due to unhandled exception", e2);
                    onInstallationFailure(stagedSession5, new PackageManagerException(-110, "Staged install failed due to unhandled exception: " + e2), supportsCheckpoint, needsCheckpoint);
                }
            }
            timingsTraceLog.traceEnd();
        } catch (RemoteException e3) {
            throw new IllegalStateException("Failed to get checkpoint status", e3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: logFailedApexSessionsIfNecessary, reason: merged with bridge method [inline-methods] */
    public void lambda$onBootCompletedBroadcastReceived$1() {
        synchronized (this.mFailedPackageNames) {
            if (!this.mFailedPackageNames.isEmpty()) {
                WatchdogRollbackLogger.logApexdRevert(this.mContext, this.mFailedPackageNames, this.mNativeFailureReason);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markStagedSessionsAsSuccessful() {
        synchronized (this.mSuccessfulStagedSessionIds) {
            for (int i = 0; i < this.mSuccessfulStagedSessionIds.size(); i++) {
                this.mApexManager.markStagedSessionSuccessful(this.mSuccessfulStagedSessionIds.get(i).intValue());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady() {
        new Lifecycle(this.mContext).startService(this);
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.pm.StagingManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                StagingManager.this.onBootCompletedBroadcastReceived();
                context.unregisterReceiver(this);
            }
        }, new IntentFilter("android.intent.action.BOOT_COMPLETED"));
        this.mFailureReasonFile.delete();
    }

    @VisibleForTesting
    void onBootCompletedBroadcastReceived() {
        this.mBootCompleted.complete(null);
        BackgroundThread.getExecutor().execute(new Runnable() { // from class: com.android.server.pm.StagingManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                StagingManager.this.lambda$onBootCompletedBroadcastReceived$1();
            }
        });
    }

    private StagedSession getStagedSession(int i) {
        StagedSession stagedSession;
        synchronized (this.mStagedSessions) {
            stagedSession = this.mStagedSessions.get(i);
        }
        return stagedSession;
    }

    @VisibleForTesting
    Map<String, ApexInfo> getStagedApexInfos(StagedSession stagedSession) {
        Preconditions.checkArgument(stagedSession != null, "Session is null");
        Preconditions.checkArgument(true ^ stagedSession.hasParentSessionId(), stagedSession.sessionId() + " session has parent session");
        Preconditions.checkArgument(stagedSession.containsApexSession(), stagedSession.sessionId() + " session does not contain apex");
        if (!stagedSession.isSessionReady() || stagedSession.isDestroyed()) {
            return Collections.emptyMap();
        }
        ApexSessionParams apexSessionParams = new ApexSessionParams();
        apexSessionParams.sessionId = stagedSession.sessionId();
        IntArray intArray = new IntArray();
        if (stagedSession.isMultiPackage()) {
            for (StagedSession stagedSession2 : stagedSession.getChildSessions()) {
                if (stagedSession2.isApexSession()) {
                    intArray.add(stagedSession2.sessionId());
                }
            }
        }
        apexSessionParams.childSessionIds = intArray.toArray();
        ApexInfo[] stagedApexInfos = this.mApexManager.getStagedApexInfos(apexSessionParams);
        ArrayMap arrayMap = new ArrayMap();
        for (ApexInfo apexInfo : stagedApexInfos) {
            arrayMap.put(apexInfo.moduleName, apexInfo);
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getStagedApexModuleNames() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mStagedSessions) {
            for (int i = 0; i < this.mStagedSessions.size(); i++) {
                StagedSession valueAt = this.mStagedSessions.valueAt(i);
                if (valueAt.isSessionReady() && !valueAt.isDestroyed() && !valueAt.hasParentSessionId() && valueAt.containsApexSession()) {
                    arrayList.addAll(getStagedApexInfos(valueAt).keySet());
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StagedApexInfo getStagedApexInfo(String str) {
        ApexInfo apexInfo;
        synchronized (this.mStagedSessions) {
            for (int i = 0; i < this.mStagedSessions.size(); i++) {
                StagedSession valueAt = this.mStagedSessions.valueAt(i);
                if (valueAt.isSessionReady() && !valueAt.isDestroyed() && !valueAt.hasParentSessionId() && valueAt.containsApexSession() && (apexInfo = getStagedApexInfos(valueAt).get(str)) != null) {
                    StagedApexInfo stagedApexInfo = new StagedApexInfo();
                    stagedApexInfo.moduleName = apexInfo.moduleName;
                    stagedApexInfo.diskImagePath = apexInfo.modulePath;
                    stagedApexInfo.versionCode = apexInfo.versionCode;
                    stagedApexInfo.versionName = apexInfo.versionName;
                    stagedApexInfo.hasClassPathJars = apexInfo.hasClassPathJars;
                    return stagedApexInfo;
                }
            }
            return null;
        }
    }

    private void notifyStagedApexObservers() {
        synchronized (this.mStagedApexObservers) {
            for (IStagedApexObserver iStagedApexObserver : this.mStagedApexObservers) {
                ApexStagedEvent apexStagedEvent = new ApexStagedEvent();
                apexStagedEvent.stagedApexModuleNames = (String[]) getStagedApexModuleNames().toArray(new String[0]);
                try {
                    iStagedApexObserver.onApexStaged(apexStagedEvent);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to contact the observer " + e.getMessage());
                }
            }
        }
    }
}
