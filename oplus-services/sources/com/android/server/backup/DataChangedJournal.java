package com.android.server.backup;

import android.util.Slog;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DataChangedJournal {
    private static final int BUFFER_SIZE_BYTES = 8192;
    private static final String FILE_NAME_PREFIX = "journal";
    private static final String TAG = "DataChangedJournal";
    private final File mFile;

    DataChangedJournal(File file) {
        Objects.requireNonNull(file);
        this.mFile = file;
    }

    public void addPackage(String str) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.mFile, "rws");
        try {
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.writeUTF(str);
            randomAccessFile.close();
        } catch (Throwable th) {
            try {
                randomAccessFile.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void forEach(Consumer<String> consumer) throws IOException {
        try {
            try {
                try {
                    while (true) {
                        try {
                            consumer.accept(new DataInputStream(new BufferedInputStream(new FileInputStream(this.mFile), 8192)).readUTF());
                        } finally {
                        }
                    }
                } finally {
                }
            } finally {
            }
        } catch (EOFException unused) {
        }
    }

    public List<String> getPackages() throws IOException {
        final ArrayList arrayList = new ArrayList();
        forEach(new Consumer() { // from class: com.android.server.backup.DataChangedJournal$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                arrayList.add((String) obj);
            }
        });
        return arrayList;
    }

    public boolean delete() {
        return this.mFile.delete();
    }

    public int hashCode() {
        return this.mFile.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof DataChangedJournal) {
            return this.mFile.equals(((DataChangedJournal) obj).mFile);
        }
        return false;
    }

    public String toString() {
        return this.mFile.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DataChangedJournal newJournal(File file) throws IOException {
        Objects.requireNonNull(file);
        return new DataChangedJournal(File.createTempFile(FILE_NAME_PREFIX, null, file));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ArrayList<DataChangedJournal> listJournals(File file) {
        ArrayList<DataChangedJournal> arrayList = new ArrayList<>();
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            Slog.w(TAG, "Failed to read journal files");
            return arrayList;
        }
        for (File file2 : listFiles) {
            arrayList.add(new DataChangedJournal(file2));
        }
        return arrayList;
    }
}
