package com.android.internal.policy;

import android.content.Context;
import android.text.TextUtils;
import android.view.animation.Animation;
import com.android.internal.policy.ITransitionAnimationExt;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;

/* loaded from: classes.dex */
public class TransitionAnimationExtImpl implements ITransitionAnimationExt {
    private static final String OPLUS_ROUNDED_CORNERS_ANIM_PREFIX = "oplus_rounded_corners_anim_";
    private static final boolean isLightOS = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_WINDOW_ANIM_LIGHT);

    public TransitionAnimationExtImpl(Object base) {
    }

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements ITransitionAnimationExt.IStaticExt {
        private static volatile StaticExtImpl sInstance = null;

        private StaticExtImpl() {
        }

        public static StaticExtImpl getInstance(Object base) {
            synchronized (StaticExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new StaticExtImpl();
                }
            }
            return sInstance;
        }

        public Animation hookLoadAnimationSafely(Context context, String packageName, int resId, Animation animation) {
            return addAnimationRoundedCorners(context, packageName, resId, animation);
        }

        private static Animation addAnimationRoundedCorners(Context context, String packageName, int resId, Animation animation) {
            if (TransitionAnimationExtImpl.isLightOS) {
                return animation;
            }
            String animResName = context.getResources().getResourceEntryName(resId);
            if (!TextUtils.isEmpty(animResName) && animResName.startsWith(TransitionAnimationExtImpl.OPLUS_ROUNDED_CORNERS_ANIM_PREFIX)) {
                animation.setHasRoundedCorners(true);
            }
            return animation;
        }
    }
}
