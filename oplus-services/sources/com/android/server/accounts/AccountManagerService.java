package com.android.server.accounts;

import android.R;
import android.accounts.Account;
import android.accounts.AccountAndUser;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.AccountManagerInternal;
import android.accounts.AccountManagerResponse;
import android.accounts.AuthenticatorDescription;
import android.accounts.CantAddAccountActivity;
import android.accounts.ChooseAccountActivity;
import android.accounts.GrantCredentialsPermissionActivity;
import android.accounts.IAccountAuthenticator;
import android.accounts.IAccountAuthenticatorResponse;
import android.accounts.IAccountManager;
import android.accounts.IAccountManagerResponse;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManager;
import android.app.admin.DevicePolicyManagerInternal;
import android.app.compat.CompatChanges;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.RegisteredServicesCache;
import android.content.pm.RegisteredServicesCacheListener;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.pm.UserInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteStatement;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.PackageMonitor;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.server.LocalServices;
import com.android.server.ServiceThread;
import com.android.server.SystemService;
import com.android.server.accounts.TokenCache;
import com.android.server.autofill.HintsHelper;
import com.google.android.collect.Lists;
import com.google.android.collect.Sets;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AccountManagerService extends IAccountManager.Stub implements RegisteredServicesCacheListener<AuthenticatorDescription> {
    private static final Intent ACCOUNTS_CHANGED_INTENT;
    private static final Bundle ACCOUNTS_CHANGED_OPTIONS = new BroadcastOptions().setDeliveryGroupPolicy(1).toBundle();
    private static final Account[] EMPTY_ACCOUNT_ARRAY;
    private static final long ENFORCE_PACKAGE_VISIBILITY_FILTERING = 154726397;
    private static final int MESSAGE_COPY_SHARED_ACCOUNT = 4;
    private static final int MESSAGE_TIMED_OUT = 3;
    private static final String PRE_N_DATABASE_NAME = "accounts.db";
    private static final int SIGNATURE_CHECK_MATCH = 1;
    private static final int SIGNATURE_CHECK_MISMATCH = 0;
    private static final int SIGNATURE_CHECK_UID_MATCH = 2;
    private static final String TAG = "AccountManagerService";
    private static final int TIMEOUT_DELAY_MS = 900000;
    private static AtomicReference<AccountManagerService> sThis;
    private final AppOpsManager mAppOpsManager;
    private final IAccountAuthenticatorCache mAuthenticatorCache;
    final Context mContext;
    final MessageHandler mHandler;
    private final Injector mInjector;
    private final PackageManager mPackageManager;
    private UserManager mUserManager;
    public IAccountManagerServiceExt mAccountManagerServiceExt = (IAccountManagerServiceExt) ExtLoader.type(IAccountManagerServiceExt.class).base(this).create();
    private final LinkedHashMap<String, Session> mSessions = new LinkedHashMap<>();
    private final SparseArray<UserAccounts> mUsers = new SparseArray<>();
    private final SparseBooleanArray mLocalUnlockedUsers = new SparseBooleanArray();
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private CopyOnWriteArrayList<AccountManagerInternal.OnAppPermissionChangeListener> mAppPermissionChangeListeners = new CopyOnWriteArrayList<>();

    private boolean isVisible(int i) {
        return i == 1 || i == 2;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Lifecycle extends SystemService {
        private AccountManagerService mService;

        public Lifecycle(Context context) {
            super(context);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.accounts.AccountManagerService, android.os.IBinder] */
        @Override // com.android.server.SystemService
        public void onStart() {
            ?? accountManagerService = new AccountManagerService(new Injector(getContext()));
            this.mService = accountManagerService;
            publishBinderService("account", accountManagerService);
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(SystemService.TargetUser targetUser) {
            this.mService.onUnlockUser(targetUser.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(SystemService.TargetUser targetUser) {
            Slog.i(AccountManagerService.TAG, "onUserStopped " + targetUser);
            this.mService.purgeUserData(targetUser.getUserIdentifier());
        }
    }

    static {
        Intent intent = new Intent("android.accounts.LOGIN_ACCOUNTS_CHANGED");
        ACCOUNTS_CHANGED_INTENT = intent;
        intent.setFlags(AudioFormat.HE_AAC_V1);
        sThis = new AtomicReference<>();
        EMPTY_ACCOUNT_ARRAY = new Account[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class UserAccounts {
        final HashMap<String, Account[]> accountCache;
        private final TokenCache accountTokenCaches;
        final AccountsDb accountsDb;
        private final Map<Account, Map<String, String>> authTokenCache;
        final Object cacheLock;
        final Object dbLock;
        private final Map<String, Map<String, Integer>> mReceiversForType;
        private final HashMap<Account, AtomicReference<String>> previousNameCache;
        private final Map<Account, Map<String, String>> userDataCache;
        private final int userId;
        private final Map<Account, Map<String, Integer>> visibilityCache;
        private final HashMap<Pair<Pair<Account, String>, Integer>, NotificationId> credentialsPermissionNotificationIds = new HashMap<>();
        private final HashMap<Account, NotificationId> signinRequiredNotificationIds = new HashMap<>();

        UserAccounts(Context context, int i, File file, File file2) {
            Object obj = new Object();
            this.cacheLock = obj;
            Object obj2 = new Object();
            this.dbLock = obj2;
            this.accountCache = new LinkedHashMap();
            this.userDataCache = new HashMap();
            this.authTokenCache = new HashMap();
            this.accountTokenCaches = new TokenCache();
            this.visibilityCache = new HashMap();
            this.mReceiversForType = new HashMap();
            this.previousNameCache = new HashMap<>();
            this.userId = i;
            synchronized (obj2) {
                synchronized (obj) {
                    this.accountsDb = AccountsDb.create(context, i, file, file2);
                }
            }
        }
    }

    public static AccountManagerService getSingleton() {
        return sThis.get();
    }

    public AccountManagerService(Injector injector) {
        this.mInjector = injector;
        Context context = injector.getContext();
        this.mContext = context;
        PackageManager packageManager = context.getPackageManager();
        this.mPackageManager = packageManager;
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mAppOpsManager = appOpsManager;
        MessageHandler messageHandler = new MessageHandler(injector.getMessageHandlerLooper());
        this.mHandler = messageHandler;
        IAccountAuthenticatorCache accountAuthenticatorCache = injector.getAccountAuthenticatorCache();
        this.mAuthenticatorCache = accountAuthenticatorCache;
        accountAuthenticatorCache.setListener(this, messageHandler);
        sThis.set(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        context.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.accounts.AccountManagerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                    return;
                }
                final String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
                AccountManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.accounts.AccountManagerService.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountManagerService.this.purgeOldGrantsAll();
                        AccountManagerService.this.removeVisibilityValuesForPackage(schemeSpecificPart);
                    }
                });
            }
        }, intentFilter);
        injector.addLocalService(new AccountManagerInternalImpl());
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_REMOVED");
        context.registerReceiverAsUser(new BroadcastReceiver() { // from class: com.android.server.accounts.AccountManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                int intExtra;
                if (!"android.intent.action.USER_REMOVED".equals(intent.getAction()) || (intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1)) < 1) {
                    return;
                }
                Slog.i(AccountManagerService.TAG, "User " + intExtra + " removed");
                AccountManagerService.this.purgeUserData(intExtra);
            }
        }, UserHandle.ALL, intentFilter2, null, null);
        new PackageMonitor() { // from class: com.android.server.accounts.AccountManagerService.3
            public void onPackageAdded(String str, int i) {
                AccountManagerService.this.cancelAccountAccessRequestNotificationIfNeeded(i, true);
            }

            public void onPackageUpdateFinished(String str, int i) {
                AccountManagerService.this.cancelAccountAccessRequestNotificationIfNeeded(i, true);
            }
        }.getWrapper().getExtImpl().register(context, messageHandler.getLooper(), UserHandle.ALL, true, new int[]{2, 3});
        appOpsManager.startWatchingMode(62, (String) null, (AppOpsManager.OnOpChangedListener) new AppOpsManager.OnOpChangedInternalListener() { // from class: com.android.server.accounts.AccountManagerService.4
            public void onOpChanged(int i, String str) {
                try {
                    int packageUidAsUser = AccountManagerService.this.mPackageManager.getPackageUidAsUser(str, ActivityManager.getCurrentUser());
                    if (AccountManagerService.this.mAppOpsManager.checkOpNoThrow(62, packageUidAsUser, str) == 0) {
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            AccountManagerService.this.cancelAccountAccessRequestNotificationIfNeeded(str, packageUidAsUser, true);
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                        } catch (Throwable th) {
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            throw th;
                        }
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
        });
        packageManager.addOnPermissionsChangeListener(new PackageManager.OnPermissionsChangedListener() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda4
            public final void onPermissionsChanged(int i) {
                AccountManagerService.this.lambda$new$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        AccountManager.invalidateLocalAccountsDataCaches();
        String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
        if (packagesForUid != null) {
            int userId = UserHandle.getUserId(i);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    Account[] accountArr = null;
                    for (String str : packagesForUid) {
                        if (this.mPackageManager.checkPermission("android.permission.GET_ACCOUNTS", str) == 0) {
                            if (accountArr == null) {
                                accountArr = getAccountsAsUser(null, userId, "android");
                                if (ArrayUtils.isEmpty(accountArr)) {
                                    return;
                                }
                            }
                            for (Account account : accountArr) {
                                cancelAccountAccessRequestNotificationIfNeeded(account, i, str, true);
                            }
                        }
                    }
                } catch (SQLiteCantOpenDatabaseException e) {
                    Slog.e(TAG, "trying to query account db that already been deleted: ", e);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getBindInstantServiceAllowed(int i) {
        return this.mAuthenticatorCache.getBindInstantServiceAllowed(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBindInstantServiceAllowed(int i, boolean z) {
        this.mAuthenticatorCache.setBindInstantServiceAllowed(i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAccountAccessRequestNotificationIfNeeded(int i, boolean z) {
        for (Account account : getAccountsAsUser(null, UserHandle.getUserId(i), "android")) {
            cancelAccountAccessRequestNotificationIfNeeded(account, i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAccountAccessRequestNotificationIfNeeded(String str, int i, boolean z) {
        for (Account account : getAccountsAsUser(null, UserHandle.getUserId(i), "android")) {
            cancelAccountAccessRequestNotificationIfNeeded(account, i, str, z);
        }
    }

    private void cancelAccountAccessRequestNotificationIfNeeded(Account account, int i, boolean z) {
        String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
        if (packagesForUid != null) {
            for (String str : packagesForUid) {
                cancelAccountAccessRequestNotificationIfNeeded(account, i, str, z);
            }
        }
    }

    private void cancelAccountAccessRequestNotificationIfNeeded(Account account, int i, String str, boolean z) {
        if (!z || hasAccountAccess(account, str, UserHandle.getUserHandleForUid(i))) {
            cancelNotification(getCredentialPermissionNotificationId(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", i), UserHandle.getUserHandleForUid(i));
        }
    }

    public boolean addAccountExplicitlyWithVisibility(Account account, String str, Bundle bundle, Map map, String str2) {
        Bundle.setDefusable(bundle, true);
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "addAccountExplicitly: " + account + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        Objects.requireNonNull(account, "account cannot be null");
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot explicitly add accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return addAccountInternal(getUserAccounts(callingUserId), account, str, bundle, callingUid, map, str2);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public Map<Account, Integer> getAccountsAndVisibilityForPackage(String str, String str2) {
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        boolean isSameApp = UserHandle.isSameApp(callingUid, 1000);
        List<String> typesForCaller = getTypesForCaller(callingUid, callingUserId, isSameApp);
        if ((str2 != null && !typesForCaller.contains(str2)) || (str2 == null && !isSameApp)) {
            throw new SecurityException("getAccountsAndVisibilityForPackage() called from unauthorized uid " + callingUid + " with packageName=" + str);
        }
        if (str2 != null) {
            typesForCaller = new ArrayList<>();
            typesForCaller.add(str2);
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return getAccountsAndVisibilityForPackage(str, typesForCaller, Integer.valueOf(callingUid), getUserAccounts(callingUserId));
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private Map<Account, Integer> getAccountsAndVisibilityForPackage(String str, List<String> list, Integer num, UserAccounts userAccounts) {
        if (!canCallerAccessPackage(str, num.intValue(), userAccounts.userId)) {
            Log.w(TAG, "getAccountsAndVisibilityForPackage#Package not found " + str);
            return new LinkedHashMap();
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (String str2 : list) {
            synchronized (userAccounts.dbLock) {
                synchronized (userAccounts.cacheLock) {
                    Account[] accountArr = userAccounts.accountCache.get(str2);
                    if (accountArr != null) {
                        for (Account account : accountArr) {
                            linkedHashMap.put(account, resolveAccountVisibility(account, str, userAccounts));
                        }
                    }
                }
            }
        }
        return filterSharedAccounts(userAccounts, linkedHashMap, num.intValue(), str);
    }

    public Map<String, Integer> getPackagesAndVisibilityForAccount(Account account) {
        Map<String, Integer> packagesAndVisibilityForAccountLocked;
        Objects.requireNonNull(account, "account cannot be null");
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId) && !isSystemUid(callingUid)) {
            throw new SecurityException(String.format("uid %s cannot get secrets for account %s", Integer.valueOf(callingUid), account));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(callingUserId);
            synchronized (userAccounts.dbLock) {
                synchronized (userAccounts.cacheLock) {
                    packagesAndVisibilityForAccountLocked = getPackagesAndVisibilityForAccountLocked(account, userAccounts);
                }
            }
            return packagesAndVisibilityForAccountLocked;
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private Map<String, Integer> getPackagesAndVisibilityForAccountLocked(Account account, UserAccounts userAccounts) {
        Map<String, Integer> map = (Map) userAccounts.visibilityCache.get(account);
        if (map != null) {
            return map;
        }
        Log.d(TAG, "Visibility was not initialized");
        HashMap hashMap = new HashMap();
        userAccounts.visibilityCache.put(account, hashMap);
        AccountManager.invalidateLocalAccountsDataCaches();
        return hashMap;
    }

    public int getAccountVisibility(Account account, String str) {
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(str, "packageName cannot be null");
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId) && !isSystemUid(callingUid)) {
            throw new SecurityException(String.format("uid %s cannot get secrets for accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(callingUserId);
            if ("android:accounts:key_legacy_visible".equals(str)) {
                int accountVisibilityFromCache = getAccountVisibilityFromCache(account, str, userAccounts);
                if (accountVisibilityFromCache != 0) {
                    return accountVisibilityFromCache;
                }
                IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
                return 2;
            }
            if (!"android:accounts:key_legacy_not_visible".equals(str)) {
                if (canCallerAccessPackage(str, callingUid, userAccounts.userId)) {
                    return resolveAccountVisibility(account, str, userAccounts).intValue();
                }
                IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
                return 3;
            }
            int accountVisibilityFromCache2 = getAccountVisibilityFromCache(account, str, userAccounts);
            if (accountVisibilityFromCache2 != 0) {
                return accountVisibilityFromCache2;
            }
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
            return 4;
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private int getAccountVisibilityFromCache(Account account, String str, UserAccounts userAccounts) {
        int intValue;
        synchronized (userAccounts.cacheLock) {
            Integer num = getPackagesAndVisibilityForAccountLocked(account, userAccounts).get(str);
            intValue = num != null ? num.intValue() : 0;
        }
        return intValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Integer resolveAccountVisibility(Account account, String str, UserAccounts userAccounts) {
        Objects.requireNonNull(str, "packageName cannot be null");
        try {
            long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
            try {
                int packageUidAsUser = this.mPackageManager.getPackageUidAsUser(str, userAccounts.userId);
                if (UserHandle.isSameApp(packageUidAsUser, 1000)) {
                    return 1;
                }
                int checkPackageSignature = checkPackageSignature(account.type, packageUidAsUser, userAccounts.userId);
                int i = 2;
                if (checkPackageSignature == 2) {
                    return 1;
                }
                int accountVisibilityFromCache = getAccountVisibilityFromCache(account, str, userAccounts);
                if (accountVisibilityFromCache != 0) {
                    return Integer.valueOf(accountVisibilityFromCache);
                }
                boolean isPermittedForPackage = isPermittedForPackage(str, userAccounts.userId, "android.permission.GET_ACCOUNTS_PRIVILEGED");
                if (isProfileOwner(packageUidAsUser)) {
                    return 1;
                }
                boolean isPreOApplication = isPreOApplication(str);
                if (checkPackageSignature != 0 || ((isPreOApplication && checkGetAccountsPermission(str, userAccounts.userId)) || ((checkReadContactsPermission(str, userAccounts.userId) && accountTypeManagesContacts(account.type, userAccounts.userId)) || isPermittedForPackage))) {
                    int accountVisibilityFromCache2 = getAccountVisibilityFromCache(account, "android:accounts:key_legacy_visible", userAccounts);
                    if (accountVisibilityFromCache2 != 0) {
                        i = accountVisibilityFromCache2;
                    }
                } else {
                    i = getAccountVisibilityFromCache(account, "android:accounts:key_legacy_not_visible", userAccounts);
                    if (i == 0) {
                        i = 4;
                    }
                }
                return Integer.valueOf(i);
            } finally {
                IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "resolveAccountVisibility#Package not found " + e.getMessage());
            return 3;
        }
    }

    private boolean isPreOApplication(String str) {
        try {
            long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
            try {
                ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(str, 0);
                if (applicationInfo != null && applicationInfo.targetSdkVersion >= 26) {
                    return false;
                }
                return true;
            } finally {
                IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "isPreOApplication#Package not found " + e.getMessage());
            return true;
        }
    }

    public boolean setAccountVisibility(Account account, String str, int i) {
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(str, "packageName cannot be null");
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId) && !isSystemUid(callingUid)) {
            throw new SecurityException(String.format("uid %s cannot get secrets for accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return setAccountVisibility(account, str, i, true, getUserAccounts(callingUserId), callingUid);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean setAccountVisibility(Account account, String str, int i, boolean z, UserAccounts userAccounts, int i2) {
        Map<String, Integer> emptyMap;
        List<String> emptyList;
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                if (z) {
                    if (isSpecialPackageKey(str)) {
                        emptyMap = getRequestingPackages(account, userAccounts);
                        emptyList = getAccountRemovedReceivers(account, userAccounts);
                    } else {
                        if (!canCallerAccessPackage(str, i2, userAccounts.userId)) {
                            return false;
                        }
                        emptyMap = new HashMap<>();
                        emptyMap.put(str, resolveAccountVisibility(account, str, userAccounts));
                        emptyList = new ArrayList<>();
                        if (shouldNotifyPackageOnAccountRemoval(account, str, userAccounts)) {
                            emptyList.add(str);
                        }
                    }
                } else {
                    if (!isSpecialPackageKey(str) && !canCallerAccessPackage(str, i2, userAccounts.userId)) {
                        return false;
                    }
                    emptyMap = Collections.emptyMap();
                    emptyList = Collections.emptyList();
                }
                if (!updateAccountVisibilityLocked(account, str, i, userAccounts)) {
                    return false;
                }
                if (z) {
                    Log.i(TAG, "Notifying visibility changed for package=" + str);
                    for (Map.Entry<String, Integer> entry : emptyMap.entrySet()) {
                        if (isVisible(entry.getValue().intValue()) != isVisible(resolveAccountVisibility(account, str, userAccounts).intValue())) {
                            notifyPackage(entry.getKey(), userAccounts);
                        }
                    }
                    Iterator<String> it = emptyList.iterator();
                    while (it.hasNext()) {
                        sendAccountRemovedBroadcast(account, it.next(), userAccounts.userId, "setAccountVisibility");
                    }
                    sendAccountsChangedBroadcast(userAccounts.userId, account.type, "setAccountVisibility");
                }
                return true;
            }
        }
    }

    private boolean updateAccountVisibilityLocked(Account account, String str, int i, UserAccounts userAccounts) {
        long findDeAccountId = userAccounts.accountsDb.findDeAccountId(account);
        if (findDeAccountId < 0) {
            return false;
        }
        StrictMode.ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        try {
            if (!userAccounts.accountsDb.setAccountVisibility(findDeAccountId, str, i)) {
                return false;
            }
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
            getPackagesAndVisibilityForAccountLocked(account, userAccounts).put(str, Integer.valueOf(i));
            AccountManager.invalidateLocalAccountsDataCaches();
            return true;
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
        }
    }

    public void registerAccountListener(String[] strArr, String str) {
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            registerAccountListener(strArr, str, getUserAccounts(callingUserId));
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void registerAccountListener(String[] strArr, String str, UserAccounts userAccounts) {
        synchronized (userAccounts.mReceiversForType) {
            if (strArr == null) {
                strArr = new String[]{null};
            }
            for (String str2 : strArr) {
                Map map = (Map) userAccounts.mReceiversForType.get(str2);
                if (map == null) {
                    map = new HashMap();
                    userAccounts.mReceiversForType.put(str2, map);
                }
                Integer num = (Integer) map.get(str);
                int i = 1;
                if (num != null) {
                    i = 1 + num.intValue();
                }
                map.put(str, Integer.valueOf(i));
            }
        }
    }

    public void unregisterAccountListener(String[] strArr, String str) {
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            unregisterAccountListener(strArr, str, getUserAccounts(callingUserId));
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void unregisterAccountListener(String[] strArr, String str, UserAccounts userAccounts) {
        synchronized (userAccounts.mReceiversForType) {
            if (strArr == null) {
                strArr = new String[]{null};
            }
            for (String str2 : strArr) {
                Map map = (Map) userAccounts.mReceiversForType.get(str2);
                if (map == null || map.get(str) == null) {
                    throw new IllegalArgumentException("attempt to unregister wrong receiver");
                }
                Integer num = (Integer) map.get(str);
                if (num.intValue() == 1) {
                    map.remove(str);
                } else {
                    map.put(str, Integer.valueOf(num.intValue() - 1));
                }
            }
        }
    }

    private void sendNotificationAccountUpdated(Account account, UserAccounts userAccounts) {
        for (Map.Entry<String, Integer> entry : getRequestingPackages(account, userAccounts).entrySet()) {
            if (entry.getValue().intValue() != 3 && entry.getValue().intValue() != 4) {
                notifyPackage(entry.getKey(), userAccounts);
            }
        }
    }

    private void notifyPackage(String str, UserAccounts userAccounts) {
        Log.i(TAG, "notifying package=" + str + " for userId=" + userAccounts.userId + ", sending broadcast of android.accounts.action.VISIBLE_ACCOUNTS_CHANGED");
        Intent intent = new Intent("android.accounts.action.VISIBLE_ACCOUNTS_CHANGED");
        intent.setPackage(str);
        intent.setFlags(1073741824);
        this.mContext.sendBroadcastAsUser(intent, new UserHandle(userAccounts.userId));
    }

    private Map<String, Integer> getRequestingPackages(Account account, UserAccounts userAccounts) {
        HashSet<String> hashSet = new HashSet();
        synchronized (userAccounts.mReceiversForType) {
            String[] strArr = {account.type, null};
            for (int i = 0; i < 2; i++) {
                Map map = (Map) userAccounts.mReceiversForType.get(strArr[i]);
                if (map != null) {
                    hashSet.addAll(map.keySet());
                }
            }
        }
        HashMap hashMap = new HashMap();
        for (String str : hashSet) {
            hashMap.put(str, resolveAccountVisibility(account, str, userAccounts));
        }
        return hashMap;
    }

    private List<String> getAccountRemovedReceivers(Account account, UserAccounts userAccounts) {
        Intent intent = new Intent("android.accounts.action.ACCOUNT_REMOVED");
        intent.setFlags(16777216);
        List queryBroadcastReceiversAsUser = this.mPackageManager.queryBroadcastReceiversAsUser(intent, 0, userAccounts.userId);
        ArrayList arrayList = new ArrayList();
        if (queryBroadcastReceiversAsUser == null) {
            return arrayList;
        }
        Iterator it = queryBroadcastReceiversAsUser.iterator();
        while (it.hasNext()) {
            String str = ((ResolveInfo) it.next()).activityInfo.applicationInfo.packageName;
            int intValue = resolveAccountVisibility(account, str, userAccounts).intValue();
            if (intValue == 1 || intValue == 2) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    private boolean shouldNotifyPackageOnAccountRemoval(Account account, String str, UserAccounts userAccounts) {
        int intValue = resolveAccountVisibility(account, str, userAccounts).intValue();
        if (intValue != 1 && intValue != 2) {
            return false;
        }
        Intent intent = new Intent("android.accounts.action.ACCOUNT_REMOVED");
        intent.setFlags(16777216);
        intent.setPackage(str);
        List queryBroadcastReceiversAsUser = this.mPackageManager.queryBroadcastReceiversAsUser(intent, 0, userAccounts.userId);
        return queryBroadcastReceiversAsUser != null && queryBroadcastReceiversAsUser.size() > 0;
    }

    private boolean isSpecialPackageKey(String str) {
        return "android:accounts:key_legacy_visible".equals(str) || "android:accounts:key_legacy_not_visible".equals(str);
    }

    private void sendAccountsChangedBroadcast(int i, String str, String str2) {
        Objects.requireNonNull(str2, "useCase can't be null");
        StringBuilder sb = new StringBuilder();
        sb.append("the accountType= ");
        if (str == null) {
            str = "";
        }
        sb.append(str);
        sb.append(" changed with useCase=");
        sb.append(str2);
        sb.append(" for userId=");
        sb.append(i);
        sb.append(", sending broadcast of ");
        Intent intent = ACCOUNTS_CHANGED_INTENT;
        sb.append(intent.getAction());
        Log.i(TAG, sb.toString());
        this.mContext.sendBroadcastAsUser(intent, new UserHandle(i), null, ACCOUNTS_CHANGED_OPTIONS);
    }

    private void sendAccountRemovedBroadcast(Account account, String str, int i, String str2) {
        Objects.requireNonNull(str2, "useCase can't be null");
        Log.i(TAG, "the account with type=" + account.type + " removed while useCase=" + str2 + " for userId=" + i + ", sending broadcast of android.accounts.action.ACCOUNT_REMOVED");
        Intent intent = new Intent("android.accounts.action.ACCOUNT_REMOVED");
        intent.setFlags(16777216);
        intent.setPackage(str);
        intent.putExtra("authAccount", account.name);
        intent.putExtra("accountType", account.type);
        this.mContext.sendBroadcastAsUser(intent, new UserHandle(i));
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        try {
            return super.onTransact(i, parcel, parcel2, i2);
        } catch (RuntimeException e) {
            if (!(e instanceof SecurityException) && !(e instanceof IllegalArgumentException)) {
                Slog.wtf(TAG, "Account Manager Crash", e);
            }
            throw e;
        }
    }

    private UserManager getUserManager() {
        if (this.mUserManager == null) {
            this.mUserManager = UserManager.get(this.mContext);
        }
        return this.mUserManager;
    }

    public void validateAccounts(int i) {
        validateAccountsInternal(getUserAccounts(i), true);
    }

    private void validateAccountsInternal(UserAccounts userAccounts, boolean z) {
        boolean z2;
        Iterator<Map.Entry<Long, Account>> it;
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "validateAccountsInternal " + userAccounts.userId + " isCeDatabaseAttached=" + userAccounts.accountsDb.isCeDatabaseAttached() + " userLocked=" + this.mLocalUnlockedUsers.get(userAccounts.userId));
        }
        if (z) {
            this.mAuthenticatorCache.invalidateCache(userAccounts.userId);
        }
        HashMap<String, Integer> authenticatorTypeAndUIDForUser = getAuthenticatorTypeAndUIDForUser(this.mAuthenticatorCache, userAccounts.userId);
        boolean isLocalUnlockedUser = isLocalUnlockedUser(userAccounts.userId);
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                AccountsDb accountsDb = userAccounts.accountsDb;
                Map<String, Integer> findMetaAuthUid = accountsDb.findMetaAuthUid();
                HashSet newHashSet = Sets.newHashSet();
                SparseBooleanArray sparseBooleanArray = null;
                for (Map.Entry<String, Integer> entry : findMetaAuthUid.entrySet()) {
                    String key = entry.getKey();
                    int intValue = entry.getValue().intValue();
                    Integer num = authenticatorTypeAndUIDForUser.get(key);
                    if (num != null && intValue == num.intValue()) {
                        authenticatorTypeAndUIDForUser.remove(key);
                    } else {
                        if (sparseBooleanArray == null) {
                            sparseBooleanArray = getUidsOfInstalledOrUpdatedPackagesAsUser(userAccounts.userId);
                        }
                        if (!sparseBooleanArray.get(intValue)) {
                            newHashSet.add(key);
                            accountsDb.deleteMetaByAuthTypeAndUid(key, intValue);
                        }
                    }
                }
                for (Map.Entry<String, Integer> entry2 : authenticatorTypeAndUIDForUser.entrySet()) {
                    accountsDb.insertOrReplaceMetaAuthTypeAndUid(entry2.getKey(), entry2.getValue().intValue());
                }
                Map<Long, Account> findAllDeAccounts = accountsDb.findAllDeAccounts();
                try {
                    userAccounts.accountCache.clear();
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    Iterator<Map.Entry<Long, Account>> it2 = findAllDeAccounts.entrySet().iterator();
                    boolean z3 = false;
                    while (it2.hasNext()) {
                        try {
                            Map.Entry<Long, Account> next = it2.next();
                            long longValue = next.getKey().longValue();
                            Account value = next.getValue();
                            if (newHashSet.contains(value.type)) {
                                Slog.w(TAG, "deleting account " + value.toSafeString() + " because type " + value.type + "'s registered authenticator no longer exist.");
                                Map<String, Integer> requestingPackages = getRequestingPackages(value, userAccounts);
                                List<String> accountRemovedReceivers = getAccountRemovedReceivers(value, userAccounts);
                                accountsDb.beginTransaction();
                                try {
                                    accountsDb.deleteDeAccount(longValue);
                                    if (isLocalUnlockedUser) {
                                        accountsDb.deleteCeAccount(longValue);
                                    }
                                    accountsDb.setTransactionSuccessful();
                                    try {
                                        Log.i(TAG, "validateAccountsInternal#Deleted UserId=" + userAccounts.userId + ", AccountId=" + longValue);
                                        it = it2;
                                        logRecord(AccountsDb.DEBUG_ACTION_AUTHENTICATOR_REMOVE, "accounts", longValue, userAccounts);
                                        userAccounts.userDataCache.remove(value);
                                        userAccounts.authTokenCache.remove(value);
                                        userAccounts.accountTokenCaches.remove(value);
                                        userAccounts.visibilityCache.remove(value);
                                        for (Map.Entry<String, Integer> entry3 : requestingPackages.entrySet()) {
                                            if (isVisible(entry3.getValue().intValue())) {
                                                notifyPackage(entry3.getKey(), userAccounts);
                                            }
                                        }
                                        Iterator<String> it3 = accountRemovedReceivers.iterator();
                                        while (it3.hasNext()) {
                                            sendAccountRemovedBroadcast(value, it3.next(), userAccounts.userId, "validateAccounts");
                                        }
                                        z3 = true;
                                    } catch (Throwable th) {
                                        th = th;
                                        z2 = true;
                                        if (z2) {
                                            sendAccountsChangedBroadcast(userAccounts.userId, "ambiguous", "validateAccounts");
                                        }
                                        throw th;
                                    }
                                } finally {
                                    accountsDb.endTransaction();
                                }
                            } else {
                                it = it2;
                                ArrayList arrayList = (ArrayList) linkedHashMap.get(value.type);
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                    linkedHashMap.put(value.type, arrayList);
                                }
                                arrayList.add(value.name);
                            }
                            it2 = it;
                        } catch (Throwable th2) {
                            th = th2;
                            z2 = z3;
                        }
                    }
                    for (Map.Entry entry4 : linkedHashMap.entrySet()) {
                        String str = (String) entry4.getKey();
                        ArrayList arrayList2 = (ArrayList) entry4.getValue();
                        int size = arrayList2.size();
                        Account[] accountArr = new Account[size];
                        for (int i = 0; i < size; i++) {
                            accountArr[i] = new Account((String) arrayList2.get(i), str, UUID.randomUUID().toString());
                        }
                        userAccounts.accountCache.put(str, accountArr);
                    }
                    userAccounts.visibilityCache.putAll(accountsDb.findAllVisibilityValues());
                    AccountManager.invalidateLocalAccountsDataCaches();
                    if (z3) {
                        sendAccountsChangedBroadcast(userAccounts.userId, "ambiguous", "validateAccounts");
                    }
                } catch (Throwable th3) {
                    th = th3;
                    z2 = false;
                }
            }
        }
    }

    private SparseBooleanArray getUidsOfInstalledOrUpdatedPackagesAsUser(int i) {
        List installedPackagesAsUser = this.mPackageManager.getInstalledPackagesAsUser(8192, i);
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray(installedPackagesAsUser.size());
        Iterator it = installedPackagesAsUser.iterator();
        while (it.hasNext()) {
            ApplicationInfo applicationInfo = ((PackageInfo) it.next()).applicationInfo;
            if (applicationInfo != null && (applicationInfo.flags & AudioDevice.OUT_IP) != 0) {
                sparseBooleanArray.put(applicationInfo.uid, true);
            }
        }
        return sparseBooleanArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HashMap<String, Integer> getAuthenticatorTypeAndUIDForUser(Context context, int i) {
        return getAuthenticatorTypeAndUIDForUser(new AccountAuthenticatorCache(context), i);
    }

    private static HashMap<String, Integer> getAuthenticatorTypeAndUIDForUser(IAccountAuthenticatorCache iAccountAuthenticatorCache, int i) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (RegisteredServicesCache.ServiceInfo<AuthenticatorDescription> serviceInfo : iAccountAuthenticatorCache.getAllServices(i)) {
            linkedHashMap.put(((AuthenticatorDescription) serviceInfo.type).type, Integer.valueOf(serviceInfo.uid));
        }
        return linkedHashMap;
    }

    private UserAccounts getUserAccountsForCaller() {
        return getUserAccounts(UserHandle.getCallingUserId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UserAccounts getUserAccounts(int i) {
        try {
            return getUserAccountsNotChecked(i);
        } catch (RuntimeException e) {
            if (!this.mPackageManager.hasSystemFeature("android.hardware.type.automotive")) {
                throw e;
            }
            Slog.wtf(TAG, "Removing user " + i + " due to exception (" + e + ") reading its account database");
            if (i == ActivityManager.getCurrentUser() && i != 0) {
                Slog.i(TAG, "Switching to system user first");
                try {
                    ActivityManager.getService().switchUser(0);
                } catch (RemoteException e2) {
                    Slog.e(TAG, "Could not switch to 0: " + e2);
                }
            }
            if (!this.getUserManager().removeUserEvenWhenDisallowed(i)) {
                Slog.e(TAG, "could not remove user " + i);
                throw e;
            }
            throw e;
        }
    }

    private UserAccounts getUserAccountsNotChecked(int i) {
        UserAccounts userAccounts;
        boolean z;
        synchronized (this.mUsers) {
            userAccounts = this.mUsers.get(i);
            if (userAccounts == null) {
                UserAccounts userAccounts2 = new UserAccounts(this.mContext, i, new File(this.mInjector.getPreNDatabaseName(i)), new File(this.mInjector.getDeDatabaseName(i)));
                this.mUsers.append(i, userAccounts2);
                purgeOldGrants(userAccounts2);
                AccountManager.invalidateLocalAccountsDataCaches();
                z = true;
                userAccounts = userAccounts2;
            } else {
                z = false;
            }
            if (!userAccounts.accountsDb.isCeDatabaseAttached() && this.mLocalUnlockedUsers.get(i)) {
                Log.i(TAG, "User " + i + " is unlocked - opening CE database");
                synchronized (userAccounts.dbLock) {
                    synchronized (userAccounts.cacheLock) {
                        userAccounts.accountsDb.attachCeDatabase(new File(this.mInjector.getCeDatabaseName(i)));
                    }
                }
                syncDeCeAccountsLocked(userAccounts);
            }
            if (z) {
                validateAccountsInternal(userAccounts, true);
            }
        }
        return userAccounts;
    }

    private void syncDeCeAccountsLocked(UserAccounts userAccounts) {
        Preconditions.checkState(Thread.holdsLock(this.mUsers), "mUsers lock must be held");
        List<Account> findCeAccountsNotInDe = userAccounts.accountsDb.findCeAccountsNotInDe();
        if (findCeAccountsNotInDe.isEmpty()) {
            return;
        }
        Slog.i(TAG, findCeAccountsNotInDe.size() + " accounts were previously deleted while user " + userAccounts.userId + " was locked. Removing accounts from CE tables");
        logRecord(userAccounts, AccountsDb.DEBUG_ACTION_SYNC_DE_CE_ACCOUNTS, "accounts");
        Iterator<Account> it = findCeAccountsNotInDe.iterator();
        while (it.hasNext()) {
            removeAccountInternal(userAccounts, it.next(), 1000);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void purgeOldGrantsAll() {
        synchronized (this.mUsers) {
            for (int i = 0; i < this.mUsers.size(); i++) {
                purgeOldGrants(this.mUsers.valueAt(i));
            }
        }
    }

    private void purgeOldGrants(UserAccounts userAccounts) {
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                Iterator<Integer> it = userAccounts.accountsDb.findAllUidGrants().iterator();
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    if (!(this.mPackageManager.getPackagesForUid(intValue) != null)) {
                        Log.d(TAG, "deleting grants for UID " + intValue + " because its package is no longer installed");
                        userAccounts.accountsDb.deleteGrantsByUid(intValue);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeVisibilityValuesForPackage(String str) {
        if (isSpecialPackageKey(str)) {
            return;
        }
        synchronized (this.mUsers) {
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                UserAccounts valueAt = this.mUsers.valueAt(i);
                try {
                    this.mPackageManager.getPackageUidAsUser(str, valueAt.userId);
                } catch (PackageManager.NameNotFoundException unused) {
                    valueAt.accountsDb.deleteAccountVisibilityForPackage(str);
                    synchronized (valueAt.dbLock) {
                        synchronized (valueAt.cacheLock) {
                            Iterator it = valueAt.visibilityCache.keySet().iterator();
                            while (it.hasNext()) {
                                getPackagesAndVisibilityForAccountLocked((Account) it.next(), valueAt).remove(str);
                            }
                            AccountManager.invalidateLocalAccountsDataCaches();
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void purgeUserData(int i) {
        UserAccounts userAccounts;
        synchronized (this.mUsers) {
            userAccounts = this.mUsers.get(i);
            this.mUsers.remove(i);
            this.mLocalUnlockedUsers.delete(i);
            AccountManager.invalidateLocalAccountsDataCaches();
        }
        if (userAccounts != null) {
            synchronized (userAccounts.dbLock) {
                synchronized (userAccounts.cacheLock) {
                    userAccounts.accountsDb.closeDebugStatement();
                    userAccounts.accountsDb.close();
                }
            }
        }
    }

    @VisibleForTesting
    void onUserUnlocked(Intent intent) {
        onUnlockUser(intent.getIntExtra("android.intent.extra.user_handle", -1));
    }

    void onUnlockUser(final int i) {
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "onUserUnlocked " + i);
        }
        synchronized (this.mUsers) {
            this.mLocalUnlockedUsers.put(i, true);
        }
        if (i < 1) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AccountManagerService.this.lambda$onUnlockUser$1(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: syncSharedAccounts, reason: merged with bridge method [inline-methods] */
    public void lambda$onUnlockUser$1(int i) {
        Account[] sharedAccountsAsUser = getSharedAccountsAsUser(i);
        if (sharedAccountsAsUser == null || sharedAccountsAsUser.length == 0) {
            return;
        }
        Account[] accountsAsUser = getAccountsAsUser(null, i, this.mContext.getOpPackageName());
        for (Account account : sharedAccountsAsUser) {
            if (!ArrayUtils.contains(accountsAsUser, account)) {
                copyAccountToUser(null, account, 0, i);
            }
        }
    }

    public void onServiceChanged(AuthenticatorDescription authenticatorDescription, int i, boolean z) {
        if (getUserManager().getUserInfo(i) == null) {
            Log.w(TAG, "onServiceChanged: ignore removed user " + i);
            return;
        }
        try {
            validateAccountsInternal(getUserAccounts(i), false);
        } catch (SQLiteCantOpenDatabaseException e) {
            Log.e(TAG, "User " + i + " may be removed when " + authenticatorDescription + " removed = " + z, e);
        }
    }

    public String getPassword(Account account) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "getPassword: " + account + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        }
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot get secrets for accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return readPasswordInternal(getUserAccounts(callingUserId), account);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private String readPasswordInternal(UserAccounts userAccounts, Account account) {
        String findAccountPasswordByNameAndType;
        if (account == null) {
            return null;
        }
        if (!isLocalUnlockedUser(userAccounts.userId)) {
            Log.w(TAG, "Password is not available - user " + userAccounts.userId + " data is locked");
            return null;
        }
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                findAccountPasswordByNameAndType = userAccounts.accountsDb.findAccountPasswordByNameAndType(account.name, account.type);
            }
        }
        return findAccountPasswordByNameAndType;
    }

    public String getPreviousName(Account account) {
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "getPreviousName: " + account + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        Objects.requireNonNull(account, "account cannot be null");
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return readPreviousNameInternal(getUserAccounts(callingUserId), account);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private String readPreviousNameInternal(UserAccounts userAccounts, Account account) {
        if (account == null) {
            return null;
        }
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                AtomicReference atomicReference = (AtomicReference) userAccounts.previousNameCache.get(account);
                if (atomicReference == null) {
                    String findDeAccountPreviousName = userAccounts.accountsDb.findDeAccountPreviousName(account);
                    userAccounts.previousNameCache.put(account, new AtomicReference(findDeAccountPreviousName));
                    return findDeAccountPreviousName;
                }
                return (String) atomicReference.get();
            }
        }
    }

    public String getUserData(Account account, String str) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, String.format("getUserData( account: %s, key: %s, callerUid: %s, pid: %s", account, str, Integer.valueOf(callingUid), Integer.valueOf(Binder.getCallingPid())));
        }
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(str, "key cannot be null");
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot get user data for accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        if (!isLocalUnlockedUser(callingUserId)) {
            Log.w(TAG, "User " + callingUserId + " data is locked. callingUid " + callingUid);
            return null;
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(callingUserId);
            if (accountExistsCache(userAccounts, account)) {
                return readUserDataInternal(userAccounts, account, str);
            }
            return null;
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public AuthenticatorDescription[] getAuthenticatorTypes(int i) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "getAuthenticatorTypes: for user id " + i + " caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        if (isCrossUser(callingUid, i) && !this.mAccountManagerServiceExt.isMultiAppUserId(i)) {
            throw new SecurityException(String.format("User %s tying to get authenticator types for %s", Integer.valueOf(UserHandle.getCallingUserId()), Integer.valueOf(i)));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return getAuthenticatorTypesInternal(i, callingUid);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private AuthenticatorDescription[] getAuthenticatorTypesInternal(int i, int i2) {
        this.mAuthenticatorCache.updateServices(i);
        Collection<RegisteredServicesCache.ServiceInfo<AuthenticatorDescription>> allServices = this.mAuthenticatorCache.getAllServices(i);
        ArrayList arrayList = new ArrayList(allServices.size());
        for (RegisteredServicesCache.ServiceInfo<AuthenticatorDescription> serviceInfo : allServices) {
            if (canCallerAccessPackage(((AuthenticatorDescription) serviceInfo.type).packageName, i2, i)) {
                arrayList.add((AuthenticatorDescription) serviceInfo.type);
            }
        }
        return (AuthenticatorDescription[]) arrayList.toArray(new AuthenticatorDescription[arrayList.size()]);
    }

    private boolean isCrossUser(int i, int i2) {
        return (i2 == UserHandle.getCallingUserId() || i == 1000 || this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0) ? false : true;
    }

    public boolean addAccountExplicitly(Account account, String str, Bundle bundle, String str2) {
        return addAccountExplicitlyWithVisibility(account, str, bundle, null, str2);
    }

    public void copyAccountToUser(final IAccountManagerResponse iAccountManagerResponse, final Account account, final int i, int i2) {
        if (isCrossUser(Binder.getCallingUid(), -1)) {
            throw new SecurityException("Calling copyAccountToUser requires android.permission.INTERACT_ACROSS_USERS_FULL");
        }
        UserAccounts userAccounts = getUserAccounts(i);
        final UserAccounts userAccounts2 = getUserAccounts(i2);
        if (userAccounts == null || userAccounts2 == null) {
            if (iAccountManagerResponse != null) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("booleanResult", false);
                try {
                    iAccountManagerResponse.onResult(bundle);
                    return;
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to report error back to the client." + e);
                    return;
                }
            }
            return;
        }
        Slog.d(TAG, "Copying account " + account.toSafeString() + " from user " + i + " to user " + i2);
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new Session(userAccounts, iAccountManagerResponse, account.type, false, false, account.name, false) { // from class: com.android.server.accounts.AccountManagerService.5
                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    return super.toDebugString(j) + ", getAccountCredentialsForClone, " + account.type;
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.getAccountCredentialsForCloning(this, account);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onResult(Bundle bundle2) {
                    Bundle.setDefusable(bundle2, true);
                    if (bundle2 != null && bundle2.getBoolean("booleanResult", false)) {
                        AccountManagerService.this.completeCloningAccount(iAccountManagerResponse, bundle2, account, userAccounts2, i);
                    } else {
                        super.onResult(bundle2);
                    }
                }
            }.bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean accountAuthenticated(Account account) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, String.format("accountAuthenticated( account: %s, callerUid: %s)", account, Integer.valueOf(callingUid)));
        }
        Objects.requireNonNull(account, "account cannot be null");
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot notify authentication for accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        if (!canUserModifyAccounts(callingUserId, callingUid) || !canUserModifyAccountsForType(callingUserId, account.type, callingUid)) {
            return false;
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            getUserAccounts(callingUserId);
            return updateLastAuthenticatedTime(account);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateLastAuthenticatedTime(Account account) {
        boolean updateAccountLastAuthenticatedTime;
        UserAccounts userAccountsForCaller = getUserAccountsForCaller();
        synchronized (userAccountsForCaller.dbLock) {
            synchronized (userAccountsForCaller.cacheLock) {
                updateAccountLastAuthenticatedTime = userAccountsForCaller.accountsDb.updateAccountLastAuthenticatedTime(account);
            }
        }
        return updateAccountLastAuthenticatedTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void completeCloningAccount(IAccountManagerResponse iAccountManagerResponse, final Bundle bundle, final Account account, UserAccounts userAccounts, final int i) {
        Bundle.setDefusable(bundle, true);
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new Session(userAccounts, iAccountManagerResponse, account.type, false, false, account.name, false) { // from class: com.android.server.accounts.AccountManagerService.6
                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    return super.toDebugString(j) + ", getAccountCredentialsForClone, " + account.type;
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    AccountManagerService accountManagerService = AccountManagerService.this;
                    for (Account account2 : accountManagerService.getAccounts(i, accountManagerService.mContext.getOpPackageName())) {
                        if (account2.equals(account)) {
                            this.mAuthenticator.addAccountFromCredentials(this, account, bundle);
                            return;
                        }
                    }
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onResult(Bundle bundle2) {
                    Bundle.setDefusable(bundle2, true);
                    super.onResult(bundle2);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onError(int i2, String str) {
                    super.onError(i2, str);
                }
            }.bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean addAccountInternal(UserAccounts userAccounts, Account account, String str, Bundle bundle, int i, Map<String, Integer> map, String str2) {
        Bundle.setDefusable(bundle, true);
        if (account == null) {
            return false;
        }
        String str3 = account.name;
        if (str3 != null && str3.length() > 200) {
            Log.w(TAG, "Account cannot be added - Name longer than 200 chars");
            return false;
        }
        String str4 = account.type;
        if (str4 != null && str4.length() > 200) {
            Log.w(TAG, "Account cannot be added - Name longer than 200 chars");
            return false;
        }
        if (!isLocalUnlockedUser(userAccounts.userId)) {
            Log.w(TAG, "Account " + account.toSafeString() + " cannot be added - user " + userAccounts.userId + " is locked. callingUid=" + i);
            return false;
        }
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                userAccounts.accountsDb.beginTransaction();
                try {
                    if (userAccounts.accountsDb.findCeAccountId(account) >= 0) {
                        Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping since the account already exists");
                        return false;
                    }
                    if (userAccounts.accountsDb.findAllDeAccounts().size() > 100) {
                        Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping since more than 100 accounts on device exist");
                        return false;
                    }
                    long insertCeAccount = userAccounts.accountsDb.insertCeAccount(account, str);
                    if (insertCeAccount < 0) {
                        Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping the DB insert failed");
                        return false;
                    }
                    if (userAccounts.accountsDb.insertDeAccount(account, insertCeAccount) < 0) {
                        Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping the DB insert failed");
                        return false;
                    }
                    if (bundle != null) {
                        for (String str5 : bundle.keySet()) {
                            if (userAccounts.accountsDb.insertExtra(insertCeAccount, str5, bundle.getString(str5)) < 0) {
                                Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping since insertExtra failed for key " + str5);
                                return false;
                            }
                            AccountManager.invalidateLocalAccountUserDataCaches();
                        }
                    }
                    if (map != null) {
                        for (Map.Entry<String, Integer> entry : map.entrySet()) {
                            setAccountVisibility(account, entry.getKey(), entry.getValue().intValue(), false, userAccounts, i);
                            insertCeAccount = insertCeAccount;
                        }
                    }
                    userAccounts.accountsDb.setTransactionSuccessful();
                    logRecord(AccountsDb.DEBUG_ACTION_ACCOUNT_ADD, "accounts", insertCeAccount, userAccounts, i);
                    insertAccountIntoCacheLocked(userAccounts, account);
                    if (getUserManager().getUserInfo(userAccounts.userId).canHaveProfile()) {
                        addAccountToLinkedRestrictedUsers(account, userAccounts.userId);
                    }
                    sendNotificationAccountUpdated(account, userAccounts);
                    Log.i(TAG, "callingUid=" + i + ", userId=" + userAccounts.userId + " added account");
                    sendAccountsChangedBroadcast(userAccounts.userId, account.type, "addAccount");
                    logAddAccountExplicitlyMetrics(str2, account.type, map);
                    return true;
                } finally {
                    userAccounts.accountsDb.endTransaction();
                }
            }
        }
    }

    private void logAddAccountExplicitlyMetrics(String str, String str2, Map<String, Integer> map) {
        DevicePolicyEventLogger.createEvent(203).setStrings(TextUtils.emptyIfNull(str2), TextUtils.emptyIfNull(str), findPackagesPerVisibility(map)).write();
    }

    private String[] findPackagesPerVisibility(Map<String, Integer> map) {
        HashMap hashMap = new HashMap();
        if (map != null) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (!hashMap.containsKey(entry.getValue())) {
                    hashMap.put(entry.getValue(), new HashSet());
                }
                hashMap.get(entry.getValue()).add(entry.getKey());
            }
        }
        return new String[]{getPackagesForVisibilityStr(0, hashMap), getPackagesForVisibilityStr(1, hashMap), getPackagesForVisibilityStr(2, hashMap), getPackagesForVisibilityStr(3, hashMap), getPackagesForVisibilityStr(4, hashMap)};
    }

    private String getPackagesForVisibilityStr(int i, Map<Integer, Set<String>> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(":");
        sb.append(map.containsKey(Integer.valueOf(i)) ? TextUtils.join(",", map.get(Integer.valueOf(i))) : "");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLocalUnlockedUser(int i) {
        boolean z;
        synchronized (this.mUsers) {
            z = this.mLocalUnlockedUsers.get(i);
        }
        return z;
    }

    private void addAccountToLinkedRestrictedUsers(Account account, int i) {
        for (UserInfo userInfo : getUserManager().getUsers()) {
            if (userInfo.isRestricted() && i == userInfo.restrictedProfileParentId) {
                addSharedAccountAsUser(account, userInfo.id);
                if (isLocalUnlockedUser(userInfo.id)) {
                    MessageHandler messageHandler = this.mHandler;
                    messageHandler.sendMessage(messageHandler.obtainMessage(4, i, userInfo.id, account));
                }
            }
        }
    }

    public void hasFeatures(IAccountManagerResponse iAccountManagerResponse, Account account, String[] strArr, int i, String str) {
        int callingUid = Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, str);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "hasFeatures: " + account + ", response " + iAccountManagerResponse + ", features " + Arrays.toString(strArr) + ", caller's uid " + callingUid + ", userId " + i + ", pid " + Binder.getCallingPid());
        }
        Preconditions.checkArgument(account != null, "account cannot be null");
        Preconditions.checkArgument(iAccountManagerResponse != null, "response cannot be null");
        Preconditions.checkArgument(strArr != null, "features cannot be null");
        if (i != UserHandle.getCallingUserId() && callingUid != 1000 && this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") != 0) {
            throw new SecurityException("User " + UserHandle.getCallingUserId() + " trying to check account features for " + i);
        }
        checkReadAccountsPermitted(callingUid, account.type, i, str);
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new TestFeaturesSession(getUserAccounts(i), iAccountManagerResponse, account, strArr).bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class TestFeaturesSession extends Session {
        private final Account mAccount;
        private final String[] mFeatures;

        public TestFeaturesSession(UserAccounts userAccounts, IAccountManagerResponse iAccountManagerResponse, Account account, String[] strArr) {
            super(AccountManagerService.this, userAccounts, iAccountManagerResponse, account.type, false, true, account.name, false);
            this.mFeatures = strArr;
            this.mAccount = account;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void run() throws RemoteException {
            try {
                this.mAuthenticator.hasFeatures(this, this.mAccount, this.mFeatures);
            } catch (RemoteException unused) {
                onError(1, "remote exception");
            }
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void onResult(Bundle bundle) {
            Bundle.setDefusable(bundle, true);
            IAccountManagerResponse responseAndClose = getResponseAndClose();
            if (responseAndClose != null) {
                try {
                    if (bundle == null) {
                        responseAndClose.onError(5, "null bundle");
                        return;
                    }
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + responseAndClose);
                    }
                    Bundle bundle2 = new Bundle();
                    bundle2.putBoolean("booleanResult", bundle.getBoolean("booleanResult", false));
                    responseAndClose.onResult(bundle2);
                } catch (RemoteException e) {
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, "failure while notifying response", e);
                    }
                }
            }
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        protected String toDebugString(long j) {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toDebugString(j));
            sb.append(", hasFeatures, ");
            sb.append(this.mAccount);
            sb.append(", ");
            String[] strArr = this.mFeatures;
            sb.append(strArr != null ? TextUtils.join(",", strArr) : null);
            return sb.toString();
        }
    }

    public void renameAccount(IAccountManagerResponse iAccountManagerResponse, Account account, String str) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "renameAccount: " + account + " -> " + str + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        }
        if (str != null && str.length() > 200) {
            Log.e(TAG, "renameAccount failed - account name longer than 200");
            throw new IllegalArgumentException("account name longer than 200");
        }
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot rename accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(callingUserId);
            Log.i(TAG, "callingUid=" + callingUid + ", userId=" + userAccounts.userId + " performing rename account");
            Account renameAccountInternal = renameAccountInternal(userAccounts, account, str);
            Bundle bundle = new Bundle();
            bundle.putString("authAccount", renameAccountInternal.name);
            bundle.putString("accountType", renameAccountInternal.type);
            bundle.putString("accountAccessId", renameAccountInternal.getAccessId());
            try {
                iAccountManagerResponse.onResult(bundle);
            } catch (RemoteException e) {
                Log.w(TAG, e.getMessage());
            }
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private Account renameAccountInternal(UserAccounts userAccounts, Account account, String str) {
        cancelNotification(getSigninRequiredNotificationId(userAccounts, account), new UserHandle(userAccounts.userId));
        synchronized (userAccounts.credentialsPermissionNotificationIds) {
            for (Pair pair : userAccounts.credentialsPermissionNotificationIds.keySet()) {
                if (account.equals(((Pair) pair.first).first)) {
                    cancelNotification((NotificationId) userAccounts.credentialsPermissionNotificationIds.get(pair), new UserHandle(userAccounts.userId));
                }
            }
        }
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                List<String> accountRemovedReceivers = getAccountRemovedReceivers(account, userAccounts);
                userAccounts.accountsDb.beginTransaction();
                Account account2 = new Account(str, account.type);
                try {
                    if (userAccounts.accountsDb.findCeAccountId(account2) >= 0) {
                        Log.e(TAG, "renameAccount failed - account with new name already exists");
                        return null;
                    }
                    long findDeAccountId = userAccounts.accountsDb.findDeAccountId(account);
                    if (findDeAccountId < 0) {
                        Log.e(TAG, "renameAccount failed - old account does not exist");
                        return null;
                    }
                    userAccounts.accountsDb.renameCeAccount(findDeAccountId, str);
                    if (!userAccounts.accountsDb.renameDeAccount(findDeAccountId, str, account.name)) {
                        Log.e(TAG, "renameAccount failed");
                        return null;
                    }
                    userAccounts.accountsDb.setTransactionSuccessful();
                    userAccounts.accountsDb.endTransaction();
                    Account insertAccountIntoCacheLocked = insertAccountIntoCacheLocked(userAccounts, account2);
                    Map map = (Map) userAccounts.userDataCache.get(account);
                    Map map2 = (Map) userAccounts.authTokenCache.get(account);
                    Map map3 = (Map) userAccounts.visibilityCache.get(account);
                    removeAccountFromCacheLocked(userAccounts, account);
                    userAccounts.userDataCache.put(insertAccountIntoCacheLocked, map);
                    userAccounts.authTokenCache.put(insertAccountIntoCacheLocked, map2);
                    userAccounts.visibilityCache.put(insertAccountIntoCacheLocked, map3);
                    userAccounts.previousNameCache.put(insertAccountIntoCacheLocked, new AtomicReference(account.name));
                    int i = userAccounts.userId;
                    if (canHaveProfile(i)) {
                        for (UserInfo userInfo : getUserManager().getAliveUsers()) {
                            if (userInfo.isRestricted() && userInfo.restrictedProfileParentId == i) {
                                renameSharedAccountAsUser(account, str, userInfo.id);
                            }
                        }
                    }
                    sendNotificationAccountUpdated(insertAccountIntoCacheLocked, userAccounts);
                    sendAccountsChangedBroadcast(userAccounts.userId, account.type, "renameAccount");
                    Iterator<String> it = accountRemovedReceivers.iterator();
                    while (it.hasNext()) {
                        sendAccountRemovedBroadcast(account, it.next(), userAccounts.userId, "renameAccount");
                    }
                    AccountManager.invalidateLocalAccountsDataCaches();
                    AccountManager.invalidateLocalAccountUserDataCaches();
                    return insertAccountIntoCacheLocked;
                } finally {
                    userAccounts.accountsDb.endTransaction();
                }
            }
        }
    }

    private boolean canHaveProfile(int i) {
        UserInfo userInfo = getUserManager().getUserInfo(i);
        return userInfo != null && userInfo.canHaveProfile();
    }

    public void removeAccountAsUser(IAccountManagerResponse iAccountManagerResponse, Account account, boolean z, int i) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "removeAccount: " + account + ", response " + iAccountManagerResponse + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid() + ", for user id " + i);
        }
        Preconditions.checkArgument(account != null, "account cannot be null");
        Preconditions.checkArgument(iAccountManagerResponse != null, "response cannot be null");
        if (isCrossUser(callingUid, i)) {
            throw new SecurityException(String.format("User %s tying remove account for %s", Integer.valueOf(UserHandle.getCallingUserId()), Integer.valueOf(i)));
        }
        UserHandle of = UserHandle.of(i);
        if (!isAccountManagedByCaller(account.type, callingUid, of.getIdentifier()) && !isSystemUid(callingUid) && !isProfileOwner(callingUid)) {
            throw new SecurityException(String.format("uid %s cannot remove accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        if (!canUserModifyAccounts(i, callingUid)) {
            try {
                iAccountManagerResponse.onError(100, "User cannot modify accounts");
                return;
            } catch (RemoteException unused) {
                return;
            }
        }
        if (!canUserModifyAccountsForType(i, account.type, callingUid)) {
            try {
                iAccountManagerResponse.onError(101, "User cannot modify accounts of this type (policy).");
                return;
            } catch (RemoteException unused2) {
                return;
            }
        }
        int callingPid = Binder.getCallingPid();
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        UserAccounts userAccounts = getUserAccounts(i);
        cancelNotification(getSigninRequiredNotificationId(userAccounts, account), of);
        synchronized (userAccounts.credentialsPermissionNotificationIds) {
            for (Pair pair : userAccounts.credentialsPermissionNotificationIds.keySet()) {
                if (account.equals(((Pair) pair.first).first)) {
                    cancelNotification((NotificationId) userAccounts.credentialsPermissionNotificationIds.get(pair), of);
                }
            }
        }
        logRecord(AccountsDb.DEBUG_ACTION_CALLED_ACCOUNT_REMOVE, "accounts", userAccounts.accountsDb.findDeAccountId(account), userAccounts, callingUid);
        try {
            new RemoveAccountSession(userAccounts, iAccountManagerResponse, account, z).bind(callingUid, callingPid);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean removeAccountExplicitly(Account account) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "removeAccountExplicitly: " + account + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        int identifier = Binder.getCallingUserHandle().getIdentifier();
        if (account == null) {
            Log.e(TAG, "account is null");
            return false;
        }
        if (!isAccountManagedByCaller(account.type, callingUid, identifier)) {
            throw new SecurityException(String.format("uid %s cannot explicitly remove accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        UserAccounts userAccountsForCaller = getUserAccountsForCaller();
        logRecord(AccountsDb.DEBUG_ACTION_CALLED_ACCOUNT_REMOVE, "accounts", userAccountsForCaller.accountsDb.findDeAccountId(account), userAccountsForCaller, callingUid);
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return removeAccountInternal(userAccountsForCaller, account, callingUid);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class RemoveAccountSession extends Session {
        final Account mAccount;

        public RemoveAccountSession(UserAccounts userAccounts, IAccountManagerResponse iAccountManagerResponse, Account account, boolean z) {
            super(AccountManagerService.this, userAccounts, iAccountManagerResponse, account.type, z, true, account.name, false);
            this.mAccount = account;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        protected String toDebugString(long j) {
            return super.toDebugString(j) + ", removeAccount, account " + this.mAccount;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void run() throws RemoteException {
            this.mAuthenticator.getAccountRemovalAllowed(this, this.mAccount);
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void onResult(Bundle bundle) {
            Bundle.setDefusable(bundle, true);
            if (bundle != null && bundle.containsKey("booleanResult") && !bundle.containsKey("intent")) {
                if (bundle.getBoolean("booleanResult")) {
                    AccountManagerService.this.removeAccountInternal(this.mAccounts, this.mAccount, IAccountAuthenticatorResponse.Stub.getCallingUid());
                }
                IAccountManagerResponse responseAndClose = getResponseAndClose();
                if (responseAndClose != null) {
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + responseAndClose);
                    }
                    try {
                        responseAndClose.onResult(bundle);
                    } catch (RemoteException e) {
                        Slog.e(AccountManagerService.TAG, "Error calling onResult()", e);
                    }
                }
            }
            super.onResult(bundle);
        }
    }

    @VisibleForTesting
    protected void removeAccountInternal(Account account) {
        removeAccountInternal(getUserAccountsForCaller(), account, IAccountManager.Stub.getCallingUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeAccountInternal(UserAccounts userAccounts, final Account account, int i) {
        boolean deleteDeAccount;
        long j;
        String str;
        boolean isLocalUnlockedUser = isLocalUnlockedUser(userAccounts.userId);
        if (!isLocalUnlockedUser) {
            Slog.i(TAG, "Removing account " + account.toSafeString() + " while user " + userAccounts.userId + " is still locked. CE data will be removed later");
        }
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                Map<String, Integer> requestingPackages = getRequestingPackages(account, userAccounts);
                List<String> accountRemovedReceivers = getAccountRemovedReceivers(account, userAccounts);
                userAccounts.accountsDb.beginTransaction();
                try {
                    long findDeAccountId = userAccounts.accountsDb.findDeAccountId(account);
                    deleteDeAccount = findDeAccountId >= 0 ? userAccounts.accountsDb.deleteDeAccount(findDeAccountId) : false;
                    if (isLocalUnlockedUser) {
                        j = findDeAccountId;
                        long findCeAccountId = userAccounts.accountsDb.findCeAccountId(account);
                        if (findCeAccountId >= 0) {
                            userAccounts.accountsDb.deleteCeAccount(findCeAccountId);
                        }
                    } else {
                        j = findDeAccountId;
                    }
                    userAccounts.accountsDb.setTransactionSuccessful();
                    if (deleteDeAccount) {
                        removeAccountFromCacheLocked(userAccounts, account);
                        for (Map.Entry<String, Integer> entry : requestingPackages.entrySet()) {
                            if (entry.getValue().intValue() == 1 || entry.getValue().intValue() == 2) {
                                notifyPackage(entry.getKey(), userAccounts);
                            }
                        }
                        Log.i(TAG, "callingUid=" + i + ", userId=" + userAccounts.userId + " removed account");
                        sendAccountsChangedBroadcast(userAccounts.userId, account.type, "removeAccount");
                        Iterator<String> it = accountRemovedReceivers.iterator();
                        while (it.hasNext()) {
                            sendAccountRemovedBroadcast(account, it.next(), userAccounts.userId, "removeAccount");
                        }
                        if (isLocalUnlockedUser) {
                            str = AccountsDb.DEBUG_ACTION_ACCOUNT_REMOVE;
                        } else {
                            str = AccountsDb.DEBUG_ACTION_ACCOUNT_REMOVE_DE;
                        }
                        logRecord(str, "accounts", j, userAccounts);
                    }
                } finally {
                    userAccounts.accountsDb.endTransaction();
                }
            }
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int i2 = userAccounts.userId;
            if (canHaveProfile(i2)) {
                for (UserInfo userInfo : getUserManager().getAliveUsers()) {
                    if (userInfo.isRestricted() && i2 == userInfo.restrictedProfileParentId) {
                        removeSharedAccountAsUser(account, userInfo.id, i);
                    }
                }
            }
            if (deleteDeAccount) {
                synchronized (userAccounts.credentialsPermissionNotificationIds) {
                    for (Pair pair : userAccounts.credentialsPermissionNotificationIds.keySet()) {
                        if (account.equals(((Pair) pair.first).first) && "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE".equals(((Pair) pair.first).second)) {
                            final int intValue = ((Integer) pair.second).intValue();
                            this.mHandler.post(new Runnable() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    AccountManagerService.this.lambda$removeAccountInternal$2(account, intValue);
                                }
                            });
                        }
                    }
                }
            }
            AccountManager.invalidateLocalAccountUserDataCaches();
            return deleteDeAccount;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAccountInternal$2(Account account, int i) {
        cancelAccountAccessRequestNotificationIfNeeded(account, i, false);
    }

    public void invalidateAuthToken(String str, String str2) {
        int callingUid = Binder.getCallingUid();
        Objects.requireNonNull(str, "accountType cannot be null");
        Objects.requireNonNull(str2, "authToken cannot be null");
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "invalidateAuthToken: accountType " + str + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(callingUserId);
            synchronized (userAccounts.dbLock) {
                userAccounts.accountsDb.beginTransaction();
                try {
                    List<Pair<Account, String>> invalidateAuthTokenLocked = invalidateAuthTokenLocked(userAccounts, str, str2);
                    userAccounts.accountsDb.setTransactionSuccessful();
                    userAccounts.accountsDb.endTransaction();
                    synchronized (userAccounts.cacheLock) {
                        for (Pair<Account, String> pair : invalidateAuthTokenLocked) {
                            writeAuthTokenIntoCacheLocked(userAccounts, (Account) pair.first, (String) pair.second, null);
                        }
                        userAccounts.accountTokenCaches.remove(str, str2);
                    }
                } catch (Throwable th) {
                    userAccounts.accountsDb.endTransaction();
                    throw th;
                }
            }
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private List<Pair<Account, String>> invalidateAuthTokenLocked(UserAccounts userAccounts, String str, String str2) {
        ArrayList arrayList = new ArrayList();
        Cursor findAuthtokenForAllAccounts = userAccounts.accountsDb.findAuthtokenForAllAccounts(str, str2);
        while (findAuthtokenForAllAccounts.moveToNext()) {
            try {
                String string = findAuthtokenForAllAccounts.getString(0);
                String string2 = findAuthtokenForAllAccounts.getString(1);
                String string3 = findAuthtokenForAllAccounts.getString(2);
                userAccounts.accountsDb.deleteAuthToken(string);
                arrayList.add(Pair.create(new Account(string2, str), string3));
            } finally {
                findAuthtokenForAllAccounts.close();
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCachedToken(UserAccounts userAccounts, Account account, String str, byte[] bArr, String str2, String str3, long j) {
        if (account == null || str2 == null || str == null || bArr == null) {
            return;
        }
        cancelNotification(getSigninRequiredNotificationId(userAccounts, account), UserHandle.of(userAccounts.userId));
        synchronized (userAccounts.cacheLock) {
            userAccounts.accountTokenCaches.put(account, str3, str2, str, bArr, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean saveAuthTokenToDatabase(UserAccounts userAccounts, Account account, String str, String str2) {
        if (account == null || str == null) {
            return false;
        }
        cancelNotification(getSigninRequiredNotificationId(userAccounts, account), UserHandle.of(userAccounts.userId));
        synchronized (userAccounts.dbLock) {
            userAccounts.accountsDb.beginTransaction();
            try {
                long findDeAccountId = userAccounts.accountsDb.findDeAccountId(account);
                if (findDeAccountId < 0) {
                    return false;
                }
                userAccounts.accountsDb.deleteAuthtokensByAccountIdAndType(findDeAccountId, str);
                if (userAccounts.accountsDb.insertAuthToken(findDeAccountId, str, str2) < 0) {
                    return false;
                }
                userAccounts.accountsDb.setTransactionSuccessful();
                userAccounts.accountsDb.endTransaction();
                synchronized (userAccounts.cacheLock) {
                    writeAuthTokenIntoCacheLocked(userAccounts, account, str, str2);
                }
                return true;
            } finally {
                userAccounts.accountsDb.endTransaction();
            }
        }
    }

    public String peekAuthToken(Account account, String str) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "peekAuthToken: " + account + ", authTokenType " + str + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(str, "authTokenType cannot be null");
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot peek the authtokens associated with accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        if (!isLocalUnlockedUser(callingUserId)) {
            Log.w(TAG, "Authtoken not available - user " + callingUserId + " data is locked. callingUid " + callingUid);
            return null;
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return readAuthTokenInternal(getUserAccounts(callingUserId), account, str);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setAuthToken(Account account, String str, String str2) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "setAuthToken: " + account + ", authTokenType " + str + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(str, "authTokenType cannot be null");
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot set auth tokens associated with accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            saveAuthTokenToDatabase(getUserAccounts(callingUserId), account, str, str2);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setPassword(Account account, String str) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "setAuthToken: " + account + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        Objects.requireNonNull(account, "account cannot be null");
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot set secrets for accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            setPasswordInternal(getUserAccounts(callingUserId), account, str, callingUid);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void setPasswordInternal(UserAccounts userAccounts, Account account, String str, int i) {
        String str2;
        if (account == null) {
            return;
        }
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                userAccounts.accountsDb.beginTransaction();
                boolean z = false;
                try {
                    long findDeAccountId = userAccounts.accountsDb.findDeAccountId(account);
                    if (findDeAccountId >= 0) {
                        userAccounts.accountsDb.updateCeAccountPassword(findDeAccountId, str);
                        userAccounts.accountsDb.deleteAuthTokensByAccountId(findDeAccountId);
                        userAccounts.authTokenCache.remove(account);
                        userAccounts.accountTokenCaches.remove(account);
                        userAccounts.accountsDb.setTransactionSuccessful();
                        z = true;
                        if (str != null && str.length() != 0) {
                            str2 = AccountsDb.DEBUG_ACTION_SET_PASSWORD;
                            logRecord(str2, "accounts", findDeAccountId, userAccounts, i);
                        }
                        str2 = AccountsDb.DEBUG_ACTION_CLEAR_PASSWORD;
                        logRecord(str2, "accounts", findDeAccountId, userAccounts, i);
                    }
                } finally {
                    userAccounts.accountsDb.endTransaction();
                    if (z) {
                        sendNotificationAccountUpdated(account, userAccounts);
                        Log.i(TAG, "callingUid=" + i + " changed password");
                        sendAccountsChangedBroadcast(userAccounts.userId, account.type, "setPassword");
                    }
                }
            }
        }
    }

    public void clearPassword(Account account) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "clearPassword: " + account + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        Objects.requireNonNull(account, "account cannot be null");
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot clear passwords for accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            setPasswordInternal(getUserAccounts(callingUserId), account, null, callingUid);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setUserData(Account account, String str, String str2) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "setUserData: " + account + ", key " + str + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        if (str == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        }
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(account.type, callingUid, callingUserId)) {
            throw new SecurityException(String.format("uid %s cannot set user data for accounts of type: %s", Integer.valueOf(callingUid), account.type));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(callingUserId);
            if (accountExistsCache(userAccounts, account)) {
                setUserdataInternal(userAccounts, account, str, str2);
            }
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean accountExistsCache(UserAccounts userAccounts, Account account) {
        synchronized (userAccounts.cacheLock) {
            if (userAccounts.accountCache.containsKey(account.type)) {
                for (Account account2 : userAccounts.accountCache.get(account.type)) {
                    if (account2.name.equals(account.name)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private void setUserdataInternal(UserAccounts userAccounts, Account account, String str, String str2) {
        synchronized (userAccounts.dbLock) {
            userAccounts.accountsDb.beginTransaction();
            try {
                long findDeAccountId = userAccounts.accountsDb.findDeAccountId(account);
                if (findDeAccountId < 0) {
                    return;
                }
                long findExtrasIdByAccountId = userAccounts.accountsDb.findExtrasIdByAccountId(findDeAccountId, str);
                if (findExtrasIdByAccountId < 0) {
                    if (userAccounts.accountsDb.insertExtra(findDeAccountId, str, str2) < 0) {
                        return;
                    }
                } else if (!userAccounts.accountsDb.updateExtra(findExtrasIdByAccountId, str2)) {
                    return;
                }
                userAccounts.accountsDb.setTransactionSuccessful();
                userAccounts.accountsDb.endTransaction();
                synchronized (userAccounts.cacheLock) {
                    writeUserDataIntoCacheLocked(userAccounts, account, str, str2);
                    AccountManager.invalidateLocalAccountUserDataCaches();
                }
            } finally {
                userAccounts.accountsDb.endTransaction();
            }
        }
    }

    private void onResult(IAccountManagerResponse iAccountManagerResponse, Bundle bundle) {
        if (bundle == null) {
            Log.e(TAG, "the result is unexpectedly null", new Exception());
        }
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, getClass().getSimpleName() + " calling onResult() on response " + iAccountManagerResponse);
        }
        try {
            iAccountManagerResponse.onResult(bundle);
        } catch (RemoteException e) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "failure while notifying response", e);
            }
        }
    }

    public void getAuthTokenLabel(IAccountManagerResponse iAccountManagerResponse, final String str, final String str2) throws RemoteException {
        Preconditions.checkArgument(str != null, "accountType cannot be null");
        Preconditions.checkArgument(str2 != null, "authTokenType cannot be null");
        int callingUid = IAccountManager.Stub.getCallingUid();
        IAccountManager.Stub.clearCallingIdentity();
        if (UserHandle.getAppId(callingUid) != 1000) {
            throw new SecurityException("can only call from system");
        }
        int userId = UserHandle.getUserId(callingUid);
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new Session(getUserAccounts(userId), iAccountManagerResponse, str, false, false, null, false) { // from class: com.android.server.accounts.AccountManagerService.7
                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    return super.toDebugString(j) + ", getAuthTokenLabel, " + str + ", authTokenType " + str2;
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.getAuthTokenLabel(this, str2);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onResult(Bundle bundle) {
                    Bundle.setDefusable(bundle, true);
                    if (bundle != null) {
                        String string = bundle.getString("authTokenLabelKey");
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("authTokenLabelKey", string);
                        super.onResult(bundle2);
                        return;
                    }
                    super.onResult(bundle);
                }
            }.bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void getAuthToken(IAccountManagerResponse iAccountManagerResponse, final Account account, final String str, final boolean z, boolean z2, final Bundle bundle) {
        String str2;
        int i;
        String readAuthTokenInternal;
        Bundle.setDefusable(bundle, true);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "getAuthToken: " + account + ", response " + iAccountManagerResponse + ", authTokenType " + str + ", notifyOnAuthFailure " + z + ", expectActivityLaunch " + z2 + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        Preconditions.checkArgument(iAccountManagerResponse != null, "response cannot be null");
        try {
            if (account == null) {
                Slog.w(TAG, "getAuthToken called with null account");
                iAccountManagerResponse.onError(7, "account is null");
                return;
            }
            if (str == null) {
                Slog.w(TAG, "getAuthToken called with null authTokenType");
                iAccountManagerResponse.onError(7, "authTokenType is null");
                return;
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                final UserAccounts userAccounts = getUserAccounts(callingUserId);
                RegisteredServicesCache.ServiceInfo<AuthenticatorDescription> serviceInfo = this.mAuthenticatorCache.getServiceInfo(AuthenticatorDescription.newKey(account.type), userAccounts.userId);
                boolean z3 = serviceInfo != null && ((AuthenticatorDescription) serviceInfo.type).customTokens;
                int callingUid = Binder.getCallingUid();
                boolean z4 = z3 || permissionIsGranted(account, str, callingUid, callingUserId);
                String string = bundle.getString("androidPackageName");
                clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    String[] packagesForUid = this.mPackageManager.getPackagesForUid(callingUid);
                    if (string == null || packagesForUid == null || !ArrayUtils.contains(packagesForUid, string)) {
                        throw new SecurityException(String.format("Uid %s is attempting to illegally masquerade as package %s!", Integer.valueOf(callingUid), string));
                    }
                    bundle.putInt("callerUid", callingUid);
                    bundle.putInt("callerPid", Binder.getCallingPid());
                    if (z) {
                        bundle.putBoolean("notifyOnAuthFailure", true);
                    }
                    long clearCallingIdentity2 = IAccountManager.Stub.clearCallingIdentity();
                    try {
                        final byte[] calculatePackageSignatureDigest = calculatePackageSignatureDigest(string, callingUserId);
                        if (!z3 && z4 && (readAuthTokenInternal = readAuthTokenInternal(userAccounts, account, str)) != null) {
                            logGetAuthTokenMetrics(string, account.type);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("authtoken", readAuthTokenInternal);
                            bundle2.putString("authAccount", account.name);
                            bundle2.putString("accountType", account.type);
                            onResult(iAccountManagerResponse, bundle2);
                            return;
                        }
                        if (z3) {
                            i = callingUid;
                            str2 = string;
                            TokenCache.Value readCachedTokenInternal = readCachedTokenInternal(userAccounts, account, str, string, calculatePackageSignatureDigest);
                            if (readCachedTokenInternal != null) {
                                logGetAuthTokenMetrics(str2, account.type);
                                if (Log.isLoggable(TAG, 2)) {
                                    Log.v(TAG, "getAuthToken: cache hit ofr custom token authenticator.");
                                }
                                Bundle bundle3 = new Bundle();
                                bundle3.putString("authtoken", readCachedTokenInternal.token);
                                bundle3.putLong("android.accounts.expiry", readCachedTokenInternal.expiryEpochMillis);
                                bundle3.putString("authAccount", account.name);
                                bundle3.putString("accountType", account.type);
                                onResult(iAccountManagerResponse, bundle3);
                                return;
                            }
                        } else {
                            str2 = string;
                            i = callingUid;
                        }
                        final int i2 = i;
                        final String str3 = str2;
                        final boolean z5 = z4;
                        final boolean z6 = z3;
                        new Session(userAccounts, iAccountManagerResponse, account.type, z2, false, account.name, false) { // from class: com.android.server.accounts.AccountManagerService.8
                            @Override // com.android.server.accounts.AccountManagerService.Session
                            protected String toDebugString(long j) {
                                Bundle bundle4 = bundle;
                                if (bundle4 != null) {
                                    bundle4.keySet();
                                }
                                return super.toDebugString(j) + ", getAuthToken, " + account.toSafeString() + ", authTokenType " + str + ", loginOptions " + bundle + ", notifyOnAuthFailure " + z;
                            }

                            @Override // com.android.server.accounts.AccountManagerService.Session
                            public void run() throws RemoteException {
                                if (!z5) {
                                    this.mAuthenticator.getAuthTokenLabel(this, str);
                                } else {
                                    this.mAuthenticator.getAuthToken(this, account, str, bundle);
                                    AccountManagerService.this.logGetAuthTokenMetrics(str3, account.type);
                                }
                            }

                            @Override // com.android.server.accounts.AccountManagerService.Session
                            public void onResult(Bundle bundle4) {
                                Bundle.setDefusable(bundle4, true);
                                if (bundle4 != null) {
                                    if (bundle4.containsKey("authTokenLabelKey")) {
                                        Intent newGrantCredentialsPermissionIntent = AccountManagerService.this.newGrantCredentialsPermissionIntent(account, null, i2, new AccountAuthenticatorResponse((IAccountAuthenticatorResponse) this), str, true);
                                        Bundle bundle5 = new Bundle();
                                        bundle5.putParcelable("intent", newGrantCredentialsPermissionIntent);
                                        onResult(bundle5);
                                        return;
                                    }
                                    String string2 = bundle4.getString("authtoken");
                                    if (string2 != null) {
                                        String string3 = bundle4.getString("authAccount");
                                        String string4 = bundle4.getString("accountType");
                                        if (TextUtils.isEmpty(string4) || TextUtils.isEmpty(string3)) {
                                            onError(5, "the type and name should not be empty");
                                            return;
                                        }
                                        Account account2 = new Account(string3, string4);
                                        if (!z6) {
                                            AccountManagerService.this.saveAuthTokenToDatabase(this.mAccounts, account2, str, string2);
                                        }
                                        long j = bundle4.getLong("android.accounts.expiry", 0L);
                                        if (z6 && j > System.currentTimeMillis()) {
                                            AccountManagerService.this.saveCachedToken(this.mAccounts, account, str3, calculatePackageSignatureDigest, str, string2, j);
                                        }
                                    }
                                    Intent intent = (Intent) bundle4.getParcelable("intent", Intent.class);
                                    if (intent != null && z && !z6) {
                                        if (!checkKeyIntent(Binder.getCallingUid(), bundle4)) {
                                            onError(5, "invalid intent in bundle returned");
                                            return;
                                        }
                                        AccountManagerService.this.doNotification(this.mAccounts, account, bundle4.getString("authFailedMessage"), intent, "android", userAccounts.userId);
                                    }
                                }
                                super.onResult(bundle4);
                            }
                        }.bind();
                    } finally {
                        IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity2);
                    }
                } finally {
                }
            } finally {
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to report error back to the client." + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logGetAuthTokenMetrics(String str, String str2) {
        DevicePolicyEventLogger.createEvent(204).setStrings(new String[]{TextUtils.emptyIfNull(str), TextUtils.emptyIfNull(str2)}).write();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private byte[] calculatePackageSignatureDigest(String str, int i) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            for (Signature signature : this.mPackageManager.getPackageInfoAsUser(str, 64, i).signatures) {
                messageDigest.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w(TAG, "Could not find packageinfo for: " + str);
            messageDigest = null;
            if (messageDigest == null) {
            }
        } catch (NoSuchAlgorithmException e) {
            Log.wtf(TAG, "SHA-256 should be available", e);
            messageDigest = null;
            if (messageDigest == null) {
            }
        }
        if (messageDigest == null) {
            return null;
        }
        return messageDigest.digest();
    }

    private void createNoCredentialsPermissionNotification(Account account, Intent intent, String str, int i) {
        String str2;
        int intExtra = intent.getIntExtra("uid", -1);
        String stringExtra = intent.getStringExtra("authTokenType");
        String string = this.mContext.getString(R.string.serviceErased, getApplicationLabel(str, i), account.name);
        int indexOf = string.indexOf(10);
        if (indexOf > 0) {
            String substring = string.substring(0, indexOf);
            str2 = string.substring(indexOf + 1);
            string = substring;
        } else {
            str2 = "";
        }
        UserHandle of = UserHandle.of(i);
        Context contextForUser = getContextForUser(of);
        installNotification(getCredentialPermissionNotificationId(account, stringExtra, intExtra), new Notification.Builder(contextForUser, SystemNotificationChannels.ACCOUNT).setSmallIcon(R.drawable.stat_sys_warning).setWhen(0L).setColor(contextForUser.getColor(R.color.system_notification_accent_color)).setContentTitle(string).setContentText(str2).setContentIntent(PendingIntent.getActivityAsUser(this.mContext, 0, intent, AudioFormat.AAC_ADIF, null, of)).build(), "android", of.getIdentifier());
    }

    private String getApplicationLabel(String str, int i) {
        try {
            PackageManager packageManager = this.mPackageManager;
            return packageManager.getApplicationLabel(packageManager.getApplicationInfoAsUser(str, 0, i)).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return str;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent newGrantCredentialsPermissionIntent(Account account, String str, int i, AccountAuthenticatorResponse accountAuthenticatorResponse, String str2, boolean z) {
        Intent intent = new Intent(this.mContext, (Class<?>) GrantCredentialsPermissionActivity.class);
        if (z) {
            intent.setFlags(AudioFormat.EVRC);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getCredentialPermissionNotificationId(account, str2, i).mTag);
        if (str == null) {
            str = "";
        }
        sb.append(str);
        intent.addCategory(sb.toString());
        intent.putExtra("account", account);
        intent.putExtra("authTokenType", str2);
        intent.putExtra("response", accountAuthenticatorResponse);
        intent.putExtra("uid", i);
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NotificationId getCredentialPermissionNotificationId(Account account, String str, int i) {
        NotificationId notificationId;
        UserAccounts userAccounts = getUserAccounts(UserHandle.getUserId(i));
        synchronized (userAccounts.credentialsPermissionNotificationIds) {
            Pair pair = new Pair(new Pair(account, str), Integer.valueOf(i));
            notificationId = (NotificationId) userAccounts.credentialsPermissionNotificationIds.get(pair);
            if (notificationId == null) {
                notificationId = new NotificationId("AccountManagerService:38:" + account.hashCode() + ":" + str.hashCode() + ":" + i, 38);
                userAccounts.credentialsPermissionNotificationIds.put(pair, notificationId);
            }
        }
        return notificationId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NotificationId getSigninRequiredNotificationId(UserAccounts userAccounts, Account account) {
        NotificationId notificationId;
        synchronized (userAccounts.signinRequiredNotificationIds) {
            notificationId = (NotificationId) userAccounts.signinRequiredNotificationIds.get(account);
            if (notificationId == null) {
                NotificationId notificationId2 = new NotificationId("AccountManagerService:37:" + account.hashCode(), 37);
                userAccounts.signinRequiredNotificationIds.put(account, notificationId2);
                notificationId = notificationId2;
            }
        }
        return notificationId;
    }

    public void addAccount(IAccountManagerResponse iAccountManagerResponse, String str, String str2, String[] strArr, boolean z, Bundle bundle) {
        Bundle.setDefusable(bundle, true);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "addAccount: accountType " + str + ", response " + iAccountManagerResponse + ", authTokenType " + str2 + ", requiredFeatures " + Arrays.toString(strArr) + ", expectActivityLaunch " + z + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        if (iAccountManagerResponse == null) {
            throw new IllegalArgumentException("response is null");
        }
        if (str == null) {
            throw new IllegalArgumentException("accountType is null");
        }
        int callingUid = Binder.getCallingUid();
        int userId = UserHandle.getUserId(callingUid);
        if (!canUserModifyAccounts(userId, callingUid)) {
            try {
                iAccountManagerResponse.onError(100, "User is not allowed to add an account!");
            } catch (RemoteException unused) {
            }
            showCantAddAccount(100, userId);
        } else if (!canUserModifyAccountsForType(userId, str, callingUid)) {
            try {
                iAccountManagerResponse.onError(101, "User cannot modify accounts of this type (policy).");
            } catch (RemoteException unused2) {
            }
            showCantAddAccount(101, userId);
        } else {
            addAccountAndLogMetrics(iAccountManagerResponse, str, str2, strArr, z, bundle, userId);
        }
    }

    public void addAccountAsUser(IAccountManagerResponse iAccountManagerResponse, String str, String str2, String[] strArr, boolean z, Bundle bundle, int i) {
        Bundle.setDefusable(bundle, true);
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "addAccount: accountType " + str + ", response " + iAccountManagerResponse + ", authTokenType " + str2 + ", requiredFeatures " + Arrays.toString(strArr) + ", expectActivityLaunch " + z + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid() + ", for user id " + i);
        }
        Preconditions.checkArgument(iAccountManagerResponse != null, "response cannot be null");
        Preconditions.checkArgument(str != null, "accountType cannot be null");
        if (isCrossUser(callingUid, i)) {
            throw new SecurityException(String.format("User %s trying to add account for %s", Integer.valueOf(UserHandle.getCallingUserId()), Integer.valueOf(i)));
        }
        if (!canUserModifyAccounts(i, callingUid)) {
            try {
                iAccountManagerResponse.onError(100, "User is not allowed to add an account!");
            } catch (RemoteException unused) {
            }
            showCantAddAccount(100, i);
        } else if (!canUserModifyAccountsForType(i, str, callingUid)) {
            try {
                iAccountManagerResponse.onError(101, "User cannot modify accounts of this type (policy).");
            } catch (RemoteException unused2) {
            }
            showCantAddAccount(101, i);
        } else {
            addAccountAndLogMetrics(iAccountManagerResponse, str, str2, strArr, z, bundle, i);
        }
    }

    private void addAccountAndLogMetrics(IAccountManagerResponse iAccountManagerResponse, final String str, final String str2, final String[] strArr, boolean z, Bundle bundle, int i) {
        int callingPid = Binder.getCallingPid();
        int callingUid = Binder.getCallingUid();
        final Bundle bundle2 = bundle == null ? new Bundle() : bundle;
        bundle2.putInt("callerUid", callingUid);
        bundle2.putInt("callerPid", callingPid);
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(i);
            logRecordWithUid(userAccounts, AccountsDb.DEBUG_ACTION_CALLED_ACCOUNT_ADD, "accounts", callingUid);
            new Session(userAccounts, iAccountManagerResponse, str, z, true, null, false, true) { // from class: com.android.server.accounts.AccountManagerService.9
                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.addAccount(this, this.mAccountType, str2, strArr, bundle2);
                    AccountManagerService.this.logAddAccountMetrics(bundle2.getString("androidPackageName"), str, strArr, str2);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(super.toDebugString(j));
                    sb.append(", addAccount, accountType ");
                    sb.append(str);
                    sb.append(", requiredFeatures ");
                    String[] strArr2 = strArr;
                    sb.append(strArr2 != null ? TextUtils.join(",", strArr2) : null);
                    return sb.toString();
                }
            }.bind(callingUid, callingPid);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAddAccountMetrics(String str, String str2, String[] strArr, String str3) {
        DevicePolicyEventLogger createEvent = DevicePolicyEventLogger.createEvent(202);
        String[] strArr2 = new String[4];
        strArr2[0] = TextUtils.emptyIfNull(str2);
        strArr2[1] = TextUtils.emptyIfNull(str);
        strArr2[2] = TextUtils.emptyIfNull(str3);
        strArr2[3] = strArr == null ? "" : TextUtils.join(";", strArr);
        createEvent.setStrings(strArr2).write();
    }

    public void startAddAccountSession(IAccountManagerResponse iAccountManagerResponse, final String str, final String str2, final String[] strArr, boolean z, Bundle bundle) {
        Bundle bundle2 = bundle;
        Bundle.setDefusable(bundle2, true);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "startAddAccountSession: accountType " + str + ", response " + iAccountManagerResponse + ", authTokenType " + str2 + ", requiredFeatures " + Arrays.toString(strArr) + ", expectActivityLaunch " + z + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        Preconditions.checkArgument(iAccountManagerResponse != null, "response cannot be null");
        Preconditions.checkArgument(str != null, "accountType cannot be null");
        int callingUid = Binder.getCallingUid();
        int userId = UserHandle.getUserId(callingUid);
        if (!canUserModifyAccounts(userId, callingUid)) {
            try {
                iAccountManagerResponse.onError(100, "User is not allowed to add an account!");
            } catch (RemoteException unused) {
            }
            showCantAddAccount(100, userId);
            return;
        }
        if (!canUserModifyAccountsForType(userId, str, callingUid)) {
            try {
                iAccountManagerResponse.onError(101, "User cannot modify accounts of this type (policy).");
            } catch (RemoteException unused2) {
            }
            showCantAddAccount(101, userId);
            return;
        }
        int callingPid = Binder.getCallingPid();
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        final Bundle bundle3 = bundle2;
        bundle3.putInt("callerUid", callingUid);
        bundle3.putInt("callerPid", callingPid);
        final String string = bundle3.getString("androidPackageName");
        boolean checkPermissionAndNote = checkPermissionAndNote(string, callingUid, "android.permission.GET_PASSWORD");
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(userId);
            logRecordWithUid(userAccounts, AccountsDb.DEBUG_ACTION_CALLED_START_ACCOUNT_ADD, "accounts", callingUid);
            new StartAccountSession(userAccounts, iAccountManagerResponse, str, z, null, false, true, checkPermissionAndNote) { // from class: com.android.server.accounts.AccountManagerService.10
                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.startAddAccountSession(this, this.mAccountType, str2, strArr, bundle3);
                    AccountManagerService.this.logAddAccountMetrics(string, str, strArr, str2);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(super.toDebugString(j));
                    sb.append(", startAddAccountSession, accountType ");
                    sb.append(str);
                    sb.append(", requiredFeatures ");
                    String[] strArr2 = strArr;
                    sb.append(strArr2 != null ? TextUtils.join(",", strArr2) : "null");
                    return sb.toString();
                }
            }.bind(callingUid, callingPid);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private abstract class StartAccountSession extends Session {
        private final boolean mIsPasswordForwardingAllowed;

        public StartAccountSession(UserAccounts userAccounts, IAccountManagerResponse iAccountManagerResponse, String str, boolean z, String str2, boolean z2, boolean z3, boolean z4) {
            super(userAccounts, iAccountManagerResponse, str, z, true, str2, z2, z3);
            this.mIsPasswordForwardingAllowed = z4;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void onResult(Bundle bundle) {
            IAccountManagerResponse responseAndClose;
            Bundle.setDefusable(bundle, true);
            this.mNumResults++;
            if (bundle != null && !checkKeyIntent(Binder.getCallingUid(), bundle)) {
                onError(5, "invalid intent in bundle returned");
                return;
            }
            if (this.mExpectActivityLaunch && bundle != null && bundle.containsKey("intent")) {
                responseAndClose = this.mResponse;
            } else {
                responseAndClose = getResponseAndClose();
            }
            if (responseAndClose == null) {
                return;
            }
            if (bundle == null) {
                if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                    Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onError() on response " + responseAndClose);
                }
                AccountManagerService.this.sendErrorResponse(responseAndClose, 5, "null bundle returned");
                return;
            }
            if (bundle.getInt("errorCode", -1) > 0) {
                AccountManagerService.this.sendErrorResponse(responseAndClose, bundle.getInt("errorCode"), bundle.getString("errorMessage"));
                return;
            }
            if (!this.mIsPasswordForwardingAllowed) {
                bundle.remove(HintsHelper.AUTOFILL_HINT_PASSWORD);
            }
            bundle.remove("authtoken");
            if (!checkKeyIntent(Binder.getCallingUid(), bundle)) {
                onError(5, "invalid intent in bundle returned");
                return;
            }
            if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + responseAndClose);
            }
            Bundle bundle2 = bundle.getBundle("accountSessionBundle");
            if (bundle2 != null) {
                String string = bundle2.getString("accountType");
                if (TextUtils.isEmpty(string) || !this.mAccountType.equalsIgnoreCase(string)) {
                    Log.w(AccountManagerService.TAG, "Account type in session bundle doesn't match request.");
                }
                bundle2.putString("accountType", this.mAccountType);
                try {
                    bundle.putBundle("accountSessionBundle", CryptoHelper.getInstance().encryptBundle(bundle2));
                } catch (GeneralSecurityException e) {
                    if (Log.isLoggable(AccountManagerService.TAG, 3)) {
                        Log.v(AccountManagerService.TAG, "Failed to encrypt session bundle!", e);
                    }
                    AccountManagerService.this.sendErrorResponse(responseAndClose, 5, "failed to encrypt session bundle");
                    return;
                }
            }
            AccountManagerService.this.sendResponse(responseAndClose, bundle);
        }
    }

    public void finishSessionAsUser(IAccountManagerResponse iAccountManagerResponse, Bundle bundle, boolean z, Bundle bundle2, int i) {
        Bundle.setDefusable(bundle, true);
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "finishSession: response " + iAccountManagerResponse + ", expectActivityLaunch " + z + ", caller's uid " + callingUid + ", caller's user id " + UserHandle.getCallingUserId() + ", pid " + Binder.getCallingPid() + ", for user id " + i);
        }
        Preconditions.checkArgument(iAccountManagerResponse != null, "response cannot be null");
        if (bundle == null || bundle.size() == 0) {
            throw new IllegalArgumentException("sessionBundle is empty");
        }
        if (isCrossUser(callingUid, i)) {
            throw new SecurityException(String.format("User %s trying to finish session for %s without cross user permission", Integer.valueOf(UserHandle.getCallingUserId()), Integer.valueOf(i)));
        }
        if (!canUserModifyAccounts(i, callingUid)) {
            sendErrorResponse(iAccountManagerResponse, 100, "User is not allowed to add an account!");
            showCantAddAccount(100, i);
            return;
        }
        int callingPid = Binder.getCallingPid();
        try {
            final Bundle decryptBundle = CryptoHelper.getInstance().decryptBundle(bundle);
            if (decryptBundle == null) {
                sendErrorResponse(iAccountManagerResponse, 8, "failed to decrypt session bundle");
                return;
            }
            final String string = decryptBundle.getString("accountType");
            if (TextUtils.isEmpty(string)) {
                sendErrorResponse(iAccountManagerResponse, 7, "accountType is empty");
                return;
            }
            if (bundle2 != null) {
                decryptBundle.putAll(bundle2);
            }
            decryptBundle.putInt("callerUid", callingUid);
            decryptBundle.putInt("callerPid", callingPid);
            if (!canUserModifyAccountsForType(i, string, callingUid)) {
                sendErrorResponse(iAccountManagerResponse, 101, "User cannot modify accounts of this type (policy).");
                showCantAddAccount(101, i);
                return;
            }
            long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
            try {
                UserAccounts userAccounts = getUserAccounts(i);
                logRecordWithUid(userAccounts, AccountsDb.DEBUG_ACTION_CALLED_ACCOUNT_SESSION_FINISH, "accounts", callingUid);
                new Session(userAccounts, iAccountManagerResponse, string, z, true, null, false, true) { // from class: com.android.server.accounts.AccountManagerService.11
                    @Override // com.android.server.accounts.AccountManagerService.Session
                    public void run() throws RemoteException {
                        this.mAuthenticator.finishSession(this, this.mAccountType, decryptBundle);
                    }

                    @Override // com.android.server.accounts.AccountManagerService.Session
                    protected String toDebugString(long j) {
                        return super.toDebugString(j) + ", finishSession, accountType " + string;
                    }
                }.bind();
            } finally {
                IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
            }
        } catch (GeneralSecurityException e) {
            if (Log.isLoggable(TAG, 3)) {
                Log.v(TAG, "Failed to decrypt session bundle!", e);
            }
            sendErrorResponse(iAccountManagerResponse, 8, "failed to decrypt session bundle");
        }
    }

    private void showCantAddAccount(int i, int i2) {
        Intent createShowAdminSupportIntent;
        DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        if (devicePolicyManagerInternal == null) {
            createShowAdminSupportIntent = getDefaultCantAddAccountIntent(i);
        } else if (i == 100) {
            createShowAdminSupportIntent = devicePolicyManagerInternal.createUserRestrictionSupportIntent(i2, "no_modify_accounts");
        } else {
            createShowAdminSupportIntent = i == 101 ? devicePolicyManagerInternal.createShowAdminSupportIntent(i2, false) : null;
        }
        if (createShowAdminSupportIntent == null) {
            createShowAdminSupportIntent = getDefaultCantAddAccountIntent(i);
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            this.mContext.startActivityAsUser(createShowAdminSupportIntent, new UserHandle(i2));
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private Intent getDefaultCantAddAccountIntent(int i) {
        Intent intent = new Intent(this.mContext, (Class<?>) CantAddAccountActivity.class);
        intent.putExtra("android.accounts.extra.ERROR_CODE", i);
        intent.addFlags(AudioFormat.EVRC);
        return intent;
    }

    public void confirmCredentialsAsUser(IAccountManagerResponse iAccountManagerResponse, final Account account, final Bundle bundle, boolean z, int i) {
        Bundle.setDefusable(bundle, true);
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "confirmCredentials: " + account + ", response " + iAccountManagerResponse + ", expectActivityLaunch " + z + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        if (isCrossUser(callingUid, i)) {
            throw new SecurityException(String.format("User %s trying to confirm account credentials for %s", Integer.valueOf(UserHandle.getCallingUserId()), Integer.valueOf(i)));
        }
        if (iAccountManagerResponse == null) {
            throw new IllegalArgumentException("response is null");
        }
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new Session(getUserAccounts(i), iAccountManagerResponse, account.type, z, true, account.name, true, true) { // from class: com.android.server.accounts.AccountManagerService.12
                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.confirmCredentials(this, account, bundle);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    return super.toDebugString(j) + ", confirmCredentials, " + account.toSafeString();
                }
            }.bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void updateCredentials(IAccountManagerResponse iAccountManagerResponse, final Account account, final String str, boolean z, final Bundle bundle) {
        Bundle.setDefusable(bundle, true);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "updateCredentials: " + account + ", response " + iAccountManagerResponse + ", authTokenType " + str + ", expectActivityLaunch " + z + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        if (iAccountManagerResponse == null) {
            throw new IllegalArgumentException("response is null");
        }
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        }
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new Session(getUserAccounts(callingUserId), iAccountManagerResponse, account.type, z, true, account.name, false, true) { // from class: com.android.server.accounts.AccountManagerService.13
                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.updateCredentials(this, account, str, bundle);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    Bundle bundle2 = bundle;
                    if (bundle2 != null) {
                        bundle2.keySet();
                    }
                    return super.toDebugString(j) + ", updateCredentials, " + account.toSafeString() + ", authTokenType " + str + ", loginOptions " + bundle;
                }
            }.bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void startUpdateCredentialsSession(IAccountManagerResponse iAccountManagerResponse, final Account account, final String str, boolean z, final Bundle bundle) {
        Bundle.setDefusable(bundle, true);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "startUpdateCredentialsSession: " + account + ", response " + iAccountManagerResponse + ", authTokenType " + str + ", expectActivityLaunch " + z + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        if (iAccountManagerResponse == null) {
            throw new IllegalArgumentException("response is null");
        }
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        }
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        boolean checkPermissionAndNote = checkPermissionAndNote(bundle.getString("androidPackageName"), callingUid, "android.permission.GET_PASSWORD");
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new StartAccountSession(getUserAccounts(callingUserId), iAccountManagerResponse, account.type, z, account.name, false, true, checkPermissionAndNote) { // from class: com.android.server.accounts.AccountManagerService.14
                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.startUpdateCredentialsSession(this, account, str, bundle);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    Bundle bundle2 = bundle;
                    if (bundle2 != null) {
                        bundle2.keySet();
                    }
                    return super.toDebugString(j) + ", startUpdateCredentialsSession, " + account.toSafeString() + ", authTokenType " + str + ", loginOptions " + bundle;
                }
            }.bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void isCredentialsUpdateSuggested(IAccountManagerResponse iAccountManagerResponse, final Account account, final String str) {
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "isCredentialsUpdateSuggested: " + account + ", response " + iAccountManagerResponse + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        if (iAccountManagerResponse == null) {
            throw new IllegalArgumentException("response is null");
        }
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        }
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("status token is empty");
        }
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new Session(getUserAccounts(callingUserId), iAccountManagerResponse, account.type, false, false, account.name, false) { // from class: com.android.server.accounts.AccountManagerService.15
                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    return super.toDebugString(j) + ", isCredentialsUpdateSuggested, " + account.toSafeString();
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.isCredentialsUpdateSuggested(this, account, str);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                public void onResult(Bundle bundle) {
                    Bundle.setDefusable(bundle, true);
                    IAccountManagerResponse responseAndClose = getResponseAndClose();
                    if (responseAndClose == null) {
                        return;
                    }
                    if (bundle == null) {
                        AccountManagerService.this.sendErrorResponse(responseAndClose, 5, "null bundle");
                        return;
                    }
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + responseAndClose);
                    }
                    if (bundle.getInt("errorCode", -1) > 0) {
                        AccountManagerService.this.sendErrorResponse(responseAndClose, bundle.getInt("errorCode"), bundle.getString("errorMessage"));
                    } else {
                        if (!bundle.containsKey("booleanResult")) {
                            AccountManagerService.this.sendErrorResponse(responseAndClose, 5, "no result in response");
                            return;
                        }
                        Bundle bundle2 = new Bundle();
                        bundle2.putBoolean("booleanResult", bundle.getBoolean("booleanResult", false));
                        AccountManagerService.this.sendResponse(responseAndClose, bundle2);
                    }
                }
            }.bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void editProperties(IAccountManagerResponse iAccountManagerResponse, final String str, boolean z) {
        int callingUid = Binder.getCallingUid();
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "editProperties: accountType " + str + ", response " + iAccountManagerResponse + ", expectActivityLaunch " + z + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        if (iAccountManagerResponse == null) {
            throw new IllegalArgumentException("response is null");
        }
        if (str == null) {
            throw new IllegalArgumentException("accountType is null");
        }
        int callingUserId = UserHandle.getCallingUserId();
        if (!isAccountManagedByCaller(str, callingUid, callingUserId) && !isSystemUid(callingUid)) {
            throw new SecurityException(String.format("uid %s cannot edit authenticator properites for account type: %s", Integer.valueOf(callingUid), str));
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            new Session(getUserAccounts(callingUserId), iAccountManagerResponse, str, z, true, null, false) { // from class: com.android.server.accounts.AccountManagerService.16
                @Override // com.android.server.accounts.AccountManagerService.Session
                public void run() throws RemoteException {
                    this.mAuthenticator.editProperties(this, this.mAccountType);
                }

                @Override // com.android.server.accounts.AccountManagerService.Session
                protected String toDebugString(long j) {
                    return super.toDebugString(j) + ", editProperties, accountType " + str;
                }
            }.bind();
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean hasAccountAccess(Account account, String str, UserHandle userHandle) {
        if (UserHandle.getAppId(Binder.getCallingUid()) != 1000) {
            throw new SecurityException("Can be called only by system UID");
        }
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(str, "packageName cannot be null");
        Objects.requireNonNull(userHandle, "userHandle cannot be null");
        int identifier = userHandle.getIdentifier();
        Preconditions.checkArgumentInRange(identifier, 0, Integer.MAX_VALUE, "user must be concrete");
        try {
            return hasAccountAccess(account, str, this.mPackageManager.getPackageUidAsUser(str, identifier));
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "hasAccountAccess#Package not found " + e.getMessage());
            return false;
        }
    }

    private String getPackageNameForUid(int i) {
        int i2;
        String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
        if (ArrayUtils.isEmpty(packagesForUid)) {
            return null;
        }
        String str = packagesForUid[0];
        if (packagesForUid.length == 1) {
            return str;
        }
        int i3 = Integer.MAX_VALUE;
        for (String str2 : packagesForUid) {
            try {
                ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(str2, 0);
                if (applicationInfo != null && (i2 = applicationInfo.targetSdkVersion) < i3) {
                    str = str2;
                    i3 = i2;
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasAccountAccess(Account account, String str, int i) {
        if (str == null && (str = getPackageNameForUid(i)) == null) {
            return false;
        }
        if (permissionIsGranted(account, null, i, UserHandle.getUserId(i))) {
            return true;
        }
        int intValue = resolveAccountVisibility(account, str, getUserAccounts(UserHandle.getUserId(i))).intValue();
        return intValue == 1 || intValue == 2;
    }

    public IntentSender createRequestAccountAccessIntentSenderAsUser(Account account, String str, UserHandle userHandle) {
        if (UserHandle.getAppId(Binder.getCallingUid()) != 1000) {
            throw new SecurityException("Can be called only by system UID");
        }
        Objects.requireNonNull(account, "account cannot be null");
        Objects.requireNonNull(str, "packageName cannot be null");
        Objects.requireNonNull(userHandle, "userHandle cannot be null");
        int identifier = userHandle.getIdentifier();
        Preconditions.checkArgumentInRange(identifier, 0, Integer.MAX_VALUE, "user must be concrete");
        try {
            Intent newRequestAccountAccessIntent = newRequestAccountAccessIntent(account, str, this.mPackageManager.getPackageUidAsUser(str, identifier), null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return PendingIntent.getActivityAsUser(this.mContext, 0, newRequestAccountAccessIntent, 1409286144, null, new UserHandle(identifier)).getIntentSender();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.e(TAG, "Unknown package " + str);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent newRequestAccountAccessIntent(final Account account, String str, final int i, final RemoteCallback remoteCallback) {
        return newGrantCredentialsPermissionIntent(account, str, i, new AccountAuthenticatorResponse((IAccountAuthenticatorResponse) new IAccountAuthenticatorResponse.Stub() { // from class: com.android.server.accounts.AccountManagerService.17
            public void onRequestContinued() {
            }

            public void onResult(Bundle bundle) throws RemoteException {
                handleAuthenticatorResponse(true);
            }

            public void onError(int i2, String str2) throws RemoteException {
                handleAuthenticatorResponse(false);
            }

            private void handleAuthenticatorResponse(boolean z) throws RemoteException {
                AccountManagerService accountManagerService = AccountManagerService.this;
                accountManagerService.cancelNotification(accountManagerService.getCredentialPermissionNotificationId(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", i), UserHandle.getUserHandleForUid(i));
                if (remoteCallback != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("booleanResult", z);
                    remoteCallback.sendResult(bundle);
                }
            }
        }), "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", false);
    }

    public boolean someUserHasAccount(Account account) {
        if (!UserHandle.isSameApp(1000, Binder.getCallingUid())) {
            throw new SecurityException("Only system can check for accounts across users");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            AccountAndUser[] allAccountsForSystemProcess = getAllAccountsForSystemProcess();
            for (int length = allAccountsForSystemProcess.length - 1; length >= 0; length--) {
                if (allAccountsForSystemProcess[length].account.equals(account)) {
                    return true;
                }
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class GetAccountsByTypeAndFeatureSession extends Session {
        private volatile Account[] mAccountsOfType;
        private volatile ArrayList<Account> mAccountsWithFeatures;
        private final int mCallingUid;
        private volatile int mCurrentAccount;
        private final String[] mFeatures;
        private final boolean mIncludeManagedNotVisible;
        private final String mPackageName;

        public GetAccountsByTypeAndFeatureSession(UserAccounts userAccounts, IAccountManagerResponse iAccountManagerResponse, String str, String[] strArr, int i, String str2, boolean z) {
            super(AccountManagerService.this, userAccounts, iAccountManagerResponse, str, false, true, null, false);
            this.mAccountsOfType = null;
            this.mAccountsWithFeatures = null;
            this.mCurrentAccount = 0;
            this.mCallingUid = i;
            this.mFeatures = strArr;
            this.mPackageName = str2;
            this.mIncludeManagedNotVisible = z;
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void run() throws RemoteException {
            this.mAccountsOfType = AccountManagerService.this.getAccountsFromCache(this.mAccounts, this.mAccountType, this.mCallingUid, this.mPackageName, this.mIncludeManagedNotVisible);
            this.mAccountsWithFeatures = new ArrayList<>(this.mAccountsOfType.length);
            this.mCurrentAccount = 0;
            checkAccount();
        }

        public void checkAccount() {
            if (this.mCurrentAccount >= this.mAccountsOfType.length) {
                sendResult();
                return;
            }
            IAccountAuthenticator iAccountAuthenticator = this.mAuthenticator;
            if (iAccountAuthenticator == null) {
                if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                    Log.v(AccountManagerService.TAG, "checkAccount: aborting session since we are no longer connected to the authenticator, " + toDebugString());
                    return;
                }
                return;
            }
            try {
                iAccountAuthenticator.hasFeatures(this, this.mAccountsOfType[this.mCurrentAccount], this.mFeatures);
            } catch (RemoteException unused) {
                onError(1, "remote exception");
            }
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        public void onResult(Bundle bundle) {
            Bundle.setDefusable(bundle, true);
            this.mNumResults++;
            if (bundle == null) {
                onError(5, "null bundle");
                return;
            }
            if (bundle.getBoolean("booleanResult", false)) {
                this.mAccountsWithFeatures.add(this.mAccountsOfType[this.mCurrentAccount]);
            }
            this.mCurrentAccount++;
            checkAccount();
        }

        public void sendResult() {
            IAccountManagerResponse responseAndClose = getResponseAndClose();
            if (responseAndClose != null) {
                try {
                    int size = this.mAccountsWithFeatures.size();
                    Account[] accountArr = new Account[size];
                    for (int i = 0; i < size; i++) {
                        accountArr[i] = this.mAccountsWithFeatures.get(i);
                    }
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + responseAndClose);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArray("accounts", accountArr);
                    responseAndClose.onResult(bundle);
                } catch (RemoteException e) {
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, "failure while notifying response", e);
                    }
                }
            }
        }

        @Override // com.android.server.accounts.AccountManagerService.Session
        protected String toDebugString(long j) {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toDebugString(j));
            sb.append(", getAccountsByTypeAndFeatures, ");
            String[] strArr = this.mFeatures;
            sb.append(strArr != null ? TextUtils.join(",", strArr) : null);
            return sb.toString();
        }
    }

    public Account[] getAccounts(int i, String str) {
        int callingUid = Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, str);
        List<String> typesVisibleToCaller = getTypesVisibleToCaller(callingUid, i, str);
        if (typesVisibleToCaller.isEmpty()) {
            return EMPTY_ACCOUNT_ARRAY;
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return getAccountsInternal(getUserAccounts(i), callingUid, str, typesVisibleToCaller, false);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public AccountAndUser[] getRunningAccountsForSystem() {
        try {
            return getAccountsForSystem(ActivityManager.getService().getRunningUserIds());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public AccountAndUser[] getAllAccountsForSystemProcess() {
        List aliveUsers = getUserManager().getAliveUsers();
        int size = aliveUsers.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = ((UserInfo) aliveUsers.get(i)).id;
        }
        return getAccountsForSystem(iArr);
    }

    private AccountAndUser[] getAccountsForSystem(int[] iArr) {
        ArrayList newArrayList = Lists.newArrayList();
        for (int i : iArr) {
            UserAccounts userAccounts = getUserAccounts(i);
            if (userAccounts != null) {
                for (Account account : getAccountsFromCache(userAccounts, null, Binder.getCallingUid(), "android", false)) {
                    newArrayList.add(new AccountAndUser(account, i));
                }
            }
        }
        return (AccountAndUser[]) newArrayList.toArray(new AccountAndUser[newArrayList.size()]);
    }

    public Account[] getAccountsAsUser(String str, int i, String str2) {
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str2);
        return getAccountsAsUserForPackage(str, i, str2, -1, str2, false);
    }

    private Account[] getAccountsAsUserForPackage(String str, int i, String str2, int i2, String str3, boolean z) {
        int callingUid = Binder.getCallingUid();
        if (i != UserHandle.getCallingUserId() && callingUid != 1000 && this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") != 0) {
            throw new SecurityException("User " + UserHandle.getCallingUserId() + " trying to get account for " + i);
        }
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "getAccounts: accountType " + str + ", caller's uid " + Binder.getCallingUid() + ", pid " + Binder.getCallingPid());
        }
        List<String> typesManagedByCaller = getTypesManagedByCaller(callingUid, UserHandle.getUserId(callingUid));
        if (i2 == -1 || (!UserHandle.isSameApp(callingUid, 1000) && (str == null || !typesManagedByCaller.contains(str)))) {
            str2 = str3;
            i2 = callingUid;
        }
        List<String> typesVisibleToCaller = getTypesVisibleToCaller(i2, i, str2);
        if (typesVisibleToCaller.isEmpty() || (str != null && !typesVisibleToCaller.contains(str))) {
            return EMPTY_ACCOUNT_ARRAY;
        }
        if (typesVisibleToCaller.contains(str)) {
            typesVisibleToCaller = new ArrayList<>();
            typesVisibleToCaller.add(str);
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            return getAccountsInternal(getUserAccounts(i), i2, str2, typesVisibleToCaller, z);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private Account[] getAccountsInternal(UserAccounts userAccounts, int i, String str, List<String> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            Account[] accountsFromCache = getAccountsFromCache(userAccounts, it.next(), i, str, z);
            if (accountsFromCache != null) {
                arrayList.addAll(Arrays.asList(accountsFromCache));
            }
        }
        Account[] accountArr = new Account[arrayList.size()];
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            accountArr[i2] = (Account) arrayList.get(i2);
        }
        return accountArr;
    }

    public void addSharedAccountsFromParentUser(int i, int i2, String str) {
        checkManageOrCreateUsersPermission("addSharedAccountsFromParentUser");
        for (Account account : getAccountsAsUser(null, i, str)) {
            addSharedAccountAsUser(account, i2);
        }
    }

    private boolean addSharedAccountAsUser(Account account, int i) {
        UserAccounts userAccounts = getUserAccounts(handleIncomingUser(i));
        userAccounts.accountsDb.deleteSharedAccount(account);
        long insertSharedAccount = userAccounts.accountsDb.insertSharedAccount(account);
        if (insertSharedAccount < 0) {
            Log.w(TAG, "insertAccountIntoDatabase: " + account.toSafeString() + ", skipping the DB insert failed");
            return false;
        }
        logRecord(AccountsDb.DEBUG_ACTION_ACCOUNT_ADD, "shared_accounts", insertSharedAccount, userAccounts);
        return true;
    }

    public boolean renameSharedAccountAsUser(Account account, String str, int i) {
        UserAccounts userAccounts = getUserAccounts(handleIncomingUser(i));
        long findSharedAccountId = userAccounts.accountsDb.findSharedAccountId(account);
        int renameSharedAccount = userAccounts.accountsDb.renameSharedAccount(account, str);
        if (renameSharedAccount > 0) {
            logRecord(AccountsDb.DEBUG_ACTION_ACCOUNT_RENAME, "shared_accounts", findSharedAccountId, userAccounts, IAccountManager.Stub.getCallingUid());
            renameAccountInternal(userAccounts, account, str);
        }
        return renameSharedAccount > 0;
    }

    public boolean removeSharedAccountAsUser(Account account, int i) {
        return removeSharedAccountAsUser(account, i, IAccountManager.Stub.getCallingUid());
    }

    private boolean removeSharedAccountAsUser(Account account, int i, int i2) {
        UserAccounts userAccounts = getUserAccounts(handleIncomingUser(i));
        long findSharedAccountId = userAccounts.accountsDb.findSharedAccountId(account);
        boolean deleteSharedAccount = userAccounts.accountsDb.deleteSharedAccount(account);
        if (deleteSharedAccount) {
            logRecord(AccountsDb.DEBUG_ACTION_ACCOUNT_REMOVE, "shared_accounts", findSharedAccountId, userAccounts, i2);
            removeAccountInternal(userAccounts, account, i2);
        }
        return deleteSharedAccount;
    }

    public Account[] getSharedAccountsAsUser(int i) {
        Account[] accountArr;
        UserAccounts userAccounts = getUserAccounts(handleIncomingUser(i));
        synchronized (userAccounts.dbLock) {
            List<Account> sharedAccounts = userAccounts.accountsDb.getSharedAccounts();
            accountArr = new Account[sharedAccounts.size()];
            sharedAccounts.toArray(accountArr);
        }
        return accountArr;
    }

    public Account[] getAccountsForPackage(String str, int i, String str2) {
        int callingUid = Binder.getCallingUid();
        if (!UserHandle.isSameApp(callingUid, 1000)) {
            throw new SecurityException("getAccountsForPackage() called from unauthorized uid " + callingUid + " with uid=" + i);
        }
        return getAccountsAsUserForPackage(null, UserHandle.getCallingUserId(), str, i, str2, true);
    }

    public Account[] getAccountsByTypeForPackage(String str, String str2, String str3) {
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        this.mAppOpsManager.checkPackage(callingUid, str3);
        try {
            int packageUidAsUser = this.mPackageManager.getPackageUidAsUser(str2, callingUserId);
            if (!UserHandle.isSameApp(callingUid, 1000) && str != null && !isAccountManagedByCaller(str, callingUid, callingUserId)) {
                return EMPTY_ACCOUNT_ARRAY;
            }
            if (!UserHandle.isSameApp(callingUid, 1000) && str == null) {
                return getAccountsAsUserForPackage(str, callingUserId, str2, packageUidAsUser, str3, false);
            }
            return getAccountsAsUserForPackage(str, callingUserId, str2, packageUidAsUser, str3, true);
        } catch (PackageManager.NameNotFoundException e) {
            Slog.e(TAG, "Couldn't determine the packageUid for " + str2 + e);
            return EMPTY_ACCOUNT_ARRAY;
        }
    }

    private boolean needToStartChooseAccountActivity(Account[] accountArr, String str) {
        if (accountArr.length < 1) {
            return false;
        }
        return accountArr.length > 1 || resolveAccountVisibility(accountArr[0], str, getUserAccounts(UserHandle.getCallingUserId())).intValue() == 4;
    }

    private void startChooseAccountActivityWithAccounts(IAccountManagerResponse iAccountManagerResponse, Account[] accountArr, String str) {
        Intent intent = new Intent(this.mContext, (Class<?>) ChooseAccountActivity.class);
        intent.putExtra("accounts", accountArr);
        intent.putExtra("accountManagerResponse", (Parcelable) new AccountManagerResponse(iAccountManagerResponse));
        intent.putExtra("androidPackageName", str);
        this.mContext.startActivityAsUser(intent, UserHandle.of(UserHandle.getCallingUserId()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGetAccountsResult(IAccountManagerResponse iAccountManagerResponse, Account[] accountArr, String str) {
        if (needToStartChooseAccountActivity(accountArr, str)) {
            startChooseAccountActivityWithAccounts(iAccountManagerResponse, accountArr, str);
            return;
        }
        if (accountArr.length == 1) {
            Bundle bundle = new Bundle();
            bundle.putString("authAccount", accountArr[0].name);
            bundle.putString("accountType", accountArr[0].type);
            onResult(iAccountManagerResponse, bundle);
            return;
        }
        onResult(iAccountManagerResponse, new Bundle());
    }

    public void getAccountByTypeAndFeatures(final IAccountManagerResponse iAccountManagerResponse, String str, String[] strArr, final String str2) {
        int callingUid = Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, str2);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "getAccount: accountType " + str + ", response " + iAccountManagerResponse + ", features " + Arrays.toString(strArr) + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        if (iAccountManagerResponse == null) {
            throw new IllegalArgumentException("response is null");
        }
        if (str == null) {
            throw new IllegalArgumentException("accountType is null");
        }
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(callingUserId);
            if (ArrayUtils.isEmpty(strArr)) {
                handleGetAccountsResult(iAccountManagerResponse, getAccountsFromCache(userAccounts, str, callingUid, str2, true), str2);
            } else {
                new GetAccountsByTypeAndFeatureSession(userAccounts, new IAccountManagerResponse.Stub() { // from class: com.android.server.accounts.AccountManagerService.18
                    public void onError(int i, String str3) throws RemoteException {
                    }

                    public void onResult(Bundle bundle) throws RemoteException {
                        Parcelable[] parcelableArray = bundle.getParcelableArray("accounts");
                        Account[] accountArr = new Account[parcelableArray.length];
                        for (int i = 0; i < parcelableArray.length; i++) {
                            accountArr[i] = (Account) parcelableArray[i];
                        }
                        AccountManagerService.this.handleGetAccountsResult(iAccountManagerResponse, accountArr, str2);
                    }
                }, str, strArr, callingUid, str2, true).bind();
            }
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void getAccountsByFeatures(IAccountManagerResponse iAccountManagerResponse, String str, String[] strArr, String str2) {
        int callingUid = Binder.getCallingUid();
        this.mAppOpsManager.checkPackage(callingUid, str2);
        if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, "getAccounts: accountType " + str + ", response " + iAccountManagerResponse + ", features " + Arrays.toString(strArr) + ", caller's uid " + callingUid + ", pid " + Binder.getCallingPid());
        }
        if (iAccountManagerResponse == null) {
            throw new IllegalArgumentException("response is null");
        }
        if (str == null) {
            throw new IllegalArgumentException("accountType is null");
        }
        int callingUserId = UserHandle.getCallingUserId();
        if (!getTypesVisibleToCaller(callingUid, callingUserId, str2).contains(str)) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArray("accounts", EMPTY_ACCOUNT_ARRAY);
            try {
                iAccountManagerResponse.onResult(bundle);
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "Cannot respond to caller do to exception.", e);
                return;
            }
        }
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            UserAccounts userAccounts = getUserAccounts(callingUserId);
            if (strArr != null && strArr.length != 0) {
                new GetAccountsByTypeAndFeatureSession(userAccounts, iAccountManagerResponse, str, strArr, callingUid, str2, false).bind();
                return;
            }
            Account[] accountsFromCache = getAccountsFromCache(userAccounts, str, callingUid, str2, false);
            Bundle bundle2 = new Bundle();
            bundle2.putParcelableArray("accounts", accountsFromCache);
            onResult(iAccountManagerResponse, bundle2);
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void onAccountAccessed(String str) throws RemoteException {
        int callingUid = Binder.getCallingUid();
        if (UserHandle.getAppId(callingUid) == 1000) {
            return;
        }
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (Account account : getAccounts(callingUserId, this.mContext.getOpPackageName())) {
                if (Objects.equals(account.getAccessId(), str) && !hasAccountAccess(account, (String) null, callingUid)) {
                    updateAppPermission(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", callingUid, true);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new AccountManagerServiceShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public abstract class Session extends IAccountAuthenticatorResponse.Stub implements IBinder.DeathRecipient, ServiceConnection {
        final String mAccountName;
        final String mAccountType;
        protected final UserAccounts mAccounts;
        final boolean mAuthDetailsRequired;
        IAccountAuthenticator mAuthenticator;
        final long mCreationTime;
        final boolean mExpectActivityLaunch;
        private int mNumErrors;
        private int mNumRequestContinued;
        public int mNumResults;
        private int mRealCallingPid;
        private int mRealCallingUid;
        IAccountManagerResponse mResponse;
        private final Object mSessionLock;
        private final boolean mStripAuthTokenFromResult;
        final boolean mUpdateLastAuthenticatedTime;

        public abstract void run() throws RemoteException;

        public Session(AccountManagerService accountManagerService, UserAccounts userAccounts, IAccountManagerResponse iAccountManagerResponse, String str, boolean z, boolean z2, String str2, boolean z3) {
            this(userAccounts, iAccountManagerResponse, str, z, z2, str2, z3, false);
        }

        public Session(UserAccounts userAccounts, IAccountManagerResponse iAccountManagerResponse, String str, boolean z, boolean z2, String str2, boolean z3, boolean z4) {
            this.mSessionLock = new Object();
            this.mRealCallingUid = -1;
            this.mRealCallingPid = -1;
            this.mNumResults = 0;
            this.mNumRequestContinued = 0;
            this.mNumErrors = 0;
            this.mAuthenticator = null;
            if (str == null) {
                throw new IllegalArgumentException("accountType is null");
            }
            this.mAccounts = userAccounts;
            this.mStripAuthTokenFromResult = z2;
            this.mResponse = iAccountManagerResponse;
            this.mAccountType = str;
            this.mExpectActivityLaunch = z;
            this.mCreationTime = SystemClock.elapsedRealtime();
            this.mAccountName = str2;
            this.mAuthDetailsRequired = z3;
            this.mUpdateLastAuthenticatedTime = z4;
            synchronized (AccountManagerService.this.mSessions) {
                AccountManagerService.this.mSessions.put(toString(), this);
            }
            scheduleTimeout();
            if (iAccountManagerResponse != null) {
                try {
                    iAccountManagerResponse.asBinder().linkToDeath(this, 0);
                } catch (RemoteException unused) {
                    this.mResponse = null;
                    binderDied();
                }
            }
        }

        IAccountManagerResponse getResponseAndClose() {
            IAccountManagerResponse iAccountManagerResponse = this.mResponse;
            if (iAccountManagerResponse == null) {
                close();
                return null;
            }
            close();
            return iAccountManagerResponse;
        }

        protected boolean checkKeyIntent(int i, Bundle bundle) {
            if (!checkKeyIntentParceledCorrectly(bundle)) {
                EventLog.writeEvent(1397638484, "250588548", Integer.valueOf(i), "");
                return false;
            }
            Intent intent = (Intent) bundle.getParcelable("intent", Intent.class);
            if (intent == null) {
                return true;
            }
            if (intent.getClipData() == null) {
                intent.setClipData(ClipData.newPlainText(null, null));
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ResolveInfo resolveActivityAsUser = AccountManagerService.this.mContext.getPackageManager().resolveActivityAsUser(intent, 0, this.mAccounts.userId);
                if (resolveActivityAsUser == null) {
                    return false;
                }
                ActivityInfo activityInfo = resolveActivityAsUser.activityInfo;
                int i2 = activityInfo.applicationInfo.uid;
                PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
                if (isExportedSystemActivity(activityInfo) || packageManagerInternal.hasSignatureCapability(i2, i, 16)) {
                    return true;
                }
                Log.e(AccountManagerService.TAG, String.format("KEY_INTENT resolved to an Activity (%s) in a package (%s) that does not share a signature with the supplying authenticator (%s).", activityInfo.name, activityInfo.packageName, this.mAccountType));
                return false;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private boolean checkKeyIntentParceledCorrectly(Bundle bundle) {
            Parcel obtain = Parcel.obtain();
            obtain.writeBundle(bundle);
            obtain.setDataPosition(0);
            Bundle readBundle = obtain.readBundle();
            obtain.recycle();
            Intent intent = (Intent) bundle.getParcelable("intent", Intent.class);
            if (intent != null && intent.getClass() != Intent.class) {
                return false;
            }
            Intent intent2 = (Intent) readBundle.getParcelable("intent", Intent.class);
            return intent == null ? intent2 == null : intent.filterEquals(intent2) && intent.getSelector() == intent2.getSelector() && (intent2.getFlags() & 195) == 0;
        }

        private boolean isExportedSystemActivity(ActivityInfo activityInfo) {
            String str = activityInfo.name;
            return "android".equals(activityInfo.packageName) && (GrantCredentialsPermissionActivity.class.getName().equals(str) || CantAddAccountActivity.class.getName().equals(str));
        }

        private void close() {
            synchronized (AccountManagerService.this.mSessions) {
                if (AccountManagerService.this.mSessions.remove(toString()) == null) {
                    return;
                }
                IAccountManagerResponse iAccountManagerResponse = this.mResponse;
                if (iAccountManagerResponse != null) {
                    try {
                        iAccountManagerResponse.asBinder().unlinkToDeath(this, 0);
                    } catch (NoSuchElementException e) {
                        Slog.e(AccountManagerService.TAG, "error unlinking to death", e);
                    }
                    this.mResponse = null;
                }
                cancelTimeout();
                unbind();
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mResponse = null;
            close();
        }

        protected String toDebugString() {
            return toDebugString(SystemClock.elapsedRealtime());
        }

        protected String toDebugString(long j) {
            StringBuilder sb = new StringBuilder();
            sb.append("Session: expectLaunch ");
            sb.append(this.mExpectActivityLaunch);
            sb.append(", connected ");
            sb.append(this.mAuthenticator != null);
            sb.append(", stats (");
            sb.append(this.mNumResults);
            sb.append("/");
            sb.append(this.mNumRequestContinued);
            sb.append("/");
            sb.append(this.mNumErrors);
            sb.append("), lifetime ");
            sb.append((j - this.mCreationTime) / 1000.0d);
            return sb.toString();
        }

        void bind() {
            if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                Log.v(AccountManagerService.TAG, "initiating bind to authenticator type " + this.mAccountType);
            }
            if (bindToAuthenticator(this.mAccountType)) {
                return;
            }
            Log.d(AccountManagerService.TAG, "bind attempt failed for " + toDebugString());
            onError(1, "bind failure");
        }

        void bind(int i, int i2) {
            this.mRealCallingUid = i;
            this.mRealCallingPid = i2;
            bind();
        }

        private void unbind() {
            synchronized (this.mSessionLock) {
                if (this.mAuthenticator != null) {
                    this.mAuthenticator = null;
                    AccountManagerService.this.mContext.unbindService(this);
                }
            }
        }

        private void scheduleTimeout() {
            MessageHandler messageHandler = AccountManagerService.this.mHandler;
            messageHandler.sendMessageDelayed(messageHandler.obtainMessage(3, this), 900000L);
        }

        public void cancelTimeout() {
            AccountManagerService.this.mHandler.removeMessages(3, this);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            synchronized (this.mSessionLock) {
                this.mAuthenticator = IAccountAuthenticator.Stub.asInterface(iBinder);
                try {
                    run();
                } catch (RemoteException unused) {
                    onError(1, "remote exception");
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            IAccountManagerResponse responseAndClose = getResponseAndClose();
            if (responseAndClose != null) {
                try {
                    responseAndClose.onError(1, "disconnected");
                } catch (RemoteException e) {
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, "Session.onServiceDisconnected: caught RemoteException while responding", e);
                    }
                }
            }
        }

        public void onTimedOut() {
            IAccountManagerResponse responseAndClose = getResponseAndClose();
            if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                Log.v(AccountManagerService.TAG, "Session.onTimedOut");
            }
            if (responseAndClose != null) {
                try {
                    responseAndClose.onError(1, "timeout");
                } catch (RemoteException e) {
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, "Session.onTimedOut: caught RemoteException while responding", e);
                    }
                }
            }
        }

        public void onResult(Bundle bundle) {
            IAccountManagerResponse responseAndClose;
            boolean z = true;
            Bundle.setDefusable(bundle, true);
            this.mNumResults++;
            if (bundle != null) {
                boolean z2 = bundle.getBoolean("booleanResult", false);
                boolean z3 = bundle.containsKey("authAccount") && bundle.containsKey("accountType");
                if (!this.mUpdateLastAuthenticatedTime || (!z2 && !z3)) {
                    z = false;
                }
                if (z || this.mAuthDetailsRequired) {
                    boolean isAccountPresentForCaller = AccountManagerService.this.isAccountPresentForCaller(this.mAccountName, this.mAccountType);
                    if (z && isAccountPresentForCaller) {
                        AccountManagerService.this.updateLastAuthenticatedTime(new Account(this.mAccountName, this.mAccountType));
                    }
                    if (this.mAuthDetailsRequired) {
                        bundle.putLong("lastAuthenticatedTime", isAccountPresentForCaller ? this.mAccounts.accountsDb.findAccountLastAuthenticatedTime(new Account(this.mAccountName, this.mAccountType)) : -1L);
                    }
                }
            }
            if (bundle != null && !checkKeyIntent(Binder.getCallingUid(), bundle)) {
                onError(5, "invalid intent in bundle returned");
                return;
            }
            if (bundle != null && !TextUtils.isEmpty(bundle.getString("authtoken"))) {
                String string = bundle.getString("authAccount");
                String string2 = bundle.getString("accountType");
                if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
                    Account account = new Account(string, string2);
                    AccountManagerService accountManagerService = AccountManagerService.this;
                    accountManagerService.cancelNotification(accountManagerService.getSigninRequiredNotificationId(this.mAccounts, account), new UserHandle(this.mAccounts.userId));
                }
            }
            if (this.mExpectActivityLaunch && bundle != null && bundle.containsKey("intent")) {
                responseAndClose = this.mResponse;
            } else {
                responseAndClose = getResponseAndClose();
            }
            if (responseAndClose != null) {
                try {
                    if (bundle == null) {
                        if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                            Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onError() on response " + responseAndClose);
                        }
                        responseAndClose.onError(5, "null bundle returned");
                        return;
                    }
                    if (this.mStripAuthTokenFromResult) {
                        bundle.remove("authtoken");
                        if (!checkKeyIntent(Binder.getCallingUid(), bundle)) {
                            onError(5, "invalid intent in bundle returned");
                            return;
                        }
                    }
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onResult() on response " + responseAndClose);
                    }
                    if (bundle.getInt("errorCode", -1) > 0) {
                        responseAndClose.onError(bundle.getInt("errorCode"), bundle.getString("errorMessage"));
                    } else {
                        responseAndClose.onResult(bundle);
                    }
                } catch (RemoteException e) {
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, "failure while notifying response", e);
                    }
                }
            }
        }

        public void onRequestContinued() {
            this.mNumRequestContinued++;
        }

        public void onError(int i, String str) {
            this.mNumErrors++;
            IAccountManagerResponse responseAndClose = getResponseAndClose();
            if (responseAndClose != null) {
                if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                    Log.v(AccountManagerService.TAG, getClass().getSimpleName() + " calling onError() on response " + responseAndClose);
                }
                try {
                    responseAndClose.onError(i, str);
                    return;
                } catch (RemoteException e) {
                    if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                        Log.v(AccountManagerService.TAG, "Session.onError: caught RemoteException while responding", e);
                        return;
                    }
                    return;
                }
            }
            if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                Log.v(AccountManagerService.TAG, "Session.onError: already closed");
            }
        }

        private boolean bindToAuthenticator(String str) {
            RegisteredServicesCache.ServiceInfo<AuthenticatorDescription> serviceInfo = AccountManagerService.this.mAuthenticatorCache.getServiceInfo(AuthenticatorDescription.newKey(str), this.mAccounts.userId);
            if (serviceInfo == null) {
                if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                    Log.v(AccountManagerService.TAG, "there is no authenticator for " + str + ", bailing out");
                }
                return false;
            }
            if (!AccountManagerService.this.isLocalUnlockedUser(this.mAccounts.userId) && !serviceInfo.componentInfo.directBootAware) {
                Slog.w(AccountManagerService.TAG, "Blocking binding to authenticator " + serviceInfo.componentName + " which isn't encryption aware");
                return false;
            }
            Intent intent = new Intent();
            intent.setAction("android.accounts.AccountAuthenticator");
            intent.setComponent(serviceInfo.componentName);
            intent.putExtra("realCallingUid", this.mRealCallingUid);
            intent.putExtra("realCallingPid", this.mRealCallingPid);
            if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                Log.v(AccountManagerService.TAG, "performing bindService to " + serviceInfo.componentName);
            }
            if (AccountManagerService.this.mContext.bindServiceAsUser(intent, this, AccountManagerService.this.mAuthenticatorCache.getBindInstantServiceAllowed(this.mAccounts.userId) ? 4194305 : 1, UserHandle.of(this.mAccounts.userId))) {
                return true;
            }
            if (Log.isLoggable(AccountManagerService.TAG, 2)) {
                Log.v(AccountManagerService.TAG, "bindService to " + serviceInfo.componentName + " failed");
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class MessageHandler extends Handler {
        MessageHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 3) {
                ((Session) message.obj).onTimedOut();
            } else {
                if (i == 4) {
                    AccountManagerService.this.copyAccountToUser(null, (Account) message.obj, message.arg1, message.arg2);
                    return;
                }
                throw new IllegalStateException("unhandled message: " + message.what);
            }
        }
    }

    private void logRecord(UserAccounts userAccounts, String str, String str2) {
        logRecord(str, str2, -1L, userAccounts);
    }

    private void logRecordWithUid(UserAccounts userAccounts, String str, String str2, int i) {
        logRecord(str, str2, -1L, userAccounts, i);
    }

    private void logRecord(String str, String str2, long j, UserAccounts userAccounts) {
        logRecord(str, str2, j, userAccounts, IAccountManager.Stub.getCallingUid());
    }

    private void logRecord(String str, String str2, long j, UserAccounts userAccounts, int i) {
        long reserveDebugDbInsertionPoint = userAccounts.accountsDb.reserveDebugDbInsertionPoint();
        if (reserveDebugDbInsertionPoint != -1) {
            this.mHandler.post(new Runnable(str, str2, j, userAccounts, i, reserveDebugDbInsertionPoint) { // from class: com.android.server.accounts.AccountManagerService.1LogRecordTask
                private final long accountId;
                private final String action;
                private final int callingUid;
                private final String tableName;
                private final UserAccounts userAccount;
                private final long userDebugDbInsertionPoint;

                {
                    this.action = str;
                    this.tableName = str2;
                    this.accountId = j;
                    this.userAccount = userAccounts;
                    this.callingUid = i;
                    this.userDebugDbInsertionPoint = reserveDebugDbInsertionPoint;
                }

                @Override // java.lang.Runnable
                public void run() {
                    synchronized (this.userAccount.accountsDb.mDebugStatementLock) {
                        SQLiteStatement statementForLogging = this.userAccount.accountsDb.getStatementForLogging();
                        if (statementForLogging == null) {
                            return;
                        }
                        statementForLogging.bindLong(1, this.accountId);
                        statementForLogging.bindString(2, this.action);
                        statementForLogging.bindString(3, AccountManagerService.this.mDateFormat.format(new Date()));
                        statementForLogging.bindLong(4, this.callingUid);
                        statementForLogging.bindString(5, this.tableName);
                        statementForLogging.bindLong(6, this.userDebugDbInsertionPoint);
                        try {
                            try {
                                statementForLogging.execute();
                            } catch (SQLiteFullException | IllegalStateException e) {
                                Slog.w(AccountManagerService.TAG, "Failed to insert a log record. accountId=" + this.accountId + " action=" + this.action + " tableName=" + this.tableName + " Error: " + e);
                            }
                        } finally {
                            statementForLogging.clearBindings();
                        }
                    }
                }
            });
        }
    }

    public IBinder onBind(Intent intent) {
        return asBinder();
    }

    private static boolean scanArgs(String[] strArr, String str) {
        if (strArr != null) {
            for (String str2 : strArr) {
                if (str.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            boolean z = scanArgs(strArr, "--checkin") || scanArgs(strArr, "-c");
            IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
            for (UserInfo userInfo : getUserManager().getUsers()) {
                indentingPrintWriter.println("User " + userInfo + ":");
                indentingPrintWriter.increaseIndent();
                dumpUser(getUserAccounts(userInfo.id), fileDescriptor, indentingPrintWriter, strArr, z);
                indentingPrintWriter.println();
                indentingPrintWriter.decreaseIndent();
            }
        }
    }

    private void dumpUser(UserAccounts userAccounts, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
        boolean isLocalUnlockedUser;
        if (z) {
            synchronized (userAccounts.dbLock) {
                userAccounts.accountsDb.dumpDeAccountsTable(printWriter);
            }
            return;
        }
        Account[] accountsFromCache = getAccountsFromCache(userAccounts, null, 1000, "android", false);
        printWriter.println("Accounts: " + accountsFromCache.length);
        for (Account account : accountsFromCache) {
            printWriter.println("  " + account.toString());
        }
        printWriter.println();
        synchronized (userAccounts.dbLock) {
            userAccounts.accountsDb.dumpDebugTable(printWriter);
        }
        printWriter.println();
        synchronized (this.mSessions) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            printWriter.println("Active Sessions: " + this.mSessions.size());
            Iterator<Session> it = this.mSessions.values().iterator();
            while (it.hasNext()) {
                printWriter.println("  " + it.next().toDebugString(elapsedRealtime));
            }
        }
        printWriter.println();
        this.mAuthenticatorCache.dump(fileDescriptor, printWriter, strArr, userAccounts.userId);
        synchronized (this.mUsers) {
            isLocalUnlockedUser = isLocalUnlockedUser(userAccounts.userId);
        }
        if (isLocalUnlockedUser) {
            printWriter.println();
            synchronized (userAccounts.dbLock) {
                Map<Account, Map<String, Integer>> findAllVisibilityValues = userAccounts.accountsDb.findAllVisibilityValues();
                printWriter.println("Account visibility:");
                for (Account account2 : findAllVisibilityValues.keySet()) {
                    printWriter.println("  " + account2.name);
                    for (Map.Entry<String, Integer> entry : findAllVisibilityValues.get(account2).entrySet()) {
                        printWriter.println("    " + entry.getKey() + ", " + entry.getValue());
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doNotification(UserAccounts userAccounts, Account account, CharSequence charSequence, Intent intent, String str, int i) {
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "doNotification: " + ((Object) charSequence) + " intent:" + intent);
            }
            if (intent.getComponent() != null && GrantCredentialsPermissionActivity.class.getName().equals(intent.getComponent().getClassName())) {
                createNoCredentialsPermissionNotification(account, intent, str, i);
            } else {
                Context contextForUser = getContextForUser(new UserHandle(i));
                NotificationId signinRequiredNotificationId = getSigninRequiredNotificationId(userAccounts, account);
                intent.addCategory(signinRequiredNotificationId.mTag);
                installNotification(signinRequiredNotificationId, new Notification.Builder(contextForUser, SystemNotificationChannels.ACCOUNT).setWhen(0L).setSmallIcon(R.drawable.stat_sys_warning).setColor(contextForUser.getColor(R.color.system_notification_accent_color)).setContentTitle(String.format(contextForUser.getText(R.string.permlab_requestInstallPackages).toString(), account.name)).setContentText(charSequence).setContentIntent(PendingIntent.getActivityAsUser(this.mContext, 0, intent, AudioFormat.AAC_ADIF, null, new UserHandle(i))).build(), str, i);
            }
        } finally {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void installNotification(NotificationId notificationId, Notification notification, String str, int i) {
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            try {
                this.mInjector.getNotificationManager().enqueueNotificationWithTag(str, "android", notificationId.mTag, notificationId.mId, notification, i);
            } catch (RemoteException unused) {
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelNotification(NotificationId notificationId, UserHandle userHandle) {
        cancelNotification(notificationId, this.mContext.getPackageName(), userHandle);
    }

    private void cancelNotification(NotificationId notificationId, String str, UserHandle userHandle) {
        long clearCallingIdentity = IAccountManager.Stub.clearCallingIdentity();
        try {
            this.mInjector.getNotificationManager().cancelNotificationWithTag(str, "android", notificationId.mTag, notificationId.mId, userHandle.getIdentifier());
        } catch (RemoteException unused) {
        } catch (Throwable th) {
            IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
        IAccountManager.Stub.restoreCallingIdentity(clearCallingIdentity);
    }

    private boolean isPermittedForPackage(String str, int i, String... strArr) {
        int permissionToOpCode;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int packageUidAsUser = this.mPackageManager.getPackageUidAsUser(str, i);
            IPackageManager packageManager = ActivityThread.getPackageManager();
            for (String str2 : strArr) {
                if (packageManager.checkPermission(str2, str, i) == 0 && ((permissionToOpCode = AppOpsManager.permissionToOpCode(str2)) == -1 || this.mAppOpsManager.checkOpNoThrow(permissionToOpCode, packageUidAsUser, str) == 0)) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException | RemoteException unused) {
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
        return false;
    }

    private boolean checkPermissionAndNote(String str, int i, String... strArr) {
        for (String str2 : strArr) {
            if (this.mContext.checkCallingOrSelfPermission(str2) == 0) {
                if (Log.isLoggable(TAG, 2)) {
                    Log.v(TAG, "  caller uid " + i + " has " + str2);
                }
                int permissionToOpCode = AppOpsManager.permissionToOpCode(str2);
                if (permissionToOpCode == -1 || this.mAppOpsManager.noteOpNoThrow(permissionToOpCode, i, str) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int handleIncomingUser(int i) {
        try {
            return ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, true, true, "", (String) null);
        } catch (RemoteException unused) {
            return i;
        }
    }

    private boolean isPrivileged(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
            if (packagesForUid == null) {
                Log.d(TAG, "No packages for callingUid " + i);
                return false;
            }
            for (String str : packagesForUid) {
                try {
                    PackageInfo packageInfo = this.mPackageManager.getPackageInfo(str, 0);
                    if (packageInfo != null && (packageInfo.applicationInfo.privateFlags & 8) != 0) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return true;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.w(TAG, "isPrivileged#Package not found " + e.getMessage());
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean permissionIsGranted(Account account, String str, int i, int i2) {
        if (UserHandle.getAppId(i) == 1000) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "Access to " + account + " granted calling uid is system");
            }
            return true;
        }
        if (isPrivileged(i)) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "Access to " + account + " granted calling uid " + i + " privileged");
            }
            return true;
        }
        if (account != null && isAccountManagedByCaller(account.type, i, i2)) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "Access to " + account + " granted calling uid " + i + " manages the account");
            }
            return true;
        }
        if (account != null && hasExplicitlyGrantedPermission(account, str, i)) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "Access to " + account + " granted calling uid " + i + " user granted access");
            }
            return true;
        }
        if (!Log.isLoggable(TAG, 2)) {
            return false;
        }
        Log.v(TAG, "Access to " + account + " not granted for uid " + i);
        return false;
    }

    private boolean isAccountVisibleToCaller(String str, int i, int i2, String str2) {
        if (str == null) {
            return false;
        }
        return getTypesVisibleToCaller(i, i2, str2).contains(str);
    }

    private boolean checkGetAccountsPermission(String str, int i) {
        return isPermittedForPackage(str, i, "android.permission.GET_ACCOUNTS", "android.permission.GET_ACCOUNTS_PRIVILEGED");
    }

    private boolean checkReadContactsPermission(String str, int i) {
        return isPermittedForPackage(str, i, "android.permission.READ_CONTACTS");
    }

    private boolean accountTypeManagesContacts(String str, int i) {
        if (str == null) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Collection<RegisteredServicesCache.ServiceInfo<AuthenticatorDescription>> allServices = this.mAuthenticatorCache.getAllServices(i);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            for (RegisteredServicesCache.ServiceInfo<AuthenticatorDescription> serviceInfo : allServices) {
                if (str.equals(((AuthenticatorDescription) serviceInfo.type).type)) {
                    return isPermittedForPackage(((AuthenticatorDescription) serviceInfo.type).packageName, i, "android.permission.WRITE_CONTACTS");
                }
            }
            return false;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    private int checkPackageSignature(String str, int i, int i2) {
        if (str == null) {
            return 0;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Collection<RegisteredServicesCache.ServiceInfo<AuthenticatorDescription>> allServices = this.mAuthenticatorCache.getAllServices(i2);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
            for (RegisteredServicesCache.ServiceInfo<AuthenticatorDescription> serviceInfo : allServices) {
                if (str.equals(((AuthenticatorDescription) serviceInfo.type).type)) {
                    int i3 = serviceInfo.uid;
                    if (i3 == i) {
                        return 2;
                    }
                    if (packageManagerInternal.hasSignatureCapability(i3, i, 16)) {
                        return 1;
                    }
                }
            }
            return 0;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    private boolean isAccountManagedByCaller(String str, int i, int i2) {
        if (str == null) {
            return false;
        }
        return getTypesManagedByCaller(i, i2).contains(str);
    }

    private List<String> getTypesVisibleToCaller(int i, int i2, String str) {
        return getTypesForCaller(i, i2, true);
    }

    private List<String> getTypesManagedByCaller(int i, int i2) {
        return getTypesForCaller(i, i2, false);
    }

    private List<String> getTypesForCaller(int i, int i2, boolean z) {
        ArrayList arrayList = new ArrayList();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Collection<RegisteredServicesCache.ServiceInfo<AuthenticatorDescription>> allServices = this.mAuthenticatorCache.getAllServices(i2);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
            for (RegisteredServicesCache.ServiceInfo<AuthenticatorDescription> serviceInfo : allServices) {
                if (z || packageManagerInternal.hasSignatureCapability(serviceInfo.uid, i, 16)) {
                    arrayList.add(((AuthenticatorDescription) serviceInfo.type).type);
                }
            }
            return arrayList;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAccountPresentForCaller(String str, String str2) {
        if (getUserAccountsForCaller().accountCache.containsKey(str2)) {
            for (Account account : getUserAccountsForCaller().accountCache.get(str2)) {
                if (account.name.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void checkManageUsersPermission(String str) {
        if (ActivityManager.checkComponentPermission("android.permission.MANAGE_USERS", Binder.getCallingUid(), -1, true) == 0) {
            return;
        }
        throw new SecurityException("You need MANAGE_USERS permission to: " + str);
    }

    private static void checkManageOrCreateUsersPermission(String str) {
        if (ActivityManager.checkComponentPermission("android.permission.MANAGE_USERS", Binder.getCallingUid(), -1, true) == 0 || ActivityManager.checkComponentPermission("android.permission.CREATE_USERS", Binder.getCallingUid(), -1, true) == 0) {
            return;
        }
        throw new SecurityException("You need MANAGE_USERS or CREATE_USERS permission to: " + str);
    }

    private boolean hasExplicitlyGrantedPermission(Account account, String str, int i) {
        long findMatchingGrantsCountAnyToken;
        if (UserHandle.getAppId(i) == 1000) {
            return true;
        }
        UserAccounts userAccounts = getUserAccounts(UserHandle.getUserId(i));
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                if (str != null) {
                    findMatchingGrantsCountAnyToken = userAccounts.accountsDb.findMatchingGrantsCount(i, str, account);
                } else {
                    findMatchingGrantsCountAnyToken = userAccounts.accountsDb.findMatchingGrantsCountAnyToken(i, account);
                }
                boolean z = findMatchingGrantsCountAnyToken > 0;
                if (z || !ActivityManager.isRunningInTestHarness()) {
                    return z;
                }
                Log.d(TAG, "no credentials permission for usage of " + account.toSafeString() + ", " + str + " by uid " + i + " but ignoring since device is in test harness.");
                return true;
            }
        }
    }

    private boolean isSystemUid(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
            if (packagesForUid != null) {
                for (String str : packagesForUid) {
                    try {
                        PackageInfo packageInfo = this.mPackageManager.getPackageInfo(str, 0);
                        if (packageInfo != null && (packageInfo.applicationInfo.flags & 1) != 0) {
                            return true;
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.w(TAG, String.format("Could not find package [%s]", str), e);
                    }
                }
            } else {
                Log.w(TAG, "No known packages with uid " + i);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void checkReadAccountsPermitted(int i, String str, int i2, String str2) {
        if (isAccountVisibleToCaller(str, i, i2, str2)) {
            return;
        }
        String format = String.format("caller uid %s cannot access %s accounts", Integer.valueOf(i), str);
        Log.w(TAG, "  " + format);
        throw new SecurityException(format);
    }

    private boolean canUserModifyAccounts(int i, int i2) {
        return isProfileOwner(i2) || !getUserManager().getUserRestrictions(new UserHandle(i)).getBoolean("no_modify_accounts");
    }

    private boolean canUserModifyAccountsForType(final int i, final String str, final int i2) {
        return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda3
            public final Object getOrThrow() {
                Boolean lambda$canUserModifyAccountsForType$3;
                lambda$canUserModifyAccountsForType$3 = AccountManagerService.this.lambda$canUserModifyAccountsForType$3(i2, i, str);
                return lambda$canUserModifyAccountsForType$3;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$canUserModifyAccountsForType$3(int i, int i2, String str) throws Exception {
        if (isProfileOwner(i)) {
            return Boolean.TRUE;
        }
        String[] accountTypesWithManagementDisabledAsUser = ((DevicePolicyManager) this.mContext.getSystemService("device_policy")).getAccountTypesWithManagementDisabledAsUser(i2);
        if (accountTypesWithManagementDisabledAsUser == null) {
            return Boolean.TRUE;
        }
        for (String str2 : accountTypesWithManagementDisabledAsUser) {
            if (str2.equals(str)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private boolean isProfileOwner(int i) {
        DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        return devicePolicyManagerInternal != null && (devicePolicyManagerInternal.isActiveProfileOwner(i) || devicePolicyManagerInternal.isActiveDeviceOwner(i));
    }

    private boolean canCallerAccessPackage(String str, int i, int i2) {
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        if (!CompatChanges.isChangeEnabled(ENFORCE_PACKAGE_VISIBILITY_FILTERING, i)) {
            return packageManagerInternal.getPackageUid(str, 0L, i2) != -1;
        }
        boolean z = !packageManagerInternal.filterAppAccess(str, i, i2);
        if (!z && Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Package " + str + " is not visible to caller " + i + " for user " + i2);
        }
        return z;
    }

    public void updateAppPermission(Account account, String str, int i, boolean z) throws RemoteException {
        if (UserHandle.getAppId(IAccountManager.Stub.getCallingUid()) != 1000) {
            throw new SecurityException();
        }
        if (z) {
            grantAppPermission(account, str, i);
        } else {
            revokeAppPermission(account, str, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void grantAppPermission(final Account account, String str, final int i) {
        if (account == null || str == null) {
            Log.e(TAG, "grantAppPermission: called with invalid arguments", new Exception());
            return;
        }
        UserAccounts userAccounts = getUserAccounts(UserHandle.getUserId(i));
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                long findDeAccountId = userAccounts.accountsDb.findDeAccountId(account);
                if (findDeAccountId >= 0) {
                    userAccounts.accountsDb.insertGrant(findDeAccountId, str, i);
                }
                cancelNotification(getCredentialPermissionNotificationId(account, str, i), UserHandle.of(userAccounts.userId));
                cancelAccountAccessRequestNotificationIfNeeded(account, i, true);
            }
        }
        Iterator<AccountManagerInternal.OnAppPermissionChangeListener> it = this.mAppPermissionChangeListeners.iterator();
        while (it.hasNext()) {
            final AccountManagerInternal.OnAppPermissionChangeListener next = it.next();
            this.mHandler.post(new Runnable() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    next.onAppPermissionChanged(account, i);
                }
            });
        }
    }

    private void revokeAppPermission(final Account account, String str, final int i) {
        if (account == null || str == null) {
            Log.e(TAG, "revokeAppPermission: called with invalid arguments", new Exception());
            return;
        }
        UserAccounts userAccounts = getUserAccounts(UserHandle.getUserId(i));
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                userAccounts.accountsDb.beginTransaction();
                try {
                    long findDeAccountId = userAccounts.accountsDb.findDeAccountId(account);
                    if (findDeAccountId >= 0) {
                        userAccounts.accountsDb.deleteGrantsByAccountIdAuthTokenTypeAndUid(findDeAccountId, str, i);
                        userAccounts.accountsDb.setTransactionSuccessful();
                    }
                    userAccounts.accountsDb.endTransaction();
                    cancelNotification(getCredentialPermissionNotificationId(account, str, i), UserHandle.of(userAccounts.userId));
                } catch (Throwable th) {
                    userAccounts.accountsDb.endTransaction();
                    throw th;
                }
            }
        }
        Iterator<AccountManagerInternal.OnAppPermissionChangeListener> it = this.mAppPermissionChangeListeners.iterator();
        while (it.hasNext()) {
            final AccountManagerInternal.OnAppPermissionChangeListener next = it.next();
            this.mHandler.post(new Runnable() { // from class: com.android.server.accounts.AccountManagerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    next.onAppPermissionChanged(account, i);
                }
            });
        }
    }

    private void removeAccountFromCacheLocked(UserAccounts userAccounts, Account account) {
        Account[] accountArr = userAccounts.accountCache.get(account.type);
        if (accountArr != null) {
            ArrayList arrayList = new ArrayList();
            for (Account account2 : accountArr) {
                if (!account2.equals(account)) {
                    arrayList.add(account2);
                }
            }
            if (arrayList.isEmpty()) {
                userAccounts.accountCache.remove(account.type);
            } else {
                userAccounts.accountCache.put(account.type, (Account[]) arrayList.toArray(new Account[arrayList.size()]));
            }
        }
        userAccounts.userDataCache.remove(account);
        userAccounts.authTokenCache.remove(account);
        userAccounts.previousNameCache.remove(account);
        userAccounts.visibilityCache.remove(account);
        AccountManager.invalidateLocalAccountsDataCaches();
    }

    private Account insertAccountIntoCacheLocked(UserAccounts userAccounts, Account account) {
        Account[] accountArr = userAccounts.accountCache.get(account.type);
        int length = accountArr != null ? accountArr.length : 0;
        Account[] accountArr2 = new Account[length + 1];
        if (accountArr != null) {
            System.arraycopy(accountArr, 0, accountArr2, 0, length);
        }
        accountArr2[length] = new Account(account, account.getAccessId() != null ? account.getAccessId() : UUID.randomUUID().toString());
        userAccounts.accountCache.put(account.type, accountArr2);
        AccountManager.invalidateLocalAccountsDataCaches();
        return accountArr2[length];
    }

    private Account[] filterAccounts(UserAccounts userAccounts, Account[] accountArr, int i, String str, boolean z) {
        String packageNameForUid = str == null ? getPackageNameForUid(i) : str;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Account account : accountArr) {
            int intValue = resolveAccountVisibility(account, packageNameForUid, userAccounts).intValue();
            if (intValue == 1 || intValue == 2 || (z && intValue == 4)) {
                linkedHashMap.put(account, Integer.valueOf(intValue));
            }
        }
        Map<Account, Integer> filterSharedAccounts = filterSharedAccounts(userAccounts, linkedHashMap, i, str);
        return (Account[]) filterSharedAccounts.keySet().toArray(new Account[filterSharedAccounts.size()]);
    }

    private Map<Account, Integer> filterSharedAccounts(UserAccounts userAccounts, Map<Account, Integer> map, int i, String str) {
        UserInfo userInfo;
        boolean z;
        String str2;
        String str3;
        if (getUserManager() == null || userAccounts == null || userAccounts.userId < 0 || i == 1000 || (userInfo = getUserManager().getUserInfo(userAccounts.userId)) == null || !userInfo.isRestricted()) {
            return map;
        }
        String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
        if (packagesForUid == null) {
            packagesForUid = new String[0];
        }
        String string = this.mContext.getResources().getString(R.string.config_defaultPictureInPictureScreenEdgeInsets);
        for (String str4 : packagesForUid) {
            if (string.contains(";" + str4 + ";")) {
                return map;
            }
        }
        Account[] sharedAccountsAsUser = getSharedAccountsAsUser(userAccounts.userId);
        if (ArrayUtils.isEmpty(sharedAccountsAsUser)) {
            return map;
        }
        String str5 = "";
        try {
            if (str != null) {
                PackageInfo packageInfo = this.mPackageManager.getPackageInfo(str, 0);
                if (packageInfo != null && (str3 = packageInfo.restrictedAccountType) != null) {
                    str5 = str3;
                }
            } else {
                for (String str6 : packagesForUid) {
                    PackageInfo packageInfo2 = this.mPackageManager.getPackageInfo(str6, 0);
                    if (packageInfo2 != null && (str2 = packageInfo2.restrictedAccountType) != null) {
                        str5 = str2;
                        break;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "filterSharedAccounts#Package not found " + e.getMessage());
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<Account, Integer> entry : map.entrySet()) {
            Account key = entry.getKey();
            if (key.type.equals(str5)) {
                linkedHashMap.put(key, entry.getValue());
            } else {
                int length = sharedAccountsAsUser.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        z = false;
                        break;
                    }
                    if (sharedAccountsAsUser[i2].equals(key)) {
                        z = true;
                        break;
                    }
                    i2++;
                }
                if (!z) {
                    linkedHashMap.put(key, entry.getValue());
                }
            }
        }
        return linkedHashMap;
    }

    protected Account[] getAccountsFromCache(UserAccounts userAccounts, String str, int i, String str2, boolean z) {
        Account[] accountArr;
        Preconditions.checkState(!Thread.holdsLock(userAccounts.cacheLock), "Method should not be called with cacheLock");
        if (str != null) {
            synchronized (userAccounts.cacheLock) {
                accountArr = userAccounts.accountCache.get(str);
            }
            if (accountArr == null) {
                return EMPTY_ACCOUNT_ARRAY;
            }
            return filterAccounts(userAccounts, (Account[]) Arrays.copyOf(accountArr, accountArr.length), i, str2, z);
        }
        synchronized (userAccounts.cacheLock) {
            Iterator<Account[]> it = userAccounts.accountCache.values().iterator();
            int i2 = 0;
            while (it.hasNext()) {
                i2 += it.next().length;
            }
            if (i2 == 0) {
                return EMPTY_ACCOUNT_ARRAY;
            }
            Account[] accountArr2 = new Account[i2];
            int i3 = 0;
            for (Account[] accountArr3 : userAccounts.accountCache.values()) {
                System.arraycopy(accountArr3, 0, accountArr2, i3, accountArr3.length);
                i3 += accountArr3.length;
            }
            return filterAccounts(userAccounts, accountArr2, i, str2, z);
        }
    }

    protected void writeUserDataIntoCacheLocked(UserAccounts userAccounts, Account account, String str, String str2) {
        Map<String, String> map = (Map) userAccounts.userDataCache.get(account);
        if (map == null) {
            map = userAccounts.accountsDb.findUserExtrasForAccount(account);
            userAccounts.userDataCache.put(account, map);
        }
        if (str2 == null) {
            map.remove(str);
        } else {
            map.put(str, str2);
        }
    }

    protected TokenCache.Value readCachedTokenInternal(UserAccounts userAccounts, Account account, String str, String str2, byte[] bArr) {
        TokenCache.Value value;
        synchronized (userAccounts.cacheLock) {
            value = userAccounts.accountTokenCaches.get(account, str, str2, bArr);
        }
        return value;
    }

    protected void writeAuthTokenIntoCacheLocked(UserAccounts userAccounts, Account account, String str, String str2) {
        Map<String, String> map = (Map) userAccounts.authTokenCache.get(account);
        if (map == null) {
            map = userAccounts.accountsDb.findAuthTokensByAccount(account);
            userAccounts.authTokenCache.put(account, map);
        }
        if (str2 == null) {
            map.remove(str);
        } else {
            map.put(str, str2);
        }
    }

    protected String readAuthTokenInternal(UserAccounts userAccounts, Account account, String str) {
        String str2;
        synchronized (userAccounts.cacheLock) {
            Map map = (Map) userAccounts.authTokenCache.get(account);
            if (map != null) {
                return (String) map.get(str);
            }
            synchronized (userAccounts.dbLock) {
                synchronized (userAccounts.cacheLock) {
                    Map<String, String> map2 = (Map) userAccounts.authTokenCache.get(account);
                    if (map2 == null) {
                        map2 = userAccounts.accountsDb.findAuthTokensByAccount(account);
                        userAccounts.authTokenCache.put(account, map2);
                    }
                    str2 = map2.get(str);
                }
            }
            return str2;
        }
    }

    private String readUserDataInternal(UserAccounts userAccounts, Account account, String str) {
        Map<String, String> map;
        Map<String, String> map2;
        synchronized (userAccounts.cacheLock) {
            map = (Map) userAccounts.userDataCache.get(account);
        }
        if (map == null) {
            synchronized (userAccounts.dbLock) {
                synchronized (userAccounts.cacheLock) {
                    map2 = (Map) userAccounts.userDataCache.get(account);
                    if (map2 == null) {
                        map2 = userAccounts.accountsDb.findUserExtrasForAccount(account);
                        userAccounts.userDataCache.put(account, map2);
                    }
                }
            }
            map = map2;
        }
        return map.get(str);
    }

    private Context getContextForUser(UserHandle userHandle) {
        try {
            Context context = this.mContext;
            return context.createPackageContextAsUser(context.getPackageName(), 0, userHandle);
        } catch (PackageManager.NameNotFoundException unused) {
            return this.mContext;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResponse(IAccountManagerResponse iAccountManagerResponse, Bundle bundle) {
        try {
            iAccountManagerResponse.onResult(bundle);
        } catch (RemoteException e) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "failure while notifying response", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendErrorResponse(IAccountManagerResponse iAccountManagerResponse, int i, String str) {
        try {
            iAccountManagerResponse.onError(i, str);
        } catch (RemoteException e) {
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "failure while notifying response", e);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class AccountManagerInternalImpl extends AccountManagerInternal {

        @GuardedBy({"mLock"})
        private AccountManagerBackupHelper mBackupHelper;
        private final Object mLock;

        private AccountManagerInternalImpl() {
            this.mLock = new Object();
        }

        public void requestAccountAccess(Account account, String str, int i, RemoteCallback remoteCallback) {
            UserAccounts userAccounts;
            if (account == null) {
                Slog.w(AccountManagerService.TAG, "account cannot be null");
                return;
            }
            if (str == null) {
                Slog.w(AccountManagerService.TAG, "packageName cannot be null");
                return;
            }
            if (i < 0) {
                Slog.w(AccountManagerService.TAG, "user id must be concrete");
                return;
            }
            if (remoteCallback == null) {
                Slog.w(AccountManagerService.TAG, "callback cannot be null");
                return;
            }
            AccountManagerService accountManagerService = AccountManagerService.this;
            if (accountManagerService.resolveAccountVisibility(account, str, accountManagerService.getUserAccounts(i)).intValue() == 3) {
                Slog.w(AccountManagerService.TAG, "requestAccountAccess: account is hidden");
                return;
            }
            if (AccountManagerService.this.hasAccountAccess(account, str, new UserHandle(i))) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("booleanResult", true);
                remoteCallback.sendResult(bundle);
                return;
            }
            try {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    int packageUidAsUser = AccountManagerService.this.mPackageManager.getPackageUidAsUser(str, i);
                    Intent newRequestAccountAccessIntent = AccountManagerService.this.newRequestAccountAccessIntent(account, str, packageUidAsUser, remoteCallback);
                    synchronized (AccountManagerService.this.mUsers) {
                        userAccounts = (UserAccounts) AccountManagerService.this.mUsers.get(i);
                    }
                    SystemNotificationChannels.createAccountChannelForPackage(str, packageUidAsUser, AccountManagerService.this.mContext);
                    AccountManagerService.this.doNotification(userAccounts, account, null, newRequestAccountAccessIntent, str, i);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Slog.e(AccountManagerService.TAG, "Unknown package " + str);
            }
        }

        public void addOnAppPermissionChangeListener(AccountManagerInternal.OnAppPermissionChangeListener onAppPermissionChangeListener) {
            AccountManagerService.this.mAppPermissionChangeListeners.add(onAppPermissionChangeListener);
        }

        public boolean hasAccountAccess(Account account, int i) {
            return AccountManagerService.this.hasAccountAccess(account, (String) null, i);
        }

        public byte[] backupAccountAccessPermissions(int i) {
            byte[] backupAccountAccessPermissions;
            synchronized (this.mLock) {
                if (this.mBackupHelper == null) {
                    this.mBackupHelper = new AccountManagerBackupHelper(AccountManagerService.this, this);
                }
                backupAccountAccessPermissions = this.mBackupHelper.backupAccountAccessPermissions(i);
            }
            return backupAccountAccessPermissions;
        }

        public void restoreAccountAccessPermissions(byte[] bArr, int i) {
            synchronized (this.mLock) {
                if (this.mBackupHelper == null) {
                    this.mBackupHelper = new AccountManagerBackupHelper(AccountManagerService.this, this);
                }
                this.mBackupHelper.restoreAccountAccessPermissions(bArr, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        private final Context mContext;

        public Injector(Context context) {
            this.mContext = context;
        }

        Looper getMessageHandlerLooper() {
            ServiceThread serviceThread = new ServiceThread(AccountManagerService.TAG, -2, true);
            serviceThread.start();
            return serviceThread.getLooper();
        }

        Context getContext() {
            return this.mContext;
        }

        void addLocalService(AccountManagerInternal accountManagerInternal) {
            LocalServices.addService(AccountManagerInternal.class, accountManagerInternal);
        }

        String getDeDatabaseName(int i) {
            return new File(Environment.getDataSystemDeDirectory(i), "accounts_de.db").getPath();
        }

        String getCeDatabaseName(int i) {
            return new File(Environment.getDataSystemCeDirectory(i), "accounts_ce.db").getPath();
        }

        String getPreNDatabaseName(int i) {
            File dataSystemDirectory = Environment.getDataSystemDirectory();
            File file = new File(Environment.getUserSystemDirectory(i), AccountManagerService.PRE_N_DATABASE_NAME);
            if (i == 0) {
                File file2 = new File(dataSystemDirectory, AccountManagerService.PRE_N_DATABASE_NAME);
                if (file2.exists() && !file.exists()) {
                    File userSystemDirectory = Environment.getUserSystemDirectory(i);
                    if (!userSystemDirectory.exists() && !userSystemDirectory.mkdirs()) {
                        throw new IllegalStateException("User dir cannot be created: " + userSystemDirectory);
                    }
                    if (!file2.renameTo(file)) {
                        throw new IllegalStateException("User dir cannot be migrated: " + file);
                    }
                }
            }
            return file.getPath();
        }

        IAccountAuthenticatorCache getAccountAuthenticatorCache() {
            return new AccountAuthenticatorCache(this.mContext);
        }

        INotificationManager getNotificationManager() {
            return NotificationManager.getService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class NotificationId {
        private final int mId;
        final String mTag;

        NotificationId(String str, int i) {
            this.mTag = str;
            this.mId = i;
        }
    }
}
