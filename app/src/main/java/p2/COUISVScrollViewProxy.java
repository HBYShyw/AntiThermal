package p2;

import android.widget.ScrollView;
import o2.COUIScrollViewProxy;

/* compiled from: COUISVScrollViewProxy.java */
/* renamed from: p2.d, reason: use source file name */
/* loaded from: classes.dex */
public class COUISVScrollViewProxy extends COUIScrollViewProxy<ScrollView> {
    public COUISVScrollViewProxy(ScrollView scrollView) {
        super(scrollView);
    }

    @Override // o2.IScrollableView
    public int a() {
        return 1;
    }

    @Override // o2.IScrollableView
    public boolean b(int i10, int i11) {
        if (i10 == 0) {
            return false;
        }
        return ((ScrollView) this.f16134a).canScrollVertically((int) (-Math.signum(i11)));
    }
}
