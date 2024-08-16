package l4;

import java.util.List;
import n4.ShapeGroup;

/* compiled from: FontCharacter.java */
/* renamed from: l4.e, reason: use source file name */
/* loaded from: classes.dex */
public class FontCharacter {

    /* renamed from: a, reason: collision with root package name */
    private final List<ShapeGroup> f14617a;

    /* renamed from: b, reason: collision with root package name */
    private final char f14618b;

    /* renamed from: c, reason: collision with root package name */
    private final double f14619c;

    /* renamed from: d, reason: collision with root package name */
    private final double f14620d;

    /* renamed from: e, reason: collision with root package name */
    private final String f14621e;

    /* renamed from: f, reason: collision with root package name */
    private final String f14622f;

    public FontCharacter(List<ShapeGroup> list, char c10, double d10, double d11, String str, String str2) {
        this.f14617a = list;
        this.f14618b = c10;
        this.f14619c = d10;
        this.f14620d = d11;
        this.f14621e = str;
        this.f14622f = str2;
    }

    public static int c(char c10, String str, String str2) {
        return ((((0 + c10) * 31) + str.hashCode()) * 31) + str2.hashCode();
    }

    public List<ShapeGroup> a() {
        return this.f14617a;
    }

    public double b() {
        return this.f14620d;
    }

    public int hashCode() {
        return c(this.f14618b, this.f14622f, this.f14621e);
    }
}
