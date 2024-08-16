package yd;

import kotlin.Metadata;
import kotlinx.coroutines.internal.f;
import ma.Unit;
import ma.p;
import ma.q;
import qa.d;
import ra.IntrinsicsJvm;
import ya.l;

/* compiled from: Cancellable.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\b\u001al\u0010\u000b\u001a\u00020\t\"\u0004\b\u0000\u0010\u0000\"\u0004\b\u0001\u0010\u0001*\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u00022\u0006\u0010\u0005\u001a\u00028\u00002\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u00032\u0016\b\u0002\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t\u0018\u00010\u0007H\u0000ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\f\u001a\u001e\u0010\u000e\u001a\u00020\t*\b\u0012\u0004\u0012\u00020\t0\u00032\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\u0003H\u0000\u001a\u001c\u0010\u0010\u001a\u00020\t2\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\u000f\u001a\u00020\bH\u0002\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011"}, d2 = {"R", "T", "Lkotlin/Function2;", "Lqa/d;", "", "receiver", "completion", "Lkotlin/Function1;", "", "Lma/f0;", "onCancellation", "c", "(Lya/p;Ljava/lang/Object;Lqa/d;Lya/l;)V", "fatalCompletion", "b", "e", "a", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class a {
    private static final void a(d<?> dVar, Throwable th) {
        p.a aVar = p.f15184e;
        dVar.resumeWith(p.a(q.a(th)));
        throw th;
    }

    public static final void b(d<? super Unit> dVar, d<?> dVar2) {
        d b10;
        try {
            b10 = IntrinsicsJvm.b(dVar);
            p.a aVar = p.f15184e;
            f.c(b10, p.a(Unit.f15173a), null, 2, null);
        } catch (Throwable th) {
            a(dVar2, th);
        }
    }

    public static final <R, T> void c(ya.p<? super R, ? super d<? super T>, ? extends Object> pVar, R r10, d<? super T> dVar, l<? super Throwable, Unit> lVar) {
        d a10;
        d b10;
        try {
            a10 = IntrinsicsJvm.a(pVar, r10, dVar);
            b10 = IntrinsicsJvm.b(a10);
            p.a aVar = p.f15184e;
            f.b(b10, p.a(Unit.f15173a), lVar);
        } catch (Throwable th) {
            a(dVar, th);
        }
    }

    public static /* synthetic */ void d(ya.p pVar, Object obj, d dVar, l lVar, int i10, Object obj2) {
        if ((i10 & 4) != 0) {
            lVar = null;
        }
        c(pVar, obj, dVar, lVar);
    }
}
