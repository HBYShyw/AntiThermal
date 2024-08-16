package s4;

/* compiled from: MeanCalculator.java */
/* renamed from: s4.f, reason: use source file name */
/* loaded from: classes.dex */
public class MeanCalculator {

    /* renamed from: a, reason: collision with root package name */
    private float f18056a;

    /* renamed from: b, reason: collision with root package name */
    private int f18057b;

    public void a(float f10) {
        float f11 = this.f18056a + f10;
        this.f18056a = f11;
        int i10 = this.f18057b + 1;
        this.f18057b = i10;
        if (i10 == Integer.MAX_VALUE) {
            this.f18056a = f11 / 2.0f;
            this.f18057b = i10 / 2;
        }
    }
}
