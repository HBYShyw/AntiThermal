package rd;

import java.util.Iterator;
import kotlin.collections._Arrays;
import za.Lambda;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public class l extends k {

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class a<T> implements Sequence<T> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Iterator f17828a;

        public a(Iterator it) {
            this.f17828a = it;
        }

        @Override // rd.Sequence
        public Iterator<T> iterator() {
            return this.f17828a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class b<T> extends Lambda implements ya.l<Sequence<? extends T>, Iterator<? extends T>> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f17829e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Iterator<T> invoke(Sequence<? extends T> sequence) {
            za.k.e(sequence, "it");
            return sequence.iterator();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class c<T> extends Lambda implements ya.l<T, T> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f17830e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        public final T invoke(T t7) {
            return t7;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class d<T> extends Lambda implements ya.l<T, T> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ya.a<T> f17831e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        d(ya.a<? extends T> aVar) {
            super(1);
            this.f17831e = aVar;
        }

        @Override // ya.l
        public final T invoke(T t7) {
            za.k.e(t7, "it");
            return this.f17831e.invoke();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Sequences.kt */
    /* loaded from: classes2.dex */
    public static final class e<T> extends Lambda implements ya.a<T> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ T f17832e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(T t7) {
            super(0);
            this.f17832e = t7;
        }

        @Override // ya.a
        public final T invoke() {
            return this.f17832e;
        }
    }

    public static <T> Sequence<T> a(Iterator<? extends T> it) {
        za.k.e(it, "<this>");
        return b(new a(it));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Sequence<T> b(Sequence<? extends T> sequence) {
        za.k.e(sequence, "<this>");
        return sequence instanceof rd.a ? sequence : new rd.a(sequence);
    }

    public static <T> Sequence<T> c() {
        return rd.d.f17809a;
    }

    public static final <T> Sequence<T> d(Sequence<? extends Sequence<? extends T>> sequence) {
        za.k.e(sequence, "<this>");
        return e(sequence, b.f17829e);
    }

    private static final <T, R> Sequence<R> e(Sequence<? extends T> sequence, ya.l<? super T, ? extends Iterator<? extends R>> lVar) {
        if (sequence instanceof p) {
            return ((p) sequence).d(lVar);
        }
        return new f(sequence, c.f17830e, lVar);
    }

    public static <T> Sequence<T> f(T t7, ya.l<? super T, ? extends T> lVar) {
        za.k.e(lVar, "nextFunction");
        if (t7 == null) {
            return rd.d.f17809a;
        }
        return new g(new e(t7), lVar);
    }

    public static <T> Sequence<T> g(ya.a<? extends T> aVar) {
        za.k.e(aVar, "nextFunction");
        return b(new g(aVar, new d(aVar)));
    }

    public static final <T> Sequence<T> h(T... tArr) {
        Sequence<T> r10;
        Sequence<T> c10;
        za.k.e(tArr, "elements");
        if (tArr.length == 0) {
            c10 = c();
            return c10;
        }
        r10 = _Arrays.r(tArr);
        return r10;
    }
}
