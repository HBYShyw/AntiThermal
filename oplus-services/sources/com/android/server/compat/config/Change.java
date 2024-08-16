package com.android.server.compat.config;

import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Change {
    private String description;
    private Boolean disabled;
    private Integer enableAfterTargetSdk;
    private Integer enableSinceTargetSdk;
    private Long id;
    private Boolean loggingOnly;
    private String name;
    private Boolean overridable;
    private String value;

    public long getId() {
        Long l = this.id;
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    boolean hasId() {
        return this.id != null;
    }

    public void setId(long j) {
        this.id = Long.valueOf(j);
    }

    public String getName() {
        return this.name;
    }

    boolean hasName() {
        return this.name != null;
    }

    public void setName(String str) {
        this.name = str;
    }

    public boolean getDisabled() {
        Boolean bool = this.disabled;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasDisabled() {
        return this.disabled != null;
    }

    public void setDisabled(boolean z) {
        this.disabled = Boolean.valueOf(z);
    }

    public boolean getLoggingOnly() {
        Boolean bool = this.loggingOnly;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasLoggingOnly() {
        return this.loggingOnly != null;
    }

    public void setLoggingOnly(boolean z) {
        this.loggingOnly = Boolean.valueOf(z);
    }

    public int getEnableAfterTargetSdk() {
        Integer num = this.enableAfterTargetSdk;
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    boolean hasEnableAfterTargetSdk() {
        return this.enableAfterTargetSdk != null;
    }

    public void setEnableAfterTargetSdk(int i) {
        this.enableAfterTargetSdk = Integer.valueOf(i);
    }

    public int getEnableSinceTargetSdk() {
        Integer num = this.enableSinceTargetSdk;
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    boolean hasEnableSinceTargetSdk() {
        return this.enableSinceTargetSdk != null;
    }

    public void setEnableSinceTargetSdk(int i) {
        this.enableSinceTargetSdk = Integer.valueOf(i);
    }

    public String getDescription() {
        return this.description;
    }

    boolean hasDescription() {
        return this.description != null;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public boolean getOverridable() {
        Boolean bool = this.overridable;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasOverridable() {
        return this.overridable != null;
    }

    public void setOverridable(boolean z) {
        this.overridable = Boolean.valueOf(z);
    }

    public String getValue() {
        return this.value;
    }

    boolean hasValue() {
        return this.value != null;
    }

    public void setValue(String str) {
        this.value = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Change read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        Change change = new Change();
        String attributeValue = xmlPullParser.getAttributeValue(null, "id");
        if (attributeValue != null) {
            change.setId(Long.parseLong(attributeValue));
        }
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "name");
        if (attributeValue2 != null) {
            change.setName(attributeValue2);
        }
        String attributeValue3 = xmlPullParser.getAttributeValue(null, "disabled");
        if (attributeValue3 != null) {
            change.setDisabled(Boolean.parseBoolean(attributeValue3));
        }
        String attributeValue4 = xmlPullParser.getAttributeValue(null, "loggingOnly");
        if (attributeValue4 != null) {
            change.setLoggingOnly(Boolean.parseBoolean(attributeValue4));
        }
        String attributeValue5 = xmlPullParser.getAttributeValue(null, "enableAfterTargetSdk");
        if (attributeValue5 != null) {
            change.setEnableAfterTargetSdk(Integer.parseInt(attributeValue5));
        }
        String attributeValue6 = xmlPullParser.getAttributeValue(null, "enableSinceTargetSdk");
        if (attributeValue6 != null) {
            change.setEnableSinceTargetSdk(Integer.parseInt(attributeValue6));
        }
        String attributeValue7 = xmlPullParser.getAttributeValue(null, "description");
        if (attributeValue7 != null) {
            change.setDescription(attributeValue7);
        }
        String attributeValue8 = xmlPullParser.getAttributeValue(null, "overridable");
        if (attributeValue8 != null) {
            change.setOverridable(Boolean.parseBoolean(attributeValue8));
        }
        String readText = XmlParser.readText(xmlPullParser);
        if (readText != null) {
            change.setValue(readText);
        }
        return change;
    }
}
