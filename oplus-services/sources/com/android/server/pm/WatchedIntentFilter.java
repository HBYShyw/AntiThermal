package com.android.server.pm;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;
import android.util.Printer;
import com.android.server.utils.Snappable;
import com.android.server.utils.WatchableImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WatchedIntentFilter extends WatchableImpl implements Snappable<WatchedIntentFilter> {
    protected IntentFilter mFilter;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class WatchedIterator<E> implements Iterator<E> {
        private final Iterator<E> mIterator;

        WatchedIterator(Iterator<E> it) {
            this.mIterator = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mIterator.hasNext();
        }

        @Override // java.util.Iterator
        public E next() {
            return this.mIterator.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.mIterator.remove();
            WatchedIntentFilter.this.onChanged();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            this.mIterator.forEachRemaining(consumer);
            WatchedIntentFilter.this.onChanged();
        }
    }

    private <E> Iterator<E> maybeWatch(Iterator<E> it) {
        return it == null ? it : new WatchedIterator(it);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onChanged() {
        dispatchChange(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WatchedIntentFilter() {
        this.mFilter = new IntentFilter();
    }

    public WatchedIntentFilter(IntentFilter intentFilter) {
        this.mFilter = new IntentFilter(intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WatchedIntentFilter(WatchedIntentFilter watchedIntentFilter) {
        this(watchedIntentFilter.getIntentFilter());
    }

    public WatchedIntentFilter(String str) {
        this.mFilter = new IntentFilter(str);
    }

    public WatchedIntentFilter(String str, String str2) throws IntentFilter.MalformedMimeTypeException {
        this.mFilter = new IntentFilter(str, str2);
    }

    public WatchedIntentFilter cloneFilter() {
        return new WatchedIntentFilter(this.mFilter);
    }

    public IntentFilter getIntentFilter() {
        return this.mFilter;
    }

    public final void setPriority(int i) {
        this.mFilter.setPriority(i);
        onChanged();
    }

    public final int getPriority() {
        return this.mFilter.getPriority();
    }

    public final void setOrder(int i) {
        this.mFilter.setOrder(i);
        onChanged();
    }

    public final int getOrder() {
        return this.mFilter.getOrder();
    }

    public final boolean getAutoVerify() {
        return this.mFilter.getAutoVerify();
    }

    public final boolean handleAllWebDataURI() {
        return this.mFilter.handleAllWebDataURI();
    }

    public final boolean handlesWebUris(boolean z) {
        return this.mFilter.handlesWebUris(z);
    }

    public final boolean needsVerification() {
        return this.mFilter.needsVerification();
    }

    public void setVerified(boolean z) {
        this.mFilter.setVerified(z);
        onChanged();
    }

    public void setVisibilityToInstantApp(int i) {
        this.mFilter.setVisibilityToInstantApp(i);
        onChanged();
    }

    public int getVisibilityToInstantApp() {
        return this.mFilter.getVisibilityToInstantApp();
    }

    public boolean isVisibleToInstantApp() {
        return this.mFilter.isVisibleToInstantApp();
    }

    public boolean isExplicitlyVisibleToInstantApp() {
        return this.mFilter.isExplicitlyVisibleToInstantApp();
    }

    public boolean isImplicitlyVisibleToInstantApp() {
        return this.mFilter.isImplicitlyVisibleToInstantApp();
    }

    public final void addAction(String str) {
        this.mFilter.addAction(str);
        onChanged();
    }

    public final int countActions() {
        return this.mFilter.countActions();
    }

    public final String getAction(int i) {
        return this.mFilter.getAction(i);
    }

    public final boolean hasAction(String str) {
        return this.mFilter.hasAction(str);
    }

    public final boolean matchAction(String str) {
        return this.mFilter.matchAction(str);
    }

    public final Iterator<String> actionsIterator() {
        return maybeWatch(this.mFilter.actionsIterator());
    }

    public final void addDataType(String str) throws IntentFilter.MalformedMimeTypeException {
        this.mFilter.addDataType(str);
        onChanged();
    }

    public final void addDynamicDataType(String str) throws IntentFilter.MalformedMimeTypeException {
        this.mFilter.addDynamicDataType(str);
        onChanged();
    }

    public final void clearDynamicDataTypes() {
        this.mFilter.clearDynamicDataTypes();
        onChanged();
    }

    public int countStaticDataTypes() {
        return this.mFilter.countStaticDataTypes();
    }

    public final boolean hasDataType(String str) {
        return this.mFilter.hasDataType(str);
    }

    public final boolean hasExactDynamicDataType(String str) {
        return this.mFilter.hasExactDynamicDataType(str);
    }

    public final boolean hasExactStaticDataType(String str) {
        return this.mFilter.hasExactStaticDataType(str);
    }

    public final int countDataTypes() {
        return this.mFilter.countDataTypes();
    }

    public final String getDataType(int i) {
        return this.mFilter.getDataType(i);
    }

    public final Iterator<String> typesIterator() {
        return maybeWatch(this.mFilter.typesIterator());
    }

    public final List<String> dataTypes() {
        return this.mFilter.dataTypes();
    }

    public final void addMimeGroup(String str) {
        this.mFilter.addMimeGroup(str);
        onChanged();
    }

    public final boolean hasMimeGroup(String str) {
        return this.mFilter.hasMimeGroup(str);
    }

    public final String getMimeGroup(int i) {
        return this.mFilter.getMimeGroup(i);
    }

    public final int countMimeGroups() {
        return this.mFilter.countMimeGroups();
    }

    public final Iterator<String> mimeGroupsIterator() {
        return maybeWatch(this.mFilter.mimeGroupsIterator());
    }

    public final void addDataScheme(String str) {
        this.mFilter.addDataScheme(str);
        onChanged();
    }

    public final int countDataSchemes() {
        return this.mFilter.countDataSchemes();
    }

    public final String getDataScheme(int i) {
        return this.mFilter.getDataScheme(i);
    }

    public final boolean hasDataScheme(String str) {
        return this.mFilter.hasDataScheme(str);
    }

    public final Iterator<String> schemesIterator() {
        return maybeWatch(this.mFilter.schemesIterator());
    }

    public final void addDataSchemeSpecificPart(String str, int i) {
        this.mFilter.addDataSchemeSpecificPart(str, i);
        onChanged();
    }

    public final void addDataSchemeSpecificPart(PatternMatcher patternMatcher) {
        this.mFilter.addDataSchemeSpecificPart(patternMatcher);
        onChanged();
    }

    public final int countDataSchemeSpecificParts() {
        return this.mFilter.countDataSchemeSpecificParts();
    }

    public final PatternMatcher getDataSchemeSpecificPart(int i) {
        return this.mFilter.getDataSchemeSpecificPart(i);
    }

    public final boolean hasDataSchemeSpecificPart(String str) {
        return this.mFilter.hasDataSchemeSpecificPart(str);
    }

    public final Iterator<PatternMatcher> schemeSpecificPartsIterator() {
        return maybeWatch(this.mFilter.schemeSpecificPartsIterator());
    }

    public final void addDataAuthority(String str, String str2) {
        this.mFilter.addDataAuthority(str, str2);
        onChanged();
    }

    public final void addDataAuthority(IntentFilter.AuthorityEntry authorityEntry) {
        this.mFilter.addDataAuthority(authorityEntry);
        onChanged();
    }

    public final int countDataAuthorities() {
        return this.mFilter.countDataAuthorities();
    }

    public final IntentFilter.AuthorityEntry getDataAuthority(int i) {
        return this.mFilter.getDataAuthority(i);
    }

    public final boolean hasDataAuthority(Uri uri) {
        return this.mFilter.hasDataAuthority(uri);
    }

    public final Iterator<IntentFilter.AuthorityEntry> authoritiesIterator() {
        return maybeWatch(this.mFilter.authoritiesIterator());
    }

    public final void addDataPath(String str, int i) {
        this.mFilter.addDataPath(str, i);
        onChanged();
    }

    public final void addDataPath(PatternMatcher patternMatcher) {
        this.mFilter.addDataPath(patternMatcher);
        onChanged();
    }

    public final int countDataPaths() {
        return this.mFilter.countDataPaths();
    }

    public final PatternMatcher getDataPath(int i) {
        return this.mFilter.getDataPath(i);
    }

    public final boolean hasDataPath(String str) {
        return this.mFilter.hasDataPath(str);
    }

    public final Iterator<PatternMatcher> pathsIterator() {
        return maybeWatch(this.mFilter.pathsIterator());
    }

    public final int matchDataAuthority(Uri uri) {
        return this.mFilter.matchDataAuthority(uri);
    }

    public final int matchDataAuthority(Uri uri, boolean z) {
        return this.mFilter.matchDataAuthority(uri, z);
    }

    public final int matchData(String str, String str2, Uri uri) {
        return this.mFilter.matchData(str, str2, uri);
    }

    public final void addCategory(String str) {
        this.mFilter.addCategory(str);
    }

    public final int countCategories() {
        return this.mFilter.countCategories();
    }

    public final String getCategory(int i) {
        return this.mFilter.getCategory(i);
    }

    public final boolean hasCategory(String str) {
        return this.mFilter.hasCategory(str);
    }

    public final Iterator<String> categoriesIterator() {
        return maybeWatch(this.mFilter.categoriesIterator());
    }

    public final String matchCategories(Set<String> set) {
        return this.mFilter.matchCategories(set);
    }

    public final int match(ContentResolver contentResolver, Intent intent, boolean z, String str) {
        return this.mFilter.match(contentResolver, intent, z, str);
    }

    public final int match(String str, String str2, String str3, Uri uri, Set<String> set, String str4) {
        return this.mFilter.match(str, str2, str3, uri, set, str4);
    }

    public final int match(String str, String str2, String str3, Uri uri, Set<String> set, String str4, boolean z, Collection<String> collection) {
        return this.mFilter.match(str, str2, str3, uri, set, str4, z, collection);
    }

    public void dump(Printer printer, String str) {
        this.mFilter.dump(printer, str);
    }

    public final int describeContents() {
        return this.mFilter.describeContents();
    }

    public boolean debugCheck() {
        return this.mFilter.debugCheck();
    }

    public boolean checkDataPathAndSchemeSpecificParts() {
        return this.mFilter.checkDataPathAndSchemeSpecificParts();
    }

    public ArrayList<String> getHostsList() {
        return this.mFilter.getHostsList();
    }

    public String[] getHosts() {
        return this.mFilter.getHosts();
    }

    public static List<WatchedIntentFilter> toWatchedIntentFilterList(List<IntentFilter> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(new WatchedIntentFilter(list.get(i)));
        }
        return arrayList;
    }

    public static List<IntentFilter> toIntentFilterList(List<WatchedIntentFilter> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i).getIntentFilter());
        }
        return arrayList;
    }

    @Override // com.android.server.utils.Snappable
    public WatchedIntentFilter snapshot() {
        return new WatchedIntentFilter(this);
    }
}
