package com.android.internal.app;

import android.os.SystemProperties;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class ResolverMultiProfilePagerAdapterExtImpl implements IResolverMultiProfilePagerAdapterExt {
    private static final String TAG = "ResolverMultiProfilePagerAdapterExtImpl";
    private boolean mDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    public ResolverMultiProfilePagerAdapterExtImpl(Object base) {
    }

    public ViewGroup getResolverProfileDescriptor(LayoutInflater inflater) {
        if (this.mDebug) {
            Slog.d(TAG, "getResolverProfileDescriptor in impl");
        }
        return (ViewGroup) inflater.inflate(201917471, (ViewGroup) null, false);
    }
}
