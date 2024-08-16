package com.android.server.display.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayQuirks {
    private List<String> quirk;

    public List<String> getQuirk() {
        if (this.quirk == null) {
            this.quirk = new ArrayList();
        }
        return this.quirk;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayQuirks read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        DisplayQuirks displayQuirks = new DisplayQuirks();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("quirk")) {
                    displayQuirks.getQuirk().add(XmlParser.readText(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return displayQuirks;
        }
        throw new DatatypeConfigurationException("DisplayQuirks is not closed");
    }
}
