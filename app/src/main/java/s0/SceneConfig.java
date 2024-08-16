package s0;

/* compiled from: SceneConfig.java */
/* renamed from: s0.m, reason: use source file name */
/* loaded from: classes.dex */
public class SceneConfig {

    /* renamed from: a, reason: collision with root package name */
    private String f18007a;

    /* renamed from: b, reason: collision with root package name */
    private EncryptAlgorithmEnum f18008b;

    /* renamed from: c, reason: collision with root package name */
    private NegotiationAlgorithmEnum f18009c;

    /* renamed from: d, reason: collision with root package name */
    private long f18010d = 0;

    /* renamed from: e, reason: collision with root package name */
    private int f18011e = 60;

    /* renamed from: f, reason: collision with root package name */
    private boolean f18012f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f18013g;

    public EncryptAlgorithmEnum a() {
        return this.f18008b;
    }

    public int b() {
        return this.f18011e;
    }

    public NegotiationAlgorithmEnum c() {
        return this.f18009c;
    }

    public String d() {
        return this.f18007a;
    }

    public boolean e() {
        return this.f18013g;
    }

    public boolean f() {
        return this.f18012f;
    }

    public void g(EncryptAlgorithmEnum encryptAlgorithmEnum) {
        this.f18008b = encryptAlgorithmEnum;
    }

    public void h(int i10) {
        this.f18011e = i10;
    }

    public void i(boolean z10) {
        this.f18012f = z10;
    }

    public void j(NegotiationAlgorithmEnum negotiationAlgorithmEnum) {
        this.f18009c = negotiationAlgorithmEnum;
    }

    public void k(String str) {
        this.f18007a = str;
    }
}
