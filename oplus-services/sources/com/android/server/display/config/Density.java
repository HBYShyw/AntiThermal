package com.android.server.display.config;

import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Density {
    private BigInteger density;
    private BigInteger height;
    private BigInteger width;

    public final BigInteger getWidth() {
        return this.width;
    }

    boolean hasWidth() {
        return this.width != null;
    }

    public final void setWidth(BigInteger bigInteger) {
        this.width = bigInteger;
    }

    public final BigInteger getHeight() {
        return this.height;
    }

    boolean hasHeight() {
        return this.height != null;
    }

    public final void setHeight(BigInteger bigInteger) {
        this.height = bigInteger;
    }

    public final BigInteger getDensity() {
        return this.density;
    }

    boolean hasDensity() {
        return this.density != null;
    }

    public final void setDensity(BigInteger bigInteger) {
        this.density = bigInteger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Density read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        Density density = new Density();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("width")) {
                    density.setWidth(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("height")) {
                    density.setHeight(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("density")) {
                    density.setDensity(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return density;
        }
        throw new DatatypeConfigurationException("Density is not closed");
    }
}
