package r8;

/* compiled from: BatterySipper.java */
/* renamed from: r8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class BatterySipper {

    /* renamed from: a, reason: collision with root package name */
    public int f17609a;

    /* renamed from: b, reason: collision with root package name */
    public long f17610b;

    /* renamed from: c, reason: collision with root package name */
    public long f17611c;

    /* renamed from: d, reason: collision with root package name */
    public long f17612d;

    /* renamed from: e, reason: collision with root package name */
    public long f17613e;

    /* renamed from: f, reason: collision with root package name */
    public long f17614f;

    /* renamed from: g, reason: collision with root package name */
    public long f17615g;

    /* renamed from: h, reason: collision with root package name */
    public double f17616h;

    /* renamed from: i, reason: collision with root package name */
    public double f17617i;

    /* renamed from: j, reason: collision with root package name */
    public double f17618j;

    /* renamed from: k, reason: collision with root package name */
    public double f17619k;

    /* renamed from: l, reason: collision with root package name */
    public double f17620l;

    /* renamed from: m, reason: collision with root package name */
    public boolean f17621m;

    /* renamed from: n, reason: collision with root package name */
    public String f17622n;

    /* renamed from: o, reason: collision with root package name */
    public a f17623o;

    /* compiled from: BatterySipper.java */
    /* renamed from: r8.a$a */
    /* loaded from: classes2.dex */
    public enum a {
        AMBIENT_DISPLAY,
        APP,
        BLUETOOTH,
        CAMERA,
        CELL,
        FLASHLIGHT,
        IDLE,
        MEMORY,
        OVERCOUNTED,
        PHONE,
        SCREEN,
        UNACCOUNTED,
        USER,
        WIFI
    }

    public void a(BatterySipper batterySipper) {
        this.f17623o = batterySipper.f17623o;
        this.f17621m = batterySipper.f17621m;
        this.f17622n = batterySipper.f17622n;
        this.f17610b += batterySipper.f17610b;
        this.f17611c += batterySipper.f17611c;
        this.f17612d += batterySipper.f17612d;
        this.f17613e += batterySipper.f17613e;
        this.f17614f += batterySipper.f17614f;
        this.f17615g += batterySipper.f17615g;
        this.f17616h += batterySipper.f17616h;
        double d10 = this.f17617i + batterySipper.f17617i;
        this.f17617i = d10;
        this.f17618j += batterySipper.f17618j;
        this.f17619k += batterySipper.f17619k;
        this.f17620l += batterySipper.f17620l;
        this.f17617i = d10 + batterySipper.f17617i;
    }

    public int b() {
        return this.f17609a;
    }
}
