package hd;

import gd.f1;
import hd.KotlinTypePreparator;
import hd.g;

/* compiled from: ClassicTypeCheckerState.kt */
/* renamed from: hd.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class ClassicTypeCheckerState {
    public static final f1 a(boolean z10, boolean z11, b bVar, KotlinTypePreparator kotlinTypePreparator, g gVar) {
        za.k.e(bVar, "typeSystemContext");
        za.k.e(kotlinTypePreparator, "kotlinTypePreparator");
        za.k.e(gVar, "kotlinTypeRefiner");
        return new f1(z10, z11, true, bVar, kotlinTypePreparator, gVar);
    }

    public static /* synthetic */ f1 b(boolean z10, boolean z11, b bVar, KotlinTypePreparator kotlinTypePreparator, g gVar, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z11 = true;
        }
        if ((i10 & 4) != 0) {
            bVar = q.f12241a;
        }
        if ((i10 & 8) != 0) {
            kotlinTypePreparator = KotlinTypePreparator.a.f12214a;
        }
        if ((i10 & 16) != 0) {
            gVar = g.a.f12215a;
        }
        return a(z10, z11, bVar, kotlinTypePreparator, gVar);
    }
}
