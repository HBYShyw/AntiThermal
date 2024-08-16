package android.util;

import android.app.ActivityThread;
import android.common.OplusFeatureCache;
import android.content.res.IOplusThemeManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import com.oplus.multiapp.OplusMultiAppManager;

/* loaded from: classes.dex */
public class IconDrawableFactoryExtImpl implements IIconDrawableFactoryExt {
    private IconDrawableFactory mIconDrawableFactory;

    public IconDrawableFactoryExtImpl(Object iconDrawableFactory) {
        this.mIconDrawableFactory = null;
        this.mIconDrawableFactory = (IconDrawableFactory) iconDrawableFactory;
    }

    public boolean isOemIcons() {
        return ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).isOplusIcons();
    }

    public boolean isMultiAppUserId(int userId) {
        return OplusMultiAppManager.getInstance().isMultiAppUserId(userId);
    }

    public Drawable hookgetBadgedIcon(Drawable icon) {
        Resources res = ActivityThread.currentActivityThread().getApplication().getResources();
        Drawable badgeShadow = res.getDrawable(201850903);
        Drawable[] drawables = icon == null ? new Drawable[]{badgeShadow} : new Drawable[]{icon, badgeShadow};
        return new LayerDrawable(drawables);
    }
}
