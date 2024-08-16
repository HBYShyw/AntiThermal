package com.android.server.compat.overrides;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ChangeOverrides {
    private Long changeId;
    private Deferred deferred;
    private Raw raw;
    private Validated validated;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Validated {
        private List<OverrideValue> overrideValue;

        public List<OverrideValue> getOverrideValue() {
            if (this.overrideValue == null) {
                this.overrideValue = new ArrayList();
            }
            return this.overrideValue;
        }

        static Validated read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
            int next;
            Validated validated = new Validated();
            xmlPullParser.getDepth();
            while (true) {
                next = xmlPullParser.next();
                if (next == 1 || next == 3) {
                    break;
                }
                if (xmlPullParser.getEventType() == 2) {
                    if (xmlPullParser.getName().equals("override-value")) {
                        validated.getOverrideValue().add(OverrideValue.read(xmlPullParser));
                    } else {
                        XmlParser.skip(xmlPullParser);
                    }
                }
            }
            if (next == 3) {
                return validated;
            }
            throw new DatatypeConfigurationException("ChangeOverrides.Validated is not closed");
        }

        void write(XmlWriter xmlWriter, String str) throws IOException {
            xmlWriter.print("<" + str);
            xmlWriter.print(">\n");
            xmlWriter.increaseIndent();
            Iterator<OverrideValue> it = getOverrideValue().iterator();
            while (it.hasNext()) {
                it.next().write(xmlWriter, "override-value");
            }
            xmlWriter.decreaseIndent();
            xmlWriter.print("</" + str + ">\n");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Deferred {
        private List<OverrideValue> overrideValue;

        public List<OverrideValue> getOverrideValue() {
            if (this.overrideValue == null) {
                this.overrideValue = new ArrayList();
            }
            return this.overrideValue;
        }

        static Deferred read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
            int next;
            Deferred deferred = new Deferred();
            xmlPullParser.getDepth();
            while (true) {
                next = xmlPullParser.next();
                if (next == 1 || next == 3) {
                    break;
                }
                if (xmlPullParser.getEventType() == 2) {
                    if (xmlPullParser.getName().equals("override-value")) {
                        deferred.getOverrideValue().add(OverrideValue.read(xmlPullParser));
                    } else {
                        XmlParser.skip(xmlPullParser);
                    }
                }
            }
            if (next == 3) {
                return deferred;
            }
            throw new DatatypeConfigurationException("ChangeOverrides.Deferred is not closed");
        }

        void write(XmlWriter xmlWriter, String str) throws IOException {
            xmlWriter.print("<" + str);
            xmlWriter.print(">\n");
            xmlWriter.increaseIndent();
            Iterator<OverrideValue> it = getOverrideValue().iterator();
            while (it.hasNext()) {
                it.next().write(xmlWriter, "override-value");
            }
            xmlWriter.decreaseIndent();
            xmlWriter.print("</" + str + ">\n");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Raw {
        private List<RawOverrideValue> rawOverrideValue;

        public List<RawOverrideValue> getRawOverrideValue() {
            if (this.rawOverrideValue == null) {
                this.rawOverrideValue = new ArrayList();
            }
            return this.rawOverrideValue;
        }

        static Raw read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
            int next;
            Raw raw = new Raw();
            xmlPullParser.getDepth();
            while (true) {
                next = xmlPullParser.next();
                if (next == 1 || next == 3) {
                    break;
                }
                if (xmlPullParser.getEventType() == 2) {
                    if (xmlPullParser.getName().equals("raw-override-value")) {
                        raw.getRawOverrideValue().add(RawOverrideValue.read(xmlPullParser));
                    } else {
                        XmlParser.skip(xmlPullParser);
                    }
                }
            }
            if (next == 3) {
                return raw;
            }
            throw new DatatypeConfigurationException("ChangeOverrides.Raw is not closed");
        }

        void write(XmlWriter xmlWriter, String str) throws IOException {
            xmlWriter.print("<" + str);
            xmlWriter.print(">\n");
            xmlWriter.increaseIndent();
            Iterator<RawOverrideValue> it = getRawOverrideValue().iterator();
            while (it.hasNext()) {
                it.next().write(xmlWriter, "raw-override-value");
            }
            xmlWriter.decreaseIndent();
            xmlWriter.print("</" + str + ">\n");
        }
    }

    public Validated getValidated() {
        return this.validated;
    }

    boolean hasValidated() {
        return this.validated != null;
    }

    public void setValidated(Validated validated) {
        this.validated = validated;
    }

    public Deferred getDeferred() {
        return this.deferred;
    }

    boolean hasDeferred() {
        return this.deferred != null;
    }

    public void setDeferred(Deferred deferred) {
        this.deferred = deferred;
    }

    public Raw getRaw() {
        return this.raw;
    }

    boolean hasRaw() {
        return this.raw != null;
    }

    public void setRaw(Raw raw) {
        this.raw = raw;
    }

    public long getChangeId() {
        Long l = this.changeId;
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    boolean hasChangeId() {
        return this.changeId != null;
    }

    public void setChangeId(long j) {
        this.changeId = Long.valueOf(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ChangeOverrides read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        ChangeOverrides changeOverrides = new ChangeOverrides();
        String attributeValue = xmlPullParser.getAttributeValue(null, "changeId");
        if (attributeValue != null) {
            changeOverrides.setChangeId(Long.parseLong(attributeValue));
        }
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("validated")) {
                    changeOverrides.setValidated(Validated.read(xmlPullParser));
                } else if (name.equals("deferred")) {
                    changeOverrides.setDeferred(Deferred.read(xmlPullParser));
                } else if (name.equals("raw")) {
                    changeOverrides.setRaw(Raw.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return changeOverrides;
        }
        throw new DatatypeConfigurationException("ChangeOverrides is not closed");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(XmlWriter xmlWriter, String str) throws IOException {
        xmlWriter.print("<" + str);
        if (hasChangeId()) {
            xmlWriter.print(" changeId=\"");
            xmlWriter.print(Long.toString(getChangeId()));
            xmlWriter.print("\"");
        }
        xmlWriter.print(">\n");
        xmlWriter.increaseIndent();
        if (hasValidated()) {
            getValidated().write(xmlWriter, "validated");
        }
        if (hasDeferred()) {
            getDeferred().write(xmlWriter, "deferred");
        }
        if (hasRaw()) {
            getRaw().write(xmlWriter, "raw");
        }
        xmlWriter.decreaseIndent();
        xmlWriter.print("</" + str + ">\n");
    }
}
