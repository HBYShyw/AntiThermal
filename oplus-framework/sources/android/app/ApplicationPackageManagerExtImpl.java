package android.app;

import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.OplusPackageManager;
import android.content.pm.OplusPermissionManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.IOplusThemeManager;
import android.content.res.IUxIconPackageManagerExt;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.multiapp.OplusMultiAppManager;
import com.oplus.permission.IOplusPermissionCheckInjector;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ApplicationPackageManagerExtImpl implements IApplicationPackageManagerExt {
    private ApplicationPackageManager mApplicationPackageManager;
    private ContextImpl mContext;
    private OplusPackageManager mOplusPm;
    private IUxIconPackageManagerExt mUxIconPmExt = IUxIconPackageManagerExt.DEFAULT;
    private static final String TAG = ApplicationPackageManagerExtImpl.class.getSimpleName();
    private static HashMap<String, Bitmap> mAppIconsCache = new HashMap<>();
    private static HashMap<String, Bitmap> mActivityIconsCache = new HashMap<>();
    private static boolean mIconCacheDirty = false;

    public ApplicationPackageManagerExtImpl(Object base) {
        this.mApplicationPackageManager = (ApplicationPackageManager) base;
    }

    public void init(ContextImpl context) {
        this.mContext = context;
        this.mOplusPm = new OplusPackageManager(context);
        this.mUxIconPmExt = OplusFrameworkFactory.getInstance().getFeature(IUxIconPackageManagerExt.DEFAULT, new Object[]{this.mApplicationPackageManager, context});
    }

    public Object getUxIconPackageManagerExt() {
        return this.mUxIconPmExt;
    }

    public boolean arePermissionsIndividuallyControlledOverrideTrue() {
        return OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_PERMISSION_INTERCEPT);
    }

    public Drawable getDefaultActivityIcon(Context context) {
        return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).getDefaultActivityIcon(this.mUxIconPmExt, context, context.getResources().mResourcesExt);
    }

    public void modifyResultInGetResourcesForApplication(Resources r, ApplicationInfo app) {
        if (r.getImpl() != null && r.getImpl().mResourcesImplExt != null) {
            r.getImpl().mResourcesImplExt.init(app.packageName);
        }
    }

    public Drawable interceptGetDrawableInLoadUnbadgedItemIcon(PackageManager manager, PackageItemInfo itemInfo, ApplicationInfo appInfo) {
        if (((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).isOplusIcons()) {
            return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).loadPackageItemIcon(itemInfo, manager, this.mUxIconPmExt, appInfo, true);
        }
        return manager.getDrawable(itemInfo.packageName, itemInfo.icon, appInfo);
    }

    public List<PackageInfo> getInstalledPackagesAsUserExt(PackageManager.PackageInfoFlags flags, int userId) {
        if (!OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission(OplusPermissionManager.PERMISSION_READ_APPS, Binder.getCallingPid(), Binder.getCallingUid(), "get_installed")) {
            try {
                String pkg = this.mContext.getPackageName();
                Slog.d(TAG, pkg + " in userId=" + userId + " doesn't have GET_INSTALLED_APPS perm, return self!");
                return Collections.singletonList(this.mApplicationPackageManager.getPackageInfoAsUser(pkg, flags, userId));
            } catch (PackageManager.NameNotFoundException e) {
                Slog.w(TAG, "getInstalledPackagesAsUserExt error : " + e.getMessage());
                return Collections.emptyList();
            }
        }
        return null;
    }

    public List<ApplicationInfo> getInstalledApplicationsAsUserExt(PackageManager.ApplicationInfoFlags flags, int userId) {
        if (!OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission(OplusPermissionManager.PERMISSION_READ_APPS, Binder.getCallingPid(), Binder.getCallingUid(), "get_installed")) {
            try {
                String pkg = this.mContext.getPackageName();
                Slog.d(TAG, pkg + " in userId=" + userId + " doesn't have GET_INSTALLED_APPS perm, return self!");
                return Collections.singletonList(this.mApplicationPackageManager.getApplicationInfoAsUser(pkg, flags, userId));
            } catch (PackageManager.NameNotFoundException e) {
                Slog.w(TAG, "getInstalledApplicationsAsUserExt error : " + e.getMessage());
                return Collections.emptyList();
            }
        }
        return null;
    }

    public void beforeGetInstalledPackagesOrApplications() {
        OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission(OplusPermissionManager.PERMISSION_READ_APPS, Binder.getCallingPid(), Binder.getCallingUid(), "get_installed");
    }

    public Bitmap getAppIconBitmap(String packageName) {
        try {
            return this.mOplusPm.getAppIconBitmap(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Map<String, Bitmap> getAppIconsCache(boolean compress) {
        try {
            return this.mOplusPm.getAppIconsCache(compress);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Map<String, Bitmap> getActivityIconsCache(IPackageDeleteObserver observer) {
        try {
            return this.mOplusPm.getActivityIconsCache(observer);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int oplusFreezePackage(String pkgName, int userId, int freezeFlag, int flag) {
        try {
            return this.mOplusPm.oplusFreezePackage(pkgName, userId, freezeFlag, flag, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int oplusUnFreezePackage(String pkgName, int userId, int freezeFlag, int flag) {
        try {
            return this.mOplusPm.oplusUnFreezePackage(pkgName, userId, freezeFlag, flag, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int getOplusFreezePackageState(String pkgName, int userId) {
        try {
            return this.mOplusPm.getOplusFreezePackageState(pkgName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean inOplusFreezePackageList(String pkgName, int userId) {
        try {
            return this.mOplusPm.inOplusFreezePackageList(pkgName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<String> getOplusFreezedPackageList(int userId) {
        try {
            return this.mOplusPm.getOplusFreezedPackageList(userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int getOplusPackageFreezeFlag(String pkgName, int userId) {
        try {
            return this.mOplusPm.getOplusPackageFreezeFlag(pkgName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean isSecurePayApp(String pkg) {
        try {
            return this.mOplusPm.isSecurePayApp(pkg);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean isFullFunctionMode() {
        return this.mOplusPm.isClosedSuperFirewall();
    }

    public boolean isClosedSuperFirewall() {
        return this.mOplusPm.isClosedSuperFirewall();
    }

    public FeatureInfo[] getOplusSystemAvailableFeatures() {
        try {
            return this.mOplusPm.getOplusSystemAvailableFeatures();
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean isSystemDataApp(String packageName) {
        try {
            return this.mOplusPm.isSystemDataApp(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public void checkEMMApkRuntimePermission(ComponentName cn2) throws SecurityException {
        String packageName = cn2.getPackageName();
        if (packageName == null) {
            throw new SecurityException("Package name is null");
        }
    }

    public void checkAndLogMultiApp(boolean print, Context context, Object name, String tag) {
        if (print && context != null && 999 == context.getUserId()) {
            Log.i(TAG, "multi app -> " + tag + " is null! " + name + " ,pkg:" + context.getPackageName());
        }
    }

    public Drawable getMultiAppUserBadgedIcon(UserHandle user) {
        if (user == null || 999 != user.getIdentifier()) {
            return null;
        }
        Drawable badge = this.mContext.getResources().getDrawable(201850903);
        return badge;
    }

    public int getMultiAppUserBadgeId(UserHandle user, boolean withbg) {
        if (user != null && OplusMultiAppManager.getInstance().isMultiAppUserId(user.getIdentifier())) {
            if (withbg) {
                return 201850911;
            }
            return 201850912;
        }
        return -1;
    }

    public boolean isMultiAppUserId(int userId) {
        return OplusMultiAppManager.getInstance().isMultiAppUserId(userId);
    }
}
