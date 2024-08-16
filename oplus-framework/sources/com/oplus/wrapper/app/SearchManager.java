package com.oplus.wrapper.app;

import android.app.SearchableInfo;
import android.content.ComponentName;
import android.database.Cursor;

/* loaded from: classes.dex */
public class SearchManager {
    private final android.app.SearchManager mSearchManager;

    public SearchManager(android.app.SearchManager searchManager) {
        this.mSearchManager = searchManager;
    }

    public Cursor getSuggestions(SearchableInfo searchable, String query) {
        return this.mSearchManager.getSuggestions(searchable, query);
    }

    public Cursor getSuggestions(SearchableInfo searchable, String query, int limit) {
        return this.mSearchManager.getSuggestions(searchable, query, limit);
    }

    public ComponentName getWebSearchActivity() {
        return this.mSearchManager.getWebSearchActivity();
    }
}
