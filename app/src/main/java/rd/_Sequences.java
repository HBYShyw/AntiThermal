package rd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.collections._Collections;
import kotlin.collections.r;
import sd.Appendable;
import za.FunctionReferenceImpl;
import za.Lambda;

/* compiled from: _Sequences.kt */
/* renamed from: rd.n */
/* loaded from: classes2.dex */
public class _Sequences extends _SequencesJvm {

    /* compiled from: Iterables.kt */
    /* renamed from: rd.n$a */
    /* loaded from: classes2.dex */
    public static final class a<T> implements Iterable<T>, ab.a {

        /* renamed from: e */
        final /* synthetic */ Sequence f17833e;

        public a(Sequence sequence) {
            this.f17833e = sequence;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return this.f17833e.iterator();
        }
    }

    /* compiled from: _Sequences.kt */
    /* renamed from: rd.n$b */
    /* loaded from: classes2.dex */
    public static final class b<T> extends Lambda implements ya.l<T, Boolean> {

        /* renamed from: e */
        public static final b f17834e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a */
        public final Boolean invoke(T t7) {
            return Boolean.valueOf(t7 == null);
        }
    }

    /* compiled from: _Sequences.kt */
    /* renamed from: rd.n$c */
    /* loaded from: classes2.dex */
    public /* synthetic */ class c<R> extends FunctionReferenceImpl implements ya.l<Sequence<? extends R>, Iterator<? extends R>> {

        /* renamed from: n */
        public static final c f17835n = new c();

        c() {
            super(1, Sequence.class, "iterator", "iterator()Ljava/util/Iterator;", 0);
        }

        @Override // ya.l
        /* renamed from: G */
        public final Iterator<R> invoke(Sequence<? extends R> sequence) {
            za.k.e(sequence, "p0");
            return sequence.iterator();
        }
    }

    public static <T> Sequence<T> A(Sequence<? extends T> sequence, ya.l<? super T, Boolean> lVar) {
        za.k.e(sequence, "<this>");
        za.k.e(lVar, "predicate");
        return new o(sequence, lVar);
    }

    public static final <T, C extends Collection<? super T>> C B(Sequence<? extends T> sequence, C c10) {
        za.k.e(sequence, "<this>");
        za.k.e(c10, "destination");
        Iterator<? extends T> it = sequence.iterator();
        while (it.hasNext()) {
            c10.add(it.next());
        }
        return c10;
    }

    public static <T> List<T> C(Sequence<? extends T> sequence) {
        List D;
        List<T> q10;
        za.k.e(sequence, "<this>");
        D = D(sequence);
        q10 = r.q(D);
        return q10;
    }

    public static <T> List<T> D(Sequence<? extends T> sequence) {
        za.k.e(sequence, "<this>");
        return (List) B(sequence, new ArrayList());
    }

    public static <T> Iterable<T> i(Sequence<? extends T> sequence) {
        za.k.e(sequence, "<this>");
        return new a(sequence);
    }

    public static <T> boolean j(Sequence<? extends T> sequence, T t7) {
        za.k.e(sequence, "<this>");
        return r(sequence, t7) >= 0;
    }

