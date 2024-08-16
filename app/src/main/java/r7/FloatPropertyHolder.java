package r7;

/* compiled from: FloatPropertyHolder.java */
/* renamed from: r7.i, reason: use source file name */
/* loaded from: classes.dex */
public abstract class FloatPropertyHolder<T> {

    /* renamed from: b, reason: collision with root package name */
    public String f17581b;

    /* renamed from: c, reason: collision with root package name */
    public float f17582c;

    /* renamed from: d, reason: collision with root package name */
    float f17583d;

    /* renamed from: e, reason: collision with root package name */
    public boolean f17584e = false;

    /* renamed from: a, reason: collision with root package name */
    public int f17580a = 0;

    public FloatPropertyHolder(String str, float f10) {
        this.f17581b = str;
        this.f17582c = f10;
    }

    public abstract float a(T t7);

    public abstract void b(T t7, float f10);

    public FloatPropertyHolder c(float f10) {
        this.f17583d = f10;
        this.f17584e = true;
        return this;
    }

    public void d(T t7, float f10) {
        b(t7, f10 * this.f17582c);
    }

    public void e(T t7) {
    }

    public void f(T t7) {
        if (this.f17584e) {
            return;
        }
        this.f17583d = a(t7);
    }
}
