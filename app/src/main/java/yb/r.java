package yb;

import gc.NullabilityQualifierWithMigrationStatus;
import java.util.Collection;
import za.DefaultConstructorMarker;

/* compiled from: AnnotationQualifiersFqNames.kt */
/* loaded from: classes2.dex */
public final class r {

    /* renamed from: a, reason: collision with root package name */
    private final NullabilityQualifierWithMigrationStatus f20128a;

    /* renamed from: b, reason: collision with root package name */
    private final Collection<AnnotationQualifierApplicabilityType> f20129b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f20130c;

    /* JADX WARN: Multi-variable type inference failed */
    public r(NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus, Collection<? extends AnnotationQualifierApplicabilityType> collection, boolean z10) {
        za.k.e(nullabilityQualifierWithMigrationStatus, "nullabilityQualifier");
        za.k.e(collection, "qualifierApplicabilityTypes");
        this.f20128a = nullabilityQualifierWithMigrationStatus;
        this.f20129b = collection;
        this.f20130c = z10;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ r b(r rVar, NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus, Collection collection, boolean z10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            nullabilityQualifierWithMigrationStatus = rVar.f20128a;
        }
        if ((i10 & 2) != 0) {
            collection = rVar.f20129b;
        }
        if ((i10 & 4) != 0) {
            z10 = rVar.f20130c;
        }
        return rVar.a(nullabilityQualifierWithMigrationStatus, collection, z10);
    }

    public final r a(NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus, Collection<? extends AnnotationQualifierApplicabilityType> collection, boolean z10) {
        za.k.e(nullabilityQualifierWithMigrationStatus, "nullabilityQualifier");
        za.k.e(collection, "qualifierApplicabilityTypes");
        return new r(nullabilityQualifierWithMigrationStatus, collection, z10);
    }

    public final boolean c() {
        return this.f20130c;
    }

    public final NullabilityQualifierWithMigrationStatus d() {
        return this.f20128a;
    }

    public final Collection<AnnotationQualifierApplicabilityType> e() {
        return this.f20129b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof r)) {
            return false;
        }
        r rVar = (r) obj;
        return za.k.a(this.f20128a, rVar.f20128a) && za.k.a(this.f20129b, rVar.f20129b) && this.f20130c == rVar.f20130c;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        int hashCode = ((this.f20128a.hashCode() * 31) + this.f20129b.hashCode()) * 31;
        boolean z10 = this.f20130c;
        int i10 = z10;
        if (z10 != 0) {
            i10 = 1;
        }
        return hashCode + i10;
    }

    public String toString() {
        return "JavaDefaultQualifiers(nullabilityQualifier=" + this.f20128a + ", qualifierApplicabilityTypes=" + this.f20129b + ", definitelyNotNull=" + this.f20130c + ')';
    }

    public /* synthetic */ r(NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus, Collection collection, boolean z10, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(nullabilityQualifierWithMigrationStatus, collection, (i10 & 4) != 0 ? nullabilityQualifierWithMigrationStatus.c() == gc.h.NOT_NULL : z10);
    }
}
