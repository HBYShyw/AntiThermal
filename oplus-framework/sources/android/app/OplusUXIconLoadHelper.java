package android.app;

import android.app.OplusUxIconConstants;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.IResourcesExt;
import android.content.res.IUxIconPackageManagerExt;
import android.content.res.OplusBaseConfiguration;
import android.content.res.Resources;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.OplusWifiManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class OplusUXIconLoadHelper {
    private static final String TAG = "OplusUXIconLoader";
    private static int sSupportUxOnline = -1;
    private static final OplusUXIconLoader sIconLoader = OplusUXIconLoader.getLoader();

    public static synchronized Drawable getDrawable(IUxIconPackageManagerExt packageManagerExt, String packageName, String activityName, int id, ApplicationInfo app, boolean loadByResolver) {
        synchronized (OplusUXIconLoadHelper.class) {
            Drawable drawable = packageManagerExt.getCachedIconForThemeHelper(packageName, id);
            if ((drawable instanceof AdaptiveIconDrawable) && ((AdaptiveIconDrawable) drawable).getWrapper().getAdaptiveIconDrawableExt() != null && ((AdaptiveIconDrawable) drawable).getWrapper().getAdaptiveIconDrawableExt().hookGetIconMask() != null) {
                sIconLoader.setDarkFilterToDrawable(drawable);
                return drawable;
            }
            Trace.traceBegin(OplusWifiManager.OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE, "#UxIcon.getDrawable");
            if (!loadByResolver) {
                sIconLoader.mOplusUxIconDrawableManager = IOplusResolverUxIconDrawableManager.DEFAULT;
            }
            Drawable drawable2 = sIconLoader.loadUxIcon(packageManagerExt, packageName, activityName, id, app, loadByResolver);
            Trace.traceEnd(OplusWifiManager.OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE);
            if (drawable2 == null) {
                return null;
            }
            packageManagerExt.putCachedIconForThemeHelper(packageName, id, drawable2);
            return drawable2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0033 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0034 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00c5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean supportUxIcon(IUxIconPackageManagerExt pmExt, ApplicationInfo ai, String packageName) {
        ApplicationInfo ai2;
        IResourcesExt oplusRes;
        OplusBaseConfiguration configuration;
        if ((ai == null && TextUtils.isEmpty(packageName)) || pmExt == null) {
            return false;
        }
        boolean supportUxIcon = false;
        if (ai == null) {
            try {
            } catch (PackageManager.NameNotFoundException e) {
                e = e;
            }
            if (pmExt.getPackageManager() != null) {
                try {
                    ai2 = pmExt.getPackageManager().getApplicationInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e2) {
                    e = e2;
                    if (OplusUxIconConstants.DEBUG_UX_ICON) {
                        Log.e(TAG, "supportUxIcon NameNotFoundException =:" + e.toString());
                    }
                    return supportUxIcon;
                }
                if (ai2 != null) {
                    return false;
                }
                try {
                    oplusRes = pmExt.getOplusBaseResourcesForThemeHelper(ai2);
                } catch (PackageManager.NameNotFoundException e3) {
                    e = e3;
                    if (OplusUxIconConstants.DEBUG_UX_ICON) {
                    }
                    return supportUxIcon;
                }
                if (oplusRes == null) {
                    return false;
                }
                OplusExtraConfiguration extraConfig = null;
                if ("system".equals(ai2.packageName)) {
                    configuration = oplusRes.getConfiguration();
                } else {
                    configuration = oplusRes.getSystemConfiguration();
                }
                if (configuration != null) {
                    extraConfig = configuration.getOplusExtraConfiguration();
                }
                int userId = extraConfig == null ? 0 : extraConfig.mUserId;
                String key = OplusUxIconConstants.SystemProperty.KEY_THEME_FLAG;
                if (userId > 0) {
                    key = OplusUxIconConstants.SystemProperty.KEY_THEME_FLAG + "." + userId;
                }
                Long themeFlag = Long.valueOf(extraConfig == null ? SystemProperties.getLong(key, 0L) : extraConfig.mThemeChangedFlags);
                supportUxIcon = (themeFlag.longValue() & 16) == 0;
                if (OplusUxIconConstants.DEBUG_UX_ICON) {
                    Log.v(TAG, "supportUxIcon themeFlag =:" + themeFlag + "; supportUxIcon = " + supportUxIcon);
                }
                return supportUxIcon;
            }
        }
        ai2 = ai;
        if (ai2 != null) {
        }
    }

    public static synchronized Drawable getUxIconDrawable(Resources res, IResourcesExt oplusRes, Drawable src, boolean isForegroundDrawable) {
        Drawable uxIconDrawable;
        synchronized (OplusUXIconLoadHelper.class) {
            uxIconDrawable = sIconLoader.getUxIconDrawable(res, oplusRes, src, isForegroundDrawable);
        }
        return uxIconDrawable;
    }

    public static void updateExtraConfig(int changes) {
        if ((8388608 & changes) != 0) {
            sIconLoader.updateExtraConfig();
        } else if ((changes & 512) != 0) {
            sIconLoader.updateDarkModeConfig();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x00da, code lost:
    
        android.app.OplusUXIconLoadHelper.sSupportUxOnline = 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static synchronized boolean supportUxOnline(PackageManager packageManager, String sourcePackageName) {
        boolean z;
        synchronized (OplusUXIconLoadHelper.class) {
            if (sSupportUxOnline == -1) {
                if (TextUtils.isEmpty(sourcePackageName)) {
                    sSupportUxOnline = 0;
                }
                if (OplusUxIconConstants.DEBUG_UX_ICON) {
                    Log.i(TAG, "supportUxOnline sourcePackageName:" + sourcePackageName);
                }
                if (sSupportUxOnline == -1 && OplusUxIconAppCheckUtils.isSystemApp(sourcePackageName)) {
                    sSupportUxOnline = 1;
                }
                if (sSupportUxOnline == -1) {
                    ApplicationInfo appInfo = null;
                    try {
                        appInfo = packageManager.getApplicationInfo(sourcePackageName, 128);
                    } catch (PackageManager.NameNotFoundException ex) {
                        Log.e(TAG, "supportUxOnline ex :" + ex.getMessage());
                    }
                    if (appInfo != null && appInfo.metaData != null) {
                        try {
                            Boolean supportUxOnline = (Boolean) appInfo.metaData.get(OplusUxIconConstants.IconTheme.OPLUS_UXIOCN_META_DATA);
                            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                                Log.i(TAG, "supportUxOnline :" + supportUxOnline);
                            }
                            if (supportUxOnline != null) {
                                sSupportUxOnline = supportUxOnline.booleanValue() ? 1 : 0;
                            }
                        } catch (Exception ex2) {
                            Log.e(TAG, "supportUxOnline error:" + ex2.getMessage());
                        }
                    }
                }
                if (sSupportUxOnline == -1) {
                    try {
                        String[] prefixs = packageManager.getResourcesForApplication(sourcePackageName).getStringArray(201785390);
                        int length = prefixs.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            }
                            String prefix = prefixs[i];
                            if (sourcePackageName.startsWith(prefix)) {
                                break;
                            }
                            i++;
                        }
                    } catch (Exception e) {
                    }
                    if (sSupportUxOnline == -1) {
                        sSupportUxOnline = 0;
                    }
                }
            }
            z = sSupportUxOnline == 1;
        }
        return z;
    }

    public static synchronized Drawable loadOverlayResolverDrawable(IUxIconPackageManagerExt packageManagerExt, String packageName, int resourceId, ApplicationInfo app, String resolverIconName) {
        Drawable drawable;
        synchronized (OplusUXIconLoadHelper.class) {
            sIconLoader.mOplusUxIconDrawableManager = new OplusResolverUxIconDrawableImpl(resolverIconName);
            drawable = getDrawable(packageManagerExt, packageName, null, resourceId, app, true);
        }
        return drawable;
    }

    public static synchronized void setDarkFilterToDrawable(Drawable drawable) {
        synchronized (OplusUXIconLoadHelper.class) {
            sIconLoader.setDarkFilterToDrawable(drawable);
        }
    }
}
