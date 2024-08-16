package cd;

import jc.c;
import lc.Flags;
import lc.NameResolver;
import lc.TypeTable;
import oc.ClassId;
import oc.FqName;
import pb.SourceElement;
import za.DefaultConstructorMarker;

/* compiled from: ProtoContainer.kt */
/* renamed from: cd.z, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class ProtoContainer {

    /* renamed from: a, reason: collision with root package name */
    private final NameResolver f5325a;

    /* renamed from: b, reason: collision with root package name */
    private final TypeTable f5326b;

    /* renamed from: c, reason: collision with root package name */
    private final SourceElement f5327c;

    /* compiled from: ProtoContainer.kt */
    /* renamed from: cd.z$a */
    /* loaded from: classes2.dex */
    public static final class a extends ProtoContainer {

        /* renamed from: d, reason: collision with root package name */
        private final jc.c f5328d;

        /* renamed from: e, reason: collision with root package name */
        private final a f5329e;

        /* renamed from: f, reason: collision with root package name */
        private final ClassId f5330f;

        /* renamed from: g, reason: collision with root package name */
        private final c.EnumC0065c f5331g;

        /* renamed from: h, reason: collision with root package name */
        private final boolean f5332h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(jc.c cVar, NameResolver nameResolver, TypeTable typeTable, SourceElement sourceElement, a aVar) {
            super(nameResolver, typeTable, sourceElement, null);
            za.k.e(cVar, "classProto");
            za.k.e(nameResolver, "nameResolver");
            za.k.e(typeTable, "typeTable");
            this.f5328d = cVar;
            this.f5329e = aVar;
            this.f5330f = NameResolverUtil.a(nameResolver, cVar.z0());
            c.EnumC0065c d10 = Flags.f14671f.d(cVar.y0());
            this.f5331g = d10 == null ? c.EnumC0065c.CLASS : d10;
            Boolean d11 = Flags.f14672g.d(cVar.y0());
            za.k.d(d11, "IS_INNER.get(classProto.flags)");
            this.f5332h = d11.booleanValue();
        }

        @Override // cd.ProtoContainer
        public FqName a() {
            FqName b10 = this.f5330f.b();
            za.k.d(b10, "classId.asSingleFqName()");
            return b10;
        }

        public final ClassId e() {
            return this.f5330f;
        }

        public final jc.c f() {
            return this.f5328d;
        }

        public final c.EnumC0065c g() {
            return this.f5331g;
        }

        public final a h() {
            return this.f5329e;
        }

        public final boolean i() {
            return this.f5332h;
        }
    }

    /* compiled from: ProtoContainer.kt */
    /* renamed from: cd.z$b */
    /* loaded from: classes2.dex */
    public static final class b extends ProtoContainer {

        /* renamed from: d, reason: collision with root package name */
        private final FqName f5333d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(FqName fqName, NameResolver nameResolver, TypeTable typeTable, SourceElement sourceElement) {
            super(nameResolver, typeTable, sourceElement, null);
            za.k.e(fqName, "fqName");
            za.k.e(nameResolver, "nameResolver");
            za.k.e(typeTable, "typeTable");
            this.f5333d = fqName;
        }

        @Override // cd.ProtoContainer
        public FqName a() {
            return this.f5333d;
        }
    }

    private ProtoContainer(NameResolver nameResolver, TypeTable typeTable, SourceElement sourceElement) {
        this.f5325a = nameResolver;
        this.f5326b = typeTable;
        this.f5327c = sourceElement;
    }

    public /* synthetic */ ProtoContainer(NameResolver nameResolver, TypeTable typeTable, SourceElement sourceElement, DefaultConstructorMarker defaultConstructorMarker) {
        this(nameResolver, typeTable, sourceElement);
    }

    public abstract FqName a();

    public final NameResolver b() {
        return this.f5325a;
    }

    public final SourceElement c() {
        return this.f5327c;
    }

    public final TypeTable d() {
        return this.f5326b;
    }

    public String toString() {
        return getClass().getSimpleName() + ": " + a();
    }
}
