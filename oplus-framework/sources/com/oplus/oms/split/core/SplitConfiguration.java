package com.oplus.oms.split.core;

import com.oplus.oms.split.splitdownload.Downloader;
import com.oplus.oms.split.splitdownload.IProvider;
import com.oplus.oms.split.splitdownload.ISplitUpdateManager;
import java.util.List;

/* loaded from: classes.dex */
public class SplitConfiguration {
    private final Class<? extends ObtainUserConfirmationDialog> mConfirmActivityName;
    private final IProvider mCustomProvider;
    private final Downloader mDownloader;
    private final boolean mLocalFirst;
    private final int mNetWorkingType;
    private final boolean mQueryStartUp;
    private final int mSplitLoadMode;
    private final ISplitUpdateManager mUpdateManager;
    private final int mUpdateTimeByHours;
    private final List<String> mWorkProcesses;

    private SplitConfiguration(Builder builder) {
        this.mSplitLoadMode = builder.mSplitLoadMode;
        this.mNetWorkingType = builder.mNetWorkingType;
        this.mUpdateTimeByHours = builder.mUpdateTimeByHours;
        this.mDownloader = builder.mDownloader;
        this.mUpdateManager = builder.mUpdateManager;
        this.mConfirmActivityName = builder.mConfirmActivityName;
        this.mQueryStartUp = builder.mQueryStartUp;
        this.mLocalFirst = builder.mLocalFirst;
        this.mCustomProvider = builder.mCustomProvider;
        this.mWorkProcesses = builder.mWorkProcesses;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getSplitLoadMode() {
        return this.mSplitLoadMode;
    }

    public int getNetWorkingType() {
        return this.mNetWorkingType;
    }

    public int getUpdateTimeByHours() {
        return this.mUpdateTimeByHours;
    }

    public Downloader getDownloader() {
        return this.mDownloader;
    }

    public ISplitUpdateManager getUpdateManager() {
        return this.mUpdateManager;
    }

    public Class<? extends ObtainUserConfirmationDialog> getConfirmActivityName() {
        return this.mConfirmActivityName;
    }

    public boolean getQueryStartUp() {
        return this.mQueryStartUp;
    }

    public boolean getLocalFirst() {
        return this.mLocalFirst;
    }

    public IProvider getCustomProvider() {
        return this.mCustomProvider;
    }

    public List<String> getWorkProcesses() {
        return this.mWorkProcesses;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private Class<? extends ObtainUserConfirmationDialog> mConfirmActivityName;
        private IProvider mCustomProvider;
        private Downloader mDownloader;
        private boolean mLocalFirst;
        private int mNetWorkingType;
        private boolean mQueryStartUp;
        private int mSplitLoadMode;
        private ISplitUpdateManager mUpdateManager;
        private int mUpdateTimeByHours;
        private List<String> mWorkProcesses;

        private Builder() {
            this.mSplitLoadMode = 1;
            this.mNetWorkingType = 3;
            this.mUpdateTimeByHours = 72;
            this.mQueryStartUp = true;
            this.mLocalFirst = false;
            this.mConfirmActivityName = ObtainUserConfirmationDialog.class;
        }

        public Builder splitLoadMode(int splitLoadMode) {
            this.mSplitLoadMode = splitLoadMode;
            return this;
        }

        public Builder netWorkingType(int netWorkingType) {
            this.mNetWorkingType = netWorkingType;
            return this;
        }

        public Builder updateTimeByHours(int updateTimeByHours) {
            this.mUpdateTimeByHours = updateTimeByHours;
            return this;
        }

        public Builder downloader(Downloader downloader) {
            this.mDownloader = downloader;
            return this;
        }

        public Builder updateManager(ISplitUpdateManager updateManager) {
            this.mUpdateManager = updateManager;
            return this;
        }

        public Builder obtainUserConfirmationClass(Class<? extends ObtainUserConfirmationDialog> confirmActivityName) {
            this.mConfirmActivityName = confirmActivityName;
            return this;
        }

        public Builder queryStartUp(boolean queryStartUp) {
            this.mQueryStartUp = queryStartUp;
            return this;
        }

        public Builder localFirst(boolean isLocalFirst) {
            this.mLocalFirst = isLocalFirst;
            return this;
        }

        public Builder customProvider(IProvider iProvider) {
            this.mCustomProvider = iProvider;
            return this;
        }

        public Builder workProcesses(List<String> workProcesses) {
            if (workProcesses != null && workProcesses.size() > 0) {
                this.mWorkProcesses = workProcesses;
            }
            return this;
        }

        public SplitConfiguration build() {
            return new SplitConfiguration(this);
        }
    }
}
