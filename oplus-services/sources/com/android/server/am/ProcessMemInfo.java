package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ProcessMemInfo {
    final String adjReason;
    final String adjType;
    long memtrack;
    final String name;
    final int oomAdj;
    final int pid;
    final int procState;
    long pss;
    long swapPss;

    public ProcessMemInfo(String str, int i, int i2, int i3, String str2, String str3) {
        this.name = str;
        this.pid = i;
        this.oomAdj = i2;
        this.procState = i3;
        this.adjType = str2;
        this.adjReason = str3;
    }
}
