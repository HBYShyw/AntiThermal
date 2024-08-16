package p2;

import com.coui.appcompat.viewpager.COUIViewPager2;
import o2.COUIScrollViewProxy;

/* compiled from: COUICVPScrollViewProxy.java */
/* renamed from: p2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUICVPScrollViewProxy extends COUIScrollViewProxy<COUIViewPager2> {
    public COUICVPScrollViewProxy(COUIViewPager2 cOUIViewPager2) {
        super(cOUIViewPager2);
    }

    @Override // o2.IScrollableView
    public int a() {
        return ((COUIViewPager2) this.f16134a).getOrientation();
    }

    @Override // o2.IScrollableView
    public boolean b(int i10, int i11) {
        int i12 = (int) (-Math.signum(i11));
        if (a() == 0) {
            return ((COUIViewPager2) this.f16134a).canScrollHorizontally(i12);
        }
        return ((COUIViewPager2) this.f16134a).canScrollVertically(i12);
    }
}
