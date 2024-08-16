package com.coui.appcompat.cardview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;

/* compiled from: CardViewApi21Impl.java */
/* loaded from: classes.dex */
class d implements f {
    private g o(e eVar) {
        return (g) eVar.e();
    }

    @Override // com.coui.appcompat.cardview.f
    public void a() {
    }

    @Override // com.coui.appcompat.cardview.f
    public void b(e eVar, Context context, ColorStateList colorStateList, float f10, float f11, float f12) {
        eVar.b(new g(colorStateList, f10));
        View f13 = eVar.f();
        f13.setClipToOutline(true);
        f13.setElevation(f11);
        d(eVar, f12);
    }

    @Override // com.coui.appcompat.cardview.f
    public void c(e eVar, float f10) {
        o(eVar).h(f10);
    }

    @Override // com.coui.appcompat.cardview.f
    public void d(e eVar, float f10) {
        o(eVar).g(f10, eVar.d(), eVar.c());
        p(eVar);
    }

    @Override // com.coui.appcompat.cardview.f
    public void e(e eVar) {
        d(eVar, i(eVar));
    }

    @Override // com.coui.appcompat.cardview.f
    public float f(e eVar) {
        return l(eVar) * 2.0f;
    }

    @Override // com.coui.appcompat.cardview.f
    public ColorStateList g(e eVar) {
        return o(eVar).b();
    }

    @Override // com.coui.appcompat.cardview.f
    public float h(e eVar) {
        return l(eVar) * 2.0f;
    }

    @Override // com.coui.appcompat.cardview.f
    public float i(e eVar) {
        return o(eVar).c();
    }

    @Override // com.coui.appcompat.cardview.f
    public void j(e eVar, ColorStateList colorStateList) {
        o(eVar).f(colorStateList);
    }

    @Override // com.coui.appcompat.cardview.f
    public float k(e eVar) {
        return eVar.f().getElevation();
    }

    @Override // com.coui.appcompat.cardview.f
    public float l(e eVar) {
        return o(eVar).d();
    }

    @Override // com.coui.appcompat.cardview.f
    public void m(e eVar, float f10) {
        eVar.f().setElevation(f10);
    }

    @Override // com.coui.appcompat.cardview.f
    public void n(e eVar) {
        d(eVar, i(eVar));
    }

    public void p(e eVar) {
        if (!eVar.d()) {
            eVar.a(0, 0, 0, 0);
            return;
        }
        float i10 = i(eVar);
        float l10 = l(eVar);
        int ceil = (int) Math.ceil(h.a(i10, l10, eVar.c()));
        int ceil2 = (int) Math.ceil(h.b(i10, l10, eVar.c()));
        eVar.a(ceil, ceil2, ceil, ceil2);
    }
}
