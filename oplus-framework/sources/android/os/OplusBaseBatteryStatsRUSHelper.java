package android.os;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import com.oplus.romupdate.RomUpdateObserver;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class OplusBaseBatteryStatsRUSHelper {
    private static final String COLUMN_NAME_1 = "version";
    private static final String COLUMN_NAME_2 = "xml";
    private static final String DAILY_BATT_PROTO_SWITCH = "daily_batt_proto_switch";
    private static final String TAG = "OplusBaseBatteryStatsRUSHelper";
    private Context mContext;
    private String mFilterName;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final Uri CONTENT_URI_WHITE_LIST = Uri.parse("content://com.nearme.romupdate.provider.db/update_list");

    public OplusBaseBatteryStatsRUSHelper(Context context, String filterName) {
        this.mContext = null;
        this.mFilterName = null;
        this.mContext = context;
        this.mFilterName = filterName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void myLog(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public void registerRomUpdate() {
        Log.d(TAG, "registerRomUpdate");
        RomUpdateObserver.getInstance().register(this.mFilterName, new RomUpdateObserver.OnReceiveListener() { // from class: android.os.OplusBaseBatteryStatsRUSHelper.1
            public void onReceive(Context context) {
                OplusBaseBatteryStatsRUSHelper.this.updateROMConfig();
            }
        });
    }

    private void parseContentFromXML(String content) {
        int type;
        if (content == null || content.isEmpty()) {
            return;
        }
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(content));
            parser.nextTag();
            parser.getEventType();
            do {
                type = parser.next();
                if (type == 2) {
                    String tag = parser.getName();
                    if (DAILY_BATT_PROTO_SWITCH.equals(tag)) {
                        String currentText = parser.nextText();
                        myLog("currentText: " + currentText);
                        if (currentText != null) {
                            if ("0".equals(currentText)) {
                                myLog("Update daily proto reporting feature to 0");
                                Settings.System.putInt(this.mContext.getContentResolver(), DAILY_BATT_PROTO_SWITCH, 0);
                            } else if ("1".equals(currentText)) {
                                myLog("Update daily proto reporting feature to 1");
                                Settings.System.putInt(this.mContext.getContentResolver(), DAILY_BATT_PROTO_SWITCH, 1);
                            }
                        }
                    }
                }
            } while (type != 1);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x00c3, code lost:
    
        if (r3 != null) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x00c5, code lost:
    
        android.util.Log.i(android.os.OplusBaseBatteryStatsRUSHelper.TAG, "getConfigFromProvider: failed");
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x00ca, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x00cb, code lost:
    
        parseContentFromXML(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x00ce, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00c0, code lost:
    
        if (r2 == null) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void getConfigFromProvider() {
        Cursor cursor = null;
        String strConfigList = null;
        Log.i(TAG, "getConfigFromProvider FILTER_NAME =" + this.mFilterName);
        try {
            try {
                String[] projection = {"version", "xml"};
                cursor = this.mContext.getContentResolver().query(CONTENT_URI_WHITE_LIST, projection, "filtername=\"" + this.mFilterName + "\"", null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    int versioncolumnIndex = cursor.getColumnIndex("version");
                    int xmlcolumnIndex = cursor.getColumnIndex("xml");
                    cursor.moveToNext();
                    int configVersion = cursor.getInt(versioncolumnIndex);
                    myLog("configVersion: " + configVersion);
                    strConfigList = cursor.getString(xmlcolumnIndex);
                    myLog("strConfigList: " + strConfigList);
                }
            } catch (Exception e) {
                Log.i(TAG, "getConfigFromProvider: Got execption. " + e);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateROMConfig() {
        Thread thread = new Thread(new GetDataFromProviderRunnable(), "ProtoConfigRomUpdate");
        Log.d(TAG, "ACTION_ROM_UPDATE_CONFIG_SUCCES");
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GetDataFromProviderRunnable implements Runnable {
        public GetDataFromProviderRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            OplusBaseBatteryStatsRUSHelper.myLog("start fetching...");
            OplusBaseBatteryStatsRUSHelper.this.getConfigFromProvider();
        }
    }
}
