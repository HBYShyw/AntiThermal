package com.android.server.wm;

import android.os.Build;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import android.view.Choreographer;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.TraceBuffer;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowTracing {
    private static final int BUFFER_CAPACITY_ALL = 20971520;
    private static final int BUFFER_CAPACITY_CRITICAL = 5242880;
    private static final int BUFFER_CAPACITY_TRIM = 10485760;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final long MAGIC_NUMBER_VALUE = 4990904633914181975L;
    private static final String TAG = "WindowTracing";
    private static final String TRACE_FILENAME = "/data/misc/wmtrace/wm_trace.winscope";
    static final String WINSCOPE_EXT = ".winscope";
    private final TraceBuffer mBuffer;
    private final Choreographer mChoreographer;
    private boolean mEnabled;
    private final Object mEnabledLock;
    private volatile boolean mEnabledLockFree;
    private final Choreographer.FrameCallback mFrameCallback;
    private final WindowManagerGlobalLock mGlobalLock;
    private int mLogLevel;
    private boolean mLogOnFrame;
    private boolean mScheduled;
    private final WindowManagerService mService;
    private final File mTraceFile;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(long j) {
        log("onFrame");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static WindowTracing createDefaultAndStartLooper(WindowManagerService windowManagerService, Choreographer choreographer) {
        return new WindowTracing(new File(TRACE_FILENAME), windowManagerService, choreographer, BUFFER_CAPACITY_TRIM);
    }

    private WindowTracing(File file, WindowManagerService windowManagerService, Choreographer choreographer, int i) {
        this(file, windowManagerService, choreographer, windowManagerService.mGlobalLock, i);
    }

    WindowTracing(File file, WindowManagerService windowManagerService, Choreographer choreographer, WindowManagerGlobalLock windowManagerGlobalLock, int i) {
        this.mEnabledLock = new Object();
        this.mFrameCallback = new Choreographer.FrameCallback() { // from class: com.android.server.wm.WindowTracing$$ExternalSyntheticLambda0
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long j) {
                WindowTracing.this.lambda$new$0(j);
            }
        };
        this.mLogLevel = 1;
        this.mLogOnFrame = false;
        this.mChoreographer = choreographer;
        this.mService = windowManagerService;
        this.mGlobalLock = windowManagerGlobalLock;
        this.mTraceFile = file;
        this.mBuffer = new TraceBuffer(i);
        setLogLevel(1, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startTrace(PrintWriter printWriter) {
        if (Build.IS_USER && !DEBUG) {
            logAndPrintln(printWriter, "Error: Tracing is not supported on user builds.");
            return;
        }
        synchronized (this.mEnabledLock) {
            ProtoLogImpl.getSingleInstance().startProtoLog(printWriter);
            logAndPrintln(printWriter, "Start tracing to " + this.mTraceFile + ".");
            this.mBuffer.resetBuffer();
            this.mEnabledLockFree = true;
            this.mEnabled = true;
        }
        log("trace.enable");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopTrace(PrintWriter printWriter) {
        if (Build.IS_USER && !DEBUG) {
            logAndPrintln(printWriter, "Error: Tracing is not supported on user builds.");
            return;
        }
        synchronized (this.mEnabledLock) {
            logAndPrintln(printWriter, "Stop tracing to " + this.mTraceFile + ". Waiting for traces to flush.");
            this.mEnabledLockFree = false;
            this.mEnabled = false;
            writeTraceToFileLocked();
            logAndPrintln(printWriter, "Trace written to " + this.mTraceFile + ".");
        }
        ProtoLogImpl.getSingleInstance().stopProtoLog(printWriter, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveForBugreport(PrintWriter printWriter) {
        if (Build.IS_USER) {
            logAndPrintln(printWriter, "Error: Tracing is not supported on user builds.");
            return;
        }
        synchronized (this.mEnabledLock) {
            if (this.mEnabled) {
                this.mEnabledLockFree = false;
                this.mEnabled = false;
                logAndPrintln(printWriter, "Stop tracing to " + this.mTraceFile + ". Waiting for traces to flush.");
                writeTraceToFileLocked();
                logAndPrintln(printWriter, "Trace written to " + this.mTraceFile + ".");
                ProtoLogImpl.getSingleInstance().stopProtoLog(printWriter, true);
                logAndPrintln(printWriter, "Start tracing to " + this.mTraceFile + ".");
                this.mBuffer.resetBuffer();
                this.mEnabledLockFree = true;
                this.mEnabled = true;
                ProtoLogImpl.getSingleInstance().startProtoLog(printWriter);
            }
        }
    }

    private void setLogLevel(int i, PrintWriter printWriter) {
        logAndPrintln(printWriter, "Setting window tracing log level to " + i);
        this.mLogLevel = i;
        if (i == 0) {
            setBufferCapacity(BUFFER_CAPACITY_ALL, printWriter);
        } else if (i == 1) {
            setBufferCapacity(BUFFER_CAPACITY_TRIM, printWriter);
        } else {
            if (i != 2) {
                return;
            }
            setBufferCapacity(BUFFER_CAPACITY_CRITICAL, printWriter);
        }
    }

    private void setLogFrequency(boolean z, PrintWriter printWriter) {
        StringBuilder sb = new StringBuilder();
        sb.append("Setting window tracing log frequency to ");
        sb.append(z ? "frame" : "transaction");
        logAndPrintln(printWriter, sb.toString());
        this.mLogOnFrame = z;
    }

    private void setBufferCapacity(int i, PrintWriter printWriter) {
        logAndPrintln(printWriter, "Setting window tracing buffer capacity to " + i + "bytes");
        this.mBuffer.setCapacity(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEnabled() {
        return this.mEnabledLockFree;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int onShellCommand(ShellCommand shellCommand) {
        char c;
        PrintWriter outPrintWriter = shellCommand.getOutPrintWriter();
        String nextArgRequired = shellCommand.getNextArgRequired();
        nextArgRequired.hashCode();
        char c2 = 65535;
        switch (nextArgRequired.hashCode()) {
            case -892481550:
                if (nextArgRequired.equals("status")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -390772652:
                if (nextArgRequired.equals("save-for-bugreport")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3530753:
                if (nextArgRequired.equals("size")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3540994:
                if (nextArgRequired.equals("stop")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 97692013:
                if (nextArgRequired.equals("frame")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 102865796:
                if (nextArgRequired.equals("level")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 109757538:
                if (nextArgRequired.equals("start")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 2141246174:
                if (nextArgRequired.equals("transaction")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                logAndPrintln(outPrintWriter, getStatus());
                return 0;
            case 1:
                saveForBugreport(outPrintWriter);
                return 0;
            case 2:
                setBufferCapacity(Integer.parseInt(shellCommand.getNextArgRequired()) * 1024, outPrintWriter);
                this.mBuffer.resetBuffer();
                return 0;
            case 3:
                stopTrace(outPrintWriter);
                return 0;
            case 4:
                setLogFrequency(true, outPrintWriter);
                this.mBuffer.resetBuffer();
                return 0;
            case 5:
                String lowerCase = shellCommand.getNextArgRequired().toLowerCase();
                lowerCase.hashCode();
                switch (lowerCase.hashCode()) {
                    case 96673:
                        if (lowerCase.equals("all")) {
                            c2 = 0;
                            break;
                        }
                        break;
                    case 3568674:
                        if (lowerCase.equals("trim")) {
                            c2 = 1;
                            break;
                        }
                        break;
                    case 1952151455:
                        if (lowerCase.equals("critical")) {
                            c2 = 2;
                            break;
                        }
                        break;
                }
                switch (c2) {
                    case 0:
                        setLogLevel(0, outPrintWriter);
                        break;
                    case 1:
                        setLogLevel(1, outPrintWriter);
                        break;
                    case 2:
                        setLogLevel(2, outPrintWriter);
                        break;
                    default:
                        setLogLevel(1, outPrintWriter);
                        break;
                }
                this.mBuffer.resetBuffer();
                return 0;
            case 6:
                startTrace(outPrintWriter);
                return 0;
            case 7:
                setLogFrequency(false, outPrintWriter);
                this.mBuffer.resetBuffer();
                return 0;
            default:
                outPrintWriter.println("Unknown command: " + nextArgRequired);
                outPrintWriter.println("Window manager trace options:");
                outPrintWriter.println("  start: Start logging");
                outPrintWriter.println("  stop: Stop logging");
                outPrintWriter.println("  save-for-bugreport: Save logging data to file if it's running.");
                outPrintWriter.println("  frame: Log trace once per frame");
                outPrintWriter.println("  transaction: Log each transaction");
                outPrintWriter.println("  size: Set the maximum log size (in KB)");
                outPrintWriter.println("  status: Print trace status");
                outPrintWriter.println("  level [lvl]: Set the log level between");
                outPrintWriter.println("    lvl may be one of:");
                outPrintWriter.println("      critical: Only visible windows with reduced information");
                outPrintWriter.println("      trim: All windows with reduced");
                outPrintWriter.println("      all: All window and information");
                return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ");
        sb.append(isEnabled() ? "Enabled" : "Disabled");
        sb.append("\nLog level: ");
        sb.append(this.mLogLevel);
        sb.append("\n");
        sb.append(this.mBuffer.getStatus());
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logState(String str) {
        if (isEnabled()) {
            if (this.mLogOnFrame) {
                schedule();
            } else {
                log(str);
            }
        }
    }

    private void schedule() {
        if (this.mScheduled) {
            return;
        }
        this.mScheduled = true;
        this.mChoreographer.postFrameCallback(this.mFrameCallback);
    }

    private void log(String str) {
        Trace.traceBegin(32L, "traceStateLocked");
        try {
            try {
                ProtoOutputStream protoOutputStream = new ProtoOutputStream();
                long start = protoOutputStream.start(2246267895810L);
                protoOutputStream.write(1125281431553L, SystemClock.elapsedRealtimeNanos());
                protoOutputStream.write(1138166333442L, str);
                long start2 = protoOutputStream.start(1146756268035L);
                WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        Trace.traceBegin(32L, "dumpDebugLocked");
                        try {
                            this.mService.dumpDebugLocked(protoOutputStream, this.mLogLevel);
                            Trace.traceEnd(32L);
                        } finally {
                        }
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                protoOutputStream.end(start2);
                protoOutputStream.end(start);
                this.mBuffer.add(protoOutputStream);
                this.mScheduled = false;
            } catch (Exception e) {
                Log.wtf(TAG, "Exception while tracing state", e);
            }
        } finally {
        }
    }

    private void logAndPrintln(PrintWriter printWriter, String str) {
        Log.i(TAG, str);
        if (printWriter != null) {
            printWriter.println(str);
            printWriter.flush();
        }
    }

    private void writeTraceToFileLocked() {
        try {
            try {
                Trace.traceBegin(32L, "writeTraceToFileLocked");
                ProtoOutputStream protoOutputStream = new ProtoOutputStream();
                protoOutputStream.write(1125281431553L, MAGIC_NUMBER_VALUE);
                protoOutputStream.write(1125281431555L, TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis()) - SystemClock.elapsedRealtimeNanos());
                this.mBuffer.writeTraceToFile(this.mTraceFile, protoOutputStream);
            } catch (IOException e) {
                Log.e(TAG, "Unable to write buffer to file", e);
            }
        } finally {
            Trace.traceEnd(32L);
        }
    }
}
