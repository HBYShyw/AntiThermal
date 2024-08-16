package com.android.server.tv.tunerresourcemanager;

import java.util.HashSet;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ClientProfile {
    public static final int INVALID_GROUP_ID = -1;
    public static final int INVALID_RESOURCE_ID = -1;
    private int mGroupId;
    private final int mId;
    private boolean mIsPriorityOverwritten;
    private int mNiceValue;
    private int mPrimaryUsingFrontendHandle;
    private int mPriority;
    private final int mProcessId;
    private Set<Integer> mShareFeClientIds;
    private final String mTvInputSessionId;
    private final int mUseCase;
    private int mUsingCasSystemId;
    private int mUsingCiCamId;
    private Set<Integer> mUsingDemuxHandles;
    private Set<Integer> mUsingFrontendHandles;
    private Set<Integer> mUsingLnbHandles;

    private ClientProfile(Builder builder) {
        this.mGroupId = -1;
        this.mPrimaryUsingFrontendHandle = -1;
        this.mUsingFrontendHandles = new HashSet();
        this.mShareFeClientIds = new HashSet();
        this.mUsingDemuxHandles = new HashSet();
        this.mUsingLnbHandles = new HashSet();
        this.mUsingCasSystemId = -1;
        this.mUsingCiCamId = -1;
        this.mIsPriorityOverwritten = false;
        this.mId = builder.mId;
        this.mTvInputSessionId = builder.mTvInputSessionId;
        this.mUseCase = builder.mUseCase;
        this.mProcessId = builder.mProcessId;
    }

    public int getId() {
        return this.mId;
    }

    public String getTvInputSessionId() {
        return this.mTvInputSessionId;
    }

    public int getUseCase() {
        return this.mUseCase;
    }

    public int getProcessId() {
        return this.mProcessId;
    }

    public boolean isPriorityOverwritten() {
        return this.mIsPriorityOverwritten;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public int getPriority() {
        return this.mPriority - this.mNiceValue;
    }

    public void setGroupId(int i) {
        this.mGroupId = i;
    }

    public void setPriority(int i) {
        if (i < 0) {
            return;
        }
        this.mPriority = i;
    }

    public void overwritePriority(int i) {
        if (i < 0) {
            return;
        }
        this.mIsPriorityOverwritten = true;
        this.mPriority = i;
    }

    public void setNiceValue(int i) {
        this.mNiceValue = i;
    }

    public void useFrontend(int i) {
        this.mUsingFrontendHandles.add(Integer.valueOf(i));
    }

    public void setPrimaryFrontend(int i) {
        this.mPrimaryUsingFrontendHandle = i;
    }

    public int getPrimaryFrontend() {
        return this.mPrimaryUsingFrontendHandle;
    }

    public void shareFrontend(int i) {
        this.mShareFeClientIds.add(Integer.valueOf(i));
    }

    public void stopSharingFrontend(int i) {
        this.mShareFeClientIds.remove(Integer.valueOf(i));
    }

    public Set<Integer> getInUseFrontendHandles() {
        return this.mUsingFrontendHandles;
    }

    public Set<Integer> getShareFeClientIds() {
        return this.mShareFeClientIds;
    }

    public void releaseFrontend() {
        this.mUsingFrontendHandles.clear();
        this.mShareFeClientIds.clear();
        this.mPrimaryUsingFrontendHandle = -1;
    }

    public void useDemux(int i) {
        this.mUsingDemuxHandles.add(Integer.valueOf(i));
    }

    public Set<Integer> getInUseDemuxHandles() {
        return this.mUsingDemuxHandles;
    }

    public void releaseDemux(int i) {
        this.mUsingDemuxHandles.remove(Integer.valueOf(i));
    }

    public void useLnb(int i) {
        this.mUsingLnbHandles.add(Integer.valueOf(i));
    }

    public Set<Integer> getInUseLnbHandles() {
        return this.mUsingLnbHandles;
    }

    public void releaseLnb(int i) {
        this.mUsingLnbHandles.remove(Integer.valueOf(i));
    }

    public void useCas(int i) {
        this.mUsingCasSystemId = i;
    }

    public int getInUseCasSystemId() {
        return this.mUsingCasSystemId;
    }

    public void releaseCas() {
        this.mUsingCasSystemId = -1;
    }

    public void useCiCam(int i) {
        this.mUsingCiCamId = i;
    }

    public int getInUseCiCamId() {
        return this.mUsingCiCamId;
    }

    public void releaseCiCam() {
        this.mUsingCiCamId = -1;
    }

    public void reclaimAllResources() {
        this.mUsingFrontendHandles.clear();
        this.mShareFeClientIds.clear();
        this.mPrimaryUsingFrontendHandle = -1;
        this.mUsingLnbHandles.clear();
        this.mUsingCasSystemId = -1;
        this.mUsingCiCamId = -1;
    }

    public String toString() {
        return "ClientProfile[id=" + this.mId + ", tvInputSessionId=" + this.mTvInputSessionId + ", useCase=" + this.mUseCase + ", processId=" + this.mProcessId + "]";
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Builder {
        private final int mId;
        private int mProcessId;
        private String mTvInputSessionId;
        private int mUseCase;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(int i) {
            this.mId = i;
        }

        public Builder useCase(int i) {
            this.mUseCase = i;
            return this;
        }

        public Builder tvInputSessionId(String str) {
            this.mTvInputSessionId = str;
            return this;
        }

        public Builder processId(int i) {
            this.mProcessId = i;
            return this;
        }

        public ClientProfile build() {
            return new ClientProfile(this);
        }
    }
}
