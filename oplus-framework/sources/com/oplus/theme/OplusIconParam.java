package com.oplus.theme;

import android.util.Log;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
public class OplusIconParam {
    private static final boolean LOGE = false;
    private static final String TAG = "OplusIconParam";
    private static final String TAG_DETECT_MASK_BORDER_OFFSET = "DetectMaskBorderOffset";
    private static final String TAG_SCALE = "Scale";
    private static final String TAG_XOFFSETPCT = "XOffsetPCT";
    private static final String TAG_YOFFSETPCT = "YOffsetPCT";
    public String mCurrentTag;
    public String mPath;
    public float mScale = 0.0f;
    public float mXOffsetPCT = 0.0f;
    public float mYOffsetPCT = 0.0f;
    public float mDetectMaskBorderOffset = 0.065f;

    public OplusIconParam(String path) {
        this.mPath = path;
    }

    public void myLog(String str) {
    }

    public String getPath() {
        return this.mPath;
    }

    /* loaded from: classes.dex */
    class ThemeXmlHandler extends DefaultHandler {
        ThemeXmlHandler() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            OplusIconParam.this.mCurrentTag = localName;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
            String value = new String(ch, start, length);
            if (OplusIconParam.TAG_SCALE.equals(OplusIconParam.this.mCurrentTag)) {
                OplusIconParam.this.mScale = Float.parseFloat(value);
                return;
            }
            if (OplusIconParam.TAG_XOFFSETPCT.equals(OplusIconParam.this.mCurrentTag)) {
                OplusIconParam.this.mXOffsetPCT = Float.parseFloat(value);
            } else if (OplusIconParam.TAG_YOFFSETPCT.equals(OplusIconParam.this.mCurrentTag)) {
                OplusIconParam.this.mYOffsetPCT = Float.parseFloat(value);
            } else if (OplusIconParam.TAG_DETECT_MASK_BORDER_OFFSET.equals(OplusIconParam.this.mCurrentTag)) {
                OplusIconParam.this.mDetectMaskBorderOffset = Float.parseFloat(value);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String name) throws SAXException {
            OplusIconParam.this.mCurrentTag = null;
        }
    }

    public boolean parseXml() {
        String path;
        if (!OplusThirdPartUtil.mIsDefaultTheme) {
            path = OplusThirdPartUtil.sThemePath;
        } else {
            path = OplusThemeUtil.getDefaultThemePath();
        }
        String launcherName = OplusThirdPartUtil.getLauncherName(path);
        try {
            ZipFile param = new ZipFile(path + launcherName);
            ZipEntry entry = param.getEntry(this.mPath);
            if (entry == null) {
                param.close();
                myLog("parseXml:entry is null");
                return false;
            }
            InputStream input = param.getInputStream(entry);
            if (input == null) {
                param.close();
                myLog("parseXml:input is null");
                return false;
            }
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(input, new ThemeXmlHandler());
            input.close();
            param.close();
            return true;
        } catch (ZipException e) {
            myLog("parseXml:ZipFile is destroyed, mPath = " + this.mPath);
            return false;
        } catch (Exception ex) {
            Log.e(TAG, "parseXml. ex = " + ex);
            return false;
        }
    }

    public boolean parseXmlForUser(int userId) {
        String path = OplusThirdPartUtil.sThemePath;
        String launcherName = OplusThirdPartUtil.getLauncherName(path);
        try {
            ZipFile param = new ZipFile(path + launcherName);
            ZipEntry entry = param.getEntry(this.mPath);
            if (entry == null) {
                param.close();
                myLog("parseXml:entry is null");
                return false;
            }
            InputStream input = param.getInputStream(entry);
            if (input == null) {
                param.close();
                myLog("parseXml:input is null");
                return false;
            }
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(input, new ThemeXmlHandler());
            input.close();
            param.close();
            return true;
        } catch (ZipException e) {
            myLog("parseXml:ZipFile is destroyed, mPath = " + this.mPath);
            return false;
        } catch (Exception ex) {
            Log.e(TAG, "parseXml. ex = " + ex);
            return false;
        }
    }

    public float getScale() {
        return this.mScale;
    }

    public float getXOffset() {
        return this.mXOffsetPCT;
    }

    public float getYOffset() {
        return this.mYOffsetPCT;
    }

    public float getDetectMaskBorderOffset() {
        return this.mDetectMaskBorderOffset;
    }
}
