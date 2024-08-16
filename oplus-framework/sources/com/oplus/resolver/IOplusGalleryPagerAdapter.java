package com.oplus.resolver;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.view.View;
import com.oplus.widget.OplusItem;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusGalleryPagerAdapter {
    public static final IOplusGalleryPagerAdapter DEFAULT = new IOplusGalleryPagerAdapter() { // from class: com.oplus.resolver.IOplusGalleryPagerAdapter.1
    };

    default List<OplusItem> loadBitmap(Intent originIntent, List<ResolveInfo> riList, int pagerNumber, int pagerSize, int placeholderCount) {
        return new ArrayList();
    }

    default OplusItem[][] listToArray(List<OplusItem> oplusItems) {
        return (OplusItem[][]) Array.newInstance((Class<?>) OplusItem.class, 0, 0);
    }

    default View createPagerView(List<OplusItem> appinfo, int pagerNumber, int pagerSize) {
        return null;
    }

    default void dismiss() {
    }

    default void setOplusResolverItemEventListener(IOplusResolverGridItemClickListener listener) {
    }

    default void updateUserHandle(UserHandle userHandle) {
    }

    default void setColumnSize(int columnSize) {
    }

    default void unRegister() {
    }
}
