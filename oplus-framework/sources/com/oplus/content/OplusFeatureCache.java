package com.oplus.content;

import android.app.PropertyInvalidatedCache;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Log;
import com.oplus.annotation.OplusFeature;
import com.oplus.content.IOplusFeatureConfigManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: classes.dex */
public class OplusFeatureCache {
    public static final int ALL_TYPE = 6;
    private static final PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean> MEMORY_FEATURE_CACHE;
    private static final PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean> PERSIST_FEATURE_CACHE;
    private static final PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean> READONLY_FEATURE_CACHE;
    private static final String TAG = "OplusFeatureCache";
    private static final ArrayList<String> READONLYFEATURES = new ArrayList<>();
    private static final ArrayList<String> MEMORYFEATURES = new ArrayList<>();
    private static final ArrayList<String> PERSISTFEATURES = new ArrayList<>();
    private static final ArrayList<String> READONLYNATIVEFEATURES = new ArrayList<>();
    private static final ArrayList<String> MEMORYNATIVEFEATURES = new ArrayList<>();
    private static final ArrayList<String> PERSISTNATIVEFEATURES = new ArrayList<>();
    private static final ArrayMap<Integer, FeatureInvalidatedCache> FEATURES_CACHE_MAP = new ArrayMap<>();

