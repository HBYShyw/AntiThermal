package yb;

import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import fb._Ranges;
import hc.SignatureBuildingComponents;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.MapsJVM;
import kotlin.collections._Collections;
import kotlin.collections._Sets;
import kotlin.collections.m0;
import kotlin.collections.s0;
import oc.Name;
import xc.JvmPrimitiveType;
import za.DefaultConstructorMarker;

/* compiled from: SpecialGenericSignatures.kt */
/* renamed from: yb.i0, reason: use source file name */
/* loaded from: classes2.dex */
public class SpecialGenericSignatures {

    /* renamed from: a, reason: collision with root package name */
    public static final a f20091a = new a(null);

    /* renamed from: b, reason: collision with root package name */
    private static final List<a.C0117a> f20092b;

    /* renamed from: c, reason: collision with root package name */
    private static final List<String> f20093c;

    /* renamed from: d, reason: collision with root package name */
    private static final List<String> f20094d;

    /* renamed from: e, reason: collision with root package name */
    private static final Map<a.C0117a, c> f20095e;

    /* renamed from: f, reason: collision with root package name */
    private static final Map<String, c> f20096f;

    /* renamed from: g, reason: collision with root package name */
    private static final Set<Name> f20097g;

    /* renamed from: h, reason: collision with root package name */
    private static final Set<String> f20098h;

    /* renamed from: i, reason: collision with root package name */
    private static final a.C0117a f20099i;

    /* renamed from: j, reason: collision with root package name */
    private static final Map<a.C0117a, Name> f20100j;

    /* renamed from: k, reason: collision with root package name */
    private static final Map<String, Name> f20101k;

    /* renamed from: l, reason: collision with root package name */
    private static final List<Name> f20102l;

    /* renamed from: m, reason: collision with root package name */
    private static final Map<Name, Name> f20103m;

    /* compiled from: SpecialGenericSignatures.kt */
    /* renamed from: yb.i0$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: SpecialGenericSignatures.kt */
        /* renamed from: yb.i0$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0117a {

            /* renamed from: a, reason: collision with root package name */
            private final Name f20104a;

            /* renamed from: b, reason: collision with root package name */
            private final String f20105b;

            public C0117a(Name name, String str) {
                za.k.e(name, "name");
                za.k.e(str, "signature");
                this.f20104a = name;
                this.f20105b = str;
            }

            public final Name a() {
                return this.f20104a;
            }

