package com.android.server.pm;

import android.content.pm.ResolveInfo;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class QueryIntentActivitiesResult {
    public boolean addInstant;
    public List<ResolveInfo> answer;
    public List<ResolveInfo> result;
    public boolean sortResult;

    /* JADX INFO: Access modifiers changed from: package-private */
    public QueryIntentActivitiesResult(List<ResolveInfo> list) {
        this.sortResult = false;
        this.addInstant = false;
        this.result = null;
        this.answer = list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public QueryIntentActivitiesResult(boolean z, boolean z2, List<ResolveInfo> list) {
        this.answer = null;
        this.sortResult = z;
        this.addInstant = z2;
        this.result = list;
    }
}
