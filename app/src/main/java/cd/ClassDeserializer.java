package cd;

import ed.DeserializedClassDescriptor;
import java.util.Iterator;
import java.util.Set;
import kotlin.collections.SetsJVM;
import lc.BinaryVersion;
import lc.NameResolver;
import lc.TypeTable;
import lc.VersionRequirement;
import mb.StandardNames;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.ClassDescriptor;
import pb.PackageFragmentDescriptor;
import pb.SourceElement;
import pb.m0;
import pb.o0;
import rb.ClassDescriptorFactory;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: ClassDeserializer.kt */
/* renamed from: cd.i */
/* loaded from: classes2.dex */
public final class ClassDeserializer {

    /* renamed from: c */
    public static final b f5232c = new b(null);

    /* renamed from: d */
    private static final Set<ClassId> f5233d;

    /* renamed from: a */
    private final k f5234a;

    /* renamed from: b */
    private final ya.l<a, ClassDescriptor> f5235b;

    /* compiled from: ClassDeserializer.kt */
    /* renamed from: cd.i$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a */
        private final ClassId f5236a;

        /* renamed from: b */
        private final ClassData f5237b;

        public a(ClassId classId, ClassData classData) {
            za.k.e(classId, "classId");
            this.f5236a = classId;
            this.f5237b = classData;
        }

        public final ClassData a() {
            return this.f5237b;
        }

        public final ClassId b() {
            return this.f5236a;
        }

        public boolean equals(Object obj) {
            return (obj instanceof a) && za.k.a(this.f5236a, ((a) obj).f5236a);
        }

        public int hashCode() {
            return this.f5236a.hashCode();
        }
    }

    /* compiled from: ClassDeserializer.kt */
    /* renamed from: cd.i$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Set<ClassId> a() {
            return ClassDeserializer.f5233d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClassDeserializer.kt */
    /* renamed from: cd.i$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<a, ClassDescriptor> {
        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a */
        public final ClassDescriptor invoke(a aVar) {
            za.k.e(aVar, "key");
            return ClassDeserializer.this.c(aVar);
        }
    }

    static {
        Set<ClassId> d10;
        d10 = SetsJVM.d(ClassId.m(StandardNames.a.f15295d.l()));
        f5233d = d10;
    }

    public ClassDeserializer(k kVar) {
        za.k.e(kVar, "components");
        this.f5234a = kVar;
        this.f5235b = kVar.u().b(new c());
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00b8 A[EDGE_INSN: B:43:0x00b8->B:44:0x00b8 BREAK  A[LOOP:1: B:34:0x0090->B:48:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:? A[LOOP:1: B:34:0x0090->B:48:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final ClassDescriptor c(a aVar) {
        Object obj;
        m a10;
        boolean z10;
        ClassId b10 = aVar.b();
        Iterator<ClassDescriptorFactory> it = this.f5234a.k().iterator();
        while (it.hasNext()) {
            ClassDescriptor a11 = it.next().a(b10);
            if (a11 != null) {
                return a11;
            }
        }
        if (f5233d.contains(b10)) {
            return null;
        }
        ClassData a12 = aVar.a();
        if (a12 == null && (a12 = this.f5234a.e().a(b10)) == null) {
            return null;
        }
        NameResolver a13 = a12.a();
        jc.c b11 = a12.b();
        BinaryVersion c10 = a12.c();
        SourceElement d10 = a12.d();
        ClassId g6 = b10.g();
        if (g6 != null) {
            ClassDescriptor e10 = e(this, g6, null, 2, null);
            DeserializedClassDescriptor deserializedClassDescriptor = e10 instanceof DeserializedClassDescriptor ? (DeserializedClassDescriptor) e10 : null;
            if (deserializedClassDescriptor == null) {
                return null;
            }
            Name j10 = b10.j();
            za.k.d(j10, "classId.shortClassName");
            if (!deserializedClassDescriptor.p1(j10)) {
                return null;
            }
            a10 = deserializedClassDescriptor.j1();
        } else {
            m0 r10 = this.f5234a.r();
            FqName h10 = b10.h();
            za.k.d(h10, "classId.packageFqName");
            Iterator<T> it2 = o0.c(r10, h10).iterator();
            while (true) {
                if (!it2.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it2.next();
                PackageFragmentDescriptor packageFragmentDescriptor = (PackageFragmentDescriptor) obj;
                if (packageFragmentDescriptor instanceof DeserializedPackageFragment) {
                    Name j11 = b10.j();
                    za.k.d(j11, "classId.shortClassName");
                    if (!((DeserializedPackageFragment) packageFragmentDescriptor).T0(j11)) {
                        z10 = false;
                        if (!z10) {
                            break;
                        }
                    }
                }
                z10 = true;
                if (!z10) {
                }
            }
            PackageFragmentDescriptor packageFragmentDescriptor2 = (PackageFragmentDescriptor) obj;
            if (packageFragmentDescriptor2 == null) {
                return null;
            }
            k kVar = this.f5234a;
            jc.t c12 = b11.c1();
            za.k.d(c12, "classProto.typeTable");
            TypeTable typeTable = new TypeTable(c12);
            VersionRequirement.a aVar2 = VersionRequirement.f14699b;
            jc.w e12 = b11.e1();
            za.k.d(e12, "classProto.versionRequirementTable");
            a10 = kVar.a(packageFragmentDescriptor2, a13, typeTable, aVar2.a(e12), c10, null);
        }
        return new DeserializedClassDescriptor(a10, b11, a13, c10, d10);
    }

    public static /* synthetic */ ClassDescriptor e(ClassDeserializer classDeserializer, ClassId classId, ClassData classData, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            classData = null;
        }
        return classDeserializer.d(classId, classData);
    }

    public final ClassDescriptor d(ClassId classId, ClassData classData) {
        za.k.e(classId, "classId");
        return this.f5235b.invoke(new a(classId, classData));
    }
}
