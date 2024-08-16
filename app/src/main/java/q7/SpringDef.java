package q7;

import o7.Vector;
import p7.Body;

/* compiled from: SpringDef.java */
/* renamed from: q7.c, reason: use source file name */
/* loaded from: classes.dex */
public class SpringDef {

    /* renamed from: a, reason: collision with root package name */
    public Body f16929a;

    /* renamed from: b, reason: collision with root package name */
    public Body f16930b;

    /* renamed from: c, reason: collision with root package name */
    public final Vector f16931c;

    /* renamed from: d, reason: collision with root package name */
    public float f16932d;

    /* renamed from: e, reason: collision with root package name */
    public float f16933e;

    /* renamed from: f, reason: collision with root package name */
    public float f16934f;

    public SpringDef() {
        Vector vector = new Vector();
        this.f16931c = vector;
        vector.d(0.0f, 0.0f);
        this.f16932d = Float.MAX_VALUE;
        this.f16933e = 6.0f;
        this.f16934f = 0.8f;
    }
}
