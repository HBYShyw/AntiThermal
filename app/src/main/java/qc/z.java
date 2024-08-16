package qc;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: WireFormat.java */
/* loaded from: classes2.dex */
public final class z {

    /* renamed from: a, reason: collision with root package name */
    static final int f17375a = c(1, 3);

    /* renamed from: b, reason: collision with root package name */
    static final int f17376b = c(1, 4);

    /* renamed from: c, reason: collision with root package name */
    static final int f17377c = c(2, 0);

    /* renamed from: d, reason: collision with root package name */
    static final int f17378d = c(3, 2);

    /* JADX WARN: Enum visitor error
    jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'i' uses external variables
    	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:451)
    	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:395)
    	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:324)
    	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:262)
    	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
    	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
     */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* compiled from: WireFormat.java */
    /* loaded from: classes2.dex */
    public static class b {

        /* renamed from: g, reason: collision with root package name */
        public static final b f17379g;

        /* renamed from: h, reason: collision with root package name */
        public static final b f17380h;

        /* renamed from: i, reason: collision with root package name */
        public static final b f17381i;

        /* renamed from: j, reason: collision with root package name */
        public static final b f17382j;

        /* renamed from: k, reason: collision with root package name */
        public static final b f17383k;

        /* renamed from: l, reason: collision with root package name */
        public static final b f17384l;

        /* renamed from: m, reason: collision with root package name */
        public static final b f17385m;

        /* renamed from: n, reason: collision with root package name */
        public static final b f17386n;

        /* renamed from: o, reason: collision with root package name */
        public static final b f17387o;

        /* renamed from: p, reason: collision with root package name */
        public static final b f17388p;

        /* renamed from: q, reason: collision with root package name */
        public static final b f17389q;

        /* renamed from: r, reason: collision with root package name */
        public static final b f17390r;

        /* renamed from: s, reason: collision with root package name */
        public static final b f17391s;

        /* renamed from: t, reason: collision with root package name */
        public static final b f17392t;

        /* renamed from: u, reason: collision with root package name */
        public static final b f17393u;

        /* renamed from: v, reason: collision with root package name */
        public static final b f17394v;

        /* renamed from: w, reason: collision with root package name */
        public static final b f17395w;

        /* renamed from: x, reason: collision with root package name */
        public static final b f17396x;

        /* renamed from: y, reason: collision with root package name */
        private static final /* synthetic */ b[] f17397y;

        /* renamed from: e, reason: collision with root package name */
        private final c f17398e;

        /* renamed from: f, reason: collision with root package name */
        private final int f17399f;

        /* compiled from: WireFormat.java */
        /* loaded from: classes2.dex */
        enum a extends b {
            a(String str, int i10, c cVar, int i11) {
                super(str, i10, cVar, i11);
            }

            @Override // qc.z.b
            public boolean c() {
                return false;
            }
        }

        /* compiled from: WireFormat.java */
        /* renamed from: qc.z$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        enum C0095b extends b {
            C0095b(String str, int i10, c cVar, int i11) {
                super(str, i10, cVar, i11);
            }

            @Override // qc.z.b
            public boolean c() {
                return false;
            }
        }

        /* compiled from: WireFormat.java */
        /* loaded from: classes2.dex */
        enum c extends b {
            c(String str, int i10, c cVar, int i11) {
                super(str, i10, cVar, i11);
            }

            @Override // qc.z.b
            public boolean c() {
                return false;
            }
        }

        /* compiled from: WireFormat.java */
        /* loaded from: classes2.dex */
        enum d extends b {
            d(String str, int i10, c cVar, int i11) {
                super(str, i10, cVar, i11);
            }

            @Override // qc.z.b
            public boolean c() {
                return false;
            }
        }

        static {
            b bVar = new b("DOUBLE", 0, c.DOUBLE, 1);
            f17379g = bVar;
            b bVar2 = new b("FLOAT", 1, c.FLOAT, 5);
            f17380h = bVar2;
            c cVar = c.LONG;
            b bVar3 = new b("INT64", 2, cVar, 0);
            f17381i = bVar3;
            b bVar4 = new b("UINT64", 3, cVar, 0);
            f17382j = bVar4;
            c cVar2 = c.INT;
            b bVar5 = new b("INT32", 4, cVar2, 0);
            f17383k = bVar5;
            b bVar6 = new b("FIXED64", 5, cVar, 1);
            f17384l = bVar6;
            b bVar7 = new b("FIXED32", 6, cVar2, 5);
            f17385m = bVar7;
            b bVar8 = new b("BOOL", 7, c.BOOLEAN, 0);
            f17386n = bVar8;
            a aVar = new a("STRING", 8, c.STRING, 2);
            f17387o = aVar;
            c cVar3 = c.MESSAGE;
            C0095b c0095b = new C0095b("GROUP", 9, cVar3, 3);
            f17388p = c0095b;
            c cVar4 = new c("MESSAGE", 10, cVar3, 2);
            f17389q = cVar4;
            d dVar = new d("BYTES", 11, c.BYTE_STRING, 2);
            f17390r = dVar;
            b bVar9 = new b("UINT32", 12, cVar2, 0);
            f17391s = bVar9;
            b bVar10 = new b("ENUM", 13, c.ENUM, 0);
            f17392t = bVar10;
            b bVar11 = new b("SFIXED32", 14, cVar2, 5);
            f17393u = bVar11;
            b bVar12 = new b("SFIXED64", 15, cVar, 1);
            f17394v = bVar12;
            b bVar13 = new b("SINT32", 16, cVar2, 0);
            f17395w = bVar13;
            b bVar14 = new b("SINT64", 17, cVar, 0);
            f17396x = bVar14;
            f17397y = new b[]{bVar, bVar2, bVar3, bVar4, bVar5, bVar6, bVar7, bVar8, aVar, c0095b, cVar4, dVar, bVar9, bVar10, bVar11, bVar12, bVar13, bVar14};
        }

        public static b valueOf(String str) {
            return (b) Enum.valueOf(b.class, str);
        }

        public static b[] values() {
            return (b[]) f17397y.clone();
        }

        public c a() {
            return this.f17398e;
        }

        public int b() {
            return this.f17399f;
        }

        public boolean c() {
            return true;
        }

        private b(String str, int i10, c cVar, int i11) {
            this.f17398e = cVar;
            this.f17399f = i11;
        }
    }

    /* compiled from: WireFormat.java */
    /* loaded from: classes2.dex */
    public enum c {
        INT(0),
        LONG(0L),
        FLOAT(Float.valueOf(0.0f)),
        DOUBLE(Double.valueOf(UserProfileInfo.Constant.NA_LAT_LON)),
        BOOLEAN(Boolean.FALSE),
        STRING(""),
        BYTE_STRING(d.f17259e),
        ENUM(null),
        MESSAGE(null);


        /* renamed from: e, reason: collision with root package name */
        private final Object f17410e;

        c(Object obj) {
            this.f17410e = obj;
        }
    }

    public static int a(int i10) {
        return i10 >>> 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(int i10) {
        return i10 & 7;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(int i10, int i11) {
        return (i10 << 3) | i11;
    }
}
