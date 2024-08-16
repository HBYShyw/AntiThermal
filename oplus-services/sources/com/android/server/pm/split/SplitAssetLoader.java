package com.android.server.pm.split;

import android.content.res.ApkAssets;
import android.content.res.AssetManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface SplitAssetLoader extends AutoCloseable {
    ApkAssets getBaseApkAssets();

    AssetManager getBaseAssetManager() throws IllegalArgumentException;

    AssetManager getSplitAssetManager(int i) throws IllegalArgumentException;
}
