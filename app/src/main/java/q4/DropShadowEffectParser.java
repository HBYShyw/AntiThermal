package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import m4.AnimatableColorValue;
import m4.AnimatableFloatValue;
import r4.c;

/* compiled from: DropShadowEffectParser.java */
/* renamed from: q4.k, reason: use source file name */
/* loaded from: classes.dex */
public class DropShadowEffectParser {

    /* renamed from: f, reason: collision with root package name */
    private static final c.a f16861f = c.a.a("ef");

    /* renamed from: g, reason: collision with root package name */
    private static final c.a f16862g = c.a.a("nm", "v");

    /* renamed from: a, reason: collision with root package name */
    private AnimatableColorValue f16863a;

    /* renamed from: b, reason: collision with root package name */
    private AnimatableFloatValue f16864b;

    /* renamed from: c, reason: collision with root package name */
    private AnimatableFloatValue f16865c;

    /* renamed from: d, reason: collision with root package name */
    private AnimatableFloatValue f16866d;

    /* renamed from: e, reason: collision with root package name */
    private AnimatableFloatValue f16867e;

    /* JADX WARN: Code restructure failed: missing block: B:46:0x0052, code lost:
    
        if (r0.equals("Opacity") == false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        cVar.m();
        String str = "";
        while (cVar.w()) {
            int i02 = cVar.i0(f16862g);
            if (i02 != 0) {
                char c10 = 1;
                if (i02 != 1) {
                    cVar.j0();
                    cVar.m0();
                } else {
                    str.hashCode();
                    switch (str.hashCode()) {
                        case 353103893:
                            if (str.equals("Distance")) {
                                c10 = 0;
                                break;
                            }
                            break;
                        case 397447147:
                            break;
                        case 1041377119:
                            if (str.equals("Direction")) {
                                c10 = 2;
                                break;
                            }
                            break;
                        case 1379387491:
                            if (str.equals("Shadow Color")) {
                                c10 = 3;
                                break;
                            }
                            break;
                        case 1383710113:
                            if (str.equals("Softness")) {
                                c10 = 4;
                                break;
                            }
                            break;
                    }
                    c10 = 65535;
                    switch (c10) {
                        case 0:
                            this.f16866d = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                            break;
                        case 1:
                            this.f16864b = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
                            break;
                        case 2:
                            this.f16865c = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
                            break;
                        case 3:
                            this.f16863a = AnimatableValueParser.c(cVar, effectiveAnimationComposition);
                            break;
                        case 4:
                            this.f16867e = AnimatableValueParser.e(cVar, effectiveAnimationComposition);
                            break;
                        default:
                            cVar.m0();
                            break;
                    }
                }
            } else {
                str = cVar.X();
            }
        }
        cVar.v();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DropShadowEffect b(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        AnimatableFloatValue animatableFloatValue;
        AnimatableFloatValue animatableFloatValue2;
        AnimatableFloatValue animatableFloatValue3;
        AnimatableFloatValue animatableFloatValue4;
        while (cVar.w()) {
            if (cVar.i0(f16861f) != 0) {
                cVar.j0();
                cVar.m0();
            } else {
                cVar.c();
                while (cVar.w()) {
                    a(cVar, effectiveAnimationComposition);
                }
                cVar.u();
            }
        }
        AnimatableColorValue animatableColorValue = this.f16863a;
        if (animatableColorValue == null || (animatableFloatValue = this.f16864b) == null || (animatableFloatValue2 = this.f16865c) == null || (animatableFloatValue3 = this.f16866d) == null || (animatableFloatValue4 = this.f16867e) == null) {
            return null;
        }
        return new DropShadowEffect(animatableColorValue, animatableFloatValue, animatableFloatValue2, animatableFloatValue3, animatableFloatValue4);
    }
}
