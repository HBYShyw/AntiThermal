package rc;

import java.util.ArrayList;
import java.util.List;
import kotlin.collections.x;
import oc.FqNameUnsafe;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PackageFragmentDescriptor;
import pb.TypeParameterDescriptor;

/* compiled from: ClassifierNamePolicy.kt */
/* renamed from: rc.b, reason: use source file name */
/* loaded from: classes2.dex */
public interface ClassifierNamePolicy {

    /* compiled from: ClassifierNamePolicy.kt */
    /* renamed from: rc.b$a */
    /* loaded from: classes2.dex */
    public static final class a implements ClassifierNamePolicy {

        /* renamed from: a, reason: collision with root package name */
        public static final a f17699a = new a();

        private a() {
        }

        @Override // rc.ClassifierNamePolicy
        public String a(ClassifierDescriptor classifierDescriptor, rc.c cVar) {
            za.k.e(classifierDescriptor, "classifier");
            za.k.e(cVar, "renderer");
            if (classifierDescriptor instanceof TypeParameterDescriptor) {
                Name name = ((TypeParameterDescriptor) classifierDescriptor).getName();
                za.k.d(name, "classifier.name");
                return cVar.v(name, false);
            }
            FqNameUnsafe m10 = sc.e.m(classifierDescriptor);
            za.k.d(m10, "getFqName(classifier)");
            return cVar.u(m10);
        }
    }

    /* compiled from: ClassifierNamePolicy.kt */
    /* renamed from: rc.b$b */
    /* loaded from: classes2.dex */
    public static final class b implements ClassifierNamePolicy {

        /* renamed from: a, reason: collision with root package name */
        public static final b f17700a = new b();

        private b() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v0, types: [pb.h, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r1v1, types: [pb.j0, pb.m] */
        /* JADX WARN: Type inference failed for: r1v2, types: [pb.m] */
        @Override // rc.ClassifierNamePolicy
        public String a(ClassifierDescriptor classifierDescriptor, rc.c cVar) {
            List F;
            za.k.e(classifierDescriptor, "classifier");
            za.k.e(cVar, "renderer");
            if (classifierDescriptor instanceof TypeParameterDescriptor) {
                Name name = ((TypeParameterDescriptor) classifierDescriptor).getName();
                za.k.d(name, "classifier.name");
                return cVar.v(name, false);
            }
            ArrayList arrayList = new ArrayList();
            do {
                arrayList.add(classifierDescriptor.getName());
                classifierDescriptor = classifierDescriptor.b();
            } while (classifierDescriptor instanceof ClassDescriptor);
            F = x.F(arrayList);
            return RenderingUtils.c(F);
        }
    }

    /* compiled from: ClassifierNamePolicy.kt */
    /* renamed from: rc.b$c */
    /* loaded from: classes2.dex */
    public static final class c implements ClassifierNamePolicy {

        /* renamed from: a, reason: collision with root package name */
        public static final c f17701a = new c();

        private c() {
        }

        private final String b(ClassifierDescriptor classifierDescriptor) {
            Name name = classifierDescriptor.getName();
            za.k.d(name, "descriptor.name");
            String b10 = RenderingUtils.b(name);
            if (classifierDescriptor instanceof TypeParameterDescriptor) {
                return b10;
            }
            DeclarationDescriptor b11 = classifierDescriptor.b();
            za.k.d(b11, "descriptor.containingDeclaration");
            String c10 = c(b11);
            if (c10 == null || za.k.a(c10, "")) {
                return b10;
            }
            return c10 + '.' + b10;
        }

        private final String c(DeclarationDescriptor declarationDescriptor) {
            if (declarationDescriptor instanceof ClassDescriptor) {
                return b((ClassifierDescriptor) declarationDescriptor);
            }
            if (!(declarationDescriptor instanceof PackageFragmentDescriptor)) {
                return null;
            }
            FqNameUnsafe j10 = ((PackageFragmentDescriptor) declarationDescriptor).d().j();
            za.k.d(j10, "descriptor.fqName.toUnsafe()");
            return RenderingUtils.a(j10);
        }

        @Override // rc.ClassifierNamePolicy
        public String a(ClassifierDescriptor classifierDescriptor, rc.c cVar) {
            za.k.e(classifierDescriptor, "classifier");
            za.k.e(cVar, "renderer");
            return b(classifierDescriptor);
        }
    }

    String a(ClassifierDescriptor classifierDescriptor, rc.c cVar);
}
