package b1;

import java.util.Date;
import java.util.Objects;

/* compiled from: EcKeyGenParameterSpec.java */
/* renamed from: b1.a, reason: use source file name */
/* loaded from: classes.dex */
public class EcKeyGenParameterSpec {

    /* renamed from: a, reason: collision with root package name */
    private final String f4530a;

    /* renamed from: b, reason: collision with root package name */
    private final int f4531b;

    /* renamed from: c, reason: collision with root package name */
    private final String f4532c;

    /* renamed from: d, reason: collision with root package name */
    private final Date f4533d;

    /* compiled from: EcKeyGenParameterSpec.java */
    /* renamed from: b1.a$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private final String f4534a;

        /* renamed from: b, reason: collision with root package name */
        private final int f4535b;

        /* renamed from: c, reason: collision with root package name */
        private String f4536c;

        /* renamed from: d, reason: collision with root package name */
        private Date f4537d;

        public b(String str, int i10) {
            Objects.requireNonNull(str, "keystoreAlias == null");
            if (!str.isEmpty()) {
                this.f4534a = str;
                this.f4535b = i10;
                this.f4536c = "secp256r1";
                this.f4537d = null;
                return;
            }
            throw new IllegalArgumentException("keystoreAlias must not be empty");
        }

        public EcKeyGenParameterSpec e() {
            return new EcKeyGenParameterSpec(this);
        }

        public b f(Date date) {
            this.f4537d = date;
            return this;
        }
    }

    public Date a() {
        return this.f4533d;
    }

    public String b() {
        return this.f4530a;
    }

    public int c() {
        return this.f4531b;
    }

    public String d() {
        return this.f4532c;
    }

    private EcKeyGenParameterSpec(b bVar) {
        this.f4530a = bVar.f4534a;
        this.f4531b = bVar.f4535b;
        this.f4532c = bVar.f4536c;
        this.f4533d = bVar.f4537d;
    }
}
