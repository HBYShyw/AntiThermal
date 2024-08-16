package android.app;

import android.text.TextUtils;
import android.util.ArrayMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/* loaded from: classes.dex */
public class AppDynamicalLogEnabler {
    private final ArrayMap<String, Field> mCaches = new ArrayMap<>();

    protected AppDynamicalLogEnabler() {
    }

    public static AppDynamicalLogEnabler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private String generateKey(String className, String fieldName) {
        return className + "_" + fieldName;
    }

    private Field getField(String className, String fieldName) {
        String key = generateKey(className, fieldName);
        Field cache = this.mCaches.get(key);
        if (cache == null) {
            try {
                cache = Class.forName(className).getDeclaredField(fieldName);
                cache.setAccessible(true);
                this.mCaches.put(key, cache);
                return cache;
            } catch (Exception e) {
                e.printStackTrace();
                return cache;
            }
        }
        return cache;
    }

    protected final void setDynamicalLogEnable(boolean on, String className, String... fieldNames) {
        if (className == null || fieldNames == null) {
            return;
        }
        try {
            for (String fieldName : fieldNames) {
                Field field = getField(className, fieldName);
                if (field != null && Modifier.isStatic(field.getModifiers())) {
                    field.setBoolean(null, on);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDynamicalLogConfig(List<String> configs) {
        closeAllDebugLog();
        if (configs == null) {
            return;
        }
        for (String item : configs) {
            ConfigItem configItem = ConfigItem.fromSerializeString(item);
            if (configItem != null) {
                setDynamicalLogEnable(true, configItem.getClazzName(), configItem.getFields());
            }
        }
    }

    public void closeAllDebugLog() {
        try {
            for (Field field : this.mCaches.values()) {
                if (field != null && Modifier.isStatic(field.getModifiers())) {
                    field.setBoolean(null, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes.dex */
    public static class ConfigItem {
        private String mClazzName;
        private String mFieldNames;

        public ConfigItem(String clsName, String fieldNames) {
            this.mClazzName = clsName;
            this.mFieldNames = fieldNames;
        }

        public static ConfigItem fromSerializeString(String config) {
            if (TextUtils.isEmpty(config)) {
                return null;
            }
            String[] ret = config.split(":");
            if (ret.length > 1) {
                return new ConfigItem(ret[0], ret[1]);
            }
            return null;
        }

        public String toSerializeString() {
            return this.mClazzName + ":" + this.mFieldNames;
        }

        public String getClazzName() {
            return this.mClazzName;
        }

        public String[] getFields() {
            if (!TextUtils.isEmpty(this.mFieldNames)) {
                return this.mFieldNames.split(",");
            }
            return null;
        }

        public void addFields(String fields) {
            if (!TextUtils.isEmpty(this.mFieldNames)) {
                this.mFieldNames = this.mFieldNames.concat(",").concat(fields);
            } else {
                this.mFieldNames = fields;
            }
        }

        public String toString() {
            return "ConfigItem{mClazzName='" + this.mClazzName + "', mFieldNames=" + this.mFieldNames + '}';
        }
    }

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final AppDynamicalLogEnabler INSTANCE = new AppDynamicalLogEnabler();

        private LazyHolder() {
        }
    }
}
