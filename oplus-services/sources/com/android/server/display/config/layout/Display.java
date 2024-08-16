package com.android.server.display.config.layout;

import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Display {
    private BigInteger address;
    private String brightnessThrottlingMapId;
    private Boolean defaultDisplay;
    private String displayGroup;
    private Boolean enabled;
    private String position;
    private String refreshRateThermalThrottlingMapId;
    private String refreshRateZoneId;

    public BigInteger getAddress() {
        return this.address;
    }

    boolean hasAddress() {
        return this.address != null;
    }

    public void setAddress(BigInteger bigInteger) {
        this.address = bigInteger;
    }

    public String getPosition() {
        return this.position;
    }

    boolean hasPosition() {
        return this.position != null;
    }

    public void setPosition(String str) {
        this.position = str;
    }

    public String getBrightnessThrottlingMapId() {
        return this.brightnessThrottlingMapId;
    }

    boolean hasBrightnessThrottlingMapId() {
        return this.brightnessThrottlingMapId != null;
    }

    public void setBrightnessThrottlingMapId(String str) {
        this.brightnessThrottlingMapId = str;
    }

    public String getRefreshRateThermalThrottlingMapId() {
        return this.refreshRateThermalThrottlingMapId;
    }

    boolean hasRefreshRateThermalThrottlingMapId() {
        return this.refreshRateThermalThrottlingMapId != null;
    }

    public void setRefreshRateThermalThrottlingMapId(String str) {
        this.refreshRateThermalThrottlingMapId = str;
    }

    public boolean isEnabled() {
        Boolean bool = this.enabled;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasEnabled() {
        return this.enabled != null;
    }

    public void setEnabled(boolean z) {
        this.enabled = Boolean.valueOf(z);
    }

    public boolean isDefaultDisplay() {
        Boolean bool = this.defaultDisplay;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasDefaultDisplay() {
        return this.defaultDisplay != null;
    }

    public void setDefaultDisplay(boolean z) {
        this.defaultDisplay = Boolean.valueOf(z);
    }

    public String getRefreshRateZoneId() {
        return this.refreshRateZoneId;
    }

    boolean hasRefreshRateZoneId() {
        return this.refreshRateZoneId != null;
    }

    public void setRefreshRateZoneId(String str) {
        this.refreshRateZoneId = str;
    }

    public String getDisplayGroup() {
        return this.displayGroup;
    }

    boolean hasDisplayGroup() {
        return this.displayGroup != null;
    }

    public void setDisplayGroup(String str) {
        this.displayGroup = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Display read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        Display display = new Display();
        String attributeValue = xmlPullParser.getAttributeValue(null, "enabled");
        if (attributeValue != null) {
            display.setEnabled(Boolean.parseBoolean(attributeValue));
        }
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "defaultDisplay");
        if (attributeValue2 != null) {
            display.setDefaultDisplay(Boolean.parseBoolean(attributeValue2));
        }
        String attributeValue3 = xmlPullParser.getAttributeValue(null, "refreshRateZoneId");
        if (attributeValue3 != null) {
            display.setRefreshRateZoneId(attributeValue3);
        }
        String attributeValue4 = xmlPullParser.getAttributeValue(null, "displayGroup");
        if (attributeValue4 != null) {
            display.setDisplayGroup(attributeValue4);
        }
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("address")) {
                    display.setAddress(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("position")) {
                    display.setPosition(XmlParser.readText(xmlPullParser));
                } else if (name.equals("brightnessThrottlingMapId")) {
                    display.setBrightnessThrottlingMapId(XmlParser.readText(xmlPullParser));
                } else if (name.equals("refreshRateThermalThrottlingMapId")) {
                    display.setRefreshRateThermalThrottlingMapId(XmlParser.readText(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return display;
        }
        throw new DatatypeConfigurationException("Display is not closed");
    }
}
