package kb;

import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kb.e;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import kotlin.collections.s;
import vb.reflectClassUtil;
import za.DefaultConstructorMarker;

/* compiled from: AnnotationConstructorCaller.kt */
/* loaded from: classes2.dex */
public final class a implements e {

    /* renamed from: a, reason: collision with root package name */
    private final Class<?> f14233a;

    /* renamed from: b, reason: collision with root package name */
    private final List<String> f14234b;

    /* renamed from: c, reason: collision with root package name */
    private final EnumC0070a f14235c;

    /* renamed from: d, reason: collision with root package name */
    private final List<Method> f14236d;

    /* renamed from: e, reason: collision with root package name */
    private final List<Type> f14237e;

    /* renamed from: f, reason: collision with root package name */
    private final List<Class<?>> f14238f;

    /* renamed from: g, reason: collision with root package name */
    private final List<Object> f14239g;

    /* compiled from: AnnotationConstructorCaller.kt */
    /* renamed from: kb.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public enum EnumC0070a {
        CALL_BY_NAME,
        POSITIONAL_CALL
    }

    /* compiled from: AnnotationConstructorCaller.kt */
    /* loaded from: classes2.dex */
    public enum b {
        JAVA,
        KOTLIN
    }

    public a(Class<?> cls, List<String> list, EnumC0070a enumC0070a, b bVar, List<Method> list2) {
        int u7;
        int u10;
        int u11;
        List j02;
        za.k.e(cls, "jClass");
        za.k.e(list, "parameterNames");
        za.k.e(enumC0070a, "callMode");
        za.k.e(bVar, "origin");
        za.k.e(list2, "methods");
        this.f14233a = cls;
        this.f14234b = list;
        this.f14235c = enumC0070a;
        this.f14236d = list2;
        u7 = s.u(list2, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = list2.iterator();
        while (it.hasNext()) {
            arrayList.add(((Method) it.next()).getGenericReturnType());
        }
        this.f14237e = arrayList;
        List<Method> list3 = this.f14236d;
        u10 = s.u(list3, 10);
        ArrayList arrayList2 = new ArrayList(u10);
        Iterator<T> it2 = list3.iterator();
        while (it2.hasNext()) {
            Class<?> returnType = ((Method) it2.next()).getReturnType();
            za.k.d(returnType, "it");
            Class<?> g6 = reflectClassUtil.g(returnType);
            if (g6 != null) {
                returnType = g6;
            }
            arrayList2.add(returnType);
        }
        this.f14238f = arrayList2;
        List<Method> list4 = this.f14236d;
        u11 = s.u(list4, 10);
        ArrayList arrayList3 = new ArrayList(u11);
        Iterator<T> it3 = list4.iterator();
        while (it3.hasNext()) {
            arrayList3.add(((Method) it3.next()).getDefaultValue());
        }
        this.f14239g = arrayList3;
        if (this.f14235c == EnumC0070a.POSITIONAL_CALL && bVar == b.JAVA) {
            j02 = _Collections.j0(this.f14234b, ThermalBaseConfig.Item.ATTR_VALUE);
            if (!j02.isEmpty()) {
                throw new UnsupportedOperationException("Positional call of a Java annotation constructor is allowed only if there are no parameters or one parameter named \"value\". This restriction exists because Java annotations (in contrast to Kotlin)do not impose any order on their arguments. Use KCallable#callBy instead.");
            }
        }
    }

    @Override // kb.e
    public List<Type> a() {
        return this.f14237e;
    }

    @Override // kb.e
    public /* bridge */ /* synthetic */ Member b() {
        return (Member) e();
    }

    public void c(Object[] objArr) {
        e.a.a(this, objArr);
    }

    @Override // kb.e
    public Object d(Object[] objArr) {
        List G0;
        Map q10;
        za.k.e(objArr, Constants.MessagerConstants.ARGS_KEY);
        c(objArr);
        ArrayList arrayList = new ArrayList(objArr.length);
        int length = objArr.length;
        int i10 = 0;
        int i11 = 0;
        while (i10 < length) {
            Object obj = objArr[i10];
            int i12 = i11 + 1;
            Object k10 = (obj == null && this.f14235c == EnumC0070a.CALL_BY_NAME) ? this.f14239g.get(i11) : c.k(obj, this.f14238f.get(i11));
            if (k10 != null) {
                arrayList.add(k10);
                i10++;
                i11 = i12;
            } else {
                c.j(i11, this.f14234b.get(i11), this.f14238f.get(i11));
                throw null;
            }
        }
        Class<?> cls = this.f14233a;
        G0 = _Collections.G0(this.f14234b, arrayList);
        q10 = m0.q(G0);
        return c.d(cls, q10, this.f14236d);
    }

    public Void e() {
        return null;
    }

    @Override // kb.e
    public Type f() {
        return this.f14233a;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ a(Class cls, List list, EnumC0070a enumC0070a, b bVar, List list2, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(cls, list, enumC0070a, bVar, list2);
        int u7;
        if ((i10 & 16) != 0) {
            u7 = s.u(list, 10);
            list2 = new ArrayList(u7);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                list2.add(cls.getDeclaredMethod((String) it.next(), new Class[0]));
            }
        }
    }
}
