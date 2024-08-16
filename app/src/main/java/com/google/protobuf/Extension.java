package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.MessageLite;

/* loaded from: classes.dex */
public abstract class Extension<ContainingType extends MessageLite, Type> extends ExtensionLite<ContainingType, Type> {

    /* loaded from: classes.dex */
    protected enum ExtensionType {
        IMMUTABLE,
        MUTABLE,
        PROTO1
    }

    /* loaded from: classes.dex */
    public enum MessageType {
        PROTO1,
        PROTO2
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object fromReflectionType(Object obj);

    public abstract Descriptors.FieldDescriptor getDescriptor();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract ExtensionType getExtensionType();

    @Override // com.google.protobuf.ExtensionLite
    public abstract Message getMessageDefaultInstance();

    public MessageType getMessageType() {
        return MessageType.PROTO2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionLite
    public final boolean isLite() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object singularFromReflectionType(Object obj);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object singularToReflectionType(Object obj);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object toReflectionType(Object obj);
}
