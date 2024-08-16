package l;

/* compiled from: Pools.java */
/* loaded from: classes.dex */
class g<T> implements f<T> {

    /* renamed from: a, reason: collision with root package name */
    private final Object[] f14520a;

    /* renamed from: b, reason: collision with root package name */
    private int f14521b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(int i10) {
        if (i10 > 0) {
            this.f14520a = new Object[i10];
            return;
        }
        throw new IllegalArgumentException("The max pool size must be > 0");
    }

    @Override // l.f
    public boolean a(T t7) {
        int i10 = this.f14521b;
        Object[] objArr = this.f14520a;
        if (i10 >= objArr.length) {
            return false;
        }
        objArr[i10] = t7;
        this.f14521b = i10 + 1;
        return true;
    }

    @Override // l.f
    public T b() {
        int i10 = this.f14521b;
        if (i10 <= 0) {
            return null;
        }
        int i11 = i10 - 1;
        Object[] objArr = this.f14520a;
        T t7 = (T) objArr[i11];
        objArr[i11] = null;
        this.f14521b = i10 - 1;
        return t7;
    }

    @Override // l.f
    public void c(T[] tArr, int i10) {
        if (i10 > tArr.length) {
            i10 = tArr.length;
        }
        for (int i11 = 0; i11 < i10; i11++) {
            T t7 = tArr[i11];
            int i12 = this.f14521b;
            Object[] objArr = this.f14520a;
            if (i12 < objArr.length) {
                objArr[i12] = t7;
                this.f14521b = i12 + 1;
            }
        }
    }
}
