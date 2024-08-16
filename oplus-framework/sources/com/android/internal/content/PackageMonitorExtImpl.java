package com.android.internal.content;

import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentFilterExtImpl;
import android.os.Handler;
import android.os.Looper;
import android.os.PatternMatcher;
import android.os.Process;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.os.BackgroundThread;
import java.util.Objects;

/* loaded from: classes.dex */
public class PackageMonitorExtImpl implements IPackageMonitorExt {
    private static final int FLAG_DEFAULT_VALUE = 0;
    private static final int FLAG_NO_REPLACING_CATEGORY = 2;
    private static final int FLAG_REPLACING_CATEGORY = 1;
    private static final String PACKAGE_RESTRICTION_FILTER_VALUE_NOREPLACING = "@PACKAGE=NOREPLACING";
    private static final String PACKAGE_RESTRICTION_FILTER_VALUE_REPLACING = "@PACKAGE=REPLACING";
    private static final String TAG = PackageMonitorExtImpl.class.getSimpleName();
    private PackageMonitor mBase;

    public PackageMonitorExtImpl(Object base) {
        this.mBase = (PackageMonitor) base;
    }

    private void register(Context context, Looper thread, UserHandle user, boolean externalStorage, IntentFilter packageFilter, IntentFilter otherFilter) {
        registerInner(context, user, externalStorage, thread == null ? BackgroundThread.getHandler() : new Handler(thread), packageFilter, otherFilter);
    }

    public final void register(Context context, Looper thread, UserHandle user, boolean externalStorage, int[] callbackMethods) {
        register(context, thread, user, externalStorage, callbackMethods, (PatternMatcher) null);
    }

    public final void register(Context context, Looper thread, UserHandle user, boolean externalStorage, int[] callbackMethods, PatternMatcher ssp) {
        register(context, user, externalStorage, thread == null ? BackgroundThread.getHandler() : new Handler(thread), callbackMethods, ssp);
    }

    public final void register(Context context, UserHandle user, boolean externalStorage, Handler handler, int[] callbackMethods) {
        register(context, user, externalStorage, handler, callbackMethods, (PatternMatcher) null);
    }

    public final void register(Context context, UserHandle user, boolean externalStorage, Handler handler, int[] callbackMethods, PatternMatcher ssp) {
        char c;
        int[] iArr = callbackMethods;
        if (iArr == null || iArr.length == 0) {
            Slog.w(TAG, "callbackMethods is null or empty, use original PackageMonitor");
            this.mBase.register(context, user, externalStorage, handler);
            return;
        }
        IntentFilter packageFilter = new IntentFilter();
        packageFilter.addDataScheme("package");
        if (ssp != null) {
            packageFilter.addDataSchemeSpecificPart(ssp);
        }
        IntentFilter noPackageFilter = new IntentFilter();
        int[] replacingFlags = new int[2];
        int length = iArr.length;
        int i = 0;
        while (i < length) {
            int callbackMethod = iArr[i];
            int i2 = length;
            switch (callbackMethod) {
                case 1:
                case 20:
                    Slog.w(TAG, "callbackMethod need all action, use original PackageMonitor");
                    this.mBase.register(context, user, externalStorage, handler);
                    return;
                case 2:
                    packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
                    c = 2;
                    replacingFlags[0] = replacingFlags[0] | 2;
                    break;
                case 3:
                    packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
                    replacingFlags[0] = replacingFlags[0] | 1;
                    c = 2;
                    break;
                case 4:
                    packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
                    noPackageFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
                    replacingFlags[0] = replacingFlags[0] | 2;
                    replacingFlags[0] = replacingFlags[0] | 1;
                    c = 2;
                    break;
                case 5:
                    packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
                    replacingFlags[0] = replacingFlags[0] | 1;
                    packageFilter.addAction("android.intent.action.PACKAGE_CHANGED");
                    c = 2;
                    break;
                case 6:
                    packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
                    replacingFlags[1] = replacingFlags[1] | 1;
                    c = 2;
                    break;
                case 7:
                case 8:
                    packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
                    replacingFlags[1] = replacingFlags[1] | 2;
                    c = 2;
                    break;
                case 9:
                    packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
                    replacingFlags[1] = replacingFlags[1] | 1;
                    c = 2;
                    replacingFlags[1] = replacingFlags[1] | 2;
                    noPackageFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
                    break;
                case 10:
                    packageFilter.addAction("android.intent.action.PACKAGE_CHANGED");
                    c = 2;
                    break;
                case 11:
                    packageFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
                    c = 2;
                    break;
                case 12:
                    packageFilter.addAction("android.intent.action.QUERY_PACKAGE_RESTART");
                    packageFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
                    c = 2;
                    break;
                case 13:
                    noPackageFilter.addAction("android.intent.action.UID_REMOVED");
                    c = 2;
                    break;
                case 14:
                    noPackageFilter.addAction("android.intent.action.USER_STOPPED");
                    c = 2;
                    break;
                case 15:
                    noPackageFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
                    c = 2;
                    break;
                case 16:
                    noPackageFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
                    c = 2;
                    break;
                case 17:
                    noPackageFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
                    c = 2;
                    break;
                case 18:
                    noPackageFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
                    c = 2;
                    break;
                case 19:
                    packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
                    packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
                    replacingFlags[1] = replacingFlags[1] | 2;
                    packageFilter.addAction("android.intent.action.PACKAGE_CHANGED");
                    noPackageFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
                    noPackageFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
                    noPackageFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
                    noPackageFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
                    c = 2;
                    break;
                default:
                    c = 2;
                    break;
            }
            i++;
            iArr = callbackMethods;
            length = i2;
        }
        addReplacingCategoryIfNeed(packageFilter, "android.intent.action.PACKAGE_ADDED", replacingFlags[0]);
        addReplacingCategoryIfNeed(packageFilter, "android.intent.action.PACKAGE_REMOVED", replacingFlags[1]);
        registerInner(context, user, externalStorage, handler, packageFilter, noPackageFilter);
    }

