package com.oplus.notification.redpackage;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.oplus.util.OplusNavigationBarUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RedPackageAssistRUSManager {
    private static final boolean DEBUG;
    public static final String ENVELOPE_CONTENT_TAG = "envelope_content_tag";
    public static final String ENVELOPE_FILTER_FIELD = "envelope_filter_field";
    public static final String ENVELOPE_FILTER_VALUE = "envelope_filter_value";
    public static final String ENVELOPE_GROUP_TAG = "envelope_group_tag";
    public static final String ENVELOPE_USER_FIELD = "envelope_user_field";
    public static final String ENVELOPE_USER_NAME_TAG_FIRST = "envelope_user_name_tag_first";
    public static final String ENVELOPE_USER_NAME_TAG_LAST = "envelope_user_name_tag_last";
    public static final int MESSAGE_DOWNLOAD_DATA_FROM_RUS = 2;
    public static final int MESSAGE_INIT_FILE = 1;
    public static final int MESSAGE_UPGRADE_DATA_FROM_LOCAL = 3;
    public static final String OPLUS_REDPACKAGE_ASSIST_ATTRIBUTE_CONFIG_DIR = "/data/oplus/os/notification";
    public static final String OPLUS_REDPACKAGE_ASSIST_ATTRIBUTE_CONFIG_FILE_PATH = "/data/oplus/os/notification/sys_systemui_redpackage_assist_config.xml";
    public static final String OPLUS_REDPACKAGE_ASSIST_CONFIG_KEY = "sys_systemui_redpackage_assist_config";
    public static final String OPLUS_REDPACKAGE_ASSIST_CONFIG_NAME = "sys_systemui_redpackage_assist_config.xml";
    public static final String PKG_VERSION = "pkg_version";
    private static final String TAG;
    private static final List<AdaptationEnvelopeInfo> mAdaptationEnvelopeInfoList;
    private static final String[] mDefaultEnvelopeInfo;
    private static final Object mLock;
    private static final RedPackageAssistRUSManager sRedPackageAssistRUSManager;
    private Context mContext;
    private volatile int mCurrentIndex = -1;
    private HandlerThread mHandlerThread;
    private Handler mMainHandler;
    private RedPackageRUSReceiver mRedPackageRUSReceiver;
    private Handler mThreadHandler;

    static {
        String simpleName = RedPackageAssistRUSManager.class.getSimpleName();
        TAG = simpleName;
        DEBUG = Log.isLoggable(simpleName, 3);
        sRedPackageAssistRUSManager = new RedPackageAssistRUSManager();
        mLock = new Object();
        mAdaptationEnvelopeInfoList = new ArrayList();
        mDefaultEnvelopeInfo = new String[]{"0", "MainUI_User_Last_Msg_Type", "436207665", "Main_User", "@chatroom", "]", ": [微信红包]", "[微信红包]"};
    }

    public static RedPackageAssistRUSManager getInstance() {
        return sRedPackageAssistRUSManager;
    }

    private RedPackageAssistRUSManager() {
    }

    public void init(Context context) {
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        Handler handler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.oplus.notification.redpackage.RedPackageAssistRUSManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        RedPackageAssistRUSManager.this.creatRedPackageConfigFile();
                        return;
                    case 2:
                        RedPackageAssistRUSManager redPackageAssistRUSManager = RedPackageAssistRUSManager.this;
                        redPackageAssistRUSManager.downloadDataFromRUS(redPackageAssistRUSManager.mContext, RedPackageAssistRUSManager.OPLUS_REDPACKAGE_ASSIST_CONFIG_KEY);
                        return;
                    case 3:
                        RedPackageAssistRUSManager.this.updateRedpackageDataFromLocal();
                        if (RedPackageAssistRUSManager.mAdaptationEnvelopeInfoList == null || RedPackageAssistRUSManager.mAdaptationEnvelopeInfoList.isEmpty()) {
                            RedPackageAssistRUSManager.this.updateEnvelopeDefaultInfo();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mThreadHandler = handler;
        handler.sendEmptyMessage(1);
        RedPackageRUSReceiver redPackageRUSReceiver = new RedPackageRUSReceiver();
        this.mRedPackageRUSReceiver = redPackageRUSReceiver;
        redPackageRUSReceiver.registerRedPackageRUSReceiver(this.mContext);
        this.mThreadHandler.sendEmptyMessage(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void creatRedPackageConfigFile() {
        File redPackageAssistConfigDirectory = new File(OPLUS_REDPACKAGE_ASSIST_ATTRIBUTE_CONFIG_DIR);
        File redPackageAssistConfigPath = new File(OPLUS_REDPACKAGE_ASSIST_ATTRIBUTE_CONFIG_FILE_PATH);
        try {
            if (!redPackageAssistConfigDirectory.exists()) {
                redPackageAssistConfigDirectory.mkdirs();
            }
            if (!redPackageAssistConfigPath.exists()) {
                redPackageAssistConfigPath.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (DEBUG) {
                Log.e(TAG, "init redpackage Dir failed!!!");
            }
        }
    }

    public void downloadDataFromRUS(Context context, String fileKey) {
        String xmlValue = RUSUpgradeUtils.getDataFromProvider(context, fileKey);
        String remoteConfigVersion = RUSUpgradeUtils.getConfigVersion(xmlValue);
        String localConfigVersion = RUSUpgradeUtils.getRedPackageRUSVersion(context);
        if (localConfigVersion == null || remoteConfigVersion.compareTo(localConfigVersion) > 0) {
            if (xmlValue != null) {
                if (fileKey.equals(OPLUS_REDPACKAGE_ASSIST_CONFIG_KEY)) {
                    RUSUpgradeUtils.saveStrToFile(context, OPLUS_REDPACKAGE_ASSIST_CONFIG_NAME, xmlValue);
                    List<AdaptationEnvelopeInfo> tempList = RUSUpgradeUtils.parseRedpackageString2List(xmlValue);
                    updateEnvelopeWhenRUSArrived(tempList);
                } else if (DEBUG) {
                    Log.d(TAG, "we need do nothing because this RUS content is null\n");
                }
            }
            RUSUpgradeUtils.setRedPackageRUSVersion2Local(context, remoteConfigVersion);
        }
    }

    public static String getRedPackageDataFromLocalFile() {
        String str;
        StringBuilder sb;
        String s = "";
        InputStream is = null;
        File xmlFile = new File(OPLUS_REDPACKAGE_ASSIST_ATTRIBUTE_CONFIG_FILE_PATH);
        try {
            if (xmlFile.exists()) {
                try {
                    is = new FileInputStream(xmlFile);
                    s = RUSUpgradeUtils.inputStream2String(is);
                    try {
                        is.close();
                    } catch (IOException e) {
                        e = e;
                        if (DEBUG) {
                            str = TAG;
                            sb = new StringBuilder();
                            Log.d(str, sb.append("redpackage local file:error:").append(e.getMessage()).toString());
                        }
                    }
                } catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e3) {
                            e = e3;
                            if (DEBUG) {
                                str = TAG;
                                sb = new StringBuilder();
                                Log.d(str, sb.append("redpackage local file:error:").append(e.getMessage()).toString());
                            }
                        }
                    }
                }
            } else if (DEBUG) {
                Log.d(TAG, "redpackage local file is not exit!\n");
            }
            return s;
        } catch (Throwable e4) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e5) {
                    if (DEBUG) {
                        Log.d(TAG, "redpackage local file:error:" + e5.getMessage());
                    }
                }
            }
            throw e4;
        }
    }

    public void updateEnvelopeWhenRUSArrived(List<AdaptationEnvelopeInfo> list) {
        if (!OplusNavigationBarUtil.getInstance().isHasInitialized() || list == null || list.isEmpty()) {
            return;
        }
        synchronized (mLock) {
            List<AdaptationEnvelopeInfo> list2 = mAdaptationEnvelopeInfoList;
            list2.clear();
            list2.addAll(list);
            if (DEBUG) {
                Log.d(TAG, "we have upgrade the RUS Data and the value is " + list2 + "\n");
            }
        }
    }

    public void updateRedpackageDataFromLocal() {
        if (!OplusNavigationBarUtil.getInstance().isHasInitialized()) {
            return;
        }
        String xmlValue = getRedPackageDataFromLocalFile();
        if (xmlValue == null || xmlValue.isEmpty()) {
            updateEnvelopeDefaultInfo();
            return;
        }
        List<AdaptationEnvelopeInfo> tempList = RUSUpgradeUtils.parseRedpackageString2List(xmlValue);
        if (tempList == null || tempList.isEmpty()) {
            return;
        }
        synchronized (mLock) {
            List<AdaptationEnvelopeInfo> list = mAdaptationEnvelopeInfoList;
            list.clear();
            list.addAll(tempList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEnvelopeDefaultInfo() {
        synchronized (mLock) {
            AdaptationEnvelopeInfo info = new AdaptationEnvelopeInfo();
            String[] strArr = mDefaultEnvelopeInfo;
            info.setPkgVersion(strArr[0]);
            info.setEnvelopeFilterField(strArr[1]);
            info.setEnvelopeFilterValue(strArr[2]);
            info.setEnvelopeUserField(strArr[3]);
            info.setEnvelopeGroupTag(strArr[4]);
            info.setEnvelopeUserNameTagFirst(strArr[5]);
            info.setEnvelopeUserNameTagLast(strArr[6]);
            info.setEnvelopeContentTag(strArr[7]);
            mAdaptationEnvelopeInfoList.add(info);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RedPackageRUSReceiver {
        public static final String OPLUS_COMPONENT_SAFE = "oplus.permission.OPLUS_COMPONENT_SAFE";
        private static final String ROM_UPDATE_CONFIG_SUCCESS_ACTION = "oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS";
        private static final String ROM_UPDATE_CONFIG_SUCCES_EXTRA = "ROM_UPDATE_CONFIG_LIST";
        private static final String TAG = "RomUpdateReceiver";
        private final boolean DEBUG = Log.isLoggable(TAG, 3);
        private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.oplus.notification.redpackage.RedPackageAssistRUSManager.RedPackageRUSReceiver.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (RedPackageRUSReceiver.this.DEBUG) {
                    Log.d(RedPackageRUSReceiver.TAG, "onReceive: action start:" + action);
                }
                if ("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS".equals(action)) {
                    List<String> changeTableNameList = null;
                    try {
                        changeTableNameList = intent.getStringArrayListExtra("ROM_UPDATE_CONFIG_LIST");
                    } catch (Exception e) {
                        Log.e(RedPackageRUSReceiver.TAG, "onReceive: get update config list failed", e);
                    }
                    if (changeTableNameList == null || changeTableNameList.isEmpty()) {
                        Log.d(RedPackageRUSReceiver.TAG, "list null or empty");
                        return;
                    } else if (changeTableNameList.contains(RedPackageAssistRUSManager.OPLUS_REDPACKAGE_ASSIST_CONFIG_KEY)) {
                        RedPackageAssistRUSManager.this.mThreadHandler.sendEmptyMessage(2);
                    }
                } else if (RedPackageRUSReceiver.this.DEBUG) {
                    Log.d(RedPackageRUSReceiver.TAG, "run:RomUpdateReceiver.onReceive lose a broadcast:" + action + "," + intent.getDataString());
                }
                if (RedPackageRUSReceiver.this.DEBUG) {
                    Log.d(RedPackageRUSReceiver.TAG, "onReceive: action end:" + action);
                }
            }
        };

        public RedPackageRUSReceiver() {
        }

        public void registerRedPackageRUSReceiver(Context context) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS");
            context.registerReceiver(this.mReceiver, filter, "oplus.permission.OPLUS_COMPONENT_SAFE", null);
        }
    }

    public static boolean compareVersion(String versionA, String versionB) {
        Log.i(TAG, "A:" + versionA + " B:" + versionB);
        if (versionA == null || versionA.equals("") || versionB == null || versionB.equals("")) {
            return false;
        }
        if (versionA.equals(versionB)) {
            return true;
        }
        String[] arrayA = versionA.split("\\.");
        String[] arrayB = versionB.split("\\.");
        int length = arrayA.length < arrayB.length ? arrayA.length : arrayB.length;
        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(arrayB[i]) > Integer.parseInt(arrayA[i])) {
                Log.d(TAG, "B:" + Integer.parseInt(arrayB[i]) + " > A:" + Integer.parseInt(arrayA[i]));
                return true;
            }
            if (Integer.parseInt(arrayB[i]) < Integer.parseInt(arrayA[i])) {
                Log.d(TAG, "B:" + Integer.parseInt(arrayB[i]) + " < A:" + Integer.parseInt(arrayA[i]));
                return false;
            }
        }
        return false;
    }

    public static String getVersion(Context context, String pkgName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(pkgName, 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            Log.d(TAG, "GetVersion failed! e:" + e.getMessage(), e);
            return null;
        }
    }

    public boolean shouldSendRedpackageBroadcast(Notification notification) {
        PendingIntent intent = notification.contentIntent;
        if (intent == null) {
            Log.d(TAG, "contentIntent is null");
            return false;
        }
        synchronized (mLock) {
            List<AdaptationEnvelopeInfo> list = mAdaptationEnvelopeInfoList;
            if (list.size() == 0 || list == null) {
                updateEnvelopeDefaultInfo();
            }
            int i = 0;
            while (true) {
                List<AdaptationEnvelopeInfo> list2 = mAdaptationEnvelopeInfoList;
                if (i >= list2.size()) {
                    return false;
                }
                if (intent.getIntent().getIntExtra(list2.get(i).getEnvelopeFilterField(), -1) != -1) {
                    int aWeChatMsgId = intent.getIntent().getIntExtra(list2.get(i).getEnvelopeFilterField(), -1);
                    if (aWeChatMsgId == Integer.parseInt(list2.get(i).getEnvelopeFilterValue())) {
                        this.mCurrentIndex = i;
                        return true;
                    }
                }
                i++;
            }
        }
    }

    public AdaptationEnvelopeInfo getCurrentRedpackageInfo() {
        return mAdaptationEnvelopeInfoList.get(this.mCurrentIndex);
    }

    /* loaded from: classes.dex */
    private class FileObserverPolicy extends FileObserver {
        private String mFocusPath;

        public FileObserverPolicy(String path) {
            super(path, 8);
            this.mFocusPath = path;
            if (RedPackageAssistRUSManager.DEBUG) {
                Log.d(RedPackageAssistRUSManager.TAG, "RedPackages--FileObserverPolicy_path = " + path);
            }
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, String path) {
            if (RedPackageAssistRUSManager.DEBUG) {
                Log.d(RedPackageAssistRUSManager.TAG, "RedPackages--onEvent: event = " + event + ",focusPath = " + this.mFocusPath);
            }
            if (event == 8 && this.mFocusPath.equals(RedPackageAssistRUSManager.OPLUS_REDPACKAGE_ASSIST_ATTRIBUTE_CONFIG_FILE_PATH) && RedPackageAssistRUSManager.DEBUG) {
                Log.d(RedPackageAssistRUSManager.TAG, "RedPackages--onEvent: focusPath = OPLUS_REDPACKAGE_ASSIST_ATTRIBUTE_CONFIG_FILE_PATH");
            }
        }
    }
}
