package ub;

import com.oplus.backup.sdk.common.utils.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import vb.reflectClassUtil;

/* compiled from: ReflectKotlinClass.kt */
/* loaded from: classes2.dex */
final class m {

    /* renamed from: a, reason: collision with root package name */
    public static final m f18986a = new m();

    private m() {
    }

    public final String a(Constructor<?> constructor) {
        za.k.e(constructor, "constructor");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("(");
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        za.k.d(parameterTypes, "constructor.parameterTypes");
        for (Class<?> cls : parameterTypes) {
            za.k.d(cls, "parameterType");
            sb2.append(reflectClassUtil.b(cls));
        }
        sb2.append(")V");
        String sb3 = sb2.toString();
        za.k.d(sb3, "sb.toString()");
        return sb3;
    }

    public final String b(Field field) {
        za.k.e(field, "field");
        Class<?> type = field.getType();
        za.k.d(type, "field.type");
        return reflectClassUtil.b(type);
    }

    public final String c(Method method) {
        za.k.e(method, Constants.MessagerConstants.METHOD_KEY);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        za.k.d(parameterTypes, "method.parameterTypes");
        for (Class<?> cls : parameterTypes) {
            za.k.d(cls, "parameterType");
            sb2.append(reflectClassUtil.b(cls));
        }
        sb2.append(")");
        Class<?> returnType = method.getReturnType();
        za.k.d(returnType, "method.returnType");
        sb2.append(reflectClassUtil.b(returnType));
        String sb3 = sb2.toString();
        za.k.d(sb3, "sb.toString()");
        return sb3;
    }
}
