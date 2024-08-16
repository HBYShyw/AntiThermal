package jb;

import java.lang.reflect.Method;
import kotlin.collections._Arrays;
import vb.reflectClassUtil;
import za.Lambda;

/* compiled from: RuntimeTypeMapper.kt */
/* loaded from: classes2.dex */
public final class m0 {

    /* compiled from: RuntimeTypeMapper.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<Class<?>, CharSequence> {

        /* renamed from: e */
        public static final a f13290e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a */
        public final CharSequence invoke(Class<?> cls) {
            za.k.d(cls, "it");
            return reflectClassUtil.b(cls);
        }
    }

    public static final /* synthetic */ String a(Method method) {
        return b(method);
    }

    public static final String b(Method method) {
        String O;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        za.k.d(parameterTypes, "parameterTypes");
        O = _Arrays.O(parameterTypes, "", "(", ")", 0, null, a.f13290e, 24, null);
        sb2.append(O);
        Class<?> returnType = method.getReturnType();
        za.k.d(returnType, "returnType");
        sb2.append(reflectClassUtil.b(returnType));
        return sb2.toString();
    }
}
