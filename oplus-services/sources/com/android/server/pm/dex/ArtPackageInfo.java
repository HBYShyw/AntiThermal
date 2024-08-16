package com.android.server.pm.dex;

import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ArtPackageInfo {
    private final List<String> mCodePaths;
    private final List<String> mInstructionSets;
    private final String mOatDir;
    private final String mPackageName;

    public ArtPackageInfo(String str, List<String> list, List<String> list2, String str2) {
        this.mPackageName = str;
        this.mInstructionSets = list;
        this.mCodePaths = list2;
        this.mOatDir = str2;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public List<String> getInstructionSets() {
        return this.mInstructionSets;
    }

    public List<String> getCodePaths() {
        return this.mCodePaths;
    }

    public String getOatDir() {
        return this.mOatDir;
    }
}
