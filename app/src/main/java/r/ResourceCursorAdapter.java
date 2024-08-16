package r;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* compiled from: ResourceCursorAdapter.java */
/* renamed from: r.c, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ResourceCursorAdapter extends CursorAdapter {

    /* renamed from: m, reason: collision with root package name */
    private int f17452m;

    /* renamed from: n, reason: collision with root package name */
    private int f17453n;

    /* renamed from: o, reason: collision with root package name */
    private LayoutInflater f17454o;

    @Deprecated
    public ResourceCursorAdapter(Context context, int i10, Cursor cursor, boolean z10) {
        super(context, cursor, z10);
        this.f17453n = i10;
        this.f17452m = i10;
        this.f17454o = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // r.CursorAdapter
    public View c(Context context, Cursor cursor, ViewGroup viewGroup) {
        return this.f17454o.inflate(this.f17453n, viewGroup, false);
    }

    @Override // r.CursorAdapter
    public View d(Context context, Cursor cursor, ViewGroup viewGroup) {
        return this.f17454o.inflate(this.f17452m, viewGroup, false);
    }
}
