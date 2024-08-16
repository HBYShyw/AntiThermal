package com.android.server.firewall;

import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class FilterList implements Filter {
    protected final ArrayList<Filter> children = new ArrayList<>();

    public FilterList readFromXml(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        int depth = xmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            readChild(xmlPullParser);
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readChild(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        this.children.add(IntentFirewall.parseFilter(xmlPullParser));
    }
}
