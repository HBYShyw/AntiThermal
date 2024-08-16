package id;

import gd.TypeConstructor;
import gd.g0;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.collections.r;
import mb.DefaultBuiltIns;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;

/* compiled from: ErrorTypeConstructor.kt */
/* renamed from: id.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class ErrorTypeConstructor implements TypeConstructor {

    /* renamed from: a, reason: collision with root package name */
    private final ErrorTypeKind f12782a;

    /* renamed from: b, reason: collision with root package name */
    private final String[] f12783b;

    /* renamed from: c, reason: collision with root package name */
    private final String f12784c;

    public ErrorTypeConstructor(ErrorTypeKind errorTypeKind, String... strArr) {
        za.k.e(errorTypeKind, "kind");
        za.k.e(strArr, "formatParams");
        this.f12782a = errorTypeKind;
        this.f12783b = strArr;
        String b10 = ErrorEntity.ERROR_TYPE.b();
        String b11 = errorTypeKind.b();
        Object[] copyOf = Arrays.copyOf(strArr, strArr.length);
        String format = String.format(b11, Arrays.copyOf(copyOf, copyOf.length));
        za.k.d(format, "format(this, *args)");
        String format2 = String.format(b10, Arrays.copyOf(new Object[]{format}, 1));
        za.k.d(format2, "format(this, *args)");
        this.f12784c = format2;
    }

    public final ErrorTypeKind c() {
        return this.f12782a;
    }

    public final String d(int i10) {
        return this.f12783b[i10];
    }

    @Override // gd.TypeConstructor
    public List<TypeParameterDescriptor> getParameters() {
        List<TypeParameterDescriptor> j10;
        j10 = r.j();
        return j10;
    }

    @Override // gd.TypeConstructor
    public Collection<g0> q() {
        List j10;
        j10 = r.j();
        return j10;
    }

    @Override // gd.TypeConstructor
    public KotlinBuiltIns t() {
        return DefaultBuiltIns.f15215h.a();
    }

    public String toString() {
        return this.f12784c;
    }

    @Override // gd.TypeConstructor
    public TypeConstructor u(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this;
    }

    @Override // gd.TypeConstructor
    public ClassifierDescriptor v() {
        return ErrorUtils.f12833a.h();
    }

    @Override // gd.TypeConstructor
    public boolean w() {
        return false;
    }
}
