package android.app;

import android.text.TextUtils;
import android.util.Log;
import com.oplus.theme.OplusThemeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
public class OplusUxDrawableMappingHelper {
    private static final String MAPPING_NAME = "drawableMapping.xml";
    private static final String TAG = "OplusUxDrawableMappingHelper";
    private static final List<String> sMappingPackageList = new ArrayList();
    private static boolean sParsed = false;

    public static synchronized void parsePackMapping() {
        String str;
        String str2;
        synchronized (OplusUxDrawableMappingHelper.class) {
            if (sParsed) {
                return;
            }
            Log.d(TAG, "start parsePackMapping");
            InputStream input = null;
            try {
                try {
                    input = new FileInputStream(new File(OplusThemeUtil.SYSTEM_THEME_DEFAULT_PATH, MAPPING_NAME));
                    List<String> list = sMappingPackageList;
                    list.clear();
                    parseXml(input);
                    sParsed = true;
                    Log.d(TAG, "parsePackMapping success--->size:" + list.size());
                } catch (Exception e) {
                    Log.e(TAG, "parsePackMapping parseXml error");
                    if (input != null) {
                        try {
                            input.close();
                        } catch (Exception e2) {
                            str = TAG;
                            str2 = "parsePackMapping input error";
                            Log.e(str, str2);
                        }
                    }
                }
                try {
                    input.close();
                } catch (Exception e3) {
                    str = TAG;
                    str2 = "parsePackMapping input error";
                    Log.e(str, str2);
                }
            } finally {
            }
        }
    }

    private static void parseXml(InputStream inStream) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser saxParser = spf.newSAXParser();
        saxParser.parse(inStream, new MappingXmlHandler());
        inStream.close();
    }

    public static boolean containsPackageName(String packageName) {
        return sMappingPackageList.contains(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MappingXmlHandler extends DefaultHandler {
        private MappingXmlHandler() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String name, Attributes attributes) {
            if (localName.equalsIgnoreCase("item")) {
                String packageName = attributes.getValue("package");
                if (!TextUtils.isEmpty(packageName)) {
                    OplusUxDrawableMappingHelper.sMappingPackageList.add(packageName);
                }
            }
        }
    }
}
