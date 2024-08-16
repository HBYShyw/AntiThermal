package androidx.viewpager2.widget;

import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/* compiled from: CompositeOnPageChangeCallback.java */
/* renamed from: androidx.viewpager2.widget.b, reason: use source file name */
/* loaded from: classes.dex */
final class CompositeOnPageChangeCallback extends ViewPager2.i {

    /* renamed from: a, reason: collision with root package name */
    private final List<ViewPager2.i> f4273a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompositeOnPageChangeCallback(int i10) {
        this.f4273a = new ArrayList(i10);
    }

    private void e(ConcurrentModificationException concurrentModificationException) {
        throw new IllegalStateException("Adding and removing callbacks during dispatch to callbacks is not supported", concurrentModificationException);
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void a(int i10) {
        try {
            Iterator<ViewPager2.i> it = this.f4273a.iterator();
            while (it.hasNext()) {
                it.next().a(i10);
            }
        } catch (ConcurrentModificationException e10) {
            e(e10);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void b(int i10, float f10, int i11) {
        try {
            Iterator<ViewPager2.i> it = this.f4273a.iterator();
            while (it.hasNext()) {
                it.next().b(i10, f10, i11);
            }
        } catch (ConcurrentModificationException e10) {
            e(e10);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void c(int i10) {
        try {
            Iterator<ViewPager2.i> it = this.f4273a.iterator();
            while (it.hasNext()) {
                it.next().c(i10);
            }
        } catch (ConcurrentModificationException e10) {
            e(e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(ViewPager2.i iVar) {
        this.f4273a.add(iVar);
    }
}
