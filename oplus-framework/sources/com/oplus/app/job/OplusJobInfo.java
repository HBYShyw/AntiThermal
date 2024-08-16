package com.oplus.app.job;

import android.app.job.IJobInfoExt;
import android.app.job.JobInfo;

/* loaded from: classes.dex */
public class OplusJobInfo {
    public static final int PROTECT_FORE_NET = 1;
    public static final int PROTECT_FORE_FRAME = 0;
    public static final int SCENE_MODE_VIDEO = 1;
    public static final int SCENE_MODE_VIDEO_CALL = 2;
    public static final int SCENE_MODE_GAME = 4;

    private OplusJobInfo() {
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private JobInfo.Builder mBuilder;

        public Builder(JobInfo.Builder builder) {
            this.mBuilder = null;
            if (builder == null) {
                return;
            }
            this.mBuilder = builder;
        }

        public void setRequiresBattIdle(boolean requiresBattIdle) {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return;
            }
            builder.getWrapper().makeBuilderExt().setRequiresBattIdle(requiresBattIdle, 0);
        }

        public boolean getRequiresBattIdle() {
            IJobInfoExt.JobBuilderExt builderExt;
            JobInfo.Builder builder = this.mBuilder;
            return (builder == null || (builderExt = builder.getWrapper().getExtImpl()) == null || !builderExt.mRequiresBattIdle) ? false : true;
        }

        public void setExtraJob(boolean isExtraJob) {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return;
            }
            builder.getWrapper().makeBuilderExt().mIsOplusJob = isExtraJob;
        }

        public boolean isExtraJob() {
            IJobInfoExt.JobBuilderExt builderExt;
            JobInfo.Builder builder = this.mBuilder;
            return (builder == null || (builderExt = builder.getWrapper().getExtImpl()) == null || !builderExt.mIsOplusJob) ? false : true;
        }

        public void setRequiresProtectFore(boolean requiresProtectFore) {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return;
            }
            builder.getWrapper().makeBuilderExt().mRequiresProtectFore = requiresProtectFore;
        }

        public void setRequiresProtectFore(boolean requiresProtectFore, int protectForeType) {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return;
            }
            IJobInfoExt.JobBuilderExt builderExt = builder.getWrapper().makeBuilderExt();
            builderExt.mRequiresProtectFore = requiresProtectFore;
            builderExt.mProtectForeType = protectForeType;
        }

        public boolean getRequiresProtectFore() {
            IJobInfoExt.JobBuilderExt builderExt;
            JobInfo.Builder builder = this.mBuilder;
            return (builder == null || (builderExt = builder.getWrapper().getExtImpl()) == null || !builderExt.mRequiresProtectFore) ? false : true;
        }

        public void setHasCpuConstraint(boolean hasCpuConstraint) {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return;
            }
            builder.getWrapper().makeBuilderExt().mHasCpuConstraint = hasCpuConstraint;
        }

        public boolean getHasCpuConstraint() {
            IJobInfoExt.JobBuilderExt builderExt;
            JobInfo.Builder builder = this.mBuilder;
            return (builder == null || (builderExt = builder.getWrapper().getExtImpl()) == null || !builderExt.mHasCpuConstraint) ? false : true;
        }

        public void setExtraStr(String str) {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return;
            }
            builder.getWrapper().makeBuilderExt().mOplusExtraStr = str;
        }

        public String getExtraStr() {
            IJobInfoExt.JobBuilderExt builderExt;
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null || (builderExt = builder.getWrapper().getExtImpl()) == null) {
                return null;
            }
            return builderExt.mOplusExtraStr;
        }

        public void setHasTemperatureConstraint(boolean hasTemperatureConstraint) {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return;
            }
            builder.getWrapper().makeBuilderExt().mHasTemperatureConstraint = hasTemperatureConstraint;
        }

        public boolean getHasTemperatureConstraint() {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return false;
            }
            IJobInfoExt.JobBuilderExt builderExt = builder.getWrapper().getExtImpl();
            return (builderExt != null ? Boolean.valueOf(builderExt.mHasTemperatureConstraint) : null).booleanValue();
        }

        public void setRequiresProtectScene(boolean requiresProtectScene, int protectScene) {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return;
            }
            IJobInfoExt.JobBuilderExt builderExt = builder.getWrapper().makeBuilderExt();
            builderExt.mHasProtectSceneConstraint = requiresProtectScene;
            builderExt.mProtectScene = protectScene;
        }

        public boolean getRequiresProtectScene() {
            JobInfo.Builder builder = this.mBuilder;
            if (builder == null) {
                return false;
            }
            IJobInfoExt.JobBuilderExt builderExt = builder.getWrapper().getExtImpl();
            return (builderExt != null ? Boolean.valueOf(builderExt.mHasProtectSceneConstraint) : null).booleanValue();
        }
    }
}
