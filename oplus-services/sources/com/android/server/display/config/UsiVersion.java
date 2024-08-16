package com.android.server.display.config;

import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UsiVersion {
    private BigInteger majorVersion;
    private BigInteger minorVersion;

    public final BigInteger getMajorVersion() {
        return this.majorVersion;
    }

    boolean hasMajorVersion() {
        return this.majorVersion != null;
    }

    public final void setMajorVersion(BigInteger bigInteger) {
        this.majorVersion = bigInteger;
    }

    public final BigInteger getMinorVersion() {
        return this.minorVersion;
    }

    boolean hasMinorVersion() {
        return this.minorVersion != null;
    }

    public final void setMinorVersion(BigInteger bigInteger) {
        this.minorVersion = bigInteger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UsiVersion read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        UsiVersion usiVersion = new UsiVersion();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("majorVersion")) {
                    usiVersion.setMajorVersion(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("minorVersion")) {
                    usiVersion.setMinorVersion(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return usiVersion;
        }
        throw new DatatypeConfigurationException("UsiVersion is not closed");
    }
}
