package qb;

import gd.Variance;
import gd.g0;
import gd.o0;
import java.util.List;
import java.util.Map;
import kotlin.collections.m0;
import kotlin.collections.r;
import ma.u;
import mb.KotlinBuiltIns;
import mb.StandardNames;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.ModuleDescriptor;
import uc.v;
import za.Lambda;

/* compiled from: annotationUtil.kt */
/* renamed from: qb.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class annotationUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final Name f17189a;

    /* renamed from: b, reason: collision with root package name */
    private static final Name f17190b;

    /* renamed from: c, reason: collision with root package name */
    private static final Name f17191c;

    /* renamed from: d, reason: collision with root package name */
    private static final Name f17192d;

    /* renamed from: e, reason: collision with root package name */
    private static final Name f17193e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: annotationUtil.kt */
    /* renamed from: qb.f$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<ModuleDescriptor, g0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ KotlinBuiltIns f17194e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(KotlinBuiltIns kotlinBuiltIns) {
            super(1);
            this.f17194e = kotlinBuiltIns;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(ModuleDescriptor moduleDescriptor) {
            za.k.e(moduleDescriptor, "module");
            o0 l10 = moduleDescriptor.t().l(Variance.INVARIANT, this.f17194e.W());
            za.k.d(l10, "module.builtIns.getArray…ce.INVARIANT, stringType)");
            return l10;
        }
    }

    static {
        Name f10 = Name.f("message");
        za.k.d(f10, "identifier(\"message\")");
        f17189a = f10;
        Name f11 = Name.f("replaceWith");
        za.k.d(f11, "identifier(\"replaceWith\")");
        f17190b = f11;
        Name f12 = Name.f("level");
        za.k.d(f12, "identifier(\"level\")");
        f17191c = f12;
        Name f13 = Name.f("expression");
        za.k.d(f13, "identifier(\"expression\")");
        f17192d = f13;
        Name f14 = Name.f("imports");
        za.k.d(f14, "identifier(\"imports\")");
        f17193e = f14;
    }

    public static final AnnotationDescriptor a(KotlinBuiltIns kotlinBuiltIns, String str, String str2, String str3) {
        List j10;
        Map l10;
        Map l11;
        za.k.e(kotlinBuiltIns, "<this>");
        za.k.e(str, "message");
        za.k.e(str2, "replaceWith");
        za.k.e(str3, "level");
        FqName fqName = StandardNames.a.B;
        Name name = f17193e;
        j10 = r.j();
        l10 = m0.l(u.a(f17192d, new v(str2)), u.a(name, new uc.b(j10, new a(kotlinBuiltIns))));
        BuiltInAnnotationDescriptor builtInAnnotationDescriptor = new BuiltInAnnotationDescriptor(kotlinBuiltIns, fqName, l10);
        FqName fqName2 = StandardNames.a.f15337y;
        Name name2 = f17191c;
        ClassId m10 = ClassId.m(StandardNames.a.A);
        za.k.d(m10, "topLevel(StandardNames.FqNames.deprecationLevel)");
        Name f10 = Name.f(str3);
        za.k.d(f10, "identifier(level)");
        l11 = m0.l(u.a(f17189a, new v(str)), u.a(f17190b, new uc.a(builtInAnnotationDescriptor)), u.a(name2, new uc.j(m10, f10)));
        return new BuiltInAnnotationDescriptor(kotlinBuiltIns, fqName2, l11);
    }

    public static /* synthetic */ AnnotationDescriptor b(KotlinBuiltIns kotlinBuiltIns, String str, String str2, String str3, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            str2 = "";
        }
        if ((i10 & 4) != 0) {
            str3 = "WARNING";
        }
        return a(kotlinBuiltIns, str, str2, str3);
    }
}
