package androidx.room;

import androidx.lifecycle.LiveData;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: InvalidationLiveDataContainer.java */
/* renamed from: androidx.room.d, reason: use source file name */
/* loaded from: classes.dex */
public class InvalidationLiveDataContainer {

    /* renamed from: a, reason: collision with root package name */
    final Set<LiveData> f3844a = Collections.newSetFromMap(new IdentityHashMap());

    /* renamed from: b, reason: collision with root package name */
    private final RoomDatabase f3845b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InvalidationLiveDataContainer(RoomDatabase roomDatabase) {
        this.f3845b = roomDatabase;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> LiveData<T> a(String[] strArr, boolean z10, Callable<T> callable) {
        return new RoomTrackingLiveData(this.f3845b, this, z10, callable, strArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(LiveData liveData) {
        this.f3844a.add(liveData);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(LiveData liveData) {
        this.f3844a.remove(liveData);
    }
}
