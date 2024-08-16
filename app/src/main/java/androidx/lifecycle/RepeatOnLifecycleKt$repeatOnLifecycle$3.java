package androidx.lifecycle;

import androidx.lifecycle.h;
import kotlin.Metadata;
import ma.Unit;
import ma.p;
import ra.IntrinsicsJvm;
import td.Dispatchers;
import td.MainCoroutineDispatcher;
import td.i1;

/* compiled from: RepeatOnLifecycle.kt */
@Metadata(bv = {}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Ltd/h0;", "Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 6, 0})
@sa.f(c = "androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3", f = "RepeatOnLifecycle.kt", l = {84}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class RepeatOnLifecycleKt$repeatOnLifecycle$3 extends sa.k implements ya.p<td.h0, qa.d<? super Unit>, Object> {

    /* renamed from: i, reason: collision with root package name */
    int f3107i;

    /* renamed from: j, reason: collision with root package name */
    private /* synthetic */ Object f3108j;

    /* renamed from: k, reason: collision with root package name */
    final /* synthetic */ h f3109k;

    /* renamed from: l, reason: collision with root package name */
    final /* synthetic */ h.c f3110l;

    /* renamed from: m, reason: collision with root package name */
    final /* synthetic */ ya.p<td.h0, qa.d<? super Unit>, Object> f3111m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: RepeatOnLifecycle.kt */
    @Metadata(bv = {}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Ltd/h0;", "Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 6, 0})
    @sa.f(c = "androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1", f = "RepeatOnLifecycle.kt", l = {166}, m = "invokeSuspend")
    /* renamed from: androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends sa.k implements ya.p<td.h0, qa.d<? super Unit>, Object> {

        /* renamed from: i, reason: collision with root package name */
        Object f3112i;

        /* renamed from: j, reason: collision with root package name */
        Object f3113j;

        /* renamed from: k, reason: collision with root package name */
        Object f3114k;

        /* renamed from: l, reason: collision with root package name */
        Object f3115l;

        /* renamed from: m, reason: collision with root package name */
        Object f3116m;

        /* renamed from: n, reason: collision with root package name */
        Object f3117n;

        /* renamed from: o, reason: collision with root package name */
        int f3118o;

        /* renamed from: p, reason: collision with root package name */
        final /* synthetic */ h f3119p;

        /* renamed from: q, reason: collision with root package name */
        final /* synthetic */ h.c f3120q;

        /* renamed from: r, reason: collision with root package name */
        final /* synthetic */ td.h0 f3121r;

        /* renamed from: s, reason: collision with root package name */
        final /* synthetic */ ya.p<td.h0, qa.d<? super Unit>, Object> f3122s;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass1(h hVar, h.c cVar, td.h0 h0Var, ya.p<? super td.h0, ? super qa.d<? super Unit>, ? extends Object> pVar, qa.d<? super AnonymousClass1> dVar) {
            super(2, dVar);
            this.f3119p = hVar;
            this.f3120q = cVar;
            this.f3121r = h0Var;
            this.f3122s = pVar;
        }

        @Override // sa.a
        public final qa.d<Unit> create(Object obj, qa.d<?> dVar) {
            return new AnonymousClass1(this.f3119p, this.f3120q, this.f3121r, this.f3122s, dVar);
        }

        @Override // ya.p
        public final Object invoke(td.h0 h0Var, qa.d<? super Unit> dVar) {
            return ((AnonymousClass1) create(h0Var, dVar)).invokeSuspend(Unit.f15173a);
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x00c5  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x00cf  */
        /* JADX WARN: Type inference failed for: r10v0, types: [androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1, T] */
        @Override // sa.a
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) {
            Object c10;
            za.y yVar;
            za.y yVar2;
            qa.d b10;
            Object c11;
            i1 i1Var;
            LifecycleEventObserver lifecycleEventObserver;
            c10 = ra.d.c();
            int i10 = this.f3118o;
            if (i10 == 0) {
                ma.q.b(obj);
                if (this.f3119p.b() == h.c.DESTROYED) {
                    return Unit.f15173a;
                }
                final za.y yVar3 = new za.y();
                za.y yVar4 = new za.y();
                try {
                    h.c cVar = this.f3120q;
                    h hVar = this.f3119p;
                    final td.h0 h0Var = this.f3121r;
                    final ya.p<td.h0, qa.d<? super Unit>, Object> pVar = this.f3122s;
                    this.f3112i = yVar3;
                    this.f3113j = yVar4;
                    this.f3114k = cVar;
                    this.f3115l = hVar;
                    this.f3116m = h0Var;
                    this.f3117n = pVar;
                    this.f3118o = 1;
                    b10 = IntrinsicsJvm.b(this);
                    final td.l lVar = new td.l(b10, 1);
                    lVar.z();
                    final h.b d10 = h.b.d(cVar);
                    final h.b a10 = h.b.a(cVar);
                    final kotlinx.coroutines.sync.b b11 = kotlinx.coroutines.sync.d.b(false, 1, null);
                    ?? r10 = new LifecycleEventObserver() { // from class: androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1

                        /* compiled from: RepeatOnLifecycle.kt */
                        @Metadata(bv = {}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Ltd/h0;", "Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 6, 0})
                        @sa.f(c = "androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1", f = "RepeatOnLifecycle.kt", l = {171, 110}, m = "invokeSuspend")
                        /* loaded from: classes.dex */
                        static final class a extends sa.k implements ya.p<td.h0, qa.d<? super Unit>, Object> {

                            /* renamed from: i, reason: collision with root package name */
                            Object f3130i;

                            /* renamed from: j, reason: collision with root package name */
                            Object f3131j;

                            /* renamed from: k, reason: collision with root package name */
                            int f3132k;

                            /* renamed from: l, reason: collision with root package name */
                            final /* synthetic */ kotlinx.coroutines.sync.b f3133l;

                            /* renamed from: m, reason: collision with root package name */
                            final /* synthetic */ ya.p<td.h0, qa.d<? super Unit>, Object> f3134m;

                            /* JADX INFO: Access modifiers changed from: package-private */
                            /* compiled from: RepeatOnLifecycle.kt */
                            @Metadata(bv = {}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Ltd/h0;", "Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 6, 0})
                            @sa.f(c = "androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1", f = "RepeatOnLifecycle.kt", l = {111}, m = "invokeSuspend")
                            /* renamed from: androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$a$a, reason: collision with other inner class name */
                            /* loaded from: classes.dex */
                            public static final class C0004a extends sa.k implements ya.p<td.h0, qa.d<? super Unit>, Object> {

                                /* renamed from: i, reason: collision with root package name */
                                int f3135i;

                                /* renamed from: j, reason: collision with root package name */
                                private /* synthetic */ Object f3136j;

                                /* renamed from: k, reason: collision with root package name */
                                final /* synthetic */ ya.p<td.h0, qa.d<? super Unit>, Object> f3137k;

                                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                                /* JADX WARN: Multi-variable type inference failed */
                                C0004a(ya.p<? super td.h0, ? super qa.d<? super Unit>, ? extends Object> pVar, qa.d<? super C0004a> dVar) {
                                    super(2, dVar);
                                    this.f3137k = pVar;
                                }

                                @Override // sa.a
                                public final qa.d<Unit> create(Object obj, qa.d<?> dVar) {
                                    C0004a c0004a = new C0004a(this.f3137k, dVar);
                                    c0004a.f3136j = obj;
                                    return c0004a;
                                }

                                @Override // ya.p
                                public final Object invoke(td.h0 h0Var, qa.d<? super Unit> dVar) {
                                    return ((C0004a) create(h0Var, dVar)).invokeSuspend(Unit.f15173a);
                                }

                                @Override // sa.a
                                public final Object invokeSuspend(Object obj) {
                                    Object c10;
                                    c10 = ra.d.c();
                                    int i10 = this.f3135i;
                                    if (i10 == 0) {
                                        ma.q.b(obj);
                                        td.h0 h0Var = (td.h0) this.f3136j;
                                        ya.p<td.h0, qa.d<? super Unit>, Object> pVar = this.f3137k;
                                        this.f3135i = 1;
                                        if (pVar.invoke(h0Var, this) == c10) {
                                            return c10;
                                        }
                                    } else {
                                        if (i10 != 1) {
                                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                        }
                                        ma.q.b(obj);
                                    }
                                    return Unit.f15173a;
                                }
                            }

                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            /* JADX WARN: Multi-variable type inference failed */
                            a(kotlinx.coroutines.sync.b bVar, ya.p<? super td.h0, ? super qa.d<? super Unit>, ? extends Object> pVar, qa.d<? super a> dVar) {
                                super(2, dVar);
                                this.f3133l = bVar;
                                this.f3134m = pVar;
                            }

                            @Override // sa.a
                            public final qa.d<Unit> create(Object obj, qa.d<?> dVar) {
                                return new a(this.f3133l, this.f3134m, dVar);
                            }

                            @Override // ya.p
                            public final Object invoke(td.h0 h0Var, qa.d<? super Unit> dVar) {
                                return ((a) create(h0Var, dVar)).invokeSuspend(Unit.f15173a);
                            }

                            @Override // sa.a
                            public final Object invokeSuspend(Object obj) {
                                Object c10;
                                kotlinx.coroutines.sync.b bVar;
                                ya.p<td.h0, qa.d<? super Unit>, Object> pVar;
                                Throwable th;
                                kotlinx.coroutines.sync.b bVar2;
                                c10 = ra.d.c();
                                int i10 = this.f3132k;
                                try {
                                    if (i10 == 0) {
                                        ma.q.b(obj);
                                        bVar = this.f3133l;
                                        pVar = this.f3134m;
                                        this.f3130i = bVar;
                                        this.f3131j = pVar;
                                        this.f3132k = 1;
                                        if (bVar.b(null, this) == c10) {
                                            return c10;
                                        }
                                    } else {
                                        if (i10 != 1) {
                                            if (i10 != 2) {
                                                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                            }
                                            bVar2 = (kotlinx.coroutines.sync.b) this.f3130i;
                                            try {
                                                ma.q.b(obj);
                                                Unit unit = Unit.f15173a;
                                                bVar2.a(null);
                                                return unit;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                bVar2.a(null);
                                                throw th;
                                            }
                                        }
                                        pVar = (ya.p) this.f3131j;
                                        kotlinx.coroutines.sync.b bVar3 = (kotlinx.coroutines.sync.b) this.f3130i;
                                        ma.q.b(obj);
                                        bVar = bVar3;
                                    }
                                    C0004a c0004a = new C0004a(pVar, null);
                                    this.f3130i = bVar;
                                    this.f3131j = null;
                                    this.f3132k = 2;
                                    if (td.i0.b(c0004a, this) == c10) {
                                        return c10;
                                    }
                                    bVar2 = bVar;
                                    Unit unit2 = Unit.f15173a;
                                    bVar2.a(null);
                                    return unit2;
                                } catch (Throwable th3) {
                                    kotlinx.coroutines.sync.b bVar4 = bVar;
                                    th = th3;
                                    bVar2 = bVar4;
                                    bVar2.a(null);
                                    throw th;
                                }
                            }
                        }

                        /* JADX WARN: Type inference failed for: r7v3, types: [T, td.i1] */
                        @Override // androidx.lifecycle.LifecycleEventObserver
                        public final void a(o oVar, h.b bVar) {
                            ?? b12;
                            za.k.e(oVar, "<anonymous parameter 0>");
                            za.k.e(bVar, "event");
                            if (bVar == h.b.this) {
                                za.y<i1> yVar5 = yVar3;
                                b12 = td.h.b(h0Var, null, null, new a(b11, pVar, null), 3, null);
                                yVar5.f20376e = b12;
                                return;
                            }
                            if (bVar == a10) {
                                i1 i1Var2 = yVar3.f20376e;
                                if (i1Var2 != null) {
                                    i1.a.a(i1Var2, null, 1, null);
                                }
                                yVar3.f20376e = null;
                            }
                            if (bVar == h.b.ON_DESTROY) {
                                td.k<Unit> kVar = lVar;
                                p.a aVar = ma.p.f15184e;
                                kVar.resumeWith(ma.p.a(Unit.f15173a));
                            }
                        }
                    };
                    yVar4.f20376e = r10;
                    hVar.a((LifecycleEventObserver) r10);
                    Object w10 = lVar.w();
                    c11 = ra.d.c();
                    if (w10 == c11) {
                        sa.h.c(this);
                    }
                    if (w10 == c10) {
                        return c10;
                    }
                    yVar = yVar3;
                    yVar2 = yVar4;
                } catch (Throwable th) {
                    th = th;
                    yVar = yVar3;
                    yVar2 = yVar4;
                    i1Var = (i1) yVar.f20376e;
                    if (i1Var != null) {
                        i1.a.a(i1Var, null, 1, null);
                    }
                    lifecycleEventObserver = (LifecycleEventObserver) yVar2.f20376e;
                    if (lifecycleEventObserver != null) {
                        this.f3119p.c(lifecycleEventObserver);
                    }
                    throw th;
                }
            } else {
                if (i10 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                yVar2 = (za.y) this.f3113j;
                yVar = (za.y) this.f3112i;
                try {
                    ma.q.b(obj);
                } catch (Throwable th2) {
                    th = th2;
                    i1Var = (i1) yVar.f20376e;
                    if (i1Var != null) {
                    }
                    lifecycleEventObserver = (LifecycleEventObserver) yVar2.f20376e;
                    if (lifecycleEventObserver != null) {
                    }
                    throw th;
                }
            }
            i1 i1Var2 = (i1) yVar.f20376e;
            if (i1Var2 != null) {
                i1.a.a(i1Var2, null, 1, null);
            }
            LifecycleEventObserver lifecycleEventObserver2 = (LifecycleEventObserver) yVar2.f20376e;
            if (lifecycleEventObserver2 != null) {
                this.f3119p.c(lifecycleEventObserver2);
            }
            return Unit.f15173a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public RepeatOnLifecycleKt$repeatOnLifecycle$3(h hVar, h.c cVar, ya.p<? super td.h0, ? super qa.d<? super Unit>, ? extends Object> pVar, qa.d<? super RepeatOnLifecycleKt$repeatOnLifecycle$3> dVar) {
        super(2, dVar);
        this.f3109k = hVar;
        this.f3110l = cVar;
        this.f3111m = pVar;
    }

    @Override // sa.a
    public final qa.d<Unit> create(Object obj, qa.d<?> dVar) {
        RepeatOnLifecycleKt$repeatOnLifecycle$3 repeatOnLifecycleKt$repeatOnLifecycle$3 = new RepeatOnLifecycleKt$repeatOnLifecycle$3(this.f3109k, this.f3110l, this.f3111m, dVar);
        repeatOnLifecycleKt$repeatOnLifecycle$3.f3108j = obj;
        return repeatOnLifecycleKt$repeatOnLifecycle$3;
    }

    @Override // ya.p
    public final Object invoke(td.h0 h0Var, qa.d<? super Unit> dVar) {
        return ((RepeatOnLifecycleKt$repeatOnLifecycle$3) create(h0Var, dVar)).invokeSuspend(Unit.f15173a);
    }

    @Override // sa.a
    public final Object invokeSuspend(Object obj) {
        Object c10;
        c10 = ra.d.c();
        int i10 = this.f3107i;
        if (i10 == 0) {
            ma.q.b(obj);
            td.h0 h0Var = (td.h0) this.f3108j;
            MainCoroutineDispatcher f19019j = Dispatchers.c().getF19019j();
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.f3109k, this.f3110l, h0Var, this.f3111m, null);
            this.f3107i = 1;
            if (td.g.c(f19019j, anonymousClass1, this) == c10) {
                return c10;
            }
        } else {
            if (i10 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ma.q.b(obj);
        }
        return Unit.f15173a;
    }
}
