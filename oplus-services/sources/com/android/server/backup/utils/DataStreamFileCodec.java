package com.android.server.backup.utils;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DataStreamFileCodec<T> {
    private final DataStreamCodec<T> mCodec;
    private final File mFile;

    public DataStreamFileCodec(File file, DataStreamCodec<T> dataStreamCodec) {
        this.mFile = file;
        this.mCodec = dataStreamCodec;
    }

    public T deserialize() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.mFile);
        try {
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            try {
                T deserialize = this.mCodec.deserialize(dataInputStream);
                dataInputStream.close();
                fileInputStream.close();
                return deserialize;
            } finally {
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void serialize(T t) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(this.mFile);
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
                try {
                    this.mCodec.serialize(t, dataOutputStream);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    bufferedOutputStream.close();
                    fileOutputStream.close();
                } finally {
                }
            } finally {
            }
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
