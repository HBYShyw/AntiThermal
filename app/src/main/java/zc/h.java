package zc;

import java.util.Collection;
import java.util.Set;
import kotlin.collections.s0;
import oc.Name;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import za.Lambda;

/* compiled from: MemberScope.kt */
/* loaded from: classes2.dex */
public interface h extends ResolutionScope {

    /* renamed from: a, reason: collision with root package name */
    public static final a f20461a = a.f20462a;

    /* compiled from: MemberScope.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f20462a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final ya.l<Name, Boolean> f20463b = C0121a.f20464e;

        /* compiled from: MemberScope.kt */
        /* renamed from: zc.h$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0121a extends Lambda implements ya.l<Name, Boolean> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0121a f20464e = new C0121a();

            C0121a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Boolean invoke(Name name) {
                za.k.e(name, "it");
                return Boolean.TRUE;
            }
        }

        private a() {
        }

        public final ya.l<Name, Boolean> a() {
            return f20463b;
        }
    }

    /* compiled from: MemberScope.kt */
    /* loaded from: classes2.dex */
    public static final class b extends MemberScopeImpl {

        /* renamed from: b, reason: collision with root package name */
        public static final b f20465b = new b();

        private b() {
        }

        @Override // zc.MemberScopeImpl, zc.h
        public Set<Name> b() {
            Set<Name> e10;
            e10 = s0.e();
            return e10;
        }

        @Override // zc.MemberScopeImpl, zc.h
        public Set<Name> d() {
            Set<Name> e10;
            e10 = s0.e();
            return e10;
        }

        @Override // zc.MemberScopeImpl, zc.h
        public Set<Name> f() {
            Set<Name> e10;
            e10 = s0.e();
            return e10;
        }
    }

    Collection<? extends PropertyDescriptor> a(Name name, xb.b bVar);

    Set<Name> b();

    Collection<? extends SimpleFunctionDescriptor> c(Name name, xb.b bVar);

    Set<Name> d();

    Set<Name> f();
}
