package yd;

import kotlin.Metadata;
import kotlinx.coroutines.internal.e0;
import kotlinx.coroutines.internal.y;
import ma.p;
import ma.q;
import qa.d;
import qa.g;
import sa.h;
import td.q1;
import td.v;
import ya.p;
import za.TypeIntrinsics;

/* compiled from: Undispatched.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aT\u0010\b\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0000\"\u0004\b\u0001\u0010\u0001*\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u00022\u0006\u0010\u0005\u001a\u00028\u00002\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0003H\u0000ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001aV\u0010\f\u001a\u0004\u0018\u00010\u0004\"\u0004\b\u0000\u0010\u0001\"\u0004\b\u0001\u0010\u0000*\b\u0012\u0004\u0012\u00028\u00000\n2\u0006\u0010\u0005\u001a\u00028\u00012\"\u0010\u000b\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0002H\u0000ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000e"}, d2 = {"R", "T", "Lkotlin/Function2;", "Lqa/d;", "", "receiver", "completion", "Lma/f0;", "a", "(Lya/p;Ljava/lang/Object;Lqa/d;)V", "Lkotlinx/coroutines/internal/y;", "block", "b", "(Lkotlinx/coroutines/internal/y;Ljava/lang/Object;Lya/p;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* renamed from: yd.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class Undispatched {
    public static final <R, T> void a(p<? super R, ? super d<? super T>, ? extends Object> pVar, R r10, d<? super T> dVar) {
        Object c10;
        d a10 = h.a(dVar);
        try {
            g f18758i = dVar.getF18758i();
            Object c11 = e0.c(f18758i, null);
            try {
                Object invoke = ((p) TypeIntrinsics.d(pVar, 2)).invoke(r10, a10);
                c10 = ra.d.c();
                if (invoke != c10) {
                    p.a aVar = ma.p.f15184e;
                    a10.resumeWith(ma.p.a(invoke));
                }
            } finally {
                e0.a(f18758i, c11);
            }
        } catch (Throwable th) {
            p.a aVar2 = ma.p.f15184e;
            a10.resumeWith(ma.p.a(q.a(th)));
        }
    }

    public static final <T, R> Object b(y<? super T> yVar, R r10, ya.p<? super R, ? super d<? super T>, ? extends Object> pVar) {
        Object vVar;
        Object c10;
        Object c11;
        Object c12;
        try {
            vVar = ((ya.p) TypeIntrinsics.d(pVar, 2)).invoke(r10, yVar);
        } catch (Throwable th) {
            vVar = new v(th, false, 2, null);
        }
        c10 = ra.d.c();
        if (vVar == c10) {
            c12 = ra.d.c();
            return c12;
        }
        Object T = yVar.T(vVar);
        if (T == q1.f18780b) {
            c11 = ra.d.c();
            return c11;
        }
        if (!(T instanceof v)) {
            return q1.h(T);
        }
        throw ((v) T).f18800a;
    }
}
