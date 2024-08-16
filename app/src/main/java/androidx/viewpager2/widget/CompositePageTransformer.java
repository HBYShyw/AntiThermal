package androidx.viewpager2.widget;

import android.view.View;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: CompositePageTransformer.java */
/* renamed from: androidx.viewpager2.widget.c, reason: use source file name */
/* loaded from: classes.dex */
public final class CompositePageTransformer implements ViewPager2.k {

    /* renamed from: a, reason: collision with root package name */
    private final List<ViewPager2.k> f4274a = new ArrayList();

    @Override // androidx.viewpager2.widget.ViewPager2.k
    public void a(View view, float f10) {
        Iterator<ViewPager2.k> it = this.f4274a.iterator();
        while (it.hasNext()) {
            it.next().a(view, f10);
        }
    }

    public void b(ViewPager2.k kVar) {
        this.f4274a.add(kVar);
    }
}
