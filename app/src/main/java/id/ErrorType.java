package id;

import gd.TypeConstructor;
import gd.TypeProjection;
import gd.c1;
import gd.o0;
import java.util.Arrays;
import java.util.List;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.PrimitiveCompanionObjects;

/* compiled from: ErrorType.kt */
/* renamed from: id.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class ErrorType extends o0 {

    /* renamed from: f, reason: collision with root package name */
    private final TypeConstructor f12775f;

    /* renamed from: g, reason: collision with root package name */
    private final zc.h f12776g;

    /* renamed from: h, reason: collision with root package name */
    private final ErrorTypeKind f12777h;

    /* renamed from: i, reason: collision with root package name */
    private final List<TypeProjection> f12778i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f12779j;

    /* renamed from: k, reason: collision with root package name */
    private final String[] f12780k;

    /* renamed from: l, reason: collision with root package name */
    private final String f12781l;

    public /* synthetic */ ErrorType(TypeConstructor typeConstructor, zc.h hVar, ErrorTypeKind errorTypeKind, List list, boolean z10, String[] strArr, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(typeConstructor, hVar, errorTypeKind, (i10 & 8) != 0 ? r.j() : list, (i10 & 16) != 0 ? false : z10, strArr);
    }

    @Override // gd.g0
    public List<TypeProjection> U0() {
        return this.f12778i;
    }

    @Override // gd.g0
    public c1 V0() {
        return c1.f11749f.h();
    }

    @Override // gd.g0
    public TypeConstructor W0() {
        return this.f12775f;
    }

    @Override // gd.g0
    public boolean X0() {
        return this.f12779j;
    }

    @Override // gd.v1
    /* renamed from: d1 */
    public o0 a1(boolean z10) {
        TypeConstructor W0 = W0();
        zc.h u7 = u();
        ErrorTypeKind errorTypeKind = this.f12777h;
        List<TypeProjection> U0 = U0();
        String[] strArr = this.f12780k;
        return new ErrorType(W0, u7, errorTypeKind, U0, z10, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    @Override // gd.v1
    /* renamed from: e1 */
    public o0 c1(c1 c1Var) {
        za.k.e(c1Var, "newAttributes");
        return this;
    }

    public final String f1() {
        return this.f12781l;
    }

    public final ErrorTypeKind g1() {
        return this.f12777h;
    }

    @Override // gd.v1
    /* renamed from: h1, reason: merged with bridge method [inline-methods] */
    public ErrorType g1(hd.g gVar) {
        za.k.e(gVar, "kotlinTypeRefiner");
        return this;
    }

    @Override // gd.g0
    public zc.h u() {
        return this.f12776g;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ErrorType(TypeConstructor typeConstructor, zc.h hVar, ErrorTypeKind errorTypeKind, List<? extends TypeProjection> list, boolean z10, String... strArr) {
        za.k.e(typeConstructor, "constructor");
        za.k.e(hVar, "memberScope");
        za.k.e(errorTypeKind, "kind");
        za.k.e(list, "arguments");
        za.k.e(strArr, "formatParams");
        this.f12775f = typeConstructor;
        this.f12776g = hVar;
        this.f12777h = errorTypeKind;
        this.f12778i = list;
        this.f12779j = z10;
        this.f12780k = strArr;
        PrimitiveCompanionObjects primitiveCompanionObjects = PrimitiveCompanionObjects.f20358a;
        String b10 = errorTypeKind.b();
        Object[] copyOf = Arrays.copyOf(strArr, strArr.length);
        String format = String.format(b10, Arrays.copyOf(copyOf, copyOf.length));
        za.k.d(format, "format(format, *args)");
        this.f12781l = format;
    }
}
