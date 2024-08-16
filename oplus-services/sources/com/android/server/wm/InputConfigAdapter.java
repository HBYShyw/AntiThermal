package com.android.server.wm;

import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class InputConfigAdapter {
    private static final List<FlagMapping> INPUT_FEATURE_TO_CONFIG_MAP;
    private static final int INPUT_FEATURE_TO_CONFIG_MASK;
    private static final List<FlagMapping> LAYOUT_PARAM_FLAG_TO_CONFIG_MAP;
    private static final int LAYOUT_PARAM_FLAG_TO_CONFIG_MASK;

    private InputConfigAdapter() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class FlagMapping {
        final int mFlag;
        final int mInputConfig;
        final boolean mInverted;

        FlagMapping(int i, int i2, boolean z) {
            this.mFlag = i;
            this.mInputConfig = i2;
            this.mInverted = z;
        }
    }

    static {
        List<FlagMapping> of = List.of(new FlagMapping(1, 1, false), new FlagMapping(2, 2048, false), new FlagMapping(4, 16384, false));
        INPUT_FEATURE_TO_CONFIG_MAP = of;
        INPUT_FEATURE_TO_CONFIG_MASK = computeMask(of);
        List<FlagMapping> of2 = List.of(new FlagMapping(16, 8, false), new FlagMapping(8388608, 16, true), new FlagMapping(262144, 512, false), new FlagMapping(536870912, 1024, false));
        LAYOUT_PARAM_FLAG_TO_CONFIG_MAP = of2;
        LAYOUT_PARAM_FLAG_TO_CONFIG_MASK = computeMask(of2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMask() {
        return LAYOUT_PARAM_FLAG_TO_CONFIG_MASK | INPUT_FEATURE_TO_CONFIG_MASK | 64;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getInputConfigFromWindowParams(int i, int i2, int i3) {
        return (i == 2013 ? 64 : 0) | applyMapping(i2, LAYOUT_PARAM_FLAG_TO_CONFIG_MAP) | applyMapping(i3, INPUT_FEATURE_TO_CONFIG_MAP);
    }

    private static int applyMapping(int i, List<FlagMapping> list) {
        int i2 = 0;
        for (FlagMapping flagMapping : list) {
            if (((flagMapping.mFlag & i) != 0) != flagMapping.mInverted) {
                i2 |= flagMapping.mInputConfig;
            }
        }
        return i2;
    }

    private static int computeMask(List<FlagMapping> list) {
        Iterator<FlagMapping> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            i |= it.next().mInputConfig;
        }
        return i;
    }
}
