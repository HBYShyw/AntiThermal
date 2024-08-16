package com.android.server.compat.overrides;

import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OverrideValue {
    private Boolean enabled;
    private String packageName;

    public String getPackageName() {
        return this.packageName;
    }

    boolean hasPackageName() {
        return this.packageName != null;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public boolean getEnabled() {
        Boolean bool = this.enabled;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasEnabled() {
        return this.enabled != null;
    }

    public void setEnabled(boolean z) {
        this.enabled = Boolean.valueOf(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OverrideValue read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        OverrideValue overrideValue = new OverrideValue();
        String attributeValue = xmlPullParser.getAttributeValue(null, "packageName");
        if (attributeValue != null) {
            overrideValue.setPackageName(attributeValue);
        }
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "enabled");
        if (attributeValue2 != null) {
            overrideValue.setEnabled(Boolean.parseBoolean(attributeValue2));
        }
        XmlParser.skip(xmlPullParser);
        return overrideValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(XmlWriter xmlWriter, String str) throws IOException {
        xmlWriter.print("<" + str);
        if (hasPackageName()) {
            xmlWriter.print(" packageName=\"");
            xmlWriter.print(getPackageName());
            xmlWriter.print("\"");
        }
        if (hasEnabled()) {
            xmlWriter.print(" enabled=\"");
            xmlWriter.print(Boolean.toString(getEnabled()));
            xmlWriter.print("\"");
        }
        xmlWriter.print(">\n");
        xmlWriter.print("</" + str + ">\n");
    }
}
