package zc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import kotlin.collections.r;
import za.DefaultConstructorMarker;

/* compiled from: MemberScope.kt */
/* loaded from: classes2.dex */
public final class d {

    /* renamed from: c, reason: collision with root package name */
    public static final a f20424c;

    /* renamed from: d, reason: collision with root package name */
    private static int f20425d;

    /* renamed from: e, reason: collision with root package name */
    private static final int f20426e;

    /* renamed from: f, reason: collision with root package name */
    private static final int f20427f;

    /* renamed from: g, reason: collision with root package name */
    private static final int f20428g;

    /* renamed from: h, reason: collision with root package name */
    private static final int f20429h;

    /* renamed from: i, reason: collision with root package name */
    private static final int f20430i;

    /* renamed from: j, reason: collision with root package name */
    private static final int f20431j;

    /* renamed from: k, reason: collision with root package name */
    private static final int f20432k;

    /* renamed from: l, reason: collision with root package name */
    private static final int f20433l;

    /* renamed from: m, reason: collision with root package name */
    private static final int f20434m;

    /* renamed from: n, reason: collision with root package name */
    private static final int f20435n;

    /* renamed from: o, reason: collision with root package name */
    public static final d f20436o;

    /* renamed from: p, reason: collision with root package name */
    public static final d f20437p;

    /* renamed from: q, reason: collision with root package name */
    public static final d f20438q;

    /* renamed from: r, reason: collision with root package name */
    public static final d f20439r;

    /* renamed from: s, reason: collision with root package name */
    public static final d f20440s;

    /* renamed from: t, reason: collision with root package name */
    public static final d f20441t;

    /* renamed from: u, reason: collision with root package name */
    public static final d f20442u;

    /* renamed from: v, reason: collision with root package name */
    public static final d f20443v;

    /* renamed from: w, reason: collision with root package name */
    public static final d f20444w;

    /* renamed from: x, reason: collision with root package name */
    public static final d f20445x;

    /* renamed from: y, reason: collision with root package name */
    private static final List<a.C0120a> f20446y;

    /* renamed from: z, reason: collision with root package name */
    private static final List<a.C0120a> f20447z;

    /* renamed from: a, reason: collision with root package name */
    private final List<c> f20448a;

    /* renamed from: b, reason: collision with root package name */
    private final int f20449b;

    /* compiled from: MemberScope.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: MemberScope.kt */
        /* renamed from: zc.d$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        private static final class C0120a {

            /* renamed from: a, reason: collision with root package name */
            private final int f20450a;

            /* renamed from: b, reason: collision with root package name */
            private final String f20451b;

            public C0120a(int i10, String str) {
                za.k.e(str, "name");
                this.f20450a = i10;
                this.f20451b = str;
            }

            public final int a() {
                return this.f20450a;
            }

