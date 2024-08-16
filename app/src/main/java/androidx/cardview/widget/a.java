package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CardViewApi21Impl.java */
/* loaded from: classes.dex */
public class a implements c {
    private d p(b bVar) {
        return (d) bVar.e();
    }

    @Override // androidx.cardview.widget.c
    public void a() {
    }

    @Override // androidx.cardview.widget.c
    public void b(b bVar, Context context, ColorStateList colorStateList, float f10, float f11, float f12) {
        bVar.b(new d(colorStateList, f10));
        View f13 = bVar.f();
        f13.setClipToOutline(true);
        f13.setElevation(f11);
        o(bVar, f12);
    }

    @Override // androidx.cardview.widget.c
    public void c(b bVar, float f10) {
        p(bVar).h(f10);
    }

    @Override // androidx.cardview.widget.c
    public float d(b bVar) {
        return bVar.f().getElevation();
    }

    @Override // androidx.cardview.widget.c
    public float e(b bVar) {
        return p(bVar).d();
    }

    @Override // androidx.cardview.widget.c
    public void f(b bVar) {
        o(bVar, h(bVar));
    }

    @Override // androidx.cardview.widget.c
    public void g(b bVar, float f10) {
        bVar.f().setElevation(f10);
    }

    @Override // androidx.cardview.widget.c
    public float h(b bVar) {
        return p(bVar).c();
    }

    @Override // androidx.cardview.widget.c
    public ColorStateList i(b bVar) {
        return p(bVar).b();
    }

    @Override // androidx.cardview.widget.c
    public void j(b bVar) {
        if (!bVar.d()) {
            bVar.a(0, 0, 0, 0);
            return;
        }
        float h10 = h(bVar);
        float e10 = e(bVar);
        int ceil = (int) Math.ceil(e.a(h10, e10, bVar.c()));
        int ceil2 = (int) Math.ceil(e.b(h10, e10, bVar.c()));
        bVar.a(ceil, ceil2, ceil, ceil2);
    }

    @Override // androidx.cardview.widget.c
    public float k(b bVar) {
        return e(bVar) * 2.0f;
    }

    @Override // androidx.cardview.widget.c
    public float l(b bVar) {
        return e(bVar) * 2.0f;
    }

    @Override // androidx.cardview.widget.c
    public void m(b bVar) {
        o(bVar, h(bVar));
    }

    @Override // androidx.cardview.widget.c
    public void n(b bVar, ColorStateList colorStateList) {
        p(bVar).f(colorStateList);
    }

    @Override // androidx.cardview.widget.c
    public void o(b bVar, float f10) {
        p(bVar).g(f10, bVar.d(), bVar.c());
        j(bVar);
    }
}
