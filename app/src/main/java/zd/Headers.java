package zd;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.collections.MutableCollections;
import sd.StringsJVM;
import ta.progressionUtil;
import za.DefaultConstructorMarker;

/* compiled from: Headers.kt */
@Metadata(bv = {}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\u0006\u0018\u00002\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00020\u0001:\u0002\u001c\u001dB\u0017\b\u0002\u0012\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00030\u0018¢\u0006\u0004\b\u001a\u0010\u001bJ\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0086\u0002J\u000e\u0010\b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\t\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0006J\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00030\n2\u0006\u0010\u0004\u001a\u00020\u0003J\u001b\u0010\r\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00020\fH\u0096\u0002J\u0006\u0010\u000f\u001a\u00020\u000eJ\u0013\u0010\u0013\u001a\u00020\u00122\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0014\u001a\u00020\u0006H\u0016J\b\u0010\u0015\u001a\u00020\u0003H\u0016R\u0011\u0010\u0016\u001a\u00020\u00068G¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u001e"}, d2 = {"Lzd/t;", "", "Lma/o;", "", "name", "d", "", ThermalBaseConfig.Item.ATTR_INDEX, "e", "g", "", "h", "", "iterator", "Lzd/t$a;", "f", "", "other", "", "equals", "hashCode", "toString", "size", "()I", "", "namesAndValues", "<init>", "([Ljava/lang/String;)V", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.t, reason: use source file name */
/* loaded from: classes2.dex */
public final class Headers implements Iterable<ma.o<? extends String, ? extends String>>, ab.a {

    /* renamed from: f, reason: collision with root package name */
    public static final b f20708f = new b(null);

    /* renamed from: e, reason: collision with root package name */
    private final String[] f20709e;

    /* compiled from: Headers.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0014\u0010\u0015J\u0017\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\b\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002J\u001f\u0010\t\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\t\u0010\nJ\u000e\u0010\u000b\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0002J\u0019\u0010\f\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0086\u0002J\u0006\u0010\u000e\u001a\u00020\rR \u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00020\u000f8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u0016"}, d2 = {"Lzd/t$a;", "", "", "line", "b", "(Ljava/lang/String;)Lzd/t$a;", "name", ThermalBaseConfig.Item.ATTR_VALUE, "a", "c", "(Ljava/lang/String;Ljava/lang/String;)Lzd/t$a;", "f", "g", "Lzd/t;", "d", "", "namesAndValues", "Ljava/util/List;", "e", "()Ljava/util/List;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.t$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final List<String> f20710a = new ArrayList(20);

        public final a a(String name, String value) {
            za.k.e(name, "name");
            za.k.e(value, ThermalBaseConfig.Item.ATTR_VALUE);
            b bVar = Headers.f20708f;
            bVar.d(name);
            bVar.e(value, name);
            c(name, value);
            return this;
        }

        public final a b(String line) {
            int U;
            za.k.e(line, "line");
            U = sd.v.U(line, COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR, 1, false, 4, null);
            if (U != -1) {
                String substring = line.substring(0, U);
                za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                String substring2 = line.substring(U + 1);
                za.k.d(substring2, "this as java.lang.String).substring(startIndex)");
                c(substring, substring2);
            } else if (line.charAt(0) == ':') {
                String substring3 = line.substring(1);
                za.k.d(substring3, "this as java.lang.String).substring(startIndex)");
                c("", substring3);
            } else {
                c("", line);
            }
            return this;
        }

        public final a c(String name, String value) {
            CharSequence J0;
            za.k.e(name, "name");
            za.k.e(value, ThermalBaseConfig.Item.ATTR_VALUE);
            e().add(name);
            List<String> e10 = e();
            J0 = sd.v.J0(value);
            e10.add(J0.toString());
            return this;
        }

        public final Headers d() {
            Object[] array = this.f20710a.toArray(new String[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            return new Headers((String[]) array, null);
        }

        public final List<String> e() {
            return this.f20710a;
        }

        public final a f(String name) {
            boolean r10;
            za.k.e(name, "name");
            int i10 = 0;
            while (i10 < e().size()) {
                r10 = StringsJVM.r(name, e().get(i10), true);
                if (r10) {
                    e().remove(i10);
                    e().remove(i10);
                    i10 -= 2;
                }
                i10 += 2;
            }
            return this;
        }

        public final a g(String name, String value) {
            za.k.e(name, "name");
            za.k.e(value, ThermalBaseConfig.Item.ATTR_VALUE);
            b bVar = Headers.f20708f;
            bVar.d(name);
            bVar.e(value, name);
            f(name);
            c(name, value);
            return this;
        }
    }

    /* compiled from: Headers.kt */
    @Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010J'\u0010\u0006\u001a\u0004\u0018\u00010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00022\u0006\u0010\u0005\u001a\u00020\u0003H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0003H\u0002J\u0018\u0010\u000b\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0003H\u0002J#\u0010\r\u001a\u00020\f2\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u0002\"\u00020\u0003H\u0007¢\u0006\u0004\b\r\u0010\u000e¨\u0006\u0011"}, d2 = {"Lzd/t$b;", "", "", "", "namesAndValues", "name", "f", "([Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", "Lma/f0;", "d", ThermalBaseConfig.Item.ATTR_VALUE, "e", "Lzd/t;", "g", "([Ljava/lang/String;)Lzd/t;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.t$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void d(String str) {
            if (str.length() > 0) {
                int length = str.length();
                int i10 = 0;
                while (i10 < length) {
                    int i11 = i10 + 1;
                    char charAt = str.charAt(i10);
                    if (!('!' <= charAt && charAt < 127)) {
                        throw new IllegalArgumentException(ae.d.s("Unexpected char %#04x at %d in header name: %s", Integer.valueOf(charAt), Integer.valueOf(i10), str).toString());
                    }
                    i10 = i11;
                }
                return;
            }
            throw new IllegalArgumentException("name is empty".toString());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:13:0x005b A[LOOP:0: B:2:0x0006->B:13:0x005b, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0026 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final void e(String str, String str2) {
            boolean z10;
            int length = str.length();
            int i10 = 0;
            while (i10 < length) {
                int i11 = i10 + 1;
                char charAt = str.charAt(i10);
                if (charAt != '\t') {
                    if (!(' ' <= charAt && charAt < 127)) {
                        z10 = false;
                        if (z10) {
                            throw new IllegalArgumentException(za.k.l(ae.d.s("Unexpected char %#04x at %d in %s value", Integer.valueOf(charAt), Integer.valueOf(i10), str2), ae.d.F(str2) ? "" : za.k.l(": ", str)).toString());
                        }
                        i10 = i11;
                    }
                }
                z10 = true;
                if (z10) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String f(String[] namesAndValues, String name) {
            boolean r10;
            int length = namesAndValues.length - 2;
            int b10 = progressionUtil.b(length, 0, -2);
            if (b10 > length) {
                return null;
            }
            while (true) {
                int i10 = length - 2;
                r10 = StringsJVM.r(name, namesAndValues[length], true);
                if (r10) {
                    return namesAndValues[length + 1];
                }
                if (length == b10) {
                    return null;
                }
                length = i10;
            }
        }

        public final Headers g(String... namesAndValues) {
            CharSequence J0;
            za.k.e(namesAndValues, "namesAndValues");
            int i10 = 0;
            if (namesAndValues.length % 2 == 0) {
                String[] strArr = (String[]) namesAndValues.clone();
                int length = strArr.length;
                int i11 = 0;
                while (i11 < length) {
                    int i12 = i11 + 1;
                    if (strArr[i11] != null) {
                        J0 = sd.v.J0(strArr[i11]);
                        strArr[i11] = J0.toString();
                        i11 = i12;
                    } else {
                        throw new IllegalArgumentException("Headers cannot be null".toString());
                    }
                }
                int b10 = progressionUtil.b(0, strArr.length - 1, 2);
                if (b10 >= 0) {
                    while (true) {
                        int i13 = i10 + 2;
                        String str = strArr[i10];
                        String str2 = strArr[i10 + 1];
                        d(str);
                        e(str2, str);
                        if (i10 == b10) {
                            break;
                        }
                        i10 = i13;
                    }
                }
                return new Headers(strArr, null);
            }
            throw new IllegalArgumentException("Expected alternating header names and values".toString());
        }
    }

    private Headers(String[] strArr) {
        this.f20709e = strArr;
    }

    public /* synthetic */ Headers(String[] strArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(strArr);
    }

    public final String d(String name) {
        za.k.e(name, "name");
        return f20708f.f(this.f20709e, name);
    }

    public final String e(int index) {
        return this.f20709e[index * 2];
    }

    public boolean equals(Object other) {
        return (other instanceof Headers) && Arrays.equals(this.f20709e, ((Headers) other).f20709e);
    }

    public final a f() {
        a aVar = new a();
        MutableCollections.A(aVar.e(), this.f20709e);
        return aVar;
    }

    public final String g(int index) {
        return this.f20709e[(index * 2) + 1];
    }

    public final List<String> h(String name) {
        List<String> j10;
        boolean r10;
        za.k.e(name, "name");
        int size = size();
        ArrayList arrayList = null;
        int i10 = 0;
        while (i10 < size) {
            int i11 = i10 + 1;
            r10 = StringsJVM.r(name, e(i10), true);
            if (r10) {
                if (arrayList == null) {
                    arrayList = new ArrayList(2);
                }
                arrayList.add(g(i10));
            }
            i10 = i11;
        }
        if (arrayList != null) {
            List<String> unmodifiableList = Collections.unmodifiableList(arrayList);
            za.k.d(unmodifiableList, "{\n      Collections.unmodifiableList(result)\n    }");
            return unmodifiableList;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    public int hashCode() {
        return Arrays.hashCode(this.f20709e);
    }

    @Override // java.lang.Iterable
    public Iterator<ma.o<? extends String, ? extends String>> iterator() {
        int size = size();
        ma.o[] oVarArr = new ma.o[size];
        for (int i10 = 0; i10 < size; i10++) {
            oVarArr[i10] = ma.u.a(e(i10), g(i10));
        }
        return za.b.a(oVarArr);
    }

    public final int size() {
        return this.f20709e.length / 2;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        int size = size();
        int i10 = 0;
        while (i10 < size) {
            int i11 = i10 + 1;
            String e10 = e(i10);
            String g6 = g(i10);
            sb2.append(e10);
            sb2.append(": ");
            if (ae.d.F(e10)) {
                g6 = "██";
            }
            sb2.append(g6);
            sb2.append("\n");
            i10 = i11;
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }
}
