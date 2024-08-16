package com.oplus.os;

import android.os.SystemProperties;
import com.oplus.os.OplusBuild;

/* loaded from: classes.dex */
public class ModuleBuild {

    /* loaded from: classes.dex */
    public static final class ModuleVersionCodes {
        public static final int OS_14_0 = 30;
    }

    /* loaded from: classes.dex */
    public static class VERSION {
        public static final int SDK_VERSION = OplusBuild.VERSION.SDK_VERSION;
        public static final int SDK_SUB_VERSION = SystemProperties.getInt("ro.build.version.module.sub_api", 1);
    }
}
