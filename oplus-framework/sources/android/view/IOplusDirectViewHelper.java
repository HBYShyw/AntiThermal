package android.view;

import android.common.OplusFeatureList;
import android.os.Parcel;

/* loaded from: classes.dex */
public interface IOplusDirectViewHelper extends IOplusDirectWindow {
    public static final IOplusDirectViewHelper DEFAULT = new IOplusDirectViewHelper() { // from class: android.view.IOplusDirectViewHelper.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDirectViewHelper;
    }

    default IOplusDirectViewHelper getDefault() {
        return DEFAULT;
    }

    default boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
        return false;
    }
}
