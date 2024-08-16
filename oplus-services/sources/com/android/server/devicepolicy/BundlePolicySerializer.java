package com.android.server.devicepolicy;

import android.app.admin.BundlePolicyValue;
import android.app.admin.PackagePolicyKey;
import android.app.admin.PolicyKey;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class BundlePolicySerializer extends PolicySerializer<Bundle> {
    private static final String ATTR_KEY = "key";
    private static final String ATTR_MULTIPLE = "m";
    private static final String ATTR_TYPE_BOOLEAN = "b";
    private static final String ATTR_TYPE_BUNDLE = "B";
    private static final String ATTR_TYPE_BUNDLE_ARRAY = "BA";
    private static final String ATTR_TYPE_INTEGER = "i";
    private static final String ATTR_TYPE_STRING = "s";
    private static final String ATTR_TYPE_STRING_ARRAY = "sa";
    private static final String ATTR_VALUE_TYPE = "type";
    private static final String TAG = "BundlePolicySerializer";
    private static final String TAG_ENTRY = "entry";
    private static final String TAG_VALUE = "value";

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(PolicyKey policyKey, TypedXmlSerializer typedXmlSerializer, Bundle bundle) throws IOException {
        Objects.requireNonNull(bundle);
        Objects.requireNonNull(policyKey);
        if (!(policyKey instanceof PackagePolicyKey)) {
            throw new IllegalArgumentException("policyKey is not of type PackagePolicyKey");
        }
        writeBundle(bundle, typedXmlSerializer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public BundlePolicyValue readFromXml(TypedXmlPullParser typedXmlPullParser) {
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        try {
            int depth = typedXmlPullParser.getDepth();
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                readBundle(bundle, arrayList, typedXmlPullParser);
            }
            return new BundlePolicyValue(bundle);
        } catch (IOException | XmlPullParserException e) {
            Log.e(TAG, "Error parsing Bundle policy.", e);
            return null;
        }
    }

    private static void readBundle(Bundle bundle, ArrayList<String> arrayList, TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        if (typedXmlPullParser.getEventType() == 2 && typedXmlPullParser.getName().equals(TAG_ENTRY)) {
            String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_KEY);
            String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE_TYPE);
            int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_MULTIPLE, -1);
            if (attributeInt != -1) {
                arrayList.clear();
                while (attributeInt > 0) {
                    int next = typedXmlPullParser.next();
                    if (next == 1) {
                        break;
                    }
                    if (next == 2 && typedXmlPullParser.getName().equals(TAG_VALUE)) {
                        arrayList.add(typedXmlPullParser.nextText().trim());
                        attributeInt--;
                    }
                }
                String[] strArr = new String[arrayList.size()];
                arrayList.toArray(strArr);
                bundle.putStringArray(attributeValue, strArr);
                return;
            }
            if (ATTR_TYPE_BUNDLE.equals(attributeValue2)) {
                bundle.putBundle(attributeValue, readBundleEntry(typedXmlPullParser, arrayList));
                return;
            }
            if (ATTR_TYPE_BUNDLE_ARRAY.equals(attributeValue2)) {
                int depth = typedXmlPullParser.getDepth();
                ArrayList arrayList2 = new ArrayList();
                while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                    arrayList2.add(readBundleEntry(typedXmlPullParser, arrayList));
                }
                bundle.putParcelableArray(attributeValue, (Parcelable[]) arrayList2.toArray(new Bundle[arrayList2.size()]));
                return;
            }
            String trim = typedXmlPullParser.nextText().trim();
            if (ATTR_TYPE_BOOLEAN.equals(attributeValue2)) {
                bundle.putBoolean(attributeValue, Boolean.parseBoolean(trim));
            } else if (ATTR_TYPE_INTEGER.equals(attributeValue2)) {
                bundle.putInt(attributeValue, Integer.parseInt(trim));
            } else {
                bundle.putString(attributeValue, trim);
            }
        }
    }

    private static Bundle readBundleEntry(TypedXmlPullParser typedXmlPullParser, ArrayList<String> arrayList) throws IOException, XmlPullParserException {
        Bundle bundle = new Bundle();
        int depth = typedXmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
            readBundle(bundle, arrayList, typedXmlPullParser);
        }
        return bundle;
    }

    private static void writeBundle(Bundle bundle, TypedXmlSerializer typedXmlSerializer) throws IOException {
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            typedXmlSerializer.startTag((String) null, TAG_ENTRY);
            typedXmlSerializer.attribute((String) null, ATTR_KEY, str);
            if (obj instanceof Boolean) {
                typedXmlSerializer.attribute((String) null, ATTR_VALUE_TYPE, ATTR_TYPE_BOOLEAN);
                typedXmlSerializer.text(obj.toString());
            } else if (obj instanceof Integer) {
                typedXmlSerializer.attribute((String) null, ATTR_VALUE_TYPE, ATTR_TYPE_INTEGER);
                typedXmlSerializer.text(obj.toString());
            } else if (obj == null || (obj instanceof String)) {
                typedXmlSerializer.attribute((String) null, ATTR_VALUE_TYPE, ATTR_TYPE_STRING);
                typedXmlSerializer.text(obj != null ? (String) obj : "");
            } else if (obj instanceof Bundle) {
                typedXmlSerializer.attribute((String) null, ATTR_VALUE_TYPE, ATTR_TYPE_BUNDLE);
                writeBundle((Bundle) obj, typedXmlSerializer);
            } else {
                int i = 0;
                if (obj instanceof Parcelable[]) {
                    typedXmlSerializer.attribute((String) null, ATTR_VALUE_TYPE, ATTR_TYPE_BUNDLE_ARRAY);
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    int length = parcelableArr.length;
                    while (i < length) {
                        Parcelable parcelable = parcelableArr[i];
                        if (!(parcelable instanceof Bundle)) {
                            throw new IllegalArgumentException("bundle-array can only hold Bundles");
                        }
                        typedXmlSerializer.startTag((String) null, TAG_ENTRY);
                        typedXmlSerializer.attribute((String) null, ATTR_VALUE_TYPE, ATTR_TYPE_BUNDLE);
                        writeBundle((Bundle) parcelable, typedXmlSerializer);
                        typedXmlSerializer.endTag((String) null, TAG_ENTRY);
                        i++;
                    }
                } else {
                    typedXmlSerializer.attribute((String) null, ATTR_VALUE_TYPE, ATTR_TYPE_STRING_ARRAY);
                    String[] strArr = (String[]) obj;
                    typedXmlSerializer.attributeInt((String) null, ATTR_MULTIPLE, strArr.length);
                    int length2 = strArr.length;
                    while (i < length2) {
                        String str2 = strArr[i];
                        typedXmlSerializer.startTag((String) null, TAG_VALUE);
                        if (str2 == null) {
                            str2 = "";
                        }
                        typedXmlSerializer.text(str2);
                        typedXmlSerializer.endTag((String) null, TAG_VALUE);
                        i++;
                    }
                }
            }
            typedXmlSerializer.endTag((String) null, TAG_ENTRY);
        }
    }
}
