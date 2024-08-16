package gd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.ClassifierDescriptorWithTypeParameters;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.TypeParameterDescriptor;

/* compiled from: StarProjectionImpl.kt */
/* loaded from: classes2.dex */
public final class v0 {

    /* compiled from: StarProjectionImpl.kt */
    /* loaded from: classes2.dex */
    public static final class a extends h1 {

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ List<TypeConstructor> f11896d;

        /* JADX WARN: Multi-variable type inference failed */
        a(List<? extends TypeConstructor> list) {
            this.f11896d = list;
        }

        @Override // gd.h1
        public TypeProjection k(TypeConstructor typeConstructor) {
            za.k.e(typeConstructor, "key");
            if (!this.f11896d.contains(typeConstructor)) {
                return null;
            }
            ClassifierDescriptor v7 = typeConstructor.v();
            za.k.c(v7, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.TypeParameterDescriptor");
            return s1.s((TypeParameterDescriptor) v7);
        }
    }

    private static final g0 a(List<? extends TypeConstructor> list, List<? extends g0> list2, KotlinBuiltIns kotlinBuiltIns) {
        Object T;
        TypeSubstitutor g6 = TypeSubstitutor.g(new a(list));
        T = _Collections.T(list2);
        g0 p10 = g6.p((g0) T, Variance.OUT_VARIANCE);
        if (p10 == null) {
            p10 = kotlinBuiltIns.y();
        }
        za.k.d(p10, "typeParameters: List<Typ… ?: builtIns.defaultBound");
        return p10;
    }

    public static final g0 b(TypeParameterDescriptor typeParameterDescriptor) {
        int u7;
        int u10;
        za.k.e(typeParameterDescriptor, "<this>");
        DeclarationDescriptor b10 = typeParameterDescriptor.b();
        za.k.d(b10, "this.containingDeclaration");
        if (b10 instanceof ClassifierDescriptorWithTypeParameters) {
            List<TypeParameterDescriptor> parameters = ((ClassifierDescriptorWithTypeParameters) b10).n().getParameters();
            za.k.d(parameters, "descriptor.typeConstructor.parameters");
            u10 = kotlin.collections.s.u(parameters, 10);
            ArrayList arrayList = new ArrayList(u10);
            Iterator<T> it = parameters.iterator();
            while (it.hasNext()) {
                TypeConstructor n10 = ((TypeParameterDescriptor) it.next()).n();
                za.k.d(n10, "it.typeConstructor");
                arrayList.add(n10);
            }
            List<g0> upperBounds = typeParameterDescriptor.getUpperBounds();
            za.k.d(upperBounds, "upperBounds");
            return a(arrayList, upperBounds, wc.c.j(typeParameterDescriptor));
        }
        if (b10 instanceof FunctionDescriptor) {
            List<TypeParameterDescriptor> m10 = ((FunctionDescriptor) b10).m();
            za.k.d(m10, "descriptor.typeParameters");
            u7 = kotlin.collections.s.u(m10, 10);
            ArrayList arrayList2 = new ArrayList(u7);
            Iterator<T> it2 = m10.iterator();
            while (it2.hasNext()) {
                TypeConstructor n11 = ((TypeParameterDescriptor) it2.next()).n();
                za.k.d(n11, "it.typeConstructor");
                arrayList2.add(n11);
            }
            List<g0> upperBounds2 = typeParameterDescriptor.getUpperBounds();
            za.k.d(upperBounds2, "upperBounds");
            return a(arrayList2, upperBounds2, wc.c.j(typeParameterDescriptor));
        }
        throw new IllegalArgumentException("Unsupported descriptor type to build star projection type based on type parameters of it");
    }
}
