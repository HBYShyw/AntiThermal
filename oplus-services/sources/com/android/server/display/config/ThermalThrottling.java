package com.android.server.display.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ThermalThrottling {
    private List<BrightnessThrottlingMap> brightnessThrottlingMap;
    private List<RefreshRateThrottlingMap> refreshRateThrottlingMap;

    public final List<BrightnessThrottlingMap> getBrightnessThrottlingMap() {
        if (this.brightnessThrottlingMap == null) {
            this.brightnessThrottlingMap = new ArrayList();
        }
        return this.brightnessThrottlingMap;
    }

    public final List<RefreshRateThrottlingMap> getRefreshRateThrottlingMap() {
        if (this.refreshRateThrottlingMap == null) {
            this.refreshRateThrottlingMap = new ArrayList();
        }
        return this.refreshRateThrottlingMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ThermalThrottling read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        ThermalThrottling thermalThrottling = new ThermalThrottling();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("brightnessThrottlingMap")) {
                    thermalThrottling.getBrightnessThrottlingMap().add(BrightnessThrottlingMap.read(xmlPullParser));
                } else if (name.equals("refreshRateThrottlingMap")) {
                    thermalThrottling.getRefreshRateThrottlingMap().add(RefreshRateThrottlingMap.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return thermalThrottling;
        }
        throw new DatatypeConfigurationException("ThermalThrottling is not closed");
    }
}
