package fd;

/* compiled from: SingleThreadValue.java */
/* renamed from: fd.l, reason: use source file name */
/* loaded from: classes2.dex */
class SingleThreadValue<T> {

    /* renamed from: a, reason: collision with root package name */
    private final T f11451a;

    /* renamed from: b, reason: collision with root package name */
    private final Thread f11452b = Thread.currentThread();

    /* JADX INFO: Access modifiers changed from: package-private */
    public SingleThreadValue(T t7) {
        this.f11451a = t7;
    }

    public T a() {
        if (b()) {
            return this.f11451a;
        }
        throw new IllegalStateException("No value in this thread (hasValue should be checked before)");
    }

    public boolean b() {
        return this.f11452b == Thread.currentThread();
    }
}
