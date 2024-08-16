package cd;

import oc.ClassId;

/* compiled from: IncompatibleVersionErrorData.kt */
/* renamed from: cd.t, reason: use source file name */
/* loaded from: classes2.dex */
public final class IncompatibleVersionErrorData<T> {

    /* renamed from: a, reason: collision with root package name */
    private final T f5287a;

    /* renamed from: b, reason: collision with root package name */
    private final T f5288b;

    /* renamed from: c, reason: collision with root package name */
    private final String f5289c;

    /* renamed from: d, reason: collision with root package name */
    private final ClassId f5290d;

    public IncompatibleVersionErrorData(T t7, T t10, String str, ClassId classId) {
        za.k.e(str, "filePath");
        za.k.e(classId, "classId");
        this.f5287a = t7;
        this.f5288b = t10;
        this.f5289c = str;
        this.f5290d = classId;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IncompatibleVersionErrorData)) {
            return false;
        }
        IncompatibleVersionErrorData incompatibleVersionErrorData = (IncompatibleVersionErrorData) obj;
        return za.k.a(this.f5287a, incompatibleVersionErrorData.f5287a) && za.k.a(this.f5288b, incompatibleVersionErrorData.f5288b) && za.k.a(this.f5289c, incompatibleVersionErrorData.f5289c) && za.k.a(this.f5290d, incompatibleVersionErrorData.f5290d);
    }

    public int hashCode() {
        T t7 = this.f5287a;
        int hashCode = (t7 == null ? 0 : t7.hashCode()) * 31;
        T t10 = this.f5288b;
        return ((((hashCode + (t10 != null ? t10.hashCode() : 0)) * 31) + this.f5289c.hashCode()) * 31) + this.f5290d.hashCode();
    }

    public String toString() {
        return "IncompatibleVersionErrorData(actualVersion=" + this.f5287a + ", expectedVersion=" + this.f5288b + ", filePath=" + this.f5289c + ", classId=" + this.f5290d + ')';
    }
}
