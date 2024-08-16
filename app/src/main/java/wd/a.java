package wd;

import kotlin.Metadata;
import ma.Unit;
import ma.q;

/* compiled from: Flow.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b'\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u00022\b\u0012\u0004\u0012\u00028\u00000\u0003B\u0007¢\u0006\u0004\b\n\u0010\u000bJ!\u0010\u0007\u001a\u00020\u00062\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0086@ø\u0001\u0000¢\u0006\u0004\b\u0007\u0010\bJ!\u0010\t\u001a\u00020\u00062\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H¦@ø\u0001\u0000¢\u0006\u0004\b\t\u0010\b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, d2 = {"Lwd/a;", "T", "Lwd/b;", "", "Lwd/c;", "collector", "Lma/f0;", "collect", "(Lwd/c;Lqa/d;)Ljava/lang/Object;", "a", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class a<T> implements b<T> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Flow.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    @sa.f(c = "kotlinx.coroutines.flow.AbstractFlow", f = "Flow.kt", l = {230}, m = "collect")
    /* renamed from: wd.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public static final class C0115a extends sa.d {

        /* renamed from: h, reason: collision with root package name */
        Object f19441h;

        /* renamed from: i, reason: collision with root package name */
        /* synthetic */ Object f19442i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ a<T> f19443j;

        /* renamed from: k, reason: collision with root package name */
        int f19444k;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C0115a(a<T> aVar, qa.d<? super C0115a> dVar) {
            super(dVar);
            this.f19443j = aVar;
        }

        @Override // sa.a
        public final Object invokeSuspend(Object obj) {
            this.f19442i = obj;
            this.f19444k |= Integer.MIN_VALUE;
            return this.f19443j.collect(null, this);
        }
    }

    public abstract Object a(FlowCollector<? super T> flowCollector, qa.d<? super Unit> dVar);

    /* JADX WARN: Removed duplicated region for block: B:21:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0023  */
    @Override // wd.b
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object collect(FlowCollector<? super T> flowCollector, qa.d<? super Unit> dVar) {
        C0115a c0115a;
        Object c10;
        int i10;
        xd.c cVar;
        if (dVar instanceof C0115a) {
            c0115a = (C0115a) dVar;
            int i11 = c0115a.f19444k;
            if ((i11 & Integer.MIN_VALUE) != 0) {
                c0115a.f19444k = i11 - Integer.MIN_VALUE;
                Object obj = c0115a.f19442i;
                c10 = ra.d.c();
                i10 = c0115a.f19444k;
                if (i10 != 0) {
                    q.b(obj);
                    xd.c cVar2 = new xd.c(flowCollector, c0115a.getContext());
                    try {
                        c0115a.f19441h = cVar2;
                        c0115a.f19444k = 1;
                        if (a(cVar2, c0115a) == c10) {
                            return c10;
                        }
                        cVar = cVar2;
                    } catch (Throwable th) {
                        th = th;
                        cVar = cVar2;
                        cVar.b();
                        throw th;
                    }
                } else {
                    if (i10 != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    cVar = (xd.c) c0115a.f19441h;
                    try {
                        q.b(obj);
                    } catch (Throwable th2) {
                        th = th2;
                        cVar.b();
                        throw th;
                    }
                }
                cVar.b();
                return Unit.f15173a;
            }
        }
        c0115a = new C0115a(this, dVar);
        Object obj2 = c0115a.f19442i;
        c10 = ra.d.c();
        i10 = c0115a.f19444k;
        if (i10 != 0) {
        }
        cVar.b();
        return Unit.f15173a;
    }
}
