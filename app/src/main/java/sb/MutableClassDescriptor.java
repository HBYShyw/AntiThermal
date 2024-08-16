package sb;

import fd.StorageManager;
import gd.ClassTypeConstructorImpl;
import gd.TypeConstructor;
import gd.o0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.DeclarationDescriptor;
import pb.Modality;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import pb.ValueClassRepresentation;
import zc.h;

/* compiled from: MutableClassDescriptor.java */
/* renamed from: sb.y, reason: use source file name */
/* loaded from: classes2.dex */
public class MutableClassDescriptor extends ClassDescriptorBase {

    /* renamed from: m, reason: collision with root package name */
    private final ClassKind f18407m;

    /* renamed from: n, reason: collision with root package name */
    private final boolean f18408n;

    /* renamed from: o, reason: collision with root package name */
    private Modality f18409o;

    /* renamed from: p, reason: collision with root package name */
    private pb.u f18410p;

    /* renamed from: q, reason: collision with root package name */
    private TypeConstructor f18411q;

    /* renamed from: r, reason: collision with root package name */
    private List<TypeParameterDescriptor> f18412r;

    /* renamed from: s, reason: collision with root package name */
    private final Collection<gd.g0> f18413s;

    /* renamed from: t, reason: collision with root package name */
    private final StorageManager f18414t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MutableClassDescriptor(DeclarationDescriptor declarationDescriptor, ClassKind classKind, boolean z10, boolean z11, Name name, SourceElement sourceElement, StorageManager storageManager) {
        super(storageManager, declarationDescriptor, name, sourceElement, z11);
        if (declarationDescriptor == null) {
            J0(0);
        }
        if (classKind == null) {
            J0(1);
        }
        if (name == null) {
            J0(2);
        }
        if (sourceElement == null) {
            J0(3);
        }
        if (storageManager == null) {
            J0(4);
        }
        this.f18413s = new ArrayList();
        this.f18414t = storageManager;
        this.f18407m = classKind;
        this.f18408n = z10;
    }

