package com.android.server.location.common;

import com.android.server.location.common.OplusLbsFeatureList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusCommonFactory {
    boolean isValid(int i);

    default <T extends IOplusCommonFeature> T getFeature(T t, Object... objArr) {
        verityParams(t);
        return t;
    }

    default <T extends IOplusCommonFeature> void verityParams(T t) {
        if (t == null) {
            throw new IllegalArgumentException("def can not be null");
        }
        if (OplusLbsFeatureList.OplusIndex.End != t.index()) {
            return;
        }
        throw new IllegalArgumentException(t + "must override index() method");
    }

    default void verityParamsType(String str, Object[] objArr, int i, Class... clsArr) {
        if (objArr == null || clsArr == null || objArr.length != i || clsArr.length != i) {
            throw new IllegalArgumentException(str + " need +" + i + " params");
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (!clsArr[i2].isInstance(objArr[i2])) {
                throw new IllegalArgumentException(clsArr[i2].getName() + " is not instance " + objArr[i2]);
            }
        }
    }
}
