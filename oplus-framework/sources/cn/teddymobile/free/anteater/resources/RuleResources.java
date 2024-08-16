package cn.teddymobile.free.anteater.resources;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.OplusTelephonyManager;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.Rule;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RuleResources {
    private static final String POSTFIX_PARSER = "Parser";
    private static final String TAG = RuleResources.class.getSimpleName();
    private static final Uri URI_RULE = new Uri.Builder().scheme(OplusTelephonyManager.BUNDLE_CONTENT).authority(UriConstants.AUTHORITY).path(UriConstants.PATH_RULE).build();
    private final Handler mHandler;
    private final ArrayList<Rule> mRuleList = new ArrayList<>();
    private final ArrayList<String> mSceneList = new ArrayList<>();
    private final String[] mSelectionArgs = new String[1];
    private RulesObserver mRulesObserver = null;
    private boolean mInited = false;

    /* loaded from: classes.dex */
    public interface InitCallback {
        Handler createWorkHandler(Context context, String str, int i);

        void onLoadResult(Context context, boolean z, ArrayList<String> arrayList);
    }

    public RuleResources(Handler handler) {
        this.mHandler = handler;
    }

    public ArrayList<Rule> getRuleList() {
        ArrayList<Rule> arrayList;
        synchronized (this.mRuleList) {
            arrayList = this.mRuleList;
        }
        return arrayList;
    }

    public ArrayList<String> getSceneList() {
        ArrayList<String> arrayList;
        synchronized (this.mSceneList) {
            arrayList = this.mSceneList;
        }
        return arrayList;
    }

    public boolean isInited() {
        boolean z;
        synchronized (RuleResources.class) {
            z = this.mInited;
        }
        return z;
    }

    public void init(final Context context, final InitCallback callback) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.resources.RuleResources.1
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (RuleResources.class) {
                        if (RuleResources.this.mInited) {
                            RuleResources.this.loadFromCache(context, callback);
                        } else {
                            RuleResources.this.loadFromProvider(context, callback);
                            RuleResources.this.mInited = true;
                        }
                    }
                }
            });
        }
    }

    public void registerObserver(Context context) {
        if (this.mHandler != null) {
            if (this.mRulesObserver == null) {
                this.mRulesObserver = new RulesObserver(this.mHandler, context, URI_RULE);
            }
            try {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.registerContentObserver(this.mRulesObserver.getUri(), false, this.mRulesObserver);
                Logger.i(TAG, "registerObserver : " + this.mRulesObserver.getUri());
            } catch (Exception e) {
                Logger.i(TAG, e.toString());
            }
        }
    }

    public void unregisterObserver() {
        RulesObserver rulesObserver = this.mRulesObserver;
        if (rulesObserver != null) {
            try {
                Context context = rulesObserver.getContext();
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.unregisterContentObserver(this.mRulesObserver);
                Logger.i(TAG, "unregisterObserver : " + this.mRulesObserver.getUri());
            } catch (Exception e) {
                Logger.i(TAG, e.toString());
            }
            this.mRulesObserver = null;
        }
    }

    private String extractScene(Rule rule) {
        String parser = rule.getParser();
        if (parser.endsWith(POSTFIX_PARSER)) {
            return parser.substring(0, parser.length() - POSTFIX_PARSER.length());
        }
        return parser;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRules(Context context) {
        loadFromProvider(context, null);
    }

    private void onLoadResult(Context context, InitCallback callback, String tag) {
        int ruleSize;
        synchronized (this.mRuleList) {
            ruleSize = this.mRuleList.size();
        }
        Logger.i(TAG, tag + ". Size = " + ruleSize);
        if (callback != null) {
            synchronized (this.mSceneList) {
                callback.onLoadResult(context, ruleSize > 0, this.mSceneList);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadFromCache(Context context, InitCallback callback) {
        onLoadResult(context, callback, "loadFromCache");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x010f, code lost:
    
        if (r15 == null) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0111, code lost:
    
        r3 = "loadFromProvider";
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0117, code lost:
    
        onLoadResult(r14, r15, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x011a, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0114, code lost:
    
        r3 = "updateFromProvider";
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x010c, code lost:
    
        if (0 == 0) goto L54;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadFromProvider(Context context, InitCallback callback) {
        synchronized (this.mRuleList) {
            this.mRuleList.clear();
        }
        synchronized (this.mSceneList) {
            this.mSceneList.clear();
        }
        String packageName = context.getPackageName();
        this.mSelectionArgs[0] = packageName;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            try {
                cursor = contentResolver.query(URI_RULE, null, "package_name = ?", this.mSelectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String data = cursor.getString(cursor.getColumnIndex("data"));
                    long start = SystemClock.uptimeMillis();
                    try {
                        try {
                            JSONArray ruleArray = new JSONArray(data);
                            for (int i = 0; i < ruleArray.length(); i++) {
                                JSONObject ruleObject = ruleArray.getJSONObject(i);
                                Rule rule = new Rule();
                                rule.loadFromJSON(ruleObject);
                                synchronized (this.mRuleList) {
                                    this.mRuleList.add(rule);
                                }
                                String scene = extractScene(rule);
                                synchronized (this.mSceneList) {
                                    if (!this.mSceneList.contains(scene)) {
                                        this.mSceneList.add(scene);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            String str = TAG;
                            Logger.w(str, e.getMessage(), e);
                            long spend = SystemClock.uptimeMillis() - start;
                            Logger.d(str, "loadRuleFromJSON=" + spend + "ms");
                        }
                    } finally {
                        long spend2 = SystemClock.uptimeMillis() - start;
                        Logger.d(TAG, "loadRuleFromJSON=" + spend2 + "ms");
                    }
                }
            } catch (Exception e2) {
                Logger.i(TAG, e2.toString());
            }
        } finally {
            if (0 != 0) {
                cursor.close();
            }
        }
    }

    /* loaded from: classes.dex */
    private class RulesObserver extends ContentObserver {
        private final Context mContext;
        private final Uri mUri;

        public RulesObserver(Handler handler, Context context, Uri uri) {
            super(handler);
            this.mContext = context;
            this.mUri = uri;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            RuleResources.this.updateRules(this.mContext);
        }

        public Context getContext() {
            return this.mContext;
        }

        public Uri getUri() {
            return this.mUri;
        }
    }
}
