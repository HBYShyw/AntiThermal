package com.oplus.wrapper.res;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class AssetManager {
    private final android.content.res.AssetManager mAssetManager;

    public AssetManager() {
        this.mAssetManager = new android.content.res.AssetManager();
    }

    public AssetManager(android.content.res.AssetManager assetManager) {
        this.mAssetManager = assetManager;
    }

    public int addAssetPath(String path) {
        return this.mAssetManager.addAssetPath(path);
    }

    public InputStream openNonAsset(String fileName) throws IOException {
        return this.mAssetManager.openNonAsset(fileName);
    }

    public ApkAssets[] getApkAssets() {
        android.content.res.ApkAssets[] apkAssetsList = this.mAssetManager.getApkAssets();
        if (apkAssetsList == null) {
            return null;
        }
        ApkAssets[] apkAssetWrapperList = new ApkAssets[apkAssetsList.length];
        for (int i = 0; i < apkAssetsList.length; i++) {
            ApkAssets apkAssetsWrapper = new ApkAssets(apkAssetsList[i]);
            apkAssetWrapperList[i] = apkAssetsWrapper;
        }
        return apkAssetWrapperList;
    }
}
