package uc;

import gd.g0;
import gd.o0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import oc.ClassId;
import oc.Name;
import pb.ClassDescriptor;
import pb.ModuleDescriptor;
import pb.findClassInModule;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class j extends g<ma.o<? extends ClassId, ? extends Name>> {

    /* renamed from: b, reason: collision with root package name */
    private final ClassId f18994b;

    /* renamed from: c, reason: collision with root package name */
    private final Name f18995c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public j(ClassId classId, Name name) {
        super(ma.u.a(classId, name));
        za.k.e(classId, "enumClassId");
        za.k.e(name, "enumEntryName");
        this.f18994b = classId;
        this.f18995c = name;
    }

    @Override // uc.g
    public g0 a(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        ClassDescriptor a10 = findClassInModule.a(moduleDescriptor, this.f18994b);
        o0 o0Var = null;
        if (a10 != null) {
            if (!sc.e.A(a10)) {
                a10 = null;
            }
            if (a10 != null) {
                o0Var = a10.x();
            }
        }
        if (o0Var != null) {
            return o0Var;
        }
        ErrorTypeKind errorTypeKind = ErrorTypeKind.C0;
        String classId = this.f18994b.toString();
        za.k.d(classId, "enumClassId.toString()");
        String name = this.f18995c.toString();
        za.k.d(name, "enumEntryName.toString()");
        return ErrorUtils.d(errorTypeKind, classId, name);
    }

    public final Name c() {
        return this.f18995c;
    }

    @Override // uc.g
    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f18994b.j());
        sb2.append('.');
        sb2.append(this.f18995c);
        return sb2.toString();
    }
}
