package android.hardware.broadcastradio;

import java.lang.reflect.Array;
import java.util.StringJoiner;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface AnnouncementType$$ {
    static String toString(byte b) {
        return b == 0 ? "INVALID" : b == 1 ? "EMERGENCY" : b == 2 ? "WARNING" : b == 3 ? "TRAFFIC" : b == 4 ? "WEATHER" : b == 5 ? "NEWS" : b == 6 ? "EVENT" : b == 7 ? "SPORT" : b == 8 ? "MISC" : Byte.toString(b);
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
            if (cls != byte[].class) {
                throw new IllegalArgumentException("wrong type: " + cls);
            }
            byte[] bArr = (byte[]) obj;
            int length = bArr.length;
            while (i < length) {
                stringJoiner.add(toString(bArr[i]));
                i++;
            }
        }
        return stringJoiner.toString();
    }
}
