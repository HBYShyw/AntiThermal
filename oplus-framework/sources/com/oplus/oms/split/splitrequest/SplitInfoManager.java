package com.oplus.oms.split.splitrequest;

import android.content.Context;
import java.util.Collection;
import java.util.List;

/* loaded from: classes.dex */
public interface SplitInfoManager {
    Collection<SplitInfo> getAllSplitInfo(Context context);

    Collection<SplitInfo> getAllUpdateSplitInfo(Context context);

    String getBaseAppVersionName(Context context);

    String getDefaultSplitInfoVersion();

    String getOmsId(Context context);

    List<String> getSplitEntryFragments(Context context);

    SplitInfo getSplitInfo(Context context, String str);

    List<SplitInfo> getSplitInfos(Context context, Collection<String> collection);

    SplitInfo getSplitUpdateInfo(Context context, String str);
}
