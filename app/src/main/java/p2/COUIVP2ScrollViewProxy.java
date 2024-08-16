package p2;

import androidx.viewpager2.widget.ViewPager2;
import o2.COUIScrollViewProxy;

/* compiled from: COUIVP2ScrollViewProxy.java */
/* renamed from: p2.e, reason: use source file name */
/* loaded from: classes.dex */
public class COUIVP2ScrollViewProxy extends COUIScrollViewProxy<ViewPager2> {
    public COUIVP2ScrollViewProxy(ViewPager2 viewPager2) {
        super(viewPager2);
    }

    @Override // o2.IScrollableView
    public int a() {
        return ((ViewPager2) this.f16134a).getOrientation();
    }

    @Override // o2.IScrollableView
    public boolean b(int i10, int i11) {
        int i12 = (int) (-Math.signum(i11));
        if (i10 == 0) {
            return ((ViewPager2) this.f16134a).canScrollHorizontally(i12);
        }
        return ((ViewPager2) this.f16134a).canScrollVertically(i12);
    }
}
