package com.oplus.epona.route;

import android.content.Context;

/* loaded from: classes.dex */
public class RouteData {
    private final Context mContext;
    private final int mRequestCode;

    public RouteData(Context context, int i10) {
        this.mContext = context;
        this.mRequestCode = i10;
    }

    public Context getContext() {
        return this.mContext;
    }

    public int getRequestCode() {
        return this.mRequestCode;
    }
}
