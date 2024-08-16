package q4;

import n4.MergePaths;
import r4.c;

/* compiled from: MergePathsParser.java */
/* renamed from: q4.y, reason: use source file name */
/* loaded from: classes.dex */
class MergePathsParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16897a = c.a.a("nm", "mm", "hd");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MergePaths a(r4.c cVar) {
        String str = null;
        boolean z10 = false;
        MergePaths.a aVar = null;
        while (cVar.w()) {
            int i02 = cVar.i0(f16897a);
            if (i02 == 0) {
                str = cVar.X();
            } else if (i02 == 1) {
                aVar = MergePaths.a.a(cVar.S());
            } else if (i02 != 2) {
                cVar.j0();
                cVar.m0();
            } else {
                z10 = cVar.L();
            }
        }
        return new MergePaths(str, aVar, z10);
    }
}
