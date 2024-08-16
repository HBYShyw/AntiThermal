package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import n4.BlurEffect;
import r4.c;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BlurEffectParser.java */
/* renamed from: q4.e, reason: use source file name */
/* loaded from: classes.dex */
public class BlurEffectParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16842a = c.a.a("ef");

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16843b = c.a.a("ty", "v");

    private static BlurEffect a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        cVar.m();
        BlurEffect blurEffect = null;
        while (true) {
            boolean z10 = false;
            while (cVar.w()) {
                int i02 = cVar.i0(f16843b);
                if (i02 != 0) {
                    if (i02 != 1) {
                        cVar.j0();
                        cVar.m0();
                    } else if (z10) {
                        blurEffect = new BlurEffect(AnimatableValueParser.e(cVar, effectiveAnimationComposition));
                    } else {
                        cVar.m0();
                    }
                } else if (cVar.S() == 0) {
                    z10 = true;
                }
            }
            cVar.v();
            return blurEffect;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BlurEffect b(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        BlurEffect blurEffect = null;
        while (cVar.w()) {
            if (cVar.i0(f16842a) != 0) {
                cVar.j0();
                cVar.m0();
            } else {
                cVar.c();
                while (cVar.w()) {
                    BlurEffect a10 = a(cVar, effectiveAnimationComposition);
                    if (a10 != null) {
                        blurEffect = a10;
                    }
                }
                cVar.u();
            }
        }
        return blurEffect;
    }
}
