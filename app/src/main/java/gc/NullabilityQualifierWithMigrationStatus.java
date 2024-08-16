package gc;

import za.DefaultConstructorMarker;

/* compiled from: NullabilityQualifierWithMigrationStatus.kt */
/* renamed from: gc.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class NullabilityQualifierWithMigrationStatus {

    /* renamed from: a, reason: collision with root package name */
    private final h f11671a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f11672b;

    public NullabilityQualifierWithMigrationStatus(h hVar, boolean z10) {
        za.k.e(hVar, "qualifier");
        this.f11671a = hVar;
        this.f11672b = z10;
    }

    public static /* synthetic */ NullabilityQualifierWithMigrationStatus b(NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus, h hVar, boolean z10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            hVar = nullabilityQualifierWithMigrationStatus.f11671a;
        }
        if ((i10 & 2) != 0) {
            z10 = nullabilityQualifierWithMigrationStatus.f11672b;
        }
        return nullabilityQualifierWithMigrationStatus.a(hVar, z10);
    }

    public final NullabilityQualifierWithMigrationStatus a(h hVar, boolean z10) {
        za.k.e(hVar, "qualifier");
        return new NullabilityQualifierWithMigrationStatus(hVar, z10);
    }

    public final h c() {
        return this.f11671a;
    }

    public final boolean d() {
        return this.f11672b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NullabilityQualifierWithMigrationStatus)) {
            return false;
        }
        NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus = (NullabilityQualifierWithMigrationStatus) obj;
        return this.f11671a == nullabilityQualifierWithMigrationStatus.f11671a && this.f11672b == nullabilityQualifierWithMigrationStatus.f11672b;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        int hashCode = this.f11671a.hashCode() * 31;
        boolean z10 = this.f11672b;
        int i10 = z10;
        if (z10 != 0) {
            i10 = 1;
        }
        return hashCode + i10;
    }

    public String toString() {
        return "NullabilityQualifierWithMigrationStatus(qualifier=" + this.f11671a + ", isForWarningOnly=" + this.f11672b + ')';
    }

    public /* synthetic */ NullabilityQualifierWithMigrationStatus(h hVar, boolean z10, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(hVar, (i10 & 2) != 0 ? false : z10);
    }
}
