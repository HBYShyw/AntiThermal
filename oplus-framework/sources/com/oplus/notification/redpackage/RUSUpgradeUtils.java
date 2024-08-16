package com.oplus.notification.redpackage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.notification.redpackage.xmlsax.Xml;
import com.oplus.notification.redpackage.xmlsax.XmlAttribute;
import com.oplus.notification.redpackage.xmlsax.XmlNode;
import com.oplus.notification.redpackage.xmlsax.XmlParse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RUSUpgradeUtils {
    public static final String ATTRIBUTE_CONTENT_TAG = "content_tag";
    public static final String ATTRIBUTE_FILTER_FIELD = "filter_field";
    public static final String ATTRIBUTE_FILTER_VALUE = "filter_value";
    public static final String ATTRIBUTE_GROUP_TAG = "group_tag";
    public static final String ATTRIBUTE_KEY_VERSION = "pkg_version";
    public static final String ATTRIBUTE_USER_FIELD = "user_field";
    public static final String ATTRIBUTE_USER_NAME_TAG_FIRST = "user_name_tag_first";
    public static final String ATTRIBUTE_USER_NAME_TAG_LAST = "user_name_tag_last";
    public static final String COLUMN_NAME_XML = "xml";
    private static final boolean DEBUG;
    public static final String KEY_CONFIG = "config";
    public static final String KEY_REDPACKAGE = "redpackage";
    private static final Uri ROM_UPDATE_CONTENT_URI;
    private static final String TAG;
    private static final String XML_KEY_VERSION = "version";

    static {
        String simpleName = RUSUpgradeUtils.class.getSimpleName();
        TAG = simpleName;
        DEBUG = Log.isLoggable(simpleName, 3);
        ROM_UPDATE_CONTENT_URI = Uri.parse("content://com.nearme.romupdate.provider.db/update_list");
    }

    public static String getDataFromProvider(Context context, String filterName) {
        Cursor cursor = null;
        String xmlValue = null;
        String[] projection = {"xml"};
        try {
            cursor = context.getContentResolver().query(ROM_UPDATE_CONTENT_URI, projection, "filtername=\"" + filterName + "\"", null, null);
            if (cursor != null && cursor.getCount() > 0) {
                int xmlColumnIndex = cursor.getColumnIndex("xml");
                cursor.moveToNext();
                xmlValue = cursor.getString(xmlColumnIndex);
            } else {
                Log.w(TAG, "The Filtrate app cursor is null !!!");
            }
            if (cursor != null) {
                cursor.close();
            }
            return xmlValue;
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            Log.w(TAG, "We can not get Filtrate app data from provider");
            return null;
        }
    }

    public static String getDataVersionFromProvider(Context context, String configName) {
        Log.d(TAG, "getDataVersionFromProvider configName:" + configName);
        Cursor cursor = null;
        String version = "";
        String[] projection = {"version"};
        try {
            cursor = context.getContentResolver().query(ROM_UPDATE_CONTENT_URI, projection, "filtername=\"" + configName + "\"", null, null);
            if (cursor != null && cursor.getCount() > 0) {
                int versionColumnIndex = cursor.getColumnIndex("version");
                cursor.moveToNext();
                version = cursor.getString(versionColumnIndex);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            Log.d(TAG, "We can not get data version with " + configName + " from provider");
        }
        return version;
    }

    public static void setRedPackageRUSVersion2Local(Context context, String version) {
        Settings.Secure.putStringForUser(context.getContentResolver(), RedPackageAssistRUSManager.OPLUS_REDPACKAGE_ASSIST_CONFIG_KEY, version, 0);
    }

    public static String getRedPackageRUSVersion(Context context) {
        return Settings.Secure.getStringForUser(context.getContentResolver(), RedPackageAssistRUSManager.OPLUS_REDPACKAGE_ASSIST_CONFIG_KEY, 0);
    }

    public static List<AdaptationEnvelopeInfo> parseRedpackageString2List(String xmlValue) {
        List<AdaptationEnvelopeInfo> tempEnvelopeInfoList = new ArrayList<>();
        try {
            XmlParse xmlParse = XmlParse.builder();
            Xml xml = xmlParse.parse(xmlValue);
            XmlNode rootNode = xml.getRootNode();
            XmlNode[] xmlNodeList = rootNode.getAllChildNodes();
            for (XmlNode xmlNode : xmlNodeList) {
                if (KEY_REDPACKAGE.equals(xmlNode.getName())) {
                    XmlAttribute attrPkg = xmlNode.getAttribute("pkg_version");
                    if (attrPkg != null) {
                        AdaptationEnvelopeInfo redpackageConfig = parseRedPackageAsTag(xmlNode);
                        if (DEBUG) {
                            Log.d(TAG, "the node AdaptationEnvelopeInfo is : " + redpackageConfig);
                        }
                        tempEnvelopeInfoList.add(redpackageConfig);
                    } else if (DEBUG) {
                        Log.d(TAG, "parseConfigList: pkg is null:" + xmlNode.toXml());
                    }
                } else if (DEBUG) {
                    Log.d(TAG, "parseConfigList:warning:unknown tag:" + xmlNode.toXml());
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "parseConfigList:error:" + e.getMessage() + ",xml=" + xmlValue, e);
        }
        return tempEnvelopeInfoList;
    }

    public static AdaptationEnvelopeInfo parseRedPackageAsTag(XmlNode xmlNode) {
        AdaptationEnvelopeInfo redpackageItem = null;
        try {
            redpackageItem = new AdaptationEnvelopeInfo();
            if (xmlNode.getAttribute("pkg_version") != null) {
                redpackageItem.setPkgVersion(xmlNode.getAttribute("pkg_version").getValue());
            }
            if (xmlNode.getAttribute(ATTRIBUTE_FILTER_FIELD) != null) {
                redpackageItem.setEnvelopeFilterField(xmlNode.getAttribute(ATTRIBUTE_FILTER_FIELD).getValue());
            }
            if (xmlNode.getAttribute(ATTRIBUTE_FILTER_VALUE) != null) {
                redpackageItem.setEnvelopeFilterValue(xmlNode.getAttribute(ATTRIBUTE_FILTER_VALUE).getValue());
            }
            if (xmlNode.getAttribute(ATTRIBUTE_USER_FIELD) != null) {
                redpackageItem.setEnvelopeUserField(xmlNode.getAttribute(ATTRIBUTE_USER_FIELD).getValue());
            }
            if (xmlNode.getAttribute(ATTRIBUTE_GROUP_TAG) != null) {
                redpackageItem.setEnvelopeGroupTag(xmlNode.getAttribute(ATTRIBUTE_GROUP_TAG).getValue());
            }
            if (xmlNode.getAttribute(ATTRIBUTE_USER_NAME_TAG_FIRST) != null) {
                redpackageItem.setEnvelopeUserNameTagFirst(xmlNode.getAttribute(ATTRIBUTE_USER_NAME_TAG_FIRST).getValue());
            }
            if (xmlNode.getAttribute(ATTRIBUTE_USER_NAME_TAG_LAST) != null) {
                redpackageItem.setEnvelopeUserNameTagLast(xmlNode.getAttribute(ATTRIBUTE_USER_NAME_TAG_LAST).getValue());
            }
            if (xmlNode.getAttribute(ATTRIBUTE_CONTENT_TAG) != null) {
                redpackageItem.setEnvelopeContentTag(xmlNode.getAttribute(ATTRIBUTE_CONTENT_TAG).getValue());
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "parseAsPackageTag:error:" + e.getMessage());
            }
        }
        return redpackageItem;
    }

    public static String inputStream2String(InputStream is) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            while (true) {
                try {
                    int i = is.read();
                    if (i != -1) {
                        os.write(i);
                    } else {
                        String byteArrayOutputStream = os.toString(Charset.defaultCharset().toString());
                        os.close();
                        return byteArrayOutputStream;
                    }
                } finally {
                }
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "inputStream2String--e:" + e.getMessage());
                return "";
            }
            return "";
        }
    }

    public static String getConfigVersion(String xmlValue) {
        String version = "";
        if (TextUtils.isEmpty(xmlValue)) {
            return "";
        }
        try {
            XmlParse xmlParse = XmlParse.builder();
            Xml xml = xmlParse.parse(xmlValue);
            XmlNode versionNode = null;
            XmlNode rootNode = xml.getRootNode();
            if (rootNode != null) {
                versionNode = rootNode.getChildNode("version");
            }
            if (versionNode != null) {
                version = versionNode.getValue().trim();
            }
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
                Log.d(TAG, "getConfigVersion--e:" + e.getMessage());
            }
        }
        if (DEBUG) {
            Log.d(TAG, "getConfigVersion:" + version);
        }
        return version;
    }

    public static void saveStrToFile(Context context, String name, String str) {
        if (context == null || name == null || str == null) {
            return;
        }
        File redPackageAssistConfigDirectory = new File(RedPackageAssistRUSManager.OPLUS_REDPACKAGE_ASSIST_ATTRIBUTE_CONFIG_DIR);
        if (!redPackageAssistConfigDirectory.exists()) {
            try {
                redPackageAssistConfigDirectory.mkdirs();
            } catch (Exception e) {
                Log.d(TAG, "failed create path, e = " + e.getMessage(), e);
            }
        }
        Log.d(TAG, "saveStrToFile:" + redPackageAssistConfigDirectory + "/" + name);
        File file = new File(redPackageAssistConfigDirectory, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e2) {
                Log.d(TAG, "failed write file " + e2);
            }
        }
        writeStrToFile(file, str);
    }

    private static void writeStrToFile(File file, String str) {
        String str2;
        StringBuilder sb;
        if (file == null) {
            return;
        }
        FileOutputStream fileos = null;
        try {
            try {
                try {
                    try {
                        fileos = new FileOutputStream(file);
                        fileos.write(str.getBytes(StandardCharsets.UTF_8));
                        fileos.flush();
                        try {
                            fileos.close();
                        } catch (IOException e) {
                            e = e;
                            str2 = TAG;
                            sb = new StringBuilder();
                            Log.d(str2, sb.append("failed write file ").append(e).toString());
                        }
                    } catch (IllegalArgumentException e2) {
                        Log.d(TAG, "failed write file " + e2);
                        if (fileos != null) {
                            try {
                                fileos.close();
                            } catch (IOException e3) {
                                e = e3;
                                str2 = TAG;
                                sb = new StringBuilder();
                                Log.d(str2, sb.append("failed write file ").append(e).toString());
                            }
                        }
                    } catch (Exception e4) {
                        Log.d(TAG, "failed write file " + e4);
                        if (fileos != null) {
                            try {
                                fileos.close();
                            } catch (IOException e5) {
                                e = e5;
                                str2 = TAG;
                                sb = new StringBuilder();
                                Log.d(str2, sb.append("failed write file ").append(e).toString());
                            }
                        }
                    }
                } catch (IOException e6) {
                    Log.d(TAG, "failed write file " + e6);
                    if (fileos != null) {
                        try {
                            fileos.close();
                        } catch (IOException e7) {
                            e = e7;
                            str2 = TAG;
                            sb = new StringBuilder();
                            Log.d(str2, sb.append("failed write file ").append(e).toString());
                        }
                    }
                }
            } catch (IllegalStateException e8) {
                Log.d(TAG, "failed write file " + e8);
                if (fileos != null) {
                    try {
                        fileos.close();
                    } catch (IOException e9) {
                        e = e9;
                        str2 = TAG;
                        sb = new StringBuilder();
                        Log.d(str2, sb.append("failed write file ").append(e).toString());
                    }
                }
            }
        } catch (Throwable th) {
            if (fileos != null) {
                try {
                    fileos.close();
                } catch (IOException e10) {
                    Log.d(TAG, "failed write file " + e10);
                }
            }
            throw th;
        }
    }
}
