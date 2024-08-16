package android.content.pm;

import android.common.OplusFeatureCache;
import android.content.res.IOplusThemeManager;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;

/* loaded from: classes.dex */
public class LauncherActivityInfoExtImpl implements ILauncherActivityInfoExt {
    private LauncherActivityInfo mLauncherActivityInfo;

    public LauncherActivityInfoExtImpl(Object launcherActivityInfo) {
        this.mLauncherActivityInfo = (LauncherActivityInfo) launcherActivityInfo;
    }

    public boolean isOplusIcons() {
        return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).isOplusIcons();
    }

    public Drawable getBadgedIcon(int density, PackageManager pm, UserHandle user, ActivityInfo info) {
        return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).getBadgedIcon(this.mLauncherActivityInfo, density, pm, user, info);
    }
}
