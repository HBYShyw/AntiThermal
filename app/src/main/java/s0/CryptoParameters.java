package s0;

import java.security.Key;
import t0.InvalidArgumentException;

/* compiled from: CryptoParameters.java */
/* renamed from: s0.g, reason: use source file name */
/* loaded from: classes.dex */
public class CryptoParameters {

    /* renamed from: a, reason: collision with root package name */
    private final b f17966a;

    /* renamed from: b, reason: collision with root package name */
    private final Key f17967b;

    /* renamed from: c, reason: collision with root package name */
    private final byte[] f17968c;

    /* renamed from: d, reason: collision with root package name */
    private final byte[] f17969d;

    /* renamed from: e, reason: collision with root package name */
    private final byte[] f17970e;

    /* renamed from: f, reason: collision with root package name */
    private final int f17971f;

    /* renamed from: g, reason: collision with root package name */
    private final String f17972g;

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* compiled from: CryptoParameters.java */
    /* renamed from: s0.g$b */
    /* loaded from: classes.dex */
    public static abstract class b {

        /* renamed from: e, reason: collision with root package name */
        public static final b f17973e;

        /* renamed from: f, reason: collision with root package name */
        public static final b f17974f;

        /* renamed from: g, reason: collision with root package name */
        public static final b f17975g;

        /* renamed from: h, reason: collision with root package name */
        private static final /* synthetic */ b[] f17976h;

        /* compiled from: CryptoParameters.java */
        /* renamed from: s0.g$b$a */
        /* loaded from: classes.dex */
        enum a extends b {
            a(String str, int i10) {
                super(str, i10);
            }

            @Override // s0.CryptoParameters.b
            public String b() {
                return "AES/GCM/NoPadding";
            }
        }

        /* compiled from: CryptoParameters.java */
        /* renamed from: s0.g$b$b, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        enum C0099b extends b {
            C0099b(String str, int i10) {
                super(str, i10);
            }

            @Override // s0.CryptoParameters.b
            public String b() {
                return "AES/CTR/NoPadding";
            }
        }

        /* compiled from: CryptoParameters.java */
        /* renamed from: s0.g$b$c */
        /* loaded from: classes.dex */
        enum c extends b {
            c(String str, int i10) {
                super(str, i10);
            }

            @Override // s0.CryptoParameters.b
            public String b() {
                return "AES/CBC/PKCS5Padding";
            }
        }

        static {
            a aVar = new a("AES_GCM_NoPadding", 0);
            f17973e = aVar;
            C0099b c0099b = new C0099b("AES_CTR_NoPadding", 1);
            f17974f = c0099b;
            c cVar = new c("AES_CBC_PKCS5Padding", 2);
            f17975g = cVar;
            f17976h = new b[]{aVar, c0099b, cVar};
        }

        private b(String str, int i10) {
        }

        public static b a(String str) {
            if (str.equals("AES/GCM/NoPadding")) {
                return f17973e;
            }
            if (str.equals("AES/CTR/NoPadding")) {
                return f17974f;
            }
            if (str.equals("AES/CBC/PKCS5Padding")) {
                return f17975g;
            }
            return null;
        }

        public static b valueOf(String str) {
            return (b) Enum.valueOf(b.class, str);
        }

        public static b[] values() {
            return (b[]) f17976h.clone();
        }

        public abstract String b();
    }

    /* compiled from: CryptoParameters.java */
    /* renamed from: s0.g$c */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        private byte[] f17977a = null;

        /* renamed from: b, reason: collision with root package name */
        private Key f17978b = null;

        /* renamed from: c, reason: collision with root package name */
        private b f17979c = null;

        /* renamed from: d, reason: collision with root package name */
        private byte[] f17980d = null;

        /* renamed from: e, reason: collision with root package name */
        private byte[] f17981e = null;

        /* renamed from: f, reason: collision with root package name */
        private int f17982f = 128;

        /* renamed from: g, reason: collision with root package name */
        private String f17983g = null;

        public CryptoParameters h() {
            CryptoParameters cryptoParameters = new CryptoParameters(this);
            if (cryptoParameters.f17967b != null) {
                if (cryptoParameters.f17970e != null) {
                    return cryptoParameters;
                }
                throw new InvalidArgumentException("The cryptoText has not been set");
            }
            throw new InvalidArgumentException("The key has not been set");
        }

        public c i(byte[] bArr) {
            this.f17981e = bArr;
            return this;
        }

        public c j(b bVar) {
            this.f17979c = bVar;
            return this;
        }

        public c k(byte[] bArr) {
            this.f17977a = bArr;
            return this;
        }

        public c l(byte[] bArr) {
            this.f17980d = bArr;
            return this;
        }

        public c m(Key key) {
            this.f17978b = key;
            return this;
        }
    }

    public byte[] c() {
        return this.f17969d;
    }

    public b d() {
        return this.f17966a;
    }

    public int e() {
        return this.f17971f;
    }

    public byte[] f() {
        return this.f17970e;
    }

    public byte[] g() {
        return this.f17968c;
    }

    public Key h() {
        return this.f17967b;
    }

    private CryptoParameters(c cVar) {
        this.f17966a = cVar.f17979c;
        this.f17967b = cVar.f17978b;
        this.f17968c = cVar.f17980d;
        this.f17969d = cVar.f17981e;
        this.f17970e = cVar.f17977a;
        this.f17971f = cVar.f17982f;
        this.f17972g = cVar.f17983g;
    }
}