    public static <T> int k(Sequence<? extends T> sequence) {
        za.k.e(sequence, "<this>");
        Iterator<? extends T> it = sequence.iterator();
        int i10 = 0;
        while (it.hasNext()) {
            it.next();
            i10++;
            if (i10 < 0) {
                r.s();
            }
        }
        return i10;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Sequence<T> l(Sequence<? extends T> sequence, int i10) {
        za.k.e(sequence, "<this>");
        if (i10 >= 0) {
            return i10 == 0 ? sequence : sequence instanceof rd.c ? ((rd.c) sequence).a(i10) : new rd.b(sequence, i10);
        }
        throw new IllegalArgumentException(("Requested element count " + i10 + " is less than zero.").toString());
    }

    public static <T> Sequence<T> m(Sequence<? extends T> sequence, ya.l<? super T, Boolean> lVar) {
        za.k.e(sequence, "<this>");
        za.k.e(lVar, "predicate");
        return new e(sequence, true, lVar);
    }

    public static <T> Sequence<T> n(Sequence<? extends T> sequence, ya.l<? super T, Boolean> lVar) {
        za.k.e(sequence, "<this>");
        za.k.e(lVar, "predicate");
        return new e(sequence, false, lVar);
    }

    public static <T> Sequence<T> o(Sequence<? extends T> sequence) {
        Sequence<T> n10;
        za.k.e(sequence, "<this>");
        n10 = n(sequence, b.f17834e);
        za.k.c(n10, "null cannot be cast to non-null type kotlin.sequences.Sequence<T of kotlin.sequences.SequencesKt___SequencesKt.filterNotNull>");
        return n10;
    }

    public static <T> T p(Sequence<? extends T> sequence) {
        za.k.e(sequence, "<this>");
        Iterator<? extends T> it = sequence.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public static <T, R> Sequence<R> q(Sequence<? extends T> sequence, ya.l<? super T, ? extends Sequence<? extends R>> lVar) {
        za.k.e(sequence, "<this>");
        za.k.e(lVar, "transform");
        return new f(sequence, lVar, c.f17835n);
    }

    public static final <T> int r(Sequence<? extends T> sequence, T t7) {
        za.k.e(sequence, "<this>");
        int i10 = 0;
        for (T t10 : sequence) {
            if (i10 < 0) {
                r.t();
            }
            if (za.k.a(t7, t10)) {
                return i10;
            }
            i10++;
        }
        return -1;
    }

    public static final <T, A extends Appendable> A s(Sequence<? extends T> sequence, A a10, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l<? super T, ? extends CharSequence> lVar) {
        za.k.e(sequence, "<this>");
        za.k.e(a10, "buffer");
        za.k.e(charSequence, "separator");
        za.k.e(charSequence2, "prefix");
        za.k.e(charSequence3, "postfix");
        za.k.e(charSequence4, "truncated");
        a10.append(charSequence2);
        int i11 = 0;
        for (T t7 : sequence) {
            i11++;
            if (i11 > 1) {
                a10.append(charSequence);
            }
            if (i10 >= 0 && i11 > i10) {
                break;
            }
            Appendable.a(a10, t7, lVar);
        }
        if (i10 >= 0 && i11 > i10) {
            a10.append(charSequence4);
        }
        a10.append(charSequence3);
        return a10;
    }

    public static final <T> String t(Sequence<? extends T> sequence, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l<? super T, ? extends CharSequence> lVar) {
        za.k.e(sequence, "<this>");
        za.k.e(charSequence, "separator");
        za.k.e(charSequence2, "prefix");
        za.k.e(charSequence3, "postfix");
        za.k.e(charSequence4, "truncated");
        String sb2 = ((StringBuilder) s(sequence, new StringBuilder(), charSequence, charSequence2, charSequence3, i10, charSequence4, lVar)).toString();
        za.k.d(sb2, "joinTo(StringBuilder(), …ed, transform).toString()");
        return sb2;
    }

    public static /* synthetic */ String u(Sequence sequence, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l lVar, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            charSequence = ", ";
        }
        CharSequence charSequence5 = (i11 & 2) != 0 ? "" : charSequence2;
        CharSequence charSequence6 = (i11 & 4) == 0 ? charSequence3 : "";
        if ((i11 & 8) != 0) {
            i10 = -1;
        }
        int i12 = i10;
        if ((i11 & 16) != 0) {
            charSequence4 = "...";
        }
        CharSequence charSequence7 = charSequence4;
        if ((i11 & 32) != 0) {
            lVar = null;
        }
        return t(sequence, charSequence, charSequence5, charSequence6, i12, charSequence7, lVar);
    }

    public static <T> T v(Sequence<? extends T> sequence) {
        za.k.e(sequence, "<this>");
        Iterator<? extends T> it = sequence.iterator();
        if (it.hasNext()) {
            T next = it.next();
            while (it.hasNext()) {
                next = it.next();
            }
            return next;
        }
        throw new NoSuchElementException("Sequence is empty.");
    }

    public static <T, R> Sequence<R> w(Sequence<? extends T> sequence, ya.l<? super T, ? extends R> lVar) {
        za.k.e(sequence, "<this>");
        za.k.e(lVar, "transform");
        return new p(sequence, lVar);
    }

    public static <T, R> Sequence<R> x(Sequence<? extends T> sequence, ya.l<? super T, ? extends R> lVar) {
        Sequence<R> o10;
        za.k.e(sequence, "<this>");
        za.k.e(lVar, "transform");
        o10 = o(new p(sequence, lVar));
        return o10;
    }

    public static <T> Sequence<T> y(Sequence<? extends T> sequence, Iterable<? extends T> iterable) {
        Sequence K;
        za.k.e(sequence, "<this>");
        za.k.e(iterable, "elements");
        K = _Collections.K(iterable);
        return l.d(l.h(sequence, K));
    }

    public static <T> Sequence<T> z(Sequence<? extends T> sequence, T t7) {
        za.k.e(sequence, "<this>");
        return l.d(l.h(sequence, l.h(t7)));
    }
}
