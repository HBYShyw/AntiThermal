package com.android.server.search;

import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.android.internal.annotations.GuardedBy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ISearchablesWrapper {
    default List<ResolveInfo> findGlobalSearchActivities() {
        return null;
    }

    default ComponentName findGlobalSearchActivity(List<ResolveInfo> list) {
        return null;
    }

    default ComponentName findWebSearchActivity(ComponentName componentName) {
        return null;
    }

    @GuardedBy({"Searchables.this"})
    default List<SearchableInfo> getSearchablesInGlobalSearchList() {
        return null;
    }

    @GuardedBy({"Searchables.this"})
    default List<SearchableInfo> getSearchablesList() {
        return null;
    }

    @GuardedBy({"Searchables.this"})
    default Map<ComponentName, SearchableInfo> getSearchablesMap() {
        return null;
    }

    default List<ResolveInfo> queryIntentActivities(Intent intent, int i) {
        return null;
    }

    @GuardedBy({"Searchables.this"})
    default void setCurrentGlobalSearchActivity(ComponentName componentName) {
    }

    @GuardedBy({"Searchables.this"})
    default void setGlobalSearchActivities(List<ResolveInfo> list) {
    }

    @GuardedBy({"Searchables.this"})
    default void setSearchablesMap(HashMap<ComponentName, SearchableInfo> hashMap) {
    }

    @GuardedBy({"Searchables.this"})
    default void setWebSearchActivity(ComponentName componentName) {
    }
}
