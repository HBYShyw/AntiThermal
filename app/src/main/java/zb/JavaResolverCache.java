package zb;

import fc.l;
import fc.n;
import fc.q;
import oc.FqName;
import pb.ClassDescriptor;
import pb.ConstructorDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;

/* compiled from: JavaResolverCache.java */
/* renamed from: zb.g, reason: use source file name */
/* loaded from: classes2.dex */
public interface JavaResolverCache {

    /* renamed from: a, reason: collision with root package name */
    public static final JavaResolverCache f20404a = new a();

    /* compiled from: JavaResolverCache.java */
    /* renamed from: zb.g$a */
    /* loaded from: classes2.dex */
    static class a implements JavaResolverCache {
        a() {
        }

        private static /* synthetic */ void f(int i10) {
            Object[] objArr = new Object[3];
            switch (i10) {
                case 1:
                    objArr[0] = "member";
                    break;
                case 2:
                case 4:
                case 6:
                case 8:
                    objArr[0] = "descriptor";
                    break;
                case 3:
                    objArr[0] = "element";
                    break;
                case 5:
                    objArr[0] = "field";
                    break;
                case 7:
                    objArr[0] = "javaClass";
                    break;
                default:
                    objArr[0] = "fqName";
                    break;
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/components/JavaResolverCache$1";
            switch (i10) {
                case 1:
                case 2:
                    objArr[2] = "recordMethod";
                    break;
                case 3:
                case 4:
                    objArr[2] = "recordConstructor";
                    break;
                case 5:
                case 6:
                    objArr[2] = "recordField";
                    break;
                case 7:
                case 8:
                    objArr[2] = "recordClass";
                    break;
                default:
                    objArr[2] = "getClassResolvedFromSource";
                    break;
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // zb.JavaResolverCache
        public void a(l lVar, ConstructorDescriptor constructorDescriptor) {
            if (lVar == null) {
                f(3);
            }
            if (constructorDescriptor == null) {
                f(4);
            }
        }

        @Override // zb.JavaResolverCache
        public void b(q qVar, SimpleFunctionDescriptor simpleFunctionDescriptor) {
            if (qVar == null) {
                f(1);
            }
            if (simpleFunctionDescriptor == null) {
                f(2);
            }
        }

        @Override // zb.JavaResolverCache
        public void c(fc.g gVar, ClassDescriptor classDescriptor) {
            if (gVar == null) {
                f(7);
            }
            if (classDescriptor == null) {
                f(8);
            }
        }

        @Override // zb.JavaResolverCache
        public ClassDescriptor d(FqName fqName) {
            if (fqName != null) {
                return null;
            }
            f(0);
            return null;
        }

        @Override // zb.JavaResolverCache
        public void e(n nVar, PropertyDescriptor propertyDescriptor) {
            if (nVar == null) {
                f(5);
            }
            if (propertyDescriptor == null) {
                f(6);
            }
        }
    }

    void a(l lVar, ConstructorDescriptor constructorDescriptor);

    void b(q qVar, SimpleFunctionDescriptor simpleFunctionDescriptor);

    void c(fc.g gVar, ClassDescriptor classDescriptor);

    ClassDescriptor d(FqName fqName);

    void e(n nVar, PropertyDescriptor propertyDescriptor);
}
