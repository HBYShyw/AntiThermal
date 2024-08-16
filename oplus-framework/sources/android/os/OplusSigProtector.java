package android.os;

import android.app.Application;
import android.util.Log;
import android.util.Xml;
import dalvik.annotation.optimization.FastNative;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public class OplusSigProtector {
    private static final int INIT_DELAY_TIME = 2000;
    private static final String LIB_NAME = "sigprotector_jni";
    private static final String PACKAGE_TAG = "package";
    private static final String SIGNAL_11_TAG = "signal_11";
    private static final String SIGNAL_12_TAG = "signal_12";
    private static final String TAG = "OplusSigProtector";
    private static final String XML_FILE = "/data/local/tmp/sigprotector.xml";
    private static final String[] SIGNAL_11_LIST = {"com.ss.android.article.news", "com.eg.android.AlipayGphone", "com.qiyi.video.lite", "com.csii.jincheng", "com.ylzinfo.chinahrss", "org.cocos2dx.BookseFuZhouMajiang.nearme.gamecenter", "com.jieshun.jscarlife", "cn.swiftpass.enterprise.cib.appstore", "com.bill99.kuaiqian", "com.iss.shenzhenmetro", "com.xingin.xhs", "com.tencent.jkchess"};
    private static final String[] SIGNAL_12_LIST = {"com.ss.android.ugc.aweme", "com.xingin.xhs", "com.tencent.jkchess"};
    private static ArrayList<String> sSignal11List = null;
    private static ArrayList<String> sSignal12List = null;

    /* renamed from: -$$Nest$smisLogKillProcess, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m162$$Nest$smisLogKillProcess() {
        return isLogKillProcess();
    }

    /* renamed from: -$$Nest$smisSigProtectProcess, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m163$$Nest$smisSigProtectProcess() {
        return isSigProtectProcess();
    }

    @FastNative
    public static native void nativeInitExitHandler();

    @FastNative
    public static native void nativeInitKillHandler();

    @FastNative
    public static native void nativeInitSigProtector();

    @FastNative
    public static native void nativeStartSigProtector();

    static {
        try {
            System.loadLibrary(LIB_NAME);
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Failed to LoadLibrary sigprotector_jni");
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:10:0x002a. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:32:0x005e. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x001d. Please report as an issue. */
    private static void parseXml(FileReader xmlReader) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(xmlReader);
            int eventType = parser.getEventType();
            ArrayList<String> packageList = null;
            String packageName = null;
            while (true) {
                boolean z = true;
                if (eventType != 1) {
                    switch (eventType) {
                        case 0:
                        case 1:
                        default:
                            eventType = parser.next();
                        case 2:
                            String name = parser.getName();
                            switch (name.hashCode()) {
                                case -807062458:
                                    if (name.equals("package")) {
                                        z = 2;
                                        break;
                                    }
                                    z = -1;
                                    break;
                                case 1073322775:
                                    if (name.equals(SIGNAL_11_TAG)) {
                                        z = false;
                                        break;
                                    }
                                    z = -1;
                                    break;
                                case 1073322776:
                                    if (name.equals(SIGNAL_12_TAG)) {
                                        break;
                                    }
                                    z = -1;
                                    break;
                                default:
                                    z = -1;
                                    break;
                            }
                            switch (z) {
                                case false:
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    sSignal11List = arrayList;
                                    packageList = arrayList;
                                    break;
                                case true:
                                    ArrayList<String> arrayList2 = new ArrayList<>();
                                    sSignal12List = arrayList2;
                                    packageList = arrayList2;
                                    break;
                                case true:
                                    packageName = parser.getAttributeValue(null, "name");
                                    break;
                            }
                            eventType = parser.next();
                            break;
                        case 3:
                            String name2 = parser.getName();
                            switch (name2.hashCode()) {
                                case -807062458:
                                    if (name2.equals("package")) {
                                        z = 2;
                                        break;
                                    }
                                    z = -1;
                                    break;
                                case 1073322775:
                                    if (name2.equals(SIGNAL_11_TAG)) {
                                        z = false;
                                        break;
                                    }
                                    z = -1;
                                    break;
                                case 1073322776:
                                    if (name2.equals(SIGNAL_12_TAG)) {
                                        break;
                                    }
                                    z = -1;
                                    break;
                                default:
                                    z = -1;
                                    break;
                            }
                            switch (z) {
                                case false:
                                case true:
                                    packageList = null;
                                    break;
                                case true:
                                    if (packageList != null && packageName != null) {
                                        packageList.add(packageName);
                                    }
                                    packageName = null;
                                    break;
                            }
                            eventType = parser.next();
                            break;
                    }
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            Log.w(TAG, "Couldn't read xml file ");
        } catch (XmlPullParserException e2) {
            Log.w(TAG, "Couldn't not parse xml file " + e2.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x001f -> B:9:0x0038). Please report as a decompilation issue!!! */
    public static void parseWhiteList() {
        if (sSignal11List != null || sSignal12List != null) {
            Log.w(TAG, "Already parsed white list");
            return;
        }
        FileReader xmlReader = null;
        try {
            try {
                xmlReader = new FileReader(XML_FILE);
                parseXml(xmlReader);
                xmlReader.close();
            } catch (FileNotFoundException e) {
                if (xmlReader != null) {
                    xmlReader.close();
                }
            } catch (Throwable th) {
                if (xmlReader != null) {
                    try {
                        xmlReader.close();
                    } catch (IOException e2) {
                        Log.w(TAG, "Couldn't close xml file ");
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            Log.w(TAG, "Couldn't close xml file ");
        }
    }

    private static boolean isSigProtectProcess() {
        String processName = Application.getProcessName();
        if (processName == null) {
            Log.w(TAG, "processName is null!");
            return false;
        }
        ArrayList<String> arrayList = sSignal11List;
        if (arrayList != null && arrayList.contains(processName)) {
            Log.d(TAG, "processName is in signal 11 white list!");
            return true;
        }
        for (String pkg : SIGNAL_11_LIST) {
            if (processName.equals(pkg)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLogKillProcess() {
        String processName = Application.getProcessName();
        if (processName == null) {
            Log.w(TAG, "processName is null!");
            return false;
        }
        ArrayList<String> arrayList = sSignal12List;
        if (arrayList != null && arrayList.contains(processName)) {
            Log.d(TAG, "processName is in signal 12 white list!");
            return true;
        }
        for (String pkg : SIGNAL_12_LIST) {
            if (processName.equals(pkg)) {
                return true;
            }
        }
        return false;
    }

    public static void init() {
        boolean debugVersion = SystemProperties.getBoolean("debug.oplus.sigprotector.enable", false);
        boolean isPreVersion = SystemProperties.get("ro.build.version.ota", "ota_version").contains("PRE");
        if (isPreVersion || debugVersion) {
            nativeInitExitHandler();
            nativeInitSigProtector();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: android.os.OplusSigProtector.1
                @Override // java.lang.Runnable
                public void run() {
                    OplusSigProtector.parseWhiteList();
                    if (OplusSigProtector.m163$$Nest$smisSigProtectProcess()) {
                        OplusSigProtector.nativeStartSigProtector();
                    }
                    if (OplusSigProtector.m162$$Nest$smisLogKillProcess()) {
                        OplusSigProtector.nativeInitKillHandler();
                    }
                }
            }, 2000L);
        }
    }
}
