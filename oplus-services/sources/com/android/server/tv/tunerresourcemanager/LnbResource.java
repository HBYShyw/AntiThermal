package com.android.server.tv.tunerresourcemanager;

import com.android.server.tv.tunerresourcemanager.TunerResourceBasic;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class LnbResource extends TunerResourceBasic {
    private LnbResource(Builder builder) {
        super(builder);
    }

    public String toString() {
        return "LnbResource[handle=" + this.mHandle + ", isInUse=" + this.mIsInUse + ", ownerClientId=" + this.mOwnerClientId + "]";
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Builder extends TunerResourceBasic.Builder {
        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(int i) {
            super(i);
        }

        @Override // com.android.server.tv.tunerresourcemanager.TunerResourceBasic.Builder
        public LnbResource build() {
            return new LnbResource(this);
        }
    }
}
