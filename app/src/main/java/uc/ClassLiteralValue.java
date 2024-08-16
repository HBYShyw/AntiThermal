package uc;

import oc.ClassId;

/* compiled from: ClassLiteralValue.kt */
/* renamed from: uc.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class ClassLiteralValue {

    /* renamed from: a, reason: collision with root package name */
    private final ClassId f18988a;

    /* renamed from: b, reason: collision with root package name */
    private final int f18989b;

    public ClassLiteralValue(ClassId classId, int i10) {
        za.k.e(classId, "classId");
        this.f18988a = classId;
        this.f18989b = i10;
    }

    public final ClassId a() {
        return this.f18988a;
    }

    public final int b() {
        return this.f18989b;
    }

    public final int c() {
        return this.f18989b;
    }

    public final ClassId d() {
        return this.f18988a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ClassLiteralValue)) {
            return false;
        }
        ClassLiteralValue classLiteralValue = (ClassLiteralValue) obj;
        return za.k.a(this.f18988a, classLiteralValue.f18988a) && this.f18989b == classLiteralValue.f18989b;
    }

    public int hashCode() {
        return (this.f18988a.hashCode() * 31) + Integer.hashCode(this.f18989b);
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        int i10 = this.f18989b;
        for (int i11 = 0; i11 < i10; i11++) {
            sb2.append("kotlin/Array<");
        }
        sb2.append(this.f18988a);
        int i12 = this.f18989b;
        for (int i13 = 0; i13 < i12; i13++) {
            sb2.append(">");
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }
}
