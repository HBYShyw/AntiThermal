package android.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class LauncherIconsExtImpl implements ILauncherIconsExt {
    public LauncherIconsExtImpl(Object object) {
    }

    public Drawable getCorpIconBadgeShadow(Resources overlayableRes) {
        return overlayableRes.getDrawable(201851395);
    }

    public Drawable getCorpIconBadgeColor(Resources overlayableRes) {
        return overlayableRes.getDrawable(201851394).getConstantState().newDrawable().mutate();
    }
}
