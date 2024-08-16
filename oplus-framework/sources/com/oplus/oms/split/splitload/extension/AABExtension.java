package com.oplus.oms.split.splitload.extension;

import android.app.Application;
import android.content.Context;
import com.oplus.oms.split.common.SplitBaseInfoProvider;
import com.oplus.oms.split.splitload.extension.fakecomponents.FakeActivity;
import com.oplus.oms.split.splitload.extension.fakecomponents.FakeReceiver;
import com.oplus.oms.split.splitload.extension.fakecomponents.FakeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class AABExtension {
    private static final AtomicReference<AABExtension> sAABCompatReference = new AtomicReference<>(null);
    private final AABExtensionManager mExtensionManager;
    private final Map<String, List<ContentProviderProxy>> mSplitContentProviderMap = new HashMap();

    private AABExtension() {
        Set<String> splitNames = getSplitNames();
        this.mExtensionManager = new AABExtensionManagerImpl(new SplitComponentInfoProvider(splitNames));
    }

    public static AABExtension getInstance() {
        AtomicReference<AABExtension> atomicReference = sAABCompatReference;
        if (atomicReference.get() == null) {
            atomicReference.set(new AABExtension());
        }
        return atomicReference.get();
    }

    public Application createApplication(ClassLoader classLoader, String splitName) throws AABExtensionException {
        return this.mExtensionManager.createApplication(classLoader, splitName);
    }

    public void activeApplication(Application splitApplication, Context appContext) throws AABExtensionException {
        this.mExtensionManager.activeApplication(splitApplication, appContext);
    }

    public void removeSplitProviders(String splitName) {
        List<ContentProviderProxy> providerProxies = this.mSplitContentProviderMap.get(splitName);
        if (providerProxies != null) {
            for (ContentProviderProxy providerProxy : providerProxies) {
                providerProxy.removeRealContentProvider();
            }
        }
    }

    public void createAndActivateSplitProviders(ClassLoader classLoader, String splitName) throws AABExtensionException {
        List<ContentProviderProxy> providerProxies = this.mSplitContentProviderMap.get(splitName);
        if (providerProxies != null) {
            for (ContentProviderProxy providerProxy : providerProxies) {
                providerProxy.createAndActivateRealContentProvider(classLoader);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void put(String splitName, ContentProviderProxy providerProxy) {
        List<ContentProviderProxy> providerProxies = this.mSplitContentProviderMap.get(splitName);
        if (providerProxies == null) {
            providerProxies = new ArrayList();
            this.mSplitContentProviderMap.put(splitName, providerProxies);
        }
        providerProxies.add(providerProxy);
    }

    public Class<?> getFakeComponent(String className) {
        if (this.mExtensionManager.isSplitActivity(className)) {
            return FakeActivity.class;
        }
        if (this.mExtensionManager.isSplitService(className)) {
            return FakeService.class;
        }
        if (this.mExtensionManager.isSplitReceiver(className)) {
            return FakeReceiver.class;
        }
        return null;
    }

    public String getSplitNameForComponent(String component) {
        return this.mExtensionManager.getSplitNameForComponent(component);
    }

    private Set<String> getSplitNames() {
        String[] dynamicFeatures = SplitBaseInfoProvider.getDynamicFeatures();
        Set<String> modules = new HashSet<>();
        if (dynamicFeatures != null && dynamicFeatures.length > 0) {
            modules.addAll(Arrays.asList(dynamicFeatures));
        }
        return modules;
    }

    public final void clear() {
        this.mSplitContentProviderMap.clear();
    }
}
