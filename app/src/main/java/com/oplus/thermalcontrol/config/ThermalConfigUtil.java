package com.oplus.thermalcontrol.config;

import android.content.Context;
import android.database.Cursor;
import b6.LocalLog;
import com.oplus.thermalcontrol.ThermalControlConfig;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* loaded from: classes2.dex */
public class ThermalConfigUtil {
    public static final String TAG = "ThermalConfigUtil";

    public static Optional<Integer> StringToInt(String str) {
        try {
            return Optional.of(Integer.valueOf(Integer.parseInt(str)));
        } catch (NumberFormatException unused) {
            return Optional.empty();
        }
    }

    public static Document getConfigDocumentFromXml(String str) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
            DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
            newInstance.setExpandEntityReferences(false);
            Document parse = newInstance.newDocumentBuilder().parse(byteArrayInputStream);
            if (parse == null) {
                LocalLog.b(TAG, "getConfigDocumentFromXml, null doc!");
            }
            return parse;
        } catch (IOException e10) {
            LocalLog.b(TAG, "IOException:" + e10);
            return null;
        } catch (ParserConfigurationException e11) {
            LocalLog.b(TAG, "ParserConfigurationException:" + e11);
            return null;
        } catch (SAXException e12) {
            LocalLog.b(TAG, "SAXException:" + e12);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0049 A[Catch: Exception -> 0x003f, TRY_LEAVE, TryCatch #0 {Exception -> 0x003f, blocks: (B:18:0x002d, B:20:0x0033, B:7:0x0049, B:5:0x0041), top: B:17:0x002d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String getDataFromProvider(Context context, String str) {
        Cursor cursor;
        String string;
        String[] strArr = {ThermalControlConfig.COLUMN_NAME_XML};
        try {
            cursor = context.getContentResolver().query(ThermalControlConfig.CONTENT_URI, strArr, "filtername=\"" + str + "\"", null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        int columnIndex = cursor.getColumnIndex(ThermalControlConfig.COLUMN_NAME_XML);
                        cursor.moveToNext();
                        string = cursor.getString(columnIndex);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (Exception e10) {
                    e = e10;
                    if (cursor != null) {
                        cursor.close();
                    }
                    LocalLog.l(TAG, "We can not get Filtrate app data from provider,because of " + e);
                    return null;
                }
            }
            LocalLog.l(TAG, "The Filtrate app cursor is null !!!");
            string = null;
            if (cursor != null) {
            }
            return string;
        } catch (Exception e11) {
            e = e11;
            cursor = null;
        }
    }

    public static Document getDefaultConfigDocument(String str) {
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(thermalConfigFile(str));
            if (parse == null) {
                LocalLog.b(TAG, "getDefaultConfigDocument, null doc!");
            }
            return parse;
        } catch (IOException e10) {
            LocalLog.b(TAG, "IOException:" + e10);
            return null;
        } catch (ParserConfigurationException e11) {
            LocalLog.b(TAG, "ParserConfigurationException:" + e11);
            return null;
        } catch (SAXException e12) {
            LocalLog.b(TAG, "SAXException:" + e12);
            return null;
        }
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("-?[0-9]*").matcher(str).matches();
    }

    public static String parseVersionForDocument(Document document, String str) {
        Element element;
        String tagName;
        Element documentElement = document.getDocumentElement();
        String trim = documentElement.getTagName().trim();
        if (trim != null && trim.equals(str)) {
            NodeList childNodes = documentElement.getChildNodes();
            int i10 = 0;
            while (true) {
                if (i10 >= childNodes.getLength()) {
                    break;
                }
                Node item = childNodes.item(i10);
                if ((item instanceof Element) && (tagName = (element = (Element) item).getTagName()) != null && tagName.equals("version")) {
                    String textContent = element.getTextContent();
                    if (isNumeric(textContent)) {
                        return textContent;
                    }
                    LocalLog.l(TAG, "parseVersion: not numeric. configVersion: " + textContent);
                } else {
                    i10++;
                }
            }
            return null;
        }
        LocalLog.b(TAG, "loadApListFromXml, xml root tag is not " + str);
        return null;
    }

    public static File thermalConfigFile(String str) {
        String str2 = "/my_bigball/etc/temperature_profile/" + str;
        String str3 = "/odm/etc/temperature_profile/" + str;
        File file = new File(str2);
        if (file.exists()) {
            return file;
        }
        File file2 = new File(str3);
        file2.exists();
        return file2;
    }
}
