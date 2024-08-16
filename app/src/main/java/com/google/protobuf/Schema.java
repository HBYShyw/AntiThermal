package com.google.protobuf;

import com.google.protobuf.ArrayDecoders;

/* JADX INFO: Access modifiers changed from: package-private */
@CheckReturnValue
/* loaded from: classes.dex */
public interface Schema<T> {
    boolean equals(T t7, T t10);

    int getSerializedSize(T t7);

    int hashCode(T t7);

    boolean isInitialized(T t7);

    void makeImmutable(T t7);

    void mergeFrom(T t7, Reader reader, ExtensionRegistryLite extensionRegistryLite);

    void mergeFrom(T t7, T t10);

    void mergeFrom(T t7, byte[] bArr, int i10, int i11, ArrayDecoders.Registers registers);

    T newInstance();

    void writeTo(T t7, Writer writer);
}
