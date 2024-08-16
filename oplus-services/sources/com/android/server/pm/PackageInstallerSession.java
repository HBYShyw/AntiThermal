package com.android.server.pm;

import android.R;
import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManager;
import android.app.admin.DevicePolicyManagerInternal;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.Checksum;
import android.content.pm.DataLoaderManager;
import android.content.pm.DataLoaderParams;
import android.content.pm.DataLoaderParamsParcel;
import android.content.pm.FileSystemControlParcel;
import android.content.pm.IDataLoader;
import android.content.pm.IDataLoaderStatusListener;
import android.content.pm.IOnChecksumsReadyListener;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.IPackageInstallerSession;
import android.content.pm.IPackageInstallerSessionFileSystemConnector;
import android.content.pm.IPackageLoadingProgressCallback;
import android.content.pm.InstallSourceInfo;
import android.content.pm.InstallationFile;
import android.content.pm.InstallationFileParcel;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.SigningDetails;
import android.content.pm.dex.DexMetadataHelper;
import android.content.pm.parsing.ApkLite;
import android.content.pm.parsing.ApkLiteParseUtils;
import android.content.pm.parsing.PackageLite;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.ULocale;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileBridge;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ParcelableException;
import android.os.RemoteException;
import android.os.RevocableFileDescriptor;
import android.os.SELinux;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.incremental.IStorageHealthListener;
import android.os.incremental.IncrementalFileStorages;
import android.os.incremental.IncrementalManager;
import android.os.incremental.PerUidReadTimeouts;
import android.os.incremental.StorageHealthCheckParams;
import android.os.storage.StorageManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Int64Ref;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.ExceptionUtils;
import android.util.IntArray;
import android.util.Log;
import android.util.MathUtils;
import android.util.Slog;
import android.util.SparseArray;
import android.util.apk.ApkSignatureVerifier;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.compat.IPlatformCompat;
import com.android.internal.content.InstallLocationUtils;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.os.SomeArgs;
import com.android.internal.security.VerityUtils;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.LocalServices;
import com.android.server.SystemServerInitThreadPool$;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.pm.Installer;
import com.android.server.pm.PackageInstallerService;
import com.android.server.pm.PackageInstallerSession;
import com.android.server.pm.PackageSessionVerifier;
import com.android.server.pm.StagingManager;
import com.android.server.pm.dex.DexManager;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageInstallerSession extends IPackageInstallerSession.Stub {
    private static final String APEX_FILE_EXTENSION = ".apex";
    private static final int APP_METADATA_FILE_ACCESS_MODE = 416;
    private static final String ATTR_ABI_OVERRIDE = "abiOverride";
    private static final String ATTR_APPLICATION_ENABLED_SETTING_PERSISTENT = "applicationEnabledSettingPersistent";

    @Deprecated
    private static final String ATTR_APP_ICON = "appIcon";
    private static final String ATTR_APP_LABEL = "appLabel";
    private static final String ATTR_APP_PACKAGE_NAME = "appPackageName";
    private static final String ATTR_CHECKSUM_KIND = "checksumKind";
    private static final String ATTR_CHECKSUM_VALUE = "checksumValue";
    private static final String ATTR_COMMITTED = "committed";
    private static final String ATTR_COMMITTED_MILLIS = "committedMillis";
    private static final String ATTR_CREATED_MILLIS = "createdMillis";
    private static final String ATTR_DATALOADER_ARGUMENTS = "dataLoaderArguments";
    private static final String ATTR_DATALOADER_CLASS_NAME = "dataLoaderClassName";
    private static final String ATTR_DATALOADER_PACKAGE_NAME = "dataLoaderPackageName";
    private static final String ATTR_DATALOADER_TYPE = "dataLoaderType";
    private static final String ATTR_DESTROYED = "destroyed";
    private static final String ATTR_INITIATING_PACKAGE_NAME = "installInitiatingPackageName";
    private static final String ATTR_INSTALLER_ATTRIBUTION_TAG = "installerAttributionTag";
    private static final String ATTR_INSTALLER_PACKAGE_NAME = "installerPackageName";
    private static final String ATTR_INSTALLER_PACKAGE_UID = "installerPackageUid";
    private static final String ATTR_INSTALLER_UID = "installerUid";
    private static final String ATTR_INSTALL_FLAGS = "installFlags";
    private static final String ATTR_INSTALL_LOCATION = "installLocation";
    private static final String ATTR_INSTALL_REASON = "installRason";
    private static final String ATTR_IS_APPLIED = "isApplied";
    private static final String ATTR_IS_DATALOADER = "isDataLoader";
    private static final String ATTR_IS_FAILED = "isFailed";
    private static final String ATTR_IS_READY = "isReady";
    private static final String ATTR_LENGTH_BYTES = "lengthBytes";
    private static final String ATTR_LOCATION = "location";
    private static final String ATTR_METADATA = "metadata";
    private static final String ATTR_MODE = "mode";
    private static final String ATTR_MULTI_PACKAGE = "multiPackage";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_ORIGINATING_PACKAGE_NAME = "installOriginatingPackageName";
    private static final String ATTR_ORIGINATING_UID = "originatingUid";
    private static final String ATTR_ORIGINATING_URI = "originatingUri";
    private static final String ATTR_PACKAGE_SOURCE = "packageSource";
    private static final String ATTR_PARENT_SESSION_ID = "parentSessionId";
    private static final String ATTR_PREPARED = "prepared";
    private static final String ATTR_REFERRER_URI = "referrerUri";
    private static final String ATTR_SEALED = "sealed";
    private static final String ATTR_SESSION_ERROR_CODE = "errorCode";
    private static final String ATTR_SESSION_ERROR_MESSAGE = "errorMessage";
    private static final String ATTR_SESSION_ID = "sessionId";
    private static final String ATTR_SESSION_STAGE_CID = "sessionStageCid";
    private static final String ATTR_SESSION_STAGE_DIR = "sessionStageDir";
    private static final String ATTR_SIGNATURE = "signature";
    private static final String ATTR_SIZE_BYTES = "sizeBytes";
    private static final String ATTR_STAGED_SESSION = "stagedSession";
    private static final String ATTR_UPDATED_MILLIS = "updatedMillis";
    private static final String ATTR_UPDATE_OWNER_PACKAGE_NAME = "updateOwnererPackageName";
    private static final String ATTR_USER_ID = "userId";
    private static final String ATTR_VOLUME_UUID = "volumeUuid";
    private static final long DEFAULT_APP_METADATA_BYTE_SIZE_LIMIT = 32000;
    private static final int INCREMENTAL_STORAGE_BLOCKED_TIMEOUT_MS = 2000;
    private static final int INCREMENTAL_STORAGE_UNHEALTHY_MONITORING_MS = 60000;
    private static final int INCREMENTAL_STORAGE_UNHEALTHY_TIMEOUT_MS = 7000;
    private static final int INVALID_TARGET_SDK_VERSION = Integer.MAX_VALUE;
    private static final boolean LOGD = true;
    private static final int MSG_INSTALL = 3;
    private static final int MSG_ON_PACKAGE_INSTALLED = 4;
    private static final int MSG_ON_SESSION_SEALED = 1;
    private static final int MSG_PRE_APPROVAL_REQUEST = 6;
    private static final int MSG_SESSION_VALIDATION_FAILURE = 5;
    private static final int MSG_STREAM_VALIDATE_AND_COMMIT = 2;
    private static final long PRE_APPROVAL_WITH_UPDATE_OWNERSHIP_FIX = 293644536;
    private static final String PROPERTY_APP_METADATA_BYTE_SIZE_LIMIT = "app_metadata_byte_size_limit";
    private static final String PROPERTY_NAME_INHERIT_NATIVE = "pi.inherit_native_on_dont_kill";
    private static final String REMOVE_MARKER_EXTENSION = ".removed";
    private static final long SILENT_INSTALL_ALLOWED = 265131695;
    private static final String SYSTEM_DATA_LOADER_PACKAGE = "android";
    private static final String TAG = "PackageInstallerSession";
    private static final String TAG_AUTO_REVOKE_PERMISSIONS_MODE = "auto-revoke-permissions-mode";
    static final String TAG_CHILD_SESSION = "childSession";
    private static final String TAG_DENY_PERMISSION = "deny-permission";
    private static final String TAG_GRANTED_RUNTIME_PERMISSION = "granted-runtime-permission";
    private static final String TAG_GRANT_PERMISSION = "grant-permission";
    static final String TAG_SESSION = "session";
    static final String TAG_SESSION_CHECKSUM = "sessionChecksum";
    static final String TAG_SESSION_CHECKSUM_SIGNATURE = "sessionChecksumSignature";
    static final String TAG_SESSION_FILE = "sessionFile";
    private static final String TAG_WHITELISTED_RESTRICTED_PERMISSION = "whitelisted-restricted-permission";
    private static final int USER_ACTION_NOT_NEEDED = 0;
    private static final int USER_ACTION_PENDING_APK_PARSING = 2;
    private static final int USER_ACTION_REQUIRED = 1;
    private static final int USER_ACTION_REQUIRED_UPDATE_OWNER_REMINDER = 3;

    @GuardedBy({"mLock"})
    private long committedMillis;
    final long createdMillis;
    private final PackageInstallerService.InternalCallback mCallback;
    private final Context mContext;
    private volatile boolean mDestroyed;

    @GuardedBy({"mLock"})
    private String mFinalMessage;

    @GuardedBy({"mLock"})
    private int mFinalStatus;
    private final Handler mHandler;
    private final Handler.Callback mHandlerCallback;

    @GuardedBy({"mLock"})
    private boolean mHasDeviceAdminReceiver;

    @GuardedBy({"mLock"})
    private IncrementalFileStorages mIncrementalFileStorages;

    @GuardedBy({"mLock"})
    private File mInheritedFilesBase;

    @GuardedBy({"mLock"})
    private InstallSource mInstallSource;
    private final Installer mInstaller;
    private volatile int mInstallerUid;
    private final String mOriginalInstallerPackageName;
    private final int mOriginalInstallerUid;

    @GuardedBy({"mLock"})
    private PackageLite mPackageLite;

    @GuardedBy({"mLock"})
    private String mPackageName;

    @GuardedBy({"mLock"})
    private int mParentSessionId;

    @GuardedBy({"mLock"})
    private Runnable mPendingAbandonCallback;
    private final PackageManagerService mPm;

    @GuardedBy({"mLock"})
    private PackageInstaller.PreapprovalDetails mPreapprovalDetails;

    @GuardedBy({"mLock"})
    private boolean mPrepared;

    @GuardedBy({"mLock"})
    private IntentSender mRemoteStatusReceiver;

    @GuardedBy({"mLock"})
    private File mResolvedBaseFile;

    @GuardedBy({"mLock"})
    private boolean mSessionApplied;

    @GuardedBy({"mLock"})
    private int mSessionErrorCode;

    @GuardedBy({"mLock"})
    private String mSessionErrorMessage;

    @GuardedBy({"mLock"})
    private boolean mSessionFailed;
    private final PackageSessionProvider mSessionProvider;

    @GuardedBy({"mLock"})
    private boolean mSessionReady;

    @GuardedBy({"mLock"})
    private boolean mShouldBeSealed;

    @GuardedBy({"mLock"})
    private SigningDetails mSigningDetails;
    private final SilentUpdatePolicy mSilentUpdatePolicy;
    final StagedSession mStagedSession;
    private final StagingManager mStagingManager;
    private Boolean mUserActionRequired;

    @GuardedBy({"mLock"})
    private int mUserActionRequirement;

    @GuardedBy({"mLock"})
    private boolean mVerityFoundForApks;

    @GuardedBy({"mLock"})
    private long mVersionCode;
    private final IPackageInstallerSessionWrapper mWrapper;
    final PackageInstaller.SessionParams params;
    final int sessionId;
    final String stageCid;
    final File stageDir;

    @GuardedBy({"mLock"})
    private long updatedMillis;
    final int userId;
    private static final int[] EMPTY_CHILD_SESSION_ARRAY = EmptyArray.INT;
    private static final InstallationFile[] EMPTY_INSTALLATION_FILE_ARRAY = new InstallationFile[0];
    private static final FileFilter sAddedApkFilter = new FileFilter() { // from class: com.android.server.pm.PackageInstallerSession.1
        @Override // java.io.FileFilter
        public boolean accept(File file) {
            return (file.isDirectory() || file.getName().endsWith(PackageInstallerSession.REMOVE_MARKER_EXTENSION) || PackageInstallerSession.isAppMetadata(file) || DexMetadataHelper.isDexMetadataFile(file) || VerityUtils.isFsveritySignatureFile(file) || ApkChecksums.isDigestOrDigestSignatureFile(file)) ? false : true;
        }
    };
    private static final FileFilter sAddedFilter = new FileFilter() { // from class: com.android.server.pm.PackageInstallerSession.2
        @Override // java.io.FileFilter
        public boolean accept(File file) {
            return (file.isDirectory() || file.getName().endsWith(PackageInstallerSession.REMOVE_MARKER_EXTENSION)) ? false : true;
        }
    };
    private static final FileFilter sRemovedFilter = new FileFilter() { // from class: com.android.server.pm.PackageInstallerSession.3
        @Override // java.io.FileFilter
        public boolean accept(File file) {
            return !file.isDirectory() && file.getName().endsWith(PackageInstallerSession.REMOVE_MARKER_EXTENSION);
        }
    };
    private final AtomicInteger mActiveCount = new AtomicInteger();
    private final Object mLock = new Object();
    private final AtomicBoolean mTransactionLock = new AtomicBoolean(false);
    private final Object mProgressLock = new Object();

    @GuardedBy({"mProgressLock"})
    private float mClientProgress = 0.0f;

    @GuardedBy({"mProgressLock"})
    private float mInternalProgress = 0.0f;

    @GuardedBy({"mProgressLock"})
    private float mProgress = 0.0f;

    @GuardedBy({"mProgressLock"})
    private float mReportedProgress = -1.0f;

    @GuardedBy({"mProgressLock"})
    private float mIncrementalProgress = 0.0f;

    @GuardedBy({"mLock"})
    private boolean mSealed = false;
    private final AtomicBoolean mPreapprovalRequested = new AtomicBoolean(false);
    private final AtomicBoolean mCommitted = new AtomicBoolean(false);

    @GuardedBy({"mLock"})
    private boolean mStageDirInUse = false;
    private boolean mVerificationInProgress = false;

    @GuardedBy({"mLock"})
    private boolean mPermissionsManuallyAccepted = false;

    @GuardedBy({"mLock"})
    private final ArrayList<RevocableFileDescriptor> mFds = new ArrayList<>();

    @GuardedBy({"mLock"})
    private final ArrayList<FileBridge> mBridges = new ArrayList<>();

    @GuardedBy({"mLock"})
    private SparseArray<PackageInstallerSession> mChildSessions = new SparseArray<>();
    IPackageInstallerSessionExt mPackageInstallerSessionExt = (IPackageInstallerSessionExt) ExtLoader.type(IPackageInstallerSessionExt.class).base(this).create();
    IPackageInstallerSessionSocExt mPackageInstallerSessionSocExt = (IPackageInstallerSessionSocExt) ExtLoader.type(IPackageInstallerSessionSocExt.class).base(this).create();

    @GuardedBy({"mLock"})
    private boolean mMarkStageDirUserNoConsume = false;

    @GuardedBy({"mLock"})
    private ArraySet<FileEntry> mFiles = new ArraySet<>();

    @GuardedBy({"mLock"})
    private ArrayMap<String, PerFileChecksum> mChecksums = new ArrayMap<>();

    @GuardedBy({"mLock"})
    private final List<File> mResolvedStagedFiles = new ArrayList();

    @GuardedBy({"mLock"})
    private final List<File> mResolvedInheritedFiles = new ArrayList();

    @GuardedBy({"mLock"})
    private final List<String> mResolvedInstructionSets = new ArrayList();

    @GuardedBy({"mLock"})
    private final List<String> mResolvedNativeLibPaths = new ArrayList();
    private volatile boolean mDataLoaderFinished = false;

    @GuardedBy({"mLock"})
    private int mValidatedTargetSdk = Integer.MAX_VALUE;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface UserActionRequirement {
    }

    private static boolean isStagedSessionStateValid(boolean z, boolean z2, boolean z3) {
        return ((z || z2 || z3) && (!z || z2 || z3) && ((z || !z2 || z3) && (z || z2 || !z3))) ? false : true;
    }

    private static int userActionRequirementToReason(@UserActionRequirement int i) {
        return i != 3 ? 0 : 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class FileEntry {
        private final InstallationFile mFile;
        private final int mIndex;

        FileEntry(int i, InstallationFile installationFile) {
            this.mIndex = i;
            this.mFile = installationFile;
        }

        int getIndex() {
            return this.mIndex;
        }

        InstallationFile getFile() {
            return this.mFile;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof FileEntry)) {
                return false;
            }
            FileEntry fileEntry = (FileEntry) obj;
            return this.mFile.getLocation() == fileEntry.mFile.getLocation() && TextUtils.equals(this.mFile.getName(), fileEntry.mFile.getName());
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mFile.getLocation()), this.mFile.getName());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PerFileChecksum {
        private final Checksum[] mChecksums;
        private final byte[] mSignature;

        PerFileChecksum(Checksum[] checksumArr, byte[] bArr) {
            this.mChecksums = checksumArr;
            this.mSignature = bArr;
        }

        Checksum[] getChecksums() {
            return this.mChecksums;
        }

        byte[] getSignature() {
            return this.mSignature;
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class StagedSession implements StagingManager.StagedSession {
        public StagedSession() {
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public List<StagingManager.StagedSession> getChildSessions() {
            ArrayList arrayList;
            PackageInstallerSession packageInstallerSession = PackageInstallerSession.this;
            if (!packageInstallerSession.params.isMultiPackage) {
                return Collections.EMPTY_LIST;
            }
            synchronized (packageInstallerSession.mLock) {
                int size = PackageInstallerSession.this.mChildSessions.size();
                arrayList = new ArrayList(size);
                for (int i = 0; i < size; i++) {
                    arrayList.add(((PackageInstallerSession) PackageInstallerSession.this.mChildSessions.valueAt(i)).mStagedSession);
                }
            }
            return arrayList;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public PackageInstaller.SessionParams sessionParams() {
            return PackageInstallerSession.this.params;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isMultiPackage() {
            return PackageInstallerSession.this.params.isMultiPackage;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isApexSession() {
            return (PackageInstallerSession.this.params.installFlags & 131072) != 0;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public int sessionId() {
            return PackageInstallerSession.this.sessionId;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean containsApexSession() {
            return sessionContains(new Predicate() { // from class: com.android.server.pm.PackageInstallerSession$StagedSession$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean isApexSession;
                    isApexSession = ((StagingManager.StagedSession) obj).isApexSession();
                    return isApexSession;
                }
            });
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public String getPackageName() {
            return PackageInstallerSession.this.getPackageName();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void setSessionReady() {
            PackageInstallerSession.this.setSessionReady();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void setSessionFailed(int i, String str) {
            PackageInstallerSession packageInstallerSession = PackageInstallerSession.this;
            packageInstallerSession.mPackageInstallerSessionExt.recordSotaAppResult(packageInstallerSession.mStagedSession, i, str);
            PackageInstallerSession.this.setSessionFailed(i, str);
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void setSessionApplied() {
            PackageInstallerSession.this.setSessionApplied();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean containsApkSession() {
            return PackageInstallerSession.this.containsApkSession();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public CompletableFuture<Void> installSession() {
            PackageInstallerSession.this.assertCallerIsOwnerOrRootOrSystem();
            PackageInstallerSession.this.assertNotChild("StagedSession#installSession");
            Preconditions.checkArgument(isCommitted() && isSessionReady());
            return PackageInstallerSession.this.install();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean hasParentSessionId() {
            return PackageInstallerSession.this.hasParentSessionId();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public int getParentSessionId() {
            return PackageInstallerSession.this.getParentSessionId();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isCommitted() {
            return PackageInstallerSession.this.isCommitted();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isInTerminalState() {
            return PackageInstallerSession.this.isInTerminalState();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isDestroyed() {
            return PackageInstallerSession.this.isDestroyed();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public long getCommittedMillis() {
            return PackageInstallerSession.this.getCommittedMillis();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$sessionContains$1(Predicate predicate, PackageInstallerSession packageInstallerSession) {
            return predicate.test(packageInstallerSession.mStagedSession);
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean sessionContains(final Predicate<StagingManager.StagedSession> predicate) {
            return PackageInstallerSession.this.sessionContains(new Predicate() { // from class: com.android.server.pm.PackageInstallerSession$StagedSession$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$sessionContains$1;
                    lambda$sessionContains$1 = PackageInstallerSession.StagedSession.lambda$sessionContains$1(predicate, (PackageInstallerSession) obj);
                    return lambda$sessionContains$1;
                }
            });
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isSessionReady() {
            return PackageInstallerSession.this.isSessionReady();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isSessionApplied() {
            return PackageInstallerSession.this.isSessionApplied();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isSessionFailed() {
            return PackageInstallerSession.this.isSessionFailed();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void abandon() {
            PackageInstallerSession.this.abandon();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void verifySession() {
            PackageInstallerSession.this.assertCallerIsOwnerOrRootOrSystem();
            Preconditions.checkArgument(isCommitted());
            Preconditions.checkArgument(!isInTerminalState());
            PackageInstallerSession.this.verify();
        }
    }

    static boolean isDataLoaderInstallation(PackageInstaller.SessionParams sessionParams) {
        return sessionParams.dataLoaderParams != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSystemDataLoaderInstallation(PackageInstaller.SessionParams sessionParams) {
        if (isDataLoaderInstallation(sessionParams)) {
            return "android".equals(sessionParams.dataLoaderParams.getComponentName().getPackageName());
        }
        return false;
    }

    private boolean isDataLoaderInstallation() {
        return isDataLoaderInstallation(this.params);
    }

    private boolean isStreamingInstallation() {
        return isDataLoaderInstallation() && this.params.dataLoaderParams.getType() == 1;
    }

    private boolean isIncrementalInstallation() {
        return isDataLoaderInstallation() && this.params.dataLoaderParams.getType() == 2;
    }

    private boolean isSystemDataLoaderInstallation() {
        return isSystemDataLoaderInstallation(this.params);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInstallerDeviceOwnerOrAffiliatedProfileOwner() {
        DevicePolicyManagerInternal devicePolicyManagerInternal;
        assertNotLocked("isInstallerDeviceOwnerOrAffiliatedProfileOwner");
        return this.userId == UserHandle.getUserId(getInstallerUid()) && (devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class)) != null && devicePolicyManagerInternal.canSilentlyInstallPackage(getInstallSource().mInstallerPackageName, this.mInstallerUid);
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x0162, code lost:
    
        if (r3 == false) goto L137;
     */
    /* JADX WARN: Removed duplicated region for block: B:105:0x016c A[RETURN] */
    @UserActionRequirement
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int computeUserActionRequirement() {
        PackageInstaller.PreapprovalDetails preapprovalDetails;
        synchronized (this.mLock) {
            if (this.mPermissionsManuallyAccepted) {
                return 0;
            }
            String str = this.mPackageName;
            if (str == null) {
                str = (!this.mPreapprovalRequested.get() || (preapprovalDetails = this.mPreapprovalDetails) == null) ? null : preapprovalDetails.getPackageName();
            }
            boolean z = this.mHasDeviceAdminReceiver;
            PackageInstaller.SessionParams sessionParams = this.params;
            int i = ((sessionParams.installFlags & 1024) != 0 || sessionParams.requireUserAction == 1) ? 1 : 0;
            Computer snapshotComputer = this.mPm.snapshotComputer();
            boolean z2 = snapshotComputer.checkUidPermission("android.permission.INSTALL_PACKAGES", this.mInstallerUid) == 0;
            boolean z3 = snapshotComputer.checkUidPermission("android.permission.INSTALL_SELF_UPDATES", this.mInstallerUid) == 0;
            boolean z4 = snapshotComputer.checkUidPermission("android.permission.INSTALL_PACKAGE_UPDATES", this.mInstallerUid) == 0;
            boolean z5 = snapshotComputer.checkUidPermission("android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION", this.mInstallerUid) == 0;
            boolean z6 = snapshotComputer.checkUidPermission("android.permission.INSTALL_DPC_PACKAGES", this.mInstallerUid) == 0;
            int packageUid = snapshotComputer.getPackageUid(str, 0L, this.userId);
            boolean z7 = packageUid != -1 || isApexSession();
            InstallSourceInfo installSourceInfo = z7 ? snapshotComputer.getInstallSourceInfo(str, this.userId) : null;
            String installingPackageName = installSourceInfo != null ? installSourceInfo.getInstallingPackageName() : null;
            String updateOwnerPackageName = installSourceInfo != null ? installSourceInfo.getUpdateOwnerPackageName() : null;
            boolean z8 = z7 && Objects.equals(installingPackageName, getInstallerPackageName());
            boolean equals = TextUtils.equals(updateOwnerPackageName, getInstallerPackageName());
            boolean z9 = packageUid == this.mInstallerUid;
            boolean z10 = z2 || (z4 && z7) || ((z3 && z9) || (z6 && z));
            boolean z11 = this.mInstallerUid == 0;
            boolean z12 = UserHandle.getAppId(this.mInstallerUid) == 1000 || this.mPackageInstallerSessionExt.hasSilentInstallationPermissions(snapshotComputer, this.mOriginalInstallerPackageName, this.mOriginalInstallerUid);
            boolean z13 = this.mInstallerUid == 2000;
            boolean z14 = (this.params.installFlags & 67108864) != 0;
            boolean z15 = PackageManagerService.isUpdateOwnershipEnforcementAvailable() && updateOwnerPackageName != null;
            if (z11 || z12 || isInstallerDeviceOwnerOrAffiliatedProfileOwner()) {
                return i;
            }
            if (z15 && !isApexSession() && !equals && !z13 && !z14) {
                return 3;
            }
            if (z10) {
                return i;
            }
            if (!snapshotComputer.isInstallDisabledForPackage(getInstallerPackageName(), this.mInstallerUid, this.userId) && this.params.requireUserAction == 2 && z5) {
                if (z15) {
                    if (!this.mPackageInstallerSessionExt.adjustUserActionPendingInComputeUserActionRequirement()) {
                        return 2;
                    }
                } else if (!this.mPackageInstallerSessionExt.adjustUserActionPendingInComputeUserActionRequirement()) {
                }
            }
            return 1;
        }
    }

    private void updateUserActionRequirement(int i) {
        synchronized (this.mLock) {
            this.mUserActionRequirement = i;
        }
    }

    public PackageInstallerSession(PackageInstallerService.InternalCallback internalCallback, Context context, PackageManagerService packageManagerService, PackageSessionProvider packageSessionProvider, SilentUpdatePolicy silentUpdatePolicy, Looper looper, StagingManager stagingManager, int i, int i2, int i3, InstallSource installSource, PackageInstaller.SessionParams sessionParams, long j, long j2, File file, String str, InstallationFile[] installationFileArr, ArrayMap<String, PerFileChecksum> arrayMap, boolean z, boolean z2, boolean z3, boolean z4, int[] iArr, int i4, boolean z5, boolean z6, boolean z7, int i5, String str2) {
        this.mPrepared = false;
        this.mShouldBeSealed = false;
        this.mSessionErrorCode = 0;
        this.mDestroyed = false;
        Handler.Callback callback = new Handler.Callback() { // from class: com.android.server.pm.PackageInstallerSession.4
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        PackageInstallerSession.this.handleSessionSealed();
                        break;
                    case 2:
                        PackageInstallerSession.this.handleStreamValidateAndCommit();
                        break;
                    case 3:
                        PackageInstallerSession.this.handleInstall();
                        break;
                    case 4:
                        SomeArgs someArgs = (SomeArgs) message.obj;
                        String str3 = (String) someArgs.arg1;
                        String str4 = (String) someArgs.arg2;
                        Bundle bundle = (Bundle) someArgs.arg3;
                        IntentSender intentSender = (IntentSender) someArgs.arg4;
                        int i6 = someArgs.argi1;
                        boolean z8 = someArgs.argi2 == 1;
                        someArgs.recycle();
                        Context context2 = PackageInstallerSession.this.mContext;
                        PackageInstallerSession packageInstallerSession = PackageInstallerSession.this;
                        PackageInstallerSession.sendOnPackageInstalled(context2, intentSender, packageInstallerSession.sessionId, packageInstallerSession.isInstallerDeviceOwnerOrAffiliatedProfileOwner(), PackageInstallerSession.this.userId, str3, i6, z8, str4, bundle);
                        break;
                    case 5:
                        PackageInstallerSession.this.onSessionValidationFailure(message.arg1, (String) message.obj);
                        break;
                    case 6:
                        PackageInstallerSession.this.handlePreapprovalRequest();
                        break;
                }
                PackageInstallerSession packageInstallerSession2 = PackageInstallerSession.this;
                packageInstallerSession2.mPackageInstallerSessionExt.handleInHandlerCallback(message, packageInstallerSession2, packageInstallerSession2.mPm);
                return true;
            }
        };
        this.mHandlerCallback = callback;
        this.mWrapper = new PackageInstallerSessionWrapper();
        this.mCallback = internalCallback;
        this.mContext = context;
        this.mPm = packageManagerService;
        this.mInstaller = packageManagerService != null ? packageManagerService.mInstaller : null;
        this.mSessionProvider = packageSessionProvider;
        this.mSilentUpdatePolicy = silentUpdatePolicy;
        this.mHandler = new Handler(looper, callback);
        this.mStagingManager = stagingManager;
        this.sessionId = i;
        this.userId = i2;
        this.mOriginalInstallerUid = i3;
        this.mInstallerUid = i3;
        Objects.requireNonNull(installSource);
        this.mInstallSource = installSource;
        this.mOriginalInstallerPackageName = installSource.mInstallerPackageName;
        this.params = sessionParams;
        this.createdMillis = j;
        this.updatedMillis = j;
        this.committedMillis = j2;
        this.stageDir = file;
        this.stageCid = str;
        this.mShouldBeSealed = z4;
        if (iArr != null) {
            for (int i6 : iArr) {
                this.mChildSessions.put(i6, null);
            }
        }
        this.mParentSessionId = i4;
        if (installationFileArr != null) {
            this.mFiles.ensureCapacity(installationFileArr.length);
            int length = installationFileArr.length;
            for (int i7 = 0; i7 < length; i7++) {
                if (!this.mFiles.add(new FileEntry(i7, installationFileArr[i7]))) {
                    throw new IllegalArgumentException("Trying to add a duplicate installation file");
                }
            }
        }
        if (arrayMap != null) {
            this.mChecksums.putAll((ArrayMap<? extends String, ? extends PerFileChecksum>) arrayMap);
        }
        if (!sessionParams.isMultiPackage) {
            if ((file == null) == (str == null)) {
                throw new IllegalArgumentException("Exactly one of stageDir or stageCid stage must be set");
            }
        }
        this.mPrepared = z;
        this.mCommitted.set(z2);
        this.mDestroyed = z3;
        this.mSessionReady = z5;
        this.mSessionApplied = z7;
        this.mSessionFailed = z6;
        this.mSessionErrorCode = i5;
        this.mSessionErrorMessage = str2 != null ? str2 : "";
        this.mStagedSession = sessionParams.isStaged ? new StagedSession() : null;
        if (isDataLoaderInstallation()) {
            if (isApexSession()) {
                throw new IllegalArgumentException("DataLoader installation of APEX modules is not allowed.");
            }
            if (isSystemDataLoaderInstallation() && this.mContext.checkCallingOrSelfPermission("com.android.permission.USE_SYSTEM_DATA_LOADERS") != 0) {
                throw new SecurityException("You need the com.android.permission.USE_SYSTEM_DATA_LOADERS permission to use system data loaders");
            }
        }
        if (isIncrementalInstallation() && !IncrementalManager.isAllowed()) {
            throw new IllegalArgumentException("Incremental installation not allowed.");
        }
    }

    private boolean shouldScrubData(int i) {
        return i >= 10000 && getInstallerUid() != i;
    }

    public PackageInstaller.SessionInfo generateInfoForCaller(boolean z, int i) {
        return generateInfoInternal(z, shouldScrubData(i));
    }

    public PackageInstaller.SessionInfo generateInfoScrubbed(boolean z) {
        return generateInfoInternal(z, true);
    }

    private PackageInstaller.SessionInfo generateInfoInternal(boolean z, boolean z2) {
        float f;
        String str;
        PackageInstaller.SessionInfo sessionInfo = new PackageInstaller.SessionInfo();
        synchronized (this.mProgressLock) {
            f = this.mProgress;
        }
        synchronized (this.mLock) {
            sessionInfo.sessionId = this.sessionId;
            sessionInfo.userId = this.userId;
            InstallSource installSource = this.mInstallSource;
            sessionInfo.installerPackageName = installSource.mInstallerPackageName;
            sessionInfo.installerAttributionTag = installSource.mInstallerAttributionTag;
            sessionInfo.resolvedBaseCodePath = null;
            if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_INSTALLED_SESSION_PATHS") == 0) {
                File file = this.mResolvedBaseFile;
                if (file == null) {
                    List<File> addedApksLocked = getAddedApksLocked();
                    if (addedApksLocked.size() > 0) {
                        file = addedApksLocked.get(0);
                    }
                }
                if (file != null) {
                    sessionInfo.resolvedBaseCodePath = file.getAbsolutePath();
                }
            }
            sessionInfo.progress = f;
            sessionInfo.sealed = this.mSealed;
            sessionInfo.isCommitted = isCommitted();
            sessionInfo.isPreapprovalRequested = isPreapprovalRequested();
            sessionInfo.active = this.mActiveCount.get() > 0;
            PackageInstaller.SessionParams sessionParams = this.params;
            sessionInfo.mode = sessionParams.mode;
            sessionInfo.installReason = sessionParams.installReason;
            sessionInfo.installScenario = sessionParams.installScenario;
            sessionInfo.sizeBytes = sessionParams.sizeBytes;
            PackageInstaller.PreapprovalDetails preapprovalDetails = this.mPreapprovalDetails;
            if (preapprovalDetails != null) {
                str = preapprovalDetails.getPackageName();
            } else {
                String str2 = this.mPackageName;
                str = str2 != null ? str2 : sessionParams.appPackageName;
            }
            sessionInfo.appPackageName = str;
            if (z) {
                PackageInstaller.PreapprovalDetails preapprovalDetails2 = this.mPreapprovalDetails;
                sessionInfo.appIcon = (preapprovalDetails2 == null || preapprovalDetails2.getIcon() == null) ? this.params.appIcon : this.mPreapprovalDetails.getIcon();
            }
            PackageInstaller.PreapprovalDetails preapprovalDetails3 = this.mPreapprovalDetails;
            sessionInfo.appLabel = preapprovalDetails3 != null ? preapprovalDetails3.getLabel() : this.params.appLabel;
            PackageInstaller.SessionParams sessionParams2 = this.params;
            sessionInfo.installLocation = sessionParams2.installLocation;
            if (!z2) {
                sessionInfo.originatingUri = sessionParams2.originatingUri;
            }
            sessionInfo.originatingUid = sessionParams2.originatingUid;
            if (!z2) {
                sessionInfo.referrerUri = sessionParams2.referrerUri;
            }
            sessionInfo.grantedRuntimePermissions = sessionParams2.getLegacyGrantedRuntimePermissions();
            PackageInstaller.SessionParams sessionParams3 = this.params;
            sessionInfo.whitelistedRestrictedPermissions = sessionParams3.whitelistedRestrictedPermissions;
            sessionInfo.autoRevokePermissionsMode = sessionParams3.autoRevokePermissionsMode;
            sessionInfo.installFlags = sessionParams3.installFlags;
            sessionInfo.isMultiPackage = sessionParams3.isMultiPackage;
            sessionInfo.isStaged = sessionParams3.isStaged;
            sessionInfo.rollbackDataPolicy = sessionParams3.rollbackDataPolicy;
            sessionInfo.parentSessionId = this.mParentSessionId;
            sessionInfo.childSessionIds = getChildSessionIdsLocked();
            sessionInfo.isSessionApplied = this.mSessionApplied;
            sessionInfo.isSessionReady = this.mSessionReady;
            sessionInfo.isSessionFailed = this.mSessionFailed;
            sessionInfo.setSessionErrorCode(this.mSessionErrorCode, this.mSessionErrorMessage);
            sessionInfo.createdMillis = this.createdMillis;
            sessionInfo.updatedMillis = this.updatedMillis;
            sessionInfo.requireUserAction = this.params.requireUserAction;
            sessionInfo.installerUid = this.mInstallerUid;
            PackageInstaller.SessionParams sessionParams4 = this.params;
            sessionInfo.packageSource = sessionParams4.packageSource;
            sessionInfo.applicationEnabledSettingPersistent = sessionParams4.applicationEnabledSettingPersistent;
            sessionInfo.pendingUserActionReason = userActionRequirementToReason(this.mUserActionRequirement);
        }
        return sessionInfo;
    }

    public boolean isPrepared() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPrepared;
        }
        return z;
    }

    public boolean isSealed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSealed;
        }
        return z;
    }

    boolean isPreapprovalRequested() {
        return this.mPreapprovalRequested.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCommitted() {
        return this.mCommitted.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDestroyed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInTerminalState() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSessionApplied || this.mSessionFailed;
        }
        return z;
    }

    public boolean isStagedAndInTerminalState() {
        return this.params.isStaged && isInTerminalState();
    }

    private void assertNotLocked(String str) {
        if (Thread.holdsLock(this.mLock)) {
            throw new IllegalStateException(str + " is holding mLock");
        }
    }

    private void assertSealed(String str) {
        if (isSealed()) {
            return;
        }
        throw new IllegalStateException(str + " before sealing");
    }

    @GuardedBy({"mLock"})
    private void assertPreparedAndNotPreapprovalRequestedLocked(String str) {
        assertPreparedAndNotSealedLocked(str);
        if (isPreapprovalRequested()) {
            throw new IllegalStateException(str + " not allowed after requesting");
        }
    }

    @GuardedBy({"mLock"})
    private void assertPreparedAndNotSealedLocked(String str) {
        assertPreparedAndNotCommittedOrDestroyedLocked(str);
        if (this.mSealed) {
            throw new SecurityException(str + " not allowed after sealing");
        }
    }

    @GuardedBy({"mLock"})
    private void assertPreparedAndNotCommittedOrDestroyedLocked(String str) {
        assertPreparedAndNotDestroyedLocked(str);
        if (isCommitted()) {
            throw new SecurityException(str + " not allowed after commit");
        }
    }

    @GuardedBy({"mLock"})
    private void assertPreparedAndNotDestroyedLocked(String str) {
        if (!this.mPrepared) {
            throw new IllegalStateException(str + " before prepared");
        }
        if (this.mDestroyed) {
            throw new SecurityException(str + " not allowed after destruction");
        }
    }

    @GuardedBy({"mProgressLock"})
    private void setClientProgressLocked(float f) {
        boolean z = this.mClientProgress == 0.0f;
        this.mClientProgress = f;
        computeProgressLocked(z);
    }

    public void setClientProgress(float f) {
        assertCallerIsOwnerOrRoot();
        synchronized (this.mProgressLock) {
            setClientProgressLocked(f);
        }
    }

    public void addClientProgress(float f) {
        assertCallerIsOwnerOrRoot();
        synchronized (this.mProgressLock) {
            setClientProgressLocked(this.mClientProgress + f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mProgressLock"})
    public void computeProgressLocked(boolean z) {
        if (!isIncrementalInstallation() || !isCommitted()) {
            this.mProgress = MathUtils.constrain(this.mClientProgress * 0.8f, 0.0f, 0.8f) + MathUtils.constrain(this.mInternalProgress * 0.2f, 0.0f, 0.2f);
        } else {
            float f = this.mIncrementalProgress;
            if (f - this.mProgress >= 0.01d) {
                this.mProgress = f;
            }
        }
        if (z || this.mProgress - this.mReportedProgress >= 0.01d) {
            float f2 = this.mProgress;
            this.mReportedProgress = f2;
            this.mCallback.onSessionProgressChanged(this, f2);
        }
    }

    public String[] getNames() {
        assertCallerIsOwnerRootOrVerifier();
        synchronized (this.mLock) {
            assertPreparedAndNotDestroyedLocked("getNames");
            if (!isCommitted()) {
                return getNamesLocked();
            }
            return getStageDirContentsLocked();
        }
    }

    @GuardedBy({"mLock"})
    private String[] getStageDirContentsLocked() {
        File file = this.stageDir;
        if (file == null) {
            return EmptyArray.STRING;
        }
        String[] list = file.list();
        return list == null ? EmptyArray.STRING : list;
    }

    @GuardedBy({"mLock"})
    private String[] getNamesLocked() {
        if (!isDataLoaderInstallation()) {
            return getStageDirContentsLocked();
        }
        InstallationFile[] installationFilesLocked = getInstallationFilesLocked();
        String[] strArr = new String[installationFilesLocked.length];
        int length = installationFilesLocked.length;
        for (int i = 0; i < length; i++) {
            strArr[i] = installationFilesLocked[i].getName();
        }
        return strArr;
    }

    @GuardedBy({"mLock"})
    private InstallationFile[] getInstallationFilesLocked() {
        InstallationFile[] installationFileArr = new InstallationFile[this.mFiles.size()];
        Iterator<FileEntry> it = this.mFiles.iterator();
        while (it.hasNext()) {
            FileEntry next = it.next();
            installationFileArr[next.getIndex()] = next.getFile();
        }
        return installationFileArr;
    }

    private static ArrayList<File> filterFiles(File file, String[] strArr, FileFilter fileFilter) {
        ArrayList<File> arrayList = new ArrayList<>(strArr.length);
        for (String str : strArr) {
            File file2 = new File(file, str);
            if (fileFilter.accept(file2)) {
                arrayList.add(file2);
            }
        }
        return arrayList;
    }

    @GuardedBy({"mLock"})
    private List<File> getAddedApksLocked() {
        return filterFiles(this.stageDir, getNamesLocked(), sAddedApkFilter);
    }

    @GuardedBy({"mLock"})
    private List<File> getRemovedFilesLocked() {
        return filterFiles(this.stageDir, getNamesLocked(), sRemovedFilter);
    }

    public void setChecksums(String str, Checksum[] checksumArr, byte[] bArr) {
        if (checksumArr.length == 0) {
            return;
        }
        String str2 = getInstallSource().mInitiatingPackageName;
        if (PackageManagerServiceUtils.isInstalledByAdb(str2)) {
            str2 = getInstallSource().mInstallerPackageName;
        }
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalStateException("Installer package is empty.");
        }
        ((AppOpsManager) this.mContext.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str2);
        if (((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getPackage(str2) == null) {
            throw new IllegalStateException("Can't obtain calling installer's package.");
        }
        if (bArr != null && bArr.length != 0) {
            try {
                ApkChecksums.verifySignature(checksumArr, bArr);
            } catch (IOException | NoSuchAlgorithmException | SignatureException e) {
                throw new IllegalArgumentException("Can't verify signature", e);
            }
        }
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("addChecksums");
            if (this.mChecksums.containsKey(str)) {
                throw new IllegalStateException("Duplicate checksums.");
            }
            this.mChecksums.put(str, new PerFileChecksum(checksumArr, bArr));
        }
    }

    public void requestChecksums(String str, int i, int i2, List list, IOnChecksumsReadyListener iOnChecksumsReadyListener) {
        String str2;
        assertCallerIsOwnerRootOrVerifier();
        File file = new File(this.stageDir, str);
        if (PackageManagerServiceUtils.isInstalledByAdb(getInstallSource().mInitiatingPackageName)) {
            str2 = getInstallSource().mInstallerPackageName;
        } else {
            str2 = getInstallSource().mInitiatingPackageName;
        }
        try {
            this.mPm.requestFileChecksums(file, str2, i, i2, list, iOnChecksumsReadyListener);
        } catch (FileNotFoundException e) {
            throw new ParcelableException(e);
        }
    }

    public void removeSplit(String str) {
        if (isDataLoaderInstallation()) {
            throw new IllegalStateException("Cannot remove splits in a data loader installation session.");
        }
        if (TextUtils.isEmpty(this.params.appPackageName)) {
            throw new IllegalStateException("Must specify package name to remove a split");
        }
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("removeSplit");
            try {
                createRemoveSplitMarkerLocked(str);
            } catch (IOException e) {
                throw ExceptionUtils.wrap(e);
            }
        }
    }

    private static String getRemoveMarkerName(String str) {
        String str2 = str + REMOVE_MARKER_EXTENSION;
        if (FileUtils.isValidExtFilename(str2)) {
            return str2;
        }
        throw new IllegalArgumentException("Invalid marker: " + str2);
    }

    @GuardedBy({"mLock"})
    private void createRemoveSplitMarkerLocked(String str) throws IOException {
        try {
            File file = new File(this.stageDir, getRemoveMarkerName(str));
            file.createNewFile();
            Os.chmod(file.getAbsolutePath(), 0);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    private void assertShellOrSystemCalling(String str) {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 0 || callingUid == 1000 || callingUid == 2000) {
            return;
        }
        this.mPackageInstallerSessionExt.adjustAssertShellOrSystemCallingThrowException(this.mPm, str);
    }

    private void assertCanWrite(boolean z) {
        if (isDataLoaderInstallation()) {
            throw new IllegalStateException("Cannot write regular files in a data loader installation session.");
        }
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotSealedLocked("assertCanWrite");
        }
        if (z) {
            assertShellOrSystemCalling("Reverse mode");
        }
    }

    private File getTmpAppMetadataFile() {
        return new File(Environment.getDataAppDirectory(this.params.volumeUuid), this.sessionId + "-app.metadata");
    }

    private File getStagedAppMetadataFile() {
        File file = new File(this.stageDir, "app.metadata");
        if (file.exists()) {
            return file;
        }
        return null;
    }

    private static boolean isAppMetadata(String str) {
        return str.endsWith("app.metadata");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAppMetadata(File file) {
        return isAppMetadata(file.getName());
    }

    public ParcelFileDescriptor getAppMetadataFd() {
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("getAppMetadataFd");
            if (getStagedAppMetadataFile() == null) {
                return null;
            }
            try {
                return openReadInternalLocked("app.metadata");
            } catch (IOException e) {
                throw ExceptionUtils.wrap(e);
            }
        }
    }

    public void removeAppMetadata() {
        File stagedAppMetadataFile = getStagedAppMetadataFile();
        if (stagedAppMetadataFile != null) {
            stagedAppMetadataFile.delete();
        }
    }

    private static long getAppMetadataSizeLimit() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return DeviceConfig.getLong("package_manager_service", PROPERTY_APP_METADATA_BYTE_SIZE_LIMIT, DEFAULT_APP_METADATA_BYTE_SIZE_LIMIT);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public ParcelFileDescriptor openWriteAppMetadata() {
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotSealedLocked("openWriteAppMetadata");
        }
        try {
            return doWriteInternal("app.metadata", 0L, -1L, null);
        } catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public ParcelFileDescriptor openWrite(String str, long j, long j2) {
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d(TAG, "openWrite: " + str + ", " + j + ", " + j2);
        }
        assertCanWrite(false);
        try {
            this.mPackageInstallerSessionSocExt.boostBeforeOpenWrite();
            return this.mPackageInstallerSessionExt.adjustResultInOpenWrite(doWriteInternal(str, j, j2, null), str, this.mHandler, this.mPm, this.mInstallerUid, this.stageDir);
        } catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public void write(String str, long j, long j2, ParcelFileDescriptor parcelFileDescriptor) {
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d(TAG, "write: " + str + ", " + j + ", " + j2);
        }
        assertCanWrite(parcelFileDescriptor != null);
        try {
            doWriteInternal(str, j, j2, parcelFileDescriptor);
            this.mPackageInstallerSessionExt.afterWrite(str, this.mHandler, this.mPm, this.mInstallerUid, this.stageDir);
        } catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public void stageViaHardLink(String str) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("link() can only be run by the system");
        }
        File file = new File(this.stageDir, new File(str).getName());
        String absolutePath = file.getAbsolutePath();
        try {
            try {
                Os.link(str, absolutePath);
                Os.chmod(absolutePath, 420);
            } catch (ErrnoException e) {
                e.rethrowAsIOException();
            }
            if (SELinux.restorecon(file)) {
                return;
            }
            throw new IOException("Can't relabel file: " + file);
        } catch (IOException e2) {
            try {
                Os.unlink(absolutePath);
            } catch (Exception unused) {
                Slog.d(TAG, "Failed to unlink session file: " + absolutePath);
            }
            throw ExceptionUtils.wrap(e2);
        }
    }

    private ParcelFileDescriptor openTargetInternal(String str, int i, int i2) throws IOException, ErrnoException {
        return new ParcelFileDescriptor(Os.open(str, i, i2));
    }

    private ParcelFileDescriptor createRevocableFdInternal(RevocableFileDescriptor revocableFileDescriptor, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        int detachFd = parcelFileDescriptor.detachFd();
        FileDescriptor fileDescriptor = new FileDescriptor();
        fileDescriptor.setInt$(detachFd);
        revocableFileDescriptor.init(this.mContext, fileDescriptor);
        return revocableFileDescriptor.getRevocableFileDescriptor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ParcelFileDescriptor doWriteInternal(String str, long j, long j2, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        FileBridge fileBridge;
        RevocableFileDescriptor revocableFileDescriptor;
        synchronized (this.mLock) {
            if (PackageInstaller.ENABLE_REVOCABLE_FD) {
                RevocableFileDescriptor revocableFileDescriptor2 = new RevocableFileDescriptor();
                this.mFds.add(revocableFileDescriptor2);
                revocableFileDescriptor = revocableFileDescriptor2;
                fileBridge = null;
            } else {
                FileBridge fileBridge2 = new FileBridge();
                this.mBridges.add(fileBridge2);
                fileBridge = fileBridge2;
                revocableFileDescriptor = null;
            }
        }
        try {
            if (!FileUtils.isValidExtFilename(str)) {
                throw new IllegalArgumentException("Invalid name: " + str);
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                File file = new File(this.stageDir, str);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                this.mPackageInstallerSessionExt.beforeOpenInDoWriteInternal(file);
                int i = str.equals("app.metadata") ? APP_METADATA_FILE_ACCESS_MODE : 420;
                ParcelFileDescriptor openTargetInternal = openTargetInternal(file.getAbsolutePath(), OsConstants.O_CREAT | OsConstants.O_WRONLY, i);
                Os.chmod(file.getAbsolutePath(), i);
                if (this.stageDir != null && j2 > 0) {
                    ((StorageManager) this.mContext.getSystemService(StorageManager.class)).allocateBytes(openTargetInternal.getFileDescriptor(), j2, InstallLocationUtils.translateAllocateFlags(this.params.installFlags));
                }
                if (j > 0) {
                    Os.lseek(openTargetInternal.getFileDescriptor(), j, OsConstants.SEEK_SET);
                }
                if (parcelFileDescriptor != null) {
                    try {
                        final Int64Ref int64Ref = new Int64Ref(0L);
                        FileUtils.copy(parcelFileDescriptor.getFileDescriptor(), openTargetInternal.getFileDescriptor(), j2, null, new SystemServerInitThreadPool$.ExternalSyntheticLambda1(), new FileUtils.ProgressListener() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda6
                            @Override // android.os.FileUtils.ProgressListener
                            public final void onProgress(long j3) {
                                PackageInstallerSession.this.lambda$doWriteInternal$0(int64Ref, j3);
                            }
                        });
                        IoUtils.closeQuietly(openTargetInternal);
                        IoUtils.closeQuietly(parcelFileDescriptor);
                        synchronized (this.mLock) {
                            if (PackageInstaller.ENABLE_REVOCABLE_FD) {
                                this.mFds.remove(revocableFileDescriptor);
                            } else {
                                fileBridge.forceClose();
                                this.mBridges.remove(fileBridge);
                            }
                        }
                        return null;
                    } catch (Throwable th) {
                        IoUtils.closeQuietly(openTargetInternal);
                        IoUtils.closeQuietly(parcelFileDescriptor);
                        synchronized (this.mLock) {
                            if (PackageInstaller.ENABLE_REVOCABLE_FD) {
                                this.mFds.remove(revocableFileDescriptor);
                            } else {
                                fileBridge.forceClose();
                                this.mBridges.remove(fileBridge);
                            }
                            throw th;
                        }
                    }
                }
                if (PackageInstaller.ENABLE_REVOCABLE_FD) {
                    return createRevocableFdInternal(revocableFileDescriptor, openTargetInternal);
                }
                fileBridge.setTargetFile(openTargetInternal);
                fileBridge.start();
                return fileBridge.getClientSocket();
            } catch (Throwable th2) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th2;
            }
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doWriteInternal$0(Int64Ref int64Ref, long j) {
        if (this.params.sizeBytes > 0) {
            long j2 = j - int64Ref.value;
            int64Ref.value = j;
            synchronized (this.mProgressLock) {
                setClientProgressLocked(this.mClientProgress + (((float) j2) / ((float) this.params.sizeBytes)));
            }
        }
    }

    public ParcelFileDescriptor openRead(String str) {
        ParcelFileDescriptor openReadInternalLocked;
        if (isDataLoaderInstallation()) {
            throw new IllegalStateException("Cannot read regular files in a data loader installation session.");
        }
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("openRead");
            try {
                openReadInternalLocked = openReadInternalLocked(str);
            } catch (IOException e) {
                throw ExceptionUtils.wrap(e);
            }
        }
        return openReadInternalLocked;
    }

    @GuardedBy({"mLock"})
    private ParcelFileDescriptor openReadInternalLocked(String str) throws IOException {
        try {
            if (!FileUtils.isValidExtFilename(str)) {
                throw new IllegalArgumentException("Invalid name: " + str);
            }
            return new ParcelFileDescriptor(Os.open(new File(this.stageDir, str).getAbsolutePath(), OsConstants.O_RDONLY, 0));
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    private void assertCallerIsOwnerRootOrVerifier() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 0 || callingUid == this.mInstallerUid) {
            return;
        }
        if (isSealed() && this.mContext.checkCallingOrSelfPermission("android.permission.PACKAGE_VERIFICATION_AGENT") == 0) {
            return;
        }
        throw new SecurityException("Session does not belong to uid " + callingUid);
    }

    private void assertCallerIsOwnerOrRoot() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 0 || callingUid == this.mInstallerUid) {
            return;
        }
        throw new SecurityException("Session does not belong to uid " + callingUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertCallerIsOwnerOrRootOrSystem() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 0 || callingUid == this.mInstallerUid || callingUid == 1000) {
            return;
        }
        throw new SecurityException("Session does not belong to uid " + callingUid);
    }

    @GuardedBy({"mLock"})
    private void assertNoWriteFileTransfersOpenLocked() {
        Iterator<RevocableFileDescriptor> it = this.mFds.iterator();
        while (it.hasNext()) {
            if (!it.next().isRevoked()) {
                throw new SecurityException("Files still open");
            }
        }
        Iterator<FileBridge> it2 = this.mBridges.iterator();
        while (it2.hasNext()) {
            if (!it2.next().isClosed()) {
                throw new SecurityException("Files still open");
            }
        }
    }

    public void commit(IntentSender intentSender, boolean z) {
        assertNotChild("commit");
        this.mPackageInstallerSessionExt.beforeSessionCommit(this);
        this.mPackageInstallerSessionSocExt.boostBeforeCommit();
        if (markAsSealed(intentSender, z)) {
            if (isMultiPackage()) {
                synchronized (this.mLock) {
                    boolean z2 = false;
                    for (int size = this.mChildSessions.size() - 1; size >= 0; size--) {
                        if (!this.mChildSessions.valueAt(size).markAsSealed(null, z)) {
                            z2 = true;
                        }
                    }
                    if (z2) {
                        return;
                    }
                }
            }
            File stagedAppMetadataFile = getStagedAppMetadataFile();
            if (stagedAppMetadataFile != null) {
                long appMetadataSizeLimit = getAppMetadataSizeLimit();
                if (stagedAppMetadataFile.length() > appMetadataSizeLimit) {
                    stagedAppMetadataFile.delete();
                    throw new IllegalArgumentException("App metadata size exceeds the maximum allowed limit of " + appMetadataSizeLimit);
                }
                if (isIncrementalInstallation()) {
                    stagedAppMetadataFile.renameTo(getTmpAppMetadataFile());
                }
            }
            dispatchSessionSealed();
        }
    }

    public void seal() {
        assertNotChild("seal");
        assertCallerIsOwnerOrRoot();
        try {
            sealInternal();
            Iterator<PackageInstallerSession> it = getChildSessions().iterator();
            while (it.hasNext()) {
                it.next().sealInternal();
            }
        } catch (PackageManagerException e) {
            throw new IllegalStateException("Package is not valid", e);
        }
    }

    private void sealInternal() throws PackageManagerException {
        synchronized (this.mLock) {
            sealLocked();
        }
    }

    public List<String> fetchPackageNames() {
        assertNotChild("fetchPackageNames");
        assertCallerIsOwnerOrRoot();
        List<PackageInstallerSession> selfOrChildSessions = getSelfOrChildSessions();
        ArrayList arrayList = new ArrayList(selfOrChildSessions.size());
        Iterator<PackageInstallerSession> it = selfOrChildSessions.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().fetchPackageName());
        }
        return arrayList;
    }

    private String fetchPackageName() {
        String packageName;
        assertSealed("fetchPackageName");
        synchronized (this.mLock) {
            ParseTypeImpl forDefaultParsing = ParseTypeImpl.forDefaultParsing();
            Iterator<File> it = getAddedApksLocked().iterator();
            while (it.hasNext()) {
                ParseResult parseApkLite = ApkLiteParseUtils.parseApkLite(forDefaultParsing.reset(), it.next(), 0);
                if (parseApkLite.isError()) {
                    throw new IllegalStateException("Can't parse package for session=" + this.sessionId, parseApkLite.getException());
                }
                packageName = ((ApkLite) parseApkLite.getResult()).getPackageName();
                if (packageName != null) {
                }
            }
            throw new IllegalStateException("Can't fetch package name for session=" + this.sessionId);
        }
        return packageName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchSessionSealed() {
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSessionSealed() {
        assertSealed("dispatchSessionSealed");
        this.mCallback.onSessionSealedBlocking(this);
        dispatchStreamValidateAndCommit();
    }

    private void dispatchStreamValidateAndCommit() {
        this.mHandler.obtainMessage(2).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStreamValidateAndCommit() {
        this.mPackageInstallerSessionExt.beforeHandleStreamValidateAndCommit();
        try {
            this.mPackageInstallerSessionExt.checkMainlineLimited(this);
            Iterator<PackageInstallerSession> it = getChildSessions().iterator();
            boolean z = true;
            while (it.hasNext()) {
                z &= it.next().streamValidateAndCommit();
            }
            if (z && streamValidateAndCommit()) {
                this.mHandler.obtainMessage(3).sendToTarget();
            }
        } catch (PackageManagerException e) {
            destroy();
            String completeMessage = ExceptionUtils.getCompleteMessage(e);
            dispatchSessionFinished(e.error, completeMessage, null);
            maybeFinishChildSessions(e.error, completeMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePreapprovalRequest() {
        if (sendPendingUserActionIntentIfNeeded()) {
            return;
        }
        dispatchSessionPreappoved();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class FileSystemConnector extends IPackageInstallerSessionFileSystemConnector.Stub {
        final Set<String> mAddedFiles = new ArraySet();

        FileSystemConnector(List<InstallationFileParcel> list) {
            Iterator<InstallationFileParcel> it = list.iterator();
            while (it.hasNext()) {
                this.mAddedFiles.add(it.next().name);
            }
        }

        public void writeData(String str, long j, long j2, ParcelFileDescriptor parcelFileDescriptor) {
            if (parcelFileDescriptor == null) {
                throw new IllegalArgumentException("incomingFd can't be null");
            }
            if (!this.mAddedFiles.contains(str)) {
                throw new SecurityException("File name is not in the list of added files.");
            }
            try {
                PackageInstallerSession.this.doWriteInternal(str, j, j2, parcelFileDescriptor);
            } catch (IOException e) {
                throw ExceptionUtils.wrap(e);
            }
        }
    }

    private static boolean isSecureFrpInstallAllowed(Context context, int i) {
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        String[] knownPackageNames = packageManagerInternal.getKnownPackageNames(2, 0);
        AndroidPackage androidPackage = packageManagerInternal.getPackage(i);
        return (androidPackage == null || !ArrayUtils.contains(knownPackageNames, androidPackage.getPackageName())) && context.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") == 0;
    }

    private static boolean isIncrementalInstallationAllowed(String str) {
        PackageStateInternal packageStateInternal = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getPackageStateInternal(str);
        if (packageStateInternal == null || packageStateInternal.getPkg() == null) {
            return true;
        }
        return (packageStateInternal.isSystem() || packageStateInternal.isUpdatedSystemApp()) ? false : true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x005e, code lost:
    
        r5.mContext.enforceCallingOrSelfPermission("android.permission.INSTALL_PACKAGES", null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x006a, code lost:
    
        if (r5.mInstallerUid == r5.mOriginalInstallerUid) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0074, code lost:
    
        throw new java.lang.IllegalArgumentException("Session has not been transferred");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean markAsSealed(IntentSender intentSender, boolean z) {
        Preconditions.checkState(intentSender != null || hasParentSessionId(), "statusReceiver can't be null for the root session");
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotDestroyedLocked("commit of session " + this.sessionId);
            assertNoWriteFileTransfersOpenLocked();
            if ((Settings.Global.getInt(this.mContext.getContentResolver(), "secure_frp_mode", 0) == 1) && !isSecureFrpInstallAllowed(this.mContext, Binder.getCallingUid())) {
                throw new SecurityException("Can't install packages while in secure FRP");
            }
            if (this.mInstallerUid != this.mOriginalInstallerUid) {
                throw new IllegalArgumentException("Session has been transferred");
            }
            setRemoteStatusReceiver(intentSender);
            if (this.mSealed) {
                return true;
            }
            try {
                sealLocked();
                return true;
            } catch (PackageManagerException unused) {
                return false;
            }
        }
    }

    private boolean streamValidateAndCommit() throws PackageManagerException {
        try {
            synchronized (this.mLock) {
                if (isCommitted()) {
                    return true;
                }
                if (!this.params.isMultiPackage) {
                    if (!prepareDataLoaderLocked()) {
                        return false;
                    }
                    if (isApexSession()) {
                        validateApexInstallLocked();
                    } else {
                        validateApkInstallLocked();
                    }
                }
                if (this.mDestroyed) {
                    throw new PackageManagerException(-110, "Session destroyed");
                }
                if (!isIncrementalInstallation()) {
                    synchronized (this.mProgressLock) {
                        this.mClientProgress = 1.0f;
                        computeProgressLocked(true);
                    }
                }
                this.mActiveCount.incrementAndGet();
                if (!this.mCommitted.compareAndSet(false, true)) {
                    throw new PackageManagerException(-110, TextUtils.formatSimple("The mCommitted of session %d should be false originally", new Object[]{Integer.valueOf(this.sessionId)}));
                }
                this.committedMillis = System.currentTimeMillis();
                return true;
            }
        } catch (PackageManagerException e) {
            throw e;
        } catch (Throwable th) {
            throw new PackageManagerException(th);
        }
    }

    @GuardedBy({"mLock"})
    private List<PackageInstallerSession> getChildSessionsLocked() {
        List<PackageInstallerSession> list = Collections.EMPTY_LIST;
        if (!isMultiPackage()) {
            return list;
        }
        int size = this.mChildSessions.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(this.mChildSessions.valueAt(i));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<PackageInstallerSession> getChildSessions() {
        List<PackageInstallerSession> childSessionsLocked;
        synchronized (this.mLock) {
            childSessionsLocked = getChildSessionsLocked();
        }
        return childSessionsLocked;
    }

    private List<PackageInstallerSession> getSelfOrChildSessions() {
        return isMultiPackage() ? getChildSessions() : Collections.singletonList(this);
    }

    @GuardedBy({"mLock"})
    private void sealLocked() throws PackageManagerException {
        try {
            assertNoWriteFileTransfersOpenLocked();
            assertPreparedAndNotDestroyedLocked("sealing of session " + this.sessionId);
            this.mSealed = true;
        } catch (Throwable th) {
            throw onSessionValidationFailure(new PackageManagerException(th));
        }
    }

    private PackageManagerException onSessionValidationFailure(PackageManagerException packageManagerException) {
        onSessionValidationFailure(packageManagerException.error, ExceptionUtils.getCompleteMessage(packageManagerException));
        return packageManagerException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSessionValidationFailure(int i, String str) {
        destroyInternal();
        dispatchSessionFinished(i, str, null);
    }

    private void onSessionVerificationFailure(int i, String str) {
        Slog.e(TAG, "Failed to verify session " + this.sessionId);
        dispatchSessionFinished(i, str, null);
        maybeFinishChildSessions(i, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSystemDataLoaderUnrecoverable() {
        final DeletePackageHelper deletePackageHelper = new DeletePackageHelper(this.mPm);
        final String packageName = getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                PackageInstallerSession.lambda$onSystemDataLoaderUnrecoverable$1(DeletePackageHelper.this, packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onSystemDataLoaderUnrecoverable$1(DeletePackageHelper deletePackageHelper, String str) {
        if (deletePackageHelper.deletePackageX(str, -1L, 0, 2, true) != 1) {
            Slog.e(TAG, "Failed to uninstall package with failed dataloader: " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAfterSessionRead(SparseArray<PackageInstallerSession> sparseArray) {
        synchronized (this.mLock) {
            for (int size = this.mChildSessions.size() - 1; size >= 0; size--) {
                int keyAt = this.mChildSessions.keyAt(size);
                PackageInstallerSession packageInstallerSession = sparseArray.get(keyAt);
                if (packageInstallerSession != null) {
                    this.mChildSessions.setValueAt(size, packageInstallerSession);
                } else {
                    Slog.e(TAG, "Child session not existed: " + keyAt);
                    this.mChildSessions.removeAt(size);
                }
            }
            if (!this.mShouldBeSealed || isStagedAndInTerminalState()) {
                return;
            }
            try {
                sealLocked();
            } catch (PackageManagerException e) {
                Slog.e(TAG, "Package not valid", e);
            }
            if (!isMultiPackage() && isStaged() && isCommitted()) {
                PackageInstallerSession packageInstallerSession2 = hasParentSessionId() ? sparseArray.get(getParentSessionId()) : this;
                if (packageInstallerSession2 != null && !packageInstallerSession2.isStagedAndInTerminalState()) {
                    if (isApexSession()) {
                        validateApexInstallLocked();
                    } else {
                        validateApkInstallLocked();
                    }
                }
            }
        }
    }

    public void markUpdated() {
        synchronized (this.mLock) {
            this.updatedMillis = System.currentTimeMillis();
        }
    }

    public void transfer(String str) {
        Preconditions.checkArgument(!TextUtils.isEmpty(str));
        Computer snapshotComputer = this.mPm.snapshotComputer();
        ApplicationInfo applicationInfo = snapshotComputer.getApplicationInfo(str, 0L, this.userId);
        if (applicationInfo == null) {
            throw new ParcelableException(new PackageManager.NameNotFoundException(str));
        }
        if (snapshotComputer.checkUidPermission("android.permission.INSTALL_PACKAGES", applicationInfo.uid) != 0) {
            throw new SecurityException("Destination package " + str + " does not have the android.permission.INSTALL_PACKAGES permission");
        }
        if (!this.params.areHiddenOptionsSet()) {
            throw new SecurityException("Can only transfer sessions that use public options");
        }
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("transfer");
            try {
                sealLocked();
                this.mInstallerUid = applicationInfo.uid;
                this.mInstallSource = InstallSource.create(str, null, str, this.mInstallerUid, str, null, this.params.packageSource);
            } catch (PackageManagerException e) {
                throw new IllegalStateException("Package is not valid", e);
            }
        }
    }

    private static boolean checkUserActionRequirement(PackageInstallerSession packageInstallerSession, IntentSender intentSender) {
        if (packageInstallerSession.isMultiPackage()) {
            return false;
        }
        int computeUserActionRequirement = packageInstallerSession.computeUserActionRequirement();
        packageInstallerSession.updateUserActionRequirement(computeUserActionRequirement);
        if (computeUserActionRequirement == 1 || computeUserActionRequirement == 3) {
            packageInstallerSession.sendPendingUserActionIntent(intentSender);
            return true;
        }
        if (!packageInstallerSession.isApexSession() && computeUserActionRequirement == 2) {
            if (!isTargetSdkConditionSatisfied(packageInstallerSession)) {
                packageInstallerSession.sendPendingUserActionIntent(intentSender);
                return true;
            }
            if (packageInstallerSession.params.requireUserAction == 2) {
                if (!packageInstallerSession.mSilentUpdatePolicy.isSilentUpdateAllowed(packageInstallerSession.getInstallerPackageName(), packageInstallerSession.getPackageName())) {
                    packageInstallerSession.sendPendingUserActionIntent(intentSender);
                    return true;
                }
                packageInstallerSession.mSilentUpdatePolicy.track(packageInstallerSession.getInstallerPackageName(), packageInstallerSession.getPackageName());
            }
        }
        return false;
    }

    private static boolean isTargetSdkConditionSatisfied(PackageInstallerSession packageInstallerSession) {
        int i;
        String str;
        synchronized (packageInstallerSession.mLock) {
            i = packageInstallerSession.mValidatedTargetSdk;
            str = packageInstallerSession.mPackageName;
        }
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.packageName = str;
        applicationInfo.targetSdkVersion = i;
        IPlatformCompat asInterface = IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat"));
        if (i == Integer.MAX_VALUE) {
            return false;
        }
        try {
            return asInterface.isChangeEnabled(SILENT_INSTALL_ALLOWED, applicationInfo);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get a response from PLATFORM_COMPAT_SERVICE", e);
            return false;
        }
    }

    private boolean sendPendingUserActionIntentIfNeeded() {
        if (isCommitted()) {
            assertNotChild("PackageInstallerSession#sendPendingUserActionIntentIfNeeded");
        }
        final IntentSender remoteStatusReceiver = getRemoteStatusReceiver();
        return sessionContains(new Predicate() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$sendPendingUserActionIntentIfNeeded$2;
                lambda$sendPendingUserActionIntentIfNeeded$2 = PackageInstallerSession.lambda$sendPendingUserActionIntentIfNeeded$2(remoteStatusReceiver, (PackageInstallerSession) obj);
                return lambda$sendPendingUserActionIntentIfNeeded$2;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$sendPendingUserActionIntentIfNeeded$2(IntentSender intentSender, PackageInstallerSession packageInstallerSession) {
        return checkUserActionRequirement(packageInstallerSession, intentSender);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInstall() {
        if (isInstallerDeviceOwnerOrAffiliatedProfileOwner()) {
            DevicePolicyEventLogger.createEvent(HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE).setAdmin(getInstallSource().mInstallerPackageName).write();
        }
        boolean sendPendingUserActionIntentIfNeeded = sendPendingUserActionIntentIfNeeded();
        if (this.mUserActionRequired == null) {
            this.mUserActionRequired = Boolean.valueOf(sendPendingUserActionIntentIfNeeded);
        }
        if (sendPendingUserActionIntentIfNeeded) {
            deactivate();
            return;
        }
        if (this.mUserActionRequired.booleanValue()) {
            activate();
        }
        if (this.mVerificationInProgress) {
            Slog.w(TAG, "Verification is already in progress for session " + this.sessionId);
            return;
        }
        this.mVerificationInProgress = true;
        if (this.params.isStaged) {
            this.mStagedSession.verifySession();
        } else {
            verify();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verify() {
        try {
            List<PackageInstallerSession> childSessions = getChildSessions();
            if (isMultiPackage()) {
                for (PackageInstallerSession packageInstallerSession : childSessions) {
                    packageInstallerSession.prepareInheritedFiles();
                    packageInstallerSession.parseApkAndExtractNativeLibraries();
                }
            } else {
                prepareInheritedFiles();
                parseApkAndExtractNativeLibraries();
            }
            verifyNonStaged();
        } catch (PackageManagerException e) {
            String installStatusToString = PackageManager.installStatusToString(e.error, ExceptionUtils.getCompleteMessage(e));
            setSessionFailed(e.error, installStatusToString);
            onSessionVerificationFailure(e.error, installStatusToString);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IntentSender getRemoteStatusReceiver() {
        IntentSender intentSender;
        synchronized (this.mLock) {
            intentSender = this.mRemoteStatusReceiver;
        }
        return intentSender;
    }

    private void setRemoteStatusReceiver(IntentSender intentSender) {
        synchronized (this.mLock) {
            this.mRemoteStatusReceiver = intentSender;
        }
    }

    private void prepareInheritedFiles() throws PackageManagerException {
        if (isApexSession() || this.params.mode != 2) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mStageDirInUse) {
                throw new PackageManagerException(-110, "Session files in use");
            }
            if (this.mDestroyed) {
                throw new PackageManagerException(-110, "Session destroyed");
            }
            if (!this.mSealed) {
                throw new PackageManagerException(-110, "Session not sealed");
            }
            try {
                List<File> list = this.mResolvedInheritedFiles;
                File file = this.stageDir;
                String name = file.getName();
                Slog.d(TAG, "Inherited files: " + this.mResolvedInheritedFiles);
                if (!this.mResolvedInheritedFiles.isEmpty() && this.mInheritedFilesBase == null) {
                    throw new IllegalStateException("mInheritedFilesBase == null");
                }
                if (isLinkPossible(list, file)) {
                    if (!this.mResolvedInstructionSets.isEmpty()) {
                        createOatDirs(name, this.mResolvedInstructionSets, new File(file, "oat"));
                    }
                    if (!this.mResolvedNativeLibPaths.isEmpty()) {
                        for (String str : this.mResolvedNativeLibPaths) {
                            int lastIndexOf = str.lastIndexOf(47);
                            if (lastIndexOf >= 0 && lastIndexOf < str.length() - 1) {
                                File file2 = new File(file, str.substring(1, lastIndexOf));
                                if (!file2.exists()) {
                                    NativeLibraryHelper.createNativeLibrarySubdir(file2);
                                }
                                NativeLibraryHelper.createNativeLibrarySubdir(new File(file2, str.substring(lastIndexOf + 1)));
                            }
                            Slog.e(TAG, "Skipping native library creation for linking due to invalid path: " + str);
                        }
                    }
                    linkFiles(name, list, file, this.mInheritedFilesBase);
                } else {
                    copyFiles(list, file);
                }
            } catch (IOException e) {
                throw new PackageManagerException(-4, "Failed to inherit existing install", e);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void markStageDirInUseLocked() throws PackageManagerException {
        if (this.mDestroyed) {
            throw new PackageManagerException(-110, "Session destroyed");
        }
        this.mStageDirInUse = true;
        this.mMarkStageDirUserNoConsume = true;
    }

    private void parseApkAndExtractNativeLibraries() throws PackageManagerException {
        PackageLite orParsePackageLiteLocked;
        synchronized (this.mLock) {
            if (this.mStageDirInUse) {
                throw new PackageManagerException(-110, "Session files in use");
            }
            if (this.mDestroyed) {
                throw new PackageManagerException(-110, "Session destroyed");
            }
            if (!this.mSealed) {
                throw new PackageManagerException(-110, "Session not sealed");
            }
            Objects.requireNonNull(this.mPackageName);
            Objects.requireNonNull(this.mSigningDetails);
            Objects.requireNonNull(this.mResolvedBaseFile);
            if (!isApexSession()) {
                orParsePackageLiteLocked = getOrParsePackageLiteLocked(this.stageDir, 0);
            } else {
                orParsePackageLiteLocked = getOrParsePackageLiteLocked(this.mResolvedBaseFile, 0);
            }
            if (orParsePackageLiteLocked != null) {
                this.mPackageLite = orParsePackageLiteLocked;
                if (!isApexSession()) {
                    synchronized (this.mProgressLock) {
                        this.mInternalProgress = 0.5f;
                        computeProgressLocked(true);
                    }
                    if (!this.mPackageInstallerSessionExt.hasPreExtractNativeLibsFinished()) {
                        extractNativeLibraries(this.mPackageLite, this.stageDir, this.params.abiOverride, mayInheritNativeLibs());
                    }
                    if (PackageManagerService.DEBUG_INSTALL) {
                        Slog.d(TAG, "extractNativeLibraries end");
                    }
                }
            }
        }
    }

    private void verifyNonStaged() throws PackageManagerException {
        synchronized (this.mLock) {
            markStageDirInUseLocked();
        }
        this.mSessionProvider.getSessionVerifier().verify(this, new PackageSessionVerifier.Callback() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda0
            @Override // com.android.server.pm.PackageSessionVerifier.Callback
            public final void onResult(int i, String str) {
                PackageInstallerSession.this.lambda$verifyNonStaged$4(i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyNonStaged$4(final int i, final String str) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                PackageInstallerSession.this.lambda$verifyNonStaged$3(i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyNonStaged$3(int i, String str) {
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d(TAG, "SessionVerifier verify callback: " + i + ", " + str);
        }
        if (dispatchPendingAbandonCallback()) {
            return;
        }
        if (i == 1) {
            onVerificationComplete();
        } else {
            onSessionVerificationFailure(i, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class InstallResult {
        public final Bundle extras;
        public final PackageInstallerSession session;

        InstallResult(PackageInstallerSession packageInstallerSession, Bundle bundle) {
            this.session = packageInstallerSession;
            this.extras = bundle;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CompletableFuture<Void> install() {
        final List<CompletableFuture<InstallResult>> installNonStaged = installNonStaged();
        return CompletableFuture.allOf((CompletableFuture[]) installNonStaged.toArray(new CompletableFuture[installNonStaged.size()])).whenComplete(new BiConsumer() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PackageInstallerSession.this.lambda$install$5(installNonStaged, (Void) obj, (Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$install$5(List list, Void r3, Throwable th) {
        if (th == null) {
            setSessionApplied();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                InstallResult installResult = (InstallResult) ((CompletableFuture) it.next()).join();
                installResult.session.dispatchSessionFinished(1, "Session installed", installResult.extras);
            }
            return;
        }
        PackageManagerException packageManagerException = (PackageManagerException) th.getCause();
        int i = packageManagerException.error;
        setSessionFailed(i, PackageManager.installStatusToString(i, packageManagerException.getMessage()));
        dispatchSessionFinished(packageManagerException.error, packageManagerException.getMessage(), null);
        maybeFinishChildSessions(packageManagerException.error, packageManagerException.getMessage());
    }

    private List<CompletableFuture<InstallResult>> installNonStaged() {
        try {
            ArrayList arrayList = new ArrayList();
            CompletableFuture<InstallResult> completableFuture = new CompletableFuture<>();
            arrayList.add(completableFuture);
            InstallingSession createInstallingSession = createInstallingSession(completableFuture);
            if (isMultiPackage()) {
                List<PackageInstallerSession> childSessions = getChildSessions();
                ArrayList arrayList2 = new ArrayList(childSessions.size());
                for (int i = 0; i < childSessions.size(); i++) {
                    PackageInstallerSession packageInstallerSession = childSessions.get(i);
                    CompletableFuture<InstallResult> completableFuture2 = new CompletableFuture<>();
                    arrayList.add(completableFuture2);
                    InstallingSession createInstallingSession2 = packageInstallerSession.createInstallingSession(completableFuture2);
                    if (createInstallingSession2 != null) {
                        arrayList2.add(createInstallingSession2);
                    }
                }
                if (!arrayList2.isEmpty()) {
                    Objects.requireNonNull(createInstallingSession);
                    createInstallingSession.installStage(arrayList2);
                }
            } else if (createInstallingSession != null) {
                createInstallingSession.installStage();
            }
            return arrayList;
        } catch (PackageManagerException e) {
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(CompletableFuture.failedFuture(e));
            return arrayList3;
        }
    }

    private void sendPendingUserActionIntent(IntentSender intentSender) {
        Intent intent = new Intent(isPreapprovalRequested() && !isCommitted() ? "android.content.pm.action.CONFIRM_PRE_APPROVAL" : "android.content.pm.action.CONFIRM_INSTALL");
        intent.setPackage(this.mPm.getPackageInstallerPackageName());
        intent.putExtra("android.content.pm.extra.SESSION_ID", this.sessionId);
        sendOnUserActionRequired(this.mContext, intentSender, this.sessionId, intent);
    }

    private void onVerificationComplete() {
        if (isStaged()) {
            this.mPackageInstallerSessionExt.onStagedSessionVerificationComplete(this, this.mStagedSession, this.mPm);
            this.mStagingManager.commitSession(this.mStagedSession);
            sendUpdateToRemoteStatusReceiver(1, "Session staged", null);
            return;
        }
        install();
    }

    private InstallingSession createInstallingSession(final CompletableFuture<InstallResult> completableFuture) throws PackageManagerException {
        UserHandle userHandle;
        InstallingSession installingSession;
        synchronized (this.mLock) {
            if (!this.mSealed) {
                throw new PackageManagerException(-110, "Session not sealed");
            }
            markStageDirInUseLocked();
        }
        if (isMultiPackage()) {
            completableFuture.complete(new InstallResult(this, null));
        } else if (isApexSession() && this.params.isStaged) {
            completableFuture.complete(new InstallResult(this, null));
            return null;
        }
        IPackageInstallObserver2.Stub stub = new IPackageInstallObserver2.Stub() { // from class: com.android.server.pm.PackageInstallerSession.5
            public void onUserActionRequired(Intent intent) {
                throw new IllegalStateException();
            }

            public void onPackageInstalled(String str, int i, String str2, Bundle bundle) {
                if (i == 1) {
                    completableFuture.complete(new InstallResult(PackageInstallerSession.this, bundle));
                } else {
                    completableFuture.completeExceptionally(new PackageManagerException(i, str2));
                }
            }
        };
        if ((this.params.installFlags & 64) != 0) {
            userHandle = UserHandle.ALL;
        } else {
            userHandle = new UserHandle(this.userId);
        }
        UserHandle userHandle2 = userHandle;
        PackageInstaller.SessionParams sessionParams = this.params;
        if (sessionParams.isStaged) {
            sessionParams.installFlags |= DumpState.DUMP_COMPILER_STATS;
        }
        if (!isMultiPackage() && !isApexSession()) {
            synchronized (this.mLock) {
                if (this.mPackageLite == null) {
                    Slog.wtf(TAG, "Session: " + this.sessionId + ". Don't have a valid PackageLite.");
                }
                this.mPackageLite = getOrParsePackageLiteLocked(this.stageDir, 0);
            }
        }
        synchronized (this.mLock) {
            installingSession = new InstallingSession(this.sessionId, this.stageDir, stub, this.params, this.mInstallSource, userHandle2, this.mSigningDetails, this.mInstallerUid, this.mPackageLite, this.mPm);
        }
        return installingSession;
    }

    @GuardedBy({"mLock"})
    private PackageLite getOrParsePackageLiteLocked(File file, int i) throws PackageManagerException {
        PackageLite packageLite = this.mPackageLite;
        if (packageLite != null) {
            return packageLite;
        }
        ParseResult parsePackageLite = ApkLiteParseUtils.parsePackageLite(ParseTypeImpl.forDefaultParsing(), file, i);
        if (parsePackageLite.isError()) {
            throw new PackageManagerException(-110, parsePackageLite.getErrorMessage(), parsePackageLite.getException());
        }
        return (PackageLite) parsePackageLite.getResult();
    }

    private static void maybeRenameFile(File file, File file2) throws PackageManagerException {
        if (file.equals(file2) || file.renameTo(file2)) {
            return;
        }
        throw new PackageManagerException(-110, "Could not rename file " + file + " to " + file2);
    }

    private void logDataLoaderInstallationSession(int i) {
        String packageName = getPackageName();
        String str = (this.params.installFlags & 32) == 0 ? packageName : "";
        long currentTimeMillis = System.currentTimeMillis();
        FrameworkStatsLog.write(263, isIncrementalInstallation(), str, currentTimeMillis - this.createdMillis, i, getApksSize(packageName), i != 1 ? -1 : this.mPm.snapshotComputer().getPackageUid(packageName, 0L, this.userId));
    }

    private long getApksSize(String str) {
        File path;
        PackageStateInternal packageStateInternal = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getPackageStateInternal(str);
        long j = 0;
        if (packageStateInternal == null || (path = packageStateInternal.getPath()) == null) {
            return 0L;
        }
        if (path.isFile() && path.getName().toLowerCase().endsWith(".apk")) {
            return path.length();
        }
        if (!path.isDirectory()) {
            return 0L;
        }
        File[] listFiles = path.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].getName().toLowerCase().endsWith(".apk")) {
                j += listFiles[i].length();
            }
        }
        return j;
    }

    private boolean mayInheritNativeLibs() {
        if (SystemProperties.getBoolean(PROPERTY_NAME_INHERIT_NATIVE, true)) {
            PackageInstaller.SessionParams sessionParams = this.params;
            if (sessionParams.mode == 2 && (sessionParams.installFlags & 1) != 0) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isApexSession() {
        return (this.params.installFlags & 131072) != 0;
    }

    boolean sessionContains(Predicate<PackageInstallerSession> predicate) {
        List<PackageInstallerSession> childSessionsLocked;
        if (!isMultiPackage()) {
            return predicate.test(this);
        }
        synchronized (this.mLock) {
            childSessionsLocked = getChildSessionsLocked();
        }
        Iterator<PackageInstallerSession> it = childSessionsLocked.iterator();
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$containsApkSession$6(PackageInstallerSession packageInstallerSession) {
        return !packageInstallerSession.isApexSession();
    }

    boolean containsApkSession() {
        return sessionContains(new Predicate() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$containsApkSession$6;
                lambda$containsApkSession$6 = PackageInstallerSession.lambda$containsApkSession$6((PackageInstallerSession) obj);
                return lambda$containsApkSession$6;
            }
        });
    }

    @GuardedBy({"mLock"})
    private void validateApexInstallLocked() throws PackageManagerException {
        List<File> addedApksLocked = getAddedApksLocked();
        if (addedApksLocked.isEmpty()) {
            throw new PackageManagerException(-2, TextUtils.formatSimple("Session: %d. No packages staged in %s", new Object[]{Integer.valueOf(this.sessionId), this.stageDir.getAbsolutePath()}));
        }
        if (ArrayUtils.size(addedApksLocked) > 1) {
            throw new PackageManagerException(-2, "Too many files for apex install");
        }
        File file = addedApksLocked.get(0);
        String name = file.getName();
        if (!name.endsWith(APEX_FILE_EXTENSION)) {
            name = name + APEX_FILE_EXTENSION;
        }
        if (!FileUtils.isValidExtFilename(name)) {
            throw new PackageManagerException(-2, "Invalid filename: " + name);
        }
        File file2 = new File(this.stageDir, name);
        resolveAndStageFileLocked(file, file2, null);
        this.mResolvedBaseFile = file2;
        this.mPackageName = null;
        ParseResult parseApkLite = ApkLiteParseUtils.parseApkLite(ParseTypeImpl.forDefaultParsing().reset(), this.mResolvedBaseFile, 32);
        if (parseApkLite.isError()) {
            throw new PackageManagerException(parseApkLite.getErrorCode(), parseApkLite.getErrorMessage(), parseApkLite.getException());
        }
        ApkLite apkLite = (ApkLite) parseApkLite.getResult();
        if (this.mPackageName == null) {
            this.mPackageName = apkLite.getPackageName();
            this.mVersionCode = apkLite.getLongVersionCode();
        }
        this.mSigningDetails = apkLite.getSigningDetails();
        this.mHasDeviceAdminReceiver = apkLite.isHasDeviceAdminReceiver();
    }

    /* JADX WARN: Removed duplicated region for block: B:198:0x0422  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x048d  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x04e3  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0583  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x05cf  */
    @GuardedBy({"mLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private PackageLite validateApkInstallLocked() throws PackageManagerException {
        int i;
        File parentFile;
        File file;
        boolean z;
        PackageLite packageLite;
        int i2;
        File file2;
        File[] fileArr;
        File[] fileArr2;
        File file3;
        File[] listFiles;
        int length;
        int i3;
        File[] fileArr3;
        File[] listFiles2;
        Iterator<File> it;
        String installerPackageName;
        this.mPackageLite = null;
        this.mPackageName = null;
        this.mVersionCode = -1L;
        this.mSigningDetails = SigningDetails.UNKNOWN;
        this.mResolvedBaseFile = null;
        this.mResolvedStagedFiles.clear();
        this.mResolvedInheritedFiles.clear();
        PackageInfo packageInfo = this.mPm.snapshotComputer().getPackageInfo(this.params.appPackageName, 67108928L, this.userId);
        if (this.params.mode == 2 && (packageInfo == null || packageInfo.applicationInfo == null)) {
            throw new PackageManagerException(-2, "Missing existing base package");
        }
        this.mVerityFoundForApks = PackageManagerServiceUtils.isApkVerityEnabled() && this.params.mode == 2 && VerityUtils.hasFsverity(packageInfo.applicationInfo.getBaseCodePath()) && new File(VerityUtils.getFsveritySignatureFilePath(packageInfo.applicationInfo.getBaseCodePath())).exists();
        List<File> removedFilesLocked = getRemovedFilesLocked();
        ArrayList<String> arrayList = new ArrayList();
        if (!removedFilesLocked.isEmpty()) {
            Iterator<File> it2 = removedFilesLocked.iterator();
            while (it2.hasNext()) {
                arrayList.add(it2.next().getName().substring(0, r9.length() - 8));
            }
        }
        List<File> addedApksLocked = getAddedApksLocked();
        if (addedApksLocked.isEmpty() && (arrayList.size() == 0 || getStagedAppMetadataFile() != null)) {
            throw new PackageManagerException(-2, TextUtils.formatSimple("Session: %d. No packages staged in %s", new Object[]{Integer.valueOf(this.sessionId), this.stageDir.getAbsolutePath()}));
        }
        ArraySet arraySet = new ArraySet();
        ArraySet arraySet2 = new ArraySet();
        ArraySet arraySet3 = new ArraySet();
        ArrayMap arrayMap = new ArrayMap();
        ParseTypeImpl forDefaultParsing = ParseTypeImpl.forDefaultParsing();
        Iterator<File> it3 = addedApksLocked.iterator();
        ApkLite apkLite = null;
        while (it3.hasNext()) {
            File next = it3.next();
            ParseResult<ApkLite> preParseRetInValidateApkInstall = this.mPackageInstallerSessionExt.getPreParseRetInValidateApkInstall(next);
            if (preParseRetInValidateApkInstall == null) {
                preParseRetInValidateApkInstall = ApkLiteParseUtils.parseApkLite(forDefaultParsing.reset(), next, 32);
            }
            if (PackageManagerService.DEBUG_INSTALL) {
                StringBuilder sb = new StringBuilder();
                sb.append("parseApkLite end in validateApkInstall: ");
                sb.append(next == null ? null : next.getAbsolutePath());
                Slog.d(TAG, sb.toString());
            }
            if (preParseRetInValidateApkInstall.isError()) {
                throw new PackageManagerException(preParseRetInValidateApkInstall.getErrorCode(), preParseRetInValidateApkInstall.getErrorMessage(), preParseRetInValidateApkInstall.getException());
            }
            ApkLite apkLite2 = (ApkLite) preParseRetInValidateApkInstall.getResult();
            if (!arraySet.add(apkLite2.getSplitName())) {
                throw new PackageManagerException(-2, "Split " + apkLite2.getSplitName() + " was defined multiple times");
            }
            if (this.mPackageName == null) {
                this.mPackageName = apkLite2.getPackageName();
                this.mVersionCode = apkLite2.getLongVersionCode();
            }
            if (this.mSigningDetails == SigningDetails.UNKNOWN) {
                this.mSigningDetails = apkLite2.getSigningDetails();
            }
            this.mHasDeviceAdminReceiver = apkLite2.isHasDeviceAdminReceiver();
            assertApkConsistentLocked(String.valueOf(next), apkLite2);
            String splitNameToFileName = ApkLiteParseUtils.splitNameToFileName(apkLite2);
            if (!FileUtils.isValidExtFilename(splitNameToFileName)) {
                throw new PackageManagerException(-2, "Invalid filename: " + splitNameToFileName);
            }
            if (apkLite2.getInstallLocation() == -1 || (installerPackageName = getInstallerPackageName()) == null) {
                it = it3;
            } else {
                it = it3;
                if (this.params.installLocation != apkLite2.getInstallLocation()) {
                    Slog.wtf(TAG, installerPackageName + " drops manifest attribute android:installLocation in " + splitNameToFileName + " for " + this.mPackageName);
                }
            }
            File file4 = new File(this.stageDir, splitNameToFileName);
            resolveAndStageFileLocked(next, file4, apkLite2.getSplitName());
            if (apkLite2.getSplitName() == null) {
                this.mResolvedBaseFile = file4;
                apkLite = apkLite2;
            } else {
                arrayMap.put(apkLite2.getSplitName(), apkLite2);
            }
            CollectionUtils.addAll(arraySet3, apkLite2.getRequiredSplitTypes());
            CollectionUtils.addAll(arraySet2, apkLite2.getSplitTypes());
            it3 = it;
        }
        if (arrayList.size() > 0) {
            if (packageInfo == null) {
                throw new PackageManagerException(-2, "Missing existing base package for " + this.mPackageName);
            }
            for (String str : arrayList) {
                if (!ArrayUtils.contains(packageInfo.splitNames, str)) {
                    throw new PackageManagerException(-2, "Split not found: " + str);
                }
            }
            if (this.mPackageName == null) {
                this.mPackageName = packageInfo.packageName;
                this.mVersionCode = packageInfo.getLongVersionCode();
            }
            if (this.mSigningDetails == SigningDetails.UNKNOWN) {
                this.mSigningDetails = unsafeGetCertsWithoutVerification(packageInfo.applicationInfo.sourceDir);
            }
        }
        if (isIncrementalInstallation()) {
            if (!isIncrementalInstallationAllowed(this.mPackageName)) {
                throw new PackageManagerException(-116, "Incremental installation of this package is not allowed.");
            }
            File tmpAppMetadataFile = getTmpAppMetadataFile();
            if (tmpAppMetadataFile.exists()) {
                try {
                    try {
                        getIncrementalFileStorages().makeFile("app.metadata", Files.readAllBytes(tmpAppMetadataFile.toPath()), APP_METADATA_FILE_ACCESS_MODE);
                    } catch (IOException e) {
                        Slog.e(TAG, "Failed to write app metadata to incremental storage", e);
                    }
                } finally {
                    tmpAppMetadataFile.delete();
                }
            }
        }
        if (this.mInstallerUid != this.mOriginalInstallerUid && (TextUtils.isEmpty(this.mPackageName) || !this.mPackageName.equals(this.mOriginalInstallerPackageName))) {
            throw new PackageManagerException(-23, "Can only transfer sessions that update the original installer");
        }
        if (!this.mChecksums.isEmpty()) {
            throw new PackageManagerException(-116, "Invalid checksum name(s): " + String.join(",", this.mChecksums.keySet()));
        }
        if (this.params.mode == 1) {
            if (!arraySet.contains(null)) {
                throw new PackageManagerException(-2, "Full install must include a base package");
            }
            if ((this.params.installFlags & 4096) != 0) {
                EventLog.writeEvent(1397638484, "219044664");
                this.params.setDontKillApp(false);
            }
            if (apkLite.isSplitRequired() && (arraySet.size() <= 1 || !arraySet2.containsAll(arraySet3))) {
                throw new PackageManagerException(-28, "Missing split for " + this.mPackageName);
            }
            ParseResult composePackageLiteFromApks = ApkLiteParseUtils.composePackageLiteFromApks(forDefaultParsing.reset(), this.stageDir, apkLite, arrayMap, true);
            if (composePackageLiteFromApks.isError()) {
                throw new PackageManagerException(composePackageLiteFromApks.getErrorCode(), composePackageLiteFromApks.getErrorMessage(), composePackageLiteFromApks.getException());
            }
            packageLite = (PackageLite) composePackageLiteFromApks.getResult();
            this.mPackageLite = packageLite;
            z = true;
        } else {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            ParseResult parsePackageLite = ApkLiteParseUtils.parsePackageLite(forDefaultParsing.reset(), new File(applicationInfo.getCodePath()), 0);
            if (parsePackageLite.isError()) {
                throw new PackageManagerException(-110, parsePackageLite.getErrorMessage(), parsePackageLite.getException());
            }
            PackageLite packageLite2 = (PackageLite) parsePackageLite.getResult();
            assertPackageConsistentLocked("Existing", packageLite2.getPackageName(), packageLite2.getLongVersionCode());
            if (!this.mSigningDetails.signaturesMatchExactly(unsafeGetCertsWithoutVerification(packageLite2.getBaseApkPath()))) {
                throw new PackageManagerException(-2, "Existing signatures are inconsistent");
            }
            if (this.mResolvedBaseFile == null) {
                File file5 = new File(applicationInfo.getBaseCodePath());
                this.mResolvedBaseFile = file5;
                inheritFileLocked(file5);
                CollectionUtils.addAll(arraySet3, packageLite2.getBaseRequiredSplitTypes());
            } else if ((this.params.installFlags & 4096) != 0) {
                EventLog.writeEvent(1397638484, "219044664");
                i = 0;
                this.params.setDontKillApp(false);
                if (!ArrayUtils.isEmpty(packageLite2.getSplitNames())) {
                    for (int i4 = i; i4 < packageLite2.getSplitNames().length; i4++) {
                        String str2 = packageLite2.getSplitNames()[i4];
                        File file6 = new File(packageLite2.getSplitApkPaths()[i4]);
                        boolean contains = arrayList.contains(str2);
                        if (!arraySet.contains(str2) && !contains) {
                            inheritFileLocked(file6);
                            CollectionUtils.addAll(arraySet3, packageLite2.getRequiredSplitTypes()[i4]);
                            CollectionUtils.addAll(arraySet2, packageLite2.getSplitTypes()[i4]);
                        }
                    }
                }
                parentFile = new File(applicationInfo.getBaseCodePath()).getParentFile();
                this.mInheritedFilesBase = parentFile;
                file = new File(parentFile, "oat");
                if (file.exists() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                    String[] allDexCodeInstructionSets = InstructionSets.getAllDexCodeInstructionSets();
                    length = listFiles.length;
                    i3 = i;
                    while (i3 < length) {
                        File file7 = listFiles[i3];
                        if (ArrayUtils.contains(allDexCodeInstructionSets, file7.getName()) && (listFiles2 = file7.listFiles()) != null) {
                            fileArr3 = listFiles;
                            if (listFiles2.length != 0) {
                                this.mResolvedInstructionSets.add(file7.getName());
                                this.mResolvedInheritedFiles.addAll(Arrays.asList(listFiles2));
                            }
                        } else {
                            fileArr3 = listFiles;
                        }
                        i3++;
                        listFiles = fileArr3;
                    }
                }
                if (mayInheritNativeLibs() && arrayList.isEmpty()) {
                    File[] fileArr4 = {new File(parentFile, "lib"), new File(parentFile, "lib64")};
                    i2 = 0;
                    while (i2 < 2) {
                        File file8 = fileArr4[i2];
                        if (file8.exists() && file8.isDirectory()) {
                            ArrayList<String> arrayList2 = new ArrayList();
                            ArrayList arrayList3 = new ArrayList();
                            File[] listFiles3 = file8.listFiles();
                            int length2 = listFiles3.length;
                            fileArr = fileArr4;
                            int i5 = 0;
                            while (i5 < length2) {
                                int i6 = length2;
                                File file9 = listFiles3[i5];
                                if (file9.isDirectory()) {
                                    fileArr2 = listFiles3;
                                    try {
                                        String relativePath = getRelativePath(file9, parentFile);
                                        File[] listFiles4 = file9.listFiles();
                                        file3 = parentFile;
                                        if (listFiles4 != null && listFiles4.length != 0) {
                                            arrayList2.add(relativePath);
                                            arrayList3.addAll(Arrays.asList(listFiles4));
                                        }
                                    } catch (IOException e2) {
                                        file2 = parentFile;
                                        Slog.e(TAG, "Skipping linking of native library directory!", e2);
                                        arrayList2.clear();
                                        arrayList3.clear();
                                    }
                                } else {
                                    fileArr2 = listFiles3;
                                    file3 = parentFile;
                                }
                                i5++;
                                length2 = i6;
                                listFiles3 = fileArr2;
                                parentFile = file3;
                            }
                            file2 = parentFile;
                            for (String str3 : arrayList2) {
                                if (!this.mResolvedNativeLibPaths.contains(str3)) {
                                    this.mResolvedNativeLibPaths.add(str3);
                                }
                            }
                            this.mResolvedInheritedFiles.addAll(arrayList3);
                        } else {
                            file2 = parentFile;
                            fileArr = fileArr4;
                        }
                        i2++;
                        fileArr4 = fileArr;
                        parentFile = file2;
                    }
                }
                if (packageLite2.isSplitRequired()) {
                    z = true;
                } else {
                    boolean z2 = ArrayUtils.size(packageLite2.getSplitNames()) == arrayList.size();
                    z = true;
                    boolean z3 = arraySet.size() == 1 && arraySet.contains(null);
                    if ((z2 && (arraySet.isEmpty() || z3)) || !arraySet2.containsAll(arraySet3)) {
                        throw new PackageManagerException(-28, "Missing split for " + this.mPackageName);
                    }
                }
                packageLite = packageLite2;
            }
            i = 0;
            if (!ArrayUtils.isEmpty(packageLite2.getSplitNames())) {
            }
            parentFile = new File(applicationInfo.getBaseCodePath()).getParentFile();
            this.mInheritedFilesBase = parentFile;
            file = new File(parentFile, "oat");
            if (file.exists()) {
                String[] allDexCodeInstructionSets2 = InstructionSets.getAllDexCodeInstructionSets();
                length = listFiles.length;
                i3 = i;
                while (i3 < length) {
                }
            }
            if (mayInheritNativeLibs()) {
                File[] fileArr42 = {new File(parentFile, "lib"), new File(parentFile, "lib64")};
                i2 = 0;
                while (i2 < 2) {
                }
            }
            if (packageLite2.isSplitRequired()) {
            }
            packageLite = packageLite2;
        }
        assertPreapprovalDetailsConsistentIfNeededLocked(packageLite, packageInfo);
        if (packageLite.isUseEmbeddedDex()) {
            for (File file10 : this.mResolvedStagedFiles) {
                if (file10.getName().endsWith(".apk") && !DexManager.auditUncompressedDexInApk(file10.getPath())) {
                    throw new PackageManagerException(-2, "Some dex are not uncompressed and aligned correctly for " + this.mPackageName);
                }
            }
        }
        if ((this.mInstallerUid == 2000 ? z : false) && isIncrementalInstallation() && this.mIncrementalFileStorages != null && !packageLite.isDebuggable() && !packageLite.isProfileableByShell()) {
            this.mIncrementalFileStorages.disallowReadLogs();
        }
        this.mValidatedTargetSdk = packageLite.getTargetSdk();
        return packageLite;
    }

    @GuardedBy({"mLock"})
    private void stageFileLocked(File file, File file2) throws PackageManagerException {
        this.mResolvedStagedFiles.add(file2);
        maybeRenameFile(file, file2);
    }

    @GuardedBy({"mLock"})
    private void maybeStageFsveritySignatureLocked(File file, File file2, boolean z) throws PackageManagerException {
        File file3 = new File(VerityUtils.getFsveritySignatureFilePath(file.getPath()));
        if (file3.exists()) {
            stageFileLocked(file3, new File(VerityUtils.getFsveritySignatureFilePath(file2.getPath())));
        } else if (z) {
            throw new PackageManagerException(-118, "Missing corresponding fs-verity signature to " + file);
        }
    }

    @GuardedBy({"mLock"})
    private void maybeStageDexMetadataLocked(File file, File file2) throws PackageManagerException {
        File findDexMetadataForFile = DexMetadataHelper.findDexMetadataForFile(file);
        if (findDexMetadataForFile == null) {
            return;
        }
        if (!FileUtils.isValidExtFilename(findDexMetadataForFile.getName())) {
            throw new PackageManagerException(-2, "Invalid filename: " + findDexMetadataForFile);
        }
        File file3 = new File(this.stageDir, DexMetadataHelper.buildDexMetadataPathForApk(file2.getName()));
        stageFileLocked(findDexMetadataForFile, file3);
        maybeStageFsveritySignatureLocked(findDexMetadataForFile, file3, DexMetadataHelper.isFsVerityRequired());
    }

    private IncrementalFileStorages getIncrementalFileStorages() {
        IncrementalFileStorages incrementalFileStorages;
        synchronized (this.mLock) {
            incrementalFileStorages = this.mIncrementalFileStorages;
        }
        return incrementalFileStorages;
    }

    private void storeBytesToInstallationFile(String str, String str2, byte[] bArr) throws IOException {
        IncrementalFileStorages incrementalFileStorages = getIncrementalFileStorages();
        if (!isIncrementalInstallation() || incrementalFileStorages == null) {
            FileUtils.bytesToFile(str2, bArr);
        } else {
            incrementalFileStorages.makeFile(str, bArr, 511);
        }
    }

    @GuardedBy({"mLock"})
    private void maybeStageDigestsLocked(File file, File file2, String str) throws PackageManagerException {
        PerFileChecksum perFileChecksum = this.mChecksums.get(file.getName());
        if (perFileChecksum == null) {
            return;
        }
        this.mChecksums.remove(file.getName());
        Checksum[] checksums = perFileChecksum.getChecksums();
        if (checksums.length == 0) {
            return;
        }
        String buildDigestsPathForApk = ApkChecksums.buildDigestsPathForApk(file2.getName());
        File file3 = new File(this.stageDir, buildDigestsPathForApk);
        try {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    ApkChecksums.writeChecksums(byteArrayOutputStream, checksums);
                    byte[] signature = perFileChecksum.getSignature();
                    if (signature != null && signature.length > 0) {
                        ApkChecksums.verifySignature(checksums, signature);
                    }
                    storeBytesToInstallationFile(buildDigestsPathForApk, file3.getAbsolutePath(), byteArrayOutputStream.toByteArray());
                    stageFileLocked(file3, file3);
                    if (signature != null && signature.length != 0) {
                        String buildSignaturePathForDigests = ApkChecksums.buildSignaturePathForDigests(buildDigestsPathForApk);
                        File file4 = new File(this.stageDir, buildSignaturePathForDigests);
                        storeBytesToInstallationFile(buildSignaturePathForDigests, file4.getAbsolutePath(), signature);
                        stageFileLocked(file4, file4);
                        byteArrayOutputStream.close();
                        return;
                    }
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (NoSuchAlgorithmException | SignatureException e) {
                throw new PackageManagerException(GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_INVALID_TRANSITION, "Failed to verify digests' signature for " + this.mPackageName, e);
            }
        } catch (IOException e2) {
            throw new PackageManagerException(-4, "Failed to store digests for " + this.mPackageName, e2);
        }
    }

    @GuardedBy({"mLock"})
    private boolean isFsVerityRequiredForApk(File file, File file2) throws PackageManagerException {
        if (this.mVerityFoundForApks) {
            return true;
        }
        if (!new File(VerityUtils.getFsveritySignatureFilePath(file.getPath())).exists()) {
            return false;
        }
        this.mVerityFoundForApks = true;
        for (File file3 : this.mResolvedStagedFiles) {
            if (file3.getName().endsWith(".apk") && !file2.getName().equals(file3.getName())) {
                throw new PackageManagerException(-118, "Previously staged apk is missing fs-verity signature");
            }
        }
        return true;
    }

    @GuardedBy({"mLock"})
    private void resolveAndStageFileLocked(File file, File file2, String str) throws PackageManagerException {
        stageFileLocked(file, file2);
        maybeStageFsveritySignatureLocked(file, file2, isFsVerityRequiredForApk(file, file2));
        maybeStageDexMetadataLocked(file, file2);
        maybeStageDigestsLocked(file, file2, str);
    }

    @GuardedBy({"mLock"})
    private void maybeInheritFsveritySignatureLocked(File file) {
        File file2 = new File(VerityUtils.getFsveritySignatureFilePath(file.getPath()));
        if (file2.exists()) {
            this.mResolvedInheritedFiles.add(file2);
        }
    }

    @GuardedBy({"mLock"})
    private void inheritFileLocked(File file) {
        this.mResolvedInheritedFiles.add(file);
        maybeInheritFsveritySignatureLocked(file);
        File findDexMetadataForFile = DexMetadataHelper.findDexMetadataForFile(file);
        if (findDexMetadataForFile != null) {
            this.mResolvedInheritedFiles.add(findDexMetadataForFile);
            maybeInheritFsveritySignatureLocked(findDexMetadataForFile);
        }
        File findDigestsForFile = ApkChecksums.findDigestsForFile(file);
        if (findDigestsForFile != null) {
            this.mResolvedInheritedFiles.add(findDigestsForFile);
            File findSignatureForDigests = ApkChecksums.findSignatureForDigests(findDigestsForFile);
            if (findSignatureForDigests != null) {
                this.mResolvedInheritedFiles.add(findSignatureForDigests);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void assertApkConsistentLocked(String str, ApkLite apkLite) throws PackageManagerException {
        assertPackageConsistentLocked(str, apkLite.getPackageName(), apkLite.getLongVersionCode());
        if (this.mSigningDetails.signaturesMatchExactly(apkLite.getSigningDetails())) {
            return;
        }
        throw new PackageManagerException(-2, str + " signatures are inconsistent");
    }

    @GuardedBy({"mLock"})
    private void assertPackageConsistentLocked(String str, String str2, long j) throws PackageManagerException {
        if (!this.mPackageName.equals(str2)) {
            throw new PackageManagerException(-2, str + " package " + str2 + " inconsistent with " + this.mPackageName);
        }
        String str3 = this.params.appPackageName;
        if (str3 != null && !str3.equals(str2)) {
            throw new PackageManagerException(-2, str + " specified package " + this.params.appPackageName + " inconsistent with " + str2);
        }
        if (this.mVersionCode == j) {
            return;
        }
        throw new PackageManagerException(-2, str + " version code " + j + " inconsistent with " + this.mVersionCode);
    }

    @GuardedBy({"mLock"})
    private void assertPreapprovalDetailsConsistentIfNeededLocked(PackageLite packageLite, PackageInfo packageInfo) throws PackageManagerException {
        if (this.mPreapprovalDetails == null || !isPreapprovalRequested()) {
            return;
        }
        if (!TextUtils.equals(this.mPackageName, this.mPreapprovalDetails.getPackageName())) {
            throw new PackageManagerException(-110, this.mPreapprovalDetails + " inconsistent with " + this.mPackageName);
        }
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageInfo == null) {
            packageInfo = this.mPm.snapshotComputer().getPackageInfo(this.mPackageName, 0L, this.userId);
        }
        CharSequence label = this.mPreapprovalDetails.getLabel();
        if (packageInfo == null || !TextUtils.equals(label, packageManager.getApplicationLabel(packageInfo.applicationInfo))) {
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(packageLite.getPath(), PackageManager.PackageInfoFlags.of(0L));
            if (packageArchiveInfo == null) {
                throw new PackageManagerException(-2, "Failure to obtain package info from APK files.");
            }
            List allApkPaths = packageLite.getAllApkPaths();
            ULocale locale = this.mPreapprovalDetails.getLocale();
            ApplicationInfo applicationInfo = packageArchiveInfo.applicationInfo;
            boolean z = false;
            for (int size = allApkPaths.size() - 1; size >= 0 && !z; size--) {
                z |= TextUtils.equals(getAppLabel((String) allApkPaths.get(size), locale, applicationInfo), label);
            }
            if (z) {
                return;
            }
            throw new PackageManagerException(-110, this.mPreapprovalDetails + " inconsistent with app label");
        }
    }

    private CharSequence getAppLabel(String str, ULocale uLocale, ApplicationInfo applicationInfo) throws PackageManagerException {
        Resources resources = this.mContext.getResources();
        AssetManager assetManager = new AssetManager();
        Configuration configuration = new Configuration(resources.getConfiguration());
        try {
            assetManager.setApkAssets(new ApkAssets[]{ApkAssets.loadFromPath(str)}, false);
            configuration.setLocale(uLocale.toLocale());
            return TextUtils.trimToSize(tryLoadingAppLabel(new Resources(assetManager, resources.getDisplayMetrics(), configuration), applicationInfo), 1000);
        } catch (IOException unused) {
            throw new PackageManagerException(-2, "Failure to get resources from package archive " + str);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:5:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private CharSequence tryLoadingAppLabel(Resources resources, ApplicationInfo applicationInfo) {
        String trim;
        int i = applicationInfo.labelRes;
        if (i != 0) {
            try {
                trim = resources.getText(i).toString().trim();
            } catch (Resources.NotFoundException unused) {
            }
            if (trim == null) {
                return trim;
            }
            CharSequence charSequence = applicationInfo.nonLocalizedLabel;
            return charSequence != null ? charSequence : applicationInfo.packageName;
        }
        trim = null;
        if (trim == null) {
        }
    }

    private SigningDetails unsafeGetCertsWithoutVerification(String str) throws PackageManagerException {
        ParseResult unsafeGetCertsWithoutVerification = ApkSignatureVerifier.unsafeGetCertsWithoutVerification(ParseTypeImpl.forDefaultParsing(), str, 1);
        if (unsafeGetCertsWithoutVerification.isError()) {
            throw new PackageManagerException(-2, "Couldn't obtain signatures from APK : " + str);
        }
        return (SigningDetails) unsafeGetCertsWithoutVerification.getResult();
    }

    private static boolean isLinkPossible(List<File> list, File file) {
        try {
            StructStat stat = Os.stat(file.getAbsolutePath());
            Iterator<File> it = list.iterator();
            while (it.hasNext()) {
                if (Os.stat(it.next().getAbsolutePath()).st_dev != stat.st_dev) {
                    return false;
                }
            }
            return true;
        } catch (ErrnoException e) {
            Slog.w(TAG, "Failed to detect if linking possible: " + e);
            return false;
        }
    }

    public int getInstallerUid() {
        int i;
        synchronized (this.mLock) {
            i = this.mInstallerUid;
        }
        return i;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public String getPackageName() {
        String str;
        synchronized (this.mLock) {
            str = this.mPackageName;
        }
        return str;
    }

    public long getUpdatedMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.updatedMillis;
        }
        return j;
    }

    long getCommittedMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.committedMillis;
        }
        return j;
    }

    String getInstallerPackageName() {
        return getInstallSource().mInstallerPackageName;
    }

    String getInstallerAttributionTag() {
        return getInstallSource().mInstallerAttributionTag;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallSource getInstallSource() {
        InstallSource installSource;
        synchronized (this.mLock) {
            installSource = this.mInstallSource;
        }
        return installSource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SigningDetails getSigningDetails() {
        SigningDetails signingDetails;
        synchronized (this.mLock) {
            signingDetails = this.mSigningDetails;
        }
        return signingDetails;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageLite getPackageLite() {
        PackageLite packageLite;
        synchronized (this.mLock) {
            packageLite = this.mPackageLite;
        }
        return packageLite;
    }

    public boolean getUserActionRequired() {
        Boolean bool = this.mUserActionRequired;
        if (bool != null) {
            return bool.booleanValue();
        }
        Slog.wtf(TAG, "mUserActionRequired should not be null.");
        return false;
    }

    private static String getRelativePath(File file, File file2) throws IOException {
        String absolutePath = file.getAbsolutePath();
        String absolutePath2 = file2.getAbsolutePath();
        if (absolutePath.contains("/.")) {
            throw new IOException("Invalid path (was relative) : " + absolutePath);
        }
        if (absolutePath.startsWith(absolutePath2)) {
            return absolutePath.substring(absolutePath2.length());
        }
        throw new IOException("File: " + absolutePath + " outside base: " + absolutePath2);
    }

    private void createOatDirs(String str, List<String> list, File file) throws PackageManagerException {
        this.mPackageInstallerSessionExt.beforeCreateOatDirs();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            try {
                this.mInstaller.createOatDir(str, file.getAbsolutePath(), it.next());
            } catch (Installer.InstallerException e) {
                throw PackageManagerException.from(e);
            }
        }
    }

    private void linkFile(String str, String str2, String str3, String str4) throws IOException {
        try {
            IncrementalFileStorages incrementalFileStorages = getIncrementalFileStorages();
            if (incrementalFileStorages == null || !incrementalFileStorages.makeLink(str2, str3, str4)) {
                this.mInstaller.linkFile(str, str2, str3, str4);
            }
        } catch (Installer.InstallerException | IOException e) {
            throw new IOException("failed linkOrCreateDir(" + str2 + ", " + str3 + ", " + str4 + ")", e);
        }
    }

    private void linkFiles(String str, List<File> list, File file, File file2) throws IOException {
        Iterator<File> it = list.iterator();
        while (it.hasNext()) {
            linkFile(str, getRelativePath(it.next(), file2), file2.getAbsolutePath(), file.getAbsolutePath());
        }
        Slog.d(TAG, "Linked " + list.size() + " files into " + file);
    }

    private static void copyFiles(List<File> list, File file) throws IOException {
        for (File file2 : file.listFiles()) {
            if (file2.getName().endsWith(".tmp")) {
                file2.delete();
            }
        }
        for (File file3 : list) {
            File createTempFile = File.createTempFile("inherit", ".tmp", file);
            Slog.d(TAG, "Copying " + file3 + " to " + createTempFile);
            if (!FileUtils.copyFile(file3, createTempFile)) {
                throw new IOException("Failed to copy " + file3 + " to " + createTempFile);
            }
            try {
                Os.chmod(createTempFile.getAbsolutePath(), 420);
                File file4 = new File(file, file3.getName());
                Slog.d(TAG, "Renaming " + createTempFile + " to " + file4);
                if (!createTempFile.renameTo(file4)) {
                    throw new IOException("Failed to rename " + createTempFile + " to " + file4);
                }
            } catch (ErrnoException unused) {
                throw new IOException("Failed to chmod " + createTempFile);
            }
        }
        Slog.d(TAG, "Copied " + list.size() + " files into " + file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extractNativeLibraries(PackageLite packageLite, File file, String str, boolean z) throws PackageManagerException {
        Objects.requireNonNull(packageLite);
        File file2 = new File(file, "lib");
        if (!z) {
            NativeLibraryHelper.removeNativeBinariesFromDirLI(file2, true);
        }
        NativeLibraryHelper.Handle handle = null;
        try {
            try {
                handle = NativeLibraryHelper.Handle.create(packageLite);
                int copyNativeBinariesWithOverride = NativeLibraryHelper.copyNativeBinariesWithOverride(handle, file2, str, isIncrementalInstallation());
                if (copyNativeBinariesWithOverride == 1) {
                    return;
                }
                throw new PackageManagerException(copyNativeBinariesWithOverride, "Failed to extract native libraries, res=" + copyNativeBinariesWithOverride);
            } catch (IOException e) {
                throw new PackageManagerException(-110, "Failed to extract native libraries", e);
            }
        } finally {
            IoUtils.closeQuietly(handle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPermissionsResult(boolean z) {
        if (!isSealed() && !isPreapprovalRequested()) {
            throw new SecurityException("Must be sealed to accept permissions");
        }
        PackageInstallerSession session = (hasParentSessionId() && isCommitted()) ? this.mSessionProvider.getSession(getParentSessionId()) : this;
        if (z) {
            synchronized (this.mLock) {
                this.mPermissionsManuallyAccepted = true;
            }
            session.mHandler.obtainMessage(isCommitted() ? 3 : 6).sendToTarget();
        } else {
            session.destroy();
            session.dispatchSessionFinished(-115, "User rejected permissions", null);
            session.maybeFinishChildSessions(-115, "User rejected permissions");
        }
    }

    public void open() throws IOException {
        boolean z;
        activate();
        synchronized (this.mLock) {
            z = this.mPrepared;
            if (!z) {
                File file = this.stageDir;
                if (file != null) {
                    PackageInstallerService.prepareStageDir(file);
                } else if (!this.params.isMultiPackage) {
                    throw new IllegalArgumentException("stageDir must be set");
                }
                this.mPrepared = true;
            }
        }
        if (z) {
            return;
        }
        this.mCallback.onSessionPrepared(this);
    }

    private void activate() {
        if (this.mActiveCount.getAndIncrement() == 0) {
            this.mCallback.onSessionActiveChanged(this, true);
        }
    }

    public void close() {
        closeInternal(true);
    }

    private void closeInternal(boolean z) {
        synchronized (this.mLock) {
            if (z) {
                assertCallerIsOwnerOrRoot();
            }
        }
        deactivate();
    }

    private void deactivate() {
        int decrementAndGet;
        synchronized (this.mLock) {
            decrementAndGet = this.mActiveCount.decrementAndGet();
        }
        if (decrementAndGet == 0) {
            this.mCallback.onSessionActiveChanged(this, false);
        }
    }

    private void maybeFinishChildSessions(int i, String str) {
        Iterator<PackageInstallerSession> it = getChildSessions().iterator();
        while (it.hasNext()) {
            it.next().dispatchSessionFinished(i, str, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertNotChild(String str) {
        if (hasParentSessionId()) {
            throw new IllegalStateException(str + " can't be called on a child session, id=" + this.sessionId + " parentId=" + getParentSessionId());
        }
    }

    private boolean dispatchPendingAbandonCallback() {
        synchronized (this.mLock) {
            if (!this.mStageDirInUse) {
                return false;
            }
            if (!this.mMarkStageDirUserNoConsume) {
                return true;
            }
            this.mMarkStageDirUserNoConsume = false;
            this.mStageDirInUse = false;
            Runnable runnable = this.mPendingAbandonCallback;
            this.mPendingAbandonCallback = null;
            if (runnable == null) {
                return false;
            }
            runnable.run();
            return true;
        }
    }

    public void abandon() {
        this.mPackageInstallerSessionSocExt.boostBeforeAbandon();
        synchronized (this.mLock) {
            assertNotChild("abandon");
            assertCallerIsOwnerOrRootOrSystem();
            if (isInTerminalState()) {
                return;
            }
            this.mDestroyed = true;
            Runnable runnable = new Runnable() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    PackageInstallerSession.this.lambda$abandon$7();
                }
            };
            if (this.mStageDirInUse) {
                this.mPendingAbandonCallback = runnable;
                this.mCallback.onSessionChanged(this);
            } else {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    runnable.run();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$abandon$7() {
        assertNotLocked("abandonStaged");
        if (isStaged() && isCommitted()) {
            this.mStagingManager.abortCommittedSession(this.mStagedSession);
        }
        destroy();
        dispatchSessionFinished(-115, "Session was abandoned", null);
        maybeFinishChildSessions(-115, "Session was abandoned because the parent session is abandoned");
    }

    public boolean isMultiPackage() {
        return this.params.isMultiPackage;
    }

    public boolean isStaged() {
        return this.params.isStaged;
    }

    public int getInstallFlags() {
        return this.params.installFlags;
    }

    public DataLoaderParamsParcel getDataLoaderParams() {
        this.mContext.enforceCallingOrSelfPermission("com.android.permission.USE_INSTALLER_V2", null);
        DataLoaderParams dataLoaderParams = this.params.dataLoaderParams;
        if (dataLoaderParams != null) {
            return dataLoaderParams.getData();
        }
        return null;
    }

    public void addFile(int i, String str, long j, byte[] bArr, byte[] bArr2) {
        this.mContext.enforceCallingOrSelfPermission("com.android.permission.USE_INSTALLER_V2", null);
        if (!isDataLoaderInstallation()) {
            throw new IllegalStateException("Cannot add files to non-data loader installation session.");
        }
        if (isStreamingInstallation() && i != 0) {
            throw new IllegalArgumentException("Non-incremental installation only supports /data/app placement: " + str);
        }
        if (bArr == null) {
            throw new IllegalArgumentException("DataLoader installation requires valid metadata: " + str);
        }
        if (!FileUtils.isValidExtFilename(str)) {
            throw new IllegalArgumentException("Invalid name: " + str);
        }
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("addFile");
            ArraySet<FileEntry> arraySet = this.mFiles;
            if (!arraySet.add(new FileEntry(arraySet.size(), new InstallationFile(i, str, j, bArr, bArr2)))) {
                throw new IllegalArgumentException("File already added: " + str);
            }
        }
    }

    public void removeFile(int i, String str) {
        this.mContext.enforceCallingOrSelfPermission("com.android.permission.USE_INSTALLER_V2", null);
        if (!isDataLoaderInstallation()) {
            throw new IllegalStateException("Cannot add files to non-data loader installation session.");
        }
        if (TextUtils.isEmpty(this.params.appPackageName)) {
            throw new IllegalStateException("Must specify package name to remove a split");
        }
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("removeFile");
            ArraySet<FileEntry> arraySet = this.mFiles;
            if (!arraySet.add(new FileEntry(arraySet.size(), new InstallationFile(i, getRemoveMarkerName(str), -1L, (byte[]) null, (byte[]) null)))) {
                throw new IllegalArgumentException("File already removed: " + str);
            }
        }
    }

    @GuardedBy({"mLock"})
    private boolean prepareDataLoaderLocked() throws PackageManagerException {
        if (!isDataLoaderInstallation() || this.mDataLoaderFinished) {
            return true;
        }
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        for (InstallationFile installationFile : getInstallationFilesLocked()) {
            if (sAddedFilter.accept(new File(this.stageDir, installationFile.getName()))) {
                arrayList.add(installationFile.getData());
            } else if (sRemovedFilter.accept(new File(this.stageDir, installationFile.getName()))) {
                arrayList2.add(installationFile.getName().substring(0, installationFile.getName().length() - 8));
            }
        }
        final DataLoaderParams dataLoaderParams = this.params.dataLoaderParams;
        final boolean isIncrementalInstallation = true ^ isIncrementalInstallation();
        final boolean isSystemDataLoaderInstallation = isSystemDataLoaderInstallation();
        IDataLoaderStatusListener.Stub stub = new IDataLoaderStatusListener.Stub() { // from class: com.android.server.pm.PackageInstallerSession.6
            public void onStatusChanged(int i, int i2) {
                if (i2 == 0 || i2 == 1 || i2 == 5) {
                    return;
                }
                if (PackageInstallerSession.this.mDestroyed || PackageInstallerSession.this.mDataLoaderFinished) {
                    if (i2 == 9 && isSystemDataLoaderInstallation) {
                        PackageInstallerSession.this.onSystemDataLoaderUnrecoverable();
                        return;
                    }
                    return;
                }
                try {
                    switch (i2) {
                        case 2:
                            if (isIncrementalInstallation) {
                                FileSystemControlParcel fileSystemControlParcel = new FileSystemControlParcel();
                                fileSystemControlParcel.callback = new FileSystemConnector(arrayList);
                                PackageInstallerSession.this.getDataLoader(i).create(i, dataLoaderParams.getData(), fileSystemControlParcel, this);
                                return;
                            }
                            return;
                        case 3:
                            if (isIncrementalInstallation) {
                                PackageInstallerSession.this.getDataLoader(i).start(i);
                                return;
                            }
                            return;
                        case 4:
                            IDataLoader dataLoader = PackageInstallerSession.this.getDataLoader(i);
                            List list = arrayList;
                            InstallationFileParcel[] installationFileParcelArr = (InstallationFileParcel[]) list.toArray(new InstallationFileParcel[list.size()]);
                            List list2 = arrayList2;
                            dataLoader.prepareImage(i, installationFileParcelArr, (String[]) list2.toArray(new String[list2.size()]));
                            return;
                        case 5:
                        default:
                            return;
                        case 6:
                            PackageInstallerSession.this.mDataLoaderFinished = true;
                            if (PackageInstallerSession.this.hasParentSessionId()) {
                                PackageInstallerSession.this.mSessionProvider.getSession(PackageInstallerSession.this.getParentSessionId()).dispatchSessionSealed();
                            } else {
                                PackageInstallerSession.this.dispatchSessionSealed();
                            }
                            if (isIncrementalInstallation) {
                                PackageInstallerSession.this.getDataLoader(i).destroy(i);
                                return;
                            }
                            return;
                        case 7:
                            PackageInstallerSession.this.mDataLoaderFinished = true;
                            PackageInstallerSession.this.dispatchSessionValidationFailure(-20, "Failed to prepare image.");
                            if (isIncrementalInstallation) {
                                PackageInstallerSession.this.getDataLoader(i).destroy(i);
                                return;
                            }
                            return;
                        case 8:
                            PackageInstallerSession.sendPendingStreaming(PackageInstallerSession.this.mContext, PackageInstallerSession.this.getRemoteStatusReceiver(), PackageInstallerSession.this.sessionId, "DataLoader unavailable");
                            return;
                        case 9:
                            throw new PackageManagerException(-20, "DataLoader reported unrecoverable failure.");
                    }
                } catch (RemoteException e) {
                    PackageInstallerSession.sendPendingStreaming(PackageInstallerSession.this.mContext, PackageInstallerSession.this.getRemoteStatusReceiver(), PackageInstallerSession.this.sessionId, e.getMessage());
                } catch (PackageManagerException e2) {
                    PackageInstallerSession.this.mDataLoaderFinished = true;
                    PackageInstallerSession.this.dispatchSessionValidationFailure(e2.error, ExceptionUtils.getCompleteMessage(e2));
                }
            }
        };
        if (!isIncrementalInstallation) {
            PackageManagerService packageManagerService = this.mPm;
            PerUidReadTimeouts[] perUidReadTimeouts = packageManagerService.getPerUidReadTimeouts(packageManagerService.snapshotComputer());
            StorageHealthCheckParams storageHealthCheckParams = new StorageHealthCheckParams();
            storageHealthCheckParams.blockedTimeoutMs = 2000;
            storageHealthCheckParams.unhealthyTimeoutMs = INCREMENTAL_STORAGE_UNHEALTHY_TIMEOUT_MS;
            storageHealthCheckParams.unhealthyMonitoringMs = 60000;
            IStorageHealthListener.Stub stub2 = new IStorageHealthListener.Stub() { // from class: com.android.server.pm.PackageInstallerSession.7
                public void onHealthStatus(int i, int i2) {
                    if (PackageInstallerSession.this.mDestroyed || PackageInstallerSession.this.mDataLoaderFinished) {
                        return;
                    }
                    if (i2 == 1 || i2 == 2) {
                        if (isSystemDataLoaderInstallation) {
                            return;
                        }
                    } else if (i2 != 3) {
                        return;
                    }
                    PackageInstallerSession.this.mDataLoaderFinished = true;
                    PackageInstallerSession.this.dispatchSessionValidationFailure(-20, "Image is missing pages required for installation.");
                }
            };
            try {
                PackageInfo packageInfo = this.mPm.snapshotComputer().getPackageInfo(this.params.appPackageName, 0L, this.userId);
                File parentFile = (packageInfo == null || packageInfo.applicationInfo == null) ? null : new File(packageInfo.applicationInfo.getCodePath()).getParentFile();
                IncrementalFileStorages incrementalFileStorages = this.mIncrementalFileStorages;
                if (incrementalFileStorages == null) {
                    this.mIncrementalFileStorages = IncrementalFileStorages.initialize(this.mContext, this.stageDir, parentFile, dataLoaderParams, stub, storageHealthCheckParams, stub2, arrayList, perUidReadTimeouts, new IPackageLoadingProgressCallback.Stub() { // from class: com.android.server.pm.PackageInstallerSession.8
                        public void onPackageLoadingProgressChanged(float f) {
                            synchronized (PackageInstallerSession.this.mProgressLock) {
                                PackageInstallerSession.this.mIncrementalProgress = f;
                                PackageInstallerSession.this.computeProgressLocked(true);
                            }
                        }
                    });
                } else {
                    incrementalFileStorages.startLoading(dataLoaderParams, stub, storageHealthCheckParams, stub2, perUidReadTimeouts);
                }
                return false;
            } catch (IOException e) {
                throw new PackageManagerException(-20, e.getMessage(), e.getCause());
            }
        }
        if (getDataLoaderManager().bindToDataLoader(this.sessionId, dataLoaderParams.getData(), 0L, stub)) {
            return false;
        }
        throw new PackageManagerException(-20, "Failed to initialize data loader");
    }

    private DataLoaderManager getDataLoaderManager() throws PackageManagerException {
        DataLoaderManager dataLoaderManager = (DataLoaderManager) this.mContext.getSystemService(DataLoaderManager.class);
        if (dataLoaderManager != null) {
            return dataLoaderManager;
        }
        throw new PackageManagerException(-20, "Failed to find data loader manager service");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IDataLoader getDataLoader(int i) throws PackageManagerException {
        IDataLoader dataLoader = getDataLoaderManager().getDataLoader(i);
        if (dataLoader != null) {
            return dataLoader;
        }
        throw new PackageManagerException(-20, "Failure to obtain data loader");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchSessionValidationFailure(int i, String str) {
        this.mHandler.obtainMessage(5, i, -1, str).sendToTarget();
    }

    @GuardedBy({"mLock"})
    private int[] getChildSessionIdsLocked() {
        int size = this.mChildSessions.size();
        if (size == 0) {
            return EMPTY_CHILD_SESSION_ARRAY;
        }
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = this.mChildSessions.keyAt(i);
        }
        return iArr;
    }

    public int[] getChildSessionIds() {
        int[] childSessionIdsLocked;
        synchronized (this.mLock) {
            childSessionIdsLocked = getChildSessionIdsLocked();
        }
        return childSessionIdsLocked;
    }

    private boolean canBeAddedAsChild(int i) {
        boolean z;
        synchronized (this.mLock) {
            z = ((hasParentSessionId() && this.mParentSessionId != i) || isCommitted() || this.mDestroyed) ? false : true;
        }
        return z;
    }

    private void acquireTransactionLock() {
        if (!this.mTransactionLock.compareAndSet(false, true)) {
            throw new UnsupportedOperationException("Concurrent access not supported");
        }
    }

    private void releaseTransactionLock() {
        this.mTransactionLock.compareAndSet(true, false);
    }

    public void addChildSessionId(int i) {
        if (!this.params.isMultiPackage) {
            throw new IllegalStateException("Single-session " + this.sessionId + " can't have child.");
        }
        PackageInstallerSession session = this.mSessionProvider.getSession(i);
        if (session == null) {
            throw new IllegalStateException("Unable to add child session " + i + " as it does not exist.");
        }
        PackageInstaller.SessionParams sessionParams = session.params;
        if (sessionParams.isMultiPackage) {
            throw new IllegalStateException("Multi-session " + i + " can't be a child.");
        }
        PackageInstaller.SessionParams sessionParams2 = this.params;
        if (sessionParams2.isStaged != sessionParams.isStaged) {
            throw new IllegalStateException("Multipackage Inconsistency: session " + session.sessionId + " and session " + this.sessionId + " have inconsistent staged settings");
        }
        if (sessionParams2.getEnableRollback() != session.params.getEnableRollback()) {
            throw new IllegalStateException("Multipackage Inconsistency: session " + session.sessionId + " and session " + this.sessionId + " have inconsistent rollback settings");
        }
        boolean z = containsApkSession() || !session.isApexSession();
        boolean z2 = sessionContains(new Predicate() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isApexSession;
                isApexSession = ((PackageInstallerSession) obj).isApexSession();
                return isApexSession;
            }
        }) || session.isApexSession();
        if (!this.params.isStaged && z && z2) {
            throw new IllegalStateException("Mix of APK and APEX is not supported for non-staged multi-package session");
        }
        try {
            acquireTransactionLock();
            session.acquireTransactionLock();
            if (!session.canBeAddedAsChild(this.sessionId)) {
                throw new IllegalStateException("Unable to add child session " + i + " as it is in an invalid state.");
            }
            synchronized (this.mLock) {
                assertCallerIsOwnerOrRoot();
                assertPreparedAndNotSealedLocked("addChildSessionId");
                if (this.mChildSessions.indexOfKey(i) >= 0) {
                    return;
                }
                session.setParentSessionId(this.sessionId);
                this.mChildSessions.put(i, session);
            }
        } finally {
            releaseTransactionLock();
            session.releaseTransactionLock();
        }
    }

    public void removeChildSessionId(int i) {
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("removeChildSessionId");
            int indexOfKey = this.mChildSessions.indexOfKey(i);
            if (indexOfKey < 0) {
                return;
            }
            PackageInstallerSession valueAt = this.mChildSessions.valueAt(indexOfKey);
            try {
                acquireTransactionLock();
                valueAt.acquireTransactionLock();
                valueAt.setParentSessionId(-1);
                this.mChildSessions.removeAt(indexOfKey);
            } finally {
                releaseTransactionLock();
                valueAt.releaseTransactionLock();
            }
        }
    }

    void setParentSessionId(int i) {
        synchronized (this.mLock) {
            if (i != -1) {
                if (this.mParentSessionId != -1) {
                    throw new IllegalStateException("The parent of " + this.sessionId + " is alreadyset to " + this.mParentSessionId);
                }
            }
            this.mParentSessionId = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasParentSessionId() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mParentSessionId != -1;
        }
        return z;
    }

    public int getParentSessionId() {
        int i;
        synchronized (this.mLock) {
            i = this.mParentSessionId;
        }
        return i;
    }

    private void dispatchSessionFinished(int i, String str, Bundle bundle) {
        this.mPackageInstallerSessionExt.beforeDispatchSessionFinished(this);
        sendUpdateToRemoteStatusReceiver(i, str, bundle);
        synchronized (this.mLock) {
            this.mFinalStatus = i;
            this.mFinalMessage = str;
        }
        boolean z = i == 1;
        boolean z2 = bundle == null || !bundle.getBoolean("android.intent.extra.REPLACING");
        if (z && z2 && this.mPm.mInstallerService.okToSendBroadcasts()) {
            this.mPm.sendSessionCommitBroadcast(generateInfoScrubbed(true), this.userId);
        }
        this.mCallback.onSessionFinished(this, z);
        if (isDataLoaderInstallation()) {
            logDataLoaderInstallationSession(i);
        }
        this.mPackageInstallerSessionExt.afterDispatchSessionFinished(this, this.mPm);
    }

    private void sendUpdateToRemoteStatusReceiver(int i, String str, Bundle bundle) {
        IntentSender remoteStatusReceiver = getRemoteStatusReceiver();
        if (remoteStatusReceiver != null) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = getPackageName();
            obtain.arg2 = str;
            obtain.arg3 = bundle;
            obtain.arg4 = remoteStatusReceiver;
            obtain.argi1 = i;
            obtain.argi2 = (!isPreapprovalRequested() || isCommitted()) ? 0 : 1;
            this.mHandler.obtainMessage(4, obtain).sendToTarget();
        }
    }

    private void dispatchSessionPreappoved() {
        IntentSender remoteStatusReceiver = getRemoteStatusReceiver();
        Intent intent = new Intent();
        intent.putExtra("android.content.pm.extra.SESSION_ID", this.sessionId);
        intent.putExtra("android.content.pm.extra.STATUS", 0);
        intent.putExtra("android.content.pm.extra.PRE_APPROVAL", true);
        try {
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
            remoteStatusReceiver.sendIntent(this.mContext, 0, intent, null, null, null, makeBasic.toBundle());
        } catch (IntentSender.SendIntentException unused) {
        }
    }

    public void requestUserPreapproval(PackageInstaller.PreapprovalDetails preapprovalDetails, IntentSender intentSender) {
        validatePreapprovalRequest(preapprovalDetails, intentSender);
        this.mPackageInstallerSessionExt.beforeRequestUserPreapprovalAvailable(this.mPm.mRequiredInstallerPackage);
        if (!PackageManagerService.isPreapprovalRequestAvailable()) {
            sendUpdateToRemoteStatusReceiver(-129, "Request user pre-approval is currently not available.", null);
        } else {
            dispatchPreapprovalRequest();
        }
    }

    private void validatePreapprovalRequest(PackageInstaller.PreapprovalDetails preapprovalDetails, IntentSender intentSender) {
        assertCallerIsOwnerOrRoot();
        if (isMultiPackage()) {
            throw new IllegalStateException("Session " + this.sessionId + " is a parent of multi-package session and requestUserPreapproval on the parent session isn't supported.");
        }
        synchronized (this.mLock) {
            assertPreparedAndNotSealedLocked("request of session " + this.sessionId);
            this.mPreapprovalDetails = preapprovalDetails;
            setRemoteStatusReceiver(intentSender);
        }
    }

    private void dispatchPreapprovalRequest() {
        synchronized (this.mLock) {
            assertPreparedAndNotPreapprovalRequestedLocked("dispatchPreapprovalRequest");
        }
        markAsPreapprovalRequested();
        this.mHandler.obtainMessage(6).sendToTarget();
    }

    private void markAsPreapprovalRequested() {
        this.mPreapprovalRequested.set(true);
    }

    public boolean isApplicationEnabledSettingPersistent() {
        return this.params.applicationEnabledSettingPersistent;
    }

    public boolean isRequestUpdateOwnership() {
        return (this.params.installFlags & DumpState.DUMP_APEX) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSessionReady() {
        synchronized (this.mLock) {
            if (!this.mDestroyed && !this.mSessionFailed) {
                this.mSessionReady = true;
                this.mSessionApplied = false;
                this.mSessionFailed = false;
                this.mSessionErrorCode = 0;
                this.mSessionErrorMessage = "";
                this.mCallback.onSessionChanged(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSessionFailed(int i, String str) {
        synchronized (this.mLock) {
            if (!this.mDestroyed && !this.mSessionFailed) {
                this.mSessionReady = false;
                this.mSessionApplied = false;
                this.mSessionFailed = true;
                this.mSessionErrorCode = i;
                this.mSessionErrorMessage = str;
                Slog.d(TAG, "Marking session " + this.sessionId + " as failed: " + str);
                destroy();
                this.mCallback.onSessionChanged(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSessionApplied() {
        synchronized (this.mLock) {
            this.mPackageInstallerSessionExt.recordSotaAppResult(this.mStagedSession, 1, "");
            if (!this.mDestroyed && !this.mSessionFailed) {
                this.mSessionReady = false;
                this.mSessionApplied = true;
                this.mSessionFailed = false;
                this.mSessionErrorCode = 1;
                this.mSessionErrorMessage = "";
                Slog.d(TAG, "Marking session " + this.sessionId + " as applied");
                destroy();
                this.mCallback.onSessionChanged(this);
            }
        }
    }

    boolean isSessionReady() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSessionReady;
        }
        return z;
    }

    boolean isSessionApplied() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSessionApplied;
        }
        return z;
    }

    boolean isSessionFailed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSessionFailed;
        }
        return z;
    }

    int getSessionErrorCode() {
        int i;
        synchronized (this.mLock) {
            i = this.mSessionErrorCode;
        }
        return i;
    }

    String getSessionErrorMessage() {
        String str;
        synchronized (this.mLock) {
            str = this.mSessionErrorMessage;
        }
        return str;
    }

    private void destroy() {
        destroyInternal();
        Iterator<PackageInstallerSession> it = getChildSessions().iterator();
        while (it.hasNext()) {
            it.next().destroyInternal();
        }
    }

    private void destroyInternal() {
        IncrementalFileStorages incrementalFileStorages;
        synchronized (this.mLock) {
            this.mSealed = true;
            if (!this.params.isStaged) {
                this.mDestroyed = true;
            }
            Iterator<RevocableFileDescriptor> it = this.mFds.iterator();
            while (it.hasNext()) {
                it.next().revoke();
            }
            Iterator<FileBridge> it2 = this.mBridges.iterator();
            while (it2.hasNext()) {
                it2.next().forceClose();
            }
            incrementalFileStorages = this.mIncrementalFileStorages;
            this.mIncrementalFileStorages = null;
        }
        if (incrementalFileStorages != null) {
            try {
                incrementalFileStorages.cleanUpAndMarkComplete();
            } catch (Installer.InstallerException unused) {
                return;
            }
        }
        File file = this.stageDir;
        if (file != null) {
            this.mInstaller.rmPackageDir(file.getName(), this.stageDir.getAbsolutePath());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this.mLock) {
            dumpLocked(indentingPrintWriter);
        }
    }

    @GuardedBy({"mLock"})
    private void dumpLocked(IndentingPrintWriter indentingPrintWriter) {
        float f;
        float f2;
        indentingPrintWriter.println("Session " + this.sessionId + ":");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.printPair("userId", Integer.valueOf(this.userId));
        indentingPrintWriter.printPair("mOriginalInstallerUid", Integer.valueOf(this.mOriginalInstallerUid));
        indentingPrintWriter.printPair("mOriginalInstallerPackageName", this.mOriginalInstallerPackageName);
        indentingPrintWriter.printPair(ATTR_INSTALLER_PACKAGE_NAME, this.mInstallSource.mInstallerPackageName);
        indentingPrintWriter.printPair(ATTR_INITIATING_PACKAGE_NAME, this.mInstallSource.mInitiatingPackageName);
        indentingPrintWriter.printPair(ATTR_ORIGINATING_PACKAGE_NAME, this.mInstallSource.mOriginatingPackageName);
        indentingPrintWriter.printPair("mInstallerUid", Integer.valueOf(this.mInstallerUid));
        indentingPrintWriter.printPair(ATTR_CREATED_MILLIS, Long.valueOf(this.createdMillis));
        indentingPrintWriter.printPair(ATTR_UPDATED_MILLIS, Long.valueOf(this.updatedMillis));
        indentingPrintWriter.printPair(ATTR_COMMITTED_MILLIS, Long.valueOf(this.committedMillis));
        indentingPrintWriter.printPair("stageDir", this.stageDir);
        indentingPrintWriter.printPair("stageCid", this.stageCid);
        indentingPrintWriter.println();
        this.params.dump(indentingPrintWriter);
        synchronized (this.mProgressLock) {
            f = this.mClientProgress;
            f2 = this.mProgress;
        }
        indentingPrintWriter.printPair("mClientProgress", Float.valueOf(f));
        indentingPrintWriter.printPair("mProgress", Float.valueOf(f2));
        indentingPrintWriter.printPair("mCommitted", this.mCommitted);
        indentingPrintWriter.printPair("mPreapprovalRequested", this.mPreapprovalRequested);
        indentingPrintWriter.printPair("mSealed", Boolean.valueOf(this.mSealed));
        indentingPrintWriter.printPair("mPermissionsManuallyAccepted", Boolean.valueOf(this.mPermissionsManuallyAccepted));
        indentingPrintWriter.printPair("mStageDirInUse", Boolean.valueOf(this.mStageDirInUse));
        indentingPrintWriter.printPair("mDestroyed", Boolean.valueOf(this.mDestroyed));
        indentingPrintWriter.printPair("mFds", Integer.valueOf(this.mFds.size()));
        indentingPrintWriter.printPair("mBridges", Integer.valueOf(this.mBridges.size()));
        indentingPrintWriter.printPair("mFinalStatus", Integer.valueOf(this.mFinalStatus));
        indentingPrintWriter.printPair("mFinalMessage", this.mFinalMessage);
        indentingPrintWriter.printPair("params.isMultiPackage", Boolean.valueOf(this.params.isMultiPackage));
        indentingPrintWriter.printPair("params.isStaged", Boolean.valueOf(this.params.isStaged));
        indentingPrintWriter.printPair("mParentSessionId", Integer.valueOf(this.mParentSessionId));
        indentingPrintWriter.printPair("mChildSessionIds", getChildSessionIdsLocked());
        indentingPrintWriter.printPair("mSessionApplied", Boolean.valueOf(this.mSessionApplied));
        indentingPrintWriter.printPair("mSessionFailed", Boolean.valueOf(this.mSessionFailed));
        indentingPrintWriter.printPair("mSessionReady", Boolean.valueOf(this.mSessionReady));
        indentingPrintWriter.printPair("mSessionErrorCode", Integer.valueOf(this.mSessionErrorCode));
        indentingPrintWriter.printPair("mSessionErrorMessage", this.mSessionErrorMessage);
        indentingPrintWriter.printPair("mPreapprovalDetails", this.mPreapprovalDetails);
        indentingPrintWriter.println();
        indentingPrintWriter.decreaseIndent();
    }

    private static void sendOnUserActionRequired(Context context, IntentSender intentSender, int i, Intent intent) {
        Intent intent2 = new Intent();
        intent2.putExtra("android.content.pm.extra.SESSION_ID", i);
        intent2.putExtra("android.content.pm.extra.STATUS", -1);
        intent2.putExtra("android.content.pm.extra.PRE_APPROVAL", "android.content.pm.action.CONFIRM_PRE_APPROVAL".equals(intent.getAction()));
        intent2.putExtra("android.intent.extra.INTENT", intent);
        try {
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
            intentSender.sendIntent(context, 0, intent2, null, null, null, makeBasic.toBundle());
        } catch (IntentSender.SendIntentException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendOnPackageInstalled(Context context, IntentSender intentSender, int i, boolean z, int i2, String str, int i3, boolean z2, String str2, Bundle bundle) {
        if (1 == i3 && z) {
            Notification buildSuccessNotification = PackageInstallerService.buildSuccessNotification(context, getDeviceOwnerInstalledPackageMsg(context, bundle != null && bundle.getBoolean("android.intent.extra.REPLACING")), str, i2);
            if (buildSuccessNotification != null) {
                ((NotificationManager) context.getSystemService("notification")).notify(str, 21, buildSuccessNotification);
            }
        }
        Intent intent = new Intent();
        intent.putExtra("android.content.pm.extra.PACKAGE_NAME", str);
        intent.putExtra("android.content.pm.extra.SESSION_ID", i);
        intent.putExtra("android.content.pm.extra.STATUS", PackageManager.installStatusToPublicStatus(i3));
        intent.putExtra("android.content.pm.extra.STATUS_MESSAGE", PackageManager.installStatusToString(i3, str2));
        intent.putExtra("android.content.pm.extra.LEGACY_STATUS", i3);
        intent.putExtra("android.content.pm.extra.PRE_APPROVAL", z2);
        if (bundle != null) {
            String string = bundle.getString("android.content.pm.extra.FAILURE_EXISTING_PACKAGE");
            if (!TextUtils.isEmpty(string)) {
                intent.putExtra("android.content.pm.extra.OTHER_PACKAGE_NAME", string);
            }
        }
        try {
            if (PackageManagerService.DEBUG_INSTALL) {
                Slog.d(TAG, "sendIntent in sendOnPackageInstalled: " + i + ", " + i2 + ", " + str + ", " + i3 + ", " + str2);
            }
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
            intentSender.sendIntent(context, 0, intent, null, null, null, makeBasic.toBundle());
        } catch (IntentSender.SendIntentException unused) {
        }
    }

    private static String getDeviceOwnerInstalledPackageMsg(final Context context, boolean z) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        if (z) {
            return devicePolicyManager.getResources().getString("Core.PACKAGE_UPDATED_BY_DO", new Supplier() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda9
                @Override // java.util.function.Supplier
                public final Object get() {
                    String string;
                    string = context.getString(R.string.permlab_useFingerprint);
                    return string;
                }
            });
        }
        return devicePolicyManager.getResources().getString("Core.PACKAGE_INSTALLED_BY_DO", new Supplier() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda10
            @Override // java.util.function.Supplier
            public final Object get() {
                String string;
                string = context.getString(R.string.permlab_useFaceAuthentication);
                return string;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendPendingStreaming(Context context, IntentSender intentSender, int i, String str) {
        if (intentSender == null) {
            Slog.e(TAG, "Missing receiver for pending streaming status.");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("android.content.pm.extra.SESSION_ID", i);
        intent.putExtra("android.content.pm.extra.STATUS", -2);
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("android.content.pm.extra.STATUS_MESSAGE", "Staging Image Not Ready [" + str + "]");
        } else {
            intent.putExtra("android.content.pm.extra.STATUS_MESSAGE", "Staging Image Not Ready");
        }
        try {
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
            intentSender.sendIntent(context, 0, intent, null, null, null, makeBasic.toBundle());
        } catch (IntentSender.SendIntentException unused) {
        }
    }

    private static void writePermissionsLocked(TypedXmlSerializer typedXmlSerializer, PackageInstaller.SessionParams sessionParams) throws IOException {
        ArrayMap permissionStates = sessionParams.getPermissionStates();
        for (int i = 0; i < permissionStates.size(); i++) {
            String str = (String) permissionStates.keyAt(i);
            String str2 = ((Integer) permissionStates.valueAt(i)).intValue() == 1 ? TAG_GRANT_PERMISSION : TAG_DENY_PERMISSION;
            typedXmlSerializer.startTag((String) null, str2);
            XmlUtils.writeStringAttribute(typedXmlSerializer, "name", str);
            typedXmlSerializer.endTag((String) null, str2);
        }
    }

    private static void writeWhitelistedRestrictedPermissionsLocked(TypedXmlSerializer typedXmlSerializer, List<String> list) throws IOException {
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                typedXmlSerializer.startTag((String) null, TAG_WHITELISTED_RESTRICTED_PERMISSION);
                XmlUtils.writeStringAttribute(typedXmlSerializer, "name", list.get(i));
                typedXmlSerializer.endTag((String) null, TAG_WHITELISTED_RESTRICTED_PERMISSION);
            }
        }
    }

    private static void writeAutoRevokePermissionsMode(TypedXmlSerializer typedXmlSerializer, int i) throws IOException {
        typedXmlSerializer.startTag((String) null, TAG_AUTO_REVOKE_PERMISSIONS_MODE);
        typedXmlSerializer.attributeInt((String) null, ATTR_MODE, i);
        typedXmlSerializer.endTag((String) null, TAG_AUTO_REVOKE_PERMISSIONS_MODE);
    }

    private static File buildAppIconFile(int i, File file) {
        return new File(file, "app_icon." + i + ".png");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0269 A[Catch: all -> 0x0359, LOOP:0: B:29:0x0267->B:30:0x0269, LOOP_END, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x000d, B:11:0x000f, B:13:0x007e, B:14:0x0088, B:16:0x008c, B:17:0x0092, B:20:0x0180, B:22:0x0187, B:23:0x01c3, B:25:0x01e2, B:27:0x01e8, B:28:0x0261, B:30:0x0269, B:32:0x027e, B:34:0x0286, B:36:0x02c7, B:38:0x02d0, B:40:0x02e8, B:42:0x0311, B:44:0x0314, B:46:0x031c, B:48:0x0332, B:53:0x0336, B:52:0x034e, B:57:0x0351, B:60:0x01ed, B:62:0x01f3, B:64:0x01ff, B:69:0x0225, B:70:0x0252, B:77:0x025d, B:78:0x0260), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0286 A[Catch: all -> 0x0359, LOOP:1: B:33:0x0284->B:34:0x0286, LOOP_END, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x000d, B:11:0x000f, B:13:0x007e, B:14:0x0088, B:16:0x008c, B:17:0x0092, B:20:0x0180, B:22:0x0187, B:23:0x01c3, B:25:0x01e2, B:27:0x01e8, B:28:0x0261, B:30:0x0269, B:32:0x027e, B:34:0x0286, B:36:0x02c7, B:38:0x02d0, B:40:0x02e8, B:42:0x0311, B:44:0x0314, B:46:0x031c, B:48:0x0332, B:53:0x0336, B:52:0x034e, B:57:0x0351, B:60:0x01ed, B:62:0x01f3, B:64:0x01ff, B:69:0x0225, B:70:0x0252, B:77:0x025d, B:78:0x0260), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x02d0 A[Catch: all -> 0x0359, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x000d, B:11:0x000f, B:13:0x007e, B:14:0x0088, B:16:0x008c, B:17:0x0092, B:20:0x0180, B:22:0x0187, B:23:0x01c3, B:25:0x01e2, B:27:0x01e8, B:28:0x0261, B:30:0x0269, B:32:0x027e, B:34:0x0286, B:36:0x02c7, B:38:0x02d0, B:40:0x02e8, B:42:0x0311, B:44:0x0314, B:46:0x031c, B:48:0x0332, B:53:0x0336, B:52:0x034e, B:57:0x0351, B:60:0x01ed, B:62:0x01f3, B:64:0x01ff, B:69:0x0225, B:70:0x0252, B:77:0x025d, B:78:0x0260), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x031c A[Catch: all -> 0x0359, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x000d, B:11:0x000f, B:13:0x007e, B:14:0x0088, B:16:0x008c, B:17:0x0092, B:20:0x0180, B:22:0x0187, B:23:0x01c3, B:25:0x01e2, B:27:0x01e8, B:28:0x0261, B:30:0x0269, B:32:0x027e, B:34:0x0286, B:36:0x02c7, B:38:0x02d0, B:40:0x02e8, B:42:0x0311, B:44:0x0314, B:46:0x031c, B:48:0x0332, B:53:0x0336, B:52:0x034e, B:57:0x0351, B:60:0x01ed, B:62:0x01f3, B:64:0x01ff, B:69:0x0225, B:70:0x0252, B:77:0x025d, B:78:0x0260), top: B:3:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void write(TypedXmlSerializer typedXmlSerializer, File file) throws IOException {
        FileOutputStream fileOutputStream;
        int size;
        int i;
        int size2;
        synchronized (this.mLock) {
            if (!this.mDestroyed || this.params.isStaged) {
                FileOutputStream fileOutputStream2 = null;
                typedXmlSerializer.startTag((String) null, TAG_SESSION);
                typedXmlSerializer.attributeInt((String) null, ATTR_SESSION_ID, this.sessionId);
                typedXmlSerializer.attributeInt((String) null, "userId", this.userId);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_INSTALLER_PACKAGE_NAME, this.mInstallSource.mInstallerPackageName);
                typedXmlSerializer.attributeInt((String) null, ATTR_INSTALLER_PACKAGE_UID, this.mInstallSource.mInstallerPackageUid);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_UPDATE_OWNER_PACKAGE_NAME, this.mInstallSource.mUpdateOwnerPackageName);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_INSTALLER_ATTRIBUTION_TAG, this.mInstallSource.mInstallerAttributionTag);
                typedXmlSerializer.attributeInt((String) null, ATTR_INSTALLER_UID, this.mInstallerUid);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_INITIATING_PACKAGE_NAME, this.mInstallSource.mInitiatingPackageName);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_ORIGINATING_PACKAGE_NAME, this.mInstallSource.mOriginatingPackageName);
                typedXmlSerializer.attributeLong((String) null, ATTR_CREATED_MILLIS, this.createdMillis);
                typedXmlSerializer.attributeLong((String) null, ATTR_UPDATED_MILLIS, this.updatedMillis);
                typedXmlSerializer.attributeLong((String) null, ATTR_COMMITTED_MILLIS, this.committedMillis);
                File file2 = this.stageDir;
                if (file2 != null) {
                    XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_SESSION_STAGE_DIR, file2.getAbsolutePath());
                }
                String str = this.stageCid;
                if (str != null) {
                    XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_SESSION_STAGE_CID, str);
                }
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_PREPARED, this.mPrepared);
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_COMMITTED, isCommitted());
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_DESTROYED, this.mDestroyed);
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_SEALED, this.mSealed);
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_MULTI_PACKAGE, this.params.isMultiPackage);
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_STAGED_SESSION, this.params.isStaged);
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_IS_READY, this.mSessionReady);
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_IS_FAILED, this.mSessionFailed);
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_IS_APPLIED, this.mSessionApplied);
                typedXmlSerializer.attributeInt((String) null, ATTR_PACKAGE_SOURCE, this.params.packageSource);
                typedXmlSerializer.attributeInt((String) null, ATTR_SESSION_ERROR_CODE, this.mSessionErrorCode);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_SESSION_ERROR_MESSAGE, this.mSessionErrorMessage);
                typedXmlSerializer.attributeInt((String) null, ATTR_PARENT_SESSION_ID, this.mParentSessionId);
                typedXmlSerializer.attributeInt((String) null, ATTR_MODE, this.params.mode);
                typedXmlSerializer.attributeInt((String) null, ATTR_INSTALL_FLAGS, this.params.installFlags);
                typedXmlSerializer.attributeInt((String) null, ATTR_INSTALL_LOCATION, this.params.installLocation);
                typedXmlSerializer.attributeLong((String) null, ATTR_SIZE_BYTES, this.params.sizeBytes);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_APP_PACKAGE_NAME, this.params.appPackageName);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_APP_LABEL, this.params.appLabel);
                XmlUtils.writeUriAttribute(typedXmlSerializer, ATTR_ORIGINATING_URI, this.params.originatingUri);
                typedXmlSerializer.attributeInt((String) null, ATTR_ORIGINATING_UID, this.params.originatingUid);
                XmlUtils.writeUriAttribute(typedXmlSerializer, ATTR_REFERRER_URI, this.params.referrerUri);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_ABI_OVERRIDE, this.params.abiOverride);
                XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_VOLUME_UUID, this.params.volumeUuid);
                typedXmlSerializer.attributeInt((String) null, ATTR_INSTALL_REASON, this.params.installReason);
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_APPLICATION_ENABLED_SETTING_PERSISTENT, this.params.applicationEnabledSettingPersistent);
                boolean z = this.params.dataLoaderParams != null;
                XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_IS_DATALOADER, z);
                if (z) {
                    typedXmlSerializer.attributeInt((String) null, ATTR_DATALOADER_TYPE, this.params.dataLoaderParams.getType());
                    XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_DATALOADER_PACKAGE_NAME, this.params.dataLoaderParams.getComponentName().getPackageName());
                    XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_DATALOADER_CLASS_NAME, this.params.dataLoaderParams.getComponentName().getClassName());
                    XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_DATALOADER_ARGUMENTS, this.params.dataLoaderParams.getArguments());
                }
                writePermissionsLocked(typedXmlSerializer, this.params);
                writeWhitelistedRestrictedPermissionsLocked(typedXmlSerializer, this.params.whitelistedRestrictedPermissions);
                writeAutoRevokePermissionsMode(typedXmlSerializer, this.params.autoRevokePermissionsMode);
                File buildAppIconFile = buildAppIconFile(this.sessionId, file);
                if (this.params.appIcon == null && buildAppIconFile.exists()) {
                    buildAppIconFile.delete();
                } else if (this.params.appIcon != null && buildAppIconFile.lastModified() != this.params.appIconLastModified) {
                    Slog.w(TAG, "Writing changed icon " + buildAppIconFile);
                    try {
                        fileOutputStream = new FileOutputStream(buildAppIconFile);
                    } catch (IOException e) {
                        e = e;
                        fileOutputStream = null;
                    } catch (Throwable th) {
                        th = th;
                        IoUtils.closeQuietly(fileOutputStream2);
                        throw th;
                    }
                    try {
                        try {
                            this.params.appIcon.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                        } catch (Throwable th2) {
                            th = th2;
                            fileOutputStream2 = fileOutputStream;
                            IoUtils.closeQuietly(fileOutputStream2);
                            throw th;
                        }
                    } catch (IOException e2) {
                        e = e2;
                        Slog.w(TAG, "Failed to write icon " + buildAppIconFile + ": " + e.getMessage());
                        IoUtils.closeQuietly(fileOutputStream);
                        this.params.appIconLastModified = buildAppIconFile.lastModified();
                        while (r4 < r1) {
                        }
                        while (r4 < r1) {
                        }
                        size = this.mChecksums.size();
                        while (i < size) {
                        }
                        size2 = this.mChecksums.size();
                        while (r3 < size2) {
                        }
                        typedXmlSerializer.endTag((String) null, TAG_SESSION);
                    }
                    IoUtils.closeQuietly(fileOutputStream);
                    this.params.appIconLastModified = buildAppIconFile.lastModified();
                }
                for (int i2 : getChildSessionIdsLocked()) {
                    typedXmlSerializer.startTag((String) null, TAG_CHILD_SESSION);
                    typedXmlSerializer.attributeInt((String) null, ATTR_SESSION_ID, i2);
                    typedXmlSerializer.endTag((String) null, TAG_CHILD_SESSION);
                }
                for (InstallationFile installationFile : getInstallationFilesLocked()) {
                    typedXmlSerializer.startTag((String) null, TAG_SESSION_FILE);
                    typedXmlSerializer.attributeInt((String) null, ATTR_LOCATION, installationFile.getLocation());
                    XmlUtils.writeStringAttribute(typedXmlSerializer, "name", installationFile.getName());
                    typedXmlSerializer.attributeLong((String) null, ATTR_LENGTH_BYTES, installationFile.getLengthBytes());
                    XmlUtils.writeByteArrayAttribute(typedXmlSerializer, ATTR_METADATA, installationFile.getMetadata());
                    XmlUtils.writeByteArrayAttribute(typedXmlSerializer, ATTR_SIGNATURE, installationFile.getSignature());
                    typedXmlSerializer.endTag((String) null, TAG_SESSION_FILE);
                }
                size = this.mChecksums.size();
                for (i = 0; i < size; i++) {
                    String keyAt = this.mChecksums.keyAt(i);
                    for (Checksum checksum : this.mChecksums.valueAt(i).getChecksums()) {
                        typedXmlSerializer.startTag((String) null, TAG_SESSION_CHECKSUM);
                        XmlUtils.writeStringAttribute(typedXmlSerializer, "name", keyAt);
                        typedXmlSerializer.attributeInt((String) null, ATTR_CHECKSUM_KIND, checksum.getType());
                        XmlUtils.writeByteArrayAttribute(typedXmlSerializer, ATTR_CHECKSUM_VALUE, checksum.getValue());
                        typedXmlSerializer.endTag((String) null, TAG_SESSION_CHECKSUM);
                    }
                }
                size2 = this.mChecksums.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    String keyAt2 = this.mChecksums.keyAt(i3);
                    byte[] signature = this.mChecksums.valueAt(i3).getSignature();
                    if (signature != null && signature.length != 0) {
                        typedXmlSerializer.startTag((String) null, TAG_SESSION_CHECKSUM_SIGNATURE);
                        XmlUtils.writeStringAttribute(typedXmlSerializer, "name", keyAt2);
                        XmlUtils.writeByteArrayAttribute(typedXmlSerializer, ATTR_SIGNATURE, signature);
                        typedXmlSerializer.endTag((String) null, TAG_SESSION_CHECKSUM_SIGNATURE);
                    }
                }
                typedXmlSerializer.endTag((String) null, TAG_SESSION);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:55:0x027d. Please report as an issue. */
    public static PackageInstallerSession readFromXml(TypedXmlPullParser typedXmlPullParser, PackageInstallerService.InternalCallback internalCallback, Context context, PackageManagerService packageManagerService, Looper looper, StagingManager stagingManager, File file, PackageSessionProvider packageSessionProvider, SilentUpdatePolicy silentUpdatePolicy) throws IOException, XmlPullParserException {
        int i;
        String str;
        int[] iArr;
        ArrayMap arrayMap;
        int i2;
        ArrayMap arrayMap2;
        String str2;
        IntArray intArray;
        int i3;
        int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_SESSION_ID);
        int attributeInt2 = typedXmlPullParser.getAttributeInt((String) null, "userId");
        String readStringAttribute = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_INSTALLER_PACKAGE_NAME);
        int attributeInt3 = typedXmlPullParser.getAttributeInt((String) null, ATTR_INSTALLER_PACKAGE_UID, -1);
        String readStringAttribute2 = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_UPDATE_OWNER_PACKAGE_NAME);
        String readStringAttribute3 = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_INSTALLER_ATTRIBUTION_TAG);
        int attributeInt4 = typedXmlPullParser.getAttributeInt((String) null, ATTR_INSTALLER_UID, packageManagerService.snapshotComputer().getPackageUid(readStringAttribute, 8192L, attributeInt2));
        String readStringAttribute4 = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_INITIATING_PACKAGE_NAME);
        String readStringAttribute5 = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_ORIGINATING_PACKAGE_NAME);
        long attributeLong = typedXmlPullParser.getAttributeLong((String) null, ATTR_CREATED_MILLIS);
        typedXmlPullParser.getAttributeLong((String) null, ATTR_UPDATED_MILLIS);
        long attributeLong2 = typedXmlPullParser.getAttributeLong((String) null, ATTR_COMMITTED_MILLIS, 0L);
        String readStringAttribute6 = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_SESSION_STAGE_DIR);
        File file2 = readStringAttribute6 != null ? new File(readStringAttribute6) : null;
        String readStringAttribute7 = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_SESSION_STAGE_CID);
        boolean attributeBoolean = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_PREPARED, true);
        boolean attributeBoolean2 = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_COMMITTED, false);
        boolean attributeBoolean3 = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_DESTROYED, false);
        boolean attributeBoolean4 = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_SEALED, false);
        int attributeInt5 = typedXmlPullParser.getAttributeInt((String) null, ATTR_PARENT_SESSION_ID, -1);
        PackageInstaller.SessionParams sessionParams = new PackageInstaller.SessionParams(-1);
        sessionParams.isMultiPackage = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_MULTI_PACKAGE, false);
        sessionParams.isStaged = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_STAGED_SESSION, false);
        String str3 = ATTR_MODE;
        sessionParams.mode = typedXmlPullParser.getAttributeInt((String) null, ATTR_MODE);
        sessionParams.installFlags = typedXmlPullParser.getAttributeInt((String) null, ATTR_INSTALL_FLAGS);
        sessionParams.installLocation = typedXmlPullParser.getAttributeInt((String) null, ATTR_INSTALL_LOCATION);
        sessionParams.sizeBytes = typedXmlPullParser.getAttributeLong((String) null, ATTR_SIZE_BYTES);
        sessionParams.appPackageName = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_APP_PACKAGE_NAME);
        sessionParams.appIcon = XmlUtils.readBitmapAttribute(typedXmlPullParser, ATTR_APP_ICON);
        sessionParams.appLabel = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_APP_LABEL);
        sessionParams.originatingUri = XmlUtils.readUriAttribute(typedXmlPullParser, ATTR_ORIGINATING_URI);
        sessionParams.originatingUid = typedXmlPullParser.getAttributeInt((String) null, ATTR_ORIGINATING_UID, -1);
        sessionParams.referrerUri = XmlUtils.readUriAttribute(typedXmlPullParser, ATTR_REFERRER_URI);
        sessionParams.abiOverride = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_ABI_OVERRIDE);
        sessionParams.volumeUuid = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_VOLUME_UUID);
        sessionParams.installReason = typedXmlPullParser.getAttributeInt((String) null, ATTR_INSTALL_REASON);
        sessionParams.packageSource = typedXmlPullParser.getAttributeInt((String) null, ATTR_PACKAGE_SOURCE);
        sessionParams.applicationEnabledSettingPersistent = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_APPLICATION_ENABLED_SETTING_PERSISTENT, false);
        if (typedXmlPullParser.getAttributeBoolean((String) null, ATTR_IS_DATALOADER, false)) {
            i = attributeInt4;
            sessionParams.dataLoaderParams = new DataLoaderParams(typedXmlPullParser.getAttributeInt((String) null, ATTR_DATALOADER_TYPE), new ComponentName(XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_DATALOADER_PACKAGE_NAME), XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_DATALOADER_CLASS_NAME)), XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_DATALOADER_ARGUMENTS));
        } else {
            i = attributeInt4;
        }
        File buildAppIconFile = buildAppIconFile(attributeInt, file);
        if (buildAppIconFile.exists()) {
            sessionParams.appIcon = BitmapFactory.decodeFile(buildAppIconFile.getAbsolutePath());
            sessionParams.appIconLastModified = buildAppIconFile.lastModified();
        }
        boolean attributeBoolean5 = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_IS_READY, false);
        boolean attributeBoolean6 = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_IS_FAILED, false);
        boolean attributeBoolean7 = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_IS_APPLIED, false);
        int attributeInt6 = typedXmlPullParser.getAttributeInt((String) null, ATTR_SESSION_ERROR_CODE, 0);
        String readStringAttribute8 = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_SESSION_ERROR_MESSAGE);
        if (!isStagedSessionStateValid(attributeBoolean5, attributeBoolean7, attributeBoolean6)) {
            throw new IllegalArgumentException("Can't restore staged session with invalid state.");
        }
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        ArraySet arraySet2 = new ArraySet();
        ArrayList arrayList2 = new ArrayList();
        IntArray intArray2 = new IntArray();
        ArrayList arrayList3 = new ArrayList();
        ArrayMap arrayMap3 = new ArrayMap();
        ArrayMap arrayMap4 = new ArrayMap();
        int depth = typedXmlPullParser.getDepth();
        int i4 = 3;
        int i5 = 3;
        while (true) {
            int next = typedXmlPullParser.next();
            str = readStringAttribute4;
            if (next != 1 && (next != i4 || typedXmlPullParser.getDepth() > depth)) {
                if (next == i4 || next == 4) {
                    i2 = depth;
                    arrayMap2 = arrayMap3;
                    str2 = str3;
                    intArray = intArray2;
                } else {
                    String name = typedXmlPullParser.getName();
                    name.hashCode();
                    switch (name.hashCode()) {
                        case -1558680102:
                            if (name.equals(TAG_CHILD_SESSION)) {
                                i3 = 0;
                                break;
                            }
                            break;
                        case -1361644609:
                            if (name.equals(TAG_SESSION_CHECKSUM_SIGNATURE)) {
                                i3 = 1;
                                break;
                            }
                            break;
                        case -606495946:
                            if (name.equals(TAG_GRANTED_RUNTIME_PERMISSION)) {
                                i3 = 2;
                                break;
                            }
                            break;
                        case -22892238:
                            if (name.equals(TAG_SESSION_FILE)) {
                                i3 = i4;
                                break;
                            }
                            break;
                        case 158177050:
                            if (name.equals(TAG_WHITELISTED_RESTRICTED_PERMISSION)) {
                                i3 = 4;
                                break;
                            }
                            break;
                        case 641551609:
                            if (name.equals(TAG_SESSION_CHECKSUM)) {
                                i3 = 5;
                                break;
                            }
                            break;
                        case 1529564053:
                            if (name.equals(TAG_AUTO_REVOKE_PERMISSIONS_MODE)) {
                                i3 = 6;
                                break;
                            }
                            break;
                        case 1620305696:
                            if (name.equals(TAG_GRANT_PERMISSION)) {
                                i3 = 7;
                                break;
                            }
                            break;
                        case 1658008624:
                            if (name.equals(TAG_DENY_PERMISSION)) {
                                i3 = 8;
                                break;
                            }
                            break;
                    }
                    i3 = -1;
                    switch (i3) {
                        case 0:
                            i2 = depth;
                            arrayMap2 = arrayMap3;
                            str2 = str3;
                            intArray = intArray2;
                            intArray.add(typedXmlPullParser.getAttributeInt((String) null, ATTR_SESSION_ID, -1));
                            break;
                        case 1:
                            i2 = depth;
                            arrayMap2 = arrayMap3;
                            str2 = str3;
                            intArray = intArray2;
                            arrayMap4.put(XmlUtils.readStringAttribute(typedXmlPullParser, "name"), XmlUtils.readByteArrayAttribute(typedXmlPullParser, ATTR_SIGNATURE));
                            break;
                        case 2:
                            i2 = depth;
                            arrayMap2 = arrayMap3;
                            str2 = str3;
                            intArray = intArray2;
                            arrayList.add(XmlUtils.readStringAttribute(typedXmlPullParser, "name"));
                            break;
                        case 3:
                            i2 = depth;
                            str2 = str3;
                            arrayMap2 = arrayMap3;
                            intArray = intArray2;
                            arrayList3.add(new InstallationFile(typedXmlPullParser.getAttributeInt((String) null, ATTR_LOCATION, 0), XmlUtils.readStringAttribute(typedXmlPullParser, "name"), typedXmlPullParser.getAttributeLong((String) null, ATTR_LENGTH_BYTES, -1L), XmlUtils.readByteArrayAttribute(typedXmlPullParser, ATTR_METADATA), XmlUtils.readByteArrayAttribute(typedXmlPullParser, ATTR_SIGNATURE)));
                            break;
                        case 4:
                            i2 = depth;
                            str2 = str3;
                            arrayList2.add(XmlUtils.readStringAttribute(typedXmlPullParser, "name"));
                            arrayMap2 = arrayMap3;
                            intArray = intArray2;
                            break;
                        case 5:
                            String readStringAttribute9 = XmlUtils.readStringAttribute(typedXmlPullParser, "name");
                            i2 = depth;
                            str2 = str3;
                            Checksum checksum = new Checksum(typedXmlPullParser.getAttributeInt((String) null, ATTR_CHECKSUM_KIND, 0), XmlUtils.readByteArrayAttribute(typedXmlPullParser, ATTR_CHECKSUM_VALUE));
                            List list = (List) arrayMap3.get(readStringAttribute9);
                            if (list == null) {
                                list = new ArrayList();
                                arrayMap3.put(readStringAttribute9, list);
                            }
                            list.add(checksum);
                            arrayMap2 = arrayMap3;
                            intArray = intArray2;
                            break;
                        case 6:
                            i5 = typedXmlPullParser.getAttributeInt((String) null, str3);
                            i2 = depth;
                            arrayMap2 = arrayMap3;
                            str2 = str3;
                            intArray = intArray2;
                            break;
                        case 7:
                            arraySet.add(XmlUtils.readStringAttribute(typedXmlPullParser, "name"));
                            i2 = depth;
                            arrayMap2 = arrayMap3;
                            str2 = str3;
                            intArray = intArray2;
                            break;
                        case 8:
                            arraySet2.add(XmlUtils.readStringAttribute(typedXmlPullParser, "name"));
                            i2 = depth;
                            arrayMap2 = arrayMap3;
                            str2 = str3;
                            intArray = intArray2;
                            break;
                        default:
                            i2 = depth;
                            arrayMap2 = arrayMap3;
                            str2 = str3;
                            intArray = intArray2;
                            break;
                    }
                    intArray2 = intArray;
                    arrayMap3 = arrayMap2;
                    readStringAttribute4 = str;
                    depth = i2;
                    str3 = str2;
                    i4 = 3;
                }
                intArray2 = intArray;
                arrayMap3 = arrayMap2;
                readStringAttribute4 = str;
                depth = i2;
                str3 = str2;
                i4 = 3;
            }
        }
        ArrayMap arrayMap5 = arrayMap3;
        IntArray intArray3 = intArray2;
        if (arrayList.size() > 0) {
            sessionParams.setPermissionStates(arrayList, Collections.emptyList());
        } else {
            sessionParams.setPermissionStates(arraySet, arraySet2);
        }
        if (arrayList2.size() > 0) {
            sessionParams.whitelistedRestrictedPermissions = arrayList2;
        }
        sessionParams.autoRevokePermissionsMode = i5;
        if (intArray3.size() > 0) {
            iArr = new int[intArray3.size()];
            int size = intArray3.size();
            for (int i6 = 0; i6 < size; i6++) {
                iArr[i6] = intArray3.get(i6);
            }
        } else {
            iArr = EMPTY_CHILD_SESSION_ARRAY;
        }
        int[] iArr2 = iArr;
        InstallationFile[] installationFileArr = !arrayList3.isEmpty() ? (InstallationFile[]) arrayList3.toArray(EMPTY_INSTALLATION_FILE_ARRAY) : null;
        if (arrayMap5.isEmpty()) {
            arrayMap = null;
        } else {
            ArrayMap arrayMap6 = new ArrayMap(arrayMap5.size());
            int size2 = arrayMap5.size();
            for (int i7 = 0; i7 < size2; i7++) {
                String str4 = (String) arrayMap5.keyAt(i7);
                List list2 = (List) arrayMap5.valueAt(i7);
                arrayMap6.put(str4, new PerFileChecksum((Checksum[]) list2.toArray(new Checksum[list2.size()]), (byte[]) arrayMap4.get(str4)));
            }
            arrayMap = arrayMap6;
        }
        return new PackageInstallerSession(internalCallback, context, packageManagerService, packageSessionProvider, silentUpdatePolicy, looper, stagingManager, attributeInt, attributeInt2, i, InstallSource.create(str, readStringAttribute5, readStringAttribute, attributeInt3, readStringAttribute2, readStringAttribute3, sessionParams.packageSource), sessionParams, attributeLong, attributeLong2, file2, readStringAttribute7, installationFileArr, arrayMap, attributeBoolean, attributeBoolean2, attributeBoolean3, attributeBoolean4, iArr2, attributeInt5, attributeBoolean5, attributeBoolean6, attributeBoolean7, attributeInt6, readStringAttribute8);
    }

    public IPackageInstallerSessionWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class PackageInstallerSessionWrapper implements IPackageInstallerSessionWrapper {
        private PackageInstallerSessionWrapper() {
        }

        @Override // com.android.server.pm.IPackageInstallerSessionWrapper
        public void extractNativeLibraries(PackageLite packageLite, File file, String str, boolean z) throws PackageManagerException {
            PackageInstallerSession.this.extractNativeLibraries(packageLite, file, str, z);
        }
    }
}
