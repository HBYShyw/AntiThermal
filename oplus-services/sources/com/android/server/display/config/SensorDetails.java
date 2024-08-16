package com.android.server.display.config;

import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SensorDetails {
    private String name;
    private RefreshRateRange refreshRate;
    private String type;

    public final String getType() {
        return this.type;
    }

    boolean hasType() {
        return this.type != null;
    }

    public final void setType(String str) {
        this.type = str;
    }

    public final String getName() {
        return this.name;
    }

    boolean hasName() {
        return this.name != null;
    }

    public final void setName(String str) {
        this.name = str;
    }

    public final RefreshRateRange getRefreshRate() {
        return this.refreshRate;
    }

    boolean hasRefreshRate() {
        return this.refreshRate != null;
    }

    public final void setRefreshRate(RefreshRateRange refreshRateRange) {
        this.refreshRate = refreshRateRange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SensorDetails read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        SensorDetails sensorDetails = new SensorDetails();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("type")) {
                    sensorDetails.setType(XmlParser.readText(xmlPullParser));
                } else if (name.equals("name")) {
                    sensorDetails.setName(XmlParser.readText(xmlPullParser));
                } else if (name.equals("refreshRate")) {
                    sensorDetails.setRefreshRate(RefreshRateRange.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return sensorDetails;
        }
        throw new DatatypeConfigurationException("SensorDetails is not closed");
    }
}
