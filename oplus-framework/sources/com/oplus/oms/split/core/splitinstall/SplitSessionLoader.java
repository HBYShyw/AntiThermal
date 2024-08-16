package com.oplus.oms.split.core.splitinstall;

import android.content.Context;
import android.content.Intent;
import java.util.List;

/* loaded from: classes.dex */
public interface SplitSessionLoader {
    void load(Context context, List<Intent> list, SplitSessionStatusChanger<?> splitSessionStatusChanger);
}
