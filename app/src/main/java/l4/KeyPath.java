package l4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: KeyPath.java */
/* renamed from: l4.f, reason: use source file name */
/* loaded from: classes.dex */
public class KeyPath {

    /* renamed from: c, reason: collision with root package name */
    public static final KeyPath f14623c = new KeyPath("COMPOSITION");

    /* renamed from: a, reason: collision with root package name */
    private final List<String> f14624a;

    /* renamed from: b, reason: collision with root package name */
    private KeyPathElement f14625b;

    public KeyPath(String... strArr) {
        this.f14624a = Arrays.asList(strArr);
    }

    private boolean b() {
        return this.f14624a.get(r1.size() - 1).equals("**");
    }

    private boolean f(String str) {
        return "__container".equals(str);
    }

    public KeyPath a(String str) {
        KeyPath keyPath = new KeyPath(this);
        keyPath.f14624a.add(str);
        return keyPath;
    }

    public boolean c(String str, int i10) {
        if (i10 >= this.f14624a.size()) {
            return false;
        }
        boolean z10 = i10 == this.f14624a.size() - 1;
        String str2 = this.f14624a.get(i10);
        if (!str2.equals("**")) {
            return (z10 || (i10 == this.f14624a.size() + (-2) && b())) && (str2.equals(str) || str2.equals("*"));
        }
        if (!z10 && this.f14624a.get(i10 + 1).equals(str)) {
            return i10 == this.f14624a.size() + (-2) || (i10 == this.f14624a.size() + (-3) && b());
        }
        if (z10) {
            return true;
        }
        int i11 = i10 + 1;
        if (i11 < this.f14624a.size() - 1) {
            return false;
        }
        return this.f14624a.get(i11).equals(str);
    }

    public KeyPathElement d() {
        return this.f14625b;
    }

    public int e(String str, int i10) {
        if (f(str)) {
            return 0;
        }
        if (this.f14624a.get(i10).equals("**")) {
            return (i10 != this.f14624a.size() - 1 && this.f14624a.get(i10 + 1).equals(str)) ? 2 : 0;
        }
        return 1;
    }

    public boolean g(String str, int i10) {
        if (f(str)) {
            return true;
        }
        if (i10 >= this.f14624a.size()) {
            return false;
        }
        return this.f14624a.get(i10).equals(str) || this.f14624a.get(i10).equals("**") || this.f14624a.get(i10).equals("*");
    }

    public boolean h(String str, int i10) {
        return "__container".equals(str) || i10 < this.f14624a.size() - 1 || this.f14624a.get(i10).equals("**");
    }

    public KeyPath i(KeyPathElement keyPathElement) {
        KeyPath keyPath = new KeyPath(this);
        keyPath.f14625b = keyPathElement;
        return keyPath;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("KeyPath{keys=");
        sb2.append(this.f14624a);
        sb2.append(",resolved=");
        sb2.append(this.f14625b != null);
        sb2.append('}');
        return sb2.toString();
    }

    private KeyPath(KeyPath keyPath) {
        this.f14624a = new ArrayList(keyPath.f14624a);
        this.f14625b = keyPath.f14625b;
    }
}
