package com.android.server.slice;

import android.app.AppOpsManager;
import android.app.role.OnRoleHoldersChangedListener;
import android.app.role.RoleManager;
import android.app.slice.ISliceManager;
import android.app.slice.SliceSpec;
import android.app.usage.UsageStatsManagerInternal;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.AssistUtils;
import com.android.server.LocalServices;
import com.android.server.ServiceThread;
import com.android.server.SystemService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SliceManagerService extends ISliceManager.Stub {
    private static final String TAG = "SliceManagerService";
    private final AppOpsManager mAppOps;
    private final UsageStatsManagerInternal mAppUsageStats;
    private final AssistUtils mAssistUtils;

    @GuardedBy({"mLock"})
    private final SparseArray<PackageMatchingCache> mAssistantLookup;
    private String mCachedDefaultHome;
    private final Context mContext;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private final SparseArray<PackageMatchingCache> mHomeLookup;
    private final Object mLock;
    private final PackageManagerInternal mPackageManagerInternal;
    private final SlicePermissionManager mPermissions;

    @GuardedBy({"mLock"})
    private final ArrayMap<Uri, PinnedSliceState> mPinnedSlicesByUri;
    private final BroadcastReceiver mReceiver;
    private RoleObserver mRoleObserver;

    /* JADX INFO: Access modifiers changed from: private */
    public void onUnlockUser(int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void systemReady() {
    }

    public SliceManagerService(Context context) {
        this(context, createHandler().getLooper());
    }

    @VisibleForTesting
    SliceManagerService(Context context, Looper looper) {
        this.mLock = new Object();
        this.mPinnedSlicesByUri = new ArrayMap<>();
        this.mAssistantLookup = new SparseArray<>();
        this.mHomeLookup = new SparseArray<>();
        this.mCachedDefaultHome = null;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.slice.SliceManagerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                if (intExtra == -10000) {
                    Slog.w(SliceManagerService.TAG, "Intent broadcast does not contain user handle: " + intent);
                    return;
                }
                Uri data = intent.getData();
                String schemeSpecificPart = data != null ? data.getSchemeSpecificPart() : null;
                if (schemeSpecificPart == null) {
                    Slog.w(SliceManagerService.TAG, "Intent broadcast does not contain package name: " + intent);
                    return;
                }
                String action = intent.getAction();
                action.hashCode();
                if (!action.equals("android.intent.action.PACKAGE_DATA_CLEARED")) {
                    if (action.equals("android.intent.action.PACKAGE_REMOVED") && !intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                        SliceManagerService.this.mPermissions.removePkg(schemeSpecificPart, intExtra);
                        return;
                    }
                    return;
                }
                SliceManagerService.this.mPermissions.removePkg(schemeSpecificPart, intExtra);
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mContext = context;
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        Objects.requireNonNull(packageManagerInternal);
        this.mPackageManagerInternal = packageManagerInternal;
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mAssistUtils = new AssistUtils(context);
        Handler handler = new Handler(looper);
        this.mHandler = handler;
        this.mAppUsageStats = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
        this.mPermissions = new SlicePermissionManager(context, looper);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        this.mRoleObserver = new RoleObserver();
        context.registerReceiverAsUser(broadcastReceiver, UserHandle.ALL, intentFilter, null, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStopUser(final int i) {
        synchronized (this.mLock) {
            this.mPinnedSlicesByUri.values().removeIf(new Predicate() { // from class: com.android.server.slice.SliceManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onStopUser$0;
                    lambda$onStopUser$0 = SliceManagerService.lambda$onStopUser$0(i, (PinnedSliceState) obj);
                    return lambda$onStopUser$0;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onStopUser$0(int i, PinnedSliceState pinnedSliceState) {
        return ContentProvider.getUserIdFromUri(pinnedSliceState.getUri()) == i;
    }

    public Uri[] getPinnedSlices(String str) {
        verifyCaller(str);
        int identifier = Binder.getCallingUserHandle().getIdentifier();
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            for (PinnedSliceState pinnedSliceState : this.mPinnedSlicesByUri.values()) {
                if (Objects.equals(str, pinnedSliceState.getPkg())) {
                    Uri uri = pinnedSliceState.getUri();
                    if (ContentProvider.getUserIdFromUri(uri, identifier) == identifier) {
                        arrayList.add(ContentProvider.getUriWithoutUserId(uri));
                    }
                }
            }
        }
        return (Uri[]) arrayList.toArray(new Uri[arrayList.size()]);
    }

    public void pinSlice(final String str, Uri uri, SliceSpec[] sliceSpecArr, IBinder iBinder) throws RemoteException {
        verifyCaller(str);
        enforceAccess(str, uri);
        final int identifier = Binder.getCallingUserHandle().getIdentifier();
        Uri maybeAddUserId = ContentProvider.maybeAddUserId(uri, identifier);
        final String providerPkg = getProviderPkg(maybeAddUserId, identifier);
        getOrCreatePinnedSlice(maybeAddUserId, providerPkg).pin(str, sliceSpecArr, iBinder);
        this.mHandler.post(new Runnable() { // from class: com.android.server.slice.SliceManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SliceManagerService.this.lambda$pinSlice$1(providerPkg, str, identifier);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pinSlice$1(String str, String str2, int i) {
        if (str == null || Objects.equals(str2, str)) {
            return;
        }
        this.mAppUsageStats.reportEvent(str, i, (isAssistant(str2, i) || isDefaultHomeApp(str2, i)) ? 13 : 14);
    }

    public void unpinSlice(String str, Uri uri, IBinder iBinder) throws RemoteException {
        verifyCaller(str);
        enforceAccess(str, uri);
        Uri maybeAddUserId = ContentProvider.maybeAddUserId(uri, Binder.getCallingUserHandle().getIdentifier());
        try {
            PinnedSliceState pinnedSlice = getPinnedSlice(maybeAddUserId);
            if (pinnedSlice == null || !pinnedSlice.unpin(str, iBinder)) {
                return;
            }
            removePinnedSlice(maybeAddUserId);
        } catch (IllegalStateException e) {
            Slog.w(TAG, e.getMessage());
        }
    }

    public boolean hasSliceAccess(String str) throws RemoteException {
        verifyCaller(str);
        return hasFullSliceAccess(str, Binder.getCallingUserHandle().getIdentifier());
    }

    public SliceSpec[] getPinnedSpecs(Uri uri, String str) throws RemoteException {
        verifyCaller(str);
        enforceAccess(str, uri);
        return getPinnedSlice(ContentProvider.maybeAddUserId(uri, Binder.getCallingUserHandle().getIdentifier())).getSpecs();
    }

    public void grantSlicePermission(String str, String str2, Uri uri) throws RemoteException {
        verifyCaller(str);
        int identifier = Binder.getCallingUserHandle().getIdentifier();
        enforceOwner(str, uri, identifier);
        this.mPermissions.grantSliceAccess(str2, identifier, str, identifier, uri);
    }

    public void revokeSlicePermission(String str, String str2, Uri uri) throws RemoteException {
        verifyCaller(str);
        int identifier = Binder.getCallingUserHandle().getIdentifier();
        enforceOwner(str, uri, identifier);
        this.mPermissions.revokeSliceAccess(str2, identifier, str, identifier, uri);
    }

    public int checkSlicePermission(Uri uri, String str, int i, int i2, String[] strArr) {
        return checkSlicePermissionInternal(uri, str, null, i, i2, strArr);
    }

    private int checkSlicePermissionInternal(Uri uri, String str, String str2, int i, int i2, String[] strArr) {
        int userId = UserHandle.getUserId(i2);
        if (str2 == null) {
            String[] packagesForUid = this.mContext.getPackageManager().getPackagesForUid(i2);
            int length = packagesForUid.length;
            int i3 = 0;
            while (i3 < length) {
                int i4 = i3;
                if (checkSlicePermissionInternal(uri, str, packagesForUid[i3], i, i2, strArr) == 0) {
                    return 0;
                }
                i3 = i4 + 1;
            }
            return -1;
        }
        if (hasFullSliceAccess(str2, userId) || this.mPermissions.hasPermission(str2, userId, uri)) {
            return 0;
        }
        if (strArr != null && str != null) {
            enforceOwner(str, uri, userId);
            verifyCaller(str);
            for (String str3 : strArr) {
                if (this.mContext.checkPermission(str3, i, i2) == 0) {
                    int userIdFromUri = ContentProvider.getUserIdFromUri(uri, userId);
                    this.mPermissions.grantSliceAccess(str2, userId, getProviderPkg(uri, userIdFromUri), userIdFromUri, uri);
                    return 0;
                }
            }
        }
        return -1;
    }

    public void grantPermissionFromUser(Uri uri, String str, String str2, boolean z) {
        verifyCaller(str2);
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_SLICE_PERMISSIONS", "Slice granting requires MANAGE_SLICE_PERMISSIONS");
        int identifier = Binder.getCallingUserHandle().getIdentifier();
        if (z) {
            this.mPermissions.grantFullAccess(str, identifier);
        } else {
            Uri build = uri.buildUpon().path("").build();
            int userIdFromUri = ContentProvider.getUserIdFromUri(build, identifier);
            this.mPermissions.grantSliceAccess(str, identifier, getProviderPkg(build, userIdFromUri), userIdFromUri, build);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mContext.getContentResolver().notifyChange(uri, null);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public byte[] getBackupPayload(int i) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Caller must be system");
        }
        if (i != 0) {
            Slog.w(TAG, "getBackupPayload: cannot backup policy for user " + i);
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            XmlSerializer newSerializer = XmlPullParserFactory.newInstance().newSerializer();
            newSerializer.setOutput(byteArrayOutputStream, Xml.Encoding.UTF_8.name());
            this.mPermissions.writeBackup(newSerializer);
            newSerializer.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | XmlPullParserException e) {
            Slog.w(TAG, "getBackupPayload: error writing payload for user " + i, e);
            return null;
        }
    }

    public void applyRestore(byte[] bArr, int i) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Caller must be system");
        }
        if (bArr == null) {
            Slog.w(TAG, "applyRestore: no payload to restore for user " + i);
            return;
        }
        if (i != 0) {
            Slog.w(TAG, "applyRestore: cannot restore policy for user " + i);
            return;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(byteArrayInputStream, Xml.Encoding.UTF_8.name());
            this.mPermissions.readRestore(newPullParser);
        } catch (IOException | NumberFormatException | XmlPullParserException e) {
            Slog.w(TAG, "applyRestore: error reading payload", e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new SliceShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    private void enforceOwner(String str, Uri uri, int i) {
        if (!Objects.equals(getProviderPkg(uri, i), str) || str == null) {
            throw new SecurityException("Caller must own " + uri);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removePinnedSlice(Uri uri) {
        synchronized (this.mLock) {
            this.mPinnedSlicesByUri.remove(uri).destroy();
        }
    }

    private PinnedSliceState getPinnedSlice(Uri uri) {
        PinnedSliceState pinnedSliceState;
        synchronized (this.mLock) {
            pinnedSliceState = this.mPinnedSlicesByUri.get(uri);
            if (pinnedSliceState == null) {
                throw new IllegalStateException(String.format("Slice %s not pinned", uri.toString()));
            }
        }
        return pinnedSliceState;
    }

    private PinnedSliceState getOrCreatePinnedSlice(Uri uri, String str) {
        PinnedSliceState pinnedSliceState;
        synchronized (this.mLock) {
            pinnedSliceState = this.mPinnedSlicesByUri.get(uri);
            if (pinnedSliceState == null) {
                pinnedSliceState = createPinnedSlice(uri, str);
                this.mPinnedSlicesByUri.put(uri, pinnedSliceState);
            }
        }
        return pinnedSliceState;
    }

    @VisibleForTesting
    protected PinnedSliceState createPinnedSlice(Uri uri, String str) {
        return new PinnedSliceState(this, uri, str);
    }

    public Object getLock() {
        return this.mLock;
    }

    public Context getContext() {
        return this.mContext;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    protected int checkAccess(String str, Uri uri, int i, int i2) {
        return checkSlicePermissionInternal(uri, null, str, i2, i, null);
    }

    private String getProviderPkg(Uri uri, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ProviderInfo resolveContentProviderAsUser = this.mContext.getPackageManager().resolveContentProviderAsUser(ContentProvider.getUriWithoutUserId(uri).getAuthority(), 0, ContentProvider.getUserIdFromUri(uri, i));
            return resolveContentProviderAsUser == null ? null : resolveContentProviderAsUser.packageName;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void enforceCrossUser(String str, Uri uri) {
        int identifier = Binder.getCallingUserHandle().getIdentifier();
        if (ContentProvider.getUserIdFromUri(uri, identifier) != identifier) {
            getContext().enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "Slice interaction across users requires INTERACT_ACROSS_USERS_FULL");
        }
    }

    private void enforceAccess(String str, Uri uri) throws RemoteException {
        if (checkAccess(str, uri, Binder.getCallingUid(), Binder.getCallingPid()) != 0 && !Objects.equals(str, getProviderPkg(uri, ContentProvider.getUserIdFromUri(uri, Binder.getCallingUserHandle().getIdentifier())))) {
            throw new SecurityException("Access to slice " + uri + " is required");
        }
        enforceCrossUser(str, uri);
    }

    private void verifyCaller(String str) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
    }

    private boolean hasFullSliceAccess(String str, int i) {
        boolean z;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (!isDefaultHomeApp(str, i) && !isAssistant(str, i)) {
                if (!isGrantedFullAccess(str, i)) {
                    z = false;
                    return z;
                }
            }
            z = true;
            return z;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean isAssistant(String str, int i) {
        return getAssistantMatcher(i).matches(str);
    }

    private boolean isDefaultHomeApp(String str, int i) {
        return getHomeMatcher(i).matches(str);
    }

    private PackageMatchingCache getAssistantMatcher(final int i) {
        PackageMatchingCache packageMatchingCache = this.mAssistantLookup.get(i);
        if (packageMatchingCache != null) {
            return packageMatchingCache;
        }
        PackageMatchingCache packageMatchingCache2 = new PackageMatchingCache(new Supplier() { // from class: com.android.server.slice.SliceManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                String lambda$getAssistantMatcher$2;
                lambda$getAssistantMatcher$2 = SliceManagerService.this.lambda$getAssistantMatcher$2(i);
                return lambda$getAssistantMatcher$2;
            }
        });
        this.mAssistantLookup.put(i, packageMatchingCache2);
        return packageMatchingCache2;
    }

    private PackageMatchingCache getHomeMatcher(final int i) {
        PackageMatchingCache packageMatchingCache = this.mHomeLookup.get(i);
        if (packageMatchingCache != null) {
            return packageMatchingCache;
        }
        PackageMatchingCache packageMatchingCache2 = new PackageMatchingCache(new Supplier() { // from class: com.android.server.slice.SliceManagerService$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                String lambda$getHomeMatcher$3;
                lambda$getHomeMatcher$3 = SliceManagerService.this.lambda$getHomeMatcher$3(i);
                return lambda$getHomeMatcher$3;
            }
        });
        this.mHomeLookup.put(i, packageMatchingCache2);
        return packageMatchingCache2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: getAssistant, reason: merged with bridge method [inline-methods] */
    public String lambda$getAssistantMatcher$2(int i) {
        ComponentName assistComponentForUser = this.mAssistUtils.getAssistComponentForUser(i);
        if (assistComponentForUser == null) {
            return null;
        }
        return assistComponentForUser.getPackageName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* renamed from: getDefaultHome, reason: merged with bridge method [inline-methods] */
    public String lambda$getHomeMatcher$3(int i) {
        String str = this.mCachedDefaultHome;
        if (str != null) {
            return str;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ArrayList arrayList = new ArrayList();
            ComponentName homeActivitiesAsUser = this.mPackageManagerInternal.getHomeActivitiesAsUser(arrayList, i);
            this.mCachedDefaultHome = homeActivitiesAsUser != null ? homeActivitiesAsUser.getPackageName() : null;
            if (homeActivitiesAsUser == null) {
                int size = arrayList.size();
                int i2 = Integer.MIN_VALUE;
                for (int i3 = 0; i3 < size; i3++) {
                    ResolveInfo resolveInfo = (ResolveInfo) arrayList.get(i3);
                    if (resolveInfo.activityInfo.applicationInfo.isSystemApp() && resolveInfo.priority >= i2) {
                        homeActivitiesAsUser = resolveInfo.activityInfo.getComponentName();
                        i2 = resolveInfo.priority;
                    }
                }
            }
            return homeActivitiesAsUser != null ? homeActivitiesAsUser.getPackageName() : null;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void invalidateCachedDefaultHome() {
        this.mCachedDefaultHome = null;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class RoleObserver implements OnRoleHoldersChangedListener {
        private final Executor mExecutor;
        private RoleManager mRm;

        RoleObserver() {
            this.mExecutor = SliceManagerService.this.mContext.getMainExecutor();
            register();
        }

        public void register() {
            RoleManager roleManager = (RoleManager) SliceManagerService.this.mContext.getSystemService(RoleManager.class);
            this.mRm = roleManager;
            if (roleManager != null) {
                roleManager.addOnRoleHoldersChangedListenerAsUser(this.mExecutor, this, UserHandle.ALL);
                SliceManagerService.this.invalidateCachedDefaultHome();
            }
        }

        public void onRoleHoldersChanged(String str, UserHandle userHandle) {
            if ("android.app.role.HOME".equals(str)) {
                SliceManagerService.this.invalidateCachedDefaultHome();
            }
        }
    }

    private boolean isGrantedFullAccess(String str, int i) {
        return this.mPermissions.hasFullAccess(str, i);
    }

    private static ServiceThread createHandler() {
        ServiceThread serviceThread = new ServiceThread(TAG, 10, true);
        serviceThread.start();
        return serviceThread;
    }

    public String[] getAllPackagesGranted(String str) {
        String providerPkg = getProviderPkg(new Uri.Builder().scheme("content").authority(str).build(), 0);
        return providerPkg == null ? new String[0] : this.mPermissions.getAllPackagesGranted(providerPkg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PackageMatchingCache {
        private String mCurrentPkg;
        private final Supplier<String> mPkgSource;

        public PackageMatchingCache(Supplier<String> supplier) {
            this.mPkgSource = supplier;
        }

        public boolean matches(String str) {
            if (str == null) {
                return false;
            }
            if (str.equals(this.mCurrentPkg)) {
                return true;
            }
            String str2 = this.mPkgSource.get();
            this.mCurrentPkg = str2;
            return str.equals(str2);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Lifecycle extends SystemService {
        private SliceManagerService mService;

        public Lifecycle(Context context) {
            super(context);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.slice.SliceManagerService, android.os.IBinder] */
        public void onStart() {
            ?? sliceManagerService = new SliceManagerService(getContext());
            this.mService = sliceManagerService;
            publishBinderService("slice", (IBinder) sliceManagerService);
        }

        public void onBootPhase(int i) {
            if (i == 550) {
                this.mService.systemReady();
            }
        }

        public void onUserUnlocking(SystemService.TargetUser targetUser) {
            this.mService.onUnlockUser(targetUser.getUserIdentifier());
        }

        public void onUserStopping(SystemService.TargetUser targetUser) {
            this.mService.onStopUser(targetUser.getUserIdentifier());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class SliceGrant {
        private final String mPkg;
        private final Uri mUri;
        private final int mUserId;

        public SliceGrant(Uri uri, String str, int i) {
            this.mUri = uri;
            this.mPkg = str;
            this.mUserId = i;
        }

        public int hashCode() {
            return this.mUri.hashCode() + this.mPkg.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof SliceGrant)) {
                return false;
            }
            SliceGrant sliceGrant = (SliceGrant) obj;
            return Objects.equals(sliceGrant.mUri, this.mUri) && Objects.equals(sliceGrant.mPkg, this.mPkg) && sliceGrant.mUserId == this.mUserId;
        }
    }
}
