package com.android.server.display.config;

import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Thresholds {
    private BrightnessThresholds brighteningThresholds;
    private BrightnessThresholds darkeningThresholds;

    public final BrightnessThresholds getBrighteningThresholds() {
        return this.brighteningThresholds;
    }

    boolean hasBrighteningThresholds() {
        return this.brighteningThresholds != null;
    }

    public final void setBrighteningThresholds(BrightnessThresholds brightnessThresholds) {
        this.brighteningThresholds = brightnessThresholds;
    }

    public final BrightnessThresholds getDarkeningThresholds() {
        return this.darkeningThresholds;
    }

    boolean hasDarkeningThresholds() {
        return this.darkeningThresholds != null;
    }

    public final void setDarkeningThresholds(BrightnessThresholds brightnessThresholds) {
        this.darkeningThresholds = brightnessThresholds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Thresholds read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        Thresholds thresholds = new Thresholds();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("brighteningThresholds")) {
                    thresholds.setBrighteningThresholds(BrightnessThresholds.read(xmlPullParser));
                } else if (name.equals("darkeningThresholds")) {
                    thresholds.setDarkeningThresholds(BrightnessThresholds.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return thresholds;
        }
        throw new DatatypeConfigurationException("Thresholds is not closed");
    }
}
