package oc;

import sd.StringsJVM;
import za.DefaultConstructorMarker;

/* compiled from: CallableId.kt */
/* renamed from: oc.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class CallableId {

    /* renamed from: e, reason: collision with root package name */
    private static final a f16421e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    @Deprecated
    private static final Name f16422f;

    /* renamed from: g, reason: collision with root package name */
    @Deprecated
    private static final FqName f16423g;

    /* renamed from: a, reason: collision with root package name */
    private final FqName f16424a;

    /* renamed from: b, reason: collision with root package name */
    private final FqName f16425b;

    /* renamed from: c, reason: collision with root package name */
    private final Name f16426c;

    /* renamed from: d, reason: collision with root package name */
    private final FqName f16427d;

    /* compiled from: CallableId.kt */
    /* renamed from: oc.a$a */
    /* loaded from: classes2.dex */
    private static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        Name name = SpecialNames.f16458m;
        f16422f = name;
        FqName k10 = FqName.k(name);
        za.k.d(k10, "topLevel(LOCAL_NAME)");
        f16423g = k10;
    }

    public CallableId(FqName fqName, FqName fqName2, Name name, FqName fqName3) {
        za.k.e(fqName, "packageName");
        za.k.e(name, "callableName");
        this.f16424a = fqName;
        this.f16425b = fqName2;
        this.f16426c = name;
        this.f16427d = fqName3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CallableId)) {
            return false;
        }
        CallableId callableId = (CallableId) obj;
        return za.k.a(this.f16424a, callableId.f16424a) && za.k.a(this.f16425b, callableId.f16425b) && za.k.a(this.f16426c, callableId.f16426c) && za.k.a(this.f16427d, callableId.f16427d);
    }

    public int hashCode() {
        int hashCode = this.f16424a.hashCode() * 31;
        FqName fqName = this.f16425b;
        int hashCode2 = (((hashCode + (fqName == null ? 0 : fqName.hashCode())) * 31) + this.f16426c.hashCode()) * 31;
        FqName fqName2 = this.f16427d;
        return hashCode2 + (fqName2 != null ? fqName2.hashCode() : 0);
    }

    public String toString() {
        String y4;
        StringBuilder sb2 = new StringBuilder();
        String b10 = this.f16424a.b();
        za.k.d(b10, "packageName.asString()");
        y4 = StringsJVM.y(b10, '.', '/', false, 4, null);
        sb2.append(y4);
        sb2.append("/");
        FqName fqName = this.f16425b;
        if (fqName != null) {
            sb2.append(fqName);
            sb2.append(".");
        }
        sb2.append(this.f16426c);
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public /* synthetic */ CallableId(FqName fqName, FqName fqName2, Name name, FqName fqName3, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(fqName, fqName2, name, (i10 & 8) != 0 ? null : fqName3);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public CallableId(FqName fqName, Name name) {
        this(fqName, null, name, null, 8, null);
        za.k.e(fqName, "packageName");
        za.k.e(name, "callableName");
    }
}
