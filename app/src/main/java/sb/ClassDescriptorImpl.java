package sb;

import fd.StorageManager;
import gd.ClassTypeConstructorImpl;
import gd.TypeConstructor;
import gd.o0;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.Modality;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueClassRepresentation;
import zc.h;

/* compiled from: ClassDescriptorImpl.java */
/* renamed from: sb.h, reason: use source file name */
/* loaded from: classes2.dex */
public class ClassDescriptorImpl extends ClassDescriptorBase {

    /* renamed from: m, reason: collision with root package name */
    private final Modality f18278m;

    /* renamed from: n, reason: collision with root package name */
    private final ClassKind f18279n;

    /* renamed from: o, reason: collision with root package name */
    private final TypeConstructor f18280o;

    /* renamed from: p, reason: collision with root package name */
    private zc.h f18281p;

    /* renamed from: q, reason: collision with root package name */
    private Set<ClassConstructorDescriptor> f18282q;

    /* renamed from: r, reason: collision with root package name */
    private ClassConstructorDescriptor f18283r;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ClassDescriptorImpl(DeclarationDescriptor declarationDescriptor, Name name, Modality modality, ClassKind classKind, Collection<gd.g0> collection, SourceElement sourceElement, boolean z10, StorageManager storageManager) {
        super(storageManager, declarationDescriptor, name, sourceElement, z10);
        if (declarationDescriptor == null) {
            J0(0);
        }
        if (name == null) {
            J0(1);
        }
        if (modality == null) {
            J0(2);
        }
        if (classKind == null) {
            J0(3);
        }
        if (collection == null) {
            J0(4);
        }
        if (sourceElement == null) {
            J0(5);
        }
        if (storageManager == null) {
            J0(6);
        }
        this.f18278m = modality;
        this.f18279n = classKind;
        this.f18280o = new ClassTypeConstructorImpl(this, Collections.emptyList(), collection, storageManager);
    }

    private static /* synthetic */ void J0(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                str = "@NotNull method %s.%s must not return null";
                break;
            case 12:
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                i11 = 2;
                break;
            case 12:
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
                objArr[0] = "name";
                break;
            case 2:
                objArr[0] = "modality";
                break;
            case 3:
                objArr[0] = "kind";
                break;
            case 4:
                objArr[0] = "supertypes";
                break;
            case 5:
                objArr[0] = "source";
                break;
            case 6:
                objArr[0] = "storageManager";
                break;
            case 7:
                objArr[0] = "unsubstitutedMemberScope";
                break;
            case 8:
                objArr[0] = "constructors";
                break;
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/ClassDescriptorImpl";
                break;
            case 12:
                objArr[0] = "kotlinTypeRefiner";
                break;
            default:
                objArr[0] = "containingDeclaration";
                break;
        }
        switch (i10) {
            case 9:
                objArr[1] = "getAnnotations";
                break;
            case 10:
                objArr[1] = "getTypeConstructor";
                break;
            case 11:
                objArr[1] = "getConstructors";
                break;
            case 12:
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/ClassDescriptorImpl";
                break;
            case 13:
                objArr[1] = "getUnsubstitutedMemberScope";
                break;
            case 14:
                objArr[1] = "getStaticScope";
                break;
            case 15:
                objArr[1] = "getKind";
                break;
            case 16:
                objArr[1] = "getModality";
                break;
            case 17:
                objArr[1] = "getVisibility";
                break;
            case 18:
                objArr[1] = "getDeclaredTypeParameters";
                break;
            case 19:
                objArr[1] = "getSealedSubclasses";
                break;
        }
        switch (i10) {
            case 7:
            case 8:
                objArr[2] = "initialize";
                break;
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                break;
            case 12:
                objArr[2] = "getUnsubstitutedMemberScope";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                throw new IllegalStateException(format);
            case 12:
            default:
                throw new IllegalArgumentException(format);
        }
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptorWithTypeParameters
    public List<TypeParameterDescriptor> B() {
        List<TypeParameterDescriptor> emptyList = Collections.emptyList();
        if (emptyList == null) {
            J0(18);
        }
        return emptyList;
    }

    @Override // pb.ClassDescriptor
    public boolean F() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public ValueClassRepresentation<o0> G0() {
        return null;
    }

    @Override // pb.ClassDescriptor
    public boolean L() {
        return false;
    }

    @Override // pb.MemberDescriptor
    public boolean N0() {
        return false;
    }

    @Override // sb.t
    public zc.h Q(hd.g gVar) {
        if (gVar == null) {
            J0(12);
        }
        zc.h hVar = this.f18281p;
        if (hVar == null) {
            J0(13);
        }
        return hVar;
    }

    @Override // pb.ClassDescriptor
    public boolean R0() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassDescriptor> S() {
        List emptyList = Collections.emptyList();
        if (emptyList == null) {
            J0(19);
        }
        return emptyList;
    }

    public final void T0(zc.h hVar, Set<ClassConstructorDescriptor> set, ClassConstructorDescriptor classConstructorDescriptor) {
        if (hVar == null) {
            J0(7);
        }
        if (set == null) {
            J0(8);
        }
        this.f18281p = hVar;
        this.f18282q = set;
        this.f18283r = classConstructorDescriptor;
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return false;
    }

    @Override // pb.ClassDescriptor
    public ClassConstructorDescriptor Z() {
        return this.f18283r;
    }

    @Override // pb.ClassDescriptor
    public zc.h a0() {
        h.b bVar = h.b.f20465b;
        if (bVar == null) {
            J0(14);
        }
        return bVar;
    }

    @Override // pb.ClassDescriptor
    public ClassDescriptor c0() {
        return null;
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u uVar = DescriptorVisibilities.f16733e;
        if (uVar == null) {
            J0(17);
        }
        return uVar;
    }

    @Override // pb.ClassDescriptor
    public ClassKind getKind() {
        ClassKind classKind = this.f18279n;
        if (classKind == null) {
            J0(15);
        }
        return classKind;
    }

    @Override // qb.a
    public qb.g i() {
        qb.g b10 = qb.g.f17195b.b();
        if (b10 == null) {
            J0(9);
        }
        return b10;
    }

    @Override // pb.ClassifierDescriptor
    public TypeConstructor n() {
        TypeConstructor typeConstructor = this.f18280o;
        if (typeConstructor == null) {
            J0(10);
        }
        return typeConstructor;
    }

    @Override // pb.ClassDescriptor, pb.MemberDescriptor
    public Modality o() {
        Modality modality = this.f18278m;
        if (modality == null) {
            J0(16);
        }
        return modality;
    }

    @Override // pb.ClassDescriptor
    public Collection<ClassConstructorDescriptor> p() {
        Set<ClassConstructorDescriptor> set = this.f18282q;
        if (set == null) {
            J0(11);
        }
        return set;
    }

    @Override // pb.ClassDescriptor
    public boolean q() {
        return false;
    }

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public boolean r() {
        return false;
    }

    public String toString() {
        return "class " + getName();
    }

    @Override // pb.ClassDescriptor
    public boolean y() {
        return false;
    }
}
