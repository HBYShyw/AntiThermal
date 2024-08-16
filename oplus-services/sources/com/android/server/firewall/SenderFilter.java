package com.android.server.firewall;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManagerInternal;
import android.os.Process;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class SenderFilter {
    private static final String ATTR_TYPE = "type";
    public static final FilterFactory FACTORY = new FilterFactory("sender") { // from class: com.android.server.firewall.SenderFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public Filter newFilter(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
            String attributeValue = xmlPullParser.getAttributeValue(null, SenderFilter.ATTR_TYPE);
            if (attributeValue == null) {
                throw new XmlPullParserException("type attribute must be specified for <sender>", xmlPullParser, null);
            }
            if (attributeValue.equals("system")) {
                return SenderFilter.SYSTEM;
            }
            if (attributeValue.equals(SenderFilter.VAL_SIGNATURE)) {
                return SenderFilter.SIGNATURE;
            }
            if (attributeValue.equals(SenderFilter.VAL_SYSTEM_OR_SIGNATURE)) {
                return SenderFilter.SYSTEM_OR_SIGNATURE;
            }
            if (attributeValue.equals(SenderFilter.VAL_USER_ID)) {
                return SenderFilter.USER_ID;
            }
            throw new XmlPullParserException("Invalid type attribute for <sender>: " + attributeValue, xmlPullParser, null);
        }
    };
    private static final Filter SIGNATURE = new Filter() { // from class: com.android.server.firewall.SenderFilter.2
        @Override // com.android.server.firewall.Filter
        public boolean matches(IntentFirewall intentFirewall, ComponentName componentName, Intent intent, int i, int i2, String str, int i3) {
            return intentFirewall.signaturesMatch(i, i3);
        }
    };
    private static final Filter SYSTEM = new Filter() { // from class: com.android.server.firewall.SenderFilter.3
        @Override // com.android.server.firewall.Filter
        public boolean matches(IntentFirewall intentFirewall, ComponentName componentName, Intent intent, int i, int i2, String str, int i3) {
            return SenderFilter.isPrivilegedApp(intentFirewall.getPackageManager(), i, i2);
        }
    };
    private static final Filter SYSTEM_OR_SIGNATURE = new Filter() { // from class: com.android.server.firewall.SenderFilter.4
        @Override // com.android.server.firewall.Filter
        public boolean matches(IntentFirewall intentFirewall, ComponentName componentName, Intent intent, int i, int i2, String str, int i3) {
            return SenderFilter.isPrivilegedApp(intentFirewall.getPackageManager(), i, i2) || intentFirewall.signaturesMatch(i, i3);
        }
    };
    private static final Filter USER_ID = new Filter() { // from class: com.android.server.firewall.SenderFilter.5
        @Override // com.android.server.firewall.Filter
        public boolean matches(IntentFirewall intentFirewall, ComponentName componentName, Intent intent, int i, int i2, String str, int i3) {
            return intentFirewall.checkComponentPermission(null, i2, i, i3, false);
        }
    };
    private static final String VAL_SIGNATURE = "signature";
    private static final String VAL_SYSTEM = "system";
    private static final String VAL_SYSTEM_OR_SIGNATURE = "system|signature";
    private static final String VAL_USER_ID = "userId";

    SenderFilter() {
    }

    static boolean isPrivilegedApp(PackageManagerInternal packageManagerInternal, int i, int i2) {
        if (i == 1000 || i == 0 || i2 == Process.myPid() || i2 == 0) {
            return true;
        }
        return packageManagerInternal.isUidPrivileged(i);
    }
}
