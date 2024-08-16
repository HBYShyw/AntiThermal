package android.content.res;

import android.R;
import android.app.OplusThemeHelper;
import android.app.OplusUXIconLoadHelper;
import android.app.OplusUxIconConfigParser;
import android.app.OplusUxIconConstants;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.wifi.OplusWifiManager;
import android.os.Debug;
import android.os.FileUtils;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.theme.OplusThemeUtil;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.widget.OplusMaxLinearLayout;
import java.io.File;
import oplus.content.res.OplusExtraConfiguration;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class OplusThemeManager implements IOplusThemeManager {
    private static final String TAG = "OplusThemeManager";
    private static volatile OplusThemeManager sInstance = null;

    public static OplusThemeManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusThemeManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusThemeManager();
                }
            }
        }
        return sInstance;
    }

    OplusThemeManager() {
    }

    @Override // android.content.res.IOplusThemeManager
    public void init(IResourcesImplExt resourcesImplExt, String name) {
        if (resourcesImplExt != null) {
            resourcesImplExt.init(name);
        }
    }

    @Override // android.content.res.IOplusThemeManager
    public CharSequence getText(IResourcesImplExt resourcesImplExt, int id, CharSequence text) {
        CharSequence themeChar = resourcesImplExt.getThemeCharSequence(id);
        if (themeChar != null) {
            return themeChar;
        }
        return text;
    }

    @Override // android.content.res.IOplusThemeManager
    public Drawable getDefaultActivityIcon(IUxIconPackageManagerExt packageManagerExt, Context context, IResourcesExt resourcesExt) {
        Trace.traceBegin(OplusWifiManager.OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE, "#UxIcon.getDefaultActivityIcon");
        if ("android.content.cts".equals(context.getPackageName())) {
            return context.getDrawable(R.drawable.sym_def_app_icon);
        }
        Drawable dr = context.getDrawable(R.mipmap.sym_def_app_icon);
        if (dr != null) {
            if (OplusUXIconLoadHelper.supportUxIcon(packageManagerExt, context.getApplicationInfo(), context.getPackageName())) {
                dr = OplusUXIconLoadHelper.getUxIconDrawable(context.getResources(), resourcesExt, dr, false);
            } else {
                dr = OplusThemeHelper.getInstance().getDrawableByConvert(resourcesExt, context.getResources(), dr);
            }
        }
        Trace.traceEnd(OplusWifiManager.OPLUS_WIFI_FEATURE_NOT_SUPPORT_AUTO_CHANGE);
        return dr;
    }

    @Override // android.content.res.IOplusThemeManager
    public Drawable loadPackageItemIcon(PackageItemInfo info, PackageManager pm, IUxIconPackageManagerExt pmExt, ApplicationInfo appInfo, boolean isConvertEnable) {
        if (isConvertEnable) {
            return pmExt.loadItemIcon(info, appInfo, true);
        }
        return pm.loadItemIcon(info, appInfo);
    }

    @Override // android.content.res.IOplusThemeManager
    public Drawable getBadgedIcon(LauncherActivityInfo info, int density, PackageManager pm, UserHandle user, ActivityInfo activity) {
        Drawable originalIcon = activity.loadIcon(pm);
        if (originalIcon == null) {
            originalIcon = activity.loadDefaultIcon(pm);
        }
        return pm.getUserBadgedIcon(originalIcon, user);
    }

    @Override // android.content.res.IOplusThemeManager
    public boolean isOplusIcons() {
        return true;
    }

    @Override // android.content.res.IOplusThemeManager
    public boolean supportUxIcon(IUxIconPackageManagerExt pmExt, ApplicationInfo ai, String packageName) {
        return OplusUXIconLoadHelper.supportUxIcon(pmExt, ai, packageName);
    }

    @Override // android.content.res.IOplusThemeManager
    public Drawable getDrawableFromUxIcon(IUxIconPackageManagerExt packageManagerExt, String packageName, String activityName, int id, ApplicationInfo appInfo, boolean loadByResolver) {
        return OplusUXIconLoadHelper.getDrawable(packageManagerExt, packageName, activityName, id, appInfo, loadByResolver);
    }

    @Override // android.content.res.IOplusThemeManager
    public Drawable getDrawableForApp(Resources res, IResourcesExt colorRes, Drawable src, boolean isForegroundDrawable) {
        return OplusUXIconLoadHelper.getUxIconDrawable(res, colorRes, src, isForegroundDrawable);
    }

    @Override // android.content.res.IOplusThemeManager
    public long getIconConfigFromSettings(ContentResolver resolver, Context context, int userId) {
        long uxIconConfig = Settings.System.getLongForUser(resolver, OplusUxIconConstants.SystemProperty.KEY_UX_ICON_CONFIG, -1L, userId);
        if (uxIconConfig == -1) {
            if (userId == 0 && SystemProperties.getInt(OplusUxIconConstants.SystemProperty.KEY_UX_ICON_THEME_FLAG, 0) == 0) {
                SystemProperties.set(OplusUxIconConstants.SystemProperty.KEY_UX_ICON_THEME_FLAG, String.valueOf(-1));
            }
            boolean isForeign = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_UXICON_EXP);
            long uxIconConfig2 = OplusUxIconConfigParser.getDefaultIconConfig(isForeign, context, userId);
            Settings.System.putLongForUser(resolver, OplusUxIconConstants.SystemProperty.KEY_UX_ICON_CONFIG, uxIconConfig2, userId);
            return uxIconConfig2;
        }
        return uxIconConfig;
    }

    @Override // android.content.res.IOplusThemeManager
    public void setIconConfigToSettings(ContentResolver resolver, long uxIconConfig, int userId) {
        if (uxIconConfig > 0) {
            Settings.System.putLongForUser(resolver, OplusUxIconConstants.SystemProperty.KEY_UX_ICON_CONFIG, uxIconConfig, userId);
        } else {
            Log.e(TAG, "setIconConfigToSettings error: uxIconConfig <= 0 " + uxIconConfig + "  userId:" + userId);
        }
    }

    @Override // android.content.res.IOplusThemeManager
    public void updateExtraConfigForUxIcon(int changes) {
        OplusUXIconLoadHelper.updateExtraConfig(changes);
        if (OplusUxIconConstants.DEBUG_UX_ICON) {
            Log.i(TAG, "updateExtraConfigForUxIcon changes = " + changes + "; callers:" + Debug.getCallers(10));
        }
    }

    @Override // android.content.res.IOplusThemeManager
    public boolean supportUxOnline(PackageManager packageManager, String sourcePackageName) {
        return OplusUXIconLoadHelper.supportUxOnline(packageManager, sourcePackageName);
    }

    @Override // android.content.res.IOplusThemeManager
    public void onCleanupUserForTheme(int userId) {
        if (userId != 0) {
            File themeFileForUser = new File("/data/theme/" + userId);
            File themeApplyForUser = new File("/data/theme/applying/" + userId);
            boolean cleanUserThemeResult = false;
            boolean deleteApply = false;
            if (themeFileForUser.exists()) {
                cleanUserThemeResult = FileUtils.deleteContentsAndDir(themeFileForUser);
            }
            if (themeApplyForUser.exists()) {
                deleteApply = FileUtils.deleteContentsAndDir(themeApplyForUser);
            }
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.i(TAG, "onCleanupUserForTheme= " + cleanUserThemeResult + ", deleteApply= " + deleteApply + ", user= " + userId);
            }
        }
    }

    @Override // android.content.res.IOplusThemeManager
    public Drawable loadOverlayResolverDrawable(IUxIconPackageManagerExt packageManagerExt, String packageName, int resourceId, ApplicationInfo app, String resolverIconName) {
        Drawable dr;
        if (OplusUXIconLoadHelper.supportUxIcon(packageManagerExt, app, packageName)) {
            dr = OplusUXIconLoadHelper.loadOverlayResolverDrawable(packageManagerExt, packageName, resourceId, app, resolverIconName);
        } else {
            dr = OplusThemeHelper.getInstance().getDrawable(packageManagerExt, packageName, resourceId, app, null);
        }
        if (dr == null) {
            Drawable dr2 = packageManagerExt.getPackageManager().getDrawable(packageName, resourceId, app);
            return dr2;
        }
        return dr;
    }

    @Override // android.content.res.IOplusThemeManager
    public boolean shouldReportExtraConfig(int changes, int realChanges) {
        return OplusExtraConfiguration.shouldReportExtra(changes, realChanges);
    }

    @Override // android.content.res.IOplusThemeManager
    public void setDarkFilterToDrawable(Drawable drawable) {
        OplusUXIconLoadHelper.setDarkFilterToDrawable(drawable);
    }

    @Override // android.content.res.IOplusThemeManager
    public boolean shouldHookStateListDrawable(IResourcesImplExt resImpExt, XmlPullParser parser, AttributeSet attrs) {
        if (resImpExt != null && resImpExt.isHasDrawables() && parser.getName().equals("color") && attrs.getAttributeCount() > 0) {
            return true;
        }
        return false;
    }

    @Override // android.content.res.IOplusThemeManager
    public Drawable hookStateListDrawable(XmlPullParser parser, AttributeSet attrs, Resources.Theme theme, Resources r, TypedArray typedArray, IResourcesImplExt resImpExt) {
        try {
            int id = typedArray.getResourceId(0, 0);
            if (id == 0) {
                return null;
            }
            String entryName = r.getResourceEntryName(id);
            if (TextUtils.isEmpty(entryName)) {
                return null;
            }
            String path = "res/drawable-xxhdpi/" + entryName + ".xml";
            TypedValue value = new TypedValue();
            value.string = path;
            Drawable dr = resImpExt.loadOverlayDrawable(r, value, id);
            return dr;
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    @Override // android.content.res.IOplusThemeManager
    public String getAppThemeVersion(String pkgName, boolean change) {
        return OplusThemeUtil.getAppThemeVersion(pkgName, change);
    }

    @Override // android.content.res.IOplusThemeManager
    public boolean interceptOplusConfigChange(ActivityInfo activityInfo, IPackageManager pm, Configuration config, int changes) {
        if (activityInfo == null || OplusThemeResources.FRAMEWORK_PACKAGE.equals(activityInfo.packageName) || pm == null || changes == 0) {
            return false;
        }
        if ((activityInfo.getRealConfigChanged() & 512) != 0 && (16777216 & changes) != 0 && (changes & 512) != 0) {
            return true;
        }
        int overrideChanges = 0;
        ActivityInfo appInfo = null;
        try {
            OplusBaseConfiguration baseConfig = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, config);
            if (baseConfig != null && baseConfig.mOplusExtraConfiguration != null) {
                int userId = Math.max(baseConfig.mOplusExtraConfiguration.mUserId, 0);
                appInfo = pm.getActivityInfo(activityInfo.getComponentName(), 128L, userId);
            }
        } catch (RemoteException | SecurityException ex) {
            if (OplusUxIconConstants.DEBUG_UX_ICON) {
                Log.i(TAG, "interceptOplusConfigChange ex: " + activityInfo.getComponentName() + ", " + ex);
            }
        }
        if (appInfo != null && appInfo.metaData != null && (overrideChanges = appInfo.metaData.getInt("oplus_config_change", 0)) != 0) {
            overrideChanges |= activityInfo.getRealConfigChanged();
        }
        if (overrideChanges == 0) {
            return false;
        }
        int newChanges = changes;
        if ((changes & Integer.MIN_VALUE) != 0 && (Integer.MIN_VALUE & overrideChanges) == 0) {
            newChanges = changes & OplusMaxLinearLayout.INVALID_MAX_VALUE;
        }
        if (newChanges == 0 || ((~overrideChanges) & newChanges) != 0) {
            return false;
        }
        return true;
    }
}
