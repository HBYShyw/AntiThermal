package com.android.server.display.config;

import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AutoBrightness {
    private BigInteger brighteningLightDebounceMillis;
    private BigInteger darkeningLightDebounceMillis;
    private DisplayBrightnessMapping displayBrightnessMapping;
    private Boolean enabled;

    public final BigInteger getBrighteningLightDebounceMillis() {
        return this.brighteningLightDebounceMillis;
    }

    boolean hasBrighteningLightDebounceMillis() {
        return this.brighteningLightDebounceMillis != null;
    }

    public final void setBrighteningLightDebounceMillis(BigInteger bigInteger) {
        this.brighteningLightDebounceMillis = bigInteger;
    }

    public final BigInteger getDarkeningLightDebounceMillis() {
        return this.darkeningLightDebounceMillis;
    }

    boolean hasDarkeningLightDebounceMillis() {
        return this.darkeningLightDebounceMillis != null;
    }

    public final void setDarkeningLightDebounceMillis(BigInteger bigInteger) {
        this.darkeningLightDebounceMillis = bigInteger;
    }

    public final DisplayBrightnessMapping getDisplayBrightnessMapping() {
        return this.displayBrightnessMapping;
    }

    boolean hasDisplayBrightnessMapping() {
        return this.displayBrightnessMapping != null;
    }

    public final void setDisplayBrightnessMapping(DisplayBrightnessMapping displayBrightnessMapping) {
        this.displayBrightnessMapping = displayBrightnessMapping;
    }

    public boolean getEnabled() {
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AutoBrightness read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        AutoBrightness autoBrightness = new AutoBrightness();
        String attributeValue = xmlPullParser.getAttributeValue(null, "enabled");
        if (attributeValue != null) {
            autoBrightness.setEnabled(Boolean.parseBoolean(attributeValue));
        }
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("brighteningLightDebounceMillis")) {
                    autoBrightness.setBrighteningLightDebounceMillis(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("darkeningLightDebounceMillis")) {
                    autoBrightness.setDarkeningLightDebounceMillis(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("displayBrightnessMapping")) {
                    autoBrightness.setDisplayBrightnessMapping(DisplayBrightnessMapping.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return autoBrightness;
        }
        throw new DatatypeConfigurationException("AutoBrightness is not closed");
    }
}
