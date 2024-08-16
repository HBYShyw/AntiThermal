package com.android.server;

import android.os.DropBoxManager;
import java.io.IOException;
import java.util.HashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBootReceiverCallbackExt {
    void onAddFileToDropBox(DropBoxManager dropBoxManager, HashMap<String, Long> hashMap, String str, String str2, int i, String str3) throws IOException;
}
