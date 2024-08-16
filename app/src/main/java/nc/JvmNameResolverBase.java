package nc;

import fb._Ranges;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.IndexedValue;
import kotlin.collections.MapsJVM;
import kotlin.collections._Collections;
import kotlin.collections.r;
import kotlin.collections.s;
import lc.NameResolver;
import mc.JvmProtoBuf;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: JvmNameResolverBase.kt */
/* renamed from: nc.g, reason: use source file name */
/* loaded from: classes2.dex */
public class JvmNameResolverBase implements NameResolver {

    /* renamed from: d, reason: collision with root package name */
    public static final a f15998d = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private static final String f15999e;

    /* renamed from: f, reason: collision with root package name */
    private static final List<String> f16000f;

    /* renamed from: g, reason: collision with root package name */
    private static final Map<String, Integer> f16001g;

    /* renamed from: a, reason: collision with root package name */
    private final String[] f16002a;

    /* renamed from: b, reason: collision with root package name */
    private final Set<Integer> f16003b;

    /* renamed from: c, reason: collision with root package name */
    private final List<JvmProtoBuf.e.c> f16004c;

    /* compiled from: JvmNameResolverBase.kt */
    /* renamed from: nc.g$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: JvmNameResolverBase.kt */
    /* renamed from: nc.g$b */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f16005a;

        static {
            int[] iArr = new int[JvmProtoBuf.e.c.EnumC0077c.values().length];
            try {
                iArr[JvmProtoBuf.e.c.EnumC0077c.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[JvmProtoBuf.e.c.EnumC0077c.INTERNAL_TO_CLASS_ID.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[JvmProtoBuf.e.c.EnumC0077c.DESC_TO_CLASS_ID.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f16005a = iArr;
        }
    }

    static {
        List m10;
        String c02;
        List<String> m11;
        Iterable<IndexedValue> F0;
        int u7;
        int e10;
        int c10;
        m10 = r.m('k', 'o', 't', 'l', 'i', 'n');
        c02 = _Collections.c0(m10, "", null, null, 0, null, null, 62, null);
        f15999e = c02;
        m11 = r.m(c02 + "/Any", c02 + "/Nothing", c02 + "/Unit", c02 + "/Throwable", c02 + "/Number", c02 + "/Byte", c02 + "/Double", c02 + "/Float", c02 + "/Int", c02 + "/Long", c02 + "/Short", c02 + "/Boolean", c02 + "/Char", c02 + "/CharSequence", c02 + "/String", c02 + "/Comparable", c02 + "/Enum", c02 + "/Array", c02 + "/ByteArray", c02 + "/DoubleArray", c02 + "/FloatArray", c02 + "/IntArray", c02 + "/LongArray", c02 + "/ShortArray", c02 + "/BooleanArray", c02 + "/CharArray", c02 + "/Cloneable", c02 + "/Annotation", c02 + "/collections/Iterable", c02 + "/collections/MutableIterable", c02 + "/collections/Collection", c02 + "/collections/MutableCollection", c02 + "/collections/List", c02 + "/collections/MutableList", c02 + "/collections/Set", c02 + "/collections/MutableSet", c02 + "/collections/Map", c02 + "/collections/MutableMap", c02 + "/collections/Map.Entry", c02 + "/collections/MutableMap.MutableEntry", c02 + "/collections/Iterator", c02 + "/collections/MutableIterator", c02 + "/collections/ListIterator", c02 + "/collections/MutableListIterator");
        f16000f = m11;
        F0 = _Collections.F0(m11);
        u7 = s.u(F0, 10);
        e10 = MapsJVM.e(u7);
        c10 = _Ranges.c(e10, 16);
        LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
        for (IndexedValue indexedValue : F0) {
            linkedHashMap.put((String) indexedValue.d(), Integer.valueOf(indexedValue.c()));
        }
        f16001g = linkedHashMap;
    }

    public JvmNameResolverBase(String[] strArr, Set<Integer> set, List<JvmProtoBuf.e.c> list) {
        k.e(strArr, "strings");
        k.e(set, "localNameIndices");
        k.e(list, "records");
        this.f16002a = strArr;
        this.f16003b = set;
        this.f16004c = list;
    }

    @Override // lc.NameResolver
    public String a(int i10) {
        return getString(i10);
    }

    @Override // lc.NameResolver
    public boolean b(int i10) {
        return this.f16003b.contains(Integer.valueOf(i10));
    }

    @Override // lc.NameResolver
    public String getString(int i10) {
        String str;
        JvmProtoBuf.e.c cVar = this.f16004c.get(i10);
        if (cVar.K()) {
            str = cVar.D();
        } else {
            if (cVar.I()) {
                List<String> list = f16000f;
                int size = list.size();
                int z10 = cVar.z();
                if (z10 >= 0 && z10 < size) {
                    str = list.get(cVar.z());
                }
            }
            str = this.f16002a[i10];
        }
        if (cVar.F() >= 2) {
            List<Integer> G = cVar.G();
            k.d(G, "substringIndexList");
            Integer num = G.get(0);
            Integer num2 = G.get(1);
            k.d(num, "begin");
            if (num.intValue() >= 0) {
                int intValue = num.intValue();
                k.d(num2, "end");
                if (intValue <= num2.intValue() && num2.intValue() <= str.length()) {
                    k.d(str, "string");
                    str = str.substring(num.intValue(), num2.intValue());
                    k.d(str, "this as java.lang.String…ing(startIndex, endIndex)");
                }
            }
        }
        String str2 = str;
        if (cVar.B() >= 2) {
            List<Integer> C = cVar.C();
            k.d(C, "replaceCharList");
            Integer num3 = C.get(0);
            Integer num4 = C.get(1);
            k.d(str2, "string");
            str2 = StringsJVM.y(str2, (char) num3.intValue(), (char) num4.intValue(), false, 4, null);
        }
        String str3 = str2;
        JvmProtoBuf.e.c.EnumC0077c y4 = cVar.y();
        if (y4 == null) {
            y4 = JvmProtoBuf.e.c.EnumC0077c.NONE;
        }
        int i11 = b.f16005a[y4.ordinal()];
        if (i11 == 2) {
            k.d(str3, "string");
            str3 = StringsJVM.y(str3, '$', '.', false, 4, null);
        } else if (i11 == 3) {
            if (str3.length() >= 2) {
                k.d(str3, "string");
                str3 = str3.substring(1, str3.length() - 1);
                k.d(str3, "this as java.lang.String…ing(startIndex, endIndex)");
            }
            String str4 = str3;
            k.d(str4, "string");
            str3 = StringsJVM.y(str4, '$', '.', false, 4, null);
        }
        k.d(str3, "string");
        return str3;
    }
}
