package com.coui.appcompat.searchview;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.SearchView;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class COUISearchView extends SearchView {

    /* renamed from: e, reason: collision with root package name */
    private SearchView.SearchAutoComplete f7438e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f7439f;

    public COUISearchView(Context context) {
        super(context);
        this.f7439f = true;
    }

    public SearchView.SearchAutoComplete getSearchAutoComplete() {
        SearchView.SearchAutoComplete searchAutoComplete = this.f7438e;
        if (searchAutoComplete != null) {
            return searchAutoComplete;
        }
        try {
            Field declaredField = Class.forName("androidx.appcompat.widget.SearchView").getDeclaredField("mSearchSrcTextView");
            declaredField.setAccessible(true);
            SearchView.SearchAutoComplete searchAutoComplete2 = (SearchView.SearchAutoComplete) declaredField.get(this);
            this.f7438e = searchAutoComplete2;
            return searchAutoComplete2;
        } catch (Exception e10) {
            e10.printStackTrace();
            return null;
        }
    }

    public COUISearchView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7439f = true;
    }

    public COUISearchView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7439f = true;
    }
}
