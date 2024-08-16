package com.coui.appcompat.viewpager;

import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/* compiled from: COUICompositeOnPageChangeCallback.java */
/* renamed from: com.coui.appcompat.viewpager.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUICompositeOnPageChangeCallback extends ViewPager2.i {

    /* renamed from: a, reason: collision with root package name */
    private final List<ViewPager2.i> f8137a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUICompositeOnPageChangeCallback(int i10) {
        this.f8137a = new ArrayList(i10);
    }

    private void f(ConcurrentModificationException concurrentModificationException) {
        throw new IllegalStateException("Adding and removing callbacks during dispatch to callbacks is not supported", concurrentModificationException);
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void a(int i10) {
        try {
            Iterator<ViewPager2.i> it = this.f8137a.iterator();
            while (it.hasNext()) {
                it.next().a(i10);
            }
        } catch (ConcurrentModificationException e10) {
            f(e10);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void b(int i10, float f10, int i11) {
        try {
            Iterator<ViewPager2.i> it = this.f8137a.iterator();
            while (it.hasNext()) {
                it.next().b(i10, f10, i11);
            }
        } catch (ConcurrentModificationException e10) {
            f(e10);
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void c(int i10) {
        try {
            Iterator<ViewPager2.i> it = this.f8137a.iterator();
            while (it.hasNext()) {
                it.next().c(i10);
            }
        } catch (ConcurrentModificationException e10) {
            f(e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(ViewPager2.i iVar) {
        this.f8137a.add(iVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(ViewPager2.i iVar) {
        this.f8137a.remove(iVar);
    }
}
