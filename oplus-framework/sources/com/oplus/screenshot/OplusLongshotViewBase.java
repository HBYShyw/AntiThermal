package com.oplus.screenshot;

import android.content.Context;

/* loaded from: classes.dex */
public interface OplusLongshotViewBase {
    boolean canLongScroll();

    int computeLongScrollExtent();

    int computeLongScrollOffset();

    int computeLongScrollRange();

    boolean findViewsLongshotInfo(OplusLongshotViewInfo oplusLongshotViewInfo);

    Context getContext();

    boolean isLongshotVisibleToUser();
}