            public final String b() {
                return this.f20451b;
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int j() {
            int i10 = d.f20425d;
            a aVar = d.f20424c;
            d.f20425d <<= 1;
            return i10;
        }

        public final int b() {
            return d.f20432k;
        }

        public final int c() {
            return d.f20433l;
        }

        public final int d() {
            return d.f20430i;
        }

        public final int e() {
            return d.f20426e;
        }

        public final int f() {
            return d.f20429h;
        }

        public final int g() {
            return d.f20427f;
        }

        public final int h() {
            return d.f20428g;
        }

        public final int i() {
            return d.f20431j;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static {
        a.C0120a c0120a;
        a.C0120a c0120a2;
        a aVar = new a(null);
        f20424c = aVar;
        f20425d = 1;
        int j10 = aVar.j();
        f20426e = j10;
        int j11 = aVar.j();
        f20427f = j11;
        int j12 = aVar.j();
        f20428g = j12;
        int j13 = aVar.j();
        f20429h = j13;
        int j14 = aVar.j();
        f20430i = j14;
        int j15 = aVar.j();
        f20431j = j15;
        int j16 = aVar.j() - 1;
        f20432k = j16;
        int i10 = j10 | j11 | j12;
        f20433l = i10;
        int i11 = j11 | j14 | j15;
        f20434m = i11;
        int i12 = j14 | j15;
        f20435n = i12;
        int i13 = 2;
        f20436o = new d(j16, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20437p = new d(i12, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20438q = new d(j10, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20439r = new d(j11, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20440s = new d(j12, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20441t = new d(i10, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20442u = new d(j13, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20443v = new d(j14, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20444w = new d(j15, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        f20445x = new d(i11, 0 == true ? 1 : 0, i13, 0 == true ? 1 : 0);
        Field[] fields = d.class.getFields();
        za.k.d(fields, "T::class.java.fields");
        ArrayList<Field> arrayList = new ArrayList();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                arrayList.add(field);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (Field field2 : arrayList) {
            Object obj = field2.get(null);
            d dVar = obj instanceof d ? (d) obj : null;
            if (dVar != null) {
                int i14 = dVar.f20449b;
                String name = field2.getName();
                za.k.d(name, "field.name");
                c0120a2 = new a.C0120a(i14, name);
            } else {
                c0120a2 = null;
            }
            if (c0120a2 != null) {
                arrayList2.add(c0120a2);
            }
        }
        f20446y = arrayList2;
        Field[] fields2 = d.class.getFields();
        za.k.d(fields2, "T::class.java.fields");
        ArrayList arrayList3 = new ArrayList();
        for (Field field3 : fields2) {
            if (Modifier.isStatic(field3.getModifiers())) {
                arrayList3.add(field3);
            }
        }
        ArrayList<Field> arrayList4 = new ArrayList();
        for (Object obj2 : arrayList3) {
            if (za.k.a(((Field) obj2).getType(), Integer.TYPE)) {
                arrayList4.add(obj2);
            }
        }
        ArrayList arrayList5 = new ArrayList();
        for (Field field4 : arrayList4) {
            Object obj3 = field4.get(null);
            za.k.c(obj3, "null cannot be cast to non-null type kotlin.Int");
            int intValue = ((Integer) obj3).intValue();
            if ((intValue == ((-intValue) & intValue)) == true) {
                String name2 = field4.getName();
                za.k.d(name2, "field.name");
                c0120a = new a.C0120a(intValue, name2);
            } else {
                c0120a = null;
            }
            if (c0120a != null) {
                arrayList5.add(c0120a);
            }
        }
        f20447z = arrayList5;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public d(int i10, List<? extends c> list) {
        za.k.e(list, "excludes");
        this.f20448a = list;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            i10 &= ~((c) it.next()).a();
        }
        this.f20449b = i10;
    }

    public final boolean a(int i10) {
        return (this.f20449b & i10) != 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!za.k.a(d.class, obj != null ? obj.getClass() : null)) {
            return false;
        }
        za.k.c(obj, "null cannot be cast to non-null type org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter");
        d dVar = (d) obj;
        return za.k.a(this.f20448a, dVar.f20448a) && this.f20449b == dVar.f20449b;
    }

    public int hashCode() {
        return (this.f20448a.hashCode() * 31) + this.f20449b;
    }

    public final List<c> l() {
        return this.f20448a;
    }

    public final int m() {
        return this.f20449b;
    }

    public final d n(int i10) {
        int i11 = i10 & this.f20449b;
        if (i11 == 0) {
            return null;
        }
        return new d(i11, this.f20448a);
    }

    public String toString() {
        Object obj;
        Iterator<T> it = f20446y.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((a.C0120a) obj).a() == this.f20449b) {
                break;
            }
        }
        a.C0120a c0120a = (a.C0120a) obj;
        String b10 = c0120a != null ? c0120a.b() : null;
        if (b10 == null) {
            List<a.C0120a> list = f20447z;
            ArrayList arrayList = new ArrayList();
            for (a.C0120a c0120a2 : list) {
                String b11 = a(c0120a2.a()) ? c0120a2.b() : null;
                if (b11 != null) {
                    arrayList.add(b11);
                }
            }
            b10 = _Collections.c0(arrayList, " | ", null, null, 0, null, null, 62, null);
        }
        return "DescriptorKindFilter(" + b10 + ", " + this.f20448a + ')';
    }

    public /* synthetic */ d(int i10, List list, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this(i10, (i11 & 2) != 0 ? r.j() : list);
    }
}
