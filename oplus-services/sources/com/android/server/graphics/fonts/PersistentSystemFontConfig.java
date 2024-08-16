package com.android.server.graphics.fonts;

import android.graphics.fonts.FontUpdateRequest;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Slog;
import android.util.Xml;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class PersistentSystemFontConfig {
    private static final String ATTR_VALUE = "value";
    private static final String TAG = "PersistentSystemFontConfig";
    private static final String TAG_FAMILY = "family";
    private static final String TAG_LAST_MODIFIED_DATE = "lastModifiedDate";
    private static final String TAG_ROOT = "fontConfig";
    private static final String TAG_UPDATED_FONT_DIR = "updatedFontDir";

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Config {
        public long lastModifiedMillis;
        public final Set<String> updatedFontDirs = new ArraySet();
        public final List<FontUpdateRequest.Family> fontFamilies = new ArrayList();
    }

    PersistentSystemFontConfig() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0059, code lost:
    
        if (r3.equals(com.android.server.graphics.fonts.PersistentSystemFontConfig.TAG_FAMILY) == false) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void loadFromXml(InputStream inputStream, Config config) throws XmlPullParserException, IOException {
        TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(inputStream);
        while (true) {
            int next = resolvePullParser.next();
            boolean z = true;
            if (next == 1) {
                return;
            }
            if (next == 2) {
                int depth = resolvePullParser.getDepth();
                String name = resolvePullParser.getName();
                if (depth == 1) {
                    if (!TAG_ROOT.equals(name)) {
                        Slog.e(TAG, "Invalid root tag: " + name);
                        return;
                    }
                } else if (depth == 2) {
                    name.hashCode();
                    switch (name.hashCode()) {
                        case -1540845619:
                            if (name.equals(TAG_LAST_MODIFIED_DATE)) {
                                z = false;
                                break;
                            }
                            break;
                        case -1281860764:
                            break;
                        case -23402365:
                            if (name.equals(TAG_UPDATED_FONT_DIR)) {
                                z = 2;
                                break;
                            }
                            break;
                    }
                    z = -1;
                    switch (z) {
                        case false:
                            config.lastModifiedMillis = parseLongAttribute(resolvePullParser, ATTR_VALUE, 0L);
                            break;
                        case true:
                            config.fontFamilies.add(FontUpdateRequest.Family.readFromXml(resolvePullParser));
                            break;
                        case true:
                            config.updatedFontDirs.add(getAttribute(resolvePullParser, ATTR_VALUE));
                            break;
                        default:
                            Slog.w(TAG, "Skipping unknown tag: " + name);
                            break;
                    }
                }
            }
        }
    }

    public static void writeToXml(OutputStream outputStream, Config config) throws IOException {
        TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(outputStream);
        resolveSerializer.startDocument((String) null, Boolean.TRUE);
        resolveSerializer.startTag((String) null, TAG_ROOT);
        resolveSerializer.startTag((String) null, TAG_LAST_MODIFIED_DATE);
        resolveSerializer.attribute((String) null, ATTR_VALUE, Long.toString(config.lastModifiedMillis));
        resolveSerializer.endTag((String) null, TAG_LAST_MODIFIED_DATE);
        for (String str : config.updatedFontDirs) {
            resolveSerializer.startTag((String) null, TAG_UPDATED_FONT_DIR);
            resolveSerializer.attribute((String) null, ATTR_VALUE, str);
            resolveSerializer.endTag((String) null, TAG_UPDATED_FONT_DIR);
        }
        List<FontUpdateRequest.Family> list = config.fontFamilies;
        for (int i = 0; i < list.size(); i++) {
            FontUpdateRequest.Family family = list.get(i);
            resolveSerializer.startTag((String) null, TAG_FAMILY);
            FontUpdateRequest.Family.writeFamilyToXml(resolveSerializer, family);
            resolveSerializer.endTag((String) null, TAG_FAMILY);
        }
        resolveSerializer.endTag((String) null, TAG_ROOT);
        resolveSerializer.endDocument();
    }

    private static long parseLongAttribute(TypedXmlPullParser typedXmlPullParser, String str, long j) {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, str);
        if (TextUtils.isEmpty(attributeValue)) {
            return j;
        }
        try {
            return Long.parseLong(attributeValue);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    private static String getAttribute(TypedXmlPullParser typedXmlPullParser, String str) {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, str);
        return attributeValue == null ? "" : attributeValue;
    }
}
