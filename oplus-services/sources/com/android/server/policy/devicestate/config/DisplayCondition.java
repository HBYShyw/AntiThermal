package com.android.server.policy.devicestate.config;

import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DisplayCondition {
    private BigInteger displayId;
    private Boolean displayOn;

    public boolean getDisplayOn() {
        Boolean bool = this.displayOn;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasDisplayOn() {
        return this.displayOn != null;
    }

    public void setDisplayOn(boolean z) {
        this.displayOn = Boolean.valueOf(z);
    }

    public BigInteger getDisplayId() {
        return this.displayId;
    }

    boolean hasDisplayId() {
        return this.displayId != null;
    }

    public void setDisplayId(BigInteger bigInteger) {
        this.displayId = bigInteger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayCondition read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        DisplayCondition displayCondition = new DisplayCondition();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("displayOn")) {
                    displayCondition.setDisplayOn(Boolean.parseBoolean(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("displayId")) {
                    displayCondition.setDisplayId(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return displayCondition;
        }
        throw new DatatypeConfigurationException("DisplayCondition is not closed");
    }
}
