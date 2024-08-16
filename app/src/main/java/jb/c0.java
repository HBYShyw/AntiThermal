package jb;

import gd.s1;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import jb.b0;
import jb.i;
import jb.j;
import kb.CallerImpl;
import kb.InternalUnderlyingValOfInlineClass;
import kb.ThrowingCaller;
import ma.NoWhenBranchMatchedException;
import mc.JvmProtoBuf;
import nc.JvmProtoBufUtil;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.PropertyDescriptor;
import sc.inlineClassesUtils;

/* compiled from: KPropertyImpl.kt */
/* loaded from: classes2.dex */
public final class c0 {
    /* JADX WARN: Removed duplicated region for block: B:14:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0118  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final kb.e<?> b(b0.a<?, ?> aVar, boolean z10) {
        i.e c10;
        Method c11;
        kb.e aVar2;
        JvmProtoBuf.c x10;
        Method F;
        kb.e cVar;
        Method f10;
        if (KDeclarationContainerImpl.f13292e.a().b(aVar.s().R())) {
            return ThrowingCaller.f14278a;
        }
        j f11 = l0.f13288a.f(aVar.s().L());
        if (f11 instanceof j.c) {
            j.c cVar2 = (j.c) f11;
            JvmProtoBuf.d f12 = cVar2.f();
            if (z10) {
                if (f12.B()) {
                    x10 = f12.w();
                    F = x10 == null ? aVar.s().G().F(cVar2.d().getString(x10.s()), cVar2.d().getString(x10.r())) : null;
                    if (F != null) {
                        if (inlineClassesUtils.d(aVar.s().L()) && za.k.a(aVar.s().L().g(), DescriptorVisibilities.f16732d)) {
                            Class<?> i10 = kb.i.i(aVar.s().L().b());
                            if (i10 != null && (f10 = kb.i.f(i10, aVar.s().L())) != null) {
                                aVar2 = aVar.K() ? new InternalUnderlyingValOfInlineClass.a(f10, f(aVar)) : new InternalUnderlyingValOfInlineClass.b(f10);
                            } else {
                                throw new KotlinReflectionInternalError("Underlying property of inline class " + aVar.s() + " should have a field");
                            }
                        } else {
                            Field Q = aVar.s().Q();
                            if (Q != null) {
                                aVar2 = c(aVar, z10, Q);
                            } else {
                                throw new KotlinReflectionInternalError("No accessors or field is found for property " + aVar.s());
                            }
                        }
                    } else {
                        if (!Modifier.isStatic(F.getModifiers())) {
                            cVar = aVar.K() ? new CallerImpl.h.a(F, f(aVar)) : new CallerImpl.h.d(F);
                        } else if (d(aVar)) {
                            cVar = aVar.K() ? new CallerImpl.h.b(F) : new CallerImpl.h.e(F);
                        } else {
                            cVar = aVar.K() ? new CallerImpl.h.c(F, f(aVar)) : new CallerImpl.h.f(F);
                        }
                        aVar2 = cVar;
                    }
                }
                x10 = null;
                if (x10 == null) {
                }
                if (F != null) {
                }
            } else {
                if (f12.C()) {
                    x10 = f12.x();
                    if (x10 == null) {
                    }
                    if (F != null) {
                    }
                }
                x10 = null;
                if (x10 == null) {
                }
                if (F != null) {
                }
            }
        } else if (f11 instanceof j.a) {
            aVar2 = c(aVar, z10, ((j.a) f11).b());
        } else if (f11 instanceof j.b) {
            if (z10) {
                c11 = ((j.b) f11).b();
            } else {
                j.b bVar = (j.b) f11;
                c11 = bVar.c();
                if (c11 == null) {
                    throw new KotlinReflectionInternalError("No source found for setter of Java method property: " + bVar.b());
                }
            }
            aVar2 = aVar.K() ? new CallerImpl.h.a(c11, f(aVar)) : new CallerImpl.h.d(c11);
        } else {
            if (f11 instanceof j.d) {
                if (z10) {
                    c10 = ((j.d) f11).b();
                } else {
                    c10 = ((j.d) f11).c();
                    if (c10 == null) {
                        throw new KotlinReflectionInternalError("No setter found for property " + aVar.s());
                    }
                }
                Method F2 = aVar.s().G().F(c10.c(), c10.b());
                if (F2 != null) {
                    Modifier.isStatic(F2.getModifiers());
                    return aVar.K() ? new CallerImpl.h.a(F2, f(aVar)) : new CallerImpl.h.d(F2);
                }
                throw new KotlinReflectionInternalError("No accessor found for property " + aVar.s());
            }
            throw new NoWhenBranchMatchedException();
        }
        return kb.i.c(aVar2, aVar.L(), false, 2, null);
    }

    private static final CallerImpl<Field> c(b0.a<?, ?> aVar, boolean z10, Field field) {
        CallerImpl<Field> aVar2;
        if (g(aVar.s().L()) || !Modifier.isStatic(field.getModifiers())) {
            if (z10) {
                if (!aVar.K()) {
                    return new CallerImpl.f.c(field);
                }
                aVar2 = new CallerImpl.f.a(field, f(aVar));
            } else {
                aVar2 = aVar.K() ? new CallerImpl.g.a(field, e(aVar), f(aVar)) : new CallerImpl.g.c(field, e(aVar));
            }
        } else if (d(aVar)) {
            if (z10) {
                return aVar.K() ? new CallerImpl.f.b(field) : new CallerImpl.f.d(field);
            }
            aVar2 = aVar.K() ? new CallerImpl.g.b(field, e(aVar)) : new CallerImpl.g.d(field, e(aVar));
        } else {
            if (z10) {
                return new CallerImpl.f.e(field);
            }
            aVar2 = new CallerImpl.g.e(field, e(aVar));
        }
        return aVar2;
    }

    private static final boolean d(b0.a<?, ?> aVar) {
        return aVar.s().L().i().a(o0.j());
    }

    private static final boolean e(b0.a<?, ?> aVar) {
        return !s1.l(aVar.s().L().getType());
    }

    public static final Object f(b0.a<?, ?> aVar) {
        za.k.e(aVar, "<this>");
        return aVar.s().M();
    }

    private static final boolean g(PropertyDescriptor propertyDescriptor) {
        DeclarationDescriptor b10 = propertyDescriptor.b();
        za.k.d(b10, "containingDeclaration");
        if (!sc.e.x(b10)) {
            return false;
        }
        DeclarationDescriptor b11 = b10.b();
        return !(sc.e.C(b11) || sc.e.t(b11)) || ((propertyDescriptor instanceof ed.j) && JvmProtoBufUtil.f(((ed.j) propertyDescriptor).M()));
    }
}
