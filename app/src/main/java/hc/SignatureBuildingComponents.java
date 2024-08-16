package hc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections._Collections;
import za.Lambda;

/* compiled from: SignatureBuildingComponents.kt */
/* renamed from: hc.y, reason: use source file name */
/* loaded from: classes2.dex */
public final class SignatureBuildingComponents {

    /* renamed from: a, reason: collision with root package name */
    public static final SignatureBuildingComponents f12209a = new SignatureBuildingComponents();

    /* compiled from: SignatureBuildingComponents.kt */
    /* renamed from: hc.y$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<String, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f12210e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(String str) {
            za.k.e(str, "it");
            return SignatureBuildingComponents.f12209a.c(str);
        }
    }

    private SignatureBuildingComponents() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String c(String str) {
        if (str.length() <= 1) {
            return str;
        }
        return 'L' + str + ';';
    }

    public final String[] b(String... strArr) {
        za.k.e(strArr, "signatures");
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            arrayList.add("<init>(" + str + ")V");
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public final Set<String> d(String str, String... strArr) {
        za.k.e(str, "internalName");
        za.k.e(strArr, "signatures");
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (String str2 : strArr) {
            linkedHashSet.add(str + '.' + str2);
        }
        return linkedHashSet;
    }

    public final Set<String> e(String str, String... strArr) {
        za.k.e(str, "name");
        za.k.e(strArr, "signatures");
        return d(h(str), (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public final Set<String> f(String str, String... strArr) {
        za.k.e(str, "name");
        za.k.e(strArr, "signatures");
        return d(i(str), (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public final String g(String str) {
        za.k.e(str, "name");
        return "java/util/function/" + str;
    }

    public final String h(String str) {
        za.k.e(str, "name");
        return "java/lang/" + str;
    }

    public final String i(String str) {
        za.k.e(str, "name");
        return "java/util/" + str;
    }

    public final String j(String str, List<String> list, String str2) {
        String c02;
        za.k.e(str, "name");
        za.k.e(list, "parameters");
        za.k.e(str2, "ret");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append('(');
        c02 = _Collections.c0(list, "", null, null, 0, null, a.f12210e, 30, null);
        sb2.append(c02);
        sb2.append(')');
        sb2.append(c(str2));
        return sb2.toString();
    }

    public final String k(String str, String str2) {
        za.k.e(str, "internalName");
        za.k.e(str2, "jvmDescriptor");
        return str + '.' + str2;
    }
}
