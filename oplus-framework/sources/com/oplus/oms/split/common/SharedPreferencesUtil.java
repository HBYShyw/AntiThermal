package com.oplus.oms.split.common;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Map;

/* loaded from: classes.dex */
public class SharedPreferencesUtil {
    private static final String SP_NAME = "oms";
    private static final String TAG = "SharedPreferencesUtil";
    private static volatile SharedPreferencesUtil mInstance;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;

    private SharedPreferencesUtil(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("oms", 0);
            this.mSharedPreferences = sharedPreferences;
            this.mEditor = sharedPreferences.edit();
        }
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (mInstance == null && context != null) {
                    mInstance = new SharedPreferencesUtil(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public void put(String key, Object object) {
        SharedPreferences.Editor editor = this.mEditor;
        if (editor == null) {
            return;
        }
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, ((Integer) object).intValue());
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) object).booleanValue());
        } else if (object instanceof Float) {
            editor.putFloat(key, ((Float) object).floatValue());
        } else if (object instanceof Long) {
            editor.putLong(key, ((Long) object).longValue());
        } else {
            SplitLog.w(TAG, " put key - value: value type error,please check", new Object[0]);
        }
        this.mEditor.apply();
    }

    public void put(Map<String, Object> map) {
        if (map == null || this.mEditor == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                this.mEditor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                this.mEditor.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Boolean) {
                this.mEditor.putBoolean(key, ((Boolean) value).booleanValue());
            } else if (value instanceof Float) {
                this.mEditor.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Long) {
                this.mEditor.putLong(key, ((Long) value).longValue());
            } else {
                SplitLog.w(TAG, " put map value type error,please check", new Object[0]);
            }
        }
        this.mEditor.apply();
    }

    public Object get(String key, Object defaultObject) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            return null;
        }
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        }
        if (defaultObject instanceof Integer) {
            return Integer.valueOf(sharedPreferences.getInt(key, ((Integer) defaultObject).intValue()));
        }
        if (defaultObject instanceof Boolean) {
            return Boolean.valueOf(sharedPreferences.getBoolean(key, ((Boolean) defaultObject).booleanValue()));
        }
        if (defaultObject instanceof Float) {
            return Float.valueOf(sharedPreferences.getFloat(key, ((Float) defaultObject).floatValue()));
        }
        if (defaultObject instanceof Long) {
            return Long.valueOf(sharedPreferences.getLong(key, ((Long) defaultObject).longValue()));
        }
        SplitLog.w(TAG, " get defaultObject type error,please check", new Object[0]);
        return null;
    }

    public void removeAll() {
        SharedPreferences.Editor editor = this.mEditor;
        if (editor != null) {
            editor.clear();
            this.mEditor.apply();
        }
    }

    public boolean contains(String key) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            return false;
        }
        return sharedPreferences.contains(key);
    }

    public Map<String, ?> getAll() {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getAll();
    }

    public void remove(String key) {
        if (contains(key)) {
            this.mEditor.remove(key);
            this.mEditor.apply();
        }
    }
}
