package yb;

import java.util.Collection;
import kotlin.collections._Collections;
import mb.KotlinBuiltIns;
import oc.Name;
import pb.CallableMemberDescriptor;
import za.Lambda;

/* compiled from: ClassicBuiltinSpecialProperties.kt */
/* renamed from: yb.i, reason: use source file name */
/* loaded from: classes2.dex */
public final class ClassicBuiltinSpecialProperties {

    /* renamed from: a, reason: collision with root package name */
    public static final ClassicBuiltinSpecialProperties f20089a = new ClassicBuiltinSpecialProperties();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClassicBuiltinSpecialProperties.kt */
    /* renamed from: yb.i$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f20090e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            return Boolean.valueOf(ClassicBuiltinSpecialProperties.f20089a.b(callableMemberDescriptor));
        }
    }

    private ClassicBuiltinSpecialProperties() {
    }

    private final boolean c(CallableMemberDescriptor callableMemberDescriptor) {
        boolean L;
        L = _Collections.L(g.f20075a.c(), wc.c.h(callableMemberDescriptor));
        if (L && callableMemberDescriptor.l().isEmpty()) {
            return true;
        }
        if (!KotlinBuiltIns.f0(callableMemberDescriptor)) {
            return false;
        }
        Collection<? extends CallableMemberDescriptor> e10 = callableMemberDescriptor.e();
        za.k.d(e10, "overriddenDescriptors");
        if (!e10.isEmpty()) {
            for (CallableMemberDescriptor callableMemberDescriptor2 : e10) {
                ClassicBuiltinSpecialProperties classicBuiltinSpecialProperties = f20089a;
                za.k.d(callableMemberDescriptor2, "it");
                if (classicBuiltinSpecialProperties.b(callableMemberDescriptor2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public final String a(CallableMemberDescriptor callableMemberDescriptor) {
        Name name;
        za.k.e(callableMemberDescriptor, "<this>");
        KotlinBuiltIns.f0(callableMemberDescriptor);
        CallableMemberDescriptor f10 = wc.c.f(wc.c.s(callableMemberDescriptor), false, a.f20090e, 1, null);
        if (f10 == null || (name = g.f20075a.a().get(wc.c.l(f10))) == null) {
            return null;
        }
        return name.b();
    }

    public final boolean b(CallableMemberDescriptor callableMemberDescriptor) {
        za.k.e(callableMemberDescriptor, "callableMemberDescriptor");
        if (g.f20075a.d().contains(callableMemberDescriptor.getName())) {
            return c(callableMemberDescriptor);
        }
        return false;
    }
}
