package com.oplus.wrapper.dalvik.system;

/* loaded from: classes.dex */
public class VMRuntime {
    private VMRuntime() {
    }

    public static String getInstructionSet(String abi) {
        return dalvik.system.VMRuntime.getInstructionSet(abi);
    }
}
