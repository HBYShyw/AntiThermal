package com.android.server.pm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.InstantAppInfo;
import android.content.pm.PermissionInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.permission.PermissionManager;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.PackageUtils;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.job.controllers.JobStatus;
import com.android.server.pm.InstantAppRegistry;
import com.android.server.pm.parsing.PackageInfoUtils;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.PackageStateUtils;
import com.android.server.pm.pkg.PackageUserStateInternal;
import com.android.server.utils.Snappable;
import com.android.server.utils.SnapshotCache;
import com.android.server.utils.Watchable;
import com.android.server.utils.WatchableImpl;
import com.android.server.utils.Watched;
import com.android.server.utils.WatchedSparseArray;
import com.android.server.utils.WatchedSparseBooleanArray;
import com.android.server.utils.Watcher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import libcore.io.IoUtils;
import libcore.util.HexEncoding;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class InstantAppRegistry implements Watchable, Snappable {
    private static final String ATTR_GRANTED = "granted";
    private static final String ATTR_LABEL = "label";
    private static final String ATTR_NAME = "name";
    private static final boolean DEBUG = false;
    private static final long DEFAULT_INSTALLED_INSTANT_APP_MAX_CACHE_PERIOD = 15552000000L;
    static final long DEFAULT_INSTALLED_INSTANT_APP_MIN_CACHE_PERIOD = 604800000;
    private static final long DEFAULT_UNINSTALLED_INSTANT_APP_MAX_CACHE_PERIOD = 15552000000L;
    static final long DEFAULT_UNINSTALLED_INSTANT_APP_MIN_CACHE_PERIOD = 604800000;
    private static final String INSTANT_APPS_FOLDER = "instant";
    private static final String INSTANT_APP_ANDROID_ID_FILE = "android_id";
    private static final String INSTANT_APP_COOKIE_FILE_PREFIX = "cookie_";
    private static final String INSTANT_APP_COOKIE_FILE_SIFFIX = ".dat";
    private static final String INSTANT_APP_ICON_FILE = "icon.png";
    private static final String INSTANT_APP_METADATA_FILE = "metadata.xml";
    private static final String LOG_TAG = "InstantAppRegistry";
    private static final String TAG_PACKAGE = "package";
    private static final String TAG_PERMISSION = "permission";
    private static final String TAG_PERMISSIONS = "permissions";
    private final Context mContext;
    private final CookiePersistence mCookiePersistence;
    private final DeletePackageHelper mDeletePackageHelper;

    @Watched
    @GuardedBy({"mLock"})
    private final WatchedSparseArray<WatchedSparseBooleanArray> mInstalledInstantAppUids;

    @Watched
    @GuardedBy({"mLock"})
    private final WatchedSparseArray<WatchedSparseArray<WatchedSparseBooleanArray>> mInstantGrants;
    private final Object mLock;
    private final Watcher mObserver;
    private final PermissionManagerServiceInternal mPermissionManager;
    private final SnapshotCache<InstantAppRegistry> mSnapshot;

    @Watched
    @GuardedBy({"mLock"})
    private final WatchedSparseArray<List<UninstalledInstantAppState>> mUninstalledInstantApps;
    private final UserManagerInternal mUserManager;
    private final WatchableImpl mWatchable;

    @Override // com.android.server.utils.Watchable
    public void registerObserver(Watcher watcher) {
        this.mWatchable.registerObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(Watcher watcher) {
        this.mWatchable.unregisterObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(Watcher watcher) {
        return this.mWatchable.isRegisteredObserver(watcher);
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(Watchable watchable) {
        this.mWatchable.dispatchChange(watchable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onChanged() {
        dispatchChange(this);
    }

    private SnapshotCache<InstantAppRegistry> makeCache() {
        return new SnapshotCache<InstantAppRegistry>(this, this) { // from class: com.android.server.pm.InstantAppRegistry.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public InstantAppRegistry createSnapshot() {
                InstantAppRegistry instantAppRegistry = new InstantAppRegistry();
                instantAppRegistry.mWatchable.seal();
                return instantAppRegistry;
            }
        };
    }

    public InstantAppRegistry(Context context, PermissionManagerServiceInternal permissionManagerServiceInternal, UserManagerInternal userManagerInternal, DeletePackageHelper deletePackageHelper) {
        this.mLock = new Object();
        this.mWatchable = new WatchableImpl();
        Watcher watcher = new Watcher() { // from class: com.android.server.pm.InstantAppRegistry.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                InstantAppRegistry.this.onChanged();
            }
        };
        this.mObserver = watcher;
        this.mContext = context;
        this.mPermissionManager = permissionManagerServiceInternal;
        this.mUserManager = userManagerInternal;
        this.mDeletePackageHelper = deletePackageHelper;
        this.mCookiePersistence = new CookiePersistence(BackgroundThread.getHandler().getLooper());
        WatchedSparseArray<List<UninstalledInstantAppState>> watchedSparseArray = new WatchedSparseArray<>();
        this.mUninstalledInstantApps = watchedSparseArray;
        WatchedSparseArray<WatchedSparseArray<WatchedSparseBooleanArray>> watchedSparseArray2 = new WatchedSparseArray<>();
        this.mInstantGrants = watchedSparseArray2;
        WatchedSparseArray<WatchedSparseBooleanArray> watchedSparseArray3 = new WatchedSparseArray<>();
        this.mInstalledInstantAppUids = watchedSparseArray3;
        watchedSparseArray.registerObserver(watcher);
        watchedSparseArray2.registerObserver(watcher);
        watchedSparseArray3.registerObserver(watcher);
        Watchable.verifyWatchedAttributes(this, watcher);
        this.mSnapshot = makeCache();
    }

    private InstantAppRegistry(InstantAppRegistry instantAppRegistry) {
        this.mLock = new Object();
        this.mWatchable = new WatchableImpl();
        this.mObserver = new Watcher() { // from class: com.android.server.pm.InstantAppRegistry.1
            @Override // com.android.server.utils.Watcher
            public void onChange(Watchable watchable) {
                InstantAppRegistry.this.onChanged();
            }
        };
        this.mContext = instantAppRegistry.mContext;
        this.mPermissionManager = instantAppRegistry.mPermissionManager;
        this.mUserManager = instantAppRegistry.mUserManager;
        this.mDeletePackageHelper = instantAppRegistry.mDeletePackageHelper;
        this.mCookiePersistence = null;
        this.mUninstalledInstantApps = new WatchedSparseArray<>(instantAppRegistry.mUninstalledInstantApps);
        this.mInstantGrants = new WatchedSparseArray<>(instantAppRegistry.mInstantGrants);
        this.mInstalledInstantAppUids = new WatchedSparseArray<>(instantAppRegistry.mInstalledInstantAppUids);
        this.mSnapshot = null;
    }

    @Override // com.android.server.utils.Snappable
    public InstantAppRegistry snapshot() {
        return this.mSnapshot.snapshot();
    }

    public byte[] getInstantAppCookie(AndroidPackage androidPackage, int i) {
        synchronized (this.mLock) {
            byte[] pendingPersistCookieLPr = this.mCookiePersistence.getPendingPersistCookieLPr(androidPackage, i);
            if (pendingPersistCookieLPr != null) {
                return pendingPersistCookieLPr;
            }
            File peekInstantCookieFile = peekInstantCookieFile(androidPackage.getPackageName(), i);
            if (peekInstantCookieFile != null && peekInstantCookieFile.exists()) {
                try {
                    return IoUtils.readFileAsByteArray(peekInstantCookieFile.toString());
                } catch (IOException unused) {
                    Slog.w(LOG_TAG, "Error reading cookie file: " + peekInstantCookieFile);
                }
            }
            return null;
        }
    }

    public boolean setInstantAppCookie(AndroidPackage androidPackage, byte[] bArr, int i, int i2) {
        synchronized (this.mLock) {
            if (bArr != null) {
                if (bArr.length > 0 && bArr.length > i) {
                    Slog.e(LOG_TAG, "Instant app cookie for package " + androidPackage.getPackageName() + " size " + bArr.length + " bytes while max size is " + i);
                    return false;
                }
            }
            this.mCookiePersistence.schedulePersistLPw(i2, androidPackage, bArr);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistInstantApplicationCookie(byte[] bArr, String str, File file, int i) {
        synchronized (this.mLock) {
            File instantApplicationDir = getInstantApplicationDir(str, i);
            if (!instantApplicationDir.exists() && !instantApplicationDir.mkdirs()) {
                Slog.e(LOG_TAG, "Cannot create instant app cookie directory");
                return;
            }
            if (file.exists() && !file.delete()) {
                Slog.e(LOG_TAG, "Cannot delete instant app cookie file");
            }
            if (bArr != null && bArr.length > 0) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    try {
                        fileOutputStream.write(bArr, 0, bArr.length);
                        fileOutputStream.close();
                    } finally {
                    }
                } catch (IOException e) {
                    Slog.e(LOG_TAG, "Error writing instant app cookie file: " + file, e);
                }
            }
        }
    }

    public Bitmap getInstantAppIcon(String str, int i) {
        synchronized (this.mLock) {
            File file = new File(getInstantApplicationDir(str, i), INSTANT_APP_ICON_FILE);
            if (!file.exists()) {
                return null;
            }
            return BitmapFactory.decodeFile(file.toString());
        }
    }

    public String getInstantAppAndroidId(String str, int i) {
        synchronized (this.mLock) {
            File file = new File(getInstantApplicationDir(str, i), INSTANT_APP_ANDROID_ID_FILE);
            if (file.exists()) {
                try {
                    return IoUtils.readFileAsString(file.getAbsolutePath());
                } catch (IOException e) {
                    Slog.e(LOG_TAG, "Failed to read instant app android id file: " + file, e);
                }
            }
            byte[] bArr = new byte[8];
            new SecureRandom().nextBytes(bArr);
            String encodeToString = HexEncoding.encodeToString(bArr, false);
            File instantApplicationDir = getInstantApplicationDir(str, i);
            if (!instantApplicationDir.exists() && !instantApplicationDir.mkdirs()) {
                Slog.e(LOG_TAG, "Cannot create instant app cookie directory");
                return encodeToString;
            }
            File file2 = new File(getInstantApplicationDir(str, i), INSTANT_APP_ANDROID_ID_FILE);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                try {
                    fileOutputStream.write(encodeToString.getBytes());
                    fileOutputStream.close();
                } catch (Throwable th) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e2) {
                Slog.e(LOG_TAG, "Error writing instant app android id file: " + file2, e2);
            }
            return encodeToString;
        }
    }

    public List<InstantAppInfo> getInstantApps(Computer computer, int i) {
        List<InstantAppInfo> installedInstantApplications = getInstalledInstantApplications(computer, i);
        List<InstantAppInfo> uninstalledInstantApplications = getUninstalledInstantApplications(computer, i);
        if (installedInstantApplications == null) {
            return uninstalledInstantApplications;
        }
        if (uninstalledInstantApplications != null) {
            installedInstantApplications.addAll(uninstalledInstantApplications);
        }
        return installedInstantApplications;
    }

    public void onPackageInstalled(Computer computer, String str, int[] iArr) {
        PackageStateInternal packageStateInternal = computer.getPackageStateInternal(str);
        final AndroidPackageInternal pkg = packageStateInternal == null ? null : packageStateInternal.getPkg();
        if (pkg == null) {
            return;
        }
        synchronized (this.mLock) {
            for (int i : iArr) {
                if (packageStateInternal.getUserStateOrDefault(i).isInstalled()) {
                    propagateInstantAppPermissionsIfNeeded(pkg, i);
                    if (packageStateInternal.getUserStateOrDefault(i).isInstantApp()) {
                        addInstantApp(i, packageStateInternal.getAppId());
                    }
                    removeUninstalledInstantAppStateLPw(new Predicate() { // from class: com.android.server.pm.InstantAppRegistry$$ExternalSyntheticLambda3
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$onPackageInstalled$0;
                            lambda$onPackageInstalled$0 = InstantAppRegistry.lambda$onPackageInstalled$0(AndroidPackage.this, (InstantAppRegistry.UninstalledInstantAppState) obj);
                            return lambda$onPackageInstalled$0;
                        }
                    }, i);
                    File instantApplicationDir = getInstantApplicationDir(pkg.getPackageName(), i);
                    new File(instantApplicationDir, INSTANT_APP_METADATA_FILE).delete();
                    new File(instantApplicationDir, INSTANT_APP_ICON_FILE).delete();
                    File peekInstantCookieFile = peekInstantCookieFile(pkg.getPackageName(), i);
                    if (peekInstantCookieFile != null) {
                        String substring = peekInstantCookieFile.getName().substring(7, r6.length() - 4);
                        if (pkg.getSigningDetails().checkCapability(substring, 1)) {
                            return;
                        }
                        for (String str2 : PackageUtils.computeSignaturesSha256Digests(pkg.getSigningDetails().getSignatures())) {
                            if (str2.equals(substring)) {
                                return;
                            }
                        }
                        Slog.i(LOG_TAG, "Signature for package " + pkg.getPackageName() + " changed - dropping cookie");
                        this.mCookiePersistence.cancelPendingPersistLPw(pkg, i);
                        peekInstantCookieFile.delete();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onPackageInstalled$0(AndroidPackage androidPackage, UninstalledInstantAppState uninstalledInstantAppState) {
        return uninstalledInstantAppState.mInstantAppInfo.getPackageName().equals(androidPackage.getPackageName());
    }

    public void onPackageUninstalled(AndroidPackage androidPackage, PackageSetting packageSetting, int[] iArr, boolean z) {
        if (packageSetting == null) {
            return;
        }
        synchronized (this.mLock) {
            for (int i : iArr) {
                if (!z || !packageSetting.getInstalled(i)) {
                    if (packageSetting.getInstantApp(i)) {
                        addUninstalledInstantAppLPw(packageSetting, i);
                        removeInstantAppLPw(i, packageSetting.getAppId());
                    } else {
                        deleteDir(getInstantApplicationDir(androidPackage.getPackageName(), i));
                        this.mCookiePersistence.cancelPendingPersistLPw(androidPackage, i);
                        removeAppLPw(i, packageSetting.getAppId());
                    }
                }
            }
        }
    }

    public void onUserRemoved(int i) {
        synchronized (this.mLock) {
            this.mUninstalledInstantApps.remove(i);
            this.mInstalledInstantAppUids.remove(i);
            this.mInstantGrants.remove(i);
            deleteDir(getInstantApplicationsDir(i));
        }
    }

    public boolean isInstantAccessGranted(int i, int i2, int i3) {
        synchronized (this.mLock) {
            WatchedSparseArray<WatchedSparseBooleanArray> watchedSparseArray = this.mInstantGrants.get(i);
            if (watchedSparseArray == null) {
                return false;
            }
            WatchedSparseBooleanArray watchedSparseBooleanArray = watchedSparseArray.get(i2);
            if (watchedSparseBooleanArray == null) {
                return false;
            }
            return watchedSparseBooleanArray.get(i3);
        }
    }

    public boolean grantInstantAccess(int i, Intent intent, int i2, int i3) {
        Set<String> categories;
        synchronized (this.mLock) {
            WatchedSparseArray<WatchedSparseBooleanArray> watchedSparseArray = this.mInstalledInstantAppUids;
            if (watchedSparseArray == null) {
                return false;
            }
            WatchedSparseBooleanArray watchedSparseBooleanArray = watchedSparseArray.get(i);
            if (watchedSparseBooleanArray != null && watchedSparseBooleanArray.get(i3)) {
                if (watchedSparseBooleanArray.get(i2)) {
                    return false;
                }
                if (intent != null && "android.intent.action.VIEW".equals(intent.getAction()) && (categories = intent.getCategories()) != null && categories.contains("android.intent.category.BROWSABLE")) {
                    return false;
                }
                WatchedSparseArray<WatchedSparseBooleanArray> watchedSparseArray2 = this.mInstantGrants.get(i);
                if (watchedSparseArray2 == null) {
                    watchedSparseArray2 = new WatchedSparseArray<>();
                    this.mInstantGrants.put(i, watchedSparseArray2);
                }
                WatchedSparseBooleanArray watchedSparseBooleanArray2 = watchedSparseArray2.get(i2);
                if (watchedSparseBooleanArray2 == null) {
                    watchedSparseBooleanArray2 = new WatchedSparseBooleanArray();
                    watchedSparseArray2.put(i2, watchedSparseBooleanArray2);
                }
                watchedSparseBooleanArray2.put(i3, true);
                return true;
            }
            return false;
        }
    }

    public void addInstantApp(int i, int i2) {
        synchronized (this.mLock) {
            WatchedSparseBooleanArray watchedSparseBooleanArray = this.mInstalledInstantAppUids.get(i);
            if (watchedSparseBooleanArray == null) {
                watchedSparseBooleanArray = new WatchedSparseBooleanArray();
                this.mInstalledInstantAppUids.put(i, watchedSparseBooleanArray);
            }
            watchedSparseBooleanArray.put(i2, true);
        }
        onChanged();
    }

    @GuardedBy({"mLock"})
    private void removeInstantAppLPw(int i, int i2) {
        WatchedSparseBooleanArray watchedSparseBooleanArray;
        WatchedSparseArray<WatchedSparseBooleanArray> watchedSparseArray = this.mInstalledInstantAppUids;
        if (watchedSparseArray == null || (watchedSparseBooleanArray = watchedSparseArray.get(i)) == null) {
            return;
        }
        try {
            watchedSparseBooleanArray.delete(i2);
            WatchedSparseArray<WatchedSparseArray<WatchedSparseBooleanArray>> watchedSparseArray2 = this.mInstantGrants;
            if (watchedSparseArray2 == null) {
                return;
            }
            WatchedSparseArray<WatchedSparseBooleanArray> watchedSparseArray3 = watchedSparseArray2.get(i);
            if (watchedSparseArray3 == null) {
                return;
            }
            for (int size = watchedSparseArray3.size() - 1; size >= 0; size--) {
                watchedSparseArray3.valueAt(size).delete(i2);
            }
        } finally {
            onChanged();
        }
    }

    @GuardedBy({"mLock"})
    private void removeAppLPw(int i, int i2) {
        WatchedSparseArray<WatchedSparseBooleanArray> watchedSparseArray;
        WatchedSparseArray<WatchedSparseArray<WatchedSparseBooleanArray>> watchedSparseArray2 = this.mInstantGrants;
        if (watchedSparseArray2 == null || (watchedSparseArray = watchedSparseArray2.get(i)) == null) {
            return;
        }
        watchedSparseArray.delete(i2);
        onChanged();
    }

    @GuardedBy({"mLock"})
    private void addUninstalledInstantAppLPw(PackageStateInternal packageStateInternal, int i) {
        InstantAppInfo createInstantAppInfoForPackage = createInstantAppInfoForPackage(packageStateInternal, i, false);
        if (createInstantAppInfoForPackage == null) {
            return;
        }
        List<UninstalledInstantAppState> list = this.mUninstalledInstantApps.get(i);
        if (list == null) {
            list = new ArrayList<>();
            this.mUninstalledInstantApps.put(i, list);
        }
        list.add(new UninstalledInstantAppState(createInstantAppInfoForPackage, System.currentTimeMillis()));
        writeUninstalledInstantAppMetadata(createInstantAppInfoForPackage, i);
        writeInstantApplicationIconLPw(packageStateInternal.getPkg(), i);
    }

    private void writeInstantApplicationIconLPw(AndroidPackage androidPackage, int i) {
        Bitmap bitmap;
        if (getInstantApplicationDir(androidPackage.getPackageName(), i).exists()) {
            Drawable loadIcon = AndroidPackageUtils.generateAppInfoWithoutState(androidPackage).loadIcon(this.mContext.getPackageManager());
            if (loadIcon instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) loadIcon).getBitmap();
            } else {
                Bitmap createBitmap = Bitmap.createBitmap(loadIcon.getIntrinsicWidth(), loadIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                loadIcon.setBounds(0, 0, loadIcon.getIntrinsicWidth(), loadIcon.getIntrinsicHeight());
                loadIcon.draw(canvas);
                bitmap = createBitmap;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(getInstantApplicationDir(androidPackage.getPackageName(), i), INSTANT_APP_ICON_FILE));
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.close();
                } finally {
                }
            } catch (Exception e) {
                Slog.e(LOG_TAG, "Error writing instant app icon", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasInstantApplicationMetadata(String str, int i) {
        return hasUninstalledInstantAppState(str, i) || hasInstantAppMetadata(str, i);
    }

    public void deleteInstantApplicationMetadata(final String str, int i) {
        synchronized (this.mLock) {
            removeUninstalledInstantAppStateLPw(new Predicate() { // from class: com.android.server.pm.InstantAppRegistry$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$deleteInstantApplicationMetadata$1;
                    lambda$deleteInstantApplicationMetadata$1 = InstantAppRegistry.lambda$deleteInstantApplicationMetadata$1(str, (InstantAppRegistry.UninstalledInstantAppState) obj);
                    return lambda$deleteInstantApplicationMetadata$1;
                }
            }, i);
            File instantApplicationDir = getInstantApplicationDir(str, i);
            new File(instantApplicationDir, INSTANT_APP_METADATA_FILE).delete();
            new File(instantApplicationDir, INSTANT_APP_ICON_FILE).delete();
            new File(instantApplicationDir, INSTANT_APP_ANDROID_ID_FILE).delete();
            File peekInstantCookieFile = peekInstantCookieFile(str, i);
            if (peekInstantCookieFile != null) {
                peekInstantCookieFile.delete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$deleteInstantApplicationMetadata$1(String str, UninstalledInstantAppState uninstalledInstantAppState) {
        return uninstalledInstantAppState.mInstantAppInfo.getPackageName().equals(str);
    }

    @GuardedBy({"mLock"})
    private void removeUninstalledInstantAppStateLPw(Predicate<UninstalledInstantAppState> predicate, int i) {
        List<UninstalledInstantAppState> list;
        WatchedSparseArray<List<UninstalledInstantAppState>> watchedSparseArray = this.mUninstalledInstantApps;
        if (watchedSparseArray == null || (list = watchedSparseArray.get(i)) == null) {
            return;
        }
        for (int size = list.size() - 1; size >= 0; size--) {
            if (predicate.test(list.get(size))) {
                list.remove(size);
                if (list.isEmpty()) {
                    this.mUninstalledInstantApps.remove(i);
                    onChanged();
                    return;
                }
            }
        }
    }

    private boolean hasUninstalledInstantAppState(String str, int i) {
        synchronized (this.mLock) {
            WatchedSparseArray<List<UninstalledInstantAppState>> watchedSparseArray = this.mUninstalledInstantApps;
            if (watchedSparseArray == null) {
                return false;
            }
            List<UninstalledInstantAppState> list = watchedSparseArray.get(i);
            if (list == null) {
                return false;
            }
            int size = list.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (str.equals(list.get(i2).mInstantAppInfo.getPackageName())) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean hasInstantAppMetadata(String str, int i) {
        File instantApplicationDir = getInstantApplicationDir(str, i);
        return new File(instantApplicationDir, INSTANT_APP_METADATA_FILE).exists() || new File(instantApplicationDir, INSTANT_APP_ICON_FILE).exists() || new File(instantApplicationDir, INSTANT_APP_ANDROID_ID_FILE).exists() || peekInstantCookieFile(str, i) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pruneInstantApps(Computer computer) {
        try {
            pruneInstantApps(computer, JobStatus.NO_LATEST_RUNTIME, Settings.Global.getLong(this.mContext.getContentResolver(), "installed_instant_app_max_cache_period", 15552000000L), Settings.Global.getLong(this.mContext.getContentResolver(), "uninstalled_instant_app_max_cache_period", 15552000000L));
        } catch (IOException e) {
            Slog.e(LOG_TAG, "Error pruning installed and uninstalled instant apps", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean pruneInstalledInstantApps(Computer computer, long j, long j2) {
        try {
            return pruneInstantApps(computer, j, j2, JobStatus.NO_LATEST_RUNTIME);
        } catch (IOException e) {
            Slog.e(LOG_TAG, "Error pruning installed instant apps", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean pruneUninstalledInstantApps(Computer computer, long j, long j2) {
        try {
            return pruneInstantApps(computer, j, JobStatus.NO_LATEST_RUNTIME, j2);
        } catch (IOException e) {
            Slog.e(LOG_TAG, "Error pruning uninstalled instant apps", e);
            return false;
        }
    }

    private boolean pruneInstantApps(Computer computer, long j, long j2, final long j3) throws IOException {
        File[] listFiles;
        File findPathForUuid = ((StorageManager) this.mContext.getSystemService(StorageManager.class)).findPathForUuid(StorageManager.UUID_PRIVATE_INTERNAL);
        if (findPathForUuid.getUsableSpace() >= j) {
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        int[] userIds = this.mUserManager.getUserIds();
        final ArrayMap<String, ? extends PackageStateInternal> packageStates = computer.getPackageStates();
        int size = packageStates.size();
        ArrayList arrayList = null;
        for (int i = 0; i < size; i++) {
            PackageStateInternal valueAt = packageStates.valueAt(i);
            AndroidPackageInternal pkg = valueAt == null ? null : valueAt.getPkg();
            if (pkg != null && currentTimeMillis - valueAt.getTransientState().getLatestPackageUseTimeInMills() >= j2) {
                int length = userIds.length;
                int i2 = 0;
                boolean z = false;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    PackageUserStateInternal userStateOrDefault = valueAt.getUserStateOrDefault(userIds[i2]);
                    if (userStateOrDefault.isInstalled()) {
                        if (!userStateOrDefault.isInstantApp()) {
                            z = false;
                            break;
                        }
                        z = true;
                    }
                    i2++;
                }
                if (z) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(pkg.getPackageName());
                }
            }
        }
        if (arrayList != null) {
            arrayList.sort(new Comparator() { // from class: com.android.server.pm.InstantAppRegistry$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$pruneInstantApps$2;
                    lambda$pruneInstantApps$2 = InstantAppRegistry.lambda$pruneInstantApps$2(packageStates, (String) obj, (String) obj2);
                    return lambda$pruneInstantApps$2;
                }
            });
        }
        if (arrayList != null) {
            int size2 = arrayList.size();
            for (int i3 = 0; i3 < size2; i3++) {
                if (this.mDeletePackageHelper.deletePackageX((String) arrayList.get(i3), -1L, 0, 2, true) == 1 && findPathForUuid.getUsableSpace() >= j) {
                    return true;
                }
            }
        }
        synchronized (this.mLock) {
            for (int i4 : this.mUserManager.getUserIds()) {
                removeUninstalledInstantAppStateLPw(new Predicate() { // from class: com.android.server.pm.InstantAppRegistry$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$pruneInstantApps$3;
                        lambda$pruneInstantApps$3 = InstantAppRegistry.lambda$pruneInstantApps$3(j3, (InstantAppRegistry.UninstalledInstantAppState) obj);
                        return lambda$pruneInstantApps$3;
                    }
                }, i4);
                File instantApplicationsDir = getInstantApplicationsDir(i4);
                if (instantApplicationsDir.exists() && (listFiles = instantApplicationsDir.listFiles()) != null) {
                    for (File file : listFiles) {
                        if (file.isDirectory()) {
                            File file2 = new File(file, INSTANT_APP_METADATA_FILE);
                            if (file2.exists() && System.currentTimeMillis() - file2.lastModified() > j3) {
                                deleteDir(file);
                                if (findPathForUuid.getUsableSpace() >= j) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$pruneInstantApps$2(ArrayMap arrayMap, String str, String str2) {
        PackageStateInternal packageStateInternal;
        PackageStateInternal packageStateInternal2 = (PackageStateInternal) arrayMap.get(str);
        PackageStateInternal packageStateInternal3 = (PackageStateInternal) arrayMap.get(str2);
        AndroidPackageInternal pkg = packageStateInternal2 == null ? null : packageStateInternal2.getPkg();
        AndroidPackageInternal pkg2 = packageStateInternal3 != null ? packageStateInternal3.getPkg() : null;
        if (pkg == null && pkg2 == null) {
            return 0;
        }
        if (pkg == null) {
            return -1;
        }
        if (pkg2 == null) {
            return 1;
        }
        PackageStateInternal packageStateInternal4 = (PackageStateInternal) arrayMap.get(pkg.getPackageName());
        if (packageStateInternal4 == null || (packageStateInternal = (PackageStateInternal) arrayMap.get(pkg2.getPackageName())) == null) {
            return 0;
        }
        if (packageStateInternal4.getTransientState().getLatestPackageUseTimeInMills() > packageStateInternal.getTransientState().getLatestPackageUseTimeInMills()) {
            return 1;
        }
        return (packageStateInternal4.getTransientState().getLatestPackageUseTimeInMills() >= packageStateInternal.getTransientState().getLatestPackageUseTimeInMills() && PackageStateUtils.getEarliestFirstInstallTime(packageStateInternal4.getUserStates()) > PackageStateUtils.getEarliestFirstInstallTime(packageStateInternal.getUserStates())) ? 1 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$pruneInstantApps$3(long j, UninstalledInstantAppState uninstalledInstantAppState) {
        return System.currentTimeMillis() - uninstalledInstantAppState.mTimestamp > j;
    }

    private List<InstantAppInfo> getInstalledInstantApplications(Computer computer, int i) {
        InstantAppInfo createInstantAppInfoForPackage;
        ArrayMap<String, ? extends PackageStateInternal> packageStates = computer.getPackageStates();
        int size = packageStates.size();
        ArrayList arrayList = null;
        for (int i2 = 0; i2 < size; i2++) {
            PackageStateInternal valueAt = packageStates.valueAt(i2);
            if (valueAt != null && valueAt.getUserStateOrDefault(i).isInstantApp() && (createInstantAppInfoForPackage = createInstantAppInfoForPackage(valueAt, i, true)) != null) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(createInstantAppInfoForPackage);
            }
        }
        return arrayList;
    }

    private InstantAppInfo createInstantAppInfoForPackage(PackageStateInternal packageStateInternal, int i, boolean z) {
        AndroidPackageInternal pkg = packageStateInternal.getPkg();
        if (pkg == null || !packageStateInternal.getUserStateOrDefault(i).isInstalled()) {
            return null;
        }
        String[] strArr = new String[pkg.getRequestedPermissions().size()];
        pkg.getRequestedPermissions().toArray(strArr);
        Set<String> grantedPermissions = this.mPermissionManager.getGrantedPermissions(pkg.getPackageName(), i);
        String[] strArr2 = new String[grantedPermissions.size()];
        grantedPermissions.toArray(strArr2);
        ApplicationInfo generateApplicationInfo = PackageInfoUtils.generateApplicationInfo(packageStateInternal.getPkg(), 0L, packageStateInternal.getUserStateOrDefault(i), i, packageStateInternal);
        if (z) {
            return new InstantAppInfo(generateApplicationInfo, strArr, strArr2);
        }
        return new InstantAppInfo(generateApplicationInfo.packageName, generateApplicationInfo.loadLabel(this.mContext.getPackageManager()), strArr, strArr2);
    }

    private List<InstantAppInfo> getUninstalledInstantApplications(Computer computer, int i) {
        List<UninstalledInstantAppState> uninstalledInstantAppStates = getUninstalledInstantAppStates(i);
        ArrayList arrayList = null;
        if (uninstalledInstantAppStates != null && !uninstalledInstantAppStates.isEmpty()) {
            int size = uninstalledInstantAppStates.size();
            for (int i2 = 0; i2 < size; i2++) {
                UninstalledInstantAppState uninstalledInstantAppState = uninstalledInstantAppStates.get(i2);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(uninstalledInstantAppState.mInstantAppInfo);
            }
        }
        return arrayList;
    }

    @SuppressLint({"MissingPermission"})
    private void propagateInstantAppPermissionsIfNeeded(AndroidPackage androidPackage, int i) {
        InstantAppInfo peekOrParseUninstalledInstantAppInfo = peekOrParseUninstalledInstantAppInfo(androidPackage.getPackageName(), i);
        if (peekOrParseUninstalledInstantAppInfo == null || ArrayUtils.isEmpty(peekOrParseUninstalledInstantAppInfo.getGrantedPermissions())) {
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            for (String str : peekOrParseUninstalledInstantAppInfo.getGrantedPermissions()) {
                if (canPropagatePermission(str) && androidPackage.getRequestedPermissions().contains(str)) {
                    ((PermissionManager) this.mContext.getSystemService(PermissionManager.class)).grantRuntimePermission(androidPackage.getPackageName(), str, UserHandle.of(i));
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean canPropagatePermission(String str) {
        PermissionInfo permissionInfo = ((PermissionManager) this.mContext.getSystemService(PermissionManager.class)).getPermissionInfo(str, 0);
        if (permissionInfo != null) {
            return (permissionInfo.getProtection() == 1 || (permissionInfo.getProtectionFlags() & 32) != 0) && (permissionInfo.getProtectionFlags() & 4096) != 0;
        }
        return false;
    }

    private InstantAppInfo peekOrParseUninstalledInstantAppInfo(String str, int i) {
        List<UninstalledInstantAppState> list;
        synchronized (this.mLock) {
            WatchedSparseArray<List<UninstalledInstantAppState>> watchedSparseArray = this.mUninstalledInstantApps;
            if (watchedSparseArray != null && (list = watchedSparseArray.get(i)) != null) {
                int size = list.size();
                for (int i2 = 0; i2 < size; i2++) {
                    UninstalledInstantAppState uninstalledInstantAppState = list.get(i2);
                    if (uninstalledInstantAppState.mInstantAppInfo.getPackageName().equals(str)) {
                        return uninstalledInstantAppState.mInstantAppInfo;
                    }
                }
            }
            UninstalledInstantAppState parseMetadataFile = parseMetadataFile(new File(getInstantApplicationDir(str, i), INSTANT_APP_METADATA_FILE));
            if (parseMetadataFile == null) {
                return null;
            }
            return parseMetadataFile.mInstantAppInfo;
        }
    }

    private List<UninstalledInstantAppState> getUninstalledInstantAppStates(int i) {
        List<UninstalledInstantAppState> list;
        File[] listFiles;
        UninstalledInstantAppState parseMetadataFile;
        synchronized (this.mLock) {
            WatchedSparseArray<List<UninstalledInstantAppState>> watchedSparseArray = this.mUninstalledInstantApps;
            if (watchedSparseArray != null) {
                list = watchedSparseArray.get(i);
                if (list != null) {
                    return list;
                }
            } else {
                list = null;
            }
            File instantApplicationsDir = getInstantApplicationsDir(i);
            if (instantApplicationsDir.exists() && (listFiles = instantApplicationsDir.listFiles()) != null) {
                for (File file : listFiles) {
                    if (file.isDirectory() && (parseMetadataFile = parseMetadataFile(new File(file, INSTANT_APP_METADATA_FILE))) != null) {
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(parseMetadataFile);
                    }
                }
            }
            synchronized (this.mLock) {
                this.mUninstalledInstantApps.put(i, list);
            }
            return list;
        }
    }

    private static UninstalledInstantAppState parseMetadataFile(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream openRead = new AtomicFile(file).openRead();
            try {
                try {
                    return new UninstalledInstantAppState(parseMetadata(Xml.resolvePullParser(openRead), file.getParentFile().getName()), file.lastModified());
                } catch (IOException | XmlPullParserException e) {
                    throw new IllegalStateException("Failed parsing instant metadata file: " + file, e);
                }
            } finally {
                IoUtils.closeQuietly(openRead);
            }
        } catch (FileNotFoundException unused) {
            Slog.i(LOG_TAG, "No instant metadata file");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File computeInstantCookieFile(String str, String str2, int i) {
        return new File(getInstantApplicationDir(str, i), INSTANT_APP_COOKIE_FILE_PREFIX + str2 + INSTANT_APP_COOKIE_FILE_SIFFIX);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File peekInstantCookieFile(String str, int i) {
        File[] listFiles;
        File instantApplicationDir = getInstantApplicationDir(str, i);
        if (!instantApplicationDir.exists() || (listFiles = instantApplicationDir.listFiles()) == null) {
            return null;
        }
        for (File file : listFiles) {
            if (!file.isDirectory() && file.getName().startsWith(INSTANT_APP_COOKIE_FILE_PREFIX) && file.getName().endsWith(INSTANT_APP_COOKIE_FILE_SIFFIX)) {
                return file;
            }
        }
        return null;
    }

    private static InstantAppInfo parseMetadata(TypedXmlPullParser typedXmlPullParser, String str) throws IOException, XmlPullParserException {
        int depth = typedXmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
            if ("package".equals(typedXmlPullParser.getName())) {
                return parsePackage(typedXmlPullParser, str);
            }
        }
        return null;
    }

    private static InstantAppInfo parsePackage(TypedXmlPullParser typedXmlPullParser, String str) throws IOException, XmlPullParserException {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_LABEL);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int depth = typedXmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
            if (TAG_PERMISSIONS.equals(typedXmlPullParser.getName())) {
                parsePermissions(typedXmlPullParser, arrayList, arrayList2);
            }
        }
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        String[] strArr2 = new String[arrayList2.size()];
        arrayList2.toArray(strArr2);
        return new InstantAppInfo(str, attributeValue, strArr, strArr2);
    }

    private static void parsePermissions(TypedXmlPullParser typedXmlPullParser, List<String> list, List<String> list2) throws IOException, XmlPullParserException {
        int depth = typedXmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
            if ("permission".equals(typedXmlPullParser.getName())) {
                String readStringAttribute = XmlUtils.readStringAttribute(typedXmlPullParser, "name");
                list.add(readStringAttribute);
                if (typedXmlPullParser.getAttributeBoolean((String) null, ATTR_GRANTED, false)) {
                    list2.add(readStringAttribute);
                }
            }
        }
    }

    private void writeUninstalledInstantAppMetadata(InstantAppInfo instantAppInfo, int i) {
        FileOutputStream startWrite;
        File instantApplicationDir = getInstantApplicationDir(instantAppInfo.getPackageName(), i);
        if (instantApplicationDir.exists() || instantApplicationDir.mkdirs()) {
            AtomicFile atomicFile = new AtomicFile(new File(instantApplicationDir, INSTANT_APP_METADATA_FILE));
            FileOutputStream fileOutputStream = null;
            try {
                startWrite = atomicFile.startWrite();
            } catch (Throwable th) {
                th = th;
            }
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                resolveSerializer.startTag((String) null, "package");
                resolveSerializer.attribute((String) null, ATTR_LABEL, instantAppInfo.loadLabel(this.mContext.getPackageManager()).toString());
                resolveSerializer.startTag((String) null, TAG_PERMISSIONS);
                for (String str : instantAppInfo.getRequestedPermissions()) {
                    resolveSerializer.startTag((String) null, "permission");
                    resolveSerializer.attribute((String) null, "name", str);
                    if (ArrayUtils.contains(instantAppInfo.getGrantedPermissions(), str)) {
                        resolveSerializer.attributeBoolean((String) null, ATTR_GRANTED, true);
                    }
                    resolveSerializer.endTag((String) null, "permission");
                }
                resolveSerializer.endTag((String) null, TAG_PERMISSIONS);
                resolveSerializer.endTag((String) null, "package");
                resolveSerializer.endDocument();
                atomicFile.finishWrite(startWrite);
                IoUtils.closeQuietly(startWrite);
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = startWrite;
                try {
                    Slog.wtf(LOG_TAG, "Failed to write instant state, restoring backup", th);
                    atomicFile.failWrite(fileOutputStream);
                } finally {
                    IoUtils.closeQuietly(fileOutputStream);
                }
            }
        }
    }

    private static File getInstantApplicationsDir(int i) {
        return new File(Environment.getUserSystemDirectory(i), INSTANT_APPS_FOLDER);
    }

    private static File getInstantApplicationDir(String str, int i) {
        return new File(getInstantApplicationsDir(i), str);
    }

    private static void deleteDir(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                deleteDir(file2);
            }
        }
        file.delete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class UninstalledInstantAppState {
        final InstantAppInfo mInstantAppInfo;
        final long mTimestamp;

        public UninstalledInstantAppState(InstantAppInfo instantAppInfo, long j) {
            this.mInstantAppInfo = instantAppInfo;
            this.mTimestamp = j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class CookiePersistence extends Handler {
        private static final long PERSIST_COOKIE_DELAY_MILLIS = 1000;
        private final SparseArray<ArrayMap<String, SomeArgs>> mPendingPersistCookies;

        public CookiePersistence(Looper looper) {
            super(looper);
            this.mPendingPersistCookies = new SparseArray<>();
        }

        public void schedulePersistLPw(int i, AndroidPackage androidPackage, byte[] bArr) {
            File computeInstantCookieFile = InstantAppRegistry.computeInstantCookieFile(androidPackage.getPackageName(), PackageUtils.computeSignaturesSha256Digest(androidPackage.getSigningDetails().getSignatures()), i);
            if (!androidPackage.getSigningDetails().hasSignatures()) {
                Slog.wtf(InstantAppRegistry.LOG_TAG, "Parsed Instant App contains no valid signatures!");
            }
            File peekInstantCookieFile = InstantAppRegistry.peekInstantCookieFile(androidPackage.getPackageName(), i);
            if (peekInstantCookieFile != null && !computeInstantCookieFile.equals(peekInstantCookieFile)) {
                peekInstantCookieFile.delete();
            }
            cancelPendingPersistLPw(androidPackage, i);
            addPendingPersistCookieLPw(i, androidPackage, bArr, computeInstantCookieFile);
            sendMessageDelayed(obtainMessage(i, androidPackage), 1000L);
        }

        public byte[] getPendingPersistCookieLPr(AndroidPackage androidPackage, int i) {
            SomeArgs someArgs;
            ArrayMap<String, SomeArgs> arrayMap = this.mPendingPersistCookies.get(i);
            if (arrayMap == null || (someArgs = arrayMap.get(androidPackage.getPackageName())) == null) {
                return null;
            }
            return (byte[]) someArgs.arg1;
        }

        public void cancelPendingPersistLPw(AndroidPackage androidPackage, int i) {
            removeMessages(i, androidPackage);
            SomeArgs removePendingPersistCookieLPr = removePendingPersistCookieLPr(androidPackage, i);
            if (removePendingPersistCookieLPr != null) {
                removePendingPersistCookieLPr.recycle();
            }
        }

        private void addPendingPersistCookieLPw(int i, AndroidPackage androidPackage, byte[] bArr, File file) {
            ArrayMap<String, SomeArgs> arrayMap = this.mPendingPersistCookies.get(i);
            if (arrayMap == null) {
                arrayMap = new ArrayMap<>();
                this.mPendingPersistCookies.put(i, arrayMap);
            }
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = bArr;
            obtain.arg2 = file;
            arrayMap.put(androidPackage.getPackageName(), obtain);
        }

        private SomeArgs removePendingPersistCookieLPr(AndroidPackage androidPackage, int i) {
            ArrayMap<String, SomeArgs> arrayMap = this.mPendingPersistCookies.get(i);
            if (arrayMap == null) {
                return null;
            }
            SomeArgs remove = arrayMap.remove(androidPackage.getPackageName());
            if (!arrayMap.isEmpty()) {
                return remove;
            }
            this.mPendingPersistCookies.remove(i);
            return remove;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            AndroidPackage androidPackage = (AndroidPackage) message.obj;
            SomeArgs removePendingPersistCookieLPr = removePendingPersistCookieLPr(androidPackage, i);
            if (removePendingPersistCookieLPr == null) {
                return;
            }
            byte[] bArr = (byte[]) removePendingPersistCookieLPr.arg1;
            File file = (File) removePendingPersistCookieLPr.arg2;
            removePendingPersistCookieLPr.recycle();
            InstantAppRegistry.this.persistInstantApplicationCookie(bArr, androidPackage.getPackageName(), file, i);
        }
    }
}
