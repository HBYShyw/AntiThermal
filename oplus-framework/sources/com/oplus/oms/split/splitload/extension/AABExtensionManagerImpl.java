package com.oplus.oms.split.splitload.extension;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
final class AABExtensionManagerImpl implements AABExtensionManager {
    private final SplitComponentInfoProvider mInfoProvider;
    private List<String> mSplitActivities;
    private Map<String, List<String>> mSplitActivitiesMap;
    private List<String> mSplitReceivers;
    private List<String> mSplitServices;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AABExtensionManagerImpl(SplitComponentInfoProvider infoProvider) {
        this.mInfoProvider = infoProvider;
    }

    @Override // com.oplus.oms.split.splitload.extension.AABExtensionManager
    public Application createApplication(ClassLoader classLoader, String splitName) throws AABExtensionException {
        Throwable error = null;
        SplitComponentInfoProvider splitComponentInfoProvider = this.mInfoProvider;
        if (splitComponentInfoProvider == null) {
            return null;
        }
        String applicationName = splitComponentInfoProvider.getSplitApplicationName(splitName);
        if (!TextUtils.isEmpty(applicationName)) {
            try {
                Class<?> appClass = classLoader.loadClass(applicationName);
                return (Application) appClass.getDeclaredConstructor(null).newInstance(new Object[0]);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                error = e;
            }
        }
        if (error == null) {
            return null;
        }
        throw new AABExtensionException(error);
    }

    @Override // com.oplus.oms.split.splitload.extension.AABExtensionManager
    public void activeApplication(Application app, Context appContext) throws AABExtensionException {
        if (app != null) {
            Throwable error = null;
            try {
                Method method = ContextWrapper.class.getDeclaredMethod("attachBaseContext", Context.class);
                method.setAccessible(true);
                method.invoke(app, appContext);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                error = e;
            }
            if (error != null) {
                throw new AABExtensionException(error);
            }
        }
    }

    private Map<String, List<String>> getSplitActivitiesMap() {
        SplitComponentInfoProvider splitComponentInfoProvider;
        if (this.mSplitActivitiesMap == null && (splitComponentInfoProvider = this.mInfoProvider) != null) {
            this.mSplitActivitiesMap = splitComponentInfoProvider.getSplitActivitiesMap();
        }
        return this.mSplitActivitiesMap;
    }

    @Override // com.oplus.oms.split.splitload.extension.AABExtensionManager
    public boolean isSplitActivity(String name) {
        if (this.mSplitActivities == null && getSplitActivitiesMap() != null) {
            Collection<List<String>> values = getSplitActivitiesMap().values();
            List<String> allSplitActivities = new ArrayList<>(0);
            if (!values.isEmpty()) {
                for (List<String> activities : values) {
                    allSplitActivities.addAll(activities);
                }
            }
            this.mSplitActivities = allSplitActivities;
        }
        List<String> list = this.mSplitActivities;
        if (list == null) {
            return false;
        }
        return list.contains(name);
    }

    @Override // com.oplus.oms.split.splitload.extension.AABExtensionManager
    public String getSplitNameForComponent(String component) {
        SplitComponentInfoProvider splitComponentInfoProvider = this.mInfoProvider;
        if (splitComponentInfoProvider == null) {
            return null;
        }
        return splitComponentInfoProvider.getComponentMap().get(component);
    }

    @Override // com.oplus.oms.split.splitload.extension.AABExtensionManager
    public boolean isSplitService(String name) {
        SplitComponentInfoProvider splitComponentInfoProvider;
        if (this.mSplitServices == null && (splitComponentInfoProvider = this.mInfoProvider) != null) {
            this.mSplitServices = splitComponentInfoProvider.getSplitServices();
        }
        List<String> list = this.mSplitServices;
        if (list == null) {
            return false;
        }
        return list.contains(name);
    }

    @Override // com.oplus.oms.split.splitload.extension.AABExtensionManager
    public boolean isSplitReceiver(String name) {
        SplitComponentInfoProvider splitComponentInfoProvider;
        if (this.mSplitReceivers == null && (splitComponentInfoProvider = this.mInfoProvider) != null) {
            this.mSplitReceivers = splitComponentInfoProvider.getSplitReceivers();
        }
        List<String> list = this.mSplitReceivers;
        if (list == null) {
            return false;
        }
        return list.contains(name);
    }
}
