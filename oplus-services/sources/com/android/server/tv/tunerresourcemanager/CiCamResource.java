package com.android.server.tv.tunerresourcemanager;

import com.android.server.tv.tunerresourcemanager.CasResource;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class CiCamResource extends CasResource {
    private CiCamResource(Builder builder) {
        super(builder);
    }

    @Override // com.android.server.tv.tunerresourcemanager.CasResource
    public String toString() {
        return "CiCamResource[systemId=" + getSystemId() + ", isFullyUsed=" + isFullyUsed() + ", maxSessionNum=" + getMaxSessionNum() + ", ownerClients=" + ownersMapToString() + "]";
    }

    public int getCiCamId() {
        return getSystemId();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Builder extends CasResource.Builder {
        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(int i) {
            super(i);
        }

        @Override // com.android.server.tv.tunerresourcemanager.CasResource.Builder
        public Builder maxSessionNum(int i) {
            this.mMaxSessionNum = i;
            return this;
        }

        @Override // com.android.server.tv.tunerresourcemanager.CasResource.Builder
        public CiCamResource build() {
            return new CiCamResource(this);
        }
    }
}
