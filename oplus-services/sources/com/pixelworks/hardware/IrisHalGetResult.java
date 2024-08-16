package com.pixelworks.hardware;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class IrisHalGetResult {
    public String json;
    public int ret;
    public int[] values;

    public IrisHalGetResult() {
        this.ret = -1;
        this.values = new int[0];
    }

    public IrisHalGetResult(int i) {
        this.ret = i;
        this.values = new int[0];
    }

    public IrisHalGetResult(int i, int[] iArr) {
        this.ret = i;
        this.values = iArr;
    }

    public IrisHalGetResult(int i, String str) {
        this.ret = i;
        this.json = str;
    }
}
