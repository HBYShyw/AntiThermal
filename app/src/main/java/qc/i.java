package qc;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import qc.a;
import qc.h;
import qc.j;
import qc.q;
import qc.z;

/* compiled from: GeneratedMessageLite.java */
/* loaded from: classes2.dex */
public abstract class i extends qc.a implements Serializable {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GeneratedMessageLite.java */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f17295a;

        static {
            int[] iArr = new int[z.c.values().length];
            f17295a = iArr;
            try {
                iArr[z.c.MESSAGE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f17295a[z.c.ENUM.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* compiled from: GeneratedMessageLite.java */
    /* loaded from: classes2.dex */
    public static abstract class b<MessageType extends i, BuilderType extends b> extends a.AbstractC0092a<BuilderType> {

        /* renamed from: e, reason: collision with root package name */
        private qc.d f17296e = qc.d.f17259e;

        @Override // 
        public BuilderType d() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        public final qc.d e() {
            return this.f17296e;
        }

        public abstract BuilderType f(MessageType messagetype);

        public final BuilderType g(qc.d dVar) {
            this.f17296e = dVar;
            return this;
        }
    }

    /* compiled from: GeneratedMessageLite.java */
    /* loaded from: classes2.dex */
    public static abstract class c<MessageType extends d<MessageType>, BuilderType extends c<MessageType, BuilderType>> extends b<MessageType, BuilderType> implements r {

        /* renamed from: f, reason: collision with root package name */
        private h<e> f17297f = h.g();

        /* renamed from: g, reason: collision with root package name */
        private boolean f17298g;

        /* JADX INFO: Access modifiers changed from: private */
        public h<e> i() {
            this.f17297f.q();
            this.f17298g = false;
            return this.f17297f;
        }

        private void j() {
            if (this.f17298g) {
                return;
            }
            this.f17297f = this.f17297f.clone();
            this.f17298g = true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void k(MessageType messagetype) {
            j();
            this.f17297f.r(((d) messagetype).f17299f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GeneratedMessageLite.java */
    /* loaded from: classes2.dex */
    public static final class e implements h.b<e> {

        /* renamed from: e, reason: collision with root package name */
        final j.b<?> f17304e;

        /* renamed from: f, reason: collision with root package name */
        final int f17305f;

        /* renamed from: g, reason: collision with root package name */
        final z.b f17306g;

        /* renamed from: h, reason: collision with root package name */
        final boolean f17307h;

        /* renamed from: i, reason: collision with root package name */
        final boolean f17308i;

        e(j.b<?> bVar, int i10, z.b bVar2, boolean z10, boolean z11) {
            this.f17304e = bVar;
            this.f17305f = i10;
            this.f17306g = bVar2;
            this.f17307h = z10;
            this.f17308i = z11;
        }

        @Override // java.lang.Comparable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compareTo(e eVar) {
            return this.f17305f - eVar.f17305f;
        }

        public j.b<?> b() {
            return this.f17304e;
        }

        @Override // qc.h.b
        public z.c getLiteJavaType() {
            return this.f17306g.a();
        }

        @Override // qc.h.b
        public z.b getLiteType() {
            return this.f17306g;
        }

        @Override // qc.h.b
        public int getNumber() {
            return this.f17305f;
        }

        @Override // qc.h.b
        public boolean isPacked() {
            return this.f17308i;
        }

        @Override // qc.h.b
        public boolean isRepeated() {
            return this.f17307h;
        }

        @Override // qc.h.b
        public q.a k(q.a aVar, q qVar) {
            return ((b) aVar).f((i) qVar);
        }
    }

    /* compiled from: GeneratedMessageLite.java */
    /* loaded from: classes2.dex */
    public static class f<ContainingType extends q, Type> {

        /* renamed from: a, reason: collision with root package name */
        final ContainingType f17309a;

        /* renamed from: b, reason: collision with root package name */
        final Type f17310b;

        /* renamed from: c, reason: collision with root package name */
        final q f17311c;

        /* renamed from: d, reason: collision with root package name */
        final e f17312d;

        /* renamed from: e, reason: collision with root package name */
        final Class f17313e;

        /* renamed from: f, reason: collision with root package name */
        final Method f17314f;

        f(ContainingType containingtype, Type type, q qVar, e eVar, Class cls) {
            if (containingtype != null) {
                if (eVar.getLiteType() == z.b.f17389q && qVar == null) {
                    throw new IllegalArgumentException("Null messageDefaultInstance");
                }
                this.f17309a = containingtype;
                this.f17310b = type;
                this.f17311c = qVar;
                this.f17312d = eVar;
                this.f17313e = cls;
                if (j.a.class.isAssignableFrom(cls)) {
                    this.f17314f = i.e(cls, "valueOf", Integer.TYPE);
                    return;
                } else {
                    this.f17314f = null;
                    return;
                }
            }
            throw new IllegalArgumentException("Null containingTypeDefaultInstance");
        }

        Object a(Object obj) {
            if (this.f17312d.isRepeated()) {
                if (this.f17312d.getLiteJavaType() != z.c.ENUM) {
                    return obj;
                }
                ArrayList arrayList = new ArrayList();
                Iterator it = ((List) obj).iterator();
                while (it.hasNext()) {
                    arrayList.add(e(it.next()));
                }
                return arrayList;
            }
            return e(obj);
        }

        public ContainingType b() {
            return this.f17309a;
        }

        public q c() {
            return this.f17311c;
        }

        public int d() {
            return this.f17312d.getNumber();
        }

        Object e(Object obj) {
            return this.f17312d.getLiteJavaType() == z.c.ENUM ? i.f(this.f17314f, null, (Integer) obj) : obj;
        }

        Object f(Object obj) {
            return this.f17312d.getLiteJavaType() == z.c.ENUM ? Integer.valueOf(((j.a) obj).getNumber()) : obj;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public i() {
    }

    static Method e(Class cls, String str, Class... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException e10) {
            String name = cls.getName();
            String valueOf = String.valueOf(str);
            StringBuilder sb2 = new StringBuilder(name.length() + 45 + valueOf.length());
            sb2.append("Generated message class \"");
            sb2.append(name);
            sb2.append("\" missing method \"");
            sb2.append(valueOf);
            sb2.append("\".");
            throw new RuntimeException(sb2.toString(), e10);
        }
    }

    static Object f(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e10) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e10);
        } catch (InvocationTargetException e11) {
            Throwable cause = e11.getCause();
            if (!(cause instanceof RuntimeException)) {
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
            }
            throw ((RuntimeException) cause);
        }
    }

    public static <ContainingType extends q, Type> f<ContainingType, Type> h(ContainingType containingtype, q qVar, j.b<?> bVar, int i10, z.b bVar2, boolean z10, Class cls) {
        return new f<>(containingtype, Collections.emptyList(), qVar, new e(bVar, i10, bVar2, true, z10), cls);
    }

    public static <ContainingType extends q, Type> f<ContainingType, Type> i(ContainingType containingtype, Type type, q qVar, j.b<?> bVar, int i10, z.b bVar2, Class cls) {
        return new f<>(containingtype, type, qVar, new e(bVar, i10, bVar2, false, false), cls);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:5:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0045  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static <MessageType extends q> boolean k(h<e> hVar, MessageType messagetype, qc.e eVar, qc.f fVar, g gVar, int i10) {
        boolean z10;
        boolean z11;
        Object build;
        q qVar;
        int b10 = z.b(i10);
        f b11 = gVar.b(messagetype, z.a(i10));
        if (b11 != null) {
            if (b10 == h.l(b11.f17312d.getLiteType(), false)) {
                z10 = false;
                z11 = false;
            } else {
                e eVar2 = b11.f17312d;
                if (eVar2.f17307h && eVar2.f17306g.c() && b10 == h.l(b11.f17312d.getLiteType(), true)) {
                    z10 = false;
                    z11 = true;
                }
            }
            if (!z10) {
                return eVar.P(i10, fVar);
            }
            if (z11) {
                int j10 = eVar.j(eVar.A());
                if (b11.f17312d.getLiteType() == z.b.f17392t) {
                    while (eVar.e() > 0) {
                        Object findValueByNumber = b11.f17312d.b().findValueByNumber(eVar.n());
                        if (findValueByNumber == null) {
                            return true;
                        }
                        hVar.a(b11.f17312d, b11.f(findValueByNumber));
                    }
                } else {
                    while (eVar.e() > 0) {
                        hVar.a(b11.f17312d, h.u(eVar, b11.f17312d.getLiteType(), false));
                    }
                }
                eVar.i(j10);
            } else {
                int i11 = a.f17295a[b11.f17312d.getLiteJavaType().ordinal()];
                if (i11 == 1) {
                    q.a aVar = null;
                    if (!b11.f17312d.isRepeated() && (qVar = (q) hVar.h(b11.f17312d)) != null) {
                        aVar = qVar.toBuilder();
                    }
                    if (aVar == null) {
                        aVar = b11.c().newBuilderForType();
                    }
                    if (b11.f17312d.getLiteType() == z.b.f17388p) {
                        eVar.r(b11.d(), aVar, gVar);
                    } else {
                        eVar.v(aVar, gVar);
                    }
                    build = aVar.build();
                } else if (i11 != 2) {
                    build = h.u(eVar, b11.f17312d.getLiteType(), false);
                } else {
                    int n10 = eVar.n();
                    Object findValueByNumber2 = b11.f17312d.b().findValueByNumber(n10);
                    if (findValueByNumber2 == null) {
                        fVar.o0(i10);
                        fVar.y0(n10);
                        return true;
                    }
                    build = findValueByNumber2;
                }
                if (b11.f17312d.isRepeated()) {
                    hVar.a(b11.f17312d, b11.f(build));
                } else {
                    hVar.v(b11.f17312d, b11.f(build));
                }
            }
            return true;
        }
        z11 = false;
        z10 = true;
        if (!z10) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void g() {
    }

    @Override // qc.q
    public s<? extends q> getParserForType() {
        throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean j(qc.e eVar, qc.f fVar, g gVar, int i10) {
        return eVar.P(i10, fVar);
    }

    /* compiled from: GeneratedMessageLite.java */
    /* loaded from: classes2.dex */
    public static abstract class d<MessageType extends d<MessageType>> extends i implements r {

        /* renamed from: f, reason: collision with root package name */
        private final h<e> f17299f;

        /* JADX INFO: Access modifiers changed from: protected */
        /* compiled from: GeneratedMessageLite.java */
        /* loaded from: classes2.dex */
        public class a {

            /* renamed from: a, reason: collision with root package name */
            private final Iterator<Map.Entry<e, Object>> f17300a;

            /* renamed from: b, reason: collision with root package name */
            private Map.Entry<e, Object> f17301b;

            /* renamed from: c, reason: collision with root package name */
            private final boolean f17302c;

            /* synthetic */ a(d dVar, boolean z10, a aVar) {
                this(z10);
            }

            public void a(int i10, qc.f fVar) {
                while (true) {
                    Map.Entry<e, Object> entry = this.f17301b;
                    if (entry == null || entry.getKey().getNumber() >= i10) {
                        return;
                    }
                    e key = this.f17301b.getKey();
                    if (this.f17302c && key.getLiteJavaType() == z.c.MESSAGE && !key.isRepeated()) {
                        fVar.f0(key.getNumber(), (q) this.f17301b.getValue());
                    } else {
                        h.z(key, this.f17301b.getValue(), fVar);
                    }
                    if (this.f17300a.hasNext()) {
                        this.f17301b = this.f17300a.next();
                    } else {
                        this.f17301b = null;
                    }
                }
            }

            private a(boolean z10) {
                Iterator<Map.Entry<e, Object>> p10 = d.this.f17299f.p();
                this.f17300a = p10;
                if (p10.hasNext()) {
                    this.f17301b = p10.next();
                }
                this.f17302c = z10;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public d() {
            this.f17299f = h.t();
        }

        private void u(f<MessageType, ?> fVar) {
            if (fVar.b() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // qc.i
        public void g() {
            this.f17299f.q();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // qc.i
        public boolean j(qc.e eVar, qc.f fVar, g gVar, int i10) {
            return i.k(this.f17299f, getDefaultInstanceForType(), eVar, fVar, gVar, i10);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public boolean n() {
            return this.f17299f.n();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public int o() {
            return this.f17299f.k();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final <Type> Type p(f<MessageType, Type> fVar) {
            u(fVar);
            Object h10 = this.f17299f.h(fVar.f17312d);
            if (h10 == null) {
                return fVar.f17310b;
            }
            return (Type) fVar.a(h10);
        }

        public final <Type> Type q(f<MessageType, List<Type>> fVar, int i10) {
            u(fVar);
            return (Type) fVar.e(this.f17299f.i(fVar.f17312d, i10));
        }

        public final <Type> int r(f<MessageType, List<Type>> fVar) {
            u(fVar);
            return this.f17299f.j(fVar.f17312d);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final <Type> boolean s(f<MessageType, Type> fVar) {
            u(fVar);
            return this.f17299f.m(fVar.f17312d);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public d<MessageType>.a t() {
            return new a(this, false, null);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public d(c<MessageType, ?> cVar) {
            this.f17299f = cVar.i();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public i(b bVar) {
    }
}
