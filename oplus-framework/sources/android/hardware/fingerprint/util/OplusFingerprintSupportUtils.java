package android.hardware.fingerprint.util;

import android.os.SystemProperties;
import android.util.Log;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class OplusFingerprintSupportUtils implements IOplusFingerprintSupportUtilsExt {
    private static final String PROP_SENSOR_TPYE = "persist.vendor.fingerprint.sensor_type";
    private static final String SENSOR_TYPE_BACK = "back";
    private static final String SENSOR_TYPE_FRONT = "front";
    private static final String SENSOR_TYPE_OPTICAL = "optical";
    private static final String SENSOR_TYPE_SIDE = "side";
    private static final String SENSOR_TYPE_UNKNOW = "unknow";
    private static String TAG = "Biometrics/Fingerprint/OplusFingerprintSupportUtil";
    private static String mSensorType;

    public OplusFingerprintSupportUtils(Object base) {
    }

    public int getFingerprintSensorType() {
        return getSupportSensorType();
    }

    public static boolean isSupportedFingerprintShutter() {
        return OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_FINGERPRINT_SHUTTER);
    }

    public static int getSupportSensorType() {
        mSensorType = SystemProperties.get(PROP_SENSOR_TPYE, SENSOR_TYPE_UNKNOW);
        Log.d(TAG, "getSupportSensorType mSensorType:" + mSensorType);
        if (SENSOR_TYPE_OPTICAL.equals(mSensorType)) {
            return 3;
        }
        if (SENSOR_TYPE_SIDE.equals(mSensorType)) {
            return 4;
        }
        if (SENSOR_TYPE_FRONT.equals(mSensorType)) {
            return 5;
        }
        if (SENSOR_TYPE_BACK.equals(mSensorType)) {
            return 1;
        }
        return 0;
    }

    public static Object getDeclaredField(Object target, String clsName, String fieldName) {
        try {
            Class<?> cls = Class.forName(clsName);
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object result = field.get(target);
            return result;
        } catch (Exception e) {
            Log.i(TAG, "getDeclaredField exception caught : " + e.getMessage());
            return null;
        }
    }

    public static void setDeclaredField(Object target, String clsName, String fieldName, Object value) {
        try {
            Class<?> cls = Class.forName(clsName);
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            Log.i(TAG, "setDeclaredField exception caught : " + e.getMessage());
        }
    }

    public static Object invokeDeclaredMethod(Object target, String clsName, String methodName, Class[] parameterTypes, Object[] args) {
        try {
            Class cls = Class.forName(clsName);
            Method method = cls.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(target, args);
            return result;
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "ClassNotFoundException : " + e.getMessage());
            return null;
        } catch (IllegalAccessException e2) {
            Log.i(TAG, "IllegalAccessException : " + e2.getMessage());
            return null;
        } catch (NoSuchMethodException e3) {
            Log.i(TAG, "NoSuchMethodException : " + e3.getMessage());
            return null;
        } catch (SecurityException e4) {
            Log.i(TAG, "SecurityException : " + e4.getMessage());
            return null;
        } catch (InvocationTargetException e5) {
            Log.i(TAG, "InvocationTargetException : " + e5.getMessage());
            return null;
        } catch (Exception e6) {
            Log.e(TAG, "Exception : " + e6.getMessage());
            return null;
        }
    }
}
