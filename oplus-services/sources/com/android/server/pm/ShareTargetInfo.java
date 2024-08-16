package com.android.server.pm;

import android.text.TextUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ShareTargetInfo {
    private static final String ATTR_HOST = "host";
    private static final String ATTR_MIME_TYPE = "mimeType";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String ATTR_PATH_PATTERN = "pathPattern";
    private static final String ATTR_PATH_PREFIX = "pathPrefix";
    private static final String ATTR_PORT = "port";
    private static final String ATTR_SCHEME = "scheme";
    private static final String ATTR_TARGET_CLASS = "targetClass";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_DATA = "data";
    private static final String TAG_SHARE_TARGET = "share-target";
    final String[] mCategories;
    final String mTargetClass;
    final TargetData[] mTargetData;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TargetData {
        final String mHost;
        final String mMimeType;
        final String mPath;
        final String mPathPattern;
        final String mPathPrefix;
        final String mPort;
        final String mScheme;

        /* JADX INFO: Access modifiers changed from: package-private */
        public TargetData(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
            this.mScheme = str;
            this.mHost = str2;
            this.mPort = str3;
            this.mPath = str4;
            this.mPathPattern = str5;
            this.mPathPrefix = str6;
            this.mMimeType = str7;
        }

        public void toStringInner(StringBuilder sb) {
            if (!TextUtils.isEmpty(this.mScheme)) {
                sb.append(" scheme=");
                sb.append(this.mScheme);
            }
            if (!TextUtils.isEmpty(this.mHost)) {
                sb.append(" host=");
                sb.append(this.mHost);
            }
            if (!TextUtils.isEmpty(this.mPort)) {
                sb.append(" port=");
                sb.append(this.mPort);
            }
            if (!TextUtils.isEmpty(this.mPath)) {
                sb.append(" path=");
                sb.append(this.mPath);
            }
            if (!TextUtils.isEmpty(this.mPathPattern)) {
                sb.append(" pathPattern=");
                sb.append(this.mPathPattern);
            }
            if (!TextUtils.isEmpty(this.mPathPrefix)) {
                sb.append(" pathPrefix=");
                sb.append(this.mPathPrefix);
            }
            if (TextUtils.isEmpty(this.mMimeType)) {
                return;
            }
            sb.append(" mimeType=");
            sb.append(this.mMimeType);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            toStringInner(sb);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShareTargetInfo(TargetData[] targetDataArr, String str, String[] strArr) {
        this.mTargetData = targetDataArr;
        this.mTargetClass = str;
        this.mCategories = strArr;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("targetClass=");
        sb.append(this.mTargetClass);
        for (int i = 0; i < this.mTargetData.length; i++) {
            sb.append(" data={");
            this.mTargetData[i].toStringInner(sb);
            sb.append("}");
        }
        for (int i2 = 0; i2 < this.mCategories.length; i2++) {
            sb.append(" category=");
            sb.append(this.mCategories[i2]);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveToXml(TypedXmlSerializer typedXmlSerializer) throws IOException {
        typedXmlSerializer.startTag((String) null, TAG_SHARE_TARGET);
        ShortcutService.writeAttr(typedXmlSerializer, ATTR_TARGET_CLASS, this.mTargetClass);
        for (int i = 0; i < this.mTargetData.length; i++) {
            typedXmlSerializer.startTag((String) null, "data");
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_SCHEME, this.mTargetData[i].mScheme);
            ShortcutService.writeAttr(typedXmlSerializer, "host", this.mTargetData[i].mHost);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PORT, this.mTargetData[i].mPort);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PATH, this.mTargetData[i].mPath);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PATH_PATTERN, this.mTargetData[i].mPathPattern);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PATH_PREFIX, this.mTargetData[i].mPathPrefix);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_MIME_TYPE, this.mTargetData[i].mMimeType);
            typedXmlSerializer.endTag((String) null, "data");
        }
        for (int i2 = 0; i2 < this.mCategories.length; i2++) {
            typedXmlSerializer.startTag((String) null, TAG_CATEGORY);
            ShortcutService.writeAttr(typedXmlSerializer, "name", this.mCategories[i2]);
            typedXmlSerializer.endTag((String) null, TAG_CATEGORY);
        }
        typedXmlSerializer.endTag((String) null, TAG_SHARE_TARGET);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShareTargetInfo loadFromXml(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        String parseStringAttribute = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_TARGET_CLASS);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next != 1) {
                if (next == 2) {
                    String name = typedXmlPullParser.getName();
                    name.hashCode();
                    if (name.equals("data")) {
                        arrayList.add(parseTargetData(typedXmlPullParser));
                    } else if (name.equals(TAG_CATEGORY)) {
                        arrayList2.add(ShortcutService.parseStringAttribute(typedXmlPullParser, "name"));
                    }
                } else if (next == 3 && typedXmlPullParser.getName().equals(TAG_SHARE_TARGET)) {
                    break;
                }
            } else {
                break;
            }
        }
        if (arrayList.isEmpty() || parseStringAttribute == null || arrayList2.isEmpty()) {
            return null;
        }
        return new ShareTargetInfo((TargetData[]) arrayList.toArray(new TargetData[arrayList.size()]), parseStringAttribute, (String[]) arrayList2.toArray(new String[arrayList2.size()]));
    }

    private static TargetData parseTargetData(TypedXmlPullParser typedXmlPullParser) {
        return new TargetData(ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_SCHEME), ShortcutService.parseStringAttribute(typedXmlPullParser, "host"), ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_PORT), ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_PATH), ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_PATH_PATTERN), ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_PATH_PREFIX), ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_MIME_TYPE));
    }
}
