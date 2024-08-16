package com.oplus.wrapper.content.pm;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;
import android.os.UserHandle;
import com.oplus.wrapper.os.storage.VolumeInfo;
import com.oplus.zoomwindow.OplusZoomWindowManager;
import java.util.List;

/* loaded from: classes.dex */
public class PackageManager {
    public static final String COMPILER_FILTER_EVERYTHING = "everything";
    public static final String COMPILER_FILTER_EXTRACT = "extract";
    public static final String COMPILER_FILTER_QUICKEN = "quicken";
    public static final String COMPILER_FILTER_SPEED = "speed";
    public static final String COMPILER_FILTER_SPEED_PROFILE = "speed-profile";
    public static final String COMPILER_FILTER_VERIFY = "verify";
    public static final int DELETE_ALL_USERS = 2;
    public static final int DELETE_CHATTY = Integer.MIN_VALUE;
    public static final int DELETE_DONT_KILL_APP = 8;
    public static final int DELETE_KEEP_DATA = 1;
    public static final int DELETE_SYSTEM_APP = 4;
    private final android.content.pm.PackageManager mPackageManager;
    public static final int INSTALL_REPLACE_EXISTING = getInstallReplaceExisting();
    public static final int FLAG_PERMISSION_REVIEW_REQUIRED = getFlagPermissionReviewRequired();
    public static final int INSTALL_FAILED_INVALID_URI = getInstallFailedInvalidUri();
    public static final int RESTRICTION_NONE = getRestrictionNone();
    public static final int RESTRICTION_HIDE_FROM_SUGGESTIONS = getRestrictionHideFromSuggestions();
    public static final int RESTRICTION_HIDE_NOTIFICATIONS = getRestrictionHideNotifications();
    public static final int MATCH_ANY_USER = getMatchAnyUser();

    private static int getRestrictionNone() {
        return 0;
    }

    private static int getRestrictionHideFromSuggestions() {
        return 1;
    }

    private static int getRestrictionHideNotifications() {
        return 2;
    }

    private static int getInstallReplaceExisting() {
        return 2;
    }

    private static int getFlagPermissionReviewRequired() {
        return 64;
    }

    private static int getInstallFailedInvalidUri() {
        return -3;
    }

    private static int getMatchAnyUser() {
        return OplusZoomWindowManager.FLAG_TOUCH_OUTSIDE_CONTROL_VIEW;
    }

    public static final boolean performDexOptMode(String packageName, boolean checkProfiles, String targetCompilerFilter, boolean force, boolean bootComplete, String splitName) {
        android.content.pm.IPackageManager pmInterface = ActivityThread.getPackageManager();
        if (pmInterface == null) {
            return false;
        }
        try {
            return pmInterface.performDexOptMode(packageName, checkProfiles, targetCompilerFilter, force, bootComplete, splitName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public PackageManager(android.content.pm.PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    public ComponentName getHomeActivities(List<android.content.pm.ResolveInfo> outActivities) {
        return this.mPackageManager.getHomeActivities(outActivities);
    }

    public void deleteApplicationCacheFiles(String packageName, final IPackageDataObserver observer) {
        android.content.pm.IPackageDataObserver packageDataObserver = observer == null ? null : new IPackageDataObserver.Stub() { // from class: com.oplus.wrapper.content.pm.PackageManager.1
            public void onRemoveCompleted(String packageName2, boolean succeeded) throws RemoteException {
                observer.onRemoveCompleted(packageName2, succeeded);
            }
        };
        this.mPackageManager.deleteApplicationCacheFiles(packageName, packageDataObserver);
    }

    public void deleteApplicationCacheFilesAsUser(String packageName, int userId, final IPackageDataObserver observer) {
        android.content.pm.IPackageDataObserver packageDataObserver = observer == null ? null : new IPackageDataObserver.Stub() { // from class: com.oplus.wrapper.content.pm.PackageManager.2
            public void onRemoveCompleted(String packageName2, boolean succeeded) throws RemoteException {
                observer.onRemoveCompleted(packageName2, succeeded);
            }
        };
        this.mPackageManager.deleteApplicationCacheFilesAsUser(packageName, userId, packageDataObserver);
    }

    public int movePackage(String packageName, VolumeInfo vol) {
        return this.mPackageManager.movePackage(packageName, vol.getVolumeInfo());
    }

    public List<android.content.pm.ApplicationInfo> getInstalledApplicationsAsUser(int flags, int userId) {
        return this.mPackageManager.getInstalledApplicationsAsUser(flags, userId);
    }

    public List<android.content.pm.ResolveInfo> queryIntentActivitiesAsUser(Intent intent, int flags, int userId) {
        return this.mPackageManager.queryIntentActivitiesAsUser(intent, flags, userId);
    }

    public void grantRuntimePermission(String packageName, String permName, UserHandle user) {
        this.mPackageManager.grantRuntimePermission(packageName, permName, user);
    }

    public void deletePackage(String packageName, final IPackageDeleteObserver observer, int flags) {
        android.content.pm.IPackageDeleteObserver iPackageDeleteObserver = null;
        if (observer != null) {
            iPackageDeleteObserver = new IPackageDeleteObserver.Stub() { // from class: com.oplus.wrapper.content.pm.PackageManager.3
                public void packageDeleted(String packageName2, int returnCode) {
                    observer.packageDeleted(packageName2, returnCode);
                }
            };
        }
        this.mPackageManager.deletePackage(packageName, iPackageDeleteObserver, flags);
    }

    public boolean shouldShowRequestPermissionRationale(String permName) {
        return this.mPackageManager.shouldShowRequestPermissionRationale(permName);
    }

    public void getPackageSizeInfoAsUser(String packageName, int userId, final IPackageStatsObserver observer) {
        android.content.pm.IPackageStatsObserver iPackageStatsObserver = null;
        if (observer != null) {
            iPackageStatsObserver = new IPackageStatsObserver.Stub() { // from class: com.oplus.wrapper.content.pm.PackageManager.4
                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
                    observer.onGetStatsCompleted(pStats, succeeded);
                }
            };
        }
        this.mPackageManager.getPackageSizeInfoAsUser(packageName, userId, iPackageStatsObserver);
    }

    public void getPackageSizeInfo(String packageName, final IPackageStatsObserver observer) {
        android.content.pm.IPackageStatsObserver iPackageStatsObserver = null;
        if (observer != null) {
            iPackageStatsObserver = new IPackageStatsObserver.Stub() { // from class: com.oplus.wrapper.content.pm.PackageManager.5
                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
                    observer.onGetStatsCompleted(pStats, succeeded);
                }
            };
        }
        this.mPackageManager.getPackageSizeInfo(packageName, iPackageStatsObserver);
    }

    public android.content.pm.ApplicationInfo getApplicationInfoAsUser(String packageName, PackageManager.ApplicationInfoFlags flags, int userId) throws PackageManager.NameNotFoundException {
        return this.mPackageManager.getApplicationInfoAsUser(packageName, flags, userId);
    }

    public String[] setDistractingPackageRestrictions(String[] packages, int restrictionFlags) {
        return this.mPackageManager.setDistractingPackageRestrictions(packages, restrictionFlags);
    }
}
