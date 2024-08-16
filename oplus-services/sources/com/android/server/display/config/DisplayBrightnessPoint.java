package com.android.server.display.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayBrightnessPoint {
    private BigInteger lux;
    private BigDecimal nits;

    public final BigInteger getLux() {
        return this.lux;
    }

    boolean hasLux() {
        return this.lux != null;
    }

    public final void setLux(BigInteger bigInteger) {
        this.lux = bigInteger;
    }

    public final BigDecimal getNits() {
        return this.nits;
    }

    boolean hasNits() {
        return this.nits != null;
    }

    public final void setNits(BigDecimal bigDecimal) {
        this.nits = bigDecimal;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayBrightnessPoint read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        DisplayBrightnessPoint displayBrightnessPoint = new DisplayBrightnessPoint();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("lux")) {
                    displayBrightnessPoint.setLux(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("nits")) {
                    displayBrightnessPoint.setNits(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return displayBrightnessPoint;
        }
        throw new DatatypeConfigurationException("DisplayBrightnessPoint is not closed");
    }
}
