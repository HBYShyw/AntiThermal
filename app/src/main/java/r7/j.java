package r7;

/* compiled from: FloatValueHolder.java */
/* loaded from: classes.dex */
public class j extends FloatPropertyHolder<UIItem> {

    /* renamed from: f, reason: collision with root package name */
    float f17585f;

    public j(float f10) {
        this("floatValue", f10);
    }

    @Override // r7.FloatPropertyHolder
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public float a(UIItem uIItem) {
        return this.f17585f;
    }

    @Override // r7.FloatPropertyHolder
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public void b(UIItem uIItem, float f10) {
        this.f17585f = f10;
    }

    @Override // r7.FloatPropertyHolder
    /* renamed from: i, reason: merged with bridge method [inline-methods] */
    public void e(UIItem uIItem) {
        d(uIItem, uIItem.f17608h.f17597a);
    }

    @Override // r7.FloatPropertyHolder
    /* renamed from: j, reason: merged with bridge method [inline-methods] */
    public void f(UIItem uIItem) {
        super.f(uIItem);
        uIItem.f17605e.f16270a = this.f17583d;
    }

    public j(String str, float f10) {
        this(str, f10, 1.0f);
    }

    public j(String str, float f10, float f11) {
        super(str, f11);
        this.f17585f = f10;
    }
}
