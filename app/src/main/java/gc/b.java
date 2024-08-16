package gc;

import gd.g0;
import java.util.Map;
import oc.FqName;
import oc.Name;
import pb.SourceElement;
import qb.AnnotationDescriptor;

/* compiled from: typeEnhancement.kt */
/* loaded from: classes2.dex */
final class b implements AnnotationDescriptor {

    /* renamed from: a, reason: collision with root package name */
    public static final b f11649a = new b();

    private b() {
    }

    private final Void b() {
        throw new IllegalStateException("No methods should be called on this descriptor. Only its presence matters".toString());
    }

    @Override // qb.AnnotationDescriptor
    public Map<Name, uc.g<?>> a() {
        b();
        throw null;
    }

    @Override // qb.AnnotationDescriptor
    public FqName d() {
        return AnnotationDescriptor.a.a(this);
    }

    @Override // qb.AnnotationDescriptor
    public g0 getType() {
        b();
        throw null;
    }

    public String toString() {
        return "[EnhancedType]";
    }

    @Override // qb.AnnotationDescriptor
    public SourceElement z() {
        b();
        throw null;
    }
}
