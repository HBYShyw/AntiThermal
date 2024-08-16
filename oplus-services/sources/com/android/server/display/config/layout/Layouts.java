package com.android.server.display.config.layout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Layouts {
    private List<Layout> layout;

    public List<Layout> getLayout() {
        if (this.layout == null) {
            this.layout = new ArrayList();
        }
        return this.layout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Layouts read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        Layouts layouts = new Layouts();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("layout")) {
                    layouts.getLayout().add(Layout.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return layouts;
        }
        throw new DatatypeConfigurationException("Layouts is not closed");
    }
}
