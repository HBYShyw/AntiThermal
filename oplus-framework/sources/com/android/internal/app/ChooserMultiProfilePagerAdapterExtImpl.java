package com.android.internal.app;

import android.os.SystemProperties;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class ChooserMultiProfilePagerAdapterExtImpl implements IChooserMultiProfilePagerAdapterExt {
    private static final String TAG = "ChooserMultiProfilePagerAdapterExtImpl";
    private boolean mDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    public ChooserMultiProfilePagerAdapterExtImpl(Object base) {
    }

    public ViewGroup getChooserProfileDescriptor(LayoutInflater inflater) {
        if (this.mDebug) {
            Slog.d(TAG, "getChooserProfileDescriptor in impl");
        }
        return (ViewGroup) inflater.inflate(201917472, (ViewGroup) null, false);
    }
}
