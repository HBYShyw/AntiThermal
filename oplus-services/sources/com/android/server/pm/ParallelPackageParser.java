package com.android.server.pm;

import android.os.Trace;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ConcurrentUtils;
import com.android.server.pm.parsing.PackageParser2;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ParallelPackageParser {
    private static final int MAX_THREADS = 4;
    private static final int QUEUE_CAPACITY = 30;
    private final ExecutorService mExecutorService;
    private volatile String mInterruptedInThread;
    private final PackageParser2 mPackageParser;
    private final BlockingQueue<ParseResult> mQueue = new ArrayBlockingQueue(30);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ExecutorService makeExecutorService() {
        return ConcurrentUtils.newFixedThreadPool(4, "package-parsing-thread", -2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParallelPackageParser(PackageParser2 packageParser2, ExecutorService executorService) {
        this.mPackageParser = packageParser2;
        this.mExecutorService = executorService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ParseResult {
        ParsedPackage parsedPackage;
        File scanFile;
        Throwable throwable;

        ParseResult() {
        }

        public String toString() {
            return "ParseResult{parsedPackage=" + this.parsedPackage + ", scanFile=" + this.scanFile + ", throwable=" + this.throwable + '}';
        }
    }

    public ParseResult take() {
        try {
            if (this.mInterruptedInThread != null) {
                throw new InterruptedException("Interrupted in " + this.mInterruptedInThread);
            }
            return this.mQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    public void submit(final File file, final int i) {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.server.pm.ParallelPackageParser$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ParallelPackageParser.this.lambda$submit$0(file, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$submit$0(File file, int i) {
        ParseResult parseResult = new ParseResult();
        Trace.traceBegin(262144L, "parallel parsePackage [" + file + "]");
        try {
            try {
                parseResult.scanFile = file;
                parseResult.parsedPackage = parsePackage(file, i);
            } finally {
                try {
                    this.mQueue.put(parseResult);
                } finally {
                }
            }
            this.mQueue.put(parseResult);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            this.mInterruptedInThread = Thread.currentThread().getName();
        }
    }

    @VisibleForTesting
    protected ParsedPackage parsePackage(File file, int i) throws PackageManagerException {
        return this.mPackageParser.parsePackage(file, i, true);
    }
}
