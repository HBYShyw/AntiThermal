package com.android.server.display.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SdrHdrRatioMap {
    private List<SdrHdrRatioPoint> point;

    public final List<SdrHdrRatioPoint> getPoint() {
        if (this.point == null) {
            this.point = new ArrayList();
        }
        return this.point;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SdrHdrRatioMap read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        SdrHdrRatioMap sdrHdrRatioMap = new SdrHdrRatioMap();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("point")) {
                    sdrHdrRatioMap.getPoint().add(SdrHdrRatioPoint.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return sdrHdrRatioMap;
        }
        throw new DatatypeConfigurationException("SdrHdrRatioMap is not closed");
    }
}
