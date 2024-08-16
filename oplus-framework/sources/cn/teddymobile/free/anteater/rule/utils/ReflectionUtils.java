package cn.teddymobile.free.anteater.rule.utils;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import cn.teddymobile.free.anteater.logger.Logger;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ReflectionUtils {
    public static final String REFLECTION_NAME_THIS_OBJECT = "this$0";
    private static final String TAG = ReflectionUtils.class.getSimpleName();
    public static List<String> excludeDeepFirstClassList = new ArrayList();
    public static List<String> excludeDeepFirstPackagePrefixList = new ArrayList();
    public static List<String> excludeFieldClassList = new ArrayList();
    public static List<String> excludeFieldPackagePrefixList = new ArrayList();

    static {
        excludeDeepFirstClassList.add("androidx.appcompat.app.AppCompatActivity");
        excludeDeepFirstClassList.add("androidx.appcompat.app.AppCompatDelegate");
        excludeDeepFirstPackagePrefixList.add("android.");
        excludeDeepFirstPackagePrefixList.add("sun.");
        excludeDeepFirstPackagePrefixList.add("java.");
        excludeFieldClassList.add("androidx.appcompat.app.AppCompatActivity");
        excludeFieldClassList.add("androidx.appcompat.app.AppCompatDelegate");
        excludeFieldPackagePrefixList.add("android.");
        excludeFieldPackagePrefixList.add("sun.");
        excludeFieldPackagePrefixList.add("java.");
    }

    public static void printReflectionResult(Object obj, String filePath) {
        JSONArray jsonArray = deepFirstFields(obj, 0);
        DetectionFileUtils.logFile(jsonArray.toString(), filePath);
    }

    public static JSONArray deepFirstFields(Object obj, int deep) {
        Logger.i(TAG, " -------- deep " + deep + " start -------- ");
        JSONArray jsonArray = new JSONArray();
        if (obj == null) {
            return jsonArray;
        }
        int newDeep = deep + 1;
        try {
            int limitDeep = SystemProperties.getInt("reflection_deep", 5);
            if (newDeep > limitDeep) {
                JSONObject object = new JSONObject();
                object.put("field_class_name", obj.getClass().getCanonicalName());
                object.put("info", "exceed limit " + limitDeep + "levels");
                jsonArray.put(object);
                return jsonArray;
            }
            Class<?> clazz = obj.getClass();
            List<Field> fields = getAllFields(clazz);
            for (Field fieldItem : fields) {
                JSONObject object2 = logField(obj, fieldItem);
                if (object2 != null) {
                    Logger.i(TAG, "filed_object: " + object2.toString());
                    if (SystemProperties.getBoolean("reflection.favorite.store", true)) {
                        jsonArray.put(object2);
                    }
                    if (!fieldItem.getName().equals(REFLECTION_NAME_THIS_OBJECT)) {
                        Class<?> fieldItemClazz = fieldItem.getType();
                        if (!excludePrimitiveType(fieldItemClazz) && !excludeClass(fieldItemClazz.getCanonicalName(), excludeDeepFirstClassList) && !excludePackagePrefix(fieldItemClazz.getCanonicalName(), excludeDeepFirstPackagePrefixList)) {
                            if (SystemProperties.getBoolean("reflection.favorite.store", true)) {
                                object2.put("leaf", deepFirstFields(fieldItem.get(obj), newDeep));
                            } else {
                                deepFirstFields(fieldItem.get(obj), newDeep);
                            }
                        }
                    }
                }
            }
            Logger.i(TAG, " -------- deep " + deep + " end -------- ");
            return jsonArray;
        } catch (Exception e) {
            if (e instanceof IllegalAccessException) {
                Logger.e(TAG, "IllegalAccessException when deepFirstFields");
            } else if (e instanceof JSONException) {
                Logger.e(TAG, "JSONException when deepFirstFields");
            } else {
                e.printStackTrace();
            }
            return jsonArray;
        }
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        while (clazz != null && !Object.class.equals(clazz) && !excludePrimitiveType(clazz) && !excludeClass(clazz.getCanonicalName(), excludeFieldClassList) && !excludePackagePrefix(clazz.getCanonicalName(), excludeFieldPackagePrefixList)) {
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public static JSONObject logField(Object obj, Field field) {
        try {
            JSONObject object = new JSONObject();
            field.setAccessible(true);
            String field_class_name = field.getType().toString();
            object.put("field_class_name", field_class_name);
            if (field_class_name.contains("kotlin.Any")) {
                object.put("field_value", "");
            } else {
                Object value = field.get(obj);
                object.put("field_value", value != null ? value.toString() : "");
            }
            object.put("field_name", field.getName());
            object.put("parent_class_name", obj.getClass().getCanonicalName());
            return object;
        } catch (Exception e) {
            if (e instanceof IllegalAccessException) {
                Logger.e(TAG, "IllegalAccessException when logField");
                return null;
            }
            if (e instanceof JSONException) {
                Logger.e(TAG, "JSONException when logField");
                return null;
            }
            e.printStackTrace();
            return null;
        }
    }

    public static String getGenericType(Object obj, String filedName) {
        try {
            Field field = obj.getClass().getDeclaredField(filedName);
            Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            return type.getTypeName();
        } catch (Exception e) {
            if (e instanceof NoSuchFieldException) {
                Logger.e(TAG, "NoSuchFieldException when getGenericType");
                return "";
            }
            if (e instanceof ClassCastException) {
                Logger.e(TAG, "ClassCastException when getGenericType");
                return "";
            }
            e.printStackTrace();
            return "";
        }
    }

    public static void addExcludeDeepFirstClassList(List<String> exclude) {
        for (String s : exclude) {
            if (!excludeDeepFirstClassList.contains(s)) {
                excludeDeepFirstClassList.add(s);
            }
        }
    }

    public static void addExcludeDeepFirstPackagePrefixList(List<String> exclude) {
        for (String s : exclude) {
            if (!excludeDeepFirstPackagePrefixList.contains(s)) {
                excludeDeepFirstPackagePrefixList.add(s);
            }
        }
    }

    public static void addExcludeFieldClassList(List<String> exclude) {
        for (String s : exclude) {
            if (!excludeFieldClassList.contains(s)) {
                excludeFieldClassList.add(s);
            }
        }
    }

    public static void addExcludeFieldPackagePrefixList(List<String> exclude) {
        for (String s : exclude) {
            if (!excludeFieldPackagePrefixList.contains(s)) {
                excludeFieldPackagePrefixList.add(s);
            }
        }
    }

    public static boolean excludePrimitiveType(Class<?> clazz) {
        return Integer.TYPE.equals(clazz) || Short.TYPE.equals(clazz) || Long.TYPE.equals(clazz) || Float.TYPE.equals(clazz) || Double.TYPE.equals(clazz) || Character.TYPE.equals(clazz) || Boolean.TYPE.equals(clazz) || Byte.TYPE.equals(clazz) || Object.class.equals(clazz) || clazz.isArray() || clazz.isEnum() || String.class.equals(clazz) || Bundle.class.equals(clazz) || List.class.equals(clazz) || Uri.class.equals(clazz);
    }

    public static boolean excludeClass(String canonicalName, List<String> excludeList) {
        if (canonicalName == null) {
            return false;
        }
        for (String canonical : excludeList) {
            if (canonicalName.equals(canonical)) {
                return true;
            }
        }
        return false;
    }

    public static boolean excludePackagePrefix(String canonicalName, List<String> excludeList) {
        if (canonicalName == null) {
            return false;
        }
        for (String canonical : excludeList) {
            if (canonicalName.startsWith(canonical)) {
                return true;
            }
        }
        return false;
    }
}
