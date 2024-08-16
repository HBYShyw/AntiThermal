package com.coui.appcompat.expandable;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: COUIExpandableRecyclerAdapter.java */
/* renamed from: com.coui.appcompat.expandable.a, reason: use source file name */
/* loaded from: classes.dex */
public interface COUIExpandableRecyclerAdapter {
    RecyclerView.c0 a(ViewGroup viewGroup, int i10);

    void b(RecyclerView.j jVar);

    void c(int i10, boolean z10, RecyclerView.c0 c0Var);

    void d(RecyclerView.j jVar);

    void e(int i10, int i11, boolean z10, RecyclerView.c0 c0Var);

    RecyclerView.c0 f(ViewGroup viewGroup, int i10);

    long getChildId(int i10, int i11);

    int getChildType(int i10, int i11);

    int getChildrenCount(int i10);

    long getCombinedChildId(long j10, long j11);

    long getCombinedGroupId(long j10);

    int getGroupCount();

    long getGroupId(int i10);

    int getGroupType(int i10);

    int getGroupTypeCount();

    boolean hasStableIds();

    boolean isChildSelectable(int i10, int i11);

    void onGroupCollapsed(int i10);

    void onGroupExpanded(int i10);
}
