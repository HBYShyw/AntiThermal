package ob;

import fd.StorageManager;
import fd.m;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.SetsJVM;
import kotlin.collections._Collections;
import kotlin.collections.s0;
import mb.BuiltInsPackageFragment;
import mb.StandardNames;
import oc.ClassId;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.DeclarationDescriptor;
import pb.Modality;
import pb.ModuleDescriptor;
import pb.PackageFragmentDescriptor;
import pb.SourceElement;
import rb.ClassDescriptorFactory;
import sb.ClassDescriptorImpl;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: JvmBuiltInClassDescriptorFactory.kt */
/* renamed from: ob.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmBuiltInClassDescriptorFactory implements ClassDescriptorFactory {

    /* renamed from: g, reason: collision with root package name */
    private static final Name f16363g;

    /* renamed from: h, reason: collision with root package name */
    private static final ClassId f16364h;

    /* renamed from: a, reason: collision with root package name */
    private final ModuleDescriptor f16365a;

    /* renamed from: b, reason: collision with root package name */
    private final ya.l<ModuleDescriptor, DeclarationDescriptor> f16366b;

    /* renamed from: c, reason: collision with root package name */
    private final fd.i f16367c;

    /* renamed from: e, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f16361e = {Reflection.g(new PropertyReference1Impl(Reflection.b(JvmBuiltInClassDescriptorFactory.class), "cloneable", "getCloneable()Lorg/jetbrains/kotlin/descriptors/impl/ClassDescriptorImpl;"))};

    /* renamed from: d, reason: collision with root package name */
    public static final b f16360d = new b(null);

    /* renamed from: f, reason: collision with root package name */
    private static final FqName f16362f = StandardNames.f15283u;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: JvmBuiltInClassDescriptorFactory.kt */
    /* renamed from: ob.e$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<ModuleDescriptor, BuiltInsPackageFragment> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f16368e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final BuiltInsPackageFragment invoke(ModuleDescriptor moduleDescriptor) {
            Object T;
            za.k.e(moduleDescriptor, "module");
            List<PackageFragmentDescriptor> R = moduleDescriptor.H(JvmBuiltInClassDescriptorFactory.f16362f).R();
            ArrayList arrayList = new ArrayList();
            for (Object obj : R) {
                if (obj instanceof BuiltInsPackageFragment) {
                    arrayList.add(obj);
                }
            }
            T = _Collections.T(arrayList);
            return (BuiltInsPackageFragment) T;
        }
    }

    /* compiled from: JvmBuiltInClassDescriptorFactory.kt */
    /* renamed from: ob.e$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final ClassId a() {
            return JvmBuiltInClassDescriptorFactory.f16364h;
        }
    }

    /* compiled from: JvmBuiltInClassDescriptorFactory.kt */
    /* renamed from: ob.e$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.a<ClassDescriptorImpl> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ StorageManager f16370f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(StorageManager storageManager) {
            super(0);
            this.f16370f = storageManager;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ClassDescriptorImpl invoke() {
            List e10;
            Set<ClassConstructorDescriptor> e11;
            DeclarationDescriptor declarationDescriptor = (DeclarationDescriptor) JvmBuiltInClassDescriptorFactory.this.f16366b.invoke(JvmBuiltInClassDescriptorFactory.this.f16365a);
            Name name = JvmBuiltInClassDescriptorFactory.f16363g;
            Modality modality = Modality.ABSTRACT;
            ClassKind classKind = ClassKind.INTERFACE;
            e10 = CollectionsJVM.e(JvmBuiltInClassDescriptorFactory.this.f16365a.t().i());
            ClassDescriptorImpl classDescriptorImpl = new ClassDescriptorImpl(declarationDescriptor, name, modality, classKind, e10, SourceElement.f16664a, false, this.f16370f);
            CloneableClassScope cloneableClassScope = new CloneableClassScope(this.f16370f, classDescriptorImpl);
            e11 = s0.e();
            classDescriptorImpl.T0(cloneableClassScope, e11, null);
            return classDescriptorImpl;
        }
    }

    static {
        FqNameUnsafe fqNameUnsafe = StandardNames.a.f15295d;
        Name i10 = fqNameUnsafe.i();
        za.k.d(i10, "cloneable.shortName()");
        f16363g = i10;
        ClassId m10 = ClassId.m(fqNameUnsafe.l());
        za.k.d(m10, "topLevel(StandardNames.FqNames.cloneable.toSafe())");
        f16364h = m10;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JvmBuiltInClassDescriptorFactory(StorageManager storageManager, ModuleDescriptor moduleDescriptor, ya.l<? super ModuleDescriptor, ? extends DeclarationDescriptor> lVar) {
        za.k.e(storageManager, "storageManager");
        za.k.e(moduleDescriptor, "moduleDescriptor");
        za.k.e(lVar, "computeContainingDeclaration");
        this.f16365a = moduleDescriptor;
        this.f16366b = lVar;
        this.f16367c = storageManager.g(new c(storageManager));
    }

    private final ClassDescriptorImpl i() {
        return (ClassDescriptorImpl) m.a(this.f16367c, this, f16361e[0]);
    }

    @Override // rb.ClassDescriptorFactory
    public ClassDescriptor a(ClassId classId) {
        za.k.e(classId, "classId");
        if (za.k.a(classId, f16364h)) {
            return i();
        }
        return null;
    }

    @Override // rb.ClassDescriptorFactory
    public boolean b(FqName fqName, Name name) {
        za.k.e(fqName, "packageFqName");
        za.k.e(name, "name");
        return za.k.a(name, f16363g) && za.k.a(fqName, f16362f);
    }

    @Override // rb.ClassDescriptorFactory
    public Collection<ClassDescriptor> c(FqName fqName) {
        Set e10;
        Set d10;
        za.k.e(fqName, "packageFqName");
        if (za.k.a(fqName, f16362f)) {
            d10 = SetsJVM.d(i());
            return d10;
        }
        e10 = s0.e();
        return e10;
    }

    public /* synthetic */ JvmBuiltInClassDescriptorFactory(StorageManager storageManager, ModuleDescriptor moduleDescriptor, ya.l lVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(storageManager, moduleDescriptor, (i10 & 4) != 0 ? a.f16368e : lVar);
    }
}
