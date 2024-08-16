package com.android.server.display.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayBrightnessMapping {
    private List<DisplayBrightnessPoint> displayBrightnessPoint;

    public final List<DisplayBrightnessPoint> getDisplayBrightnessPoint() {
        if (this.displayBrightnessPoint == null) {
            this.displayBrightnessPoint = new ArrayList();
        }
        return this.displayBrightnessPoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayBrightnessMapping read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        DisplayBrightnessMapping displayBrightnessMapping = new DisplayBrightnessMapping();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("displayBrightnessPoint")) {
                    displayBrightnessMapping.getDisplayBrightnessPoint().add(DisplayBrightnessPoint.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return displayBrightnessMapping;
        }
        throw new DatatypeConfigurationException("DisplayBrightnessMapping is not closed");
    }
}
