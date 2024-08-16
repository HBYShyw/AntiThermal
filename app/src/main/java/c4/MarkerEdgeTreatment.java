package c4;

/* compiled from: MarkerEdgeTreatment.java */
/* renamed from: c4.g, reason: use source file name */
/* loaded from: classes.dex */
public final class MarkerEdgeTreatment extends EdgeTreatment {

    /* renamed from: a, reason: collision with root package name */
    private final float f4763a;

    public MarkerEdgeTreatment(float f10) {
        this.f4763a = f10 - 0.001f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // c4.EdgeTreatment
    public boolean a() {
        return true;
    }

    @Override // c4.EdgeTreatment
    public void b(float f10, float f11, float f12, o oVar) {
        float sqrt = (float) ((this.f4763a * Math.sqrt(2.0d)) / 2.0d);
        float sqrt2 = (float) Math.sqrt(Math.pow(this.f4763a, 2.0d) - Math.pow(sqrt, 2.0d));
        oVar.n(f11 - sqrt, ((float) (-((this.f4763a * Math.sqrt(2.0d)) - this.f4763a))) + sqrt2);
        oVar.m(f11, (float) (-((this.f4763a * Math.sqrt(2.0d)) - this.f4763a)));
        oVar.m(f11 + sqrt, ((float) (-((this.f4763a * Math.sqrt(2.0d)) - this.f4763a))) + sqrt2);
    }
}
