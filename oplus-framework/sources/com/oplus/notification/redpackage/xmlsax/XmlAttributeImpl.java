package com.oplus.notification.redpackage.xmlsax;

/* loaded from: classes.dex */
class XmlAttributeImpl implements XmlAttribute {
    private String mName;
    private XmlNodeImpl mParent;
    private String mValue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public XmlAttributeImpl(String name) {
        this.mName = name;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setValue(String newValue) {
        this.mValue = newValue;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlAttribute
    public String getName() {
        return this.mName;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlAttribute
    public String getValue() {
        return this.mValue;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlAttribute
    public XmlNode getParentNode() {
        return this.mParent;
    }

    public void setParentNode(XmlNodeImpl parent) {
        this.mParent = parent;
    }
}
