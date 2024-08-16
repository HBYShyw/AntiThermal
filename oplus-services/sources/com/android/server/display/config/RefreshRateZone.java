package com.android.server.display.config;

import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RefreshRateZone {
    private String id;
    private RefreshRateRange refreshRateRange;

    public final RefreshRateRange getRefreshRateRange() {
        return this.refreshRateRange;
    }

    boolean hasRefreshRateRange() {
        return this.refreshRateRange != null;
    }

    public final void setRefreshRateRange(RefreshRateRange refreshRateRange) {
        this.refreshRateRange = refreshRateRange;
    }

    public String getId() {
        return this.id;
    }

    boolean hasId() {
        return this.id != null;
    }

    public void setId(String str) {
        this.id = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RefreshRateZone read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        RefreshRateZone refreshRateZone = new RefreshRateZone();
        String attributeValue = xmlPullParser.getAttributeValue(null, "id");
        if (attributeValue != null) {
            refreshRateZone.setId(attributeValue);
        }
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("refreshRateRange")) {
                    refreshRateZone.setRefreshRateRange(RefreshRateRange.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return refreshRateZone;
        }
        throw new DatatypeConfigurationException("RefreshRateZone is not closed");
    }
}
