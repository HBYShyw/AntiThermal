package p2;

import androidx.viewpager.widget.ViewPager;
import o2.COUIScrollViewProxy;

/* compiled from: COUIVPScrollViewProxy.java */
/* renamed from: p2.f, reason: use source file name */
/* loaded from: classes.dex */
public class COUIVPScrollViewProxy extends COUIScrollViewProxy<ViewPager> {
    public COUIVPScrollViewProxy(ViewPager viewPager) {
        super(viewPager);
    }

    @Override // o2.IScrollableView
    public int a() {
        return 0;
    }

    @Override // o2.IScrollableView
    public boolean b(int i10, int i11) {
        if (i10 == 1) {
            return false;
        }
        return ((ViewPager) this.f16134a).canScrollHorizontally((int) (-Math.signum(i11)));
    }
}
