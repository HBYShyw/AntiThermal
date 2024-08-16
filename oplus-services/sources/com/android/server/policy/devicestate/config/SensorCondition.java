package com.android.server.policy.devicestate.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SensorCondition {
    private String name;
    private String type;
    private Boolean unregister;
    private List<NumericRange> value;

    public String getType() {
        return this.type;
    }

    boolean hasType() {
        return this.type != null;
    }

    public void setType(String str) {
        this.type = str;
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

    public List<NumericRange> getValue() {
        if (this.value == null) {
            this.value = new ArrayList();
        }
        return this.value;
    }

    public boolean getUnregister() {
        Boolean bool = this.unregister;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    boolean hasUnregister() {
        return this.unregister != null;
    }

    public void setUnregister(boolean z) {
        this.unregister = Boolean.valueOf(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SensorCondition read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        SensorCondition sensorCondition = new SensorCondition();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("type")) {
                    sensorCondition.setType(XmlParser.readText(xmlPullParser));
                } else if (name.equals("name")) {
                    sensorCondition.setName(XmlParser.readText(xmlPullParser));
                } else if (name.equals("value")) {
                    sensorCondition.getValue().add(NumericRange.read(xmlPullParser));
                } else if (name.equals("unregister")) {
                    sensorCondition.setUnregister(Boolean.parseBoolean(XmlParser.readText(xmlPullParser)));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return sensorCondition;
        }
        throw new DatatypeConfigurationException("SensorCondition is not closed");
    }
}
