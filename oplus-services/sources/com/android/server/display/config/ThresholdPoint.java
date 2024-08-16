package com.android.server.display.config;

import java.io.IOException;
import java.math.BigDecimal;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ThresholdPoint {
    private BigDecimal percentage;
    private BigDecimal threshold;

    public final BigDecimal getThreshold() {
        return this.threshold;
    }

    boolean hasThreshold() {
        return this.threshold != null;
    }

    public final void setThreshold(BigDecimal bigDecimal) {
        this.threshold = bigDecimal;
    }

    public final BigDecimal getPercentage() {
        return this.percentage;
    }

    boolean hasPercentage() {
        return this.percentage != null;
    }

    public final void setPercentage(BigDecimal bigDecimal) {
        this.percentage = bigDecimal;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ThresholdPoint read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        ThresholdPoint thresholdPoint = new ThresholdPoint();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("threshold")) {
                    thresholdPoint.setThreshold(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("percentage")) {
                    thresholdPoint.setPercentage(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return thresholdPoint;
        }
        throw new DatatypeConfigurationException("ThresholdPoint is not closed");
    }
}
