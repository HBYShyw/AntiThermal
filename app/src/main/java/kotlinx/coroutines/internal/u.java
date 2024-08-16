package kotlinx.coroutines.internal;

import kotlin.Metadata;
import ma.Unit;
import za.Lambda;

/* compiled from: OnUndeliveredElement.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0004\u001aE\u0010\u0007\u001a\u0004\u0018\u00010\u0005\"\u0004\b\u0000\u0010\u0000*\u0018\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00028\u0000`\u00032\u0006\u0010\u0004\u001a\u00028\u00002\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0000¢\u0006\u0004\b\u0007\u0010\b\u001a?\u0010\u000b\u001a\u00020\u0002\"\u0004\b\u0000\u0010\u0000*\u0018\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00028\u0000`\u00032\u0006\u0010\u0004\u001a\u00028\u00002\u0006\u0010\n\u001a\u00020\tH\u0000¢\u0006\u0004\b\u000b\u0010\f\u001aK\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0000*\u0018\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00028\u0000`\u00032\u0006\u0010\u0004\u001a\u00028\u00002\u0006\u0010\n\u001a\u00020\tH\u0000¢\u0006\u0004\b\u000e\u0010\u000f**\b\u0000\u0010\u0010\u001a\u0004\b\u0000\u0010\u0000\"\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00020\u00012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00020\u0001¨\u0006\u0011"}, d2 = {"E", "Lkotlin/Function1;", "Lma/f0;", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "element", "Lkotlinx/coroutines/internal/i0;", "undeliveredElementException", "c", "(Lya/l;Ljava/lang/Object;Lkotlinx/coroutines/internal/i0;)Lkotlinx/coroutines/internal/i0;", "Lqa/g;", "context", "b", "(Lya/l;Ljava/lang/Object;Lqa/g;)V", "", "a", "(Lya/l;Ljava/lang/Object;Lqa/g;)Lya/l;", "OnUndeliveredElement", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class u {

    /* compiled from: OnUndeliveredElement.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0004\u001a\u00020\u0003\"\u0004\b\u0000\u0010\u00002\u0006\u0010\u0002\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"E", "", "<anonymous parameter 0>", "Lma/f0;", "a", "(Ljava/lang/Throwable;)V"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<Throwable, Unit> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ya.l<E, Unit> f14399e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ E f14400f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ qa.g f14401g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        a(ya.l<? super E, Unit> lVar, E e10, qa.g gVar) {
            super(1);
            this.f14399e = lVar;
            this.f14400f = e10;
            this.f14401g = gVar;
        }

        public final void a(Throwable th) {
            u.b(this.f14399e, this.f14400f, this.f14401g);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            a(th);
            return Unit.f15173a;
        }
    }

    public static final <E> ya.l<Throwable, Unit> a(ya.l<? super E, Unit> lVar, E e10, qa.g gVar) {
        return new a(lVar, e10, gVar);
    }

    public static final <E> void b(ya.l<? super E, Unit> lVar, E e10, qa.g gVar) {
        i0 c10 = c(lVar, e10, null);
        if (c10 != null) {
            td.f0.a(gVar, c10);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <E> i0 c(ya.l<? super E, Unit> lVar, E e10, i0 i0Var) {
        try {
            lVar.invoke(e10);
        } catch (Throwable th) {
            if (i0Var != null && i0Var.getCause() != th) {
                ma.b.a(i0Var, th);
            } else {
                return new i0("Exception in undelivered element handler for " + e10, th);
            }
        }
        return i0Var;
    }

    public static /* synthetic */ i0 d(ya.l lVar, Object obj, i0 i0Var, int i10, Object obj2) {
        if ((i10 & 2) != 0) {
            i0Var = null;
        }
        return c(lVar, obj, i0Var);
    }
}
