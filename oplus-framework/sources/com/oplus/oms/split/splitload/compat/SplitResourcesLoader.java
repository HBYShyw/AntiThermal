package com.oplus.oms.split.splitload.compat;

import android.content.Context;
import android.content.res.Resources;
import com.oplus.oms.split.splitload.SplitCompatResourcesException;

/* loaded from: classes.dex */
public interface SplitResourcesLoader {
    void loadResources(Context context, Resources resources) throws SplitCompatResourcesException;

    void loadResources(Context context, Resources resources, String str) throws SplitCompatResourcesException;
}
