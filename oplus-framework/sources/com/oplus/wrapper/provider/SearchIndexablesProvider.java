package com.oplus.wrapper.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

/* loaded from: classes.dex */
public abstract class SearchIndexablesProvider extends ContentProvider {
    private final SearchIndexablesProviderInner mInnerProvider = new SearchIndexablesProviderInner();

    public abstract Cursor queryNonIndexableKeys(String[] strArr);

    public abstract Cursor queryRawData(String[] strArr);

    public abstract Cursor queryXmlResources(String[] strArr);

    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo info) {
        this.mInnerProvider.attachInfo(context, info);
        super.attachInfo(context, info);
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.mInnerProvider.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return this.mInnerProvider.getType(uri);
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        return this.mInnerProvider.insert(uri, values);
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return this.mInnerProvider.delete(uri, selection, selectionArgs);
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return this.mInnerProvider.update(uri, values, selection, selectionArgs);
    }

    public Cursor querySiteMapPairs() {
        return this.mInnerProvider.superQuerySiteMapPairs();
    }

    public Cursor querySliceUriPairs() {
        return this.mInnerProvider.superQuerySliceUriPairs();
    }

    public Cursor queryDynamicRawData(String[] projection) {
        return this.mInnerProvider.superQueryDynamicRawData(projection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SearchIndexablesProviderInner extends android.provider.SearchIndexablesProvider {
        private SearchIndexablesProviderInner() {
        }

        public boolean onCreate() {
            return true;
        }

        public Cursor queryXmlResources(String[] projection) {
            return SearchIndexablesProvider.this.queryXmlResources(projection);
        }

        public Cursor queryRawData(String[] projection) {
            return SearchIndexablesProvider.this.queryRawData(projection);
        }

        public Cursor queryNonIndexableKeys(String[] projection) {
            return SearchIndexablesProvider.this.queryNonIndexableKeys(projection);
        }

        public Cursor querySiteMapPairs() {
            return SearchIndexablesProvider.this.querySiteMapPairs();
        }

        public Cursor querySliceUriPairs() {
            return SearchIndexablesProvider.this.querySliceUriPairs();
        }

        public Cursor queryDynamicRawData(String[] projection) {
            return SearchIndexablesProvider.this.queryDynamicRawData(projection);
        }

        Cursor superQuerySiteMapPairs() {
            return super.querySiteMapPairs();
        }

        Cursor superQuerySliceUriPairs() {
            return super.querySliceUriPairs();
        }

        Cursor superQueryDynamicRawData(String[] projection) {
            return super.queryDynamicRawData(projection);
        }
    }
}
