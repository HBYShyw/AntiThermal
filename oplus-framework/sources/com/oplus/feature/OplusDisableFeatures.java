package com.oplus.feature;

/* loaded from: classes.dex */
public class OplusDisableFeatures {
    /* JADX INFO: Access modifiers changed from: private */
    public static String getDeptFeature(String dept, String module) {
        return "oppo." + dept + "." + module + ".disable";
    }

    /* loaded from: classes.dex */
    public static class SystemCenter {
        public static final String TRANSLATE = getFeature("translate");
        public static final String LONGSHOT = getFeature("longshot");

        private static String getFeature(String module) {
            return OplusDisableFeatures.getDeptFeature("system_center", module);
        }
    }
}
