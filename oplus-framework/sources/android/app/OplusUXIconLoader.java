package android.app;

import android.app.OplusUxIconConstants;
import android.app.uxicons.CustomAdaptiveIconConfig;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.IResourcesExt;
import android.content.res.IUxIconPackageManagerExt;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.OplusPalette;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.IAdaptiveIconDrawableExt;
import android.graphics.drawable.LayerDrawable;
import android.net.wifi.OplusWifiManager;
import android.os.Trace;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.util.OplusRoundRectUtil;
import com.oplus.util.UxScreenUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class OplusUXIconLoader {
    private static final String TAG = "OplusUXIconLoader";
    private static String mIconPackName;
    private static boolean mNeedUpdateIconMap;
    private static long sVersionCode;
    private String[] mCommonStylePathArray;
    private String[] mCommonStylePrefixArray;
    private LightingColorFilter mDarkModeColorFilter;
    private OplusIconPackUtil mIconPackUtil;
    private volatile Boolean mIsExpVersion;
    private String[] mSpecialStylePathArray;
    private String[] mSpecialStylePrefixArray;
    private static volatile OplusUXIconLoader sInstance = null;
    private static OplusIconConfig sIconConfig = new OplusIconConfig();
    IOplusResolverUxIconDrawableManager mOplusUxIconDrawableManager = IOplusResolverUxIconDrawableManager.DEFAULT;
    private boolean mHasInitConfigArray = false;
    private ArrayList<Integer> mCommonStyleConfigArray = new ArrayList<>();
    private ArrayList<Integer> mSpecialStyleConfigArray = new ArrayList<>();
    private float mDarkModeBrightness = 0.84f;
    private HashMap<String, String> mAppIconNameMap = new HashMap<>();
    private HashMap<String, String> mComponentNameMap = new HashMap<>();
    private HashMap<String, Integer> mAppIconResMap = new HashMap<>();

    public static OplusUXIconLoader getLoader() {
        if (sInstance == null) {
            synchronized (OplusUXIconLoader.class) {
                if (sInstance == null) {
                    sInstance = new OplusUXIconLoader();
                }
            }
        }
        return sInstance;
    }

    private OplusUXIconLoader() {
        int color = getDarkModeColorInCurrentContrast(this.mDarkModeBrightness);
        this.mDarkModeColorFilter = new LightingColorFilter(color, 0);
    }

    public void updateExtraConfig() {
        if (!sIconConfig.isEmpty()) {
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.v(TAG, "sIconConfig.addUpdateConfig(OplusIconConfig.UPDATE_ICON_CONFIG)");
            }
            sIconConfig.addUpdateConfig(1);
        }
    }

    public void updateDarkModeConfig() {
        if (!sIconConfig.isEmpty()) {
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.v(TAG, "sIconConfig.addUpdateConfig(OplusIconConfig.UPDATE_DARKMODE_CONFIG)");
            }
            sIconConfig.addUpdateConfig(2);
        }
    }

    private void setDarkFilterToDrawable(Drawable drawable, boolean needColorFilter) {
        if (drawable == null) {
            return;
        }
        if (!needColorFilter) {
            ColorFilter colorFilter = drawable.getColorFilter();
            if (colorFilter == null) {
                drawable.setColorFilter(null);
                return;
            } else {
                if ((colorFilter instanceof LightingColorFilter) && ((LightingColorFilter) colorFilter).getColorMultiply() == this.mDarkModeColorFilter.getColorMultiply()) {
                    drawable.setColorFilter(null);
                    return;
                }
                return;
            }
        }
        if (drawable instanceof AdaptiveIconDrawable) {
            setDarkFilterToDrawable(((AdaptiveIconDrawable) drawable).getForeground());
            setDarkFilterToDrawable(((AdaptiveIconDrawable) drawable).getBackground());
            return;
        }
        if (drawable instanceof LayerDrawable) {
            int count = ((LayerDrawable) drawable).getNumberOfLayers();
            for (int i = 0; i < count; i++) {
                Drawable childDrawable = ((LayerDrawable) drawable).getDrawable(i);
                setDarkFilterToDrawable(childDrawable);
            }
            return;
        }
        if (drawable instanceof DrawableWrapper) {
            setDarkFilterToDrawable(((DrawableWrapper) drawable).getDrawable());
            return;
        }
        if (drawable instanceof DrawableContainer) {
            setDarkFilterToDrawable(drawable.getCurrent());
            return;
        }
        Drawable.ConstantState constantState = drawable.getConstantState();
        try {
            Class constantClass = constantState.getClass();
            Field field = constantClass.getDeclaredField("mTint");
            field.setAccessible(true);
            if (field.get(constantState) != null) {
                if (OplusUxIconConstants.DEBUG_UX_ICON) {
                    Log.d(TAG, "setDarkFilterToDrawable mTint not null");
                    return;
                }
                return;
            }
        } catch (Exception e) {
        }
        drawable.setColorFilter(this.mDarkModeColorFilter);
    }

    public void setDarkFilterToDrawable(Drawable drawable) {
        setDarkFilterToDrawable(drawable, sIconConfig.isDarkMode() && sIconConfig.isEnableDarkModeIcon());
    }

    public Drawable getUxIconDrawable(Resources res, IResourcesExt oplusRes, Drawable src, boolean isForegroundDrawable) {
        if (sIconConfig.isEmpty() || sIconConfig.isNeedUpdate()) {
            if (oplusRes == null) {
                return src;
            }
            Configuration systemConfiguration = oplusRes.getSystemConfiguration();
            if (systemConfiguration == null) {
                return src;
            }
            OplusExtraConfiguration oplusExtraConfiguration = systemConfiguration.getOplusExtraConfiguration();
            if (oplusExtraConfiguration != null) {
                checkConfig(res, oplusExtraConfiguration);
                if (sIconConfig.isEmpty()) {
                    return src;
                }
            } else {
                return src;
            }
        }
        if (OplusUxIconConstants.DEBUG_UX_ICON) {
            Log.v(TAG, "getUxIconDrawable sIconConfig = " + sIconConfig);
        }
        int pos = this.mCommonStyleConfigArray.indexOf(Integer.valueOf(sIconConfig.getTheme()));
        if (pos == 1) {
            int iconMaxSize = res.getDimensionPixelSize(201654414);
            if (UxScreenUtil.isFoldDisplay() && UxScreenUtil.isWhiteSwan()) {
                iconMaxSize = res.getDimensionPixelSize(201654492);
            } else if (UxScreenUtil.isFoldDisplay() && !UxScreenUtil.isDragonFly()) {
                iconMaxSize = res.getDimensionPixelSize(201654491);
            } else if (UxScreenUtil.isTabletDevices()) {
                iconMaxSize = res.getDimensionPixelSize(201654492);
            }
            if (iconMaxSize <= 0) {
                return null;
            }
            float scale = (OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getIconSize()) * 1.0f) / iconMaxSize;
            return buildAdaptiveIconDrawableForThirdParty(res, null, src, src instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) src).getMonochrome() : null, OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()), (int) (OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()) * scale));
        }
        if (isForegroundDrawable) {
            Drawable bgDrawable = getBackgroundDrawable(src);
            return buildAdaptiveIconDrawable(res, src, bgDrawable, src instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) src).getMonochrome() : null, false, false);
        }
        return buildAdaptiveIconDrawable(res, null, src, src instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) src).getMonochrome() : null, false, false);
    }

    public Drawable loadUxIcon(IUxIconPackageManagerExt packageManagerExt, String packageName, String activityName, int id, ApplicationInfo applicationInfo, boolean loadByResolver) {
        ApplicationInfo applicationInfo2;
        Resources res;
        OplusExtraConfiguration extraConfig;
        String iconNamePrefix;
        boolean loadApkIcon;
        Drawable resolveDrawable;
        Resources res2;
        Drawable uxicon;
        if (OplusUxIconConstants.DEBUG_UX_ICON) {
            Log.v(TAG, "loadIcon packageName = " + packageName + ",applicationInfo =:" + applicationInfo + "; loadByResolver = " + loadByResolver);
        }
        if (applicationInfo != null) {
            applicationInfo2 = applicationInfo;
        } else {
            try {
                applicationInfo2 = packageManagerExt.getPackageManager().getApplicationInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }
        if (applicationInfo2 == null || TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            IResourcesExt oplusRes = packageManagerExt.getOplusBaseResourcesForThemeHelper(applicationInfo2);
            if (oplusRes == null || (res = oplusRes.getResources()) == null) {
                return null;
            }
            if (sIconConfig.isEmpty() || sIconConfig.isNeedUpdate()) {
                if ("system".equals(packageName) && oplusRes.getConfiguration() != null) {
                    extraConfig = oplusRes.getConfiguration().getOplusExtraConfiguration();
                } else {
                    if (oplusRes.getSystemConfiguration() == null) {
                        return null;
                    }
                    extraConfig = oplusRes.getSystemConfiguration().getOplusExtraConfiguration();
                }
                checkConfig(res, extraConfig);
                if (sIconConfig.isEmpty()) {
                    return null;
                }
            }
            if (id == applicationInfo2.icon) {
                iconNamePrefix = "";
                loadApkIcon = false;
            } else if (!OplusUxIconConstants.IconLoader.COM_ANDROID_CONTACTS.equals(packageName) || sIconConfig.getTheme() == 5) {
                iconNamePrefix = "";
                loadApkIcon = true;
            } else {
                iconNamePrefix = OplusUxIconConstants.IconLoader.DIALER_PREFIX;
                loadApkIcon = false;
            }
            Drawable originDrawable = packageManagerExt.getPackageManager().getDrawable(packageName, id, applicationInfo2);
            Drawable resolveDrawable2 = this.mOplusUxIconDrawableManager.getDrawable(packageManagerExt.getPackageManager(), packageName, applicationInfo2, originDrawable);
            if (sIconConfig.getTheme() == 5) {
                Drawable packIcon = getDrawableForIconPackTheme(packageManagerExt.getPackageManager(), applicationInfo2, packageName, activityName, iconNamePrefix, res, resolveDrawable2);
                if (packIcon != null) {
                    return packIcon;
                }
                resolveDrawable = resolveDrawable2;
                res2 = res;
            } else {
                if (id == 0) {
                    return null;
                }
                if (loadApkIcon) {
                    resolveDrawable = resolveDrawable2;
                    res2 = res;
                } else {
                    resolveDrawable = resolveDrawable2;
                    if (originDrawable != resolveDrawable) {
                        res2 = res;
                    } else {
                        res2 = res;
                        boolean isSystemApp = OplusUxIconAppCheckUtils.isPresetApp(res2, packageName);
                        if ((OplusUxDrawableMappingHelper.containsPackageName(packageName) || isSystemApp || !(originDrawable instanceof AdaptiveIconDrawable) || sIconConfig.getTheme() == 1) && (uxicon = getDrawableForUXIconTheme(packageName, res2, packageManagerExt.getPackageManager(), iconNamePrefix, resolveDrawable)) != null) {
                            return uxicon;
                        }
                    }
                }
            }
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.v(TAG, "loadIcon packageName = " + packageName + ",sIconConfig =:" + sIconConfig);
            }
            return getDrawableForAppIcon(packageName, id, applicationInfo2, res2, packageManagerExt.getPackageManager(), resolveDrawable);
        } catch (PackageManager.NameNotFoundException e2) {
            return null;
        }
    }

    private Drawable getDrawableForIconPackTheme(PackageManager packageManager, ApplicationInfo applicationInfo, String packageName, String activityName, String iconNamePrefix, Resources res, Drawable originDrawable) {
        String className;
        boolean offUxIcon;
        Drawable drawable;
        Resources resources;
        Drawable drawable2;
        Resources resources2;
        Drawable background;
        Drawable background2;
        Drawable drawable3;
        Drawable background3;
        String str = mIconPackName;
        if (str == null || str.equals("")) {
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.d(TAG, "mIconPackName is null");
            }
            return null;
        }
        if (OplusUxIconConstants.DEBUG_UX_ICON) {
            Log.d(TAG, "mIconPackName is not null, try to load from icon pack firstly");
        }
        if (activityName != null) {
            className = activityName;
        } else {
            className = "";
        }
        String className2 = mIconPackName;
        Drawable drawable4 = loadDrawableFromIconPack(className2, className, packageManager, applicationInfo);
        if (drawable4 != null) {
            setDarkFilterToDrawable(drawable4);
            return buildAdaptiveIconDrawableForIconPack(res, null, drawable4, null, OplusRoundRectUtil.getInstance().getPath(new Rect(0, 0, 150, 150), 0.0f), true, false);
        }
        if (originDrawable == null) {
            return null;
        }
        boolean isSystemApp = OplusUxIconAppCheckUtils.isPresetApp(res, packageName);
        if (!isSystemApp) {
            offUxIcon = false;
        } else {
            boolean offUxIcon2 = isOffUxIcon(packageManager, packageName);
            offUxIcon = offUxIcon2;
        }
        Drawable foreground = findAppDrawable(packageName, iconNamePrefix + getRectFgPrefix(), res, false, isSystemApp, offUxIcon);
        Drawable background4 = findAppDrawable(packageName, iconNamePrefix + getRectBgPrefix(), res, false, isSystemApp, offUxIcon);
        ComponentName componentName = new ComponentName(packageName, className);
        if (foreground != null) {
            OplusIconPackUtil oplusIconPackUtil = this.mIconPackUtil;
            if (oplusIconPackUtil == null) {
                background3 = background4;
            } else {
                if (oplusIconPackUtil.hasGenerateIconPack()) {
                    return generateIconPackDrawable(res, componentName, new LayerDrawable(new Drawable[]{background4, foreground}), originDrawable instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) originDrawable).getMonochrome() : null, true, false);
                }
                background3 = background4;
            }
            return buildAdaptiveIconDrawable(res, foreground, background3, originDrawable instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) originDrawable).getMonochrome() : null, true, false);
        }
        int sizeThreshold = (int) ((res.getDisplayMetrics().density * 37.0f) + 0.5f);
        if (originDrawable instanceof AdaptiveIconDrawable) {
            Drawable foreground2 = ((AdaptiveIconDrawable) originDrawable).getForeground();
            Drawable background5 = ((AdaptiveIconDrawable) originDrawable).getBackground();
            OplusIconPackUtil oplusIconPackUtil2 = this.mIconPackUtil;
            if (oplusIconPackUtil2 != null && oplusIconPackUtil2.hasGenerateIconPack()) {
                return generateIconPackDrawable(res, componentName, originDrawable, originDrawable instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) originDrawable).getMonochrome() : null, false, true);
            }
            return buildAdaptiveIconDrawable(res, foreground2, background5, originDrawable instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) originDrawable).getMonochrome() : null, false, true);
        }
        if (originDrawable.getIntrinsicWidth() >= sizeThreshold) {
            if (hasTransparentPixels(originDrawable)) {
                drawable = originDrawable;
                resources = res;
            } else {
                OplusIconPackUtil oplusIconPackUtil3 = this.mIconPackUtil;
                if (oplusIconPackUtil3 == null) {
                    drawable3 = originDrawable;
                } else if (!oplusIconPackUtil3.hasGenerateIconPack()) {
                    drawable3 = originDrawable;
                } else {
                    return generateIconPackDrawable(res, componentName, originDrawable, originDrawable instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) originDrawable).getMonochrome() : null, false, false);
                }
                return buildAdaptiveIconDrawable(res, foreground, originDrawable, drawable3 instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) drawable3).getMonochrome() : null, true, false);
            }
        } else {
            drawable = originDrawable;
            resources = res;
        }
        Drawable foreground3 = drawable;
        Drawable background6 = getDefaultBackgroundDrawable();
        OplusIconPackUtil oplusIconPackUtil4 = this.mIconPackUtil;
        if (oplusIconPackUtil4 == null || !oplusIconPackUtil4.hasGenerateIconPack()) {
            drawable2 = drawable;
            resources2 = resources;
            background = background6;
            background2 = foreground3;
        } else {
            drawable2 = drawable;
            resources2 = resources;
            background = this.mIconPackUtil.generateIconPackDrawable(componentName, buildAdaptiveIconDrawableForThirdParty(res, foreground3, background6, drawable instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) drawable).getMonochrome() : null, (int) ((res.getDisplayMetrics().density * 38.0f) + 0.5f), new CustomAdaptiveIconConfig.Builder(resources).create().getDefaultIconSize()));
            background2 = null;
        }
        return buildAdaptiveIconDrawableForThirdParty(res, background2, background, drawable2 instanceof AdaptiveIconDrawable ? ((AdaptiveIconDrawable) drawable2).getMonochrome() : null, (int) ((res.getDisplayMetrics().density * 38.0f) + 0.5f), OplusUxIconConfigParser.getPxFromIconConfigDp(resources2, sIconConfig.getIconSize()));
    }

    private Drawable generateIconPackDrawable(Resources res, ComponentName componentName, Drawable drawable, Drawable monoChrome, boolean isPlatformDrawable, boolean isAdaptiveIconDrawable) {
        Drawable result = drawable;
        OplusIconPackUtil oplusIconPackUtil = this.mIconPackUtil;
        if (oplusIconPackUtil != null) {
            result = oplusIconPackUtil.generateIconPackDrawable(componentName, drawable);
        }
        return buildAdaptiveIconDrawableForIconPack(res, null, result, monoChrome, OplusRoundRectUtil.getInstance().getPath(new Rect(0, 0, 150, 150), 0.0f), true, false);
    }

    private Drawable getDrawableForUXIconTheme(String packageName, Resources res, PackageManager packageManager, String iconNamePrefix, Drawable originDrawable) {
        boolean offUxIcon;
        Drawable drawable;
        int iconMaxSize;
        Drawable drawable2;
        Drawable drawable3;
        Drawable drawable4;
        int pos = this.mCommonStyleConfigArray.indexOf(Integer.valueOf(sIconConfig.getTheme()));
        int customThemeConfigPos = this.mCommonStyleConfigArray.size() - 1;
        Drawable foreground = null;
        Drawable background = null;
        boolean isSystemApp = OplusUxIconAppCheckUtils.isPresetApp(res, packageName);
        if (isSystemApp) {
            boolean offUxIcon2 = isOffUxIcon(packageManager, packageName);
            offUxIcon = offUxIcon2;
        } else {
            offUxIcon = false;
        }
        if (pos >= 0 && pos < this.mCommonStyleConfigArray.size()) {
            if (pos == 2) {
                Drawable foreground2 = findAppDrawable(packageName, iconNamePrefix + getCommonStylePrefixExceptRect(pos), res, false, isSystemApp, offUxIcon);
                if (foreground2 != null) {
                    if (!(originDrawable instanceof AdaptiveIconDrawable)) {
                        drawable4 = null;
                    } else {
                        drawable4 = ((AdaptiveIconDrawable) originDrawable).getMonochrome();
                    }
                    return buildAdaptiveIconDrawable(res, null, foreground2, drawable4, true, false);
                }
                boolean z = offUxIcon;
                foreground = findAppDrawable(packageName, iconNamePrefix + getRectFgPrefix(), res, false, isSystemApp, z);
                background = findAppDrawable(packageName, iconNamePrefix + getRectBgPrefix(), res, false, isSystemApp, z);
            } else if (pos == 0 || pos == customThemeConfigPos) {
                boolean z2 = offUxIcon;
                foreground = findAppDrawable(packageName, iconNamePrefix + getRectFgPrefix(), res, false, isSystemApp, z2);
                background = findAppDrawable(packageName, iconNamePrefix + getRectBgPrefix(), res, false, isSystemApp, z2);
            } else if (pos == 1) {
                Drawable foreground3 = findAppDrawable(packageName, iconNamePrefix + getCommonStylePrefixExceptRect(pos), res, false, isSystemApp, offUxIcon);
                if (foreground3 != null) {
                    setDarkFilterToDrawable(foreground3);
                    if (!(originDrawable instanceof AdaptiveIconDrawable)) {
                        drawable3 = null;
                    } else {
                        drawable3 = ((AdaptiveIconDrawable) originDrawable).getMonochrome();
                    }
                    return buildAdaptiveIconDrawable(res, null, foreground3, drawable3, true, false);
                }
                boolean z3 = offUxIcon;
                Drawable foreground4 = findAppDrawable(packageName, iconNamePrefix + getRectFgPrefix(), res, false, isSystemApp, z3);
                Drawable background2 = findAppDrawable(packageName, iconNamePrefix + getRectBgPrefix(), res, false, isSystemApp, z3);
                int iconMaxSize2 = res.getDimensionPixelSize(201654414);
                if (UxScreenUtil.isFoldDisplay() && UxScreenUtil.isWhiteSwan()) {
                    int iconMaxSize3 = res.getDimensionPixelSize(201654492);
                    iconMaxSize = iconMaxSize3;
                } else if (UxScreenUtil.isFoldDisplay() && !UxScreenUtil.isDragonFly()) {
                    int iconMaxSize4 = res.getDimensionPixelSize(201654491);
                    iconMaxSize = iconMaxSize4;
                } else if (!UxScreenUtil.isTabletDevices()) {
                    iconMaxSize = iconMaxSize2;
                } else {
                    int iconMaxSize5 = res.getDimensionPixelSize(201654492);
                    iconMaxSize = iconMaxSize5;
                }
                if (iconMaxSize <= 0) {
                    return null;
                }
                float scale = (OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getIconSize()) * 1.0f) / iconMaxSize;
                if (!(originDrawable instanceof AdaptiveIconDrawable)) {
                    drawable2 = null;
                } else {
                    drawable2 = ((AdaptiveIconDrawable) originDrawable).getMonochrome();
                }
                return buildAdaptiveIconDrawableForThirdParty(res, foreground4, background2, drawable2, OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()), (int) (OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()) * scale));
            }
        } else {
            int specialPos = this.mSpecialStyleConfigArray.indexOf(Integer.valueOf(sIconConfig.getTheme()));
            if (specialPos >= 0 && specialPos < this.mSpecialStylePrefixArray.length) {
                if (isSystemApp) {
                    foreground = findAppDrawable(packageName, iconNamePrefix + getSpecialStylePrefix(specialPos), res, true, true, offUxIcon);
                    if (foreground != null) {
                        setDarkFilterToDrawable(foreground);
                        return foreground;
                    }
                } else {
                    boolean z4 = offUxIcon;
                    foreground = findAppDrawable(packageName, iconNamePrefix + getRectFgPrefix(), res, true, false, z4);
                    background = findAppDrawable(packageName, iconNamePrefix + getRectBgPrefix(), res, true, false, z4);
                }
            }
        }
        if (foreground == null) {
            return null;
        }
        if (!(originDrawable instanceof AdaptiveIconDrawable)) {
            drawable = null;
        } else {
            drawable = ((AdaptiveIconDrawable) originDrawable).getMonochrome();
        }
        return buildAdaptiveIconDrawable(res, foreground, background, drawable, true, false);
    }

    private boolean isLauncherIcon(String pkgName, PackageManager packageManager, int id) {
        Intent intent = new Intent();
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setAction("android.intent.action.MAIN");
        intent.setPackage(pkgName);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 787456);
        for (ResolveInfo info : resolveInfoList) {
            if (info.getIconResource() == id) {
                if (OplusUxIconConstants.DEBUG_UX_ICON) {
                    Log.d(TAG, "isLauncherIcon true: pkgName=" + pkgName);
                    return true;
                }
                return true;
            }
        }
        if (OplusUxIconConstants.DEBUG_UX_ICON) {
            Log.d(TAG, "isLauncherIcon false: pkgName=" + pkgName);
            return false;
        }
        return false;
    }

    private Drawable getDrawableForAppIcon(String packageName, int id, ApplicationInfo applicationInfo, Resources res, PackageManager packageManager, Drawable drawable) {
        Drawable foreground;
        Drawable foreground2;
        int iconMaxSize;
        int iconMaxSize2;
        int pos = this.mCommonStyleConfigArray.indexOf(Integer.valueOf(sIconConfig.getTheme()));
        if (packageName.contains("android.app.stubs")) {
            return drawable;
        }
        Drawable drawable2 = null;
        if (drawable == null) {
            return null;
        }
        if (OplusUxIconAppCheckUtils.isDeskActivity(res, packageName)) {
            if (pos == 1) {
                if (drawable instanceof AdaptiveIconDrawable) {
                    drawable2 = ((AdaptiveIconDrawable) drawable).getMonochrome();
                }
                return buildAdaptiveIconDrawable(res, drawable, null, drawable2, false, false);
            }
            if (drawable instanceof AdaptiveIconDrawable) {
                drawable2 = ((AdaptiveIconDrawable) drawable).getMonochrome();
            }
            return buildAdaptiveIconDrawable(res, null, drawable, drawable2, false, false);
        }
        int sizeThreshold = (int) ((res.getDisplayMetrics().density * 37.0f) + 0.5f);
        if (pos == 1) {
            int iconMaxSize3 = res.getDimensionPixelSize(201654414);
            if (UxScreenUtil.isFoldDisplay() && UxScreenUtil.isWhiteSwan()) {
                int iconMaxSize4 = res.getDimensionPixelSize(201654492);
                iconMaxSize2 = iconMaxSize4;
            } else if (UxScreenUtil.isFoldDisplay() && !UxScreenUtil.isDragonFly()) {
                int iconMaxSize5 = res.getDimensionPixelSize(201654491);
                iconMaxSize2 = iconMaxSize5;
            } else if (!UxScreenUtil.isTabletDevices()) {
                iconMaxSize2 = iconMaxSize3;
            } else {
                int iconMaxSize6 = res.getDimensionPixelSize(201654492);
                iconMaxSize2 = iconMaxSize6;
            }
            if (iconMaxSize2 <= 0) {
                return null;
            }
            float scale = (OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getIconSize()) * 1.0f) / iconMaxSize2;
            if ((applicationInfo.iconRes == id || isLauncherIcon(packageName, packageManager, id)) && hasTransparentPixels(drawable)) {
                Drawable background = getDefaultBackgroundDrawable();
                if (drawable instanceof AdaptiveIconDrawable) {
                    drawable2 = ((AdaptiveIconDrawable) drawable).getMonochrome();
                }
                return buildAdaptiveIconDrawableForThirdParty(res, drawable, background, drawable2, (int) (((res.getDisplayMetrics().density * 38.0f) + 0.5f) * 1.25f), (int) (OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()) * scale));
            }
            if (drawable instanceof AdaptiveIconDrawable) {
                drawable2 = ((AdaptiveIconDrawable) drawable).getMonochrome();
            }
            return buildAdaptiveIconDrawableForThirdParty(res, null, drawable, drawable2, OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()), (int) (OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()) * scale));
        }
        boolean isAdaptiveIconDrawable = false;
        if (drawable instanceof AdaptiveIconDrawable) {
            Drawable foreground3 = ((AdaptiveIconDrawable) drawable).getForeground();
            Drawable background2 = ((AdaptiveIconDrawable) drawable).getBackground();
            isAdaptiveIconDrawable = true;
            foreground = foreground3;
            foreground2 = background2;
        } else if (pos == 2 || pos == 0) {
            if ((applicationInfo.iconRes == id || isLauncherIcon(packageName, packageManager, id)) && (drawable.getIntrinsicWidth() < sizeThreshold || hasTransparentPixels(drawable))) {
                Drawable background3 = getDefaultBackgroundDrawable();
                if (drawable instanceof AdaptiveIconDrawable) {
                    drawable2 = ((AdaptiveIconDrawable) drawable).getMonochrome();
                }
                return buildAdaptiveIconDrawableForThirdParty(res, drawable, background3, drawable2, (int) ((res.getDisplayMetrics().density * 38.0f) + 0.5f), OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getIconSize()));
            }
            isAdaptiveIconDrawable = false;
            foreground = null;
            foreground2 = drawable;
        } else if ((applicationInfo.iconRes != id && !isLauncherIcon(packageName, packageManager, id)) || (drawable.getIntrinsicWidth() >= sizeThreshold && !hasTransparentPixels(drawable))) {
            foreground = null;
            foreground2 = drawable;
        } else {
            Drawable background4 = getDefaultBackgroundDrawable();
            int iconMaxSize7 = res.getDimensionPixelSize(201654414);
            if (UxScreenUtil.isFoldDisplay() && UxScreenUtil.isWhiteSwan()) {
                int iconMaxSize8 = res.getDimensionPixelSize(201654492);
                iconMaxSize = iconMaxSize8;
            } else if (UxScreenUtil.isFoldDisplay() && !UxScreenUtil.isDragonFly()) {
                int iconMaxSize9 = res.getDimensionPixelSize(201654491);
                iconMaxSize = iconMaxSize9;
            } else if (!UxScreenUtil.isTabletDevices()) {
                iconMaxSize = iconMaxSize7;
            } else {
                int iconMaxSize10 = res.getDimensionPixelSize(201654492);
                iconMaxSize = iconMaxSize10;
            }
            if (iconMaxSize <= 0) {
                return null;
            }
            float scale2 = (OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()) * 1.0f) / iconMaxSize;
            if (drawable instanceof AdaptiveIconDrawable) {
                drawable2 = ((AdaptiveIconDrawable) drawable).getMonochrome();
            }
            return buildAdaptiveIconDrawableForThirdParty(res, drawable, background4, drawable2, (int) (((res.getDisplayMetrics().density * 38.0f) + 0.5f) * scale2), OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getIconSize()));
        }
        if (drawable instanceof AdaptiveIconDrawable) {
            drawable2 = ((AdaptiveIconDrawable) drawable).getMonochrome();
        }
        return buildAdaptiveIconDrawable(res, foreground, foreground2, drawable2, false, isAdaptiveIconDrawable);
    }

    private Drawable getDefaultBackgroundDrawable() {
        Drawable defaultBackgroundDrawable = new ColorDrawable(Color.parseColor(OplusUxIconConstants.IconLoader.DEFAULT_BACKGROUND_COLOR));
        setDarkFilterToDrawable(defaultBackgroundDrawable);
        return defaultBackgroundDrawable;
    }

    private Drawable loadDrawableFromIconPack(String iconPackageName, String activityName, PackageManager pm, ApplicationInfo applicationInfo) {
        try {
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.d(TAG, "loadDrawableFromIconPack from " + iconPackageName);
            }
            Resources iconPackResources = pm.getResourcesForApplication(iconPackageName);
            OplusIconPackMappingHelper.parsePackMapping();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(iconPackageName, 787456);
                if (packageInfo != null) {
                    long newVersionCode = packageInfo.getLongVersionCode();
                    if (newVersionCode != sVersionCode) {
                        sVersionCode = newVersionCode;
                        mNeedUpdateIconMap = true;
                    }
                }
                if (mNeedUpdateIconMap || this.mIconPackUtil == null) {
                    OplusIconPackUtil oplusIconPackUtil = new OplusIconPackUtil(iconPackageName, iconPackResources);
                    this.mIconPackUtil = oplusIconPackUtil;
                    oplusIconPackUtil.getIconResMapFromXml();
                    mNeedUpdateIconMap = false;
                }
                if (iconPackageName != null && activityName != null) {
                    return this.mIconPackUtil.getDrawableIconForPackage(new ComponentName(applicationInfo.packageName, activityName));
                }
                return null;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "loadDrawableFromIconPack exception: " + e.getMessage());
                return null;
            }
        } catch (Exception e2) {
            this.mIconPackUtil = null;
            Log.e(TAG, e2.getMessage(), e2);
            return null;
        }
    }

    private void checkConfig(Resources res, OplusExtraConfiguration extraConfig) {
        OplusUxDrawableMappingHelper.parsePackMapping();
        if (!this.mHasInitConfigArray) {
            this.mHasInitConfigArray = true;
            initConfigArray(res);
            int color = getDarkModeColorInCurrentContrast(this.mDarkModeBrightness);
            this.mDarkModeColorFilter = new LightingColorFilter(color, 0);
            boolean isDarkMode = (res.getConfiguration().uiMode & 48) == 32;
            sIconConfig.setIsDarkMode(isDarkMode);
            OplusUxIconConfigParser.parseConfig(sIconConfig, extraConfig, res, this.mCommonStyleConfigArray, this.mSpecialStyleConfigArray, this.mCommonStylePathArray, this.mSpecialStylePathArray);
            sIconConfig.setUserId(extraConfig.mUserId);
        }
        int color2 = extraConfig.mUserId;
        if (color2 != -1 && sIconConfig.getUserId() != -1 && sIconConfig.getUserId() != extraConfig.mUserId) {
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.v(TAG, "extraConfig.mUserId = " + extraConfig.mUserId + " is not equals to sIconConfig.mUserId = " + sIconConfig.getUserId() + " ignore changes");
            }
        } else {
            if ((sIconConfig.getNeedUpdateConfig() & 1) != 0) {
                OplusUxIconConfigParser.parseConfig(sIconConfig, extraConfig, res, this.mCommonStyleConfigArray, this.mSpecialStyleConfigArray, this.mCommonStylePathArray, this.mSpecialStylePathArray);
                sIconConfig.removeUpdateConfig(1);
            }
            if ((sIconConfig.getNeedUpdateConfig() & 2) != 0) {
                boolean isDarkMode2 = (res.getConfiguration().uiMode & 48) == 32;
                sIconConfig.setIsDarkMode(isDarkMode2);
                sIconConfig.removeUpdateConfig(2);
            }
            if (!extraConfig.mIconPackName.equals(mIconPackName)) {
                if (OplusUxIconConstants.DEBUG_UX_ICON) {
                    Log.d(TAG, "checkConfig set mNeedUpdateIconMap true");
                }
                mNeedUpdateIconMap = true;
                mIconPackName = extraConfig.mIconPackName;
            }
            Log.v(TAG, "checkConfig sIconConfig =:" + sIconConfig);
        }
        OplusUxIconAppCheckUtils.resetPresetAppsList();
    }

    private boolean isExpVersion() {
        if (this.mIsExpVersion == null) {
            synchronized (OplusUXIconLoader.class) {
                if (this.mIsExpVersion == null) {
                    initIsExpVersionValues();
                    if (this.mIsExpVersion == null) {
                        return false;
                    }
                }
            }
        }
        return this.mIsExpVersion.booleanValue();
    }

    private void initIsExpVersionValues() {
        try {
            this.mIsExpVersion = Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_UXICON_EXP));
        } catch (Exception e) {
            Log.e(TAG, "RemoteException --> " + e.getMessage());
        }
        if (OplusUxIconConstants.DEBUG_UX_ICON) {
            Log.v(TAG, "mIsExpVersion = " + this.mIsExpVersion);
        }
    }

    private void initConfigArray(Resources res) {
        int[] commonThemeConfigArray = res.getIntArray(201785383);
        for (int i : commonThemeConfigArray) {
            this.mCommonStyleConfigArray.add(Integer.valueOf(i));
        }
        int[] specialThemeConfigArray = res.getIntArray(201785387);
        for (int i2 : specialThemeConfigArray) {
            this.mSpecialStyleConfigArray.add(Integer.valueOf(i2));
        }
        this.mCommonStylePrefixArray = res.getStringArray(201785382);
        this.mSpecialStylePrefixArray = res.getStringArray(201785386);
        if (isExpVersion()) {
            this.mCommonStylePathArray = res.getStringArray(201785349);
        } else {
            this.mCommonStylePathArray = res.getStringArray(201785380);
        }
        this.mSpecialStylePathArray = res.getStringArray(201785384);
    }

    private boolean isOffUxIcon(PackageManager pm, String packageName) {
        Boolean isOff;
        if (pm == null) {
            return false;
        }
        ApplicationInfo appInfo = null;
        try {
            appInfo = pm.getApplicationInfo(packageName, 128);
        } catch (PackageManager.NameNotFoundException ex) {
            Log.e(TAG, "is off uxicon error: " + ex.getMessage());
        }
        if (appInfo == null || appInfo.metaData == null || (isOff = (Boolean) appInfo.metaData.get(OplusUxIconConstants.IconTheme.OPLUS_OFF_UXICON_META_DATA)) == null || !isOff.booleanValue()) {
            return false;
        }
        return true;
    }

    private String buildIconPath(String baseFilePath, String packageName, String targetIconName) {
        StringBuilder pathBuild = new StringBuilder(baseFilePath).append(packageName).append("/").append(targetIconName).append(OplusUxIconConstants.IconLoader.PNG_REG);
        String iconPath = pathBuild.toString();
        return iconPath;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable findAppDrawable(String packageName, String iconName, Resources res, boolean isSpecialStyle, boolean isSystemApp, boolean offUxIcon) {
        String str;
        Trace.traceBegin(OplusWifiManager.OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE, "#UxIcon.getDrawable.findAppDrawable");
        Drawable drawable = null;
        res.getDisplayMetrics();
        String densityName = OplusUxIconConstants.IconLoader.getDensityName(DisplayMetrics.DENSITY_DEVICE_STABLE);
        StringBuilder sb = new StringBuilder();
        if (new File(OplusUxIconConstants.IconLoader.MY_COUNTRY_DEFAULT_THEME_FILE_PATH).exists()) {
            str = OplusUxIconConstants.IconLoader.MY_COUNTRY_DEFAULT_THEME_FILE_PATH;
        } else if (new File(OplusUxIconConstants.IconLoader.MY_CARRIER_DEFAULT_THEME_FILE_PATH).exists()) {
            str = OplusUxIconConstants.IconLoader.MY_CARRIER_DEFAULT_THEME_FILE_PATH;
        } else {
            str = OplusUxIconConstants.IconLoader.MY_STOCK_DEFAULT_THEME_FILE_PATH;
        }
        String filePath = sb.append(str).append(densityName).append(File.separator).toString();
        if (OplusUxIconConstants.DEBUG_UX_ICON) {
            Log.d(TAG, "filepath=" + filePath);
        }
        if (isSystemApp) {
            drawable = loadUXIconByPath(buildIconPath(!isSpecialStyle ? filePath : OplusUxIconConstants.IconLoader.BASE_PRODUCT_DEFAULT_THEME_FILE_PATH, packageName, iconName), res);
        } else {
            boolean artPlusOn = sIconConfig.isArtPlusOn();
            if (artPlusOn && (drawable = loadUXIconByPath(buildIconPath(OplusUxIconConstants.IconLoader.BASE_UX_ICONS_FILE_PATH, packageName, iconName), res)) == null) {
                drawable = loadUXIconByPath(buildIconPath(filePath, packageName, iconName), res);
            }
        }
        Trace.traceEnd(OplusWifiManager.OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE);
        return drawable;
    }

    private Drawable buildAdaptiveIconDrawable(Resources res, Drawable foreground, Drawable background, Drawable monochromeDrawable, boolean isPlatformDrawable, boolean isAdaptiveIconDrawable) {
        if (foreground != null || background != null) {
            setDarkFilterToDrawable(foreground);
            setDarkFilterToDrawable(background);
            AdaptiveIconDrawable iconDrawable = new AdaptiveIconDrawable(background, foreground, monochromeDrawable);
            IAdaptiveIconDrawableExt iAdaptiveIconDrawableExt = iconDrawable.getWrapper().getAdaptiveIconDrawableExt();
            if (iAdaptiveIconDrawableExt != null) {
                iAdaptiveIconDrawableExt.buildAdaptiveIconDrawable(res, OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getIconSize()), OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()), new Path(sIconConfig.getShapePath()), isPlatformDrawable, isAdaptiveIconDrawable);
            }
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.v(TAG, "buildAdaptiveIconDrawable foreground =:" + foreground + ",background =:" + background + ",config =:");
            }
            return iconDrawable;
        }
        return null;
    }

    private Drawable buildAdaptiveIconDrawableForThirdParty(Resources res, Drawable foreground, Drawable background, Drawable monochromeDrawable, int foregroundSize, int backgroundSize) {
        if (foreground != null || background != null) {
            setDarkFilterToDrawable(foreground);
            setDarkFilterToDrawable(background);
            AdaptiveIconDrawable iconDrawable = new AdaptiveIconDrawable(background, foreground, monochromeDrawable);
            IAdaptiveIconDrawableExt iAdaptiveIconDrawableExt = iconDrawable.getWrapper().getAdaptiveIconDrawableExt();
            if (iAdaptiveIconDrawableExt != null) {
                iAdaptiveIconDrawableExt.buildAdaptiveIconDrawable(res, backgroundSize, foregroundSize, new Path(sIconConfig.getShapePath()), false, false);
            }
            return iconDrawable;
        }
        return null;
    }

    private Drawable buildAdaptiveIconDrawableForIconPack(Resources res, Drawable foreground, Drawable background, Drawable monochromeDrawable, Path path, boolean isPlatformDrawable, boolean isAdaptiveIconDrawable) {
        if (foreground != null || background != null) {
            AdaptiveIconDrawable iconDrawable = new AdaptiveIconDrawable(background, foreground, monochromeDrawable);
            IAdaptiveIconDrawableExt iAdaptiveIconDrawableExt = iconDrawable.getWrapper().getAdaptiveIconDrawableExt();
            if (iAdaptiveIconDrawableExt != null) {
                iAdaptiveIconDrawableExt.buildAdaptiveIconDrawable(res, OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getIconSize()), OplusUxIconConfigParser.getPxFromIconConfigDp(res, sIconConfig.getForegroundSize()), path, isPlatformDrawable, isAdaptiveIconDrawable);
            }
            return iconDrawable;
        }
        return null;
    }

    private Drawable loadUXIconByPath(String path, Resources res) {
        if (TextUtils.isEmpty(path)) {
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.v(TAG, "loadUXIconByPath isEmpty(path).");
                return null;
            }
            return null;
        }
        return getDrawableFromPath(path, res);
    }

    private Drawable getBackgroundDrawable(Drawable src) {
        Bitmap temp = getBitmapFromDrawable(src);
        int backgroundColor = OplusPalette.from(temp).generateEdageWithStep(10, 20).getTransMaxColor(-1);
        if (backgroundColor == -1) {
            Drawable drawable = getDefaultBackgroundDrawable();
            return drawable;
        }
        Drawable drawable2 = new ColorDrawable(backgroundColor);
        temp.recycle();
        return drawable2;
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        if (w <= 0) {
            w = 1;
        }
        int h = drawable.getIntrinsicHeight();
        if (h <= 0) {
            h = 1;
        }
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    private String getSpecialStylePrefix(int pos) {
        String[] strArr = this.mSpecialStylePrefixArray;
        if (pos < strArr.length) {
            return strArr[pos];
        }
        return "";
    }

    private String getCommonStylePrefixExceptRect(int pos) {
        int i = pos + 1;
        String[] strArr = this.mCommonStylePrefixArray;
        if (i < strArr.length) {
            return strArr[pos + 1];
        }
        return "";
    }

    private String getRectFgPrefix() {
        return this.mCommonStylePrefixArray[0];
    }

    private String getRectBgPrefix() {
        return this.mCommonStylePrefixArray[1];
    }

    private Drawable getDrawableFromPath(String pathName, Resources res) {
        if (pathName == null || res == null) {
            return null;
        }
        Trace.traceBegin(OplusWifiManager.OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE, pathName);
        try {
            FileInputStream stream = new FileInputStream(pathName);
            try {
                Drawable createFromResourceStream = Drawable.createFromResourceStream(res, null, stream, pathName, null);
                stream.close();
                return createFromResourceStream;
            } catch (Throwable th) {
                try {
                    stream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException e) {
            return null;
        } finally {
            Trace.traceEnd(OplusWifiManager.OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE);
        }
    }

    private boolean hasTransparentPixels(Drawable drawable) {
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        int transparentCount = 0;
        int pixels = 0;
        int xStep = (int) Math.ceil((bitmap.getWidth() * 1.0f) / 4.0f);
        int yStep = (int) Math.ceil((bitmap.getHeight() * 1.0f) / 4.0f);
        for (int i = xStep; i < bitmap.getWidth(); i += xStep) {
            try {
                for (int j = 1; j < 4; j++) {
                    if (Color.alpha(bitmap.getPixel(i, j)) < 220) {
                        pixels++;
                    }
                    if (j == 3 && pixels > 1) {
                        transparentCount++;
                    }
                }
                pixels = 0;
            } catch (IllegalArgumentException e) {
            }
        }
        for (int i2 = xStep; i2 < bitmap.getWidth(); i2 += xStep) {
            for (int j2 = bitmap.getHeight() - 2; j2 > (bitmap.getHeight() - 4) - 1; j2--) {
                if (Color.alpha(bitmap.getPixel(i2, j2)) < 220) {
                    pixels++;
                }
                if (j2 == bitmap.getHeight() - 4 && pixels > 1) {
                    transparentCount++;
                }
            }
            pixels = 0;
        }
        for (int i3 = yStep; i3 < bitmap.getHeight(); i3 += yStep) {
            for (int j3 = 1; j3 < 4; j3++) {
                if (Color.alpha(bitmap.getPixel(j3, i3)) < 220) {
                    pixels++;
                }
                if (j3 == 3 && pixels > 1) {
                    transparentCount++;
                }
            }
            pixels = 0;
        }
        for (int i4 = yStep; i4 < bitmap.getHeight(); i4 += yStep) {
            for (int j4 = bitmap.getWidth() - 2; j4 > (bitmap.getWidth() - 4) - 1; j4--) {
                if (Color.alpha(bitmap.getPixel(j4, i4)) < 220) {
                    pixels++;
                }
                if (j4 == bitmap.getWidth() - 4 && pixels > 1) {
                    transparentCount++;
                }
            }
            pixels = 0;
        }
        return transparentCount >= 6;
    }

    private int getDarkModeColorInCurrentContrast(float currentContrast) {
        int currentColorR;
        if (currentContrast == -1.0f) {
            currentColorR = 214;
        } else {
            currentColorR = (int) (255.0f * currentContrast);
        }
        String colorR = Integer.toHexString(currentColorR);
        StringBuilder sb = new StringBuilder();
        sb.append(colorR).append(colorR).append(colorR);
        return Integer.parseInt(sb.toString(), 16);
    }
}
