package com.oplus.filter;

import android.content.Context;
import android.util.ArraySet;
import android.util.LruCache;
import android.util.Slog;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes.dex */
public class DynamicFilterValueItemsHelper {
    private static final int LRU_DEFAULT_MAX_SIZE = 16;
    private DynamicFilterManager mDynamicFilterManager;
    private String mFilterName;
    private LruCache<String, Set<String>> mTagItemsMap = new LruCache<>(16);
    private static volatile DynamicFilterValueItemsHelper sDynamicFilterValueItemsHelper = null;
    private static final String TAG = DynamicFilterValueItemsHelper.class.getSimpleName();
    private static final Set<String> sEmptySet = new ArraySet(0);

    public static DynamicFilterValueItemsHelper getInstance(Context context, String name) {
        if (sDynamicFilterValueItemsHelper == null) {
            synchronized (DynamicFilterValueItemsHelper.class) {
                if (sDynamicFilterValueItemsHelper == null) {
                    sDynamicFilterValueItemsHelper = new DynamicFilterValueItemsHelper(context, name);
                }
            }
        }
        return sDynamicFilterValueItemsHelper;
    }

    private DynamicFilterValueItemsHelper(Context context, String name) {
        this.mDynamicFilterManager = (DynamicFilterManager) context.getSystemService(DynamicFilterManager.SERVICE_NAME);
        this.mFilterName = name;
    }

    public boolean inFilter(String tag) {
        return this.mDynamicFilterManager.inFilter(this.mFilterName, tag);
    }

    public synchronized boolean containsItem(String tag, String valueItem) {
        if (tag == null) {
            return false;
        }
        Set<String> items = this.mTagItemsMap.get(tag);
        if (items == null) {
            items = getValueItems(tag);
            if (items.isEmpty()) {
                items = sEmptySet;
            }
            this.mTagItemsMap.put(tag, items);
        }
        return items.contains(valueItem);
    }

    private Set<String> getValueItems(String tag) {
        Set<String> items = new ArraySet<>();
        String value = this.mDynamicFilterManager.getFilterTagValue(this.mFilterName, tag);
        if (value != null) {
            try {
                JSONArray jsonArray = new JSONArray(value);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String s = jsonArray.getString(i);
                    if (s != null) {
                        items.add(s);
                    }
                }
            } catch (JSONException e) {
                Slog.e(TAG, "getValueItems: parse value of tag " + tag + " with JSONException!");
            }
        }
        return items;
    }
}
