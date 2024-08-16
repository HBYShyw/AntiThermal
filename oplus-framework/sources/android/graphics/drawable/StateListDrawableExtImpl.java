package android.graphics.drawable;

import android.common.OplusFeatureCache;
import android.content.res.IOplusThemeManager;
import android.content.res.IResourcesImplExt;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class StateListDrawableExtImpl implements IStateListDrawableExt {
    public StateListDrawableExtImpl(Object stateListDrawable) {
    }

    public Drawable hookInflateChildElements(Drawable dr, IResourcesImplExt resImpExt, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme, Resources r, TypedArray typedArray) {
        if (r.getImpl() != null && ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).shouldHookStateListDrawable(resImpExt, parser, attrs)) {
            dr = ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).hookStateListDrawable(parser, attrs, theme, r, typedArray, resImpExt);
        }
        typedArray.recycle();
        return dr;
    }
}
