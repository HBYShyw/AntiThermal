package com.android.server.display.config;

import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HbmTiming {
    private BigInteger timeMaxSecs_all;
    private BigInteger timeMinSecs_all;
    private BigInteger timeWindowSecs_all;

    public final BigInteger getTimeWindowSecs_all() {
        return this.timeWindowSecs_all;
    }

    boolean hasTimeWindowSecs_all() {
        return this.timeWindowSecs_all != null;
    }

    public final void setTimeWindowSecs_all(BigInteger bigInteger) {
        this.timeWindowSecs_all = bigInteger;
    }

    public final BigInteger getTimeMaxSecs_all() {
        return this.timeMaxSecs_all;
    }

    boolean hasTimeMaxSecs_all() {
        return this.timeMaxSecs_all != null;
    }

    public final void setTimeMaxSecs_all(BigInteger bigInteger) {
        this.timeMaxSecs_all = bigInteger;
    }

    public final BigInteger getTimeMinSecs_all() {
        return this.timeMinSecs_all;
    }

    boolean hasTimeMinSecs_all() {
        return this.timeMinSecs_all != null;
    }

    public final void setTimeMinSecs_all(BigInteger bigInteger) {
        this.timeMinSecs_all = bigInteger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HbmTiming read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        HbmTiming hbmTiming = new HbmTiming();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("timeWindowSecs")) {
                    hbmTiming.setTimeWindowSecs_all(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("timeMaxSecs")) {
                    hbmTiming.setTimeMaxSecs_all(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("timeMinSecs")) {
                    hbmTiming.setTimeMinSecs_all(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return hbmTiming;
        }
        throw new DatatypeConfigurationException("HbmTiming is not closed");
    }
}
