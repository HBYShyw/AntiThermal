package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import java.util.ArrayList;
import java.util.Collections;
import m4.AnimatableColorValue;
import m4.AnimatableFloatValue;
import m4.AnimatableIntegerValue;
import n4.ShapeStroke;
import r4.c;
import t4.Keyframe;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ShapeStrokeParser.java */
/* renamed from: q4.k0, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeStrokeParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16868a = c.a.a("nm", "c", "w", "o", "lc", "lj", "ml", "hd", "d");

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16869b = c.a.a("n", "v");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShapeStroke a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        char c10;
        ArrayList arrayList = new ArrayList();
        boolean z10 = false;
        float f10 = 0.0f;
        String str = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableColorValue animatableColorValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        ShapeStroke.b bVar = null;
        ShapeStroke.c cVar2 = null;
        AnimatableIntegerValue animatableIntegerValue = null;
        while (cVar.w()) {
            switch (cVar.i0(f16868a)) {
                case 0:
                    str = cVar.X();
                    break;
                case 1:
                    animatableColorValue = AnimatableValueParser.c(cVar, effectiveAnimationComposition);
                    break;
                case 2:
                    animatableFloatValue2 = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                    break;
                case 3:
                    animatableIntegerValue = AnimatableValueParser.h(cVar, effectiveAnimationComposition);
                    break;
                case 4:
                    bVar = ShapeStroke.b.values()[cVar.S() - 1];
                    break;
                case 5:
                    cVar2 = ShapeStroke.c.values()[cVar.S() - 1];
                    break;
                case 6:
                    f10 = (float) cVar.N();
                    break;
                case 7:
                    z10 = cVar.L();
                    break;
                case 8:
                    cVar.c();
                    while (cVar.w()) {
                        cVar.m();
                        String str2 = null;
                        AnimatableFloatValue animatableFloatValue3 = null;
                        while (cVar.w()) {
                            int i02 = cVar.i0(f16869b);
                            if (i02 == 0) {
                                str2 = cVar.X();
                            } else if (i02 != 1) {
                                cVar.j0();
                                cVar.m0();
                            } else {
                                animatableFloatValue3 = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                            }
                        }
                        cVar.v();
                        str2.hashCode();
                        switch (str2.hashCode()) {
                            case 100:
                                if (str2.equals("d")) {
                                    c10 = 0;
                                    break;
                                }
                                break;
                            case 103:
                                if (str2.equals("g")) {
                                    c10 = 1;
                                    break;
                                }
                                break;
                            case 111:
                                if (str2.equals("o")) {
                                    c10 = 2;
                                    break;
                                }
                                break;
                        }
                        c10 = 65535;
                        switch (c10) {
                            case 0:
                            case 1:
                                effectiveAnimationComposition.v(true);
                                arrayList.add(animatableFloatValue3);
                                break;
                            case 2:
                                animatableFloatValue = animatableFloatValue3;
                                break;
                        }
                    }
                    cVar.u();
                    if (arrayList.size() != 1) {
                        break;
                    } else {
                        arrayList.add(arrayList.get(0));
                        break;
                    }
                    break;
                default:
                    cVar.m0();
                    break;
            }
        }
        if (animatableIntegerValue == null) {
            animatableIntegerValue = new AnimatableIntegerValue(Collections.singletonList(new Keyframe(100)));
        }
        return new ShapeStroke(str, animatableFloatValue, arrayList, animatableColorValue, animatableIntegerValue, animatableFloatValue2, bVar, cVar2, f10, z10);
    }
}
