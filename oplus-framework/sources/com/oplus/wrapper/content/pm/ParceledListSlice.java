package com.oplus.wrapper.content.pm;

import android.os.Parcelable;
import java.util.List;

/* loaded from: classes.dex */
public class ParceledListSlice<T extends Parcelable> {
    private final android.content.pm.ParceledListSlice<T> mParceledListSlice;

    public ParceledListSlice(android.content.pm.ParceledListSlice<T> parceledListSlice) {
        this.mParceledListSlice = parceledListSlice;
    }

    public ParceledListSlice(List<T> list) {
        this.mParceledListSlice = new android.content.pm.ParceledListSlice<>(list);
    }

    public android.content.pm.ParceledListSlice<T> getParceledListSlice() {
        return this.mParceledListSlice;
    }

    public List<T> getList() {
        return this.mParceledListSlice.getList();
    }
}
