package o4;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import com.oplus.anim.EffectiveAnimationComposition;
import com.oplus.anim.EffectiveAnimationDrawable;
import com.oplus.anim.EffectiveAnimationProperty;
import i4.ContentGroup;
import j.LongSparseArray;
import j4.BaseKeyframeAnimation;
import j4.TextKeyframeAnimation;
import j4.ValueCallbackKeyframeAnimation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import l4.DocumentData;
import l4.Font;
import l4.FontCharacter;
import m4.AnimatableColorValue;
import m4.AnimatableFloatValue;
import m4.AnimatableTextProperties;
import n4.ShapeGroup;
import t4.EffectiveValueCallback;

/* compiled from: TextLayer.java */
/* renamed from: o4.i, reason: use source file name */
/* loaded from: classes.dex */
public class TextLayer extends BaseLayer {
    private final StringBuilder B;
    private final RectF C;
    private final Matrix D;
    private final Paint E;
    private final Paint F;
    private final Map<FontCharacter, List<ContentGroup>> G;
    private final LongSparseArray<String> H;
    private final TextKeyframeAnimation I;
    private final EffectiveAnimationDrawable J;
    private final EffectiveAnimationComposition K;
    private BaseKeyframeAnimation<Integer, Integer> L;
    private BaseKeyframeAnimation<Integer, Integer> M;
    private BaseKeyframeAnimation<Integer, Integer> N;
    private BaseKeyframeAnimation<Integer, Integer> O;
    private BaseKeyframeAnimation<Float, Float> P;
    private BaseKeyframeAnimation<Float, Float> Q;
    private BaseKeyframeAnimation<Float, Float> R;
    private BaseKeyframeAnimation<Float, Float> S;
    private BaseKeyframeAnimation<Float, Float> T;
    private BaseKeyframeAnimation<Typeface, Typeface> U;

    /* compiled from: TextLayer.java */
    /* renamed from: o4.i$a */
    /* loaded from: classes.dex */
    class a extends Paint {
        a(int i10) {
            super(i10);
            setStyle(Paint.Style.FILL);
        }
    }

