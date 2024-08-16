package com.android.server.pm;

import android.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.ActivityManagerNative;
import android.app.BroadcastOptions;
import android.app.IActivityManager;
import android.app.IStopUserCallback;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.StatsManager;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManagerInternal;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.PackagePartitions;
import android.content.pm.ShortcutServiceInternal;
import android.content.pm.UserInfo;
import android.content.pm.UserPackage;
import android.content.pm.UserProperties;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.IProgressListener;
import android.os.IUserManager;
import android.os.IUserRestrictionsListener;
import android.os.Message;
import android.os.PackageTagsList;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SELinux;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.ShellCallback;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageManagerInternal;
import android.provider.Settings;
import android.service.voice.VoiceInteractionManagerInternal;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.EventLog;
import android.util.IndentingPrintWriter;
import android.util.IntArray;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.StatsEvent;
import android.util.TimeUtils;
import android.util.TypedValue;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IAppOpsService;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.RoSystemProperties;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import com.android.internal.widget.LockPatternUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.BundleUtils;
import com.android.server.LocalServices;
import com.android.server.LockGuard;
import com.android.server.SystemService;
import com.android.server.am.UserState;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.health.HealthServiceWrapperHidl;
import com.android.server.job.controllers.JobStatus;
import com.android.server.pm.ResilientAtomicFile;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.UserManagerService;
import com.android.server.pm.UserTypeFactory;
import com.android.server.sensorprivacy.SensorPrivacyService;
import com.android.server.storage.DeviceStorageMonitorInternal;
import com.android.server.tare.AlarmManagerEconomicPolicy;
import com.android.server.utils.Slogf;
import com.android.server.utils.TimingsTraceAndSlog;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UserManagerService extends IUserManager.Stub {
    private static final int ALLOWED_FLAGS_FOR_CREATE_USERS_PERMISSION = 38700;
    private static final String ATTR_CONVERTED_FROM_PRE_CREATED = "convertedFromPreCreated";
    private static final String ATTR_CREATION_TIME = "created";
    private static final String ATTR_FLAGS = "flags";
    private static final String ATTR_GUEST_TO_REMOVE = "guestToRemove";
    private static final String ATTR_ICON_PATH = "icon";
    private static final String ATTR_ID = "id";
    private static final String ATTR_KEY = "key";
    private static final String ATTR_LAST_ENTERED_FOREGROUND_TIME = "lastEnteredForeground";
    private static final String ATTR_LAST_LOGGED_IN_FINGERPRINT = "lastLoggedInFingerprint";
    private static final String ATTR_LAST_LOGGED_IN_TIME = "lastLoggedIn";
    private static final String ATTR_MULTIPLE = "m";
    private static final String ATTR_NEXT_SERIAL_NO = "nextSerialNumber";
    private static final String ATTR_PARTIAL = "partial";
    private static final String ATTR_PRE_CREATED = "preCreated";
    private static final String ATTR_PROFILE_BADGE = "profileBadge";
    private static final String ATTR_PROFILE_GROUP_ID = "profileGroupId";
    private static final String ATTR_RESTRICTED_PROFILE_PARENT_ID = "restrictedProfileParentId";
    private static final String ATTR_SEED_ACCOUNT_NAME = "seedAccountName";
    private static final String ATTR_SEED_ACCOUNT_TYPE = "seedAccountType";
    private static final String ATTR_SERIAL_NO = "serialNumber";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_TYPE_BOOLEAN = "b";
    private static final String ATTR_TYPE_BUNDLE = "B";
    private static final String ATTR_TYPE_BUNDLE_ARRAY = "BA";
    private static final String ATTR_TYPE_INTEGER = "i";
    private static final String ATTR_TYPE_STRING = "s";
    private static final String ATTR_TYPE_STRING_ARRAY = "sa";
    private static final String ATTR_USER_TYPE_VERSION = "userTypeConfigVersion";
    private static final String ATTR_USER_VERSION = "version";
    private static final String ATTR_VALUE_TYPE = "type";
    private static final long BOOT_USER_SET_TIMEOUT_MS = 300000;
    static final boolean DBG = false;
    public static final boolean DBG_ALLOCATION = false;
    static final boolean DBG_MUMD = false;
    private static final boolean DBG_WITH_STACKTRACE = false;
    private static final long EPOCH_PLUS_30_YEARS = 946080000000L;
    private static final String LOG_TAG = "UserManagerService";

    @VisibleForTesting
    static final int MAX_RECENTLY_REMOVED_IDS_SIZE = 100;

    @VisibleForTesting
    static final int MAX_USER_ID = 21474;

    @VisibleForTesting
    static final int MIN_USER_ID = 10;
    private static final boolean RELEASE_DELETED_USER_ID = false;
    private static final String RESTRICTIONS_FILE_PREFIX = "res_";
    private static final String TAG_ACCOUNT = "account";
    private static final String TAG_DEVICE_OWNER_USER_ID = "deviceOwnerUserId";
    private static final String TAG_DEVICE_POLICY_GLOBAL_RESTRICTIONS = "device_policy_global_restrictions";
    private static final String TAG_DEVICE_POLICY_LOCAL_RESTRICTIONS = "device_policy_local_restrictions";
    private static final String TAG_DEVICE_POLICY_RESTRICTIONS = "device_policy_restrictions";
    private static final String TAG_ENTRY = "entry";
    private static final String TAG_GLOBAL_RESTRICTION_OWNER_ID = "globalRestrictionOwnerUserId";
    private static final String TAG_GUEST_RESTRICTIONS = "guestRestrictions";
    private static final String TAG_IGNORE_PREPARE_STORAGE_ERRORS = "ignorePrepareStorageErrors";
    private static final String TAG_LAST_REQUEST_QUIET_MODE_ENABLED_CALL = "lastRequestQuietModeEnabledCall";
    private static final String TAG_NAME = "name";
    private static final String TAG_RESTRICTIONS = "restrictions";
    private static final String TAG_SEED_ACCOUNT_OPTIONS = "seedAccountOptions";
    private static final String TAG_USER = "user";
    private static final String TAG_USERS = "users";
    private static final String TAG_USER_PROPERTIES = "userProperties";
    private static final String TAG_VALUE = "value";
    private static final String TRON_DEMO_CREATED = "users_demo_created";
    private static final String TRON_GUEST_CREATED = "users_guest_created";
    private static final String TRON_USER_CREATED = "users_user_created";
    private static final String USER_LIST_FILENAME = "userlist.xml";
    private static final String USER_PHOTO_FILENAME = "photo.png";
    private static final String USER_PHOTO_FILENAME_TMP = "photo.png.tmp";
    private static final int USER_VERSION = 11;
    static final int WRITE_USER_DELAY = 2000;
    static final int WRITE_USER_LIST_MSG = 2;
    static final int WRITE_USER_MSG = 1;
    private static final String XML_SUFFIX = ".xml";
    private static UserManagerService sInstance;
    private final String ACTION_DISABLE_QUIET_MODE_AFTER_UNLOCK;
    private ActivityManagerInternal mAmInternal;
    private IAppOpsService mAppOpsService;
    private final Object mAppRestrictionsLock;

    @GuardedBy({"mRestrictionsLock"})
    private final RestrictionsSet mAppliedUserRestrictions;

    @GuardedBy({"mRestrictionsLock"})
    private final RestrictionsSet mBaseUserRestrictions;

    @GuardedBy({"mUsersLock"})
    private int mBootUser;
    private final CountDownLatch mBootUserLatch;

    @GuardedBy({"mRestrictionsLock"})
    private final RestrictionsSet mCachedEffectiveUserRestrictions;
    private final BroadcastReceiver mConfigurationChangeReceiver;
    private final Context mContext;
    private DevicePolicyManagerInternal mDevicePolicyManagerInternal;

    @GuardedBy({"mRestrictionsLock"})
    private final RestrictionsSet mDevicePolicyUserRestrictions;
    private final BroadcastReceiver mDisableQuietModeCallback;

    @GuardedBy({"mUsersLock"})
    private boolean mForceEphemeralUsers;

    @GuardedBy({"mGuestRestrictions"})
    private final Bundle mGuestRestrictions;
    private final Handler mHandler;

    @GuardedBy({"mUsersLock"})
    private boolean mIsDeviceManaged;

    @GuardedBy({"mUsersLock"})
    private final SparseBooleanArray mIsUserManaged;
    private final Configuration mLastConfiguration;
    private final LocalService mLocalService;
    private final LockPatternUtils mLockPatternUtils;

    @GuardedBy({"mPackagesLock"})
    private int mNextSerialNumber;
    private final AtomicReference<String> mOwnerName;
    private final TypedValue mOwnerNameTypedValue;
    private final Object mPackagesLock;
    private final PackageManagerService mPm;
    private PackageManagerInternal mPmInternal;
    private final IBinder mQuietModeToken;

    @GuardedBy({"mUsersLock"})
    private final LinkedList<Integer> mRecentlyRemovedIds;

    @GuardedBy({"mUsersLock"})
    private final SparseBooleanArray mRemovingUserIds;
    private final Object mRestrictionsLock;
    private final UserSystemPackageInstaller mSystemPackageInstaller;
    private boolean mUpdatingSystemUserMode;
    public final AtomicInteger mUser0Allocations;
    private final UserDataPreparer mUserDataPreparer;

    @GuardedBy({"mUsersLock"})
    private int[] mUserIds;

    @GuardedBy({"mUsersLock"})
    private int[] mUserIdsIncludingPreCreated;
    private final UserJourneyLogger mUserJourneyLogger;

    @GuardedBy({"mUserLifecycleListeners"})
    private final ArrayList<UserManagerInternal.UserLifecycleListener> mUserLifecycleListeners;
    private final File mUserListFile;
    private IUserManagerServiceExt mUserManagerServiceExt;
    private final IBinder mUserRestrictionToken;

    @GuardedBy({"mUserRestrictionsListeners"})
    private final ArrayList<UserManagerInternal.UserRestrictionsListener> mUserRestrictionsListeners;

    @GuardedBy({"mUserStates"})
    private final WatchedUserStates mUserStates;
    private int mUserTypeVersion;
    private final ArrayMap<String, UserTypeDetails> mUserTypes;
    private int mUserVersion;
    private final UserVisibilityMediator mUserVisibilityMediator;

    @GuardedBy({"mUsersLock"})
    private final SparseArray<UserData> mUsers;
    private final File mUsersDir;
    private final Object mUsersLock;
    private UserManagerServiceWrapper mWrapper;
    private static final String USER_INFO_DIR = "system" + File.separator + "users";
    private static final int[] QUIET_MODE_RESTRICTED_APP_OPS = {0, 1, 2, 56, 79, 77, HdmiCecKeycode.CEC_KEYCODE_F4_YELLOW, 27, 26};

    private static boolean isAtMostOneFlag(int i) {
        return (i & (i + (-1))) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class UserData {
        String account;
        UserInfo info;
        private boolean mIgnorePrepareStorageErrors;
        long mLastEnteredForegroundTimeMillis;
        private long mLastRequestQuietModeEnabledMillis;
        boolean persistSeedData;
        String seedAccountName;
        PersistableBundle seedAccountOptions;
        String seedAccountType;
        long startRealtime;
        long unlockRealtime;
        UserProperties userProperties;

        UserData() {
        }

        void setLastRequestQuietModeEnabledMillis(long j) {
            this.mLastRequestQuietModeEnabledMillis = j;
        }

        long getLastRequestQuietModeEnabledMillis() {
            return this.mLastRequestQuietModeEnabledMillis;
        }

        boolean getIgnorePrepareStorageErrors() {
            return this.mIgnorePrepareStorageErrors;
        }

        void setIgnorePrepareStorageErrors() {
            if (Build.VERSION.DEVICE_INITIAL_SDK_INT < 33) {
                this.mIgnorePrepareStorageErrors = true;
            } else {
                Slog.w(UserManagerService.LOG_TAG, "Not setting mIgnorePrepareStorageErrors to true since this is a new device");
            }
        }

        void clearSeedAccountData() {
            this.seedAccountName = null;
            this.seedAccountType = null;
            this.seedAccountOptions = null;
            this.persistSeedData = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.pm.UserManagerService$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.android.server.pm.DISABLE_QUIET_MODE_AFTER_UNLOCK".equals(intent.getAction())) {
                final IntentSender intentSender = (IntentSender) intent.getParcelableExtra("android.intent.extra.INTENT", IntentSender.class);
                final int intExtra = intent.getIntExtra("android.intent.extra.USER_ID", -10000);
                BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.pm.UserManagerService$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserManagerService.AnonymousClass1.this.lambda$onReceive$0(intExtra, intentSender);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(int i, IntentSender intentSender) {
            UserManagerService.this.setQuietModeEnabled(i, false, intentSender, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class DisableQuietModeUserUnlockedCallback extends IProgressListener.Stub {
        private final IntentSender mTarget;

        public void onProgress(int i, int i2, Bundle bundle) {
        }

        public void onStarted(int i, Bundle bundle) {
        }

        public DisableQuietModeUserUnlockedCallback(IntentSender intentSender) {
            Objects.requireNonNull(intentSender);
            this.mTarget = intentSender;
        }

        public void onFinished(int i, Bundle bundle) {
            UserManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.pm.UserManagerService$DisableQuietModeUserUnlockedCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UserManagerService.DisableQuietModeUserUnlockedCallback.this.lambda$onFinished$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFinished$0() {
            try {
                UserManagerService.this.mContext.startIntentSender(this.mTarget, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Slog.e(UserManagerService.LOG_TAG, "Failed to start the target in the callback", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class WatchedUserStates {
        final SparseIntArray states = new SparseIntArray();

        public WatchedUserStates() {
            invalidateIsUserUnlockedCache();
        }

        public int get(int i) {
            return this.states.get(i);
        }

        public int get(int i, int i2) {
            return this.states.indexOfKey(i) >= 0 ? this.states.get(i) : i2;
        }

        public void put(int i, int i2) {
            this.states.put(i, i2);
            invalidateIsUserUnlockedCache();
        }

        public void delete(int i) {
            this.states.delete(i);
            invalidateIsUserUnlockedCache();
        }

        public boolean has(int i) {
            return this.states.get(i, -10000) != -10000;
        }

        public String toString() {
            return this.states.toString();
        }

        private void invalidateIsUserUnlockedCache() {
            UserManager.invalidateIsUserUnlockedCache();
        }
    }

    public static UserManagerService getInstance() {
        UserManagerService userManagerService;
        synchronized (UserManagerService.class) {
            userManagerService = sInstance;
        }
        return userManagerService;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class LifeCycle extends SystemService {
        private UserManagerService mUms;

        public LifeCycle(Context context) {
            super(context);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.pm.UserManagerService, android.os.IBinder] */
        public void onStart() {
            ?? userManagerService = UserManagerService.getInstance();
            this.mUms = userManagerService;
            publishBinderService(UserManagerService.TAG_USER, (IBinder) userManagerService);
        }

        public void onBootPhase(int i) {
            if (i == 550) {
                this.mUms.cleanupPartialUsers();
                if (this.mUms.mPm.isDeviceUpgrading()) {
                    this.mUms.cleanupPreCreatedUsers();
                }
                this.mUms.registerStatsCallbacks();
            }
        }

        public void onUserStarting(SystemService.TargetUser targetUser) {
            boolean z;
            synchronized (this.mUms.mUsersLock) {
                UserData userDataLU = this.mUms.getUserDataLU(targetUser.getUserIdentifier());
                if (userDataLU != null) {
                    userDataLU.startRealtime = SystemClock.elapsedRealtime();
                    if (targetUser.getUserIdentifier() == 0 && targetUser.isFull()) {
                        this.mUms.setLastEnteredForegroundTimeToNow(userDataLU);
                    } else {
                        z = userDataLU.info.isManagedProfile() && userDataLU.info.isQuietModeEnabled();
                    }
                }
            }
            if (z) {
                this.mUms.setAppOpsRestrictedForQuietMode(targetUser.getUserIdentifier(), true);
            }
        }

        public void onUserUnlocking(SystemService.TargetUser targetUser) {
            synchronized (this.mUms.mUsersLock) {
                UserData userDataLU = this.mUms.getUserDataLU(targetUser.getUserIdentifier());
                if (userDataLU != null) {
                    userDataLU.unlockRealtime = SystemClock.elapsedRealtime();
                }
            }
        }

        public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
            synchronized (this.mUms.mUsersLock) {
                UserData userDataLU = this.mUms.getUserDataLU(targetUser2.getUserIdentifier());
                if (userDataLU != null) {
                    this.mUms.setLastEnteredForegroundTimeToNow(userDataLU);
                }
            }
        }

        public void onUserStopping(SystemService.TargetUser targetUser) {
            synchronized (this.mUms.mUsersLock) {
                UserData userDataLU = this.mUms.getUserDataLU(targetUser.getUserIdentifier());
                if (userDataLU != null) {
                    userDataLU.startRealtime = 0L;
                    userDataLU.unlockRealtime = 0L;
                }
            }
        }
    }

    @VisibleForTesting
    UserManagerService(Context context) {
        this(context, null, null, new Object(), context.getCacheDir(), null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserManagerService(Context context, PackageManagerService packageManagerService, UserDataPreparer userDataPreparer, Object obj) {
        this(context, packageManagerService, userDataPreparer, obj, Environment.getDataDirectory(), null);
    }

    @VisibleForTesting
    UserManagerService(Context context, PackageManagerService packageManagerService, UserDataPreparer userDataPreparer, Object obj, File file, SparseArray<UserData> sparseArray) {
        this.mUsersLock = LockGuard.installNewLock(2);
        this.mRestrictionsLock = new Object();
        this.mAppRestrictionsLock = new Object();
        this.mUserRestrictionToken = new Binder();
        this.mQuietModeToken = new Binder();
        this.mBootUserLatch = new CountDownLatch(1);
        this.mBaseUserRestrictions = new RestrictionsSet();
        this.mCachedEffectiveUserRestrictions = new RestrictionsSet();
        this.mAppliedUserRestrictions = new RestrictionsSet();
        this.mDevicePolicyUserRestrictions = new RestrictionsSet();
        this.mGuestRestrictions = new Bundle();
        this.mRemovingUserIds = new SparseBooleanArray();
        this.mRecentlyRemovedIds = new LinkedList<>();
        this.mUserVersion = 0;
        this.mUserTypeVersion = 0;
        this.mIsUserManaged = new SparseBooleanArray();
        this.mUserRestrictionsListeners = new ArrayList<>();
        this.mUserLifecycleListeners = new ArrayList<>();
        this.mUserJourneyLogger = new UserJourneyLogger();
        this.ACTION_DISABLE_QUIET_MODE_AFTER_UNLOCK = "com.android.server.pm.DISABLE_QUIET_MODE_AFTER_UNLOCK";
        this.mDisableQuietModeCallback = new AnonymousClass1();
        this.mOwnerName = new AtomicReference<>();
        this.mOwnerNameTypedValue = new TypedValue();
        this.mLastConfiguration = new Configuration();
        this.mConfigurationChangeReceiver = new BroadcastReceiver() { // from class: com.android.server.pm.UserManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.CONFIGURATION_CHANGED".equals(intent.getAction())) {
                    UserManagerService.this.invalidateOwnerNameIfNecessary(context2.getResources(), false);
                }
            }
        };
        WatchedUserStates watchedUserStates = new WatchedUserStates();
        this.mUserStates = watchedUserStates;
        this.mBootUser = -10000;
        byte b = 0;
        this.mWrapper = new UserManagerServiceWrapper();
        this.mUserManagerServiceExt = (IUserManagerServiceExt) ExtLoader.type(IUserManagerServiceExt.class).base(this).create();
        this.mContext = context;
        this.mWrapper.getExtImpl().init(context);
        this.mPm = packageManagerService;
        this.mPackagesLock = obj;
        this.mUsers = sparseArray == null ? new SparseArray<>() : sparseArray;
        MainHandler mainHandler = new MainHandler();
        this.mHandler = mainHandler;
        this.mUserVisibilityMediator = new UserVisibilityMediator(mainHandler);
        this.mUserDataPreparer = userDataPreparer;
        ArrayMap<String, UserTypeDetails> userTypes = UserTypeFactory.getUserTypes();
        this.mUserTypes = userTypes;
        invalidateOwnerNameIfNecessary(context.getResources(), true);
        synchronized (obj) {
            File file2 = new File(file, USER_INFO_DIR);
            this.mUsersDir = file2;
            file2.mkdirs();
            new File(file2, String.valueOf(0)).mkdirs();
            FileUtils.setPermissions(file2.toString(), 509, -1, -1);
            this.mUserListFile = new File(file2, USER_LIST_FILENAME);
            initDefaultGuestRestrictions();
            readUserListLP();
            sInstance = this;
        }
        this.mSystemPackageInstaller = new UserSystemPackageInstaller(this, userTypes);
        LocalService localService = new LocalService();
        this.mLocalService = localService;
        LocalServices.addService(UserManagerInternal.class, localService);
        this.mLockPatternUtils = new LockPatternUtils(context);
        watchedUserStates.put(0, 0);
        this.mUser0Allocations = null;
        emulateSystemUserModeIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady() {
        this.mAppOpsService = IAppOpsService.Stub.asInterface(ServiceManager.getService("appops"));
        synchronized (this.mRestrictionsLock) {
            applyUserRestrictionsLR(0);
        }
        this.mContext.registerReceiver(this.mDisableQuietModeCallback, new IntentFilter("com.android.server.pm.DISABLE_QUIET_MODE_AFTER_UNLOCK"), null, this.mHandler);
        this.mContext.registerReceiver(this.mConfigurationChangeReceiver, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"), null, this.mHandler);
        markEphemeralUsersForRemoval();
        this.mWrapper.getExtImpl().systemReady();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserManagerInternal getInternalForInjectorOnly() {
        return this.mLocalService;
    }

    private void markEphemeralUsersForRemoval() {
        int i;
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i2 = 0; i2 < size; i2++) {
                UserInfo userInfo = this.mUsers.valueAt(i2).info;
                if (userInfo.isEphemeral() && !userInfo.preCreated && (i = userInfo.id) != 0) {
                    addRemovingUserIdLocked(i);
                    userInfo.partial = true;
                    userInfo.flags |= 64;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupPartialUsers() {
        int i;
        ArrayList arrayList = new ArrayList();
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i2 = 0; i2 < size; i2++) {
                UserInfo userInfo = this.mUsers.valueAt(i2).info;
                if ((userInfo.partial || userInfo.guestToRemove) && userInfo.id != 0) {
                    arrayList.add(userInfo);
                    if (!this.mRemovingUserIds.get(userInfo.id)) {
                        addRemovingUserIdLocked(userInfo.id);
                    }
                    userInfo.partial = true;
                }
            }
        }
        int size2 = arrayList.size();
        for (i = 0; i < size2; i++) {
            UserInfo userInfo2 = (UserInfo) arrayList.get(i);
            Slog.w(LOG_TAG, "Removing partially created user " + userInfo2.id + " (name=" + userInfo2.name + ")");
            removeUserState(userInfo2.id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupPreCreatedUsers() {
        ArrayList arrayList;
        int i;
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            arrayList = new ArrayList(size);
            for (int i2 = 0; i2 < size; i2++) {
                UserInfo userInfo = this.mUsers.valueAt(i2).info;
                if (userInfo.preCreated) {
                    arrayList.add(userInfo);
                    addRemovingUserIdLocked(userInfo.id);
                    userInfo.flags |= 64;
                    userInfo.partial = true;
                }
            }
        }
        int size2 = arrayList.size();
        for (i = 0; i < size2; i++) {
            UserInfo userInfo2 = (UserInfo) arrayList.get(i);
            Slog.i(LOG_TAG, "Removing pre-created user " + userInfo2.id);
            removeUserState(userInfo2.id);
        }
    }

    public String getUserAccount(int i) {
        String str;
        checkManageUserAndAcrossUsersFullPermission("get user account");
        synchronized (this.mUsersLock) {
            str = this.mUsers.get(i).account;
        }
        return str;
    }

    public void setUserAccount(int i, String str) {
        checkManageUserAndAcrossUsersFullPermission("set user account");
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                UserData userData = this.mUsers.get(i);
                if (userData == null) {
                    Slog.e(LOG_TAG, "User not found for setting user account: u" + i);
                    return;
                }
                if (Objects.equals(userData.account, str)) {
                    userData = null;
                } else {
                    userData.account = str;
                }
                if (userData != null) {
                    writeUserLP(userData);
                }
            }
        }
    }

    public UserInfo getPrimaryUser() {
        checkManageUsersPermission("query users");
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                UserInfo userInfo = this.mUsers.valueAt(i).info;
                if (userInfo.isPrimary() && !this.mRemovingUserIds.get(userInfo.id)) {
                    return userInfo;
                }
            }
            return null;
        }
    }

    public int getMainUserId() {
        checkQueryOrCreateUsersPermission("get main user id");
        return getMainUserIdUnchecked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMainUserIdUnchecked() {
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                UserInfo userInfo = this.mUsers.valueAt(i).info;
                if (userInfo.isMain() && !this.mRemovingUserIds.get(userInfo.id)) {
                    return userInfo.id;
                }
            }
            return -10000;
        }
    }

    public void setBootUser(int i) {
        checkCreateUsersPermission("Set boot user");
        synchronized (this.mUsersLock) {
            Slogf.i(LOG_TAG, "setBootUser %d", Integer.valueOf(i));
            this.mBootUser = i;
        }
        this.mBootUserLatch.countDown();
    }

    public int getBootUser() {
        checkCreateUsersPermission("Get boot user");
        try {
            return getBootUserUnchecked();
        } catch (UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getBootUserUnchecked() throws UserManager.CheckedUserOperationException {
        synchronized (this.mUsersLock) {
            int i = this.mBootUser;
            if (i != -10000) {
                UserData userData = this.mUsers.get(i);
                if (userData != null && userData.info.supportsSwitchToByUser()) {
                    Slogf.i(LOG_TAG, "Using provided boot user: %d", Integer.valueOf(this.mBootUser));
                    return this.mBootUser;
                }
                Slogf.w(LOG_TAG, "Provided boot user cannot be switched to: %d", Integer.valueOf(this.mBootUser));
            }
            if (!isHeadlessSystemUserMode()) {
                return 0;
            }
            int previousFullUserToEnterForeground = getPreviousFullUserToEnterForeground();
            if (previousFullUserToEnterForeground != -10000) {
                Slogf.i(LOG_TAG, "Boot user is previous user %d", Integer.valueOf(previousFullUserToEnterForeground));
                return previousFullUserToEnterForeground;
            }
            synchronized (this.mUsersLock) {
                int size = this.mUsers.size();
                for (int i2 = 0; i2 < size; i2++) {
                    UserData valueAt = this.mUsers.valueAt(i2);
                    if (valueAt.info.supportsSwitchToByUser()) {
                        int i3 = valueAt.info.id;
                        Slogf.i(LOG_TAG, "Boot user is first switchable user %d", Integer.valueOf(i3));
                        return i3;
                    }
                }
                throw new UserManager.CheckedUserOperationException("No switchable users found", 1);
            }
        }
    }

    public int getPreviousFullUserToEnterForeground() {
        int i;
        checkQueryOrCreateUsersPermission("get previous user");
        int currentUserId = getCurrentUserId();
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            i = -10000;
            long j = 0;
            for (int i2 = 0; i2 < size; i2++) {
                UserData valueAt = this.mUsers.valueAt(i2);
                UserInfo userInfo = valueAt.info;
                int i3 = userInfo.id;
                if (i3 != currentUserId && userInfo.isFull() && !valueAt.info.partial && !this.mRemovingUserIds.get(i3)) {
                    long j2 = valueAt.mLastEnteredForegroundTimeMillis;
                    if (j2 > j) {
                        j = j2;
                        i = i3;
                    }
                }
            }
        }
        return i;
    }

    public List<UserInfo> getUsers(boolean z) {
        return getUsers(true, z, true);
    }

    public List<UserInfo> getUsers(boolean z, boolean z2, boolean z3) {
        checkCreateUsersPermission("query users");
        return getUsersInternal(z, z2, z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<UserInfo> getUsersInternal(boolean z, boolean z2, boolean z3) {
        ArrayList arrayList;
        synchronized (this.mUsersLock) {
            arrayList = new ArrayList(this.mUsers.size());
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                UserInfo userInfo = this.mUsers.valueAt(i).info;
                if ((!z || !userInfo.partial) && ((!z2 || !this.mRemovingUserIds.get(userInfo.id)) && (!z3 || !userInfo.preCreated))) {
                    arrayList.add(userWithName(userInfo));
                }
            }
        }
        return arrayList;
    }

    public List<UserInfo> getProfiles(int i, boolean z) {
        boolean hasCreateUsersPermission;
        List<UserInfo> profilesLU;
        if (i != UserHandle.getCallingUserId()) {
            checkQueryOrCreateUsersPermission("getting profiles related to user " + i);
            hasCreateUsersPermission = true;
        } else {
            hasCreateUsersPermission = hasCreateUsersPermission();
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mUsersLock) {
                profilesLU = getProfilesLU(i, null, z, hasCreateUsersPermission);
            }
            return profilesLU;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int[] getProfileIds(int i, boolean z) {
        return getProfileIds(i, null, z);
    }

    public int[] getProfileIds(int i, String str, boolean z) {
        int[] array;
        if (i != UserHandle.getCallingUserId()) {
            checkQueryOrCreateUsersPermission("getting profiles related to user " + i);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mUsersLock) {
                array = getProfileIdsLU(i, str, z).toArray();
            }
            return array;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"mUsersLock"})
    private List<UserInfo> getProfilesLU(int i, String str, boolean z, boolean z2) {
        UserInfo userWithName;
        IntArray profileIdsLU = getProfileIdsLU(i, str, z);
        ArrayList arrayList = new ArrayList(profileIdsLU.size());
        for (int i2 = 0; i2 < profileIdsLU.size(); i2++) {
            UserInfo userInfo = this.mUsers.get(profileIdsLU.get(i2)).info;
            if (!z2) {
                userWithName = new UserInfo(userInfo);
                userWithName.name = null;
                userWithName.iconPath = null;
            } else {
                userWithName = userWithName(userInfo);
            }
            if (userWithName != null) {
                arrayList.add(userWithName);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mUsersLock"})
    public IntArray getProfileIdsLU(int i, String str, boolean z) {
        UserInfo userInfoLU = getUserInfoLU(i);
        IntArray intArray = new IntArray(this.mUsers.size());
        if (userInfoLU == null) {
            return intArray;
        }
        int size = this.mUsers.size();
        for (int i2 = 0; i2 < size; i2++) {
            UserInfo userInfo = this.mUsers.valueAt(i2).info;
            if (isProfileOf(userInfoLU, userInfo) && ((!z || userInfo.isEnabled()) && !this.mRemovingUserIds.get(userInfo.id) && !userInfo.partial && (str == null || str.equals(userInfo.userType)))) {
                intArray.add(userInfo.id);
            }
        }
        return intArray;
    }

    public int getCredentialOwnerProfile(int i) {
        checkManageUsersPermission("get the credential owner");
        if (!this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
            synchronized (this.mUsersLock) {
                UserInfo profileParentLU = getProfileParentLU(i);
                if (profileParentLU != null) {
                    return profileParentLU.id;
                }
            }
        }
        return i;
    }

    public boolean isSameProfileGroup(int i, int i2) {
        if (i == i2) {
            return true;
        }
        checkQueryUsersPermission("check if in the same profile group");
        return isSameProfileGroupNoChecks(i, i2);
    }

    private boolean isSameProfileGroupNoChecks(int i, int i2) {
        int i3;
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            if (userInfoLU != null && userInfoLU.profileGroupId != -10000) {
                UserInfo userInfoLU2 = getUserInfoLU(i2);
                if (userInfoLU2 != null && (i3 = userInfoLU2.profileGroupId) != -10000) {
                    return userInfoLU.profileGroupId == i3;
                }
                return false;
            }
            return false;
        }
    }

    public UserInfo getProfileParent(int i) {
        UserInfo profileParentLU;
        if (!hasManageUsersOrPermission("android.permission.INTERACT_ACROSS_USERS")) {
            throw new SecurityException("You need MANAGE_USERS or INTERACT_ACROSS_USERS permission to get the profile parent");
        }
        synchronized (this.mUsersLock) {
            profileParentLU = getProfileParentLU(i);
        }
        return profileParentLU;
    }

    public int getProfileParentId(int i) {
        checkManageUsersPermission("get the profile parent");
        return getProfileParentIdUnchecked(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getProfileParentIdUnchecked(int i) {
        synchronized (this.mUsersLock) {
            UserInfo profileParentLU = getProfileParentLU(i);
            if (profileParentLU == null) {
                return i;
            }
            return profileParentLU.id;
        }
    }

    @GuardedBy({"mUsersLock"})
    private UserInfo getProfileParentLU(int i) {
        int i2;
        UserInfo userInfoLU = getUserInfoLU(i);
        if (userInfoLU == null || (i2 = userInfoLU.profileGroupId) == i || i2 == -10000) {
            return null;
        }
        return getUserInfoLU(i2);
    }

    private static boolean isProfileOf(UserInfo userInfo, UserInfo userInfo2) {
        int i;
        return userInfo.id == userInfo2.id || ((i = userInfo.profileGroupId) != -10000 && i == userInfo2.profileGroupId);
    }

    private void broadcastProfileAvailabilityChanges(UserHandle userHandle, UserHandle userHandle2, boolean z) {
        Intent intent = new Intent();
        if (z) {
            intent.setAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        } else {
            intent.setAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        }
        intent.putExtra("android.intent.extra.QUIET_MODE", z);
        intent.putExtra("android.intent.extra.USER", userHandle);
        intent.putExtra("android.intent.extra.user_handle", userHandle.getIdentifier());
        getDevicePolicyManagerInternal().broadcastIntentToManifestReceivers(intent, userHandle2, true);
        intent.addFlags(AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE);
        this.mContext.sendBroadcastAsUser(intent, userHandle2, null, new BroadcastOptions().setDeferralPolicy(2).setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey("android.intent.action.MANAGED_PROFILE_AVAILABLE", String.valueOf(userHandle.getIdentifier())).toBundle());
    }

    public boolean requestQuietModeEnabled(String str, boolean z, int i, IntentSender intentSender, int i2) {
        Objects.requireNonNull(str);
        if (z && intentSender != null) {
            throw new IllegalArgumentException("target should only be specified when we are disabling quiet mode.");
        }
        boolean z2 = (i2 & 2) != 0;
        boolean z3 = (i2 & 1) != 0;
        if (z2 && z3) {
            throw new IllegalArgumentException("invalid flags: " + i2);
        }
        ensureCanModifyQuietMode(str, Binder.getCallingUid(), i, intentSender != null, z2);
        if (z3 && str.equals(getPackageManagerInternal().getSystemUiServiceComponent().getPackageName())) {
            throw new SecurityException("SystemUI is not allowed to set QUIET_MODE_DISABLE_ONLY_IF_CREDENTIAL_NOT_REQUIRED");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (z) {
                setQuietModeEnabled(i, true, intentSender, str);
                return true;
            }
            boolean isManagedProfileWithUnifiedChallenge = this.mLockPatternUtils.isManagedProfileWithUnifiedChallenge(i);
            if (isManagedProfileWithUnifiedChallenge && (!((KeyguardManager) this.mContext.getSystemService(KeyguardManager.class)).isDeviceLocked(this.mLocalService.getProfileParentId(i)) || z3)) {
                this.mLockPatternUtils.tryUnlockWithCachedUnifiedChallenge(i);
            }
            if (!((z2 || !this.mLockPatternUtils.isSecure(i) || (isManagedProfileWithUnifiedChallenge && StorageManager.isUserKeyUnlocked(i))) ? false : true)) {
                setQuietModeEnabled(i, false, intentSender, str);
                return true;
            }
            if (z3) {
                return false;
            }
            showConfirmCredentialToDisableQuietMode(i, intentSender);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void ensureCanModifyQuietMode(String str, int i, int i2, boolean z, boolean z2) {
        verifyCallingPackage(str, i);
        if (hasManageUsersPermission()) {
            return;
        }
        if (z) {
            throw new SecurityException("MANAGE_USERS permission is required to start intent after disabling quiet mode.");
        }
        if (z2) {
            throw new SecurityException("MANAGE_USERS permission is required to disable quiet mode without credentials.");
        }
        if (!isSameProfileGroupNoChecks(UserHandle.getUserId(i), i2)) {
            throw new SecurityException("MANAGE_USERS permission is required to modify quiet mode for a different profile group.");
        }
        if (hasPermissionGranted("android.permission.MODIFY_QUIET_MODE", i)) {
            return;
        }
        ShortcutServiceInternal shortcutServiceInternal = (ShortcutServiceInternal) LocalServices.getService(ShortcutServiceInternal.class);
        if (shortcutServiceInternal == null || !shortcutServiceInternal.isForegroundDefaultLauncher(str, i)) {
            throw new SecurityException("Can't modify quiet mode, caller is neither foreground default launcher nor has MANAGE_USERS/MODIFY_QUIET_MODE permission");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setQuietModeEnabled(int i, boolean z, IntentSender intentSender, String str) {
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            UserInfo profileParentLU = getProfileParentLU(i);
            if (this.mWrapper.getExtImpl().isMultiAppUser(i)) {
                Slog.i(LOG_TAG, "should not setQuietMode for multiapp user: " + i);
                return;
            }
            if (userInfoLU == null || !userInfoLU.isManagedProfile()) {
                throw new IllegalArgumentException("User " + i + " is not a profile");
            }
            if (userInfoLU.isQuietModeEnabled() == z) {
                Slog.i(LOG_TAG, "Quiet mode is already " + z);
                return;
            }
            userInfoLU.flags ^= 128;
            UserData userDataLU = getUserDataLU(userInfoLU.id);
            synchronized (this.mPackagesLock) {
                writeUserLP(userDataLU);
            }
            if (getDevicePolicyManagerInternal().isKeepProfilesRunningEnabled()) {
                getPackageManagerInternal().setPackagesSuspendedForQuietMode(i, z);
                setAppOpsRestrictedForQuietMode(i, z);
                if (z && !this.mLockPatternUtils.isManagedProfileWithUnifiedChallenge(i)) {
                    ((TrustManager) this.mContext.getSystemService(TrustManager.class)).setDeviceLockedForUser(i, true);
                }
                if (!z && intentSender != null) {
                    try {
                        this.mContext.startIntentSender(intentSender, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Slog.e(LOG_TAG, "Failed to start intent after disabling quiet mode", e);
                    }
                }
            } else {
                try {
                    if (z) {
                        ActivityManager.getService().stopUser(i, true, (IStopUserCallback) null);
                        ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).killForegroundAppsForUser(i);
                    } else {
                        ActivityManager.getService().startProfileWithListener(i, intentSender != null ? new DisableQuietModeUserUnlockedCallback(intentSender) : null);
                    }
                } catch (RemoteException e2) {
                    e2.rethrowAsRuntimeException();
                }
            }
            logQuietModeEnabled(i, z, str);
            broadcastProfileAvailabilityChanges(userInfoLU.getUserHandle(), profileParentLU.getUserHandle(), z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAppOpsRestrictedForQuietMode(int i, boolean z) {
        for (int i2 : QUIET_MODE_RESTRICTED_APP_OPS) {
            try {
                this.mAppOpsService.setUserRestriction(i2, z, this.mQuietModeToken, i, (PackageTagsList) null);
            } catch (RemoteException e) {
                Slog.w(LOG_TAG, "Unable to limit app ops", e);
            }
        }
    }

    private void logQuietModeEnabled(int i, boolean z, String str) {
        UserData userDataLU;
        long j;
        Slogf.i(LOG_TAG, "requestQuietModeEnabled called by package %s, with enableQuietMode %b.", str, Boolean.valueOf(z));
        synchronized (this.mUsersLock) {
            userDataLU = getUserDataLU(i);
        }
        if (userDataLU == null) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (userDataLU.getLastRequestQuietModeEnabledMillis() != 0) {
            j = userDataLU.getLastRequestQuietModeEnabledMillis();
        } else {
            j = userDataLU.info.creationTime;
        }
        DevicePolicyEventLogger.createEvent(55).setStrings(new String[]{str}).setBoolean(z).setTimePeriod(currentTimeMillis - j).write();
        userDataLU.setLastRequestQuietModeEnabledMillis(currentTimeMillis);
    }

    public boolean isQuietModeEnabled(int i) {
        UserInfo userInfoLU;
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                userInfoLU = getUserInfoLU(i);
            }
            if (userInfoLU != null && userInfoLU.isManagedProfile()) {
                return userInfoLU.isQuietModeEnabled();
            }
            return false;
        }
    }

    private void showConfirmCredentialToDisableQuietMode(int i, IntentSender intentSender) {
        Intent createConfirmDeviceCredentialIntent = ((KeyguardManager) this.mContext.getSystemService("keyguard")).createConfirmDeviceCredentialIntent(null, null, i);
        if (createConfirmDeviceCredentialIntent == null) {
            return;
        }
        Intent intent = new Intent("com.android.server.pm.DISABLE_QUIET_MODE_AFTER_UNLOCK");
        if (intentSender != null) {
            intent.putExtra("android.intent.extra.INTENT", intentSender);
        }
        intent.putExtra("android.intent.extra.USER_ID", i);
        intent.setPackage(this.mContext.getPackageName());
        intent.addFlags(268435456);
        createConfirmDeviceCredentialIntent.putExtra("android.intent.extra.INTENT", PendingIntent.getBroadcast(this.mContext, 0, intent, 1409286144).getIntentSender());
        createConfirmDeviceCredentialIntent.setFlags(276824064);
        this.mContext.startActivityAsUser(createConfirmDeviceCredentialIntent, UserHandle.of(getProfileParentIdUnchecked(i)));
    }

    public void setUserEnabled(int i) {
        UserInfo userInfoLU;
        boolean z;
        checkManageUsersPermission("enable user");
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                userInfoLU = getUserInfoLU(i);
                if (userInfoLU == null || userInfoLU.isEnabled()) {
                    z = false;
                } else {
                    userInfoLU.flags ^= 64;
                    writeUserLP(getUserDataLU(userInfoLU.id));
                    z = true;
                }
            }
        }
        if (z && userInfoLU != null && userInfoLU.isProfile()) {
            sendProfileAddedBroadcast(userInfoLU.profileGroupId, userInfoLU.id);
        }
    }

    public void setUserAdmin(int i) {
        UserInfo userInfoLU;
        checkManageUserAndAcrossUsersFullPermission("set user admin");
        this.mUserJourneyLogger.logUserJourneyBegin(i, 7);
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                userInfoLU = getUserInfoLU(i);
            }
            if (userInfoLU == null) {
                this.mUserJourneyLogger.logNullUserJourneyError(7, getCurrentUserId(), i, "", -1);
            } else {
                if (userInfoLU.isAdmin()) {
                    this.mUserJourneyLogger.logUserJourneyFinishWithError(getCurrentUserId(), userInfoLU, 7, 5);
                    return;
                }
                userInfoLU.flags ^= 2;
                writeUserLP(getUserDataLU(userInfoLU.id));
                this.mUserJourneyLogger.logUserJourneyFinishWithError(getCurrentUserId(), userInfoLU, 7, -1);
            }
        }
    }

    public void revokeUserAdmin(int i) {
        checkManageUserAndAcrossUsersFullPermission("revoke admin privileges");
        this.mUserJourneyLogger.logUserJourneyBegin(i, 8);
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                UserData userDataLU = getUserDataLU(i);
                if (userDataLU == null) {
                    this.mUserJourneyLogger.logNullUserJourneyError(8, getCurrentUserId(), i, "", -1);
                    return;
                }
                if (!userDataLU.info.isAdmin()) {
                    this.mUserJourneyLogger.logUserJourneyFinishWithError(getCurrentUserId(), userDataLU.info, 8, 6);
                    return;
                }
                userDataLU.info.flags ^= 2;
                writeUserLP(userDataLU);
                this.mUserJourneyLogger.logUserJourneyFinishWithError(getCurrentUserId(), userDataLU.info, 8, -1);
            }
        }
    }

    public void evictCredentialEncryptionKey(int i) {
        checkManageUsersPermission("evict CE key");
        IActivityManager iActivityManager = ActivityManagerNative.getDefault();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                iActivityManager.restartUserInBackground(i, isProfileUnchecked(i) ? 3 : 2);
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isUserOfType(int i, String str) {
        checkQueryOrCreateUsersPermission("check user type");
        return str != null && str.equals(getUserTypeNoChecks(i));
    }

    private String getUserTypeNoChecks(int i) {
        String str;
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            str = userInfoLU != null ? userInfoLU.userType : null;
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserTypeDetails getUserTypeDetailsNoChecks(int i) {
        String userTypeNoChecks = getUserTypeNoChecks(i);
        if (userTypeNoChecks != null) {
            return this.mUserTypes.get(userTypeNoChecks);
        }
        return null;
    }

    private UserTypeDetails getUserTypeDetails(UserInfo userInfo) {
        String str = userInfo != null ? userInfo.userType : null;
        if (str != null) {
            return this.mUserTypes.get(str);
        }
        return null;
    }

    public UserInfo getUserInfo(int i) {
        UserInfo userWithName;
        checkQueryOrCreateUsersPermission("query user");
        synchronized (this.mUsersLock) {
            userWithName = userWithName(getUserInfoLU(i));
        }
        return userWithName;
    }

    private UserInfo userWithName(UserInfo userInfo) {
        String guestName;
        if (userInfo != null && userInfo.name == null) {
            if (userInfo.id == 0) {
                guestName = getOwnerName();
            } else if (userInfo.isMain()) {
                guestName = getOwnerName();
            } else {
                guestName = userInfo.isGuest() ? getGuestName() : null;
            }
            if (guestName != null) {
                UserInfo userInfo2 = new UserInfo(userInfo);
                userInfo2.name = guestName;
                return userInfo2;
            }
        }
        return userInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUserTypeSubtypeOfFull(String str) {
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        return userTypeDetails != null && userTypeDetails.isFull();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUserTypeSubtypeOfProfile(String str) {
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        return userTypeDetails != null && userTypeDetails.isProfile();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUserTypeSubtypeOfSystem(String str) {
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        return userTypeDetails != null && userTypeDetails.isSystem();
    }

    public UserProperties getUserPropertiesCopy(int i) {
        checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserProperties");
        UserProperties userPropertiesInternal = getUserPropertiesInternal(i);
        if (userPropertiesInternal != null) {
            return new UserProperties(userPropertiesInternal, Binder.getCallingUid() == 1000, hasManageUsersPermission(), hasQueryUsersPermission());
        }
        throw new IllegalArgumentException("Cannot access properties for user " + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserProperties getUserPropertiesInternal(int i) {
        synchronized (this.mUsersLock) {
            UserData userDataLU = getUserDataLU(i);
            if (userDataLU == null) {
                return null;
            }
            return userDataLU.userProperties;
        }
    }

    public boolean hasBadge(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "hasBadge");
        UserTypeDetails userTypeDetailsNoChecks = getUserTypeDetailsNoChecks(i);
        return userTypeDetailsNoChecks != null && userTypeDetailsNoChecks.hasBadge();
    }

    public int getUserBadgeLabelResId(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserBadgeLabelResId");
        UserInfo userInfoNoChecks = getUserInfoNoChecks(i);
        UserTypeDetails userTypeDetails = getUserTypeDetails(userInfoNoChecks);
        if (userInfoNoChecks == null || userTypeDetails == null || !userTypeDetails.hasBadge()) {
            Slog.e(LOG_TAG, "Requested badge label for non-badged user " + i);
            return 0;
        }
        return userTypeDetails.getBadgeLabel(userInfoNoChecks.profileBadge);
    }

    public int getUserBadgeColorResId(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserBadgeColorResId");
        UserInfo userInfoNoChecks = getUserInfoNoChecks(i);
        UserTypeDetails userTypeDetails = getUserTypeDetails(userInfoNoChecks);
        if (userInfoNoChecks == null || userTypeDetails == null || !userTypeDetails.hasBadge()) {
            Slog.e(LOG_TAG, "Requested badge dark color for non-badged user " + i);
            return 0;
        }
        return userTypeDetails.getBadgeColor(userInfoNoChecks.profileBadge);
    }

    public int getUserBadgeDarkColorResId(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserBadgeDarkColorResId");
        UserInfo userInfoNoChecks = getUserInfoNoChecks(i);
        UserTypeDetails userTypeDetails = getUserTypeDetails(userInfoNoChecks);
        if (userInfoNoChecks == null || userTypeDetails == null || !userTypeDetails.hasBadge()) {
            Slog.e(LOG_TAG, "Requested badge color for non-badged user " + i);
            return 0;
        }
        return userTypeDetails.getDarkThemeBadgeColor(userInfoNoChecks.profileBadge);
    }

    public int getUserIconBadgeResId(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserIconBadgeResId");
        UserTypeDetails userTypeDetailsNoChecks = getUserTypeDetailsNoChecks(i);
        if (userTypeDetailsNoChecks == null || !userTypeDetailsNoChecks.hasBadge()) {
            Slog.e(LOG_TAG, "Requested icon badge for non-badged user " + i);
            return 0;
        }
        return userTypeDetailsNoChecks.getIconBadge();
    }

    public int getUserBadgeResId(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserBadgeResId");
        UserTypeDetails userTypeDetailsNoChecks = getUserTypeDetailsNoChecks(i);
        if (userTypeDetailsNoChecks == null || !userTypeDetailsNoChecks.hasBadge()) {
            Slog.e(LOG_TAG, "Requested badge for non-badged user " + i);
            return 0;
        }
        return userTypeDetailsNoChecks.getBadgePlain();
    }

    public int getUserBadgeNoBackgroundResId(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserBadgeNoBackgroundResId");
        UserTypeDetails userTypeDetailsNoChecks = getUserTypeDetailsNoChecks(i);
        if (userTypeDetailsNoChecks == null || !userTypeDetailsNoChecks.hasBadge()) {
            Slog.e(LOG_TAG, "Requested badge (no background) for non-badged user " + i);
            return 0;
        }
        return userTypeDetailsNoChecks.getBadgeNoBackground();
    }

    public boolean isProfile(int i) {
        checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(i, "isProfile");
        return isProfileUnchecked(i);
    }

    private boolean isProfileUnchecked(int i) {
        boolean z;
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            z = userInfoLU != null && userInfoLU.isProfile();
        }
        return z;
    }

    public String getProfileType(int i) {
        checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(i, "getProfileType");
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            if (userInfoLU != null) {
                return userInfoLU.isProfile() ? userInfoLU.userType : "";
            }
            return null;
        }
    }

    public boolean isUserUnlockingOrUnlocked(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "isUserUnlockingOrUnlocked");
        return this.mLocalService.isUserUnlockingOrUnlocked(i);
    }

    public boolean isUserUnlocked(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "isUserUnlocked");
        return this.mLocalService.isUserUnlocked(i);
    }

    public boolean isUserRunning(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "isUserRunning");
        return this.mLocalService.isUserRunning(i);
    }

    public boolean isUserForeground(int i) {
        int callingUserId = UserHandle.getCallingUserId();
        if (callingUserId == i || hasManageUsersOrPermission("android.permission.INTERACT_ACROSS_USERS")) {
            return i == getCurrentUserId();
        }
        throw new SecurityException("Caller from user " + callingUserId + " needs MANAGE_USERS or INTERACT_ACROSS_USERS permission to check if another user (" + i + ") is running in the foreground");
    }

    public boolean isUserVisible(int i) {
        int callingUserId = UserHandle.getCallingUserId();
        if (callingUserId != i && !hasManageUsersOrPermission("android.permission.INTERACT_ACROSS_USERS")) {
            throw new SecurityException("Caller from user " + callingUserId + " needs MANAGE_USERS or INTERACT_ACROSS_USERS permission to check if another user (" + i + ") is visible");
        }
        return this.mUserVisibilityMediator.isUserVisible(i);
    }

    @VisibleForTesting
    Pair<Integer, Integer> getCurrentAndTargetUserIds() {
        ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal == null) {
            Slog.w(LOG_TAG, "getCurrentAndTargetUserId() called too early, ActivityManagerInternal is not set yet");
            return new Pair<>(-10000, -10000);
        }
        return activityManagerInternal.getCurrentAndTargetUserIds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public int getCurrentUserId() {
        ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal == null) {
            Slog.w(LOG_TAG, "getCurrentUserId() called too early, ActivityManagerInternal is not set yet");
            return -10000;
        }
        return activityManagerInternal.getCurrentUserId();
    }

    @VisibleForTesting
    boolean isCurrentUserOrRunningProfileOfCurrentUser(int i) {
        int currentUserId = getCurrentUserId();
        if (currentUserId == i) {
            return true;
        }
        if (isProfileUnchecked(i) && getProfileParentIdUnchecked(i) == currentUserId) {
            return isUserRunning(i);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUserVisibleOnDisplay(int i, int i2) {
        return this.mUserVisibilityMediator.isUserVisible(i, i2);
    }

    public int[] getVisibleUsers() {
        if (!hasManageUsersOrPermission("android.permission.INTERACT_ACROSS_USERS")) {
            throw new SecurityException("Caller needs MANAGE_USERS or INTERACT_ACROSS_USERS permission to get list of visible users");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mUserVisibilityMediator.getVisibleUsers().toArray();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int getMainDisplayIdAssignedToUser() {
        return this.mUserVisibilityMediator.getMainDisplayAssignedToUser(UserHandle.getUserId(Binder.getCallingUid()));
    }

    public String getUserName() {
        String str;
        int callingUid = Binder.getCallingUid();
        if (!hasQueryOrCreateUsersPermission() && !hasPermissionGranted("android.permission.GET_ACCOUNTS_PRIVILEGED", callingUid)) {
            throw new SecurityException("You need MANAGE_USERS, CREATE_USERS, QUERY_USERS, or GET_ACCOUNTS_PRIVILEGED permissions to: get user name");
        }
        int userId = UserHandle.getUserId(callingUid);
        synchronized (this.mUsersLock) {
            UserInfo userWithName = userWithName(getUserInfoLU(userId));
            return (userWithName == null || (str = userWithName.name) == null) ? "" : str;
        }
    }

    public long getUserStartRealtime() {
        int userId = UserHandle.getUserId(Binder.getCallingUid());
        synchronized (this.mUsersLock) {
            UserData userDataLU = getUserDataLU(userId);
            if (userDataLU == null) {
                return 0L;
            }
            return userDataLU.startRealtime;
        }
    }

    public long getUserUnlockRealtime() {
        synchronized (this.mUsersLock) {
            UserData userDataLU = getUserDataLU(UserHandle.getUserId(Binder.getCallingUid()));
            if (userDataLU == null) {
                return 0L;
            }
            return userDataLU.unlockRealtime;
        }
    }

    private void checkManageOrInteractPermissionIfCallerInOtherProfileGroup(int i, String str) {
        int callingUserId = UserHandle.getCallingUserId();
        if (callingUserId == i || isSameProfileGroupNoChecks(callingUserId, i) || hasManageUsersPermission() || hasPermissionGranted("android.permission.INTERACT_ACROSS_USERS", Binder.getCallingUid())) {
            return;
        }
        throw new SecurityException("You need INTERACT_ACROSS_USERS or MANAGE_USERS permission to: check " + str);
    }

    private void checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(int i, String str) {
        int callingUserId = UserHandle.getCallingUserId();
        if (callingUserId == i || isSameProfileGroupNoChecks(callingUserId, i) || hasQueryUsersPermission() || hasPermissionGranted("android.permission.INTERACT_ACROSS_USERS", Binder.getCallingUid())) {
            return;
        }
        throw new SecurityException("You need INTERACT_ACROSS_USERS, MANAGE_USERS, or QUERY_USERS permission to: check " + str);
    }

    private void checkQueryOrCreateUsersPermissionIfCallerInOtherProfileGroup(int i, String str) {
        int callingUserId = UserHandle.getCallingUserId();
        if (callingUserId == i || isSameProfileGroupNoChecks(callingUserId, i)) {
            return;
        }
        checkQueryOrCreateUsersPermission(str);
    }

    public boolean isDemoUser(int i) {
        boolean z;
        if (UserHandle.getCallingUserId() != i && !hasManageUsersPermission()) {
            throw new SecurityException("You need MANAGE_USERS permission to query if u=" + i + " is a demo user");
        }
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            z = userInfoLU != null && userInfoLU.isDemo();
        }
        return z;
    }

    public boolean isAdminUser(int i) {
        boolean z;
        checkQueryOrCreateUsersPermissionIfCallerInOtherProfileGroup(i, "isAdminUser");
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            z = userInfoLU != null && userInfoLU.isAdmin();
        }
        return z;
    }

    public boolean isPreCreated(int i) {
        boolean z;
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "isPreCreated");
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            z = userInfoLU != null && userInfoLU.preCreated;
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getUserSwitchability(int i) {
        int i2;
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserSwitchability");
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        timingsTraceAndSlog.traceBegin("getUserSwitchability-" + i);
        timingsTraceAndSlog.traceBegin("TM.isInCall");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
            if (telecomManager != null) {
                if (telecomManager.isInCall()) {
                    i2 = 1;
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    timingsTraceAndSlog.traceEnd();
                    timingsTraceAndSlog.traceBegin("hasUserRestriction-DISALLOW_USER_SWITCH");
                    if (this.mLocalService.hasUserRestriction("no_user_switch", i)) {
                        i2 |= 2;
                    }
                    timingsTraceAndSlog.traceEnd();
                    if (!isHeadlessSystemUserMode()) {
                        timingsTraceAndSlog.traceBegin("getInt-ALLOW_USER_SWITCHING_WHEN_SYSTEM_USER_LOCKED");
                        boolean z = Settings.Global.getInt(this.mContext.getContentResolver(), "allow_user_switching_when_system_user_locked", 0) != 0;
                        timingsTraceAndSlog.traceEnd();
                        timingsTraceAndSlog.traceBegin("isUserUnlocked-USER_SYSTEM");
                        boolean isUserUnlocked = this.mLocalService.isUserUnlocked(0);
                        timingsTraceAndSlog.traceEnd();
                        if (!z && !isUserUnlocked) {
                            i2 |= 4;
                        }
                    }
                    timingsTraceAndSlog.traceEnd();
                    return i2;
                }
            }
            i2 = 0;
            Binder.restoreCallingIdentity(clearCallingIdentity);
            timingsTraceAndSlog.traceEnd();
            timingsTraceAndSlog.traceBegin("hasUserRestriction-DISALLOW_USER_SWITCH");
            if (this.mLocalService.hasUserRestriction("no_user_switch", i)) {
            }
            timingsTraceAndSlog.traceEnd();
            if (!isHeadlessSystemUserMode()) {
            }
            timingsTraceAndSlog.traceEnd();
            return i2;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    boolean isUserSwitcherEnabled(int i) {
        return UserManager.supportsMultipleUsers() && !hasUserRestriction("no_user_switch", i) && !UserManager.isDeviceInDemoMode(this.mContext) && (Settings.Global.getInt(this.mContext.getContentResolver(), "user_switcher_enabled", Resources.getSystem().getBoolean(17891816) ? 1 : 0) != 0) == true;
    }

    public boolean isUserSwitcherEnabled(boolean z, int i) {
        if (isUserSwitcherEnabled(i)) {
            return z || !hasUserRestriction("no_add_user", i) || areThereMultipleSwitchableUsers();
        }
        return false;
    }

    private boolean areThereMultipleSwitchableUsers() {
        Iterator<UserInfo> it = getUsers(true, true, true).iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (it.next().supportsSwitchToByUser()) {
                if (z) {
                    return true;
                }
                z = true;
            }
        }
        return false;
    }

    public boolean isRestricted(int i) {
        boolean isRestricted;
        if (i != UserHandle.getCallingUserId()) {
            checkCreateUsersPermission("query isRestricted for user " + i);
        }
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            isRestricted = userInfoLU == null ? false : userInfoLU.isRestricted();
        }
        return isRestricted;
    }

    public boolean canHaveRestrictedProfile(int i) {
        checkManageUsersPermission("canHaveRestrictedProfile");
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            boolean z = false;
            if (userInfoLU != null && userInfoLU.canHaveProfile()) {
                if (!userInfoLU.isAdmin()) {
                    return false;
                }
                if (!this.mIsDeviceManaged && !this.mIsUserManaged.get(i)) {
                    z = true;
                }
                return z;
            }
            return false;
        }
    }

    public boolean hasRestrictedProfiles(int i) {
        checkManageUsersPermission("hasRestrictedProfiles");
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i2 = 0; i2 < size; i2++) {
                UserInfo userInfo = this.mUsers.valueAt(i2).info;
                if (i != userInfo.id && userInfo.restrictedProfileParentId == i) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mUsersLock"})
    public UserInfo getUserInfoLU(int i) {
        UserData userData = this.mUsers.get(i);
        if (userData == null || !userData.info.partial || this.mRemovingUserIds.get(i)) {
            if (userData != null) {
                return userData.info;
            }
            return null;
        }
        Slog.w(LOG_TAG, "getUserInfo: unknown user #" + i);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mUsersLock"})
    public UserData getUserDataLU(int i) {
        UserData userData = this.mUsers.get(i);
        if (userData == null || !userData.info.partial || this.mRemovingUserIds.get(i)) {
            return userData;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserInfo getUserInfoNoChecks(int i) {
        UserInfo userInfo;
        synchronized (this.mUsersLock) {
            UserData userData = this.mUsers.get(i);
            userInfo = userData != null ? userData.info : null;
        }
        return userInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserData getUserDataNoChecks(int i) {
        UserData userData;
        synchronized (this.mUsersLock) {
            userData = this.mUsers.get(i);
        }
        return userData;
    }

    public boolean exists(int i) {
        return this.mLocalService.exists(i);
    }

    private int getCrossProfileIntentFilterAccessControl(int i) {
        UserProperties userPropertiesInternal = getUserPropertiesInternal(i);
        if (userPropertiesInternal != null) {
            return userPropertiesInternal.getCrossProfileIntentFilterAccessControl();
        }
        return 0;
    }

    public void enforceCrossProfileIntentFilterAccess(int i, int i2, int i3, boolean z) {
        if (isCrossProfileIntentFilterAccessible(i, i2, z)) {
            return;
        }
        throw new SecurityException("CrossProfileIntentFilter cannot be accessed by user " + i3);
    }

    public boolean isCrossProfileIntentFilterAccessible(int i, int i2, boolean z) {
        int crossProfileIntentFilterAccessControl = getCrossProfileIntentFilterAccessControl(i, i2);
        if (10 == crossProfileIntentFilterAccessControl && !PackageManagerServiceUtils.isSystemOrRoot()) {
            return false;
        }
        if (20 == crossProfileIntentFilterAccessControl) {
            return z && PackageManagerServiceUtils.isSystemOrRoot();
        }
        return true;
    }

    public int getCrossProfileIntentFilterAccessControl(int i, int i2) {
        return Math.max(getCrossProfileIntentFilterAccessControl(i), getCrossProfileIntentFilterAccessControl(i2));
    }

    public void setUserName(int i, String str) {
        checkManageUsersPermission("rename users");
        synchronized (this.mPackagesLock) {
            UserData userDataNoChecks = getUserDataNoChecks(i);
            if (userDataNoChecks != null) {
                UserInfo userInfo = userDataNoChecks.info;
                if (!userInfo.partial) {
                    if (Objects.equals(str, userInfo.name)) {
                        Slogf.i(LOG_TAG, "setUserName: ignoring for user #%d as it didn't change (%s)", Integer.valueOf(i), getRedacted(str));
                        return;
                    }
                    if (str == null) {
                        Slogf.i(LOG_TAG, "setUserName: resetting name of user #%d", Integer.valueOf(i));
                    } else {
                        Slogf.i(LOG_TAG, "setUserName: setting name of user #%d to %s", Integer.valueOf(i), getRedacted(str));
                    }
                    userDataNoChecks.info.name = str;
                    writeUserLP(userDataNoChecks);
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        sendUserInfoChangedBroadcast(i);
                        return;
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
            Slogf.w(LOG_TAG, "setUserName: unknown user #%d", Integer.valueOf(i));
        }
    }

    public boolean setUserEphemeral(int i, boolean z) {
        checkCreateUsersPermission("update ephemeral user flag");
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                UserData userData = this.mUsers.get(i);
                if (userData == null) {
                    Slog.e(LOG_TAG, "User not found for setting ephemeral mode: u" + i);
                    return false;
                }
                UserInfo userInfo = userData.info;
                int i2 = userInfo.flags;
                boolean z2 = (i2 & 256) != 0;
                if (((i2 & 8192) != 0) && !z) {
                    Slog.e(LOG_TAG, "Failed to change user state to non-ephemeral for user " + i);
                    return false;
                }
                if (z2 == z) {
                    userData = null;
                } else if (z) {
                    userInfo.flags = i2 | 256;
                } else {
                    userInfo.flags = i2 & (-257);
                }
                if (userData != null) {
                    writeUserLP(userData);
                }
                return true;
            }
        }
    }

    public void setUserIcon(int i, Bitmap bitmap) {
        try {
            checkManageUsersPermission("update users");
            enforceUserRestriction("no_set_user_icon", i, "Cannot set user icon");
            this.mLocalService.setUserIcon(i, bitmap);
        } catch (UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUserInfoChangedBroadcast(int i) {
        Intent intent = new Intent("android.intent.action.USER_INFO_CHANGED");
        intent.putExtra("android.intent.extra.user_handle", i);
        intent.addFlags(1073741824);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public ParcelFileDescriptor getUserIcon(int i) {
        if (!hasManageUsersOrPermission("android.permission.GET_ACCOUNTS_PRIVILEGED")) {
            throw new SecurityException("You need MANAGE_USERS or GET_ACCOUNTS_PRIVILEGED permissions to: get user icon");
        }
        synchronized (this.mPackagesLock) {
            UserInfo userInfoNoChecks = getUserInfoNoChecks(i);
            if (userInfoNoChecks != null && !userInfoNoChecks.partial) {
                int callingUserId = UserHandle.getCallingUserId();
                int i2 = getUserInfoNoChecks(callingUserId).profileGroupId;
                boolean z = i2 != -10000 && i2 == userInfoNoChecks.profileGroupId;
                if (callingUserId != i && !z) {
                    checkManageUsersPermission("get the icon of a user who is not related");
                }
                String str = userInfoNoChecks.iconPath;
                if (str == null) {
                    return null;
                }
                try {
                    return ParcelFileDescriptor.open(new File(str), 268435456);
                } catch (FileNotFoundException e) {
                    Slog.e(LOG_TAG, "Couldn't find icon file", e);
                    return null;
                }
            }
            Slog.w(LOG_TAG, "getUserIcon: unknown user #" + i);
            return null;
        }
    }

    public void makeInitialized(int i) {
        boolean z;
        checkManageUsersPermission("makeInitialized");
        synchronized (this.mUsersLock) {
            UserData userData = this.mUsers.get(i);
            if (userData != null) {
                UserInfo userInfo = userData.info;
                if (!userInfo.partial) {
                    int i2 = userInfo.flags;
                    if ((i2 & 16) == 0) {
                        userInfo.flags = i2 | 16;
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        scheduleWriteUser(i);
                        return;
                    }
                    return;
                }
            }
            Slog.w(LOG_TAG, "makeInitialized: unknown user #" + i);
        }
    }

    private void initDefaultGuestRestrictions() {
        synchronized (this.mGuestRestrictions) {
            if (this.mGuestRestrictions.isEmpty()) {
                UserTypeDetails userTypeDetails = this.mUserTypes.get("android.os.usertype.full.GUEST");
                if (userTypeDetails == null) {
                    Slog.wtf(LOG_TAG, "Can't set default guest restrictions: type doesn't exist.");
                    return;
                }
                userTypeDetails.addDefaultRestrictionsTo(this.mGuestRestrictions);
            }
        }
    }

    public Bundle getDefaultGuestRestrictions() {
        Bundle bundle;
        checkManageUsersPermission("getDefaultGuestRestrictions");
        synchronized (this.mGuestRestrictions) {
            bundle = new Bundle(this.mGuestRestrictions);
        }
        return bundle;
    }

    public void setDefaultGuestRestrictions(Bundle bundle) {
        checkManageUsersPermission("setDefaultGuestRestrictions");
        synchronized (this.mGuestRestrictions) {
            this.mGuestRestrictions.clear();
            this.mGuestRestrictions.putAll(bundle);
            List<UserInfo> guestUsers = getGuestUsers();
            for (int i = 0; i < guestUsers.size(); i++) {
                synchronized (this.mRestrictionsLock) {
                    updateUserRestrictionsInternalLR(this.mGuestRestrictions, guestUsers.get(i).id);
                }
            }
        }
        synchronized (this.mPackagesLock) {
            writeUserListLP();
        }
    }

    @VisibleForTesting
    void setUserRestrictionInner(int i, String str, boolean z) {
        if (!UserRestrictionsUtils.isValidRestriction(str)) {
            Slog.e(LOG_TAG, "Setting invalid restriction " + str);
            return;
        }
        synchronized (this.mRestrictionsLock) {
            Bundle clone = BundleUtils.clone(this.mDevicePolicyUserRestrictions.getRestrictions(i));
            clone.putBoolean(str, z);
            if (this.mDevicePolicyUserRestrictions.updateRestrictions(i, clone)) {
                if (i == -1) {
                    applyUserRestrictionsForAllUsersLR();
                } else {
                    applyUserRestrictionsLR(i);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDevicePolicyUserRestrictionsInner(int i, Bundle bundle, RestrictionsSet restrictionsSet, boolean z) {
        synchronized (this.mRestrictionsLock) {
            IntArray userIds = this.mDevicePolicyUserRestrictions.getUserIds();
            this.mCachedEffectiveUserRestrictions.removeAllRestrictions();
            this.mDevicePolicyUserRestrictions.removeAllRestrictions();
            this.mDevicePolicyUserRestrictions.updateRestrictions(-1, bundle);
            IntArray userIds2 = restrictionsSet.getUserIds();
            for (int i2 = 0; i2 < userIds2.size(); i2++) {
                int i3 = userIds2.get(i2);
                this.mDevicePolicyUserRestrictions.updateRestrictions(i3, restrictionsSet.getRestrictions(i3));
                userIds.add(i3);
            }
            applyUserRestrictionsForAllUsersLR();
            for (int i4 = 0; i4 < userIds.size(); i4++) {
                if (userIds.get(i4) != -1) {
                    applyUserRestrictionsLR(userIds.get(i4));
                }
            }
        }
    }

    @GuardedBy({"mRestrictionsLock"})
    private Bundle computeEffectiveUserRestrictionsLR(int i) {
        Bundle restrictionsNonNull = this.mBaseUserRestrictions.getRestrictionsNonNull(i);
        Bundle restrictionsNonNull2 = this.mDevicePolicyUserRestrictions.getRestrictionsNonNull(-1);
        Bundle restrictionsNonNull3 = this.mDevicePolicyUserRestrictions.getRestrictionsNonNull(i);
        if (restrictionsNonNull2.isEmpty() && restrictionsNonNull3.isEmpty()) {
            return restrictionsNonNull;
        }
        Bundle clone = BundleUtils.clone(restrictionsNonNull);
        UserRestrictionsUtils.merge(clone, restrictionsNonNull2);
        UserRestrictionsUtils.merge(clone, restrictionsNonNull3);
        return clone;
    }

    @GuardedBy({"mRestrictionsLock"})
    private void invalidateEffectiveUserRestrictionsLR(int i) {
        this.mCachedEffectiveUserRestrictions.remove(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle getEffectiveUserRestrictions(int i) {
        Bundle restrictions;
        synchronized (this.mRestrictionsLock) {
            restrictions = this.mCachedEffectiveUserRestrictions.getRestrictions(i);
            if (restrictions == null) {
                restrictions = computeEffectiveUserRestrictionsLR(i);
                this.mCachedEffectiveUserRestrictions.updateRestrictions(i, restrictions);
            }
        }
        return restrictions;
    }

    public boolean hasUserRestriction(String str, int i) {
        if (!userExists(i)) {
            return false;
        }
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "hasUserRestriction");
        return this.mLocalService.hasUserRestriction(str, i);
    }

    public boolean hasUserRestrictionOnAnyUser(String str) {
        if (!UserRestrictionsUtils.isValidRestriction(str)) {
            return false;
        }
        List<UserInfo> users = getUsers(true);
        for (int i = 0; i < users.size(); i++) {
            Bundle effectiveUserRestrictions = getEffectiveUserRestrictions(users.get(i).id);
            if (effectiveUserRestrictions != null && effectiveUserRestrictions.getBoolean(str)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSettingRestrictedForUser(String str, int i, String str2, int i2) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Non-system caller");
        }
        return UserRestrictionsUtils.isSettingRestrictedForUser(this.mContext, str, i, str2, i2);
    }

    public void addUserRestrictionsListener(final IUserRestrictionsListener iUserRestrictionsListener) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Non-system caller");
        }
        this.mLocalService.addUserRestrictionsListener(new UserManagerInternal.UserRestrictionsListener() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda2
            @Override // com.android.server.pm.UserManagerInternal.UserRestrictionsListener
            public final void onUserRestrictionsChanged(int i, Bundle bundle, Bundle bundle2) {
                UserManagerService.lambda$addUserRestrictionsListener$0(iUserRestrictionsListener, i, bundle, bundle2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addUserRestrictionsListener$0(IUserRestrictionsListener iUserRestrictionsListener, int i, Bundle bundle, Bundle bundle2) {
        try {
            iUserRestrictionsListener.onUserRestrictionsChanged(i, bundle, bundle2);
        } catch (RemoteException e) {
            Slog.e("IUserRestrictionsListener", "Unable to invoke listener: " + e.getMessage());
        }
    }

    public int getUserRestrictionSource(String str, int i) {
        List<UserManager.EnforcingUser> userRestrictionSources = getUserRestrictionSources(str, i);
        int i2 = 0;
        for (int size = userRestrictionSources.size() - 1; size >= 0; size--) {
            i2 |= userRestrictionSources.get(size).getUserRestrictionSource();
        }
        return i2;
    }

    public List<UserManager.EnforcingUser> getUserRestrictionSources(String str, int i) {
        checkQueryUsersPermission("call getUserRestrictionSources.");
        if (!hasUserRestriction(str, i)) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        if (hasBaseUserRestriction(str, i)) {
            arrayList.add(new UserManager.EnforcingUser(-10000, 1));
        }
        arrayList.addAll(getDevicePolicyManagerInternal().getUserRestrictionSources(str, i));
        return arrayList;
    }

    public Bundle getUserRestrictions(int i) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(i, "getUserRestrictions");
        return BundleUtils.clone(getEffectiveUserRestrictions(i));
    }

    public boolean hasBaseUserRestriction(String str, int i) {
        checkCreateUsersPermission("hasBaseUserRestriction");
        boolean z = false;
        if (!UserRestrictionsUtils.isValidRestriction(str)) {
            return false;
        }
        synchronized (this.mRestrictionsLock) {
            Bundle restrictions = this.mBaseUserRestrictions.getRestrictions(i);
            if (restrictions != null && restrictions.getBoolean(str, false)) {
                z = true;
            }
        }
        return z;
    }

    public void setUserRestriction(String str, boolean z, int i) {
        checkManageUsersPermission("setUserRestriction");
        if (UserRestrictionsUtils.isValidRestriction(str)) {
            if (!userExists(i)) {
                Slogf.w(LOG_TAG, "Cannot set user restriction %s. User with id %d does not exist", str, Integer.valueOf(i));
                return;
            }
            synchronized (this.mRestrictionsLock) {
                Bundle clone = BundleUtils.clone(this.mBaseUserRestrictions.getRestrictions(i));
                clone.putBoolean(str, z);
                updateUserRestrictionsInternalLR(clone, i);
            }
        }
    }

    @GuardedBy({"mRestrictionsLock"})
    private void updateUserRestrictionsInternalLR(Bundle bundle, final int i) {
        Bundle nonNull = UserRestrictionsUtils.nonNull(this.mAppliedUserRestrictions.getRestrictions(i));
        if (bundle != null) {
            Preconditions.checkState(this.mBaseUserRestrictions.getRestrictions(i) != bundle);
            Preconditions.checkState(this.mCachedEffectiveUserRestrictions.getRestrictions(i) != bundle);
            if (this.mBaseUserRestrictions.updateRestrictions(i, bundle)) {
                scheduleWriteUser(i);
            }
        }
        final Bundle computeEffectiveUserRestrictionsLR = computeEffectiveUserRestrictionsLR(i);
        this.mCachedEffectiveUserRestrictions.updateRestrictions(i, computeEffectiveUserRestrictionsLR);
        if (this.mAppOpsService != null) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    UserManagerService.this.lambda$updateUserRestrictionsInternalLR$1(computeEffectiveUserRestrictionsLR, i);
                }
            });
        }
        propagateUserRestrictionsLR(i, computeEffectiveUserRestrictionsLR, nonNull);
        this.mAppliedUserRestrictions.updateRestrictions(i, new Bundle(computeEffectiveUserRestrictionsLR));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUserRestrictionsInternalLR$1(Bundle bundle, int i) {
        try {
            this.mAppOpsService.setUserRestrictions(bundle, this.mUserRestrictionToken, i);
        } catch (RemoteException unused) {
            Slog.w(LOG_TAG, "Unable to notify AppOpsService of UserRestrictions");
        }
    }

    @GuardedBy({"mRestrictionsLock"})
    private void propagateUserRestrictionsLR(final int i, Bundle bundle, Bundle bundle2) {
        if (UserRestrictionsUtils.areEqual(bundle, bundle2)) {
            return;
        }
        final Bundle bundle3 = new Bundle(bundle);
        final Bundle bundle4 = new Bundle(bundle2);
        this.mHandler.post(new Runnable() { // from class: com.android.server.pm.UserManagerService.3
            @Override // java.lang.Runnable
            public void run() {
                int size;
                UserManagerInternal.UserRestrictionsListener[] userRestrictionsListenerArr;
                UserRestrictionsUtils.applyUserRestrictions(UserManagerService.this.mContext, i, bundle3, bundle4);
                synchronized (UserManagerService.this.mUserRestrictionsListeners) {
                    size = UserManagerService.this.mUserRestrictionsListeners.size();
                    userRestrictionsListenerArr = new UserManagerInternal.UserRestrictionsListener[size];
                    UserManagerService.this.mUserRestrictionsListeners.toArray(userRestrictionsListenerArr);
                }
                for (int i2 = 0; i2 < size; i2++) {
                    userRestrictionsListenerArr[i2].onUserRestrictionsChanged(i, bundle3, bundle4);
                }
                UserManagerService.this.mContext.sendBroadcastAsUser(new Intent("android.os.action.USER_RESTRICTIONS_CHANGED").setFlags(1073741824), UserHandle.of(i), null, BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).toBundle());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mRestrictionsLock"})
    public void applyUserRestrictionsLR(int i) {
        updateUserRestrictionsInternalLR(null, i);
        scheduleWriteUser(i);
    }

    @GuardedBy({"mRestrictionsLock"})
    private void applyUserRestrictionsForAllUsersLR() {
        this.mCachedEffectiveUserRestrictions.removeAllRestrictions();
        this.mHandler.post(new Runnable() { // from class: com.android.server.pm.UserManagerService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    int[] runningUserIds = ActivityManager.getService().getRunningUserIds();
                    synchronized (UserManagerService.this.mRestrictionsLock) {
                        for (int i : runningUserIds) {
                            UserManagerService.this.applyUserRestrictionsLR(i);
                        }
                    }
                } catch (RemoteException unused) {
                    Slog.w(UserManagerService.LOG_TAG, "Unable to access ActivityManagerService");
                }
            }
        });
    }

    private boolean isUserLimitReached() {
        int aliveUsersExcludingGuestsCountLU;
        synchronized (this.mUsersLock) {
            aliveUsersExcludingGuestsCountLU = getAliveUsersExcludingGuestsCountLU();
        }
        return aliveUsersExcludingGuestsCountLU >= UserManager.getMaxSupportedUsers() && !isCreationOverrideEnabled();
    }

    private boolean canAddMoreUsersOfType(UserTypeDetails userTypeDetails) {
        if (!isUserTypeEnabled(userTypeDetails)) {
            return false;
        }
        int maxAllowed = userTypeDetails.getMaxAllowed();
        if (maxAllowed == -1) {
            return true;
        }
        return getNumberOfUsersOfType(userTypeDetails.getName()) < maxAllowed || isCreationOverrideEnabled();
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x003c A[Catch: all -> 0x007b, TryCatch #0 {, blocks: (B:8:0x001b, B:10:0x0028, B:13:0x002f, B:14:0x0036, B:16:0x003c, B:18:0x004a, B:26:0x0057, B:32:0x005d, B:34:0x005f, B:37:0x0071, B:38:0x0079, B:40:0x0067), top: B:7:0x001b }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x005d A[Catch: all -> 0x007b, DONT_GENERATE, TryCatch #0 {, blocks: (B:8:0x001b, B:10:0x0028, B:13:0x002f, B:14:0x0036, B:16:0x003c, B:18:0x004a, B:26:0x0057, B:32:0x005d, B:34:0x005f, B:37:0x0071, B:38:0x0079, B:40:0x0067), top: B:7:0x001b }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x005f A[Catch: all -> 0x007b, TryCatch #0 {, blocks: (B:8:0x001b, B:10:0x0028, B:13:0x002f, B:14:0x0036, B:16:0x003c, B:18:0x004a, B:26:0x0057, B:32:0x005d, B:34:0x005f, B:37:0x0071, B:38:0x0079, B:40:0x0067), top: B:7:0x001b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getRemainingCreatableUserCount(String str) {
        int i;
        checkQueryOrCreateUsersPermission("get the remaining number of users that can be added.");
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        if (userTypeDetails == null || !isUserTypeEnabled(userTypeDetails)) {
            return 0;
        }
        synchronized (this.mUsersLock) {
            int aliveUsersExcludingGuestsCountLU = getAliveUsersExcludingGuestsCountLU();
            int i2 = Integer.MAX_VALUE;
            if (!UserManager.isUserTypeGuest(str) && !UserManager.isUserTypeDemo(str)) {
                i = UserManager.getMaxSupportedUsers() - aliveUsersExcludingGuestsCountLU;
                if (userTypeDetails.isManagedProfile()) {
                    if (!this.mContext.getPackageManager().hasSystemFeature("android.software.managed_users")) {
                        return 0;
                    }
                    if ((aliveUsersExcludingGuestsCountLU == 1) & (i <= 0)) {
                        i = 1;
                    }
                }
                if (i > 0) {
                    return 0;
                }
                if (userTypeDetails.getMaxAllowed() != -1) {
                    i2 = userTypeDetails.getMaxAllowed() - getNumberOfUsersOfType(str);
                }
                return Math.max(0, Math.min(i, i2));
            }
            i = Integer.MAX_VALUE;
            if (userTypeDetails.isManagedProfile()) {
            }
            if (i > 0) {
            }
        }
    }

    private int getNumberOfUsersOfType(String str) {
        int i;
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                UserInfo userInfo = this.mUsers.valueAt(i2).info;
                if (userInfo.userType.equals(str) && !userInfo.guestToRemove && !this.mRemovingUserIds.get(userInfo.id) && !userInfo.preCreated) {
                    i++;
                }
            }
        }
        return i;
    }

    public boolean canAddMoreUsersOfType(String str) {
        checkCreateUsersPermission("check if more users can be added.");
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        return userTypeDetails != null && canAddMoreUsersOfType(userTypeDetails);
    }

    public boolean isUserTypeEnabled(String str) {
        checkCreateUsersPermission("check if user type is enabled.");
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        return userTypeDetails != null && isUserTypeEnabled(userTypeDetails);
    }

    private boolean isUserTypeEnabled(UserTypeDetails userTypeDetails) {
        return userTypeDetails.isEnabled() || isCreationOverrideEnabled();
    }

    private boolean isCreationOverrideEnabled() {
        return Build.isDebuggable() && SystemProperties.getBoolean("debug.user.creation_override", false);
    }

    public boolean canAddMoreManagedProfiles(int i, boolean z) {
        return canAddMoreProfilesToUser("android.os.usertype.profile.MANAGED", i, z);
    }

    public boolean canAddMoreProfilesToUser(String str, int i, boolean z) {
        return getRemainingCreatableProfileCount(str, i, z) > 0 || isCreationOverrideEnabled();
    }

    public int getRemainingCreatableProfileCount(String str, int i) {
        return getRemainingCreatableProfileCount(str, i, false);
    }

    private int getRemainingCreatableProfileCount(String str, int i, boolean z) {
        checkQueryOrCreateUsersPermission("get the remaining number of profiles that can be added to the given user.");
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        if (userTypeDetails == null || !isUserTypeEnabled(userTypeDetails)) {
            return 0;
        }
        boolean isManagedProfile = userTypeDetails.isManagedProfile();
        if (isManagedProfile && !this.mContext.getPackageManager().hasSystemFeature("android.software.managed_users")) {
            return 0;
        }
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            if (userInfoLU != null && userInfoLU.canHaveProfile()) {
                int length = getProfileIds(i, str, false).length;
                int i2 = 1;
                int i3 = (length <= 0 || !z) ? 0 : 1;
                int aliveUsersExcludingGuestsCountLU = getAliveUsersExcludingGuestsCountLU() - i3;
                int maxSupportedUsers = UserManager.getMaxSupportedUsers() - aliveUsersExcludingGuestsCountLU;
                if (maxSupportedUsers > 0 || !isManagedProfile || aliveUsersExcludingGuestsCountLU != 1) {
                    i2 = maxSupportedUsers;
                }
                int maxUsersOfTypePerParent = getMaxUsersOfTypePerParent(userTypeDetails);
                if (maxUsersOfTypePerParent != -1) {
                    i2 = Math.min(i2, maxUsersOfTypePerParent - (length - i3));
                }
                if (i2 <= 0) {
                    return 0;
                }
                if (userTypeDetails.getMaxAllowed() != -1) {
                    i2 = Math.min(i2, userTypeDetails.getMaxAllowed() - (getNumberOfUsersOfType(str) - i3));
                }
                return Math.max(0, i2);
            }
            return 0;
        }
    }

    @GuardedBy({"mUsersLock"})
    private int getAliveUsersExcludingGuestsCountLU() {
        int size = this.mUsers.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            UserInfo userInfo = this.mUsers.valueAt(i2).info;
            if (!this.mRemovingUserIds.get(userInfo.id) && !userInfo.isGuest() && !userInfo.preCreated && !this.mWrapper.getExtImpl().isCustomUser(userInfo.flags)) {
                i++;
            }
        }
        return i;
    }

    private static final void checkManageUserAndAcrossUsersFullPermission(String str) {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 1000 || callingUid == 0) {
            return;
        }
        if (hasPermissionGranted("android.permission.MANAGE_USERS", callingUid) && hasPermissionGranted("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid)) {
            return;
        }
        throw new SecurityException("You need MANAGE_USERS and INTERACT_ACROSS_USERS_FULL permission to: " + str);
    }

    private static boolean hasPermissionGranted(String str, int i) {
        return ActivityManager.checkComponentPermission(str, i, -1, true) == 0;
    }

    private static final void checkManageUsersPermission(String str) {
        if (hasManageUsersPermission()) {
            return;
        }
        throw new SecurityException("You need MANAGE_USERS permission to: " + str);
    }

    private static final void checkCreateUsersPermission(String str) {
        if (hasCreateUsersPermission()) {
            return;
        }
        throw new SecurityException("You either need MANAGE_USERS or CREATE_USERS permission to: " + str);
    }

    private static final void checkQueryUsersPermission(String str) {
        if (hasQueryUsersPermission()) {
            return;
        }
        throw new SecurityException("You either need MANAGE_USERS or QUERY_USERS permission to: " + str);
    }

    private static final void checkQueryOrCreateUsersPermission(String str) {
        if (hasQueryOrCreateUsersPermission()) {
            return;
        }
        throw new SecurityException("You either need MANAGE_USERS, CREATE_USERS, or QUERY_USERS permission to: " + str);
    }

    private static final void checkCreateUsersPermission(int i) {
        if (((-38701) & i) == 0) {
            if (hasCreateUsersPermission()) {
                return;
            }
            throw new SecurityException("You either need MANAGE_USERS or CREATE_USERS permission to create an user with flags: " + i);
        }
        if (hasManageUsersPermission()) {
            return;
        }
        throw new SecurityException("You need MANAGE_USERS permission to create an user  with flags: " + i);
    }

    private static final boolean hasManageUsersPermission() {
        return hasManageUsersPermission(Binder.getCallingUid());
    }

    private static boolean hasManageUsersPermission(int i) {
        return UserHandle.isSameApp(i, 1000) || i == 0 || hasPermissionGranted("android.permission.MANAGE_USERS", i);
    }

    private static final boolean hasManageUsersOrPermission(String str) {
        int callingUid = Binder.getCallingUid();
        return hasManageUsersPermission(callingUid) || hasPermissionGranted(str, callingUid);
    }

    private static final boolean hasCreateUsersPermission() {
        return hasManageUsersOrPermission("android.permission.CREATE_USERS");
    }

    private static final boolean hasQueryUsersPermission() {
        return hasManageUsersOrPermission("android.permission.QUERY_USERS");
    }

    private static final boolean hasQueryOrCreateUsersPermission() {
        return hasCreateUsersPermission() || hasPermissionGranted("android.permission.QUERY_USERS", Binder.getCallingUid());
    }

    private static void checkSystemOrRoot(String str) {
        int callingUid = Binder.getCallingUid();
        if (UserHandle.isSameApp(callingUid, 1000) || callingUid == 0) {
            return;
        }
        throw new SecurityException("Only system may: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPackagesLock"})
    public void writeBitmapLP(UserInfo userInfo, Bitmap bitmap) {
        try {
            File file = new File(this.mUsersDir, Integer.toString(userInfo.id));
            File file2 = new File(file, USER_PHOTO_FILENAME);
            File file3 = new File(file, USER_PHOTO_FILENAME_TMP);
            if (!file.exists()) {
                file.mkdir();
                FileUtils.setPermissions(file.getPath(), 505, -1, -1);
            }
            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
            FileOutputStream fileOutputStream = new FileOutputStream(file3);
            if (bitmap.compress(compressFormat, 100, fileOutputStream) && file3.renameTo(file2) && SELinux.restorecon(file2)) {
                userInfo.iconPath = file2.getAbsolutePath();
            }
            try {
                fileOutputStream.close();
            } catch (IOException unused) {
            }
            file3.delete();
        } catch (FileNotFoundException e) {
            Slog.w(LOG_TAG, "Error setting photo for user ", e);
        }
    }

    public int[] getUserIds() {
        int[] iArr;
        synchronized (this.mUsersLock) {
            iArr = this.mUserIds;
        }
        return iArr;
    }

    @VisibleForTesting
    boolean userExists(int i) {
        synchronized (this.mUsersLock) {
            for (int i2 : this.mUserIds) {
                if (i2 == i) {
                    return true;
                }
            }
            return false;
        }
    }

    public int[] getUserIdsIncludingPreCreated() {
        int[] iArr;
        synchronized (this.mUsersLock) {
            iArr = this.mUserIdsIncludingPreCreated;
        }
        return iArr;
    }

    public boolean isHeadlessSystemUserMode() {
        boolean z;
        synchronized (this.mUsersLock) {
            z = this.mUsers.get(0).info.isFull() ? false : true;
        }
        return z;
    }

    private boolean isDefaultHeadlessSystemUserMode() {
        if (!Build.isDebuggable()) {
            return RoSystemProperties.MULTIUSER_HEADLESS_SYSTEM_USER;
        }
        String str = SystemProperties.get("persist.debug.user_mode_emulation");
        if (!TextUtils.isEmpty(str)) {
            if ("headless".equals(str)) {
                return true;
            }
            if ("full".equals(str)) {
                return false;
            }
            if (!HealthServiceWrapperHidl.INSTANCE_VENDOR.equals(str)) {
                Slogf.e(LOG_TAG, "isDefaultHeadlessSystemUserMode(): ignoring invalid valued of property %s: %s", "persist.debug.user_mode_emulation", str);
            }
        }
        return RoSystemProperties.MULTIUSER_HEADLESS_SYSTEM_USER;
    }

    private void emulateSystemUserModeIfNeeded() {
        String str;
        int i;
        UserInfo earliestCreatedFullUser;
        if (Build.isDebuggable() && !TextUtils.isEmpty(SystemProperties.get("persist.debug.user_mode_emulation"))) {
            boolean isDefaultHeadlessSystemUserMode = isDefaultHeadlessSystemUserMode();
            synchronized (this.mPackagesLock) {
                synchronized (this.mUsersLock) {
                    boolean z = false;
                    UserData userData = this.mUsers.get(0);
                    if (userData == null) {
                        Slogf.wtf(LOG_TAG, "emulateSystemUserModeIfNeeded(): no system user data");
                        return;
                    }
                    int mainUserIdUnchecked = getMainUserIdUnchecked();
                    UserInfo userInfo = userData.info;
                    int i2 = userInfo.flags;
                    if (isDefaultHeadlessSystemUserMode) {
                        str = "android.os.usertype.system.HEADLESS";
                        i = i2 & (-1025) & (-16385);
                    } else {
                        str = "android.os.usertype.full.SYSTEM";
                        i = i2 | 1024 | 16384;
                    }
                    if (userInfo.userType.equals(str)) {
                        Slogf.d(LOG_TAG, "emulateSystemUserModeIfNeeded(): system user type is already %s, returning", str);
                        return;
                    }
                    Slogf.i(LOG_TAG, "Persisting emulated system user data: type changed from %s to %s, flags changed from %s to %s", userData.info.userType, str, UserInfo.flagsToString(i2), UserInfo.flagsToString(i));
                    UserInfo userInfo2 = userData.info;
                    userInfo2.userType = str;
                    userInfo2.flags = i;
                    writeUserLP(userData);
                    UserData userDataNoChecks = getUserDataNoChecks(mainUserIdUnchecked);
                    if (isDefaultHeadlessSystemUserMode) {
                        if (userDataNoChecks != null && (userDataNoChecks.info.flags & 2048) == 0) {
                            z = true;
                        }
                        if (!z && isMainUserPermanentAdmin() && (earliestCreatedFullUser = getEarliestCreatedFullUser()) != null) {
                            Slogf.i(LOG_TAG, "Designating user " + earliestCreatedFullUser.id + " to be Main");
                            earliestCreatedFullUser.flags = earliestCreatedFullUser.flags | 16384;
                            writeUserLP(getUserDataNoChecks(earliestCreatedFullUser.id));
                        }
                    } else if (userDataNoChecks != null && (userDataNoChecks.info.flags & 2048) == 0) {
                        Slogf.i(LOG_TAG, "Transferring Main to user 0 from " + userDataNoChecks.info.id);
                        UserInfo userInfo3 = userDataNoChecks.info;
                        userInfo3.flags = userInfo3.flags & (-16385);
                        writeUserLP(userDataNoChecks);
                    } else {
                        Slogf.i(LOG_TAG, "Designated user 0 to be Main");
                    }
                    this.mUpdatingSystemUserMode = true;
                }
            }
        }
    }

    private ResilientAtomicFile getUserListFile() {
        return new ResilientAtomicFile(this.mUserListFile, new File(this.mUserListFile.getParent(), this.mUserListFile.getName() + ".backup"), new File(this.mUserListFile.getParent(), this.mUserListFile.getName() + ".reservecopy"), 505, "user list", new ResilientAtomicFile.ReadEventLogger() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda3
            @Override // com.android.server.pm.ResilientAtomicFile.ReadEventLogger
            public final void logEvent(int i, String str) {
                UserManagerService.this.lambda$getUserListFile$2(i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getUserListFile$2(int i, String str) {
        Slog.e(LOG_TAG, str);
        scheduleWriteUserList();
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x00dc, code lost:
    
        if (r3.getName().equals(com.android.server.pm.UserManagerService.TAG_RESTRICTIONS) == false) goto L112;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00de, code lost:
    
        r4 = r11.mGuestRestrictions;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00e0, code lost:
    
        monitor-enter(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00e1, code lost:
    
        com.android.server.pm.UserRestrictionsUtils.readRestrictions(r3, r11.mGuestRestrictions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00e6, code lost:
    
        monitor-exit(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x006a, code lost:
    
        continue;
     */
    @GuardedBy({"mPackagesLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void readUserListLP() {
        FileInputStream fileInputStream;
        Exception e;
        int next;
        ResilientAtomicFile userListFile = getUserListFile();
        try {
            try {
                fileInputStream = userListFile.openRead();
            } catch (Exception e2) {
                fileInputStream = null;
                e = e2;
            }
            try {
                if (fileInputStream == null) {
                    Slog.e(LOG_TAG, "userlist.xml not found, fallback to single user");
                    fallbackToSingleUserLP();
                    userListFile.close();
                    return;
                }
                TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                do {
                    next = resolvePullParser.next();
                    if (next == 2) {
                        break;
                    }
                } while (next != 1);
                if (next != 2) {
                    Slog.e(LOG_TAG, "Unable to read user list");
                    fallbackToSingleUserLP();
                    userListFile.close();
                    return;
                }
                this.mNextSerialNumber = -1;
                if (resolvePullParser.getName().equals("users")) {
                    this.mNextSerialNumber = resolvePullParser.getAttributeInt((String) null, ATTR_NEXT_SERIAL_NO, this.mNextSerialNumber);
                    this.mUserVersion = resolvePullParser.getAttributeInt((String) null, ATTR_USER_VERSION, this.mUserVersion);
                    this.mUserTypeVersion = resolvePullParser.getAttributeInt((String) null, ATTR_USER_TYPE_VERSION, this.mUserTypeVersion);
                }
                while (true) {
                    int next2 = resolvePullParser.next();
                    if (next2 == 1) {
                        updateUserIds();
                        upgradeIfNecessaryLP();
                        userListFile.close();
                        synchronized (this.mUsersLock) {
                            if (this.mUsers.size() == 0) {
                                Slog.e(LOG_TAG, "mUsers is empty, fallback to single user");
                                fallbackToSingleUserLP();
                            }
                        }
                        return;
                    }
                    if (next2 == 2) {
                        String name = resolvePullParser.getName();
                        if (name.equals(TAG_USER)) {
                            UserData readUserLP = readUserLP(resolvePullParser.getAttributeInt((String) null, ATTR_ID), this.mUserVersion);
                            if (readUserLP == null) {
                                Slog.i(LOG_TAG, "there is something wrong parsing the userid.xml user file,fallback to default user");
                                fallbackToSingleUserLP();
                                userListFile.close();
                                return;
                            } else {
                                synchronized (this.mUsersLock) {
                                    this.mUsers.put(readUserLP.info.id, readUserLP);
                                    int i = this.mNextSerialNumber;
                                    if (i < 0 || i <= readUserLP.info.id) {
                                        this.mNextSerialNumber = readUserLP.info.id + 1;
                                    }
                                }
                            }
                        } else if (name.equals(TAG_GUEST_RESTRICTIONS)) {
                            while (true) {
                                int next3 = resolvePullParser.next();
                                if (next3 != 1 && next3 != 3) {
                                    if (next3 == 2) {
                                        break;
                                    }
                                }
                            }
                        } else {
                            continue;
                        }
                    }
                }
            } catch (Exception e3) {
                e = e3;
                userListFile.failRead(fileInputStream, e);
                readUserListLP();
                userListFile.close();
            }
        } catch (Throwable th) {
            if (userListFile != null) {
                try {
                    userListFile.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @GuardedBy({"mPackagesLock"})
    private void upgradeIfNecessaryLP() {
        upgradeIfNecessaryLP(this.mUserVersion, this.mUserTypeVersion);
    }

    @GuardedBy({"mPackagesLock"})
    @VisibleForTesting
    void upgradeIfNecessaryLP(int i, int i2) {
        UserInfo earliestCreatedFullUser;
        Slog.i(LOG_TAG, "Upgrading users from userVersion " + i + " to 11");
        ArraySet arraySet = new ArraySet();
        int i3 = this.mUserVersion;
        int i4 = this.mUserTypeVersion;
        if (i < 1) {
            UserData userDataNoChecks = getUserDataNoChecks(0);
            if ("Primary".equals(userDataNoChecks.info.name)) {
                userDataNoChecks.info.name = this.mContext.getResources().getString(R.string.permlab_useBiometric);
                arraySet.add(Integer.valueOf(userDataNoChecks.info.id));
            }
            i = 1;
        }
        if (i < 2) {
            UserInfo userInfo = getUserDataNoChecks(0).info;
            int i5 = userInfo.flags;
            if ((i5 & 16) == 0) {
                userInfo.flags = i5 | 16;
                arraySet.add(Integer.valueOf(userInfo.id));
            }
            i = 2;
        }
        if (i < 4) {
            i = 4;
        }
        if (i < 5) {
            initDefaultGuestRestrictions();
            i = 5;
        }
        if (i < 6) {
            synchronized (this.mUsersLock) {
                for (int i6 = 0; i6 < this.mUsers.size(); i6++) {
                    UserData valueAt = this.mUsers.valueAt(i6);
                    if (valueAt.info.isRestricted()) {
                        UserInfo userInfo2 = valueAt.info;
                        if (userInfo2.restrictedProfileParentId == -10000) {
                            userInfo2.restrictedProfileParentId = 0;
                            arraySet.add(Integer.valueOf(userInfo2.id));
                        }
                    }
                }
            }
            i = 6;
        }
        if (i < 7) {
            synchronized (this.mRestrictionsLock) {
                if (this.mDevicePolicyUserRestrictions.removeRestrictionsForAllUsers("ensure_verify_apps")) {
                    this.mDevicePolicyUserRestrictions.getRestrictionsNonNull(-1).putBoolean("ensure_verify_apps", true);
                }
            }
            List<UserInfo> guestUsers = getGuestUsers();
            for (int i7 = 0; i7 < guestUsers.size(); i7++) {
                UserInfo userInfo3 = guestUsers.get(i7);
                if (userInfo3 != null && !hasUserRestriction("no_config_wifi", userInfo3.id)) {
                    setUserRestriction("no_config_wifi", true, userInfo3.id);
                }
            }
            i = 7;
        }
        this.mWrapper.getExtImpl().hookUsersUpgraded(this.mUsers);
        Set<Integer> hookUsersIdToWrite = this.mWrapper.getExtImpl().hookUsersIdToWrite(arraySet);
        if (i < 8) {
            synchronized (this.mUsersLock) {
                UserData userData = this.mUsers.get(0);
                userData.info.flags |= 2048;
                if (!isDefaultHeadlessSystemUserMode()) {
                    userData.info.flags |= 1024;
                }
                hookUsersIdToWrite.add(Integer.valueOf(userData.info.id));
                for (int i8 = 1; i8 < this.mUsers.size(); i8++) {
                    UserInfo userInfo4 = this.mUsers.valueAt(i8).info;
                    int i9 = userInfo4.flags;
                    if ((i9 & 32) == 0) {
                        userInfo4.flags = i9 | 1024;
                        hookUsersIdToWrite.add(Integer.valueOf(userInfo4.id));
                    }
                }
            }
            i = 8;
        }
        if (i < 9) {
            synchronized (this.mUsersLock) {
                for (int i10 = 0; i10 < this.mUsers.size(); i10++) {
                    UserData valueAt2 = this.mUsers.valueAt(i10);
                    UserInfo userInfo5 = valueAt2.info;
                    int i11 = userInfo5.flags;
                    if ((i11 & 2048) == 0) {
                        try {
                            userInfo5.userType = UserInfo.getDefaultUserType(i11);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalStateException("Cannot upgrade user with flags " + Integer.toHexString(i11) + " because it doesn't correspond to a valid user type.", e);
                        }
                    } else if ((i11 & 1024) != 0) {
                        userInfo5.userType = "android.os.usertype.full.SYSTEM";
                    } else {
                        userInfo5.userType = "android.os.usertype.system.HEADLESS";
                    }
                    UserTypeDetails userTypeDetails = this.mUserTypes.get(valueAt2.info.userType);
                    if (userTypeDetails == null) {
                        throw new IllegalStateException("Cannot upgrade user with flags " + Integer.toHexString(i11) + " because " + valueAt2.info.userType + " isn't defined on this device!");
                    }
                    UserInfo userInfo6 = valueAt2.info;
                    userInfo6.flags = userTypeDetails.getDefaultUserInfoFlags() | userInfo6.flags;
                    hookUsersIdToWrite.add(Integer.valueOf(valueAt2.info.id));
                }
            }
            i = 9;
        }
        if (i < 10) {
            synchronized (this.mUsersLock) {
                for (int i12 = 0; i12 < this.mUsers.size(); i12++) {
                    UserData valueAt3 = this.mUsers.valueAt(i12);
                    UserTypeDetails userTypeDetails2 = this.mUserTypes.get(valueAt3.info.userType);
                    if (userTypeDetails2 == null) {
                        throw new IllegalStateException("Cannot upgrade user because " + valueAt3.info.userType + " isn't defined on this device!");
                    }
                    valueAt3.userProperties = new UserProperties(userTypeDetails2.getDefaultUserPropertiesReference());
                    hookUsersIdToWrite.add(Integer.valueOf(valueAt3.info.id));
                }
            }
            i = 10;
        }
        if (i < 11) {
            if (isHeadlessSystemUserMode()) {
                if (isMainUserPermanentAdmin() && (earliestCreatedFullUser = getEarliestCreatedFullUser()) != null) {
                    earliestCreatedFullUser.flags |= 16384;
                    hookUsersIdToWrite.add(Integer.valueOf(earliestCreatedFullUser.id));
                }
            } else {
                synchronized (this.mUsersLock) {
                    UserInfo userInfo7 = this.mUsers.get(0).info;
                    userInfo7.flags |= 16384;
                    hookUsersIdToWrite.add(Integer.valueOf(userInfo7.id));
                }
            }
            i = 11;
        }
        int userTypeVersion = UserTypeFactory.getUserTypeVersion();
        if (userTypeVersion > i2) {
            synchronized (this.mUsersLock) {
                upgradeUserTypesLU(UserTypeFactory.getUserTypeUpgrades(), this.mUserTypes, i2, hookUsersIdToWrite);
            }
        }
        if (i < 11) {
            Slog.w(LOG_TAG, "User version " + this.mUserVersion + " didn't upgrade as expected to 11");
            return;
        }
        if (i > 11) {
            Slog.wtf(LOG_TAG, "Upgraded user version " + this.mUserVersion + " is higher the SDK's one of 11. Someone forgot to update USER_VERSION?");
        }
        this.mUserVersion = i;
        this.mUserTypeVersion = userTypeVersion;
        if (i3 < i || i4 < userTypeVersion) {
            Iterator<Integer> it = hookUsersIdToWrite.iterator();
            while (it.hasNext()) {
                UserData userDataNoChecks2 = getUserDataNoChecks(it.next().intValue());
                if (userDataNoChecks2 != null) {
                    writeUserLP(userDataNoChecks2);
                }
            }
            writeUserListLP();
        }
    }

    @GuardedBy({"mUsersLock"})
    private void upgradeUserTypesLU(List<UserTypeFactory.UserTypeUpgrade> list, ArrayMap<String, UserTypeDetails> arrayMap, int i, Set<Integer> set) {
        for (UserTypeFactory.UserTypeUpgrade userTypeUpgrade : list) {
            if (i <= userTypeUpgrade.getUpToVersion()) {
                for (int i2 = 0; i2 < this.mUsers.size(); i2++) {
                    UserData valueAt = this.mUsers.valueAt(i2);
                    if (userTypeUpgrade.getFromType().equals(valueAt.info.userType)) {
                        UserTypeDetails userTypeDetails = arrayMap.get(userTypeUpgrade.getToType());
                        if (userTypeDetails == null) {
                            throw new IllegalStateException("Upgrade destination user type not defined: " + userTypeUpgrade.getToType());
                        }
                        upgradeProfileToTypeLU(valueAt.info, userTypeDetails);
                        set.add(Integer.valueOf(valueAt.info.id));
                    }
                }
            }
        }
    }

    @GuardedBy({"mUsersLock"})
    @VisibleForTesting
    void upgradeProfileToTypeLU(UserInfo userInfo, UserTypeDetails userTypeDetails) {
        Slog.i(LOG_TAG, "Upgrading user " + userInfo.id + " from " + userInfo.userType + " to " + userTypeDetails.getName());
        if (!userInfo.isProfile()) {
            throw new IllegalStateException("Can only upgrade profile types. " + userInfo.userType + " is not a profile type.");
        }
        if (!canAddMoreProfilesToUser(userTypeDetails.getName(), userInfo.profileGroupId, false)) {
            Slog.w(LOG_TAG, "Exceeded maximum profiles of type " + userTypeDetails.getName() + " for user " + userInfo.id + ". Maximum allowed= " + userTypeDetails.getMaxAllowedPerParent());
        }
        UserTypeDetails userTypeDetails2 = this.mUserTypes.get(userInfo.userType);
        int defaultUserInfoFlags = userTypeDetails2 != null ? userTypeDetails2.getDefaultUserInfoFlags() : 4096;
        userInfo.userType = userTypeDetails.getName();
        userInfo.flags = (defaultUserInfoFlags ^ userInfo.flags) | userTypeDetails.getDefaultUserInfoFlags();
        synchronized (this.mRestrictionsLock) {
            if (!BundleUtils.isEmpty(userTypeDetails.getDefaultRestrictions())) {
                Bundle clone = BundleUtils.clone(this.mBaseUserRestrictions.getRestrictions(userInfo.id));
                UserRestrictionsUtils.merge(clone, userTypeDetails.getDefaultRestrictions());
                updateUserRestrictionsInternalLR(clone, userInfo.id);
            }
        }
        userInfo.profileBadge = getFreeProfileBadgeLU(userInfo.profileGroupId, userInfo.userType);
    }

    private UserInfo getEarliestCreatedFullUser() {
        List<UserInfo> usersInternal = getUsersInternal(true, true, true);
        UserInfo userInfo = null;
        long j = JobStatus.NO_LATEST_RUNTIME;
        for (int i = 0; i < usersInternal.size(); i++) {
            UserInfo userInfo2 = usersInternal.get(i);
            if (userInfo2.isFull() && userInfo2.isAdmin()) {
                long j2 = userInfo2.creationTime;
                if (j2 >= 0 && j2 < j) {
                    userInfo = userInfo2;
                    j = j2;
                }
            }
        }
        return userInfo;
    }

    @GuardedBy({"mPackagesLock"})
    private void fallbackToSingleUserLP() {
        String str = isDefaultHeadlessSystemUserMode() ? "android.os.usertype.system.HEADLESS" : "android.os.usertype.full.SYSTEM";
        UserData putUserInfo = putUserInfo(new UserInfo(0, (String) null, (String) null, this.mUserTypes.get(str).getDefaultUserInfoFlags() | 16, str));
        putUserInfo.userProperties = new UserProperties(this.mUserTypes.get(putUserInfo.info.userType).getDefaultUserPropertiesReference());
        this.mNextSerialNumber = 10;
        this.mUserVersion = 11;
        this.mUserTypeVersion = UserTypeFactory.getUserTypeVersion();
        Bundle bundle = new Bundle();
        try {
            for (String str2 : this.mContext.getResources().getStringArray(R.array.config_displayWhiteBalanceDecreaseThresholds)) {
                if (UserRestrictionsUtils.isValidRestriction(str2)) {
                    bundle.putBoolean(str2, true);
                }
            }
        } catch (Resources.NotFoundException e) {
            Slog.e(LOG_TAG, "Couldn't find resource: config_defaultFirstUserRestrictions", e);
        }
        if (!bundle.isEmpty()) {
            synchronized (this.mRestrictionsLock) {
                this.mBaseUserRestrictions.updateRestrictions(0, bundle);
            }
        }
        initDefaultGuestRestrictions();
        writeUserLP(putUserInfo);
        writeUserListLP();
    }

    private String getOwnerName() {
        return this.mOwnerName.get();
    }

    private String getGuestName() {
        return this.mContext.getString(R.string.lockscreen_sim_puk_locked_instructions);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateOwnerNameIfNecessary(Resources resources, boolean z) {
        int updateFrom = this.mLastConfiguration.updateFrom(resources.getConfiguration());
        if (z || (this.mOwnerNameTypedValue.changingConfigurations & updateFrom) != 0) {
            resources.getValue(R.string.permlab_useBiometric, this.mOwnerNameTypedValue, true);
            CharSequence coerceToString = this.mOwnerNameTypedValue.coerceToString();
            this.mOwnerName.set(coerceToString != null ? coerceToString.toString() : null);
        }
    }

    private void scheduleWriteUserList() {
        if (this.mHandler.hasMessages(2)) {
            return;
        }
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2), 2000L);
    }

    private void scheduleWriteUser(int i) {
        if (this.mHandler.hasMessages(1, Integer.valueOf(i))) {
            return;
        }
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, Integer.valueOf(i)), 2000L);
    }

    private ResilientAtomicFile getUserFile(final int i) {
        return new ResilientAtomicFile(new File(this.mUsersDir, i + XML_SUFFIX), new File(this.mUsersDir, i + XML_SUFFIX + ".backup"), new File(this.mUsersDir, i + XML_SUFFIX + ".reservecopy"), 505, "user info", new ResilientAtomicFile.ReadEventLogger() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda5
            @Override // com.android.server.pm.ResilientAtomicFile.ReadEventLogger
            public final void logEvent(int i2, String str) {
                UserManagerService.this.lambda$getUserFile$3(i, i2, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getUserFile$3(int i, int i2, String str) {
        Slog.e(LOG_TAG, str);
        if (getUserDataNoChecks(i) != null) {
            scheduleWriteUser(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0038  */
    @GuardedBy({"mPackagesLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void writeUserLP(UserData userData) {
        FileOutputStream fileOutputStream;
        ResilientAtomicFile userFile = getUserFile(userData.info.id);
        try {
            try {
                fileOutputStream = userFile.startWrite();
                try {
                    writeUserLP(userData, fileOutputStream);
                    userFile.finishWrite(fileOutputStream);
                } catch (Exception e) {
                    e = e;
                    Slog.e(LOG_TAG, "Error writing user info " + userData.info.id, e);
                    userFile.failWrite(fileOutputStream);
                    if (userFile == null) {
                    }
                }
            } catch (Exception e2) {
                e = e2;
                fileOutputStream = null;
            }
            if (userFile == null) {
                userFile.close();
            }
        } catch (Throwable th) {
            if (userFile != null) {
                try {
                    userFile.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @GuardedBy({"mPackagesLock"})
    @VisibleForTesting
    void writeUserLP(UserData userData, OutputStream outputStream) throws IOException, XmlPullParserException {
        TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(outputStream);
        resolveSerializer.startDocument((String) null, Boolean.TRUE);
        resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        UserInfo userInfo = userData.info;
        resolveSerializer.startTag((String) null, TAG_USER);
        resolveSerializer.attributeInt((String) null, ATTR_ID, userInfo.id);
        resolveSerializer.attributeInt((String) null, ATTR_SERIAL_NO, userInfo.serialNumber);
        resolveSerializer.attributeInt((String) null, ATTR_FLAGS, userInfo.flags);
        resolveSerializer.attribute((String) null, "type", userInfo.userType);
        resolveSerializer.attributeLong((String) null, ATTR_CREATION_TIME, userInfo.creationTime);
        resolveSerializer.attributeLong((String) null, ATTR_LAST_LOGGED_IN_TIME, userInfo.lastLoggedInTime);
        String str = userInfo.lastLoggedInFingerprint;
        if (str != null) {
            resolveSerializer.attribute((String) null, ATTR_LAST_LOGGED_IN_FINGERPRINT, str);
        }
        resolveSerializer.attributeLong((String) null, ATTR_LAST_ENTERED_FOREGROUND_TIME, userData.mLastEnteredForegroundTimeMillis);
        String str2 = userInfo.iconPath;
        if (str2 != null) {
            resolveSerializer.attribute((String) null, ATTR_ICON_PATH, str2);
        }
        if (userInfo.partial) {
            resolveSerializer.attributeBoolean((String) null, ATTR_PARTIAL, true);
        }
        if (userInfo.preCreated) {
            resolveSerializer.attributeBoolean((String) null, ATTR_PRE_CREATED, true);
        }
        if (userInfo.convertedFromPreCreated) {
            resolveSerializer.attributeBoolean((String) null, ATTR_CONVERTED_FROM_PRE_CREATED, true);
        }
        if (userInfo.guestToRemove) {
            resolveSerializer.attributeBoolean((String) null, ATTR_GUEST_TO_REMOVE, true);
        }
        int i = userInfo.profileGroupId;
        if (i != -10000) {
            resolveSerializer.attributeInt((String) null, ATTR_PROFILE_GROUP_ID, i);
        }
        resolveSerializer.attributeInt((String) null, ATTR_PROFILE_BADGE, userInfo.profileBadge);
        int i2 = userInfo.restrictedProfileParentId;
        if (i2 != -10000) {
            resolveSerializer.attributeInt((String) null, ATTR_RESTRICTED_PROFILE_PARENT_ID, i2);
        }
        if (userData.persistSeedData) {
            String str3 = userData.seedAccountName;
            if (str3 != null) {
                resolveSerializer.attribute((String) null, ATTR_SEED_ACCOUNT_NAME, truncateString(str3, SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS));
            }
            String str4 = userData.seedAccountType;
            if (str4 != null) {
                resolveSerializer.attribute((String) null, ATTR_SEED_ACCOUNT_TYPE, truncateString(str4, SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS));
            }
        }
        if (userInfo.name != null) {
            resolveSerializer.startTag((String) null, "name");
            resolveSerializer.text(truncateString(userInfo.name, 100));
            resolveSerializer.endTag((String) null, "name");
        }
        synchronized (this.mRestrictionsLock) {
            UserRestrictionsUtils.writeRestrictions(resolveSerializer, this.mBaseUserRestrictions.getRestrictions(userInfo.id), TAG_RESTRICTIONS);
            UserRestrictionsUtils.writeRestrictions(resolveSerializer, this.mDevicePolicyUserRestrictions.getRestrictions(-1), TAG_DEVICE_POLICY_GLOBAL_RESTRICTIONS);
            UserRestrictionsUtils.writeRestrictions(resolveSerializer, this.mDevicePolicyUserRestrictions.getRestrictions(userInfo.id), TAG_DEVICE_POLICY_LOCAL_RESTRICTIONS);
        }
        if (userData.account != null) {
            resolveSerializer.startTag((String) null, TAG_ACCOUNT);
            resolveSerializer.text(userData.account);
            resolveSerializer.endTag((String) null, TAG_ACCOUNT);
        }
        if (userData.persistSeedData && userData.seedAccountOptions != null) {
            resolveSerializer.startTag((String) null, TAG_SEED_ACCOUNT_OPTIONS);
            userData.seedAccountOptions.saveToXml(resolveSerializer);
            resolveSerializer.endTag((String) null, TAG_SEED_ACCOUNT_OPTIONS);
        }
        if (userData.userProperties != null) {
            resolveSerializer.startTag((String) null, TAG_USER_PROPERTIES);
            userData.userProperties.writeToXml(resolveSerializer);
            resolveSerializer.endTag((String) null, TAG_USER_PROPERTIES);
        }
        if (userData.getLastRequestQuietModeEnabledMillis() != 0) {
            resolveSerializer.startTag((String) null, TAG_LAST_REQUEST_QUIET_MODE_ENABLED_CALL);
            resolveSerializer.text(String.valueOf(userData.getLastRequestQuietModeEnabledMillis()));
            resolveSerializer.endTag((String) null, TAG_LAST_REQUEST_QUIET_MODE_ENABLED_CALL);
        }
        resolveSerializer.startTag((String) null, TAG_IGNORE_PREPARE_STORAGE_ERRORS);
        resolveSerializer.text(String.valueOf(userData.getIgnorePrepareStorageErrors()));
        resolveSerializer.endTag((String) null, TAG_IGNORE_PREPARE_STORAGE_ERRORS);
        resolveSerializer.endTag((String) null, TAG_USER);
        resolveSerializer.endDocument();
    }

    private String truncateString(String str, int i) {
        return (str == null || str.length() <= i) ? str : str.substring(0, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    @GuardedBy({"mPackagesLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void writeUserListLP() {
        int size;
        int[] iArr;
        int i;
        ResilientAtomicFile userListFile = getUserListFile();
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream startWrite = userListFile.startWrite();
                try {
                    TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                    resolveSerializer.startDocument((String) null, Boolean.TRUE);
                    resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                    resolveSerializer.startTag((String) null, "users");
                    resolveSerializer.attributeInt((String) null, ATTR_NEXT_SERIAL_NO, this.mNextSerialNumber);
                    resolveSerializer.attributeInt((String) null, ATTR_USER_VERSION, this.mUserVersion);
                    resolveSerializer.attributeInt((String) null, ATTR_USER_TYPE_VERSION, this.mUserTypeVersion);
                    resolveSerializer.startTag((String) null, TAG_GUEST_RESTRICTIONS);
                    synchronized (this.mGuestRestrictions) {
                        UserRestrictionsUtils.writeRestrictions(resolveSerializer, this.mGuestRestrictions, TAG_RESTRICTIONS);
                    }
                    resolveSerializer.endTag((String) null, TAG_GUEST_RESTRICTIONS);
                    synchronized (this.mUsersLock) {
                        size = this.mUsers.size();
                        iArr = new int[size];
                        for (int i2 = 0; i2 < size; i2++) {
                            iArr[i2] = this.mUsers.valueAt(i2).info.id;
                        }
                    }
                    for (i = 0; i < size; i++) {
                        int i3 = iArr[i];
                        resolveSerializer.startTag((String) null, TAG_USER);
                        resolveSerializer.attributeInt((String) null, ATTR_ID, i3);
                        resolveSerializer.endTag((String) null, TAG_USER);
                    }
                    resolveSerializer.endTag((String) null, "users");
                    resolveSerializer.endDocument();
                    userListFile.finishWrite(startWrite);
                } catch (Exception e) {
                    e = e;
                    fileOutputStream = startWrite;
                    Slog.e(LOG_TAG, "Error writing user list", e);
                    userListFile.failWrite(fileOutputStream);
                    if (userListFile == null) {
                    }
                }
            } catch (Throwable th) {
                if (userListFile != null) {
                    try {
                        userListFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
        }
        if (userListFile == null) {
            userListFile.close();
        }
    }

    @GuardedBy({"mPackagesLock"})
    private UserData readUserLP(int i, int i2) {
        FileInputStream fileInputStream;
        Exception e;
        ResilientAtomicFile userFile = getUserFile(i);
        try {
            try {
                fileInputStream = userFile.openRead();
            } catch (Throwable th) {
                if (userFile != null) {
                    try {
                        userFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Exception e2) {
            fileInputStream = null;
            e = e2;
        }
        try {
            if (fileInputStream == null) {
                Slog.e(LOG_TAG, "User info not found, returning null, user id: " + i);
                userFile.close();
                return null;
            }
            UserData readUserLP = readUserLP(i, fileInputStream, i2);
            userFile.close();
            return readUserLP;
        } catch (Exception e3) {
            e = e3;
            Slog.e(LOG_TAG, "Error reading user info, user id: " + i);
            userFile.failRead(fileInputStream, e);
            UserData readUserLP2 = readUserLP(i, i2);
            userFile.close();
            return readUserLP2;
        }
    }

    @GuardedBy({"mPackagesLock"})
    @VisibleForTesting
    UserData readUserLP(int i, InputStream inputStream, int i2) throws IOException, XmlPullParserException {
        int next;
        int i3;
        int i4;
        int i5;
        Bundle bundle;
        boolean z;
        Bundle bundle2;
        Bundle bundle3;
        String str;
        String str2;
        int i6;
        Bundle bundle4;
        boolean z2;
        String str3;
        long j;
        long j2;
        boolean z3;
        UserProperties userProperties;
        PersistableBundle persistableBundle;
        boolean z4;
        String str4;
        String str5;
        boolean z5;
        int i7;
        String str6;
        String str7;
        boolean z6;
        long j3;
        long j4;
        int i8;
        int i9;
        Bundle readRestrictions;
        TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(inputStream);
        do {
            next = resolvePullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next != 2) {
            Slog.e(LOG_TAG, "Unable to read user " + i);
            return null;
        }
        if (next != 2 || !resolvePullParser.getName().equals(TAG_USER)) {
            i3 = i;
            i4 = -10000;
            i5 = -10000;
            bundle = null;
            z = false;
            bundle2 = null;
            bundle3 = null;
            str = null;
            str2 = null;
            i6 = 0;
            bundle4 = null;
            z2 = false;
            str3 = null;
            j = 0;
            j2 = 0;
            z3 = false;
            userProperties = null;
            persistableBundle = null;
            z4 = true;
            str4 = null;
            str5 = null;
            z5 = false;
            i7 = 0;
            str6 = null;
            str7 = null;
            z6 = false;
            j3 = 0;
            j4 = 0;
        } else {
            if (resolvePullParser.getAttributeInt((String) null, ATTR_ID, -1) != i) {
                Slog.e(LOG_TAG, "User id does not match the file name");
                return null;
            }
            int attributeInt = resolvePullParser.getAttributeInt((String) null, ATTR_SERIAL_NO, i);
            int attributeInt2 = resolvePullParser.getAttributeInt((String) null, ATTR_FLAGS, 0);
            String attributeValue = resolvePullParser.getAttributeValue((String) null, "type");
            String intern = attributeValue != null ? attributeValue.intern() : null;
            String attributeValue2 = resolvePullParser.getAttributeValue((String) null, ATTR_ICON_PATH);
            long attributeLong = resolvePullParser.getAttributeLong((String) null, ATTR_CREATION_TIME, 0L);
            long attributeLong2 = resolvePullParser.getAttributeLong((String) null, ATTR_LAST_LOGGED_IN_TIME, 0L);
            String attributeValue3 = resolvePullParser.getAttributeValue((String) null, ATTR_LAST_LOGGED_IN_FINGERPRINT);
            long attributeLong3 = resolvePullParser.getAttributeLong((String) null, ATTR_LAST_ENTERED_FOREGROUND_TIME, 0L);
            int attributeInt3 = resolvePullParser.getAttributeInt((String) null, ATTR_PROFILE_GROUP_ID, -10000);
            int attributeInt4 = resolvePullParser.getAttributeInt((String) null, ATTR_PROFILE_BADGE, 0);
            int attributeInt5 = resolvePullParser.getAttributeInt((String) null, ATTR_RESTRICTED_PROFILE_PARENT_ID, -10000);
            boolean attributeBoolean = resolvePullParser.getAttributeBoolean((String) null, ATTR_PARTIAL, false);
            boolean attributeBoolean2 = resolvePullParser.getAttributeBoolean((String) null, ATTR_PRE_CREATED, false);
            boolean attributeBoolean3 = resolvePullParser.getAttributeBoolean((String) null, ATTR_CONVERTED_FROM_PRE_CREATED, false);
            boolean attributeBoolean4 = resolvePullParser.getAttributeBoolean((String) null, ATTR_GUEST_TO_REMOVE, false);
            String attributeValue4 = resolvePullParser.getAttributeValue((String) null, ATTR_SEED_ACCOUNT_NAME);
            String attributeValue5 = resolvePullParser.getAttributeValue((String) null, ATTR_SEED_ACCOUNT_TYPE);
            boolean z7 = (attributeValue4 == null && attributeValue5 == null) ? false : true;
            int depth = resolvePullParser.getDepth();
            long j5 = 0;
            String str8 = null;
            String str9 = null;
            PersistableBundle persistableBundle2 = null;
            UserProperties userProperties2 = null;
            Bundle bundle5 = null;
            Bundle bundle6 = null;
            Bundle bundle7 = null;
            Bundle bundle8 = null;
            boolean z8 = true;
            while (true) {
                int next2 = resolvePullParser.next();
                i9 = attributeInt2;
                if (next2 == 1) {
                    break;
                }
                int i10 = 3;
                if (next2 == 3) {
                    if (resolvePullParser.getDepth() <= depth) {
                        break;
                    }
                    i10 = 3;
                }
                if (next2 != i10 && next2 != 4) {
                    String name = resolvePullParser.getName();
                    if ("name".equals(name)) {
                        if (resolvePullParser.next() == 4) {
                            str8 = resolvePullParser.getText();
                        }
                    } else if (TAG_RESTRICTIONS.equals(name)) {
                        bundle5 = UserRestrictionsUtils.readRestrictions(resolvePullParser);
                    } else if (TAG_DEVICE_POLICY_RESTRICTIONS.equals(name)) {
                        bundle6 = UserRestrictionsUtils.readRestrictions(resolvePullParser);
                    } else if (TAG_DEVICE_POLICY_LOCAL_RESTRICTIONS.equals(name)) {
                        if (i2 < 10) {
                            readRestrictions = RestrictionsSet.readRestrictions(resolvePullParser, TAG_DEVICE_POLICY_LOCAL_RESTRICTIONS).mergeAll();
                        } else {
                            readRestrictions = UserRestrictionsUtils.readRestrictions(resolvePullParser);
                        }
                        bundle7 = readRestrictions;
                    } else if (TAG_DEVICE_POLICY_GLOBAL_RESTRICTIONS.equals(name)) {
                        bundle8 = UserRestrictionsUtils.readRestrictions(resolvePullParser);
                    } else if (TAG_ACCOUNT.equals(name)) {
                        if (resolvePullParser.next() == 4) {
                            str9 = resolvePullParser.getText();
                        }
                    } else if (TAG_SEED_ACCOUNT_OPTIONS.equals(name)) {
                        persistableBundle2 = PersistableBundle.restoreFromXml(resolvePullParser);
                        z7 = true;
                    } else if (TAG_USER_PROPERTIES.equals(name)) {
                        UserTypeDetails userTypeDetails = this.mUserTypes.get(intern);
                        if (userTypeDetails == null) {
                            Slog.e(LOG_TAG, "User has properties but no user type!");
                            return null;
                        }
                        userProperties2 = new UserProperties(resolvePullParser, userTypeDetails.getDefaultUserPropertiesReference());
                    } else if (TAG_LAST_REQUEST_QUIET_MODE_ENABLED_CALL.equals(name)) {
                        if (resolvePullParser.next() == 4) {
                            j5 = Long.parseLong(resolvePullParser.getText());
                        }
                    } else if (TAG_IGNORE_PREPARE_STORAGE_ERRORS.equals(name) && resolvePullParser.next() == 4) {
                        z8 = Boolean.parseBoolean(resolvePullParser.getText());
                    }
                }
                attributeInt2 = i9;
            }
            z2 = attributeBoolean;
            i5 = attributeInt5;
            str7 = intern;
            j = attributeLong;
            j4 = attributeLong3;
            j3 = j5;
            z3 = attributeBoolean2;
            z = attributeBoolean3;
            z6 = z7;
            z5 = attributeBoolean4;
            str = str8;
            bundle = bundle5;
            bundle3 = bundle7;
            bundle2 = bundle8;
            z4 = z8;
            str5 = attributeValue5;
            i6 = i9;
            i4 = attributeInt3;
            str4 = attributeValue4;
            i7 = attributeInt4;
            str2 = attributeValue2;
            j2 = attributeLong2;
            persistableBundle = persistableBundle2;
            userProperties = userProperties2;
            bundle4 = bundle6;
            str3 = attributeValue3;
            i3 = attributeInt;
            str6 = str9;
        }
        Bundle bundle9 = bundle2;
        Bundle bundle10 = bundle3;
        Bundle bundle11 = bundle4;
        UserInfo userInfo = new UserInfo(i, str, str2, i6, str7);
        userInfo.serialNumber = i3;
        userInfo.creationTime = j;
        userInfo.lastLoggedInTime = j2;
        userInfo.lastLoggedInFingerprint = str3;
        userInfo.partial = z2;
        userInfo.preCreated = z3;
        userInfo.convertedFromPreCreated = z;
        userInfo.guestToRemove = z5;
        userInfo.profileGroupId = i4;
        userInfo.profileBadge = i7;
        userInfo.restrictedProfileParentId = i5;
        UserData userData = new UserData();
        userData.info = userInfo;
        userData.account = str6;
        userData.seedAccountName = str4;
        userData.seedAccountType = str5;
        userData.persistSeedData = z6;
        userData.seedAccountOptions = persistableBundle;
        userData.userProperties = userProperties;
        userData.setLastRequestQuietModeEnabledMillis(j3);
        userData.mLastEnteredForegroundTimeMillis = j4;
        if (z4) {
            userData.setIgnorePrepareStorageErrors();
        }
        synchronized (this.mRestrictionsLock) {
            if (bundle != null) {
                try {
                    i8 = i;
                    this.mBaseUserRestrictions.updateRestrictions(i8, bundle);
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                i8 = i;
            }
            if (bundle10 != null) {
                this.mDevicePolicyUserRestrictions.updateRestrictions(i8, bundle10);
                if (bundle11 != null) {
                    Slog.wtf(LOG_TAG, "Seeing both legacy and current local restrictions in xml");
                }
            } else if (bundle11 != null) {
                this.mDevicePolicyUserRestrictions.updateRestrictions(i8, bundle11);
            }
            if (bundle9 != null) {
                this.mDevicePolicyUserRestrictions.updateRestrictions(-1, bundle9);
            }
        }
        return userData;
    }

    @GuardedBy({"mAppRestrictionsLock"})
    private static boolean cleanAppRestrictionsForPackageLAr(String str, int i) {
        File file = new File(Environment.getUserSystemDirectory(i), packageToRestrictionsFileName(str));
        if (!file.exists()) {
            return false;
        }
        file.delete();
        return true;
    }

    public UserInfo createProfileForUserWithThrow(String str, String str2, int i, int i2, String[] strArr) throws ServiceSpecificException {
        checkCreateUsersPermission(i);
        try {
            return createUserInternal(str, str2, i, i2, strArr);
        } catch (UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    public UserInfo createProfileForUserEvenWhenDisallowedWithThrow(String str, String str2, int i, int i2, String[] strArr) throws ServiceSpecificException {
        checkCreateUsersPermission(i);
        try {
            return createUserInternalUnchecked(str, str2, i, i2, false, strArr, null);
        } catch (UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    public UserInfo createUserWithThrow(String str, String str2, int i) throws ServiceSpecificException {
        checkCreateUsersPermission(i);
        try {
            return createUserInternal(str, str2, i, -10000, null);
        } catch (UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    public UserInfo preCreateUserWithThrow(String str) throws ServiceSpecificException {
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        int defaultUserInfoFlags = userTypeDetails != null ? userTypeDetails.getDefaultUserInfoFlags() : 0;
        checkCreateUsersPermission(defaultUserInfoFlags);
        Preconditions.checkArgument(isUserTypeEligibleForPreCreation(userTypeDetails), "cannot pre-create user of type " + str);
        Slog.i(LOG_TAG, "Pre-creating user of type " + str);
        try {
            return createUserInternalUnchecked(null, str, defaultUserInfoFlags, -10000, true, null, null);
        } catch (UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    public UserHandle createUserWithAttributes(String str, String str2, int i, Bitmap bitmap, String str3, String str4, PersistableBundle persistableBundle) throws ServiceSpecificException {
        checkCreateUsersPermission(i);
        if (someUserHasAccountNoChecks(str3, str4)) {
            throw new ServiceSpecificException(7);
        }
        try {
            UserInfo createUserInternal = createUserInternal(str, str2, i, -10000, null);
            if (bitmap != null) {
                this.mLocalService.setUserIcon(createUserInternal.id, bitmap);
            }
            setSeedAccountDataNoChecks(createUserInternal.id, str3, str4, persistableBundle, true);
            return createUserInternal.getUserHandle();
        } catch (UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    private UserInfo createUserInternal(String str, String str2, int i, int i2, String[] strArr) throws UserManager.CheckedUserOperationException {
        String str3;
        if (UserManager.isUserTypeCloneProfile(str2)) {
            str3 = "no_add_clone_profile";
        } else {
            str3 = UserManager.isUserTypeManagedProfile(str2) ? "no_add_managed_profile" : "no_add_user";
        }
        enforceUserRestriction(str3, UserHandle.getCallingUserId(), "Cannot add user");
        return createUserInternalUnchecked(str, str2, i, i2, false, strArr, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserInfo createUserInternalUnchecked(String str, String str2, int i, int i2, boolean z, String[] strArr, Object obj) throws UserManager.CheckedUserOperationException {
        UserInfo userInfo;
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        this.mWrapper.getExtImpl().ormsCreateUserBoost(2000);
        timingsTraceAndSlog.traceBegin("createUser-" + i);
        this.mUserJourneyLogger.logUserJourneyBegin(-1, 4);
        try {
            userInfo = createUserInternalUncheckedNoTracing(str, str2, i, i2, z, strArr, timingsTraceAndSlog, obj);
        } catch (Throwable th) {
            th = th;
            userInfo = null;
        }
        try {
            this.mWrapper.getExtImpl().onCreateUserInternal(userInfo);
            if (userInfo != null) {
                this.mUserJourneyLogger.logUserCreateJourneyFinish(getCurrentUserId(), userInfo);
            } else {
                this.mUserJourneyLogger.logNullUserJourneyError(4, getCurrentUserId(), -1, str2, i);
            }
            timingsTraceAndSlog.traceEnd();
            return userInfo;
        } catch (Throwable th2) {
            th = th2;
            if (userInfo != null) {
                this.mUserJourneyLogger.logUserCreateJourneyFinish(getCurrentUserId(), userInfo);
            } else {
                this.mUserJourneyLogger.logNullUserJourneyError(4, getCurrentUserId(), -1, str2, i);
            }
            timingsTraceAndSlog.traceEnd();
            throw th;
        }
    }

    private UserInfo createUserInternalUncheckedNoTracing(String str, String str2, int i, int i2, boolean z, String[] strArr, TimingsTraceAndSlog timingsTraceAndSlog, Object obj) throws UserManager.CheckedUserOperationException {
        UserData userDataLU;
        int nextAvailableId;
        String str3;
        UserInfo convertPreCreatedUserIfPossible;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mWrapper.getExtImpl().ensureCanCreateStudyUserOrThrowIfNeeded(i);
        String truncateString = truncateString(str, 100);
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str2);
        if (userTypeDetails == null) {
            throwCheckedUserOperationException("Cannot create user of invalid user type: " + str2, 1);
        }
        String intern = str2.intern();
        int defaultUserInfoFlags = userTypeDetails.getDefaultUserInfoFlags() | i;
        if (!checkUserTypeConsistency(defaultUserInfoFlags)) {
            throwCheckedUserOperationException("Cannot add user. Flags (" + Integer.toHexString(defaultUserInfoFlags) + ") and userTypeDetails (" + intern + ") are inconsistent.", 1);
        }
        if ((defaultUserInfoFlags & 2048) != 0) {
            throwCheckedUserOperationException("Cannot add user. Flags (" + Integer.toHexString(defaultUserInfoFlags) + ") indicated SYSTEM user, which cannot be created.", 1);
        }
        if (!isUserTypeEnabled(userTypeDetails)) {
            throwCheckedUserOperationException("Cannot add a user of disabled type " + intern + ".", 6);
        }
        synchronized (this.mUsersLock) {
            if (this.mForceEphemeralUsers) {
                defaultUserInfoFlags |= 256;
            }
        }
        if (!z && i2 < 0 && isUserTypeEligibleForPreCreation(userTypeDetails) && (convertPreCreatedUserIfPossible = convertPreCreatedUserIfPossible(intern, defaultUserInfoFlags, truncateString, obj)) != null) {
            return convertPreCreatedUserIfPossible;
        }
        if (((DeviceStorageMonitorInternal) LocalServices.getService(DeviceStorageMonitorInternal.class)).isMemoryLow()) {
            throwCheckedUserOperationException("Cannot add user. Not enough space on disk.", 5);
        }
        boolean isProfile = userTypeDetails.isProfile();
        boolean isUserTypeGuest = UserManager.isUserTypeGuest(intern);
        boolean isUserTypeRestricted = UserManager.isUserTypeRestricted(intern);
        boolean isUserTypeDemo = UserManager.isUserTypeDemo(intern);
        boolean isUserTypeManagedProfile = UserManager.isUserTypeManagedProfile(intern);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mPackagesLock) {
                try {
                    if (i2 != -10000) {
                        try {
                            synchronized (this.mUsersLock) {
                                userDataLU = getUserDataLU(i2);
                            }
                            if (userDataLU == null) {
                                throwCheckedUserOperationException("Cannot find user data for parent user " + i2, 1);
                            }
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } else {
                        userDataLU = null;
                    }
                    UserData userData = userDataLU;
                    if (!z && !canAddMoreUsersOfType(userTypeDetails)) {
                        throwCheckedUserOperationException("Cannot add more users of type " + intern + ". Maximum number of that type already exists.", 6);
                    }
                    if (!isUserTypeGuest && !isUserTypeManagedProfile && !isUserTypeDemo && isUserLimitReached() && !this.mWrapper.getExtImpl().isCustomUser(defaultUserInfoFlags)) {
                        throwCheckedUserOperationException("Cannot add user. Maximum user limit is reached.", 6);
                    }
                    if (isProfile && !canAddMoreProfilesToUser(intern, i2, false) && !this.mWrapper.getExtImpl().isCustomUser(defaultUserInfoFlags)) {
                        throwCheckedUserOperationException("Cannot add more profiles of type " + intern + " for user " + i2, 6);
                    }
                    if (isUserTypeRestricted && i2 != 0 && !isCreationOverrideEnabled()) {
                        throwCheckedUserOperationException("Cannot add restricted profile - parent user must be system", 1);
                    }
                    if (this.mWrapper.getExtImpl().isCustomUser(defaultUserInfoFlags)) {
                        nextAvailableId = this.mWrapper.getExtImpl().getNextAvailableId(defaultUserInfoFlags);
                        if (nextAvailableId == -1) {
                            nextAvailableId = getNextAvailableId();
                        }
                    } else {
                        nextAvailableId = getNextAvailableId();
                    }
                    int i3 = nextAvailableId;
                    Slog.i(LOG_TAG, "Creating user " + i3 + " of type " + intern);
                    Environment.getUserSystemDirectory(i3).mkdirs();
                    synchronized (this.mUsersLock) {
                        try {
                            if (userData != null) {
                                try {
                                    if (userData.info.isEphemeral()) {
                                        defaultUserInfoFlags |= 256;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            }
                            if (z) {
                                defaultUserInfoFlags &= -257;
                            }
                            if ((defaultUserInfoFlags & 256) != 0) {
                                defaultUserInfoFlags |= 8192;
                            }
                            int i4 = defaultUserInfoFlags;
                            UserInfo userInfo = new UserInfo(i3, truncateString, (String) null, i4, intern);
                            int i5 = this.mNextSerialNumber;
                            this.mNextSerialNumber = i5 + 1;
                            userInfo.serialNumber = i5;
                            userInfo.creationTime = getCreationTime();
                            userInfo.partial = true;
                            userInfo.preCreated = z;
                            userInfo.lastLoggedInFingerprint = PackagePartitions.FINGERPRINT;
                            if (!userTypeDetails.hasBadge() || i2 == -10000) {
                                str3 = intern;
                            } else {
                                str3 = intern;
                                userInfo.profileBadge = getFreeProfileBadgeLU(i2, str3);
                            }
                            UserData userData2 = new UserData();
                            userData2.info = userInfo;
                            userData2.userProperties = new UserProperties(userTypeDetails.getDefaultUserPropertiesReference());
                            this.mUsers.put(i3, userData2);
                            writeUserLP(userData2);
                            writeUserListLP();
                            if (userData != null) {
                                if (isProfile) {
                                    UserInfo userInfo2 = userData.info;
                                    if (userInfo2.profileGroupId == -10000) {
                                        userInfo2.profileGroupId = userInfo2.id;
                                        writeUserLP(userData);
                                    }
                                    userInfo.profileGroupId = userData.info.profileGroupId;
                                } else if (isUserTypeRestricted) {
                                    UserInfo userInfo3 = userData.info;
                                    if (userInfo3.restrictedProfileParentId == -10000) {
                                        userInfo3.restrictedProfileParentId = userInfo3.id;
                                        writeUserLP(userData);
                                    }
                                    userInfo.restrictedProfileParentId = userData.info.restrictedProfileParentId;
                                }
                            }
                            long elapsedRealtime2 = SystemClock.elapsedRealtime();
                            String str4 = str3;
                            this.mWrapper.getExtImpl().createUserEnter(elapsedRealtime, str, str3, i4, z, i3);
                            timingsTraceAndSlog.traceBegin("createUserKey");
                            ((StorageManager) this.mContext.getSystemService(StorageManager.class)).createUserKey(i3, userInfo.serialNumber, userInfo.isEphemeral());
                            timingsTraceAndSlog.traceEnd();
                            long elapsedRealtime3 = SystemClock.elapsedRealtime();
                            long j = elapsedRealtime3 - elapsedRealtime2;
                            timingsTraceAndSlog.traceBegin("prepareUserData");
                            this.mUserDataPreparer.prepareUserData(i3, userInfo.serialNumber, 1);
                            timingsTraceAndSlog.traceEnd();
                            timingsTraceAndSlog.traceBegin("LSS.createNewUser");
                            this.mLockPatternUtils.createNewUser(i3, userInfo.serialNumber);
                            timingsTraceAndSlog.traceEnd();
                            long elapsedRealtime4 = SystemClock.elapsedRealtime() - elapsedRealtime3;
                            Set<String> installablePackagesForUserType = this.mSystemPackageInstaller.getInstallablePackagesForUserType(str4);
                            long elapsedRealtime5 = SystemClock.elapsedRealtime();
                            String[] hookDisallowedPackages = this.mWrapper.getExtImpl().hookDisallowedPackages(i3, i4, strArr);
                            this.mWrapper.getExtImpl().setUserIsMultiSystem(i3, i4);
                            timingsTraceAndSlog.traceBegin("PM.createNewUser");
                            this.mPm.createNewUser(i3, installablePackagesForUserType, hookDisallowedPackages);
                            timingsTraceAndSlog.traceEnd();
                            long elapsedRealtime6 = SystemClock.elapsedRealtime() - elapsedRealtime5;
                            userInfo.partial = false;
                            synchronized (this.mPackagesLock) {
                                writeUserLP(userData2);
                            }
                            updateUserIds();
                            Bundle bundle = new Bundle();
                            if (isUserTypeGuest) {
                                synchronized (this.mGuestRestrictions) {
                                    bundle.putAll(this.mGuestRestrictions);
                                }
                            } else {
                                userTypeDetails.addDefaultRestrictionsTo(bundle);
                            }
                            synchronized (this.mRestrictionsLock) {
                                this.mBaseUserRestrictions.updateRestrictions(i3, bundle);
                            }
                            long elapsedRealtime7 = SystemClock.elapsedRealtime();
                            timingsTraceAndSlog.traceBegin("PM.onNewUserCreated-" + i3);
                            this.mPm.onNewUserCreated(i3, false);
                            timingsTraceAndSlog.traceEnd();
                            applyDefaultUserSettings(userTypeDetails, i3);
                            setDefaultCrossProfileIntentFilters(i3, userTypeDetails, bundle, i2);
                            long elapsedRealtime8 = SystemClock.elapsedRealtime() - elapsedRealtime7;
                            if (z) {
                                Slog.i(LOG_TAG, "starting pre-created user " + userInfo.toFullString());
                                try {
                                    ActivityManager.getService().startUserInBackground(i3);
                                } catch (RemoteException e) {
                                    Slog.w(LOG_TAG, "could not start pre-created user " + i3, e);
                                }
                            } else {
                                dispatchUserAdded(userInfo, obj);
                            }
                            this.mWrapper.getExtImpl().createUserExit(elapsedRealtime, str, str4, i4, z, i3, SystemClock.elapsedRealtime() - elapsedRealtime, j, elapsedRealtime4, elapsedRealtime6, elapsedRealtime8);
                            return userInfo;
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void applyDefaultUserSettings(UserTypeDetails userTypeDetails, int i) {
        Bundle defaultSystemSettings = userTypeDetails.getDefaultSystemSettings();
        Bundle defaultSecureSettings = userTypeDetails.getDefaultSecureSettings();
        if (defaultSystemSettings.isEmpty() && defaultSecureSettings.isEmpty()) {
            return;
        }
        int size = defaultSystemSettings.size();
        String[] strArr = (String[]) defaultSystemSettings.keySet().toArray(new String[size]);
        for (int i2 = 0; i2 < size; i2++) {
            String str = strArr[i2];
            if (!Settings.System.putStringForUser(this.mContext.getContentResolver(), str, defaultSystemSettings.getString(str), i)) {
                Slog.e(LOG_TAG, "Failed to insert default system setting: " + str);
            }
        }
        int size2 = defaultSecureSettings.size();
        String[] strArr2 = (String[]) defaultSecureSettings.keySet().toArray(new String[size2]);
        for (int i3 = 0; i3 < size2; i3++) {
            String str2 = strArr2[i3];
            if (!Settings.Secure.putStringForUser(this.mContext.getContentResolver(), str2, defaultSecureSettings.getString(str2), i)) {
                Slog.e(LOG_TAG, "Failed to insert default secure setting: " + str2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDefaultCrossProfileIntentFilters(int i, UserTypeDetails userTypeDetails, Bundle bundle, int i2) {
        if (userTypeDetails == null || !userTypeDetails.isProfile() || userTypeDetails.getDefaultCrossProfileIntentFilters().isEmpty()) {
            return;
        }
        boolean z = bundle.getBoolean("no_sharing_into_profile", false);
        int size = userTypeDetails.getDefaultCrossProfileIntentFilters().size();
        for (int i3 = 0; i3 < size; i3++) {
            DefaultCrossProfileIntentFilter defaultCrossProfileIntentFilter = userTypeDetails.getDefaultCrossProfileIntentFilters().get(i3);
            if (!z || !defaultCrossProfileIntentFilter.letsPersonalDataIntoProfile) {
                if (defaultCrossProfileIntentFilter.direction == 0) {
                    PackageManagerService packageManagerService = this.mPm;
                    packageManagerService.addCrossProfileIntentFilter(packageManagerService.snapshotComputer(), defaultCrossProfileIntentFilter.filter, this.mContext.getOpPackageName(), i, i2, defaultCrossProfileIntentFilter.flags);
                } else {
                    PackageManagerService packageManagerService2 = this.mPm;
                    packageManagerService2.addCrossProfileIntentFilter(packageManagerService2.snapshotComputer(), defaultCrossProfileIntentFilter.filter, this.mContext.getOpPackageName(), i2, i, defaultCrossProfileIntentFilter.flags);
                }
            }
        }
    }

    private UserInfo convertPreCreatedUserIfPossible(String str, int i, String str2, final Object obj) {
        UserData preCreatedUserLU;
        synchronized (this.mUsersLock) {
            preCreatedUserLU = getPreCreatedUserLU(str);
        }
        if (preCreatedUserLU == null) {
            return null;
        }
        synchronized (this.mUserStates) {
            if (this.mUserStates.has(preCreatedUserLU.info.id)) {
                Slog.w(LOG_TAG, "Cannot reuse pre-created user " + preCreatedUserLU.info.id + " because it didn't stop yet");
                return null;
            }
            final UserInfo userInfo = preCreatedUserLU.info;
            int i2 = userInfo.flags | i;
            if (!checkUserTypeConsistency(i2)) {
                Slog.wtf(LOG_TAG, "Cannot reuse pre-created user " + userInfo.id + " of type " + str + " because flags are inconsistent. Flags (" + Integer.toHexString(i) + "); preCreatedUserFlags ( " + Integer.toHexString(userInfo.flags) + ").");
                return null;
            }
            Slog.i(LOG_TAG, "Reusing pre-created user " + userInfo.id + " of type " + str + " and bestowing on it flags " + UserInfo.flagsToString(i));
            userInfo.name = str2;
            userInfo.flags = i2;
            userInfo.preCreated = false;
            userInfo.convertedFromPreCreated = true;
            userInfo.creationTime = getCreationTime();
            synchronized (this.mPackagesLock) {
                writeUserLP(preCreatedUserLU);
                writeUserListLP();
            }
            updateUserIds();
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda1
                public final void runOrThrow() {
                    UserManagerService.this.lambda$convertPreCreatedUserIfPossible$4(userInfo, obj);
                }
            });
            return userInfo;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$convertPreCreatedUserIfPossible$4(UserInfo userInfo, Object obj) throws Exception {
        this.mPm.onNewUserCreated(userInfo.id, true);
        dispatchUserAdded(userInfo, obj);
        VoiceInteractionManagerInternal voiceInteractionManagerInternal = (VoiceInteractionManagerInternal) LocalServices.getService(VoiceInteractionManagerInternal.class);
        if (voiceInteractionManagerInternal != null) {
            voiceInteractionManagerInternal.onPreCreatedUserConversion(userInfo.id);
        }
    }

    @VisibleForTesting
    static boolean checkUserTypeConsistency(int i) {
        return isAtMostOneFlag(i & 4620) && isAtMostOneFlag(i & 5120) && isAtMostOneFlag(i & 6144);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean installWhitelistedSystemPackages(boolean z, boolean z2, ArraySet<String> arraySet) {
        return this.mSystemPackageInstaller.installWhitelistedSystemPackages(z || this.mUpdatingSystemUserMode, z2, arraySet);
    }

    public String[] getPreInstallableSystemPackages(String str) {
        checkCreateUsersPermission("getPreInstallableSystemPackages");
        Set<String> installablePackagesForUserType = this.mSystemPackageInstaller.getInstallablePackagesForUserType(str);
        if (installablePackagesForUserType == null) {
            return null;
        }
        return (String[]) installablePackagesForUserType.toArray(new String[installablePackagesForUserType.size()]);
    }

    private long getCreationTime() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > EPOCH_PLUS_30_YEARS) {
            return currentTimeMillis;
        }
        return 0L;
    }

    private void dispatchUserAdded(UserInfo userInfo, Object obj) {
        String str;
        synchronized (this.mUserLifecycleListeners) {
            for (int i = 0; i < this.mUserLifecycleListeners.size(); i++) {
                this.mUserLifecycleListeners.get(i).onUserCreated(userInfo, obj);
            }
        }
        Intent intent = new Intent("android.intent.action.USER_ADDED");
        intent.addFlags(DumpState.DUMP_SERVICE_PERMISSIONS);
        intent.addFlags(67108864);
        intent.putExtra("android.intent.extra.user_handle", userInfo.id);
        intent.putExtra("android.intent.extra.USER", UserHandle.of(userInfo.id));
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.MANAGE_USERS");
        Context context = this.mContext;
        if (userInfo.isGuest()) {
            str = TRON_GUEST_CREATED;
        } else {
            str = userInfo.isDemo() ? TRON_DEMO_CREATED : TRON_USER_CREATED;
        }
        MetricsLogger.count(context, str, 1);
        if (userInfo.isProfile()) {
            sendProfileAddedBroadcast(userInfo.profileGroupId, userInfo.id);
        } else if (Settings.Global.getString(this.mContext.getContentResolver(), "user_switcher_enabled") == null) {
            Settings.Global.putInt(this.mContext.getContentResolver(), "user_switcher_enabled", 1);
        }
    }

    @GuardedBy({"mUsersLock"})
    private UserData getPreCreatedUserLU(String str) {
        int size = this.mUsers.size();
        for (int i = 0; i < size; i++) {
            UserData valueAt = this.mUsers.valueAt(i);
            UserInfo userInfo = valueAt.info;
            if (userInfo.preCreated && !userInfo.partial && userInfo.userType.equals(str)) {
                if (valueAt.info.isInitialized()) {
                    return valueAt;
                }
                Slog.w(LOG_TAG, "found pre-created user of type " + str + ", but it's not initialized yet: " + valueAt.info.toFullString());
            }
        }
        return null;
    }

    private static boolean isUserTypeEligibleForPreCreation(UserTypeDetails userTypeDetails) {
        return (userTypeDetails == null || userTypeDetails.isProfile() || userTypeDetails.getName().equals("android.os.usertype.full.RESTRICTED")) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerStatsCallbacks() {
        StatsManager statsManager = (StatsManager) this.mContext.getSystemService(StatsManager.class);
        statsManager.setPullAtomCallback(10152, (StatsManager.PullAtomMetadata) null, BackgroundThread.getExecutor(), new StatsManager.StatsPullAtomCallback() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda6
            public final int onPullAtom(int i, List list) {
                int onPullAtom;
                onPullAtom = UserManagerService.this.onPullAtom(i, list);
                return onPullAtom;
            }
        });
        statsManager.setPullAtomCallback(10160, (StatsManager.PullAtomMetadata) null, BackgroundThread.getExecutor(), new StatsManager.StatsPullAtomCallback() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda6
            public final int onPullAtom(int i, List list) {
                int onPullAtom;
                onPullAtom = UserManagerService.this.onPullAtom(i, list);
                return onPullAtom;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onPullAtom(int i, List<StatsEvent> list) {
        boolean z;
        int i2 = -1;
        if (i != 10152) {
            if (i == 10160) {
                if (UserManager.getMaxSupportedUsers() <= 1) {
                    return 0;
                }
                list.add(FrameworkStatsLog.buildStatsEvent(10160, UserManager.getMaxSupportedUsers(), isUserSwitcherEnabled(-1), UserManager.supportsMultipleUsers() && !hasUserRestriction("no_add_user", -1)));
                return 0;
            }
            Slogf.e(LOG_TAG, "Unexpected atom tag: %d", Integer.valueOf(i));
            return 1;
        }
        List<UserInfo> usersInternal = getUsersInternal(true, true, true);
        int size = usersInternal.size();
        if (size <= 1) {
            return 0;
        }
        int i3 = 0;
        while (i3 < size) {
            UserInfo userInfo = usersInternal.get(i3);
            int userTypeForStatsd = UserJourneyLogger.getUserTypeForStatsd(userInfo.userType);
            String str = userTypeForStatsd == 0 ? userInfo.userType : null;
            synchronized (this.mUserStates) {
                z = this.mUserStates.get(userInfo.id, i2) == 3;
            }
            list.add(FrameworkStatsLog.buildStatsEvent(10152, userInfo.id, userTypeForStatsd, str, userInfo.flags, userInfo.creationTime, userInfo.lastLoggedInTime, z));
            i3++;
            i2 = -1;
        }
        return 0;
    }

    @VisibleForTesting
    UserData putUserInfo(UserInfo userInfo) {
        UserData userData = new UserData();
        userData.info = userInfo;
        synchronized (this.mUsersLock) {
            this.mUsers.put(userInfo.id, userData);
        }
        updateUserIds();
        return userData;
    }

    @VisibleForTesting
    void removeUserInfo(int i) {
        synchronized (this.mUsersLock) {
            this.mUsers.remove(i);
        }
    }

    public UserInfo createRestrictedProfileWithThrow(String str, int i) throws ServiceSpecificException {
        checkCreateUsersPermission("setupRestrictedProfile");
        UserInfo createProfileForUserWithThrow = createProfileForUserWithThrow(str, "android.os.usertype.full.RESTRICTED", 0, i, null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            setUserRestriction("no_modify_accounts", true, createProfileForUserWithThrow.id);
            Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "location_mode", 0, createProfileForUserWithThrow.id);
            setUserRestriction("no_share_location", true, createProfileForUserWithThrow.id);
            return createProfileForUserWithThrow;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public List<UserInfo> getGuestUsers() {
        checkManageUsersPermission("getGuestUsers");
        ArrayList arrayList = new ArrayList();
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                UserInfo userInfo = this.mUsers.valueAt(i).info;
                if (userInfo.isGuest() && !userInfo.guestToRemove && !userInfo.preCreated && !this.mRemovingUserIds.get(userInfo.id)) {
                    arrayList.add(userInfo);
                }
            }
        }
        return arrayList;
    }

    public boolean markGuestForDeletion(int i) {
        checkManageUsersPermission("Only the system can remove users");
        if (getUserRestrictions(UserHandle.getCallingUserId()).getBoolean("no_remove_user", false)) {
            Slog.w(LOG_TAG, "Cannot remove user. DISALLOW_REMOVE_USER is enabled.");
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mPackagesLock) {
                synchronized (this.mUsersLock) {
                    UserData userData = this.mUsers.get(i);
                    if (i != 0 && userData != null && !this.mRemovingUserIds.get(i)) {
                        if (!userData.info.isGuest()) {
                            return false;
                        }
                        UserInfo userInfo = userData.info;
                        userInfo.guestToRemove = true;
                        userInfo.flags |= 64;
                        writeUserLP(userData);
                        return true;
                    }
                    return false;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean removeUser(int i) {
        Slog.i(LOG_TAG, "removeUser u" + i);
        checkCreateUsersPermission("Only the system can remove users");
        String userRemovalRestriction = getUserRemovalRestriction(i);
        if (getUserRestrictions(UserHandle.getCallingUserId()).getBoolean(userRemovalRestriction, false)) {
            Slog.w(LOG_TAG, "Cannot remove user. " + userRemovalRestriction + " is enabled.");
            return false;
        }
        return removeUserWithProfilesUnchecked(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeUserWithProfilesUnchecked(int i) {
        UserInfo userInfoNoChecks = getUserInfoNoChecks(i);
        if (userInfoNoChecks == null) {
            Slog.e(LOG_TAG, TextUtils.formatSimple("Cannot remove user %d, invalid user id provided.", new Object[]{Integer.valueOf(i)}));
            return false;
        }
        if (!userInfoNoChecks.isProfile()) {
            for (int i2 : getProfileIds(i, false)) {
                if (i2 != i) {
                    Slog.i(LOG_TAG, "removing profile:" + i2 + "associated with user:" + i);
                    if (removeUserUnchecked(i2)) {
                        continue;
                    } else {
                        Slog.i(LOG_TAG, "Unable to immediately remove profile " + i2 + "associated with user " + i + ". User is set as ephemeral and will be removed on user switch or reboot.");
                        synchronized (this.mPackagesLock) {
                            UserData userDataNoChecks = getUserDataNoChecks(i);
                            userDataNoChecks.info.flags |= 256;
                            writeUserLP(userDataNoChecks);
                        }
                    }
                }
            }
        }
        return removeUserUnchecked(i);
    }

    public boolean removeUserEvenWhenDisallowed(int i) {
        checkCreateUsersPermission("Only the system can remove users");
        return removeUserWithProfilesUnchecked(i);
    }

    private String getUserRemovalRestriction(int i) {
        UserInfo userInfoLU;
        synchronized (this.mUsersLock) {
            userInfoLU = getUserInfoLU(i);
        }
        return userInfoLU != null && userInfoLU.isManagedProfile() ? "no_remove_managed_profile" : "no_remove_user";
    }

    private boolean removeUserUnchecked(int i) {
        if (this.mWrapper.getExtImpl().checkUserIfNeed(i)) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Pair<Integer, Integer> currentAndTargetUserIds = getCurrentAndTargetUserIds();
            if (i == ((Integer) currentAndTargetUserIds.first).intValue()) {
                Slog.w(LOG_TAG, "Current user cannot be removed.");
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            }
            if (i == ((Integer) currentAndTargetUserIds.second).intValue()) {
                Slog.w(LOG_TAG, "Target user of an ongoing user switch cannot be removed.");
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            }
            synchronized (this.mPackagesLock) {
                synchronized (this.mUsersLock) {
                    final UserData userData = this.mUsers.get(i);
                    if (i == 0) {
                        Slog.e(LOG_TAG, "System user cannot be removed.");
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return false;
                    }
                    if (userData == null) {
                        Slog.e(LOG_TAG, TextUtils.formatSimple("Cannot remove user %d, invalid user id provided.", new Object[]{Integer.valueOf(i)}));
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return false;
                    }
                    if (isNonRemovableMainUser(userData.info)) {
                        Slog.e(LOG_TAG, "Main user cannot be removed when it's a permanent admin user.");
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return false;
                    }
                    if (this.mRemovingUserIds.get(i)) {
                        Slog.e(LOG_TAG, TextUtils.formatSimple("User %d is already scheduled for removal.", new Object[]{Integer.valueOf(i)}));
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return false;
                    }
                    Slog.i(LOG_TAG, "Removing user " + i);
                    addRemovingUserIdLocked(i);
                    UserInfo userInfo = userData.info;
                    userInfo.partial = true;
                    userInfo.flags |= 64;
                    writeUserLP(userData);
                    this.mUserJourneyLogger.logUserJourneyBegin(i, 6);
                    this.mUserJourneyLogger.startSessionForDelayedJourney(i, 9, userData.info.creationTime);
                    try {
                        this.mAppOpsService.removeUser(i);
                    } catch (RemoteException e) {
                        Slog.w(LOG_TAG, "Unable to notify AppOpsService of removing user.", e);
                    }
                    this.mWrapper.getExtImpl().onRemoveUserUnchecked(i);
                    UserInfo userInfo2 = userData.info;
                    if (userInfo2.profileGroupId != -10000 && userInfo2.isProfile()) {
                        UserInfo userInfo3 = userData.info;
                        sendProfileRemovedBroadcast(userInfo3.profileGroupId, userInfo3.id, userInfo3.userType);
                    }
                    try {
                        boolean z = ActivityManager.getService().stopUser(i, true, new IStopUserCallback.Stub() { // from class: com.android.server.pm.UserManagerService.5
                            public void userStopped(int i2) {
                                UserManagerService.this.finishRemoveUser(i2);
                                int currentUserId = UserManagerService.this.getCurrentUserId();
                                UserManagerService.this.mUserJourneyLogger.logUserJourneyFinishWithError(currentUserId, userData.info, 6, -1);
                                UserManagerService.this.mUserJourneyLogger.logDelayedUserJourneyFinishWithError(currentUserId, userData.info, 9, -1);
                            }

                            public void userStopAborted(int i2) {
                                int currentUserId = UserManagerService.this.getCurrentUserId();
                                UserManagerService.this.mUserJourneyLogger.logUserJourneyFinishWithError(currentUserId, userData.info, 6, 3);
                                UserManagerService.this.mUserJourneyLogger.logDelayedUserJourneyFinishWithError(currentUserId, userData.info, 9, 3);
                            }
                        }) == 0;
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return z;
                    } catch (RemoteException e2) {
                        Slog.w(LOG_TAG, "Failed to stop user during removal.", e2);
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return false;
                    }
                }
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    @GuardedBy({"mUsersLock"})
    @VisibleForTesting
    void addRemovingUserIdLocked(int i) {
        this.mRemovingUserIds.put(i, true);
        this.mRecentlyRemovedIds.add(Integer.valueOf(i));
        if (this.mRecentlyRemovedIds.size() > 100) {
            this.mRecentlyRemovedIds.removeFirst();
        }
    }

    public int removeUserWhenPossible(int i, boolean z) {
        checkCreateUsersPermission("Only the system can remove users");
        if (!z) {
            String userRemovalRestriction = getUserRemovalRestriction(i);
            if (getUserRestrictions(UserHandle.getCallingUserId()).getBoolean(userRemovalRestriction, false)) {
                Slog.w(LOG_TAG, "Cannot remove user. " + userRemovalRestriction + " is enabled.");
                return -2;
            }
        }
        if (i == 0) {
            Slog.e(LOG_TAG, "System user cannot be removed.");
            return -4;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mPackagesLock) {
                synchronized (this.mUsersLock) {
                    UserData userData = this.mUsers.get(i);
                    if (userData == null) {
                        Slog.e(LOG_TAG, "Cannot remove user " + i + ", invalid user id provided.");
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return -3;
                    }
                    if (isNonRemovableMainUser(userData.info)) {
                        Slog.e(LOG_TAG, "Main user cannot be removed when it's a permanent admin user.");
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return -5;
                    }
                    if (this.mRemovingUserIds.get(i)) {
                        Slog.e(LOG_TAG, "User " + i + " is already scheduled for removal.");
                        return 2;
                    }
                    Pair<Integer, Integer> currentAndTargetUserIds = getCurrentAndTargetUserIds();
                    if (i != ((Integer) currentAndTargetUserIds.first).intValue() && i != ((Integer) currentAndTargetUserIds.second).intValue() && removeUserWithProfilesUnchecked(i)) {
                        return 0;
                    }
                    Object[] objArr = new Object[3];
                    objArr[0] = Integer.valueOf(i);
                    objArr[1] = i == ((Integer) currentAndTargetUserIds.first).intValue() ? "current user" : "target user of an ongoing user switch";
                    objArr[2] = Integer.valueOf(i);
                    Slog.i(LOG_TAG, TextUtils.formatSimple("Unable to immediately remove user %d (%s is %d). User is set as ephemeral and will be removed on user switch or reboot.", objArr));
                    userData.info.flags |= 256;
                    writeUserLP(userData);
                    return 1;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishRemoveUser(int i) {
        UserInfo userInfoLU;
        Slog.i(LOG_TAG, "finishRemoveUser " + i);
        synchronized (this.mUsersLock) {
            userInfoLU = getUserInfoLU(i);
        }
        if (userInfoLU != null && userInfoLU.preCreated) {
            Slog.i(LOG_TAG, "Removing a pre-created user with user id: " + i);
            ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).onUserStopped(i);
            removeUserState(i);
            return;
        }
        synchronized (this.mUserLifecycleListeners) {
            for (int i2 = 0; i2 < this.mUserLifecycleListeners.size(); i2++) {
                this.mUserLifecycleListeners.get(i2).onUserRemoved(userInfoLU);
            }
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Intent intent = new Intent("android.intent.action.USER_REMOVED");
            intent.addFlags(DumpState.DUMP_SERVICE_PERMISSIONS);
            intent.putExtra("android.intent.extra.user_handle", i);
            intent.putExtra("android.intent.extra.USER", UserHandle.of(i));
            getActivityManagerInternal().broadcastIntentWithCallback(intent, new AnonymousClass6(i), new String[]{"android.permission.MANAGE_USERS"}, -1, (int[]) null, (BiFunction) null, (Bundle) null);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.pm.UserManagerService$6, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass6 extends IIntentReceiver.Stub {
        final /* synthetic */ int val$userId;

        AnonymousClass6(int i) {
            this.val$userId = i;
        }

        public void performReceive(Intent intent, int i, String str, Bundle bundle, boolean z, boolean z2, int i2) {
            final int i3 = this.val$userId;
            new Thread(new Runnable() { // from class: com.android.server.pm.UserManagerService$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UserManagerService.AnonymousClass6.this.lambda$performReceive$0(i3);
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$performReceive$0(int i) {
            UserManagerService.this.getActivityManagerInternal().onUserRemoved(i);
            UserManagerService.this.removeUserState(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUserState(int i) {
        Slog.i(LOG_TAG, "Removing user state of user " + i);
        this.mLockPatternUtils.removeUser(i);
        try {
            ((StorageManager) this.mContext.getSystemService(StorageManager.class)).destroyUserKey(i);
        } catch (IllegalStateException e) {
            Slog.i(LOG_TAG, "Destroying key for user " + i + " failed, continuing anyway", e);
        }
        this.mPm.cleanUpUser(this, i);
        this.mUserDataPreparer.destroyUserData(i, 3);
        synchronized (this.mUsersLock) {
            this.mUsers.remove(i);
            this.mIsUserManaged.delete(i);
        }
        synchronized (this.mUserStates) {
            this.mUserStates.delete(i);
        }
        synchronized (this.mRestrictionsLock) {
            this.mBaseUserRestrictions.remove(i);
            this.mAppliedUserRestrictions.remove(i);
            this.mCachedEffectiveUserRestrictions.remove(i);
            if (this.mDevicePolicyUserRestrictions.remove(i)) {
                applyUserRestrictionsForAllUsersLR();
            }
        }
        synchronized (this.mPackagesLock) {
            writeUserListLP();
        }
        getUserFile(i).delete();
        updateUserIds();
        this.mWrapper.getExtImpl().onRemoveUserState(i);
        this.mWrapper.getExtImpl().onMultiAppUserRemoved(this.mContext, this.mRemovingUserIds, i);
    }

    private void sendProfileAddedBroadcast(int i, int i2) {
        sendProfileBroadcast(new Intent("android.intent.action.PROFILE_ADDED"), i, i2);
    }

    private void sendProfileRemovedBroadcast(int i, int i2, String str) {
        if (Objects.equals(str, "android.os.usertype.profile.MANAGED")) {
            sendManagedProfileRemovedBroadcast(i, i2);
        }
        sendProfileBroadcast(new Intent("android.intent.action.PROFILE_REMOVED"), i, i2);
    }

    private void sendProfileBroadcast(Intent intent, int i, int i2) {
        UserHandle of = UserHandle.of(i);
        intent.putExtra("android.intent.extra.USER", UserHandle.of(i2));
        intent.addFlags(AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE);
        this.mContext.sendBroadcastAsUser(intent, of, null);
    }

    private void sendManagedProfileRemovedBroadcast(int i, int i2) {
        Intent intent = new Intent("android.intent.action.MANAGED_PROFILE_REMOVED");
        intent.putExtra("android.intent.extra.USER", UserHandle.of(i2));
        intent.putExtra("android.intent.extra.user_handle", i2);
        UserHandle of = UserHandle.of(i);
        getDevicePolicyManagerInternal().broadcastIntentToManifestReceivers(intent, of, false);
        intent.addFlags(AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE);
        this.mContext.sendBroadcastAsUser(intent, of, null);
    }

    public Bundle getApplicationRestrictions(String str) {
        return getApplicationRestrictionsForUser(str, UserHandle.getCallingUserId());
    }

    public Bundle getApplicationRestrictionsForUser(String str, int i) {
        Bundle readApplicationRestrictionsLAr;
        if (UserHandle.getCallingUserId() != i || !UserHandle.isSameApp(Binder.getCallingUid(), getUidForPackage(str))) {
            checkSystemOrRoot("get application restrictions for other user/app " + str);
        }
        synchronized (this.mAppRestrictionsLock) {
            readApplicationRestrictionsLAr = readApplicationRestrictionsLAr(str, i);
        }
        return readApplicationRestrictionsLAr;
    }

    public void setApplicationRestrictions(String str, Bundle bundle, int i) {
        checkSystemOrRoot("set application restrictions");
        String validateName = validateName(str);
        if (validateName != null) {
            if (str.contains("../")) {
                EventLog.writeEvent(1397638484, "239701237", -1, "");
            }
            throw new IllegalArgumentException("Invalid package name: " + validateName);
        }
        boolean z = true;
        if (bundle != null) {
            bundle.setDefusable(true);
        }
        synchronized (this.mAppRestrictionsLock) {
            if (bundle != null) {
                if (!bundle.isEmpty()) {
                    writeApplicationRestrictionsLAr(str, bundle, i);
                }
            }
            z = cleanAppRestrictionsForPackageLAr(str, i);
        }
        if (z) {
            Intent intent = new Intent("android.intent.action.APPLICATION_RESTRICTIONS_CHANGED");
            intent.setPackage(str);
            intent.addFlags(1073741824);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.of(i));
        }
    }

    @VisibleForTesting
    static String validateName(String str) {
        int length = str.length();
        boolean z = true;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if ((charAt < 'a' || charAt > 'z') && (charAt < 'A' || charAt > 'Z')) {
                if (!z) {
                    if ((charAt < '0' || charAt > '9') && charAt != '_') {
                        if (charAt == '.') {
                            z = true;
                        }
                    }
                }
                return "bad character '" + charAt + "'";
            }
            z = false;
        }
        return null;
    }

    private int getUidForPackage(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int i = this.mContext.getPackageManager().getApplicationInfo(str, DumpState.DUMP_CHANGES).uid;
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return i;
        } catch (PackageManager.NameNotFoundException unused) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return -1;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    @GuardedBy({"mAppRestrictionsLock"})
    private static Bundle readApplicationRestrictionsLAr(String str, int i) {
        return readApplicationRestrictionsLAr(new AtomicFile(new File(Environment.getUserSystemDirectory(i), packageToRestrictionsFileName(str))));
    }

    @GuardedBy({"mAppRestrictionsLock"})
    @VisibleForTesting
    static Bundle readApplicationRestrictionsLAr(AtomicFile atomicFile) {
        TypedXmlPullParser resolvePullParser;
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        if (!atomicFile.getBaseFile().exists()) {
            return bundle;
        }
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = atomicFile.openRead();
                resolvePullParser = Xml.resolvePullParser(fileInputStream);
                XmlUtils.nextElement(resolvePullParser);
            } catch (IOException | XmlPullParserException e) {
                Slog.w(LOG_TAG, "Error parsing " + atomicFile.getBaseFile(), e);
            }
            if (resolvePullParser.getEventType() != 2) {
                Slog.e(LOG_TAG, "Unable to read restrictions file " + atomicFile.getBaseFile());
                return bundle;
            }
            while (resolvePullParser.next() != 1) {
                readEntry(bundle, arrayList, resolvePullParser);
            }
            return bundle;
        } finally {
            IoUtils.closeQuietly((AutoCloseable) null);
        }
    }

    private static void readEntry(Bundle bundle, ArrayList<String> arrayList, TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        if (typedXmlPullParser.getEventType() == 2 && typedXmlPullParser.getName().equals(TAG_ENTRY)) {
            String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_KEY);
            String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, "type");
            int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_MULTIPLE, -1);
            if (attributeInt != -1) {
                arrayList.clear();
                while (attributeInt > 0) {
                    int next = typedXmlPullParser.next();
                    if (next == 1) {
                        break;
                    }
                    if (next == 2 && typedXmlPullParser.getName().equals(TAG_VALUE)) {
                        arrayList.add(typedXmlPullParser.nextText().trim());
                        attributeInt--;
                    }
                }
                String[] strArr = new String[arrayList.size()];
                arrayList.toArray(strArr);
                bundle.putStringArray(attributeValue, strArr);
                return;
            }
            if (ATTR_TYPE_BUNDLE.equals(attributeValue2)) {
                bundle.putBundle(attributeValue, readBundleEntry(typedXmlPullParser, arrayList));
                return;
            }
            if (ATTR_TYPE_BUNDLE_ARRAY.equals(attributeValue2)) {
                int depth = typedXmlPullParser.getDepth();
                ArrayList arrayList2 = new ArrayList();
                while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                    arrayList2.add(readBundleEntry(typedXmlPullParser, arrayList));
                }
                bundle.putParcelableArray(attributeValue, (Parcelable[]) arrayList2.toArray(new Bundle[arrayList2.size()]));
                return;
            }
            String trim = typedXmlPullParser.nextText().trim();
            if (ATTR_TYPE_BOOLEAN.equals(attributeValue2)) {
                bundle.putBoolean(attributeValue, Boolean.parseBoolean(trim));
            } else if (ATTR_TYPE_INTEGER.equals(attributeValue2)) {
                bundle.putInt(attributeValue, Integer.parseInt(trim));
            } else {
                bundle.putString(attributeValue, trim);
            }
        }
    }

    private static Bundle readBundleEntry(TypedXmlPullParser typedXmlPullParser, ArrayList<String> arrayList) throws IOException, XmlPullParserException {
        Bundle bundle = new Bundle();
        int depth = typedXmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
            readEntry(bundle, arrayList, typedXmlPullParser);
        }
        return bundle;
    }

    @GuardedBy({"mAppRestrictionsLock"})
    private static void writeApplicationRestrictionsLAr(String str, Bundle bundle, int i) {
        writeApplicationRestrictionsLAr(bundle, new AtomicFile(new File(Environment.getUserSystemDirectory(i), packageToRestrictionsFileName(str))));
    }

    @GuardedBy({"mAppRestrictionsLock"})
    @VisibleForTesting
    static void writeApplicationRestrictionsLAr(Bundle bundle, AtomicFile atomicFile) {
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream startWrite = atomicFile.startWrite();
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                resolveSerializer.startTag((String) null, TAG_RESTRICTIONS);
                writeBundle(bundle, resolveSerializer);
                resolveSerializer.endTag((String) null, TAG_RESTRICTIONS);
                resolveSerializer.endDocument();
                atomicFile.finishWrite(startWrite);
            } catch (Exception e) {
                e = e;
                fileOutputStream = startWrite;
                atomicFile.failWrite(fileOutputStream);
                Slog.e(LOG_TAG, "Error writing application restrictions list", e);
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private static void writeBundle(Bundle bundle, TypedXmlSerializer typedXmlSerializer) throws IOException {
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            typedXmlSerializer.startTag((String) null, TAG_ENTRY);
            typedXmlSerializer.attribute((String) null, ATTR_KEY, str);
            if (obj instanceof Boolean) {
                typedXmlSerializer.attribute((String) null, "type", ATTR_TYPE_BOOLEAN);
                typedXmlSerializer.text(obj.toString());
            } else if (obj instanceof Integer) {
                typedXmlSerializer.attribute((String) null, "type", ATTR_TYPE_INTEGER);
                typedXmlSerializer.text(obj.toString());
            } else if (obj == null || (obj instanceof String)) {
                typedXmlSerializer.attribute((String) null, "type", ATTR_TYPE_STRING);
                typedXmlSerializer.text(obj != null ? (String) obj : "");
            } else if (obj instanceof Bundle) {
                typedXmlSerializer.attribute((String) null, "type", ATTR_TYPE_BUNDLE);
                writeBundle((Bundle) obj, typedXmlSerializer);
            } else {
                int i = 0;
                if (obj instanceof Parcelable[]) {
                    typedXmlSerializer.attribute((String) null, "type", ATTR_TYPE_BUNDLE_ARRAY);
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    int length = parcelableArr.length;
                    while (i < length) {
                        Parcelable parcelable = parcelableArr[i];
                        if (!(parcelable instanceof Bundle)) {
                            throw new IllegalArgumentException("bundle-array can only hold Bundles");
                        }
                        typedXmlSerializer.startTag((String) null, TAG_ENTRY);
                        typedXmlSerializer.attribute((String) null, "type", ATTR_TYPE_BUNDLE);
                        writeBundle((Bundle) parcelable, typedXmlSerializer);
                        typedXmlSerializer.endTag((String) null, TAG_ENTRY);
                        i++;
                    }
                } else {
                    typedXmlSerializer.attribute((String) null, "type", ATTR_TYPE_STRING_ARRAY);
                    String[] strArr = (String[]) obj;
                    typedXmlSerializer.attributeInt((String) null, ATTR_MULTIPLE, strArr.length);
                    int length2 = strArr.length;
                    while (i < length2) {
                        String str2 = strArr[i];
                        typedXmlSerializer.startTag((String) null, TAG_VALUE);
                        if (str2 == null) {
                            str2 = "";
                        }
                        typedXmlSerializer.text(str2);
                        typedXmlSerializer.endTag((String) null, TAG_VALUE);
                        i++;
                    }
                }
            }
            typedXmlSerializer.endTag((String) null, TAG_ENTRY);
        }
    }

    public int getUserSerialNumber(int i) {
        int i2;
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            i2 = userInfoLU != null ? userInfoLU.serialNumber : -1;
        }
        return i2;
    }

    public boolean isUserNameSet(int i) {
        boolean z;
        int callingUid = Binder.getCallingUid();
        int userId = UserHandle.getUserId(callingUid);
        if (!hasQueryOrCreateUsersPermission() && (userId != i || !hasPermissionGranted("android.permission.GET_ACCOUNTS_PRIVILEGED", callingUid))) {
            throw new SecurityException("You need MANAGE_USERS, CREATE_USERS, QUERY_USERS, or GET_ACCOUNTS_PRIVILEGED permissions to: get whether user name is set");
        }
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            z = (userInfoLU == null || userInfoLU.name == null) ? false : true;
        }
        return z;
    }

    public int getUserHandle(int i) {
        synchronized (this.mUsersLock) {
            for (int i2 : this.mUserIds) {
                UserInfo userInfoLU = getUserInfoLU(i2);
                if (userInfoLU != null && userInfoLU.serialNumber == i) {
                    return i2;
                }
            }
            return -1;
        }
    }

    public long getUserCreationTime(int i) {
        UserInfo userInfoLU;
        int callingUserId = UserHandle.getCallingUserId();
        synchronized (this.mUsersLock) {
            if (callingUserId == i) {
                userInfoLU = getUserInfoLU(i);
            } else {
                UserInfo profileParentLU = getProfileParentLU(i);
                userInfoLU = (profileParentLU == null || profileParentLU.id != callingUserId) ? null : getUserInfoLU(i);
            }
        }
        if (userInfoLU == null) {
            throw new SecurityException("userId can only be the calling user or a profile associated with this user");
        }
        return userInfoLU.creationTime;
    }

    private void updateUserIds() {
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                UserInfo userInfo = this.mUsers.valueAt(i3).info;
                if (!userInfo.partial) {
                    i2++;
                    if (!userInfo.preCreated) {
                        i++;
                    }
                }
            }
            int[] iArr = new int[i];
            int[] iArr2 = new int[i2];
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < size; i6++) {
                UserInfo userInfo2 = this.mUsers.valueAt(i6).info;
                if (!userInfo2.partial) {
                    int keyAt = this.mUsers.keyAt(i6);
                    int i7 = i4 + 1;
                    iArr2[i4] = keyAt;
                    if (!userInfo2.preCreated) {
                        iArr[i5] = keyAt;
                        i5++;
                    }
                    i4 = i7;
                }
            }
            this.mUserIds = iArr;
            this.mUserIdsIncludingPreCreated = iArr2;
            UserPackage.setValidUserIds(iArr);
        }
    }

    public void onBeforeStartUser(int i) {
        UserInfo userInfo = getUserInfo(i);
        if (userInfo == null) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        timingsTraceAndSlog.traceBegin("onBeforeStartUser-" + i);
        int i2 = userInfo.serialNumber;
        boolean equals = PackagePartitions.FINGERPRINT.equals(userInfo.lastLoggedInFingerprint) ^ true;
        timingsTraceAndSlog.traceBegin("prepareUserData");
        this.mUserDataPreparer.prepareUserData(i, i2, 1);
        timingsTraceAndSlog.traceEnd();
        long elapsedRealtime2 = SystemClock.elapsedRealtime();
        long j = elapsedRealtime2 - elapsedRealtime;
        timingsTraceAndSlog.traceBegin("reconcileAppsData");
        getPackageManagerInternal().reconcileAppsData(i, 1, equals);
        timingsTraceAndSlog.traceEnd();
        long elapsedRealtime3 = SystemClock.elapsedRealtime() - elapsedRealtime2;
        if (i != 0) {
            timingsTraceAndSlog.traceBegin("applyUserRestrictions");
            synchronized (this.mRestrictionsLock) {
                applyUserRestrictionsLR(i);
            }
            timingsTraceAndSlog.traceEnd();
        }
        timingsTraceAndSlog.traceEnd();
        this.mWrapper.getExtImpl().onBeforeStartUserExit(i, SystemClock.elapsedRealtime() - elapsedRealtime, j, elapsedRealtime3);
    }

    public void onBeforeUnlockUser(int i) {
        UserInfo userInfo = getUserInfo(i);
        if (userInfo == null) {
            return;
        }
        this.mWrapper.getExtImpl().normalizeExternalStorageData(i);
        int i2 = userInfo.serialNumber;
        boolean z = !PackagePartitions.FINGERPRINT.equals(userInfo.lastLoggedInFingerprint);
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
        timingsTraceAndSlog.traceBegin("prepareUserData-" + i);
        this.mUserDataPreparer.prepareUserData(i, i2, 2);
        timingsTraceAndSlog.traceEnd();
        ((StorageManagerInternal) LocalServices.getService(StorageManagerInternal.class)).markCeStoragePrepared(i);
        timingsTraceAndSlog.traceBegin("reconcileAppsData-" + i);
        getPackageManagerInternal().reconcileAppsData(i, 2, z);
        timingsTraceAndSlog.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reconcileUsers(String str) {
        this.mUserDataPreparer.reconcileUsers(str, getUsers(true, true, false));
    }

    public void onUserLoggedIn(int i) {
        UserData userDataNoChecks = getUserDataNoChecks(i);
        if (userDataNoChecks == null || userDataNoChecks.info.partial) {
            Slog.w(LOG_TAG, "userForeground: unknown user #" + i);
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > EPOCH_PLUS_30_YEARS) {
            userDataNoChecks.info.lastLoggedInTime = currentTimeMillis;
        }
        userDataNoChecks.info.lastLoggedInFingerprint = PackagePartitions.FINGERPRINT;
        scheduleWriteUser(i);
    }

    @VisibleForTesting
    int getNextAvailableId() {
        synchronized (this.mUsersLock) {
            int scanNextAvailableIdLocked = scanNextAvailableIdLocked();
            if (scanNextAvailableIdLocked >= 0) {
                return scanNextAvailableIdLocked;
            }
            if (this.mRemovingUserIds.size() > 0) {
                Slog.i(LOG_TAG, "All available IDs are used. Recycling LRU ids.");
                this.mRemovingUserIds.clear();
                Iterator<Integer> it = this.mRecentlyRemovedIds.iterator();
                while (it.hasNext()) {
                    this.mRemovingUserIds.put(it.next().intValue(), true);
                }
                scanNextAvailableIdLocked = scanNextAvailableIdLocked();
            }
            UserManager.invalidateStaticUserProperties();
            UserManager.invalidateUserPropertiesCache();
            if (scanNextAvailableIdLocked >= 0) {
                return scanNextAvailableIdLocked;
            }
            throw new IllegalStateException("No user id available!");
        }
    }

    @GuardedBy({"mUsersLock"})
    private int scanNextAvailableIdLocked() {
        for (int i = 10; i < MAX_USER_ID; i++) {
            if (this.mUsers.indexOfKey(i) < 0 && !this.mRemovingUserIds.get(i) && !this.mWrapper.getExtImpl().skipCustomUserId(i)) {
                return i;
            }
        }
        return -1;
    }

    private static String packageToRestrictionsFileName(String str) {
        return RESTRICTIONS_FILE_PREFIX + str + XML_SUFFIX;
    }

    private static String getRedacted(String str) {
        if (str == null) {
            return null;
        }
        return str.length() + "_chars";
    }

    public void setSeedAccountData(int i, String str, String str2, PersistableBundle persistableBundle, boolean z) {
        checkManageUsersPermission("set user seed account data");
        setSeedAccountDataNoChecks(i, str, str2, persistableBundle, z);
    }

    private void setSeedAccountDataNoChecks(int i, String str, String str2, PersistableBundle persistableBundle, boolean z) {
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                UserData userDataLU = getUserDataLU(i);
                if (userDataLU == null) {
                    Slog.e(LOG_TAG, "No such user for settings seed data u=" + i);
                    return;
                }
                userDataLU.seedAccountName = truncateString(str, SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS);
                userDataLU.seedAccountType = truncateString(str2, SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS);
                if (persistableBundle != null && persistableBundle.isBundleContentsWithinLengthLimit(1000)) {
                    userDataLU.seedAccountOptions = persistableBundle;
                }
                userDataLU.persistSeedData = z;
                if (z) {
                    writeUserLP(userDataLU);
                }
            }
        }
    }

    public String getSeedAccountName(int i) throws RemoteException {
        String str;
        checkManageUsersPermission("Cannot get seed account information");
        synchronized (this.mUsersLock) {
            UserData userDataLU = getUserDataLU(i);
            str = userDataLU == null ? null : userDataLU.seedAccountName;
        }
        return str;
    }

    public String getSeedAccountType(int i) throws RemoteException {
        String str;
        checkManageUsersPermission("Cannot get seed account information");
        synchronized (this.mUsersLock) {
            UserData userDataLU = getUserDataLU(i);
            str = userDataLU == null ? null : userDataLU.seedAccountType;
        }
        return str;
    }

    public PersistableBundle getSeedAccountOptions(int i) throws RemoteException {
        PersistableBundle persistableBundle;
        checkManageUsersPermission("Cannot get seed account information");
        synchronized (this.mUsersLock) {
            UserData userDataLU = getUserDataLU(i);
            persistableBundle = userDataLU == null ? null : userDataLU.seedAccountOptions;
        }
        return persistableBundle;
    }

    public void clearSeedAccountData(int i) throws RemoteException {
        checkManageUsersPermission("Cannot clear seed account information");
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                UserData userDataLU = getUserDataLU(i);
                if (userDataLU == null) {
                    return;
                }
                userDataLU.clearSeedAccountData();
                writeUserLP(userDataLU);
            }
        }
    }

    public boolean someUserHasSeedAccount(String str, String str2) {
        checkManageUsersPermission("check seed account information");
        return someUserHasSeedAccountNoChecks(str, str2);
    }

    private boolean someUserHasSeedAccountNoChecks(String str, String str2) {
        String str3;
        String str4;
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                UserData valueAt = this.mUsers.valueAt(i);
                if (!valueAt.info.isInitialized() && !this.mRemovingUserIds.get(valueAt.info.id) && (str3 = valueAt.seedAccountName) != null && str3.equals(str) && (str4 = valueAt.seedAccountType) != null && str4.equals(str2)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean someUserHasAccount(String str, String str2) {
        checkCreateUsersPermission("check seed account information");
        return someUserHasAccountNoChecks(str, str2);
    }

    private boolean someUserHasAccountNoChecks(final String str, final String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        final Account account = new Account(str, str2);
        return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda0
            public final Object getOrThrow() {
                Boolean lambda$someUserHasAccountNoChecks$5;
                lambda$someUserHasAccountNoChecks$5 = UserManagerService.this.lambda$someUserHasAccountNoChecks$5(account, str, str2);
                return lambda$someUserHasAccountNoChecks$5;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$someUserHasAccountNoChecks$5(Account account, String str, String str2) throws Exception {
        return Boolean.valueOf(AccountManager.get(this.mContext).someUserHasAccount(account) || someUserHasSeedAccountNoChecks(str, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLastEnteredForegroundTimeToNow(UserData userData) {
        userData.mLastEnteredForegroundTimeMillis = System.currentTimeMillis();
        scheduleWriteUser(userData.info.id);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new UserManagerServiceShellCommand(this, this.mSystemPackageInstaller, this.mLockPatternUtils, this.mContext).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Object obj;
        Object obj2;
        int i;
        Object obj3;
        if (!DumpUtils.checkDumpPermission(this.mContext, LOG_TAG, printWriter)) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        StringBuilder sb = new StringBuilder();
        if (strArr != null && strArr.length > 0) {
            String str = strArr[0];
            str.hashCode();
            if (str.equals("--visibility-mediator")) {
                this.mUserVisibilityMediator.dump(printWriter, strArr);
                return;
            } else if (str.equals("--user")) {
                dumpUser(printWriter, UserHandle.parseUserArg(strArr[1]), sb, currentTimeMillis, elapsedRealtime);
                return;
            }
        }
        int currentUserId = getCurrentUserId();
        printWriter.print("Current user: ");
        if (currentUserId != -10000) {
            printWriter.println(currentUserId);
        } else {
            printWriter.println("N/A");
        }
        printWriter.println();
        Object obj4 = this.mPackagesLock;
        synchronized (obj4) {
            try {
            } catch (Throwable th) {
                th = th;
            }
            try {
                Object obj5 = this.mUsersLock;
                synchronized (obj5) {
                    try {
                        printWriter.println("Users:");
                        int i2 = 0;
                        while (i2 < this.mUsers.size()) {
                            UserData valueAt = this.mUsers.valueAt(i2);
                            if (valueAt == null) {
                                i = i2;
                                obj3 = obj4;
                                obj2 = obj5;
                            } else {
                                i = i2;
                                obj3 = obj4;
                                obj2 = obj5;
                                try {
                                    dumpUserLocked(printWriter, valueAt, sb, currentTimeMillis, elapsedRealtime);
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            }
                            i2 = i + 1;
                            obj4 = obj3;
                            obj5 = obj2;
                        }
                        obj = obj4;
                        printWriter.println();
                        printWriter.println("Device properties:");
                        printWriter.println("  Device policy global restrictions:");
                        synchronized (this.mRestrictionsLock) {
                            UserRestrictionsUtils.dumpRestrictions(printWriter, "    ", this.mDevicePolicyUserRestrictions.getRestrictions(-1));
                        }
                        printWriter.println("  Guest restrictions:");
                        synchronized (this.mGuestRestrictions) {
                            UserRestrictionsUtils.dumpRestrictions(printWriter, "    ", this.mGuestRestrictions);
                        }
                        synchronized (this.mUsersLock) {
                            printWriter.println();
                            printWriter.println("  Device managed: " + this.mIsDeviceManaged);
                            if (this.mRemovingUserIds.size() > 0) {
                                printWriter.println();
                                printWriter.println("  Recently removed userIds: " + this.mRecentlyRemovedIds);
                            }
                        }
                        synchronized (this.mUserStates) {
                            printWriter.print("  Started users state: [");
                            int size = this.mUserStates.states.size();
                            for (int i3 = 0; i3 < size; i3++) {
                                int keyAt = this.mUserStates.states.keyAt(i3);
                                int valueAt2 = this.mUserStates.states.valueAt(i3);
                                printWriter.print(keyAt);
                                printWriter.print('=');
                                printWriter.print(UserState.stateToString(valueAt2));
                                if (i3 != size - 1) {
                                    printWriter.print(", ");
                                }
                            }
                            printWriter.println(']');
                        }
                        synchronized (this.mUsersLock) {
                            printWriter.print("  Cached user IDs: ");
                            printWriter.println(Arrays.toString(this.mUserIds));
                            printWriter.print("  Cached user IDs (including pre-created): ");
                            printWriter.println(Arrays.toString(this.mUserIdsIncludingPreCreated));
                        }
                        printWriter.println();
                        this.mUserVisibilityMediator.dump(printWriter, strArr);
                        printWriter.println();
                        printWriter.println();
                        printWriter.print("  Max users: " + UserManager.getMaxSupportedUsers());
                        printWriter.println(" (limit reached: " + isUserLimitReached() + ")");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("  Supports switchable users: ");
                        sb2.append(UserManager.supportsMultipleUsers());
                        printWriter.println(sb2.toString());
                        printWriter.println("  All guests ephemeral: " + Resources.getSystem().getBoolean(17891706));
                        printWriter.println("  Force ephemeral users: " + this.mForceEphemeralUsers);
                        boolean isHeadlessSystemUserMode = isHeadlessSystemUserMode();
                        printWriter.println("  Is headless-system mode: " + isHeadlessSystemUserMode);
                        if (isHeadlessSystemUserMode != RoSystemProperties.MULTIUSER_HEADLESS_SYSTEM_USER) {
                            printWriter.println("  (differs from the current default build value)");
                        }
                        if (!TextUtils.isEmpty(SystemProperties.get("persist.debug.user_mode_emulation"))) {
                            printWriter.println("  (emulated by 'cmd user set-system-user-mode-emulation')");
                            if (this.mUpdatingSystemUserMode) {
                                printWriter.println("  (and being updated after boot)");
                            }
                        }
                        printWriter.println("  User version: " + this.mUserVersion);
                        printWriter.println("  Owner name: " + getOwnerName());
                        synchronized (this.mUsersLock) {
                            printWriter.println("  Boot user: " + this.mBootUser);
                        }
                        printWriter.println();
                        printWriter.println("Number of listeners for");
                        synchronized (this.mUserRestrictionsListeners) {
                            printWriter.println("  restrictions: " + this.mUserRestrictionsListeners.size());
                        }
                        synchronized (this.mUserLifecycleListeners) {
                            printWriter.println("  user lifecycle events: " + this.mUserLifecycleListeners.size());
                        }
                        printWriter.println();
                        printWriter.println("User types version: " + this.mUserTypeVersion);
                        printWriter.println("User types (" + this.mUserTypes.size() + " types):");
                        for (int i4 = 0; i4 < this.mUserTypes.size(); i4++) {
                            printWriter.println("    " + this.mUserTypes.keyAt(i4) + ": ");
                            this.mUserTypes.valueAt(i4).dump(printWriter, "        ");
                        }
                        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
                        try {
                            indentingPrintWriter.println();
                            this.mSystemPackageInstaller.dump(indentingPrintWriter);
                            indentingPrintWriter.close();
                        } finally {
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        obj2 = obj5;
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                obj = obj4;
                throw th;
            }
        }
    }

    private void dumpUser(PrintWriter printWriter, int i, StringBuilder sb, long j, long j2) {
        int i2;
        if (i == -2) {
            i2 = getCurrentUserId();
            printWriter.print("Current user: ");
            if (i2 == -10000) {
                printWriter.println("Cannot determine current user");
                return;
            }
        } else {
            i2 = i;
        }
        synchronized (this.mUsersLock) {
            UserData userData = this.mUsers.get(i2);
            if (userData == null) {
                printWriter.println("User " + i2 + " not found");
                return;
            }
            dumpUserLocked(printWriter, userData, sb, j, j2);
        }
    }

    @GuardedBy({"mUsersLock"})
    private void dumpUserLocked(PrintWriter printWriter, UserData userData, StringBuilder sb, long j, long j2) {
        int i;
        UserInfo userInfo = userData.info;
        int i2 = userInfo.id;
        printWriter.print("  ");
        printWriter.print(userInfo);
        printWriter.print(" serialNo=");
        printWriter.print(userInfo.serialNumber);
        printWriter.print(" isPrimary=");
        printWriter.print(userInfo.isPrimary());
        int i3 = userInfo.profileGroupId;
        if (i3 != userInfo.id && i3 != -10000) {
            printWriter.print(" parentId=");
            printWriter.print(userInfo.profileGroupId);
        }
        if (this.mRemovingUserIds.get(i2)) {
            printWriter.print(" <removing> ");
        }
        if (userInfo.partial) {
            printWriter.print(" <partial>");
        }
        if (userInfo.preCreated) {
            printWriter.print(" <pre-created>");
        }
        if (userInfo.convertedFromPreCreated) {
            printWriter.print(" <converted>");
        }
        printWriter.println();
        printWriter.print("    Type: ");
        printWriter.println(userInfo.userType);
        printWriter.print("    Flags: ");
        printWriter.print(userInfo.flags);
        printWriter.print(" (");
        printWriter.print(UserInfo.flagsToString(userInfo.flags));
        printWriter.println(")");
        printWriter.print("    State: ");
        synchronized (this.mUserStates) {
            i = this.mUserStates.get(i2, -1);
        }
        printWriter.println(UserState.stateToString(i));
        printWriter.print("    Created: ");
        dumpTimeAgo(printWriter, sb, j, userInfo.creationTime);
        printWriter.print("    Last logged in: ");
        dumpTimeAgo(printWriter, sb, j, userInfo.lastLoggedInTime);
        printWriter.print("    Last logged in fingerprint: ");
        printWriter.println(userInfo.lastLoggedInFingerprint);
        printWriter.print("    Start time: ");
        dumpTimeAgo(printWriter, sb, j2, userData.startRealtime);
        printWriter.print("    Unlock time: ");
        dumpTimeAgo(printWriter, sb, j2, userData.unlockRealtime);
        printWriter.print("    Last entered foreground: ");
        dumpTimeAgo(printWriter, sb, j, userData.mLastEnteredForegroundTimeMillis);
        printWriter.print("    Has profile owner: ");
        printWriter.println(this.mIsUserManaged.get(i2));
        printWriter.println("    Restrictions:");
        synchronized (this.mRestrictionsLock) {
            UserRestrictionsUtils.dumpRestrictions(printWriter, "      ", this.mBaseUserRestrictions.getRestrictions(userInfo.id));
            printWriter.println("    Device policy restrictions:");
            UserRestrictionsUtils.dumpRestrictions(printWriter, "      ", this.mDevicePolicyUserRestrictions.getRestrictions(userInfo.id));
            printWriter.println("    Effective restrictions:");
            UserRestrictionsUtils.dumpRestrictions(printWriter, "      ", this.mCachedEffectiveUserRestrictions.getRestrictions(userInfo.id));
        }
        if (userData.account != null) {
            printWriter.print("    Account name: " + userData.account);
            printWriter.println();
        }
        if (userData.seedAccountName != null) {
            printWriter.print("    Seed account name: " + userData.seedAccountName);
            printWriter.println();
            if (userData.seedAccountType != null) {
                printWriter.print("         account type: " + userData.seedAccountType);
                printWriter.println();
            }
            if (userData.seedAccountOptions != null) {
                printWriter.print("         account options exist");
                printWriter.println();
            }
        }
        UserProperties userProperties = userData.userProperties;
        if (userProperties != null) {
            userProperties.println(printWriter, "    ");
        }
        printWriter.println("    Ignore errors preparing storage: " + userData.getIgnorePrepareStorageErrors());
    }

    private static void dumpTimeAgo(PrintWriter printWriter, StringBuilder sb, long j, long j2) {
        if (j2 == 0) {
            printWriter.println("<unknown>");
            return;
        }
        sb.setLength(0);
        TimeUtils.formatDuration(j - j2, sb);
        sb.append(" ago");
        printWriter.println(sb);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class MainHandler extends Handler {
        MainHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 1) {
                if (i != 2) {
                    return;
                }
                removeMessages(2);
                synchronized (UserManagerService.this.mPackagesLock) {
                    UserManagerService.this.writeUserListLP();
                }
                return;
            }
            removeMessages(1, message.obj);
            synchronized (UserManagerService.this.mPackagesLock) {
                int intValue = ((Integer) message.obj).intValue();
                UserData userDataNoChecks = UserManagerService.this.getUserDataNoChecks(intValue);
                if (userDataNoChecks != null) {
                    UserManagerService.this.writeUserLP(userDataNoChecks);
                } else {
                    Slog.i(UserManagerService.LOG_TAG, "handle(WRITE_USER_MSG): no data for user " + intValue + ", it was probably removed before handler could handle it");
                }
            }
        }
    }

    boolean isUserInitialized(int i) {
        return this.mLocalService.isUserInitialized(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class LocalService extends UserManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setDevicePolicyUserRestrictions(int i, Bundle bundle, RestrictionsSet restrictionsSet, boolean z) {
            UserManagerService.this.setDevicePolicyUserRestrictionsInner(i, bundle, restrictionsSet, z);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setUserRestriction(int i, String str, boolean z) {
            UserManagerService.this.setUserRestrictionInner(i, str, z);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean getUserRestriction(int i, String str) {
            return UserManagerService.this.getUserRestrictions(i).getBoolean(str);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void addUserRestrictionsListener(UserManagerInternal.UserRestrictionsListener userRestrictionsListener) {
            synchronized (UserManagerService.this.mUserRestrictionsListeners) {
                UserManagerService.this.mUserRestrictionsListeners.add(userRestrictionsListener);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeUserRestrictionsListener(UserManagerInternal.UserRestrictionsListener userRestrictionsListener) {
            synchronized (UserManagerService.this.mUserRestrictionsListeners) {
                UserManagerService.this.mUserRestrictionsListeners.remove(userRestrictionsListener);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void addUserLifecycleListener(UserManagerInternal.UserLifecycleListener userLifecycleListener) {
            synchronized (UserManagerService.this.mUserLifecycleListeners) {
                UserManagerService.this.mUserLifecycleListeners.add(userLifecycleListener);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeUserLifecycleListener(UserManagerInternal.UserLifecycleListener userLifecycleListener) {
            synchronized (UserManagerService.this.mUserLifecycleListeners) {
                UserManagerService.this.mUserLifecycleListeners.remove(userLifecycleListener);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setDeviceManaged(boolean z) {
            synchronized (UserManagerService.this.mUsersLock) {
                UserManagerService.this.mIsDeviceManaged = z;
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isDeviceManaged() {
            boolean z;
            synchronized (UserManagerService.this.mUsersLock) {
                z = UserManagerService.this.mIsDeviceManaged;
            }
            return z;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setUserManaged(int i, boolean z) {
            synchronized (UserManagerService.this.mUsersLock) {
                UserManagerService.this.mIsUserManaged.put(i, z);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserManaged(int i) {
            boolean z;
            synchronized (UserManagerService.this.mUsersLock) {
                z = UserManagerService.this.mIsUserManaged.get(i);
            }
            return z;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setUserIcon(int i, Bitmap bitmap) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (UserManagerService.this.mPackagesLock) {
                    UserData userDataNoChecks = UserManagerService.this.getUserDataNoChecks(i);
                    if (userDataNoChecks != null) {
                        UserInfo userInfo = userDataNoChecks.info;
                        if (!userInfo.partial) {
                            UserManagerService.this.writeBitmapLP(userInfo, bitmap);
                            UserManagerService.this.writeUserLP(userDataNoChecks);
                            UserManagerService.this.sendUserInfoChangedBroadcast(i);
                            return;
                        }
                    }
                    Slog.w(UserManagerService.LOG_TAG, "setUserIcon: unknown user #" + i);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setForceEphemeralUsers(boolean z) {
            synchronized (UserManagerService.this.mUsersLock) {
                UserManagerService.this.mForceEphemeralUsers = z;
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeAllUsers() {
            if (UserManagerService.this.getCurrentUserId() == 0) {
                UserManagerService.this.removeAllUsersExceptSystemAndPermanentAdminMain();
                return;
            }
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.pm.UserManagerService.LocalService.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (intent.getIntExtra("android.intent.extra.user_handle", -10000) != 0) {
                        return;
                    }
                    UserManagerService.this.mContext.unregisterReceiver(this);
                    UserManagerService.this.removeAllUsersExceptSystemAndPermanentAdminMain();
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            UserManagerService.this.mContext.registerReceiver(broadcastReceiver, intentFilter, null, UserManagerService.this.mHandler);
            ((ActivityManager) UserManagerService.this.mContext.getSystemService("activity")).switchUser(0);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void onEphemeralUserStop(int i) {
            synchronized (UserManagerService.this.mUsersLock) {
                UserInfo userInfoLU = UserManagerService.this.getUserInfoLU(i);
                if (userInfoLU != null && userInfoLU.isEphemeral()) {
                    userInfoLU.flags |= 64;
                    if (userInfoLU.isGuest()) {
                        userInfoLU.guestToRemove = true;
                    }
                }
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public UserInfo createUserEvenWhenDisallowed(String str, String str2, int i, String[] strArr, Object obj) throws UserManager.CheckedUserOperationException {
            return UserManagerService.this.createUserInternalUnchecked(str, str2, i, -10000, false, strArr, obj);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean removeUserEvenWhenDisallowed(int i) {
            return UserManagerService.this.removeUserWithProfilesUnchecked(i);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserRunning(int i) {
            int i2;
            synchronized (UserManagerService.this.mUserStates) {
                i2 = UserManagerService.this.mUserStates.get(i, -1);
            }
            return (i2 == -1 || i2 == 4 || i2 == 5) ? false : true;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setUserState(int i, int i2) {
            synchronized (UserManagerService.this.mUserStates) {
                UserManagerService.this.mUserStates.put(i, i2);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeUserState(int i) {
            synchronized (UserManagerService.this.mUserStates) {
                UserManagerService.this.mUserStates.delete(i);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int[] getUserIds() {
            return UserManagerService.this.getUserIds();
        }

        @Override // com.android.server.pm.UserManagerInternal
        public List<UserInfo> getUsers(boolean z) {
            return getUsers(true, z, true);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public List<UserInfo> getUsers(boolean z, boolean z2, boolean z3) {
            return UserManagerService.this.getUsersInternal(z, z2, z3);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int[] getProfileIds(int i, boolean z) {
            int[] array;
            synchronized (UserManagerService.this.mUsersLock) {
                array = UserManagerService.this.getProfileIdsLU(i, null, z).toArray();
            }
            return array;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserUnlockingOrUnlocked(int i) {
            int i2;
            synchronized (UserManagerService.this.mUserStates) {
                i2 = UserManagerService.this.mUserStates.get(i, -1);
            }
            if (i2 == 4 || i2 == 5) {
                return StorageManager.isUserKeyUnlocked(i);
            }
            return i2 == 2 || i2 == 3;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserUnlocked(int i) {
            int i2;
            synchronized (UserManagerService.this.mUserStates) {
                i2 = UserManagerService.this.mUserStates.get(i, -1);
            }
            if (i2 == 4 || i2 == 5) {
                return StorageManager.isUserKeyUnlocked(i);
            }
            return i2 == 3;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserInitialized(int i) {
            return (getUserInfo(i).flags & 16) != 0;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean exists(int i) {
            return UserManagerService.this.getUserInfoNoChecks(i) != null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:27:0x003a, code lost:
        
            return false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x0057, code lost:
        
            android.util.Slog.w(com.android.server.pm.UserManagerService.LOG_TAG, r8 + " for disabled profile " + r7 + " from " + r6);
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x007c, code lost:
        
            android.util.Slog.w(com.android.server.pm.UserManagerService.LOG_TAG, r8 + " for another profile " + r7 + " from " + r6);
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x009e, code lost:
        
            return false;
         */
        @Override // com.android.server.pm.UserManagerInternal
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean isProfileAccessible(int i, int i2, String str, boolean z) {
            if (i2 == i) {
                return true;
            }
            synchronized (UserManagerService.this.mUsersLock) {
                UserInfo userInfoLU = UserManagerService.this.getUserInfoLU(i);
                if (userInfoLU != null && !userInfoLU.isProfile()) {
                    UserInfo userInfoLU2 = UserManagerService.this.getUserInfoLU(i2);
                    if (userInfoLU2 != null && userInfoLU2.isEnabled()) {
                        int i3 = userInfoLU2.profileGroupId;
                        if (i3 != -10000 && i3 == userInfoLU.profileGroupId) {
                            return true;
                        }
                        throw new SecurityException(str + " for unrelated profile " + i2);
                    }
                    return false;
                }
                throw new SecurityException(str + " for another profile " + i2 + " from " + i);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getProfileParentId(int i) {
            return UserManagerService.this.getProfileParentIdUnchecked(i);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isSettingRestrictedForUser(String str, int i, String str2, int i2) {
            return UserManagerService.this.isSettingRestrictedForUser(str, i, str2, i2);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean hasUserRestriction(String str, int i) {
            Bundle effectiveUserRestrictions;
            return UserRestrictionsUtils.isValidRestriction(str) && (effectiveUserRestrictions = UserManagerService.this.getEffectiveUserRestrictions(i)) != null && effectiveUserRestrictions.getBoolean(str);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public UserInfo getUserInfo(int i) {
            UserData userData;
            synchronized (UserManagerService.this.mUsersLock) {
                userData = (UserData) UserManagerService.this.mUsers.get(i);
            }
            if (userData == null) {
                return null;
            }
            return userData.info;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public UserInfo[] getUserInfos() {
            UserInfo[] userInfoArr;
            synchronized (UserManagerService.this.mUsersLock) {
                int size = UserManagerService.this.mUsers.size();
                userInfoArr = new UserInfo[size];
                for (int i = 0; i < size; i++) {
                    userInfoArr[i] = ((UserData) UserManagerService.this.mUsers.valueAt(i)).info;
                }
            }
            return userInfoArr;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setDefaultCrossProfileIntentFilters(int i, int i2) {
            UserManagerService.this.setDefaultCrossProfileIntentFilters(i2, UserManagerService.this.getUserTypeDetailsNoChecks(i2), UserManagerService.this.getEffectiveUserRestrictions(i2), i);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean shouldIgnorePrepareStorageErrors(int i) {
            boolean z;
            synchronized (UserManagerService.this.mUsersLock) {
                UserData userData = (UserData) UserManagerService.this.mUsers.get(i);
                z = userData != null && userData.getIgnorePrepareStorageErrors();
            }
            return z;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public UserProperties getUserProperties(int i) {
            UserProperties userPropertiesInternal = UserManagerService.this.getUserPropertiesInternal(i);
            if (userPropertiesInternal == null) {
                Slog.w(UserManagerService.LOG_TAG, "A null UserProperties was returned for user " + i);
            }
            return userPropertiesInternal;
        }

        @Override // com.android.server.pm.UserManagerInternal
        @UserManagerInternal.UserAssignmentResult
        public int assignUserToDisplayOnStart(int i, int i2, @UserManagerInternal.UserStartMode int i3, int i4) {
            return UserManagerService.this.mUserVisibilityMediator.assignUserToDisplayOnStart(i, i2, i3, i4);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean assignUserToExtraDisplay(int i, int i2) {
            return UserManagerService.this.mUserVisibilityMediator.assignUserToExtraDisplay(i, i2);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean unassignUserFromExtraDisplay(int i, int i2) {
            return UserManagerService.this.mUserVisibilityMediator.unassignUserFromExtraDisplay(i, i2);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void unassignUserFromDisplayOnStop(int i) {
            UserManagerService.this.mUserVisibilityMediator.unassignUserFromDisplayOnStop(i);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserVisible(int i) {
            return UserManagerService.this.mUserVisibilityMediator.isUserVisible(i);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserVisible(int i, int i2) {
            return UserManagerService.this.mUserVisibilityMediator.isUserVisible(i, i2);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getMainDisplayAssignedToUser(int i) {
            return UserManagerService.this.mUserVisibilityMediator.getMainDisplayAssignedToUser(i);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int[] getDisplaysAssignedToUser(int i) {
            return UserManagerService.this.mUserVisibilityMediator.getDisplaysAssignedToUser(i);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getUserAssignedToDisplay(int i) {
            return UserManagerService.this.mUserVisibilityMediator.getUserAssignedToDisplay(i);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void addUserVisibilityListener(UserManagerInternal.UserVisibilityListener userVisibilityListener) {
            UserManagerService.this.mUserVisibilityMediator.addListener(userVisibilityListener);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeUserVisibilityListener(UserManagerInternal.UserVisibilityListener userVisibilityListener) {
            UserManagerService.this.mUserVisibilityMediator.removeListener(userVisibilityListener);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void onSystemUserVisibilityChanged(boolean z) {
            UserManagerService.this.mUserVisibilityMediator.onSystemUserVisibilityChanged(z);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int[] getUserTypesForStatsd(int[] iArr) {
            if (iArr == null) {
                return null;
            }
            int length = iArr.length;
            int[] iArr2 = new int[length];
            for (int i = 0; i < length; i++) {
                UserInfo userInfo = getUserInfo(iArr[i]);
                if (userInfo == null) {
                    UserJourneyLogger unused = UserManagerService.this.mUserJourneyLogger;
                    iArr2[i] = UserJourneyLogger.getUserTypeForStatsd("");
                } else {
                    UserJourneyLogger unused2 = UserManagerService.this.mUserJourneyLogger;
                    iArr2[i] = UserJourneyLogger.getUserTypeForStatsd(userInfo.userType);
                }
            }
            return iArr2;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getMainUserId() {
            return UserManagerService.this.getMainUserIdUnchecked();
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getBootUser(boolean z) throws UserManager.CheckedUserOperationException {
            if (z) {
                TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog();
                timingsTraceAndSlog.traceBegin("wait-boot-user");
                try {
                    if (UserManagerService.this.mBootUserLatch.getCount() != 0) {
                        Slogf.d(UserManagerService.LOG_TAG, "Sleeping for boot user to be set. Max sleep for Time: %d", Long.valueOf(UserManagerService.BOOT_USER_SET_TIMEOUT_MS));
                    }
                    if (!UserManagerService.this.mBootUserLatch.await(UserManagerService.BOOT_USER_SET_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                        Slogf.w(UserManagerService.LOG_TAG, "Boot user not set. Timeout: %d", Long.valueOf(UserManagerService.BOOT_USER_SET_TIMEOUT_MS));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Slogf.w(UserManagerService.LOG_TAG, e, "InterruptedException during wait for boot user.", new Object[0]);
                }
                timingsTraceAndSlog.traceEnd();
            }
            return UserManagerService.this.getBootUserUnchecked();
        }
    }

    private void enforceUserRestriction(String str, int i, String str2) throws UserManager.CheckedUserOperationException {
        String str3;
        if (hasUserRestriction(str, i)) {
            StringBuilder sb = new StringBuilder();
            if (str2 != null) {
                str3 = str2 + ": ";
            } else {
                str3 = "";
            }
            sb.append(str3);
            sb.append(str);
            sb.append(" is enabled.");
            String sb2 = sb.toString();
            Slog.w(LOG_TAG, sb2);
            throw new UserManager.CheckedUserOperationException(sb2, 1);
        }
    }

    private void throwCheckedUserOperationException(String str, int i) throws UserManager.CheckedUserOperationException {
        Slog.e(LOG_TAG, str);
        throw new UserManager.CheckedUserOperationException(str, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAllUsersExceptSystemAndPermanentAdminMain() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                UserInfo userInfo = this.mUsers.valueAt(i).info;
                if (userInfo.id != 0 && !isNonRemovableMainUser(userInfo)) {
                    arrayList.add(userInfo);
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            removeUser(((UserInfo) it.next()).id);
        }
    }

    private static void debug(String str) {
        Slog.d(LOG_TAG, str + "");
    }

    @VisibleForTesting
    int getMaxUsersOfTypePerParent(String str) {
        UserTypeDetails userTypeDetails = this.mUserTypes.get(str);
        if (userTypeDetails == null) {
            return 0;
        }
        return getMaxUsersOfTypePerParent(userTypeDetails);
    }

    private static int getMaxUsersOfTypePerParent(UserTypeDetails userTypeDetails) {
        int maxAllowedPerParent = userTypeDetails.getMaxAllowedPerParent();
        return (Build.IS_DEBUGGABLE && userTypeDetails.isManagedProfile()) ? SystemProperties.getInt("persist.sys.max_profiles", maxAllowedPerParent) : maxAllowedPerParent;
    }

    @GuardedBy({"mUsersLock"})
    @VisibleForTesting
    int getFreeProfileBadgeLU(int i, String str) {
        ArraySet arraySet = new ArraySet();
        int size = this.mUsers.size();
        for (int i2 = 0; i2 < size; i2++) {
            UserInfo userInfo = this.mUsers.valueAt(i2).info;
            if (userInfo.userType.equals(str) && userInfo.profileGroupId == i && !this.mRemovingUserIds.get(userInfo.id)) {
                arraySet.add(Integer.valueOf(userInfo.profileBadge));
            }
        }
        int maxUsersOfTypePerParent = getMaxUsersOfTypePerParent(str);
        if (maxUsersOfTypePerParent == -1) {
            maxUsersOfTypePerParent = Integer.MAX_VALUE;
        }
        for (int i3 = 0; i3 < maxUsersOfTypePerParent; i3++) {
            if (!arraySet.contains(Integer.valueOf(i3))) {
                return i3;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasProfile(int i) {
        synchronized (this.mUsersLock) {
            UserInfo userInfoLU = getUserInfoLU(i);
            int size = this.mUsers.size();
            for (int i2 = 0; i2 < size; i2++) {
                UserInfo userInfo = this.mUsers.valueAt(i2).info;
                if (i != userInfo.id && isProfileOf(userInfoLU, userInfo)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void verifyCallingPackage(String str, int i) {
        if (this.mPm.snapshotComputer().getPackageUid(str, 0L, UserHandle.getUserId(i)) == i) {
            return;
        }
        throw new SecurityException("Specified package " + str + " does not match the calling uid " + i);
    }

    private PackageManagerInternal getPackageManagerInternal() {
        if (this.mPmInternal == null) {
            this.mPmInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        }
        return this.mPmInternal;
    }

    private DevicePolicyManagerInternal getDevicePolicyManagerInternal() {
        if (this.mDevicePolicyManagerInternal == null) {
            this.mDevicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        }
        return this.mDevicePolicyManagerInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ActivityManagerInternal getActivityManagerInternal() {
        if (this.mAmInternal == null) {
            this.mAmInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        }
        return this.mAmInternal;
    }

    private boolean isNonRemovableMainUser(UserInfo userInfo) {
        return userInfo.isMain() && isMainUserPermanentAdmin();
    }

    public boolean isMainUserPermanentAdmin() {
        return Resources.getSystem().getBoolean(17891723);
    }

    public boolean canSwitchToHeadlessSystemUser() {
        return Resources.getSystem().getBoolean(R.bool.config_disableMenuKeyInLockScreen);
    }

    public IUserManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class UserManagerServiceWrapper implements IUserManagerServiceWrapper {
        private UserManagerServiceWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IUserManagerServiceExt getExtImpl() {
            return UserManagerService.this.mUserManagerServiceExt;
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public void setUserRestriction(String str, boolean z, int i) {
            UserManagerService.this.setUserRestriction(str, z, i);
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public void writeUserLP(UserData userData) {
            UserManagerService.this.writeUserLP(userData);
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public void writeUserListLP() {
            UserManagerService.this.writeUserListLP();
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public Object getUsersLock() {
            return UserManagerService.this.mUsersLock;
        }
    }

    public UserJourneyLogger getUserJourneyLogger() {
        return this.mUserJourneyLogger;
    }
}
