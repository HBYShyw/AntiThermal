package cn.teddymobile.free.anteater.rule.attribute.reflection;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.attribute.intent.Attribute;
import cn.teddymobile.free.anteater.rule.utils.RegularExpressionUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ObjectNode {
    private static final String CLASS_NAME_ARBITRARY = "?";
    private static final String JSON_FIELD_ALIAS = "alias";
    private static final String JSON_FIELD_ATTRIBUTE_LIST = "attribute_list";
    private static final String JSON_FIELD_CAPTURE_PATTERN = "capture_pattern";
    private static final String JSON_FIELD_CLASS_NAME = "class_name";
    private static final String JSON_FIELD_CLASS_NAME_OBFUSCATED = "class_name_obfuscated";
    private static final String JSON_FIELD_FIELD_INDEX = "field_index";
    private static final String JSON_FIELD_FIELD_NAME = "field_name";
    private static final String JSON_FIELD_FIELD_NAME_OBFUSCATED = "field_name_obfuscated";
    private static final String JSON_FIELD_LEAF = "leaf";
    private static final String JSON_FIELD_PARENT_FIELD_COUNT = "parent_field_count";
    private static final String TAG = ObjectNode.class.getSimpleName();
    private static final String TAG_ARRAY_FOREACH = "foreach";
    private static final String TAG_CONTEXT = "context";
    private static final String TAG_MAP = "map";
    private static final String TAG_ON_CLICK_LISTENER = "onClickListener";
    private static final String TAG_SUPER_CLASS = "super";
    private final String mAlias;
    private final List<Attribute> mAttributeList;
    private final String mCapturePattern;
    private final String mClassName;
    private final boolean mClassNameArbitrary;
    private final boolean mClassNameObfuscated;
    private final int mFieldIndex;
    private final String mFieldName;
    private final boolean mFieldNameObfuscated;
    private final List<ObjectNode> mLeafList;
    private final int mParentFieldCount;

    public ObjectNode(JSONObject nodeObject) throws JSONException {
        this.mFieldName = nodeObject.getString(JSON_FIELD_FIELD_NAME);
        String string = nodeObject.getString(JSON_FIELD_CLASS_NAME);
        this.mClassName = string;
        this.mFieldIndex = nodeObject.optInt(JSON_FIELD_FIELD_INDEX, -1);
        this.mParentFieldCount = nodeObject.optInt(JSON_FIELD_PARENT_FIELD_COUNT, -1);
        this.mFieldNameObfuscated = nodeObject.optBoolean(JSON_FIELD_FIELD_NAME_OBFUSCATED, false);
        this.mClassNameObfuscated = nodeObject.optBoolean(JSON_FIELD_CLASS_NAME_OBFUSCATED, false);
        this.mClassNameArbitrary = string.equals(CLASS_NAME_ARBITRARY);
        this.mLeafList = new ArrayList();
        JSONArray leafArray = nodeObject.optJSONArray(JSON_FIELD_LEAF);
        if (leafArray != null) {
            for (int i = 0; i < leafArray.length(); i++) {
                this.mLeafList.add(new ObjectNode(leafArray.getJSONObject(i)));
            }
        }
        this.mAttributeList = new ArrayList();
        JSONArray attributeArray = nodeObject.optJSONArray(JSON_FIELD_ATTRIBUTE_LIST);
        if (attributeArray != null) {
            for (int i2 = 0; i2 < attributeArray.length(); i2++) {
                this.mAttributeList.add(new Attribute(attributeArray.getJSONObject(i2)));
            }
        }
        this.mAlias = nodeObject.optString(JSON_FIELD_ALIAS, null);
        this.mCapturePattern = nodeObject.optString(JSON_FIELD_CAPTURE_PATTERN, null);
    }

    public String toString() {
        return "FieldName = " + this.mFieldName + "\nClassName = " + this.mClassName + "\nFieldIndex = " + this.mFieldIndex + "\nParentFieldCount = " + this.mParentFieldCount + "\nFieldNameObfuscated = " + this.mFieldNameObfuscated + "\nClassNameObfuscated = " + this.mClassNameObfuscated + "\nAlias = " + this.mAlias;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x073d  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x063d A[Catch: IllegalAccessException | NoSuchFieldException -> 0x06e7, TryCatch #3 {IllegalAccessException | NoSuchFieldException -> 0x06e7, blocks: (B:325:0x05be, B:327:0x05c7, B:329:0x05d9, B:331:0x05e2, B:343:0x05f6, B:344:0x0632, B:348:0x0638, B:350:0x063d, B:352:0x0641, B:354:0x0644, B:355:0x0659, B:356:0x0698, B:357:0x0619, B:358:0x0622), top: B:324:0x05be }] */
    /* JADX WARN: Removed duplicated region for block: B:356:0x0698 A[Catch: IllegalAccessException | NoSuchFieldException -> 0x06e7, TRY_LEAVE, TryCatch #3 {IllegalAccessException | NoSuchFieldException -> 0x06e7, blocks: (B:325:0x05be, B:327:0x05c7, B:329:0x05d9, B:331:0x05e2, B:343:0x05f6, B:344:0x0632, B:348:0x0638, B:350:0x063d, B:352:0x0641, B:354:0x0644, B:355:0x0659, B:356:0x0698, B:357:0x0619, B:358:0x0622), top: B:324:0x05be }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x07c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Pair<String, Object> extractAttribute(Object object, Class<?> clazz) throws JSONException {
        ReflectiveOperationException e;
        List<Pair<Object, Class<?>>> recursiveList;
        Object attribute;
        String str;
        Object attribute2;
        String str2;
        ReflectiveOperationException e2;
        if (object != null && clazz != null) {
            String str3 = TAG;
            Logger.i(str3, "ExtractAttribute for Class " + clazz.getName() + " Expected field = " + this.mFieldName);
            List<Pair<Object, Class<?>>> recursiveList2 = new ArrayList<>();
            try {
                String str4 = "Actual = ";
                if (isCustomTag(this.mFieldName)) {
                    String str5 = this.mFieldName;
                    String tag = str5.substring(1, str5.length() - 1);
                    if (tag.equals(TAG_SUPER_CLASS)) {
                        Class<?> superClass = clazz.getSuperclass();
                        if (superClass != null) {
                            if (!this.mClassNameObfuscated && !this.mClassNameArbitrary) {
                                if (superClass.getName().equals(this.mClassName)) {
                                    recursiveList2.add(new Pair<>(object, superClass));
                                } else {
                                    Logger.w(str3, "Super class is incorrect.");
                                    Logger.w(str3, "Current = " + clazz.getName());
                                    Logger.w(str3, "Expected = " + this.mClassName);
                                    Logger.w(str3, "Actual = " + superClass.getName());
                                }
                            }
                            recursiveList2.add(new Pair<>(object, superClass));
                        } else {
                            Logger.w(str3, "Super class is null.");
                            Logger.w(str3, "Current = " + clazz.getName());
                            Logger.w(str3, "Expected = " + this.mClassName);
                        }
                    } else if (tag.equals(TAG_ARRAY_FOREACH)) {
                        if (!clazz.isArray() && !(object instanceof ArrayList)) {
                            Logger.w(str3, "Current class is not an Array.");
                            Logger.w(str3, "Current = " + clazz.getName());
                        }
                        int size = 0;
                        if (clazz.isArray()) {
                            size = Array.getLength(object);
                        }
                        if (object instanceof ArrayList) {
                            size = ((ArrayList) object).size();
                        }
                        for (int i = 0; i < size; i++) {
                            Object arrayItem = null;
                            if (clazz.isArray()) {
                                arrayItem = Array.get(object, i);
                            }
                            if (object instanceof ArrayList) {
                                arrayItem = ((ArrayList) object).get(i);
                            }
                            if (arrayItem != null) {
                                if (!this.mClassNameObfuscated && !this.mClassNameArbitrary) {
                                    if (arrayItem.getClass().getName().equals(this.mClassName)) {
                                        recursiveList2.add(new Pair<>(arrayItem, arrayItem.getClass()));
                                    } else {
                                        String str6 = TAG;
                                        Logger.w(str6, "ArrayItem class name is incorrect.");
                                        Logger.w(str6, "Current = " + clazz.getName());
                                        Logger.w(str6, "Expected = " + this.mClassName);
                                        Logger.w(str6, "Actual = " + arrayItem.getClass().getName());
                                    }
                                }
                                recursiveList2.add(new Pair<>(arrayItem, arrayItem.getClass()));
                            }
                        }
                    } else if (TextUtils.isDigitsOnly(tag)) {
                        if (!clazz.isArray() && !(object instanceof ArrayList)) {
                            Logger.w(str3, "Current class is not an Array.");
                            Logger.w(str3, "Current = " + clazz.getName());
                        }
                        int arrayIndex = stringToInt(tag);
                        if (arrayIndex >= 0 && arrayIndex < Array.getLength(object)) {
                            Object arrayItem2 = null;
                            if (clazz.isArray()) {
                                arrayItem2 = Array.get(object, arrayIndex);
                            }
                            if (object instanceof ArrayList) {
                                arrayItem2 = ((ArrayList) object).get(arrayIndex);
                            }
                            if (arrayItem2 != null) {
                                if (!this.mClassNameObfuscated && !this.mClassNameArbitrary) {
                                    if (arrayItem2.getClass().getName().equals(this.mClassName)) {
                                        recursiveList2.add(new Pair<>(arrayItem2, arrayItem2.getClass()));
                                    } else {
                                        Logger.w(str3, "ArrayItem class name is incorrect.");
                                        Logger.w(str3, "Current = " + clazz.getName());
                                        Logger.w(str3, "Expected = " + this.mClassName);
                                        Logger.w(str3, "Actual = " + arrayItem2.getClass().getName());
                                    }
                                }
                                recursiveList2.add(new Pair<>(arrayItem2, arrayItem2.getClass()));
                            }
                        } else {
                            Logger.w(str3, "Array index is out of bounds.");
                            Logger.w(str3, "Current = " + clazz.getName());
                            Logger.w(str3, "Index = " + arrayIndex + "/" + Array.getLength(object));
                        }
                    } else if (tag.equals(TAG_CONTEXT)) {
                        Field contextField = View.class.getDeclaredField("mContext");
                        contextField.setAccessible(true);
                        Object contextObject = contextField.get(object);
                        if (contextObject instanceof Context) {
                            if ((contextObject instanceof ContextWrapper) && !(contextObject instanceof Activity)) {
                                ContextWrapper wrapper = (ContextWrapper) contextObject;
                                contextObject = wrapper.getBaseContext();
                            }
                            if (!this.mClassNameObfuscated && !this.mClassNameArbitrary) {
                                if (contextObject.getClass().getName().equals(this.mClassName)) {
                                    recursiveList2.add(new Pair<>(contextObject, contextObject.getClass()));
                                } else {
                                    Logger.w(str3, "Field class name is incorrect.");
                                    Logger.w(str3, "Current = " + clazz.getName());
                                    Logger.w(str3, "Expected = " + this.mClassName);
                                    Logger.w(str3, "Actual = " + contextObject.getClass().getName());
                                }
                            }
                            recursiveList2.add(new Pair<>(contextObject, contextObject.getClass()));
                        }
                    } else if (tag.equals(TAG_ON_CLICK_LISTENER)) {
                        Field listenerInfoField = View.class.getDeclaredField("mListenerInfo");
                        listenerInfoField.setAccessible(true);
                        Object listenerInfo = listenerInfoField.get(object);
                        if (listenerInfo != null) {
                            Field onClickListenerField = listenerInfo.getClass().getDeclaredField("mOnClickListener");
                            onClickListenerField.setAccessible(true);
                            Object onClickListener = onClickListenerField.get(listenerInfo);
                            if (onClickListener != null) {
                                if (!this.mClassNameObfuscated && !this.mClassNameArbitrary) {
                                    if (onClickListener.getClass().getName().equals(this.mClassName)) {
                                        recursiveList2.add(new Pair<>(onClickListener, onClickListener.getClass()));
                                    } else {
                                        Logger.w(str3, "Field class name is incorrect.");
                                        Logger.w(str3, "Current = " + clazz.getName());
                                        Logger.w(str3, "Expected = " + this.mClassName);
                                        Logger.w(str3, "Actual = " + onClickListener.getClass().getName());
                                    }
                                }
                                recursiveList2.add(new Pair<>(onClickListener, onClickListener.getClass()));
                            }
                        }
                    } else if (tag.startsWith(TAG_MAP)) {
                        String keyString = tag.substring(tag.indexOf(":") + 1);
                        if (object instanceof Map) {
                            Map map = (Map) object;
                            Object value = null;
                            Iterator it = map.keySet().iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                Object key = it.next();
                                if (key.toString().equals(keyString)) {
                                    value = map.get(key);
                                    break;
                                }
                            }
                            if (value != null) {
                                if (!this.mClassNameObfuscated && !this.mClassNameArbitrary) {
                                    if (value.getClass().getName().equals(this.mClassName)) {
                                        recursiveList2.add(new Pair<>(value, value.getClass()));
                                    } else {
                                        String str7 = TAG;
                                        Logger.w(str7, "Field class name is incorrect.");
                                        Logger.w(str7, "Current = " + clazz.getName());
                                        Logger.w(str7, "Expected = " + this.mClassName);
                                        Logger.w(str7, "Actual = " + value.getClass().getName());
                                    }
                                }
                                recursiveList2.add(new Pair<>(value, value.getClass()));
                            }
                        } else {
                            Logger.w(str3, "Field is not a Map");
                            Logger.w(str3, "Current = " + clazz.getName());
                        }
                    } else {
                        Logger.w(str3, "Unknown tag " + tag + ".");
                        Logger.w(str3, "Current = " + clazz.getName());
                    }
                } else {
                    Field[] allFields = clazz.getDeclaredFields();
                    if (this.mFieldNameObfuscated) {
                        try {
                            if (this.mClassNameObfuscated) {
                                recursiveList = recursiveList2;
                                attribute = null;
                                str = "Actual = ";
                            } else {
                                Logger.i(str3, "Class name is not obfuscated.");
                                int matchCount = 0;
                                Field matchField = null;
                                Object matchAttribute = null;
                                int length = allFields.length;
                                attribute = null;
                                int i2 = 0;
                                while (i2 < length) {
                                    Field field = allFields[i2];
                                    int i3 = length;
                                    List<Pair<Object, Class<?>>> recursiveList3 = recursiveList2;
                                    try {
                                        field.setAccessible(true);
                                        Object tempAttribute = field.get(object);
                                        if (tempAttribute != null) {
                                            str2 = str4;
                                            if (tempAttribute.getClass().getName().equals(this.mClassName)) {
                                                matchCount++;
                                                matchAttribute = tempAttribute;
                                                matchField = field;
                                            }
                                        } else {
                                            str2 = str4;
                                        }
                                        i2++;
                                        length = i3;
                                        recursiveList2 = recursiveList3;
                                        str4 = str2;
                                    } catch (IllegalAccessException | NoSuchFieldException e3) {
                                        e = e3;
                                        recursiveList2 = recursiveList3;
                                        Logger.w(TAG, e.getMessage(), e);
                                        if (this.mFieldName.equals("[foreach]")) {
                                        }
                                    }
                                }
                                recursiveList = recursiveList2;
                                str = str4;
                                if (matchCount > 0) {
                                    if (matchCount == 1) {
                                        String str8 = TAG;
                                        Logger.i(str8, "Class name found.");
                                        Logger.i(str8, "Field = " + matchField.getName());
                                        attribute2 = matchAttribute;
                                    } else {
                                        Logger.i(TAG, "Too many same class name. Use index instead.");
                                        attribute2 = null;
                                    }
                                    if (!this.mClassNameObfuscated || attribute2 == null) {
                                        if (allFields.length != this.mParentFieldCount) {
                                            int i4 = this.mFieldIndex;
                                            if (i4 >= 0 && i4 < allFields.length) {
                                                Field field2 = clazz.getDeclaredFields()[this.mFieldIndex];
                                                field2.setAccessible(true);
                                                attribute2 = field2.get(object);
                                            } else {
                                                String str9 = TAG;
                                                Logger.w(str9, "Field index is out of bounds.");
                                                Logger.w(str9, "Current = " + clazz.getName());
                                                Logger.w(str9, "Index = " + this.mFieldIndex + "/" + allFields.length);
                                            }
                                        } else {
                                            String str10 = TAG;
                                            Logger.w(str10, "Parent field count is incorrect");
                                            Logger.w(str10, "Current = " + clazz.getName());
                                            Logger.w(str10, "Expected = " + this.mParentFieldCount);
                                            Logger.w(str10, str + allFields.length);
                                        }
                                    }
                                } else {
                                    Logger.i(TAG, "Class name not found.");
                                }
                            }
                            attribute2 = attribute;
                            if (!this.mClassNameObfuscated) {
                            }
                            if (allFields.length != this.mParentFieldCount) {
                            }
                        } catch (IllegalAccessException | NoSuchFieldException e4) {
                            e = e4;
                        }
                    } else {
                        recursiveList = recursiveList2;
                        try {
                        } catch (IllegalAccessException | NoSuchFieldException e5) {
                            e2 = e5;
                            recursiveList2 = recursiveList;
                            e = e2;
                            Logger.w(TAG, e.getMessage(), e);
                            if (this.mFieldName.equals("[foreach]")) {
                            }
                        }
                        try {
                            Field field3 = clazz.getDeclaredField(this.mFieldName);
                            field3.setAccessible(true);
                            attribute2 = field3.get(object);
                        } catch (IllegalAccessException | NoSuchFieldException e6) {
                            e2 = e6;
                            recursiveList2 = recursiveList;
                            e = e2;
                            Logger.w(TAG, e.getMessage(), e);
                            if (this.mFieldName.equals("[foreach]")) {
                            }
                        }
                    }
                    if (attribute2 == null) {
                        recursiveList2 = recursiveList;
                    } else {
                        recursiveList2 = recursiveList;
                        try {
                            recursiveList2.add(new Pair<>(attribute2, attribute2.getClass()));
                        } catch (IllegalAccessException | NoSuchFieldException e7) {
                            e = e7;
                            e = e;
                            Logger.w(TAG, e.getMessage(), e);
                            if (this.mFieldName.equals("[foreach]")) {
                            }
                        }
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e8) {
                e = e8;
            }
            if (this.mFieldName.equals("[foreach]")) {
                JSONArray resultArray = new JSONArray();
                for (Pair<Object, Class<?>> pair : recursiveList2) {
                    if (this.mLeafList.size() > 0) {
                        JSONObject resultObject = new JSONObject();
                        for (ObjectNode objectNode : this.mLeafList) {
                            Pair<String, Object> attribute3 = objectNode.extractAttribute(pair.first, (Class) pair.second);
                            if (attribute3 != null) {
                                if (attribute3.first != null) {
                                    resultObject.put((String) attribute3.first, attribute3.second);
                                } else if (attribute3.second instanceof JSONObject) {
                                    JSONObject attributeObject = (JSONObject) attribute3.second;
                                    Iterator<String> iterator = attributeObject.keys();
                                    while (iterator.hasNext()) {
                                        String key2 = iterator.next();
                                        Object value2 = attributeObject.get(key2);
                                        if (checkObjectNonNull(value2)) {
                                            resultObject.put(key2, value2);
                                        }
                                    }
                                }
                            }
                        }
                        resultArray.put(resultObject);
                    } else {
                        resultArray.put(pair.first);
                    }
                }
                return new Pair<>(this.mAlias, resultArray);
            }
            if (recursiveList2.size() == 1) {
                Pair<Object, Class<?>> pair2 = recursiveList2.get(0);
                if (this.mLeafList.size() > 0) {
                    JSONObject resultObject2 = new JSONObject();
                    for (ObjectNode objectNode2 : this.mLeafList) {
                        Pair<String, Object> attribute4 = objectNode2.extractAttribute(pair2.first, (Class) pair2.second);
                        if (attribute4 != null) {
                            if (attribute4.first != null) {
                                resultObject2.put((String) attribute4.first, attribute4.second);
                            } else if (attribute4.second instanceof JSONObject) {
                                JSONObject attributeObject2 = (JSONObject) attribute4.second;
                                Iterator<String> iterator2 = attributeObject2.keys();
                                while (iterator2.hasNext()) {
                                    String key3 = iterator2.next();
                                    Object value3 = attributeObject2.get(key3);
                                    if (checkObjectNonNull(value3)) {
                                        resultObject2.put(key3, value3);
                                    }
                                }
                            } else if (attribute4.second instanceof JSONArray) {
                                JSONArray attributeArray = (JSONArray) attribute4.second;
                                for (int i5 = 0; i5 < attributeArray.length(); i5++) {
                                    JSONObject obj = attributeArray.getJSONObject(i5);
                                    Iterator<String> iterator3 = obj.keys();
                                    while (iterator3.hasNext()) {
                                        String key4 = iterator3.next();
                                        resultObject2.put(key4 + "_" + i5, obj.get(key4));
                                        recursiveList2 = recursiveList2;
                                    }
                                }
                            }
                        }
                        recursiveList2 = recursiveList2;
                    }
                    if (this.mAttributeList.size() == 0) {
                        return new Pair<>(this.mAlias, resultObject2);
                    }
                    for (Attribute attribute5 : this.mAttributeList) {
                        Logger.i(TAG, "Create Attribute.\n" + attribute5.toString());
                        Map<String, String> valueMap = new HashMap<>();
                        Iterator<String> iterator4 = resultObject2.keys();
                        while (iterator4.hasNext()) {
                            String key5 = iterator4.next();
                            Object value4 = resultObject2.get(key5);
                            if (value4 != null) {
                                valueMap.put(key5, String.valueOf(value4));
                            }
                        }
                        Pair<String, String> resultPair = attribute5.getResult(valueMap);
                        if (resultPair != null) {
                            Logger.i(TAG, "Attribute created. " + ((String) resultPair.first) + " = " + ((String) resultPair.second));
                            resultObject2.put((String) resultPair.first, resultPair.second);
                        }
                    }
                    return new Pair<>(this.mAlias, resultObject2);
                }
                String value5 = String.valueOf(pair2.first);
                String str11 = TAG;
                Logger.i(str11, "Attribute found. " + this.mAlias + " = " + value5);
                String str12 = this.mCapturePattern;
                if (str12 != null) {
                    value5 = RegularExpressionUtils.capture(value5, str12);
                    Logger.i(str11, "Capture Attribute = " + value5);
                }
                return new Pair<>(this.mAlias, value5);
            }
            return null;
        }
        return null;
    }

    private boolean isCustomTag(String fieldName) {
        return fieldName != null && fieldName.startsWith("[") && fieldName.endsWith("]");
    }

    private int stringToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            Logger.w(TAG, e.getMessage());
            return -1;
        }
    }

    private Object autoFix(Object object, Class<?> clazz, String className) throws IllegalAccessException {
        Field[] allFields = clazz.getDeclaredFields();
        for (Field field : allFields) {
            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null && value.getClass().getName().equals(className)) {
                return value;
            }
        }
        return null;
    }

    private boolean checkObjectNonNull(Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                return !TextUtils.isEmpty((String) obj);
            }
            return true;
        }
        return false;
    }
}
