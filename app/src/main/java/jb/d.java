package jb;

/* compiled from: CacheByClass.kt */
/* loaded from: classes2.dex */
final class d<V> extends jb.a<V> {

    /* renamed from: a, reason: collision with root package name */
    private final ya.l<Class<?>, V> f13166a;

    /* renamed from: b, reason: collision with root package name */
    private volatile a f13167b;

    /* compiled from: CacheByClass.kt */
    /* loaded from: classes2.dex */
    public static final class a extends ClassValue<V> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ d<V> f13168a;

        a(d<V> dVar) {
            this.f13168a = dVar;
        }

        @Override // java.lang.ClassValue
        protected V computeValue(Class<?> cls) {
            za.k.e(cls, "type");
            return (V) ((d) this.f13168a).f13166a.invoke(cls);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public d(ya.l<? super Class<?>, ? extends V> lVar) {
        za.k.e(lVar, "compute");
        this.f13166a = lVar;
        this.f13167b = c();
    }

    private final a c() {
        return new a(this);
    }

    @Override // jb.a
    public V a(Class<?> cls) {
        za.k.e(cls, "key");
        return this.f13167b.get(cls);
    }
}
