package com.coui.appcompat.cardView;

import c4.CornerTreatment;
import c4.o;

/* compiled from: COUIEmptyCornerTreatment.java */
/* renamed from: com.coui.appcompat.cardView.a, reason: use source file name */
/* loaded from: classes.dex */
class COUIEmptyCornerTreatment extends CornerTreatment {
    @Override // c4.CornerTreatment
    public void a(o oVar, float f10, float f11, float f12) {
        oVar.o(0.0f, f12 * f11, 180.0f, 0.0f);
        float f13 = f12 * 2.0f * f11;
        oVar.a(0.0f, 0.0f, f13, f13, 180.0f, 0.0f);
    }
}
