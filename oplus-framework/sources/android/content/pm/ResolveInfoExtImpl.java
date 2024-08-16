package android.content.pm;

import android.content.res.IUxIconPackageManagerExt;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class ResolveInfoExtImpl implements IResolveInfoExt {
    public ResolveInfoExtImpl(Object base) {
    }

    public Drawable getDrawableByResolvePackageNameInLoadIcon(PackageManager manager, ResolveInfo resolveInfo, String resolvePackageName, int iconResourceId) {
        if (manager == null) {
            return null;
        }
        IUxIconPackageManagerExt uxIconPackageManagerExt = (IUxIconPackageManagerExt) manager.mPackageManagerExt.getUxIconPackageManagerExt();
        if (uxIconPackageManagerExt == null) {
            uxIconPackageManagerExt = IUxIconPackageManagerExt.DEFAULT;
        }
        return uxIconPackageManagerExt.loadResolveIcon(resolveInfo, manager, resolvePackageName, iconResourceId, (ApplicationInfo) null, false);
    }

    public Drawable getDrawableByComponentInfoInLoadIcon(PackageManager manager, ResolveInfo resolveInfo, String packageName, int iconResourceId, ApplicationInfo ai) {
        if (manager == null) {
            return null;
        }
        IUxIconPackageManagerExt uxIconPackageManagerExt = (IUxIconPackageManagerExt) manager.mPackageManagerExt.getUxIconPackageManagerExt();
        if (uxIconPackageManagerExt == null) {
            uxIconPackageManagerExt = IUxIconPackageManagerExt.DEFAULT;
        }
        return uxIconPackageManagerExt.loadResolveIcon(resolveInfo, manager, packageName, iconResourceId, ai, false);
    }
}
