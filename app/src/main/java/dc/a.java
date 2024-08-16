package dc;

import gd.ErasureTypeAttributes;
import gd.TypeUsage;
import gd.o0;
import java.util.Set;
import kotlin.collections.SetsJVM;
import kotlin.collections._Sets;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: JavaTypeAttributes.kt */
/* loaded from: classes2.dex */
public final class a extends ErasureTypeAttributes {

    /* renamed from: d, reason: collision with root package name */
    private final TypeUsage f10895d;

    /* renamed from: e, reason: collision with root package name */
    private final JavaTypeFlexibility f10896e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f10897f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f10898g;

    /* renamed from: h, reason: collision with root package name */
    private final Set<TypeParameterDescriptor> f10899h;

    /* renamed from: i, reason: collision with root package name */
    private final o0 f10900i;

    public /* synthetic */ a(TypeUsage typeUsage, JavaTypeFlexibility javaTypeFlexibility, boolean z10, boolean z11, Set set, o0 o0Var, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(typeUsage, (i10 & 2) != 0 ? JavaTypeFlexibility.INFLEXIBLE : javaTypeFlexibility, (i10 & 4) != 0 ? false : z10, (i10 & 8) != 0 ? false : z11, (i10 & 16) != 0 ? null : set, (i10 & 32) != 0 ? null : o0Var);
    }

    public static /* synthetic */ a f(a aVar, TypeUsage typeUsage, JavaTypeFlexibility javaTypeFlexibility, boolean z10, boolean z11, Set set, o0 o0Var, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            typeUsage = aVar.b();
        }
        if ((i10 & 2) != 0) {
            javaTypeFlexibility = aVar.f10896e;
        }
        JavaTypeFlexibility javaTypeFlexibility2 = javaTypeFlexibility;
        if ((i10 & 4) != 0) {
            z10 = aVar.f10897f;
        }
        boolean z12 = z10;
        if ((i10 & 8) != 0) {
            z11 = aVar.f10898g;
        }
        boolean z13 = z11;
        if ((i10 & 16) != 0) {
            set = aVar.c();
        }
        Set set2 = set;
        if ((i10 & 32) != 0) {
            o0Var = aVar.a();
        }
        return aVar.e(typeUsage, javaTypeFlexibility2, z12, z13, set2, o0Var);
    }

    @Override // gd.ErasureTypeAttributes
    public o0 a() {
        return this.f10900i;
    }

    @Override // gd.ErasureTypeAttributes
    public TypeUsage b() {
        return this.f10895d;
    }

    @Override // gd.ErasureTypeAttributes
    public Set<TypeParameterDescriptor> c() {
        return this.f10899h;
    }

    public final a e(TypeUsage typeUsage, JavaTypeFlexibility javaTypeFlexibility, boolean z10, boolean z11, Set<? extends TypeParameterDescriptor> set, o0 o0Var) {
        k.e(typeUsage, "howThisTypeIsUsed");
        k.e(javaTypeFlexibility, "flexibility");
        return new a(typeUsage, javaTypeFlexibility, z10, z11, set, o0Var);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof a)) {
            return false;
        }
        a aVar = (a) obj;
        return k.a(aVar.a(), a()) && aVar.b() == b() && aVar.f10896e == this.f10896e && aVar.f10897f == this.f10897f && aVar.f10898g == this.f10898g;
    }

    public final JavaTypeFlexibility g() {
        return this.f10896e;
    }

    public final boolean h() {
        return this.f10898g;
    }

    @Override // gd.ErasureTypeAttributes
    public int hashCode() {
        o0 a10 = a();
        int hashCode = a10 != null ? a10.hashCode() : 0;
        int hashCode2 = hashCode + (hashCode * 31) + b().hashCode();
        int hashCode3 = hashCode2 + (hashCode2 * 31) + this.f10896e.hashCode();
        int i10 = hashCode3 + (hashCode3 * 31) + (this.f10897f ? 1 : 0);
        return i10 + (i10 * 31) + (this.f10898g ? 1 : 0);
    }

    public final boolean i() {
        return this.f10897f;
    }

    public final a j(boolean z10) {
        return f(this, null, null, z10, false, null, null, 59, null);
    }

    public a k(o0 o0Var) {
        return f(this, null, null, false, false, null, o0Var, 31, null);
    }

    public final a l(JavaTypeFlexibility javaTypeFlexibility) {
        k.e(javaTypeFlexibility, "flexibility");
        return f(this, null, javaTypeFlexibility, false, false, null, null, 61, null);
    }

    @Override // gd.ErasureTypeAttributes
    /* renamed from: m, reason: merged with bridge method [inline-methods] */
    public a d(TypeParameterDescriptor typeParameterDescriptor) {
        k.e(typeParameterDescriptor, "typeParameter");
        return f(this, null, null, false, false, c() != null ? _Sets.l(c(), typeParameterDescriptor) : SetsJVM.d(typeParameterDescriptor), null, 47, null);
    }

    public String toString() {
        return "JavaTypeAttributes(howThisTypeIsUsed=" + b() + ", flexibility=" + this.f10896e + ", isRaw=" + this.f10897f + ", isForAnnotationParameter=" + this.f10898g + ", visitedTypeParameters=" + c() + ", defaultType=" + a() + ')';
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public a(TypeUsage typeUsage, JavaTypeFlexibility javaTypeFlexibility, boolean z10, boolean z11, Set<? extends TypeParameterDescriptor> set, o0 o0Var) {
        super(typeUsage, set, o0Var);
        k.e(typeUsage, "howThisTypeIsUsed");
        k.e(javaTypeFlexibility, "flexibility");
        this.f10895d = typeUsage;
        this.f10896e = javaTypeFlexibility;
        this.f10897f = z10;
        this.f10898g = z11;
        this.f10899h = set;
        this.f10900i = o0Var;
    }
}
