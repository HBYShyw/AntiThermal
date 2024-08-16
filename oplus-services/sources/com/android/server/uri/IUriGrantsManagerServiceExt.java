package com.android.server.uri;

import android.net.Uri;
import android.util.ArrayMap;
import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IUriGrantsManagerServiceExt {
    default int changeTargetUid(int i, int i2, String str, String str2) {
        return i;
    }

    default int changeUserIdInUriGrantsManagerService(int i, Uri uri) {
        return i;
    }

    default boolean checkLastChanceInCheckUriPermissionLocked(SparseArray<ArrayMap<GrantUri, UriPermission>> sparseArray, GrantUri grantUri, int i, int i2, int i3) {
        return false;
    }

    default boolean isGrantedSystemApp(String str) {
        return false;
    }

    default boolean isMultiappFromUid(int i) {
        return false;
    }

    default boolean needChangeUid(SparseArray<ArrayMap<GrantUri, UriPermission>> sparseArray, String str, int i) {
        return false;
    }

    default boolean skipMultiappHandleUri(int i, Uri uri) {
        return false;
    }
}
