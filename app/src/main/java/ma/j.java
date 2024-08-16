package ma;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LazyJVM.kt */
/* loaded from: classes2.dex */
public class j {

    /* compiled from: LazyJVM.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f15177a;

        static {
            int[] iArr = new int[l.values().length];
            try {
                iArr[l.SYNCHRONIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[l.PUBLICATION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[l.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f15177a = iArr;
        }
    }

    public static <T> h<T> a(l lVar, ya.a<? extends T> aVar) {
        za.k.e(lVar, "mode");
        za.k.e(aVar, "initializer");
        int i10 = a.f15177a[lVar.ordinal()];
        if (i10 == 1) {
            return new s(aVar, null, 2, null);
        }
        if (i10 == 2) {
            return new r(aVar);
        }
        if (i10 == 3) {
            return new g0(aVar);
        }
        throw new NoWhenBranchMatchedException();
    }

    public static <T> h<T> b(ya.a<? extends T> aVar) {
        za.k.e(aVar, "initializer");
        return new s(aVar, null, 2, null);
    }
}
