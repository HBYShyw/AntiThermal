package id;

import gd.TypeConstructor;
import gd.TypeProjection;
import gd.g0;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.SetsJVM;
import kotlin.collections.r;
import oc.Name;
import pb.DeclarationDescriptor;
import pb.ModuleDescriptor;
import pb.PropertyDescriptor;

/* compiled from: ErrorUtils.kt */
/* renamed from: id.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class ErrorUtils {

    /* renamed from: a, reason: collision with root package name */
    public static final ErrorUtils f12833a = new ErrorUtils();

    /* renamed from: b, reason: collision with root package name */
    private static final ModuleDescriptor f12834b = ErrorModuleDescriptor.f12754e;

    /* renamed from: c, reason: collision with root package name */
    private static final ErrorClassDescriptor f12835c;

    /* renamed from: d, reason: collision with root package name */
    private static final g0 f12836d;

    /* renamed from: e, reason: collision with root package name */
    private static final g0 f12837e;

    /* renamed from: f, reason: collision with root package name */
    private static final PropertyDescriptor f12838f;

    /* renamed from: g, reason: collision with root package name */
    private static final Set<PropertyDescriptor> f12839g;

    static {
        Set<PropertyDescriptor> d10;
        String format = String.format(ErrorEntity.ERROR_CLASS.b(), Arrays.copyOf(new Object[]{"unknown class"}, 1));
        za.k.d(format, "format(this, *args)");
        Name i10 = Name.i(format);
        za.k.d(i10, "special(ErrorEntity.ERRO….format(\"unknown class\"))");
        f12835c = new ErrorClassDescriptor(i10);
        f12836d = d(ErrorTypeKind.f12829z, new String[0]);
        f12837e = d(ErrorTypeKind.f12824w0, new String[0]);
        ErrorPropertyDescriptor errorPropertyDescriptor = new ErrorPropertyDescriptor();
        f12838f = errorPropertyDescriptor;
        d10 = SetsJVM.d(errorPropertyDescriptor);
        f12839g = d10;
    }

    private ErrorUtils() {
    }

    public static final ErrorScope a(ErrorScopeKind errorScopeKind, boolean z10, String... strArr) {
        za.k.e(errorScopeKind, "kind");
        za.k.e(strArr, "formatParams");
        return z10 ? new ThrowingScope(errorScopeKind, (String[]) Arrays.copyOf(strArr, strArr.length)) : new ErrorScope(errorScopeKind, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public static final ErrorScope b(ErrorScopeKind errorScopeKind, String... strArr) {
        za.k.e(errorScopeKind, "kind");
        za.k.e(strArr, "formatParams");
        return a(errorScopeKind, false, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public static final ErrorType d(ErrorTypeKind errorTypeKind, String... strArr) {
        List<? extends TypeProjection> j10;
        za.k.e(errorTypeKind, "kind");
        za.k.e(strArr, "formatParams");
        ErrorUtils errorUtils = f12833a;
        j10 = r.j();
        return errorUtils.g(errorTypeKind, j10, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public static final boolean m(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor != null) {
            ErrorUtils errorUtils = f12833a;
            if (errorUtils.n(declarationDescriptor) || errorUtils.n(declarationDescriptor.b()) || declarationDescriptor == f12834b) {
                return true;
            }
        }
        return false;
    }

    private final boolean n(DeclarationDescriptor declarationDescriptor) {
        return declarationDescriptor instanceof ErrorClassDescriptor;
    }

    public static final boolean o(g0 g0Var) {
        if (g0Var == null) {
            return false;
        }
        TypeConstructor W0 = g0Var.W0();
        return (W0 instanceof ErrorTypeConstructor) && ((ErrorTypeConstructor) W0).c() == ErrorTypeKind.C;
    }

    public final ErrorType c(ErrorTypeKind errorTypeKind, TypeConstructor typeConstructor, String... strArr) {
        List<? extends TypeProjection> j10;
        za.k.e(errorTypeKind, "kind");
        za.k.e(typeConstructor, "typeConstructor");
        za.k.e(strArr, "formatParams");
        j10 = r.j();
        return f(errorTypeKind, j10, typeConstructor, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public final ErrorTypeConstructor e(ErrorTypeKind errorTypeKind, String... strArr) {
        za.k.e(errorTypeKind, "kind");
        za.k.e(strArr, "formatParams");
        return new ErrorTypeConstructor(errorTypeKind, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public final ErrorType f(ErrorTypeKind errorTypeKind, List<? extends TypeProjection> list, TypeConstructor typeConstructor, String... strArr) {
        za.k.e(errorTypeKind, "kind");
        za.k.e(list, "arguments");
        za.k.e(typeConstructor, "typeConstructor");
        za.k.e(strArr, "formatParams");
        return new ErrorType(typeConstructor, b(ErrorScopeKind.ERROR_TYPE_SCOPE, typeConstructor.toString()), errorTypeKind, list, false, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public final ErrorType g(ErrorTypeKind errorTypeKind, List<? extends TypeProjection> list, String... strArr) {
        za.k.e(errorTypeKind, "kind");
        za.k.e(list, "arguments");
        za.k.e(strArr, "formatParams");
        return f(errorTypeKind, list, e(errorTypeKind, (String[]) Arrays.copyOf(strArr, strArr.length)), (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public final ErrorClassDescriptor h() {
        return f12835c;
    }

    public final ModuleDescriptor i() {
        return f12834b;
    }

    public final Set<PropertyDescriptor> j() {
        return f12839g;
    }

    public final g0 k() {
        return f12837e;
    }

    public final g0 l() {
        return f12836d;
    }

    public final String p(g0 g0Var) {
        za.k.e(g0Var, "type");
        ld.a.s(g0Var);
        TypeConstructor W0 = g0Var.W0();
        Objects.requireNonNull(W0, "null cannot be cast to non-null type org.jetbrains.kotlin.types.error.ErrorTypeConstructor");
        return ((ErrorTypeConstructor) W0).d(0);
    }
}
