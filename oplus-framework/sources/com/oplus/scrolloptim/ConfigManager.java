package com.oplus.scrolloptim;

import android.app.ActivityThread;
import android.app.Application;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.oplus.performance.IOplusPerfService;
import com.oplus.performance.OplusPerfUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class ConfigManager {
    private static final String SHOPIFY_PACKAGE_NAME = "com.shopify.arrive";
    private static final String SYSTEM_SERVER_PACKAGE_NAME = "android";
    private static final String TAG = ConfigManager.class.getSimpleName();
    private boolean mAnimAheadEnable;
    private Map<String, Integer> mFrameInsertEnableList;
    private int mInsertDefaultNum = 0;
    private boolean mIsScrollOptEnable = OplusDebugUtil.DEBUG_ENABLE_OPT;
    private IOplusPerfService mOplusPerfService;
    private boolean mOptEnable;
    private Set<String> mSCEnableList;

    public ConfigManager() {
        initOptConfig();
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x005a A[Catch: RemoteException -> 0x009c, TryCatch #0 {RemoteException -> 0x009c, blocks: (B:5:0x001b, B:7:0x0028, B:10:0x0031, B:12:0x0035, B:13:0x003d, B:14:0x0042, B:16:0x005a, B:18:0x0060, B:19:0x0062, B:20:0x006a, B:22:0x0070, B:24:0x0085, B:26:0x008d, B:28:0x0090, B:36:0x0040), top: B:4:0x001b }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x008d A[Catch: RemoteException -> 0x009c, TryCatch #0 {RemoteException -> 0x009c, blocks: (B:5:0x001b, B:7:0x0028, B:10:0x0031, B:12:0x0035, B:13:0x003d, B:14:0x0042, B:16:0x005a, B:18:0x0060, B:19:0x0062, B:20:0x006a, B:22:0x0070, B:24:0x0085, B:26:0x008d, B:28:0x0090, B:36:0x0040), top: B:4:0x001b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void initOptConfig() {
        String pkgName;
        int i;
        Bundle insertListBundle;
        String[] array;
        this.mFrameInsertEnableList = new HashMap();
        this.mSCEnableList = new HashSet();
        IOplusPerfService oplusPerfService = getOplusPerfService();
        this.mOplusPerfService = oplusPerfService;
        if (oplusPerfService != null) {
            Bundle bundle = new Bundle();
            try {
                pkgName = getPackageName();
            } catch (RemoteException e) {
                OplusDebugUtil.e(TAG, "Got execption when initOptConfig.", e);
            }
            if (!"android".equals(pkgName) && !SHOPIFY_PACKAGE_NAME.equals(pkgName)) {
                this.mOptEnable = this.mIsScrollOptEnable ? this.mOplusPerfService.isScrollOptimEnable(pkgName, bundle) : false;
                this.mAnimAheadEnable = bundle.getBoolean(OplusPerfUtils.KEY_PRE_ANIM_ENABLE);
                this.mInsertDefaultNum = bundle.getInt(OplusPerfUtils.KEY_INSERT_DEFAULT_NUM);
                insertListBundle = bundle.getBundle(OplusPerfUtils.KEY_FRAME_INSERT_LIST);
                if (insertListBundle != null) {
                    if (insertListBundle.isEmpty()) {
                        this.mInsertDefaultNum = 0;
                    }
                    for (String s : insertListBundle.keySet()) {
                        this.mFrameInsertEnableList.put(s, Integer.valueOf(insertListBundle.getInt(s)));
                    }
                }
                array = bundle.getStringArray(OplusPerfUtils.KEY_SCROLL_CHANGED_ENABLE_LIST);
                if (array != null) {
                    for (String str : array) {
                        this.mSCEnableList.add(str);
                    }
                }
                OplusDebugUtil.d(TAG, "initOptConfig: pkg= " + getPackageName() + ", mOptEnable= " + this.mOptEnable + ", mAnimAheadEnable= " + this.mAnimAheadEnable + ", mInsertDefaultNum= " + this.mInsertDefaultNum + ", mFrameInsertEnableList= " + this.mFrameInsertEnableList + ", mSCEnableList= " + this.mSCEnableList);
            }
            this.mOptEnable = false;
            this.mAnimAheadEnable = bundle.getBoolean(OplusPerfUtils.KEY_PRE_ANIM_ENABLE);
            this.mInsertDefaultNum = bundle.getInt(OplusPerfUtils.KEY_INSERT_DEFAULT_NUM);
            insertListBundle = bundle.getBundle(OplusPerfUtils.KEY_FRAME_INSERT_LIST);
            if (insertListBundle != null) {
            }
            array = bundle.getStringArray(OplusPerfUtils.KEY_SCROLL_CHANGED_ENABLE_LIST);
            if (array != null) {
            }
            OplusDebugUtil.d(TAG, "initOptConfig: pkg= " + getPackageName() + ", mOptEnable= " + this.mOptEnable + ", mAnimAheadEnable= " + this.mAnimAheadEnable + ", mInsertDefaultNum= " + this.mInsertDefaultNum + ", mFrameInsertEnableList= " + this.mFrameInsertEnableList + ", mSCEnableList= " + this.mSCEnableList);
        }
    }

    private IOplusPerfService getOplusPerfService() {
        IOplusPerfService ret = null;
        try {
            IBinder b = ServiceManager.getService(OplusPerfUtils.OPLUS_PERF_SERVICE_NAME);
            if (b == null) {
                OplusDebugUtil.e(TAG, "getOplusPerfService: can't get service binder: IOplusPerfService");
            }
            ret = IOplusPerfService.Stub.asInterface(b);
            if (ret == null) {
                OplusDebugUtil.e(TAG, "getOplusPerfService: can't get service interface: IOplusPerfService");
            }
        } catch (Exception e) {
            OplusDebugUtil.e(TAG, "getOplusPerfService: can't get service interface: IOplusPerfService, exception info: " + e.getMessage());
        }
        return ret;
    }

    private String getPackageName() {
        Application app = ActivityThread.currentApplication();
        if (app != null && app.getApplicationContext() != null) {
            return app.getApplicationContext().getPackageName();
        }
        return "";
    }

    public boolean isScrollOptEnabled() {
        return this.mOptEnable;
    }

    public boolean isAnimAheadEnabled() {
        return this.mAnimAheadEnable;
    }

    public boolean isFrameInsertEnabled() {
        return this.mInsertDefaultNum == 1;
    }

    public boolean isScrollChangedEnabled(String activityName) {
        return this.mSCEnableList.contains(activityName);
    }

    public int getInsertNum(String activityName) {
        return this.mFrameInsertEnableList.getOrDefault(activityName, Integer.valueOf(this.mInsertDefaultNum)).intValue();
    }
}
