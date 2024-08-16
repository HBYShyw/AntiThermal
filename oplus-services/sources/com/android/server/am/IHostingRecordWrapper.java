package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IHostingRecordWrapper {
    default IHostingRecordExt getExtImpl() {
        return new IHostingRecordExt() { // from class: com.android.server.am.IHostingRecordWrapper.1
        };
    }
}
