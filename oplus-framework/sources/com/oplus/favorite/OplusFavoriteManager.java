package com.oplus.favorite;

import android.app.Activity;
import android.app.OplusActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.OplusTelephonyManager;
import android.text.TextUtils;
import android.view.View;
import com.oplus.util.OplusContextUtil;
import com.oplus.util.OplusLog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class OplusFavoriteManager implements IOplusFavoriteManager {
    private static final String ACTION_FAVORITE_NOTIFY_FAILED = "com.coloros.favorite.FavoriteBroadcast.NotifyFailed";
    private static final String ACTION_FAVORITE_NOTIFY_START = "com.coloros.favorite.FavoriteService.NotifyStart";
    private static final String EXTRA_PACKAGE_NAME = "package_name";
    private static final String EXTRA_SCENE_LIST = "scene_list";
    private static final String PACKAGE_FAVORITE = "com.coloros.favorite";
    private static final String RESULT_PATH = "result";
    private static final String SETTINGS_FAVORITE = "coloros_favorite";
    private static final String SETTING_FIRST_START = "first_start";
    public static final String TAG = "OplusFavoriteManager";
    private IOplusFavoriteEngine mEngine = null;
    private final OplusFavoriteFactory mFactory = new OplusFavoriteFactory();
    private static final boolean NOTIFY_DEBUG = SystemProperties.getBoolean("feature.favorite.notify", false);
    private static final String RESULT_AUTHORITY = "com.coloros.favorite.result.provider";
    private static final Uri RESULT_URI = new Uri.Builder().scheme(OplusTelephonyManager.BUNDLE_CONTENT).authority(RESULT_AUTHORITY).path("result").build();
    private static volatile OplusFavoriteManager sInstance = null;

    private OplusFavoriteManager() {
        setEngine(OplusFavoriteEngines.TEDDY);
    }

    public static OplusFavoriteManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusFavoriteManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusFavoriteManager();
                }
            }
        }
        return sInstance;
    }

    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    public void release() {
        IOplusFavoriteEngine iOplusFavoriteEngine = this.mEngine;
        if (iOplusFavoriteEngine != null) {
            iOplusFavoriteEngine.release();
        }
    }

    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    public void processClick(View clickView, OplusFavoriteCallback callback) {
        IOplusFavoriteEngine iOplusFavoriteEngine = this.mEngine;
        if (iOplusFavoriteEngine == null || !iOplusFavoriteEngine.isTestOn()) {
            return;
        }
        this.mEngine.processClick(clickView, callback);
    }

    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    public void processCrawl(View rootView, OplusFavoriteCallback callback, String param) {
        if (this.mEngine != null) {
            OplusFavoriteCallback callback2 = new FavoriteProcessCallback(callback);
            FavoriteStart start = new FavoriteStart(callback2, rootView);
            start.run();
            this.mEngine.processCrawl(rootView, callback2, param);
        }
    }

    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    public void processSave(View rootView, OplusFavoriteCallback callback) {
        if (this.mEngine != null) {
            this.mEngine.processSave(rootView, new FavoriteSaveCallback(callback));
        }
    }

    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    public void logActivityInfo(Activity activity) {
        IOplusFavoriteEngine iOplusFavoriteEngine = this.mEngine;
        if (iOplusFavoriteEngine == null || !iOplusFavoriteEngine.isLogOn()) {
            return;
        }
        this.mEngine.logActivityInfo(activity);
    }

    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    public void logViewInfo(View view) {
        IOplusFavoriteEngine iOplusFavoriteEngine = this.mEngine;
        if (iOplusFavoriteEngine == null || !iOplusFavoriteEngine.isLogOn()) {
            return;
        }
        this.mEngine.logViewInfo(view);
    }

    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    public Handler getWorkHandler() {
        return this.mEngine.getWorkHandler();
    }

    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    public void setEngine(OplusFavoriteEngines engine) {
        IOplusFavoriteEngine iOplusFavoriteEngine = this.mEngine;
        if (iOplusFavoriteEngine != null) {
            iOplusFavoriteEngine.release();
        }
        this.mEngine = this.mFactory.setEngine(engine);
    }

    /* JADX WARN: Incorrect condition in loop: B:3:0x000e */
    @Override // com.oplus.favorite.IOplusFavoriteManager, com.oplus.favorite.IOplusBaseFavoriteManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isSaved(Context context, String packageName, List<Bundle> data) {
        StringBuilder selectionBuilder;
        List<String> selectionArgList;
        ContentResolver contentResolver = context.getContentResolver();
        Iterator<Bundle> it = data.iterator();
        boolean isSaved = false;
        while (isSaved) {
            Bundle bundle = it.next();
            Cursor cursor = null;
            try {
                selectionBuilder = new StringBuilder();
                selectionBuilder.append("packageName = ?");
                selectionArgList = new ArrayList<>();
            } catch (Exception e) {
                e = e;
            } catch (Throwable th) {
                th = th;
            }
            try {
                try {
                    selectionArgList.add(packageName);
                    String title = bundle.getString(OplusFavoriteData.DATA_TITLE);
                    String url = bundle.getString(OplusFavoriteData.DATA_URL);
                    boolean hasTitle = !TextUtils.isEmpty(title);
                    boolean hasUrl = !TextUtils.isEmpty(url);
                    if (hasTitle && hasUrl) {
                        selectionBuilder.append(" AND title = ? AND url = ?");
                        selectionArgList.add(title);
                        selectionArgList.add(url);
                    } else if (hasTitle) {
                        selectionBuilder.append(" AND title = ?");
                        selectionArgList.add(title);
                    } else if (hasUrl) {
                        selectionBuilder.append(" AND url = ?");
                        selectionArgList.add(url);
                    } else {
                        selectionArgList.clear();
                    }
                    if (!selectionArgList.isEmpty()) {
                        String selection = selectionBuilder.toString();
                        String[] selectionArgs = (String[]) selectionArgList.toArray(new String[selectionArgList.size()]);
                        cursor = contentResolver.query(RESULT_URI, null, selection, selectionArgs, null);
                        if (cursor != null) {
                            isSaved = cursor.getCount() > 0;
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (0 != 0) {
                        cursor.close();
                    }
                    if (!isSaved) {
                        throw th;
                    }
                    return isSaved;
                }
            } catch (Exception e2) {
                e = e2;
                OplusLog.e("OplusFavoriteManager", e.getMessage(), e);
                if (0 != 0) {
                    cursor.close();
                }
                if (isSaved) {
                    return isSaved;
                }
            }
            if (isSaved) {
                break;
            }
        }
        return isSaved;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v5, types: [java.lang.StringBuilder] */
    private void queryRule(Context context) {
        long spend;
        boolean z;
        StringBuilder sb;
        String str = "ms";
        String str2 = "queryRule : spend=";
        long start = SystemClock.uptimeMillis();
        try {
            try {
                OplusActivityManager am = new OplusActivityManager();
                am.favoriteQueryRule(context.getPackageName(), new FavoriteQueryCallback(context, this.mEngine));
                spend = SystemClock.uptimeMillis() - start;
                z = DBG;
                sb = new StringBuilder();
            } catch (RemoteException e) {
                OplusLog.e("OplusFavoriteManager", e.getMessage(), e);
                spend = SystemClock.uptimeMillis() - start;
                z = DBG;
                sb = new StringBuilder();
            } catch (Exception e2) {
                OplusLog.e("OplusFavoriteManager", e2.getMessage(), e2);
                spend = SystemClock.uptimeMillis() - start;
                z = DBG;
                sb = new StringBuilder();
            }
            str2 = sb.append("queryRule : spend=").append(spend);
            str = str2.append("ms").toString();
            OplusLog.d(z, "OplusFavoriteManager", str);
        } catch (Throwable th) {
            long spend2 = SystemClock.uptimeMillis() - start;
            OplusLog.d(DBG, "OplusFavoriteManager", str2 + spend2 + str);
            throw th;
        }
    }

    private void initRule(Context context) {
        IOplusFavoriteEngine iOplusFavoriteEngine = this.mEngine;
        if (iOplusFavoriteEngine != null) {
            iOplusFavoriteEngine.init();
            queryRule(context);
        }
    }

    /* loaded from: classes.dex */
    private static class FavoriteStart implements Runnable {
        private final WeakReference<OplusFavoriteCallback> mCallback;
        private final WeakReference<View> mView;

        public FavoriteStart(OplusFavoriteCallback callback, View view) {
            this.mCallback = new WeakReference<>(callback);
            this.mView = new WeakReference<>(view);
        }

        @Override // java.lang.Runnable
        public void run() {
            OplusFavoriteCallback callback = this.mCallback.get();
            View view = this.mView.get();
            if (callback != null && view != null) {
                Context context = view.getContext();
                OplusFavoriteResult result = new OplusFavoriteResult();
                result.setPackageName(context.getPackageName());
                callback.onFavoriteStart(context, result);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FavoriteQueryCallback extends OplusFavoriteQueryCallback {
        private final WeakReference<Context> mContext;
        private final WeakReference<IOplusFavoriteEngine> mEngine;

        public FavoriteQueryCallback(Context context, IOplusFavoriteEngine engine) {
            this.mContext = new WeakReference<>(context);
            this.mEngine = new WeakReference<>(engine);
        }

        @Override // com.oplus.favorite.OplusFavoriteQueryCallback, com.oplus.favorite.IOplusFavoriteQueryCallback
        public void onQueryResult(OplusFavoriteQueryResult result) {
            Context context = this.mContext.get();
            IOplusFavoriteEngine engine = this.mEngine.get();
            if (context != null && engine != null) {
                Bundle bundle = result.getBundle();
                String data = bundle.getString("data", null);
                engine.loadRule(context, data, new FavoriteLoadCallback(null));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class FavoriteCallback extends OplusFavoriteCallback {
        private final WeakReference<OplusFavoriteCallback> mCallback;

        @Override // com.oplus.favorite.IOplusFavoriteCallback
        public boolean isSettingOn(Context context) {
            return OplusFavoriteHelper.isSettingOn(context);
        }

        public FavoriteCallback(OplusFavoriteCallback callback) {
            this.mCallback = new WeakReference<>(callback);
        }

        @Override // com.oplus.favorite.OplusFavoriteCallback, com.oplus.favorite.IOplusFavoriteCallback
        public void onFavoriteStart(Context context, OplusFavoriteResult result) {
            OplusFavoriteCallback callback = this.mCallback.get();
            if (callback != null) {
                callback.onFavoriteStart(context, result);
            }
        }

        @Override // com.oplus.favorite.OplusFavoriteCallback, com.oplus.favorite.IOplusFavoriteCallback
        public void onFavoriteFinished(Context context, OplusFavoriteResult result) {
            OplusFavoriteCallback callback = this.mCallback.get();
            if (callback != null) {
                callback.onFavoriteFinished(context, result);
            }
        }

        public OplusFavoriteCallback getOplusFavoriteCallback() {
            return this.mCallback.get();
        }
    }

    /* loaded from: classes.dex */
    private static class FavoriteLoadCallback extends FavoriteCallback {
        public FavoriteLoadCallback(OplusFavoriteCallback callback) {
            super(callback);
        }

        @Override // com.oplus.favorite.OplusFavoriteManager.FavoriteCallback, com.oplus.favorite.IOplusFavoriteCallback
        public boolean isSettingOn(Context context) {
            return OplusFavoriteHelper.isSettingOn(context);
        }

        @Override // com.oplus.favorite.OplusFavoriteCallback, com.oplus.favorite.IOplusFavoriteCallback
        public void onLoadFinished(Context context, boolean noRule, boolean emptyRule, ArrayList<String> sceneList) {
            super.onLoadFinished(context, noRule, emptyRule, sceneList);
            Activity activity = OplusContextUtil.getActivityContext(context);
            if (activity == null) {
                logI("onLoadFinished", activity, "Not Activity");
                return;
            }
            if (noRule) {
                logI("onLoadFinished", activity, "No Rule List");
                return;
            }
            if (emptyRule) {
                logE("onLoadFinished", activity, "Empty Rule List");
                return;
            }
            if (!isFirstStart(context)) {
                logI("onLoadFinished", activity, "Load Again");
            } else {
                if (!isSettingOn(context)) {
                    logI("onLoadFinished", activity, "Setting Off");
                    return;
                }
                logI("onLoadFinished", activity, "Load First");
                clearFirstStart(context);
                notifyStart(context, context.getPackageName(), sceneList);
            }
        }

        private String buildMessage(String tag, Activity activity, String msg) {
            try {
                return "[" + this.TAG + "][" + tag + "] " + activity + " : " + msg;
            } catch (Exception e) {
                OplusLog.w(false, IOplusFavoriteConstans.TAG_UNIFY, "buildMessage exception! tag = " + tag + ", msg " + msg + ", e = " + e);
                return e.getMessage();
            }
        }

        private void logI(String tag, Activity activity, String msg) {
            OplusLog.i(false, IOplusFavoriteConstans.TAG_UNIFY, buildMessage(tag, activity, msg));
        }

        private void logE(String tag, Activity activity, String msg) {
            OplusLog.e(false, IOplusFavoriteConstans.TAG_UNIFY, buildMessage(tag, activity, msg));
        }

        private SharedPreferences getSharedPreferences(Context context) {
            return context.getSharedPreferences(OplusFavoriteManager.SETTINGS_FAVORITE, 0);
        }

        private void clearFirstStart(Context context) {
            SharedPreferences settings = getSharedPreferences(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(OplusFavoriteManager.SETTING_FIRST_START, false);
            editor.commit();
        }

        private boolean isFirstStart(Context context) {
            if (OplusFavoriteManager.NOTIFY_DEBUG) {
                return true;
            }
            SharedPreferences settings = getSharedPreferences(context);
            return settings.getBoolean(OplusFavoriteManager.SETTING_FIRST_START, true);
        }

        private void notifyStart(Context context, String packageName, ArrayList<String> sceneList) {
            OplusLog.i(false, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] notifyStart : " + packageName);
            try {
                Intent intent = new Intent();
                intent.setAction(OplusFavoriteManager.ACTION_FAVORITE_NOTIFY_START);
                intent.setPackage(OplusFavoriteManager.PACKAGE_FAVORITE);
                intent.putExtra("package_name", packageName);
                intent.putExtra(OplusFavoriteManager.EXTRA_SCENE_LIST, sceneList);
                context.startForegroundService(intent);
            } catch (Exception e) {
                OplusLog.e(this.TAG, e.getMessage(), e);
            }
        }
    }

    /* loaded from: classes.dex */
    private static class FavoriteProcessCallback extends FavoriteCallback {
        public FavoriteProcessCallback(OplusFavoriteCallback callback) {
            super(callback);
        }
    }

    /* loaded from: classes.dex */
    private static class FavoriteSaveCallback extends FavoriteCallback {
        public FavoriteSaveCallback(OplusFavoriteCallback callback) {
            super(callback);
        }

        @Override // com.oplus.favorite.OplusFavoriteManager.FavoriteCallback, com.oplus.favorite.OplusFavoriteCallback, com.oplus.favorite.IOplusFavoriteCallback
        public void onFavoriteFinished(Context context, OplusFavoriteResult result) {
            super.onFavoriteFinished(context, result);
            if (TextUtils.isEmpty(result.getError())) {
                onSaveSuccess(context);
            } else {
                onSaveFailed(context);
            }
        }

        private void onSaveSuccess(Context context) {
            String packageName = context.getPackageName();
            OplusLog.i(false, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] onSaveSuccess : " + packageName);
        }

        private void onSaveFailed(Context context) {
            String packageName = context.getPackageName();
            OplusLog.i(false, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] onSaveFailed : " + packageName);
            try {
                Intent intent = new Intent();
                intent.setAction(OplusFavoriteManager.ACTION_FAVORITE_NOTIFY_FAILED);
                intent.setPackage(OplusFavoriteManager.PACKAGE_FAVORITE);
                intent.putExtra("package_name", packageName);
                context.sendBroadcast(intent);
            } catch (Exception e) {
                OplusLog.e(this.TAG, e.getMessage(), e);
            }
        }
    }
}
