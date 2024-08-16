package c4;

/* compiled from: CutCornerTreatment.java */
/* renamed from: c4.e, reason: use source file name */
/* loaded from: classes.dex */
public class CutCornerTreatment extends CornerTreatment {

    /* renamed from: a, reason: collision with root package name */
    float f4762a = -1.0f;

    @Override // c4.CornerTreatment
    public void a(o oVar, float f10, float f11, float f12) {
        oVar.o(0.0f, f12 * f11, 180.0f, 180.0f - f10);
        double d10 = f12;
        double d11 = f11;
        oVar.m((float) (Math.sin(Math.toRadians(f10)) * d10 * d11), (float) (Math.sin(Math.toRadians(90.0f - f10)) * d10 * d11));
    }
}
