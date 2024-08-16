package com.android.server.devicepolicy;

import android.app.admin.LockTaskPolicy;
import android.app.admin.PolicyKey;
import android.util.Log;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class LockTaskPolicySerializer extends PolicySerializer<LockTaskPolicy> {
    private static final String ATTR_FLAGS = "flags";
    private static final String ATTR_PACKAGES = "packages";
    private static final String ATTR_PACKAGES_SEPARATOR = ";";
    private static final String TAG = "LockTaskPolicySerializer";

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(PolicyKey policyKey, TypedXmlSerializer typedXmlSerializer, LockTaskPolicy lockTaskPolicy) throws IOException {
        Objects.requireNonNull(lockTaskPolicy);
        typedXmlSerializer.attribute((String) null, ATTR_PACKAGES, String.join(ATTR_PACKAGES_SEPARATOR, lockTaskPolicy.getPackages()));
        typedXmlSerializer.attributeInt((String) null, ATTR_FLAGS, lockTaskPolicy.getFlags());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public LockTaskPolicy readFromXml(TypedXmlPullParser typedXmlPullParser) {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_PACKAGES);
        if (attributeValue == null) {
            Log.e(TAG, "Error parsing LockTask policy value.");
            return null;
        }
        try {
            return new LockTaskPolicy(Set.of((Object[]) attributeValue.split(ATTR_PACKAGES_SEPARATOR)), typedXmlPullParser.getAttributeInt((String) null, ATTR_FLAGS));
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing LockTask policy value", e);
            return null;
        }
    }
}
