package gd;

import fd.StorageManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import pb.ClassDescriptor;
import pb.SupertypeLoopChecker;
import pb.TypeParameterDescriptor;

/* compiled from: ClassTypeConstructorImpl.java */
/* renamed from: gd.l, reason: use source file name */
/* loaded from: classes2.dex */
public class ClassTypeConstructorImpl extends AbstractClassTypeConstructor {

    /* renamed from: d, reason: collision with root package name */
    private final ClassDescriptor f11850d;

    /* renamed from: e, reason: collision with root package name */
    private final List<TypeParameterDescriptor> f11851e;

    /* renamed from: f, reason: collision with root package name */
    private final Collection<g0> f11852f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ClassTypeConstructorImpl(ClassDescriptor classDescriptor, List<? extends TypeParameterDescriptor> list, Collection<g0> collection, StorageManager storageManager) {
        super(storageManager);
        if (classDescriptor == null) {
            r(0);
        }
        if (list == null) {
            r(1);
        }
        if (collection == null) {
            r(2);
        }
        if (storageManager == null) {
            r(3);
        }
        this.f11850d = classDescriptor;
        this.f11851e = Collections.unmodifiableList(new ArrayList(list));
        this.f11852f = Collections.unmodifiableCollection(collection);
    }

    private static /* synthetic */ void r(int i10) {
        String str = (i10 == 4 || i10 == 5 || i10 == 6 || i10 == 7) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 4 || i10 == 5 || i10 == 6 || i10 == 7) ? 2 : 3];
        switch (i10) {
            case 1:
                objArr[0] = "parameters";
                break;
            case 2:
                objArr[0] = "supertypes";
                break;
            case 3:
                objArr[0] = "storageManager";
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/types/ClassTypeConstructorImpl";
                break;
            default:
                objArr[0] = "classDescriptor";
                break;
        }
        if (i10 == 4) {
            objArr[1] = "getParameters";
        } else if (i10 == 5) {
            objArr[1] = "getDeclarationDescriptor";
        } else if (i10 == 6) {
            objArr[1] = "computeSupertypes";
        } else if (i10 != 7) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/types/ClassTypeConstructorImpl";
        } else {
            objArr[1] = "getSupertypeLoopChecker";
        }
        if (i10 != 4 && i10 != 5 && i10 != 6 && i10 != 7) {
            objArr[2] = "<init>";
        }
        String format = String.format(str, objArr);
        if (i10 != 4 && i10 != 5 && i10 != 6 && i10 != 7) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // gd.TypeConstructor
    public List<TypeParameterDescriptor> getParameters() {
        List<TypeParameterDescriptor> list = this.f11851e;
        if (list == null) {
            r(4);
        }
        return list;
    }

    @Override // gd.AbstractTypeConstructor
    protected Collection<g0> h() {
        Collection<g0> collection = this.f11852f;
        if (collection == null) {
            r(6);
        }
        return collection;
    }

    @Override // gd.AbstractTypeConstructor
    protected SupertypeLoopChecker l() {
        SupertypeLoopChecker.a aVar = SupertypeLoopChecker.a.f16675a;
        if (aVar == null) {
            r(7);
        }
        return aVar;
    }

    @Override // gd.ClassifierBasedTypeConstructor, gd.TypeConstructor
    /* renamed from: s */
    public ClassDescriptor v() {
        ClassDescriptor classDescriptor = this.f11850d;
        if (classDescriptor == null) {
            r(5);
        }
        return classDescriptor;
    }

    public String toString() {
        return sc.e.m(this.f11850d).b();
    }

    @Override // gd.TypeConstructor
    public boolean w() {
        return true;
    }
}
