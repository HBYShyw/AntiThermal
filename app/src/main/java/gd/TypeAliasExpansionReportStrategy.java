package gd;

import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import qb.AnnotationDescriptor;

/* compiled from: TypeAliasExpansionReportStrategy.kt */
/* renamed from: gd.z0, reason: use source file name */
/* loaded from: classes2.dex */
public interface TypeAliasExpansionReportStrategy {

    /* compiled from: TypeAliasExpansionReportStrategy.kt */
    /* renamed from: gd.z0$a */
    /* loaded from: classes2.dex */
    public static final class a implements TypeAliasExpansionReportStrategy {

        /* renamed from: a, reason: collision with root package name */
        public static final a f11919a = new a();

        private a() {
        }

        @Override // gd.TypeAliasExpansionReportStrategy
        public void a(TypeAliasDescriptor typeAliasDescriptor) {
            za.k.e(typeAliasDescriptor, "typeAlias");
        }

        @Override // gd.TypeAliasExpansionReportStrategy
        public void b(TypeAliasDescriptor typeAliasDescriptor, TypeParameterDescriptor typeParameterDescriptor, g0 g0Var) {
            za.k.e(typeAliasDescriptor, "typeAlias");
            za.k.e(g0Var, "substitutedArgument");
        }

        @Override // gd.TypeAliasExpansionReportStrategy
        public void c(TypeSubstitutor typeSubstitutor, g0 g0Var, g0 g0Var2, TypeParameterDescriptor typeParameterDescriptor) {
            za.k.e(typeSubstitutor, "substitutor");
            za.k.e(g0Var, "unsubstitutedArgument");
            za.k.e(g0Var2, "argument");
            za.k.e(typeParameterDescriptor, "typeParameter");
        }

        @Override // gd.TypeAliasExpansionReportStrategy
        public void d(AnnotationDescriptor annotationDescriptor) {
            za.k.e(annotationDescriptor, "annotation");
        }
    }

    void a(TypeAliasDescriptor typeAliasDescriptor);

    void b(TypeAliasDescriptor typeAliasDescriptor, TypeParameterDescriptor typeParameterDescriptor, g0 g0Var);

    void c(TypeSubstitutor typeSubstitutor, g0 g0Var, g0 g0Var2, TypeParameterDescriptor typeParameterDescriptor);

    void d(AnnotationDescriptor annotationDescriptor);
}
