package com.android.server.display.config;

import java.io.IOException;
import java.math.BigDecimal;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Point {
    private BigDecimal nits;
    private BigDecimal value;

    public final BigDecimal getValue() {
        return this.value;
    }

    boolean hasValue() {
        return this.value != null;
    }

    public final void setValue(BigDecimal bigDecimal) {
        this.value = bigDecimal;
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
    public static Point read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        Point point = new Point();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("value")) {
                    point.setValue(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("nits")) {
                    point.setNits(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return point;
        }
        throw new DatatypeConfigurationException("Point is not closed");
    }
}
