package android.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public interface IOplusResolverUxIconDrawableManager {
    public static final IOplusResolverUxIconDrawableManager DEFAULT = new IOplusResolverUxIconDrawableManager() { // from class: android.app.IOplusResolverUxIconDrawableManager.1
    };

    default Drawable getDrawable(PackageManager packageManager, String packageName, ApplicationInfo applicationInfo, Drawable originDrawable) {
        return originDrawable;
    }
}
