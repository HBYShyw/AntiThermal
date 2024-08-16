package com.android.server.firewall;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class FilterFactory {
    private final String mTag;

    public abstract Filter newFilter(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException;

    /* JADX INFO: Access modifiers changed from: protected */
    public FilterFactory(String str) {
        str.getClass();
        this.mTag = str;
    }

    public String getTagName() {
        return this.mTag;
    }
}
