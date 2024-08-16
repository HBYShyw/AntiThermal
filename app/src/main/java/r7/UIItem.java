package r7;

import o7.Vector;

/* compiled from: UIItem.java */
/* renamed from: r7.m, reason: use source file name */
/* loaded from: classes.dex */
public class UIItem<K> {

    /* renamed from: a, reason: collision with root package name */
    public float f17601a;

    /* renamed from: b, reason: collision with root package name */
    public float f17602b;

    /* renamed from: c, reason: collision with root package name */
    K f17603c;

    /* renamed from: d, reason: collision with root package name */
    public final Vector f17604d;

    /* renamed from: e, reason: collision with root package name */
    public final Vector f17605e;

    /* renamed from: f, reason: collision with root package name */
    public final Vector f17606f;

    /* renamed from: g, reason: collision with root package name */
    public final Vector f17607g;

    /* renamed from: h, reason: collision with root package name */
    final Transform f17608h;

    public UIItem() {
        this(null);
    }

    public Transform a() {
        return this.f17608h;
    }

    public UIItem b(float f10, float f11) {
        this.f17601a = f10;
        this.f17602b = f11;
        return this;
    }

    public UIItem c(float f10, float f11) {
        this.f17605e.d(f10, f11);
        return this;
    }

    public UIItem d(float f10, float f11) {
        this.f17606f.d(f10, f11);
        return this;
    }

    public void e(float f10, float f11) {
        this.f17607g.d(f10, f11);
    }

    public void f(float f10, float f11) {
        Transform transform = this.f17608h;
        transform.f17597a = f10;
        transform.f17598b = f11;
    }

    public String toString() {
        return "UIItem{mTarget=" + this.f17603c + ", size=( " + this.f17601a + "," + this.f17602b + "), startPos =:" + this.f17605e + ", startVel =:" + this.f17607g + "}@" + hashCode();
    }

    public UIItem(K k10) {
        this.f17601a = 0.0f;
        this.f17602b = 0.0f;
        this.f17604d = new Vector();
        this.f17605e = new Vector();
        this.f17606f = new Vector(1.0f, 1.0f);
        this.f17607g = new Vector();
        this.f17608h = new Transform();
        this.f17603c = k10;
    }
}
