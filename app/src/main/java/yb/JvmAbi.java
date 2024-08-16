package yb;

import oc.ClassId;
import oc.FqName;
import od.capitalizeDecapitalize;
import sd.StringsJVM;

/* compiled from: JvmAbi.kt */
/* renamed from: yb.a0, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmAbi {

    /* renamed from: a, reason: collision with root package name */
    public static final JvmAbi f20005a = new JvmAbi();

    /* renamed from: b, reason: collision with root package name */
    public static final FqName f20006b;

    /* renamed from: c, reason: collision with root package name */
    public static final ClassId f20007c;

    /* renamed from: d, reason: collision with root package name */
    private static final ClassId f20008d;

    /* renamed from: e, reason: collision with root package name */
    private static final ClassId f20009e;

    static {
        FqName fqName = new FqName("kotlin.jvm.JvmField");
        f20006b = fqName;
        ClassId m10 = ClassId.m(fqName);
        za.k.d(m10, "topLevel(JVM_FIELD_ANNOTATION_FQ_NAME)");
        f20007c = m10;
        ClassId m11 = ClassId.m(new FqName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl"));
        za.k.d(m11, "topLevel(FqName(\"kotlin.….ReflectionFactoryImpl\"))");
        f20008d = m11;
        ClassId e10 = ClassId.e("kotlin/jvm/internal/RepeatableContainer");
        za.k.d(e10, "fromString(\"kotlin/jvm/i…nal/RepeatableContainer\")");
        f20009e = e10;
    }

    private JvmAbi() {
    }

    public static final String b(String str) {
        za.k.e(str, "propertyName");
        if (f(str)) {
            return str;
        }
        return "get" + capitalizeDecapitalize.a(str);
    }

    public static final boolean c(String str) {
        boolean D;
        boolean D2;
        za.k.e(str, "name");
        D = StringsJVM.D(str, "get", false, 2, null);
        if (!D) {
            D2 = StringsJVM.D(str, "is", false, 2, null);
            if (!D2) {
                return false;
            }
        }
        return true;
    }

    public static final boolean d(String str) {
        boolean D;
        za.k.e(str, "name");
        D = StringsJVM.D(str, "set", false, 2, null);
        return D;
    }

    public static final String e(String str) {
        String a10;
        za.k.e(str, "propertyName");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("set");
        if (f(str)) {
            a10 = str.substring(2);
            za.k.d(a10, "this as java.lang.String).substring(startIndex)");
        } else {
            a10 = capitalizeDecapitalize.a(str);
        }
        sb2.append(a10);
        return sb2.toString();
    }

    public static final boolean f(String str) {
        boolean D;
        za.k.e(str, "name");
        D = StringsJVM.D(str, "is", false, 2, null);
        if (!D || str.length() == 2) {
            return false;
        }
        char charAt = str.charAt(2);
        return za.k.f(97, charAt) > 0 || za.k.f(charAt, 122) > 0;
    }

    public final ClassId a() {
        return f20009e;
    }
}
