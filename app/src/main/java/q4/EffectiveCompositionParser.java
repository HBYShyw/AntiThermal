package q4;

import android.graphics.Rect;
import com.oplus.anim.EffectiveAnimationComposition;
import com.oplus.anim.EffectiveImageAsset;
import j.LongSparseArray;
import j.SparseArrayCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import l4.Font;
import l4.FontCharacter;
import l4.Marker;
import o4.e;
import r4.c;

/* compiled from: EffectiveCompositionParser.java */
/* renamed from: q4.l, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveCompositionParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16870a = c.a.a("w", "h", "ip", "op", "fr", "v", "layers", "assets", "fonts", "chars", "markers");

    /* renamed from: b, reason: collision with root package name */
    static c.a f16871b = c.a.a("id", "layers", "w", "h", "p", "u");

    /* renamed from: c, reason: collision with root package name */
    private static final c.a f16872c = c.a.a("list");

    /* renamed from: d, reason: collision with root package name */
    private static final c.a f16873d = c.a.a("cm", "tm", "dr");

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0043. Please report as an issue. */
    public static EffectiveAnimationComposition a(r4.c cVar) {
        HashMap hashMap;
        ArrayList arrayList;
        r4.c cVar2 = cVar;
        float f10 = s4.h.f();
        LongSparseArray<o4.e> longSparseArray = new LongSparseArray<>();
        ArrayList arrayList2 = new ArrayList();
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        HashMap hashMap4 = new HashMap();
        ArrayList arrayList3 = new ArrayList();
        SparseArrayCompat<FontCharacter> sparseArrayCompat = new SparseArrayCompat<>();
        EffectiveAnimationComposition effectiveAnimationComposition = new EffectiveAnimationComposition();
        cVar.m();
        float f11 = 0.0f;
        float f12 = 0.0f;
        float f13 = 0.0f;
        int i10 = 0;
        int i11 = 0;
        while (cVar.w()) {
            switch (cVar2.i0(f16870a)) {
                case 0:
                    i10 = cVar.S();
                    cVar2 = cVar;
                    break;
                case 1:
                    i11 = cVar.S();
                    cVar2 = cVar;
                    break;
                case 2:
                    f11 = (float) cVar.N();
                    cVar2 = cVar;
                    break;
                case 3:
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    f12 = ((float) cVar.N()) - 0.01f;
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
                case 4:
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    f13 = (float) cVar.N();
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
                case 5:
                    String[] split = cVar.X().split("\\.");
                    if (!s4.h.k(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 4, 4, 0)) {
                        effectiveAnimationComposition.a("EffectiveAnimation only supports bodymovin >= 4.4.0");
                    }
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
                case 6:
                    e(cVar2, effectiveAnimationComposition, arrayList2, longSparseArray);
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
                case 7:
                    b(cVar2, effectiveAnimationComposition, hashMap2, hashMap3);
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
                case 8:
                    d(cVar2, hashMap4);
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
                case 9:
                    c(cVar2, effectiveAnimationComposition, sparseArrayCompat);
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
                case 10:
                    f(cVar2, arrayList3);
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
                default:
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    cVar.j0();
                    cVar.m0();
                    cVar2 = cVar;
                    hashMap4 = hashMap;
                    arrayList3 = arrayList;
                    break;
            }
        }
        effectiveAnimationComposition.t(new Rect(0, 0, (int) (i10 * f10), (int) (i11 * f10)), f11, f12, f13, arrayList2, longSparseArray, hashMap2, hashMap3, sparseArrayCompat, hashMap4, arrayList3, f10);
        return effectiveAnimationComposition;
    }

    private static void b(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, Map<String, List<o4.e>> map, Map<String, EffectiveImageAsset> map2) {
        cVar.c();
        while (cVar.w()) {
            ArrayList arrayList = new ArrayList();
            LongSparseArray longSparseArray = new LongSparseArray();
            cVar.m();
            int i10 = 0;
            int i11 = 0;
            String str = null;
            String str2 = null;
            String str3 = null;
            while (cVar.w()) {
                int i02 = cVar.i0(f16871b);
                if (i02 == 0) {
                    str = cVar.X();
                } else if (i02 == 1) {
                    cVar.c();
                    while (cVar.w()) {
                        o4.e b10 = LayerParser.b(cVar, effectiveAnimationComposition);
                        longSparseArray.j(b10.d(), b10);
                        arrayList.add(b10);
                    }
                    cVar.u();
                } else if (i02 == 2) {
                    i10 = cVar.S();
                } else if (i02 == 3) {
                    i11 = cVar.S();
                } else if (i02 == 4) {
                    str2 = cVar.X();
                } else if (i02 != 5) {
                    cVar.j0();
                    cVar.m0();
                } else {
                    str3 = cVar.X();
                }
            }
            cVar.v();
            if (str2 != null) {
                EffectiveImageAsset effectiveImageAsset = new EffectiveImageAsset(i10, i11, str, str2, str3);
                map2.put(effectiveImageAsset.d(), effectiveImageAsset);
            } else {
                map.put(str, arrayList);
            }
        }
        cVar.u();
    }

    private static void c(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, SparseArrayCompat<FontCharacter> sparseArrayCompat) {
        cVar.c();
        while (cVar.w()) {
            FontCharacter a10 = FontCharacterParser.a(cVar, effectiveAnimationComposition);
            sparseArrayCompat.i(a10.hashCode(), a10);
        }
        cVar.u();
    }

    private static void d(r4.c cVar, Map<String, Font> map) {
        cVar.m();
        while (cVar.w()) {
            if (cVar.i0(f16872c) != 0) {
                cVar.j0();
                cVar.m0();
            } else {
                cVar.c();
                while (cVar.w()) {
                    Font a10 = FontParser.a(cVar);
                    map.put(a10.b(), a10);
                }
                cVar.u();
            }
        }
        cVar.v();
    }

    private static void e(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition, List<o4.e> list, LongSparseArray<o4.e> longSparseArray) {
        cVar.c();
        int i10 = 0;
        while (cVar.w()) {
            o4.e b10 = LayerParser.b(cVar, effectiveAnimationComposition);
            if (b10.f() == e.a.IMAGE) {
                i10++;
            }
            list.add(b10);
            longSparseArray.j(b10.d(), b10);
            if (i10 > 4) {
                s4.e.c("You have " + i10 + " images. EffectiveAnimation should primarily be used with shapes. If you are using Adobe Illustrator, convert the Illustrator layers to shape layers.");
            }
        }
        cVar.u();
    }

    private static void f(r4.c cVar, List<Marker> list) {
        cVar.c();
        while (cVar.w()) {
            String str = null;
            cVar.m();
            float f10 = 0.0f;
            float f11 = 0.0f;
            while (cVar.w()) {
                int i02 = cVar.i0(f16873d);
                if (i02 == 0) {
                    str = cVar.X();
                } else if (i02 == 1) {
                    f10 = (float) cVar.N();
                } else if (i02 != 2) {
                    cVar.j0();
                    cVar.m0();
                } else {
                    f11 = (float) cVar.N();
                }
            }
            cVar.v();
            list.add(new Marker(str, f10, f11));
        }
        cVar.u();
    }
}
