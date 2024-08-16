package com.android.server.devicepolicy;

import android.app.admin.PolicyKey;
import android.app.admin.PolicyValue;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
abstract class PolicySerializer<V> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract PolicyValue<V> readFromXml(TypedXmlPullParser typedXmlPullParser);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void saveToXml(PolicyKey policyKey, TypedXmlSerializer typedXmlSerializer, V v) throws IOException;
}
