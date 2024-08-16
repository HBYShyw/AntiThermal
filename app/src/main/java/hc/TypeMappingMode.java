package hc;

import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import gd.Variance;
import za.DefaultConstructorMarker;

/* compiled from: TypeMappingMode.kt */
/* renamed from: hc.b0, reason: use source file name */
/* loaded from: classes2.dex */
public final class TypeMappingMode {

    /* renamed from: k, reason: collision with root package name */
    public static final a f12118k = new a(null);

    /* renamed from: l, reason: collision with root package name */
    public static final TypeMappingMode f12119l;

    /* renamed from: m, reason: collision with root package name */
    public static final TypeMappingMode f12120m;

    /* renamed from: n, reason: collision with root package name */
    public static final TypeMappingMode f12121n;

    /* renamed from: o, reason: collision with root package name */
    public static final TypeMappingMode f12122o;

    /* renamed from: p, reason: collision with root package name */
    public static final TypeMappingMode f12123p;

    /* renamed from: q, reason: collision with root package name */
    public static final TypeMappingMode f12124q;

    /* renamed from: r, reason: collision with root package name */
    public static final TypeMappingMode f12125r;

    /* renamed from: s, reason: collision with root package name */
    public static final TypeMappingMode f12126s;

    /* renamed from: t, reason: collision with root package name */
    public static final TypeMappingMode f12127t;

    /* renamed from: a, reason: collision with root package name */
    private final boolean f12128a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f12129b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f12130c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f12131d;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f12132e;

    /* renamed from: f, reason: collision with root package name */
    private final TypeMappingMode f12133f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f12134g;

    /* renamed from: h, reason: collision with root package name */
    private final TypeMappingMode f12135h;

    /* renamed from: i, reason: collision with root package name */
    private final TypeMappingMode f12136i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f12137j;

    /* compiled from: TypeMappingMode.kt */
    /* renamed from: hc.b0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: TypeMappingMode.kt */
    /* renamed from: hc.b0$b */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f12138a;

        static {
            int[] iArr = new int[Variance.values().length];
            try {
                iArr[Variance.IN_VARIANCE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[Variance.INVARIANT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            f12138a = iArr;
        }
    }

    static {
        boolean z10 = false;
        boolean z11 = false;
        boolean z12 = false;
        boolean z13 = false;
        TypeMappingMode typeMappingMode = null;
        boolean z14 = false;
        TypeMappingMode typeMappingMode2 = null;
        TypeMappingMode typeMappingMode3 = null;
        boolean z15 = false;
        DefaultConstructorMarker defaultConstructorMarker = null;
        TypeMappingMode typeMappingMode4 = new TypeMappingMode(z10, false, z11, z12, z13, typeMappingMode, z14, typeMappingMode2, typeMappingMode3, z15, 1023, defaultConstructorMarker);
        f12119l = typeMappingMode4;
        boolean z16 = false;
        boolean z17 = false;
        boolean z18 = false;
        boolean z19 = false;
        boolean z20 = false;
        boolean z21 = false;
        TypeMappingMode typeMappingMode5 = null;
        TypeMappingMode typeMappingMode6 = null;
        boolean z22 = true;
        DefaultConstructorMarker defaultConstructorMarker2 = null;
        TypeMappingMode typeMappingMode7 = new TypeMappingMode(z16, z17, z18, z19, z20, null, z21, typeMappingMode5, typeMappingMode6, z22, DataLinkConstants.USER_HABIT, defaultConstructorMarker2);
        f12120m = typeMappingMode7;
        f12121n = new TypeMappingMode(z10, true, z11, z12, z13, typeMappingMode, z14, typeMappingMode2, typeMappingMode3, z15, 1021, defaultConstructorMarker);
        int i10 = 988;
        f12122o = new TypeMappingMode(z10, false, z11, z12, z13, typeMappingMode4, z14, typeMappingMode2, typeMappingMode3, z15, i10, defaultConstructorMarker);
        f12123p = new TypeMappingMode(z16, z17, z18, z19, z20, typeMappingMode7, z21, typeMappingMode5, typeMappingMode6, z22, 476, defaultConstructorMarker2);
        f12124q = new TypeMappingMode(z10, true, z11, z12, z13, typeMappingMode4, z14, typeMappingMode2, typeMappingMode3, z15, i10, defaultConstructorMarker);
        boolean z23 = false;
        boolean z24 = true;
        f12125r = new TypeMappingMode(z10, z23, z11, z24, z13, typeMappingMode4, z14, typeMappingMode2, typeMappingMode3, z15, 983, defaultConstructorMarker);
        f12126s = new TypeMappingMode(z10, z23, z11, z24, z13, typeMappingMode4, z14, typeMappingMode2, typeMappingMode3, z15, 919, defaultConstructorMarker);
        f12127t = new TypeMappingMode(z10, z23, true, false, z13, typeMappingMode4, z14, typeMappingMode2, typeMappingMode3, z15, 984, defaultConstructorMarker);
    }

    public TypeMappingMode(boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, TypeMappingMode typeMappingMode, boolean z15, TypeMappingMode typeMappingMode2, TypeMappingMode typeMappingMode3, boolean z16) {
        this.f12128a = z10;
        this.f12129b = z11;
        this.f12130c = z12;
        this.f12131d = z13;
        this.f12132e = z14;
        this.f12133f = typeMappingMode;
        this.f12134g = z15;
        this.f12135h = typeMappingMode2;
        this.f12136i = typeMappingMode3;
        this.f12137j = z16;
    }

    public final boolean a() {
        return this.f12134g;
    }

    public final boolean b() {
        return this.f12137j;
    }

    public final boolean c() {
        return this.f12129b;
    }

    public final boolean d() {
        return this.f12128a;
    }

    public final boolean e() {
        return this.f12130c;
    }

    public final TypeMappingMode f(Variance variance, boolean z10) {
        TypeMappingMode typeMappingMode;
        za.k.e(variance, "effectiveVariance");
        if (z10 && this.f12130c) {
            return this;
        }
        int i10 = b.f12138a[variance.ordinal()];
        if (i10 == 1) {
            typeMappingMode = this.f12135h;
            if (typeMappingMode == null) {
                return this;
            }
        } else if (i10 != 2) {
            typeMappingMode = this.f12133f;
            if (typeMappingMode == null) {
                return this;
            }
        } else {
            typeMappingMode = this.f12136i;
            if (typeMappingMode == null) {
                return this;
            }
        }
        return typeMappingMode;
    }

    public final TypeMappingMode g() {
        return new TypeMappingMode(this.f12128a, true, this.f12130c, this.f12131d, this.f12132e, this.f12133f, this.f12134g, this.f12135h, this.f12136i, false, 512, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ TypeMappingMode(boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, TypeMappingMode typeMappingMode, boolean z15, TypeMappingMode typeMappingMode2, TypeMappingMode typeMappingMode3, boolean z16, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(z10, z11, z12, z13, z14, typeMappingMode, (i10 & 64) != 0 ? true : z15, (i10 & 128) != 0 ? typeMappingMode : typeMappingMode2, (i10 & 256) != 0 ? typeMappingMode : typeMappingMode3, (i10 & 512) != 0 ? false : z16);
        z10 = (i10 & 1) != 0 ? true : z10;
        z11 = (i10 & 2) != 0 ? true : z11;
        z12 = (i10 & 4) != 0 ? false : z12;
        z13 = (i10 & 8) != 0 ? false : z13;
        z14 = (i10 & 16) != 0 ? false : z14;
        typeMappingMode = (i10 & 32) != 0 ? null : typeMappingMode;
    }
}
