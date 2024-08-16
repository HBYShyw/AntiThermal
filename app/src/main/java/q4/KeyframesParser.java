package q4;

import com.oplus.anim.EffectiveAnimationComposition;
import j4.PathKeyframe;
import java.util.ArrayList;
import java.util.List;
import r4.c;
import t4.Keyframe;

/* compiled from: KeyframesParser.java */
/* renamed from: q4.v, reason: use source file name */
/* loaded from: classes.dex */
class KeyframesParser {

    /* renamed from: a, reason: collision with root package name */
    static c.a f16892a = c.a.a("k");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> List<Keyframe<T>> a(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, float f10, ValueParser<T> valueParser, boolean z10) {
        ArrayList arrayList = new ArrayList();
        if (cVar.e0() == c.b.STRING) {
            effectiveAnimationComposition.a("Effective doesn't support expressions.");
            return arrayList;
        }
        cVar.m();
        while (cVar.w()) {
            if (cVar.i0(f16892a) != 0) {
                cVar.m0();
            } else if (cVar.e0() == c.b.BEGIN_ARRAY) {
                cVar.c();
                if (cVar.e0() == c.b.NUMBER) {
                    arrayList.add(KeyframeParser.c(cVar, effectiveAnimationComposition, f10, valueParser, false, z10));
                } else {
                    while (cVar.w()) {
                        arrayList.add(KeyframeParser.c(cVar, effectiveAnimationComposition, f10, valueParser, true, z10));
                    }
                }
                cVar.u();
            } else {
                arrayList.add(KeyframeParser.c(cVar, effectiveAnimationComposition, f10, valueParser, false, z10));
            }
        }
        cVar.v();
        b(arrayList);
        return arrayList;
    }

    public static <T> void b(List<? extends Keyframe<T>> list) {
        int i10;
        T t7;
        int size = list.size();
        int i11 = 0;
        while (true) {
            i10 = size - 1;
            if (i11 >= i10) {
                break;
            }
            Keyframe<T> keyframe = list.get(i11);
            i11++;
            Keyframe<T> keyframe2 = list.get(i11);
            keyframe.f18576h = Float.valueOf(keyframe2.f18575g);
            if (keyframe.f18571c == null && (t7 = keyframe2.f18570b) != null) {
                keyframe.f18571c = t7;
                if (keyframe instanceof PathKeyframe) {
                    ((PathKeyframe) keyframe).i();
                }
            }
        }
        Keyframe<T> keyframe3 = list.get(i10);
        if ((keyframe3.f18570b == null || keyframe3.f18571c == null) && list.size() > 1) {
            list.remove(keyframe3);
        }
    }
}
