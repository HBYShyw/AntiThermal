package gd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.collections._Collections;
import pb.ClassifierDescriptor;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: TypeAliasExpansion.kt */
/* renamed from: gd.y0, reason: use source file name */
/* loaded from: classes2.dex */
public final class TypeAliasExpansion {

    /* renamed from: e, reason: collision with root package name */
    public static final a f11914e = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final TypeAliasExpansion f11915a;

    /* renamed from: b, reason: collision with root package name */
    private final TypeAliasDescriptor f11916b;

    /* renamed from: c, reason: collision with root package name */
    private final List<TypeProjection> f11917c;

    /* renamed from: d, reason: collision with root package name */
    private final Map<TypeParameterDescriptor, TypeProjection> f11918d;

    /* compiled from: TypeAliasExpansion.kt */
    /* renamed from: gd.y0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final TypeAliasExpansion a(TypeAliasExpansion typeAliasExpansion, TypeAliasDescriptor typeAliasDescriptor, List<? extends TypeProjection> list) {
            int u7;
            List G0;
            Map q10;
            za.k.e(typeAliasDescriptor, "typeAliasDescriptor");
            za.k.e(list, "arguments");
            List<TypeParameterDescriptor> parameters = typeAliasDescriptor.n().getParameters();
            za.k.d(parameters, "typeAliasDescriptor.typeConstructor.parameters");
            u7 = kotlin.collections.s.u(parameters, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = parameters.iterator();
            while (it.hasNext()) {
                arrayList.add(((TypeParameterDescriptor) it.next()).a());
            }
            G0 = _Collections.G0(arrayList, list);
            q10 = kotlin.collections.m0.q(G0);
            return new TypeAliasExpansion(typeAliasExpansion, typeAliasDescriptor, list, q10, null);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private TypeAliasExpansion(TypeAliasExpansion typeAliasExpansion, TypeAliasDescriptor typeAliasDescriptor, List<? extends TypeProjection> list, Map<TypeParameterDescriptor, ? extends TypeProjection> map) {
        this.f11915a = typeAliasExpansion;
        this.f11916b = typeAliasDescriptor;
        this.f11917c = list;
        this.f11918d = map;
    }

    public /* synthetic */ TypeAliasExpansion(TypeAliasExpansion typeAliasExpansion, TypeAliasDescriptor typeAliasDescriptor, List list, Map map, DefaultConstructorMarker defaultConstructorMarker) {
        this(typeAliasExpansion, typeAliasDescriptor, list, map);
    }

    public final List<TypeProjection> a() {
        return this.f11917c;
    }

    public final TypeAliasDescriptor b() {
        return this.f11916b;
    }

    public final TypeProjection c(TypeConstructor typeConstructor) {
        za.k.e(typeConstructor, "constructor");
        ClassifierDescriptor v7 = typeConstructor.v();
        if (v7 instanceof TypeParameterDescriptor) {
            return this.f11918d.get(v7);
        }
        return null;
    }

    public final boolean d(TypeAliasDescriptor typeAliasDescriptor) {
        za.k.e(typeAliasDescriptor, "descriptor");
        if (!za.k.a(this.f11916b, typeAliasDescriptor)) {
            TypeAliasExpansion typeAliasExpansion = this.f11915a;
            if (!(typeAliasExpansion != null ? typeAliasExpansion.d(typeAliasDescriptor) : false)) {
                return false;
            }
        }
        return true;
    }
}
