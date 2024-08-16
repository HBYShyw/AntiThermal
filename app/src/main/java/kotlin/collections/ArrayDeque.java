package kotlin.collections;

import za.DefaultConstructorMarker;

/* compiled from: ArrayDeque.kt */
/* renamed from: kotlin.collections.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class ArrayDeque<E> extends AbstractMutableList<E> {

    /* renamed from: e, reason: collision with root package name */
    public static final a f14327e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final Object[] f14328f = new Object[0];

    /* compiled from: ArrayDeque.kt */
    /* renamed from: kotlin.collections.i$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int a(int i10, int i11) {
            int i12 = i10 + (i10 >> 1);
            if (i12 - i11 < 0) {
                i12 = i11;
            }
            if (i12 - 2147483639 > 0) {
                return i11 > 2147483639 ? Integer.MAX_VALUE : 2147483639;
            }
            return i12;
        }
    }
}
