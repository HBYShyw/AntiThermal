package hc;

import xc.JvmPrimitiveType;
import za.DefaultConstructorMarker;

/* compiled from: methodSignatureMapping.kt */
/* loaded from: classes2.dex */
public abstract class m {

    /* renamed from: a, reason: collision with root package name */
    public static final b f12184a = new b(null);

    /* renamed from: b, reason: collision with root package name */
    private static final d f12185b = new d(JvmPrimitiveType.BOOLEAN);

    /* renamed from: c, reason: collision with root package name */
    private static final d f12186c = new d(JvmPrimitiveType.CHAR);

    /* renamed from: d, reason: collision with root package name */
    private static final d f12187d = new d(JvmPrimitiveType.BYTE);

    /* renamed from: e, reason: collision with root package name */
    private static final d f12188e = new d(JvmPrimitiveType.SHORT);

    /* renamed from: f, reason: collision with root package name */
    private static final d f12189f = new d(JvmPrimitiveType.INT);

    /* renamed from: g, reason: collision with root package name */
    private static final d f12190g = new d(JvmPrimitiveType.FLOAT);

    /* renamed from: h, reason: collision with root package name */
    private static final d f12191h = new d(JvmPrimitiveType.LONG);

    /* renamed from: i, reason: collision with root package name */
    private static final d f12192i = new d(JvmPrimitiveType.DOUBLE);

    /* compiled from: methodSignatureMapping.kt */
    /* loaded from: classes2.dex */
    public static final class a extends m {

        /* renamed from: j, reason: collision with root package name */
        private final m f12193j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(m mVar) {
            super(null);
            za.k.e(mVar, "elementType");
            this.f12193j = mVar;
        }

        public final m i() {
            return this.f12193j;
        }
    }

    /* compiled from: methodSignatureMapping.kt */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final d a() {
            return m.f12185b;
        }

        public final d b() {
            return m.f12187d;
        }

        public final d c() {
            return m.f12186c;
        }

        public final d d() {
            return m.f12192i;
        }

        public final d e() {
            return m.f12190g;
        }

        public final d f() {
            return m.f12189f;
        }

        public final d g() {
            return m.f12191h;
        }

        public final d h() {
            return m.f12188e;
        }
    }

    /* compiled from: methodSignatureMapping.kt */
    /* loaded from: classes2.dex */
    public static final class c extends m {

        /* renamed from: j, reason: collision with root package name */
        private final String f12194j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(String str) {
            super(null);
            za.k.e(str, "internalName");
            this.f12194j = str;
        }

        public final String i() {
            return this.f12194j;
        }
    }

    /* compiled from: methodSignatureMapping.kt */
    /* loaded from: classes2.dex */
    public static final class d extends m {

        /* renamed from: j, reason: collision with root package name */
        private final JvmPrimitiveType f12195j;

        public d(JvmPrimitiveType jvmPrimitiveType) {
            super(null);
            this.f12195j = jvmPrimitiveType;
        }

        public final JvmPrimitiveType i() {
            return this.f12195j;
        }
    }

    private m() {
    }

    public /* synthetic */ m(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public String toString() {
        return o.f12196a.a(this);
    }
}
