package com.android.server.policy;

import android.util.SparseLongArray;
import com.android.server.policy.KeyCombinationManager;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IKeyCombinationManagerWrapper {
    default SparseLongArray getDownTimes() {
        return new SparseLongArray(2);
    }

    default ArrayList<KeyCombinationManager.TwoKeysCombinationRule> getRules() {
        return new ArrayList<>();
    }
}
