package com.oplus.notification.redpackage.xmlsax;

/* loaded from: classes.dex */
public interface XmlNode extends XmlAttribute {
    XmlAttribute[] getAllAttributes();

    XmlNode[] getAllChildNodes();

    XmlAttribute getAttribute(String str);

    XmlNode getChildNode(String str);

    XmlNode[] getChildNodes(String str);

    int numOfAllAttributes();

    int numOfAllChildNodes();

    String toXml();
}
