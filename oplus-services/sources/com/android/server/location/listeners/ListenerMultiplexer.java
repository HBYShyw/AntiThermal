package com.android.server.location.listeners;

import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.listeners.ListenerExecutor;
import com.android.internal.util.Preconditions;
import com.android.server.location.listeners.ListenerRegistration;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class ListenerMultiplexer<TKey, TListener, TRegistration extends ListenerRegistration<TListener>, TMergedRegistration> {

    @GuardedBy({"mMultiplexerLock"})
    private TMergedRegistration mMerged;
    protected final Object mMultiplexerLock = new Object();

    @GuardedBy({"mMultiplexerLock"})
    private final ArrayMap<TKey, TRegistration> mRegistrations = new ArrayMap<>();
    private final ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer mUpdateServiceBuffer = new UpdateServiceBuffer();
    private final ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard mReentrancyGuard = new ReentrancyGuard();

    @GuardedBy({"mMultiplexerLock"})
    private int mActiveRegistrationsCount = 0;

    @GuardedBy({"mMultiplexerLock"})
    private boolean mServiceRegistered = false;

    @GuardedBy({"mMultiplexerLock"})
    protected abstract boolean isActive(TRegistration tregistration);

    @GuardedBy({"mMultiplexerLock"})
    protected abstract TMergedRegistration mergeRegistrations(Collection<TRegistration> collection);

    @GuardedBy({"mMultiplexerLock"})
    protected void onActive() {
    }

    @GuardedBy({"mMultiplexerLock"})
    protected void onInactive() {
    }

    @GuardedBy({"mMultiplexerLock"})
    protected void onRegister() {
    }

    @GuardedBy({"mMultiplexerLock"})
    protected void onRegistrationAdded(TKey tkey, TRegistration tregistration) {
    }

    @GuardedBy({"mMultiplexerLock"})
    protected void onRegistrationRemoved(TKey tkey, TRegistration tregistration) {
    }

    @GuardedBy({"mMultiplexerLock"})
    protected void onUnregister() {
    }

    @GuardedBy({"mMultiplexerLock"})
    protected abstract boolean registerWithService(TMergedRegistration tmergedregistration, Collection<TRegistration> collection);

    @GuardedBy({"mMultiplexerLock"})
    protected abstract void unregisterWithService();

    @GuardedBy({"mMultiplexerLock"})
    protected boolean reregisterWithService(TMergedRegistration tmergedregistration, TMergedRegistration tmergedregistration2, Collection<TRegistration> collection) {
        return registerWithService(tmergedregistration2, collection);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mMultiplexerLock"})
    public void onRegistrationReplaced(TKey tkey, TRegistration tregistration, TKey tkey2, TRegistration tregistration2) {
        onRegistrationRemoved(tkey, tregistration);
        onRegistrationAdded(tkey2, tregistration2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void putRegistration(TKey tkey, TRegistration tregistration) {
        replaceRegistration(tkey, tkey, tregistration);
    }

    protected final void replaceRegistration(TKey tkey, TKey tkey2, TRegistration tregistration) {
        TRegistration tregistration2;
        Objects.requireNonNull(tkey);
        Objects.requireNonNull(tkey2);
        Objects.requireNonNull(tregistration);
        synchronized (this.mMultiplexerLock) {
            boolean z = true;
            Preconditions.checkState(!this.mReentrancyGuard.isReentrant());
            if (tkey != tkey2 && this.mRegistrations.containsKey(tkey2)) {
                z = false;
            }
            Preconditions.checkArgument(z);
            ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer acquire = this.mUpdateServiceBuffer.acquire();
            try {
                ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire2 = this.mReentrancyGuard.acquire();
                try {
                    boolean isEmpty = this.mRegistrations.isEmpty();
                    int indexOfKey = this.mRegistrations.indexOfKey(tkey);
                    if (indexOfKey >= 0) {
                        tregistration2 = this.mRegistrations.valueAt(indexOfKey);
                        unregister(tregistration2);
                        tregistration2.onUnregister();
                        if (tkey != tkey2) {
                            this.mRegistrations.removeAt(indexOfKey);
                        }
                    } else {
                        tregistration2 = null;
                    }
                    if (tkey == tkey2 && indexOfKey >= 0) {
                        this.mRegistrations.setValueAt(indexOfKey, tregistration);
                    } else {
                        this.mRegistrations.put(tkey2, tregistration);
                    }
                    if (isEmpty) {
                        onRegister();
                    }
                    tregistration.onRegister(tkey2);
                    if (tregistration2 == null) {
                        onRegistrationAdded(tkey2, tregistration);
                    } else {
                        onRegistrationReplaced(tkey, tregistration2, tkey2, tregistration);
                    }
                    onRegistrationActiveChanged(tregistration);
                    if (acquire2 != null) {
                        acquire2.close();
                    }
                    if (acquire != null) {
                        acquire.close();
                    }
                } finally {
                }
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void removeRegistrationIf(Predicate<TKey> predicate) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(!this.mReentrancyGuard.isReentrant());
            ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer acquire = this.mUpdateServiceBuffer.acquire();
            try {
                ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire2 = this.mReentrancyGuard.acquire();
                try {
                    int size = this.mRegistrations.size();
                    for (int i = 0; i < size; i++) {
                        TKey keyAt = this.mRegistrations.keyAt(i);
                        if (predicate.test(keyAt)) {
                            removeRegistration(keyAt, this.mRegistrations.valueAt(i));
                        }
                    }
                    if (acquire2 != null) {
                        acquire2.close();
                    }
                    if (acquire != null) {
                        acquire.close();
                    }
                } finally {
                }
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void removeRegistration(TKey tkey) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(!this.mReentrancyGuard.isReentrant());
            int indexOfKey = this.mRegistrations.indexOfKey(tkey);
            if (indexOfKey < 0) {
                return;
            }
            removeRegistration(indexOfKey);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void removeRegistration(TKey tkey, ListenerRegistration<?> listenerRegistration) {
        synchronized (this.mMultiplexerLock) {
            int indexOfKey = this.mRegistrations.indexOfKey(tkey);
            if (indexOfKey < 0) {
                return;
            }
            TRegistration valueAt = this.mRegistrations.valueAt(indexOfKey);
            if (valueAt != listenerRegistration) {
                return;
            }
            if (this.mReentrancyGuard.isReentrant()) {
                unregister(valueAt);
                this.mReentrancyGuard.markForRemoval(tkey, valueAt);
            } else {
                removeRegistration(indexOfKey);
            }
        }
    }

    @GuardedBy({"mMultiplexerLock"})
    private void removeRegistration(int i) {
        TKey keyAt = this.mRegistrations.keyAt(i);
        TRegistration valueAt = this.mRegistrations.valueAt(i);
        ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer acquire = this.mUpdateServiceBuffer.acquire();
        try {
            ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire2 = this.mReentrancyGuard.acquire();
            try {
                unregister(valueAt);
                onRegistrationRemoved(keyAt, valueAt);
                valueAt.onUnregister();
                this.mRegistrations.removeAt(i);
                if (this.mRegistrations.isEmpty()) {
                    onUnregister();
                }
                if (acquire2 != null) {
                    acquire2.close();
                }
                if (acquire != null) {
                    acquire.close();
                }
            } finally {
            }
        } catch (Throwable th) {
            if (acquire != null) {
                try {
                    acquire.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public final void updateService() {
        synchronized (this.mMultiplexerLock) {
            if (this.mUpdateServiceBuffer.isBuffered()) {
                this.mUpdateServiceBuffer.markUpdateServiceRequired();
                return;
            }
            int size = this.mRegistrations.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                TRegistration valueAt = this.mRegistrations.valueAt(i);
                if (valueAt.isActive()) {
                    arrayList.add(valueAt);
                }
            }
            if (arrayList.isEmpty()) {
                if (this.mServiceRegistered) {
                    this.mMerged = null;
                    this.mServiceRegistered = false;
                    unregisterWithService();
                }
            } else {
                TMergedRegistration mergeRegistrations = mergeRegistrations(arrayList);
                if (this.mServiceRegistered) {
                    if (!Objects.equals(mergeRegistrations, this.mMerged)) {
                        boolean reregisterWithService = reregisterWithService(this.mMerged, mergeRegistrations, arrayList);
                        this.mServiceRegistered = reregisterWithService;
                        this.mMerged = reregisterWithService ? mergeRegistrations : null;
                    }
                } else {
                    boolean registerWithService = registerWithService(mergeRegistrations, arrayList);
                    this.mServiceRegistered = registerWithService;
                    this.mMerged = registerWithService ? mergeRegistrations : null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void resetService() {
        synchronized (this.mMultiplexerLock) {
            if (this.mServiceRegistered) {
                this.mMerged = null;
                this.mServiceRegistered = false;
                unregisterWithService();
                updateService();
            }
        }
    }

    public UpdateServiceLock newUpdateServiceLock() {
        return new UpdateServiceLock(this.mUpdateServiceBuffer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean findRegistration(Predicate<TRegistration> predicate) {
        synchronized (this.mMultiplexerLock) {
            ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire = this.mReentrancyGuard.acquire();
            try {
                int size = this.mRegistrations.size();
                for (int i = 0; i < size; i++) {
                    if (predicate.test(this.mRegistrations.valueAt(i))) {
                        if (acquire != null) {
                            acquire.close();
                        }
                        return true;
                    }
                }
                if (acquire != null) {
                    acquire.close();
                }
                return false;
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void updateRegistrations(Predicate<TRegistration> predicate) {
        synchronized (this.mMultiplexerLock) {
            ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer acquire = this.mUpdateServiceBuffer.acquire();
            try {
                ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire2 = this.mReentrancyGuard.acquire();
                try {
                    int size = this.mRegistrations.size();
                    for (int i = 0; i < size; i++) {
                        TRegistration valueAt = this.mRegistrations.valueAt(i);
                        if (predicate.test(valueAt)) {
                            onRegistrationActiveChanged(valueAt);
                        }
                    }
                    if (acquire2 != null) {
                        acquire2.close();
                    }
                    if (acquire != null) {
                        acquire.close();
                    }
                } finally {
                }
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean updateRegistration(Object obj, Predicate<TRegistration> predicate) {
        synchronized (this.mMultiplexerLock) {
            ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer acquire = this.mUpdateServiceBuffer.acquire();
            try {
                ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire2 = this.mReentrancyGuard.acquire();
                try {
                    int indexOfKey = this.mRegistrations.indexOfKey(obj);
                    if (indexOfKey < 0) {
                        if (acquire2 != null) {
                            acquire2.close();
                        }
                        if (acquire != null) {
                            acquire.close();
                        }
                        return false;
                    }
                    TRegistration valueAt = this.mRegistrations.valueAt(indexOfKey);
                    if (predicate.test(valueAt)) {
                        onRegistrationActiveChanged(valueAt);
                    }
                    if (acquire2 != null) {
                        acquire2.close();
                    }
                    if (acquire != null) {
                        acquire.close();
                    }
                    return true;
                } finally {
                }
            } finally {
            }
        }
    }

    @GuardedBy({"mMultiplexerLock"})
    private void onRegistrationActiveChanged(TRegistration tregistration) {
        boolean z = tregistration.isRegistered() && isActive(tregistration);
        if (tregistration.setActive(z)) {
            if (z) {
                int i = this.mActiveRegistrationsCount + 1;
                this.mActiveRegistrationsCount = i;
                if (i == 1) {
                    onActive();
                }
                tregistration.onActive();
            } else {
                tregistration.onInactive();
                int i2 = this.mActiveRegistrationsCount - 1;
                this.mActiveRegistrationsCount = i2;
                if (i2 == 0) {
                    onInactive();
                }
            }
            updateService();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void deliverToListeners(Function<TRegistration, ListenerExecutor.ListenerOperation<TListener>> function) {
        ListenerExecutor.ListenerOperation<TListener> apply;
        synchronized (this.mMultiplexerLock) {
            ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire = this.mReentrancyGuard.acquire();
            try {
                int size = this.mRegistrations.size();
                for (int i = 0; i < size; i++) {
                    TRegistration valueAt = this.mRegistrations.valueAt(i);
                    if (valueAt.isActive() && (apply = function.apply(valueAt)) != null) {
                        valueAt.executeOperation(apply);
                    }
                }
                if (acquire != null) {
                    acquire.close();
                }
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void deliverToListeners(ListenerExecutor.ListenerOperation<TListener> listenerOperation) {
        synchronized (this.mMultiplexerLock) {
            ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire = this.mReentrancyGuard.acquire();
            try {
                int size = this.mRegistrations.size();
                for (int i = 0; i < size; i++) {
                    TRegistration valueAt = this.mRegistrations.valueAt(i);
                    if (valueAt.isActive()) {
                        valueAt.executeOperation(listenerOperation);
                    }
                }
                if (acquire != null) {
                    acquire.close();
                }
            } finally {
            }
        }
    }

    @GuardedBy({"mMultiplexerLock"})
    private void unregister(TRegistration tregistration) {
        tregistration.unregisterInternal();
        onRegistrationActiveChanged(tregistration);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        synchronized (this.mMultiplexerLock) {
            printWriter.print("service: ");
            printWriter.print(getServiceState());
            printWriter.println();
            if (!this.mRegistrations.isEmpty()) {
                printWriter.println("listeners:");
                int size = this.mRegistrations.size();
                for (int i = 0; i < size; i++) {
                    TRegistration valueAt = this.mRegistrations.valueAt(i);
                    printWriter.print("  ");
                    printWriter.print(valueAt);
                    if (!valueAt.isActive()) {
                        printWriter.println(" (inactive)");
                    } else {
                        printWriter.println();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mMultiplexerLock"})
    public String getServiceState() {
        if (!this.mServiceRegistered) {
            return "unregistered";
        }
        TMergedRegistration tmergedregistration = this.mMerged;
        return tmergedregistration != null ? tmergedregistration.toString() : "registered";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ReentrancyGuard implements AutoCloseable {

        @GuardedBy({"mMultiplexerLock"})
        private int mGuardCount = 0;

        @GuardedBy({"mMultiplexerLock"})
        private ArraySet<Map.Entry<TKey, ListenerRegistration<?>>> mScheduledRemovals = null;

        ReentrancyGuard() {
        }

        boolean isReentrant() {
            boolean z;
            synchronized (ListenerMultiplexer.this.mMultiplexerLock) {
                z = this.mGuardCount != 0;
            }
            return z;
        }

        void markForRemoval(TKey tkey, ListenerRegistration<?> listenerRegistration) {
            synchronized (ListenerMultiplexer.this.mMultiplexerLock) {
                Preconditions.checkState(isReentrant());
                if (this.mScheduledRemovals == null) {
                    this.mScheduledRemovals = new ArraySet<>(ListenerMultiplexer.this.mRegistrations.size());
                }
                this.mScheduledRemovals.add(new AbstractMap.SimpleImmutableEntry(tkey, listenerRegistration));
            }
        }

        ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire() {
            synchronized (ListenerMultiplexer.this.mMultiplexerLock) {
                this.mGuardCount++;
            }
            return this;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            synchronized (ListenerMultiplexer.this.mMultiplexerLock) {
                Preconditions.checkState(this.mGuardCount > 0);
                int i = this.mGuardCount - 1;
                this.mGuardCount = i;
                ArraySet<Map.Entry<TKey, ListenerRegistration<?>>> arraySet = null;
                if (i == 0) {
                    ArraySet<Map.Entry<TKey, ListenerRegistration<?>>> arraySet2 = this.mScheduledRemovals;
                    this.mScheduledRemovals = null;
                    arraySet = arraySet2;
                }
                if (arraySet == null) {
                    return;
                }
                ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer acquire = ListenerMultiplexer.this.mUpdateServiceBuffer.acquire();
                try {
                    int size = arraySet.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        Map.Entry<TKey, ListenerRegistration<?>> valueAt = arraySet.valueAt(i2);
                        ListenerMultiplexer.this.removeRegistration(valueAt.getKey(), valueAt.getValue());
                    }
                    if (acquire != null) {
                        acquire.close();
                    }
                } finally {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class UpdateServiceBuffer implements AutoCloseable {

        @GuardedBy({"this"})
        private int mBufferCount = 0;

        @GuardedBy({"this"})
        private boolean mUpdateServiceRequired = false;

        UpdateServiceBuffer() {
        }

        synchronized boolean isBuffered() {
            return this.mBufferCount != 0;
        }

        synchronized void markUpdateServiceRequired() {
            Preconditions.checkState(isBuffered());
            this.mUpdateServiceRequired = true;
        }

        synchronized ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer acquire() {
            this.mBufferCount++;
            return this;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            boolean z;
            synchronized (this) {
                z = false;
                Preconditions.checkState(this.mBufferCount > 0);
                int i = this.mBufferCount - 1;
                this.mBufferCount = i;
                if (i == 0) {
                    boolean z2 = this.mUpdateServiceRequired;
                    this.mUpdateServiceRequired = false;
                    z = z2;
                }
            }
            if (z) {
                ListenerMultiplexer.this.updateService();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class UpdateServiceLock implements AutoCloseable {
        private ListenerMultiplexer<?, ?, ?, ?>.UpdateServiceBuffer mUpdateServiceBuffer;

        UpdateServiceLock(ListenerMultiplexer<?, ?, ?, ?>.UpdateServiceBuffer updateServiceBuffer) {
            this.mUpdateServiceBuffer = updateServiceBuffer.acquire();
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            ListenerMultiplexer<?, ?, ?, ?>.UpdateServiceBuffer updateServiceBuffer = this.mUpdateServiceBuffer;
            if (updateServiceBuffer != null) {
                this.mUpdateServiceBuffer = null;
                updateServiceBuffer.close();
            }
        }
    }
}
