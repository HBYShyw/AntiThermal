package p2;

import androidx.core.widget.NestedScrollView;
import o2.COUIScrollViewProxy;

/* compiled from: COUINSVScrollViewProxy.java */
/* renamed from: p2.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUINSVScrollViewProxy extends COUIScrollViewProxy<NestedScrollView> {
    public COUINSVScrollViewProxy(NestedScrollView nestedScrollView) {
        super(nestedScrollView);
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
        return ((NestedScrollView) this.f16134a).canScrollVertically((int) (-Math.signum(i11)));
    }
}
