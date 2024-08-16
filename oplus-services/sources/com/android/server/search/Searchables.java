package com.android.server.search;

import android.app.AppGlobals;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.server.LocalServices;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Searchables {
    public static String ENHANCED_GOOGLE_SEARCH_COMPONENT_NAME = "com.google.android.providers.enhancedgooglesearch/.Launcher";
    private static final Comparator<ResolveInfo> GLOBAL_SEARCH_RANKER = new Comparator<ResolveInfo>() { // from class: com.android.server.search.Searchables.1
        @Override // java.util.Comparator
        public int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
            if (resolveInfo == resolveInfo2) {
                return 0;
            }
            boolean isSystemApp = Searchables.isSystemApp(resolveInfo);
            boolean isSystemApp2 = Searchables.isSystemApp(resolveInfo2);
            if (isSystemApp && !isSystemApp2) {
                return -1;
            }
            if (!isSystemApp2 || isSystemApp) {
                return resolveInfo2.priority - resolveInfo.priority;
            }
            return 1;
        }
    };
    public static String GOOGLE_SEARCH_COMPONENT_NAME = "com.android.googlesearch/.GoogleSearch";
    private static final String LOG_TAG = "Searchables";
    private static final String MD_LABEL_DEFAULT_SEARCHABLE = "android.app.default_searchable";
    private static final String MD_SEARCHABLE_SYSTEM_SEARCH = "*";
    private Context mContext;
    private List<ResolveInfo> mGlobalSearchActivities;
    private int mUserId;
    private HashMap<ComponentName, SearchableInfo> mSearchablesMap = null;
    private ArrayList<SearchableInfo> mSearchablesList = null;
    private ArrayList<SearchableInfo> mSearchablesInGlobalSearchList = null;
    private ComponentName mCurrentGlobalSearchActivity = null;
    private ComponentName mWebSearchActivity = null;
    private final ISearchablesWrapper mSearchablesWrapper = new SearchablesWrapper();
    private final IPackageManager mPm = AppGlobals.getPackageManager();

    public Searchables(Context context, int i) {
        this.mContext = context;
        this.mUserId = i;
    }

    public SearchableInfo getSearchableInfo(ComponentName componentName) {
        ComponentName componentName2;
        SearchableInfo searchableInfo;
        Bundle bundle;
        synchronized (this) {
            SearchableInfo searchableInfo2 = this.mSearchablesMap.get(componentName);
            if (searchableInfo2 != null) {
                if (((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).canAccessComponent(Binder.getCallingUid(), searchableInfo2.getSearchActivity(), UserHandle.getCallingUserId())) {
                    return searchableInfo2;
                }
                return null;
            }
            try {
                ActivityInfo activityInfo = this.mPm.getActivityInfo(componentName, 128L, this.mUserId);
                Bundle bundle2 = activityInfo.metaData;
                String string = bundle2 != null ? bundle2.getString(MD_LABEL_DEFAULT_SEARCHABLE) : null;
                if (string == null && (bundle = activityInfo.applicationInfo.metaData) != null) {
                    string = bundle.getString(MD_LABEL_DEFAULT_SEARCHABLE);
                }
                if (string == null || string.equals(MD_SEARCHABLE_SYSTEM_SEARCH)) {
                    return null;
                }
                String packageName = componentName.getPackageName();
                if (string.charAt(0) == '.') {
                    componentName2 = new ComponentName(packageName, packageName + string);
                } else {
                    componentName2 = new ComponentName(packageName, string);
                }
                synchronized (this) {
                    searchableInfo = this.mSearchablesMap.get(componentName2);
                    if (searchableInfo != null) {
                        this.mSearchablesMap.put(componentName, searchableInfo);
                    }
                }
                if (searchableInfo == null || !((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).canAccessComponent(Binder.getCallingUid(), searchableInfo.getSearchActivity(), UserHandle.getCallingUserId())) {
                    return null;
                }
                return searchableInfo;
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error getting activity info " + e);
                return null;
            }
        }
    }

    public void updateSearchableList() {
        ResolveInfo resolveInfo;
        SearchableInfo activityMetaData;
        HashMap<ComponentName, SearchableInfo> hashMap = new HashMap<>();
        ArrayList<SearchableInfo> arrayList = new ArrayList<>();
        ArrayList<SearchableInfo> arrayList2 = new ArrayList<>();
        Intent intent = new Intent("android.intent.action.SEARCH");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            List<ResolveInfo> queryIntentActivities = queryIntentActivities(intent, 268435584);
            List<ResolveInfo> queryIntentActivities2 = queryIntentActivities(new Intent("android.intent.action.WEB_SEARCH"), 268435584);
            if (queryIntentActivities != null || queryIntentActivities2 != null) {
                int size = queryIntentActivities == null ? 0 : queryIntentActivities.size();
                int size2 = (queryIntentActivities2 == null ? 0 : queryIntentActivities2.size()) + size;
                for (int i = 0; i < size2; i++) {
                    if (i < size) {
                        resolveInfo = queryIntentActivities.get(i);
                    } else {
                        resolveInfo = queryIntentActivities2.get(i - size);
                    }
                    ActivityInfo activityInfo = resolveInfo.activityInfo;
                    if (hashMap.get(new ComponentName(activityInfo.packageName, activityInfo.name)) == null && (activityMetaData = SearchableInfo.getActivityMetaData(this.mContext, activityInfo, this.mUserId)) != null) {
                        arrayList.add(activityMetaData);
                        hashMap.put(activityMetaData.getSearchActivity(), activityMetaData);
                        if (activityMetaData.shouldIncludeInGlobalSearch()) {
                            arrayList2.add(activityMetaData);
                        }
                    }
                }
            }
            List<ResolveInfo> findGlobalSearchActivities = findGlobalSearchActivities();
            ComponentName findGlobalSearchActivity = findGlobalSearchActivity(findGlobalSearchActivities);
            ComponentName findWebSearchActivity = findWebSearchActivity(findGlobalSearchActivity);
            synchronized (this) {
                this.mSearchablesMap = hashMap;
                this.mSearchablesList = arrayList;
                this.mSearchablesInGlobalSearchList = arrayList2;
                this.mGlobalSearchActivities = findGlobalSearchActivities;
                this.mCurrentGlobalSearchActivity = findGlobalSearchActivity;
                this.mWebSearchActivity = findWebSearchActivity;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<ResolveInfo> findGlobalSearchActivities() {
        List<ResolveInfo> queryIntentActivities = queryIntentActivities(new Intent("android.search.action.GLOBAL_SEARCH"), 268500992);
        if (queryIntentActivities != null && !queryIntentActivities.isEmpty()) {
            Collections.sort(queryIntentActivities, GLOBAL_SEARCH_RANKER);
        }
        return queryIntentActivities;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ComponentName findGlobalSearchActivity(List<ResolveInfo> list) {
        ComponentName unflattenFromString;
        String globalSearchProviderSetting = getGlobalSearchProviderSetting();
        return (TextUtils.isEmpty(globalSearchProviderSetting) || (unflattenFromString = ComponentName.unflattenFromString(globalSearchProviderSetting)) == null || !isInstalled(unflattenFromString)) ? getDefaultGlobalSearchProvider(list) : unflattenFromString;
    }

    private boolean isInstalled(ComponentName componentName) {
        Intent intent = new Intent("android.search.action.GLOBAL_SEARCH");
        intent.setComponent(componentName);
        List<ResolveInfo> queryIntentActivities = queryIntentActivities(intent, 65536);
        return (queryIntentActivities == null || queryIntentActivities.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean isSystemApp(ResolveInfo resolveInfo) {
        return (resolveInfo.activityInfo.applicationInfo.flags & 1) != 0;
    }

    private ComponentName getDefaultGlobalSearchProvider(List<ResolveInfo> list) {
        if (list != null && !list.isEmpty()) {
            ActivityInfo activityInfo = list.get(0).activityInfo;
            return new ComponentName(activityInfo.packageName, activityInfo.name);
        }
        Log.w(LOG_TAG, "No global search activity found");
        return null;
    }

    private String getGlobalSearchProviderSetting() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        return Settings.Secure.getStringForUser(contentResolver, "search_global_search_activity", contentResolver.getUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ComponentName findWebSearchActivity(ComponentName componentName) {
        if (componentName == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.WEB_SEARCH");
        intent.setPackage(componentName.getPackageName());
        List<ResolveInfo> queryIntentActivities = queryIntentActivities(intent, 65536);
        if (queryIntentActivities != null && !queryIntentActivities.isEmpty()) {
            ActivityInfo activityInfo = queryIntentActivities.get(0).activityInfo;
            return new ComponentName(activityInfo.packageName, activityInfo.name);
        }
        Log.w(LOG_TAG, "No web search activity found");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<ResolveInfo> queryIntentActivities(Intent intent, int i) {
        try {
            return this.mPm.queryIntentActivities(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), i | 8388608, this.mUserId).getList();
        } catch (RemoteException unused) {
            return null;
        }
    }

    public synchronized ArrayList<SearchableInfo> getSearchablesList() {
        return createFilterdSearchableInfoList(this.mSearchablesList);
    }

    public synchronized ArrayList<SearchableInfo> getSearchablesInGlobalSearchList() {
        return createFilterdSearchableInfoList(this.mSearchablesInGlobalSearchList);
    }

    public synchronized ArrayList<ResolveInfo> getGlobalSearchActivities() {
        return createFilterdResolveInfoList(this.mGlobalSearchActivities);
    }

    private ArrayList<SearchableInfo> createFilterdSearchableInfoList(List<SearchableInfo> list) {
        if (list == null) {
            return null;
        }
        ArrayList<SearchableInfo> arrayList = new ArrayList<>(list.size());
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        for (SearchableInfo searchableInfo : list) {
            if (packageManagerInternal.canAccessComponent(callingUid, searchableInfo.getSearchActivity(), callingUserId)) {
                arrayList.add(searchableInfo);
            }
        }
        return arrayList;
    }

    private ArrayList<ResolveInfo> createFilterdResolveInfoList(List<ResolveInfo> list) {
        if (list == null) {
            return null;
        }
        ArrayList<ResolveInfo> arrayList = new ArrayList<>(list.size());
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        for (ResolveInfo resolveInfo : list) {
            if (packageManagerInternal.canAccessComponent(callingUid, resolveInfo.activityInfo.getComponentName(), callingUserId)) {
                arrayList.add(resolveInfo);
            }
        }
        return arrayList;
    }

    public synchronized ComponentName getGlobalSearchActivity() {
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        ComponentName componentName = this.mCurrentGlobalSearchActivity;
        if (componentName == null || !packageManagerInternal.canAccessComponent(callingUid, componentName, callingUserId)) {
            return null;
        }
        return this.mCurrentGlobalSearchActivity;
    }

    public synchronized ComponentName getWebSearchActivity() {
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        int callingUid = Binder.getCallingUid();
        int callingUserId = UserHandle.getCallingUserId();
        ComponentName componentName = this.mWebSearchActivity;
        if (componentName == null || !packageManagerInternal.canAccessComponent(callingUid, componentName, callingUserId)) {
            return null;
        }
        return this.mWebSearchActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Searchable authorities:");
        synchronized (this) {
            ArrayList<SearchableInfo> arrayList = this.mSearchablesList;
            if (arrayList != null) {
                Iterator<SearchableInfo> it = arrayList.iterator();
                while (it.hasNext()) {
                    SearchableInfo next = it.next();
                    printWriter.print("  ");
                    printWriter.println(next.getSuggestAuthority());
                }
            }
        }
    }

    ISearchablesWrapper getWrapper() {
        return this.mSearchablesWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class SearchablesWrapper implements ISearchablesWrapper {
        private SearchablesWrapper() {
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public List<SearchableInfo> getSearchablesList() {
            return Searchables.this.mSearchablesList;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public List<SearchableInfo> getSearchablesInGlobalSearchList() {
            return Searchables.this.mSearchablesInGlobalSearchList;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public Map<ComponentName, SearchableInfo> getSearchablesMap() {
            return Searchables.this.mSearchablesMap;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public void setSearchablesMap(HashMap<ComponentName, SearchableInfo> hashMap) {
            Searchables.this.mSearchablesMap = hashMap;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public List<ResolveInfo> queryIntentActivities(Intent intent, int i) {
            return Searchables.this.queryIntentActivities(intent, i);
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public List<ResolveInfo> findGlobalSearchActivities() {
            return Searchables.this.findGlobalSearchActivities();
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public ComponentName findGlobalSearchActivity(List<ResolveInfo> list) {
            return Searchables.this.findGlobalSearchActivity(list);
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public ComponentName findWebSearchActivity(ComponentName componentName) {
            return Searchables.this.findWebSearchActivity(componentName);
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public void setGlobalSearchActivities(List<ResolveInfo> list) {
            Searchables.this.mGlobalSearchActivities = list;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public void setCurrentGlobalSearchActivity(ComponentName componentName) {
            Searchables.this.mCurrentGlobalSearchActivity = componentName;
        }

        @Override // com.android.server.search.ISearchablesWrapper
        public void setWebSearchActivity(ComponentName componentName) {
            Searchables.this.mWebSearchActivity = componentName;
        }
    }
}
