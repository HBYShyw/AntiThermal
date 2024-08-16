package android.os;

import android.util.Log;
import com.oplus.annotation.OplusProperty;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OplusSystemProperties {
    private static final String TAG = "OplusSystemProperties";
    private ArrayList<String> mOplusPropertyPersistList;
    private ArrayList<String> mOplusPropertyReadOnlyList;
    private ArrayList<String> mOplusPropertySysList;

    private OplusSystemProperties() {
        this.mOplusPropertyReadOnlyList = new ArrayList<>();
        this.mOplusPropertyPersistList = new ArrayList<>();
        this.mOplusPropertySysList = new ArrayList<>();
        if (Build.IS_DEBUGGABLE) {
            initOplusSystemPropertiesList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InstanceHolder {
        static OplusSystemProperties INSTANCE = new OplusSystemProperties();

        private InstanceHolder() {
        }
    }

    public static OplusSystemProperties getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private void initOplusSystemPropertiesList() {
        try {
            Field[] fields = OplusPropertyList.class.getDeclaredFields();
            for (Field field : fields) {
                boolean isAnnotation = field.isAnnotationPresent(OplusProperty.class);
                if (isAnnotation) {
                    String propertyName = (String) field.get(null);
                    Log.d(TAG, "load prop:" + propertyName);
                    OplusProperty oplusPropertyAno = field.getDeclaredAnnotation(OplusProperty.class);
                    if (oplusPropertyAno != null) {
                        switch (AnonymousClass1.$SwitchMap$com$oplus$annotation$OplusProperty$OplusPropertyType[oplusPropertyAno.value().ordinal()]) {
                            case 1:
                                this.mOplusPropertyReadOnlyList.add(propertyName);
                                break;
                            case 2:
                                this.mOplusPropertyPersistList.add(propertyName);
                                break;
                            case 3:
                                this.mOplusPropertySysList.add(propertyName);
                                break;
                            default:
                                Log.w(TAG, "Unknown type:" + oplusPropertyAno.value());
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "initOplusSystemPropertiesList failed.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.os.OplusSystemProperties$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$oplus$annotation$OplusProperty$OplusPropertyType;

        static {
            int[] iArr = new int[OplusProperty.OplusPropertyType.values().length];
            $SwitchMap$com$oplus$annotation$OplusProperty$OplusPropertyType = iArr;
            try {
                iArr[OplusProperty.OplusPropertyType.RO_PROPERTY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$oplus$annotation$OplusProperty$OplusPropertyType[OplusProperty.OplusPropertyType.PERSIST_PROPERTY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$oplus$annotation$OplusProperty$OplusPropertyType[OplusProperty.OplusPropertyType.SYS_PROPERTY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private boolean isPredefinedProperty(String propertyName) {
        if (propertyName == null) {
            return false;
        }
        if (!this.mOplusPropertyReadOnlyList.contains(propertyName) && !this.mOplusPropertyPersistList.contains(propertyName) && !this.mOplusPropertySysList.contains(propertyName)) {
            return false;
        }
        return true;
    }

    private static boolean isPredefinedOplusProperty(String propertyName) {
        if (Build.IS_DEBUGGABLE) {
            return getInstance().isPredefinedProperty(propertyName);
        }
        return true;
    }

    public static String get(String key) {
        if (!isPredefinedOplusProperty(key)) {
            Log.e(TAG, "Warning: This property is not predefined, prop:" + key);
            throw new IllegalArgumentException("Warning: This property is not predefined, prop:" + key);
        }
        return SystemProperties.get(key);
    }

    public static String get(String key, String def) {
        if (!isPredefinedOplusProperty(key)) {
            Log.e(TAG, "Warning: This property is not predefined, prop:" + key);
            throw new IllegalArgumentException("Warning: This property is not predefined, prop:" + key);
        }
        return SystemProperties.get(key, def);
    }

    public static int getInt(String key, int def) {
        if (!isPredefinedOplusProperty(key)) {
            Log.e(TAG, "Warning: This property is not predefined, prop:" + key);
            throw new IllegalArgumentException("Warning: This property is not predefined, prop:" + key);
        }
        return SystemProperties.getInt(key, def);
    }

    public static long getLong(String key, long def) {
        if (!isPredefinedOplusProperty(key)) {
            Log.e(TAG, "Warning: This property is not predefined, prop:" + key);
            throw new IllegalArgumentException("Warning: This property is not predefined, prop:" + key);
        }
        return SystemProperties.getLong(key, def);
    }

    public static boolean getBoolean(String key, boolean def) {
        if (!isPredefinedOplusProperty(key)) {
            Log.e(TAG, "Warning: This property is not predefined, prop:" + key);
            throw new IllegalArgumentException("Warning: This property is not predefined, prop:" + key);
        }
        return SystemProperties.getBoolean(key, def);
    }

    public static void set(String key, String val) {
        if (!isPredefinedOplusProperty(key)) {
            Log.e(TAG, "Warning: This property is not predefined, prop:" + key);
            throw new IllegalArgumentException("Warning: This property is not predefined, prop:" + key);
        }
        SystemProperties.set(key, val);
    }

    public void dumpOplusProperty(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (!Build.IS_DEBUGGABLE) {
            pw.println("release version, unsupported");
            return;
        }
        pw.println("RO properties:");
        Iterator<String> it = this.mOplusPropertyReadOnlyList.iterator();
        while (it.hasNext()) {
            String propRo = it.next();
            pw.println("Ro: " + propRo);
        }
        pw.println("Persist properties:");
        Iterator<String> it2 = this.mOplusPropertyPersistList.iterator();
        while (it2.hasNext()) {
            String propPersist = it2.next();
            pw.println("Persist: " + propPersist);
        }
        pw.println("Sys properties:");
        Iterator<String> it3 = this.mOplusPropertySysList.iterator();
        while (it3.hasNext()) {
            String propSys = it3.next();
            pw.println("Sys: " + propSys);
        }
    }
}
