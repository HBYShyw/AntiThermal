package com.android.server.webkit;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Trace;
import android.util.Slog;
import android.webkit.UserPackage;
import android.webkit.WebViewFactory;
import android.webkit.WebViewProviderInfo;
import android.webkit.WebViewProviderResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WebViewUpdateServiceImpl {
    private static final int MULTIPROCESS_SETTING_OFF_VALUE = Integer.MIN_VALUE;
    private static final int MULTIPROCESS_SETTING_ON_VALUE = Integer.MAX_VALUE;
    private static final long NS_PER_MS = 1000000;
    private static final int NUMBER_OF_RELROS_UNKNOWN = Integer.MAX_VALUE;
    private static final String TAG = "WebViewUpdateServiceImpl";
    private static final int VALIDITY_INCORRECT_SDK_VERSION = 1;
    private static final int VALIDITY_INCORRECT_SIGNATURE = 3;
    private static final int VALIDITY_INCORRECT_VERSION_CODE = 2;
    private static final int VALIDITY_NO_LIBRARY_FLAG = 4;
    private static final int VALIDITY_OK = 0;
    private static final int WAIT_TIMEOUT_MS = 1000;
    private final Context mContext;
    private final SystemInterface mSystemInterface;
    private long mMinimumVersionCode = -1;
    private int mNumRelroCreationsStarted = 0;
    private int mNumRelroCreationsFinished = 0;
    private boolean mWebViewPackageDirty = false;
    private boolean mAnyWebViewInstalled = false;
    private PackageInfo mCurrentWebViewPackage = null;
    private final Object mLock = new Object();

    private static String getInvalidityReason(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "Unexcepted validity-reason" : "No WebView-library manifest flag" : "Incorrect signature" : "Version code too low" : "SDK version too low";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class WebViewPackageMissingException extends Exception {
        WebViewPackageMissingException(String str) {
            super(str);
        }

        WebViewPackageMissingException(Exception exc) {
            super(exc);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebViewUpdateServiceImpl(Context context, SystemInterface systemInterface) {
        this.mContext = context;
        this.mSystemInterface = systemInterface;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't wrap try/catch for region: R(9:9|(3:10|11|(1:13)(1:46))|(2:14|15)|(2:19|(5:21|22|23|(1:25)|27))|42|22|23|(0)|27) */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x004b, code lost:
    
        r8 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x004c, code lost:
    
        r5 = r1;
        r1 = r0;
        r0 = r5;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0047 A[Catch: WebViewPackageMissingException -> 0x004b, all -> 0x0052, TRY_LEAVE, TryCatch #0 {, blocks: (B:11:0x0019, B:13:0x0021, B:15:0x0025, B:17:0x002f, B:19:0x0037, B:23:0x003f, B:25:0x0047, B:27:0x0072, B:40:0x0057), top: B:10:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0075 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:36:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void packageStateChanged(String str, int i, int i2) {
        String str2;
        boolean z;
        PackageInfo findPreferredWebViewPackage;
        boolean z2 = false;
        for (WebViewProviderInfo webViewProviderInfo : this.mSystemInterface.getWebViewPackages()) {
            if (webViewProviderInfo.packageName.equals(str)) {
                synchronized (this.mLock) {
                    try {
                        findPreferredWebViewPackage = findPreferredWebViewPackage();
                        PackageInfo packageInfo = this.mCurrentWebViewPackage;
                        str2 = packageInfo != null ? packageInfo.packageName : null;
                    } catch (WebViewPackageMissingException e) {
                        e = e;
                        str2 = null;
                    }
                    try {
                    } catch (WebViewPackageMissingException e2) {
                        e = e2;
                        boolean z3 = false;
                        this.mCurrentWebViewPackage = null;
                        Slog.e(TAG, "Could not find valid WebView package to create relro with " + e);
                        boolean z4 = z3;
                        z = z2;
                        z2 = z4;
                        if (z) {
                            return;
                        } else {
                            return;
                        }
                    }
                    if (!webViewProviderInfo.packageName.equals(findPreferredWebViewPackage.packageName) && !webViewProviderInfo.packageName.equals(str2)) {
                        if (this.mCurrentWebViewPackage != null) {
                            z = false;
                            z2 = webViewProviderInfo.packageName.equals(str2);
                            if (z) {
                                onWebViewProviderChanged(findPreferredWebViewPackage);
                            }
                        }
                    }
                    z = true;
                    z2 = webViewProviderInfo.packageName.equals(str2);
                    if (z) {
                    }
                }
                if (z || z2 || str2 == null) {
                    return;
                }
                this.mSystemInterface.killPackageDependents(str2);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareWebViewInSystemServer() {
        this.mSystemInterface.notifyZygote(isMultiProcessEnabled());
        try {
            synchronized (this.mLock) {
                this.mCurrentWebViewPackage = findPreferredWebViewPackage();
                String userChosenWebViewProvider = this.mSystemInterface.getUserChosenWebViewProvider(this.mContext);
                if (userChosenWebViewProvider != null && !userChosenWebViewProvider.equals(this.mCurrentWebViewPackage.packageName)) {
                    this.mSystemInterface.updateUserSetting(this.mContext, this.mCurrentWebViewPackage.packageName);
                }
                onWebViewProviderChanged(this.mCurrentWebViewPackage);
            }
        } catch (Throwable th) {
            Slog.e(TAG, "error preparing webview provider from system server", th);
        }
        if (getCurrentWebViewPackage() == null) {
            WebViewProviderInfo fallbackProvider = getFallbackProvider(this.mSystemInterface.getWebViewPackages());
            if (fallbackProvider != null) {
                Slog.w(TAG, "No valid provider, trying to enable " + fallbackProvider.packageName);
                this.mSystemInterface.enablePackageForAllUsers(this.mContext, fallbackProvider.packageName, true);
                return;
            }
            Slog.e(TAG, "No valid provider and no fallback available.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startZygoteWhenReady() {
        waitForAndGetProvider();
        this.mSystemInterface.ensureZygoteStarted();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleNewUser(int i) {
        if (i == 0) {
            return;
        }
        handleUserChange();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleUserRemoved(int i) {
        handleUserChange();
    }

    private void handleUserChange() {
        updateCurrentWebViewPackage(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyRelroCreationCompleted() {
        synchronized (this.mLock) {
            this.mNumRelroCreationsFinished++;
            checkIfRelrosDoneLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebViewProviderResponse waitForAndGetProvider() {
        boolean webViewIsReadyLocked;
        PackageInfo packageInfo;
        int i;
        long nanoTime = (System.nanoTime() / NS_PER_MS) + 1000;
        synchronized (this.mLock) {
            webViewIsReadyLocked = webViewIsReadyLocked();
            while (!webViewIsReadyLocked) {
                long nanoTime2 = System.nanoTime() / NS_PER_MS;
                if (nanoTime2 >= nanoTime) {
                    break;
                }
                try {
                    this.mLock.wait(nanoTime - nanoTime2);
                } catch (InterruptedException unused) {
                }
                webViewIsReadyLocked = webViewIsReadyLocked();
            }
            packageInfo = this.mCurrentWebViewPackage;
            if (webViewIsReadyLocked) {
                i = 0;
            } else if (this.mAnyWebViewInstalled) {
                String str = "Timed out waiting for relro creation, relros started " + this.mNumRelroCreationsStarted + " relros finished " + this.mNumRelroCreationsFinished + " package dirty? " + this.mWebViewPackageDirty;
                Slog.e(TAG, str);
                Trace.instant(64L, str);
                i = 3;
            } else {
                i = 4;
            }
        }
        if (!webViewIsReadyLocked) {
            Slog.w(TAG, "creating relro file timed out");
        }
        return new WebViewProviderResponse(packageInfo, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String changeProviderAndSetting(String str) {
        PackageInfo updateCurrentWebViewPackage = updateCurrentWebViewPackage(str);
        return updateCurrentWebViewPackage == null ? "" : updateCurrentWebViewPackage.packageName;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0024 A[Catch: all -> 0x0050, TRY_ENTER, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x000e, B:10:0x0014, B:14:0x0024, B:15:0x0027, B:25:0x0036, B:26:0x004e), top: B:3:0x0003, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private PackageInfo updateCurrentWebViewPackage(String str) {
        PackageInfo packageInfo;
        PackageInfo findPreferredWebViewPackage;
        boolean z;
        synchronized (this.mLock) {
            packageInfo = this.mCurrentWebViewPackage;
            if (str != null) {
                this.mSystemInterface.updateUserSetting(this.mContext, str);
            }
            try {
                findPreferredWebViewPackage = findPreferredWebViewPackage();
                if (packageInfo != null) {
                    if (findPreferredWebViewPackage.packageName.equals(packageInfo.packageName)) {
                        z = false;
                        if (z) {
                            onWebViewProviderChanged(findPreferredWebViewPackage);
                        }
                    }
                }
                z = true;
                if (z) {
                }
            } catch (WebViewPackageMissingException e) {
                this.mCurrentWebViewPackage = null;
                Slog.e(TAG, "Couldn't find WebView package to use " + e);
                return null;
            }
        }
        if (z && packageInfo != null) {
            this.mSystemInterface.killPackageDependents(packageInfo.packageName);
        }
        return findPreferredWebViewPackage;
    }

    private void onWebViewProviderChanged(PackageInfo packageInfo) {
        synchronized (this.mLock) {
            this.mAnyWebViewInstalled = true;
            if (this.mNumRelroCreationsStarted == this.mNumRelroCreationsFinished) {
                this.mCurrentWebViewPackage = packageInfo;
                this.mNumRelroCreationsStarted = Integer.MAX_VALUE;
                this.mNumRelroCreationsFinished = 0;
                this.mNumRelroCreationsStarted = this.mSystemInterface.onWebViewProviderChanged(packageInfo);
                checkIfRelrosDoneLocked();
            } else {
                this.mWebViewPackageDirty = true;
            }
        }
        if (isMultiProcessEnabled()) {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() { // from class: com.android.server.webkit.WebViewUpdateServiceImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    WebViewUpdateServiceImpl.this.startZygoteWhenReady();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebViewProviderInfo[] getValidWebViewPackages() {
        ProviderAndPackageInfo[] validWebViewPackagesAndInfos = getValidWebViewPackagesAndInfos();
        WebViewProviderInfo[] webViewProviderInfoArr = new WebViewProviderInfo[validWebViewPackagesAndInfos.length];
        for (int i = 0; i < validWebViewPackagesAndInfos.length; i++) {
            webViewProviderInfoArr[i] = validWebViewPackagesAndInfos[i].provider;
        }
        return webViewProviderInfoArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ProviderAndPackageInfo {
        public final PackageInfo packageInfo;
        public final WebViewProviderInfo provider;

        ProviderAndPackageInfo(WebViewProviderInfo webViewProviderInfo, PackageInfo packageInfo) {
            this.provider = webViewProviderInfo;
            this.packageInfo = packageInfo;
        }
    }

    private ProviderAndPackageInfo[] getValidWebViewPackagesAndInfos() {
        WebViewProviderInfo[] webViewPackages = this.mSystemInterface.getWebViewPackages();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < webViewPackages.length; i++) {
            try {
                PackageInfo packageInfoForProvider = this.mSystemInterface.getPackageInfoForProvider(webViewPackages[i]);
                if (validityResult(webViewPackages[i], packageInfoForProvider) == 0) {
                    arrayList.add(new ProviderAndPackageInfo(webViewPackages[i], packageInfoForProvider));
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return (ProviderAndPackageInfo[]) arrayList.toArray(new ProviderAndPackageInfo[arrayList.size()]);
    }

    private PackageInfo findPreferredWebViewPackage() throws WebViewPackageMissingException {
        ProviderAndPackageInfo[] validWebViewPackagesAndInfos = getValidWebViewPackagesAndInfos();
        String userChosenWebViewProvider = this.mSystemInterface.getUserChosenWebViewProvider(this.mContext);
        for (ProviderAndPackageInfo providerAndPackageInfo : validWebViewPackagesAndInfos) {
            if (providerAndPackageInfo.provider.packageName.equals(userChosenWebViewProvider) && isInstalledAndEnabledForAllUsers(this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, providerAndPackageInfo.provider))) {
                return providerAndPackageInfo.packageInfo;
            }
        }
        for (ProviderAndPackageInfo providerAndPackageInfo2 : validWebViewPackagesAndInfos) {
            WebViewProviderInfo webViewProviderInfo = providerAndPackageInfo2.provider;
            if (webViewProviderInfo.availableByDefault && isInstalledAndEnabledForAllUsers(this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, webViewProviderInfo))) {
                return providerAndPackageInfo2.packageInfo;
            }
        }
        this.mAnyWebViewInstalled = false;
        throw new WebViewPackageMissingException("Could not find a loadable WebView package");
    }

    private static boolean isInstalledAndEnabledForAllUsers(List<UserPackage> list) {
        for (UserPackage userPackage : list) {
            if (!userPackage.isInstalledPackage() || !userPackage.isEnabledPackage()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebViewProviderInfo[] getWebViewPackages() {
        return this.mSystemInterface.getWebViewPackages();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageInfo getCurrentWebViewPackage() {
        PackageInfo packageInfo;
        synchronized (this.mLock) {
            packageInfo = this.mCurrentWebViewPackage;
        }
        return packageInfo;
    }

    private boolean webViewIsReadyLocked() {
        return !this.mWebViewPackageDirty && this.mNumRelroCreationsStarted == this.mNumRelroCreationsFinished && this.mAnyWebViewInstalled;
    }

    private void checkIfRelrosDoneLocked() {
        if (this.mNumRelroCreationsStarted == this.mNumRelroCreationsFinished) {
            if (this.mWebViewPackageDirty) {
                this.mWebViewPackageDirty = false;
                try {
                    onWebViewProviderChanged(findPreferredWebViewPackage());
                    return;
                } catch (WebViewPackageMissingException unused) {
                    this.mCurrentWebViewPackage = null;
                    return;
                }
            }
            this.mLock.notifyAll();
        }
    }

    private int validityResult(WebViewProviderInfo webViewProviderInfo, PackageInfo packageInfo) {
        if (!UserPackage.hasCorrectTargetSdkVersion(packageInfo)) {
            return 1;
        }
        if (!versionCodeGE(packageInfo.getLongVersionCode(), getMinimumVersionCode()) && !this.mSystemInterface.systemIsDebuggable()) {
            return 2;
        }
        if (providerHasValidSignature(webViewProviderInfo, packageInfo, this.mSystemInterface)) {
            return WebViewFactory.getWebViewLibrary(packageInfo.applicationInfo) == null ? 4 : 0;
        }
        return 3;
    }

    private static boolean versionCodeGE(long j, long j2) {
        return j / 100000 >= j2 / 100000;
    }

    private long getMinimumVersionCode() {
        long j = this.mMinimumVersionCode;
        if (j > 0) {
            return j;
        }
        long j2 = -1;
        for (WebViewProviderInfo webViewProviderInfo : this.mSystemInterface.getWebViewPackages()) {
            if (webViewProviderInfo.availableByDefault) {
                try {
                    long factoryPackageVersion = this.mSystemInterface.getFactoryPackageVersion(webViewProviderInfo.packageName);
                    if (j2 < 0 || factoryPackageVersion < j2) {
                        j2 = factoryPackageVersion;
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
        }
        this.mMinimumVersionCode = j2;
        return j2;
    }

    private static boolean providerHasValidSignature(WebViewProviderInfo webViewProviderInfo, PackageInfo packageInfo, SystemInterface systemInterface) {
        if (systemInterface.systemIsDebuggable() || packageInfo.applicationInfo.isSystemApp()) {
            return true;
        }
        if (packageInfo.signatures.length != 1) {
            return false;
        }
        for (Signature signature : webViewProviderInfo.signatures) {
            if (signature.equals(packageInfo.signatures[0])) {
                return true;
            }
        }
        return false;
    }

    private static WebViewProviderInfo getFallbackProvider(WebViewProviderInfo[] webViewProviderInfoArr) {
        for (WebViewProviderInfo webViewProviderInfo : webViewProviderInfoArr) {
            if (webViewProviderInfo.isFallback) {
                return webViewProviderInfo;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isMultiProcessEnabled() {
        int multiProcessSetting = this.mSystemInterface.getMultiProcessSetting(this.mContext);
        return this.mSystemInterface.isMultiProcessDefaultEnabled() ? multiProcessSetting > Integer.MIN_VALUE : multiProcessSetting >= Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enableMultiProcess(boolean z) {
        PackageInfo currentWebViewPackage = getCurrentWebViewPackage();
        this.mSystemInterface.setMultiProcessSetting(this.mContext, z ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        this.mSystemInterface.notifyZygote(z);
        if (currentWebViewPackage != null) {
            this.mSystemInterface.killPackageDependents(currentWebViewPackage.packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpState(PrintWriter printWriter) {
        printWriter.println("Current WebView Update Service state");
        printWriter.println(String.format("  Multiprocess enabled: %b", Boolean.valueOf(isMultiProcessEnabled())));
        synchronized (this.mLock) {
            PackageInfo packageInfo = this.mCurrentWebViewPackage;
            if (packageInfo == null) {
                printWriter.println("  Current WebView package is null");
            } else {
                printWriter.println(String.format("  Current WebView package (name, version): (%s, %s)", packageInfo.packageName, packageInfo.versionName));
            }
            printWriter.println(String.format("  Minimum targetSdkVersion: %d", 33));
            printWriter.println(String.format("  Minimum WebView version code: %d", Long.valueOf(this.mMinimumVersionCode)));
            printWriter.println(String.format("  Number of relros started: %d", Integer.valueOf(this.mNumRelroCreationsStarted)));
            printWriter.println(String.format("  Number of relros finished: %d", Integer.valueOf(this.mNumRelroCreationsFinished)));
            printWriter.println(String.format("  WebView package dirty: %b", Boolean.valueOf(this.mWebViewPackageDirty)));
            printWriter.println(String.format("  Any WebView package installed: %b", Boolean.valueOf(this.mAnyWebViewInstalled)));
            try {
                PackageInfo findPreferredWebViewPackage = findPreferredWebViewPackage();
                printWriter.println(String.format("  Preferred WebView package (name, version): (%s, %s)", findPreferredWebViewPackage.packageName, findPreferredWebViewPackage.versionName));
            } catch (WebViewPackageMissingException unused) {
                printWriter.println(String.format("  Preferred WebView package: none", new Object[0]));
            }
            dumpAllPackageInformationLocked(printWriter);
        }
    }

    private void dumpAllPackageInformationLocked(PrintWriter printWriter) {
        WebViewProviderInfo[] webViewPackages = this.mSystemInterface.getWebViewPackages();
        printWriter.println("  WebView packages:");
        for (WebViewProviderInfo webViewProviderInfo : webViewPackages) {
            PackageInfo packageInfo = this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, webViewProviderInfo).get(0).getPackageInfo();
            if (packageInfo == null) {
                printWriter.println(String.format("    %s is NOT installed.", webViewProviderInfo.packageName));
            } else {
                int validityResult = validityResult(webViewProviderInfo, packageInfo);
                String format = String.format("versionName: %s, versionCode: %d, targetSdkVersion: %d", packageInfo.versionName, Long.valueOf(packageInfo.getLongVersionCode()), Integer.valueOf(packageInfo.applicationInfo.targetSdkVersion));
                if (validityResult == 0) {
                    boolean isInstalledAndEnabledForAllUsers = isInstalledAndEnabledForAllUsers(this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, webViewProviderInfo));
                    Object[] objArr = new Object[3];
                    objArr[0] = packageInfo.packageName;
                    objArr[1] = format;
                    objArr[2] = isInstalledAndEnabledForAllUsers ? "" : "NOT";
                    printWriter.println(String.format("    Valid package %s (%s) is %s installed/enabled for all users", objArr));
                } else {
                    printWriter.println(String.format("    Invalid package %s (%s), reason: %s", packageInfo.packageName, format, getInvalidityReason(validityResult)));
                }
            }
        }
    }
}
