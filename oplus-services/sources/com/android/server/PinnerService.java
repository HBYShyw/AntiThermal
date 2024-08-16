package com.android.server;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.IActivityManager;
import android.app.SearchManager;
import android.app.UidObserver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.SystemProperties;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.ResolverActivity;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.SystemService;
import com.android.server.wm.ActivityTaskManagerInternal;
import dalvik.system.DexFile;
import dalvik.system.VMRuntime;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PinnerService extends SystemService {
    private static final boolean DEBUG = false;
    private static final int KEY_ASSISTANT = 2;
    private static final int KEY_CAMERA = 0;
    private static final int KEY_HOME = 1;
    private static final int MATCH_FLAGS = 851968;
    private static final int MAX_ASSISTANT_PIN_SIZE = 62914560;
    private static final int MAX_CAMERA_PIN_SIZE = 83886080;
    private static final int MAX_HOME_PIN_SIZE = 6291456;
    private static final String PIN_META_FILENAME = "pinlist.meta";
    private static final String TAG = "PinnerService";
    private final IActivityManager mAm;
    private final ActivityManagerInternal mAmInternal;
    private final ActivityTaskManagerInternal mAtmInternal;
    private BinderService mBinderService;
    private final BroadcastReceiver mBroadcastReceiver;
    private final boolean mConfiguredToPinAssistant;
    private final boolean mConfiguredToPinCamera;
    private final boolean mConfiguredToPinHome;
    private final Context mContext;

    @GuardedBy({"this"})
    private final ArrayMap<Integer, Integer> mPendingRepin;

    @GuardedBy({"this"})
    private ArraySet<Integer> mPinKeys;

    @GuardedBy({"this"})
    private final ArrayMap<Integer, PinnedApp> mPinnedApps;

    @GuardedBy({"this"})
    private final ArrayList<PinnedFile> mPinnedFiles;
    private PinnerHandler mPinnerHandler;
    public IPinnerServiceExt mPinnerServiceExt;
    private SearchManager mSearchManager;
    private final UserManager mUserManager;
    private static final int PAGE_SIZE = (int) Os.sysconf(OsConstants._SC_PAGESIZE);
    private static boolean PROP_PIN_PINLIST = SystemProperties.getBoolean("pinner.use_pinlist", true);
    private static boolean PROP_PIN_ODEX = SystemProperties.getBoolean("pinner.whole_odex", true);

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface AppKey {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getNameForKey(int i) {
        if (i == 0) {
            return "Camera";
        }
        if (i == 1) {
            return "Home";
        }
        if (i != 2) {
            return null;
        }
        return "Assistant";
    }

    public PinnerService(Context context) {
        super(context);
        this.mPinnedFiles = new ArrayList<>();
        this.mPinnedApps = new ArrayMap<>();
        this.mPendingRepin = new ArrayMap<>();
        this.mPinnerHandler = null;
        this.mPinnerServiceExt = (IPinnerServiceExt) ExtLoader.type(IPinnerServiceExt.class).create();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.PinnerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
                    String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
                    ArraySet<String> arraySet = new ArraySet<>();
                    arraySet.add(schemeSpecificPart);
                    PinnerService.this.update(arraySet, true);
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mContext = context;
        this.mConfiguredToPinCamera = context.getResources().getBoolean(17891781);
        this.mConfiguredToPinHome = context.getResources().getBoolean(17891782);
        this.mConfiguredToPinAssistant = context.getResources().getBoolean(17891780);
        this.mPinKeys = createPinKeys();
        this.mPinnerHandler = new PinnerHandler(BackgroundThread.get().getLooper());
        this.mAtmInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        this.mAmInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        this.mAm = ActivityManager.getService();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(broadcastReceiver, intentFilter);
        registerUidListener();
        registerUserSetupCompleteListener();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        BinderService binderService = new BinderService();
        this.mBinderService = binderService;
        publishBinderService("pinner", binderService);
        publishLocalService(PinnerService.class, this);
        this.mPinnerHandler.obtainMessage(4001).sendToTarget();
        sendPinAppsMessage(0);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 500) {
            this.mSearchManager = (SearchManager) this.mContext.getSystemService("search");
            sendPinAppsMessage(0);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        int userIdentifier = targetUser2.getUserIdentifier();
        if (this.mUserManager.isManagedProfile(userIdentifier)) {
            return;
        }
        sendPinAppsMessage(userIdentifier);
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(SystemService.TargetUser targetUser) {
        int userIdentifier = targetUser.getUserIdentifier();
        if (this.mUserManager.isManagedProfile(userIdentifier)) {
            return;
        }
        sendPinAppsMessage(userIdentifier);
    }

    public void update(ArraySet<String> arraySet, boolean z) {
        ArraySet<Integer> pinKeys = getPinKeys();
        int currentUser = ActivityManager.getCurrentUser();
        for (int size = pinKeys.size() - 1; size >= 0; size--) {
            int intValue = pinKeys.valueAt(size).intValue();
            ApplicationInfo infoForKey = getInfoForKey(intValue, currentUser);
            if (infoForKey != null && arraySet.contains(infoForKey.packageName)) {
                Slog.i(TAG, "Updating pinned files for " + infoForKey.packageName + " force=" + z);
                sendPinAppMessage(intValue, currentUser, z);
            }
        }
        this.mPinnerServiceExt.updateExt(arraySet, z);
    }

    public List<PinnedFileStats> dumpDataForStatsd() {
        ArrayList arrayList = new ArrayList();
        synchronized (this) {
            Iterator<PinnedFile> it = this.mPinnedFiles.iterator();
            while (it.hasNext()) {
                arrayList.add(new PinnedFileStats(1000, it.next()));
            }
            Iterator<Integer> it2 = this.mPinnedApps.keySet().iterator();
            while (it2.hasNext()) {
                int intValue = it2.next().intValue();
                PinnedApp pinnedApp = this.mPinnedApps.get(Integer.valueOf(intValue));
                Iterator<PinnedFile> it3 = this.mPinnedApps.get(Integer.valueOf(intValue)).mFiles.iterator();
                while (it3.hasNext()) {
                    arrayList.add(new PinnedFileStats(pinnedApp.uid, it3.next()));
                }
            }
        }
        return arrayList;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class PinnedFileStats {
        public final String filename;
        public final int sizeKb;
        public final int uid;

        protected PinnedFileStats(int i, PinnedFile pinnedFile) {
            this.uid = i;
            String str = pinnedFile.fileName;
            this.filename = str.substring(str.lastIndexOf(47) + 1);
            this.sizeKb = pinnedFile.bytesPinned / 1024;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePinOnStart() {
        String[] strArr;
        for (String str : this.mPinnerServiceExt.replaceDefaultFiles(this.mContext.getResources().getStringArray(R.array.config_displayWhiteBalanceHighLightAmbientBrightnesses))) {
            PinnedFile pinFile = pinFile(str, Integer.MAX_VALUE, false);
            if (pinFile == null) {
                Slog.e(TAG, "Failed to pin file = " + str);
            } else {
                synchronized (this) {
                    this.mPinnedFiles.add(pinFile);
                }
                if (str.endsWith(".jar") || str.endsWith(".apk")) {
                    try {
                        strArr = DexFile.getDexFileOutputPaths(str, VMRuntime.getInstructionSet(Build.SUPPORTED_ABIS[0]));
                    } catch (IOException unused) {
                        strArr = null;
                    }
                    if (strArr == null) {
                        continue;
                    } else {
                        for (String str2 : strArr) {
                            PinnedFile pinFile2 = pinFile(str2, Integer.MAX_VALUE, false);
                            if (pinFile2 == null) {
                                Slog.i(TAG, "Failed to pin ART file = " + str2);
                            } else {
                                synchronized (this) {
                                    this.mPinnedFiles.add(pinFile2);
                                }
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private void registerUserSetupCompleteListener() {
        final Uri uriFor = Settings.Secure.getUriFor("user_setup_complete");
        this.mContext.getContentResolver().registerContentObserver(uriFor, false, new ContentObserver(null) { // from class: com.android.server.PinnerService.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (uriFor.equals(uri)) {
                    PinnerService.this.sendPinAppMessage(1, ActivityManager.getCurrentUser(), true);
                }
            }
        }, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.PinnerService$3, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass3 extends UidObserver {
        AnonymousClass3() {
        }

        public void onUidGone(int i, boolean z) {
            PinnerService.this.mPinnerHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.PinnerService$3$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((PinnerService) obj).handleUidGone(((Integer) obj2).intValue());
                }
            }, PinnerService.this, Integer.valueOf(i)));
        }

        public void onUidActive(int i) {
            PinnerService.this.mPinnerHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.PinnerService$3$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((PinnerService) obj).handleUidActive(((Integer) obj2).intValue());
                }
            }, PinnerService.this, Integer.valueOf(i)));
        }
    }

    private void registerUidListener() {
        try {
            this.mAm.registerUidObserver(new AnonymousClass3(), 10, 0, (String) null);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to register uid observer", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUidGone(int i) {
        updateActiveState(i, false);
        synchronized (this) {
            int intValue = this.mPendingRepin.getOrDefault(Integer.valueOf(i), -1).intValue();
            if (intValue == -1) {
                return;
            }
            this.mPendingRepin.remove(Integer.valueOf(i));
            pinApp(intValue, ActivityManager.getCurrentUser(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUidActive(int i) {
        updateActiveState(i, true);
    }

    private void updateActiveState(int i, boolean z) {
        synchronized (this) {
            for (int size = this.mPinnedApps.size() - 1; size >= 0; size--) {
                PinnedApp valueAt = this.mPinnedApps.valueAt(size);
                if (valueAt.uid == i) {
                    valueAt.active = z;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unpinApps() {
        ArraySet<Integer> pinKeys = getPinKeys();
        for (int size = pinKeys.size() - 1; size >= 0; size--) {
            unpinApp(pinKeys.valueAt(size).intValue());
        }
    }

    private void unpinApp(int i) {
        synchronized (this) {
            PinnedApp pinnedApp = this.mPinnedApps.get(Integer.valueOf(i));
            if (pinnedApp == null) {
                return;
            }
            this.mPinnedApps.remove(Integer.valueOf(i));
            Iterator it = new ArrayList(pinnedApp.mFiles).iterator();
            while (it.hasNext()) {
                ((PinnedFile) it.next()).close();
            }
        }
    }

    private boolean isResolverActivity(ActivityInfo activityInfo) {
        return ResolverActivity.class.getName().equals(activityInfo.name);
    }

    private ApplicationInfo getCameraInfo(int i) {
        ApplicationInfo applicationInfoForIntent = getApplicationInfoForIntent(new Intent("android.media.action.STILL_IMAGE_CAMERA"), i, false);
        if (applicationInfoForIntent == null) {
            applicationInfoForIntent = getApplicationInfoForIntent(new Intent("android.media.action.STILL_IMAGE_CAMERA_SECURE"), i, false);
        }
        return applicationInfoForIntent == null ? getApplicationInfoForIntent(new Intent("android.media.action.STILL_IMAGE_CAMERA"), i, true) : applicationInfoForIntent;
    }

    private ApplicationInfo getHomeInfo(int i) {
        return getApplicationInfoForIntent(this.mAtmInternal.getHomeIntent(), i, false);
    }

    private ApplicationInfo getAssistantInfo(int i) {
        SearchManager searchManager = this.mSearchManager;
        if (searchManager != null) {
            return getApplicationInfoForIntent(searchManager.getAssistIntent(false), i, true);
        }
        return null;
    }

    private ApplicationInfo getApplicationInfoForIntent(Intent intent, int i, boolean z) {
        ResolveInfo resolveActivityAsUser;
        if (intent == null || (resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(intent, MATCH_FLAGS, i)) == null) {
            return null;
        }
        if (!isResolverActivity(resolveActivityAsUser.activityInfo)) {
            return resolveActivityAsUser.activityInfo.applicationInfo;
        }
        if (!z) {
            return null;
        }
        Iterator it = this.mContext.getPackageManager().queryIntentActivitiesAsUser(intent, MATCH_FLAGS, i).iterator();
        ApplicationInfo applicationInfo = null;
        while (it.hasNext()) {
            ApplicationInfo applicationInfo2 = ((ResolveInfo) it.next()).activityInfo.applicationInfo;
            if ((applicationInfo2.flags & 1) != 0) {
                if (applicationInfo != null) {
                    return null;
                }
                applicationInfo = applicationInfo2;
            }
        }
        return applicationInfo;
    }

    private void sendPinAppsMessage(int i) {
        this.mPinnerHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((PinnerService) obj).pinApps(((Integer) obj2).intValue());
            }
        }, this, Integer.valueOf(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendPinAppsWithUpdatedKeysMessage(int i) {
        this.mPinnerHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((PinnerService) obj).pinAppsWithUpdatedKeys(((Integer) obj2).intValue());
            }
        }, this, Integer.valueOf(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUnpinAppsMessage() {
        this.mPinnerHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PinnerService) obj).unpinApps();
            }
        }, this));
    }

    private ArraySet<Integer> createPinKeys() {
        ArraySet<Integer> arraySet = new ArraySet<>();
        if (this.mConfiguredToPinCamera && DeviceConfig.getBoolean("runtime_native_boot", "pin_camera", SystemProperties.getBoolean("pinner.pin_camera", true))) {
            arraySet.add(0);
        }
        if (this.mConfiguredToPinHome) {
            arraySet.add(1);
        }
        if (this.mConfiguredToPinAssistant) {
            arraySet.add(2);
        }
        return arraySet;
    }

    private synchronized ArraySet<Integer> getPinKeys() {
        return this.mPinKeys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pinApps(int i) {
        pinAppsInternal(i, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pinAppsWithUpdatedKeys(int i) {
        pinAppsInternal(i, true);
    }

    private void pinAppsInternal(int i, boolean z) {
        if (z) {
            ArraySet<Integer> createPinKeys = createPinKeys();
            synchronized (this) {
                if (!this.mPinnedApps.isEmpty()) {
                    Slog.e(TAG, "Attempted to update a list of apps, but apps were already pinned. Skipping.");
                    return;
                }
                this.mPinKeys = createPinKeys;
            }
        }
        ArraySet<Integer> pinKeys = getPinKeys();
        for (int size = pinKeys.size() - 1; size >= 0; size--) {
            pinApp(pinKeys.valueAt(size).intValue(), i, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendPinAppMessage(int i, int i2, boolean z) {
        this.mPinnerHandler.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.PinnerService$$ExternalSyntheticLambda2
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                ((PinnerService) obj).pinApp(((Integer) obj2).intValue(), ((Integer) obj3).intValue(), ((Boolean) obj4).booleanValue());
            }
        }, this, Integer.valueOf(i), Integer.valueOf(i2), Boolean.valueOf(z)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pinApp(int i, int i2, boolean z) {
        int uidForKey = getUidForKey(i);
        if (!z && uidForKey != -1) {
            synchronized (this) {
                this.mPendingRepin.put(Integer.valueOf(uidForKey), Integer.valueOf(i));
            }
            return;
        }
        unpinApp(i);
        ApplicationInfo infoForKey = getInfoForKey(i, i2);
        if (infoForKey != null) {
            pinApp(i, infoForKey);
        }
    }

    private int getUidForKey(int i) {
        int i2;
        synchronized (this) {
            PinnedApp pinnedApp = this.mPinnedApps.get(Integer.valueOf(i));
            i2 = (pinnedApp == null || !pinnedApp.active) ? -1 : pinnedApp.uid;
        }
        return i2;
    }

    private ApplicationInfo getInfoForKey(int i, int i2) {
        if (i == 0) {
            return getCameraInfo(i2);
        }
        if (i == 1) {
            return getHomeInfo(i2);
        }
        if (i != 2) {
            return null;
        }
        return getAssistantInfo(i2);
    }

    private int getSizeLimitForKey(int i) {
        if (i == 0) {
            return 83886080;
        }
        if (i == 1) {
            return this.mPinnerServiceExt.customizePinLauncherBytes(MAX_HOME_PIN_SIZE);
        }
        if (i != 2) {
            return 0;
        }
        return MAX_ASSISTANT_PIN_SIZE;
    }

    private void pinApp(int i, ApplicationInfo applicationInfo) {
        if (applicationInfo == null) {
            return;
        }
        String[] strArr = null;
        PinnedApp pinnedApp = new PinnedApp(applicationInfo);
        synchronized (this) {
            this.mPinnedApps.put(Integer.valueOf(i), pinnedApp);
        }
        int sizeLimitForKey = getSizeLimitForKey(i);
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add(applicationInfo.sourceDir);
        String[] strArr2 = applicationInfo.splitSourceDirs;
        if (strArr2 != null) {
            for (String str : strArr2) {
                arrayList.add(str);
            }
        }
        int i2 = sizeLimitForKey;
        for (String str2 : arrayList) {
            if (i2 <= 0) {
                Slog.w(TAG, "Reached to the pin size limit. Skipping: " + str2);
            } else {
                PinnedFile pinFile = pinFile(str2, i2, true);
                if (pinFile == null) {
                    Slog.e(TAG, "Failed to pin " + str2);
                } else {
                    synchronized (this) {
                        pinnedApp.mFiles.add(pinFile);
                    }
                    i2 -= pinFile.bytesPinned;
                }
            }
        }
        String str3 = applicationInfo.primaryCpuAbi;
        if (str3 == null) {
            str3 = Build.SUPPORTED_ABIS[0];
        }
        try {
            strArr = DexFile.getDexFileOutputPaths(applicationInfo.getBaseCodePath(), VMRuntime.getInstructionSet(str3));
        } catch (IOException unused) {
        }
        if (strArr == null) {
            return;
        }
        for (String str4 : strArr) {
            PinnedFile pinFile2 = pinFile(str4, sizeLimitForKey, false);
            if (pinFile2 != null) {
                synchronized (this) {
                    if (PROP_PIN_ODEX) {
                        pinnedApp.mFiles.add(pinFile2);
                    }
                }
            }
        }
    }

    private static PinnedFile pinFile(String str, int i, boolean z) {
        ZipFile zipFile;
        PinRangeSource pinRangeSourceStatic;
        InputStream inputStream = null;
        if (z) {
            try {
                zipFile = maybeOpenZip(str);
            } catch (Throwable th) {
                th = th;
                zipFile = null;
                safeClose(inputStream);
                safeClose(zipFile);
                throw th;
            }
        } else {
            zipFile = null;
        }
        if (zipFile != null) {
            try {
                inputStream = maybeOpenPinMetaInZip(zipFile, str);
            } catch (Throwable th2) {
                th = th2;
                safeClose(inputStream);
                safeClose(zipFile);
                throw th;
            }
        }
        Slog.d(TAG, "pinRangeStream: " + inputStream);
        if (inputStream != null) {
            pinRangeSourceStatic = new PinRangeSourceStream(inputStream);
        } else {
            pinRangeSourceStatic = new PinRangeSourceStatic(0, Integer.MAX_VALUE);
        }
        PinnedFile pinFileRanges = pinFileRanges(str, i, pinRangeSourceStatic);
        safeClose(inputStream);
        safeClose(zipFile);
        return pinFileRanges;
    }

    private static ZipFile maybeOpenZip(String str) {
        try {
            return new ZipFile(str);
        } catch (IOException e) {
            Slog.w(TAG, String.format("could not open \"%s\" as zip: pinning as blob", str), e);
            return null;
        }
    }

    private static InputStream maybeOpenPinMetaInZip(ZipFile zipFile, String str) {
        ZipEntry entry;
        if (!PROP_PIN_PINLIST || (entry = zipFile.getEntry(PIN_META_FILENAME)) == null) {
            return null;
        }
        try {
            return zipFile.getInputStream(entry);
        } catch (IOException e) {
            Slog.w(TAG, String.format("error reading pin metadata \"%s\": pinning as blob", str), e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class PinRangeSource {
        abstract boolean read(PinRange pinRange);

        private PinRangeSource() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PinRangeSourceStatic extends PinRangeSource {
        private boolean mDone;
        private final int mPinLength;
        private final int mPinStart;

        PinRangeSourceStatic(int i, int i2) {
            super();
            this.mDone = false;
            this.mPinStart = i;
            this.mPinLength = i2;
        }

        @Override // com.android.server.PinnerService.PinRangeSource
        boolean read(PinRange pinRange) {
            pinRange.start = this.mPinStart;
            pinRange.length = this.mPinLength;
            boolean z = this.mDone;
            this.mDone = true;
            return !z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PinRangeSourceStream extends PinRangeSource {
        private boolean mDone;
        private final DataInputStream mStream;

        PinRangeSourceStream(InputStream inputStream) {
            super();
            this.mDone = false;
            this.mStream = new DataInputStream(inputStream);
        }

        @Override // com.android.server.PinnerService.PinRangeSource
        boolean read(PinRange pinRange) {
            if (!this.mDone) {
                try {
                    pinRange.start = this.mStream.readInt();
                    pinRange.length = this.mStream.readInt();
                } catch (IOException unused) {
                    this.mDone = true;
                }
            }
            return !this.mDone;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static PinnedFile pinFileRanges(String str, int i, PinRangeSource pinRangeSource) {
        int i2;
        int i3;
        int i4;
        FileDescriptor fileDescriptor = new FileDescriptor();
        long j = -1;
        int i5 = 0;
        try {
            try {
                FileDescriptor open = Os.open(str, OsConstants.O_RDONLY | OsConstants.O_CLOEXEC, 0);
                try {
                    int min = (int) Math.min(Os.fstat(open).st_size, 2147483647L);
                    try {
                        long mmap = Os.mmap(0L, min, OsConstants.PROT_READ, OsConstants.MAP_SHARED, open, 0L);
                        try {
                            PinRange pinRange = new PinRange();
                            int i6 = PAGE_SIZE;
                            if (i % i6 != 0) {
                                try {
                                    i3 = i - (i % i6);
                                } catch (ErrnoException e) {
                                    e = e;
                                    i5 = min;
                                    j = mmap;
                                    fileDescriptor = open;
                                    Slog.e(TAG, "Could not pin file " + str, e);
                                    safeClose(fileDescriptor);
                                    if (j >= 0) {
                                        return null;
                                    }
                                    safeMunmap(j, i5);
                                    return null;
                                } catch (Throwable th) {
                                    th = th;
                                    i5 = min;
                                    j = mmap;
                                    fileDescriptor = open;
                                    safeClose(fileDescriptor);
                                    if (j >= 0) {
                                        safeMunmap(j, i5);
                                    }
                                    throw th;
                                }
                            } else {
                                i3 = i;
                            }
                            i4 = 0;
                            while (i4 < i3) {
                                if (!pinRangeSource.read(pinRange)) {
                                    break;
                                }
                                int i7 = pinRange.start;
                                int i8 = pinRange.length;
                                int clamp = clamp(0, i7, min);
                                int i9 = i3 - i4;
                                int min2 = Math.min(i9, clamp(0, i8, min - clamp));
                                int i10 = PAGE_SIZE;
                                int i11 = min2 + (clamp % i10);
                                int i12 = clamp - (clamp % i10);
                                if (i11 % i10 != 0) {
                                    i11 += i10 - (i11 % i10);
                                }
                                int clamp2 = clamp(0, i11, i9);
                                if (clamp2 > 0) {
                                    Os.mlock(i12 + mmap, clamp2);
                                }
                                i4 += clamp2;
                            }
                            i2 = min;
                        } catch (ErrnoException e2) {
                            e = e2;
                            i2 = min;
                        } catch (Throwable th2) {
                            th = th2;
                            i2 = min;
                        }
                        try {
                            PinnedFile pinnedFile = new PinnedFile(mmap, min, str, i4);
                            safeClose(open);
                            return pinnedFile;
                        } catch (ErrnoException e3) {
                            e = e3;
                            i5 = i2;
                            j = mmap;
                            fileDescriptor = open;
                            Slog.e(TAG, "Could not pin file " + str, e);
                            safeClose(fileDescriptor);
                            if (j >= 0) {
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            i5 = i2;
                            j = mmap;
                            fileDescriptor = open;
                            safeClose(fileDescriptor);
                            if (j >= 0) {
                            }
                            throw th;
                        }
                    } catch (ErrnoException e4) {
                        e = e4;
                        i5 = min;
                    } catch (Throwable th4) {
                        th = th4;
                        i5 = min;
                    }
                } catch (ErrnoException e5) {
                    e = e5;
                } catch (Throwable th5) {
                    th = th5;
                }
            } catch (ErrnoException e6) {
                e = e6;
            }
        } catch (Throwable th6) {
            th = th6;
        }
    }

    private static int clamp(int i, int i2, int i3) {
        return Math.max(i, Math.min(i2, i3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void safeMunmap(long j, long j2) {
        try {
            Os.munmap(j, j2);
        } catch (ErrnoException e) {
            Slog.w(TAG, "ignoring error in unmap", e);
        }
    }

    private static void safeClose(FileDescriptor fileDescriptor) {
        if (fileDescriptor == null || !fileDescriptor.valid()) {
            return;
        }
        try {
            Os.close(fileDescriptor);
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EBADF) {
                throw new AssertionError(e);
            }
        }
    }

    private static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Slog.w(TAG, "ignoring error closing resource: " + closeable, e);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class BinderService extends Binder {
        private BinderService() {
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(PinnerService.this.mContext, PinnerService.TAG, printWriter)) {
                synchronized (PinnerService.this) {
                    Iterator it = PinnerService.this.mPinnedFiles.iterator();
                    long j = 0;
                    while (it.hasNext()) {
                        PinnedFile pinnedFile = (PinnedFile) it.next();
                        printWriter.format("%s %s\n", pinnedFile.fileName, Integer.valueOf(pinnedFile.bytesPinned));
                        j += pinnedFile.bytesPinned;
                    }
                    printWriter.println();
                    Iterator it2 = PinnerService.this.mPinnedApps.keySet().iterator();
                    while (it2.hasNext()) {
                        int intValue = ((Integer) it2.next()).intValue();
                        PinnedApp pinnedApp = (PinnedApp) PinnerService.this.mPinnedApps.get(Integer.valueOf(intValue));
                        printWriter.print(PinnerService.this.getNameForKey(intValue));
                        printWriter.print(" uid=");
                        printWriter.print(pinnedApp.uid);
                        printWriter.print(" active=");
                        printWriter.print(pinnedApp.active);
                        printWriter.println();
                        Iterator<PinnedFile> it3 = ((PinnedApp) PinnerService.this.mPinnedApps.get(Integer.valueOf(intValue))).mFiles.iterator();
                        while (it3.hasNext()) {
                            PinnedFile next = it3.next();
                            printWriter.print("  ");
                            printWriter.format("%s %s\n", next.fileName, Integer.valueOf(next.bytesPinned));
                            j += next.bytesPinned;
                        }
                    }
                    printWriter.format("Total size: %s\n", Long.valueOf(j));
                    printWriter.println();
                    if (!PinnerService.this.mPendingRepin.isEmpty()) {
                        printWriter.print("Pending repin: ");
                        Iterator it4 = PinnerService.this.mPendingRepin.values().iterator();
                        while (it4.hasNext()) {
                            printWriter.print(PinnerService.this.getNameForKey(((Integer) it4.next()).intValue()));
                            printWriter.print(' ');
                        }
                        printWriter.println();
                    }
                }
            }
        }

        private void repin() {
            PinnerService.this.sendUnpinAppsMessage();
            PinnerService.this.sendPinAppsWithUpdatedKeysMessage(0);
        }

        private void printError(FileDescriptor fileDescriptor, String str) {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(fileDescriptor));
            printWriter.println(str);
            printWriter.flush();
        }

        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            if (strArr.length < 1) {
                printError(fileDescriptor2, "Command is not given.");
                resultReceiver.send(-1, null);
                return;
            }
            String str = strArr[0];
            str.hashCode();
            if (str.equals("repin")) {
                repin();
                resultReceiver.send(0, null);
            } else {
                printError(fileDescriptor2, String.format("Unknown pinner command: %s. Supported commands: repin", str));
                resultReceiver.send(-1, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PinnedFile implements AutoCloseable {
        final int bytesPinned;
        final String fileName;
        private long mAddress;
        final int mapSize;

        PinnedFile(long j, int i, String str, int i2) {
            this.mAddress = j;
            this.mapSize = i;
            this.fileName = str;
            this.bytesPinned = i2;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            long j = this.mAddress;
            if (j >= 0) {
                PinnerService.safeMunmap(j, this.mapSize);
                this.mAddress = -1L;
            }
        }

        public void finalize() {
            close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PinRange {
        int length;
        int start;

        PinRange() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class PinnedApp {
        boolean active;
        final ArrayList<PinnedFile> mFiles;
        final int uid;

        private PinnedApp(ApplicationInfo applicationInfo) {
            this.mFiles = new ArrayList<>();
            int i = applicationInfo.uid;
            this.uid = i;
            this.active = PinnerService.this.mAmInternal.isUidActive(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class PinnerHandler extends Handler {
        static final int PIN_ONSTART_MSG = 4001;

        public PinnerHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == PIN_ONSTART_MSG) {
                PinnerService.this.handlePinOnStart();
            } else {
                super.handleMessage(message);
            }
        }
    }
}
