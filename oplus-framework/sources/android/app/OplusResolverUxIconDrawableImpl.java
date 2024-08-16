package android.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class OplusResolverUxIconDrawableImpl implements IOplusResolverUxIconDrawableManager {
    private String mIconName;

    public OplusResolverUxIconDrawableImpl(String iconName) {
        this.mIconName = iconName;
    }

    @Override // android.app.IOplusResolverUxIconDrawableManager
    public Drawable getDrawable(PackageManager packageManager, String packageName, ApplicationInfo applicationInfo, Drawable originDrawable) {
        Drawable drawable = null;
        if (!(originDrawable instanceof AdaptiveIconDrawable)) {
            try {
                Resources resources = packageManager.getResourcesForApplication(applicationInfo);
                drawable = OplusUXIconLoader.getLoader().findAppDrawable(packageName, this.mIconName, resources, false, true, false);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (drawable == null) {
            drawable = originDrawable;
        }
        OplusUXIconLoader.getLoader().mOplusUxIconDrawableManager = IOplusResolverUxIconDrawableManager.DEFAULT;
        return drawable;
    }
}
