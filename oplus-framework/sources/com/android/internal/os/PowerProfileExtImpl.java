package com.android.internal.os;

import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class PowerProfileExtImpl implements IPowerProfileExt {
    private static final String ATTR_NAME = "name";
    static final String TAG = "PowerProfile";
    private static final String TAG_ARRAY = "array";
    private static final String TAG_ARRAYITEM = "value";
    private static final String TAG_DEVICE = "device";
    private static final String TAG_ITEM = "item";
    private PowerProfile mPowerProfile;
    private String mStrProjectVersion = SystemProperties.get("ro.product.prjversion");
    private String mProjectPowerProfile = "/odm/etc/power_profile/power_profile.xml";

    public PowerProfileExtImpl(Object base) {
        this.mPowerProfile = (PowerProfile) base;
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x013c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean readPowerValuesFromXml(HashMap<String, Double[]> powerArrayMap, HashMap<String, Double> powerItemMap) {
        Log.i(TAG, "target project version: " + this.mStrProjectVersion + "  power profile : " + this.mProjectPowerProfile);
        boolean res = false;
        boolean parsingArray = false;
        ArrayList<Double> array = new ArrayList<>();
        String arrayName = null;
        if (this.mProjectPowerProfile != null && new File(this.mProjectPowerProfile).canRead()) {
            FileInputStream fis = null;
            try {
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();
                    fis = new FileInputStream(this.mProjectPowerProfile);
                    parser.setInput(fis, "utf-8");
                    XmlUtils.beginDocument(parser, TAG_DEVICE);
                    while (true) {
                        XmlUtils.nextElement(parser);
                        String element = parser.getName();
                        if (element == null) {
                            break;
                        }
                        if (parsingArray && !element.equals(TAG_ARRAYITEM)) {
                            powerArrayMap.put(arrayName, (Double[]) array.toArray(new Double[array.size()]));
                            parsingArray = false;
                        }
                        if (element.equals(TAG_ARRAY)) {
                            parsingArray = true;
                            array.clear();
                            arrayName = parser.getAttributeValue(null, "name");
                        } else {
                            if (!element.equals(TAG_ITEM) && !element.equals(TAG_ARRAYITEM)) {
                            }
                            String name = !parsingArray ? parser.getAttributeValue(null, "name") : null;
                            if (parser.next() == 4) {
                                String power = parser.getText();
                                double value = 0.0d;
                                try {
                                    value = Double.valueOf(power).doubleValue();
                                } catch (NumberFormatException e) {
                                }
                                if (element.equals(TAG_ITEM)) {
                                    try {
                                        powerItemMap.put(name, Double.valueOf(value));
                                    } catch (IOException e2) {
                                        e = e2;
                                        throw new RuntimeException(e);
                                    } catch (XmlPullParserException e3) {
                                        e = e3;
                                        throw new RuntimeException(e);
                                    }
                                } else if (parsingArray) {
                                    array.add(Double.valueOf(value));
                                }
                            }
                        }
                    }
                    if (parsingArray) {
                        powerArrayMap.put(arrayName, (Double[]) array.toArray(new Double[array.size()]));
                    }
                    res = true;
                    try {
                        fis.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                } catch (IOException e5) {
                    e = e5;
                } catch (XmlPullParserException e6) {
                    e = e6;
                } catch (Throwable th) {
                    e = th;
                    XmlPullParserException xmlPullParserException = e;
                    if (fis == null) {
                    }
                }
            } catch (Throwable th2) {
                e = th2;
                XmlPullParserException xmlPullParserException2 = e;
                if (fis == null) {
                    try {
                        fis.close();
                        throw xmlPullParserException2;
                    } catch (Exception e7) {
                        e7.printStackTrace();
                        throw xmlPullParserException2;
                    }
                }
                throw xmlPullParserException2;
            }
        }
        return res;
    }
}
