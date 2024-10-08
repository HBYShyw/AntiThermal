package com.android.server.devicepolicy;

import android.app.admin.LongPolicyValue;
import android.app.admin.PolicyKey;
import android.util.Log;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class LongPolicySerializer extends PolicySerializer<Long> {
    private static final String ATTR_VALUE = "value";
    private static final String TAG = "LongPolicySerializer";

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(PolicyKey policyKey, TypedXmlSerializer typedXmlSerializer, Long l) throws IOException {
        Objects.requireNonNull(l);
        typedXmlSerializer.attributeLong((String) null, ATTR_VALUE, l.longValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public LongPolicyValue readFromXml(TypedXmlPullParser typedXmlPullParser) {
        try {
            return new LongPolicyValue(typedXmlPullParser.getAttributeLong((String) null, ATTR_VALUE));
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing Long policy value", e);
            return null;
        }
    }
}
