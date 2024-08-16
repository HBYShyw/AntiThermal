package td;

import java.util.concurrent.CancellationException;
import kotlin.Metadata;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Job.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0012\u0010\u0003\u001a\u00020\u00022\n\b\u0002\u0010\u0001\u001a\u0004\u0018\u00010\u0000\u001a\u001c\u0010\t\u001a\u00020\b*\u00020\u00042\u0010\b\u0002\u0010\u0007\u001a\n\u0018\u00010\u0005j\u0004\u0018\u0001`\u0006\u001a\n\u0010\n\u001a\u00020\b*\u00020\u0000\u001a\n\u0010\u000b\u001a\u00020\b*\u00020\u0004¨\u0006\f"}, d2 = {"Ltd/i1;", "parent", "Ltd/t;", "a", "Lqa/g;", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "cause", "Lma/f0;", "c", "f", "e", "kotlinx-coroutines-core"}, k = 5, mv = {1, 6, 0}, xs = "kotlinx/coroutines/JobKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class n1 {
    public static final CompletableJob a(i1 i1Var) {
        return new l1(i1Var);
    }

    public static /* synthetic */ CompletableJob b(i1 i1Var, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            i1Var = null;
        }
        return m1.a(i1Var);
    }

    public static final void c(qa.g gVar, CancellationException cancellationException) {
        i1 i1Var = (i1) gVar.c(i1.f18746d);
        if (i1Var != null) {
            i1Var.m0(cancellationException);
        }
    }

    public static /* synthetic */ void d(qa.g gVar, CancellationException cancellationException, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            cancellationException = null;
        }
        m1.c(gVar, cancellationException);
    }

    public static final void e(qa.g gVar) {
        i1 i1Var = (i1) gVar.c(i1.f18746d);
        if (i1Var != null) {
            m1.f(i1Var);
        }
    }

    public static final void f(i1 i1Var) {
        if (!i1Var.b()) {
            throw i1Var.w();
        }
    }
}
