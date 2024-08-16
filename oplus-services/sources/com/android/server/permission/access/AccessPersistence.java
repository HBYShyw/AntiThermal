package com.android.server.permission.access;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.AtomicFile;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseLongArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import com.android.modules.utils.BinaryXmlPullParser;
import com.android.modules.utils.BinaryXmlSerializer;
import com.android.server.permission.access.collection.IntSet;
import com.android.server.permission.access.util.PermissionApex;
import com.android.server.permission.jarjar.kotlin.Unit;
import com.android.server.permission.jarjar.kotlin.io.Closeable;
import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Ref$ObjectRef;
import com.android.server.permission.jarjar.kotlin.ranges._Ranges;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.jetbrains.annotations.NotNull;

/* compiled from: AccessPersistence.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AccessPersistence {

    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = AccessPersistence.class.getSimpleName();

    @NotNull
    private final AccessPolicy policy;

    @GuardedBy({"scheduleLock"})
    private WriteHandler writeHandler;

    @NotNull
    private final Object scheduleLock = new Object();

    @GuardedBy({"scheduleLock"})
    @NotNull
    private final SparseLongArray pendingMutationTimesMillis = new SparseLongArray();

    @GuardedBy({"scheduleLock"})
    @NotNull
    private final SparseArray<AccessState> pendingStates = new SparseArray<>();

    @NotNull
    private final Object writeLock = new Object();

    public AccessPersistence(@NotNull AccessPolicy accessPolicy) {
        this.policy = accessPolicy;
    }

    public final void initialize() {
        this.writeHandler = new WriteHandler(BackgroundThread.getHandler().getLooper());
    }

    public final void read(@NotNull AccessState accessState) {
        readSystemState(accessState);
        IntSet userIds = accessState.getSystemState().getUserIds();
        int size = userIds.getSize();
        for (int i = 0; i < size; i++) {
            readUserState(accessState, userIds.elementAt(i));
        }
    }

    private final void readSystemState(AccessState accessState) {
        File systemFile = getSystemFile();
        try {
            FileInputStream openRead = new AtomicFile(systemFile).openRead();
            try {
                BinaryXmlPullParser binaryXmlPullParser = new BinaryXmlPullParser();
                binaryXmlPullParser.setInput(openRead, (String) null);
                this.policy.parseSystemState(binaryXmlPullParser, accessState);
                Unit unit = Unit.INSTANCE;
                Closeable.closeFinally(openRead, null);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    Closeable.closeFinally(openRead, th);
                    throw th2;
                }
            }
        } catch (FileNotFoundException unused) {
            Log.i(LOG_TAG, systemFile + " not found");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read " + systemFile, e);
        }
    }

    private final void readUserState(AccessState accessState, int i) {
        File userFile = getUserFile(i);
        try {
            FileInputStream openRead = new AtomicFile(userFile).openRead();
            try {
                BinaryXmlPullParser binaryXmlPullParser = new BinaryXmlPullParser();
                binaryXmlPullParser.setInput(openRead, (String) null);
                this.policy.parseUserState(binaryXmlPullParser, accessState, i);
                Unit unit = Unit.INSTANCE;
                Closeable.closeFinally(openRead, null);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    Closeable.closeFinally(openRead, th);
                    throw th2;
                }
            }
        } catch (FileNotFoundException unused) {
            Log.i(LOG_TAG, userFile + " not found");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read " + userFile, e);
        }
    }

    public final void write(@NotNull AccessState accessState) {
        write(accessState.getSystemState(), accessState, -1);
        SparseArray<UserState> userStates = accessState.getUserStates();
        int size = userStates.size();
        for (int i = 0; i < size; i++) {
            write(userStates.valueAt(i), accessState, userStates.keyAt(i));
        }
    }

    private final void write(WritableState writableState, AccessState accessState, int i) {
        long j;
        long coerceAtMost;
        int writeMode = writableState.getWriteMode();
        if (writeMode != 0) {
            if (writeMode == 1) {
                synchronized (this.scheduleLock) {
                    this.pendingStates.set(i, accessState);
                    Unit unit = Unit.INSTANCE;
                }
                writePendingState(i);
                return;
            }
            if (writeMode == 2) {
                synchronized (this.scheduleLock) {
                    WriteHandler writeHandler = this.writeHandler;
                    WriteHandler writeHandler2 = null;
                    if (writeHandler == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("writeHandler");
                        writeHandler = null;
                    }
                    writeHandler.removeMessages(i);
                    this.pendingStates.set(i, accessState);
                    long uptimeMillis = SystemClock.uptimeMillis();
                    SparseLongArray sparseLongArray = this.pendingMutationTimesMillis;
                    int indexOfKey = sparseLongArray.indexOfKey(i);
                    if (indexOfKey >= 0) {
                        j = sparseLongArray.valueAt(indexOfKey);
                    } else {
                        sparseLongArray.put(i, uptimeMillis);
                        j = uptimeMillis;
                    }
                    long j2 = uptimeMillis - j;
                    WriteHandler writeHandler3 = this.writeHandler;
                    if (writeHandler3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("writeHandler");
                        writeHandler3 = null;
                    }
                    Message obtainMessage = writeHandler3.obtainMessage(i);
                    if (j2 > 2000) {
                        obtainMessage.sendToTarget();
                        Unit unit2 = Unit.INSTANCE;
                    } else {
                        coerceAtMost = _Ranges.coerceAtMost(1000L, 2000 - j2);
                        WriteHandler writeHandler4 = this.writeHandler;
                        if (writeHandler4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("writeHandler");
                        } else {
                            writeHandler2 = writeHandler4;
                        }
                        writeHandler2.sendMessageDelayed(obtainMessage, coerceAtMost);
                    }
                }
                return;
            }
            throw new IllegalStateException(Integer.valueOf(writeMode).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v2, types: [T, java.lang.Object] */
    public final void writePendingState(int i) {
        synchronized (this.writeLock) {
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            synchronized (this.scheduleLock) {
                this.pendingMutationTimesMillis.delete(i);
                ref$ObjectRef.element = this.pendingStates.removeReturnOld(i);
                WriteHandler writeHandler = this.writeHandler;
                if (writeHandler == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("writeHandler");
                    writeHandler = null;
                }
                writeHandler.removeMessages(i);
                Unit unit = Unit.INSTANCE;
            }
            T t = ref$ObjectRef.element;
            if (t == 0) {
                return;
            }
            if (i == -1) {
                writeSystemState((AccessState) t);
            } else {
                writeUserState((AccessState) t, i);
            }
        }
    }

    private final void writeSystemState(AccessState accessState) {
        File systemFile = getSystemFile();
        try {
            AtomicFile atomicFile = new AtomicFile(systemFile);
            FileOutputStream startWrite = atomicFile.startWrite();
            try {
                try {
                    BinaryXmlSerializer binaryXmlSerializer = new BinaryXmlSerializer();
                    binaryXmlSerializer.setOutput(startWrite, (String) null);
                    binaryXmlSerializer.startDocument((String) null, Boolean.TRUE);
                    this.policy.serializeSystemState(binaryXmlSerializer, accessState);
                    binaryXmlSerializer.endDocument();
                    atomicFile.finishWrite(startWrite);
                    Unit unit = Unit.INSTANCE;
                    Closeable.closeFinally(startWrite, null);
                } catch (Throwable th) {
                    atomicFile.failWrite(startWrite);
                    throw th;
                }
            } finally {
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to serialize " + systemFile, e);
        }
    }

    private final void writeUserState(AccessState accessState, int i) {
        File userFile = getUserFile(i);
        try {
            AtomicFile atomicFile = new AtomicFile(userFile);
            FileOutputStream startWrite = atomicFile.startWrite();
            try {
                try {
                    BinaryXmlSerializer binaryXmlSerializer = new BinaryXmlSerializer();
                    binaryXmlSerializer.setOutput(startWrite, (String) null);
                    binaryXmlSerializer.startDocument((String) null, Boolean.TRUE);
                    this.policy.serializeUserState(binaryXmlSerializer, accessState, i);
                    binaryXmlSerializer.endDocument();
                    atomicFile.finishWrite(startWrite);
                    Unit unit = Unit.INSTANCE;
                    Closeable.closeFinally(startWrite, null);
                } catch (Throwable th) {
                    atomicFile.failWrite(startWrite);
                    throw th;
                }
            } finally {
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to serialize " + userFile, e);
        }
    }

    private final File getSystemFile() {
        return new File(PermissionApex.INSTANCE.getSystemDataDirectory(), "access.abx");
    }

    private final File getUserFile(int i) {
        return new File(PermissionApex.INSTANCE.getUserDataDirectory(i), "access.abx");
    }

    /* compiled from: AccessPersistence.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AccessPersistence.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class WriteHandler extends Handler {
        public WriteHandler(@NotNull Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(@NotNull Message message) {
            AccessPersistence.this.writePendingState(message.what);
        }
    }
}
