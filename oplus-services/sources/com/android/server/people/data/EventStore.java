package com.android.server.people.data;

import android.net.Uri;
import android.util.ArrayMap;
import com.android.internal.annotations.GuardedBy;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class EventStore {
    static final int CATEGORY_CALL = 2;
    static final int CATEGORY_CLASS_BASED = 4;
    static final int CATEGORY_LOCUS_ID_BASED = 1;
    static final int CATEGORY_SHORTCUT_BASED = 0;
    static final int CATEGORY_SMS = 3;

    @GuardedBy({"this"})
    private final List<Map<String, EventHistoryImpl>> mEventHistoryMaps;
    private final List<File> mEventsCategoryDirs;
    private final ScheduledExecutorService mScheduledExecutorService;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface EventCategory {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventStore(File file, ScheduledExecutorService scheduledExecutorService) {
        ArrayList arrayList = new ArrayList();
        this.mEventHistoryMaps = arrayList;
        ArrayList arrayList2 = new ArrayList();
        this.mEventsCategoryDirs = arrayList2;
        arrayList.add(0, new ArrayMap());
        arrayList.add(1, new ArrayMap());
        arrayList.add(2, new ArrayMap());
        arrayList.add(3, new ArrayMap());
        arrayList.add(4, new ArrayMap());
        File file2 = new File(file, "event");
        arrayList2.add(0, new File(file2, "shortcut"));
        arrayList2.add(1, new File(file2, "locus"));
        arrayList2.add(2, new File(file2, "call"));
        arrayList2.add(3, new File(file2, "sms"));
        arrayList2.add(4, new File(file2, "class"));
        this.mScheduledExecutorService = scheduledExecutorService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void loadFromDisk() {
        for (int i = 0; i < this.mEventsCategoryDirs.size(); i++) {
            this.mEventHistoryMaps.get(i).putAll(EventHistoryImpl.eventHistoriesImplFromDisk(this.mEventsCategoryDirs.get(i), this.mScheduledExecutorService));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void saveToDisk() {
        Iterator<Map<String, EventHistoryImpl>> it = this.mEventHistoryMaps.iterator();
        while (it.hasNext()) {
            Iterator<EventHistoryImpl> it2 = it.next().values().iterator();
            while (it2.hasNext()) {
                it2.next().saveToDisk();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized EventHistory getEventHistory(int i, String str) {
        return this.mEventHistoryMaps.get(i).get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized EventHistoryImpl getOrCreateEventHistory(final int i, final String str) {
        return this.mEventHistoryMaps.get(i).computeIfAbsent(str, new Function() { // from class: com.android.server.people.data.EventStore$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                EventHistoryImpl lambda$getOrCreateEventHistory$0;
                lambda$getOrCreateEventHistory$0 = EventStore.this.lambda$getOrCreateEventHistory$0(i, str, (String) obj);
                return lambda$getOrCreateEventHistory$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ EventHistoryImpl lambda$getOrCreateEventHistory$0(int i, String str, String str2) {
        return new EventHistoryImpl(new File(this.mEventsCategoryDirs.get(i), Uri.encode(str)), this.mScheduledExecutorService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void deleteEventHistory(int i, String str) {
        EventHistoryImpl remove = this.mEventHistoryMaps.get(i).remove(str);
        if (remove != null) {
            remove.onDestroy();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void deleteEventHistories(int i) {
        Iterator<EventHistoryImpl> it = this.mEventHistoryMaps.get(i).values().iterator();
        while (it.hasNext()) {
            it.next().onDestroy();
        }
        this.mEventHistoryMaps.get(i).clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void pruneOldEvents() {
        Iterator<Map<String, EventHistoryImpl>> it = this.mEventHistoryMaps.iterator();
        while (it.hasNext()) {
            Iterator<EventHistoryImpl> it2 = it.next().values().iterator();
            while (it2.hasNext()) {
                it2.next().pruneOldEvents();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void pruneOrphanEventHistories(int i, Predicate<String> predicate) {
        Set<String> keySet = this.mEventHistoryMaps.get(i).keySet();
        ArrayList arrayList = new ArrayList();
        for (String str : keySet) {
            if (!predicate.test(str)) {
                arrayList.add(str);
            }
        }
        Map<String, EventHistoryImpl> map = this.mEventHistoryMaps.get(i);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            EventHistoryImpl remove = map.remove((String) it.next());
            if (remove != null) {
                remove.onDestroy();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onDestroy() {
        Iterator<Map<String, EventHistoryImpl>> it = this.mEventHistoryMaps.iterator();
        while (it.hasNext()) {
            Iterator<EventHistoryImpl> it2 = it.next().values().iterator();
            while (it2.hasNext()) {
                it2.next().onDestroy();
            }
        }
    }
}
