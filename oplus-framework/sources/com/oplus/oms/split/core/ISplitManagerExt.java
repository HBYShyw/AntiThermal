package com.oplus.oms.split.core;

import android.app.Application;
import android.content.Context;
import com.oplus.oms.split.common.SplitInfoData;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public interface ISplitManagerExt {
    ClassLoader getSplitClassLoader(String str);

    int getSplitVersionCode(Context context, String str);

    void loadNow(Application application, Set<SplitInfoData> set);

    boolean scheduledDownload(Context context, List<String> list);

    void setNetworkStrategy(int i);

    void unloadSplit(Context context, String str);
}