            public final String b() {
                return this.f20105b;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof C0117a)) {
                    return false;
                }
                C0117a c0117a = (C0117a) obj;
                return za.k.a(this.f20104a, c0117a.f20104a) && za.k.a(this.f20105b, c0117a.f20105b);
            }

            public int hashCode() {
                return (this.f20104a.hashCode() * 31) + this.f20105b.hashCode();
            }

            public String toString() {
                return "NameAndSignature(name=" + this.f20104a + ", signature=" + this.f20105b + ')';
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final C0117a m(String str, String str2, String str3, String str4) {
            Name f10 = Name.f(str2);
            za.k.d(f10, "identifier(name)");
            return new C0117a(f10, SignatureBuildingComponents.f12209a.k(str, str2 + '(' + str3 + ')' + str4));
        }

        public final Name b(Name name) {
            za.k.e(name, "name");
            return f().get(name);
        }

        public final List<String> c() {
            return SpecialGenericSignatures.f20093c;
        }

        public final Set<Name> d() {
            return SpecialGenericSignatures.f20097g;
        }

        public final Set<String> e() {
            return SpecialGenericSignatures.f20098h;
        }

        public final Map<Name, Name> f() {
            return SpecialGenericSignatures.f20103m;
        }

        public final List<Name> g() {
            return SpecialGenericSignatures.f20102l;
        }

        public final C0117a h() {
            return SpecialGenericSignatures.f20099i;
        }

        public final Map<String, c> i() {
            return SpecialGenericSignatures.f20096f;
        }

        public final Map<String, Name> j() {
            return SpecialGenericSignatures.f20101k;
        }

        public final boolean k(Name name) {
            za.k.e(name, "<this>");
            return g().contains(name);
        }

        public final b l(String str) {
            Object j10;
            za.k.e(str, "builtinSignature");
            if (c().contains(str)) {
                return b.ONE_COLLECTION_PARAMETER;
            }
            j10 = m0.j(i(), str);
            if (((c) j10) == c.f20112f) {
                return b.OBJECT_PARAMETER_GENERIC;
            }
            return b.OBJECT_PARAMETER_NON_GENERIC;
        }
    }

    /* compiled from: SpecialGenericSignatures.kt */
    /* renamed from: yb.i0$b */
    /* loaded from: classes2.dex */
    public enum b {
        ONE_COLLECTION_PARAMETER("Ljava/util/Collection<+Ljava/lang/Object;>;", false),
        OBJECT_PARAMETER_NON_GENERIC(null, true),
        OBJECT_PARAMETER_GENERIC("Ljava/lang/Object;", true);


        /* renamed from: e, reason: collision with root package name */
        private final String f20110e;

        /* renamed from: f, reason: collision with root package name */
        private final boolean f20111f;

        b(String str, boolean z10) {
            this.f20110e = str;
            this.f20111f = z10;
        }
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* compiled from: SpecialGenericSignatures.kt */
    /* renamed from: yb.i0$c */
    /* loaded from: classes2.dex */
    public static final class c {

        /* renamed from: f, reason: collision with root package name */
        public static final c f20112f = new c("NULL", 0, null);

        /* renamed from: g, reason: collision with root package name */
        public static final c f20113g = new c("INDEX", 1, -1);

        /* renamed from: h, reason: collision with root package name */
        public static final c f20114h = new c("FALSE", 2, Boolean.FALSE);

        /* renamed from: i, reason: collision with root package name */
        public static final c f20115i = new a("MAP_GET_OR_DEFAULT", 3);

        /* renamed from: j, reason: collision with root package name */
        private static final /* synthetic */ c[] f20116j = a();

        /* renamed from: e, reason: collision with root package name */
        private final Object f20117e;

        /* compiled from: SpecialGenericSignatures.kt */
        /* renamed from: yb.i0$c$a */
        /* loaded from: classes2.dex */
        static final class a extends c {
            /* JADX WARN: Illegal instructions before constructor call */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            a(String str, int i10) {
                super(str, i10, r0, r0);
                DefaultConstructorMarker defaultConstructorMarker = null;
            }
        }

        private c(String str, int i10, Object obj) {
            this.f20117e = obj;
        }

        public /* synthetic */ c(String str, int i10, Object obj, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, i10, obj);
        }

        private static final /* synthetic */ c[] a() {
            return new c[]{f20112f, f20113g, f20114h, f20115i};
        }

        public static c valueOf(String str) {
            return (c) Enum.valueOf(c.class, str);
        }

        public static c[] values() {
            return (c[]) f20116j.clone();
        }
    }

    static {
        Set<String> h10;
        int u7;
        int u10;
        int u11;
        Map<a.C0117a, c> l10;
        int e10;
        Set k10;
        int u12;
        Set<Name> D0;
        int u13;
        Set<String> D02;
        Map<a.C0117a, Name> l11;
        int e11;
        int u14;
        int u15;
        int u16;
        int e12;
        int c10;
        h10 = s0.h("containsAll", "removeAll", "retainAll");
        u7 = kotlin.collections.s.u(h10, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (String str : h10) {
            a aVar = f20091a;
            String d10 = JvmPrimitiveType.BOOLEAN.d();
            za.k.d(d10, "BOOLEAN.desc");
            arrayList.add(aVar.m("java/util/Collection", str, "Ljava/util/Collection;", d10));
        }
        f20092b = arrayList;
        u10 = kotlin.collections.s.u(arrayList, 10);
        ArrayList arrayList2 = new ArrayList(u10);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((a.C0117a) it.next()).b());
        }
        f20093c = arrayList2;
        List<a.C0117a> list = f20092b;
        u11 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList3 = new ArrayList(u11);
        Iterator<T> it2 = list.iterator();
        while (it2.hasNext()) {
            arrayList3.add(((a.C0117a) it2.next()).a().b());
        }
        f20094d = arrayList3;
        SignatureBuildingComponents signatureBuildingComponents = SignatureBuildingComponents.f12209a;
        a aVar2 = f20091a;
        String i10 = signatureBuildingComponents.i("Collection");
        JvmPrimitiveType jvmPrimitiveType = JvmPrimitiveType.BOOLEAN;
        String d11 = jvmPrimitiveType.d();
        za.k.d(d11, "BOOLEAN.desc");
        a.C0117a m10 = aVar2.m(i10, "contains", "Ljava/lang/Object;", d11);
        c cVar = c.f20114h;
        String i11 = signatureBuildingComponents.i("Collection");
        String d12 = jvmPrimitiveType.d();
        za.k.d(d12, "BOOLEAN.desc");
        String i12 = signatureBuildingComponents.i("Map");
        String d13 = jvmPrimitiveType.d();
        za.k.d(d13, "BOOLEAN.desc");
        String i13 = signatureBuildingComponents.i("Map");
        String d14 = jvmPrimitiveType.d();
        za.k.d(d14, "BOOLEAN.desc");
        String i14 = signatureBuildingComponents.i("Map");
        String d15 = jvmPrimitiveType.d();
        za.k.d(d15, "BOOLEAN.desc");
        a.C0117a m11 = aVar2.m(signatureBuildingComponents.i("Map"), "get", "Ljava/lang/Object;", "Ljava/lang/Object;");
        c cVar2 = c.f20112f;
        String i15 = signatureBuildingComponents.i("List");
        JvmPrimitiveType jvmPrimitiveType2 = JvmPrimitiveType.INT;
        String d16 = jvmPrimitiveType2.d();
        za.k.d(d16, "INT.desc");
        a.C0117a m12 = aVar2.m(i15, "indexOf", "Ljava/lang/Object;", d16);
        c cVar3 = c.f20113g;
        String i16 = signatureBuildingComponents.i("List");
        String d17 = jvmPrimitiveType2.d();
        za.k.d(d17, "INT.desc");
        l10 = m0.l(ma.u.a(m10, cVar), ma.u.a(aVar2.m(i11, EventType.STATE_PACKAGE_CHANGED_REMOVE, "Ljava/lang/Object;", d12), cVar), ma.u.a(aVar2.m(i12, "containsKey", "Ljava/lang/Object;", d13), cVar), ma.u.a(aVar2.m(i13, "containsValue", "Ljava/lang/Object;", d14), cVar), ma.u.a(aVar2.m(i14, EventType.STATE_PACKAGE_CHANGED_REMOVE, "Ljava/lang/Object;Ljava/lang/Object;", d15), cVar), ma.u.a(aVar2.m(signatureBuildingComponents.i("Map"), "getOrDefault", "Ljava/lang/Object;Ljava/lang/Object;", "Ljava/lang/Object;"), c.f20115i), ma.u.a(m11, cVar2), ma.u.a(aVar2.m(signatureBuildingComponents.i("Map"), EventType.STATE_PACKAGE_CHANGED_REMOVE, "Ljava/lang/Object;", "Ljava/lang/Object;"), cVar2), ma.u.a(m12, cVar3), ma.u.a(aVar2.m(i16, "lastIndexOf", "Ljava/lang/Object;", d17), cVar3));
        f20095e = l10;
        e10 = MapsJVM.e(l10.size());
        LinkedHashMap linkedHashMap = new LinkedHashMap(e10);
        Iterator<T> it3 = l10.entrySet().iterator();
        while (it3.hasNext()) {
            Map.Entry entry = (Map.Entry) it3.next();
            linkedHashMap.put(((a.C0117a) entry.getKey()).b(), entry.getValue());
        }
        f20096f = linkedHashMap;
        k10 = _Sets.k(f20095e.keySet(), f20092b);
        u12 = kotlin.collections.s.u(k10, 10);
        ArrayList arrayList4 = new ArrayList(u12);
        Iterator it4 = k10.iterator();
        while (it4.hasNext()) {
            arrayList4.add(((a.C0117a) it4.next()).a());
        }
        D0 = _Collections.D0(arrayList4);
        f20097g = D0;
        u13 = kotlin.collections.s.u(k10, 10);
        ArrayList arrayList5 = new ArrayList(u13);
        Iterator it5 = k10.iterator();
        while (it5.hasNext()) {
            arrayList5.add(((a.C0117a) it5.next()).b());
        }
        D02 = _Collections.D0(arrayList5);
        f20098h = D02;
        a aVar3 = f20091a;
        JvmPrimitiveType jvmPrimitiveType3 = JvmPrimitiveType.INT;
        String d18 = jvmPrimitiveType3.d();
        za.k.d(d18, "INT.desc");
        a.C0117a m13 = aVar3.m("java/util/List", "removeAt", d18, "Ljava/lang/Object;");
        f20099i = m13;
        SignatureBuildingComponents signatureBuildingComponents2 = SignatureBuildingComponents.f12209a;
        String h11 = signatureBuildingComponents2.h("Number");
        String d19 = JvmPrimitiveType.BYTE.d();
        za.k.d(d19, "BYTE.desc");
        String h12 = signatureBuildingComponents2.h("Number");
        String d20 = JvmPrimitiveType.SHORT.d();
        za.k.d(d20, "SHORT.desc");
        String h13 = signatureBuildingComponents2.h("Number");
        String d21 = jvmPrimitiveType3.d();
        za.k.d(d21, "INT.desc");
        String h14 = signatureBuildingComponents2.h("Number");
        String d22 = JvmPrimitiveType.LONG.d();
        za.k.d(d22, "LONG.desc");
        String h15 = signatureBuildingComponents2.h("Number");
        String d23 = JvmPrimitiveType.FLOAT.d();
        za.k.d(d23, "FLOAT.desc");
        String h16 = signatureBuildingComponents2.h("Number");
        String d24 = JvmPrimitiveType.DOUBLE.d();
        za.k.d(d24, "DOUBLE.desc");
        String h17 = signatureBuildingComponents2.h("CharSequence");
        String d25 = jvmPrimitiveType3.d();
        za.k.d(d25, "INT.desc");
        String d26 = JvmPrimitiveType.CHAR.d();
        za.k.d(d26, "CHAR.desc");
        l11 = m0.l(ma.u.a(aVar3.m(h11, "toByte", "", d19), Name.f("byteValue")), ma.u.a(aVar3.m(h12, "toShort", "", d20), Name.f("shortValue")), ma.u.a(aVar3.m(h13, "toInt", "", d21), Name.f("intValue")), ma.u.a(aVar3.m(h14, "toLong", "", d22), Name.f("longValue")), ma.u.a(aVar3.m(h15, "toFloat", "", d23), Name.f("floatValue")), ma.u.a(aVar3.m(h16, "toDouble", "", d24), Name.f("doubleValue")), ma.u.a(m13, Name.f(EventType.STATE_PACKAGE_CHANGED_REMOVE)), ma.u.a(aVar3.m(h17, "get", d25, d26), Name.f("charAt")));
        f20100j = l11;
        e11 = MapsJVM.e(l11.size());
        LinkedHashMap linkedHashMap2 = new LinkedHashMap(e11);
        Iterator<T> it6 = l11.entrySet().iterator();
        while (it6.hasNext()) {
            Map.Entry entry2 = (Map.Entry) it6.next();
            linkedHashMap2.put(((a.C0117a) entry2.getKey()).b(), entry2.getValue());
        }
        f20101k = linkedHashMap2;
        Set<a.C0117a> keySet = f20100j.keySet();
        u14 = kotlin.collections.s.u(keySet, 10);
        ArrayList arrayList6 = new ArrayList(u14);
        Iterator<T> it7 = keySet.iterator();
        while (it7.hasNext()) {
            arrayList6.add(((a.C0117a) it7.next()).a());
        }
        f20102l = arrayList6;
        Set<Map.Entry<a.C0117a, Name>> entrySet = f20100j.entrySet();
        u15 = kotlin.collections.s.u(entrySet, 10);
        ArrayList<ma.o> arrayList7 = new ArrayList(u15);
        Iterator<T> it8 = entrySet.iterator();
        while (it8.hasNext()) {
            Map.Entry entry3 = (Map.Entry) it8.next();
            arrayList7.add(new ma.o(((a.C0117a) entry3.getKey()).a(), entry3.getValue()));
        }
        u16 = kotlin.collections.s.u(arrayList7, 10);
        e12 = MapsJVM.e(u16);
        c10 = _Ranges.c(e12, 16);
        LinkedHashMap linkedHashMap3 = new LinkedHashMap(c10);
        for (ma.o oVar : arrayList7) {
            linkedHashMap3.put((Name) oVar.d(), (Name) oVar.c());
        }
        f20103m = linkedHashMap3;
    }
}