    private static /* synthetic */ void J0(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
                str = "@NotNull method %s.%s must not return null";
                break;
            case 6:
            case 9:
            case 12:
            case 14:
            case 16:
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
                i11 = 2;
                break;
            case 6:
            case 9:
            case 12:
            case 14:
            case 16:
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
                objArr[0] = "kind";
                break;
            case 2:
                objArr[0] = "name";
                break;
            case 3:
                objArr[0] = "source";
                break;
            case 4:
                objArr[0] = "storageManager";
                break;
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/MutableClassDescriptor";
                break;
            case 6:
                objArr[0] = "modality";
                break;
            case 9:
                objArr[0] = "visibility";
                break;
            case 12:
                objArr[0] = "supertype";
                break;
            case 14:
                objArr[0] = "typeParameters";
                break;
            case 16:
                objArr[0] = "kotlinTypeRefiner";
                break;
            default:
                objArr[0] = "containingDeclaration";
                break;
        }
        switch (i10) {
            case 5:
                objArr[1] = "getAnnotations";
                break;
            case 6:
            case 9:
            case 12:
            case 14:
            case 16:
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/descriptors/impl/MutableClassDescriptor";
                break;
            case 7:
                objArr[1] = "getModality";
                break;
            case 8:
                objArr[1] = "getKind";
                break;
            case 10:
                objArr[1] = "getVisibility";
                break;
            case 11:
                objArr[1] = "getTypeConstructor";
                break;
            case 13:
                objArr[1] = "getConstructors";
                break;
            case 15:
                objArr[1] = "getDeclaredTypeParameters";
                break;
            case 17:
                objArr[1] = "getUnsubstitutedMemberScope";
                break;
            case 18:
                objArr[1] = "getStaticScope";
                break;
            case 19:
                objArr[1] = "getSealedSubclasses";
                break;
        }
        switch (i10) {
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
                break;
            case 6:
                objArr[2] = "setModality";
                break;
            case 9:
                objArr[2] = "setVisibility";
                break;
            case 12:
                objArr[2] = "addSupertype";
                break;
            case 14:
                objArr[2] = "setTypeParameterDescriptors";
                break;
            case 16:
                objArr[2] = "getUnsubstitutedMemberScope";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
                throw new IllegalStateException(format);
            case 6:
            case 9:
            case 12:
            case 14:
            case 16:
            default:
                throw new IllegalArgumentException(format);
        }
    }

    @Override // pb.ClassDescriptor, pb.ClassifierDescriptorWithTypeParameters
    public List<TypeParameterDescriptor> B() {
        List<TypeParameterDescriptor> list = this.f18412r;
        if (list == null) {
            J0(15);
        }
        return list;
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
            J0(16);
        }
        h.b bVar = h.b.f20465b;
        if (bVar == null) {
            J0(17);
        }
        return bVar;
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

    public void T0() {
        this.f18411q = new ClassTypeConstructorImpl(this, this.f18412r, this.f18413s, this.f18414t);
        Iterator<ClassConstructorDescriptor> it = p().iterator();
        while (it.hasNext()) {
            ((ClassConstructorDescriptorImpl) it.next()).p1(x());
        }
    }

    @Override // pb.MemberDescriptor
    public boolean U() {
        return false;
    }

    @Override // pb.ClassDescriptor
    /* renamed from: U0, reason: merged with bridge method [inline-methods] */
    public Set<ClassConstructorDescriptor> p() {
        Set<ClassConstructorDescriptor> emptySet = Collections.emptySet();
        if (emptySet == null) {
            J0(13);
        }
        return emptySet;
    }

    public void V0(Modality modality) {
        if (modality == null) {
            J0(6);
        }
        this.f18409o = modality;
    }

    public void W0(List<TypeParameterDescriptor> list) {
        if (list == null) {
            J0(14);
        }
        if (this.f18412r == null) {
            this.f18412r = new ArrayList(list);
            return;
        }
        throw new IllegalStateException("Type parameters are already set for " + getName());
    }

    public void X0(pb.u uVar) {
        if (uVar == null) {
            J0(9);
        }
        this.f18410p = uVar;
    }

    @Override // pb.ClassDescriptor
    public ClassConstructorDescriptor Z() {
        return null;
    }

    @Override // pb.ClassDescriptor
    public zc.h a0() {
        h.b bVar = h.b.f20465b;
        if (bVar == null) {
            J0(18);
        }
        return bVar;
    }

    @Override // pb.ClassDescriptor
    public ClassDescriptor c0() {
        return null;
    }

    @Override // pb.ClassDescriptor, pb.DeclarationDescriptorWithVisibility, pb.MemberDescriptor
    public pb.u g() {
        pb.u uVar = this.f18410p;
        if (uVar == null) {
            J0(10);
        }
        return uVar;
    }

    @Override // pb.ClassDescriptor
    public ClassKind getKind() {
        ClassKind classKind = this.f18407m;
        if (classKind == null) {
            J0(8);
        }
        return classKind;
    }

    @Override // qb.a
    public qb.g i() {
        qb.g b10 = qb.g.f17195b.b();
        if (b10 == null) {
            J0(5);
        }
        return b10;
    }

    @Override // pb.ClassifierDescriptor
    public TypeConstructor n() {
        TypeConstructor typeConstructor = this.f18411q;
        if (typeConstructor == null) {
            J0(11);
        }
        return typeConstructor;
    }

    @Override // pb.ClassDescriptor, pb.MemberDescriptor
    public Modality o() {
        Modality modality = this.f18409o;
        if (modality == null) {
            J0(7);
        }
        return modality;
    }

    @Override // pb.ClassDescriptor
    public boolean q() {
        return false;
    }

    @Override // pb.ClassifierDescriptorWithTypeParameters
    public boolean r() {
        return this.f18408n;
    }

    public String toString() {
        return DeclarationDescriptorImpl.Q(this);
    }

    @Override // pb.ClassDescriptor
    public boolean y() {
        return false;
    }
}
