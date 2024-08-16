package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.IRegisteredServicesCacheExt;
import android.content.pm.RegisteredServicesCache;
import android.content.pm.RegisteredServicesCacheExtImpl;
import android.net.Uri;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.util.ArrayUtils;
import com.google.android.collect.Maps;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public class RegisteredServicesCacheExtImpl implements IRegisteredServicesCacheExt {
    private static final String PROPERTY_SYSTEM_SERVER_LOAD_REDUCTION_REGISTEREDSERVICESCACHE_ENABLED = "persist.sys.oplus.system_server_load_reduction.registeredServicesCache";
    private static final String TAG = "RegisteredServicesCacheExtImpl";

    public boolean enableLoadingReduction() {
        return SystemProperties.getBoolean(PROPERTY_SYSTEM_SERVER_LOAD_REDUCTION_REGISTEREDSERVICESCACHE_ENABLED, true);
    }

    public <V> IRegisteredServicesCacheExt.ServiceMapBuilder<V> getBuilder(RegisteredServicesCache<V> base) {
        return new ServiceMapBuilderImpl(base);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Predicate<ServiceInfo> buildModificationFilter(final int[] changedUids, Intent intent) {
        String[] changedPkgs = null;
        String[] changedComponents = null;
        if (intent != null) {
            changedComponents = intent.getStringArrayExtra("android.intent.extra.changed_component_name_list");
            String action = intent.getAction();
            if ("android.intent.action.PACKAGE_ADDED".equals(action) || "android.intent.action.PACKAGE_CHANGED".equals(action) || "android.intent.action.PACKAGE_REMOVED".equals(action)) {
                String pkg = getPackageFromIntent(intent);
                if (pkg != null) {
                    changedPkgs = new String[]{pkg};
                }
            } else if ("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE".equals(action) || "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(action)) {
                changedPkgs = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
            }
        }
        if (changedPkgs != null || changedComponents != null) {
            final String[] finalChangedPkgs = changedPkgs;
            final String[] finalChangedComponents = changedComponents;
            return new Predicate() { // from class: android.content.pm.RegisteredServicesCacheExtImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return RegisteredServicesCacheExtImpl.lambda$buildModificationFilter$0(finalChangedComponents, finalChangedPkgs, (ServiceInfo) obj);
                }
            };
        }
        return new Predicate() { // from class: android.content.pm.RegisteredServicesCacheExtImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return RegisteredServicesCacheExtImpl.lambda$buildModificationFilter$1(changedUids, (ServiceInfo) obj);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$buildModificationFilter$0(String[] finalChangedComponents, String[] finalChangedPkgs, ServiceInfo si) {
        return ArrayUtils.contains(finalChangedComponents, si.getComponentName()) || ArrayUtils.contains(finalChangedPkgs, si.packageName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$buildModificationFilter$1(int[] changedUids, ServiceInfo si) {
        if (changedUids != null) {
            return ArrayUtils.contains(changedUids, si.applicationInfo.uid);
        }
        return true;
    }

    private static String getPackageFromIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            return uri.getSchemeSpecificPart();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ServiceMapBuilderImpl<V> implements IRegisteredServicesCacheExt.ServiceMapBuilder<V> {
        private final RegisteredServicesCache<V> mBase;
        private final SparseArray<UserServicesExt<V>> mUserServicesMap = new SparseArray<>(2);

        public ServiceMapBuilderImpl(RegisteredServicesCache<V> base) {
            this.mBase = base;
        }

        public Map<ComponentName, RegisteredServicesCache.ServiceInfo<V>> buildServicesMapLocked(int userId, int[] changedUids, Intent intent) {
            UserServicesExt<V> userServicesExt;
            Predicate<ServiceInfo> modificationFilter = RegisteredServicesCacheExtImpl.buildModificationFilter(changedUids, intent);
            Map<ComponentName, RegisteredServicesCache.ServiceInfo<V>> serviceInfoMap = Maps.newHashMap();
            UserServicesExt<V> userServicesExt2 = this.mUserServicesMap.get(userId);
            if (userServicesExt2 != null) {
                userServicesExt = userServicesExt2;
            } else {
                UserServicesExt<V> userServicesExt3 = new UserServicesExt<>();
                this.mUserServicesMap.put(userId, userServicesExt3);
                userServicesExt = userServicesExt3;
            }
            if (userServicesExt.serviceComponentMap != null) {
                serviceInfoMap.putAll(userServicesExt.serviceComponentMap);
            }
            try {
                Trace.traceBegin(64L, "buildServicesMap");
                List<ResolveInfo> resolveInfos = this.mBase.queryIntentServices(userId);
                final Set<ComponentName> components = (Set) resolveInfos.stream().map(new Function() { // from class: android.content.pm.RegisteredServicesCacheExtImpl$ServiceMapBuilderImpl$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        ComponentName componentName;
                        componentName = ((ResolveInfo) obj).serviceInfo.getComponentName();
                        return componentName;
                    }
                }).collect(Collectors.toSet());
                serviceInfoMap.entrySet().removeIf(new Predicate() { // from class: android.content.pm.RegisteredServicesCacheExtImpl$ServiceMapBuilderImpl$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return RegisteredServicesCacheExtImpl.ServiceMapBuilderImpl.lambda$buildServicesMapLocked$1(components, (Map.Entry) obj);
                    }
                });
                for (final ResolveInfo resolveInfo : resolveInfos) {
                    ComponentName cn2 = resolveInfo.serviceInfo.getComponentName();
                    final Supplier<RegisteredServicesCache.ServiceInfo<V>> supplier = new Supplier() { // from class: android.content.pm.RegisteredServicesCacheExtImpl$ServiceMapBuilderImpl$$ExternalSyntheticLambda2
                        @Override // java.util.function.Supplier
                        public final Object get() {
                            RegisteredServicesCache.ServiceInfo lambda$buildServicesMapLocked$2;
                            lambda$buildServicesMapLocked$2 = RegisteredServicesCacheExtImpl.ServiceMapBuilderImpl.this.lambda$buildServicesMapLocked$2(resolveInfo);
                            return lambda$buildServicesMapLocked$2;
                        }
                    };
                    if (modificationFilter.test(resolveInfo.serviceInfo)) {
                        serviceInfoMap.compute(cn2, new BiFunction() { // from class: android.content.pm.RegisteredServicesCacheExtImpl$ServiceMapBuilderImpl$$ExternalSyntheticLambda3
                            @Override // java.util.function.BiFunction
                            public final Object apply(Object obj, Object obj2) {
                                return RegisteredServicesCacheExtImpl.ServiceMapBuilderImpl.lambda$buildServicesMapLocked$3(supplier, (ComponentName) obj, (RegisteredServicesCache.ServiceInfo) obj2);
                            }
                        });
                    } else {
                        serviceInfoMap.computeIfAbsent(cn2, new Function() { // from class: android.content.pm.RegisteredServicesCacheExtImpl$ServiceMapBuilderImpl$$ExternalSyntheticLambda4
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return RegisteredServicesCacheExtImpl.ServiceMapBuilderImpl.lambda$buildServicesMapLocked$4(supplier, (ComponentName) obj);
                            }
                        });
                    }
                }
                userServicesExt.serviceComponentMap = new HashMap(serviceInfoMap);
                return serviceInfoMap;
            } finally {
                Trace.traceEnd(64L);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ boolean lambda$buildServicesMapLocked$1(Set components, Map.Entry entry) {
            return !components.contains(entry.getKey());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ RegisteredServicesCache.ServiceInfo lambda$buildServicesMapLocked$2(ResolveInfo ri) {
            try {
                return this.mBase.parseServiceInfo(ri);
            } catch (IOException | XmlPullParserException e) {
                Log.w(RegisteredServicesCacheExtImpl.TAG, "Unable to load service info " + ri, e);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ RegisteredServicesCache.ServiceInfo lambda$buildServicesMapLocked$3(Supplier supplier, ComponentName cn1, RegisteredServicesCache.ServiceInfo serviceInfo1) {
            return (RegisteredServicesCache.ServiceInfo) supplier.get();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ RegisteredServicesCache.ServiceInfo lambda$buildServicesMapLocked$4(Supplier supplier, ComponentName unused) {
            return (RegisteredServicesCache.ServiceInfo) supplier.get();
        }
    }

    /* loaded from: classes.dex */
    private static class UserServicesExt<K> {
        public Map<ComponentName, RegisteredServicesCache.ServiceInfo<K>> serviceComponentMap;

        private UserServicesExt() {
        }
    }
}
