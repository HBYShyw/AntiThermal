package android.widget;

import android.graphics.Rect;
import android.view.IViewExt;
import android.view.ViewExtImpl;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.view.IOplusScrollBarEffect;
import com.oplus.view.OplusScrollBarEffect;

/* loaded from: classes.dex */
public class ScrollBarDrawableExtImpl implements IScrollBarDrawableExt {
    private IOplusScrollBarEffect mEffect = OplusScrollBarEffect.NO_EFFECT;
    private ScrollBarDrawable mScrollBarDrawable;

    public ScrollBarDrawableExtImpl() {
    }

    public ScrollBarDrawableExtImpl(Object base) {
        this.mScrollBarDrawable = (ScrollBarDrawable) base;
    }

    public void setScrollBarEffect(IViewExt viewExt) {
        ViewExtImpl viewExtImpl = (ViewExtImpl) OplusTypeCastingHelper.typeCasting(ViewExtImpl.class, viewExt);
        if (viewExtImpl != null) {
            this.mEffect = viewExtImpl.hookScrollBar();
        }
    }

    public void getDrawRect(Rect r) {
        this.mEffect.getDrawRect(r);
    }

    public int getThumbLength(int scrollBarLength, int thickness, int extent, int range) {
        return this.mEffect.getThumbLength(scrollBarLength, thickness, extent, range);
    }
}
