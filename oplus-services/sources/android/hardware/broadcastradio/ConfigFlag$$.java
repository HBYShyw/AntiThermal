package android.hardware.broadcastradio;

import java.lang.reflect.Array;
import java.util.StringJoiner;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ConfigFlag$$ {
    static String toString(int i) {
        return i == 1 ? "FORCE_MONO" : i == 2 ? "FORCE_ANALOG" : i == 3 ? "FORCE_DIGITAL" : i == 4 ? "RDS_AF" : i == 5 ? "RDS_REG" : i == 6 ? "DAB_DAB_LINKING" : i == 7 ? "DAB_FM_LINKING" : i == 8 ? "DAB_DAB_SOFT_LINKING" : i == 9 ? "DAB_FM_SOFT_LINKING" : Integer.toString(i);
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
