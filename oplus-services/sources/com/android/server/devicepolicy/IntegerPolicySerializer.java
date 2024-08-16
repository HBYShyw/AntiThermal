package com.android.server.devicepolicy;

import android.app.admin.IntegerPolicyValue;
import android.app.admin.PolicyKey;
import android.util.Log;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class IntegerPolicySerializer extends PolicySerializer<Integer> {
    private static final String ATTR_VALUE = "value";
    private static final String TAG = "IntegerPolicySerializer";

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(PolicyKey policyKey, TypedXmlSerializer typedXmlSerializer, Integer num) throws IOException {
        Objects.requireNonNull(num);
        typedXmlSerializer.attributeInt((String) null, ATTR_VALUE, num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public IntegerPolicyValue readFromXml(TypedXmlPullParser typedXmlPullParser) {
        try {
            return new IntegerPolicyValue(typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE));
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing Integer policy value", e);
            return null;
        }
    }
}
