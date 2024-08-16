package td;

import kotlin.Metadata;
import qa.ContinuationInterceptor;
import qa.g;
import sa.CoroutineStackFrame;
import za.Lambda;

/* compiled from: CoroutineContext.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u001a\u0014\u0010\u0003\u001a\u00020\u0001*\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0001H\u0007\u001a\u0014\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0007\u001a\f\u0010\u0007\u001a\u00020\u0006*\u00020\u0001H\u0002\u001a \u0010\u000b\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u0006H\u0002\u001a(\u0010\u0010\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u000f*\u0006\u0012\u0002\b\u00030\f2\u0006\u0010\u0002\u001a\u00020\u00012\b\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0000\u001a\u0013\u0010\u0012\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u000f*\u00020\u0011H\u0080\u0010\"\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u0013*\u00020\u00018@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u0017"}, d2 = {"Ltd/h0;", "Lqa/g;", "context", "e", "addedContext", "d", "", "c", "originalContext", "appendContext", "isNewCoroutine", "a", "Lqa/d;", "", "oldValue", "Ltd/e2;", "g", "Lsa/e;", "f", "", "b", "(Lqa/g;)Ljava/lang/String;", "coroutineName", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class b0 {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CoroutineContext.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"Lqa/g;", "result", "Lqa/g$b;", "element", "a", "(Lqa/g;Lqa/g$b;)Lqa/g;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.p<qa.g, g.b, qa.g> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f18718e = new a();

        a() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final qa.g invoke(qa.g gVar, g.b bVar) {
            if (bVar instanceof a0) {
                return gVar.o0(((a0) bVar).X());
            }
            return gVar.o0(bVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CoroutineContext.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"Lqa/g;", "result", "Lqa/g$b;", "element", "a", "(Lqa/g;Lqa/g$b;)Lqa/g;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.p<qa.g, g.b, qa.g> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ za.y<qa.g> f18719e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ boolean f18720f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(za.y<qa.g> yVar, boolean z10) {
            super(2);
            this.f18719e = yVar;
            this.f18720f = z10;
        }

        /* JADX WARN: Type inference failed for: r1v3, types: [T, qa.g] */
        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final qa.g invoke(qa.g gVar, g.b bVar) {
            if (!(bVar instanceof a0)) {
                return gVar.o0(bVar);
            }
            g.b c10 = this.f18719e.f20376e.c(bVar.getKey());
            if (c10 == null) {
                return gVar.o0(this.f18720f ? ((a0) bVar).X() : (a0) bVar);
            }
            za.y<qa.g> yVar = this.f18719e;
            yVar.f20376e = yVar.f20376e.j0(bVar.getKey());
            return gVar.o0(((a0) bVar).a0(c10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CoroutineContext.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"", "result", "Lqa/g$b;", "it", "a", "(ZLqa/g$b;)Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.p<Boolean, g.b, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f18721e = new c();

        c() {
            super(2);
        }

        public final Boolean a(boolean z10, g.b bVar) {
            return Boolean.valueOf(z10 || (bVar instanceof a0));
        }

        @Override // ya.p
        public /* bridge */ /* synthetic */ Boolean invoke(Boolean bool, g.b bVar) {
            return a(bool.booleanValue(), bVar);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v4, types: [T, java.lang.Object] */
    private static final qa.g a(qa.g gVar, qa.g gVar2, boolean z10) {
        boolean c10 = c(gVar);
        boolean c11 = c(gVar2);
        if (!c10 && !c11) {
            return gVar.o0(gVar2);
        }
        za.y yVar = new za.y();
        yVar.f20376e = gVar2;
        qa.h hVar = qa.h.f17173e;
        qa.g gVar3 = (qa.g) gVar.i0(hVar, new b(yVar, z10));
        if (c11) {
            yVar.f20376e = ((qa.g) yVar.f20376e).i0(hVar, a.f18718e);
        }
        return gVar3.o0((qa.g) yVar.f20376e);
    }

    public static final String b(qa.g gVar) {
        return null;
    }

    private static final boolean c(qa.g gVar) {
        return ((Boolean) gVar.i0(Boolean.FALSE, c.f18721e)).booleanValue();
    }

    public static final qa.g d(qa.g gVar, qa.g gVar2) {
        return !c(gVar2) ? gVar.o0(gVar2) : a(gVar, gVar2, false);
    }

    public static final qa.g e(h0 h0Var, qa.g gVar) {
        qa.g a10 = a(h0Var.getF14349e(), gVar, true);
        return (a10 == Dispatchers.a() || a10.c(ContinuationInterceptor.f17170a) != null) ? a10 : a10.o0(Dispatchers.a());
    }

    public static final e2<?> f(CoroutineStackFrame coroutineStackFrame) {
        while (!(coroutineStackFrame instanceof q0) && (coroutineStackFrame = coroutineStackFrame.getCallerFrame()) != null) {
            if (coroutineStackFrame instanceof e2) {
                return (e2) coroutineStackFrame;
            }
        }
        return null;
    }

    public static final e2<?> g(qa.d<?> dVar, qa.g gVar, Object obj) {
        if (!(dVar instanceof CoroutineStackFrame)) {
            return null;
        }
        if (!(gVar.c(f2.f18738e) != null)) {
            return null;
        }
        e2<?> f10 = f((CoroutineStackFrame) dVar);
        if (f10 != null) {
            f10.F0(gVar, obj);
        }
        return f10;
    }
}
