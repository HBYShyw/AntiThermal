package com.oplus.notification.redpackage.xmlsax;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
class XmlNodeImpl implements XmlNode {
    private List<XmlAttribute> mAttributes;
    private String mName;
    private List<XmlNode> mNodes;
    private XmlNodeImpl mParent;
    private String mValue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public XmlNodeImpl(String name) {
        this.mName = name;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addChildNode(XmlNodeImpl xmlNode) {
        if (this.mNodes == null) {
            this.mNodes = new ArrayList();
        }
        this.mNodes.add(xmlNode);
        xmlNode.mParent = this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addAttributes(XmlAttributeImpl xmlAttribute) {
        if (this.mAttributes == null) {
            this.mAttributes = new ArrayList();
        }
        this.mAttributes.add(xmlAttribute);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setValue(String value) {
        this.mValue = value.trim();
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlAttribute
    public String getName() {
        return this.mName;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlAttribute
    public String getValue() {
        return this.mValue;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlNode
    public XmlNode[] getChildNodes(String nodeName) {
        if (this.mNodes == null) {
            return null;
        }
        List<XmlNode> nodes = new ArrayList<>();
        for (XmlNode node : this.mNodes) {
            if (node.getName().equals(nodeName)) {
                nodes.add(node);
            }
        }
        XmlNode[] nodeArray = new XmlNode[nodes.size()];
        if (nodeArray.length <= 0) {
            return nodeArray;
        }
        nodes.toArray(nodeArray);
        return nodeArray;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlNode
    public XmlNode getChildNode(String nodeName) {
        List<XmlNode> list = this.mNodes;
        if (list == null) {
            return null;
        }
        for (XmlNode node : list) {
            if (node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlNode
    public XmlNode[] getAllChildNodes() {
        List<XmlNode> list = this.mNodes;
        if (list == null) {
            return new XmlNode[0];
        }
        XmlNode[] xmlNodes = new XmlNode[list.size()];
        if (xmlNodes.length <= 0) {
            return xmlNodes;
        }
        this.mNodes.toArray(xmlNodes);
        return xmlNodes;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlNode
    public XmlAttribute getAttribute(String attName) {
        List<XmlAttribute> list = this.mAttributes;
        if (list == null) {
            return null;
        }
        for (XmlAttribute att : list) {
            if (att.getName().equals(attName)) {
                return att;
            }
        }
        return null;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlNode
    public XmlAttribute[] getAllAttributes() {
        List<XmlAttribute> list = this.mAttributes;
        if (list == null) {
            return new XmlAttribute[0];
        }
        XmlAttribute[] xmlAtrributes = new XmlAttribute[list.size()];
        if (xmlAtrributes.length <= 0) {
            return xmlAtrributes;
        }
        this.mAttributes.toArray(xmlAtrributes);
        return xmlAtrributes;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlNode
    public int numOfAllChildNodes() {
        List<XmlNode> list = this.mNodes;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlNode
    public int numOfAllAttributes() {
        List<XmlAttribute> list = this.mAttributes;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlAttribute
    public XmlNode getParentNode() {
        return this.mParent;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlNode
    public String toXml() {
        return null;
    }
}
