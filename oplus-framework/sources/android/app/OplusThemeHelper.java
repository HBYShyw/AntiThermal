package android.app;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.IResourcesExt;
import android.content.res.IUxIconPackageManagerExt;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.theme.OplusAppIconInfo;
import com.oplus.theme.OplusConvertIcon;
import com.oplus.theme.OplusThemeUtil;
import com.oplus.theme.OplusThirdPartUtil;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class OplusThemeHelper {
    private static final String TAG = "OplusThemeHelper";
    private ReentrantLock mLock = new ReentrantLock();
    private volatile long mLastModified = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingleTonHelper {
        private static final OplusThemeHelper INSTANCE = new OplusThemeHelper();

        private SingleTonHelper() {
        }
    }

    public static OplusThemeHelper getInstance() {
        return SingleTonHelper.INSTANCE;
    }

    public static void handleExtraConfigurationChanges(int i, Configuration configuration, Context context, Handler handler) {
        if ((134217728 & i) != 0 || (i & 512) != 0 || (8388608 & i) != 0) {
            getInstance().handleExtraConfigurationChanges(i);
        }
    }

    public void handleExtraConfigurationChanges(int i) {
        if ((134217728 & i) != 0) {
            Canvas.freeCaches();
        } else if ((i & 512) != 0) {
            Canvas.freeCaches();
        } else if ((8388608 & i) != 0) {
            Canvas.freeCaches();
        }
    }

    public Drawable getDrawable(IUxIconPackageManagerExt packagemanagerExt, String packageName, int id, ApplicationInfo applicationinfo, PackageItemInfo packageiteminfo, boolean flag) {
        if (!flag || packageiteminfo == null) {
            Drawable drawable = packagemanagerExt.getPackageManager().getDrawable(packageName, id, applicationinfo);
            return drawable;
        }
        Drawable drawable2 = getDrawable(packagemanagerExt, packageName, id, applicationinfo, packageiteminfo.name);
        return drawable2;
    }

    private boolean isGoogleApps(String packageName, boolean isDefaultTheme) {
        if (isDefaultTheme && !TextUtils.isEmpty(packageName)) {
            if (packageName.startsWith("com.google.android") || packageName.equals("com.googlesuit.ggkj") || packageName.equals("com.google.earth") || packageName.equals("net.eeekeie.kekegdleiedec") || packageName.equals("com.jsoh.GoogleService") || packageName.equals("lin.wang.allspeak") || packageName.equals("com.android.vending") || packageName.equals("com.android.chrome")) {
                return true;
            }
            return false;
        }
        return false;
    }

    public Drawable getDrawable(IUxIconPackageManagerExt packagemanagerExt, String packageName, int id, ApplicationInfo applicationinfo, String name) {
        ApplicationInfo applicationinfo2;
        if (id == 0) {
            return null;
        }
        Drawable drawable = packagemanagerExt.getCachedIconForThemeHelper(packageName, id);
        if (drawable != null) {
            return drawable;
        }
        boolean parseSucceed = true;
        int userId = 0;
        if (applicationinfo == null) {
            try {
                applicationinfo2 = packagemanagerExt.getPackageManager().getApplicationInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        } else {
            applicationinfo2 = applicationinfo;
        }
        try {
            IResourcesExt colorRes = packagemanagerExt.getOplusBaseResourcesForThemeHelper(applicationinfo2);
            if (colorRes == null) {
                return null;
            }
            Resources resources = colorRes.getResources();
            OplusExtraConfiguration exConfig = getExtraConfig(colorRes, applicationinfo2.packageName);
            if (exConfig != null) {
                userId = exConfig.mUserId;
            }
            if (needUpdateTheme(exConfig, userId)) {
                OplusConvertIcon.initConvertIconForUser(resources, userId);
                parseSucceed = OplusAppIconInfo.parseIconXmlForUser(userId);
            } else {
                if (!OplusConvertIcon.hasInit()) {
                    OplusConvertIcon.initConvertIconForUser(resources, userId);
                }
                if (OplusAppIconInfo.getAppsNumbers() <= 0) {
                    parseSucceed = OplusAppIconInfo.parseIconXmlForUser(userId);
                }
            }
            if (resources != null) {
                boolean isThirdPart = OplusAppIconInfo.isThirdPart(applicationinfo2);
                if (!isThirdPart) {
                    boolean useWrap = OplusThirdPartUtil.mIsDefaultTheme;
                    int iconIndex = OplusAppIconInfo.indexOfPackageName(applicationinfo2.packageName);
                    String tempStr = iconIndex >= 0 ? OplusAppIconInfo.getIconName(iconIndex) : null;
                    if (!TextUtils.isEmpty(tempStr) && id == applicationinfo2.icon) {
                        drawable = colorRes.loadIcon(id, tempStr, useWrap);
                    } else {
                        drawable = colorRes.loadIcon(id, useWrap);
                    }
                }
                if (drawable == null) {
                    isThirdPart = true;
                    Resources r = packagemanagerExt.getPackageManager().getResourcesForApplication(applicationinfo2);
                    drawable = r.getDrawable(id, null);
                }
                if (drawable != null && parseSucceed && !(drawable instanceof LayerDrawable)) {
                    drawable = new BitmapDrawable(resources, OplusConvertIcon.convertIconBitmap(drawable, resources, isThirdPart));
                }
                if (drawable != null) {
                    packagemanagerExt.putCachedIconForThemeHelper(packageName, id, drawable);
                }
            }
            return drawable;
        } catch (PackageManager.NameNotFoundException ex) {
            Log.w(TAG, "getDrawable. Failure get resourcesExt for " + applicationinfo2.packageName + ": " + ex.getMessage());
            return null;
        } catch (Resources.NotFoundException e2) {
            Log.w(TAG, "getDrawable. Failure retrieving icon 0x" + Integer.toHexString(id) + " in package " + packageName, e2);
            return null;
        }
    }

    public Drawable getDrawableByConvert(IResourcesExt colorRes, Resources res, Drawable drawable) {
        if (res == null || drawable == null) {
            return drawable;
        }
        boolean parseSucceed = true;
        OplusExtraConfiguration exConfig = getExtraConfig(colorRes, null);
        int userId = exConfig == null ? 0 : exConfig.mUserId;
        if (needUpdateTheme(exConfig, userId)) {
            OplusConvertIcon.initConvertIconForUser(res, userId);
            parseSucceed = OplusAppIconInfo.parseIconXmlForUser(userId);
        } else {
            if (!OplusConvertIcon.hasInit()) {
                OplusConvertIcon.initConvertIconForUser(res, userId);
            }
            if (OplusAppIconInfo.getAppsNumbers() <= 0) {
                parseSucceed = OplusAppIconInfo.parseIconXmlForUser(userId);
            }
        }
        if (!parseSucceed || (drawable instanceof LayerDrawable)) {
            return drawable;
        }
        Drawable dr = new BitmapDrawable(res, OplusConvertIcon.convertIconBitmap(drawable, res, true));
        return dr;
    }

    public boolean isCustomizedIcon(IntentFilter intentfilter) {
        return false;
    }

    private boolean needUpdateTheme(OplusExtraConfiguration configuration, int userId) {
        boolean z = false;
        if (configuration == null) {
            return false;
        }
        File icons = null;
        if ((configuration.mThemeChangedFlags & 256) != 0) {
            if (TextUtils.isEmpty(configuration.mThemePrefix)) {
                icons = new File(OplusThemeUtil.CUSTOM_THEME_PATH, "icons");
            } else {
                icons = new File(configuration.mThemePrefix, "icons");
            }
        } else if ((configuration.mThemeChangedFlags & 1) != 0) {
            if (userId > 0) {
                icons = new File("/data/theme/" + userId, "icons");
            } else {
                icons = new File("/data/theme/", "icons");
            }
        }
        if (icons != null && icons.exists() && icons.lastModified() != this.mLastModified) {
            this.mLastModified = icons.lastModified();
            return true;
        }
        long flag = configuration.mThemeChangedFlags;
        if ((1 & flag) == 0) {
            z = true;
        }
        return z ^ OplusThirdPartUtil.mIsDefaultTheme;
    }

    private OplusExtraConfiguration getExtraConfig(IResourcesExt colorRes, String packageName) {
        if ("system".equals(packageName)) {
            Configuration configuration = colorRes.getConfiguration();
            OplusExtraConfiguration exConfig = configuration != null ? configuration.getOplusExtraConfiguration() : null;
            return exConfig;
        }
        Configuration systemConfiguration = colorRes.getSystemConfiguration();
        OplusExtraConfiguration exConfig2 = systemConfiguration != null ? systemConfiguration.getOplusExtraConfiguration() : null;
        return exConfig2;
    }
}
