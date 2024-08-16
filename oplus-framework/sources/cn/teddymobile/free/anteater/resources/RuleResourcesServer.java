package cn.teddymobile.free.anteater.resources;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.OplusTelephonyManager;
import cn.teddymobile.free.anteater.logger.Logger;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class RuleResourcesServer {
    private static final String POSTFIX_PARSER = "Parser";
    private final Handler mHandler;
    private static final String TAG = RuleResourcesServer.class.getSimpleName();
    private static final Uri URI_RULE = new Uri.Builder().scheme(OplusTelephonyManager.BUNDLE_CONTENT).authority(UriConstants.AUTHORITY).path(UriConstants.PATH_RULE).build();
    private static final String[] PROJECTION = {"package_name", "data"};
    private final Map<String, String> mRuleCache = new HashMap();
    private final Map<String, RuleServerCallback> mCallbackCache = new HashMap();
    private RulesObserver mRulesObserver = null;
    private boolean mObserved = false;
    private boolean mInited = false;

    /* loaded from: classes.dex */
    public interface QueryCallback {
        Handler createWorkHandler(Context context, String str, int i);

        void linkBinderToDeath(IBinder.DeathRecipient deathRecipient);

        void onQueryResult(Context context, String str);

        void unlinkBinderToDeath(IBinder.DeathRecipient deathRecipient);
    }

    public RuleResourcesServer(Handler handler) {
        Logger.d(TAG, "<init>()");
        this.mHandler = handler;
    }

    public void startQuery(final Context context, final String packageName, final QueryCallback callback) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: cn.teddymobile.free.anteater.resources.RuleResourcesServer.1
                @Override // java.lang.Runnable
                public void run() {
                    RuleResourcesServer.this.registerObserver(context);
                    RuleResourcesServer.this.updateCache(context, false, "startQuery");
                    RuleResourcesServer.this.updateCallback(packageName, callback);
                    RuleResourcesServer ruleResourcesServer = RuleResourcesServer.this;
                    ruleResourcesServer.onQueryResult(context, ruleResourcesServer.getRule(packageName), callback);
                }
            });
        }
    }

    public void registerObserver(Context context) {
        if (this.mHandler != null && !this.mObserved) {
            Uri uri = URI_RULE;
            if (this.mRulesObserver == null) {
                this.mRulesObserver = new RulesObserver(this.mHandler, context, uri);
            }
            try {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.registerContentObserver(uri, false, this.mRulesObserver);
                Logger.i(TAG, "registerObserver : " + uri);
                this.mObserved = true;
            } catch (Exception e) {
                Logger.i(TAG, e.toString());
            }
        }
    }

    public void unregisterObserver() {
        RulesObserver rulesObserver = this.mRulesObserver;
        if (rulesObserver != null) {
            Context context = rulesObserver.getContext();
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.unregisterContentObserver(this.mRulesObserver);
            Logger.i(TAG, "unregisterObserver : " + this.mRulesObserver.getUri());
            this.mRulesObserver = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getRule(String packageName) {
        String str;
        synchronized (this.mRuleCache) {
            str = this.mRuleCache.get(packageName);
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onQueryResult(Context context, String rule, QueryCallback callback) {
        if (callback != null) {
            callback.onQueryResult(context, rule);
        }
    }

    private String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x002d, code lost:
    
        if (r5.moveToFirst() != false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x002f, code lost:
    
        r5 = getString(r5, "package_name");
        r6 = getString(r5, "data");
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003b, code lost:
    
        if (r5 == null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x003d, code lost:
    
        if (r6 == null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x003f, code lost:
    
        r7 = r10.mRuleCache;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0041, code lost:
    
        monitor-enter(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0042, code lost:
    
        r10.mRuleCache.put(r5, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0047, code lost:
    
        monitor-exit(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0050, code lost:
    
        if (r5.moveToNext() != false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0052, code lost:
    
        r10.mInited = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateCache(Context context, boolean update, String tag) {
        synchronized (RuleResourcesServer.class) {
            if (!this.mInited || update) {
                long start = SystemClock.uptimeMillis();
                synchronized (this.mRuleCache) {
                    this.mRuleCache.clear();
                }
                Cursor cursor = null;
                try {
                    try {
                        ContentResolver contentResolver = context.getContentResolver();
                        cursor = contentResolver.query(URI_RULE, PROJECTION, null, null, null);
                        if (cursor != null) {
                        }
                    } catch (Exception e) {
                        String str = TAG;
                        Logger.i(str, e.toString());
                        if (cursor != null) {
                            cursor.close();
                        }
                        long spend = SystemClock.uptimeMillis() - start;
                        Logger.d(str, "updateCache [" + tag + "] : spend=" + spend + "ms");
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    long spend2 = SystemClock.uptimeMillis() - start;
                    Logger.d(TAG, "updateCache [" + tag + "] : spend=" + spend2 + "ms");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCallback(String packageName, QueryCallback callback) {
        long start = SystemClock.uptimeMillis();
        RuleServerCallback serverCallback = null;
        String rule = getRule(packageName);
        if (rule != null) {
            synchronized (this.mCallbackCache) {
                serverCallback = this.mCallbackCache.get(packageName);
                if (serverCallback == null && callback != null) {
                    serverCallback = new RuleServerCallback(this.mCallbackCache);
                    this.mCallbackCache.put(packageName, serverCallback);
                }
            }
        }
        long spend = SystemClock.uptimeMillis() - start;
        if (serverCallback != null) {
            Logger.d(TAG, "updateCallback : " + packageName + " : spend=" + spend + "ms");
            serverCallback.setCallback(callback);
        } else {
            Logger.d(TAG, "noRuleData : " + packageName + " : spend=" + spend + "ms");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RulesObserver extends ContentObserver {
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
            RuleResourcesServer.this.updateCache(this.mContext, true, "onChange");
            synchronized (RuleResourcesServer.this.mCallbackCache) {
                for (Map.Entry<String, RuleServerCallback> entry : RuleResourcesServer.this.mCallbackCache.entrySet()) {
                    String packageName = entry.getKey();
                    RuleServerCallback callback = entry.getValue();
                    RuleResourcesServer ruleResourcesServer = RuleResourcesServer.this;
                    ruleResourcesServer.onQueryResult(this.mContext, ruleResourcesServer.getRule(packageName), callback.getCallback());
                }
            }
        }

        public Context getContext() {
            return this.mContext;
        }

        public Uri getUri() {
            return this.mUri;
        }
    }
}
