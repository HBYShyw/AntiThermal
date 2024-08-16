package l4;

/* compiled from: Marker.java */
/* renamed from: l4.h, reason: use source file name */
/* loaded from: classes.dex */
public class Marker {

    /* renamed from: a, reason: collision with root package name */
    private final String f14626a;

    /* renamed from: b, reason: collision with root package name */
    public final float f14627b;

    /* renamed from: c, reason: collision with root package name */
    public final float f14628c;

    public Marker(String str, float f10, float f11) {
        this.f14626a = str;
        this.f14628c = f11;
        this.f14627b = f10;
    }

    public boolean a(String str) {
        if (this.f14626a.equalsIgnoreCase(str)) {
            return true;
        }
        if (this.f14626a.endsWith("\r")) {
            String str2 = this.f14626a;
            if (str2.substring(0, str2.length() - 1).equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
