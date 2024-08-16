package com.oplus.oms.split.splitdownload;

import android.content.Context;
import java.io.FileNotFoundException;
import java.io.InputStream;

/* loaded from: classes.dex */
public interface IProvider {
    InputStream getSplitFileStream(Context context, String str) throws FileNotFoundException;

    int getSplitVersionCode(Context context, String str);

    String getSplitVersionName(Context context, String str);
}
