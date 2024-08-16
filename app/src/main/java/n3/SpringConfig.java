package n3;

/* compiled from: SpringConfig.java */
/* renamed from: n3.g, reason: use source file name */
/* loaded from: classes.dex */
public class SpringConfig {

    /* renamed from: c, reason: collision with root package name */
    public static SpringConfig f15746c = b(40.0d, 7.0d);

    /* renamed from: a, reason: collision with root package name */
    public double f15747a;

    /* renamed from: b, reason: collision with root package name */
    public double f15748b;

    public SpringConfig(double d10, double d11) {
        this.f15748b = d10;
        this.f15747a = d11;
    }

    public static SpringConfig a(double d10, double d11) {
        BouncyConversion bouncyConversion = new BouncyConversion(d11, d10);
        return b(bouncyConversion.f(), bouncyConversion.e());
    }

    public static SpringConfig b(double d10, double d11) {
        return new SpringConfig(OrigamiValueConverter.d(d10), OrigamiValueConverter.a(d11));
    }
}
