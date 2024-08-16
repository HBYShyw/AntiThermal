package com.android.server.pm;

import android.util.Slog;
import com.android.internal.util.ArrayUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ShortcutDumpFiles {
    private static final boolean DEBUG = false;
    private static final String TAG = "ShortcutService";
    private final ShortcutService mService;

    public ShortcutDumpFiles(ShortcutService shortcutService) {
        this.mService = shortcutService;
    }

    public boolean save(String str, Consumer<PrintWriter> consumer) {
        try {
            File dumpPath = this.mService.getDumpPath();
            dumpPath.mkdirs();
            if (!dumpPath.exists()) {
                Slog.e(TAG, "Failed to create directory: " + dumpPath);
                return false;
            }
            PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(dumpPath, str))));
            try {
                consumer.accept(printWriter);
                printWriter.close();
                return true;
            } catch (Throwable th) {
                try {
                    printWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException | RuntimeException e) {
            Slog.w(TAG, "Failed to create dump file: " + str, e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$save$0(byte[] bArr, PrintWriter printWriter) {
        printWriter.println(StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bArr)).toString());
    }

    public boolean save(String str, final byte[] bArr) {
        return save(str, new Consumer() { // from class: com.android.server.pm.ShortcutDumpFiles$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutDumpFiles.lambda$save$0(bArr, (PrintWriter) obj);
            }
        });
    }

    public void dumpAll(PrintWriter printWriter) {
        try {
            File dumpPath = this.mService.getDumpPath();
            File[] listFiles = dumpPath.listFiles(new FileFilter() { // from class: com.android.server.pm.ShortcutDumpFiles$$ExternalSyntheticLambda1
                @Override // java.io.FileFilter
                public final boolean accept(File file) {
                    boolean isFile;
                    isFile = file.isFile();
                    return isFile;
                }
            });
            if (dumpPath.exists() && !ArrayUtils.isEmpty(listFiles)) {
                Arrays.sort(listFiles, Comparator.comparing(new Function() { // from class: com.android.server.pm.ShortcutDumpFiles$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        String name;
                        name = ((File) obj).getName();
                        return name;
                    }
                }));
                for (File file : listFiles) {
                    printWriter.print("*** Dumping: ");
                    printWriter.println(file.getName());
                    printWriter.print("mtime: ");
                    printWriter.println(ShortcutService.formatTime(file.lastModified()));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    while (true) {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            } else {
                                printWriter.println(readLine);
                            }
                        } catch (Throwable th) {
                            try {
                                bufferedReader.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    }
                    bufferedReader.close();
                }
                return;
            }
            printWriter.print("  No dump files found.");
        } catch (IOException | RuntimeException e) {
            Slog.w(TAG, "Failed to print dump files", e);
        }
    }
}
