package jb;

import java.lang.ref.WeakReference;

/* compiled from: moduleByClassLoader.kt */
/* loaded from: classes2.dex */
final class p0 {

    /* renamed from: a, reason: collision with root package name */
    private final WeakReference<ClassLoader> f13318a;

    /* renamed from: b, reason: collision with root package name */
    private final int f13319b;

    /* renamed from: c, reason: collision with root package name */
    private ClassLoader f13320c;

    public p0(ClassLoader classLoader) {
        za.k.e(classLoader, "classLoader");
        this.f13318a = new WeakReference<>(classLoader);
        this.f13319b = System.identityHashCode(classLoader);
        this.f13320c = classLoader;
    }

    public final void a(ClassLoader classLoader) {
        this.f13320c = classLoader;
    }

    public boolean equals(Object obj) {
        return (obj instanceof p0) && this.f13318a.get() == ((p0) obj).f13318a.get();
    }

    public int hashCode() {
        return this.f13319b;
    }

    public String toString() {
        String classLoader;
        ClassLoader classLoader2 = this.f13318a.get();
        return (classLoader2 == null || (classLoader = classLoader2.toString()) == null) ? "<null>" : classLoader;
    }
}
