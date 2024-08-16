package com.coui.appcompat.viewpager;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Locale;

/* compiled from: COUIPageTransformerAdapter.java */
/* renamed from: com.coui.appcompat.viewpager.d, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPageTransformerAdapter extends ViewPager2.i {

    /* renamed from: a, reason: collision with root package name */
    private final LinearLayoutManager f8146a;

    /* renamed from: b, reason: collision with root package name */
    private ViewPager2.k f8147b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUIPageTransformerAdapter(LinearLayoutManager linearLayoutManager) {
        this.f8146a = linearLayoutManager;
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void a(int i10) {
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void b(int i10, float f10, int i11) {
        if (this.f8147b == null) {
            return;
        }
        float f11 = -f10;
        for (int i12 = 0; i12 < this.f8146a.J(); i12++) {
            View I = this.f8146a.I(i12);
            if (I != null) {
                this.f8147b.a(I, (this.f8146a.j0(I) - i10) + f11);
            } else {
                throw new IllegalStateException(String.format(Locale.US, "LayoutManager returned a null child at pos %d/%d while transforming pages", Integer.valueOf(i12), Integer.valueOf(this.f8146a.J())));
            }
        }
    }

    @Override // androidx.viewpager2.widget.ViewPager2.i
    public void c(int i10) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewPager2.k d() {
        return this.f8147b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(ViewPager2.k kVar) {
        this.f8147b = kVar;
    }
}
