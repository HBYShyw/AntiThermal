package com.android.server.policy.devicestate.config;

import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CameraCondition {
    private Boolean open;

    public boolean getOpen() {
        Boolean bool = this.open;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasOpen() {
        return this.open != null;
    }

    public void setOpen(boolean z) {
        this.open = Boolean.valueOf(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CameraCondition read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        CameraCondition cameraCondition = new CameraCondition();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("open")) {
                    cameraCondition.setOpen(Boolean.parseBoolean(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return cameraCondition;
        }
        throw new DatatypeConfigurationException("CameraCondition is not closed");
    }
}
