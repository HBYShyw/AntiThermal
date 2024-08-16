package com.android.server.wm;

import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.TraceBuffer;
import com.android.server.wm.Transition;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TransitionTracer {
    private static final int ACTIVE_TRACING_BUFFER_CAPACITY = 5120000;
    private static final int ALWAYS_ON_TRACING_CAPACITY = 15360;
    private static final int CHUNK_SIZE = 64;
    private static final String LOG_TAG = "TransitionTracer";
    private static final long MAGIC_NUMBER_VALUE = 4990904633914184276L;
    private static final String TRACE_FILE = "/data/misc/wmtrace/wm_transition_trace.winscope";
    static final String WINSCOPE_EXT = ".winscope";
    private final TraceBuffer mTraceBuffer = new TraceBuffer(ALWAYS_ON_TRACING_CAPACITY);
    private final Object mEnabledLock = new Object();
    private volatile boolean mActiveTracingEnabled = false;

    public void logSentTransition(Transition transition, ArrayList<Transition.ChangeInfo> arrayList) {
        try {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream(64);
            long start = protoOutputStream.start(2246267895810L);
            protoOutputStream.write(1120986464257L, transition.getSyncId());
            protoOutputStream.write(1112396529668L, transition.mLogger.mCreateTimeNs);
            protoOutputStream.write(1112396529669L, transition.mLogger.mSendTimeNs);
            protoOutputStream.write(1116691496962L, transition.getStartTransaction().getId());
            protoOutputStream.write(1116691496963L, transition.getFinishTransaction().getId());
            dumpTransitionTargetsToProto(protoOutputStream, transition, arrayList);
            protoOutputStream.end(start);
            this.mTraceBuffer.add(protoOutputStream);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unexpected exception thrown while logging transitions", e);
        }
    }

    public void logFinishedTransition(Transition transition) {
        try {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream(64);
            long start = protoOutputStream.start(2246267895810L);
            protoOutputStream.write(1120986464257L, transition.getSyncId());
            protoOutputStream.write(1112396529670L, transition.mLogger.mFinishTimeNs);
            protoOutputStream.end(start);
            this.mTraceBuffer.add(protoOutputStream);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unexpected exception thrown while logging transitions", e);
        }
    }

    public void logAbortedTransition(Transition transition) {
        try {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream(64);
            long start = protoOutputStream.start(2246267895810L);
            protoOutputStream.write(1120986464257L, transition.getSyncId());
            protoOutputStream.write(1112396529674L, transition.mLogger.mAbortTimeNs);
            protoOutputStream.end(start);
            this.mTraceBuffer.add(protoOutputStream);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unexpected exception thrown while logging transitions", e);
        }
    }

    private void dumpTransitionTargetsToProto(ProtoOutputStream protoOutputStream, Transition transition, ArrayList<Transition.ChangeInfo> arrayList) {
        Trace.beginSection("TransitionTracer#dumpTransitionTargetsToProto");
        if (this.mActiveTracingEnabled) {
            protoOutputStream.write(1120986464257L, transition.getSyncId());
        }
        protoOutputStream.write(1120986464263L, transition.mType);
        protoOutputStream.write(1120986464265L, transition.getFlags());
        for (int i = 0; i < arrayList.size(); i++) {
            long start = protoOutputStream.start(2246267895816L);
            Transition.ChangeInfo changeInfo = arrayList.get(i);
            int layerId = changeInfo.mContainer.mSurfaceControl.isValid() ? changeInfo.mContainer.mSurfaceControl.getLayerId() : -1;
            protoOutputStream.write(1120986464257L, changeInfo.mReadyMode);
            protoOutputStream.write(1120986464260L, changeInfo.mReadyFlags);
            protoOutputStream.write(1120986464258L, layerId);
            if (this.mActiveTracingEnabled) {
                protoOutputStream.write(1120986464259L, System.identityHashCode(changeInfo.mContainer));
            }
            protoOutputStream.end(start);
        }
        Trace.endSection();
    }

    public void startTrace(PrintWriter printWriter) {
        if (Build.IS_USER) {
            LogAndPrintln.e(printWriter, "Tracing is not supported on user builds.");
            return;
        }
        Trace.beginSection("TransitionTracer#startTrace");
        LogAndPrintln.i(printWriter, "Starting shell transition trace.");
        synchronized (this.mEnabledLock) {
            this.mActiveTracingEnabled = true;
            this.mTraceBuffer.resetBuffer();
            this.mTraceBuffer.setCapacity(ACTIVE_TRACING_BUFFER_CAPACITY);
        }
        Trace.endSection();
    }

    public void stopTrace(PrintWriter printWriter) {
        stopTrace(printWriter, new File(TRACE_FILE));
    }

    public void stopTrace(PrintWriter printWriter, File file) {
        if (Build.IS_USER) {
            LogAndPrintln.e(printWriter, "Tracing is not supported on user builds.");
            return;
        }
        Trace.beginSection("TransitionTracer#stopTrace");
        LogAndPrintln.i(printWriter, "Stopping shell transition trace.");
        synchronized (this.mEnabledLock) {
            this.mActiveTracingEnabled = false;
            writeTraceToFileLocked(printWriter, file);
            this.mTraceBuffer.resetBuffer();
            this.mTraceBuffer.setCapacity(ALWAYS_ON_TRACING_CAPACITY);
        }
        Trace.endSection();
    }

    public void saveForBugreport(PrintWriter printWriter) {
        if (Build.IS_USER) {
            LogAndPrintln.e(printWriter, "Tracing is not supported on user builds.");
            return;
        }
        Trace.beginSection("TransitionTracer#saveForBugreport");
        synchronized (this.mEnabledLock) {
            writeTraceToFileLocked(printWriter, new File(TRACE_FILE));
        }
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isActiveTracingEnabled() {
        return this.mActiveTracingEnabled;
    }

    private void writeTraceToFileLocked(PrintWriter printWriter, File file) {
        Trace.beginSection("TransitionTracer#writeTraceToFileLocked");
        try {
            ProtoOutputStream protoOutputStream = new ProtoOutputStream(64);
            protoOutputStream.write(1125281431553L, MAGIC_NUMBER_VALUE);
            protoOutputStream.write(1125281431555L, TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis()) - SystemClock.elapsedRealtimeNanos());
            LogAndPrintln.i(printWriter, "Writing file to " + file.getAbsolutePath() + " from process " + Process.myPid());
            this.mTraceBuffer.writeTraceToFile(file, protoOutputStream);
        } catch (IOException e) {
            LogAndPrintln.e(printWriter, "Unable to write buffer to file", e);
        }
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class LogAndPrintln {
        private LogAndPrintln() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void i(PrintWriter printWriter, String str) {
            Log.i(TransitionTracer.LOG_TAG, str);
            if (printWriter != null) {
                printWriter.println(str);
                printWriter.flush();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void e(PrintWriter printWriter, String str) {
            Log.e(TransitionTracer.LOG_TAG, str);
            if (printWriter != null) {
                printWriter.println("ERROR: " + str);
                printWriter.flush();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void e(PrintWriter printWriter, String str, Exception exc) {
            Log.e(TransitionTracer.LOG_TAG, str, exc);
            if (printWriter != null) {
                printWriter.println("ERROR: " + str + " ::\n " + exc);
                printWriter.flush();
            }
        }
    }
}
