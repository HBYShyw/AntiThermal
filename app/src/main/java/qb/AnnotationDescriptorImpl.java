package qb;

import gd.g0;
import java.util.Map;
import oc.FqName;
import oc.Name;
import pb.SourceElement;
import qb.AnnotationDescriptor;

/* compiled from: AnnotationDescriptorImpl.java */
/* renamed from: qb.d, reason: use source file name */
/* loaded from: classes2.dex */
public class AnnotationDescriptorImpl implements AnnotationDescriptor {

    /* renamed from: a, reason: collision with root package name */
    private final g0 f17175a;

    /* renamed from: b, reason: collision with root package name */
    private final Map<Name, uc.g<?>> f17176b;

    /* renamed from: c, reason: collision with root package name */
    private final SourceElement f17177c;

    public AnnotationDescriptorImpl(g0 g0Var, Map<Name, uc.g<?>> map, SourceElement sourceElement) {
        if (g0Var == null) {
            b(0);
        }
        if (map == null) {
            b(1);
        }
        if (sourceElement == null) {
            b(2);
        }
        this.f17175a = g0Var;
        this.f17176b = map;
        this.f17177c = sourceElement;
    }

    private static /* synthetic */ void b(int i10) {
        String str = (i10 == 3 || i10 == 4 || i10 == 5) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 3 || i10 == 4 || i10 == 5) ? 2 : 3];
        if (i10 == 1) {
            objArr[0] = "valueArguments";
        } else if (i10 == 2) {
            objArr[0] = "source";
        } else if (i10 == 3 || i10 == 4 || i10 == 5) {
            objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/annotations/AnnotationDescriptorImpl";
        } else {
            objArr[0] = "annotationType";
        }
        if (i10 == 3) {
            objArr[1] = "getType";
        } else if (i10 == 4) {
            objArr[1] = "getAllValueArguments";
        } else if (i10 != 5) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/annotations/AnnotationDescriptorImpl";
        } else {
            objArr[1] = "getSource";
        }
        if (i10 != 3 && i10 != 4 && i10 != 5) {
            objArr[2] = "<init>";
        }
        String format = String.format(str, objArr);
        if (i10 != 3 && i10 != 4 && i10 != 5) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // qb.AnnotationDescriptor
    public Map<Name, uc.g<?>> a() {
        Map<Name, uc.g<?>> map = this.f17176b;
        if (map == null) {
            b(4);
        }
        return map;
    }

    @Override // qb.AnnotationDescriptor
    public FqName d() {
        return AnnotationDescriptor.a.a(this);
    }

    @Override // qb.AnnotationDescriptor
    public g0 getType() {
        g0 g0Var = this.f17175a;
        if (g0Var == null) {
            b(3);
        }
        return g0Var;
    }

    public String toString() {
        return rc.c.f17708g.r(this, null);
    }

    @Override // qb.AnnotationDescriptor
    public SourceElement z() {
        SourceElement sourceElement = this.f17177c;
        if (sourceElement == null) {
            b(5);
        }
        return sourceElement;
    }
}
