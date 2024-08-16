package android.app;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.oplus.theme.OplusThemeUtil;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
class OplusIconPackMappingHelper {
    public static final String MAPPING_NAME = "packMapping.xml";
    private static final String TAG = "IconPackMappingHelper";
    public static final ArrayMap<String, String> sMappingComponentMap = new ArrayMap<>();
    public static final ArrayMap<String, String> sMappingPackageMap = new ArrayMap<>();
    private static boolean sParsed = false;

    OplusIconPackMappingHelper() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Exception] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v13 */
    /* JADX WARN: Type inference failed for: r7v14 */
    /* JADX WARN: Type inference failed for: r7v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r7v4 */
    public static void parsePackMapping() {
        String str = "parsePackMapping-time : ";
        long currentTimeMillis = System.currentTimeMillis();
        if (sParsed) {
            return;
        }
        Log.d(TAG, "start parsePackMapping");
        InputStream inputStream = null;
        ?? r7 = 1;
        r7 = 1;
        r7 = 1;
        try {
        } catch (Exception e) {
            Log.e("parsePackMapping", "input error");
            str = e;
        }
        try {
            try {
                inputStream = new FileInputStream(OplusThemeUtil.SYSTEM_THEME_DEFAULT_PATH + MAPPING_NAME);
                sMappingComponentMap.clear();
                parseXml(inputStream);
                inputStream.close();
                inputStream.close();
                sParsed = true;
                StringBuilder append = new StringBuilder().append("parsePackMapping-time : ");
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                String sb = append.append(currentTimeMillis2).toString();
                Log.e("lqc", sb);
                str = sb;
                r7 = currentTimeMillis2;
            } catch (Exception e2) {
                e2.printStackTrace();
                if (inputStream != null) {
                    inputStream.close();
                }
                sParsed = true;
                StringBuilder append2 = new StringBuilder().append("parsePackMapping-time : ");
                long currentTimeMillis3 = System.currentTimeMillis() - currentTimeMillis;
                String sb2 = append2.append(currentTimeMillis3).toString();
                Log.e("lqc", sb2);
                str = sb2;
                r7 = currentTimeMillis3;
            }
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e3) {
                    Log.e("parsePackMapping", "input error");
                    throw th;
                }
            }
            sParsed = r7;
            Log.e("lqc", str + (System.currentTimeMillis() - currentTimeMillis));
            throw th;
        }
    }

    public static void parseXml(InputStream inStream) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser saxParser = spf.newSAXParser();
        saxParser.parse(inStream, new MappingXmlHandler());
        inStream.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MappingXmlHandler extends DefaultHandler {
        MappingXmlHandler() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String name, Attributes attributes) {
            if (localName.equalsIgnoreCase("item")) {
                String componentName = attributes.getValue("component");
                String mappingComponent = attributes.getValue("mapping");
                int endIndex = componentName.indexOf("/");
                String packageName = "";
                if (endIndex > 0) {
                    packageName = componentName.substring(0, endIndex);
                }
                if (!TextUtils.isEmpty(componentName) && !TextUtils.isEmpty(mappingComponent)) {
                    OplusIconPackMappingHelper.sMappingComponentMap.put(componentName, mappingComponent);
                    OplusIconPackMappingHelper.sMappingPackageMap.put(packageName, mappingComponent);
                }
            }
        }
    }

    public static String getMappingComponent(String realComponent, String packageName) {
        String mappingComponent = sMappingComponentMap.get(realComponent);
        if (TextUtils.isEmpty(mappingComponent)) {
            return sMappingPackageMap.get(packageName);
        }
        return mappingComponent;
    }
}