    /* compiled from: TextLayer.java */
    /* renamed from: o4.i$b */
    /* loaded from: classes.dex */
    class b extends Paint {
        b(int i10) {
            super(i10);
            setStyle(Paint.Style.STROKE);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TextLayer.java */
    /* renamed from: o4.i$c */
    /* loaded from: classes.dex */
    public static /* synthetic */ class c {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f16256a;

        static {
            int[] iArr = new int[DocumentData.a.values().length];
            f16256a = iArr;
            try {
                iArr[DocumentData.a.LEFT_ALIGN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f16256a[DocumentData.a.RIGHT_ALIGN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f16256a[DocumentData.a.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextLayer(EffectiveAnimationDrawable effectiveAnimationDrawable, e eVar) {
        super(effectiveAnimationDrawable, eVar);
        AnimatableFloatValue animatableFloatValue;
        AnimatableFloatValue animatableFloatValue2;
        AnimatableColorValue animatableColorValue;
        AnimatableColorValue animatableColorValue2;
        this.B = new StringBuilder(2);
        this.C = new RectF();
        this.D = new Matrix();
        this.E = new a(1);
        this.F = new b(1);
        this.G = new HashMap();
        this.H = new LongSparseArray<>();
        this.J = effectiveAnimationDrawable;
        this.K = eVar.b();
        TextKeyframeAnimation a10 = eVar.s().a();
        this.I = a10;
        a10.a(this);
        h(a10);
        AnimatableTextProperties t7 = eVar.t();
        if (t7 != null && (animatableColorValue2 = t7.f14914a) != null) {
            BaseKeyframeAnimation<Integer, Integer> a11 = animatableColorValue2.a();
            this.L = a11;
            a11.a(this);
            h(this.L);
        }
        if (t7 != null && (animatableColorValue = t7.f14915b) != null) {
            BaseKeyframeAnimation<Integer, Integer> a12 = animatableColorValue.a();
            this.N = a12;
            a12.a(this);
            h(this.N);
        }
        if (t7 != null && (animatableFloatValue2 = t7.f14916c) != null) {
            BaseKeyframeAnimation<Float, Float> a13 = animatableFloatValue2.a();
            this.P = a13;
            a13.a(this);
            h(this.P);
        }
        if (t7 == null || (animatableFloatValue = t7.f14917d) == null) {
            return;
        }
        BaseKeyframeAnimation<Float, Float> a14 = animatableFloatValue.a();
        this.R = a14;
        a14.a(this);
        h(this.R);
    }

    private void N(DocumentData.a aVar, Canvas canvas, float f10) {
        int i10 = c.f16256a[aVar.ordinal()];
        if (i10 == 2) {
            canvas.translate(-f10, 0.0f);
        } else {
            if (i10 != 3) {
                return;
            }
            canvas.translate((-f10) / 2.0f, 0.0f);
        }
    }

    private String O(String str, int i10) {
        int codePointAt = str.codePointAt(i10);
        int charCount = Character.charCount(codePointAt) + i10;
        while (charCount < str.length()) {
            int codePointAt2 = str.codePointAt(charCount);
            if (!b0(codePointAt2)) {
                break;
            }
            charCount += Character.charCount(codePointAt2);
            codePointAt = (codePointAt * 31) + codePointAt2;
        }
        long j10 = codePointAt;
        if (this.H.c(j10)) {
            return this.H.e(j10);
        }
        this.B.setLength(0);
        while (i10 < charCount) {
            int codePointAt3 = str.codePointAt(i10);
            this.B.appendCodePoint(codePointAt3);
            i10 += Character.charCount(codePointAt3);
        }
        String sb2 = this.B.toString();
        this.H.j(j10, sb2);
        return sb2;
    }

    private void P(String str, Paint paint, Canvas canvas) {
        if (paint.getColor() == 0) {
            return;
        }
        if (paint.getStyle() == Paint.Style.STROKE && paint.getStrokeWidth() == 0.0f) {
            return;
        }
        canvas.drawText(str, 0, str.length(), 0.0f, 0.0f, paint);
    }

    private void Q(FontCharacter fontCharacter, Matrix matrix, float f10, DocumentData documentData, Canvas canvas) {
        List<ContentGroup> X = X(fontCharacter);
        for (int i10 = 0; i10 < X.size(); i10++) {
            Path path = X.get(i10).getPath();
            path.computeBounds(this.C, false);
            this.D.set(matrix);
            this.D.preTranslate(0.0f, (-documentData.f14601g) * s4.h.f());
            this.D.preScale(f10, f10);
            path.transform(this.D);
            if (documentData.f14605k) {
                T(path, this.E, canvas);
                T(path, this.F, canvas);
            } else {
                T(path, this.F, canvas);
                T(path, this.E, canvas);
            }
        }
    }

    private void R(String str, DocumentData documentData, Canvas canvas) {
        if (documentData.f14605k) {
            P(str, this.E, canvas);
            P(str, this.F, canvas);
        } else {
            P(str, this.F, canvas);
            P(str, this.E, canvas);
        }
    }

    private void S(String str, DocumentData documentData, Canvas canvas, float f10) {
        int i10 = 0;
        while (i10 < str.length()) {
            String O = O(str, i10);
            i10 += O.length();
            R(O, documentData, canvas);
            canvas.translate(this.E.measureText(O) + f10, 0.0f);
        }
    }

    private void T(Path path, Paint paint, Canvas canvas) {
        if (paint.getColor() == 0) {
            return;
        }
        if (paint.getStyle() == Paint.Style.STROKE && paint.getStrokeWidth() == 0.0f) {
            return;
        }
        canvas.drawPath(path, paint);
    }

    private void U(String str, DocumentData documentData, Matrix matrix, Font font, Canvas canvas, float f10, float f11) {
        float floatValue;
        for (int i10 = 0; i10 < str.length(); i10++) {
            FontCharacter e10 = this.K.c().e(FontCharacter.c(str.charAt(i10), font.a(), font.c()));
            if (e10 != null) {
                Q(e10, matrix, f11, documentData, canvas);
                float b10 = ((float) e10.b()) * f11 * s4.h.f() * f10;
                float f12 = documentData.f14599e / 10.0f;
                BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation = this.S;
                if (baseKeyframeAnimation != null) {
                    floatValue = baseKeyframeAnimation.h().floatValue();
                } else {
                    BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.R;
                    if (baseKeyframeAnimation2 != null) {
                        floatValue = baseKeyframeAnimation2.h().floatValue();
                    }
                    canvas.translate(b10 + (f12 * f10), 0.0f);
                }
                f12 += floatValue;
                canvas.translate(b10 + (f12 * f10), 0.0f);
            }
        }
    }

    private void V(DocumentData documentData, Matrix matrix, Font font, Canvas canvas) {
        float f10;
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation = this.T;
        if (baseKeyframeAnimation != null) {
            f10 = baseKeyframeAnimation.h().floatValue();
        } else {
            f10 = documentData.f14597c;
        }
        float f11 = f10 / 100.0f;
        float h10 = s4.h.h(matrix);
        String str = documentData.f14595a;
        float f12 = documentData.f14600f * s4.h.f();
        List<String> Z = Z(str);
        int size = Z.size();
        for (int i10 = 0; i10 < size; i10++) {
            String str2 = Z.get(i10);
            float Y = Y(str2, font, f11, h10);
            canvas.save();
            N(documentData.f14598d, canvas, Y);
            canvas.translate(0.0f, (i10 * f12) - (((size - 1) * f12) / 2.0f));
            U(str2, documentData, matrix, font, canvas, h10, f11);
            canvas.restore();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x00a9 A[LOOP:0: B:17:0x00a7->B:18:0x00a9, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void W(DocumentData documentData, Font font, Canvas canvas) {
        Typeface a02;
        float f10;
        float floatValue;
        int size;
        if (!TextUtils.isEmpty(font.a()) && font.a().contains("ColorFont")) {
            a02 = s4.h.e(Typeface.create(Typeface.DEFAULT, 0), font.c());
        } else {
            a02 = a0(font);
        }
        if (a02 == null) {
            return;
        }
        String str = documentData.f14595a;
        this.J.G();
        this.E.setTypeface(a02);
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation = this.T;
        if (baseKeyframeAnimation != null) {
            f10 = baseKeyframeAnimation.h().floatValue();
        } else {
            f10 = documentData.f14597c;
        }
        this.E.setTextSize(s4.h.f() * f10);
        this.F.setTypeface(this.E.getTypeface());
        this.F.setTextSize(this.E.getTextSize());
        float f11 = documentData.f14600f * s4.h.f();
        float f12 = documentData.f14599e / 10.0f;
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.S;
        if (baseKeyframeAnimation2 != null) {
            floatValue = baseKeyframeAnimation2.h().floatValue();
        } else {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation3 = this.R;
            if (baseKeyframeAnimation3 != null) {
                floatValue = baseKeyframeAnimation3.h().floatValue();
            }
            float f13 = ((f12 * s4.h.f()) * f10) / 100.0f;
            List<String> Z = Z(str);
            size = Z.size();
            for (int i10 = 0; i10 < size; i10++) {
                String str2 = Z.get(i10);
                float measureText = this.F.measureText(str2) + ((str2.length() - 1) * f13);
                canvas.save();
                N(documentData.f14598d, canvas, measureText);
                canvas.translate(0.0f, (i10 * f11) - (((size - 1) * f11) / 2.0f));
                S(str2, documentData, canvas, f13);
                canvas.restore();
            }
        }
        f12 += floatValue;
        float f132 = ((f12 * s4.h.f()) * f10) / 100.0f;
        List<String> Z2 = Z(str);
        size = Z2.size();
        while (i10 < size) {
        }
    }

    private List<ContentGroup> X(FontCharacter fontCharacter) {
        if (this.G.containsKey(fontCharacter)) {
            return this.G.get(fontCharacter);
        }
        List<ShapeGroup> a10 = fontCharacter.a();
        int size = a10.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i10 = 0; i10 < size; i10++) {
            arrayList.add(new ContentGroup(this.J, this, a10.get(i10)));
        }
        this.G.put(fontCharacter, arrayList);
        return arrayList;
    }

    private float Y(String str, Font font, float f10, float f11) {
        float f12 = 0.0f;
        for (int i10 = 0; i10 < str.length(); i10++) {
            FontCharacter e10 = this.K.c().e(FontCharacter.c(str.charAt(i10), font.a(), font.c()));
            if (e10 != null) {
                f12 = (float) (f12 + (e10.b() * f10 * s4.h.f() * f11));
            }
        }
        return f12;
    }

    private List<String> Z(String str) {
        return Arrays.asList(str.replaceAll("\r\n", "\r").replaceAll("\n", "\r").split("\r"));
    }

    private Typeface a0(Font font) {
        Typeface h10;
        BaseKeyframeAnimation<Typeface, Typeface> baseKeyframeAnimation = this.U;
        if (baseKeyframeAnimation != null && (h10 = baseKeyframeAnimation.h()) != null) {
            return h10;
        }
        Typeface H = this.J.H(font.a(), font.c());
        return H != null ? H : font.d();
    }

    private boolean b0(int i10) {
        return Character.getType(i10) == 16 || Character.getType(i10) == 27 || Character.getType(i10) == 6 || Character.getType(i10) == 28 || Character.getType(i10) == 19;
    }

    @Override // o4.BaseLayer, l4.KeyPathElement
    public <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback) {
        super.c(t7, effectiveValueCallback);
        if (t7 == EffectiveAnimationProperty.f9675a) {
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation = this.M;
            if (baseKeyframeAnimation != null) {
                F(baseKeyframeAnimation);
            }
            if (effectiveValueCallback == null) {
                this.M = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.M = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.a(this);
            h(this.M);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9676b) {
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2 = this.O;
            if (baseKeyframeAnimation2 != null) {
                F(baseKeyframeAnimation2);
            }
            if (effectiveValueCallback == null) {
                this.O = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation2 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.O = valueCallbackKeyframeAnimation2;
            valueCallbackKeyframeAnimation2.a(this);
            h(this.O);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9693s) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation3 = this.Q;
            if (baseKeyframeAnimation3 != null) {
                F(baseKeyframeAnimation3);
            }
            if (effectiveValueCallback == null) {
                this.Q = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation3 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.Q = valueCallbackKeyframeAnimation3;
            valueCallbackKeyframeAnimation3.a(this);
            h(this.Q);
            return;
        }
        if (t7 == EffectiveAnimationProperty.f9694t) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation4 = this.S;
            if (baseKeyframeAnimation4 != null) {
                F(baseKeyframeAnimation4);
            }
            if (effectiveValueCallback == null) {
                this.S = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation4 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.S = valueCallbackKeyframeAnimation4;
            valueCallbackKeyframeAnimation4.a(this);
            h(this.S);
            return;
        }
        if (t7 == EffectiveAnimationProperty.F) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation5 = this.T;
            if (baseKeyframeAnimation5 != null) {
                F(baseKeyframeAnimation5);
            }
            if (effectiveValueCallback == null) {
                this.T = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation5 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.T = valueCallbackKeyframeAnimation5;
            valueCallbackKeyframeAnimation5.a(this);
            h(this.T);
            return;
        }
        if (t7 == EffectiveAnimationProperty.M) {
            BaseKeyframeAnimation<Typeface, Typeface> baseKeyframeAnimation6 = this.U;
            if (baseKeyframeAnimation6 != null) {
                F(baseKeyframeAnimation6);
            }
            if (effectiveValueCallback == null) {
                this.U = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation6 = new ValueCallbackKeyframeAnimation(effectiveValueCallback);
            this.U = valueCallbackKeyframeAnimation6;
            valueCallbackKeyframeAnimation6.a(this);
            h(this.U);
        }
    }

    @Override // o4.BaseLayer, i4.DrawingContent
    public void e(RectF rectF, Matrix matrix, boolean z10) {
        super.e(rectF, matrix, z10);
        rectF.set(0.0f, 0.0f, this.K.b().width(), this.K.b().height());
    }

    @Override // o4.BaseLayer
    void s(Canvas canvas, Matrix matrix, int i10) {
        canvas.save();
        if (!this.J.o0()) {
            canvas.concat(matrix);
        }
        DocumentData h10 = this.I.h();
        Font font = this.K.h().get(h10.f14596b);
        if (font == null) {
            canvas.restore();
            return;
        }
        BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation = this.M;
        if (baseKeyframeAnimation != null) {
            this.E.setColor(baseKeyframeAnimation.h().intValue());
        } else {
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2 = this.L;
            if (baseKeyframeAnimation2 != null) {
                this.E.setColor(baseKeyframeAnimation2.h().intValue());
            } else {
                this.E.setColor(h10.f14602h);
            }
        }
        BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation3 = this.O;
        if (baseKeyframeAnimation3 != null) {
            this.F.setColor(baseKeyframeAnimation3.h().intValue());
        } else {
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation4 = this.N;
            if (baseKeyframeAnimation4 != null) {
                this.F.setColor(baseKeyframeAnimation4.h().intValue());
            } else {
                this.F.setColor(h10.f14603i);
            }
        }
        int intValue = ((this.f16207v.h() == null ? 100 : this.f16207v.h().h().intValue()) * 255) / 100;
        this.E.setAlpha(intValue);
        this.F.setAlpha(intValue);
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation5 = this.Q;
        if (baseKeyframeAnimation5 != null) {
            this.F.setStrokeWidth(baseKeyframeAnimation5.h().floatValue());
        } else {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation6 = this.P;
            if (baseKeyframeAnimation6 != null) {
                this.F.setStrokeWidth(baseKeyframeAnimation6.h().floatValue());
            } else {
                this.F.setStrokeWidth(h10.f14604j * s4.h.f() * s4.h.h(matrix));
            }
        }
        if (this.J.o0()) {
            V(h10, matrix, font, canvas);
        } else {
            W(h10, font, canvas);
        }
        canvas.restore();
    }
}
