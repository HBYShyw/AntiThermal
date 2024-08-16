package com.android.server.tv.tunerresourcemanager;

import com.android.server.tv.tunerresourcemanager.TunerResourceBasic;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DemuxResource extends TunerResourceBasic {
    private final int mFilterTypes;

    private DemuxResource(Builder builder) {
        super(builder);
        this.mFilterTypes = builder.mFilterTypes;
    }

    public int getFilterTypes() {
        return this.mFilterTypes;
    }

    public String toString() {
        return "DemuxResource[handle=" + this.mHandle + ", filterTypes=" + this.mFilterTypes + ", isInUse=" + this.mIsInUse + ", ownerClientId=" + this.mOwnerClientId + "]";
    }

    public boolean hasSufficientCaps(int i) {
        return i == (this.mFilterTypes & i);
    }

    public int getNumOfCaps() {
        int i = 1;
        int i2 = 0;
        for (int i3 = 0; i3 < 32; i3++) {
            if ((this.mFilterTypes & i) == i) {
                i2++;
            }
            i <<= 1;
        }
        return i2;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Builder extends TunerResourceBasic.Builder {
        private int mFilterTypes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(int i) {
            super(i);
        }

        public Builder filterTypes(int i) {
            this.mFilterTypes = i;
            return this;
        }

        @Override // com.android.server.tv.tunerresourcemanager.TunerResourceBasic.Builder
        public DemuxResource build() {
            return new DemuxResource(this);
        }
    }
}
