package com.android.server.policy.devicestate.config;

import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class KeyguardCondition {
    private Boolean show;

    public boolean getShow() {
        Boolean bool = this.show;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasShow() {
        return this.show != null;
    }

    public void setShow(boolean z) {
        this.show = Boolean.valueOf(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static KeyguardCondition read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        KeyguardCondition keyguardCondition = new KeyguardCondition();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("show")) {
                    keyguardCondition.setShow(Boolean.parseBoolean(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return keyguardCondition;
        }
        throw new DatatypeConfigurationException("KeyguardCondition is not closed");
    }
}
