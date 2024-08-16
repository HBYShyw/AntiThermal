package com.android.server.inputmethod;

import android.icu.util.ULocale;
import android.os.Environment;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class AdditionalSubtypeUtils {
    private static final String ADDITIONAL_SUBTYPES_FILE_NAME = "subtypes.xml";
    private static final String ATTR_ICON = "icon";
    private static final String ATTR_ID = "id";
    private static final String ATTR_IME_SUBTYPE_EXTRA_VALUE = "imeSubtypeExtraValue";
    private static final String ATTR_IME_SUBTYPE_ID = "subtypeId";
    private static final String ATTR_IME_SUBTYPE_LANGUAGE_TAG = "languageTag";
    private static final String ATTR_IME_SUBTYPE_LOCALE = "imeSubtypeLocale";
    private static final String ATTR_IME_SUBTYPE_MODE = "imeSubtypeMode";
    private static final String ATTR_IS_ASCII_CAPABLE = "isAsciiCapable";
    private static final String ATTR_IS_AUXILIARY = "isAuxiliary";
    private static final String ATTR_LABEL = "label";
    private static final String ATTR_NAME_OVERRIDE = "nameOverride";
    private static final String ATTR_NAME_PK_LANGUAGE_TAG = "pkLanguageTag";
    private static final String ATTR_NAME_PK_LAYOUT_TYPE = "pkLayoutType";
    private static final String INPUT_METHOD_PATH = "inputmethod";
    private static final String NODE_IMI = "imi";
    private static final String NODE_SUBTYPE = "subtype";
    private static final String NODE_SUBTYPES = "subtypes";
    private static final String SYSTEM_PATH = "system";
    private static final String TAG = "AdditionalSubtypeUtils";

    private AdditionalSubtypeUtils() {
    }

    private static File getInputMethodDir(int i) {
        File userSystemDirectory;
        if (i == 0) {
            userSystemDirectory = new File(Environment.getDataDirectory(), SYSTEM_PATH);
        } else {
            userSystemDirectory = Environment.getUserSystemDirectory(i);
        }
        return new File(userSystemDirectory, INPUT_METHOD_PATH);
    }

    private static AtomicFile getAdditionalSubtypeFile(File file) {
        return new AtomicFile(new File(file, ADDITIONAL_SUBTYPES_FILE_NAME), "input-subtypes");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void save(ArrayMap<String, List<InputMethodSubtype>> arrayMap, ArrayMap<String, InputMethodInfo> arrayMap2, int i) {
        File inputMethodDir = getInputMethodDir(i);
        if (arrayMap.isEmpty()) {
            if (inputMethodDir.exists()) {
                AtomicFile additionalSubtypeFile = getAdditionalSubtypeFile(inputMethodDir);
                if (additionalSubtypeFile.exists()) {
                    additionalSubtypeFile.delete();
                }
                if (FileUtils.listFilesOrEmpty(inputMethodDir).length != 0 || inputMethodDir.delete()) {
                    return;
                }
                Slog.e(TAG, "Failed to delete the empty parent directory " + inputMethodDir);
                return;
            }
            return;
        }
        if (!inputMethodDir.exists() && !inputMethodDir.mkdirs()) {
            Slog.e(TAG, "Failed to create a parent directory " + inputMethodDir);
            return;
        }
        saveToFile(arrayMap, arrayMap2, getAdditionalSubtypeFile(inputMethodDir));
    }

    @VisibleForTesting
    static void saveToFile(ArrayMap<String, List<InputMethodSubtype>> arrayMap, ArrayMap<String, InputMethodInfo> arrayMap2, AtomicFile atomicFile) {
        boolean z = arrayMap2 != null && arrayMap2.size() > 0;
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream startWrite = atomicFile.startWrite();
                try {
                    TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                    resolveSerializer.startDocument((String) null, Boolean.TRUE);
                    resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                    resolveSerializer.startTag((String) null, NODE_SUBTYPES);
                    for (String str : arrayMap.keySet()) {
                        if (z && !arrayMap2.containsKey(str)) {
                            Slog.w(TAG, "IME uninstalled or not valid.: " + str);
                        } else {
                            List<InputMethodSubtype> list = arrayMap.get(str);
                            if (list == null) {
                                Slog.e(TAG, "Null subtype list for IME " + str);
                            } else {
                                resolveSerializer.startTag((String) null, NODE_IMI);
                                resolveSerializer.attribute((String) null, ATTR_ID, str);
                                for (InputMethodSubtype inputMethodSubtype : list) {
                                    resolveSerializer.startTag((String) null, NODE_SUBTYPE);
                                    if (inputMethodSubtype.hasSubtypeId()) {
                                        resolveSerializer.attributeInt((String) null, ATTR_IME_SUBTYPE_ID, inputMethodSubtype.getSubtypeId());
                                    }
                                    resolveSerializer.attributeInt((String) null, ATTR_ICON, inputMethodSubtype.getIconResId());
                                    resolveSerializer.attributeInt((String) null, ATTR_LABEL, inputMethodSubtype.getNameResId());
                                    resolveSerializer.attribute((String) null, ATTR_NAME_OVERRIDE, inputMethodSubtype.getNameOverride().toString());
                                    ULocale physicalKeyboardHintLanguageTag = inputMethodSubtype.getPhysicalKeyboardHintLanguageTag();
                                    if (physicalKeyboardHintLanguageTag != null) {
                                        resolveSerializer.attribute((String) null, ATTR_NAME_PK_LANGUAGE_TAG, physicalKeyboardHintLanguageTag.toLanguageTag());
                                    }
                                    resolveSerializer.attribute((String) null, ATTR_NAME_PK_LAYOUT_TYPE, inputMethodSubtype.getPhysicalKeyboardHintLayoutType());
                                    resolveSerializer.attribute((String) null, ATTR_IME_SUBTYPE_LOCALE, inputMethodSubtype.getLocale());
                                    resolveSerializer.attribute((String) null, ATTR_IME_SUBTYPE_LANGUAGE_TAG, inputMethodSubtype.getLanguageTag());
                                    resolveSerializer.attribute((String) null, ATTR_IME_SUBTYPE_MODE, inputMethodSubtype.getMode());
                                    resolveSerializer.attribute((String) null, ATTR_IME_SUBTYPE_EXTRA_VALUE, inputMethodSubtype.getExtraValue());
                                    resolveSerializer.attributeInt((String) null, ATTR_IS_AUXILIARY, inputMethodSubtype.isAuxiliary() ? 1 : 0);
                                    resolveSerializer.attributeInt((String) null, ATTR_IS_ASCII_CAPABLE, inputMethodSubtype.isAsciiCapable() ? 1 : 0);
                                    resolveSerializer.endTag((String) null, NODE_SUBTYPE);
                                }
                                resolveSerializer.endTag((String) null, NODE_IMI);
                            }
                        }
                    }
                    resolveSerializer.endTag((String) null, NODE_SUBTYPES);
                    resolveSerializer.endDocument();
                    atomicFile.finishWrite(startWrite);
                    IoUtils.closeQuietly(startWrite);
                } catch (IOException e) {
                    e = e;
                    fileOutputStream = startWrite;
                    Slog.w(TAG, "Error writing subtypes", e);
                    if (fileOutputStream != null) {
                        atomicFile.failWrite(fileOutputStream);
                    }
                    IoUtils.closeQuietly(fileOutputStream);
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = startWrite;
                    IoUtils.closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void load(ArrayMap<String, List<InputMethodSubtype>> arrayMap, int i) {
        arrayMap.clear();
        AtomicFile additionalSubtypeFile = getAdditionalSubtypeFile(getInputMethodDir(i));
        if (additionalSubtypeFile.exists()) {
            loadFromFile(arrayMap, additionalSubtypeFile);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x0183 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:80:? A[Catch: IOException | NumberFormatException | XmlPullParserException -> 0x018d, IOException | NumberFormatException | XmlPullParserException -> 0x018d, IOException | NumberFormatException | XmlPullParserException -> 0x018d, SYNTHETIC, TRY_LEAVE, TryCatch #6 {IOException | NumberFormatException | XmlPullParserException -> 0x018d, blocks: (B:23:0x016b, B:79:0x018c, B:79:0x018c, B:79:0x018c, B:78:0x0189, B:78:0x0189, B:78:0x0189), top: B:2:0x0004 }] */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static void loadFromFile(ArrayMap<String, List<InputMethodSubtype>> arrayMap, AtomicFile atomicFile) {
        FileInputStream fileInputStream;
        Throwable th;
        int i;
        int i2;
        int i3;
        String str;
        String str2;
        boolean equals;
        String str3;
        String str4 = TAG;
        try {
            try {
                FileInputStream openRead = atomicFile.openRead();
                try {
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead);
                    int next = resolvePullParser.next();
                    while (true) {
                        i = 1;
                        i2 = 2;
                        if (next == 2 || next == 1) {
                            break;
                        } else {
                            next = resolvePullParser.next();
                        }
                    }
                    try {
                        if (!NODE_SUBTYPES.equals(resolvePullParser.getName())) {
                            throw new XmlPullParserException("Xml doesn't start with subtypes");
                        }
                        int depth = resolvePullParser.getDepth();
                        String str5 = null;
                        String str6 = null;
                        ArrayList arrayList = null;
                        while (true) {
                            int next2 = resolvePullParser.next();
                            if ((next2 != 3 || resolvePullParser.getDepth() > depth) && next2 != i) {
                                if (next2 == i2) {
                                    String name = resolvePullParser.getName();
                                    if (NODE_IMI.equals(name)) {
                                        str6 = resolvePullParser.getAttributeValue(str5, ATTR_ID);
                                        if (TextUtils.isEmpty(str6)) {
                                            Slog.w(str4, "Invalid imi id found in subtypes.xml");
                                        } else {
                                            arrayList = new ArrayList();
                                            arrayMap.put(str6, arrayList);
                                        }
                                    } else if (NODE_SUBTYPE.equals(name)) {
                                        try {
                                            if (!TextUtils.isEmpty(str6) && arrayList != null) {
                                                int attributeInt = resolvePullParser.getAttributeInt(str5, ATTR_ICON);
                                                int attributeInt2 = resolvePullParser.getAttributeInt(str5, ATTR_LABEL);
                                                String attributeValue = resolvePullParser.getAttributeValue(str5, ATTR_NAME_OVERRIDE);
                                                String attributeValue2 = resolvePullParser.getAttributeValue(str5, ATTR_NAME_PK_LANGUAGE_TAG);
                                                String attributeValue3 = resolvePullParser.getAttributeValue(str5, ATTR_NAME_PK_LAYOUT_TYPE);
                                                String attributeValue4 = resolvePullParser.getAttributeValue(str5, ATTR_IME_SUBTYPE_LOCALE);
                                                String attributeValue5 = resolvePullParser.getAttributeValue(str5, ATTR_IME_SUBTYPE_LANGUAGE_TAG);
                                                i3 = depth;
                                                String attributeValue6 = resolvePullParser.getAttributeValue(str5, ATTR_IME_SUBTYPE_MODE);
                                                String attributeValue7 = resolvePullParser.getAttributeValue(str5, ATTR_IME_SUBTYPE_EXTRA_VALUE);
                                                fileInputStream = openRead;
                                                try {
                                                    equals = "1".equals(String.valueOf(resolvePullParser.getAttributeValue(str5, ATTR_IS_AUXILIARY)));
                                                    str3 = str4;
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    th = th;
                                                    if (fileInputStream == null) {
                                                    }
                                                }
                                                try {
                                                    boolean equals2 = "1".equals(String.valueOf(resolvePullParser.getAttributeValue(str5, ATTR_IS_ASCII_CAPABLE)));
                                                    InputMethodSubtype.InputMethodSubtypeBuilder subtypeNameResId = new InputMethodSubtype.InputMethodSubtypeBuilder().setSubtypeNameResId(attributeInt2);
                                                    ULocale uLocale = attributeValue2 == null ? null : new ULocale(attributeValue2);
                                                    if (attributeValue3 == null) {
                                                        attributeValue3 = "";
                                                    }
                                                    InputMethodSubtype.InputMethodSubtypeBuilder isAsciiCapable = subtypeNameResId.setPhysicalKeyboardHint(uLocale, attributeValue3).setSubtypeIconResId(attributeInt).setSubtypeLocale(attributeValue4).setLanguageTag(attributeValue5).setSubtypeMode(attributeValue6).setSubtypeExtraValue(attributeValue7).setIsAuxiliary(equals).setIsAsciiCapable(equals2);
                                                    str = null;
                                                    int attributeInt3 = resolvePullParser.getAttributeInt((String) null, ATTR_IME_SUBTYPE_ID, 0);
                                                    if (attributeInt3 != 0) {
                                                        isAsciiCapable.setSubtypeId(attributeInt3);
                                                    }
                                                    if (attributeValue != null) {
                                                        isAsciiCapable.setSubtypeNameOverride(attributeValue);
                                                    }
                                                    arrayList.add(isAsciiCapable.build());
                                                    str2 = str3;
                                                    str4 = str2;
                                                    str5 = str;
                                                    depth = i3;
                                                    openRead = fileInputStream;
                                                    i = 1;
                                                    i2 = 2;
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                    if (fileInputStream == null) {
                                                    }
                                                }
                                            }
                                            str2 = str4;
                                            Slog.w(str2, "IME uninstalled or not valid.: " + str6);
                                            str4 = str2;
                                            str5 = str;
                                            depth = i3;
                                            openRead = fileInputStream;
                                            i = 1;
                                            i2 = 2;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            th = th;
                                            if (fileInputStream == null) {
                                                throw th;
                                            }
                                            try {
                                                fileInputStream.close();
                                                throw th;
                                            } catch (Throwable th5) {
                                                th.addSuppressed(th5);
                                                throw th;
                                            }
                                        }
                                        fileInputStream = openRead;
                                        i3 = depth;
                                        str = str5;
                                    }
                                }
                                fileInputStream = openRead;
                                i3 = depth;
                                str = str5;
                                str2 = str4;
                                str4 = str2;
                                str5 = str;
                                depth = i3;
                                openRead = fileInputStream;
                                i = 1;
                                i2 = 2;
                            }
                        }
                        FileInputStream fileInputStream2 = openRead;
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                    } catch (Throwable th6) {
                        th = th6;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    fileInputStream = openRead;
                }
            } catch (IOException | NumberFormatException | XmlPullParserException e) {
                e = e;
                Slog.w(TAG, "Error reading subtypes", e);
            }
        } catch (IOException | NumberFormatException | XmlPullParserException e2) {
            e = e2;
            Slog.w(TAG, "Error reading subtypes", e);
        }
    }
}
