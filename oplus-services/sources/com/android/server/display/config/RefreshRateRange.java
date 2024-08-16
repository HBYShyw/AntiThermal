package com.android.server.display.config;

import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RefreshRateRange {
    private BigInteger maximum;
    private BigInteger minimum;

    public final BigInteger getMinimum() {
        return this.minimum;
    }

    boolean hasMinimum() {
        return this.minimum != null;
    }

    public final void setMinimum(BigInteger bigInteger) {
        this.minimum = bigInteger;
    }

    public final BigInteger getMaximum() {
        return this.maximum;
    }

    boolean hasMaximum() {
        return this.maximum != null;
    }

    public final void setMaximum(BigInteger bigInteger) {
        this.maximum = bigInteger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RefreshRateRange read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        RefreshRateRange refreshRateRange = new RefreshRateRange();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("minimum")) {
                    refreshRateRange.setMinimum(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("maximum")) {
                    refreshRateRange.setMaximum(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return refreshRateRange;
        }
        throw new DatatypeConfigurationException("RefreshRateRange is not closed");
    }
}