    static {
        int i = 256;
        READONLY_FEATURE_CACHE = new PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean>(i, "sys.cache.has_feature_readonly") { // from class: com.oplus.content.OplusFeatureCache.1
            public Boolean recompute(HasOplusFeatureQuery query) {
                try {
                    return Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeatureIPC(query.name));
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        };
        PERSIST_FEATURE_CACHE = new PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean>(i, "sys.cache.has_feature_persist") { // from class: com.oplus.content.OplusFeatureCache.2
            public Boolean recompute(HasOplusFeatureQuery query) {
                try {
                    return Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeatureIPC(query.name));
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        };
        MEMORY_FEATURE_CACHE = new PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean>(i, "sys.cache.has_feature_memory") { // from class: com.oplus.content.OplusFeatureCache.3
            public Boolean recompute(HasOplusFeatureQuery query) {
                try {
                    return Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeatureIPC(query.name));
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        };
        for (int i2 = 0; i2 < IOplusFeatureConfigManager.FeatureID.INVALID.ordinal(); i2++) {
            FEATURES_CACHE_MAP.put(Integer.valueOf(i2), new FeatureInvalidatedCache(i2));
        }
    }

    public OplusFeatureCache() {
        init();
    }

    private void init() {
        long endTime;
        StringBuilder sb;
        long startTime = System.currentTimeMillis();
        try {
            try {
                Field[] fields = IOplusFeatureConfigList.class.getDeclaredFields();
                for (Field field : fields) {
                    boolean isAnnotation = field.isAnnotationPresent(OplusFeature.class);
                    if (isAnnotation) {
                        String featureName = (String) field.get(null);
                        OplusFeature annotation = (OplusFeature) field.getDeclaredAnnotation(OplusFeature.class);
                        if (annotation != null) {
                            switch (AnonymousClass4.$SwitchMap$com$oplus$annotation$OplusFeature$OplusFeatureType[annotation.value().ordinal()]) {
                                case 1:
                                    READONLYFEATURES.add(featureName);
                                    break;
                                case 2:
                                    MEMORYFEATURES.add(featureName);
                                    break;
                                case 3:
                                    PERSISTFEATURES.add(featureName);
                                    break;
                                case 4:
                                    READONLYNATIVEFEATURES.add(featureName);
                                    break;
                                case 5:
                                    MEMORYNATIVEFEATURES.add(featureName);
                                    break;
                                case 6:
                                    PERSISTNATIVEFEATURES.add(featureName);
                                    break;
                                default:
                                    Log.i(TAG, "Unknow type = " + annotation.value());
                                    break;
                            }
                        }
                    }
                }
                endTime = System.currentTimeMillis();
                sb = new StringBuilder();
            } catch (IllegalAccessException e) {
                Log.e(TAG, "IllegalAccessException:" + e.getMessage());
                endTime = System.currentTimeMillis();
                sb = new StringBuilder();
            }
            Log.i(TAG, sb.append("Milliseconds spent on init(): ").append(endTime - startTime).toString());
        } catch (Throwable th) {
            long endTime2 = System.currentTimeMillis();
            Log.i(TAG, "Milliseconds spent on init(): " + (endTime2 - startTime));
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.content.OplusFeatureCache$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$oplus$annotation$OplusFeature$OplusFeatureType;

        static {
            int[] iArr = new int[OplusFeature.OplusFeatureType.values().length];
            $SwitchMap$com$oplus$annotation$OplusFeature$OplusFeatureType = iArr;
            try {
                iArr[OplusFeature.OplusFeatureType.READONLY_FEATURE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$oplus$annotation$OplusFeature$OplusFeatureType[OplusFeature.OplusFeatureType.MEMORY_FEATURE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$oplus$annotation$OplusFeature$OplusFeatureType[OplusFeature.OplusFeatureType.PERSIST_FEATURE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$oplus$annotation$OplusFeature$OplusFeatureType[OplusFeature.OplusFeatureType.READONLY_NATIVE_FEATURE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$oplus$annotation$OplusFeature$OplusFeatureType[OplusFeature.OplusFeatureType.MEMORY_NATIVE_FEATURE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$oplus$annotation$OplusFeature$OplusFeatureType[OplusFeature.OplusFeatureType.PERSIST_NATIVE_FEATURE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class HasOplusFeatureQuery {
        public final String name;
        public final int type;

        public HasOplusFeatureQuery(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public String toString() {
            return String.format("HasOplusFeatureQuery(name=\"%s\", type=%d)", this.name, Integer.valueOf(this.type));
        }

        public boolean equals(Object o) {
            if (!(o instanceof HasOplusFeatureQuery)) {
                return false;
            }
            HasOplusFeatureQuery r = (HasOplusFeatureQuery) o;
            return Objects.equals(this.name, r.name) && this.type == r.type;
        }

        public int hashCode() {
            return Objects.hashCode(this.name);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class FeatureInvalidatedCache {
        private int mFeatureID;
        private final PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean> mMemoryFeatureCache;
        private final PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean> mPersistFeatureCache;
        private final PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean> mReadonlyFeatureCache;

        public FeatureInvalidatedCache(int featureID) {
            int i = 256;
            this.mReadonlyFeatureCache = new PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean>(i, "sys.cache.has_feature_readonly") { // from class: com.oplus.content.OplusFeatureCache.FeatureInvalidatedCache.1
                public Boolean recompute(HasOplusFeatureQuery query) {
                    try {
                        return Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeatureIPC(query.name, FeatureInvalidatedCache.this.mFeatureID));
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            };
            this.mPersistFeatureCache = new PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean>(i, "sys.cache.has_feature_persist") { // from class: com.oplus.content.OplusFeatureCache.FeatureInvalidatedCache.2
                public Boolean recompute(HasOplusFeatureQuery query) {
                    try {
                        return Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeatureIPC(query.name, FeatureInvalidatedCache.this.mFeatureID));
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            };
            this.mMemoryFeatureCache = new PropertyInvalidatedCache<HasOplusFeatureQuery, Boolean>(i, "sys.cache.has_feature_memory") { // from class: com.oplus.content.OplusFeatureCache.FeatureInvalidatedCache.3
                public Boolean recompute(HasOplusFeatureQuery query) {
                    try {
                        return Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeatureIPC(query.name, FeatureInvalidatedCache.this.mFeatureID));
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            };
            this.mFeatureID = 0;
            this.mFeatureID = featureID;
        }
    }

    public boolean query(String name) {
        if (MEMORYFEATURES.contains(name)) {
            return ((Boolean) MEMORY_FEATURE_CACHE.query(new HasOplusFeatureQuery(name, OplusFeature.OplusFeatureType.MEMORY_FEATURE.ordinal()))).booleanValue();
        }
        if (PERSISTFEATURES.contains(name)) {
            return ((Boolean) PERSIST_FEATURE_CACHE.query(new HasOplusFeatureQuery(name, OplusFeature.OplusFeatureType.PERSIST_FEATURE.ordinal()))).booleanValue();
        }
        if (READONLYFEATURES.contains(name)) {
            return ((Boolean) READONLY_FEATURE_CACHE.query(new HasOplusFeatureQuery(name, OplusFeature.OplusFeatureType.READONLY_FEATURE.ordinal()))).booleanValue();
        }
        if (isNativeFeature(name)) {
            return SystemProperties.getBoolean(name, false);
        }
        Log.e(TAG, "invalid oplus feature " + name);
        return false;
    }

    public boolean query(String name, int featureID) {
        if (featureID == IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT.ordinal()) {
            return query(name);
        }
        FeatureInvalidatedCache featureCache = FEATURES_CACHE_MAP.get(Integer.valueOf(featureID));
        if (featureCache == null) {
            Log.e(TAG, "invalid oplus feature id " + featureID);
            return false;
        }
        if (MEMORYFEATURES.contains(name)) {
            return ((Boolean) featureCache.mMemoryFeatureCache.query(new HasOplusFeatureQuery(name, OplusFeature.OplusFeatureType.READONLY_FEATURE.ordinal()))).booleanValue();
        }
        if (PERSISTFEATURES.contains(name)) {
            return ((Boolean) featureCache.mPersistFeatureCache.query(new HasOplusFeatureQuery(name, OplusFeature.OplusFeatureType.PERSIST_FEATURE.ordinal()))).booleanValue();
        }
        if (!READONLYFEATURES.contains(name)) {
            Log.e(TAG, "invalid oplus feature " + name + " for ID " + featureID);
            return false;
        }
        return ((Boolean) featureCache.mReadonlyFeatureCache.query(new HasOplusFeatureQuery(name, OplusFeature.OplusFeatureType.READONLY_FEATURE.ordinal()))).booleanValue();
    }

    private boolean isNativeFeature(String name) {
        if (MEMORYNATIVEFEATURES.contains(name) || READONLYNATIVEFEATURES.contains(name) || PERSISTNATIVEFEATURES.contains(name)) {
            return true;
        }
        return false;
    }

    public static void disableOplusFeatureCache(int featureType) {
        if (featureType == OplusFeature.OplusFeatureType.MEMORY_FEATURE.ordinal()) {
            MEMORY_FEATURE_CACHE.disableLocal();
            return;
        }
        if (featureType == OplusFeature.OplusFeatureType.PERSIST_FEATURE.ordinal()) {
            PERSIST_FEATURE_CACHE.disableLocal();
            return;
        }
        if (featureType == OplusFeature.OplusFeatureType.READONLY_FEATURE.ordinal()) {
            READONLY_FEATURE_CACHE.disableLocal();
        } else if (featureType == 6) {
            MEMORY_FEATURE_CACHE.disableLocal();
            PERSIST_FEATURE_CACHE.disableLocal();
            READONLY_FEATURE_CACHE.disableLocal();
        }
    }

    public static void invalidateOplusFeatureCache(int featureType) {
        if (featureType == OplusFeature.OplusFeatureType.MEMORY_FEATURE.ordinal()) {
            MEMORY_FEATURE_CACHE.invalidateCache();
            return;
        }
        if (featureType == OplusFeature.OplusFeatureType.PERSIST_FEATURE.ordinal()) {
            PERSIST_FEATURE_CACHE.invalidateCache();
            return;
        }
        if (featureType == OplusFeature.OplusFeatureType.READONLY_FEATURE.ordinal()) {
            READONLY_FEATURE_CACHE.invalidateCache();
        } else if (featureType == 6) {
            MEMORY_FEATURE_CACHE.invalidateCache();
            PERSIST_FEATURE_CACHE.invalidateCache();
            READONLY_FEATURE_CACHE.invalidateCache();
        }
    }

    public static void invalidateOplusFeatureCache(int featureType, int featureID) {
        FeatureInvalidatedCache featureCache = FEATURES_CACHE_MAP.get(Integer.valueOf(featureID));
        if (featureCache != null) {
            if (featureType == OplusFeature.OplusFeatureType.READONLY_FEATURE.ordinal()) {
                featureCache.mMemoryFeatureCache.invalidateCache();
                return;
            }
            if (featureType == OplusFeature.OplusFeatureType.PERSIST_FEATURE.ordinal()) {
                featureCache.mPersistFeatureCache.invalidateCache();
                return;
            }
            if (featureType == OplusFeature.OplusFeatureType.READONLY_FEATURE.ordinal()) {
                featureCache.mReadonlyFeatureCache.invalidateCache();
            } else if (featureType == 6) {
                featureCache.mMemoryFeatureCache.invalidateCache();
                featureCache.mPersistFeatureCache.invalidateCache();
                featureCache.mReadonlyFeatureCache.invalidateCache();
            }
        }
    }
}
