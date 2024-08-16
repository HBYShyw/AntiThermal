package com.android.server.pm.resolution;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.AuxiliaryResolveInfo;
import android.content.pm.InstantAppResolveInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.server.IntentResolver;
import com.android.server.pm.Computer;
import com.android.server.pm.PackageManagerException;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.UserManagerService;
import com.android.server.pm.UserNeedsBadgingCache;
import com.android.server.pm.parsing.PackageInfoUtils;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.PackageStateUtils;
import com.android.server.pm.pkg.PackageUserStateInternal;
import com.android.server.pm.pkg.component.ComponentMutateUtils;
import com.android.server.pm.pkg.component.ParsedActivity;
import com.android.server.pm.pkg.component.ParsedComponent;
import com.android.server.pm.pkg.component.ParsedIntentInfo;
import com.android.server.pm.pkg.component.ParsedMainComponent;
import com.android.server.pm.pkg.component.ParsedProvider;
import com.android.server.pm.pkg.component.ParsedProviderImpl;
import com.android.server.pm.pkg.component.ParsedService;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import com.android.server.pm.resolution.IComponentResolverExt;
import com.android.server.pm.snapshot.PackageDataSnapshot;
import com.android.server.utils.Snappable;
import com.android.server.utils.SnapshotCache;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ComponentResolver extends ComponentResolverLocked implements Snappable<ComponentResolverApi> {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_FILTERS = false;
    private static final boolean DEBUG_SHOW_INFO = false;
    private static final Set<String> PROTECTED_ACTIONS;
    public static final Comparator<ResolveInfo> RESOLVE_PRIORITY_SORTER;
    private static final String TAG = "PackageManager";
    private static final IComponentResolverExt.IStaticExt mStaticComponentResolverExt;
    private final IComponentResolverExt mComponentResolverExt;
    boolean mDeferProtectedFilters;
    List<Pair<ParsedMainComponent, ParsedIntentInfo>> mProtectedFilters;
    final SnapshotCache<ComponentResolverApi> mSnapshot;

    private void onChanged() {
        dispatchChange(this);
    }

    static {
        ArraySet arraySet = new ArraySet();
        PROTECTED_ACTIONS = arraySet;
        arraySet.add("android.intent.action.SEND");
        arraySet.add("android.intent.action.SENDTO");
        arraySet.add("android.intent.action.SEND_MULTIPLE");
        arraySet.add("android.intent.action.VIEW");
        RESOLVE_PRIORITY_SORTER = new Comparator() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$static$0;
                lambda$static$0 = ComponentResolver.lambda$static$0((ResolveInfo) obj, (ResolveInfo) obj2);
                return lambda$static$0;
            }
        };
        mStaticComponentResolverExt = (IComponentResolverExt.IStaticExt) ExtLoader.type(IComponentResolverExt.IStaticExt.class).create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$0(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
        int i = resolveInfo.priority;
        int i2 = resolveInfo2.priority;
        if (i != i2) {
            return i > i2 ? -1 : 1;
        }
        int i3 = resolveInfo.preferredOrder;
        int i4 = resolveInfo2.preferredOrder;
        if (i3 != i4) {
            return i3 > i4 ? -1 : 1;
        }
        boolean z = resolveInfo.isDefault;
        if (z != resolveInfo2.isDefault) {
            return z ? -1 : 1;
        }
        int i5 = resolveInfo.match;
        int i6 = resolveInfo2.match;
        if (i5 != i6) {
            return i5 > i6 ? -1 : 1;
        }
        boolean z2 = resolveInfo.system;
        if (z2 != resolveInfo2.system) {
            return z2 ? -1 : 1;
        }
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        if (activityInfo != null) {
            return activityInfo.packageName.compareTo(resolveInfo2.activityInfo.packageName);
        }
        ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        if (serviceInfo != null) {
            return serviceInfo.packageName.compareTo(resolveInfo2.serviceInfo.packageName);
        }
        ProviderInfo providerInfo = resolveInfo.providerInfo;
        if (providerInfo != null) {
            return providerInfo.packageName.compareTo(resolveInfo2.providerInfo.packageName);
        }
        return 0;
    }

    public ComponentResolver(UserManagerService userManagerService, final UserNeedsBadgingCache userNeedsBadgingCache) {
        super(userManagerService);
        this.mDeferProtectedFilters = true;
        this.mComponentResolverExt = (IComponentResolverExt) ExtLoader.type(IComponentResolverExt.class).base(this).create();
        this.mActivities = new ActivityIntentResolver(userManagerService, userNeedsBadgingCache);
        this.mProviders = new ProviderIntentResolver(userManagerService);
        this.mReceivers = new ReceiverIntentResolver(userManagerService, userNeedsBadgingCache);
        this.mServices = new ServiceIntentResolver(userManagerService);
        this.mProvidersByAuthority = new ArrayMap<>();
        this.mDeferProtectedFilters = true;
        this.mSnapshot = new SnapshotCache<ComponentResolverApi>(this, this) { // from class: com.android.server.pm.resolution.ComponentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.server.utils.SnapshotCache
            public ComponentResolverApi createSnapshot() {
                ComponentResolverSnapshot componentResolverSnapshot;
                synchronized (ComponentResolver.this.mLock) {
                    componentResolverSnapshot = new ComponentResolverSnapshot(ComponentResolver.this, userNeedsBadgingCache);
                }
                return componentResolverSnapshot;
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.utils.Snappable
    public ComponentResolverApi snapshot() {
        return this.mSnapshot.snapshot();
    }

    public void addAllComponents(AndroidPackage androidPackage, boolean z, String str, Computer computer) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            addActivitiesLocked(computer, androidPackage, arrayList, z);
            addReceiversLocked(computer, androidPackage, z);
            addProvidersLocked(computer, androidPackage, z);
            addServicesLocked(computer, androidPackage, z);
            onChanged();
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            Pair pair = (Pair) arrayList.get(size);
            PackageStateInternal disabledSystemPackage = computer.getDisabledSystemPackage(((ParsedActivity) pair.first).getPackageName());
            List<ParsedActivity> list = null;
            AndroidPackageInternal pkg = disabledSystemPackage == null ? null : disabledSystemPackage.getPkg();
            if (pkg != null) {
                list = pkg.getActivities();
            }
            adjustPriority(computer, list, (ParsedActivity) pair.first, (ParsedIntentInfo) pair.second, str);
            onChanged();
        }
    }

    public void removeAllComponents(AndroidPackage androidPackage, boolean z) {
        synchronized (this.mLock) {
            removeAllComponentsLocked(androidPackage, z);
            onChanged();
        }
    }

    public void fixProtectedFilterPriorities(String str) {
        synchronized (this.mLock) {
            if (this.mDeferProtectedFilters) {
                this.mDeferProtectedFilters = false;
                List<Pair<ParsedMainComponent, ParsedIntentInfo>> list = this.mProtectedFilters;
                if (list != null && list.size() != 0) {
                    List<Pair<ParsedMainComponent, ParsedIntentInfo>> list2 = this.mProtectedFilters;
                    this.mProtectedFilters = null;
                    for (int size = list2.size() - 1; size >= 0; size--) {
                        Pair<ParsedMainComponent, ParsedIntentInfo> pair = list2.get(size);
                        ParsedMainComponent parsedMainComponent = (ParsedMainComponent) pair.first;
                        IntentFilter intentFilter = ((ParsedIntentInfo) pair.second).getIntentFilter();
                        String packageName = parsedMainComponent.getPackageName();
                        parsedMainComponent.getClassName();
                        if (!packageName.equals(str)) {
                            intentFilter.setPriority(0);
                        }
                    }
                    onChanged();
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private void addActivitiesLocked(Computer computer, AndroidPackage androidPackage, List<Pair<ParsedActivity, ParsedIntentInfo>> list, boolean z) {
        int size = ArrayUtils.size(androidPackage.getActivities());
        StringBuilder sb = null;
        int i = 0;
        while (i < size) {
            ParsedActivity parsedActivity = androidPackage.getActivities().get(i);
            this.mActivities.addActivity(computer, parsedActivity, "activity", list);
            this.mComponentResolverExt.onAddActivitiesLocked(parsedActivity, androidPackage);
            if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
                if (sb == null) {
                    sb = new StringBuilder(256);
                } else {
                    sb.append(' ');
                    sb = sb;
                }
                sb.append(parsedActivity.getName());
            }
            i++;
            sb = sb;
        }
        if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  Activities: ");
            CharSequence charSequence = sb;
            if (sb == null) {
                charSequence = "<NONE>";
            }
            sb2.append((Object) charSequence);
            Log.d(TAG, sb2.toString());
        }
    }

    @GuardedBy({"mLock"})
    private void addProvidersLocked(Computer computer, AndroidPackage androidPackage, boolean z) {
        int size = ArrayUtils.size(androidPackage.getProviders());
        StringBuilder sb = null;
        int i = 0;
        while (i < size) {
            ParsedProvider parsedProvider = androidPackage.getProviders().get(i);
            this.mProviders.addProvider(computer, parsedProvider);
            this.mComponentResolverExt.onAddProvidersLocked(parsedProvider, androidPackage);
            if (parsedProvider.getAuthority() != null) {
                String[] split = parsedProvider.getAuthority().split(";");
                ComponentMutateUtils.setAuthority(parsedProvider, null);
                for (int i2 = 0; i2 < split.length; i2++) {
                    if (i2 == 1 && parsedProvider.isSyncable()) {
                        ParsedProviderImpl parsedProviderImpl = new ParsedProviderImpl(parsedProvider);
                        ComponentMutateUtils.setSyncable(parsedProviderImpl, false);
                        parsedProvider = parsedProviderImpl;
                    }
                    if (this.mProvidersByAuthority.containsKey(split[i2])) {
                        IComponentResolverExt iComponentResolverExt = this.mComponentResolverExt;
                        String str = split[i2];
                        if (!iComponentResolverExt.shouldOverrideProviderByAuthority(str, androidPackage, this.mProvidersByAuthority.get(str))) {
                            ParsedProvider parsedProvider2 = this.mProvidersByAuthority.get(split[i2]);
                            ComponentName componentName = (parsedProvider2 == null || parsedProvider2.getComponentName() == null) ? null : parsedProvider2.getComponentName();
                            Slog.w(TAG, "Skipping provider name " + split[i2] + " (in package " + androidPackage.getPackageName() + "): name already used by " + (componentName != null ? componentName.getPackageName() : "?"));
                        }
                    }
                    this.mProvidersByAuthority.put(split[i2], parsedProvider);
                    if (parsedProvider.getAuthority() == null) {
                        ComponentMutateUtils.setAuthority(parsedProvider, split[i2]);
                    } else {
                        ComponentMutateUtils.setAuthority(parsedProvider, parsedProvider.getAuthority() + ";" + split[i2]);
                    }
                    if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
                        Log.d(TAG, "Registered content provider: " + split[i2] + ", className = " + parsedProvider.getName() + ", isSyncable = " + parsedProvider.isSyncable());
                    }
                }
            }
            if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
                if (sb == null) {
                    sb = new StringBuilder(256);
                } else {
                    sb.append(' ');
                    sb = sb;
                }
                sb.append(parsedProvider.getName());
            }
            i++;
            sb = sb;
        }
        if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  Providers: ");
            CharSequence charSequence = sb;
            if (sb == null) {
                charSequence = "<NONE>";
            }
            sb2.append((Object) charSequence);
            Log.d(TAG, sb2.toString());
        }
    }

    @GuardedBy({"mLock"})
    private void addReceiversLocked(Computer computer, AndroidPackage androidPackage, boolean z) {
        int size = ArrayUtils.size(androidPackage.getReceivers());
        int i = 0;
        StringBuilder sb = null;
        while (i < size) {
            ParsedActivity parsedActivity = androidPackage.getReceivers().get(i);
            this.mReceivers.addActivity(computer, parsedActivity, ParsingPackageUtils.TAG_RECEIVER, null);
            this.mComponentResolverExt.onAddReceiversLocked(parsedActivity, androidPackage);
            if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
                if (sb == null) {
                    sb = new StringBuilder(256);
                } else {
                    sb.append(' ');
                    sb = sb;
                }
                sb.append(parsedActivity.getName());
            }
            i++;
            sb = sb;
        }
        if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  Receivers: ");
            CharSequence charSequence = sb;
            if (sb == null) {
                charSequence = "<NONE>";
            }
            sb2.append((Object) charSequence);
            Log.d(TAG, sb2.toString());
        }
    }

    @GuardedBy({"mLock"})
    private void addServicesLocked(Computer computer, AndroidPackage androidPackage, boolean z) {
        int size = ArrayUtils.size(androidPackage.getServices());
        StringBuilder sb = null;
        int i = 0;
        while (i < size) {
            ParsedService parsedService = androidPackage.getServices().get(i);
            this.mServices.addService(computer, parsedService);
            this.mComponentResolverExt.onAddServicesLocked(parsedService, androidPackage);
            if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
                if (sb == null) {
                    sb = new StringBuilder(256);
                } else {
                    sb.append(' ');
                    sb = sb;
                }
                sb.append(parsedService.getName());
            }
            i++;
            sb = sb;
        }
        if (PackageManagerService.DEBUG_PACKAGE_SCANNING && z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  Services: ");
            CharSequence charSequence = sb;
            if (sb == null) {
                charSequence = "<NONE>";
            }
            sb2.append((Object) charSequence);
            Log.d(TAG, sb2.toString());
        }
    }

    private static <T> void getIntentListSubset(List<ParsedIntentInfo> list, Function<IntentFilter, Iterator<T>> function, Iterator<T> it) {
        boolean z;
        while (it.hasNext() && list.size() != 0) {
            T next = it.next();
            Iterator<ParsedIntentInfo> it2 = list.iterator();
            while (it2.hasNext()) {
                Iterator<T> apply = function.apply(it2.next().getIntentFilter());
                while (apply != null && apply.hasNext()) {
                    T next2 = apply.next();
                    if (next2 != null && next2.equals(next)) {
                        z = true;
                        break;
                    }
                }
                z = false;
                if (!z) {
                    it2.remove();
                }
            }
        }
    }

    private static boolean isProtectedAction(IntentFilter intentFilter) {
        Iterator<String> actionsIterator = intentFilter.actionsIterator();
        while (actionsIterator != null && actionsIterator.hasNext()) {
            if (PROTECTED_ACTIONS.contains(actionsIterator.next())) {
                return true;
            }
        }
        return false;
    }

    private static ParsedActivity findMatchingActivity(List<ParsedActivity> list, ParsedActivity parsedActivity) {
        Iterator<ParsedActivity> it = list.iterator();
        while (it.hasNext()) {
            ParsedActivity next = it.next();
            if (next.getName().equals(parsedActivity.getName()) || next.getName().equals(parsedActivity.getTargetActivity())) {
                return next;
            }
            if (next.getTargetActivity() != null && (next.getTargetActivity().equals(parsedActivity.getName()) || next.getTargetActivity().equals(parsedActivity.getTargetActivity()))) {
                return next;
            }
        }
        return null;
    }

    private void adjustPriority(Computer computer, List<ParsedActivity> list, ParsedActivity parsedActivity, ParsedIntentInfo parsedIntentInfo, String str) {
        IntentFilter intentFilter = parsedIntentInfo.getIntentFilter();
        if (intentFilter.getPriority() <= 0) {
            return;
        }
        String packageName = parsedActivity.getPackageName();
        boolean isPrivileged = computer.getPackageStateInternal(packageName).isPrivileged();
        parsedActivity.getClassName();
        int i = 0;
        if (!isPrivileged) {
            intentFilter.setPriority(0);
            return;
        }
        if (isProtectedAction(intentFilter)) {
            if (this.mDeferProtectedFilters) {
                if (this.mProtectedFilters == null) {
                    this.mProtectedFilters = new ArrayList();
                }
                this.mProtectedFilters.add(Pair.create(parsedActivity, parsedIntentInfo));
                return;
            } else {
                if (packageName.equals(str)) {
                    return;
                }
                intentFilter.setPriority(0);
                return;
            }
        }
        if (list == null) {
            return;
        }
        ParsedActivity findMatchingActivity = findMatchingActivity(list, parsedActivity);
        if (findMatchingActivity == null) {
            intentFilter.setPriority(0);
            return;
        }
        ArrayList arrayList = new ArrayList(findMatchingActivity.getIntents());
        Iterator<String> actionsIterator = intentFilter.actionsIterator();
        if (actionsIterator != null) {
            getIntentListSubset(arrayList, new Function() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((IntentFilter) obj).actionsIterator();
                }
            }, actionsIterator);
            if (arrayList.size() == 0) {
                intentFilter.setPriority(0);
                return;
            }
        }
        Iterator<String> categoriesIterator = intentFilter.categoriesIterator();
        if (categoriesIterator != null) {
            getIntentListSubset(arrayList, new Function() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((IntentFilter) obj).categoriesIterator();
                }
            }, categoriesIterator);
            if (arrayList.size() == 0) {
                intentFilter.setPriority(0);
                return;
            }
        }
        Iterator<String> schemesIterator = intentFilter.schemesIterator();
        if (schemesIterator != null) {
            getIntentListSubset(arrayList, new Function() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((IntentFilter) obj).schemesIterator();
                }
            }, schemesIterator);
            if (arrayList.size() == 0) {
                intentFilter.setPriority(0);
                return;
            }
        }
        Iterator<IntentFilter.AuthorityEntry> authoritiesIterator = intentFilter.authoritiesIterator();
        if (authoritiesIterator != null) {
            getIntentListSubset(arrayList, new Function() { // from class: com.android.server.pm.resolution.ComponentResolver$$ExternalSyntheticLambda4
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((IntentFilter) obj).authoritiesIterator();
                }
            }, authoritiesIterator);
            if (arrayList.size() == 0) {
                intentFilter.setPriority(0);
                return;
            }
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            i = Math.max(i, ((ParsedIntentInfo) arrayList.get(size)).getIntentFilter().getPriority());
        }
        if (intentFilter.getPriority() > i) {
            intentFilter.setPriority(i);
        }
    }

    @GuardedBy({"mLock"})
    private void removeAllComponentsLocked(AndroidPackage androidPackage, boolean z) {
        int size = ArrayUtils.size(androidPackage.getActivities());
        StringBuilder sb = null;
        StringBuilder sb2 = null;
        int i = 0;
        while (i < size) {
            ParsedActivity parsedActivity = androidPackage.getActivities().get(i);
            this.mActivities.removeActivity(parsedActivity, "activity");
            if (PackageManagerService.DEBUG_REMOVE && z) {
                if (sb2 == null) {
                    sb2 = new StringBuilder(256);
                } else {
                    sb2.append(' ');
                    sb2 = sb2;
                }
                sb2.append(parsedActivity.getName());
            }
            i++;
            sb2 = sb2;
        }
        if (PackageManagerService.DEBUG_REMOVE && z) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("  Activities: ");
            Object obj = sb2;
            if (sb2 == null) {
                obj = "<NONE>";
            }
            sb3.append(obj);
            Log.d(TAG, sb3.toString());
        }
        int size2 = ArrayUtils.size(androidPackage.getProviders());
        StringBuilder sb4 = null;
        int i2 = 0;
        while (i2 < size2) {
            ParsedProvider parsedProvider = androidPackage.getProviders().get(i2);
            this.mProviders.removeProvider(parsedProvider);
            if (parsedProvider.getAuthority() != null) {
                String[] split = parsedProvider.getAuthority().split(";");
                for (int i3 = 0; i3 < split.length; i3++) {
                    if (this.mProvidersByAuthority.get(split[i3]) == parsedProvider) {
                        this.mProvidersByAuthority.remove(split[i3]);
                        if (PackageManagerService.DEBUG_REMOVE && z) {
                            Log.d(TAG, "Unregistered content provider: " + split[i3] + ", className = " + parsedProvider.getName() + ", isSyncable = " + parsedProvider.isSyncable());
                        }
                    }
                }
                if (PackageManagerService.DEBUG_REMOVE && z) {
                    if (sb4 == null) {
                        sb4 = new StringBuilder(256);
                    } else {
                        sb4.append(' ');
                        sb4 = sb4;
                    }
                    sb4.append(parsedProvider.getName());
                }
            }
            i2++;
            sb4 = sb4;
        }
        if (PackageManagerService.DEBUG_REMOVE && z) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("  Providers: ");
            Object obj2 = sb4;
            if (sb4 == null) {
                obj2 = "<NONE>";
            }
            sb5.append(obj2);
            Log.d(TAG, sb5.toString());
        }
        int size3 = ArrayUtils.size(androidPackage.getReceivers());
        StringBuilder sb6 = null;
        int i4 = 0;
        while (i4 < size3) {
            ParsedActivity parsedActivity2 = androidPackage.getReceivers().get(i4);
            this.mReceivers.removeActivity(parsedActivity2, ParsingPackageUtils.TAG_RECEIVER);
            if (PackageManagerService.DEBUG_REMOVE && z) {
                if (sb6 == null) {
                    sb6 = new StringBuilder(256);
                } else {
                    sb6.append(' ');
                    sb6 = sb6;
                }
                sb6.append(parsedActivity2.getName());
            }
            i4++;
            sb6 = sb6;
        }
        if (PackageManagerService.DEBUG_REMOVE && z) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("  Receivers: ");
            Object obj3 = sb6;
            if (sb6 == null) {
                obj3 = "<NONE>";
            }
            sb7.append(obj3);
            Log.d(TAG, sb7.toString());
        }
        int size4 = ArrayUtils.size(androidPackage.getServices());
        for (int i5 = 0; i5 < size4; i5++) {
            ParsedService parsedService = androidPackage.getServices().get(i5);
            this.mServices.removeService(parsedService);
            if (PackageManagerService.DEBUG_REMOVE && z) {
                if (sb == null) {
                    sb = new StringBuilder(256);
                } else {
                    sb.append(' ');
                }
                sb.append(parsedService.getName());
            }
        }
        if (PackageManagerService.DEBUG_REMOVE && z) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append("  Services: ");
            sb8.append(sb != null ? sb : "<NONE>");
            Log.d(TAG, sb8.toString());
        }
    }

    public void assertProvidersNotDefined(AndroidPackage androidPackage) throws PackageManagerException {
        synchronized (this.mLock) {
            int size = ArrayUtils.size(androidPackage.getProviders());
            for (int i = 0; i < size; i++) {
                ParsedProvider parsedProvider = androidPackage.getProviders().get(i);
                if (parsedProvider.getAuthority() != null) {
                    String[] split = parsedProvider.getAuthority().split(";");
                    for (int i2 = 0; i2 < split.length; i2++) {
                        if (this.mProvidersByAuthority.containsKey(split[i2])) {
                            ParsedProvider parsedProvider2 = this.mProvidersByAuthority.get(split[i2]);
                            String packageName = (parsedProvider2 == null || parsedProvider2.getComponentName() == null) ? "?" : parsedProvider2.getComponentName().getPackageName();
                            if (!packageName.equals(androidPackage.getPackageName())) {
                                throw new PackageManagerException(-13, "Can't install because provider name " + split[i2] + " (in package " + androidPackage.getPackageName() + ") is already used by " + packageName);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class MimeGroupsAwareIntentResolver<F extends Pair<? extends ParsedComponent, ParsedIntentInfo>, R> extends IntentResolver<F, R> {
        private boolean mIsUpdatingMimeGroup;
        private final ArrayMap<String, F[]> mMimeGroupToFilter;
        protected final UserManagerService mUserManager;

        MimeGroupsAwareIntentResolver(UserManagerService userManagerService) {
            this.mMimeGroupToFilter = new ArrayMap<>();
            this.mIsUpdatingMimeGroup = false;
            this.mUserManager = userManagerService;
        }

        MimeGroupsAwareIntentResolver(MimeGroupsAwareIntentResolver<F, R> mimeGroupsAwareIntentResolver, UserManagerService userManagerService) {
            ArrayMap<String, F[]> arrayMap = new ArrayMap<>();
            this.mMimeGroupToFilter = arrayMap;
            this.mIsUpdatingMimeGroup = false;
            this.mUserManager = userManagerService;
            copyFrom(mimeGroupsAwareIntentResolver);
            copyInto(arrayMap, mimeGroupsAwareIntentResolver.mMimeGroupToFilter);
            this.mIsUpdatingMimeGroup = mimeGroupsAwareIntentResolver.mIsUpdatingMimeGroup;
        }

        public void addFilter(PackageDataSnapshot packageDataSnapshot, F f) {
            IntentFilter intentFilter = getIntentFilter(f);
            applyMimeGroups((Computer) packageDataSnapshot, f);
            super.addFilter(packageDataSnapshot, f);
            if (this.mIsUpdatingMimeGroup) {
                return;
            }
            register_intent_filter(f, intentFilter.mimeGroupsIterator(), this.mMimeGroupToFilter, "      MimeGroup: ");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void removeFilterInternal(F f) {
            IntentFilter intentFilter = getIntentFilter(f);
            if (!this.mIsUpdatingMimeGroup) {
                unregister_intent_filter(f, intentFilter.mimeGroupsIterator(), this.mMimeGroupToFilter, "      MimeGroup: ");
            }
            super.removeFilterInternal(f);
            intentFilter.clearDynamicDataTypes();
        }

        public boolean updateMimeGroup(Computer computer, String str, String str2) {
            F[] fArr = this.mMimeGroupToFilter.get(str2);
            int length = fArr != null ? fArr.length : 0;
            this.mIsUpdatingMimeGroup = true;
            boolean z = false;
            for (int i = 0; i < length; i++) {
                F f = fArr[i];
                if (f == null) {
                    break;
                }
                if (isPackageForFilter(str, f)) {
                    z |= updateFilter(computer, f);
                }
            }
            this.mIsUpdatingMimeGroup = false;
            return z;
        }

        private boolean updateFilter(Computer computer, F f) {
            List dataTypes = getIntentFilter(f).dataTypes();
            removeFilter(f);
            addFilter((PackageDataSnapshot) computer, (Computer) f);
            return !equalLists(dataTypes, r0.dataTypes());
        }

        private boolean equalLists(List<String> list, List<String> list2) {
            if (list == null) {
                return list2 == null;
            }
            if (list2 == null || list.size() != list2.size()) {
                return false;
            }
            Collections.sort(list);
            Collections.sort(list2);
            return list.equals(list2);
        }

        private void applyMimeGroups(Computer computer, F f) {
            Set<String> set;
            IntentFilter intentFilter = getIntentFilter(f);
            for (int countMimeGroups = intentFilter.countMimeGroups() - 1; countMimeGroups >= 0; countMimeGroups--) {
                PackageStateInternal packageStateInternal = computer.getPackageStateInternal(((ParsedComponent) ((Pair) f).first).getPackageName());
                if (packageStateInternal == null) {
                    set = Collections.emptyList();
                } else {
                    set = packageStateInternal.getMimeGroups().get(intentFilter.getMimeGroup(countMimeGroups));
                }
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    try {
                        intentFilter.addDynamicDataType((String) it.next());
                    } catch (IntentFilter.MalformedMimeTypeException unused) {
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public boolean isFilterStopped(Computer computer, F f, int i) {
            if (!this.mUserManager.exists(i)) {
                return true;
            }
            PackageStateInternal packageStateInternal = computer.getPackageStateInternal(((ParsedComponent) ((Pair) f).first).getPackageName());
            if (packageStateInternal == null || packageStateInternal.getPkg() == null) {
                return false;
            }
            return ComponentResolver.mStaticComponentResolverExt.onIsFilterStopped(packageStateInternal, packageStateInternal.getUserStateOrDefault(i).isStopped());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ActivityIntentResolver extends MimeGroupsAwareIntentResolver<Pair<ParsedActivity, ParsedIntentInfo>, ResolveInfo> {
        protected final ArrayMap<ComponentName, ParsedActivity> mActivities;
        private UserNeedsBadgingCache mUserNeedsBadging;

        /* JADX INFO: Access modifiers changed from: protected */
        public Object filterToLabel(Pair<ParsedActivity, ParsedIntentInfo> pair) {
            return pair;
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ void addFilter(PackageDataSnapshot packageDataSnapshot, Pair<ParsedActivity, ParsedIntentInfo> pair) {
            super.addFilter(packageDataSnapshot, (PackageDataSnapshot) pair);
        }

        protected /* bridge */ /* synthetic */ boolean allowFilterResult(Object obj, List list) {
            return allowFilterResult((Pair<ParsedActivity, ParsedIntentInfo>) obj, (List<ResolveInfo>) list);
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ boolean updateMimeGroup(Computer computer, String str, String str2) {
            return super.updateMimeGroup(computer, str, str2);
        }

        ActivityIntentResolver(UserManagerService userManagerService, UserNeedsBadgingCache userNeedsBadgingCache) {
            super(userManagerService);
            this.mActivities = new ArrayMap<>();
            this.mUserNeedsBadging = userNeedsBadgingCache;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ActivityIntentResolver(ActivityIntentResolver activityIntentResolver, UserManagerService userManagerService, UserNeedsBadgingCache userNeedsBadgingCache) {
            super(activityIntentResolver, userManagerService);
            ArrayMap<ComponentName, ParsedActivity> arrayMap = new ArrayMap<>();
            this.mActivities = arrayMap;
            arrayMap.putAll((ArrayMap<? extends ComponentName, ? extends ParsedActivity>) activityIntentResolver.mActivities);
            this.mUserNeedsBadging = userNeedsBadgingCache;
        }

        public List<ResolveInfo> queryIntent(PackageDataSnapshot packageDataSnapshot, Intent intent, String str, boolean z, int i) {
            if (this.mUserManager.exists(i)) {
                return super.queryIntent(packageDataSnapshot, intent, str, z, i, z ? 65536 : 0);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public List<ResolveInfo> queryIntent(Computer computer, Intent intent, String str, long j, int i) {
            if (this.mUserManager.exists(i)) {
                return super.queryIntent(computer, intent, str, (65536 & j) != 0, i, j);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public List<ResolveInfo> queryIntentForPackage(Computer computer, Intent intent, String str, long j, List<ParsedActivity> list, int i) {
            if (!this.mUserManager.exists(i)) {
                return null;
            }
            if (list == null) {
                return Collections.emptyList();
            }
            boolean z = (j & 65536) != 0;
            int size = list.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i2 = 0; i2 < size; i2++) {
                ParsedActivity parsedActivity = list.get(i2);
                List<ParsedIntentInfo> intents = parsedActivity.getIntents();
                if (!intents.isEmpty()) {
                    Pair<ParsedActivity, ParsedIntentInfo>[] m2398newArray = m2398newArray(intents.size());
                    for (int i3 = 0; i3 < intents.size(); i3++) {
                        m2398newArray[i3] = Pair.create(parsedActivity, intents.get(i3));
                    }
                    arrayList.add(m2398newArray);
                }
            }
            return super.queryIntentFromList(computer, intent, str, z, arrayList, i, j);
        }

        protected void addActivity(Computer computer, ParsedActivity parsedActivity, String str, List<Pair<ParsedActivity, ParsedIntentInfo>> list) {
            this.mActivities.put(parsedActivity.getComponentName(), parsedActivity);
            int size = parsedActivity.getIntents().size();
            for (int i = 0; i < size; i++) {
                ParsedIntentInfo parsedIntentInfo = parsedActivity.getIntents().get(i);
                IntentFilter intentFilter = parsedIntentInfo.getIntentFilter();
                if (list != null && "activity".equals(str)) {
                    list.add(Pair.create(parsedActivity, parsedIntentInfo));
                }
                if (!intentFilter.debugCheck()) {
                    Log.w(ComponentResolver.TAG, "==> For Activity " + parsedActivity.getName());
                }
                addFilter((PackageDataSnapshot) computer, Pair.create(parsedActivity, parsedIntentInfo));
            }
        }

        protected void removeActivity(ParsedActivity parsedActivity, String str) {
            this.mActivities.remove(parsedActivity.getComponentName());
            int size = parsedActivity.getIntents().size();
            for (int i = 0; i < size; i++) {
                ParsedIntentInfo parsedIntentInfo = parsedActivity.getIntents().get(i);
                parsedIntentInfo.getIntentFilter();
                removeFilter(Pair.create(parsedActivity, parsedIntentInfo));
            }
        }

        protected boolean allowFilterResult(Pair<ParsedActivity, ParsedIntentInfo> pair, List<ResolveInfo> list) {
            for (int size = list.size() - 1; size >= 0; size--) {
                ActivityInfo activityInfo = list.get(size).activityInfo;
                if (Objects.equals(activityInfo.name, ((ParsedActivity) pair.first).getName()) && Objects.equals(activityInfo.packageName, ((ParsedActivity) pair.first).getPackageName())) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: newArray, reason: merged with bridge method [inline-methods] */
        public Pair<ParsedActivity, ParsedIntentInfo>[] m2398newArray(int i) {
            return new Pair[i];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public boolean isPackageForFilter(String str, Pair<ParsedActivity, ParsedIntentInfo> pair) {
            return str.equals(((ParsedActivity) pair.first).getPackageName());
        }

        private void log(String str, ParsedIntentInfo parsedIntentInfo, int i, int i2) {
            Slog.w(ComponentResolver.TAG, str + "; match: " + DebugUtils.flagsToString(IntentFilter.class, "MATCH_", i) + "; userId: " + i2 + "; intent info: " + parsedIntentInfo);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public ResolveInfo newResult(Computer computer, Pair<ParsedActivity, ParsedIntentInfo> pair, int i, int i2, long j) {
            PackageStateInternal packageStateInternal;
            ParsedActivity parsedActivity = (ParsedActivity) pair.first;
            ParsedIntentInfo parsedIntentInfo = (ParsedIntentInfo) pair.second;
            IntentFilter intentFilter = parsedIntentInfo.getIntentFilter();
            if (!this.mUserManager.exists(i2) || (packageStateInternal = computer.getPackageStateInternal(parsedActivity.getPackageName())) == null || packageStateInternal.getPkg() == null || !PackageStateUtils.isEnabledAndMatches(packageStateInternal, parsedActivity, j, i2)) {
                return null;
            }
            PackageUserStateInternal userStateOrDefault = packageStateInternal.getUserStateOrDefault(i2);
            ActivityInfo generateActivityInfo = PackageInfoUtils.generateActivityInfo(packageStateInternal.getPkg(), parsedActivity, j, userStateOrDefault, i2, packageStateInternal);
            if (generateActivityInfo == null) {
                return null;
            }
            boolean z = (33554432 & j) != 0;
            boolean z2 = (j & 16777216) != 0;
            boolean z3 = z2 && intentFilter.isVisibleToInstantApp() && (!z || intentFilter.isExplicitlyVisibleToInstantApp());
            boolean z4 = (j & 8388608) != 0;
            if (z2 && !z3 && !userStateOrDefault.isInstantApp()) {
                return null;
            }
            if (!z4 && userStateOrDefault.isInstantApp()) {
                return null;
            }
            if (userStateOrDefault.isInstantApp() && packageStateInternal.isUpdateAvailable()) {
                return null;
            }
            ResolveInfo resolveInfo = new ResolveInfo(intentFilter.hasCategory("android.intent.category.BROWSABLE"));
            resolveInfo.activityInfo = generateActivityInfo;
            if ((j & 64) != 0) {
                resolveInfo.filter = intentFilter;
            }
            resolveInfo.handleAllWebDataURI = intentFilter.handleAllWebDataURI();
            resolveInfo.priority = intentFilter.getPriority();
            resolveInfo.match = i;
            resolveInfo.isDefault = parsedIntentInfo.isHasDefault();
            resolveInfo.labelRes = parsedIntentInfo.getLabelRes();
            resolveInfo.nonLocalizedLabel = parsedIntentInfo.getNonLocalizedLabel();
            if (this.mUserNeedsBadging.get(i2)) {
                resolveInfo.noResourceId = true;
            } else {
                resolveInfo.icon = parsedIntentInfo.getIcon();
            }
            resolveInfo.iconResourceId = parsedIntentInfo.getIcon();
            resolveInfo.system = resolveInfo.activityInfo.applicationInfo.isSystemApp();
            resolveInfo.isInstantAppAvailable = userStateOrDefault.isInstantApp();
            resolveInfo.userHandle = UserHandle.of(i2);
            return resolveInfo;
        }

        protected void sortResults(List<ResolveInfo> list) {
            list.sort(ComponentResolver.RESOLVE_PRIORITY_SORTER);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void dumpFilter(PrintWriter printWriter, String str, Pair<ParsedActivity, ParsedIntentInfo> pair) {
            ParsedActivity parsedActivity = (ParsedActivity) pair.first;
            ParsedIntentInfo parsedIntentInfo = (ParsedIntentInfo) pair.second;
            printWriter.print(str);
            printWriter.print(Integer.toHexString(System.identityHashCode(parsedActivity)));
            printWriter.print(' ');
            ComponentName.printShortString(printWriter, parsedActivity.getPackageName(), parsedActivity.getClassName());
            printWriter.print(" filter ");
            printWriter.println(Integer.toHexString(System.identityHashCode(parsedIntentInfo)));
        }

        protected void dumpFilterLabel(PrintWriter printWriter, String str, Object obj, int i) {
            Pair pair = (Pair) obj;
            printWriter.print(str);
            printWriter.print(Integer.toHexString(System.identityHashCode(pair.first)));
            printWriter.print(' ');
            ComponentName.printShortString(printWriter, ((ParsedActivity) pair.first).getPackageName(), ((ParsedActivity) pair.first).getClassName());
            if (i > 1) {
                printWriter.print(" (");
                printWriter.print(i);
                printWriter.print(" filters)");
            }
            printWriter.println();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public IntentFilter getIntentFilter(Pair<ParsedActivity, ParsedIntentInfo> pair) {
            return ((ParsedIntentInfo) pair.second).getIntentFilter();
        }

        protected List<ParsedActivity> getResolveList(AndroidPackage androidPackage) {
            return androidPackage.getActivities();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ReceiverIntentResolver extends ActivityIntentResolver {
        ReceiverIntentResolver(UserManagerService userManagerService, UserNeedsBadgingCache userNeedsBadgingCache) {
            super(userManagerService, userNeedsBadgingCache);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ReceiverIntentResolver(ReceiverIntentResolver receiverIntentResolver, UserManagerService userManagerService, UserNeedsBadgingCache userNeedsBadgingCache) {
            super(receiverIntentResolver, userManagerService, userNeedsBadgingCache);
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.ActivityIntentResolver
        protected List<ParsedActivity> getResolveList(AndroidPackage androidPackage) {
            return androidPackage.getReceivers();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ProviderIntentResolver extends MimeGroupsAwareIntentResolver<Pair<ParsedProvider, ParsedIntentInfo>, ResolveInfo> {
        final ArrayMap<ComponentName, ParsedProvider> mProviders;

        /* JADX INFO: Access modifiers changed from: protected */
        public Object filterToLabel(Pair<ParsedProvider, ParsedIntentInfo> pair) {
            return pair;
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ void addFilter(PackageDataSnapshot packageDataSnapshot, Pair<ParsedProvider, ParsedIntentInfo> pair) {
            super.addFilter(packageDataSnapshot, (PackageDataSnapshot) pair);
        }

        protected /* bridge */ /* synthetic */ boolean allowFilterResult(Object obj, List list) {
            return allowFilterResult((Pair<ParsedProvider, ParsedIntentInfo>) obj, (List<ResolveInfo>) list);
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ boolean updateMimeGroup(Computer computer, String str, String str2) {
            return super.updateMimeGroup(computer, str, str2);
        }

        ProviderIntentResolver(UserManagerService userManagerService) {
            super(userManagerService);
            this.mProviders = new ArrayMap<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ProviderIntentResolver(ProviderIntentResolver providerIntentResolver, UserManagerService userManagerService) {
            super(providerIntentResolver, userManagerService);
            ArrayMap<ComponentName, ParsedProvider> arrayMap = new ArrayMap<>();
            this.mProviders = arrayMap;
            arrayMap.putAll((ArrayMap<? extends ComponentName, ? extends ParsedProvider>) providerIntentResolver.mProviders);
        }

        public List<ResolveInfo> queryIntent(PackageDataSnapshot packageDataSnapshot, Intent intent, String str, boolean z, int i) {
            if (this.mUserManager.exists(i)) {
                return super.queryIntent(packageDataSnapshot, intent, str, z, i, z ? 65536L : 0L);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public List<ResolveInfo> queryIntent(Computer computer, Intent intent, String str, long j, int i) {
            if (this.mUserManager.exists(i)) {
                return super.queryIntent(computer, intent, str, (65536 & j) != 0, i, j);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public List<ResolveInfo> queryIntentForPackage(Computer computer, Intent intent, String str, long j, List<ParsedProvider> list, int i) {
            if (!this.mUserManager.exists(i)) {
                return null;
            }
            if (list == null) {
                return Collections.emptyList();
            }
            boolean z = (j & 65536) != 0;
            int size = list.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i2 = 0; i2 < size; i2++) {
                ParsedProvider parsedProvider = list.get(i2);
                List<ParsedIntentInfo> intents = parsedProvider.getIntents();
                if (!intents.isEmpty()) {
                    Pair<ParsedProvider, ParsedIntentInfo>[] m2400newArray = m2400newArray(intents.size());
                    for (int i3 = 0; i3 < intents.size(); i3++) {
                        m2400newArray[i3] = Pair.create(parsedProvider, intents.get(i3));
                    }
                    arrayList.add(m2400newArray);
                }
            }
            return super.queryIntentFromList(computer, intent, str, z, arrayList, i, j);
        }

        void addProvider(Computer computer, ParsedProvider parsedProvider) {
            if (this.mProviders.containsKey(parsedProvider.getComponentName())) {
                Slog.w(ComponentResolver.TAG, "Provider " + parsedProvider.getComponentName() + " already defined; ignoring");
                return;
            }
            this.mProviders.put(parsedProvider.getComponentName(), parsedProvider);
            int size = parsedProvider.getIntents().size();
            for (int i = 0; i < size; i++) {
                ParsedIntentInfo parsedIntentInfo = parsedProvider.getIntents().get(i);
                if (!parsedIntentInfo.getIntentFilter().debugCheck()) {
                    Log.w(ComponentResolver.TAG, "==> For Provider " + parsedProvider.getName());
                }
                addFilter((PackageDataSnapshot) computer, Pair.create(parsedProvider, parsedIntentInfo));
            }
        }

        void removeProvider(ParsedProvider parsedProvider) {
            this.mProviders.remove(parsedProvider.getComponentName());
            int size = parsedProvider.getIntents().size();
            for (int i = 0; i < size; i++) {
                ParsedIntentInfo parsedIntentInfo = parsedProvider.getIntents().get(i);
                parsedIntentInfo.getIntentFilter();
                removeFilter(Pair.create(parsedProvider, parsedIntentInfo));
            }
        }

        protected boolean allowFilterResult(Pair<ParsedProvider, ParsedIntentInfo> pair, List<ResolveInfo> list) {
            for (int size = list.size() - 1; size >= 0; size--) {
                ProviderInfo providerInfo = list.get(size).providerInfo;
                if (Objects.equals(providerInfo.name, ((ParsedProvider) pair.first).getClassName()) && Objects.equals(providerInfo.packageName, ((ParsedProvider) pair.first).getPackageName())) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: newArray, reason: merged with bridge method [inline-methods] */
        public Pair<ParsedProvider, ParsedIntentInfo>[] m2400newArray(int i) {
            return new Pair[i];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public boolean isPackageForFilter(String str, Pair<ParsedProvider, ParsedIntentInfo> pair) {
            return str.equals(((ParsedProvider) pair.first).getPackageName());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public ResolveInfo newResult(Computer computer, Pair<ParsedProvider, ParsedIntentInfo> pair, int i, int i2, long j) {
            ApplicationInfo generateApplicationInfo;
            ProviderInfo generateProviderInfo;
            if (!this.mUserManager.exists(i2)) {
                return null;
            }
            ParsedProvider parsedProvider = (ParsedProvider) pair.first;
            ParsedIntentInfo parsedIntentInfo = (ParsedIntentInfo) pair.second;
            IntentFilter intentFilter = parsedIntentInfo.getIntentFilter();
            PackageStateInternal packageStateInternal = computer.getPackageStateInternal(parsedProvider.getPackageName());
            if (packageStateInternal == null || packageStateInternal.getPkg() == null || !PackageStateUtils.isEnabledAndMatches(packageStateInternal, parsedProvider, j, i2)) {
                return null;
            }
            PackageUserStateInternal userStateOrDefault = packageStateInternal.getUserStateOrDefault(i2);
            boolean z = (16777216 & j) != 0;
            boolean z2 = (8388608 & j) != 0;
            if (z && !intentFilter.isVisibleToInstantApp() && !userStateOrDefault.isInstantApp()) {
                return null;
            }
            if (!z2 && userStateOrDefault.isInstantApp()) {
                return null;
            }
            if ((userStateOrDefault.isInstantApp() && packageStateInternal.isUpdateAvailable()) || (generateApplicationInfo = PackageInfoUtils.generateApplicationInfo(packageStateInternal.getPkg(), j, userStateOrDefault, i2, packageStateInternal)) == null || (generateProviderInfo = PackageInfoUtils.generateProviderInfo(packageStateInternal.getPkg(), parsedProvider, j, userStateOrDefault, generateApplicationInfo, i2, packageStateInternal)) == null) {
                return null;
            }
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.providerInfo = generateProviderInfo;
            if ((64 & j) != 0) {
                resolveInfo.filter = intentFilter;
            }
            resolveInfo.priority = intentFilter.getPriority();
            resolveInfo.match = i;
            resolveInfo.isDefault = parsedIntentInfo.isHasDefault();
            resolveInfo.labelRes = parsedIntentInfo.getLabelRes();
            resolveInfo.nonLocalizedLabel = parsedIntentInfo.getNonLocalizedLabel();
            resolveInfo.icon = parsedIntentInfo.getIcon();
            resolveInfo.system = resolveInfo.providerInfo.applicationInfo.isSystemApp();
            return resolveInfo;
        }

        protected void sortResults(List<ResolveInfo> list) {
            list.sort(ComponentResolver.RESOLVE_PRIORITY_SORTER);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void dumpFilter(PrintWriter printWriter, String str, Pair<ParsedProvider, ParsedIntentInfo> pair) {
            ParsedProvider parsedProvider = (ParsedProvider) pair.first;
            ParsedIntentInfo parsedIntentInfo = (ParsedIntentInfo) pair.second;
            printWriter.print(str);
            printWriter.print(Integer.toHexString(System.identityHashCode(parsedProvider)));
            printWriter.print(' ');
            ComponentName.printShortString(printWriter, parsedProvider.getPackageName(), parsedProvider.getClassName());
            printWriter.print(" filter ");
            printWriter.println(Integer.toHexString(System.identityHashCode(parsedIntentInfo)));
        }

        protected void dumpFilterLabel(PrintWriter printWriter, String str, Object obj, int i) {
            Pair pair = (Pair) obj;
            printWriter.print(str);
            printWriter.print(Integer.toHexString(System.identityHashCode(pair.first)));
            printWriter.print(' ');
            ComponentName.printShortString(printWriter, ((ParsedProvider) pair.first).getPackageName(), ((ParsedProvider) pair.first).getClassName());
            if (i > 1) {
                printWriter.print(" (");
                printWriter.print(i);
                printWriter.print(" filters)");
            }
            printWriter.println();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public IntentFilter getIntentFilter(Pair<ParsedProvider, ParsedIntentInfo> pair) {
            return ((ParsedIntentInfo) pair.second).getIntentFilter();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ServiceIntentResolver extends MimeGroupsAwareIntentResolver<Pair<ParsedService, ParsedIntentInfo>, ResolveInfo> {
        final ArrayMap<ComponentName, ParsedService> mServices;

        /* JADX INFO: Access modifiers changed from: protected */
        public Object filterToLabel(Pair<ParsedService, ParsedIntentInfo> pair) {
            return pair;
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ void addFilter(PackageDataSnapshot packageDataSnapshot, Pair<ParsedService, ParsedIntentInfo> pair) {
            super.addFilter(packageDataSnapshot, (PackageDataSnapshot) pair);
        }

        protected /* bridge */ /* synthetic */ boolean allowFilterResult(Object obj, List list) {
            return allowFilterResult((Pair<ParsedService, ParsedIntentInfo>) obj, (List<ResolveInfo>) list);
        }

        @Override // com.android.server.pm.resolution.ComponentResolver.MimeGroupsAwareIntentResolver
        public /* bridge */ /* synthetic */ boolean updateMimeGroup(Computer computer, String str, String str2) {
            return super.updateMimeGroup(computer, str, str2);
        }

        ServiceIntentResolver(UserManagerService userManagerService) {
            super(userManagerService);
            this.mServices = new ArrayMap<>();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ServiceIntentResolver(ServiceIntentResolver serviceIntentResolver, UserManagerService userManagerService) {
            super(serviceIntentResolver, userManagerService);
            ArrayMap<ComponentName, ParsedService> arrayMap = new ArrayMap<>();
            this.mServices = arrayMap;
            arrayMap.putAll((ArrayMap<? extends ComponentName, ? extends ParsedService>) serviceIntentResolver.mServices);
        }

        public List<ResolveInfo> queryIntent(PackageDataSnapshot packageDataSnapshot, Intent intent, String str, boolean z, int i) {
            if (this.mUserManager.exists(i)) {
                return super.queryIntent(packageDataSnapshot, intent, str, z, i, z ? 65536L : 0L);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public List<ResolveInfo> queryIntent(Computer computer, Intent intent, String str, long j, int i) {
            if (this.mUserManager.exists(i)) {
                return super.queryIntent(computer, intent, str, (65536 & j) != 0, i, j);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public List<ResolveInfo> queryIntentForPackage(Computer computer, Intent intent, String str, long j, List<ParsedService> list, int i) {
            if (!this.mUserManager.exists(i)) {
                return null;
            }
            if (list == null) {
                return Collections.emptyList();
            }
            boolean z = (j & 65536) != 0;
            int size = list.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i2 = 0; i2 < size; i2++) {
                ParsedService parsedService = list.get(i2);
                List<ParsedIntentInfo> intents = parsedService.getIntents();
                if (intents.size() > 0) {
                    Pair<ParsedService, ParsedIntentInfo>[] m2401newArray = m2401newArray(intents.size());
                    for (int i3 = 0; i3 < intents.size(); i3++) {
                        m2401newArray[i3] = Pair.create(parsedService, intents.get(i3));
                    }
                    arrayList.add(m2401newArray);
                }
            }
            return super.queryIntentFromList(computer, intent, str, z, arrayList, i, j);
        }

        void addService(Computer computer, ParsedService parsedService) {
            this.mServices.put(parsedService.getComponentName(), parsedService);
            int size = parsedService.getIntents().size();
            for (int i = 0; i < size; i++) {
                ParsedIntentInfo parsedIntentInfo = parsedService.getIntents().get(i);
                if (!parsedIntentInfo.getIntentFilter().debugCheck()) {
                    Log.w(ComponentResolver.TAG, "==> For Service " + parsedService.getName());
                }
                addFilter((PackageDataSnapshot) computer, Pair.create(parsedService, parsedIntentInfo));
            }
        }

        void removeService(ParsedService parsedService) {
            this.mServices.remove(parsedService.getComponentName());
            int size = parsedService.getIntents().size();
            for (int i = 0; i < size; i++) {
                ParsedIntentInfo parsedIntentInfo = parsedService.getIntents().get(i);
                parsedIntentInfo.getIntentFilter();
                removeFilter(Pair.create(parsedService, parsedIntentInfo));
            }
        }

        protected boolean allowFilterResult(Pair<ParsedService, ParsedIntentInfo> pair, List<ResolveInfo> list) {
            for (int size = list.size() - 1; size >= 0; size--) {
                ServiceInfo serviceInfo = list.get(size).serviceInfo;
                if (Objects.equals(serviceInfo.name, ((ParsedService) pair.first).getClassName()) && Objects.equals(serviceInfo.packageName, ((ParsedService) pair.first).getPackageName())) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: newArray, reason: merged with bridge method [inline-methods] */
        public Pair<ParsedService, ParsedIntentInfo>[] m2401newArray(int i) {
            return new Pair[i];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public boolean isPackageForFilter(String str, Pair<ParsedService, ParsedIntentInfo> pair) {
            return str.equals(((ParsedService) pair.first).getPackageName());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public ResolveInfo newResult(Computer computer, Pair<ParsedService, ParsedIntentInfo> pair, int i, int i2, long j) {
            if (!this.mUserManager.exists(i2)) {
                return null;
            }
            ParsedService parsedService = (ParsedService) pair.first;
            ParsedIntentInfo parsedIntentInfo = (ParsedIntentInfo) pair.second;
            IntentFilter intentFilter = parsedIntentInfo.getIntentFilter();
            PackageStateInternal packageStateInternal = computer.getPackageStateInternal(parsedService.getPackageName());
            if (packageStateInternal == null || packageStateInternal.getPkg() == null || !PackageStateUtils.isEnabledAndMatches(packageStateInternal, parsedService, j, i2)) {
                return null;
            }
            PackageUserStateInternal userStateOrDefault = packageStateInternal.getUserStateOrDefault(i2);
            ServiceInfo generateServiceInfo = PackageInfoUtils.generateServiceInfo(packageStateInternal.getPkg(), parsedService, j, userStateOrDefault, i2, packageStateInternal);
            if (generateServiceInfo == null) {
                return null;
            }
            boolean z = (16777216 & j) != 0;
            boolean z2 = (8388608 & j) != 0;
            if (z && !intentFilter.isVisibleToInstantApp() && !userStateOrDefault.isInstantApp()) {
                return null;
            }
            if (!z2 && userStateOrDefault.isInstantApp()) {
                return null;
            }
            if (userStateOrDefault.isInstantApp() && packageStateInternal.isUpdateAvailable()) {
                return null;
            }
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.serviceInfo = generateServiceInfo;
            if ((j & 64) != 0) {
                resolveInfo.filter = intentFilter;
            }
            resolveInfo.priority = intentFilter.getPriority();
            resolveInfo.match = i;
            resolveInfo.isDefault = parsedIntentInfo.isHasDefault();
            resolveInfo.labelRes = parsedIntentInfo.getLabelRes();
            resolveInfo.nonLocalizedLabel = parsedIntentInfo.getNonLocalizedLabel();
            resolveInfo.icon = parsedIntentInfo.getIcon();
            resolveInfo.system = resolveInfo.serviceInfo.applicationInfo.isSystemApp();
            return resolveInfo;
        }

        protected void sortResults(List<ResolveInfo> list) {
            list.sort(ComponentResolver.RESOLVE_PRIORITY_SORTER);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void dumpFilter(PrintWriter printWriter, String str, Pair<ParsedService, ParsedIntentInfo> pair) {
            ParsedService parsedService = (ParsedService) pair.first;
            ParsedIntentInfo parsedIntentInfo = (ParsedIntentInfo) pair.second;
            printWriter.print(str);
            printWriter.print(Integer.toHexString(System.identityHashCode(parsedService)));
            printWriter.print(' ');
            ComponentName.printShortString(printWriter, parsedService.getPackageName(), parsedService.getClassName());
            printWriter.print(" filter ");
            printWriter.print(Integer.toHexString(System.identityHashCode(parsedIntentInfo)));
            if (parsedService.getPermission() != null) {
                printWriter.print(" permission ");
                printWriter.println(parsedService.getPermission());
            } else {
                printWriter.println();
            }
        }

        protected void dumpFilterLabel(PrintWriter printWriter, String str, Object obj, int i) {
            Pair pair = (Pair) obj;
            printWriter.print(str);
            printWriter.print(Integer.toHexString(System.identityHashCode(pair.first)));
            printWriter.print(' ');
            ComponentName.printShortString(printWriter, ((ParsedService) pair.first).getPackageName(), ((ParsedService) pair.first).getClassName());
            if (i > 1) {
                printWriter.print(" (");
                printWriter.print(i);
                printWriter.print(" filters)");
            }
            printWriter.println();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public IntentFilter getIntentFilter(Pair<ParsedService, ParsedIntentInfo> pair) {
            return ((ParsedIntentInfo) pair.second).getIntentFilter();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class InstantAppIntentResolver extends IntentResolver<AuxiliaryResolveInfo.AuxiliaryFilter, AuxiliaryResolveInfo.AuxiliaryFilter> {
        final ArrayMap<String, Pair<Integer, InstantAppResolveInfo>> mOrderResult = new ArrayMap<>();
        private final UserManagerService mUserManager;

        /* JADX INFO: Access modifiers changed from: protected */
        public IntentFilter getIntentFilter(AuxiliaryResolveInfo.AuxiliaryFilter auxiliaryFilter) {
            return auxiliaryFilter;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public boolean isPackageForFilter(String str, AuxiliaryResolveInfo.AuxiliaryFilter auxiliaryFilter) {
            return true;
        }

        public InstantAppIntentResolver(UserManagerService userManagerService) {
            this.mUserManager = userManagerService;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: newArray, reason: merged with bridge method [inline-methods] */
        public AuxiliaryResolveInfo.AuxiliaryFilter[] m2399newArray(int i) {
            return new AuxiliaryResolveInfo.AuxiliaryFilter[i];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public AuxiliaryResolveInfo.AuxiliaryFilter newResult(Computer computer, AuxiliaryResolveInfo.AuxiliaryFilter auxiliaryFilter, int i, int i2, long j) {
            if (!this.mUserManager.exists(i2)) {
                return null;
            }
            String packageName = auxiliaryFilter.resolveInfo.getPackageName();
            Integer valueOf = Integer.valueOf(auxiliaryFilter.getOrder());
            Pair<Integer, InstantAppResolveInfo> pair = this.mOrderResult.get(packageName);
            if (pair != null && ((Integer) pair.first).intValue() >= valueOf.intValue()) {
                return null;
            }
            InstantAppResolveInfo instantAppResolveInfo = auxiliaryFilter.resolveInfo;
            if (valueOf.intValue() > 0) {
                this.mOrderResult.put(packageName, new Pair<>(valueOf, instantAppResolveInfo));
            }
            return auxiliaryFilter;
        }

        protected void filterResults(List<AuxiliaryResolveInfo.AuxiliaryFilter> list) {
            if (this.mOrderResult.size() == 0) {
                return;
            }
            int size = list.size();
            int i = 0;
            while (i < size) {
                InstantAppResolveInfo instantAppResolveInfo = list.get(i).resolveInfo;
                String packageName = instantAppResolveInfo.getPackageName();
                Pair<Integer, InstantAppResolveInfo> pair = this.mOrderResult.get(packageName);
                if (pair != null) {
                    if (pair.second == instantAppResolveInfo) {
                        this.mOrderResult.remove(packageName);
                        if (this.mOrderResult.size() == 0) {
                            return;
                        }
                    } else {
                        list.remove(i);
                        size--;
                        i--;
                    }
                }
                i++;
            }
        }
    }

    public boolean updateMimeGroup(Computer computer, String str, String str2) {
        boolean updateMimeGroup;
        synchronized (this.mLock) {
            updateMimeGroup = this.mServices.updateMimeGroup(computer, str, str2) | this.mActivities.updateMimeGroup(computer, str, str2) | false | this.mProviders.updateMimeGroup(computer, str, str2) | this.mReceivers.updateMimeGroup(computer, str, str2);
            if (updateMimeGroup) {
                onChanged();
            }
        }
        return updateMimeGroup;
    }
}
