package cd;

import lc.BinaryVersion;
import lc.NameResolver;
import pb.SourceElement;

/* compiled from: ClassData.kt */
/* renamed from: cd.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class ClassData {

    /* renamed from: a, reason: collision with root package name */
    private final NameResolver f5228a;

    /* renamed from: b, reason: collision with root package name */
    private final jc.c f5229b;

    /* renamed from: c, reason: collision with root package name */
    private final BinaryVersion f5230c;

    /* renamed from: d, reason: collision with root package name */
    private final SourceElement f5231d;

    public ClassData(NameResolver nameResolver, jc.c cVar, BinaryVersion binaryVersion, SourceElement sourceElement) {
        za.k.e(nameResolver, "nameResolver");
        za.k.e(cVar, "classProto");
        za.k.e(binaryVersion, "metadataVersion");
        za.k.e(sourceElement, "sourceElement");
        this.f5228a = nameResolver;
        this.f5229b = cVar;
        this.f5230c = binaryVersion;
        this.f5231d = sourceElement;
    }

    public final NameResolver a() {
        return this.f5228a;
    }

    public final jc.c b() {
        return this.f5229b;
    }

    public final BinaryVersion c() {
        return this.f5230c;
    }

    public final SourceElement d() {
        return this.f5231d;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ClassData)) {
            return false;
        }
        ClassData classData = (ClassData) obj;
        return za.k.a(this.f5228a, classData.f5228a) && za.k.a(this.f5229b, classData.f5229b) && za.k.a(this.f5230c, classData.f5230c) && za.k.a(this.f5231d, classData.f5231d);
    }

    public int hashCode() {
        return (((((this.f5228a.hashCode() * 31) + this.f5229b.hashCode()) * 31) + this.f5230c.hashCode()) * 31) + this.f5231d.hashCode();
    }

    public String toString() {
        return "ClassData(nameResolver=" + this.f5228a + ", classProto=" + this.f5229b + ", metadataVersion=" + this.f5230c + ", sourceElement=" + this.f5231d + ')';
    }
}
