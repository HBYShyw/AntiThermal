package xd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import ma.Unit;
import qa.g;
import qa.h;
import sa.CoroutineStackFrame;
import sd.Indent;
import td.m1;
import wd.FlowCollector;
import ya.p;
import za.Lambda;
import za.k;

/* compiled from: SafeCollector.kt */
@Metadata(bv = {}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u00022\u00020\u00032\u00020\u0004B\u001d\u0012\f\u0010#\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010$\u001a\u00020\f¢\u0006\u0004\b%\u0010&J'\u0010\n\u001a\u0004\u0018\u00010\t2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\b\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\n\u0010\u000bJ)\u0010\u000f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\b\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u001a\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00112\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0002J\n\u0010\u0015\u001a\u0004\u0018\u00010\u0014H\u0016J\"\u0010\u0018\u001a\u00020\t2\u000e\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0016H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0019J\b\u0010\u001a\u001a\u00020\u0006H\u0016J\u001b\u0010\u001b\u001a\u00020\u00062\u0006\u0010\b\u001a\u00028\u0000H\u0096@ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001cR\u0016\u0010\u001f\u001a\u0004\u0018\u00010\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001eR\u0014\u0010\"\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b \u0010!\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006'"}, d2 = {"Lxd/c;", "T", "Lwd/c;", "Lsa/d;", "Lsa/e;", "Lqa/d;", "Lma/f0;", "uCont", ThermalBaseConfig.Item.ATTR_VALUE, "", "n", "(Lqa/d;Ljava/lang/Object;)Ljava/lang/Object;", "Lqa/g;", "currentContext", "previousContext", "c", "(Lqa/g;Lqa/g;Ljava/lang/Object;)V", "Lxd/a;", "exception", "o", "Ljava/lang/StackTraceElement;", "getStackTraceElement", "Lma/p;", "result", "invokeSuspend", "(Ljava/lang/Object;)Ljava/lang/Object;", "b", "emit", "(Ljava/lang/Object;Lqa/d;)Ljava/lang/Object;", "getCallerFrame", "()Lsa/e;", "callerFrame", "getContext", "()Lqa/g;", "context", "collector", "collectContext", "<init>", "(Lwd/c;Lqa/g;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class c<T> extends sa.d implements FlowCollector<T> {

    /* renamed from: h, reason: collision with root package name */
    public final FlowCollector<T> f19724h;

    /* renamed from: i, reason: collision with root package name */
    public final g f19725i;

    /* renamed from: j, reason: collision with root package name */
    public final int f19726j;

    /* renamed from: k, reason: collision with root package name */
    private g f19727k;

    /* renamed from: l, reason: collision with root package name */
    private qa.d<? super Unit> f19728l;

    /* compiled from: SafeCollector.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0005\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00002\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"T", "", "count", "Lqa/g$b;", "<anonymous parameter 1>", "a", "(ILqa/g$b;)Ljava/lang/Integer;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements p<Integer, g.b, Integer> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f19729e = new a();

        a() {
            super(2);
        }

        public final Integer a(int i10, g.b bVar) {
            return Integer.valueOf(i10 + 1);
        }

        @Override // ya.p
        public /* bridge */ /* synthetic */ Integer invoke(Integer num, g.b bVar) {
            return a(num.intValue(), bVar);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public c(FlowCollector<? super T> flowCollector, g gVar) {
        super(b.f19722e, h.f17173e);
        this.f19724h = flowCollector;
        this.f19725i = gVar;
        this.f19726j = ((Number) gVar.i0(0, a.f19729e)).intValue();
    }

    private final void c(g currentContext, g previousContext, T value) {
        if (previousContext instanceof xd.a) {
            o((xd.a) previousContext, value);
        }
        e.a(this, currentContext);
    }

    private final Object n(qa.d<? super Unit> uCont, T value) {
        Object c10;
        g f18758i = uCont.getF18758i();
        m1.e(f18758i);
        g gVar = this.f19727k;
        if (gVar != f18758i) {
            c(f18758i, gVar, value);
            this.f19727k = f18758i;
        }
        this.f19728l = uCont;
        Object g6 = d.a().g(this.f19724h, value, this);
        c10 = ra.d.c();
        if (!k.a(g6, c10)) {
            this.f19728l = null;
        }
        return g6;
    }

    private final void o(xd.a aVar, Object obj) {
        String f10;
        f10 = Indent.f("\n            Flow exception transparency is violated:\n                Previous 'emit' call has thrown exception " + aVar.f19720e + ", but then emission attempt of value '" + obj + "' has been detected.\n                Emissions from 'catch' blocks are prohibited in order to avoid unspecified behaviour, 'Flow.catch' operator can be used instead.\n                For a more detailed explanation, please refer to Flow documentation.\n            ");
        throw new IllegalStateException(f10.toString());
    }

    @Override // sa.d, sa.a
    public void b() {
        super.b();
    }

    @Override // wd.FlowCollector
    public Object emit(T t7, qa.d<? super Unit> dVar) {
        Object c10;
        Object c11;
        try {
            Object n10 = n(dVar, t7);
            c10 = ra.d.c();
            if (n10 == c10) {
                sa.h.c(dVar);
            }
            c11 = ra.d.c();
            return n10 == c11 ? n10 : Unit.f15173a;
        } catch (Throwable th) {
            this.f19727k = new xd.a(th, dVar.getF18758i());
            throw th;
        }
    }

    @Override // sa.a, sa.CoroutineStackFrame
    public CoroutineStackFrame getCallerFrame() {
        qa.d<? super Unit> dVar = this.f19728l;
        if (dVar instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) dVar;
        }
        return null;
    }

    @Override // sa.d, qa.d
    /* renamed from: getContext */
    public g getF18758i() {
        g gVar = this.f19727k;
        return gVar == null ? h.f17173e : gVar;
    }

    @Override // sa.a
    public StackTraceElement getStackTraceElement() {
        return null;
    }

    @Override // sa.a
    public Object invokeSuspend(Object result) {
        Object c10;
        Throwable b10 = ma.p.b(result);
        if (b10 != null) {
            this.f19727k = new xd.a(b10, getF18758i());
        }
        qa.d<? super Unit> dVar = this.f19728l;
        if (dVar != null) {
            dVar.resumeWith(result);
        }
        c10 = ra.d.c();
        return c10;
    }
}
