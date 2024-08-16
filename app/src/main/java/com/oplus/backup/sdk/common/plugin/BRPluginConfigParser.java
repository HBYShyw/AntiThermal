package com.oplus.backup.sdk.common.plugin;

import android.os.Bundle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.oplus.backup.sdk.common.utils.BRLog;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.InnerUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/* loaded from: classes.dex */
public class BRPluginConfigParser {
    public static final String JSON_ENCODE = "json";
    private static final String TAG = "BRPluginConfigParser";

    private static Bundle fromJson(JsonElement jsonElement) {
        Bundle bundle = new Bundle();
        if (!jsonElement.isJsonObject()) {
            return null;
        }
        for (Map.Entry<String, JsonElement> entry : ((JsonObject) jsonElement).entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            JsonArray jsonArray = value.isJsonArray() ? (JsonArray) value : null;
            JsonPrimitive jsonPrimitive = value.isJsonPrimitive() ? (JsonPrimitive) value : null;
            if (jsonArray != null && jsonArray.size() <= 0) {
                bundle.putStringArray(key, new String[0]);
            } else if (jsonArray != null && jsonArray.get(0).isJsonPrimitive()) {
                int size = jsonArray.size();
                String[] strArr = new String[size];
                for (int i10 = 0; i10 < size; i10++) {
                    JsonPrimitive jsonPrimitive2 = (JsonPrimitive) jsonArray.get(i10);
                    if (jsonPrimitive2.isString()) {
                        strArr[i10] = jsonPrimitive2.getAsString();
                    }
                }
                bundle.putStringArray(key, strArr);
            } else if (jsonPrimitive != null) {
                if (jsonPrimitive.isBoolean()) {
                    bundle.putBoolean(key, jsonPrimitive.getAsBoolean());
                } else if (jsonPrimitive.isNumber()) {
                    bundle.putDouble(key, jsonPrimitive.getAsNumber().doubleValue());
                } else if (jsonPrimitive.isString()) {
                    bundle.putString(key, jsonPrimitive.getAsString());
                } else {
                    BRLog.d(TAG, "unable to transform json to bundle " + key);
                }
            }
        }
        return bundle;
    }

    private static String getValue(String str, String str2) {
        int indexOf = str.indexOf(str2);
        if (indexOf == -1) {
            return null;
        }
        int indexOf2 = str.indexOf(InnerUtils.EQUAL, indexOf) + 1;
        int indexOf3 = str.indexOf(";", indexOf2);
        if (indexOf3 == -1) {
            indexOf3 = str.length();
        }
        return str.substring(indexOf2, indexOf3);
    }

    public static BRPluginConfig parse(InputStream inputStream) {
        String readInputStream = readInputStream(inputStream);
        String value = getValue(readInputStream, "encode");
        String value2 = getValue(readInputStream, "version");
        String value3 = getValue(readInputStream, "context");
        if (value3 != null) {
            value3 = value3.replaceAll("\r|\n", "");
        }
        return parse(value, value2, value3);
    }

    private static String readFile(File file) {
        FileInputStream fileInputStream = null;
        if (file == null) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e10) {
            BRLog.e(TAG, "new FileInputStream failed, " + e10.getMessage());
        }
        return readInputStream(fileInputStream);
    }

    private static String readInputStream(InputStream inputStream) {
        StringBuilder sb2;
        if (inputStream == null) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            try {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                } catch (IOException e10) {
                    BRLog.e(TAG, "readInputStream, e =" + e10.getMessage());
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e11) {
                            e = e11;
                            sb2 = new StringBuilder();
                            sb2.append("close failed, ");
                            sb2.append(e.getMessage());
                            BRLog.w(TAG, sb2.toString());
                            return stringBuffer.toString();
                        }
                    }
                    bufferedReader.close();
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e12) {
                        BRLog.w(TAG, "close failed, " + e12.getMessage());
                        throw th;
                    }
                }
                bufferedReader.close();
                throw th;
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e13) {
                e = e13;
                sb2 = new StringBuilder();
                sb2.append("close failed, ");
                sb2.append(e.getMessage());
                BRLog.w(TAG, sb2.toString());
                return stringBuffer.toString();
            }
        }
        bufferedReader.close();
        return stringBuffer.toString();
    }

    public static BRPluginConfig parse(File file) {
        String readFile = readFile(file);
        String value = getValue(readFile, "encode");
        String value2 = getValue(readFile, "version");
        String value3 = getValue(readFile, "context");
        if (value3 != null) {
            return parse(value, value2, value3.replaceAll("\r|\n", ""));
        }
        return null;
    }

    protected static BRPluginConfig parse(String str, String str2, String str3) {
        Bundle bundle;
        BRLog.d(TAG, str + ", " + str2 + ", " + str3);
        if (!JSON_ENCODE.equals(str)) {
            return null;
        }
        try {
            bundle = fromJson(new JsonParser().parse(str3));
        } catch (Exception e10) {
            BRLog.w(TAG, "parse failed: " + e10.getMessage());
            bundle = null;
        }
        if (bundle != null) {
            return new BRPluginConfig(bundle);
        }
        return null;
    }
}
