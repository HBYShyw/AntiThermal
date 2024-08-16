package com.oplus.preloadsplash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.os.BackgroundThread;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class OplusPreloadSpalshRUSHelper {
    private static final String ACTION_ROM_UPDATE_CONFIG_SUCCES = "oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS";
    private static final String COLUMN_NAME_1 = "version";
    private static final String COLUMN_NAME_2 = "xml";
    private static final String FILTER_NAME = "sys_opti_preload_splash_config";
    private static final int MSG_CONFIG = 7340033;
    private static final String PARAM_MAX_FAIL_NUM = "max-fail-num";
    private static final String PARAM_MAX_SAVE_NUM = "max-save-num";
    private static final String PARAM_MIN_HEIGHT = "min-height";
    private static final String PARAM_MIN_WIDTH = "min-width";
    private static final String TAG = "OplusPreloadSpalshRUSHelper";
    private static OplusPreloadSpalshRUSHelper sInstance;
    private Handler mHandler;
    private static final Uri CONTENT_URI_WHITE_LIST = Uri.parse("content://com.nearme.romupdate.provider.db/update_list");
    private static final Object mLock = new Object();
    private static final Object mListLock = new Object();
    private int mEnable = 0;
    private int mDebug = 0;
    private int mSupportForAllApps = 0;
    private int mMinWidth = 900;
    private int mMinHeight = 900;
    private int mMaxSaveCount = 3;
    private int mMaxFailNum = 5;
    private int mListparamSize = 7;
    private boolean mIsSystemReady = false;
    private Context mContext = null;
    private int mConfigVersion = 0;
    private UpdateReceiver mReceiver = new UpdateReceiver();
    private List<String> mList = new ArrayList();
    private ArrayList<String> mPreloadList = new ArrayList<>();
    private ArrayList<String> mCurrentPreloadList = new ArrayList<>();

    private OplusPreloadSpalshRUSHelper() {
    }

    public void init(Context context) {
        if (!this.mIsSystemReady && context != null) {
            Slog.d(TAG, "init -- begin");
            this.mIsSystemReady = true;
            this.mContext = context;
            MyHandler myHandler = new MyHandler(BackgroundThread.getHandler().getLooper());
            this.mHandler = myHandler;
            myHandler.sendMessage(myHandler.obtainMessage(7340033));
            initRomUpdateBroadcast();
        }
    }

    private void initRomUpdateBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS");
        this.mContext.registerReceiver(this.mReceiver, filter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class UpdateReceiver extends BroadcastReceiver {
        private UpdateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> changeList;
            String action = intent.getAction();
            if (action.equals("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS") && (changeList = intent.getStringArrayListExtra("ROM_UPDATE_CONFIG_LIST")) != null && changeList.contains(OplusPreloadSpalshRUSHelper.FILTER_NAME)) {
                OplusPreloadSpalshRUSHelper.this.mHandler.sendMessage(OplusPreloadSpalshRUSHelper.this.mHandler.obtainMessage(7340033));
                Slog.d(OplusPreloadSpalshRUSHelper.TAG, "ACTION_ROM_UPDATE_CONFIG_SUCCES");
            }
        }
    }

    public static OplusPreloadSpalshRUSHelper getInstance() {
        OplusPreloadSpalshRUSHelper oplusPreloadSpalshRUSHelper;
        synchronized (OplusPreloadSpalshRUSHelper.class) {
            if (sInstance == null) {
                sInstance = new OplusPreloadSpalshRUSHelper();
            }
            oplusPreloadSpalshRUSHelper = sInstance;
        }
        return oplusPreloadSpalshRUSHelper;
    }

    /* loaded from: classes.dex */
    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 7340033:
                    OplusPreloadSpalshRUSHelper.this.updateRUSInfo();
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRUSInfo() {
        Slog.d(TAG, "updateRUSInfo");
        synchronized (mLock) {
            if (this.mIsSystemReady) {
                if (getConfigFromProvider()) {
                    checkParamVaild();
                } else {
                    reset();
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ab, code lost:
    
        if (r3 == null) goto L32;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean getConfigFromProvider() {
        Cursor cursor = null;
        String strConfigList = null;
        int configVersion = 0;
        Slog.d(TAG, "getConfigFromProvider FILTER_NAME =sys_opti_preload_splash_config");
        try {
            try {
                String[] projection = {"version", "xml"};
                cursor = this.mContext.getContentResolver().query(CONTENT_URI_WHITE_LIST, projection, "filtername=\"sys_opti_preload_splash_config\"", null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    int versioncolumnIndex = cursor.getColumnIndex("version");
                    int xmlcolumnIndex = cursor.getColumnIndex("xml");
                    cursor.moveToNext();
                    configVersion = cursor.getInt(versioncolumnIndex);
                    strConfigList = cursor.getString(xmlcolumnIndex);
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (strConfigList == null) {
                    Slog.d(TAG, "getDataFromProvider: failed; strConfigList == null");
                    return false;
                }
                int i = this.mConfigVersion;
                if (i != 0 && configVersion < i) {
                    Slog.d(TAG, "getDataFromProvider: failed; newVer = " + configVersion + " oldVer = " + this.mConfigVersion);
                    return false;
                }
                this.mConfigVersion = configVersion;
                StringReader strReader = null;
                try {
                    try {
                        XmlPullParser parser = Xml.newPullParser();
                        strReader = new StringReader(strConfigList);
                        parser.setInput(strReader);
                        if (!parseXml(parser)) {
                            strReader.close();
                            return false;
                        }
                    } catch (Exception e) {
                        Slog.d(TAG, "getDataFromProvider: Got execption. ", e);
                    }
                    strReader.close();
                    Slog.d(TAG, "getConfigFromProvider success");
                    return true;
                } catch (Throwable th) {
                    if (strReader != null) {
                        strReader.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th2;
            }
        } catch (Exception e2) {
            Slog.d(TAG, "getDataFromProvider: Got execption. " + e2);
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x002b, code lost:
    
        r1 = r6.next();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean parseXml(XmlPullParser parser) {
        int eventType;
        this.mPreloadList.clear();
        try {
            eventType = parser.getEventType();
        } catch (Exception e) {
            Slog.d(TAG, "parseXml: Got execption. ", e);
            return false;
        }
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String strName = parser.getName();
                    parser.next();
                    String strText = parser.getText();
                    parserConfigTag(strName, strText);
                    break;
            }
            Slog.d(TAG, "parseXml: Got execption. ", e);
            return false;
        }
        return true;
    }

    private void parserConfigTag(String tag, String value) {
        ArrayList<String> arrayList;
        try {
            if ("enable".equals(tag)) {
                this.mEnable = Integer.valueOf(value).intValue();
            } else if ("debug".equals(tag)) {
                this.mDebug = Integer.valueOf(value).intValue();
            } else if ("support-for-all-app".equals(tag)) {
                this.mSupportForAllApps = Integer.valueOf(value).intValue();
            } else if (PARAM_MIN_WIDTH.equals(tag)) {
                this.mMinWidth = Integer.valueOf(value).intValue();
            } else if (PARAM_MIN_HEIGHT.equals(tag)) {
                this.mMinHeight = Integer.valueOf(value).intValue();
            } else if (PARAM_MAX_SAVE_NUM.equals(tag)) {
                this.mMaxSaveCount = Integer.valueOf(value).intValue();
            } else if (PARAM_MAX_FAIL_NUM.equals(tag)) {
                this.mMaxFailNum = Integer.valueOf(value).intValue();
            } else if ("list-app".equals(tag) && (arrayList = this.mPreloadList) != null) {
                arrayList.add(value);
            }
        } catch (Exception e) {
            Slog.d(TAG, "parserConfigTag ", e);
        }
    }

    private void checkParamVaild() {
        if (this.mMinWidth < 0 || this.mMinHeight < 0 || this.mMaxSaveCount < 0 || this.mMaxFailNum < 0) {
            reset();
        } else {
            updateParamList();
        }
    }

    private void updateParamList() {
        synchronized (mListLock) {
            this.mList.clear();
            this.mList.add(Integer.toString(this.mEnable));
            this.mList.add(Integer.toString(this.mDebug));
            this.mList.add(Integer.toString(this.mSupportForAllApps));
            this.mList.add(Integer.toString(this.mMinWidth));
            this.mList.add(Integer.toString(this.mMinHeight));
            this.mList.add(Integer.toString(this.mMaxSaveCount));
            this.mList.add(Integer.toString(this.mMaxFailNum));
            this.mCurrentPreloadList = this.mPreloadList;
            StringBuilder sb = new StringBuilder();
            sb.append("[OplusPreloadSpalshRUSHelper] updateParamList, status: ");
            sb.append("; mEnable: " + this.mEnable);
            sb.append("; mDebug: " + this.mDebug);
            sb.append("; mSupportForAllApps: " + this.mSupportForAllApps);
            sb.append("; mMinWidth: " + this.mMinWidth);
            sb.append("; mMinHeight: " + this.mMinHeight);
            sb.append("; mMaxSaveCount: " + this.mMaxSaveCount);
            sb.append("; mMaxFailNum: " + this.mMaxFailNum);
            sb.append("; mCurrentPreloadList: " + this.mCurrentPreloadList);
            Slog.d(TAG, sb.toString());
        }
    }

    private void reset() {
        Slog.d(TAG, "reset");
        this.mEnable = 0;
        this.mDebug = 0;
        this.mSupportForAllApps = 0;
        this.mMinWidth = 900;
        this.mMinHeight = 900;
        this.mMaxSaveCount = 3;
        this.mMaxFailNum = 5;
        this.mPreloadList = getDefaultListForPreloadCache();
        updateParamList();
    }

    private ArrayList<String> getDefaultListForPreloadCache() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.tencent.mm");
        list.add("com.snda.wifilocating");
        list.add("tv.danmaku.bili");
        list.add("com.xunlei.downloadprovider");
        list.add("com.mt.mtxx.mtxx");
        list.add("com.wuba");
        list.add("com.tmall.wireless");
        list.add("com.ximalaya.ting.android");
        list.add("com.taobao.trip");
        list.add("com.immomo.momo");
        list.add("com.hunantv.imgo.activity");
        list.add("com.kingpoint.gmcchh");
        return list;
    }

    public List<String> getPreLoadSplashRUSParamInner(String name) {
        synchronized (mListLock) {
            if (!this.mIsSystemReady) {
                Slog.d(TAG, "getPreLoadSplashRUSParam error; mIsSystemReady = " + this.mIsSystemReady);
                return null;
            }
            if (this.mList.size() != this.mListparamSize) {
                Slog.d(TAG, "getPreLoadSplashRUSParam error; mList.size() = " + this.mList.size());
                return null;
            }
            boolean inWhiteList = this.mCurrentPreloadList.contains(name);
            if (0 == 0 && !inWhiteList) {
                return null;
            }
            Slog.d(TAG, "getPreLoadSplashRUSParam supportForAllApps = false; package is inWhiteList= " + inWhiteList);
            return this.mList;
        }
    }

    public static List<String> getPreLoadSplashRUSParam(String name) {
        return getInstance().getPreLoadSplashRUSParamInner(name);
    }
}
