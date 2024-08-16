package ra;

import ma.Unit;
import ma.q;
import qa.g;
import sa.h;
import sa.j;
import ya.p;
import za.TypeIntrinsics;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: IntrinsicsJvm.kt */
/* renamed from: ra.c, reason: use source file name */
/* loaded from: classes2.dex */
public class IntrinsicsJvm {

    /* compiled from: IntrinsicsJvm.kt */
    /* renamed from: ra.c$a */
    /* loaded from: classes2.dex */
    public static final class a extends j {

        /* renamed from: f, reason: collision with root package name */
        private int f17682f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ p f17683g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ Object f17684h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(qa.d dVar, p pVar, Object obj) {
            super(dVar);
            this.f17683g = pVar;
            this.f17684h = obj;
            k.c(dVar, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        }

        @Override // sa.a
        protected Object invokeSuspend(Object obj) {
            int i10 = this.f17682f;
            if (i10 == 0) {
                this.f17682f = 1;
                q.b(obj);
                k.c(this.f17683g, "null cannot be cast to non-null type kotlin.Function2<R of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$1, kotlin.coroutines.Continuation<T of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$1>, kotlin.Any?>");
                return ((p) TypeIntrinsics.d(this.f17683g, 2)).invoke(this.f17684h, this);
            }
            if (i10 == 1) {
                this.f17682f = 2;
                q.b(obj);
                return obj;
            }
            throw new IllegalStateException("This coroutine had already completed".toString());
        }
    }

    /* compiled from: IntrinsicsJvm.kt */
    /* renamed from: ra.c$b */
    /* loaded from: classes2.dex */
    public static final class b extends sa.d {

        /* renamed from: h, reason: collision with root package name */
        private int f17685h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ p f17686i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ Object f17687j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(qa.d dVar, g gVar, p pVar, Object obj) {
            super(dVar, gVar);
            this.f17686i = pVar;
            this.f17687j = obj;
            k.c(dVar, "null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        }

        @Override // sa.a
        protected Object invokeSuspend(Object obj) {
            int i10 = this.f17685h;
            if (i10 == 0) {
                this.f17685h = 1;
                q.b(obj);
                k.c(this.f17686i, "null cannot be cast to non-null type kotlin.Function2<R of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$1, kotlin.coroutines.Continuation<T of kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted$lambda$1>, kotlin.Any?>");
                return ((p) TypeIntrinsics.d(this.f17686i, 2)).invoke(this.f17687j, this);
            }
            if (i10 == 1) {
                this.f17685h = 2;
                q.b(obj);
                return obj;
            }
            throw new IllegalStateException("This coroutine had already completed".toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <R, T> qa.d<Unit> a(p<? super R, ? super qa.d<? super T>, ? extends Object> pVar, R r10, qa.d<? super T> dVar) {
        k.e(pVar, "<this>");
        k.e(dVar, "completion");
        qa.d<?> a10 = h.a(dVar);
        if (pVar instanceof sa.a) {
            return ((sa.a) pVar).create(r10, a10);
        }
        g context = a10.getContext();
        if (context == qa.h.f17173e) {
            return new a(a10, pVar, r10);
        }
        return new b(a10, context, pVar, r10);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> qa.d<T> b(qa.d<? super T> dVar) {
        qa.d<T> dVar2;
        k.e(dVar, "<this>");
        sa.d dVar3 = dVar instanceof sa.d ? (sa.d) dVar : null;
        return (dVar3 == null || (dVar2 = (qa.d<T>) dVar3.intercepted()) == null) ? dVar : dVar2;
    }
}
