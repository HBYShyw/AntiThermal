package yb;

import java.util.Arrays;
import java.util.Set;
import oc.ClassId;
import oc.FqName;
import za.DefaultConstructorMarker;

/* compiled from: JavaClassFinder.kt */
/* renamed from: yb.p, reason: use source file name */
/* loaded from: classes2.dex */
public interface JavaClassFinder {
    fc.g a(a aVar);

    fc.u b(FqName fqName, boolean z10);

    Set<String> c(FqName fqName);

    /* compiled from: JavaClassFinder.kt */
    /* renamed from: yb.p$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final ClassId f20124a;

        /* renamed from: b, reason: collision with root package name */
        private final byte[] f20125b;

        /* renamed from: c, reason: collision with root package name */
        private final fc.g f20126c;

        public a(ClassId classId, byte[] bArr, fc.g gVar) {
            za.k.e(classId, "classId");
            this.f20124a = classId;
            this.f20125b = bArr;
            this.f20126c = gVar;
        }

        public final ClassId a() {
            return this.f20124a;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return za.k.a(this.f20124a, aVar.f20124a) && za.k.a(this.f20125b, aVar.f20125b) && za.k.a(this.f20126c, aVar.f20126c);
        }

        public int hashCode() {
            int hashCode = this.f20124a.hashCode() * 31;
            byte[] bArr = this.f20125b;
            int hashCode2 = (hashCode + (bArr == null ? 0 : Arrays.hashCode(bArr))) * 31;
            fc.g gVar = this.f20126c;
            return hashCode2 + (gVar != null ? gVar.hashCode() : 0);
        }

        public String toString() {
            return "Request(classId=" + this.f20124a + ", previouslyFoundClassFileContent=" + Arrays.toString(this.f20125b) + ", outerClass=" + this.f20126c + ')';
        }

        public /* synthetic */ a(ClassId classId, byte[] bArr, fc.g gVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
            this(classId, (i10 & 2) != 0 ? null : bArr, (i10 & 4) != 0 ? null : gVar);
        }
    }
}
