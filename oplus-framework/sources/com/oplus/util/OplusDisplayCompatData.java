package com.oplus.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Slog;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public final class OplusDisplayCompatData implements Parcelable {
    public static final Parcelable.Creator<OplusDisplayCompatData> CREATOR = new Parcelable.Creator<OplusDisplayCompatData>() { // from class: com.oplus.util.OplusDisplayCompatData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusDisplayCompatData createFromParcel(Parcel in) {
            return new OplusDisplayCompatData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusDisplayCompatData[] newArray(int size) {
            return new OplusDisplayCompatData[size];
        }
    };
    private static final boolean DBG = false;
    private static final String TAG = "OplusDisplayCompatData";
    public static final int TYPE_FIRST = 0;
    public static final int TYPE_INSTALLED_COMPAT_LIST = 7;
    public static final int TYPE_INSTALLED_IME_LIST = 8;
    public static final int TYPE_INSTALLED_THIRD_PARTY_APP_LIST = 12;
    public static final int TYPE_LAST = 18;
    public static final int TYPE_LOCAL_COMPAT_LIST = 2;
    public static final int TYPE_LOCAL_CUTOUT_DEFAULT_LIST = 4;
    public static final int TYPE_LOCAL_CUTOUT_HIDE_LIST = 6;
    public static final int TYPE_LOCAL_CUTOUT_SHOW_LIST = 5;
    public static final int TYPE_LOCAL_FULL_SCREEN_LIST = 3;
    public static final int TYPE_RUS_COMPAT_LIST = 0;
    public static final int TYPE_RUS_FOLD_SCREEN_AUTO_MATCH_LIST = 18;
    public static final int TYPE_RUS_FOLD_SCREEN_FORCE_AUTO_MATCH_LIST = 14;
    public static final int TYPE_RUS_FOLD_SCREEN_FORCE_NON_HIDE_CUTOUT_LIST = 15;
    public static final int TYPE_RUS_FOLD_SCREEN_IMMERSIVE_LIST = 16;
    public static final int TYPE_RUS_FOLD_SCREEN_NON_FORCE_IMMERSIVE_LIST = 13;
    public static final int TYPE_RUS_FOLD_SCREEN_NON_IMMERSIVE_LIST = 17;
    public static final int TYPE_RUS_IMMERSIVE_LIST = 10;
    public static final int TYPE_RUS_NON_IMMERSIVE_LIST = 11;
    public static final int TYPE_RUS_NO_COMPAT_LIST = 1;
    public static final int TYPE_SHOW_DIALOG_APPS_LIST = 9;
    private SparseArray<List<String>> mLists = new SparseArray<>();
    private boolean mEnableDisplayCompat = true;
    private boolean mHasHeteromorphismFeature = false;
    private boolean mRusImmersiveDefault = false;
    private int mDisplayCutoutType = 0;

    public OplusDisplayCompatData() {
    }

    public OplusDisplayCompatData(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSparseArray(flattenList(this.mLists));
        parcel.writeByte(this.mEnableDisplayCompat ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mHasHeteromorphismFeature ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mRusImmersiveDefault ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.mDisplayCutoutType);
    }

    public void readFromParcel(Parcel in) {
        this.mLists = unFlattenList(in.readSparseArray(null, String.class));
        this.mEnableDisplayCompat = in.readByte() != 0;
        this.mHasHeteromorphismFeature = in.readByte() != 0;
        this.mRusImmersiveDefault = in.readByte() != 0;
        this.mDisplayCutoutType = in.readInt();
    }

    private SparseArray<String> flattenList(SparseArray<List<String>> list) {
        SparseArray<String> fl = new SparseArray<>();
        for (int i = 0; i < list.size(); i++) {
            fl.put(list.keyAt(i), String.join(",", list.valueAt(i)));
        }
        return fl;
    }

    private SparseArray<List<String>> unFlattenList(SparseArray<String> fl) {
        SparseArray<List<String>> list = new SparseArray<>();
        for (int i = 0; i < fl.size(); i++) {
            list.put(fl.keyAt(i), new ArrayList(Arrays.asList(fl.valueAt(i).split(","))));
        }
        return list;
    }

    public OplusDisplayCompatData updateFrom(OplusDisplayCompatData data) {
        SparseArray<List<String>> sparseArray;
        if (data == null || (sparseArray = data.mLists) == null) {
            return this;
        }
        this.mLists = sparseArray.clone();
        this.mEnableDisplayCompat = data.mEnableDisplayCompat;
        this.mHasHeteromorphismFeature = data.mHasHeteromorphismFeature;
        this.mRusImmersiveDefault = data.mRusImmersiveDefault;
        return this;
    }

    public HashMap<String, String> getCompatPackageList() {
        return new HashMap<>();
    }

    public void setCompatPackageList(HashMap<String, String> compatPackageList) {
    }

    public boolean getDisplayCompatEnabled() {
        return this.mEnableDisplayCompat;
    }

    public void setDisplatOptEnabled(boolean enabled) {
        this.mEnableDisplayCompat = enabled;
    }

    public boolean hasHeteromorphismFeature() {
        return this.mHasHeteromorphismFeature;
    }

    public void setHasHeteromorphismFeature(boolean hasFeature) {
        this.mHasHeteromorphismFeature = hasFeature;
    }

    public List<String> getWhiteList() {
        return getList(0);
    }

    public void setWhiteList(List<String> whiteList) {
        putList(0, whiteList);
    }

    public List<String> getBlackList() {
        return getList(1);
    }

    public void setBlackList(List<String> blackList) {
        putList(1, blackList);
    }

    public List<String> getLocalCompatList() {
        return getList(2);
    }

    public void setLocalCompatList(List<String> localList) {
        putList(2, localList);
    }

    public List<String> getLocalFullScreenList() {
        return getList(3);
    }

    public void setLocalFullScreenList(List<String> localList) {
        putList(3, localList);
    }

    public List<String> getLocalNonImmersiveList() {
        return getLocalCutoutDefaultList();
    }

    public void setLocalNonImmersiveList(List<String> localList) {
        setLocalCutoutDefaultList(localList);
    }

    public List<String> getLocalImmersiveList() {
        return getList(5);
    }

    public void setLocalImmersiveList(List<String> localList) {
        putList(5, localList);
    }

    public List<String> getInstalledCompatList() {
        return getList(7);
    }

    public void setInstalledCompatList(List<String> installedList) {
        putList(7, installedList);
    }

    public List<String> getInstalledImeList() {
        return getList(8);
    }

    public void setInstalledImeList(List<String> imeList) {
        putList(8, imeList);
    }

    public List<String> getShowDialogAppList() {
        return getList(9);
    }

    public void setShowDialogAppList(List<String> localList) {
        putList(9, localList);
    }

    public boolean getRusImmersiveDefault() {
        return this.mRusImmersiveDefault;
    }

    public void setRusImmersiveDefault(boolean defaultValue) {
        this.mRusImmersiveDefault = defaultValue;
    }

    public List<String> getRusImmersiveList() {
        return getList(10);
    }

    public void setRusImmersiveList(List<String> list) {
        putList(10, list);
    }

    public List<String> getRusNonImmersiveList() {
        return getList(11);
    }

    public void setRusNonImmersiveList(List<String> list) {
        putList(11, list);
    }

    public List<String> getInstalledThirdPartyAppList() {
        return getList(12);
    }

    public void setInstalledThirdPartyAppList(List<String> list) {
        putList(12, list);
    }

    public List<String> getNeedAdjustSizeAppList() {
        return new ArrayList();
    }

    public void setNeedAdjustSizeAppList(List<String> list) {
    }

    public int getDisplayCutoutType() {
        return this.mDisplayCutoutType;
    }

    public void setDisplayCutoutType(int type) {
        this.mDisplayCutoutType = type;
    }

    public void setLocalCutoutDefaultList(List<String> localList) {
        putList(4, localList);
    }

    public List<String> getLocalCutoutDefaultList() {
        return getList(4);
    }

    public void setLocalCutoutShowList(List<String> localList) {
        putList(5, localList);
    }

    public List<String> getLocalCutoutShowList() {
        return getList(5);
    }

    public void setLocalCutoutHideList(List<String> localList) {
        putList(6, localList);
    }

    public List<String> getLocalCutoutHideList() {
        return getList(6);
    }

    public List<String> getList(int type) {
        if (checkTypeInvalid(type)) {
            Slog.e(TAG, "getList: invalid type " + type);
            return new ArrayList();
        }
        List<String> list = this.mLists.get(type);
        if (list == null) {
            List<String> list2 = new ArrayList<>();
            this.mLists.put(type, list2);
            return list2;
        }
        return list;
    }

    public void putList(int type, List<String> list) {
        if (checkTypeInvalid(type)) {
            Slog.e(TAG, "putList: invalid type " + type);
            return;
        }
        if (list == null) {
            list = new ArrayList();
        }
        this.mLists.put(type, list);
    }

    public static boolean checkTypeInvalid(int type) {
        return type < 0 || type > 18;
    }
}
