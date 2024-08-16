package com.android.server.wm.utils;

import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RegionUtils {
    private RegionUtils() {
    }

    public static void rectListToRegion(List<Rect> list, Region region) {
        region.setEmpty();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            region.union(list.get(i));
        }
    }

    public static void forEachRect(Region region, Consumer<Rect> consumer) {
        RegionIterator regionIterator = new RegionIterator(region);
        Rect rect = new Rect();
        while (regionIterator.next(rect)) {
            consumer.accept(rect);
        }
    }

    public static void forEachRectReverse(Region region, Consumer<Rect> consumer) {
        RegionIterator regionIterator = new RegionIterator(region);
        ArrayList arrayList = new ArrayList();
        Rect rect = new Rect();
        while (regionIterator.next(rect)) {
            arrayList.add(new Rect(rect));
        }
        Collections.reverse(arrayList);
        arrayList.forEach(consumer);
    }
}
