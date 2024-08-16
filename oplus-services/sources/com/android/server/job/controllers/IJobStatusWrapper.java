package com.android.server.job.controllers;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IJobStatusWrapper {
    default boolean hasConstraint(int i) {
        return false;
    }

    default IJobStatusExt getExtImpl() {
        return new IJobStatusExt() { // from class: com.android.server.job.controllers.IJobStatusWrapper.1
        };
    }
}
