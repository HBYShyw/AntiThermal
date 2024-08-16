package c4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import com.google.android.material.R$styleable;

/* compiled from: ShapeAppearanceModel.java */
/* renamed from: c4.m, reason: use source file name */
/* loaded from: classes.dex */
public class ShapeAppearanceModel {

    /* renamed from: m, reason: collision with root package name */
    public static final CornerSize f4815m = new RelativeCornerSize(0.5f);

    /* renamed from: a, reason: collision with root package name */
    CornerTreatment f4816a;

    /* renamed from: b, reason: collision with root package name */
    CornerTreatment f4817b;

    /* renamed from: c, reason: collision with root package name */
    CornerTreatment f4818c;

    /* renamed from: d, reason: collision with root package name */
    CornerTreatment f4819d;

    /* renamed from: e, reason: collision with root package name */
    CornerSize f4820e;

    /* renamed from: f, reason: collision with root package name */
    CornerSize f4821f;

    /* renamed from: g, reason: collision with root package name */
    CornerSize f4822g;

    /* renamed from: h, reason: collision with root package name */
    CornerSize f4823h;

    /* renamed from: i, reason: collision with root package name */
    EdgeTreatment f4824i;

    /* renamed from: j, reason: collision with root package name */
    EdgeTreatment f4825j;

    /* renamed from: k, reason: collision with root package name */
    EdgeTreatment f4826k;

    /* renamed from: l, reason: collision with root package name */
    EdgeTreatment f4827l;

    /* compiled from: ShapeAppearanceModel.java */
    /* renamed from: c4.m$c */
    /* loaded from: classes.dex */
    public interface c {
        CornerSize a(CornerSize cornerSize);
    }

    public static b a() {
        return new b();
    }

    public static b b(Context context, int i10, int i11) {
        return c(context, i10, i11, 0);
    }

    private static b c(Context context, int i10, int i11, int i12) {
        return d(context, i10, i11, new AbsoluteCornerSize(i12));
    }

