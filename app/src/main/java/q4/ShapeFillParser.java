package q4;

import android.graphics.Path;
import com.oplus.anim.EffectiveAnimationComposition;
import java.util.Collections;
import m4.AnimatableColorValue;
import m4.AnimatableIntegerValue;
import n4.ShapeFill;
import r4.c;
import t4.Keyframe;

/* compiled from: ShapeFillParser.java */
/* renamed from: q4.h0, reason: use source file name */
/* loaded from: classes.dex */
class ShapeFillParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16851a = c.a.a("nm", "c", "o", "fillEnabled", "r", "hd");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShapeFill a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        AnimatableIntegerValue animatableIntegerValue = null;
        boolean z10 = false;
        boolean z11 = false;
        int i10 = 1;
        String str = null;
        AnimatableColorValue animatableColorValue = null;
        while (cVar.w()) {
            int i02 = cVar.i0(f16851a);
            if (i02 == 0) {
                str = cVar.X();
            } else if (i02 == 1) {
                animatableColorValue = AnimatableValueParser.c(cVar, effectiveAnimationComposition);
            } else if (i02 == 2) {
                animatableIntegerValue = AnimatableValueParser.h(cVar, effectiveAnimationComposition);
            } else if (i02 == 3) {
                z10 = cVar.L();
            } else if (i02 == 4) {
                i10 = cVar.S();
            } else if (i02 != 5) {
                cVar.j0();
                cVar.m0();
            } else {
                z11 = cVar.L();
            }
        }
        return new ShapeFill(str, z10, i10 == 1 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD, animatableColorValue, animatableIntegerValue == null ? new AnimatableIntegerValue(Collections.singletonList(new Keyframe(100))) : animatableIntegerValue, z11);
    }
}
