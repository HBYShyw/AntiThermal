package com.oplus.content;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.content.AppfeatureHelper;
import com.oplus.content.IOplusFeatureConfigManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
class AppFeatureCache {
    private static final String TAG = "AppFeatureCache";
    protected static final ConcurrentHashMap<Integer, List<AppFeatureData>> mAppFeatureCacheListMap = new ConcurrentHashMap<>();
    public static boolean mCachedEnabled = false;
    static ConcurrentHashMap<Integer, AppfeatureHelper.CACHE_MODE> mCachedModeDefaultMap = new ConcurrentHashMap<>();

    /* loaded from: classes.dex */
    private static class AppFeatureCacheHolder {
        static final AppFeatureCache INSTANCE = new AppFeatureCache();

        private AppFeatureCacheHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AppfeatureHelper.CACHE_MODE getCachedMode(int featureID) {
        if (featureID < 0 || featureID >= IOplusFeatureConfigManager.FeatureID.INVALID.ordinal()) {
            throw new IllegalArgumentException("invlid feature id");
        }
        AppfeatureHelper.CACHE_MODE cachedModeDefault = mCachedModeDefaultMap.get(Integer.valueOf(featureID));
        if (cachedModeDefault != null) {
            return cachedModeDefault;
        }
        throw new IllegalArgumentException("getCachedMode featureID is not support");
    }

    public static AppFeatureCache getInstance() {
        return AppFeatureCacheHolder.INSTANCE;
    }

    private AppFeatureCache() {
        for (int i = 0; i < IOplusFeatureConfigManager.FeatureID.INVALID.ordinal(); i++) {
            mCachedModeDefaultMap.put(Integer.valueOf(i), AppfeatureHelper.CACHE_MODE.CACHE_AND_DB);
        }
    }

    private boolean isEmpty(List list) {
        if (list != null && list.size() == 0) {
            return true;
        }
        return false;
    }

    public Cursor getCursorFromCache(int featureID, String featureName) {
        ConcurrentHashMap<Integer, List<AppFeatureData>> concurrentHashMap = mAppFeatureCacheListMap;
        List<AppFeatureData> featureCacheList = concurrentHashMap.get(Integer.valueOf(featureID));
        if (featureCacheList == null) {
            featureCacheList = new ArrayList();
            concurrentHashMap.put(Integer.valueOf(featureID), featureCacheList);
        }
        if (isEmpty(featureCacheList)) {
            return null;
        }
        return getFeatureCacheData(featureCacheList, featureName);
    }

    public void enableAppFeatureCache(ContentResolver resolver, int featureID, List<String> featureList, AppfeatureHelper.CACHE_MODE cacheMode) {
        if (featureID < 0 || featureID >= IOplusFeatureConfigManager.FeatureID.INVALID.ordinal()) {
            throw new IllegalArgumentException("invlid feature id");
        }
        Log.i(TAG, "enableAppFeatureCache: " + featureID);
        AppfeatureHelper.CACHE_MODE cachedModeDefault = mCachedModeDefaultMap.get(Integer.valueOf(featureID));
        if (cachedModeDefault != null) {
        }
        ConcurrentHashMap<Integer, List<AppFeatureData>> concurrentHashMap = mAppFeatureCacheListMap;
        List<AppFeatureData> featureCacheList = concurrentHashMap.get(Integer.valueOf(featureID));
        if (featureCacheList == null) {
            featureCacheList = new ArrayList();
            concurrentHashMap.put(Integer.valueOf(featureID), featureCacheList);
        }
        clearCursorInCache(featureCacheList);
        invalidateAppFeatureCache(resolver, featureID, featureCacheList, featureList);
    }

    private Cursor getFeatureCacheData(List<AppFeatureData> list, String featureName) {
        MatrixCursor cursor = getMatrixCursor();
        synchronized (AppFeatureCache.class) {
            for (AppFeatureData appFeatureData : list) {
                if (appFeatureData != null && appFeatureData.getFeatureName() != null && appFeatureData.getFeatureName().equals(featureName)) {
                    cursor.addRow(new Object[]{appFeatureData.getId(), appFeatureData.getFeatureName(), appFeatureData.getParameters(), appFeatureData.getJasonStr()});
                }
            }
        }
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    private void insertCursorToCache(List<AppFeatureData> list, Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }
        do {
            Integer id = Integer.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            String featureName = cursor.getString(cursor.getColumnIndexOrThrow("featurename"));
            String parameters = cursor.getString(cursor.getColumnIndexOrThrow("parameters"));
            String lists = cursor.getString(cursor.getColumnIndexOrThrow("lists"));
            synchronized (AppFeatureCache.class) {
                list.add(new AppFeatureData(id, featureName, parameters, lists));
            }
        } while (cursor.moveToNext());
    }

    private MatrixCursor getMatrixCursor() {
        String[] columnNames = {"_id", "featurename", "parameters", "lists"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        return cursor;
    }

    private void clearCursorInCache(List<AppFeatureData> list) {
        synchronized (AppFeatureCache.class) {
            Log.i(TAG, "clearCursorInCache");
            list.clear();
        }
    }

    private void invalidateAppFeatureCache(ContentResolver resolver, int featureID, List<AppFeatureData> cacheList, List<String> featureList) {
        Cursor cursor;
        Log.i(TAG, "invalidateAppFeatureCache run in");
        Uri uri = AppfeatureHelper.getUriFromFeatureMap(featureID);
        if (isEmpty(featureList)) {
            cursor = resolver.query(uri, null, null, null, null);
        } else {
            cursor = resolver.query(uri, null, "featurename in('" + TextUtils.join("','", featureList) + "')", null, null);
        }
        insertCursorToCache(cacheList, cursor);
        if (cursor != null) {
            cursor.close();
        }
        Log.i(TAG, "invalidateAppFeatureCache size: " + cacheList.size());
    }

    /* loaded from: classes.dex */
    public static class AppFeatureData {
        private final Integer _id;
        private final String featureName;
        private final String jasonStr;
        private final String parameters;

        public AppFeatureData(Integer id, String feature, String param, String jason) {
            this._id = id;
            this.featureName = feature;
            this.parameters = param;
            this.jasonStr = jason;
        }

        public String getFeatureName() {
            return this.featureName;
        }

        public String getParameters() {
            return this.parameters;
        }

        public String getJasonStr() {
            return this.jasonStr;
        }

        public Integer getId() {
            return this._id;
        }

        public String toString() {
            return "AppFeatureData{_id='" + this._id + "'featureName='" + this.featureName + "', parameters='" + this.parameters + "', jasonStr='" + this.jasonStr + "'}";
        }
    }
}
