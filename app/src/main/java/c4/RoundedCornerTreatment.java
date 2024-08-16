package c4;

/* compiled from: RoundedCornerTreatment.java */
/* renamed from: c4.l, reason: use source file name */
/* loaded from: classes.dex */
public class RoundedCornerTreatment extends CornerTreatment {

    /* renamed from: a, reason: collision with root package name */
    float f4814a = -1.0f;

    @Override // c4.CornerTreatment
    public void a(o oVar, float f10, float f11, float f12) {
        oVar.o(0.0f, f12 * f11, 180.0f, 180.0f - f10);
        float f13 = f12 * 2.0f * f11;
        oVar.a(0.0f, 0.0f, f13, f13, 180.0f, f10);
    }
}
