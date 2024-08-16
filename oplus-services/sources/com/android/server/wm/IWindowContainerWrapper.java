package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IWindowContainerWrapper {
    default int syncTransactionCommitCallbackDepth() {
        return 0;
    }

    default IWindowContainerExt getExtImpl() {
        return new IWindowContainerExt() { // from class: com.android.server.wm.IWindowContainerWrapper.1
        };
    }
}
