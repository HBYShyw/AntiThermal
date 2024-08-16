package com.android.server.wm;

import android.annotation.SystemApi;
import android.app.ActivityOptions;
import android.app.TaskInfo;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ActivityInterceptorCallback {
    public static final int DREAM_MANAGER_ORDERED_ID = 4;
    public static final int MAINLINE_FIRST_ORDERED_ID = 1000;
    public static final int MAINLINE_LAST_ORDERED_ID = 1001;
    public static final int MAINLINE_SDK_SANDBOX_ORDER_ID = 1001;
    public static final int PERMISSION_POLICY_ORDERED_ID = 1;
    public static final int PRODUCT_ORDERED_ID = 5;
    public static final int SYSTEM_FIRST_ORDERED_ID = 0;
    public static final int SYSTEM_LAST_ORDERED_ID = 5;
    public static final int VIRTUAL_DEVICE_SERVICE_ORDERED_ID = 3;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public @interface OrderedId {
    }

    static boolean isValidMainlineOrderId(int i) {
        return i >= 1000 && i <= 1001;
    }

    default void onActivityLaunched(TaskInfo taskInfo, ActivityInfo activityInfo, ActivityInterceptorInfo activityInterceptorInfo) {
    }

    ActivityInterceptResult onInterceptActivityLaunch(ActivityInterceptorInfo activityInterceptorInfo);

    static boolean isValidOrderId(int i) {
        return isValidMainlineOrderId(i) || (i >= 0 && i <= 5);
    }

    @SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class ActivityInterceptorInfo {
        private final ActivityInfo mActivityInfo;
        private final String mCallingFeatureId;
        private final String mCallingPackage;
        private final int mCallingPid;
        private final int mCallingUid;
        private final ActivityOptions mCheckedOptions;
        private final Runnable mClearOptionsAnimation;
        private final Intent mIntent;
        private final int mRealCallingPid;
        private final int mRealCallingUid;
        private final ResolveInfo mResolveInfo;
        private final String mResolvedType;
        private final int mUserId;

        public ActivityInterceptorInfo(Builder builder) {
            this.mCallingUid = builder.mCallingUid;
            this.mCallingPid = builder.mCallingPid;
            this.mRealCallingUid = builder.mRealCallingUid;
            this.mRealCallingPid = builder.mRealCallingPid;
            this.mUserId = builder.mUserId;
            this.mIntent = builder.mIntent;
            this.mResolveInfo = builder.mResolveInfo;
            this.mActivityInfo = builder.mActivityInfo;
            this.mResolvedType = builder.mResolvedType;
            this.mCallingPackage = builder.mCallingPackage;
            this.mCallingFeatureId = builder.mCallingFeatureId;
            this.mCheckedOptions = builder.mCheckedOptions;
            this.mClearOptionsAnimation = builder.mClearOptionsAnimation;
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static final class Builder {
            private final ActivityInfo mActivityInfo;
            private final int mCallingPid;
            private final int mCallingUid;
            private final Intent mIntent;
            private final int mRealCallingPid;
            private final int mRealCallingUid;
            private final ResolveInfo mResolveInfo;
            private String mResolvedType;
            private final int mUserId;
            private String mCallingPackage = null;
            private String mCallingFeatureId = null;
            private ActivityOptions mCheckedOptions = null;
            private Runnable mClearOptionsAnimation = null;

            public Builder(int i, int i2, int i3, int i4, int i5, Intent intent, ResolveInfo resolveInfo, ActivityInfo activityInfo) {
                this.mCallingUid = i;
                this.mCallingPid = i2;
                this.mRealCallingUid = i3;
                this.mRealCallingPid = i4;
                this.mUserId = i5;
                this.mIntent = intent;
                this.mResolveInfo = resolveInfo;
                this.mActivityInfo = activityInfo;
            }

            public ActivityInterceptorInfo build() {
                return new ActivityInterceptorInfo(this);
            }

            public Builder setResolvedType(String str) {
                this.mResolvedType = str;
                return this;
            }

            public Builder setCallingPackage(String str) {
                this.mCallingPackage = str;
                return this;
            }

            public Builder setCallingFeatureId(String str) {
                this.mCallingFeatureId = str;
                return this;
            }

            public Builder setCheckedOptions(ActivityOptions activityOptions) {
                this.mCheckedOptions = activityOptions;
                return this;
            }

            public Builder setClearOptionsAnimationRunnable(Runnable runnable) {
                this.mClearOptionsAnimation = runnable;
                return this;
            }
        }

        public int getCallingUid() {
            return this.mCallingUid;
        }

        public int getCallingPid() {
            return this.mCallingPid;
        }

        public int getRealCallingUid() {
            return this.mRealCallingUid;
        }

        public int getRealCallingPid() {
            return this.mRealCallingPid;
        }

        public int getUserId() {
            return this.mUserId;
        }

        public Intent getIntent() {
            return this.mIntent;
        }

        public ResolveInfo getResolveInfo() {
            return this.mResolveInfo;
        }

        public ActivityInfo getActivityInfo() {
            return this.mActivityInfo;
        }

        public String getResolvedType() {
            return this.mResolvedType;
        }

        public String getCallingPackage() {
            return this.mCallingPackage;
        }

        public String getCallingFeatureId() {
            return this.mCallingFeatureId;
        }

        public ActivityOptions getCheckedOptions() {
            return this.mCheckedOptions;
        }

        public Runnable getClearOptionsAnimationRunnable() {
            return this.mClearOptionsAnimation;
        }
    }

    @SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class ActivityInterceptResult {
        private final ActivityOptions mActivityOptions;
        private final boolean mActivityResolved;
        private final Intent mIntent;

        public ActivityInterceptResult(Intent intent, ActivityOptions activityOptions) {
            this(intent, activityOptions, false);
        }

        public ActivityInterceptResult(Intent intent, ActivityOptions activityOptions, boolean z) {
            this.mIntent = intent;
            this.mActivityOptions = activityOptions;
            this.mActivityResolved = z;
        }

        public Intent getIntent() {
            return this.mIntent;
        }

        public ActivityOptions getActivityOptions() {
            return this.mActivityOptions;
        }

        public boolean isActivityResolved() {
            return this.mActivityResolved;
        }
    }
}
