package android.content.pm;

import android.common.OplusFeatureCache;
import android.content.res.IOplusThemeManager;
import android.content.res.IUxIconPackageManagerExt;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class OplusPackageItemInfoExtImpl implements IPackageItemInfoExt {
    private PackageItemInfo mPackageItemInfo;

    public OplusPackageItemInfoExtImpl(Object base) {
        this.mPackageItemInfo = (PackageItemInfo) base;
    }

    public Drawable loadIcon(PackageItemInfo pkgItemInfo, PackageManager pm, ApplicationInfo info) {
        IUxIconPackageManagerExt uxIconPackageManagerExt = (IUxIconPackageManagerExt) pm.mPackageManagerExt.getUxIconPackageManagerExt();
        if (uxIconPackageManagerExt == null) {
            uxIconPackageManagerExt = IUxIconPackageManagerExt.DEFAULT;
        }
        return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).loadPackageItemIcon(pkgItemInfo, pm, uxIconPackageManagerExt, info, true);
    }
}
