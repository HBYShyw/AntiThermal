package com.coui.appcompat.preference;

import android.content.Context;
import android.view.View;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: COUIPreferenceItemDecoration.java */
/* renamed from: com.coui.appcompat.preference.h, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPreferenceItemDecoration extends COUIRecyclerView.a {

    /* renamed from: e, reason: collision with root package name */
    private final int[] f7073e;

    /* renamed from: f, reason: collision with root package name */
    private final int[] f7074f;

    /* renamed from: g, reason: collision with root package name */
    private PreferenceScreen f7075g;

    public COUIPreferenceItemDecoration(Context context, PreferenceScreen preferenceScreen) {
        super(context);
        this.f7073e = new int[2];
        this.f7074f = new int[2];
        this.f7075g = preferenceScreen;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.a
    public int k(RecyclerView recyclerView, int i10) {
        int width;
        int width2;
        if (this.f7075g == null) {
            return super.k(recyclerView, i10);
        }
        RecyclerView.h adapter = recyclerView.getAdapter();
        if (adapter instanceof PreferenceGroupAdapter) {
            View childAt = recyclerView.getChildAt(i10);
            Object item = ((PreferenceGroupAdapter) adapter).getItem(recyclerView.getChildAdapterPosition(childAt));
            if (item != null && (item instanceof COUIRecyclerView.b)) {
                boolean z10 = childAt.getLayoutDirection() == 1;
                COUIRecyclerView.b bVar = (COUIRecyclerView.b) item;
                View dividerEndAlignView = bVar.getDividerEndAlignView();
                if (dividerEndAlignView != null) {
                    childAt.getLocationInWindow(this.f7073e);
                    dividerEndAlignView.getLocationInWindow(this.f7074f);
                    if (z10) {
                        width = this.f7074f[0] + dividerEndAlignView.getPaddingEnd();
                        width2 = this.f7073e[0];
                    } else {
                        width = this.f7073e[0] + childAt.getWidth();
                        width2 = (this.f7074f[0] + dividerEndAlignView.getWidth()) - dividerEndAlignView.getPaddingEnd();
                    }
                    return width - width2;
                }
                return bVar.getDividerEndInset();
            }
        }
        return super.l(recyclerView, i10);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.a
    public int l(RecyclerView recyclerView, int i10) {
        int paddingStart;
        int i11;
        if (this.f7075g == null) {
            return super.l(recyclerView, i10);
        }
        RecyclerView.h adapter = recyclerView.getAdapter();
        if (adapter instanceof PreferenceGroupAdapter) {
            View childAt = recyclerView.getChildAt(i10);
            Object item = ((PreferenceGroupAdapter) adapter).getItem(recyclerView.getChildAdapterPosition(childAt));
            if (item != null && (item instanceof COUIRecyclerView.b)) {
                boolean z10 = childAt.getLayoutDirection() == 1;
                COUIRecyclerView.b bVar = (COUIRecyclerView.b) item;
                View dividerStartAlignView = bVar.getDividerStartAlignView();
                if (dividerStartAlignView != null) {
                    childAt.getLocationInWindow(this.f7073e);
                    dividerStartAlignView.getLocationInWindow(this.f7074f);
                    if (z10) {
                        paddingStart = this.f7073e[0] + childAt.getWidth();
                        i11 = (this.f7074f[0] + dividerStartAlignView.getWidth()) - dividerStartAlignView.getPaddingStart();
                    } else {
                        paddingStart = this.f7074f[0] + dividerStartAlignView.getPaddingStart();
                        i11 = this.f7073e[0];
                    }
                    return paddingStart - i11;
                }
                return bVar.getDividerStartInset();
            }
        }
        return super.l(recyclerView, i10);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.a
    public boolean n(RecyclerView recyclerView, int i10) {
        Object item;
        if (this.f7075g == null) {
            return false;
        }
        RecyclerView.h adapter = recyclerView.getAdapter();
        if ((adapter instanceof PreferenceGroupAdapter) && (item = ((PreferenceGroupAdapter) adapter).getItem(recyclerView.getChildAdapterPosition(recyclerView.getChildAt(i10)))) != null && (item instanceof COUIRecyclerView.b)) {
            return ((COUIRecyclerView.b) item).drawDivider();
        }
        return false;
    }

    public void o() {
        this.f7075g = null;
    }
}
