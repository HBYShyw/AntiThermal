package com.android.server.devicepolicy;

import android.os.Environment;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface PolicyPathProvider {
    default File getDataSystemDirectory() {
        return Environment.getDataSystemDirectory();
    }

    default File getUserSystemDirectory(int i) {
        return Environment.getUserSystemDirectory(i);
    }
}
