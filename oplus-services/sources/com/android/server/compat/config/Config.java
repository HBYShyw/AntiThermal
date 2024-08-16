package com.android.server.compat.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Config {
    private List<Change> compatChange;

    public List<Change> getCompatChange() {
        if (this.compatChange == null) {
            this.compatChange = new ArrayList();
        }
        return this.compatChange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Config read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        Config config = new Config();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("compat-change")) {
                    config.getCompatChange().add(Change.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return config;
        }
        throw new DatatypeConfigurationException("Config is not closed");
    }
}
