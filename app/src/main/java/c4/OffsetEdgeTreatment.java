package c4;

/* compiled from: OffsetEdgeTreatment.java */
/* renamed from: c4.j, reason: use source file name */
/* loaded from: classes.dex */
public final class OffsetEdgeTreatment extends EdgeTreatment {

    /* renamed from: a, reason: collision with root package name */
    private final EdgeTreatment f4811a;

    /* renamed from: b, reason: collision with root package name */
    private final float f4812b;

    public OffsetEdgeTreatment(EdgeTreatment edgeTreatment, float f10) {
        this.f4811a = edgeTreatment;
        this.f4812b = f10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // c4.EdgeTreatment
    public boolean a() {
        return this.f4811a.a();
    }

    @Override // c4.EdgeTreatment
    public void b(float f10, float f11, float f12, o oVar) {
        this.f4811a.b(f10, f11 - this.f4812b, f12, oVar);
    }
}
