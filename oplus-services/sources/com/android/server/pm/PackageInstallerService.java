package com.android.server.pm;

import android.R;
import android.app.ActivityManager;
import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PackageDeleteObserver;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManager;
import android.app.admin.DevicePolicyManagerInternal;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageInstallerCallback;
import android.content.pm.IPackageInstallerSession;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.VersionedPackage;
import android.content.pm.parsing.FrameworkParsingPackageUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteCallback;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SELinux;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.ExceptionUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.InstallLocationUtils;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.ImageUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.IoThread;
import com.android.server.LocalServices;
import com.android.server.SystemConfig;
import com.android.server.SystemService;
import com.android.server.SystemServiceManager;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.pm.PackageInstallerService;
import com.android.server.pm.PackageInstallerSession;
import com.android.server.pm.parsing.PackageParser2;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.utils.RequestThrottle;
import com.android.server.pm.verify.domain.DomainVerificationPersistence;
import com.android.server.usb.descriptors.UsbEndpointDescriptor;
import com.android.server.vibrator.VibratorManagerService;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageInstallerService extends IPackageInstaller.Stub implements PackageSessionProvider {
    private static final int ADB_DEV_MODE = 36;
    private static final int HISTORICAL_CLEAR_SIZE = 500;
    private static final int HISTORICAL_SESSIONS_THRESHOLD = 5000;
    private static final long MAX_ACTIVE_SESSIONS_NO_PERMISSION = 50;
    private static final long MAX_ACTIVE_SESSIONS_WITH_PERMISSION = 1024;
    private static final long MAX_AGE_MILLIS = 259200000;
    private static final long MAX_HISTORICAL_SESSIONS = 1048576;
    private static final long MAX_INSTALL_CONSTRAINTS_TIMEOUT_MILLIS = 604800000;
    private static final long MAX_SESSION_AGE_ON_LOW_STORAGE_MILLIS = 28800000;
    private static final long MAX_TIME_SINCE_UPDATE_MILLIS = 1814400000;
    private static final String TAG_SESSIONS = "sessions";
    private final ApexManager mApexManager;
    private AppOpsManager mAppOps;
    private final Callbacks mCallbacks;
    private final Context mContext;
    private final GentleUpdateHelper mGentleUpdateHelper;
    private final Handler mInstallHandler;
    private final HandlerThread mInstallThread;
    private final PackageManagerService mPm;
    private final PackageSessionVerifier mSessionVerifier;
    private final File mSessionsDir;
    private final AtomicFile mSessionsFile;
    private final StagingManager mStagingManager;
    private static final String TAG = "PackageInstaller";
    private static final boolean LOGD = Log.isLoggable(TAG, 3);
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    public static final Set<String> INSTALLER_CHANGEABLE_APP_OP_PERMISSIONS = Set.of("android.permission.USE_FULL_SCREEN_INTENT");
    private static final FilenameFilter sStageFilter = new FilenameFilter() { // from class: com.android.server.pm.PackageInstallerService.1
        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            return PackageInstallerService.isStageName(str);
        }
    };
    private volatile boolean mOkToSendBroadcasts = false;
    private volatile boolean mBypassNextStagedInstallerCheck = false;
    private volatile boolean mBypassNextAllowedApexUpdateCheck = false;
    private volatile int mDisableVerificationForUid = -1;
    private final InternalCallback mInternalCallback = new InternalCallback();
    private final Random mRandom = new SecureRandom();

    @GuardedBy({"mSessions"})
    private final SparseBooleanArray mAllocatedSessions = new SparseBooleanArray();

    @GuardedBy({"mSessions"})
    private final SparseArray<PackageInstallerSession> mSessions = new SparseArray<>();

    @GuardedBy({"mSessions"})
    private final List<String> mHistoricalSessions = new ArrayList();

    @GuardedBy({"mSessions"})
    private final SparseIntArray mHistoricalSessionsByInstaller = new SparseIntArray();

    @GuardedBy({"mSessions"})
    private final SparseBooleanArray mLegacySessions = new SparseBooleanArray();
    private final SilentUpdatePolicy mSilentUpdatePolicy = new SilentUpdatePolicy();
    private final RequestThrottle mSettingsWriteRequest = new RequestThrottle(IoThread.getHandler(), new Supplier() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            Boolean lambda$new$0;
            lambda$new$0 = PackageInstallerService.this.lambda$new$0();
            return lambda$new$0;
        }
    });
    public final IPackageInstallerServiceExt mServiceExt = (IPackageInstallerServiceExt) ExtLoader.type(IPackageInstallerServiceExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$registerCallback$5(int i, int i2) {
        return i == i2;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class Lifecycle extends SystemService {
        private final PackageInstallerService mPackageInstallerService;

        public void onStart() {
        }

        Lifecycle(Context context, PackageInstallerService packageInstallerService) {
            super(context);
            this.mPackageInstallerService = packageInstallerService;
        }

        public void onBootPhase(int i) {
            if (i == 550) {
                this.mPackageInstallerService.onBroadcastReady();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$new$0() {
        Boolean valueOf;
        synchronized (this.mSessions) {
            valueOf = Boolean.valueOf(writeSessionsLocked());
        }
        return valueOf;
    }

    public PackageInstallerService(Context context, PackageManagerService packageManagerService, Supplier<PackageParser2> supplier) {
        this.mContext = context;
        this.mPm = packageManagerService;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mInstallThread = handlerThread;
        handlerThread.start();
        this.mInstallHandler = new Handler(handlerThread.getLooper());
        this.mCallbacks = new Callbacks(handlerThread.getLooper());
        this.mSessionsFile = new AtomicFile(new File(Environment.getDataSystemDirectory(), "install_sessions.xml"), "package-session");
        File file = new File(Environment.getDataSystemDirectory(), "install_sessions");
        this.mSessionsDir = file;
        file.mkdirs();
        ApexManager apexManager = ApexManager.getInstance();
        this.mApexManager = apexManager;
        this.mStagingManager = new StagingManager(context);
        this.mSessionVerifier = new PackageSessionVerifier(context, packageManagerService, apexManager, supplier, handlerThread.getLooper());
        this.mGentleUpdateHelper = new GentleUpdateHelper(context, handlerThread.getLooper(), new AppStateHelper(context));
        ((SystemServiceManager) LocalServices.getService(SystemServiceManager.class)).startService(new Lifecycle(context, this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StagingManager getStagingManager() {
        return this.mStagingManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean okToSendBroadcasts() {
        return this.mOkToSendBroadcasts;
    }

    public void systemReady() {
        this.mAppOps = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
        this.mStagingManager.systemReady();
        this.mGentleUpdateHelper.systemReady();
        synchronized (this.mSessions) {
            readSessionsLocked();
            expireSessionsLocked();
            reconcileStagesLocked(StorageManager.UUID_PRIVATE_INTERNAL);
            ArraySet newArraySet = newArraySet(this.mSessionsDir.listFiles());
            for (int i = 0; i < this.mSessions.size(); i++) {
                newArraySet.remove(buildAppIconFile(this.mSessions.valueAt(i).sessionId));
            }
            Iterator it = newArraySet.iterator();
            while (it.hasNext()) {
                File file = (File) it.next();
                Slog.w(TAG, "Deleting orphan icon " + file);
                file.delete();
            }
            this.mSettingsWriteRequest.runNow();
        }
        this.mContext.registerReceiver(new AnonymousClass2(), new IntentFilter("android.intent.action.BOOT_COMPLETED"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.pm.PackageInstallerService$2, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass2 extends BroadcastReceiver {
        AnonymousClass2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            PackageInstallerService.this.mServiceExt.triggerPostBootApexSessionEvent();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BackgroundThread.getExecutor().execute(new Runnable() { // from class: com.android.server.pm.PackageInstallerService$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PackageInstallerService.AnonymousClass2.this.lambda$onReceive$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBroadcastReady() {
        this.mOkToSendBroadcasts = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreAndApplyStagedSessionIfNeeded() {
        char c;
        ArrayList arrayList = new ArrayList();
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                PackageInstallerSession valueAt = this.mSessions.valueAt(i);
                if (valueAt.isStaged()) {
                    PackageInstallerSession.StagedSession stagedSession = valueAt.mStagedSession;
                    if (!stagedSession.isInTerminalState() && stagedSession.hasParentSessionId() && getSession(stagedSession.getParentSessionId()) == null) {
                        stagedSession.setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, "An orphan staged session " + stagedSession.sessionId() + " is found, parent " + stagedSession.getParentSessionId() + " is missing");
                    } else if (!stagedSession.hasParentSessionId() && stagedSession.isCommitted() && !stagedSession.isInTerminalState()) {
                        PackageInstaller.SessionParams sessionParams = stagedSession.sessionParams();
                        if (sessionParams != null && this.mServiceExt.isSotaAppSession(stagedSession)) {
                            String sotaAppState = this.mServiceExt.getSotaAppState();
                            switch (sotaAppState.hashCode()) {
                                case -1422950650:
                                    if (sotaAppState.equals(DomainVerificationPersistence.TAG_ACTIVE)) {
                                        c = 3;
                                        break;
                                    }
                                    break;
                                case 3089282:
                                    if (sotaAppState.equals("done")) {
                                        c = 2;
                                        break;
                                    }
                                    break;
                                case 3387192:
                                    if (sotaAppState.equals("none")) {
                                        c = 0;
                                        break;
                                    }
                                    break;
                                case 109757538:
                                    if (sotaAppState.equals("start")) {
                                        c = 1;
                                        break;
                                    }
                                    break;
                            }
                            c = 65535;
                            if (c == 0 || c == 1) {
                                String str = "abort sota app " + sessionParams.appPackageName + " when state is " + sotaAppState;
                                stagedSession.setSessionFailed(UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, str);
                                Slog.d(TAG, str);
                            } else if (c == 2) {
                                Slog.d(TAG, "jump sota app " + sessionParams.appPackageName + " when state is " + sotaAppState);
                            } else if (c == 3) {
                                Slog.d(TAG, "add sota app " + sessionParams.appPackageName + " when state is " + sotaAppState);
                                arrayList.add(stagedSession);
                            }
                        } else {
                            arrayList.add(stagedSession);
                        }
                    }
                }
            }
        }
        this.mStagingManager.restoreSessions(arrayList, this.mPm.isDeviceUpgrading());
        this.mServiceExt.afterRestoreSession(arrayList, this.mSessions.clone(), this.mPm);
    }

    @GuardedBy({"mSessions"})
    private void reconcileStagesLocked(String str) {
        ArraySet<File> stagingDirsOnVolume = getStagingDirsOnVolume(str);
        for (int i = 0; i < this.mSessions.size(); i++) {
            stagingDirsOnVolume.remove(this.mSessions.valueAt(i).stageDir);
        }
        removeStagingDirs(stagingDirsOnVolume);
    }

    private ArraySet<File> getStagingDirsOnVolume(String str) {
        ArraySet<File> newArraySet = newArraySet(getTmpSessionDir(str).listFiles(sStageFilter));
        newArraySet.addAll(newArraySet(Environment.getDataStagingDirectory(str).listFiles()));
        return newArraySet;
    }

    private void removeStagingDirs(ArraySet<File> arraySet) {
        RemovePackageHelper removePackageHelper = new RemovePackageHelper(this.mPm);
        Iterator<File> it = arraySet.iterator();
        while (it.hasNext()) {
            File next = it.next();
            Slog.w(TAG, "Deleting orphan stage " + next);
            removePackageHelper.removeCodePath(next);
        }
    }

    public void onPrivateVolumeMounted(String str) {
        synchronized (this.mSessions) {
            reconcileStagesLocked(str);
        }
    }

    public void freeStageDirs(String str) {
        ArraySet<File> stagingDirsOnVolume = getStagingDirsOnVolume(str);
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                PackageInstallerSession valueAt = this.mSessions.valueAt(i);
                if (stagingDirsOnVolume.contains(valueAt.stageDir)) {
                    if (currentTimeMillis - valueAt.createdMillis >= MAX_SESSION_AGE_ON_LOW_STORAGE_MILLIS) {
                        PackageInstallerSession packageInstallerSession = !valueAt.hasParentSessionId() ? valueAt : this.mSessions.get(valueAt.getParentSessionId());
                        if (packageInstallerSession == null) {
                            Slog.e(TAG, "freeStageDirs: found an orphaned session: " + valueAt.sessionId + " parent=" + valueAt.getParentSessionId());
                        } else if (!packageInstallerSession.isDestroyed()) {
                            packageInstallerSession.abandon();
                        }
                    } else {
                        stagingDirsOnVolume.remove(valueAt.stageDir);
                    }
                }
            }
        }
        removeStagingDirs(stagingDirsOnVolume);
    }

    @Deprecated
    public File allocateStageDirLegacy(String str, boolean z) throws IOException {
        File buildTmpSessionDir;
        synchronized (this.mSessions) {
            try {
                try {
                    int allocateSessionIdLocked = allocateSessionIdLocked();
                    this.mLegacySessions.put(allocateSessionIdLocked, true);
                    buildTmpSessionDir = buildTmpSessionDir(allocateSessionIdLocked, str);
                    prepareStageDir(buildTmpSessionDir);
                } catch (IllegalStateException e) {
                    throw new IOException(e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return buildTmpSessionDir;
    }

    @Deprecated
    public String allocateExternalStageCidLegacy() {
        String str;
        synchronized (this.mSessions) {
            int allocateSessionIdLocked = allocateSessionIdLocked();
            this.mLegacySessions.put(allocateSessionIdLocked, true);
            str = "smdl" + allocateSessionIdLocked + ".tmp";
        }
        return str;
    }

    @GuardedBy({"mSessions"})
    private void readSessionsLocked() {
        if (LOGD) {
            Slog.v(TAG, "readSessionsLocked()");
        }
        this.mSessions.clear();
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    fileInputStream = this.mSessionsFile.openRead();
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                    while (true) {
                        int next = resolvePullParser.next();
                        if (next == 1) {
                            break;
                        }
                        if (next == 2 && "session".equals(resolvePullParser.getName())) {
                            try {
                                PackageInstallerSession readFromXml = PackageInstallerSession.readFromXml(resolvePullParser, this.mInternalCallback, this.mContext, this.mPm, this.mInstallThread.getLooper(), this.mStagingManager, this.mSessionsDir, this, this.mSilentUpdatePolicy);
                                this.mSessions.put(readFromXml.sessionId, readFromXml);
                                this.mAllocatedSessions.put(readFromXml.sessionId, true);
                            } catch (Exception e) {
                                Slog.e(TAG, "Could not read session", e);
                            }
                        }
                    }
                } catch (IOException | ArrayIndexOutOfBoundsException | XmlPullParserException e2) {
                    Slog.wtf(TAG, "Failed reading install sessions", e2);
                }
            } catch (FileNotFoundException unused) {
            }
            IoUtils.closeQuietly(fileInputStream);
            for (int i = 0; i < this.mSessions.size(); i++) {
                this.mSessions.valueAt(i).onAfterSessionRead(this.mSessions);
            }
        } catch (Throwable th) {
            IoUtils.closeQuietly(fileInputStream);
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003f, code lost:
    
        if (r7 >= com.android.server.pm.PackageInstallerService.MAX_TIME_SINCE_UPDATE_MILLIS) goto L17;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x007b A[SYNTHETIC] */
    @GuardedBy({"mSessions"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void expireSessionsLocked() {
        SparseArray<PackageInstallerSession> clone = this.mSessions.clone();
        int size = clone.size();
        for (int i = 0; i < size; i++) {
            PackageInstallerSession valueAt = clone.valueAt(i);
            if (!valueAt.hasParentSessionId()) {
                long currentTimeMillis = System.currentTimeMillis() - valueAt.createdMillis;
                long currentTimeMillis2 = System.currentTimeMillis() - valueAt.getUpdatedMillis();
                boolean z = true;
                if (valueAt.isStaged()) {
                    if (valueAt.isStagedAndInTerminalState()) {
                    }
                    if (z) {
                        Slog.w(TAG, "Remove old session: " + valueAt.sessionId);
                        removeActiveSession(valueAt);
                    }
                } else {
                    if (currentTimeMillis >= MAX_AGE_MILLIS) {
                        Slog.w(TAG, "Abandoning old session created at " + valueAt.createdMillis);
                        z = false;
                    }
                    if (z) {
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mSessions"})
    public void removeActiveSession(PackageInstallerSession packageInstallerSession) {
        this.mSessions.remove(packageInstallerSession.sessionId);
        addHistoricalSessionLocked(packageInstallerSession);
        for (PackageInstallerSession packageInstallerSession2 : packageInstallerSession.getChildSessions()) {
            this.mSessions.remove(packageInstallerSession2.sessionId);
            addHistoricalSessionLocked(packageInstallerSession2);
        }
    }

    @GuardedBy({"mSessions"})
    private void addHistoricalSessionLocked(PackageInstallerSession packageInstallerSession) {
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        packageInstallerSession.dump(new IndentingPrintWriter(charArrayWriter, "    "));
        if (this.mHistoricalSessions.size() > 5000) {
            Slog.d(TAG, "Historical sessions size reaches threshold, clear the oldest");
            this.mHistoricalSessions.subList(0, 500).clear();
        }
        this.mHistoricalSessions.add(charArrayWriter.toString());
        int installerUid = packageInstallerSession.getInstallerUid();
        SparseIntArray sparseIntArray = this.mHistoricalSessionsByInstaller;
        sparseIntArray.put(installerUid, sparseIntArray.get(installerUid) + 1);
    }

    @GuardedBy({"mSessions"})
    private boolean writeSessionsLocked() {
        FileOutputStream startWrite;
        if (LOGD) {
            Slog.v(TAG, "writeSessionsLocked()");
        }
        FileOutputStream fileOutputStream = null;
        try {
            startWrite = this.mSessionsFile.startWrite();
        } catch (IOException unused) {
        }
        try {
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.startTag((String) null, TAG_SESSIONS);
            int size = this.mSessions.size();
            for (int i = 0; i < size; i++) {
                this.mSessions.valueAt(i).write(resolveSerializer, this.mSessionsDir);
            }
            resolveSerializer.endTag((String) null, TAG_SESSIONS);
            resolveSerializer.endDocument();
            this.mSessionsFile.finishWrite(startWrite);
            return true;
        } catch (IOException unused2) {
            fileOutputStream = startWrite;
            if (fileOutputStream != null) {
                this.mSessionsFile.failWrite(fileOutputStream);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File buildAppIconFile(int i) {
        return new File(this.mSessionsDir, "app_icon." + i + ".png");
    }

    public int createSession(PackageInstaller.SessionParams sessionParams, String str, String str2, int i) {
        try {
            return createSessionInternal(sessionParams, str, str2, i);
        } catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:215:0x03b3  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x03bd  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0441 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x03c0  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x03ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int createSessionInternal(PackageInstaller.SessionParams sessionParams, String str, String str2, int i) throws IOException {
        int allocateSessionIdLocked;
        File file;
        String str3;
        DevicePolicyManagerInternal devicePolicyManagerInternal;
        int launcherLargeIconSize;
        int launcherLargeIconSize2;
        String[] packagesForUid;
        String str4 = str;
        int callingUid = Binder.getCallingUid();
        Computer snapshotComputer = this.mPm.snapshotComputer();
        snapshotComputer.enforceCrossUserPermission(callingUid, i, true, true, "createSession");
        if (this.mPm.isUserRestricted(i, "no_install_apps")) {
            throw new SecurityException("User restriction prevents installing");
        }
        if (sessionParams.dataLoaderParams != null && this.mContext.checkCallingOrSelfPermission("com.android.permission.USE_INSTALLER_V2") != 0) {
            throw new SecurityException("You need the com.android.permission.USE_INSTALLER_V2 permission to use a data loader");
        }
        if (sessionParams.installReason == 5 && this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_ROLLBACKS") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS") != 0) {
            throw new SecurityException("INSTALL_REASON_ROLLBACK requires the MANAGE_ROLLBACKS permission or the TEST_MANAGE_ROLLBACKS permission");
        }
        String str5 = sessionParams.appPackageName;
        if (str5 != null && !isValidPackageName(str5)) {
            if (sessionParams.appPackageName.contains("sota_app-")) {
                Slog.e(TAG, "sota_app ignore ValidPackageName");
            } else {
                sessionParams.appPackageName = null;
            }
        }
        sessionParams.appLabel = (String) TextUtils.trimToSize(sessionParams.appLabel, 1000);
        String str6 = sessionParams.installerPackageName;
        if (str6 != null && !isValidPackageName(str6)) {
            sessionParams.installerPackageName = null;
        }
        String str7 = sessionParams.installerPackageName;
        if (str7 == null) {
            str7 = str4;
        }
        if (PackageManagerServiceUtils.isRootOrShell(callingUid) || PackageInstallerSession.isSystemDataLoaderInstallation(sessionParams) || PackageManagerServiceUtils.isAdoptedShell(UserHandle.getAppId(callingUid), this.mContext)) {
            sessionParams.installFlags |= 32;
            str4 = VibratorManagerService.VibratorManagerShellCommand.SHELL_PACKAGE_NAME;
        } else {
            if (callingUid != 1000) {
                this.mAppOps.checkPackage(callingUid, str4);
            }
            if (!TextUtils.equals(str7, str4) && this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") != 0) {
                this.mAppOps.checkPackage(callingUid, str7);
            }
            int i2 = sessionParams.installFlags & (-33);
            sessionParams.installFlags = i2;
            if (!this.mServiceExt.skipRemoveInstallAllUsersFlag(i2)) {
                sessionParams.installFlags &= -65;
            }
            int i3 = sessionParams.installFlags | 2;
            sessionParams.installFlags = i3;
            if ((i3 & 65536) != 0 && !this.mPm.isCallerVerifier(snapshotComputer, callingUid)) {
                sessionParams.installFlags &= -65537;
            }
            if (this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_TEST_ONLY_PACKAGE") != 0 && !this.mServiceExt.skipRemoveInstallAllUsersFlag(sessionParams.installFlags)) {
                sessionParams.installFlags &= -5;
            }
        }
        String str8 = str4;
        int i4 = sessionParams.originatingUid;
        String str9 = (i4 == -1 || i4 == callingUid || (packagesForUid = snapshotComputer.getPackagesForUid(i4)) == null || packagesForUid.length <= 0) ? null : packagesForUid[0];
        if (Build.IS_DEBUGGABLE || PackageManagerServiceUtils.isSystemOrRoot(callingUid)) {
            sessionParams.installFlags |= 1048576;
        } else {
            sessionParams.installFlags &= -1048577;
        }
        if (this.mDisableVerificationForUid != -1) {
            if (callingUid == this.mDisableVerificationForUid) {
                sessionParams.installFlags |= 524288;
            } else {
                sessionParams.installFlags &= -524289;
            }
            this.mDisableVerificationForUid = -1;
        } else {
            int i5 = sessionParams.installFlags;
            if ((i5 & 36) != 36) {
                sessionParams.installFlags = i5 & (-524289);
            }
        }
        boolean z = (sessionParams.installFlags & 131072) != 0;
        if (z) {
            if (this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGE_UPDATES") == -1 && this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") == -1) {
                throw new SecurityException("Not allowed to perform APEX updates");
            }
        } else if (sessionParams.isStaged) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INSTALL_PACKAGES", TAG);
        }
        if (z) {
            if (!this.mApexManager.isApexSupported()) {
                throw new IllegalArgumentException("This device doesn't support the installation of APEX files");
            }
            if (sessionParams.isMultiPackage) {
                throw new IllegalArgumentException("A multi-session can't be set as APEX.");
            }
            if (PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) || this.mBypassNextAllowedApexUpdateCheck) {
                sessionParams.installFlags |= 8388608;
            } else {
                sessionParams.installFlags &= -8388609;
            }
        }
        if ((sessionParams.installFlags & DumpState.DUMP_SERVICE_PERMISSIONS) != 0 && !PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) && !Build.IS_DEBUGGABLE) {
            sessionParams.installFlags &= -16777217;
        }
        if ((sessionParams.installFlags & 2048) != 0 && !PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) && (snapshotComputer.getFlagsForUid(callingUid) & 1) == 0) {
            throw new SecurityException("Only system apps could use the PackageManager.INSTALL_INSTANT_APP flag.");
        }
        if (sessionParams.isStaged && !PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) && !this.mBypassNextStagedInstallerCheck && !isStagedInstallerAllowed(str7)) {
            throw new SecurityException("Installer not allowed to commit staged install");
        }
        if (z && !PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid) && !this.mBypassNextStagedInstallerCheck && !isStagedInstallerAllowed(str7)) {
            throw new SecurityException("Installer not allowed to commit non-staged APEX install");
        }
        this.mBypassNextStagedInstallerCheck = false;
        this.mBypassNextAllowedApexUpdateCheck = false;
        if (!sessionParams.isMultiPackage) {
            boolean z2 = this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS") == 0;
            if ((sessionParams.installFlags & 256) != 0 && !z2) {
                throw new SecurityException("You need the android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS permission to use the PackageManager.INSTALL_GRANT_ALL_REQUESTED_PERMISSIONS flag");
            }
            ArrayMap permissionStates = sessionParams.getPermissionStates();
            if (!permissionStates.isEmpty() && !z2) {
                for (int i6 = 0; i6 < permissionStates.size(); i6++) {
                    if (!INSTALLER_CHANGEABLE_APP_OP_PERMISSIONS.contains((String) permissionStates.keyAt(i6))) {
                        throw new SecurityException("You need the android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS permission to grant runtime permissions for a session");
                    }
                }
            }
            if (sessionParams.appIcon != null && (sessionParams.appIcon.getWidth() > (launcherLargeIconSize2 = (launcherLargeIconSize = ((ActivityManager) this.mContext.getSystemService("activity")).getLauncherLargeIconSize()) * 2) || sessionParams.appIcon.getHeight() > launcherLargeIconSize2)) {
                sessionParams.appIcon = Bitmap.createScaledBitmap(sessionParams.appIcon, launcherLargeIconSize, launcherLargeIconSize, true);
            }
            int i7 = sessionParams.mode;
            if (i7 != 1 && i7 != 2) {
                throw new IllegalArgumentException("Invalid install mode: " + sessionParams.mode);
            }
            int i8 = sessionParams.installFlags;
            if ((i8 & 16) != 0) {
                if (!InstallLocationUtils.fitsOnInternal(this.mContext, sessionParams)) {
                    throw new IOException("No suitable internal storage available");
                }
            } else if ((i8 & 512) != 0) {
                sessionParams.installFlags = i8 | 16;
            } else {
                sessionParams.installFlags = i8 | 16;
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    sessionParams.volumeUuid = InstallLocationUtils.resolveInstallVolume(this.mContext, sessionParams);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }
        synchronized (this.mSessions) {
            int sessionCount = getSessionCount(this.mSessions, callingUid);
            if (this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") == 0) {
                if (sessionCount >= MAX_ACTIVE_SESSIONS_WITH_PERMISSION) {
                    throw new IllegalStateException("Too many active sessions for UID " + callingUid);
                }
            } else if (sessionCount >= MAX_ACTIVE_SESSIONS_NO_PERMISSION) {
                throw new IllegalStateException("Too many active sessions for UID " + callingUid);
            }
            if (this.mHistoricalSessionsByInstaller.get(callingUid) >= MAX_HISTORICAL_SESSIONS) {
                throw new IllegalStateException("Too many historical sessions for UID " + callingUid);
            }
            allocateSessionIdLocked = allocateSessionIdLocked();
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (sessionParams.isMultiPackage) {
            file = null;
        } else if ((sessionParams.installFlags & 16) != 0) {
            file = buildSessionDir(allocateSessionIdLocked, sessionParams);
        } else {
            str3 = buildExternalStageCid(allocateSessionIdLocked);
            file = null;
            if (sessionParams.forceQueryableOverride && !PackageManagerServiceUtils.isRootOrShell(callingUid)) {
                sessionParams.forceQueryableOverride = false;
            }
            int packageUid = str7 == null ? snapshotComputer.getPackageUid(str7, 0L, i) : -1;
            String str10 = packageUid != -1 ? null : str7;
            devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
            if (devicePolicyManagerInternal != null && devicePolicyManagerInternal.isUserOrganizationManaged(i)) {
                sessionParams.installFlags |= 67108864;
            }
            if (!z || this.mContext.checkCallingOrSelfPermission("android.permission.ENFORCE_UPDATE_OWNERSHIP") == -1) {
                sessionParams.installFlags &= -33554433;
            }
            PackageInstallerSession packageInstallerSession = new PackageInstallerSession(this.mInternalCallback, this.mContext, this.mPm, this, this.mSilentUpdatePolicy, this.mInstallThread.getLooper(), this.mStagingManager, allocateSessionIdLocked, i, callingUid, InstallSource.create(str8, str9, str10, packageUid, str10, str2, sessionParams.packageSource), sessionParams, currentTimeMillis, 0L, file, str3, null, null, false, false, false, false, null, -1, false, false, false, 0, "");
            synchronized (this.mSessions) {
                this.mSessions.put(allocateSessionIdLocked, packageInstallerSession);
            }
            this.mPm.addInstallerPackageName(packageInstallerSession.getInstallSource());
            this.mCallbacks.notifySessionCreated(packageInstallerSession.sessionId, packageInstallerSession.userId);
            this.mSettingsWriteRequest.schedule();
            if (LOGD) {
                Slog.d(TAG, "Created session id=" + allocateSessionIdLocked + " staged=" + sessionParams.isStaged);
            }
            return allocateSessionIdLocked;
        }
        str3 = null;
        if (sessionParams.forceQueryableOverride) {
            sessionParams.forceQueryableOverride = false;
        }
        if (str7 == null) {
        }
        if (packageUid != -1) {
        }
        devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        if (devicePolicyManagerInternal != null) {
            sessionParams.installFlags |= 67108864;
        }
        if (!z) {
        }
        sessionParams.installFlags &= -33554433;
        PackageInstallerSession packageInstallerSession2 = new PackageInstallerSession(this.mInternalCallback, this.mContext, this.mPm, this, this.mSilentUpdatePolicy, this.mInstallThread.getLooper(), this.mStagingManager, allocateSessionIdLocked, i, callingUid, InstallSource.create(str8, str9, str10, packageUid, str10, str2, sessionParams.packageSource), sessionParams, currentTimeMillis, 0L, file, str3, null, null, false, false, false, false, null, -1, false, false, false, 0, "");
        synchronized (this.mSessions) {
        }
    }

    private boolean isStagedInstallerAllowed(String str) {
        return SystemConfig.getInstance().getWhitelistedStagedInstallers().contains(str);
    }

    public void updateSessionAppIcon(int i, Bitmap bitmap) {
        int launcherLargeIconSize;
        int launcherLargeIconSize2;
        synchronized (this.mSessions) {
            PackageInstallerSession packageInstallerSession = this.mSessions.get(i);
            if (packageInstallerSession == null || !isCallingUidOwner(packageInstallerSession)) {
                throw new SecurityException("Caller has no access to session " + i);
            }
            if (bitmap != null && (bitmap.getWidth() > (launcherLargeIconSize2 = (launcherLargeIconSize = ((ActivityManager) this.mContext.getSystemService("activity")).getLauncherLargeIconSize()) * 2) || bitmap.getHeight() > launcherLargeIconSize2)) {
                bitmap = Bitmap.createScaledBitmap(bitmap, launcherLargeIconSize, launcherLargeIconSize, true);
            }
            PackageInstaller.SessionParams sessionParams = packageInstallerSession.params;
            sessionParams.appIcon = bitmap;
            sessionParams.appIconLastModified = -1L;
            this.mInternalCallback.onSessionBadgingChanged(packageInstallerSession);
        }
    }

    public void updateSessionAppLabel(int i, String str) {
        synchronized (this.mSessions) {
            PackageInstallerSession packageInstallerSession = this.mSessions.get(i);
            if (packageInstallerSession == null || !isCallingUidOwner(packageInstallerSession)) {
                throw new SecurityException("Caller has no access to session " + i);
            }
            if (!str.equals(packageInstallerSession.params.appLabel)) {
                packageInstallerSession.params.appLabel = str;
                this.mInternalCallback.onSessionBadgingChanged(packageInstallerSession);
            }
        }
    }

    public void abandonSession(int i) {
        synchronized (this.mSessions) {
            PackageInstallerSession packageInstallerSession = this.mSessions.get(i);
            if (this.mServiceExt.canForceAbandonMainlineSession(packageInstallerSession)) {
                packageInstallerSession.abandon();
                return;
            }
            if (packageInstallerSession == null || !isCallingUidOwner(packageInstallerSession)) {
                throw new SecurityException("Caller has no access to session " + i);
            }
            packageInstallerSession.abandon();
        }
    }

    public IPackageInstallerSession openSession(int i) {
        try {
            this.mPm.mPackageManagerServiceSocExt.setInstallationBoost(true);
            return openSessionInternal(i);
        } catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private boolean checkOpenSessionAccess(PackageInstallerSession packageInstallerSession) {
        if (packageInstallerSession == null) {
            return false;
        }
        if (isCallingUidOwner(packageInstallerSession)) {
            return true;
        }
        return packageInstallerSession.isSealed() && this.mContext.checkCallingOrSelfPermission("android.permission.PACKAGE_VERIFICATION_AGENT") == 0;
    }

    private IPackageInstallerSession openSessionInternal(int i) throws IOException {
        PackageInstallerSession packageInstallerSession;
        synchronized (this.mSessions) {
            packageInstallerSession = this.mSessions.get(i);
            if (!checkOpenSessionAccess(packageInstallerSession)) {
                throw new SecurityException("Caller has no access to session " + i);
            }
            packageInstallerSession.open();
        }
        return packageInstallerSession;
    }

    @GuardedBy({"mSessions"})
    private int allocateSessionIdLocked() {
        int i = 0;
        while (true) {
            int nextInt = this.mRandom.nextInt(2147483646) + 1;
            if (!this.mAllocatedSessions.get(nextInt, false)) {
                this.mAllocatedSessions.put(nextInt, true);
                return nextInt;
            }
            int i2 = i + 1;
            if (i >= 32) {
                throw new IllegalStateException("Failed to allocate session ID");
            }
            i = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isStageName(String str) {
        return (str.startsWith("vmdl") && str.endsWith(".tmp")) || (str.startsWith("smdl") && str.endsWith(".tmp")) || str.startsWith("smdl2tmp");
    }

    static int tryParseSessionId(String str) throws IllegalArgumentException {
        if (!str.startsWith("vmdl") || !str.endsWith(".tmp")) {
            throw new IllegalArgumentException("Not a temporary session directory");
        }
        return Integer.parseInt(str.substring(4, str.length() - 4));
    }

    private static boolean isValidPackageName(String str) {
        return str.length() <= 255 && FrameworkParsingPackageUtils.validateName(str, false, true) == null;
    }

    private File getTmpSessionDir(String str) {
        return Environment.getDataAppDirectory(str);
    }

    private File buildTmpSessionDir(int i, String str) {
        return new File(getTmpSessionDir(str), "vmdl" + i + ".tmp");
    }

    private File buildSessionDir(int i, PackageInstaller.SessionParams sessionParams) {
        if (sessionParams.isStaged || (sessionParams.installFlags & 131072) != 0) {
            return new File(Environment.getDataStagingDirectory(sessionParams.volumeUuid), "session_" + i);
        }
        File buildTmpSessionDir = buildTmpSessionDir(i, sessionParams.volumeUuid);
        if (!DEBUG || Objects.equals(Integer.valueOf(tryParseSessionId(buildTmpSessionDir.getName())), Integer.valueOf(i))) {
            return buildTmpSessionDir;
        }
        throw new RuntimeException("session folder format is off: " + buildTmpSessionDir.getName() + " (" + i + ")");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void prepareStageDir(File file) throws IOException {
        if (file.exists()) {
            throw new IOException("Session dir already exists: " + file);
        }
        try {
            Os.mkdir(file.getAbsolutePath(), 509);
            Os.chmod(file.getAbsolutePath(), 509);
            if (SELinux.restorecon(file)) {
                return;
            }
            String canonicalPath = file.getCanonicalPath();
            String fileSelabelLookup = SELinux.fileSelabelLookup(canonicalPath);
            boolean fileContext = SELinux.setFileContext(canonicalPath, fileSelabelLookup);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to SELinux.restorecon session dir, path: [");
            sb.append(canonicalPath);
            sb.append("], ctx: [");
            sb.append(fileSelabelLookup);
            sb.append("]. Retrying via SELinux.fileSelabelLookup/SELinux.setFileContext: ");
            sb.append(fileContext ? "SUCCESS" : "FAILURE");
            Slog.e(TAG, sb.toString());
            if (fileContext) {
                return;
            }
            throw new IOException("Failed to restorecon session dir: " + file);
        } catch (ErrnoException e) {
            throw new IOException("Failed to prepare session dir: " + file, e);
        }
    }

    private String buildExternalStageCid(int i) {
        return "smdl" + i + ".tmp";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: shouldFilterSession, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public boolean lambda$getStagedSessions$1(Computer computer, int i, PackageInstaller.SessionInfo sessionInfo) {
        return (sessionInfo == null || i == sessionInfo.getInstallerUid() || computer.canQueryPackage(i, sessionInfo.getAppPackageName())) ? false : true;
    }

    public PackageInstaller.SessionInfo getSessionInfo(int i) {
        PackageInstaller.SessionInfo generateInfoForCaller;
        int callingUid = Binder.getCallingUid();
        synchronized (this.mSessions) {
            PackageInstallerSession packageInstallerSession = this.mSessions.get(i);
            generateInfoForCaller = (packageInstallerSession == null || (packageInstallerSession.isStaged() && packageInstallerSession.isDestroyed())) ? null : packageInstallerSession.generateInfoForCaller(true, callingUid);
        }
        if (lambda$getStagedSessions$1(this.mPm.snapshotComputer(), callingUid, generateInfoForCaller)) {
            return null;
        }
        return generateInfoForCaller;
    }

    public ParceledListSlice<PackageInstaller.SessionInfo> getStagedSessions() {
        final int callingUid = Binder.getCallingUid();
        ArrayList arrayList = new ArrayList();
        synchronized (this.mSessions) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                PackageInstallerSession valueAt = this.mSessions.valueAt(i);
                if (valueAt.isStaged() && !valueAt.isDestroyed()) {
                    arrayList.add(valueAt.generateInfoForCaller(false, callingUid));
                }
            }
        }
        final Computer snapshotComputer = this.mPm.snapshotComputer();
        arrayList.removeIf(new Predicate() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getStagedSessions$1;
                lambda$getStagedSessions$1 = PackageInstallerService.this.lambda$getStagedSessions$1(snapshotComputer, callingUid, (PackageInstaller.SessionInfo) obj);
                return lambda$getStagedSessions$1;
            }
        });
        return new ParceledListSlice<>(arrayList);
    }

    public ParceledListSlice<PackageInstaller.SessionInfo> getAllSessions(int i) {
        final int callingUid = Binder.getCallingUid();
        final Computer snapshotComputer = this.mPm.snapshotComputer();
        snapshotComputer.enforceCrossUserPermission(callingUid, this.mServiceExt.changeUserIdIfNeed(i, callingUid, snapshotComputer), true, false, "getAllSessions");
        ArrayList arrayList = new ArrayList();
        synchronized (this.mSessions) {
            for (int i2 = 0; i2 < this.mSessions.size(); i2++) {
                PackageInstallerSession valueAt = this.mSessions.valueAt(i2);
                if (valueAt.userId == i && !valueAt.hasParentSessionId() && (!valueAt.isStaged() || !valueAt.isDestroyed())) {
                    arrayList.add(valueAt.generateInfoForCaller(false, callingUid));
                }
            }
        }
        arrayList.removeIf(new Predicate() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getAllSessions$2;
                lambda$getAllSessions$2 = PackageInstallerService.this.lambda$getAllSessions$2(snapshotComputer, callingUid, (PackageInstaller.SessionInfo) obj);
                return lambda$getAllSessions$2;
            }
        });
        return new ParceledListSlice<>(arrayList);
    }

    public ParceledListSlice<PackageInstaller.SessionInfo> getMySessions(String str, int i) {
        Computer snapshotComputer = this.mPm.snapshotComputer();
        int callingUid = Binder.getCallingUid();
        snapshotComputer.enforceCrossUserPermission(callingUid, i, true, false, "getMySessions");
        this.mAppOps.checkPackage(callingUid, str);
        ArrayList arrayList = new ArrayList();
        synchronized (this.mSessions) {
            for (int i2 = 0; i2 < this.mSessions.size(); i2++) {
                PackageInstallerSession valueAt = this.mSessions.valueAt(i2);
                PackageInstaller.SessionInfo generateInfoForCaller = valueAt.generateInfoForCaller(false, 1000);
                if (Objects.equals(generateInfoForCaller.getInstallerPackageName(), str) && valueAt.userId == i && !valueAt.hasParentSessionId() && isCallingUidOwner(valueAt)) {
                    arrayList.add(generateInfoForCaller);
                }
            }
        }
        return new ParceledListSlice<>(arrayList);
    }

    public void uninstall(VersionedPackage versionedPackage, String str, int i, IntentSender intentSender, int i2) {
        Computer snapshotComputer = this.mPm.snapshotComputer();
        int callingUid = Binder.getCallingUid();
        snapshotComputer.enforceCrossUserPermission(callingUid, i2, true, true, "uninstall");
        if (!PackageManagerServiceUtils.isRootOrShell(callingUid)) {
            this.mAppOps.checkPackage(callingUid, str);
        }
        DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        boolean z = devicePolicyManagerInternal != null && devicePolicyManagerInternal.canSilentlyInstallPackage(str, callingUid);
        PackageDeleteObserverAdapter packageDeleteObserverAdapter = new PackageDeleteObserverAdapter(this.mContext, intentSender, versionedPackage.getPackageName(), z, i2);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DELETE_PACKAGES") == 0) {
            this.mPm.deletePackageVersioned(versionedPackage, packageDeleteObserverAdapter.getBinder(), i2, i);
            return;
        }
        if (z) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mPm.deletePackageVersioned(versionedPackage, packageDeleteObserverAdapter.getBinder(), i2, i);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                DevicePolicyEventLogger.createEvent(HdmiCecKeycode.CEC_KEYCODE_F1_BLUE).setAdmin(str).write();
                return;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
        if (snapshotComputer.getApplicationInfo(str, 0L, i2).targetSdkVersion >= 28) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.REQUEST_DELETE_PACKAGES", null);
        }
        Intent intent = new Intent("android.intent.action.UNINSTALL_PACKAGE");
        intent.setData(Uri.fromParts("package", versionedPackage.getPackageName(), null));
        intent.putExtra("android.content.pm.extra.CALLBACK", (Parcelable) new PackageManager.UninstallCompleteCallback(packageDeleteObserverAdapter.getBinder().asBinder()));
        packageDeleteObserverAdapter.onUserActionRequired(intent);
    }

    public void uninstallExistingPackage(VersionedPackage versionedPackage, String str, IntentSender intentSender, int i) {
        int callingUid = Binder.getCallingUid();
        this.mContext.enforceCallingOrSelfPermission("android.permission.DELETE_PACKAGES", null);
        this.mPm.snapshotComputer().enforceCrossUserPermission(callingUid, i, true, true, "uninstall");
        if (!PackageManagerServiceUtils.isRootOrShell(callingUid)) {
            this.mAppOps.checkPackage(callingUid, str);
        }
        this.mPm.deleteExistingPackageAsUser(versionedPackage, new PackageDeleteObserverAdapter(this.mContext, intentSender, versionedPackage.getPackageName(), false, i).getBinder(), i);
    }

    public void installExistingPackage(String str, int i, int i2, IntentSender intentSender, int i3, List<String> list) {
        Pair<Integer, IntentSender> installExistingPackageAsUser = new InstallPackageHelper(this.mPm).installExistingPackageAsUser(str, i3, i, i2, list, intentSender);
        int intValue = ((Integer) installExistingPackageAsUser.first).intValue();
        IntentSender intentSender2 = (IntentSender) installExistingPackageAsUser.second;
        if (intentSender2 != null) {
            InstallPackageHelper.onInstallComplete(intValue, this.mContext, intentSender2);
        }
    }

    public void setPermissionsResult(int i, boolean z) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.INSTALL_PACKAGES", TAG);
        synchronized (this.mSessions) {
            PackageInstallerSession packageInstallerSession = this.mSessions.get(i);
            if (packageInstallerSession != null) {
                packageInstallerSession.setPermissionsResult(z);
            }
        }
    }

    private boolean isValidForInstallConstraints(PackageStateInternal packageStateInternal, String str) {
        return TextUtils.equals(packageStateInternal.getInstallSource().mInstallerPackageName, str) || TextUtils.equals(packageStateInternal.getInstallSource().mUpdateOwnerPackageName, str);
    }

    private CompletableFuture<PackageInstaller.InstallConstraintsResult> checkInstallConstraintsInternal(String str, List<String> list, PackageInstaller.InstallConstraints installConstraints, long j) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(installConstraints);
        Computer snapshotComputer = this.mPm.snapshotComputer();
        int callingUid = Binder.getCallingUid();
        if (!TextUtils.equals(snapshotComputer.getNameForUid(callingUid), str)) {
            throw new SecurityException("The installerPackageName set by the caller doesn't match the caller's own package name.");
        }
        if (!PackageManagerServiceUtils.isSystemOrRootOrShell(callingUid)) {
            for (String str2 : list) {
                PackageStateInternal packageStateInternal = snapshotComputer.getPackageStateInternal(str2);
                if (packageStateInternal == null || !isValidForInstallConstraints(packageStateInternal, str)) {
                    throw new SecurityException("Caller has no access to package " + str2);
                }
            }
        }
        return this.mGentleUpdateHelper.checkInstallConstraints(list, installConstraints, j);
    }

    public void checkInstallConstraints(String str, List<String> list, PackageInstaller.InstallConstraints installConstraints, final RemoteCallback remoteCallback) {
        Objects.requireNonNull(remoteCallback);
        checkInstallConstraintsInternal(str, list, installConstraints, 0L).thenAccept(new Consumer() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PackageInstallerService.lambda$checkInstallConstraints$3(remoteCallback, (PackageInstaller.InstallConstraintsResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkInstallConstraints$3(RemoteCallback remoteCallback, PackageInstaller.InstallConstraintsResult installConstraintsResult) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("result", installConstraintsResult);
        remoteCallback.sendResult(bundle);
    }

    public void waitForInstallConstraints(String str, final List<String> list, final PackageInstaller.InstallConstraints installConstraints, final IntentSender intentSender, long j) {
        Objects.requireNonNull(intentSender);
        if (j < 0 || j > 604800000) {
            throw new IllegalArgumentException("Invalid timeoutMillis=" + j);
        }
        checkInstallConstraintsInternal(str, list, installConstraints, j).thenAccept(new Consumer() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PackageInstallerService.this.lambda$waitForInstallConstraints$4(list, installConstraints, intentSender, (PackageInstaller.InstallConstraintsResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$waitForInstallConstraints$4(List list, PackageInstaller.InstallConstraints installConstraints, IntentSender intentSender, PackageInstaller.InstallConstraintsResult installConstraintsResult) {
        Intent intent = new Intent();
        intent.putExtra("android.intent.extra.PACKAGES", (String[]) list.toArray(new String[0]));
        intent.putExtra("android.content.pm.extra.INSTALL_CONSTRAINTS", installConstraints);
        intent.putExtra("android.content.pm.extra.INSTALL_CONSTRAINTS_RESULT", installConstraintsResult);
        try {
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
            intentSender.sendIntent(this.mContext, 0, intent, null, null, null, makeBasic.toBundle());
        } catch (IntentSender.SendIntentException unused) {
        }
    }

    public void registerCallback(IPackageInstallerCallback iPackageInstallerCallback, final int i) {
        this.mPm.snapshotComputer().enforceCrossUserPermission(Binder.getCallingUid(), i, true, false, "registerCallback");
        registerCallback(iPackageInstallerCallback, new IntPredicate() { // from class: com.android.server.pm.PackageInstallerService$$ExternalSyntheticLambda3
            @Override // java.util.function.IntPredicate
            public final boolean test(int i2) {
                boolean lambda$registerCallback$5;
                lambda$registerCallback$5 = PackageInstallerService.lambda$registerCallback$5(i, i2);
                return lambda$registerCallback$5;
            }
        });
    }

    public void registerCallback(IPackageInstallerCallback iPackageInstallerCallback, IntPredicate intPredicate) {
        this.mCallbacks.register(iPackageInstallerCallback, new BroadcastCookie(Binder.getCallingUid(), intPredicate));
    }

    public void unregisterCallback(IPackageInstallerCallback iPackageInstallerCallback) {
        this.mCallbacks.unregister(iPackageInstallerCallback);
    }

    @Override // com.android.server.pm.PackageSessionProvider
    public PackageInstallerSession getSession(int i) {
        PackageInstallerSession packageInstallerSession;
        synchronized (this.mSessions) {
            packageInstallerSession = this.mSessions.get(i);
        }
        return packageInstallerSession;
    }

    @Override // com.android.server.pm.PackageSessionProvider
    public PackageSessionVerifier getSessionVerifier() {
        return this.mSessionVerifier;
    }

    @Override // com.android.server.pm.PackageSessionProvider
    public GentleUpdateHelper getGentleUpdateHelper() {
        return this.mGentleUpdateHelper;
    }

    public void bypassNextStagedInstallerCheck(boolean z) {
        if (!PackageManagerServiceUtils.isSystemOrRootOrShell(Binder.getCallingUid())) {
            throw new SecurityException("Caller not allowed to bypass staged installer check");
        }
        this.mBypassNextStagedInstallerCheck = z;
    }

    public void bypassNextAllowedApexUpdateCheck(boolean z) {
        if (!PackageManagerServiceUtils.isSystemOrRootOrShell(Binder.getCallingUid())) {
            throw new SecurityException("Caller not allowed to bypass allowed apex update check");
        }
        this.mBypassNextAllowedApexUpdateCheck = z;
    }

    public void disableVerificationForUid(int i) {
        if (!PackageManagerServiceUtils.isSystemOrRootOrShell(Binder.getCallingUid())) {
            throw new SecurityException("Operation not allowed for caller");
        }
        this.mDisableVerificationForUid = i;
    }

    public void setAllowUnlimitedSilentUpdates(String str) {
        if (!PackageManagerServiceUtils.isSystemOrRootOrShell(Binder.getCallingUid())) {
            throw new SecurityException("Caller not allowed to unlimite silent updates");
        }
        this.mSilentUpdatePolicy.setAllowUnlimitedSilentUpdates(str);
    }

    public void setSilentUpdatesThrottleTime(long j) {
        if (!PackageManagerServiceUtils.isSystemOrRootOrShell(Binder.getCallingUid())) {
            throw new SecurityException("Caller not allowed to set silent updates throttle time");
        }
        this.mSilentUpdatePolicy.setSilentUpdatesThrottleTime(j);
    }

    private static int getSessionCount(SparseArray<PackageInstallerSession> sparseArray, int i) {
        int size = sparseArray.size();
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            if (sparseArray.valueAt(i3).getInstallerUid() == i) {
                i2++;
            }
        }
        return i2;
    }

    private boolean isCallingUidOwner(PackageInstallerSession packageInstallerSession) {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 0) {
            return true;
        }
        return packageInstallerSession != null && callingUid == packageInstallerSession.getInstallerUid();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldFilterSession(Computer computer, int i, int i2) {
        PackageInstallerSession session = getSession(i2);
        return (session == null || i == session.getInstallerUid() || computer.canQueryPackage(i, session.getPackageName())) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PackageDeleteObserverAdapter extends PackageDeleteObserver {
        private final Context mContext;
        private final Notification mNotification;
        private final String mPackageName;
        private final IntentSender mTarget;

        public PackageDeleteObserverAdapter(Context context, IntentSender intentSender, String str, boolean z, int i) {
            this.mContext = context;
            this.mTarget = intentSender;
            this.mPackageName = str;
            if (z) {
                this.mNotification = PackageInstallerService.buildSuccessNotification(context, getDeviceOwnerDeletedPackageMsg(), str, i);
            } else {
                this.mNotification = null;
            }
        }

        private String getDeviceOwnerDeletedPackageMsg() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getResources().getString("Core.PACKAGE_DELETED_BY_DO", new Supplier() { // from class: com.android.server.pm.PackageInstallerService$PackageDeleteObserverAdapter$$ExternalSyntheticLambda0
                    @Override // java.util.function.Supplier
                    public final Object get() {
                        String lambda$getDeviceOwnerDeletedPackageMsg$0;
                        lambda$getDeviceOwnerDeletedPackageMsg$0 = PackageInstallerService.PackageDeleteObserverAdapter.this.lambda$getDeviceOwnerDeletedPackageMsg$0();
                        return lambda$getDeviceOwnerDeletedPackageMsg$0;
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ String lambda$getDeviceOwnerDeletedPackageMsg$0() {
            return this.mContext.getString(R.string.permlab_useDataInBackground);
        }

        public void onUserActionRequired(Intent intent) {
            if (this.mTarget == null) {
                return;
            }
            Intent intent2 = new Intent();
            intent2.putExtra("android.content.pm.extra.PACKAGE_NAME", this.mPackageName);
            intent2.putExtra("android.content.pm.extra.STATUS", -1);
            intent2.putExtra("android.intent.extra.INTENT", intent);
            try {
                BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
                makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
                this.mTarget.sendIntent(this.mContext, 0, intent2, null, null, null, makeBasic.toBundle());
            } catch (IntentSender.SendIntentException unused) {
            }
        }

        public void onPackageDeleted(String str, int i, String str2) {
            if (1 == i && this.mNotification != null) {
                ((NotificationManager) this.mContext.getSystemService("notification")).notify(str, 21, this.mNotification);
            }
            if (this.mTarget == null) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("android.content.pm.extra.PACKAGE_NAME", this.mPackageName);
            intent.putExtra("android.content.pm.extra.STATUS", PackageManager.deleteStatusToPublicStatus(i));
            intent.putExtra("android.content.pm.extra.STATUS_MESSAGE", PackageManager.deleteStatusToString(i, str2));
            intent.putExtra("android.content.pm.extra.LEGACY_STATUS", i);
            try {
                BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
                makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
                this.mTarget.sendIntent(this.mContext, 0, intent, null, null, null, makeBasic.toBundle());
            } catch (IntentSender.SendIntentException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Notification buildSuccessNotification(Context context, String str, String str2, int i) {
        PackageInfo packageInfo;
        try {
            packageInfo = AppGlobals.getPackageManager().getPackageInfo(str2, 67108864L, i);
        } catch (RemoteException unused) {
            packageInfo = null;
        }
        if (packageInfo == null || packageInfo.applicationInfo == null) {
            Slog.w(TAG, "Notification not built for package: " + str2);
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        return new Notification.Builder(context, SystemNotificationChannels.DEVICE_ADMIN).setSmallIcon(R.drawable.ic_contact_picture_180_holo_dark).setColor(context.getResources().getColor(R.color.system_notification_accent_color)).setContentTitle(packageInfo.applicationInfo.loadLabel(packageManager)).setContentText(str).setStyle(new Notification.BigTextStyle().bigText(str)).setLargeIcon(ImageUtils.buildScaledBitmap(packageInfo.applicationInfo.loadIcon(packageManager), context.getResources().getDimensionPixelSize(R.dimen.notification_large_icon_width), context.getResources().getDimensionPixelSize(R.dimen.notification_large_icon_height))).build();
    }

    public static <E> ArraySet<E> newArraySet(E... eArr) {
        ArraySet<E> arraySet = new ArraySet<>();
        if (eArr != null) {
            arraySet.ensureCapacity(eArr.length);
            Collections.addAll(arraySet, eArr);
        }
        return arraySet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class BroadcastCookie {
        public final int callingUid;
        public final IntPredicate userCheck;

        BroadcastCookie(int i, IntPredicate intPredicate) {
            this.callingUid = i;
            this.userCheck = intPredicate;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class Callbacks extends Handler {
        private static final int MSG_SESSION_ACTIVE_CHANGED = 3;
        private static final int MSG_SESSION_BADGING_CHANGED = 2;
        private static final int MSG_SESSION_CREATED = 1;
        private static final int MSG_SESSION_FINISHED = 5;
        private static final int MSG_SESSION_PROGRESS_CHANGED = 4;
        private final RemoteCallbackList<IPackageInstallerCallback> mCallbacks;

        public Callbacks(Looper looper) {
            super(looper);
            this.mCallbacks = new RemoteCallbackList<>();
        }

        public void register(IPackageInstallerCallback iPackageInstallerCallback, BroadcastCookie broadcastCookie) {
            this.mCallbacks.register(iPackageInstallerCallback, broadcastCookie);
        }

        public void unregister(IPackageInstallerCallback iPackageInstallerCallback) {
            this.mCallbacks.unregister(iPackageInstallerCallback);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.arg1;
            int i2 = message.arg2;
            int beginBroadcast = this.mCallbacks.beginBroadcast();
            Computer snapshotComputer = PackageInstallerService.this.mPm.snapshotComputer();
            for (int i3 = 0; i3 < beginBroadcast; i3++) {
                IPackageInstallerCallback broadcastItem = this.mCallbacks.getBroadcastItem(i3);
                BroadcastCookie broadcastCookie = (BroadcastCookie) this.mCallbacks.getBroadcastCookie(i3);
                if (broadcastCookie.userCheck.test(i2) && !PackageInstallerService.this.shouldFilterSession(snapshotComputer, broadcastCookie.callingUid, i)) {
                    try {
                        invokeCallback(broadcastItem, message);
                    } catch (RemoteException unused) {
                    }
                }
            }
            this.mCallbacks.finishBroadcast();
        }

        private void invokeCallback(IPackageInstallerCallback iPackageInstallerCallback, Message message) throws RemoteException {
            int i = message.arg1;
            int i2 = message.what;
            if (i2 == 1) {
                iPackageInstallerCallback.onSessionCreated(i);
                return;
            }
            if (i2 == 2) {
                iPackageInstallerCallback.onSessionBadgingChanged(i);
                return;
            }
            if (i2 == 3) {
                iPackageInstallerCallback.onSessionActiveChanged(i, ((Boolean) message.obj).booleanValue());
            } else if (i2 == 4) {
                iPackageInstallerCallback.onSessionProgressChanged(i, ((Float) message.obj).floatValue());
            } else {
                if (i2 != 5) {
                    return;
                }
                iPackageInstallerCallback.onSessionFinished(i, ((Boolean) message.obj).booleanValue());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionCreated(int i, int i2) {
            obtainMessage(1, i, i2).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionBadgingChanged(int i, int i2) {
            obtainMessage(2, i, i2).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionActiveChanged(int i, int i2, boolean z) {
            obtainMessage(3, i, i2, Boolean.valueOf(z)).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifySessionProgressChanged(int i, int i2, float f) {
            obtainMessage(4, i, i2, Float.valueOf(f)).sendToTarget();
        }

        public void notifySessionFinished(int i, int i2, boolean z) {
            obtainMessage(5, i, i2, Boolean.valueOf(z)).sendToTarget();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class ParentChildSessionMap {
        private final Comparator<PackageInstallerSession> mSessionCreationComparator;
        private TreeMap<PackageInstallerSession, TreeSet<PackageInstallerSession>> mSessionMap;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ long lambda$new$0(PackageInstallerSession packageInstallerSession) {
            if (packageInstallerSession != null) {
                return packageInstallerSession.createdMillis;
            }
            return -1L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$new$1(PackageInstallerSession packageInstallerSession) {
            if (packageInstallerSession != null) {
                return packageInstallerSession.sessionId;
            }
            return -1;
        }

        ParentChildSessionMap() {
            Comparator<PackageInstallerSession> thenComparingInt = Comparator.comparingLong(new ToLongFunction() { // from class: com.android.server.pm.PackageInstallerService$ParentChildSessionMap$$ExternalSyntheticLambda0
                @Override // java.util.function.ToLongFunction
                public final long applyAsLong(Object obj) {
                    long lambda$new$0;
                    lambda$new$0 = PackageInstallerService.ParentChildSessionMap.lambda$new$0((PackageInstallerSession) obj);
                    return lambda$new$0;
                }
            }).thenComparingInt(new ToIntFunction() { // from class: com.android.server.pm.PackageInstallerService$ParentChildSessionMap$$ExternalSyntheticLambda1
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    int lambda$new$1;
                    lambda$new$1 = PackageInstallerService.ParentChildSessionMap.lambda$new$1((PackageInstallerSession) obj);
                    return lambda$new$1;
                }
            });
            this.mSessionCreationComparator = thenComparingInt;
            this.mSessionMap = new TreeMap<>(thenComparingInt);
        }

        boolean containsSession() {
            return !this.mSessionMap.isEmpty();
        }

        private void addParentSession(PackageInstallerSession packageInstallerSession) {
            if (this.mSessionMap.containsKey(packageInstallerSession)) {
                return;
            }
            this.mSessionMap.put(packageInstallerSession, new TreeSet<>(this.mSessionCreationComparator));
        }

        private void addChildSession(PackageInstallerSession packageInstallerSession, PackageInstallerSession packageInstallerSession2) {
            addParentSession(packageInstallerSession2);
            this.mSessionMap.get(packageInstallerSession2).add(packageInstallerSession);
        }

        void addSession(PackageInstallerSession packageInstallerSession, PackageInstallerSession packageInstallerSession2) {
            if (packageInstallerSession.hasParentSessionId()) {
                addChildSession(packageInstallerSession, packageInstallerSession2);
            } else {
                addParentSession(packageInstallerSession);
            }
        }

        void dump(String str, IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println(str + " install sessions:");
            indentingPrintWriter.increaseIndent();
            for (Map.Entry<PackageInstallerSession, TreeSet<PackageInstallerSession>> entry : this.mSessionMap.entrySet()) {
                PackageInstallerSession key = entry.getKey();
                if (key != null) {
                    indentingPrintWriter.print(str + " ");
                    key.dump(indentingPrintWriter);
                    indentingPrintWriter.println();
                    indentingPrintWriter.increaseIndent();
                }
                Iterator<PackageInstallerSession> it = entry.getValue().iterator();
                while (it.hasNext()) {
                    PackageInstallerSession next = it.next();
                    indentingPrintWriter.print(str + " Child ");
                    next.dump(indentingPrintWriter);
                    indentingPrintWriter.println();
                }
                indentingPrintWriter.decreaseIndent();
            }
            indentingPrintWriter.println();
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this.mSessions) {
            ParentChildSessionMap parentChildSessionMap = new ParentChildSessionMap();
            ParentChildSessionMap parentChildSessionMap2 = new ParentChildSessionMap();
            ParentChildSessionMap parentChildSessionMap3 = new ParentChildSessionMap();
            int size = this.mSessions.size();
            for (int i = 0; i < size; i++) {
                PackageInstallerSession valueAt = this.mSessions.valueAt(i);
                PackageInstallerSession session = valueAt.hasParentSessionId() ? getSession(valueAt.getParentSessionId()) : valueAt;
                if (session == null) {
                    parentChildSessionMap2.addSession(valueAt, session);
                } else if (session.isStagedAndInTerminalState()) {
                    parentChildSessionMap3.addSession(valueAt, session);
                } else {
                    parentChildSessionMap.addSession(valueAt, session);
                }
            }
            parentChildSessionMap.dump("Active", indentingPrintWriter);
            if (parentChildSessionMap2.containsSession()) {
                parentChildSessionMap2.dump("Orphaned", indentingPrintWriter);
            }
            parentChildSessionMap3.dump("Finalized", indentingPrintWriter);
            indentingPrintWriter.println("Historical install sessions:");
            indentingPrintWriter.increaseIndent();
            int size2 = this.mHistoricalSessions.size();
            for (int i2 = 0; i2 < size2; i2++) {
                indentingPrintWriter.print(this.mHistoricalSessions.get(i2));
                indentingPrintWriter.println();
            }
            indentingPrintWriter.println();
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("Legacy install sessions:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println(this.mLegacySessions.toString());
            indentingPrintWriter.println();
            indentingPrintWriter.decreaseIndent();
        }
        this.mSilentUpdatePolicy.dump(indentingPrintWriter);
        this.mGentleUpdateHelper.dump(indentingPrintWriter);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class InternalCallback {
        public InternalCallback() {
        }

        public void onSessionBadgingChanged(PackageInstallerSession packageInstallerSession) {
            PackageInstallerService.this.mCallbacks.notifySessionBadgingChanged(packageInstallerSession.sessionId, packageInstallerSession.userId);
            PackageInstallerService.this.mSettingsWriteRequest.schedule();
        }

        public void onSessionActiveChanged(PackageInstallerSession packageInstallerSession, boolean z) {
            PackageInstallerService.this.mCallbacks.notifySessionActiveChanged(packageInstallerSession.sessionId, packageInstallerSession.userId, z);
        }

        public void onSessionProgressChanged(PackageInstallerSession packageInstallerSession, float f) {
            PackageInstallerService.this.mCallbacks.notifySessionProgressChanged(packageInstallerSession.sessionId, packageInstallerSession.userId, f);
        }

        public void onSessionChanged(PackageInstallerSession packageInstallerSession) {
            packageInstallerSession.markUpdated();
            PackageInstallerService.this.mSettingsWriteRequest.schedule();
            if (PackageInstallerService.this.mOkToSendBroadcasts && !packageInstallerSession.isDestroyed() && packageInstallerSession.isStaged()) {
                PackageInstallerService.this.sendSessionUpdatedBroadcast(packageInstallerSession.generateInfoForCaller(false, 1000), packageInstallerSession.userId);
            }
        }

        public void onSessionFinished(final PackageInstallerSession packageInstallerSession, final boolean z) {
            PackageInstallerService.this.mCallbacks.notifySessionFinished(packageInstallerSession.sessionId, packageInstallerSession.userId, z);
            PackageInstallerService.this.mInstallHandler.post(new Runnable() { // from class: com.android.server.pm.PackageInstallerService.InternalCallback.1
                /* JADX WARN: Removed duplicated region for block: B:20:0x004a A[Catch: all -> 0x0075, TryCatch #0 {, blocks: (B:9:0x0024, B:11:0x002c, B:13:0x0034, B:15:0x003c, B:20:0x004a, B:22:0x0053, B:24:0x0065, B:25:0x0068, B:26:0x0073), top: B:8:0x0024 }] */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void run() {
                    boolean z2;
                    if (packageInstallerSession.isStaged() && !z) {
                        PackageInstallerService.this.mStagingManager.abortSession(packageInstallerSession.mStagedSession);
                    }
                    synchronized (PackageInstallerService.this.mSessions) {
                        if (!packageInstallerSession.hasParentSessionId()) {
                            if (packageInstallerSession.isStaged() && !packageInstallerSession.isDestroyed() && packageInstallerSession.isCommitted()) {
                                z2 = false;
                                if (z2) {
                                    PackageInstallerService.this.removeActiveSession(packageInstallerSession);
                                }
                            }
                            z2 = true;
                            if (z2) {
                            }
                        }
                        File buildAppIconFile = PackageInstallerService.this.buildAppIconFile(packageInstallerSession.sessionId);
                        if (buildAppIconFile.exists()) {
                            buildAppIconFile.delete();
                        }
                        PackageInstallerService.this.mSettingsWriteRequest.runNow();
                    }
                }
            });
        }

        public void onSessionPrepared(PackageInstallerSession packageInstallerSession) {
            PackageInstallerService.this.mSettingsWriteRequest.schedule();
        }

        public void onSessionSealedBlocking(PackageInstallerSession packageInstallerSession) {
            PackageInstallerService.this.mSettingsWriteRequest.runNow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSessionUpdatedBroadcast(PackageInstaller.SessionInfo sessionInfo, int i) {
        if (TextUtils.isEmpty(sessionInfo.installerPackageName)) {
            return;
        }
        this.mContext.sendBroadcastAsUser(new Intent("android.content.pm.action.SESSION_UPDATED").putExtra("android.content.pm.extra.SESSION", sessionInfo).setPackage(sessionInfo.installerPackageName), UserHandle.of(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onInstallerPackageDeleted(int i, int i2) {
        synchronized (this.mSessions) {
            for (int i3 = 0; i3 < this.mSessions.size(); i3++) {
                PackageInstallerSession valueAt = this.mSessions.valueAt(i3);
                if (matchesInstaller(valueAt, i, i2)) {
                    if (valueAt.hasParentSessionId()) {
                        valueAt = this.mSessions.get(valueAt.getParentSessionId());
                    }
                    if (valueAt != null && matchesInstaller(valueAt, i, i2) && !valueAt.isDestroyed()) {
                        valueAt.abandon();
                    }
                }
            }
        }
    }

    private boolean matchesInstaller(PackageInstallerSession packageInstallerSession, int i, int i2) {
        int installerUid = packageInstallerSession.getInstallerUid();
        return i == -1 ? UserHandle.getAppId(installerUid) == i : UserHandle.getUid(i2, i) == installerUid;
    }
}
