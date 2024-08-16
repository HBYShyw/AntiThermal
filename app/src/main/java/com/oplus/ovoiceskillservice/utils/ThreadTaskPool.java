package com.oplus.ovoiceskillservice.utils;

import android.util.Log;
import com.oplus.ovoiceskillservice.utils.ThreadTask;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes.dex */
public class ThreadTaskPool {
    private static final String TAG = "OVSS.ThreadTaskPool";
    private static final long TIMEOUT_INFINITE = 0;
    private static Thread executorThread;
    private static Queue<ThreadTask> tasks = new LinkedList();
    private static RunState running = RunState.RUNNING;
    private static long stopTimeout = 0;
    private static long stopRecordTime = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.ovoiceskillservice.utils.ThreadTaskPool$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$oplus$ovoiceskillservice$utils$ThreadTask$TaskGuard;

        static {
            int[] iArr = new int[ThreadTask.TaskGuard.values().length];
            $SwitchMap$com$oplus$ovoiceskillservice$utils$ThreadTask$TaskGuard = iArr;
            try {
                iArr[ThreadTask.TaskGuard.ENTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$oplus$ovoiceskillservice$utils$ThreadTask$TaskGuard[ThreadTask.TaskGuard.DISCARD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$oplus$ovoiceskillservice$utils$ThreadTask$TaskGuard[ThreadTask.TaskGuard.RETRY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* loaded from: classes.dex */
    public enum RunState {
        RUNNING,
        STOP_TASKOVER,
        STOP_NOW
    }

    public static void add(ThreadTask threadTask) {
        Thread thread = executorThread;
        if (thread == null) {
            start();
        } else if (!thread.isAlive() || executorThread.isInterrupted()) {
            Log.d(TAG, "executorThread is not alive");
            try {
                executorThread.interrupt();
                start();
            } catch (Exception e10) {
                Log.e(TAG, "executorThread.start error", e10);
            }
        }
        tasks.offer(threadTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doRun() {
        InterruptedException e10;
        long j10;
        RunState runState;
        RunState runState2;
        Log.d(TAG, "taskThread run start");
        long j11 = 0;
        while (true) {
            if (!running.equals(RunState.STOP_NOW)) {
                if (j11 < 300) {
                    try {
                        ThreadTask poll = tasks.poll();
                        if (poll != null) {
                            try {
                                int i10 = AnonymousClass2.$SwitchMap$com$oplus$ovoiceskillservice$utils$ThreadTask$TaskGuard[poll.guard().ordinal()];
                                if (i10 == 1) {
                                    Log.d(TAG, "task enter");
                                    poll.run();
                                } else if (i10 == 2) {
                                    Log.d(TAG, "task discard");
                                } else if (i10 == 3) {
                                    tasks.offer(poll);
                                }
                                j11 = 0;
                            } catch (InterruptedException e11) {
                                e10 = e11;
                                j10 = 0;
                                Log.e(TAG, "sleep interrupted", e10);
                                j11 = j10;
                            }
                        }
                        runState = running;
                        runState2 = RunState.STOP_TASKOVER;
                    } catch (InterruptedException e12) {
                        long j12 = j11;
                        e10 = e12;
                        j10 = j12;
                    }
                    if (runState.equals(runState2) && tasks.size() == 0) {
                        Log.d(TAG, "stop task over");
                        terminate();
                        break;
                    } else if (running.equals(runState2) && System.currentTimeMillis() - stopRecordTime >= stopTimeout) {
                        Log.d(TAG, "stop timeout");
                        terminate();
                        break;
                    } else if (tasks.size() <= 1) {
                        Thread.sleep(100L);
                        j11++;
                    }
                } else {
                    return;
                }
            } else {
                break;
            }
        }
        Log.d(TAG, "taskThread run over!!!");
    }

    public static void shutdown() {
        Log.d(TAG, "shutdown");
        shutdown(0L);
    }

    public static void shutdownAndWait(long j10, Object obj) {
        shutdown(j10);
    }

    public static void start() {
        running = RunState.RUNNING;
        stopTimeout = 0L;
        stopRecordTime = 0L;
        Thread thread = new Thread(new Runnable() { // from class: com.oplus.ovoiceskillservice.utils.ThreadTaskPool.1
            @Override // java.lang.Runnable
            public void run() {
                ThreadTaskPool.doRun();
            }
        });
        executorThread = thread;
        thread.start();
    }

    public static void terminate() {
        Log.d(TAG, "terminate");
        running = RunState.STOP_NOW;
        tasks.clear();
    }

    public static void shutdown(long j10) {
        Log.d(TAG, "shutdown: " + j10);
        stopTimeout = j10;
        stopRecordTime = System.currentTimeMillis();
        running = RunState.STOP_TASKOVER;
        try {
            Thread thread = executorThread;
            if (thread != null) {
                thread.join(j10);
            }
        } catch (InterruptedException e10) {
            Log.e(TAG, "join interrupted", e10);
        }
    }
}
