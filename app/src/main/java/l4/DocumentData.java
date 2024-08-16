package l4;

/* compiled from: DocumentData.java */
/* renamed from: l4.b, reason: use source file name */
/* loaded from: classes.dex */
public class DocumentData {

    /* renamed from: a, reason: collision with root package name */
    public final String f14595a;

    /* renamed from: b, reason: collision with root package name */
    public final String f14596b;

    /* renamed from: c, reason: collision with root package name */
    public final float f14597c;

    /* renamed from: d, reason: collision with root package name */
    public final a f14598d;

    /* renamed from: e, reason: collision with root package name */
    public final int f14599e;

    /* renamed from: f, reason: collision with root package name */
    public final float f14600f;

    /* renamed from: g, reason: collision with root package name */
    public final float f14601g;

    /* renamed from: h, reason: collision with root package name */
    public final int f14602h;

    /* renamed from: i, reason: collision with root package name */
    public final int f14603i;

    /* renamed from: j, reason: collision with root package name */
    public final float f14604j;

    /* renamed from: k, reason: collision with root package name */
    public final boolean f14605k;

    /* compiled from: DocumentData.java */
    /* renamed from: l4.b$a */
    /* loaded from: classes.dex */
    public enum a {
        LEFT_ALIGN,
        RIGHT_ALIGN,
        CENTER
    }

    public DocumentData(String str, String str2, float f10, a aVar, int i10, float f11, float f12, int i11, int i12, float f13, boolean z10) {
        this.f14595a = str;
        this.f14596b = str2;
        this.f14597c = f10;
        this.f14598d = aVar;
        this.f14599e = i10;
        this.f14600f = f11;
        this.f14601g = f12;
        this.f14602h = i11;
        this.f14603i = i12;
        this.f14604j = f13;
        this.f14605k = z10;
    }

    public int hashCode() {
        int hashCode = (((((int) ((((this.f14595a.hashCode() * 31) + this.f14596b.hashCode()) * 31) + this.f14597c)) * 31) + this.f14598d.ordinal()) * 31) + this.f14599e;
        long floatToRawIntBits = Float.floatToRawIntBits(this.f14600f);
        return (((hashCode * 31) + ((int) (floatToRawIntBits ^ (floatToRawIntBits >>> 32)))) * 31) + this.f14602h;
    }
}
