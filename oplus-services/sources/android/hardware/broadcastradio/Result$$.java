package android.hardware.broadcastradio;

import java.lang.reflect.Array;
import java.util.StringJoiner;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface Result$$ {
    static String toString(int i) {
        return i == 0 ? "OK" : i == 1 ? "INTERNAL_ERROR" : i == 2 ? "INVALID_ARGUMENTS" : i == 3 ? "INVALID_STATE" : i == 4 ? "NOT_SUPPORTED" : i == 5 ? "TIMEOUT" : i == 6 ? "CANCELED" : i == 7 ? "UNKNOWN_ERROR" : Integer.toString(i);
    }

    static String arrayToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        Class<?> cls = obj.getClass();
        if (!cls.isArray()) {
            throw new IllegalArgumentException("not an array: " + obj);
        }
        Class<?> componentType = cls.getComponentType();
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        int i = 0;
        if (componentType.isArray()) {
            while (i < Array.getLength(obj)) {
                stringJoiner.add(arrayToString(Array.get(obj, i)));
                i++;
            }
        } else {
            if (cls != int[].class) {
                throw new IllegalArgumentException("wrong type: " + cls);
            }
            int[] iArr = (int[]) obj;
            int length = iArr.length;
            while (i < length) {
                stringJoiner.add(toString(iArr[i]));
                i++;
            }
        }
        return stringJoiner.toString();
    }
}
