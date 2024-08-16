package com.android.server.people.data;

import android.net.Uri;
import android.os.FileUtils;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.SparseArray;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.cpu.CpuInfoReader$;
import com.android.server.people.data.AbstractProtoDiskReadWriter;
import com.android.server.people.data.EventHistoryImpl;
import com.google.android.collect.Lists;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class EventHistoryImpl implements EventHistory {
    private static final String EVENTS_DIR = "events";
    private static final String INDEXES_DIR = "indexes";
    private static final long MAX_EVENTS_AGE = 14400000;
    private static final long PRUNE_OLD_EVENTS_DELAY = 900000;

    @GuardedBy({"this"})
    private final SparseArray<EventIndex> mEventIndexArray;
    private final EventIndexesProtoDiskReadWriter mEventIndexesProtoDiskReadWriter;
    private final EventsProtoDiskReadWriter mEventsProtoDiskReadWriter;
    private final Injector mInjector;
    private long mLastPruneTime;

    @GuardedBy({"this"})
    private final EventList mRecentEvents;
    private final File mRootDir;
    private final ScheduledExecutorService mScheduledExecutorService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventHistoryImpl(File file, ScheduledExecutorService scheduledExecutorService) {
        this(new Injector(), file, scheduledExecutorService);
    }

    @VisibleForTesting
    EventHistoryImpl(Injector injector, File file, ScheduledExecutorService scheduledExecutorService) {
        this.mEventIndexArray = new SparseArray<>();
        this.mRecentEvents = new EventList();
        this.mInjector = injector;
        this.mScheduledExecutorService = scheduledExecutorService;
        this.mLastPruneTime = injector.currentTimeMillis();
        this.mRootDir = file;
        this.mEventsProtoDiskReadWriter = new EventsProtoDiskReadWriter(new File(file, EVENTS_DIR), scheduledExecutorService);
        this.mEventIndexesProtoDiskReadWriter = new EventIndexesProtoDiskReadWriter(new File(file, INDEXES_DIR), scheduledExecutorService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map<String, EventHistoryImpl> eventHistoriesImplFromDisk(File file, ScheduledExecutorService scheduledExecutorService) {
        return eventHistoriesImplFromDisk(new Injector(), file, scheduledExecutorService);
    }

    @VisibleForTesting
    static Map<String, EventHistoryImpl> eventHistoriesImplFromDisk(Injector injector, File file, ScheduledExecutorService scheduledExecutorService) {
        ArrayMap arrayMap = new ArrayMap();
        File[] listFiles = file.listFiles((FileFilter) new CpuInfoReader$.ExternalSyntheticLambda2());
        if (listFiles == null) {
            return arrayMap;
        }
        for (File file2 : listFiles) {
            File[] listFiles2 = file2.listFiles(new FilenameFilter() { // from class: com.android.server.people.data.EventHistoryImpl$$ExternalSyntheticLambda1
                @Override // java.io.FilenameFilter
                public final boolean accept(File file3, String str) {
                    boolean lambda$eventHistoriesImplFromDisk$0;
                    lambda$eventHistoriesImplFromDisk$0 = EventHistoryImpl.lambda$eventHistoriesImplFromDisk$0(file3, str);
                    return lambda$eventHistoriesImplFromDisk$0;
                }
            });
            if (listFiles2 != null && listFiles2.length == 2) {
                EventHistoryImpl eventHistoryImpl = new EventHistoryImpl(injector, file2, scheduledExecutorService);
                eventHistoryImpl.loadFromDisk();
                arrayMap.put(Uri.decode(file2.getName()), eventHistoryImpl);
            }
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$eventHistoriesImplFromDisk$0(File file, String str) {
        return EVENTS_DIR.equals(str) || INDEXES_DIR.equals(str);
    }

    @VisibleForTesting
    synchronized void loadFromDisk() {
        this.mScheduledExecutorService.execute(new Runnable() { // from class: com.android.server.people.data.EventHistoryImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                EventHistoryImpl.this.lambda$loadFromDisk$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFromDisk$1() {
        synchronized (this) {
            EventList loadRecentEventsFromDisk = this.mEventsProtoDiskReadWriter.loadRecentEventsFromDisk();
            if (loadRecentEventsFromDisk != null) {
                loadRecentEventsFromDisk.removeOldEvents(this.mInjector.currentTimeMillis() - 14400000);
                this.mRecentEvents.addAll(loadRecentEventsFromDisk.getAllEvents());
            }
            SparseArray<EventIndex> loadIndexesFromDisk = this.mEventIndexesProtoDiskReadWriter.loadIndexesFromDisk();
            if (loadIndexesFromDisk != null) {
                for (int i = 0; i < loadIndexesFromDisk.size(); i++) {
                    this.mEventIndexArray.put(loadIndexesFromDisk.keyAt(i), loadIndexesFromDisk.valueAt(i));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void saveToDisk() {
        this.mEventsProtoDiskReadWriter.saveEventsImmediately(this.mRecentEvents);
        this.mEventIndexesProtoDiskReadWriter.saveIndexesImmediately(this.mEventIndexArray);
    }

    @Override // com.android.server.people.data.EventHistory
    public synchronized EventIndex getEventIndex(int i) {
        EventIndex eventIndex;
        eventIndex = this.mEventIndexArray.get(i);
        return eventIndex != null ? new EventIndex(eventIndex) : this.mInjector.createEventIndex();
    }

    @Override // com.android.server.people.data.EventHistory
    public synchronized EventIndex getEventIndex(Set<Integer> set) {
        EventIndex createEventIndex;
        createEventIndex = this.mInjector.createEventIndex();
        Iterator<Integer> it = set.iterator();
        while (it.hasNext()) {
            EventIndex eventIndex = this.mEventIndexArray.get(it.next().intValue());
            if (eventIndex != null) {
                createEventIndex = EventIndex.combine(createEventIndex, eventIndex);
            }
        }
        return createEventIndex;
    }

    @Override // com.android.server.people.data.EventHistory
    public synchronized List<Event> queryEvents(Set<Integer> set, long j, long j2) {
        return this.mRecentEvents.queryEvents(set, j, j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addEvent(Event event) {
        pruneOldEvents();
        addEventInMemory(event);
        this.mEventsProtoDiskReadWriter.scheduleEventsSave(this.mRecentEvents);
        this.mEventIndexesProtoDiskReadWriter.scheduleIndexesSave(this.mEventIndexArray);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onDestroy() {
        this.mEventIndexArray.clear();
        this.mRecentEvents.clear();
        this.mEventsProtoDiskReadWriter.deleteRecentEventsFile();
        this.mEventIndexesProtoDiskReadWriter.deleteIndexesFile();
        FileUtils.deleteContentsAndDir(this.mRootDir);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void pruneOldEvents() {
        long currentTimeMillis = this.mInjector.currentTimeMillis();
        if (currentTimeMillis - this.mLastPruneTime > PRUNE_OLD_EVENTS_DELAY) {
            this.mRecentEvents.removeOldEvents(currentTimeMillis - 14400000);
            this.mLastPruneTime = currentTimeMillis;
        }
    }

    private synchronized void addEventInMemory(Event event) {
        EventIndex eventIndex = this.mEventIndexArray.get(event.getType());
        if (eventIndex == null) {
            eventIndex = this.mInjector.createEventIndex();
            this.mEventIndexArray.put(event.getType(), eventIndex);
        }
        eventIndex.addEvent(event.getTimestamp());
        this.mRecentEvents.add(event);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        Injector() {
        }

        EventIndex createEventIndex() {
            return new EventIndex();
        }

        long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class EventsProtoDiskReadWriter extends AbstractProtoDiskReadWriter<EventList> {
        private static final String RECENT_FILE = "recent";
        private static final String TAG = "EventsProtoDiskReadWriter";

        EventsProtoDiskReadWriter(File file, ScheduledExecutorService scheduledExecutorService) {
            super(file, scheduledExecutorService);
            file.mkdirs();
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        AbstractProtoDiskReadWriter.ProtoStreamWriter<EventList> protoStreamWriter() {
            return new AbstractProtoDiskReadWriter.ProtoStreamWriter() { // from class: com.android.server.people.data.EventHistoryImpl$EventsProtoDiskReadWriter$$ExternalSyntheticLambda1
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter
                public final void write(ProtoOutputStream protoOutputStream, Object obj) {
                    EventHistoryImpl.EventsProtoDiskReadWriter.lambda$protoStreamWriter$0(protoOutputStream, (EventList) obj);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$protoStreamWriter$0(ProtoOutputStream protoOutputStream, EventList eventList) {
            for (Event event : eventList.getAllEvents()) {
                long start = protoOutputStream.start(2246267895809L);
                event.writeToProto(protoOutputStream);
                protoOutputStream.end(start);
            }
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        AbstractProtoDiskReadWriter.ProtoStreamReader<EventList> protoStreamReader() {
            return new AbstractProtoDiskReadWriter.ProtoStreamReader() { // from class: com.android.server.people.data.EventHistoryImpl$EventsProtoDiskReadWriter$$ExternalSyntheticLambda0
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader
                public final Object read(ProtoInputStream protoInputStream) {
                    EventList lambda$protoStreamReader$1;
                    lambda$protoStreamReader$1 = EventHistoryImpl.EventsProtoDiskReadWriter.lambda$protoStreamReader$1(protoInputStream);
                    return lambda$protoStreamReader$1;
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ EventList lambda$protoStreamReader$1(ProtoInputStream protoInputStream) {
            ArrayList newArrayList = Lists.newArrayList();
            while (protoInputStream.nextField() != -1) {
                try {
                    if (protoInputStream.getFieldNumber() == 1) {
                        long start = protoInputStream.start(2246267895809L);
                        Event readFromProto = Event.readFromProto(protoInputStream);
                        protoInputStream.end(start);
                        newArrayList.add(readFromProto);
                    }
                } catch (IOException e) {
                    Slog.e(TAG, "Failed to read protobuf input stream.", e);
                }
            }
            EventList eventList = new EventList();
            eventList.addAll(newArrayList);
            return eventList;
        }

        void scheduleEventsSave(EventList eventList) {
            scheduleSave(RECENT_FILE, eventList);
        }

        void saveEventsImmediately(EventList eventList) {
            saveImmediately(RECENT_FILE, eventList);
        }

        EventList loadRecentEventsFromDisk() {
            return read(RECENT_FILE);
        }

        void deleteRecentEventsFile() {
            delete(RECENT_FILE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class EventIndexesProtoDiskReadWriter extends AbstractProtoDiskReadWriter<SparseArray<EventIndex>> {
        private static final String INDEXES_FILE = "index";
        private static final String TAG = "EventIndexesProtoDiskReadWriter";

        EventIndexesProtoDiskReadWriter(File file, ScheduledExecutorService scheduledExecutorService) {
            super(file, scheduledExecutorService);
            file.mkdirs();
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        AbstractProtoDiskReadWriter.ProtoStreamWriter<SparseArray<EventIndex>> protoStreamWriter() {
            return new AbstractProtoDiskReadWriter.ProtoStreamWriter() { // from class: com.android.server.people.data.EventHistoryImpl$EventIndexesProtoDiskReadWriter$$ExternalSyntheticLambda1
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter
                public final void write(ProtoOutputStream protoOutputStream, Object obj) {
                    EventHistoryImpl.EventIndexesProtoDiskReadWriter.lambda$protoStreamWriter$0(protoOutputStream, (SparseArray) obj);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$protoStreamWriter$0(ProtoOutputStream protoOutputStream, SparseArray sparseArray) {
            for (int i = 0; i < sparseArray.size(); i++) {
                int keyAt = sparseArray.keyAt(i);
                EventIndex eventIndex = (EventIndex) sparseArray.valueAt(i);
                long start = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1120986464257L, keyAt);
                long start2 = protoOutputStream.start(1146756268034L);
                eventIndex.writeToProto(protoOutputStream);
                protoOutputStream.end(start2);
                protoOutputStream.end(start);
            }
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        AbstractProtoDiskReadWriter.ProtoStreamReader<SparseArray<EventIndex>> protoStreamReader() {
            return new AbstractProtoDiskReadWriter.ProtoStreamReader() { // from class: com.android.server.people.data.EventHistoryImpl$EventIndexesProtoDiskReadWriter$$ExternalSyntheticLambda0
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader
                public final Object read(ProtoInputStream protoInputStream) {
                    SparseArray lambda$protoStreamReader$1;
                    lambda$protoStreamReader$1 = EventHistoryImpl.EventIndexesProtoDiskReadWriter.lambda$protoStreamReader$1(protoInputStream);
                    return lambda$protoStreamReader$1;
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ SparseArray lambda$protoStreamReader$1(ProtoInputStream protoInputStream) {
            SparseArray sparseArray = new SparseArray();
            while (protoInputStream.nextField() != -1) {
                try {
                    if (protoInputStream.getFieldNumber() == 1) {
                        long start = protoInputStream.start(2246267895809L);
                        EventIndex eventIndex = EventIndex.EMPTY;
                        int i = 0;
                        while (protoInputStream.nextField() != -1) {
                            int fieldNumber = protoInputStream.getFieldNumber();
                            if (fieldNumber == 1) {
                                i = protoInputStream.readInt(1120986464257L);
                            } else if (fieldNumber == 2) {
                                long start2 = protoInputStream.start(1146756268034L);
                                eventIndex = EventIndex.readFromProto(protoInputStream);
                                protoInputStream.end(start2);
                            } else {
                                Slog.w(TAG, "Could not read undefined field: " + protoInputStream.getFieldNumber());
                            }
                        }
                        sparseArray.append(i, eventIndex);
                        protoInputStream.end(start);
                    }
                } catch (IOException e) {
                    Slog.e(TAG, "Failed to read protobuf input stream.", e);
                }
            }
            return sparseArray;
        }

        void scheduleIndexesSave(SparseArray<EventIndex> sparseArray) {
            scheduleSave(INDEXES_FILE, sparseArray);
        }

        void saveIndexesImmediately(SparseArray<EventIndex> sparseArray) {
            saveImmediately(INDEXES_FILE, sparseArray);
        }

        SparseArray<EventIndex> loadIndexesFromDisk() {
            return read(INDEXES_FILE);
        }

        void deleteIndexesFile() {
            delete(INDEXES_FILE);
        }
    }
}
