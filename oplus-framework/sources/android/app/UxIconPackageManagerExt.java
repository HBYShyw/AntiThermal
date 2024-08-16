package android.app;

import android.app.OplusUxIconConstants;
import android.common.OplusFeatureCache;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.IOplusThemeManager;
import android.content.res.IResourcesExt;
import android.content.res.IUxIconPackageManagerExt;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class UxIconPackageManagerExt implements IUxIconPackageManagerExt {
    private static final String CONTACT_PREFIX = "dialer_";
    private static final String EDIT_ICON_PATH = "/data/oplus/uxicons/choose/";
    private static final String ICON_SUFFIX = ".png";
    private static final String META_DATA = "uxicon_support_editicon";
    private static final String TAG = "UxIconPackageManagerExt";
    private Context mContext;
    private PackageManager mPackageManager;

    public UxIconPackageManagerExt(PackageManager packageManager, Context context) {
        this.mPackageManager = null;
        this.mContext = null;
        this.mPackageManager = packageManager;
        this.mContext = context;
    }

    public PackageManager getPackageManager() {
        return this.mPackageManager;
    }

    public UserManager getUserManager() {
        ApplicationPackageManager applicationPackageManager = this.mPackageManager;
        if (applicationPackageManager != null && (applicationPackageManager instanceof ApplicationPackageManager)) {
            return applicationPackageManager.getUserManager();
        }
        return UserManager.get(this.mContext);
    }

    public boolean checkChooseIconsRootPath() {
        File rootFile = new File(EDIT_ICON_PATH);
        if (!rootFile.exists()) {
            Log.d(TAG, "UxIcon checkChooseIconsRootPath false");
            return false;
        }
        return true;
    }

    private Drawable loadItemIconWithoutEdit(PackageItemInfo itemInfo, ApplicationInfo appInfo, boolean isConvertEnable) {
        if (isConvertEnable && ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).supportUxOnline(this.mPackageManager, this.mContext.getPackageName())) {
            if (((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).supportUxIcon(this, appInfo, itemInfo.packageName)) {
                String activityName = null;
                if (itemInfo instanceof ActivityInfo) {
                    activityName = itemInfo.name;
                }
                Drawable dr = ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).getDrawableFromUxIcon(this, itemInfo.packageName, activityName, itemInfo.icon, appInfo, false);
                return dr;
            }
            Drawable dr2 = OplusThemeHelper.getInstance().getDrawable(this, itemInfo.packageName, itemInfo.icon, appInfo, itemInfo.name);
            return dr2;
        }
        Drawable dr3 = this.mPackageManager.getDrawable(itemInfo.packageName, itemInfo.icon, appInfo);
        return dr3;
    }

    public Drawable loadItemIcon(PackageItemInfo itemInfo, ApplicationInfo appInfo, boolean isConvertEnable) {
        if (itemInfo.packageName == null) {
            return null;
        }
        if (checkChooseIconsRootPath()) {
            boolean supportEditIcon = false;
            try {
                ApplicationInfo usageAppInfo = ActivityThread.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 128L, this.mContext.getUserId());
                if (usageAppInfo != null && usageAppInfo.metaData != null) {
                    supportEditIcon = usageAppInfo.metaData.getBoolean(META_DATA, false);
                }
            } catch (RemoteException e) {
            }
            if (supportEditIcon) {
                FileInputStream fis = null;
                try {
                    try {
                        String path = EDIT_ICON_PATH + itemInfo.packageName + ".png";
                        if (appInfo.packageName.equals(OplusUxIconConstants.IconLoader.COM_ANDROID_CONTACTS) && (itemInfo instanceof ActivityInfo) && ((ActivityInfo) itemInfo).getIconResource() != appInfo.icon) {
                            path = "/data/oplus/uxicons/choose/dialer_" + itemInfo.packageName + ".png";
                        }
                        fis = new FileInputStream(path);
                        Bitmap bm = BitmapFactory.decodeStream(fis);
                        dr = bm != null ? new BitmapDrawable(bm) : null;
                        fis.close();
                    } catch (Exception e2) {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (Throwable th) {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e3) {
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                }
                if (dr != null) {
                    Log.e(TAG, "success loadItemIcon --" + itemInfo.packageName + " return choose drawable");
                    OplusUXIconLoadHelper.setDarkFilterToDrawable(dr);
                    return dr;
                }
            }
        }
        return loadItemIconWithoutEdit(itemInfo, appInfo, isConvertEnable);
    }

    public Drawable getCachedIconForThemeHelper(String packageName, int id) {
        ApplicationPackageManager applicationPackageManager = this.mPackageManager;
        if (applicationPackageManager == null || !(applicationPackageManager instanceof ApplicationPackageManager)) {
            return null;
        }
        IApplicationPackageManagerWrapper wrapper = applicationPackageManager.getWrapper();
        Object resourceName = wrapper.newResourceName(packageName, id);
        return wrapper.getCachedIcon(resourceName);
    }

    public void putCachedIconForThemeHelper(String packageName, int id, Drawable dr) {
        ApplicationPackageManager applicationPackageManager = this.mPackageManager;
        if (applicationPackageManager == null || !(applicationPackageManager instanceof ApplicationPackageManager)) {
            return;
        }
        IApplicationPackageManagerWrapper wrapper = applicationPackageManager.getWrapper();
        Object resourceName = wrapper.newResourceName(packageName, id);
        wrapper.putCachedIcon(resourceName, dr);
    }

    public IResourcesExt getOplusBaseResourcesForThemeHelper(ApplicationInfo app) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = this.mPackageManager;
        if (packageManager == null) {
            return null;
        }
        Resources resources = packageManager.getResourcesForApplication(app);
        return resources.mResourcesExt;
    }

    public Drawable getUxIconDrawable(Drawable src, boolean isForegroundDrawable) {
        return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).getDrawableForApp(src, isForegroundDrawable);
    }

    public Drawable getUxIconDrawable(String packageName, Drawable src, boolean isForegroundDrawable) {
        PackageManager packageManager = this.mPackageManager;
        if (packageManager == null) {
            return src;
        }
        try {
            Resources res = packageManager.getResourcesForApplication(packageName);
            if (res == null) {
                return src;
            }
            return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).getDrawableForApp(res, res.mResourcesExt, src, isForegroundDrawable);
        } catch (PackageManager.NameNotFoundException ex) {
            Log.e(TAG, "getResourcesForApplication error: " + ex.getMessage());
            return src;
        }
    }

    public Drawable loadResolveIcon(ResolveInfo info, PackageManager pm, String packageName, int resid, ApplicationInfo appInfo, boolean convert) {
        Drawable dr = loadDrawableFromTheme(info, packageName, resid, appInfo, convert);
        if (dr == null) {
            return pm.getDrawable(packageName, resid, appInfo);
        }
        return dr;
    }

    private Drawable loadDrawableFromTheme(ResolveInfo info, String packageName, int resid, ApplicationInfo ai, boolean convert) {
        if (convert) {
            if (OplusUXIconLoadHelper.supportUxIcon(this, ai, packageName)) {
                String activityName = info.activityInfo != null ? info.activityInfo.name : null;
                Drawable dr = OplusUXIconLoadHelper.getDrawable(this, packageName, activityName, resid, ai, true);
                return dr;
            }
            Drawable dr2 = OplusThemeHelper.getInstance().getDrawable(this, packageName, resid, ai, null);
            return dr2;
        }
        ComponentInfo ci = info.activityInfo != null ? info.activityInfo : info.serviceInfo;
        return OplusThemeHelper.getInstance().getDrawable(this, packageName, info.icon, ai, ci, OplusThemeHelper.getInstance().isCustomizedIcon(info.filter));
    }

    public void clearCachedIconForActivity(ComponentName activityName) throws PackageManager.NameNotFoundException {
        if (activityName != null && !TextUtils.isEmpty(activityName.getPackageName())) {
            ApplicationPackageManager.handlePackageBroadcast(3, new String[]{activityName.getPackageName()}, false);
        }
    }
}
