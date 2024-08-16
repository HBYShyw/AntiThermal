package android.content.res;

import android.R;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
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
import android.os.UserHandle;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public interface IOplusThemeManager extends IOplusCommonFeature {
    public static final IOplusThemeManager DEFAULT = new IOplusThemeManager() { // from class: android.content.res.IOplusThemeManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusThemeManager;
    }

    default IOplusThemeManager getDefault() {
        return DEFAULT;
    }

    default void init(IResourcesImplExt resources, String name) {
    }

    default CharSequence getText(IResourcesImplExt res, int id, CharSequence text) {
        return text;
    }

    default Drawable getDefaultActivityIcon(IUxIconPackageManagerExt packageManagerExt, Context context, IResourcesExt resourcesExt) {
        return context.getDrawable(R.drawable.sym_def_app_icon);
    }

    default Drawable loadPackageItemIcon(PackageItemInfo info, PackageManager pm, IUxIconPackageManagerExt pmExt, ApplicationInfo appInfo, boolean isConvertEnable) {
        return pm.loadItemIcon(info, appInfo);
    }

    default Drawable getBadgedIcon(LauncherActivityInfo info, int density, PackageManager pm, UserHandle user, ActivityInfo activity) {
        Drawable originalIcon = info.getIcon(density);
        return pm.getUserBadgedIcon(originalIcon, user);
    }

    default boolean isOplusIcons() {
        return false;
    }

    default boolean supportUxIcon(IUxIconPackageManagerExt pm, ApplicationInfo app, String packageName) {
        return false;
    }

    default Drawable getDrawableFromUxIcon(IUxIconPackageManagerExt packageManagerExt, String packageName, String activityName, int id, ApplicationInfo appInfo, boolean loadByResolver) {
        if (packageManagerExt != null && packageManagerExt.getPackageManager() != null) {
            return packageManagerExt.getPackageManager().getDrawable(packageName, id, appInfo);
        }
        return null;
    }

    default Drawable getDrawableForApp(Drawable src, boolean isForegroundDrawable) {
        return src;
    }

    default Drawable getDrawableForApp(Resources res, IResourcesExt colorRes, Drawable src, boolean isForegroundDrawable) {
        return src;
    }

    default long getIconConfigFromSettings(ContentResolver resolver, Context context, int userId) {
        return -1L;
    }

    default void setIconConfigToSettings(ContentResolver resolver, long uxIconConfig, int userId) {
    }

    default void updateExtraConfigForUxIcon(int changes) {
    }

    default boolean supportUxOnline(PackageManager packageManager, String sourcePackageName) {
        return false;
    }

    default void onCleanupUserForTheme(int userId) {
    }

    default Drawable loadOverlayResolverDrawable(IUxIconPackageManagerExt packageManagerExt, String packageName, int resourceId, ApplicationInfo app, String resolverIconName) {
        if (packageManagerExt != null && packageManagerExt.getPackageManager() != null) {
            return packageManagerExt.getPackageManager().getDrawable(packageName, resourceId, app);
        }
        return null;
    }

    default boolean shouldReportExtraConfig(int changes, int realChanges) {
        return false;
    }

    default void setDarkFilterToDrawable(Drawable drawable) {
    }

    default boolean shouldHookStateListDrawable(IResourcesImplExt resImpExt, XmlPullParser parser, AttributeSet attrs) {
        return false;
    }

    default Drawable hookStateListDrawable(XmlPullParser parser, AttributeSet attrs, Resources.Theme theme, Resources resouce, TypedArray typedArray, IResourcesImplExt resImpExt) {
        return null;
    }

    default boolean interceptOplusConfigChange(ActivityInfo activityInfo, IPackageManager pm, Configuration config, int changes) {
        return false;
    }

    default String getAppThemeVersion(String pkgName, boolean change) {
        return null;
    }
}
