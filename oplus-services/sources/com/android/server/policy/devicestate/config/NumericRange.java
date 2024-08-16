package com.android.server.policy.devicestate.config;

import java.io.IOException;
import java.math.BigDecimal;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NumericRange {
    private BigDecimal maxInclusive_optional;
    private BigDecimal max_optional;
    private BigDecimal minInclusive_optional;
    private BigDecimal min_optional;

    public BigDecimal getMin_optional() {
        return this.min_optional;
    }

    boolean hasMin_optional() {
        return this.min_optional != null;
    }

    public void setMin_optional(BigDecimal bigDecimal) {
        this.min_optional = bigDecimal;
    }

    public BigDecimal getMinInclusive_optional() {
        return this.minInclusive_optional;
    }

    boolean hasMinInclusive_optional() {
        return this.minInclusive_optional != null;
    }

    public void setMinInclusive_optional(BigDecimal bigDecimal) {
        this.minInclusive_optional = bigDecimal;
    }

    public BigDecimal getMax_optional() {
        return this.max_optional;
    }

    boolean hasMax_optional() {
        return this.max_optional != null;
    }

    public void setMax_optional(BigDecimal bigDecimal) {
        this.max_optional = bigDecimal;
    }

    public BigDecimal getMaxInclusive_optional() {
        return this.maxInclusive_optional;
    }

    boolean hasMaxInclusive_optional() {
        return this.maxInclusive_optional != null;
    }

    public void setMaxInclusive_optional(BigDecimal bigDecimal) {
        this.maxInclusive_optional = bigDecimal;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NumericRange read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        NumericRange numericRange = new NumericRange();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("min")) {
                    numericRange.setMin_optional(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("min-inclusive")) {
                    numericRange.setMinInclusive_optional(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("max")) {
                    numericRange.setMax_optional(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("max-inclusive")) {
                    numericRange.setMaxInclusive_optional(new BigDecimal(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return numericRange;
        }
        throw new DatatypeConfigurationException("NumericRange is not closed");
    }
}
