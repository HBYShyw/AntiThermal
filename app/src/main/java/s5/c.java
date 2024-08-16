package s5;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.lang.reflect.Method;
import java.util.Arrays;
import kotlin.Metadata;
import za.k;

/* compiled from: ReflectUtil.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0006\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\u0018\u0010\u0004\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00032\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0002J;\u0010\n\u001a\u0004\u0018\u00010\u00012\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\u0007\u001a\u00020\u00062\u0016\u0010\t\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00010\b\"\u0004\u0018\u00010\u0001¢\u0006\u0004\b\n\u0010\u000b¨\u0006\u000e"}, d2 = {"Ls5/c;", "", ThermalBaseConfig.Item.ATTR_VALUE, "Ljava/lang/Class;", "a", "clz", "", "methodName", "", "parameters", "b", "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    public static final c f18068a = new c();

    private c() {
    }

    private final Class<?> a(Object value) {
        if (value instanceof Integer) {
            return Integer.TYPE;
        }
        if (value instanceof Long) {
            return Long.TYPE;
        }
        if (value instanceof Float) {
            return Float.TYPE;
        }
        if (value instanceof Boolean) {
            return Boolean.TYPE;
        }
        if (value instanceof Double) {
            return Double.TYPE;
        }
        if (value instanceof Object[]) {
            return String[].class;
        }
        if (value instanceof int[]) {
            return int[].class;
        }
        if (value instanceof long[]) {
            return long[].class;
        }
        if (value instanceof float[]) {
            return float[].class;
        }
        if (value instanceof double[]) {
            return double[].class;
        }
        if (value instanceof boolean[]) {
            return boolean[].class;
        }
        if (value instanceof Context) {
            return Context.class;
        }
        if (value instanceof Drawable) {
            return Drawable.class;
        }
        return String.class;
    }

    public final Object b(Class<?> clz, String methodName, Object... parameters) {
        k.e(clz, "clz");
        k.e(methodName, "methodName");
        k.e(parameters, "parameters");
        try {
            int length = parameters.length;
            Class[] clsArr = new Class[length];
            for (int i10 = 0; i10 < length; i10++) {
                clsArr[i10] = a(parameters[i10]);
            }
            Method declaredMethod = clz.getDeclaredMethod(methodName, (Class[]) Arrays.copyOf(clsArr, length));
            if (declaredMethod != null) {
                return declaredMethod.invoke(null, Arrays.copyOf(parameters, parameters.length));
            }
            return null;
        } catch (Exception e10) {
            e10.printStackTrace();
            return null;
        }
    }
}
