package com.oplus.wrapper.provider;

/* loaded from: classes.dex */
public class SearchIndexableResource {
    private final android.provider.SearchIndexableResource mSearchIndexableResource;

    public SearchIndexableResource(int rank, int xmlResId, String className, int iconResId) {
        this.mSearchIndexableResource = new android.provider.SearchIndexableResource(rank, xmlResId, className, iconResId);
    }

    public int getRank() {
        return this.mSearchIndexableResource.rank;
    }

    public int getXmlResId() {
        return this.mSearchIndexableResource.xmlResId;
    }

    public int getIconResId() {
        return this.mSearchIndexableResource.iconResId;
    }
}
