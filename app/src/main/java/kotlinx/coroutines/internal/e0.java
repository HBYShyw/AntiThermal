package kotlinx.coroutines.internal;

import java.util.Objects;
import kotlin.Metadata;
import qa.g;
import td.b2;
import za.Lambda;

/* compiled from: ThreadContext.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\u0000\u001a\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0001\u001a\u00020\u00002\b\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u001a\u0010\b\u001a\u00020\u00072\u0006\u0010\u0001\u001a\u00020\u00002\b\u0010\u0006\u001a\u0004\u0018\u00010\u0002H\u0000\"\u0014\u0010\u000b\u001a\u00020\t8\u0000X\u0081\u0004¢\u0006\u0006\n\u0004\b\b\u0010\n¨\u0006\f"}, d2 = {"Lqa/g;", "context", "", "b", "countOrElement", "c", "oldState", "Lma/f0;", "a", "Lkotlinx/coroutines/internal/a0;", "Lkotlinx/coroutines/internal/a0;", "NO_THREAD_ELEMENTS", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class e0 {

    /* renamed from: a, reason: collision with root package name */
    public static final Symbol f14355a = new Symbol("NO_THREAD_ELEMENTS");

    /* renamed from: b, reason: collision with root package name */
    private static final ya.p<Object, g.b, Object> f14356b = a.f14359e;

    /* renamed from: c, reason: collision with root package name */
    private static final ya.p<b2<?>, g.b, b2<?>> f14357c = b.f14360e;

    /* renamed from: d, reason: collision with root package name */
    private static final ya.p<h0, g.b, h0> f14358d = c.f14361e;

    /* compiled from: ThreadContext.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0004\u001a\u0004\u0018\u00010\u00002\b\u0010\u0001\u001a\u0004\u0018\u00010\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"", "countOrElement", "Lqa/g$b;", "element", "a", "(Ljava/lang/Object;Lqa/g$b;)Ljava/lang/Object;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.p<Object, g.b, Object> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f14359e = new a();

        a() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Object invoke(Object obj, g.b bVar) {
            if (!(bVar instanceof b2)) {
                return obj;
            }
            Integer num = obj instanceof Integer ? (Integer) obj : null;
            int intValue = num != null ? num.intValue() : 1;
            return intValue == 0 ? bVar : Integer.valueOf(intValue + 1);
        }
    }

    /* compiled from: ThreadContext.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0004\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00002\f\u0010\u0001\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"Ltd/b2;", "found", "Lqa/g$b;", "element", "a", "(Ltd/b2;Lqa/g$b;)Ltd/b2;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.p<b2<?>, g.b, b2<?>> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f14360e = new b();

        b() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final b2<?> invoke(b2<?> b2Var, g.b bVar) {
            if (b2Var != null) {
                return b2Var;
            }
            if (bVar instanceof b2) {
                return (b2) bVar;
            }
            return null;
        }
    }

    /* compiled from: ThreadContext.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"Lkotlinx/coroutines/internal/h0;", "state", "Lqa/g$b;", "element", "a", "(Lkotlinx/coroutines/internal/h0;Lqa/g$b;)Lkotlinx/coroutines/internal/h0;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.p<h0, g.b, h0> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f14361e = new c();

        c() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final h0 invoke(h0 h0Var, g.b bVar) {
            if (bVar instanceof b2) {
                b2<?> b2Var = (b2) bVar;
                h0Var.a(b2Var, b2Var.m(h0Var.f14367a));
            }
            return h0Var;
        }
    }

    public static final void a(qa.g gVar, Object obj) {
        if (obj == f14355a) {
            return;
        }
        if (obj instanceof h0) {
            ((h0) obj).b(gVar);
            return;
        }
        Object i02 = gVar.i0(null, f14357c);
        Objects.requireNonNull(i02, "null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
        ((b2) i02).S(gVar, obj);
    }

    public static final Object b(qa.g gVar) {
        Object i02 = gVar.i0(0, f14356b);
        za.k.b(i02);
        return i02;
    }

    public static final Object c(qa.g gVar, Object obj) {
        if (obj == null) {
            obj = b(gVar);
        }
        if (obj == 0) {
            return f14355a;
        }
        if (obj instanceof Integer) {
            return gVar.i0(new h0(gVar, ((Number) obj).intValue()), f14358d);
        }
        return ((b2) obj).m(gVar);
    }
}
