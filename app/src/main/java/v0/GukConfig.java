package v0;

import java.nio.charset.StandardCharsets;
import l0.IAuthConfig;

/* compiled from: GukConfig.java */
/* renamed from: v0.a, reason: use source file name */
/* loaded from: classes.dex */
public class GukConfig implements IAuthConfig {

    /* renamed from: a, reason: collision with root package name */
    private String f19026a;

    /* renamed from: b, reason: collision with root package name */
    private String f19027b;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f19028c;

    /* renamed from: d, reason: collision with root package name */
    private byte[] f19029d;

    /* renamed from: e, reason: collision with root package name */
    private int f19030e;

    /* renamed from: f, reason: collision with root package name */
    private String f19031f;

    /* compiled from: GukConfig.java */
    /* renamed from: v0.a$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private String f19032a = "SMYijOgbT1JfVMug";

        /* renamed from: b, reason: collision with root package name */
        private String f19033b = null;

        /* renamed from: c, reason: collision with root package name */
        private byte[] f19034c = "".getBytes(StandardCharsets.UTF_8);

        /* renamed from: d, reason: collision with root package name */
        private byte[] f19035d = new byte[32];

        /* renamed from: e, reason: collision with root package name */
        private int f19036e = 32;

        /* renamed from: f, reason: collision with root package name */
        private String f19037f = "sha256";
    }

    public GukConfig() {
        this(new a());
    }

    public String a() {
        return this.f19031f;
    }

    public byte[] b() {
        return this.f19028c;
    }

    public String c() {
        return this.f19026a;
    }

    public int d() {
        return this.f19030e;
    }

    public byte[] e() {
        return this.f19029d;
    }

    public void f(byte[] bArr) {
        this.f19028c = bArr;
    }

    public void g(byte[] bArr) {
        this.f19029d = bArr;
    }

    private GukConfig(a aVar) {
        this.f19026a = aVar.f19032a;
        this.f19027b = aVar.f19033b;
        this.f19028c = aVar.f19034c;
        this.f19029d = aVar.f19035d;
        this.f19030e = aVar.f19036e;
        this.f19031f = aVar.f19037f;
    }
}
