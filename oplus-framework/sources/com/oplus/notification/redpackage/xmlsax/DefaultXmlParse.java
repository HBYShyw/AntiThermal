package com.oplus.notification.redpackage.xmlsax;

import android.util.Log;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
public class DefaultXmlParse extends XmlParse {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    String mTemp = "";
    XmlImpl mXml;
    XMLReader mXmlReader;

    public DefaultXmlParse() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            this.mXmlReader = parser.getXMLReader();
        } catch (ParserConfigurationException e) {
            Log.e(TAG, "ParserConfigurationException ", e);
        } catch (SAXException e2) {
            Log.e(TAG, "SAXException ", e2);
        }
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlParse
    public Xml parse(InputStream is) {
        try {
            this.mXmlReader.setContentHandler(new XMLParseHandler());
            this.mXmlReader.parse(new InputSource(is));
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "parse stream: " + e.getMessage());
            }
        }
        return this.mXml;
    }

    @Override // com.oplus.notification.redpackage.xmlsax.XmlParse
    public Xml parse(String s) {
        try {
            this.mXmlReader.setContentHandler(new XMLParseHandler());
            InputSource is = new InputSource(new StringReader(s));
            this.mXmlReader.parse(is);
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "parse string: " + e.getMessage());
                Log.d(TAG, "parse string: " + s);
            }
        }
        return this.mXml;
    }

    /* loaded from: classes.dex */
    class XMLParseHandler extends DefaultHandler {
        XmlNodeImpl mXmlNode;

        XMLParseHandler() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            DefaultXmlParse.this.mTemp = "";
            if (this.mXmlNode == null) {
                this.mXmlNode = new XmlNodeImpl(localName);
            } else {
                XmlNodeImpl node = new XmlNodeImpl(localName);
                this.mXmlNode.addChildNode(node);
                this.mXmlNode = node;
            }
            if (atts == null) {
                return;
            }
            for (int i = 0; i < atts.getLength(); i++) {
                String aName = atts.getLocalName(i);
                XmlAttributeImpl attribute = new XmlAttributeImpl(aName);
                attribute.setValue(atts.getValue(aName));
                this.mXmlNode.addAttributes(attribute);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
            String value = new String(ch, start, length);
            if (!"".equals(value)) {
                StringBuilder sb = new StringBuilder();
                DefaultXmlParse defaultXmlParse = DefaultXmlParse.this;
                defaultXmlParse.mTemp = sb.append(defaultXmlParse.mTemp).append(value).toString();
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            this.mXmlNode.setValue(DefaultXmlParse.this.mTemp);
            DefaultXmlParse.this.mTemp = "";
            XmlNodeImpl node = (XmlNodeImpl) this.mXmlNode.getParentNode();
            if (node == null) {
                return;
            }
            this.mXmlNode = node;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endDocument() throws SAXException {
            if (DefaultXmlParse.this.mXml == null) {
                DefaultXmlParse.this.mXml = new XmlImpl();
            }
            DefaultXmlParse.this.mXml.setmXmlNode(this.mXmlNode);
        }
    }
}
