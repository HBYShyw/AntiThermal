package com.android.server.devicepolicy;

import android.app.admin.ComponentNamePolicyValue;
import android.app.admin.PolicyKey;
import android.content.ComponentName;
import android.util.Log;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class ComponentNamePolicySerializer extends PolicySerializer<ComponentName> {
    private static final String ATTR_CLASS_NAME = "class-name";
    private static final String ATTR_PACKAGE_NAME = "package-name";
    private static final String TAG = "ComponentNamePolicySerializer";

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(PolicyKey policyKey, TypedXmlSerializer typedXmlSerializer, ComponentName componentName) throws IOException {
        Objects.requireNonNull(componentName);
        typedXmlSerializer.attribute((String) null, ATTR_PACKAGE_NAME, componentName.getPackageName());
        typedXmlSerializer.attribute((String) null, ATTR_CLASS_NAME, componentName.getClassName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public ComponentNamePolicyValue readFromXml(TypedXmlPullParser typedXmlPullParser) {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_PACKAGE_NAME);
        String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, ATTR_CLASS_NAME);
        if (attributeValue == null || attributeValue2 == null) {
            Log.e(TAG, "Error parsing ComponentName policy.");
            return null;
        }
        return new ComponentNamePolicyValue(new ComponentName(attributeValue, attributeValue2));
    }
}
