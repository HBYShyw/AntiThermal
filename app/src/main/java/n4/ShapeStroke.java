package n4;

import android.graphics.Paint;
import com.oplus.anim.EffectiveAnimationDrawable;
import i4.Content;
import i4.StrokeContent;
import java.util.List;
import m4.AnimatableColorValue;
import m4.AnimatableFloatValue;
import m4.AnimatableIntegerValue;
import o4.BaseLayer;

/* compiled from: ShapeStroke.java */
/* renamed from: n4.q, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeStroke implements ContentModel {

    /* renamed from: a, reason: collision with root package name */
    private final String f15844a;

    /* renamed from: b, reason: collision with root package name */
    private final AnimatableFloatValue f15845b;

    /* renamed from: c, reason: collision with root package name */
    private final List<AnimatableFloatValue> f15846c;

    /* renamed from: d, reason: collision with root package name */
    private final AnimatableColorValue f15847d;

    /* renamed from: e, reason: collision with root package name */
    private final AnimatableIntegerValue f15848e;

    /* renamed from: f, reason: collision with root package name */
    private final AnimatableFloatValue f15849f;

    /* renamed from: g, reason: collision with root package name */
    private final b f15850g;

    /* renamed from: h, reason: collision with root package name */
    private final c f15851h;

    /* renamed from: i, reason: collision with root package name */
    private final float f15852i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f15853j;

    /* compiled from: ShapeStroke.java */
    /* renamed from: n4.q$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f15854a;

        /* renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f15855b;

        static {
            int[] iArr = new int[c.values().length];
            f15855b = iArr;
            try {
                iArr[c.BEVEL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f15855b[c.MITER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f15855b[c.ROUND.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[b.values().length];
            f15854a = iArr2;
            try {
                iArr2[b.BUTT.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f15854a[b.ROUND.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f15854a[b.UNKNOWN.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    /* compiled from: ShapeStroke.java */
    /* renamed from: n4.q$b */
    /* loaded from: classes.dex */
    public enum b {
        BUTT,
        ROUND,
        UNKNOWN;

        public Paint.Cap a() {
            int i10 = a.f15854a[ordinal()];
            if (i10 == 1) {
                return Paint.Cap.BUTT;
            }
            if (i10 != 2) {
                return Paint.Cap.SQUARE;
            }
            return Paint.Cap.ROUND;
        }
    }

    /* compiled from: ShapeStroke.java */
    /* renamed from: n4.q$c */
    /* loaded from: classes.dex */
    public enum c {
        MITER,
        ROUND,
        BEVEL;

        public Paint.Join a() {
            int i10 = a.f15855b[ordinal()];
            if (i10 == 1) {
                return Paint.Join.BEVEL;
            }
            if (i10 == 2) {
                return Paint.Join.MITER;
            }
            if (i10 != 3) {
                return null;
            }
            return Paint.Join.ROUND;
        }
    }

    public ShapeStroke(String str, AnimatableFloatValue animatableFloatValue, List<AnimatableFloatValue> list, AnimatableColorValue animatableColorValue, AnimatableIntegerValue animatableIntegerValue, AnimatableFloatValue animatableFloatValue2, b bVar, c cVar, float f10, boolean z10) {
        this.f15844a = str;
        this.f15845b = animatableFloatValue;
        this.f15846c = list;
        this.f15847d = animatableColorValue;
        this.f15848e = animatableIntegerValue;
        this.f15849f = animatableFloatValue2;
        this.f15850g = bVar;
        this.f15851h = cVar;
        this.f15852i = f10;
        this.f15853j = z10;
    }

    @Override // n4.ContentModel
    public Content a(EffectiveAnimationDrawable effectiveAnimationDrawable, BaseLayer baseLayer) {
        return new StrokeContent(effectiveAnimationDrawable, baseLayer, this);
    }

    public b b() {
        return this.f15850g;
    }

    public AnimatableColorValue c() {
        return this.f15847d;
    }

    public AnimatableFloatValue d() {
        return this.f15845b;
    }

    public c e() {
        return this.f15851h;
    }

    public List<AnimatableFloatValue> f() {
        return this.f15846c;
    }

    public float g() {
        return this.f15852i;
    }

    public String h() {
        return this.f15844a;
    }

    public AnimatableIntegerValue i() {
        return this.f15848e;
    }

    public AnimatableFloatValue j() {
        return this.f15849f;
    }

    public boolean k() {
        return this.f15853j;
    }
}
