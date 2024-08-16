package com.google.protobuf;

import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public interface MessageLite extends MessageLiteOrBuilder {

    /* loaded from: classes.dex */
    public interface Builder extends MessageLiteOrBuilder, Cloneable {
        MessageLite build();

        MessageLite buildPartial();

        Builder clear();

        /* renamed from: clone */
        Builder m5clone();

        boolean mergeDelimitedFrom(InputStream inputStream);

        boolean mergeDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite);

        Builder mergeFrom(ByteString byteString);

        Builder mergeFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite);

        Builder mergeFrom(CodedInputStream codedInputStream);

        Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite);

        Builder mergeFrom(MessageLite messageLite);

        Builder mergeFrom(InputStream inputStream);

        Builder mergeFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite);

        Builder mergeFrom(byte[] bArr);

        Builder mergeFrom(byte[] bArr, int i10, int i11);

        Builder mergeFrom(byte[] bArr, int i10, int i11, ExtensionRegistryLite extensionRegistryLite);

        Builder mergeFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite);
    }

    Parser<? extends MessageLite> getParserForType();

    int getSerializedSize();

    Builder newBuilderForType();

    Builder toBuilder();

    byte[] toByteArray();

    ByteString toByteString();

    void writeDelimitedTo(OutputStream outputStream);

    void writeTo(CodedOutputStream codedOutputStream);

    void writeTo(OutputStream outputStream);
}
