package androidx.lifecycle;

import androidx.lifecycle.h;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: RepeatOnLifecycle.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\u001aC\u0010\t\u001a\u00020\u0006*\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u00012\"\u0010\b\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0003H\u0086@ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000b"}, d2 = {"Landroidx/lifecycle/h;", "Landroidx/lifecycle/h$c;", "state", "Lkotlin/Function2;", "Ltd/h0;", "Lqa/d;", "Lma/f0;", "", "block", "a", "(Landroidx/lifecycle/h;Landroidx/lifecycle/h$c;Lya/p;Lqa/d;)Ljava/lang/Object;", "lifecycle-runtime-ktx_release"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class RepeatOnLifecycleKt {
    public static final Object a(h hVar, h.c cVar, ya.p<? super td.h0, ? super qa.d<? super Unit>, ? extends Object> pVar, qa.d<? super Unit> dVar) {
        Object c10;
        if (cVar != h.c.INITIALIZED) {
            if (hVar.b() == h.c.DESTROYED) {
                return Unit.f15173a;
            }
            Object b10 = td.i0.b(new RepeatOnLifecycleKt$repeatOnLifecycle$3(hVar, cVar, pVar, null), dVar);
            c10 = ra.d.c();
            return b10 == c10 ? b10 : Unit.f15173a;
        }
        throw new IllegalArgumentException("repeatOnLifecycle cannot start work with the INITIALIZED lifecycle state.".toString());
    }
}
