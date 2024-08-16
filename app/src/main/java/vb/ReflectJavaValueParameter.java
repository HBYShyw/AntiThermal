package vb;

import java.lang.annotation.Annotation;
import java.util.List;
import oc.FqName;
import oc.Name;

/* compiled from: ReflectJavaValueParameter.kt */
/* renamed from: vb.b0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaValueParameter extends ReflectJavaElement implements fc.b0 {

    /* renamed from: a, reason: collision with root package name */
    private final ReflectJavaType f19214a;

    /* renamed from: b, reason: collision with root package name */
    private final Annotation[] f19215b;

    /* renamed from: c, reason: collision with root package name */
    private final String f19216c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f19217d;

    public ReflectJavaValueParameter(ReflectJavaType reflectJavaType, Annotation[] annotationArr, String str, boolean z10) {
        za.k.e(reflectJavaType, "type");
        za.k.e(annotationArr, "reflectAnnotations");
        this.f19214a = reflectJavaType;
        this.f19215b = annotationArr;
        this.f19216c = str;
        this.f19217d = z10;
    }

    @Override // fc.b0
    /* renamed from: W, reason: merged with bridge method [inline-methods] */
    public ReflectJavaType getType() {
        return this.f19214a;
    }

    @Override // fc.b0
    public boolean a() {
        return this.f19217d;
    }

    @Override // fc.b0
    public Name getName() {
        String str = this.f19216c;
        if (str != null) {
            return Name.e(str);
        }
        return null;
    }

    @Override // fc.d
    public boolean k() {
        return false;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(ReflectJavaValueParameter.class.getName());
        sb2.append(": ");
        sb2.append(a() ? "vararg " : "");
        sb2.append(getName());
        sb2.append(": ");
        sb2.append(getType());
        return sb2.toString();
    }

    @Override // fc.d
    public List<ReflectJavaAnnotation> i() {
        return i.b(this.f19215b);
    }

    @Override // fc.d
    public ReflectJavaAnnotation j(FqName fqName) {
        za.k.e(fqName, "fqName");
        return i.a(this.f19215b, fqName);
    }
}
