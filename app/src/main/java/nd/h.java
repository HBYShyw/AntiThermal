package nd;

import java.util.Arrays;
import java.util.Collection;
import nd.g;
import oc.Name;
import pb.FunctionDescriptor;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
public final class h {

    /* renamed from: a, reason: collision with root package name */
    private final Name f16020a;

    /* renamed from: b, reason: collision with root package name */
    private final sd.j f16021b;

    /* renamed from: c, reason: collision with root package name */
    private final Collection<Name> f16022c;

    /* renamed from: d, reason: collision with root package name */
    private final ya.l<FunctionDescriptor, String> f16023d;

    /* renamed from: e, reason: collision with root package name */
    private final f[] f16024e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l {

        /* renamed from: e, reason: collision with root package name */
        public static final a f16025e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Void invoke(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "$this$null");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l {

        /* renamed from: e, reason: collision with root package name */
        public static final b f16026e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Void invoke(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "$this$null");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l {

        /* renamed from: e, reason: collision with root package name */
        public static final c f16027e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Void invoke(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "$this$null");
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private h(Name name, sd.j jVar, Collection<Name> collection, ya.l<? super FunctionDescriptor, String> lVar, f... fVarArr) {
        this.f16020a = name;
        this.f16021b = jVar;
        this.f16022c = collection;
        this.f16023d = lVar;
        this.f16024e = fVarArr;
    }

    public final g a(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "functionDescriptor");
        for (f fVar : this.f16024e) {
            String b10 = fVar.b(functionDescriptor);
            if (b10 != null) {
                return new g.b(b10);
            }
        }
        String invoke = this.f16023d.invoke(functionDescriptor);
        if (invoke != null) {
            return new g.b(invoke);
        }
        return g.c.f16019b;
    }

    public final boolean b(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "functionDescriptor");
        if (this.f16020a != null && !za.k.a(functionDescriptor.getName(), this.f16020a)) {
            return false;
        }
        if (this.f16021b != null) {
            String b10 = functionDescriptor.getName().b();
            za.k.d(b10, "functionDescriptor.name.asString()");
            if (!this.f16021b.b(b10)) {
                return false;
            }
        }
        Collection<Name> collection = this.f16022c;
        return collection == null || collection.contains(functionDescriptor.getName());
    }

    public /* synthetic */ h(Name name, f[] fVarArr, ya.l lVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(name, fVarArr, (ya.l<? super FunctionDescriptor, String>) ((i10 & 4) != 0 ? a.f16025e : lVar));
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public h(Name name, f[] fVarArr, ya.l<? super FunctionDescriptor, String> lVar) {
        this(name, (sd.j) null, (Collection<Name>) null, lVar, (f[]) Arrays.copyOf(fVarArr, fVarArr.length));
        za.k.e(name, "name");
        za.k.e(fVarArr, "checks");
        za.k.e(lVar, "additionalChecks");
    }

    public /* synthetic */ h(sd.j jVar, f[] fVarArr, ya.l lVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(jVar, fVarArr, (ya.l<? super FunctionDescriptor, String>) ((i10 & 4) != 0 ? b.f16026e : lVar));
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public h(sd.j jVar, f[] fVarArr, ya.l<? super FunctionDescriptor, String> lVar) {
        this((Name) null, jVar, (Collection<Name>) null, lVar, (f[]) Arrays.copyOf(fVarArr, fVarArr.length));
        za.k.e(jVar, "regex");
        za.k.e(fVarArr, "checks");
        za.k.e(lVar, "additionalChecks");
    }

    public /* synthetic */ h(Collection collection, f[] fVarArr, ya.l lVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((Collection<Name>) collection, fVarArr, (ya.l<? super FunctionDescriptor, String>) ((i10 & 4) != 0 ? c.f16027e : lVar));
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public h(Collection<Name> collection, f[] fVarArr, ya.l<? super FunctionDescriptor, String> lVar) {
        this((Name) null, (sd.j) null, collection, lVar, (f[]) Arrays.copyOf(fVarArr, fVarArr.length));
        za.k.e(collection, "nameList");
        za.k.e(fVarArr, "checks");
        za.k.e(lVar, "additionalChecks");
    }
}
