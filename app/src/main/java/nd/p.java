package nd;

import ad.ReceiverValue;
import gd.g0;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import mb.KotlinBuiltIns;
import nd.k;
import nd.r;
import nd.t;
import oc.ClassId;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.TypeAliasDescriptor;
import pb.ValueParameterDescriptor;
import pb.findClassInModule;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
public final class p extends nd.b {

    /* renamed from: a, reason: collision with root package name */
    public static final p f16041a = new p();

    /* renamed from: b, reason: collision with root package name */
    private static final List<h> f16042b;

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<FunctionDescriptor, String> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f16043e = new a();

        a() {
            super(1);
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0027, code lost:
        
            if ((!wc.c.c(r2) && r2.q0() == null) == true) goto L13;
         */
        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final String invoke(FunctionDescriptor functionDescriptor) {
            Object g02;
            za.k.e(functionDescriptor, "$this$$receiver");
            List<ValueParameterDescriptor> l10 = functionDescriptor.l();
            za.k.d(l10, "valueParameters");
            g02 = _Collections.g0(l10);
            ValueParameterDescriptor valueParameterDescriptor = (ValueParameterDescriptor) g02;
            boolean z10 = true;
            if (valueParameterDescriptor != null) {
            }
            z10 = false;
            p pVar = p.f16041a;
            if (z10) {
                return null;
            }
            return "last parameter should not have a default value or be a vararg";
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<FunctionDescriptor, String> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f16044e = new b();

        b() {
            super(1);
        }

        private static final boolean b(DeclarationDescriptor declarationDescriptor) {
            return (declarationDescriptor instanceof ClassDescriptor) && KotlinBuiltIns.a0((ClassDescriptor) declarationDescriptor);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke(FunctionDescriptor functionDescriptor) {
            boolean z10;
            za.k.e(functionDescriptor, "$this$$receiver");
            p pVar = p.f16041a;
            DeclarationDescriptor b10 = functionDescriptor.b();
            za.k.d(b10, "containingDeclaration");
            boolean z11 = true;
            if (!b(b10)) {
                Collection<? extends FunctionDescriptor> e10 = functionDescriptor.e();
                za.k.d(e10, "overriddenDescriptors");
                if (!e10.isEmpty()) {
                    Iterator<T> it = e10.iterator();
                    while (it.hasNext()) {
                        DeclarationDescriptor b11 = ((FunctionDescriptor) it.next()).b();
                        za.k.d(b11, "it.containingDeclaration");
                        if (b(b11)) {
                            z10 = true;
                            break;
                        }
                    }
                }
                z10 = false;
                if (!z10) {
                    z11 = false;
                }
            }
            if (z11) {
                return null;
            }
            return "must override ''equals()'' in Any";
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.l<FunctionDescriptor, String> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f16045e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke(FunctionDescriptor functionDescriptor) {
            boolean z10;
            za.k.e(functionDescriptor, "$this$$receiver");
            ReceiverParameterDescriptor m02 = functionDescriptor.m0();
            if (m02 == null) {
                m02 = functionDescriptor.r0();
            }
            p pVar = p.f16041a;
            boolean z11 = false;
            if (m02 != null) {
                g0 f10 = functionDescriptor.f();
                if (f10 != null) {
                    g0 type = m02.getType();
                    za.k.d(type, "receiver.type");
                    z10 = ld.a.p(f10, type);
                } else {
                    z10 = false;
                }
                if (z10 || pVar.d(functionDescriptor, m02)) {
                    z11 = true;
                }
            }
            if (z11) {
                return null;
            }
            return "receiver must be a supertype of the return type";
        }
    }

    static {
        List m10;
        List<h> m11;
        Name name = OperatorNameConventions.f16056k;
        k.b bVar = k.b.f16033b;
        f[] fVarArr = {bVar, new t.a(1)};
        Name name2 = OperatorNameConventions.f16057l;
        f[] fVarArr2 = {bVar, new t.a(2)};
        Name name3 = OperatorNameConventions.f16047b;
        m mVar = m.f16035a;
        j jVar = j.f16029a;
        Name name4 = OperatorNameConventions.f16053h;
        t.d dVar = t.d.f16088b;
        r.a aVar = r.a.f16075d;
        Name name5 = OperatorNameConventions.f16055j;
        t.c cVar = t.c.f16087b;
        m10 = kotlin.collections.r.m(OperatorNameConventions.f16069x, OperatorNameConventions.f16070y);
        m11 = kotlin.collections.r.m(new h(name, fVarArr, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(name2, fVarArr2, a.f16043e), new h(name3, new f[]{bVar, mVar, new t.a(2), jVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.f16048c, new f[]{bVar, mVar, new t.a(3), jVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.f16049d, new f[]{bVar, mVar, new t.b(2), jVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.f16054i, new f[]{bVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(name4, new f[]{bVar, dVar, mVar, aVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(name5, new f[]{bVar, cVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.f16058m, new f[]{bVar, cVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.f16059n, new f[]{bVar, cVar, aVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.I, new f[]{bVar, dVar, mVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.J, new f[]{bVar, dVar, mVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.f16050e, new f[]{k.a.f16032b}, b.f16044e), new h(OperatorNameConventions.f16052g, new f[]{bVar, r.b.f16077d, dVar, mVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.S, new f[]{bVar, dVar, mVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.R, new f[]{bVar, cVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(m10, new f[]{bVar}, c.f16045e), new h(OperatorNameConventions.T, new f[]{bVar, r.c.f16079d, dVar, mVar}, (ya.l) null, 4, (DefaultConstructorMarker) null), new h(OperatorNameConventions.f16061p, new f[]{bVar, cVar}, (ya.l) null, 4, (DefaultConstructorMarker) null));
        f16042b = m11;
    }

    private p() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean d(FunctionDescriptor functionDescriptor, ReceiverParameterDescriptor receiverParameterDescriptor) {
        ClassId k10;
        g0 f10;
        ReceiverValue value = receiverParameterDescriptor.getValue();
        za.k.d(value, "receiver.value");
        if (!(value instanceof ad.e)) {
            return false;
        }
        ClassDescriptor w10 = ((ad.e) value).w();
        if (!w10.U() || (k10 = wc.c.k(w10)) == null) {
            return false;
        }
        ClassifierDescriptor b10 = findClassInModule.b(wc.c.p(w10), k10);
        if (!(b10 instanceof TypeAliasDescriptor)) {
            b10 = null;
        }
        TypeAliasDescriptor typeAliasDescriptor = (TypeAliasDescriptor) b10;
        if (typeAliasDescriptor == null || (f10 = functionDescriptor.f()) == null) {
            return false;
        }
        return ld.a.p(f10, typeAliasDescriptor.e0());
    }

    @Override // nd.b
    public List<h> b() {
        return f16042b;
    }
}
