package zb;

import fc.m;
import gd.g0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.MutableCollections;
import kotlin.collections.m0;
import kotlin.collections.s;
import kotlin.collections.s0;
import ma.u;
import mb.StandardNames;
import oc.ClassId;
import oc.Name;
import pb.ModuleDescriptor;
import pb.ValueParameterDescriptor;
import qb.KotlinRetention;
import qb.KotlinTarget;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: JavaAnnotationMapper.kt */
/* loaded from: classes2.dex */
public final class d {

    /* renamed from: a, reason: collision with root package name */
    public static final d f20396a = new d();

    /* renamed from: b, reason: collision with root package name */
    private static final Map<String, EnumSet<KotlinTarget>> f20397b;

    /* renamed from: c, reason: collision with root package name */
    private static final Map<String, KotlinRetention> f20398c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JavaAnnotationMapper.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements l<ModuleDescriptor, g0> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f20399e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(ModuleDescriptor moduleDescriptor) {
            k.e(moduleDescriptor, "module");
            ValueParameterDescriptor b10 = DescriptorResolverUtils.b(c.f20391a.d(), moduleDescriptor.t().o(StandardNames.a.H));
            g0 type = b10 != null ? b10.getType() : null;
            return type == null ? ErrorUtils.d(ErrorTypeKind.H0, new String[0]) : type;
        }
    }

    static {
        Map<String, EnumSet<KotlinTarget>> l10;
        Map<String, KotlinRetention> l11;
        l10 = m0.l(u.a("PACKAGE", EnumSet.noneOf(KotlinTarget.class)), u.a("TYPE", EnumSet.of(KotlinTarget.f17246x, KotlinTarget.K)), u.a("ANNOTATION_TYPE", EnumSet.of(KotlinTarget.f17247y)), u.a("TYPE_PARAMETER", EnumSet.of(KotlinTarget.f17248z)), u.a("FIELD", EnumSet.of(KotlinTarget.B)), u.a("LOCAL_VARIABLE", EnumSet.of(KotlinTarget.C)), u.a("PARAMETER", EnumSet.of(KotlinTarget.D)), u.a("CONSTRUCTOR", EnumSet.of(KotlinTarget.E)), u.a("METHOD", EnumSet.of(KotlinTarget.F, KotlinTarget.G, KotlinTarget.H)), u.a("TYPE_USE", EnumSet.of(KotlinTarget.I)));
        f20397b = l10;
        l11 = m0.l(u.a("RUNTIME", KotlinRetention.RUNTIME), u.a("CLASS", KotlinRetention.BINARY), u.a("SOURCE", KotlinRetention.SOURCE));
        f20398c = l11;
    }

    private d() {
    }

    public final uc.g<?> a(fc.b bVar) {
        m mVar = bVar instanceof m ? (m) bVar : null;
        if (mVar == null) {
            return null;
        }
        Map<String, KotlinRetention> map = f20398c;
        Name d10 = mVar.d();
        KotlinRetention kotlinRetention = map.get(d10 != null ? d10.b() : null);
        if (kotlinRetention == null) {
            return null;
        }
        ClassId m10 = ClassId.m(StandardNames.a.K);
        k.d(m10, "topLevel(StandardNames.F…ames.annotationRetention)");
        Name f10 = Name.f(kotlinRetention.name());
        k.d(f10, "identifier(retention.name)");
        return new uc.j(m10, f10);
    }

    public final Set<KotlinTarget> b(String str) {
        Set<KotlinTarget> e10;
        EnumSet<KotlinTarget> enumSet = f20397b.get(str);
        if (enumSet != null) {
            return enumSet;
        }
        e10 = s0.e();
        return e10;
    }

    public final uc.g<?> c(List<? extends fc.b> list) {
        int u7;
        k.e(list, "arguments");
        ArrayList<m> arrayList = new ArrayList();
        for (Object obj : list) {
            if (obj instanceof m) {
                arrayList.add(obj);
            }
        }
        ArrayList<KotlinTarget> arrayList2 = new ArrayList();
        for (m mVar : arrayList) {
            d dVar = f20396a;
            Name d10 = mVar.d();
            MutableCollections.z(arrayList2, dVar.b(d10 != null ? d10.b() : null));
        }
        u7 = s.u(arrayList2, 10);
        ArrayList arrayList3 = new ArrayList(u7);
        for (KotlinTarget kotlinTarget : arrayList2) {
            ClassId m10 = ClassId.m(StandardNames.a.J);
            k.d(m10, "topLevel(StandardNames.FqNames.annotationTarget)");
            Name f10 = Name.f(kotlinTarget.name());
            k.d(f10, "identifier(kotlinTarget.name)");
            arrayList3.add(new uc.j(m10, f10));
        }
        return new uc.b(arrayList3, a.f20399e);
    }
}
