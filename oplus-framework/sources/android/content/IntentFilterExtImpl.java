package android.content;

import android.os.Parcel;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class IntentFilterExtImpl implements IIntentFilterExt {
    public static final String BROADCAST_CATEGORY_EXPAND_FLAG = "oplusBrEx@";
    private static final int CATEGORY_ELEMENT_SIZE = 3;
    private static final int CATEGORY_FILTER_ITEM_SIZE = 2;
    private static final String TAG = "IntentFilterExtImpl";
    private IntentFilter mIntentFilter;
    private ArrayMap<String, List<OplusActionFilter>> mOplusActionFilterMap = new ArrayMap<>();
    private int mOriginPriority;

    public IntentFilterExtImpl(Object obj) {
        this.mIntentFilter = (IntentFilter) obj;
    }

    public void copy(IIntentFilterExt intentFilterExt) {
        copyOplusActionFilterMap(intentFilterExt);
        copyOriginPriority(intentFilterExt);
    }

    private void copyOplusActionFilterMap(IIntentFilterExt intentFilterExt) {
        this.mOplusActionFilterMap = intentFilterExt.getOplusActionFilterMap();
    }

    private void copyOriginPriority(IIntentFilterExt intentFilterExt) {
        this.mOriginPriority = intentFilterExt.getOriginPriority();
    }

    public void createFromParcel(Parcel in) {
        createOplusActionFilterMap(in);
        createOriginPriority(in);
    }

    private void createOriginPriority(Parcel in) {
        this.mOriginPriority = in.readInt();
    }

    private void createOplusActionFilterMap(Parcel in) {
        in.readMap(this.mOplusActionFilterMap, ArrayMap.class.getClassLoader());
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeOplusActionFilterMap(dest, flags);
        writeOriginPriority(dest, flags);
    }

    private void writeOriginPriority(Parcel dest, int flags) {
        dest.writeInt(this.mOriginPriority);
    }

    private void writeOplusActionFilterMap(Parcel dest, int flags) {
        dest.writeMap(this.mOplusActionFilterMap);
    }

    public ArrayMap<String, List<OplusActionFilter>> getOplusActionFilterMap() {
        return this.mOplusActionFilterMap;
    }

    public void hookAddCategory(String category) {
        parseCategory(category);
    }

    private void parseCategory(String category) {
        String[] elements;
        if (category == null || !category.startsWith(BROADCAST_CATEGORY_EXPAND_FLAG) || (elements = category.split("@")) == null || elements.length != 3) {
            return;
        }
        String action = elements[1];
        String content = elements[2];
        String[] filters = content.split("\\|");
        for (String filter : filters) {
            String[] items = filter.split("=");
            if (items.length == 2) {
                addActionFilter(action, items[0], items[1]);
            } else {
                Log.d(TAG, "parseCategory: parse error . items length = " + items.length);
                return;
            }
        }
    }

    private void addActionFilter(String action, String filterName, String filterValue) {
        List<OplusActionFilter> actionFilterList = this.mOplusActionFilterMap.get(action);
        if (actionFilterList == null) {
            actionFilterList = new ArrayList();
            this.mOplusActionFilterMap.put(action, actionFilterList);
        }
        actionFilterList.add(new OplusActionFilter(filterName, filterValue));
    }

    public int getOriginPriority() {
        return this.mOriginPriority;
    }

    public void limitPriority() {
        int priority = this.mIntentFilter.getPriority();
        if (priority >= 1000) {
            this.mOriginPriority = priority;
            this.mIntentFilter.setPriority(999);
        }
    }
}
