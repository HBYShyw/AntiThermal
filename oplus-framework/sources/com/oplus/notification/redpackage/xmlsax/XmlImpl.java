package com.oplus.notification.redpackage.xmlsax;

/* loaded from: classes.dex */
class XmlImpl implements Xml {
    private XmlNode mXmlNode;

    @Override // com.oplus.notification.redpackage.xmlsax.Xml
    public XmlNode getRootNode() {
        return this.mXmlNode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setmXmlNode(XmlNode mXmlNode) {
        this.mXmlNode = mXmlNode;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.Xml
    public String toXml() {
        return this.mXmlNode.toXml();
    }
}
