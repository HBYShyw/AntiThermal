package com.oplus.app;

import android.content.pm.OplusPackageManager;
import android.database.sqlite.SQLiteDebug;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class OplusAppDynamicFeatureManager implements IOplusAppDynamicFeatureManager {
    private static final String BACKSLASH_SYMBOL = "/";
    private static final String BLANK_SPACE_SYMBOL = " ";
    private static final String COLON_SYMBOL = ":";
    private static final String LEFT_BRACE_SYMBOL = "{";
    private static final int MAX_SIZE = 100;
    private static final String POINT_SYMBOL = ".";
    private static final int RESOURCE_ID_POSITION = 6;
    private static final String RIGHT_BRACE_SYMBOL = "}";
    private static final String TAG = "OplusAppDynamicFeatureManager";
    private OplusPackageManager mOplusPm;
    private ExecutorService mThreadPool;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static volatile OplusAppDynamicFeatureManager sInstance = null;

    public static OplusAppDynamicFeatureManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusAppDynamicFeatureManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusAppDynamicFeatureManager();
                }
            }
        }
        return sInstance;
    }

    private OplusAppDynamicFeatureManager() {
        this.mOplusPm = null;
        this.mThreadPool = null;
        this.mOplusPm = new OplusPackageManager();
        this.mThreadPool = Executors.newSingleThreadExecutor();
    }

    @Override // com.oplus.app.IOplusAppDynamicFeatureManager
    public void parseAppDynamicInfo(final String packageName, final String activityName, final View view) {
        checkThreadPool();
        this.mThreadPool.execute(new Runnable() { // from class: com.oplus.app.OplusAppDynamicFeatureManager.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    OplusAppDynamicFeatureManager.this.startToParseAppDynamicInfo(packageName, activityName, view);
                } catch (Exception e) {
                    Log.e(OplusAppDynamicFeatureManager.TAG, "parse app dynamic info exception: " + e.getMessage());
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startToParseAppDynamicInfo(String packageName, String activityName, View view) {
        if (view == null) {
            Log.w(TAG, "view is null");
            return;
        }
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "parse view hierarchy: " + packageName + "/" + activityName);
        }
        long startTime = System.currentTimeMillis();
        OplusAppDynamicFeatureData featureData = new OplusAppDynamicFeatureData();
        featureData.setPackageName(packageName);
        featureData.setActivityName(activityName);
        Map<String, Integer> components = new ArrayMap<>();
        Map<String, Integer> resourceIds = new ArrayMap<>();
        parseViewHierarchyInfo(view, components, resourceIds);
        if (components.size() >= 100 || resourceIds.size() >= 100) {
            if (z) {
                Log.w(TAG, "parse view size is too big!");
            }
        } else {
            featureData.setComponentNames(components);
            featureData.setIdNames(resourceIds);
            onGetAppDynamicFeatureFinished(featureData);
            if (z) {
                Log.d(TAG, "get app dynamic feature cost: " + (System.currentTimeMillis() - startTime) + " ms");
            }
        }
    }

    private void parseViewHierarchyInfo(View view, Map<String, Integer> components, Map<String, Integer> resourceIds) {
        ViewGroup g;
        int n;
        if (!(view instanceof ViewGroup)) {
            return;
        }
        Stack<View> stack = new Stack<>();
        ViewGroup grp = (ViewGroup) view;
        int N = grp.getChildCount();
        if (N <= 0) {
            return;
        }
        for (int i = 0; i < N; i++) {
            View v = grp.getChildAt(i);
            if (v != null) {
                stack.push(v);
            }
        }
        while (!stack.empty()) {
            View v2 = stack.pop();
            parseViewString(v2.toString(), components, resourceIds);
            if ((v2 instanceof ViewGroup) && (n = (g = (ViewGroup) v2).getChildCount()) > 0) {
                for (int i2 = 0; i2 < n; i2++) {
                    if (g.getChildAt(i2) != null) {
                        stack.push(g.getChildAt(i2));
                    }
                }
            }
        }
    }

    private void parseViewString(String viewString, Map<String, Integer> components, Map<String, Integer> resourceIds) {
        if (TextUtils.isEmpty(viewString)) {
            return;
        }
        int leftBraceIndex = viewString.lastIndexOf(LEFT_BRACE_SYMBOL);
        int rightBraceIndex = viewString.lastIndexOf(RIGHT_BRACE_SYMBOL);
        if (leftBraceIndex == -1 || rightBraceIndex == -1 || leftBraceIndex > rightBraceIndex) {
            return;
        }
        String componentName = parseComponentName(viewString.substring(0, leftBraceIndex));
        if (componentName != null) {
            if (components.containsKey(componentName)) {
                Integer value = components.get(componentName);
                if (value != null) {
                    components.put(componentName, Integer.valueOf(value.intValue() + 1));
                } else {
                    components.put(componentName, 1);
                }
            } else {
                components.put(componentName, 1);
            }
        }
        String resourceIdName = parseResourceIdName(viewString.substring(leftBraceIndex + 1, rightBraceIndex));
        if (resourceIdName != null) {
            if (resourceIds.containsKey(resourceIdName)) {
                Integer value2 = resourceIds.get(resourceIdName);
                if (value2 != null) {
                    resourceIds.put(resourceIdName, Integer.valueOf(value2.intValue() + 1));
                    return;
                } else {
                    resourceIds.put(resourceIdName, 1);
                    return;
                }
            }
            resourceIds.put(resourceIdName, 1);
        }
    }

    private String parseComponentName(String str) {
        int beginIndex;
        if (TextUtils.isEmpty(str) || (beginIndex = str.lastIndexOf(POINT_SYMBOL)) == -1) {
            return null;
        }
        return str.substring(beginIndex + 1);
    }

    private String parseResourceIdName(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] splitName = str.split(BLANK_SPACE_SYMBOL);
        if (splitName.length >= 6) {
            return splitName[5];
        }
        return null;
    }

    private void parseTextFromView(View view, List<String> texts) {
        if (view != null && (view instanceof TextView)) {
            TextView textView = (TextView) view;
            CharSequence text = textView.getText();
            if (text != null) {
                String content = text.toString();
                texts.add(content);
            }
        }
    }

    private List<String> getDatabaseInfo(String packageName) {
        long startTime = System.currentTimeMillis();
        List<String> database = new ArrayList<>();
        SQLiteDebug.PagerStats stats = SQLiteDebug.getDatabaseInfo();
        if (stats != null) {
            for (int i = 0; i < stats.dbStats.size(); i++) {
                SQLiteDebug.DbStats dbStats = (SQLiteDebug.DbStats) stats.dbStats.get(i);
                if (dbStats != null) {
                    database.add(dbStats.dbName);
                }
            }
        }
        if (DEBUG) {
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "get database info for " + packageName + " cost: " + (endTime - startTime) + " ms");
        }
        return database;
    }

    private void onGetAppDynamicFeatureFinished(OplusAppDynamicFeatureData featureData) {
        checkOplusPackageManager();
        OplusPackageManager oplusPackageManager = this.mOplusPm;
        if (oplusPackageManager != null) {
            try {
                oplusPackageManager.dynamicDetectApp(featureData);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException: " + e.getMessage());
            }
        }
    }

    private void checkOplusPackageManager() {
        if (this.mOplusPm == null) {
            this.mOplusPm = new OplusPackageManager();
        }
    }

    private void checkThreadPool() {
        if (this.mThreadPool == null) {
            this.mThreadPool = Executors.newSingleThreadExecutor();
        }
    }
}
