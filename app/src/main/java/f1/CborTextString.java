package f1;

import java.util.Objects;

/* compiled from: CborTextString.java */
/* renamed from: f1.u, reason: use source file name */
/* loaded from: classes.dex */
public class CborTextString extends CborObject {

    /* renamed from: c, reason: collision with root package name */
    private final String f11303c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11304d;

    public CborTextString(String str) {
        this(str, -1L);
    }

    @Override // f1.CborObject
    public boolean equals(Object obj) {
        if (!(obj instanceof CborTextString)) {
            return false;
        }
        CborTextString cborTextString = (CborTextString) obj;
        return this.f11304d == cborTextString.f11304d && super.equals(obj) && this.f11303c.equals(cborTextString.f11303c);
    }

    public String g() {
        return this.f11303c;
    }

    public boolean h() {
        return this.f11304d;
    }

    @Override // f1.CborObject
    public int hashCode() {
        if (this.f11303c != null) {
            return Objects.hash(Integer.valueOf(super.hashCode()), Boolean.valueOf(this.f11304d)) + this.f11303c.hashCode();
        }
        return 0;
    }

    public String toString() {
        if (this.f11303c == null) {
            return "null";
        }
        if (c() != -1) {
            return c() + "(\"" + this.f11303c + "\")";
        }
        return "\"" + this.f11303c + "\"";
    }

    public CborTextString(String str, long j10) {
        super(3, j10);
        this.f11304d = false;
        this.f11303c = str;
    }
}
