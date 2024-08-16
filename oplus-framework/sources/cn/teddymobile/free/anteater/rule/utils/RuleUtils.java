package cn.teddymobile.free.anteater.rule.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.OplusTelephonyManager;
import android.text.TextUtils;
import cn.teddymobile.free.anteater.resources.UriConstants;
import cn.teddymobile.free.anteater.rule.Rule;
import com.oplus.util.OplusLog;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RuleUtils {
    public static final boolean DBG;
    public static final boolean LOG_DEBUG;
    public static final boolean LOG_PANIC;
    public static final String TAG = "RuleUtils";
    private static final Uri URI_RULE;

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        LOG_PANIC = z;
        boolean z2 = SystemProperties.getBoolean("log.favorite.debug", false);
        LOG_DEBUG = z2;
        DBG = z || z2;
        URI_RULE = new Uri.Builder().scheme(OplusTelephonyManager.BUNDLE_CONTENT).authority(UriConstants.AUTHORITY).path(UriConstants.PATH_RULE).build();
    }

    public static String queryRuleFromProvider(Context context) {
        long start = SystemClock.uptimeMillis();
        String result = null;
        Cursor cursor = null;
        try {
            try {
                String[] PROJECTION = {"data"};
                String[] whereArgs = {context.getPackageName()};
                ContentResolver contentResolver = context.getContentResolver();
                cursor = contentResolver.query(URI_RULE, PROJECTION, "package_name = ?", whereArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex("data"));
                }
            } catch (Exception e) {
                boolean z = DBG;
                OplusLog.w(z, TAG, e.getMessage());
                if (cursor != null) {
                    cursor.close();
                }
                long spend = SystemClock.uptimeMillis() - start;
                OplusLog.i(z, TAG, "queryRuleFromProvider: spend=" + spend + "ms");
            }
            return result;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            long spend2 = SystemClock.uptimeMillis() - start;
            OplusLog.i(DBG, TAG, "queryRuleFromProvider: spend=" + spend2 + "ms");
        }
    }

    public static List<Rule> parseRule(String data) {
        List<Rule> ruleList = new ArrayList<>();
        if (!TextUtils.isEmpty(data)) {
            long start = SystemClock.uptimeMillis();
            try {
                try {
                    JSONArray ruleArray = new JSONArray(data);
                    for (int i = 0; i < ruleArray.length(); i++) {
                        JSONObject ruleObject = ruleArray.getJSONObject(i);
                        Rule rule = new Rule();
                        rule.loadFromJSON(ruleObject);
                        ruleList.add(rule);
                    }
                } catch (Exception e) {
                    boolean z = DBG;
                    OplusLog.w(z, TAG, e.getMessage());
                    long spend = SystemClock.uptimeMillis() - start;
                    OplusLog.i(z, TAG, "parseRule=" + spend + "ms");
                }
            } finally {
                long spend2 = SystemClock.uptimeMillis() - start;
                OplusLog.i(DBG, TAG, "parseRule=" + spend2 + "ms");
            }
        }
        return ruleList;
    }
}
