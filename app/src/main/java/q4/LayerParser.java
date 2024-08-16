package q4;

import android.graphics.Color;
import android.graphics.Rect;
import com.oplus.anim.EffectiveAnimationComposition;
import java.util.ArrayList;
import java.util.Collections;
import m4.AnimatableFloatValue;
import m4.AnimatableTextFrame;
import m4.AnimatableTextProperties;
import m4.AnimatableTransform;
import n4.BlurEffect;
import n4.ContentModel;
import o4.e;
import r4.c;
import t4.Keyframe;

/* compiled from: LayerParser.java */
/* renamed from: q4.w, reason: use source file name */
/* loaded from: classes.dex */
public class LayerParser {

    /* renamed from: a, reason: collision with root package name */
    private static final c.a f16893a = c.a.a("nm", "ind", "refId", "ty", "parent", "sw", "sh", "sc", "ks", "tt", "masksProperties", "shapes", "t", "ef", "sr", "st", "w", "h", "ip", "op", "tm", "cl", "hd");

    /* renamed from: b, reason: collision with root package name */
    private static final c.a f16894b = c.a.a("d", "a");

    /* renamed from: c, reason: collision with root package name */
    private static final c.a f16895c = c.a.a("ty", "nm");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LayerParser.java */
    /* renamed from: q4.w$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f16896a;

        static {
            int[] iArr = new int[e.b.values().length];
            f16896a = iArr;
            try {
                iArr[e.b.LUMA.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f16896a[e.b.LUMA_INVERTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static o4.e a(EffectiveAnimationComposition effectiveAnimationComposition) {
        Rect b10 = effectiveAnimationComposition.b();
        return new o4.e(Collections.emptyList(), effectiveAnimationComposition, "__container", -1L, e.a.PRE_COMP, -1L, null, Collections.emptyList(), new AnimatableTransform(), 0, 0, 0, 0.0f, 0.0f, b10.width(), b10.height(), null, null, Collections.emptyList(), e.b.NONE, null, false, null, null);
    }

    public static o4.e b(r4.c cVar, EffectiveAnimationComposition effectiveAnimationComposition) {
        ArrayList arrayList;
        ArrayList arrayList2;
        float f10;
        e.b bVar = e.b.NONE;
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        cVar.m();
        Float valueOf = Float.valueOf(1.0f);
        Float valueOf2 = Float.valueOf(0.0f);
        e.b bVar2 = bVar;
        float f11 = 1.0f;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        boolean z10 = false;
        e.a aVar = null;
        String str = null;
        AnimatableTransform animatableTransform = null;
        AnimatableTextFrame animatableTextFrame = null;
        AnimatableTextProperties animatableTextProperties = null;
        AnimatableFloatValue animatableFloatValue = null;
        BlurEffect blurEffect = null;
        DropShadowEffect dropShadowEffect = null;
        float f12 = 0.0f;
        float f13 = 0.0f;
        float f14 = 0.0f;
        long j10 = -1;
        long j11 = 0;
        String str2 = null;
        String str3 = "UNSET";
        while (cVar.w()) {
            switch (cVar.i0(f16893a)) {
                case 0:
                    str3 = cVar.X();
                    break;
                case 1:
                    j11 = cVar.S();
                    break;
                case 2:
                    str = cVar.X();
                    break;
                case 3:
                    int S = cVar.S();
                    aVar = e.a.UNKNOWN;
                    if (S >= aVar.ordinal()) {
                        break;
                    } else {
                        aVar = e.a.values()[S];
                        break;
                    }
                case 4:
                    j10 = cVar.S();
                    break;
                case 5:
                    i10 = (int) (cVar.S() * s4.h.f());
                    break;
                case 6:
                    i11 = (int) (cVar.S() * s4.h.f());
                    break;
                case 7:
                    i12 = Color.parseColor(cVar.X());
                    break;
                case 8:
                    animatableTransform = AnimatableTransformParser.g(cVar, effectiveAnimationComposition);
                    break;
                case 9:
                    int S2 = cVar.S();
                    if (S2 >= e.b.values().length) {
                        effectiveAnimationComposition.a("Unsupported matte type: " + S2);
                        break;
                    } else {
                        bVar2 = e.b.values()[S2];
                        int i15 = a.f16896a[bVar2.ordinal()];
                        if (i15 == 1) {
                            effectiveAnimationComposition.a("Unsupported matte type: Luma");
                        } else if (i15 == 2) {
                            effectiveAnimationComposition.a("Unsupported matte type: Luma Inverted");
                        }
                        effectiveAnimationComposition.s(1);
                        break;
                    }
                case 10:
                    cVar.c();
                    while (cVar.w()) {
                        arrayList3.add(MaskParser.a(cVar, effectiveAnimationComposition));
                    }
                    effectiveAnimationComposition.s(arrayList3.size());
                    cVar.u();
                    break;
                case 11:
                    cVar.c();
                    while (cVar.w()) {
                        ContentModel a10 = ContentModelParser.a(cVar, effectiveAnimationComposition);
                        if (a10 != null) {
                            arrayList4.add(a10);
                        }
                    }
                    cVar.u();
                    break;
                case 12:
                    cVar.m();
                    while (cVar.w()) {
                        int i02 = cVar.i0(f16894b);
                        if (i02 == 0) {
                            animatableTextFrame = AnimatableValueParser.d(cVar, effectiveAnimationComposition);
                        } else if (i02 != 1) {
                            cVar.j0();
                            cVar.m0();
                        } else {
                            cVar.c();
                            if (cVar.w()) {
                                animatableTextProperties = AnimatableTextPropertiesParser.a(cVar, effectiveAnimationComposition);
                            }
                            while (cVar.w()) {
                                cVar.m0();
                            }
                            cVar.u();
                        }
                    }
                    cVar.v();
                    break;
                case 13:
                    cVar.c();
                    ArrayList arrayList5 = new ArrayList();
                    while (cVar.w()) {
                        cVar.m();
                        while (cVar.w()) {
                            int i03 = cVar.i0(f16895c);
                            if (i03 == 0) {
                                int S3 = cVar.S();
                                if (S3 == 29) {
                                    blurEffect = BlurEffectParser.b(cVar, effectiveAnimationComposition);
                                } else if (S3 == 25) {
                                    dropShadowEffect = new DropShadowEffectParser().b(cVar, effectiveAnimationComposition);
                                }
                            } else if (i03 != 1) {
                                cVar.j0();
                                cVar.m0();
                            } else {
                                arrayList5.add(cVar.X());
                            }
                        }
                        cVar.v();
                    }
                    cVar.u();
                    effectiveAnimationComposition.a("Effective doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: " + arrayList5);
                    break;
                case 14:
                    f11 = (float) cVar.N();
                    break;
                case 15:
                    f13 = (float) cVar.N();
                    break;
                case 16:
                    i13 = (int) (cVar.S() * s4.h.f());
                    break;
                case 17:
                    i14 = (int) (cVar.S() * s4.h.f());
                    break;
                case 18:
                    f12 = (float) cVar.N();
                    break;
                case 19:
                    f14 = (float) cVar.N();
                    break;
                case 20:
                    animatableFloatValue = AnimatableValueParser.f(cVar, effectiveAnimationComposition, false);
                    break;
                case 21:
                    str2 = cVar.X();
                    break;
                case 22:
                    z10 = cVar.L();
                    break;
                default:
                    cVar.j0();
                    cVar.m0();
                    break;
            }
        }
        cVar.v();
        ArrayList arrayList6 = new ArrayList();
        if (f12 > 0.0f) {
            arrayList = arrayList3;
            arrayList2 = arrayList6;
            arrayList2.add(new Keyframe(effectiveAnimationComposition, valueOf2, valueOf2, null, 0.0f, Float.valueOf(f12)));
            f10 = 0.0f;
        } else {
            arrayList = arrayList3;
            arrayList2 = arrayList6;
            f10 = 0.0f;
        }
        if (f14 <= f10) {
            f14 = effectiveAnimationComposition.g();
        }
        arrayList2.add(new Keyframe(effectiveAnimationComposition, valueOf, valueOf, null, f12, Float.valueOf(f14)));
        arrayList2.add(new Keyframe(effectiveAnimationComposition, valueOf2, valueOf2, null, f14, Float.valueOf(Float.MAX_VALUE)));
        if (str3.endsWith(".ai") || "ai".equals(str2)) {
            effectiveAnimationComposition.a("Convert your Illustrator layers to shape layers.");
        }
        return new o4.e(arrayList4, effectiveAnimationComposition, str3, j11, aVar, j10, str, arrayList, animatableTransform, i10, i11, i12, f11, f13, i13, i14, animatableTextFrame, animatableTextProperties, arrayList2, bVar2, animatableFloatValue, z10, blurEffect, dropShadowEffect);
    }
}