    private void addReplacingCategoryIfNeed(IntentFilter packageFilter, String action, int packageReplacingFlag) {
        if ((packageReplacingFlag & 1) == 1) {
            if ((packageReplacingFlag & 2) != 2) {
                packageFilter.addCategory(IntentFilterExtImpl.BROADCAST_CATEGORY_EXPAND_FLAG + action + PACKAGE_RESTRICTION_FILTER_VALUE_REPLACING);
            }
        } else if ((packageReplacingFlag & 2) == 2) {
            packageFilter.addCategory(IntentFilterExtImpl.BROADCAST_CATEGORY_EXPAND_FLAG + action + PACKAGE_RESTRICTION_FILTER_VALUE_NOREPLACING);
        }
    }

    private void registerInner(Context context, UserHandle user, boolean externalStorage, Handler handler, IntentFilter packageFilter, IntentFilter noPackageFilter) {
        if (packageFilter == null && noPackageFilter == null) {
            Slog.w(TAG, "registerInner: packageFilter and noPackageFilter is null, use original PackageMonitor");
            this.mBase.register(context, user, externalStorage, handler);
            return;
        }
        if (this.mBase.mRegisteredContext != null) {
            throw new IllegalStateException("Already registered");
        }
        this.mBase.mRegisteredContext = context;
        this.mBase.mRegisteredHandler = (Handler) Objects.requireNonNull(handler);
        setPriorityForCore(packageFilter, noPackageFilter);
        if (user != null) {
            if (packageFilter != null && packageFilter.countActions() > 0) {
                PackageMonitor packageMonitor = this.mBase;
                context.registerReceiverAsUser(packageMonitor, user, packageFilter, null, packageMonitor.mRegisteredHandler);
            }
            if (noPackageFilter != null && noPackageFilter.countActions() > 0) {
                PackageMonitor packageMonitor2 = this.mBase;
                context.registerReceiverAsUser(packageMonitor2, user, noPackageFilter, null, packageMonitor2.mRegisteredHandler);
                return;
            }
            return;
        }
        if (packageFilter != null && packageFilter.countActions() > 0) {
            PackageMonitor packageMonitor3 = this.mBase;
            context.registerReceiver(packageMonitor3, packageFilter, null, packageMonitor3.mRegisteredHandler);
        }
        if (noPackageFilter != null && noPackageFilter.countActions() > 0) {
            PackageMonitor packageMonitor4 = this.mBase;
            context.registerReceiver(packageMonitor4, noPackageFilter, null, packageMonitor4.mRegisteredHandler);
        }
    }

    private void setPriorityForCore(IntentFilter packageFilter, IntentFilter noPackageFilter) {
        if (!UserHandle.isCore(Process.myUid())) {
            return;
        }
        if (packageFilter != null) {
            packageFilter.setPriority(1000);
        }
        if (noPackageFilter != null) {
            noPackageFilter.setPriority(1000);
        }
    }
}
