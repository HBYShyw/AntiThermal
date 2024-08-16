package oc;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import sd.StringsJVM;

/* compiled from: FqNamesUtil.kt */
/* loaded from: classes2.dex */
public final class e {

    /* compiled from: FqNamesUtil.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f16441a;

        static {
            int[] iArr = new int[k.values().length];
            try {
                iArr[k.BEGINNING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[k.AFTER_DOT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[k.MIDDLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f16441a = iArr;
        }
    }

    public static final <V> V a(FqName fqName, Map<FqName, ? extends V> map) {
        Object next;
        za.k.e(fqName, "<this>");
        za.k.e(map, "values");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator<Map.Entry<FqName, ? extends V>> it = map.entrySet().iterator();
        while (true) {
            boolean z10 = true;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<FqName, ? extends V> next2 = it.next();
            FqName key = next2.getKey();
            if (!za.k.a(fqName, key) && !b(fqName, key)) {
                z10 = false;
            }
            if (z10) {
                linkedHashMap.put(next2.getKey(), next2.getValue());
            }
        }
        if (!(!linkedHashMap.isEmpty())) {
            linkedHashMap = null;
        }
        if (linkedHashMap == null) {
            return null;
        }
        Iterator it2 = linkedHashMap.entrySet().iterator();
        if (it2.hasNext()) {
            next = it2.next();
            if (it2.hasNext()) {
                int length = g((FqName) ((Map.Entry) next).getKey(), fqName).b().length();
                do {
                    Object next3 = it2.next();
                    int length2 = g((FqName) ((Map.Entry) next3).getKey(), fqName).b().length();
                    if (length > length2) {
                        next = next3;
                        length = length2;
                    }
                } while (it2.hasNext());
            }
        } else {
            next = null;
        }
        Map.Entry entry = (Map.Entry) next;
        if (entry != null) {
            return (V) entry.getValue();
        }
        return null;
    }

    public static final boolean b(FqName fqName, FqName fqName2) {
        za.k.e(fqName, "<this>");
        za.k.e(fqName2, "packageName");
        return za.k.a(f(fqName), fqName2);
    }

    private static final boolean c(String str, String str2) {
        boolean D;
        D = StringsJVM.D(str, str2, false, 2, null);
        return D && str.charAt(str2.length()) == '.';
    }

    public static final boolean d(FqName fqName, FqName fqName2) {
        za.k.e(fqName, "<this>");
        za.k.e(fqName2, "packageName");
        if (za.k.a(fqName, fqName2) || fqName2.d()) {
            return true;
        }
        String b10 = fqName.b();
        za.k.d(b10, "this.asString()");
        String b11 = fqName2.b();
        za.k.d(b11, "packageName.asString()");
        return c(b10, b11);
    }

    public static final boolean e(String str) {
        if (str == null) {
            return false;
        }
        k kVar = k.BEGINNING;
        for (int i10 = 0; i10 < str.length(); i10++) {
            char charAt = str.charAt(i10);
            int i11 = a.f16441a[kVar.ordinal()];
            if (i11 == 1 || i11 == 2) {
                if (!Character.isJavaIdentifierPart(charAt)) {
                    return false;
                }
                kVar = k.MIDDLE;
            } else if (i11 != 3) {
                continue;
            } else if (charAt == '.') {
                kVar = k.AFTER_DOT;
            } else if (!Character.isJavaIdentifierPart(charAt)) {
                return false;
            }
        }
        return kVar != k.AFTER_DOT;
    }

    public static final FqName f(FqName fqName) {
        za.k.e(fqName, "<this>");
        if (fqName.d()) {
            return null;
        }
        return fqName.e();
    }

    public static final FqName g(FqName fqName, FqName fqName2) {
        za.k.e(fqName, "<this>");
        za.k.e(fqName2, "prefix");
        if (!d(fqName, fqName2) || fqName2.d()) {
            return fqName;
        }
        if (za.k.a(fqName, fqName2)) {
            FqName fqName3 = FqName.f16431c;
            za.k.d(fqName3, "ROOT");
            return fqName3;
        }
        String b10 = fqName.b();
        za.k.d(b10, "asString()");
        String substring = b10.substring(fqName2.b().length() + 1);
        za.k.d(substring, "this as java.lang.String).substring(startIndex)");
        return new FqName(substring);
    }
}
