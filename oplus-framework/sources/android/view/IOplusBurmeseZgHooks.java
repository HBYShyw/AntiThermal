package android.view;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;

/* loaded from: classes.dex */
public interface IOplusBurmeseZgHooks extends IOplusCommonFeature {
    public static final IOplusBurmeseZgHooks DEFAULT = new IOplusBurmeseZgHooks() { // from class: android.view.IOplusBurmeseZgHooks.1
    };

    default IOplusBurmeseZgHooks getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusBurmeseZgHooks;
    }

    default void initBurmeseZgFlag(Context context) {
    }

    default void updateBurmeseZgFlag(Context context) {
    }

    default boolean getZgFlag() {
        return false;
    }

    default void updateBurmeseEncodingForUser(Context context, Configuration config, int userId) {
    }

    default void updateBurmeseConfig(Configuration configuration) {
    }

    default void initBurmeseConfigForUser(ContentResolver resolver, Configuration configuration) {
    }
}
