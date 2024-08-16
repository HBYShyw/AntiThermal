package ic;

import fb._Ranges;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.MapsJVM;
import kotlin.collections._ArraysJvm;
import kotlin.collections.r;
import nc.JvmMetadataVersion;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: KotlinClassHeader.kt */
/* renamed from: ic.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class KotlinClassHeader {

    /* renamed from: a, reason: collision with root package name */
    private final a f12705a;

    /* renamed from: b, reason: collision with root package name */
    private final JvmMetadataVersion f12706b;

    /* renamed from: c, reason: collision with root package name */
    private final String[] f12707c;

    /* renamed from: d, reason: collision with root package name */
    private final String[] f12708d;

    /* renamed from: e, reason: collision with root package name */
    private final String[] f12709e;

    /* renamed from: f, reason: collision with root package name */
    private final String f12710f;

    /* renamed from: g, reason: collision with root package name */
    private final int f12711g;

    /* renamed from: h, reason: collision with root package name */
    private final String f12712h;

    /* renamed from: i, reason: collision with root package name */
    private final byte[] f12713i;

    /* compiled from: KotlinClassHeader.kt */
    /* renamed from: ic.a$a */
    /* loaded from: classes2.dex */
    public enum a {
        UNKNOWN(0),
        CLASS(1),
        FILE_FACADE(2),
        SYNTHETIC_CLASS(3),
        MULTIFILE_CLASS(4),
        MULTIFILE_CLASS_PART(5);


        /* renamed from: f, reason: collision with root package name */
        public static final C0053a f12714f = new C0053a(null);

        /* renamed from: g, reason: collision with root package name */
        private static final Map<Integer, a> f12715g;

        /* renamed from: e, reason: collision with root package name */
        private final int f12723e;

        /* compiled from: KotlinClassHeader.kt */
        /* renamed from: ic.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0053a {
            private C0053a() {
            }

            public /* synthetic */ C0053a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final a a(int i10) {
                a aVar = (a) a.f12715g.get(Integer.valueOf(i10));
                return aVar == null ? a.UNKNOWN : aVar;
            }
        }

        static {
            int e10;
            int c10;
            a[] values = values();
            e10 = MapsJVM.e(values.length);
            c10 = _Ranges.c(e10, 16);
            LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
            for (a aVar : values) {
                linkedHashMap.put(Integer.valueOf(aVar.f12723e), aVar);
            }
            f12715g = linkedHashMap;
        }

        a(int i10) {
            this.f12723e = i10;
        }

        public static final a c(int i10) {
            return f12714f.a(i10);
        }
    }

    public KotlinClassHeader(a aVar, JvmMetadataVersion jvmMetadataVersion, String[] strArr, String[] strArr2, String[] strArr3, String str, int i10, String str2, byte[] bArr) {
        k.e(aVar, "kind");
        k.e(jvmMetadataVersion, "metadataVersion");
        this.f12705a = aVar;
        this.f12706b = jvmMetadataVersion;
        this.f12707c = strArr;
        this.f12708d = strArr2;
        this.f12709e = strArr3;
        this.f12710f = str;
        this.f12711g = i10;
        this.f12712h = str2;
        this.f12713i = bArr;
    }

    private final boolean h(int i10, int i11) {
        return (i10 & i11) != 0;
    }

    public final String[] a() {
        return this.f12707c;
    }

    public final String[] b() {
        return this.f12708d;
    }

    public final a c() {
        return this.f12705a;
    }

    public final JvmMetadataVersion d() {
        return this.f12706b;
    }

    public final String e() {
        String str = this.f12710f;
        if (this.f12705a == a.MULTIFILE_CLASS_PART) {
            return str;
        }
        return null;
    }

    public final List<String> f() {
        List<String> j10;
        String[] strArr = this.f12707c;
        if (!(this.f12705a == a.MULTIFILE_CLASS)) {
            strArr = null;
        }
        List<String> e10 = strArr != null ? _ArraysJvm.e(strArr) : null;
        if (e10 != null) {
            return e10;
        }
        j10 = r.j();
        return j10;
    }

    public final String[] g() {
        return this.f12709e;
    }

    public final boolean i() {
        return h(this.f12711g, 2);
    }

    public final boolean j() {
        return h(this.f12711g, 64) && !h(this.f12711g, 32);
    }

    public final boolean k() {
        return h(this.f12711g, 16) && !h(this.f12711g, 32);
    }

    public String toString() {
        return this.f12705a + " version=" + this.f12706b;
    }
}
