package com.android.server.firewall;

import android.content.ComponentName;
import android.content.Intent;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class SenderPermissionFilter implements Filter {
    private static final String ATTR_NAME = "name";
    public static final FilterFactory FACTORY = new FilterFactory("sender-permission") { // from class: com.android.server.firewall.SenderPermissionFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public Filter newFilter(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
            String attributeValue = xmlPullParser.getAttributeValue(null, SenderPermissionFilter.ATTR_NAME);
            if (attributeValue == null) {
                throw new XmlPullParserException("Permission name must be specified.", xmlPullParser, null);
            }
            return new SenderPermissionFilter(attributeValue);
        }
    };
    private final String mPermission;

    private SenderPermissionFilter(String str) {
        this.mPermission = str;
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(IntentFirewall intentFirewall, ComponentName componentName, Intent intent, int i, int i2, String str, int i3) {
        return intentFirewall.checkComponentPermission(this.mPermission, i2, i, i3, true);
    }
}
