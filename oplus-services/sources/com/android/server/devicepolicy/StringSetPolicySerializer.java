package com.android.server.devicepolicy;

import android.app.admin.PolicyKey;
import android.app.admin.PolicyValue;
import android.app.admin.StringSetPolicyValue;
import android.util.Log;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class StringSetPolicySerializer extends PolicySerializer<Set<String>> {
    private static final String ATTR_VALUES = "strings";
    private static final String ATTR_VALUES_SEPARATOR = ";";

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(PolicyKey policyKey, TypedXmlSerializer typedXmlSerializer, Set<String> set) throws IOException {
        Objects.requireNonNull(set);
        typedXmlSerializer.attribute((String) null, ATTR_VALUES, String.join(ATTR_VALUES_SEPARATOR, set));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public PolicyValue<Set<String>> readFromXml(TypedXmlPullParser typedXmlPullParser) {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUES);
        if (attributeValue == null) {
            Log.e("DevicePolicyEngine", "Error parsing StringSet policy value.");
            return null;
        }
        return new StringSetPolicyValue(Set.of((Object[]) attributeValue.split(ATTR_VALUES_SEPARATOR)));
    }
}