    private static b d(Context context, int i10, int i11, CornerSize cornerSize) {
        if (i11 != 0) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, i10);
            i10 = i11;
            context = contextThemeWrapper;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i10, R$styleable.ShapeAppearance);
        try {
            int i12 = obtainStyledAttributes.getInt(R$styleable.ShapeAppearance_cornerFamily, 0);
            int i13 = obtainStyledAttributes.getInt(R$styleable.ShapeAppearance_cornerFamilyTopLeft, i12);
            int i14 = obtainStyledAttributes.getInt(R$styleable.ShapeAppearance_cornerFamilyTopRight, i12);
            int i15 = obtainStyledAttributes.getInt(R$styleable.ShapeAppearance_cornerFamilyBottomRight, i12);
            int i16 = obtainStyledAttributes.getInt(R$styleable.ShapeAppearance_cornerFamilyBottomLeft, i12);
            CornerSize m10 = m(obtainStyledAttributes, R$styleable.ShapeAppearance_cornerSize, cornerSize);
            CornerSize m11 = m(obtainStyledAttributes, R$styleable.ShapeAppearance_cornerSizeTopLeft, m10);
            CornerSize m12 = m(obtainStyledAttributes, R$styleable.ShapeAppearance_cornerSizeTopRight, m10);
            CornerSize m13 = m(obtainStyledAttributes, R$styleable.ShapeAppearance_cornerSizeBottomRight, m10);
            return new b().H(i13, m11).M(i14, m12).z(i15, m13).u(i16, m(obtainStyledAttributes, R$styleable.ShapeAppearance_cornerSizeBottomLeft, m10));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static b e(Context context, AttributeSet attributeSet, int i10, int i11) {
        return f(context, attributeSet, i10, i11, 0);
    }

    public static b f(Context context, AttributeSet attributeSet, int i10, int i11, int i12) {
        return g(context, attributeSet, i10, i11, new AbsoluteCornerSize(i12));
    }

    public static b g(Context context, AttributeSet attributeSet, int i10, int i11, CornerSize cornerSize) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.MaterialShape, i10, i11);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.MaterialShape_shapeAppearance, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(R$styleable.MaterialShape_shapeAppearanceOverlay, 0);
        obtainStyledAttributes.recycle();
        return d(context, resourceId, resourceId2, cornerSize);
    }

    private static CornerSize m(TypedArray typedArray, int i10, CornerSize cornerSize) {
        TypedValue peekValue = typedArray.peekValue(i10);
        if (peekValue == null) {
            return cornerSize;
        }
        int i11 = peekValue.type;
        if (i11 == 5) {
            return new AbsoluteCornerSize(TypedValue.complexToDimensionPixelSize(peekValue.data, typedArray.getResources().getDisplayMetrics()));
        }
        return i11 == 6 ? new RelativeCornerSize(peekValue.getFraction(1.0f, 1.0f)) : cornerSize;
    }

    public EdgeTreatment h() {
        return this.f4826k;
    }

    public CornerTreatment i() {
        return this.f4819d;
    }

    public CornerSize j() {
        return this.f4823h;
    }

    public CornerTreatment k() {
        return this.f4818c;
    }

    public CornerSize l() {
        return this.f4822g;
    }

    public EdgeTreatment n() {
        return this.f4827l;
    }

    public EdgeTreatment o() {
        return this.f4825j;
    }

    public EdgeTreatment p() {
        return this.f4824i;
    }

    public CornerTreatment q() {
        return this.f4816a;
    }

    public CornerSize r() {
        return this.f4820e;
    }

    public CornerTreatment s() {
        return this.f4817b;
    }

    public CornerSize t() {
        return this.f4821f;
    }

    public boolean u(RectF rectF) {
        boolean z10 = this.f4827l.getClass().equals(EdgeTreatment.class) && this.f4825j.getClass().equals(EdgeTreatment.class) && this.f4824i.getClass().equals(EdgeTreatment.class) && this.f4826k.getClass().equals(EdgeTreatment.class);
        float a10 = this.f4820e.a(rectF);
        return z10 && ((this.f4821f.a(rectF) > a10 ? 1 : (this.f4821f.a(rectF) == a10 ? 0 : -1)) == 0 && (this.f4823h.a(rectF) > a10 ? 1 : (this.f4823h.a(rectF) == a10 ? 0 : -1)) == 0 && (this.f4822g.a(rectF) > a10 ? 1 : (this.f4822g.a(rectF) == a10 ? 0 : -1)) == 0) && ((this.f4817b instanceof RoundedCornerTreatment) && (this.f4816a instanceof RoundedCornerTreatment) && (this.f4818c instanceof RoundedCornerTreatment) && (this.f4819d instanceof RoundedCornerTreatment));
    }

    public b v() {
        return new b(this);
    }

    public ShapeAppearanceModel w(float f10) {
        return v().o(f10).m();
    }

    public ShapeAppearanceModel x(CornerSize cornerSize) {
        return v().p(cornerSize).m();
    }

    public ShapeAppearanceModel y(c cVar) {
        return v().K(cVar.a(r())).P(cVar.a(t())).x(cVar.a(j())).C(cVar.a(l())).m();
    }

    private ShapeAppearanceModel(b bVar) {
        this.f4816a = bVar.f4828a;
        this.f4817b = bVar.f4829b;
        this.f4818c = bVar.f4830c;
        this.f4819d = bVar.f4831d;
        this.f4820e = bVar.f4832e;
        this.f4821f = bVar.f4833f;
        this.f4822g = bVar.f4834g;
        this.f4823h = bVar.f4835h;
        this.f4824i = bVar.f4836i;
        this.f4825j = bVar.f4837j;
        this.f4826k = bVar.f4838k;
        this.f4827l = bVar.f4839l;
    }

    /* compiled from: ShapeAppearanceModel.java */
    /* renamed from: c4.m$b */
    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private CornerTreatment f4828a;

        /* renamed from: b, reason: collision with root package name */
        private CornerTreatment f4829b;

        /* renamed from: c, reason: collision with root package name */
        private CornerTreatment f4830c;

        /* renamed from: d, reason: collision with root package name */
        private CornerTreatment f4831d;

        /* renamed from: e, reason: collision with root package name */
        private CornerSize f4832e;

        /* renamed from: f, reason: collision with root package name */
        private CornerSize f4833f;

        /* renamed from: g, reason: collision with root package name */
        private CornerSize f4834g;

        /* renamed from: h, reason: collision with root package name */
        private CornerSize f4835h;

        /* renamed from: i, reason: collision with root package name */
        private EdgeTreatment f4836i;

        /* renamed from: j, reason: collision with root package name */
        private EdgeTreatment f4837j;

        /* renamed from: k, reason: collision with root package name */
        private EdgeTreatment f4838k;

        /* renamed from: l, reason: collision with root package name */
        private EdgeTreatment f4839l;

        public b() {
            this.f4828a = MaterialShapeUtils.b();
            this.f4829b = MaterialShapeUtils.b();
            this.f4830c = MaterialShapeUtils.b();
            this.f4831d = MaterialShapeUtils.b();
            this.f4832e = new AbsoluteCornerSize(0.0f);
            this.f4833f = new AbsoluteCornerSize(0.0f);
            this.f4834g = new AbsoluteCornerSize(0.0f);
            this.f4835h = new AbsoluteCornerSize(0.0f);
            this.f4836i = MaterialShapeUtils.c();
            this.f4837j = MaterialShapeUtils.c();
            this.f4838k = MaterialShapeUtils.c();
            this.f4839l = MaterialShapeUtils.c();
        }

        private static float n(CornerTreatment cornerTreatment) {
            if (cornerTreatment instanceof RoundedCornerTreatment) {
                return ((RoundedCornerTreatment) cornerTreatment).f4814a;
            }
            if (cornerTreatment instanceof CutCornerTreatment) {
                return ((CutCornerTreatment) cornerTreatment).f4762a;
            }
            return -1.0f;
        }

        public b A(CornerTreatment cornerTreatment) {
            this.f4830c = cornerTreatment;
            float n10 = n(cornerTreatment);
            if (n10 != -1.0f) {
                B(n10);
            }
            return this;
        }

        public b B(float f10) {
            this.f4834g = new AbsoluteCornerSize(f10);
            return this;
        }

        public b C(CornerSize cornerSize) {
            this.f4834g = cornerSize;
            return this;
        }

        public b D(EdgeTreatment edgeTreatment) {
            this.f4839l = edgeTreatment;
            return this;
        }

        public b E(EdgeTreatment edgeTreatment) {
            this.f4837j = edgeTreatment;
            return this;
        }

        public b F(EdgeTreatment edgeTreatment) {
            this.f4836i = edgeTreatment;
            return this;
        }

        public b G(int i10, float f10) {
            return I(MaterialShapeUtils.a(i10)).J(f10);
        }

        public b H(int i10, CornerSize cornerSize) {
            return I(MaterialShapeUtils.a(i10)).K(cornerSize);
        }

        public b I(CornerTreatment cornerTreatment) {
            this.f4828a = cornerTreatment;
            float n10 = n(cornerTreatment);
            if (n10 != -1.0f) {
                J(n10);
            }
            return this;
        }

        public b J(float f10) {
            this.f4832e = new AbsoluteCornerSize(f10);
            return this;
        }

        public b K(CornerSize cornerSize) {
            this.f4832e = cornerSize;
            return this;
        }

        public b L(int i10, float f10) {
            return N(MaterialShapeUtils.a(i10)).O(f10);
        }

        public b M(int i10, CornerSize cornerSize) {
            return N(MaterialShapeUtils.a(i10)).P(cornerSize);
        }

        public b N(CornerTreatment cornerTreatment) {
            this.f4829b = cornerTreatment;
            float n10 = n(cornerTreatment);
            if (n10 != -1.0f) {
                O(n10);
            }
            return this;
        }

        public b O(float f10) {
            this.f4833f = new AbsoluteCornerSize(f10);
            return this;
        }

        public b P(CornerSize cornerSize) {
            this.f4833f = cornerSize;
            return this;
        }

        public ShapeAppearanceModel m() {
            return new ShapeAppearanceModel(this);
        }

        public b o(float f10) {
            return J(f10).O(f10).B(f10).w(f10);
        }

        public b p(CornerSize cornerSize) {
            return K(cornerSize).P(cornerSize).C(cornerSize).x(cornerSize);
        }

        public b q(int i10, float f10) {
            return r(MaterialShapeUtils.a(i10)).o(f10);
        }

        public b r(CornerTreatment cornerTreatment) {
            return I(cornerTreatment).N(cornerTreatment).A(cornerTreatment).v(cornerTreatment);
        }

        public b s(EdgeTreatment edgeTreatment) {
            this.f4838k = edgeTreatment;
            return this;
        }

        public b t(int i10, float f10) {
            return v(MaterialShapeUtils.a(i10)).w(f10);
        }

        public b u(int i10, CornerSize cornerSize) {
            return v(MaterialShapeUtils.a(i10)).x(cornerSize);
        }

        public b v(CornerTreatment cornerTreatment) {
            this.f4831d = cornerTreatment;
            float n10 = n(cornerTreatment);
            if (n10 != -1.0f) {
                w(n10);
            }
            return this;
        }

        public b w(float f10) {
            this.f4835h = new AbsoluteCornerSize(f10);
            return this;
        }

        public b x(CornerSize cornerSize) {
            this.f4835h = cornerSize;
            return this;
        }

        public b y(int i10, float f10) {
            return A(MaterialShapeUtils.a(i10)).B(f10);
        }

        public b z(int i10, CornerSize cornerSize) {
            return A(MaterialShapeUtils.a(i10)).C(cornerSize);
        }

        public b(ShapeAppearanceModel shapeAppearanceModel) {
            this.f4828a = MaterialShapeUtils.b();
            this.f4829b = MaterialShapeUtils.b();
            this.f4830c = MaterialShapeUtils.b();
            this.f4831d = MaterialShapeUtils.b();
            this.f4832e = new AbsoluteCornerSize(0.0f);
            this.f4833f = new AbsoluteCornerSize(0.0f);
            this.f4834g = new AbsoluteCornerSize(0.0f);
            this.f4835h = new AbsoluteCornerSize(0.0f);
            this.f4836i = MaterialShapeUtils.c();
            this.f4837j = MaterialShapeUtils.c();
            this.f4838k = MaterialShapeUtils.c();
            this.f4839l = MaterialShapeUtils.c();
            this.f4828a = shapeAppearanceModel.f4816a;
            this.f4829b = shapeAppearanceModel.f4817b;
            this.f4830c = shapeAppearanceModel.f4818c;
            this.f4831d = shapeAppearanceModel.f4819d;
            this.f4832e = shapeAppearanceModel.f4820e;
            this.f4833f = shapeAppearanceModel.f4821f;
            this.f4834g = shapeAppearanceModel.f4822g;
            this.f4835h = shapeAppearanceModel.f4823h;
            this.f4836i = shapeAppearanceModel.f4824i;
            this.f4837j = shapeAppearanceModel.f4825j;
            this.f4838k = shapeAppearanceModel.f4826k;
            this.f4839l = shapeAppearanceModel.f4827l;
        }
    }

    public ShapeAppearanceModel() {
        this.f4816a = MaterialShapeUtils.b();
        this.f4817b = MaterialShapeUtils.b();
        this.f4818c = MaterialShapeUtils.b();
        this.f4819d = MaterialShapeUtils.b();
        this.f4820e = new AbsoluteCornerSize(0.0f);
        this.f4821f = new AbsoluteCornerSize(0.0f);
        this.f4822g = new AbsoluteCornerSize(0.0f);
        this.f4823h = new AbsoluteCornerSize(0.0f);
        this.f4824i = MaterialShapeUtils.c();
        this.f4825j = MaterialShapeUtils.c();
        this.f4826k = MaterialShapeUtils.c();
        this.f4827l = MaterialShapeUtils.c();
    }
}
