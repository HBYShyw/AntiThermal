package ob;

import hc.SignatureBuildingComponents;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.MutableCollections;
import kotlin.collections._Sets;
import kotlin.collections.r;
import mb.StandardNames;
import oc.ClassId;
import oc.FqNameUnsafe;
import xc.JvmPrimitiveType;

/* compiled from: JvmBuiltInsSignatures.kt */
/* renamed from: ob.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class JvmBuiltInsSignatures {

    /* renamed from: a, reason: collision with root package name */
    public static final JvmBuiltInsSignatures f16414a;

    /* renamed from: b, reason: collision with root package name */
    private static final Set<String> f16415b;

    /* renamed from: c, reason: collision with root package name */
    private static final Set<String> f16416c;

    /* renamed from: d, reason: collision with root package name */
    private static final Set<String> f16417d;

    /* renamed from: e, reason: collision with root package name */
    private static final Set<String> f16418e;

    /* renamed from: f, reason: collision with root package name */
    private static final Set<String> f16419f;

    /* renamed from: g, reason: collision with root package name */
    private static final Set<String> f16420g;

    static {
        Set<String> l10;
        Set k10;
        Set k11;
        Set k12;
        Set k13;
        Set k14;
        Set<String> k15;
        Set k16;
        Set k17;
        Set k18;
        Set k19;
        Set k20;
        Set<String> k21;
        Set k22;
        Set<String> k23;
        Set k24;
        Set<String> k25;
        JvmBuiltInsSignatures jvmBuiltInsSignatures = new JvmBuiltInsSignatures();
        f16414a = jvmBuiltInsSignatures;
        SignatureBuildingComponents signatureBuildingComponents = SignatureBuildingComponents.f12209a;
        l10 = _Sets.l(signatureBuildingComponents.f("Collection", "toArray()[Ljava/lang/Object;", "toArray([Ljava/lang/Object;)[Ljava/lang/Object;"), "java/lang/annotation/Annotation.annotationType()Ljava/lang/Class;");
        f16415b = l10;
        k10 = _Sets.k(jvmBuiltInsSignatures.b(), signatureBuildingComponents.f("List", "sort(Ljava/util/Comparator;)V"));
        k11 = _Sets.k(k10, signatureBuildingComponents.e("String", "codePointAt(I)I", "codePointBefore(I)I", "codePointCount(II)I", "compareToIgnoreCase(Ljava/lang/String;)I", "concat(Ljava/lang/String;)Ljava/lang/String;", "contains(Ljava/lang/CharSequence;)Z", "contentEquals(Ljava/lang/CharSequence;)Z", "contentEquals(Ljava/lang/StringBuffer;)Z", "endsWith(Ljava/lang/String;)Z", "equalsIgnoreCase(Ljava/lang/String;)Z", "getBytes()[B", "getBytes(II[BI)V", "getBytes(Ljava/lang/String;)[B", "getBytes(Ljava/nio/charset/Charset;)[B", "getChars(II[CI)V", "indexOf(I)I", "indexOf(II)I", "indexOf(Ljava/lang/String;)I", "indexOf(Ljava/lang/String;I)I", "intern()Ljava/lang/String;", "isEmpty()Z", "lastIndexOf(I)I", "lastIndexOf(II)I", "lastIndexOf(Ljava/lang/String;)I", "lastIndexOf(Ljava/lang/String;I)I", "matches(Ljava/lang/String;)Z", "offsetByCodePoints(II)I", "regionMatches(ILjava/lang/String;II)Z", "regionMatches(ZILjava/lang/String;II)Z", "replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", "replace(CC)Ljava/lang/String;", "replaceFirst(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", "replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", "split(Ljava/lang/String;I)[Ljava/lang/String;", "split(Ljava/lang/String;)[Ljava/lang/String;", "startsWith(Ljava/lang/String;I)Z", "startsWith(Ljava/lang/String;)Z", "substring(II)Ljava/lang/String;", "substring(I)Ljava/lang/String;", "toCharArray()[C", "toLowerCase()Ljava/lang/String;", "toLowerCase(Ljava/util/Locale;)Ljava/lang/String;", "toUpperCase()Ljava/lang/String;", "toUpperCase(Ljava/util/Locale;)Ljava/lang/String;", "trim()Ljava/lang/String;", "isBlank()Z", "lines()Ljava/util/stream/Stream;", "repeat(I)Ljava/lang/String;"));
        k12 = _Sets.k(k11, signatureBuildingComponents.e("Double", "isInfinite()Z", "isNaN()Z"));
        k13 = _Sets.k(k12, signatureBuildingComponents.e("Float", "isInfinite()Z", "isNaN()Z"));
        k14 = _Sets.k(k13, signatureBuildingComponents.e("Enum", "getDeclaringClass()Ljava/lang/Class;", "finalize()V"));
        k15 = _Sets.k(k14, signatureBuildingComponents.e("CharSequence", "isEmpty()Z"));
        f16416c = k15;
        k16 = _Sets.k(signatureBuildingComponents.e("CharSequence", "codePoints()Ljava/util/stream/IntStream;", "chars()Ljava/util/stream/IntStream;"), signatureBuildingComponents.f("Iterator", "forEachRemaining(Ljava/util/function/Consumer;)V"));
        k17 = _Sets.k(k16, signatureBuildingComponents.e("Iterable", "forEach(Ljava/util/function/Consumer;)V", "spliterator()Ljava/util/Spliterator;"));
        k18 = _Sets.k(k17, signatureBuildingComponents.e("Throwable", "setStackTrace([Ljava/lang/StackTraceElement;)V", "fillInStackTrace()Ljava/lang/Throwable;", "getLocalizedMessage()Ljava/lang/String;", "printStackTrace()V", "printStackTrace(Ljava/io/PrintStream;)V", "printStackTrace(Ljava/io/PrintWriter;)V", "getStackTrace()[Ljava/lang/StackTraceElement;", "initCause(Ljava/lang/Throwable;)Ljava/lang/Throwable;", "getSuppressed()[Ljava/lang/Throwable;", "addSuppressed(Ljava/lang/Throwable;)V"));
        k19 = _Sets.k(k18, signatureBuildingComponents.f("Collection", "spliterator()Ljava/util/Spliterator;", "parallelStream()Ljava/util/stream/Stream;", "stream()Ljava/util/stream/Stream;", "removeIf(Ljava/util/function/Predicate;)Z"));
        k20 = _Sets.k(k19, signatureBuildingComponents.f("List", "replaceAll(Ljava/util/function/UnaryOperator;)V"));
        k21 = _Sets.k(k20, signatureBuildingComponents.f("Map", "getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "forEach(Ljava/util/function/BiConsumer;)V", "replaceAll(Ljava/util/function/BiFunction;)V", "merge(Ljava/lang/Object;Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;", "computeIfPresent(Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;", "putIfAbsent(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "replace(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z", "replace(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;", "compute(Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;"));
        f16417d = k21;
        k22 = _Sets.k(signatureBuildingComponents.f("Collection", "removeIf(Ljava/util/function/Predicate;)Z"), signatureBuildingComponents.f("List", "replaceAll(Ljava/util/function/UnaryOperator;)V", "sort(Ljava/util/Comparator;)V"));
        k23 = _Sets.k(k22, signatureBuildingComponents.f("Map", "computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;", "computeIfPresent(Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;", "compute(Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;", "merge(Ljava/lang/Object;Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;", "putIfAbsent(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "remove(Ljava/lang/Object;Ljava/lang/Object;)Z", "replaceAll(Ljava/util/function/BiFunction;)V", "replace(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "replace(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z"));
        f16418e = k23;
        Set<String> a10 = jvmBuiltInsSignatures.a();
        String[] b10 = signatureBuildingComponents.b("D");
        k24 = _Sets.k(a10, signatureBuildingComponents.e("Float", (String[]) Arrays.copyOf(b10, b10.length)));
        String[] b11 = signatureBuildingComponents.b("[C", "[CII", "[III", "[BIILjava/lang/String;", "[BIILjava/nio/charset/Charset;", "[BLjava/lang/String;", "[BLjava/nio/charset/Charset;", "[BII", "[B", "Ljava/lang/StringBuffer;", "Ljava/lang/StringBuilder;");
        k25 = _Sets.k(k24, signatureBuildingComponents.e("String", (String[]) Arrays.copyOf(b11, b11.length)));
        f16419f = k25;
        String[] b12 = signatureBuildingComponents.b("Ljava/lang/String;Ljava/lang/Throwable;ZZ");
        f16420g = signatureBuildingComponents.e("Throwable", (String[]) Arrays.copyOf(b12, b12.length));
    }

    private JvmBuiltInsSignatures() {
    }

    private final Set<String> a() {
        List m10;
        SignatureBuildingComponents signatureBuildingComponents = SignatureBuildingComponents.f12209a;
        JvmPrimitiveType jvmPrimitiveType = JvmPrimitiveType.BYTE;
        m10 = r.m(JvmPrimitiveType.BOOLEAN, jvmPrimitiveType, JvmPrimitiveType.DOUBLE, JvmPrimitiveType.FLOAT, jvmPrimitiveType, JvmPrimitiveType.INT, JvmPrimitiveType.LONG, JvmPrimitiveType.SHORT);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator it = m10.iterator();
        while (it.hasNext()) {
            String b10 = ((JvmPrimitiveType) it.next()).g().g().b();
            za.k.d(b10, "it.wrapperFqName.shortName().asString()");
            String[] b11 = signatureBuildingComponents.b("Ljava/lang/String;");
            MutableCollections.z(linkedHashSet, signatureBuildingComponents.e(b10, (String[]) Arrays.copyOf(b11, b11.length)));
        }
        return linkedHashSet;
    }

    private final Set<String> b() {
        List<JvmPrimitiveType> m10;
        SignatureBuildingComponents signatureBuildingComponents = SignatureBuildingComponents.f12209a;
        m10 = r.m(JvmPrimitiveType.BOOLEAN, JvmPrimitiveType.CHAR);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (JvmPrimitiveType jvmPrimitiveType : m10) {
            String b10 = jvmPrimitiveType.g().g().b();
            za.k.d(b10, "it.wrapperFqName.shortName().asString()");
            MutableCollections.z(linkedHashSet, signatureBuildingComponents.e(b10, jvmPrimitiveType.e() + "Value()" + jvmPrimitiveType.d()));
        }
        return linkedHashSet;
    }

    public final Set<String> c() {
        return f16415b;
    }

    public final Set<String> d() {
        return f16419f;
    }

    public final Set<String> e() {
        return f16416c;
    }

    public final Set<String> f() {
        return f16418e;
    }

    public final Set<String> g() {
        return f16420g;
    }

    public final Set<String> h() {
        return f16417d;
    }

    public final boolean i(FqNameUnsafe fqNameUnsafe) {
        za.k.e(fqNameUnsafe, "fqName");
        return za.k.a(fqNameUnsafe, StandardNames.a.f15305i) || StandardNames.e(fqNameUnsafe);
    }

    public final boolean j(FqNameUnsafe fqNameUnsafe) {
        za.k.e(fqNameUnsafe, "fqName");
        if (i(fqNameUnsafe)) {
            return true;
        }
        ClassId n10 = JavaToKotlinClassMap.f16339a.n(fqNameUnsafe);
        if (n10 == null) {
            return false;
        }
        try {
            return Serializable.class.isAssignableFrom(Class.forName(n10.b().b()));
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
