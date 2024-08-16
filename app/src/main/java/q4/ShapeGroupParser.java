package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import java.util.ArrayList;
import n4.ContentModel;
import n4.ShapeGroup;
import r4.c;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ShapeGroupParser.java */
/* renamed from: q4.i0, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeGroupParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16854a = c.a.a("nm", "hd", "it");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShapeGroup a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        ArrayList arrayList = new ArrayList();
        String str = null;
        boolean z10 = false;
        while (cVar.w()) {
            int i02 = cVar.i0(f16854a);
            if (i02 == 0) {
                str = cVar.X();
            } else if (i02 == 1) {
                z10 = cVar.L();
            } else if (i02 != 2) {
                cVar.m0();
            } else {
                cVar.c();
                while (cVar.w()) {
                    ContentModel a10 = ContentModelParser.a(cVar, effectiveAnimationComposition);
                    if (a10 != null) {
                        arrayList.add(a10);
                    }
                }
                cVar.u();
            }
        }
        return new ShapeGroup(str, arrayList, z10);
    }
}
