package nc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections._Collections;
import kotlin.collections.r;
import sd.StringsJVM;
import ta.progressionUtil;
import za.k;

/* compiled from: ClassMapperLite.kt */
/* renamed from: nc.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class ClassMapperLite {

    /* renamed from: a, reason: collision with root package name */
    public static final ClassMapperLite f15982a = new ClassMapperLite();

    /* renamed from: b, reason: collision with root package name */
    private static final String f15983b;

    /* renamed from: c, reason: collision with root package name */
    private static final Map<String, String> f15984c;

    static {
        List m10;
        String c02;
        List m11;
        List<String> m12;
        List<String> m13;
        List<String> m14;
        m10 = r.m('k', 'o', 't', 'l', 'i', 'n');
        c02 = _Collections.c0(m10, "", null, null, 0, null, null, 62, null);
        f15983b = c02;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        m11 = r.m("Boolean", "Z", "Char", "C", "Byte", "B", "Short", "S", "Int", "I", "Float", "F", "Long", "J", "Double", "D");
        int b10 = progressionUtil.b(0, m11.size() - 1, 2);
        if (b10 >= 0) {
            int i10 = 0;
            while (true) {
                StringBuilder sb2 = new StringBuilder();
                String str = f15983b;
                sb2.append(str);
                sb2.append('/');
                sb2.append((String) m11.get(i10));
                int i11 = i10 + 1;
                linkedHashMap.put(sb2.toString(), m11.get(i11));
                linkedHashMap.put(str + '/' + ((String) m11.get(i10)) + "Array", '[' + ((String) m11.get(i11)));
                if (i10 == b10) {
                    break;
                } else {
                    i10 += 2;
                }
            }
        }
        linkedHashMap.put(f15983b + "/Unit", "V");
        a(linkedHashMap, "Any", "java/lang/Object");
        a(linkedHashMap, "Nothing", "java/lang/Void");
        a(linkedHashMap, "Annotation", "java/lang/annotation/Annotation");
        m12 = r.m("String", "CharSequence", "Throwable", "Cloneable", "Number", "Comparable", "Enum");
        for (String str2 : m12) {
            a(linkedHashMap, str2, "java/lang/" + str2);
        }
        m13 = r.m("Iterator", "Collection", "List", "Set", "Map", "ListIterator");
        for (String str3 : m13) {
            a(linkedHashMap, "collections/" + str3, "java/util/" + str3);
            a(linkedHashMap, "collections/Mutable" + str3, "java/util/" + str3);
        }
        a(linkedHashMap, "collections/Iterable", "java/lang/Iterable");
        a(linkedHashMap, "collections/MutableIterable", "java/lang/Iterable");
        a(linkedHashMap, "collections/Map.Entry", "java/util/Map$Entry");
        a(linkedHashMap, "collections/MutableMap.MutableEntry", "java/util/Map$Entry");
        for (int i12 = 0; i12 < 23; i12++) {
            StringBuilder sb3 = new StringBuilder();
            String str4 = f15983b;
            sb3.append(str4);
            sb3.append("/jvm/functions/Function");
            sb3.append(i12);
            a(linkedHashMap, "Function" + i12, sb3.toString());
            a(linkedHashMap, "reflect/KFunction" + i12, str4 + "/reflect/KFunction");
        }
        m14 = r.m("Char", "Byte", "Short", "Int", "Float", "Long", "Double", "String", "Enum");
        for (String str5 : m14) {
            a(linkedHashMap, str5 + ".Companion", f15983b + "/jvm/internal/" + str5 + "CompanionObject");
        }
        f15984c = linkedHashMap;
    }

    private ClassMapperLite() {
    }

    private static final void a(Map<String, String> map, String str, String str2) {
        map.put(f15983b + '/' + str, 'L' + str2 + ';');
    }

    public static final String b(String str) {
        String y4;
        k.e(str, "classId");
        String str2 = f15984c.get(str);
        if (str2 != null) {
            return str2;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append('L');
        y4 = StringsJVM.y(str, '.', '$', false, 4, null);
        sb2.append(y4);
        sb2.append(';');
        return sb2.toString();
    }
}
