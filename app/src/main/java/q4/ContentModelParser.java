package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import n4.ContentModel;
import r4.c;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ContentModelParser.java */
/* renamed from: q4.h, reason: use source file name */
/* loaded from: classes.dex */
public class ContentModelParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16850a = c.a.a("ty", "d");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x00b4, code lost:
    
        if (r2.equals("gf") == false) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ContentModel a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        ContentModel contentModel;
        String str;
        cVar.m();
        char c10 = 2;
        int i10 = 2;
        while (true) {
            contentModel = null;
            if (!cVar.w()) {
                str = null;
                break;
            }
            int i02 = cVar.i0(f16850a);
            if (i02 == 0) {
                str = cVar.X();
                break;
            }
            if (i02 != 1) {
                cVar.j0();
                cVar.m0();
            } else {
                i10 = cVar.S();
            }
        }
        if (str == null) {
            return null;
        }
        switch (str.hashCode()) {
            case 3239:
                if (str.equals("el")) {
                    c10 = 0;
                    break;
                }
                c10 = 65535;
                break;
            case 3270:
                if (str.equals("fl")) {
                    c10 = 1;
                    break;
                }
                c10 = 65535;
                break;
            case 3295:
                break;
            case 3307:
                if (str.equals("gr")) {
                    c10 = 3;
                    break;
                }
                c10 = 65535;
                break;
            case 3308:
                if (str.equals("gs")) {
                    c10 = 4;
                    break;
                }
                c10 = 65535;
                break;
            case 3488:
                if (str.equals("mm")) {
                    c10 = 5;
                    break;
                }
                c10 = 65535;
                break;
            case 3633:
                if (str.equals("rc")) {
                    c10 = 6;
                    break;
                }
                c10 = 65535;
                break;
            case 3646:
                if (str.equals("rp")) {
                    c10 = 7;
                    break;
                }
                c10 = 65535;
                break;
            case 3669:
                if (str.equals("sh")) {
                    c10 = '\b';
                    break;
                }
                c10 = 65535;
                break;
            case 3679:
                if (str.equals("sr")) {
                    c10 = '\t';
                    break;
                }
                c10 = 65535;
                break;
            case 3681:
                if (str.equals("st")) {
                    c10 = '\n';
                    break;
                }
                c10 = 65535;
                break;
            case 3705:
                if (str.equals("tm")) {
                    c10 = 11;
                    break;
                }
                c10 = 65535;
                break;
            case 3710:
                if (str.equals("tr")) {
                    c10 = '\f';
                    break;
                }
                c10 = 65535;
                break;
            default:
                c10 = 65535;
                break;
        }
        switch (c10) {
            case 0:
                contentModel = CircleShapeParser.a(cVar, effectiveAnimationComposition, i10);
                break;
            case 1:
                contentModel = ShapeFillParser.a(cVar, effectiveAnimationComposition);
                break;
            case 2:
                contentModel = GradientFillParser.a(cVar, effectiveAnimationComposition);
                break;
            case 3:
                contentModel = ShapeGroupParser.a(cVar, effectiveAnimationComposition);
                break;
            case 4:
                contentModel = GradientStrokeParser.a(cVar, effectiveAnimationComposition);
                break;
            case 5:
                contentModel = MergePathsParser.a(cVar);
                effectiveAnimationComposition.a("Animation contains merge paths. Merge paths are only supported on KitKat+ and must be manually enabled by calling enableMergePathsForKitKatAndAbove().");
                break;
            case 6:
                contentModel = RectangleShapeParser.a(cVar, effectiveAnimationComposition);
                break;
            case 7:
                contentModel = RepeaterParser.a(cVar, effectiveAnimationComposition);
                break;
            case '\b':
                contentModel = ShapePathParser.a(cVar, effectiveAnimationComposition);
                break;
            case '\t':
                contentModel = PolystarShapeParser.a(cVar, effectiveAnimationComposition);
                break;
            case '\n':
                contentModel = ShapeStrokeParser.a(cVar, effectiveAnimationComposition);
                break;
            case 11:
                contentModel = ShapeTrimPathParser.a(cVar, effectiveAnimationComposition);
                break;
            case '\f':
                contentModel = AnimatableTransformParser.g(cVar, effectiveAnimationComposition);
                break;
            default:
                s4.e.c("Unknown shape type " + str);
                break;
        }
        while (cVar.w()) {
            cVar.m0();
        }
        cVar.v();
        return contentModel;
    }
}
