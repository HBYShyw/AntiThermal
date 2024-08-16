package com.android.server.compat.overrides;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Overrides {
    private List<ChangeOverrides> changeOverrides;

    public List<ChangeOverrides> getChangeOverrides() {
        if (this.changeOverrides == null) {
            this.changeOverrides = new ArrayList();
        }
        return this.changeOverrides;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Overrides read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        Overrides overrides = new Overrides();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("change-overrides")) {
                    overrides.getChangeOverrides().add(ChangeOverrides.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return overrides;
        }
        throw new DatatypeConfigurationException("Overrides is not closed");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(XmlWriter xmlWriter, String str) throws IOException {
        xmlWriter.print("<" + str);
        xmlWriter.print(">\n");
        xmlWriter.increaseIndent();
        Iterator<ChangeOverrides> it = getChangeOverrides().iterator();
        while (it.hasNext()) {
            it.next().write(xmlWriter, "change-overrides");
        }
        xmlWriter.decreaseIndent();
        xmlWriter.print("</" + str + ">\n");
    }
}
