package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableIntegerValue;
import m4.AnimatableShapeValue;
import n4.Mask;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MaskParser.java */
/* renamed from: q4.x, reason: use source file name */
/* loaded from: classes.dex */
public class MaskParser {
    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x006a, code lost:
    
        if (r1.equals("s") == false) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Mask a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        boolean z10;
        cVar.m();
        Mask.a aVar = null;
        boolean z11 = false;
        AnimatableShapeValue animatableShapeValue = null;
        AnimatableIntegerValue animatableIntegerValue = null;
        while (cVar.w()) {
            String U = cVar.U();
            U.hashCode();
            char c10 = 3;
            switch (U.hashCode()) {
                case 111:
                    if (U.equals("o")) {
                        z10 = false;
                        break;
                    }
                    break;
                case 3588:
                    if (U.equals("pt")) {
                        z10 = true;
                        break;
                    }
                    break;
                case 104433:
                    if (U.equals("inv")) {
                        z10 = 2;
                        break;
                    }
                    break;
                case 3357091:
                    if (U.equals("mode")) {
                        z10 = 3;
                        break;
                    }
                    break;
            }
            z10 = -1;
            switch (z10) {
                case false:
                    animatableIntegerValue = AnimatableValueParser.h(cVar, effectiveAnimationComposition);
                    break;
                case true:
                    animatableShapeValue = AnimatableValueParser.k(cVar, effectiveAnimationComposition);
                    break;
                case true:
                    z11 = cVar.L();
                    break;
                case true:
                    String X = cVar.X();
                    X.hashCode();
                    switch (X.hashCode()) {
                        case 97:
                            if (X.equals("a")) {
                                c10 = 0;
                                break;
                            }
                            break;
                        case 105:
                            if (X.equals("i")) {
                                c10 = 1;
                                break;
                            }
                            break;
                        case 110:
                            if (X.equals("n")) {
                                c10 = 2;
                                break;
                            }
                            break;
                        case 115:
                            break;
                    }
                    c10 = 65535;
                    switch (c10) {
                        case 0:
                            aVar = Mask.a.MASK_MODE_ADD;
                            break;
                        case 1:
                            effectiveAnimationComposition.a("Animation contains intersect masks. They are not supported but will be treated like add masks.");
                            aVar = Mask.a.MASK_MODE_INTERSECT;
                            break;
                        case 2:
                            aVar = Mask.a.MASK_MODE_NONE;
                            break;
                        case 3:
                            aVar = Mask.a.MASK_MODE_SUBTRACT;
                            break;
                        default:
                            s4.e.c("Unknown mask mode " + U + ". Defaulting to Add.");
                            aVar = Mask.a.MASK_MODE_ADD;
                            break;
                    }
                default:
                    cVar.m0();
                    break;
            }
        }
        cVar.v();
        return new Mask(aVar, animatableShapeValue, animatableIntegerValue, z11);
    }
}
