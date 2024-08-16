package gc;

import fb._Ranges;
import hc.SignatureBuildingComponents;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.IndexedValue;
import kotlin.collections.MapsJVM;
import kotlin.collections._Arrays;
import ma.Unit;
import ma.u;
import xc.JvmPrimitiveType;

/* compiled from: predefinedEnhancementInfo.kt */
/* loaded from: classes2.dex */
final class m {

    /* renamed from: a, reason: collision with root package name */
    private final Map<String, k> f11719a = new LinkedHashMap();

    /* compiled from: predefinedEnhancementInfo.kt */
    /* loaded from: classes2.dex */
    public final class a {

        /* renamed from: a, reason: collision with root package name */
        private final String f11720a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ m f11721b;

        /* compiled from: predefinedEnhancementInfo.kt */
        /* renamed from: gc.m$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public final class C0038a {

            /* renamed from: a, reason: collision with root package name */
            private final String f11722a;

            /* renamed from: b, reason: collision with root package name */
            private final List<ma.o<String, q>> f11723b;

            /* renamed from: c, reason: collision with root package name */
            private ma.o<String, q> f11724c;

            /* renamed from: d, reason: collision with root package name */
            final /* synthetic */ a f11725d;

            public C0038a(a aVar, String str) {
                za.k.e(str, "functionName");
                this.f11725d = aVar;
                this.f11722a = str;
                this.f11723b = new ArrayList();
                this.f11724c = u.a("V", null);
            }

            public final ma.o<String, k> a() {
                int u7;
                int u10;
                SignatureBuildingComponents signatureBuildingComponents = SignatureBuildingComponents.f12209a;
                String b10 = this.f11725d.b();
                String str = this.f11722a;
                List<ma.o<String, q>> list = this.f11723b;
                u7 = kotlin.collections.s.u(list, 10);
                ArrayList arrayList = new ArrayList(u7);
                Iterator<T> it = list.iterator();
                while (it.hasNext()) {
                    arrayList.add((String) ((ma.o) it.next()).c());
                }
                String k10 = signatureBuildingComponents.k(b10, signatureBuildingComponents.j(str, arrayList, this.f11724c.c()));
                q d10 = this.f11724c.d();
                List<ma.o<String, q>> list2 = this.f11723b;
                u10 = kotlin.collections.s.u(list2, 10);
                ArrayList arrayList2 = new ArrayList(u10);
                Iterator<T> it2 = list2.iterator();
                while (it2.hasNext()) {
                    arrayList2.add((q) ((ma.o) it2.next()).d());
                }
                return u.a(k10, new k(d10, arrayList2));
            }

            public final void b(String str, e... eVarArr) {
                Iterable<IndexedValue> s02;
                int u7;
                int e10;
                int c10;
                q qVar;
                za.k.e(str, "type");
                za.k.e(eVarArr, "qualifiers");
                List<ma.o<String, q>> list = this.f11723b;
                if (eVarArr.length == 0) {
                    qVar = null;
                } else {
                    s02 = _Arrays.s0(eVarArr);
                    u7 = kotlin.collections.s.u(s02, 10);
                    e10 = MapsJVM.e(u7);
                    c10 = _Ranges.c(e10, 16);
                    LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
                    for (IndexedValue indexedValue : s02) {
                        linkedHashMap.put(Integer.valueOf(indexedValue.c()), (e) indexedValue.d());
                    }
                    qVar = new q(linkedHashMap);
                }
                list.add(u.a(str, qVar));
            }

            public final void c(String str, e... eVarArr) {
                Iterable<IndexedValue> s02;
                int u7;
                int e10;
                int c10;
                za.k.e(str, "type");
                za.k.e(eVarArr, "qualifiers");
                s02 = _Arrays.s0(eVarArr);
                u7 = kotlin.collections.s.u(s02, 10);
                e10 = MapsJVM.e(u7);
                c10 = _Ranges.c(e10, 16);
                LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
                for (IndexedValue indexedValue : s02) {
                    linkedHashMap.put(Integer.valueOf(indexedValue.c()), (e) indexedValue.d());
                }
                this.f11724c = u.a(str, new q(linkedHashMap));
            }

            public final void d(JvmPrimitiveType jvmPrimitiveType) {
                za.k.e(jvmPrimitiveType, "type");
                String d10 = jvmPrimitiveType.d();
                za.k.d(d10, "type.desc");
                this.f11724c = u.a(d10, null);
            }
        }

        public a(m mVar, String str) {
            za.k.e(str, "className");
            this.f11721b = mVar;
            this.f11720a = str;
        }

        public final void a(String str, ya.l<? super C0038a, Unit> lVar) {
            za.k.e(str, "name");
            za.k.e(lVar, "block");
            Map map = this.f11721b.f11719a;
            C0038a c0038a = new C0038a(this, str);
            lVar.invoke(c0038a);
            ma.o<String, k> a10 = c0038a.a();
            map.put(a10.c(), a10.d());
        }

        public final String b() {
            return this.f11720a;
        }
    }

    public final Map<String, k> b() {
        return this.f11719a;
    }
}
